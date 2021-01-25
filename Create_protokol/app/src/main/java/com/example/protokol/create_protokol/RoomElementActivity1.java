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
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class RoomElementActivity1 extends AppCompatActivity {

    DBHelper dbHelper;
    private TemplatePDF templatePDF;

    //ЗАГОЛОВКИ
    String[] date = {"Дата проведения проверки «", "   ", "» ", "           ", " ", "       ", " г."};
    String[] months = {"января", "февраля", "марта", "апреля", "мая", "июня", "июля", "августа", "сентября", "октября", "ноября", "декабря"};
    private String[]header = {"№ п/п", "Месторасположение и наименование электрооборудования", "Кол-во проверенных элементов", "R перех. допустимое, (Ом)", "R перех. измеренное, (Ом)", "Вывод о\nсоответствии нормативному документу"};
    String zag = "Климатические условия при проведении измерений";
    String[] uslovia = {"Температура воздуха ", "UNDER", "\u00b0C. Влажность воздуха ", "UNDER", "%. Атмосферное давление ", "UNDER", " мм.рт.ст.(бар)."};
    String zag2 = "Нормативные и технические документы, на соответствие требованиям которых проведена проверка:";
    String line1 = "                                                                       ";
    String line2 = "ПУЭ 1.8.39 п.2; ПТЭЭП Приложение 3";
    String[] sign = {"Испытание провели:", "", "", "", "", "",
            "", "( Должность )", "", "( подпись )", "", "( Ф.И.О. )",
            "Проверил:", "", "", "", "", "",
            "", "( Должность )", "", "( подпись )", "", "( Ф.И.О. )"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_element1);

        dbHelper = new DBHelper(this);
        final SQLiteDatabase database = dbHelper.getWritableDatabase();

        final ListView floors = findViewById(R.id.floors);
        Button addFloor = findViewById(R.id.button9);
        Button pdf = findViewById(R.id.button8);
        Button back_btn = findViewById(R.id.button10);

        //НАСТРАИВАЕМ ACTIONBAR
        getSupportActionBar().setSubtitle("Этажи");
        getSupportActionBar().setTitle("Металлосвязь");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ЗАПРОС В БД И ЗАПОЛНЕНИЕ СПИСКА ЭТАЖЕЙ
        addSpisokFloors(database, floors);

        //ДОБАВИТЬ ЭТАЖ
        addFloor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(RoomElementActivity1.this);
                final View myView = getLayoutInflater().inflate(R.layout.dialog_for_marks,null);
                alert.setCancelable(false);
                alert.setTitle("Введите название этажа:");
                final AutoCompleteTextView input = myView.findViewById(R.id.autoCompleteTextView3);
                ImageView arrow = myView.findViewById(R.id.imageView4);
                //ОТОБРАЖЕНИЕ ВЫПЛЫВАЮЩЕГО СПИСКА
                ArrayAdapter<String>adapter1 = new ArrayAdapter<String>(RoomElementActivity1.this, android.R.layout.simple_dropdown_item_1line, getFloors(database));
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
                        final String nameFloor = input.getText().toString();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(DBHelper.FL_NAME, nameFloor);
                        database.insert(DBHelper.TABLE_FLOORS, null, contentValues);
                        //ЗАПРОС В БД И ЗАПОЛНЕНИЕ СПИСКА КОМНАТ
                        addSpisokFloors(database, floors);
                        //Если новое название этажа, то вносим его в базу
                        if (!Arrays.asList(getFloors(database)).contains(nameFloor)){
                            ContentValues newName = new ContentValues();
                            newName.put(DBHelper.LIB_FLOOR_NAME, nameFloor);
                            database.insert(DBHelper.TABLE_LIBRARY_FLOORS, null, newName);
                        }
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Этаж <" + nameFloor + "> добавлен", Toast.LENGTH_SHORT);
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

        //ПЕРЕХОД К КОМНАТАМ И РЕДАКТОР ЭТАЖА
        floors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, final long id) {

                //ЗАПРОС В БД ДЛЯ ПОЛУЧЕНИЯ ID НУЖНОГО ЭТАЖА
                Cursor cursor4 = database.query(DBHelper.TABLE_FLOORS, new String[] {DBHelper.FL_ID}, null, null, null, null, "_id DESC");
                cursor4.moveToPosition(position);
                int idFloorIndex = cursor4.getColumnIndex(DBHelper.FL_ID);
                final int idFloor = cursor4.getInt(idFloorIndex);
                cursor4.close();

                if (((TextView) view).getText().toString().equals("БЕЗ ЭТАЖА")) {
                    Intent intent = new Intent("android.intent.action.RoomElement2");
                    intent.putExtra("nameFloor", ((TextView) view).getText().toString());
                    intent.putExtra("idFloor", idFloor);
                    startActivity(intent);
                }
                else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(RoomElementActivity1.this);
                    alert.setTitle(((TextView) view).getText());
                    String arrayMenu[] = {"\nПерейти к комнатам\n", "\nИзменить название\n", "\nУдалить этаж\n"};
                    alert.setItems(arrayMenu, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //ПЕРЕЙТИ К КОМНАТАМ
                            if (which == 0) {
                                Intent intent = new Intent("android.intent.action.RoomElement2");
                                intent.putExtra("nameFloor", ((TextView) view).getText().toString());
                                intent.putExtra("idFloor", idFloor);
                                startActivity(intent);
                            }

                            //ИЗМЕНИТЬ НАЗВАНИЕ
                            if (which == 1) {
                                AlertDialog.Builder alert1 = new AlertDialog.Builder(RoomElementActivity1.this);
                                final View myView = getLayoutInflater().inflate(R.layout.dialog_for_marks, null);
                                alert1.setCancelable(false);
                                alert1.setTitle("Введите новое название этажа:");
                                final AutoCompleteTextView input = myView.findViewById(R.id.autoCompleteTextView3);
                                ImageView arrow = myView.findViewById(R.id.imageView4);
                                input.setText(((TextView) view).getText().toString());
                                //ОТОБРАЖЕНИЕ ВЫПЛЫВАЮЩЕГО СПИСКА
                                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(RoomElementActivity1.this, android.R.layout.simple_dropdown_item_1line, getFloors(database));
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
                                        String namefl = input.getText().toString();
                                        ContentValues uppname = new ContentValues();
                                        uppname.put(DBHelper.FL_NAME, namefl);
                                        database.update(DBHelper.TABLE_FLOORS,
                                                uppname,
                                                "_id = ?",
                                                new String[]{String.valueOf(idFloor)});
                                        //ЗАПРОС В БД И ЗАПОЛНЕНИЕ СПИСКА КОМНАТ
                                        addSpisokFloors(database, floors);
                                        //Если новое название комнаты, то вносим его в базу
                                        if (!Arrays.asList(getFloors(database)).contains(namefl)) {
                                            ContentValues newName = new ContentValues();
                                            newName.put(DBHelper.LIB_FLOOR_NAME, namefl);
                                            database.insert(DBHelper.TABLE_LIBRARY_FLOORS, null, newName);
                                        }
                                        Toast toast1 = Toast.makeText(getApplicationContext(),
                                                "Название изменено: " + namefl, Toast.LENGTH_SHORT);
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

                            //УДАЛИТЬ ЭТАЖ
                            if (which == 2) {

                                //ПОДТВЕРЖДЕНИЕ
                                AlertDialog.Builder builder4 = new AlertDialog.Builder(RoomElementActivity1.this);
                                builder4.setCancelable(false);
                                builder4.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        Cursor cursor1;
                                        Cursor cursor = database.query(DBHelper.TABLE_ROOMS, new String[]{DBHelper.KEY_ID, DBHelper.KEY_ID_FLOOR}, "rfl_id = ?", new String[]{String.valueOf(idFloor)}, null, null, null);
                                        if (cursor.moveToFirst()) {
                                            int room_IdIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                                            do {
                                                cursor1 = database.query(DBHelper.TABLE_ELEMENTS, new String[]{DBHelper.EL_ID}, "room_id = ?", new String[]{cursor.getString(room_IdIndex)}, null, null, null);
                                                if (cursor1.moveToFirst()) {
                                                    int el_idIndex = cursor1.getColumnIndex(DBHelper.EL_ID);
                                                    do {
                                                        database.delete(DBHelper.TABLE_ELEMENTS_PZ, "el_id = ?", new String[]{cursor1.getString(el_idIndex)});
                                                    } while (cursor1.moveToNext());
                                                }
                                                cursor1.close();
                                                database.delete(DBHelper.TABLE_ELEMENTS, "room_id = ?", new String[]{cursor.getString(room_IdIndex)});
                                            } while (cursor.moveToNext());
                                        }
                                        cursor.close();
                                        database.delete(DBHelper.TABLE_ROOMS, "rfl_id = ?", new String[]{String.valueOf(idFloor)});
                                        database.delete(DBHelper.TABLE_FLOORS, "_id = ?", new String[]{String.valueOf(idFloor)});
                                        //ЗАПРОС В БД И ЗАПОЛНЕНИЕ СПИСКА КОМНАТ
                                        addSpisokFloors(database, floors);
                                        Toast toast2 = Toast.makeText(getApplicationContext(),
                                                "Этаж <" + ((TextView) view).getText() + "> удален", Toast.LENGTH_SHORT);
                                        toast2.show();
                                    }
                                });
                                builder4.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {

                                    }
                                });
                                builder4.setMessage("Вы точно хотите удалить этаж <" + ((TextView) view).getText() + ">?");
                                AlertDialog dialog4 = builder4.create();
                                dialog4.show();
                            }
                        }
                    });
                    alert.show();
                }
            }
        });

        //ОТКРЫТЬ PDF
        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //ПРОСТО ОТКРЫТЬ ИЛИ С СОХРАНЕНИЕМ?
                AlertDialog.Builder builder1 = new AlertDialog.Builder(RoomElementActivity1.this);
                builder1.setPositiveButton("Посмотреть", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //ПРОСТО ПОСМОТРЕТЬ
                        opPFD(database, null);
                    }
                });
                builder1.setNegativeButton("Сохранить", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //ЗАПРАШИВАЕМ НАЗВАНИЕ ФАЙЛА
                        AlertDialog.Builder alert = new AlertDialog.Builder(RoomElementActivity1.this);
                        final View myView = getLayoutInflater().inflate(R.layout.dialog_for_names,null);
                        alert.setCancelable(false);
                        alert.setTitle("Введите название сохраняемого файла:");
                        final EditText input = myView.findViewById(R.id.editText);
                        openKeyboard();
                        alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                closeKeyboard(myView);
                                String namefile = input.getText().toString();
                                if (namefile.equals(""))
                                    namefile = null;
                                opPFD(database, namefile);
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
                builder1.setMessage("Хотите просто посмотреть файл или же открыть с дальнейшим сохранением?");
                AlertDialog dialog1 = builder1.create();
                dialog1.show();
            }
        });

        //ГОТОВО
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RoomElementActivity1.this, MenuItemsActivity.class);
                startActivity(intent);
            }
        });

        //НАСТРОЙКА PDF
