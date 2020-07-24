package com.example.protokol.create_protokol;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class AutomaticsActivity2 extends AppCompatActivity {

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automatics2);

        dbHelper = new DBHelper(this);
        final SQLiteDatabase database = dbHelper.getWritableDatabase();

        TextView floor = findViewById(R.id.textView6);
        final ListView rooms = findViewById(R.id.rooms);
        Button addRoom = findViewById(R.id.button9);
        final String nameFloor = getIntent().getStringExtra("nameFloor");
        final int idFloor = getIntent().getIntExtra("idFloor", 0);

        //НАСТРАИВАЕМ ACTIONBAR
        getSupportActionBar().setSubtitle("Комнаты");
        getSupportActionBar().setTitle("Автомат. выключатели");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ВЫВОД ЭТАЖА
        floor.setText("Этаж: " + nameFloor);

        //ЗАПРОС В БД И ЗАПОЛНЕНИЕ СПИСКА КОМНАТ
        addSpisokRooms(database, rooms, idFloor);

        //ДОБАВИТЬ КОМНАТУ
        addRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(AutomaticsActivity2.this);
                final View myView = getLayoutInflater().inflate(R.layout.dialog_for_marks,null);
                alert.setCancelable(false);
                alert.setTitle("Введите название комнаты:");
                final AutoCompleteTextView input = myView.findViewById(R.id.autoCompleteTextView3);
                ImageView arrow = myView.findViewById(R.id.imageView4);
                //ОТОБРАЖЕНИЕ ВЫПЛЫВАЮЩЕГО СПИСКА
                ArrayAdapter<String>adapter1 = new ArrayAdapter<String>(AutomaticsActivity2.this, android.R.layout.simple_dropdown_item_1line, getRooms(database));
                input.setAdapter(adapter1);
                openKeyboard();
                arrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        closeKeyboard(myView);
                        input.showDropDown();
                    }
                });
                alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        closeKeyboard(myView);
                        final String nameRoom = input.getText().toString();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(DBHelper.AU_ROOM_NAME, nameRoom);
                        contentValues.put(DBHelper.AU_ID_RFLOOR, idFloor);
                        database.insert(DBHelper.TABLE_AU_ROOMS, null, contentValues);
                        //ЗАПРОС В БД И ЗАПОЛНЕНИЕ СПИСКА КОМНАТ
                        addSpisokRooms(database, rooms, idFloor);
                        //Если новое название комнаты, то вносим его в базу
                        if (!Arrays.asList(getRooms(database)).contains(nameRoom)){
                            ContentValues newName = new ContentValues();
                            newName.put(DBHelper.LIB_ROOM_NAME, nameRoom);
                            database.insert(DBHelper.TABLE_LIBRARY_ROOMS, null, newName);
                        }
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Комната <" + nameRoom + "> добавлена", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
                alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        closeKeyboard(myView);
                    }
                });
                alert.setView(myView);
                alert.show();
            }
        });

        //ПЕРЕХОД К ЩИТАМ И РЕДАКТОР КОМНАТЫ
        rooms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, final long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(AutomaticsActivity2.this);
                alert.setTitle(((TextView) view).getText());
                String arrayMenu[] = {"\nПерейти к щитам\n", "\nИзменить название\n", "\nУдалить комнату\n"};
                alert.setItems(arrayMenu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //ЗАПРОС В БД ДЛЯ ПОЛУЧЕНИЯ ID НУЖНОЙ КОМНАТЫ
                        Cursor cursor4 = database.query(DBHelper.TABLE_AU_ROOMS, new String[] {DBHelper.AU_ROOM_ID}, "au_rfl_id = ?", new String[] {String.valueOf(idFloor)}, null, null, "_id DESC");
                        cursor4.moveToPosition(position);
                        int idRoomIndex = cursor4.getColumnIndex(DBHelper.AU_ROOM_ID);
                        final int idRoom = cursor4.getInt(idRoomIndex);
                        cursor4.close();

                        //ПЕРЕЙТИ К ЩИТАМ
                        if (which == 0) {
                            Intent intent = new Intent("android.intent.action.Automatics3");
                            intent.putExtra("nameFloor", nameFloor);
                            intent.putExtra("idFloor", idFloor);
                            intent.putExtra("nameRoom", ((TextView) view).getText().toString());
                            intent.putExtra("idRoom", idRoom);
                            startActivity(intent);
                        }

                        //ИЗМЕНИТЬ НАЗВАНИЕ
                        if (which == 1) {
                            AlertDialog.Builder alert1 = new AlertDialog.Builder(AutomaticsActivity2.this);
                            final View myView = getLayoutInflater().inflate(R.layout.dialog_for_marks,null);
                            alert1.setCancelable(false);
                            alert1.setTitle("Введите новое название комнаты:");
                            final AutoCompleteTextView input = myView.findViewById(R.id.autoCompleteTextView3);
                            ImageView arrow = myView.findViewById(R.id.imageView4);
                            input.setText(((TextView) view).getText().toString());
                            //ОТОБРАЖЕНИЕ ВЫПЛЫВАЮЩЕГО СПИСКА
                            ArrayAdapter<String>adapter1 = new ArrayAdapter<String>(AutomaticsActivity2.this, android.R.layout.simple_dropdown_item_1line, getRooms(database));
                            input.setAdapter(adapter1);
                            openKeyboard();
                            arrow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    closeKeyboard(myView);
                                    input.showDropDown();
                                }
                            });
                            alert1.setPositiveButton("Изменить", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    closeKeyboard(myView);
                                    String namer = input.getText().toString();
                                    ContentValues uppname = new ContentValues();
                                    uppname.put(DBHelper.AU_ROOM_NAME, namer);
                                    database.update(DBHelper.TABLE_AU_ROOMS,
                                            uppname,
                                            "_id = ?",
                                            new String[] {String.valueOf(idRoom)});
                                    //ЗАПРОС В БД И ЗАПОЛНЕНИЕ СПИСКА КОМНАТ
                                    addSpisokRooms(database, rooms, idFloor);
                                    //Если новое название комнаты, то вносим его в базу
                                    if (!Arrays.asList(getRooms(database)).contains(namer)){
                                        ContentValues newName = new ContentValues();
                                        newName.put(DBHelper.LIB_ROOM_NAME, namer);
                                        database.insert(DBHelper.TABLE_LIBRARY_ROOMS, null, newName);
                                    }
                                    Toast toast1 = Toast.makeText(getApplicationContext(),
                                            "Название изменено: " + namer, Toast.LENGTH_SHORT);
                                    toast1.show();
                                }
                            });
                            alert1.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    closeKeyboard(myView);
                                }
                            });
                            alert1.setView(myView);
                            alert1.show();
                        }

                        //УДАЛИТЬ КОМНАТУ
                        if (which == 2) {
                            //ПОДТВЕРЖДЕНИЕ
                            AlertDialog.Builder builder4 = new AlertDialog.Builder(AutomaticsActivity2.this);
                            builder4.setCancelable(false);
                            builder4.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Cursor cursor = database.query(DBHelper.TABLE_AU_LINES, new String[] {DBHelper.AU_LINE_ID, DBHelper.AU_ID_LROOM}, "au_lroom_id = ?", new String[] {String.valueOf(idRoom)}, null, null, null);
                                    if (cursor.moveToFirst()) {
                                        int line_IdIndex = cursor.getColumnIndex(DBHelper.AU_LINE_ID);
                                        do {
                                            database.delete(DBHelper.TABLE_AUTOMATICS, "auline_id = ?", new String[] {cursor.getString(line_IdIndex)});
                                        } while (cursor.moveToNext());
                                    }
                                    cursor.close();
                                    database.delete(DBHelper.TABLE_AU_LINES, "au_lroom_id = ?", new String[] {String.valueOf(idRoom)});
                                    database.delete(DBHelper.TABLE_AU_ROOMS, "_id = ?", new String[] {String.valueOf(idRoom)});
                                    //ЗАПРОС В БД И ЗАПОЛНЕНИЕ СПИСКА КОМНАТ
                                    addSpisokRooms(database, rooms, idFloor);
                                    Toast toast2 = Toast.makeText(getApplicationContext(),
                                            "Комната <" + ((TextView) view).getText() + "> удалена", Toast.LENGTH_SHORT);
                                    toast2.show();
                                }
                            });
                            builder4.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                }
                            });
                            builder4.setMessage("Вы точно хотите удалить комнату <" + ((TextView) view).getText() + ">?");
                            AlertDialog dialog4 = builder4.create();
                            dialog4.show();
                        }
                    }
                });
                alert.show();
            }
        });
    }

    //НА ГЛАВНУЮ
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    //НАЗАД
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent("android.intent.action.Automatics1");
                startActivity(intent);
                return true;
            case R.id.action_main:
                Intent intent1 = new Intent(AutomaticsActivity2.this, MainActivity.class);
                startActivity(intent1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addSpisokRooms(SQLiteDatabase database, ListView rooms, int idFloor) {
        final ArrayList<String> spisokRooms = new ArrayList <String>();
        Cursor cursor = database.query(DBHelper.TABLE_AU_ROOMS, new String[] {DBHelper.AU_ROOM_NAME}, "au_rfl_id = ?", new String[] {String.valueOf(idFloor)}, null, null, "_id DESC");
        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(DBHelper.AU_ROOM_NAME);
            do {
                spisokRooms.add(cursor.getString(nameIndex));
            } while (cursor.moveToNext());
        }
        cursor.close();
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, spisokRooms);
        rooms.setAdapter(adapter);
    }

    //ПОЛУЧЕНИЕ КОМНАТ
    public String[] getRooms(SQLiteDatabase database) {
        final ArrayList<String> roomsDB = new ArrayList <String>();
        Cursor cursor = database.query(DBHelper.TABLE_LIBRARY_ROOMS, new String[] {DBHelper.LIB_ROOM_NAME}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(DBHelper.LIB_ROOM_NAME);
            do {
                roomsDB.add(cursor.getString(nameIndex));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return roomsDB.toArray(new String[roomsDB.size()]);
    }

    void openKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }

    void closeKeyboard(View myView) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(myView.getWindowToken(),0);
    }
}
