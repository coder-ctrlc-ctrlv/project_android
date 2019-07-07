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
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class LibraryActivity extends AppCompatActivity {

    DBHelper dbHelper;
    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;
    ListView library_items;
    EditText filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        dbHelper = new DBHelper(this);
        final SQLiteDatabase database = dbHelper.getWritableDatabase();

        filter = findViewById(R.id.userFilter);
        library_items = findViewById(R.id.userList);
        final String lib = getIntent().getStringExtra("lib");

        //НАСТРАИВАЕМ ACTIONBAR
        getSupportActionBar().setTitle("Библиотека");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ОПРЕДЕЛЯЕМ ТИП БИБЛИОТЕКИ
        if (lib.equals("namesEl"))
            getSupportActionBar().setSubtitle("Названия элементов");
        if (lib.equals("marks"))
            getSupportActionBar().setSubtitle("Марки");
        if (lib.equals("rooms"))
            getSupportActionBar().setSubtitle("Комнаты");
        if (lib.equals("lines"))
            getSupportActionBar().setSubtitle("Щиты");
        if (lib.equals("floors"))
            getSupportActionBar().setSubtitle("Этажи");

        //ЗАПОЛЯНЕМ LISTVIEW ИЗНАЧАЛЬНО
        getLibraryItems(database, lib);

        filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals(""))
                    getLibraryItems(database, lib);
                else
                    if (s.toString().matches("[а-яА-Яa-zA-Z]+"))
                        searchItem(database, s.toString(), lib);
                    else {
                        AlertDialog.Builder alert = new AlertDialog.Builder(LibraryActivity.this);
                        alert.setCancelable(false);
                        alert.setMessage("Вы можете вводить только буквы русского или английского алфавитов. " +
                                "При других символах поиск работать не будет.");
                        alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        });
                        alert.show();
                    }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //ИЗМЕНИТЬ ИЛИ УДАЛИТЬ
        library_items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, final long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(LibraryActivity.this);
                alert.setTitle(((TextView) view).getText());
                String arrayMenu[] = {"\nРедактировать\n", "\nУдалить элемент\n"};
                alert.setItems(arrayMenu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //ИЗМЕНИТЬ
                        if (which == 0) {
                            AlertDialog.Builder alert1 = new AlertDialog.Builder(LibraryActivity.this);
                            final View myView = getLayoutInflater().inflate(R.layout.dialog_for_names, null);
                            alert1.setCancelable(false);
                            alert1.setTitle("Введите новое название:");
                            final EditText input = myView.findViewById(R.id.editText);
                            input.setText(((TextView) view).getText().toString());
                            //ОТКРЫВАЕМ КЛАВИАТУРУ
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                            alert1.setPositiveButton("Изменить", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    String name = input.getText().toString();
                                    ContentValues uppname = new ContentValues();
                                    if (lib.equals("namesEl")) {
                                        uppname.put(DBHelper.NAME_EL, name);
                                        database.update(DBHelper.TABLE_NAMES_EL,
                                                uppname,
                                                "name_el = ?",
                                                new String[]{String.valueOf(((TextView) view).getText())});
                                    }
                                    if (lib.equals("marks")) {
                                        uppname.put(DBHelper.MARK, name);
                                        database.update(DBHelper.TABLE_MARKS,
                                                uppname,
                                                "mark = ?",
                                                new String[]{String.valueOf(((TextView) view).getText())});
                                    }
                                    if (lib.equals("rooms")) {
                                        uppname.put(DBHelper.LIB_ROOM_NAME, name);
                                        database.update(DBHelper.TABLE_LIBRARY_ROOMS,
                                                uppname,
                                                "room_name = ?",
                                                new String[]{String.valueOf(((TextView) view).getText())});
                                    }
                                    if (lib.equals("lines")) {
                                        uppname.put(DBHelper.LIB_LINE_NAME, name);
                                        database.update(DBHelper.TABLE_LIBRARY_LINES,
                                                uppname,
                                                "line_name = ?",
                                                new String[]{String.valueOf(((TextView) view).getText())});
                                    }
                                    if (lib.equals("floors")) {
                                        uppname.put(DBHelper.LIB_FLOOR_NAME, name);
                                        database.update(DBHelper.TABLE_LIBRARY_FLOORS,
                                                uppname,
                                                "floor_name = ?",
                                                new String[]{String.valueOf(((TextView) view).getText())});
                                    }
                                    //СКРЫВАЕМ КЛАВИАТУРУ
                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(myView.getWindowToken(), 0);
                                    //ЗАПРОС В БД И ЗАПОЛНЕНИЕ СПИСКА КОМНАТ
                                    getLibraryItems(database, lib);
                                    filter.setText("");
                                    Toast toast1 = Toast.makeText(getApplicationContext(),
                                            "Название изменено: " + name, Toast.LENGTH_SHORT);
                                    toast1.show();
                                }
                            });
                            alert1.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    //СКРЫВАЕМ КЛАВИАТУРУ
                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(myView.getWindowToken(), 0);
                                }
                            });
                            alert1.setView(myView);
                            alert1.show();
                        }

                        //УДАЛИТЬ ГРУППУ
                        if (which == 1) {
                            //ПОДТВЕРЖДЕНИЕ
                            AlertDialog.Builder builder4 = new AlertDialog.Builder(LibraryActivity.this);
                            builder4.setCancelable(false);
                            builder4.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    if (lib.equals("namesEl"))
                                        database.delete(DBHelper.TABLE_NAMES_EL, "name_el = ?", new String[] {String.valueOf(((TextView) view).getText())});
                                    if (lib.equals("marks"))
                                        database.delete(DBHelper.TABLE_MARKS, "mark = ?", new String[] {String.valueOf(((TextView) view).getText())});
                                    if (lib.equals("rooms"))
                                        database.delete(DBHelper.TABLE_LIBRARY_ROOMS, "room_name = ?", new String[] {String.valueOf(((TextView) view).getText())});
                                    if (lib.equals("lines"))
                                        database.delete(DBHelper.TABLE_LIBRARY_LINES, "line_name = ?", new String[] {String.valueOf(((TextView) view).getText())});
                                    if (lib.equals("floors"))
                                        database.delete(DBHelper.TABLE_LIBRARY_FLOORS, "floor_name = ?", new String[] {String.valueOf(((TextView) view).getText())});
                                    getLibraryItems(database, lib);
                                    filter.setText("");
                                    Toast toast2 = Toast.makeText(getApplicationContext(),
                                            "Элемент был удален из списка", Toast.LENGTH_SHORT);
                                    toast2.show();
                                }
                            });
                            builder4.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                }
                            });
                            builder4.setMessage("Вы точно хотите удалить <" + ((TextView) view).getText() + ">?");
                            AlertDialog dialog4 = builder4.create();
                            dialog4.show();
                        }
                    }
                });
                alert.show();
            }
        });
    }

    //НАЗАД
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(LibraryActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void searchItem(SQLiteDatabase db, String text, String lib) {
        listItems.clear();
        if (lib.equals("namesEl")) {
            Cursor cursor = db.rawQuery("SELECT " + DBHelper.NAME_EL + " FROM " + DBHelper.TABLE_NAMES_EL +
                    " WHERE " + DBHelper.NAME_EL + " LIKE '%" + text + "%' ORDER BY lower(" + DBHelper.NAME_EL + ");", null);
            if (cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(DBHelper.NAME_EL);
                do {
                    listItems.add(cursor.getString(nameIndex));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        if (lib.equals("marks")) {
            Cursor cursor1 = db.rawQuery("SELECT " + DBHelper.MARK + " FROM " + DBHelper.TABLE_MARKS +
                    " WHERE " + DBHelper.MARK + " LIKE '%" + text + "%' ORDER BY lower(" + DBHelper.MARK + ");", null);
            if (cursor1.moveToFirst()) {
                int nameIndex = cursor1.getColumnIndex(DBHelper.MARK);
                do {
                    listItems.add(cursor1.getString(nameIndex));
                } while (cursor1.moveToNext());
            }
            cursor1.close();
        }
        if (lib.equals("rooms")) {
            Cursor cursor1 = db.rawQuery("SELECT " + DBHelper.LIB_ROOM_NAME + " FROM " + DBHelper.TABLE_LIBRARY_ROOMS +
                    " WHERE " + DBHelper.LIB_ROOM_NAME + " LIKE '%" + text + "%' ORDER BY lower(" + DBHelper.LIB_ROOM_NAME + ");", null);
            if (cursor1.moveToFirst()) {
                int nameIndex = cursor1.getColumnIndex(DBHelper.LIB_ROOM_NAME);
                do {
                    listItems.add(cursor1.getString(nameIndex));
                } while (cursor1.moveToNext());
            }
            cursor1.close();
        }
        if (lib.equals("lines")) {
            Cursor cursor1 = db.rawQuery("SELECT " + DBHelper.LIB_LINE_NAME + " FROM " + DBHelper.TABLE_LIBRARY_LINES +
                    " WHERE " + DBHelper.LIB_LINE_NAME + " LIKE '%" + text + "%' ORDER BY lower(" + DBHelper.LIB_LINE_NAME + ");", null);
            if (cursor1.moveToFirst()) {
                int nameIndex = cursor1.getColumnIndex(DBHelper.LIB_LINE_NAME);
                do {
                    listItems.add(cursor1.getString(nameIndex));
                } while (cursor1.moveToNext());
            }
            cursor1.close();
        }
        if (lib.equals("floors")) {
            Cursor cursor1 = db.rawQuery("SELECT " + DBHelper.LIB_FLOOR_NAME + " FROM " + DBHelper.TABLE_LIBRARY_FLOORS +
                    " WHERE " + DBHelper.LIB_FLOOR_NAME + " LIKE '%" + text + "%' ORDER BY lower(" + DBHelper.LIB_FLOOR_NAME + ");", null);
            if (cursor1.moveToFirst()) {
                int nameIndex = cursor1.getColumnIndex(DBHelper.LIB_FLOOR_NAME);
                do {
                    listItems.add(cursor1.getString(nameIndex));
                } while (cursor1.moveToNext());
            }
            cursor1.close();
        }
        adapter.notifyDataSetChanged();
    }

    public void getLibraryItems(SQLiteDatabase db, String lib) {
        listItems = new ArrayList<String>();
        if (lib.equals("namesEl")) {
            Cursor cursor = db.rawQuery("SELECT " + DBHelper.NAME_EL +" FROM " + DBHelper.TABLE_NAMES_EL + " ORDER BY lower("+ DBHelper.NAME_EL +");", null);
            if (cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(DBHelper.NAME_EL);
                do {
                    listItems.add(cursor.getString(nameIndex));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        if (lib.equals("marks")) {
            Cursor cursor1 = db.rawQuery("SELECT " + DBHelper.MARK +" FROM " + DBHelper.TABLE_MARKS + " ORDER BY lower("+ DBHelper.MARK +");", null);
            if (cursor1.moveToFirst()) {
                int nameIndex = cursor1.getColumnIndex(DBHelper.MARK);
                do {
                    listItems.add(cursor1.getString(nameIndex));
                } while (cursor1.moveToNext());
            }
            cursor1.close();
        }
        if (lib.equals("rooms")) {
            Cursor cursor1 = db.rawQuery("SELECT " + DBHelper.LIB_ROOM_NAME +" FROM " + DBHelper.TABLE_LIBRARY_ROOMS + " ORDER BY lower("+ DBHelper.LIB_ROOM_NAME +");", null);
            if (cursor1.moveToFirst()) {
                int nameIndex = cursor1.getColumnIndex(DBHelper.LIB_ROOM_NAME);
                do {
                    listItems.add(cursor1.getString(nameIndex));
                } while (cursor1.moveToNext());
            }
            cursor1.close();
        }
        if (lib.equals("lines")) {
            Cursor cursor1 = db.rawQuery("SELECT " + DBHelper.LIB_LINE_NAME +" FROM " + DBHelper.TABLE_LIBRARY_LINES + " ORDER BY lower("+ DBHelper.LIB_LINE_NAME +");", null);
            if (cursor1.moveToFirst()) {
                int nameIndex = cursor1.getColumnIndex(DBHelper.LIB_LINE_NAME);
                do {
                    listItems.add(cursor1.getString(nameIndex));
                } while (cursor1.moveToNext());
            }
            cursor1.close();
        }
        if (lib.equals("floors")) {
            Cursor cursor1 = db.rawQuery("SELECT " + DBHelper.LIB_FLOOR_NAME +" FROM " + DBHelper.TABLE_LIBRARY_FLOORS + " ORDER BY lower("+ DBHelper.LIB_FLOOR_NAME +");", null);
            if (cursor1.moveToFirst()) {
                int nameIndex = cursor1.getColumnIndex(DBHelper.LIB_FLOOR_NAME);
                do {
                    listItems.add(cursor1.getString(nameIndex));
                } while (cursor1.moveToNext());
            }
            cursor1.close();
        }
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, listItems);
        library_items.setAdapter(adapter);
    }
}