//        optionsPdf.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                options(database);
//            }
//        });
    }

    //НАЗАД
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(RoomElementActivity1.this, MenuItemsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //ПОЛУЧЕНИЕ ЭТАЖЕЙ
    public String[] getFloors(SQLiteDatabase database) {
        final ArrayList<String> floorsDB = new ArrayList <String>();
        Cursor cursor = database.query(DBHelper.TABLE_LIBRARY_FLOORS, new String[] {DBHelper.LIB_FLOOR_NAME}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(DBHelper.LIB_FLOOR_NAME);
            do {
                floorsDB.add(cursor.getString(nameIndex));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return floorsDB.toArray(new String[floorsDB.size()]);
    }

    public void addSpisokFloors(SQLiteDatabase database, ListView rooms) {
        final ArrayList<String> spisokFloors = new ArrayList <String>();
        Cursor cursor = database.query(DBHelper.TABLE_FLOORS, new String[] {DBHelper.FL_NAME}, null, null, null, null, "_id DESC");
        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(DBHelper.FL_NAME);
            do {
                spisokFloors.add(cursor.getString(nameIndex));
            } while (cursor.moveToNext());
        }
        else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.FL_NAME, "БЕЗ ЭТАЖА");
            database.insert(DBHelper.TABLE_FLOORS, null, contentValues);
            spisokFloors.add("БЕЗ ЭТАЖА");
        }
        cursor.close();
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, spisokFloors);
        rooms.setAdapter(adapter);
    }

    //ДОБАВЛЕНИЕ В PDF ЗАГОЛОВКОВ
    public void start (String namefile, SQLiteDatabase database) {
        getDateAndUsl(database);
        templatePDF = new TemplatePDF(getApplicationContext());
        templatePDF.openDocument(namefile, false);
        templatePDF.addMetaData("Protokol", "Item", "Company");
        templatePDF.addTitles("РЕЗУЛЬТАТЫ", "проверки наличия цепи между заземленными установками и элементами заземленной установки", 12);
        templatePDF.addDate(date, 10);
        templatePDF.addCenter_BD(zag, 12, 0,5);
        templatePDF.addClimate(uslovia, 10);
        templatePDF.addCenter_BD(zag2, 12,7,5);
        templatePDF.addCenter_Under(line1 + line2 + line1, 10,0,5);
        templatePDF.createTableRE(header);
    }

    public void getDateAndUsl(SQLiteDatabase database) {
        String dateString;
        Cursor cursor1 = database.query(DBHelper.TABLE_TITLE, new String[] {DBHelper.TITLE_DATE, DBHelper.TITLE_TEMPERATURE,
                DBHelper.TITLE_HUMIDITY, DBHelper.TITLE_PRESSURE}, null, null, null, null, null);
        if (cursor1.moveToFirst()) {
            int dateIndex = cursor1.getColumnIndex(DBHelper.TITLE_DATE);
            int temperatureIndex = cursor1.getColumnIndex(DBHelper.TITLE_TEMPERATURE);
            int humidityIndex = cursor1.getColumnIndex(DBHelper.TITLE_HUMIDITY);
            int pressureIndex = cursor1.getColumnIndex(DBHelper.TITLE_PRESSURE);
            dateString = cursor1.getString(dateIndex);
            uslovia[1] = " " + cursor1.getString(temperatureIndex) + " ";
            uslovia[3] = " " + cursor1.getString(humidityIndex) + " ";
            uslovia[5] = " " + cursor1.getString(pressureIndex) + " ";
            date[1] = " " + Integer.parseInt(dateString.substring(0,2)) + " ";
            date[3] = " " + months[Integer.parseInt(dateString.substring(4,6)) - 1] + " ";
            date[5] = dateString.substring(8,12);
        }
        cursor1.close();
    }

    //НАСТРОЙКА ПДФ С РЕКУРСИЕЙ
