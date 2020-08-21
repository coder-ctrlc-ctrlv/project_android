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

public class DifAutomaticsActivity3 extends AppCompatActivity {

    DBHelper dbHelper;
    String nameFloor;
    int idFloor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dif_automatics3);

        dbHelper = new DBHelper(this);
        final SQLiteDatabase database = dbHelper.getWritableDatabase();

        TextView floor = findViewById(R.id.textView6);
        TextView room = findViewById(R.id.textView7);
        final ListView listLines = findViewById(R.id.lines);
        Button addLine = findViewById(R.id.button9);
        nameFloor = getIntent().getStringExtra("nameFloor");
        idFloor = getIntent().getIntExtra("idFloor", 0);
        final String nameRoom = getIntent().getStringExtra("nameRoom");
        final int idRoom = getIntent().getIntExtra("idRoom", 0);

        //НАСТРАИВАЕМ ACTIONBAR
        getSupportActionBar().setSubtitle("Щиты");
        getSupportActionBar().setTitle("Диф.автоматы");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ВЫВОД ЭТАЖА И КОМНАТЫ
        floor.setText("Этаж: " + nameFloor);
        room.setText("Комната: " + nameRoom);

        //ЗАПРОС В БД И ЗАПОЛНЕНИЕ СПИСКА ЩИТОВ
        addSpisokLines(database, listLines, idRoom);

        //ДОБАВИТЬ ЩИТ
        addLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(DifAutomaticsActivity3.this);
                final View myView = getLayoutInflater().inflate(R.layout.dialog_for_marks,null);
                alert.setCancelable(false);
                alert.setTitle("Введите название щита:");
                final AutoCompleteTextView input = myView.findViewById(R.id.autoCompleteTextView3);
                ImageView arrow = myView.findViewById(R.id.imageView4);
                //ОТОБРАЖЕНИЕ ВЫПЛЫВАЮЩЕГО СПИСКА
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(DifAutomaticsActivity3.this, android.R.layout.simple_dropdown_item_1line, getLines(database));
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
                        final String nameLine = input.getText().toString();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(DBHelper.DIF_AU_LINE_NAME, nameLine);
                        contentValues.put(DBHelper.DIF_AU_ID_LROOM, idRoom);
                        database.insert(DBHelper.TABLE_DIF_AU_LINES, null, contentValues);
                        //ЗАПРОС В БД И ЗАПОЛНЕНИЕ СПИСКА ЩИТОВ
                        addSpisokLines(database, listLines, idRoom);
                        //Если новое название щита, то вносим его в базу
                        if (!Arrays.asList(getLines(database)).contains(nameLine)){
                            ContentValues newName = new ContentValues();
                            newName.put(DBHelper.LIB_LINE_NAME, nameLine);
                            database.insert(DBHelper.TABLE_LIBRARY_LINES, null, newName);
                        }
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Щит <" + nameLine + "> добавлен", Toast.LENGTH_SHORT);
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

        //ПЕРЕХОД К ГРУППАМ И РЕДАКТОР ЩИТА
        listLines.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, final long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(DifAutomaticsActivity3.this);
                alert.setTitle(((TextView) view).getText());
                String arrayMenu[] = {"\nПерейти к аппаратам\n", "\nИзменить название\n", "\nУдалить щит\n"};
                alert.setItems(arrayMenu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //ЗАПРОС В БД ДЛЯ ПОЛУЧЕНИЯ НУЖНОЙ ЛИНИИ
                        Cursor cursor = database.query(DBHelper.TABLE_DIF_AU_LINES, new String[] {DBHelper.DIF_AU_LINE_ID}, "dif_au_lroom_id = ?", new String[] {String.valueOf(idRoom)}, null, null, "_id DESC");
                        cursor.moveToPosition(position);
                        int lineIndex = cursor.getColumnIndex(DBHelper.DIF_AU_LINE_ID);
                        final int lineId = cursor.getInt(lineIndex);
                        cursor.close();

                        //ПЕРЕЙТИ К АППАРАТАМ
                        if (which == 0) {
                            Intent intent = new Intent("android.intent.action.DifAutomatics4");
                            intent.putExtra("nameFloor", nameFloor);
                            intent.putExtra("idFloor", idFloor);
                            intent.putExtra("nameRoom", nameRoom);
                            intent.putExtra("idRoom", idRoom);
                            intent.putExtra("nameLine", ((TextView) view).getText().toString());
                            intent.putExtra("idLine", lineId);
                            startActivity(intent);
                        }

                        //ИЗМЕНИТЬ НАЗВАНИЕ
                        if (which == 1) {
                            AlertDialog.Builder alert1 = new AlertDialog.Builder(DifAutomaticsActivity3.this);
                            final View myView = getLayoutInflater().inflate(R.layout.dialog_for_marks,null);
                            alert1.setCancelable(false);
                            alert1.setTitle("Введите новое название щита:");
                            final AutoCompleteTextView input = myView.findViewById(R.id.autoCompleteTextView3);
                            ImageView arrow = myView.findViewById(R.id.imageView4);
                            input.setText(((TextView) view).getText().toString());
                            //ОТОБРАЖЕНИЕ ВЫПЛЫВАЮЩЕГО СПИСКА
                            ArrayAdapter<String>adapter1 = new ArrayAdapter<String>(DifAutomaticsActivity3.this, android.R.layout.simple_dropdown_item_1line, getLines(database));
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
                                    String namel = input.getText().toString();
                                    ContentValues uppname = new ContentValues();
                                    uppname.put(DBHelper.DIF_AU_LINE_NAME, namel);
                                    database.update(DBHelper.TABLE_DIF_AU_LINES,
                                            uppname,
                                            "_id = ?",
                                            new String[] {String.valueOf(lineId)});
                                    //ЗАПРОС В БД И ЗАПОЛНЕНИЕ СПИСКА ЩИТОВ
                                    addSpisokLines(database, listLines, idRoom);
                                    //Если новое название щита, то вносим его в базу
                                    if (!Arrays.asList(getLines(database)).contains(namel)){
                                        ContentValues newName = new ContentValues();
                                        newName.put(DBHelper.LIB_LINE_NAME, namel);
                                        database.insert(DBHelper.TABLE_LIBRARY_LINES, null, newName);
                                    }
                                    Toast toast1 = Toast.makeText(getApplicationContext(),
                                            "Название изменено: " + namel, Toast.LENGTH_SHORT);
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

                        //УДАЛИТЬ ЩИТ
                        if (which == 2) {

                            //ПОДТВЕРЖДЕНИЕ
                            AlertDialog.Builder builder4 = new AlertDialog.Builder(DifAutomaticsActivity3.this);
                            builder4.setCancelable(false);
                            builder4.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    database.delete(DBHelper.TABLE_DIF_AUTOMATICS, "dif_auline_id = ?", new String[] {String.valueOf(lineId)});
                                    database.delete(DBHelper.TABLE_DIF_AU_LINES, "_id = ?", new String[] {String.valueOf(lineId)});
                                    //ЗАПРОС В БД И ЗАПОЛНЕНИЕ СПИСКА ЩИТОВ
                                    addSpisokLines(database, listLines, idRoom);
                                    Toast toast2 = Toast.makeText(getApplicationContext(),
                                            "Щит <" + ((TextView) view).getText() + "> удален", Toast.LENGTH_SHORT);
                                    toast2.show();
                                }
                            });
                            builder4.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                }
                            });
                            builder4.setMessage("Вы точно хотите удалить щит <" + ((TextView) view).getText() + ">?");
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
                Intent intent = new Intent("android.intent.action.DifAutomatics2");
                intent.putExtra("nameFloor", nameFloor);
                intent.putExtra("idFloor", idFloor);
                startActivity(intent);
                return true;
            case R.id.action_main:
                Intent intent1 = new Intent(DifAutomaticsActivity3.this, MenuItemsActivity.class);
                startActivity(intent1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addSpisokLines(SQLiteDatabase database, ListView lines, int idRoom) {
        final ArrayList<String> spisokLines = new ArrayList <String>();
        Cursor cursor = database.query(DBHelper.TABLE_DIF_AU_LINES, new String[] {DBHelper.DIF_AU_LINE_NAME}, "dif_au_lroom_id = ?", new String[] {String.valueOf(idRoom)}, null, null, "_id DESC");
        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(DBHelper.DIF_AU_LINE_NAME);
            do {
                spisokLines.add(cursor.getString(nameIndex));
            } while (cursor.moveToNext());
        }
        cursor.close();
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, spisokLines);
        lines.setAdapter(adapter);
    }

    //ПОЛУЧЕНИЕ ЩИТОВ
    public String[] getLines(SQLiteDatabase database) {
        final ArrayList<String> linesDB = new ArrayList <String>();
        Cursor cursor = database.query(DBHelper.TABLE_LIBRARY_LINES, new String[] {DBHelper.LIB_LINE_NAME}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(DBHelper.LIB_LINE_NAME);
            do {
                linesDB.add(cursor.getString(nameIndex));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return linesDB.toArray(new String[linesDB.size()]);
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