//    public void options(final SQLiteDatabase database) {
//        AlertDialog.Builder alert = new AlertDialog.Builder(RoomElementActivity1.this);
//        alert.setCancelable(false);
//        alert.setTitle("Выберете комнату:");
//        alert.setAdapter(addSpisokRoomsPDF(database), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                //ЗАПРОС В БД ДЛЯ ПОЛУЧЕНИЯ НУЖНОЙ КОМНАТЫ
//                Cursor cursor4 = database.query(DBHelper.TABLE_ROOMS, new String[] {DBHelper.KEY_ID, DBHelper.KEY_NAME, DBHelper.KEY_HEADER, DBHelper.KEY_EMPTY_STRINGS}, null, null, null, null, null);
//                cursor4.moveToPosition(which);
//                int r_idIndex = cursor4.getColumnIndex(DBHelper.KEY_ID);
//                int roomNameIndex = cursor4.getColumnIndex(DBHelper.KEY_NAME);
//                int headerIndex = cursor4.getColumnIndex(DBHelper.KEY_HEADER);
//                int stringsIndex = cursor4.getColumnIndex(DBHelper.KEY_EMPTY_STRINGS);
//                final int r_id = cursor4.getInt(r_idIndex);
//                int strings = cursor4.getInt(stringsIndex);
//                String header = cursor4.getString(headerIndex);
//                String nameRoom = cursor4.getString(roomNameIndex);
//                cursor4.close();
//
//                AlertDialog.Builder alert1 = new AlertDialog.Builder(RoomElementActivity1.this);
//                final View myView = getLayoutInflater().inflate(R.layout.dialog_for_options_pdf, null);
//                alert1.setCancelable(false);
//                alert1.setTitle(nameRoom);
//                final CheckBox head = myView.findViewById(R.id.checkBox2);
//                Button subtract = myView.findViewById(R.id.button2);
//                Button add = myView.findViewById(R.id.button3);
//                final EditText numb = myView.findViewById(R.id.editText4);
//
//                //ЗАПОЛНЕНИЕ
//                if (header.equals("-"))
//                    head.setChecked(false);
//                else
//                    head.setChecked(true);
//                numb.setText(String.valueOf(strings));
//
//                //- ПУСТАЯ СТРОКА
//                subtract.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (Integer.parseInt(numb.getText().toString()) > 0)
//                            numb.setText(String.valueOf(Integer.parseInt(numb.getText().toString()) - 1));
//                    }
//                });
//
//                //+ ПУСТАЯ СТРОКА
//                add.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        numb.setText(String.valueOf(Integer.parseInt(numb.getText().toString()) + 1));
//                    }
//                });
//
//                alert1.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        //СОБИРАЕМ ДАННЫЕ
//                        int strings = Integer.parseInt(numb.getText().toString());
//                        String header;
//                        if (head.isChecked())
//                            header = "eсть";
//                        else
//                            header = "-";
//
//                        //ИЗМЕНЯЕМ ЗАГОЛОВОК
//                        ContentValues upphead = new ContentValues();
//                        upphead.put(DBHelper.KEY_HEADER, header);
//                        database.update(DBHelper.TABLE_ROOMS,
//                                upphead,
//                                "_id = ?",
//                                new String[] {String.valueOf(r_id)});
//
//                        //ИЗМЕНЯЕМ КОЛ-ВО ПУСТЫХ СТРОК
//                        ContentValues uppstrings = new ContentValues();
//                        uppstrings.put(DBHelper.KEY_EMPTY_STRINGS, strings);
//                        database.update(DBHelper.TABLE_ROOMS,
//                                uppstrings,
//                                "_id = ?",
//                                new String[] {String.valueOf(r_id)});
//                        options(database);
//                        Toast toast1 = Toast.makeText(getApplicationContext(),
//                                "Данные сохранены", Toast.LENGTH_SHORT);
//                        toast1.show();
//                    }
//                });
//                alert1.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        options(database);
//                    }
//                });
//                alert1.setView(myView);
//                alert1.show();
//            }
//        });
//        alert.setPositiveButton("Закрыть", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//
//            }
//        });
//        alert.show();
//    }

//    public ArrayAdapter addSpisokRoomsPDF(SQLiteDatabase database) {
//        final ArrayList<String> spisokRooms = new ArrayList <String>();
//        int countR = 1;
//        Cursor cursor = database.query(DBHelper.TABLE_ROOMS, new String[] {DBHelper.KEY_NAME, DBHelper.KEY_HEADER, DBHelper.KEY_EMPTY_STRINGS}, null, null, null, null, null);
//        if (cursor.moveToFirst()) {
//            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
//            int stringsIndex = cursor.getColumnIndex(DBHelper.KEY_EMPTY_STRINGS);
//            int headersIndex = cursor.getColumnIndex(DBHelper.KEY_HEADER);
//            do {
//                spisokRooms.add(countR++ + ". " + cursor.getString(nameIndex) + "\n(Шапка: " + cursor.getString(headersIndex) + ", Строки: " + cursor.getString(stringsIndex) + ")");
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        return new ArrayAdapter<>(this, R.layout.list_item, spisokRooms);
//    }

    //ОТКРЫТИЕ PDF
    public void opPFD(SQLiteDatabase database, String namefile) {
        if (namefile == null)
            namefile = "TepmlatePDF";
        start(namefile, database);

        //СПИСОК ЭТАЖЕЙ И КОЛ-ВО КОМНАТ
        ArrayList<String> floors = new ArrayList<>();
        ArrayList<Integer> countRooms = new ArrayList<>();
        //СПИСОК КОМНАТ И КОЛ-ВО ЭЛЕМЕНТОВ
        ArrayList<String> rooms = new ArrayList<>();
        ArrayList<Integer> countElements = new ArrayList<>();
        //СПИСОК ЭЛЕМЕНТОВ
        ArrayList<String> element = new ArrayList<>();
        ArrayList<ArrayList> elements = new ArrayList<>();
        //СПИСОК НЕЗАЗЕМЛЕННЫХ ЭЛЕМЕНТОВ
        final ArrayList<String> NZ = new ArrayList<>();

        //ЗАПРОС НА СПИСОК ЭТАЖЕЙ И КОЛ-ВО КОМНАТ
        Cursor cursor = database.rawQuery("SELECT floor, count(*) AS count_rooms FROM floors AS f " +
                "JOIN rooms AS r ON f._id = r.rfl_id AND r._id IN (" +
                "SELECT room_id FROM elements GROUP BY room_id" +
                ") GROUP BY f._id ORDER BY f._id, r._id", new String[] { });
        if (cursor.moveToFirst()) {
            int nameFloorIndex = cursor.getColumnIndex(DBHelper.FL_NAME);
            int countRoomIndex = cursor.getColumnIndex("count_rooms");
            do {
                floors.add(cursor.getString(nameFloorIndex));
                countRooms.add(cursor.getInt(countRoomIndex));
            } while (cursor.moveToNext());
        }
        cursor.close();

        //ЗАПРОС НА СПИСОК КОМНАТ И КОЛ-ВО ЭЛЕМЕНТОВ
        Cursor cursor1 = database.rawQuery("SELECT room, count(*) AS count_elements FROM rooms AS r JOIN elements AS e ON r._id = e.room_id GROUP BY r._id ORDER BY r.rfl_id, r._id, e._id", new String[] { });
        if (cursor1.moveToFirst()) {
            int nameRoomIndex = cursor1.getColumnIndex(DBHelper.KEY_NAME);
            int countElementIndex = cursor1.getColumnIndex("count_elements");
            do {
                rooms.add(cursor1.getString(nameRoomIndex));
                countElements.add(cursor1.getInt(countElementIndex));
            } while (cursor1.moveToNext());
        }
        cursor1.close();

        //ЗАПРОС НА СПИСОК ЭЛЕМЕНТОВ
        Cursor cursor2 = database.rawQuery("SELECT element, unit, number, sopr, conclusion FROM elements AS e JOIN rooms AS r ON e.room_id = r._id ORDER BY r.rfl_id, r._id, e._id", new String[] { });
        if (cursor2.moveToFirst()) {
            int elnameIndex = cursor2.getColumnIndex(DBHelper.EL_NAME);
            int elunitIndex = cursor2.getColumnIndex(DBHelper.EL_UNIT);
            int elnumberIndex = cursor2.getColumnIndex(DBHelper.EL_NUMBER);
            int elsoprIndex = cursor2.getColumnIndex(DBHelper.EL_SOPR);
            int conclusionIndex = cursor2.getColumnIndex(DBHelper.EL_CONCLUSION);
            String conclusion, unit;
            int currentElement, i, sum = 0;
            do {
                conclusion = cursor2.getString(conclusionIndex);
                unit =  cursor2.getString(elunitIndex);
                //ОПРЕДЕЛЯЕМ НЗ
                if (conclusion.equals("не соответствует")) {
                    //1.Узнаем общий номер элемента
                    if (elements.isEmpty())
                        currentElement = 1;
                    else
                        currentElement = elements.size() + 1;
                    //2.Ищем данный элемент в списке элементов
                    for (i = 0; i < countElements.size(); i++){
                        sum = sum + countElements.get(i);
                        if (sum >= currentElement)
                            break;
                    }
                    //3.Готово
                    NZ.add(String.valueOf(i + 1) + "." + String.valueOf(currentElement - (sum - countElements.get(i))));
                    sum = 0;
                }
                //УКАЗЫВАЕМ ШТ И ГН РЯДОМ С ИМЕНЕМ
                if (unit.equals("пусто"))
                    element.add(cursor2.getString(elnameIndex));
                else
                    element.add(cursor2.getString(elnameIndex) + "    " + cursor2.getString(elnumberIndex) + " " + unit);
                element.add(cursor2.getString(elnumberIndex));
                element.add("0,05");
                element.add(cursor2.getString(elsoprIndex));
                element.add(conclusion);
                elements.add(element);
                element = new ArrayList<>();
            } while (cursor2.moveToNext());
        }
        cursor2.close();
        templatePDF.createTableRE2(floors, countRooms, rooms, countElements, elements);
        String joinedNZ;
        if (!NZ.isEmpty())
            joinedNZ = TextUtils.join("; ", NZ);
        else
            joinedNZ = "нет";
        String[] end = {"a) Проверена целостность и прочность проводников заземления и зануления, переходные контакты их\n" +
                "     соединений, болтовые соединения проверены на затяжку, сварные – ударом молотка.\n" +
                "b) Сопротивление переходных контактов выше нормы, указаны в п/п ", "                                   нет",
                "c) Не заземлено оборудование, указанное в п/п ", "                          " + joinedNZ,
                "d) Величина измеренного переходного сопротивления прочих контактов заземляющих и нулевых проводников," +
                        "\n     элементов электрооборудования соответствует (", "не соответствует", ") нормам ПУЭ и ПТЭЭП"};
        templatePDF.addParagraphEnd_RE(end, sign);
        templatePDF.closeDocument();
        templatePDF.appViewPDF(RoomElementActivity1.this);
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
