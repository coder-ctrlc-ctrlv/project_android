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
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class InsulationActivity extends AppCompatActivity {

    DBHelper dbHelper;
    private TemplatePDF templatePDF;

    String[] date = {"Дата проведения проверки «", "   ", "» ", "           ", " ", "       ", " г."};
    String[] months = {"января", "февраля", "марта", "апреля", "мая", "июня", "июля", "августа", "сентября", "октября", "ноября", "декабря"};
    String zag = "Климатические условия при проведении измерений";
    String[] uslovia = {"Температура воздуха ", "UNDER", "\u00b0C. Влажность воздуха ", "UNDER", "%. Атмосферное давление ", "UNDER", " мм.рт.ст.(бар)."};
    String zag2 = "Нормативные и технические документы, на соответствие требованиям которых проведена проверка:";
    String line1 = "                                                                                                        ";
    String line2 = "ПУЭ 1.8.37 п.1, 1.8.40 п.2; ПТЭЭП Приложение 3";
    String[] sign = {"Испытание провели:", "", "", "", "", "",
            "", "( Должность )", "", "( подпись )", "", "( Ф.И.О. )",
            "Проверил:", "", "", "", "", "",
            "", "( Должность )", "", "( подпись )", "", "( Ф.И.О. )"};
    private String[] header = {"№\nп/п", "Наименование линий, по проекту", "Рабочее\nнапряжение, В", "Марка провода,\nкабеля", "Количество\nжил, сечение\nпровода,\nкабеля, мм кв.",
            "Напряжение\nмегаомметра, В", "Допустимое\nсопротивление\nизоляции, МОм", "Сопротивление изоляции, МОм", "L1-L2\n(A-B)", "L2-L3\n(В-С)", "L3-L1\n(C-A)", "L1-N\n(A-N)\n(PEN)",
            "L2-N\n(B-N)\n(PEN)", "L3-N\n(C-N)\n(PEN)", "L1-PE\n(A-PE)", "L2-PE\n(B-PE)", "L3-PE\n(C-PE)", "N-PE", "Вывод о\nсоответствии\nнормативному\nдокументу"};
    private String[] end = {"Примечание: ", "                       \u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014",
            "Заключение: ", "   Сопротивление изоляции проводов и кабелей соответствует требованиям ПУЭ 1.8.37 п.1, 1.8.40 п.2; ПТЭЭП Приложение 3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insulation);

        dbHelper = new DBHelper(this);
        final SQLiteDatabase database = dbHelper.getWritableDatabase();

        final ListView rooms = findViewById(R.id.rooms);
        Button addRoom = findViewById(R.id.button9);
        Button pdf = findViewById(R.id.button8);
        Button optionsPdf = findViewById(R.id.button10);

        //НАСТРАИВАЕМ ACTIONBAR
        getSupportActionBar().setSubtitle("Комнаты");
        getSupportActionBar().setTitle("Изоляция");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ЗАПРОС В БД И ЗАПОЛНЕНИЕ СПИСКА КОМНАТ
        addSpisokRooms(database, rooms);

        //ДОБАВИТЬ КОМНАТУ
        addRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity.this);
                final View myView = getLayoutInflater().inflate(R.layout.dialog_for_marks,null);
                alert.setCancelable(false);
                alert.setTitle("Введите название комнаты:");
                final AutoCompleteTextView input = myView.findViewById(R.id.autoCompleteTextView3);
                ImageView arrow = myView.findViewById(R.id.imageView4);
                //ОТОБРАЖЕНИЕ ВЫПЛЫВАЮЩЕГО СПИСКА
                ArrayAdapter<String>adapter1 = new ArrayAdapter<String>(InsulationActivity.this, android.R.layout.simple_dropdown_item_1line, getRooms(database));
                input.setAdapter(adapter1);
                arrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //СКРЫВАЕМ КЛАВИАТУРУ
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(myView.getWindowToken(),0);
                        input.showDropDown();
                    }
                });
                alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        final String nameRoom = input.getText().toString();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(DBHelper.LNR_NAME, nameRoom);
                        database.insert(DBHelper.TABLE_LINE_ROOMS, null, contentValues);
                        //ЗАПРОС В БД И ЗАПОЛНЕНИЕ СПИСКА КОМНАТ
                        addSpisokRooms(database, rooms);
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
                        //СКРЫВАЕМ КЛАВИАТУРУ
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(myView.getWindowToken(),0);
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
                AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity.this);
                alert.setTitle(((TextView) view).getText());
                String arrayMenu[] = {"\nПерейти к щитам\n", "\nИзменить название\n", "\nУдалить комнату\n"};
                alert.setItems(arrayMenu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //ЗАПРОС В БД ДЛЯ ПОЛУЧЕНИЯ ID НУЖНОЙ КОМНАТЫ
                        Cursor cursor4 = database.query(DBHelper.TABLE_LINE_ROOMS, new String[] {DBHelper.LNR_ID}, null, null, null, null, "_id DESC");
                        cursor4.moveToPosition(position);
                        int idRoomIndex = cursor4.getColumnIndex(DBHelper.LNR_ID);
                        final int idRoom = cursor4.getInt(idRoomIndex);
                        cursor4.close();

                        //ПЕРЕЙТИ К ЩИТАМ
                        if (which == 0) {
                            Intent intent = new Intent("android.intent.action.Insulation2");
                            intent.putExtra("nameRoom", ((TextView) view).getText().toString());
                            intent.putExtra("idRoom", idRoom);
                            startActivity(intent);
                        }

                        //ИЗМЕНИТЬ НАЗВАНИЕ
                        if (which == 1) {
                            AlertDialog.Builder alert1 = new AlertDialog.Builder(InsulationActivity.this);
                            final View myView = getLayoutInflater().inflate(R.layout.dialog_for_marks,null);
                            alert1.setCancelable(false);
                            alert1.setTitle("Введите новое название комнаты:");
                            final AutoCompleteTextView input = myView.findViewById(R.id.autoCompleteTextView3);
                            ImageView arrow = myView.findViewById(R.id.imageView4);
                            input.setText(((TextView) view).getText().toString());
                            //ОТОБРАЖЕНИЕ ВЫПЛЫВАЮЩЕГО СПИСКА
                            ArrayAdapter<String>adapter1 = new ArrayAdapter<String>(InsulationActivity.this, android.R.layout.simple_dropdown_item_1line, getRooms(database));
                            input.setAdapter(adapter1);
                            arrow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //СКРЫВАЕМ КЛАВИАТУРУ
                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(myView.getWindowToken(),0);
                                    input.showDropDown();
                                }
                            });
                            alert1.setPositiveButton("Изменить", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    String namer = input.getText().toString();
                                    ContentValues uppname = new ContentValues();
                                    uppname.put(DBHelper.LNR_NAME, namer);
                                    database.update(DBHelper.TABLE_LINE_ROOMS,
                                            uppname,
                                            "_id = ?",
                                            new String[] {String.valueOf(idRoom)});
                                    //ЗАПРОС В БД И ЗАПОЛНЕНИЕ СПИСКА КОМНАТ
                                    addSpisokRooms(database, rooms);
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
                                    //СКРЫВАЕМ КЛАВИАТУРУ
                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(myView.getWindowToken(),0);
                                }
                            });
                            alert1.setView(myView);
                            alert1.show();
                        }

                        //УДАЛИТЬ КОМНАТУ
                        if (which == 2) {

                            //ПОДТВЕРЖДЕНИЕ
                            AlertDialog.Builder builder4 = new AlertDialog.Builder(InsulationActivity.this);
                            builder4.setCancelable(false);
                            builder4.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Cursor cursor = database.query(DBHelper.TABLE_LINES, new String[] {DBHelper.LN_ID, DBHelper.LN_ID_ROOM}, "lnr_id = ?", new String[] {String.valueOf(idRoom)}, null, null, null);
                                    if (cursor.moveToFirst()) {
                                        int line_IdIndex = cursor.getColumnIndex(DBHelper.LN_ID);
                                        do {
                                            database.delete(DBHelper.TABLE_GROUPS, "grline_id = ?", new String[] {cursor.getString(line_IdIndex)});
                                        } while (cursor.moveToNext());
                                    }
                                    cursor.close();
                                    database.delete(DBHelper.TABLE_LINES, "lnr_id = ?", new String[] {String.valueOf(idRoom)});
                                    database.delete(DBHelper.TABLE_LINE_ROOMS, "_id = ?", new String[] {String.valueOf(idRoom)});
                                    //ЗАПРОС В БД И ЗАПОЛНЕНИЕ СПИСКА КОМНАТ
                                    addSpisokRooms(database, rooms);
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

        //ОТКРЫТИЕ PDF
        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ПРОСТО ОТКРЫТЬ ИЛИ С СОХРАНЕНИЕМ?
                AlertDialog.Builder builder1 = new AlertDialog.Builder(InsulationActivity.this);
                builder1.setPositiveButton("Посмотреть", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //ПРОСТО ПОСМОТРЕТЬ
                        opPFD(database, null);
                    }
                });
                builder1.setNegativeButton("Сохранить", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //ЗАПРАШИВАЕМ НАЗВАНИЕ ФАЙЛА
                        AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity.this);
                        final View myView = getLayoutInflater().inflate(R.layout.dialog_for_names,null);
                        alert.setCancelable(false);
                        alert.setTitle("Введите название сохраняемого файла:");
                        final EditText input = myView.findViewById(R.id.editText);
                        //ОТКРЫВАЕМ КЛАВИАТУРУ
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                        alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //СКРЫВАЕМ КЛАВИАТУРУ
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(myView.getWindowToken(),0);
                                String namefile = input.getText().toString();
                                if (namefile.equals(""))
                                    namefile = null;
                                opPFD(database, namefile);
                            }
                        });
                        alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //СКРЫВАЕМ КЛАВИАТУРУ
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(myView.getWindowToken(),0);
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
                Intent intent = new Intent(InsulationActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void start(String namefile, SQLiteDatabase database) {
        getDateAndUsl(database);
        templatePDF = new TemplatePDF(getApplicationContext());
        templatePDF.openDocument(namefile, true);
        templatePDF.addMetaData("Protokol", "Item", "Company");
        templatePDF.addTitles("РЕЗУЛЬТАТЫ", "проверки сопротивления изоляции проводов и кабелей", 12);
        templatePDF.addDate(date, 10);
        templatePDF.addCenter_BD(zag, 12,0,5);
        templatePDF.addClimate(uslovia, 10);
        templatePDF.addCenter_BD(zag2, 12,7,5);
        templatePDF.addCenter_Under(line1 + line2 + line1, 10,0,5);
        templatePDF.createTableInsulation(header);
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
//        AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity.this);
//        alert.setCancelable(false);
//        alert.setTitle("Выберете комнату:");
//        alert.setAdapter(addSpisokRoomsPDF(database), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                //ЗАПРОС В БД ДЛЯ ПОЛУЧЕНИЯ НУЖНОЙ КОМНАТЫ
//                Cursor cursor4 = database.query(DBHelper.TABLE_LINE_ROOMS, new String[] {DBHelper.LNR_ID, DBHelper.LNR_NAME, DBHelper.LNR_HEADER, DBHelper.LNR_EMPTY_STRINGS}, null, null, null, null, null);
//                cursor4.moveToPosition(which);
//                int r_idIndex = cursor4.getColumnIndex(DBHelper.LNR_ID);
//                int roomNameIndex = cursor4.getColumnIndex(DBHelper.LNR_NAME);
//                int headerIndex = cursor4.getColumnIndex(DBHelper.LNR_HEADER);
//                int stringsIndex = cursor4.getColumnIndex(DBHelper.LNR_EMPTY_STRINGS);
//                final int r_id = cursor4.getInt(r_idIndex);
//                final int strings = cursor4.getInt(stringsIndex);
//                final String header = cursor4.getString(headerIndex);
//                final String nameRoom = cursor4.getString(roomNameIndex);
//                cursor4.close();
//
//                AlertDialog.Builder alert2 = new AlertDialog.Builder(InsulationActivity.this);
//                alert2.setTitle(nameRoom);
//                String arrayMenu[] = {"Перейти к щитам", "Настроить комнату"};
//                alert2.setItems(arrayMenu, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        //ПЕРЕЙТИ К ЩИТАМ
//                        if (which == 0) {
//                            AlertDialog.Builder alert3 = new AlertDialog.Builder(InsulationActivity.this);
//                            alert3.setCancelable(false);
//                            alert3.setTitle("Выберете щит:");
//                            alert3.setAdapter(addSpisokLinesPDF(database, r_id), new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                    //ЗАПРОС В БД ДЛЯ ПОЛУЧЕНИЯ НУЖНОГО ЩИТА
//                                    Cursor cursor = database.query(DBHelper.TABLE_LINES, new String[] {DBHelper.LN_ID, DBHelper.LN_NAME, DBHelper.LN_EMPTY_STRINGS, DBHelper.LN_HEADER}, "lnr_id = ?", new String[] {String.valueOf(r_id)}, null, null, null);
//                                    cursor.moveToPosition(which);
//                                    int lineIndex = cursor.getColumnIndex(DBHelper.LN_ID);
//                                    int lineNameIndex = cursor.getColumnIndex(DBHelper.LN_NAME);
//                                    int lineHeaderIndex = cursor.getColumnIndex(DBHelper.LN_HEADER);
//                                    int lineStringsIndex = cursor.getColumnIndex(DBHelper.LN_EMPTY_STRINGS);
//                                    final int lineStrings = cursor.getInt(lineStringsIndex);
//                                    final String lineHeader = cursor.getString(lineHeaderIndex);
//                                    String nameLine = cursor.getString(lineNameIndex);
//                                    final int lineId = cursor.getInt(lineIndex);
//                                    cursor.close();
//
//                                    AlertDialog.Builder alert4 = new AlertDialog.Builder(InsulationActivity.this);
//                                    final View myView = getLayoutInflater().inflate(R.layout.dialog_for_options_pdf, null);
//                                    alert4.setCancelable(false);
//                                    alert4.setTitle(nameLine);
//                                    final CheckBox headL = myView.findViewById(R.id.checkBox2);
//                                    Button subtractL = myView.findViewById(R.id.button2);
//                                    Button addL = myView.findViewById(R.id.button3);
//                                    final EditText numbL = myView.findViewById(R.id.editText4);
//
//                                    //ЗАПОЛНЕНИЕ
//                                    if (lineHeader.equals("-"))
//                                        headL.setChecked(false);
//                                    else
//                                        headL.setChecked(true);
//                                    numbL.setText(String.valueOf(lineStrings));
//
//                                    //- ПУСТАЯ СТРОКА
//                                    subtractL.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            if (Integer.parseInt(numbL.getText().toString()) > 0)
//                                                numbL.setText(String.valueOf(Integer.parseInt(numbL.getText().toString()) - 1));
//                                        }
//                                    });
//
//                                    //+ ПУСТАЯ СТРОКА
//                                    addL.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            numbL.setText(String.valueOf(Integer.parseInt(numbL.getText().toString()) + 1));
//                                        }
//                                    });
//
//                                    alert4.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int whichButton) {
//                                            //СОБИРАЕМ ДАННЫЕ
//                                            int strings = Integer.parseInt(numbL.getText().toString());
//                                            String header;
//                                            if (headL.isChecked())
//                                                header = "eсть";
//                                            else
//                                                header = "-";
//
//                                            //ИЗМЕНЯЕМ ЗАГОЛОВОК
//                                            ContentValues upphead = new ContentValues();
//                                            upphead.put(DBHelper.LN_HEADER, header);
//                                            database.update(DBHelper.TABLE_LINES,
//                                                    upphead,
//                                                    "_id = ?",
//                                                    new String[] {String.valueOf(lineId)});
//
//                                            //ИЗМЕНЯЕМ КОЛ-ВО ПУСТЫХ СТРОК
//                                            ContentValues uppstrings = new ContentValues();
//                                            uppstrings.put(DBHelper.LN_EMPTY_STRINGS, strings);
//                                            database.update(DBHelper.TABLE_LINES,
//                                                    uppstrings,
//                                                    "_id = ?",
//                                                    new String[] {String.valueOf(lineId)});
//                                            options(database);
//                                            Toast toast1 = Toast.makeText(getApplicationContext(),
//                                                    "Данные сохранены", Toast.LENGTH_SHORT);
//                                            toast1.show();
//                                        }
//                                    });
//                                    alert4.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int whichButton) {
//                                            options(database);
//                                        }
//                                    });
//                                    alert4.setView(myView);
//                                    alert4.show();
//                                }
//                            });
//                            alert3.setPositiveButton("Закрыть", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int whichButton) {
//
//                                }
//                            });
//                            alert3.show();
//                        }
//
//                        //НАСТРОЙКА КОМНАТЫ
//                        if (which == 1) {
//                            AlertDialog.Builder alert1 = new AlertDialog.Builder(InsulationActivity.this);
//                            final View myView = getLayoutInflater().inflate(R.layout.dialog_for_options_pdf, null);
//                            alert1.setCancelable(false);
//                            alert1.setTitle(nameRoom);
//                            final CheckBox head = myView.findViewById(R.id.checkBox2);
//                            Button subtract = myView.findViewById(R.id.button2);
//                            Button add = myView.findViewById(R.id.button3);
//                            final EditText numb = myView.findViewById(R.id.editText4);
//
//                            //ЗАПОЛНЕНИЕ
//                            if (header.equals("-"))
//                                head.setChecked(false);
//                            else
//                                head.setChecked(true);
//                            numb.setText(String.valueOf(strings));
//
//                            //- ПУСТАЯ СТРОКА
//                            subtract.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    if (Integer.parseInt(numb.getText().toString()) > 0)
//                                        numb.setText(String.valueOf(Integer.parseInt(numb.getText().toString()) - 1));
//                                }
//                            });
//
//                            //+ ПУСТАЯ СТРОКА
//                            add.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    numb.setText(String.valueOf(Integer.parseInt(numb.getText().toString()) + 1));
//                                }
//                            });
//
//                            alert1.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int whichButton) {
//                                    //СОБИРАЕМ ДАННЫЕ
//                                    int strings = Integer.parseInt(numb.getText().toString());
//                                    String header;
//                                    if (head.isChecked())
//                                        header = "eсть";
//                                    else
//                                        header = "-";
//
//                                    //ИЗМЕНЯЕМ ЗАГОЛОВОК
//                                    ContentValues upphead = new ContentValues();
//                                    upphead.put(DBHelper.LNR_HEADER, header);
//                                    database.update(DBHelper.TABLE_LINE_ROOMS,
//                                            upphead,
//                                            "_id = ?",
//                                            new String[] {String.valueOf(r_id)});
//
//                                    //ИЗМЕНЯЕМ КОЛ-ВО ПУСТЫХ СТРОК
//                                    ContentValues uppstrings = new ContentValues();
//                                    uppstrings.put(DBHelper.LNR_EMPTY_STRINGS, strings);
//                                    database.update(DBHelper.TABLE_LINE_ROOMS,
//                                            uppstrings,
//                                            "_id = ?",
//                                            new String[] {String.valueOf(r_id)});
//                                    options(database);
//                                    Toast toast1 = Toast.makeText(getApplicationContext(),
//                                            "Данные сохранены", Toast.LENGTH_SHORT);
//                                    toast1.show();
//                                }
//                            });
//                            alert1.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int whichButton) {
//                                    options(database);
//                                }
//                            });
//                            alert1.setView(myView);
//                            alert1.show();
//                        }
//                    }
//                });
//                alert2.show();
//
//
//            }
//        });
//        alert.setPositiveButton("Закрыть", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//
//            }
//        });
//        alert.show();
//    }

    //ОТКРЫТИЕ PDF
    public void opPFD(SQLiteDatabase database, String namefile) {
        if (namefile == null)
            namefile = "TepmlatePDF";
        start(namefile, database);

        //СПИСОК КОМНАТ И КОЛ-ВО ЩИТОВ
        ArrayList<String> rooms = new ArrayList<>();
        ArrayList<Integer> countLines = new ArrayList<>();
        //СПИСОК ЩИТОВ И КОЛ-ВО ГРУПП
        ArrayList<String> lines = new ArrayList<>();
        ArrayList<Integer> countGroups = new ArrayList<>();
        //СПИСОК ГРУПП
        ArrayList<String> group = new ArrayList<>();
        ArrayList<ArrayList> groups = new ArrayList<>();

        //ЗАПРОС НА СПИСОК КОМНАТ И КОЛ-ВО ЩИТОВ
        Cursor cursor = database.rawQuery("SELECT room, count(*) AS count_lines FROM lnrooms AS r JOIN `lines` AS l ON r._id = l.lnr_id GROUP BY r._id ORDER BY r._id", new String[] { });
        if (cursor.moveToFirst()) {
            int nameRoomIndex = cursor.getColumnIndex(DBHelper.LNR_NAME);
            int countLineIndex = cursor.getColumnIndex("count_lines");
            do {
                rooms.add(cursor.getString(nameRoomIndex));
                countLines.add(cursor.getInt(countLineIndex));
            } while (cursor.moveToNext());
        }
        cursor.close();

        //ЗАПРОС НА СПИСОК ЩИТОВ И КОЛ-ВО ГРУПП
        Cursor cursor1 = database.rawQuery("SELECT line, count(*) AS count_groups FROM `lines` AS l JOIN groups AS g ON l._id = g.grline_id GROUP BY l._id ORDER BY l.lnr_id", new String[] { });
        if (cursor1.moveToFirst()) {
            int nameLineIndex = cursor1.getColumnIndex(DBHelper.LN_NAME);
            int countGroupIndex = cursor1.getColumnIndex("count_groups");
            do {
                lines.add(cursor1.getString(nameLineIndex));
                countGroups.add(cursor1.getInt(countGroupIndex));
            } while (cursor1.moveToNext());
        }
        cursor1.close();

        //ЗАПРОС НА СПИСОК ГРУПП
        Cursor cursor2 = database.rawQuery("SELECT name_group, u1, mark, vein, section, u2, r, phase, a_b, b_c, c_a, a_n, b_n, c_n, a_pe, b_pe, c_pe, n_pe, conclusion " +
                "FROM groups AS g JOIN `lines` AS l ON g.grline_id = l._id ORDER BY l.lnr_id, l._id", new String[] { });
        if (cursor2.moveToFirst()) {
            int nameGroupIndex = cursor2.getColumnIndex(DBHelper.GR_NAME);
            int markIndex = cursor2.getColumnIndex(DBHelper.GR_MARK);
            int veinIndex = cursor2.getColumnIndex(DBHelper.GR_VEIN);
            int sectionIndex = cursor2.getColumnIndex(DBHelper.GR_SECTION);
            int workUIndex = cursor2.getColumnIndex(DBHelper.GR_U1);
            int uIndex = cursor2.getColumnIndex(DBHelper.GR_U2);
            int rIndex = cursor2.getColumnIndex(DBHelper.GR_R);
            int a_bIndex = cursor2.getColumnIndex(DBHelper.GR_A_B);
            int b_cIndex = cursor2.getColumnIndex(DBHelper.GR_B_C);
            int c_aIndex = cursor2.getColumnIndex(DBHelper.GR_C_A);
            int a_nIndex = cursor2.getColumnIndex(DBHelper.GR_A_N);
            int b_nIndex = cursor2.getColumnIndex(DBHelper.GR_B_N);
            int c_nIndex = cursor2.getColumnIndex(DBHelper.GR_C_N);
            int a_peIndex = cursor2.getColumnIndex(DBHelper.GR_A_PE);
            int b_peIndex = cursor2.getColumnIndex(DBHelper.GR_B_PE);
            int c_peIndex = cursor2.getColumnIndex(DBHelper.GR_C_PE);
            int n_peIndex = cursor2.getColumnIndex(DBHelper.GR_N_PE);
            int conclusionIndex = cursor2.getColumnIndex(DBHelper.GR_CONCLUSION);
            String vein;
            do {
                vein = cursor2.getString(veinIndex);
                group.add(cursor2.getString(nameGroupIndex));
                group.add(cursor2.getString(workUIndex));
                group.add(cursor2.getString(markIndex));
                if (vein.equals("-"))
                    group.add("-");
                else
                    group.add(vein + "x" + cursor2.getString(sectionIndex));
                group.add(cursor2.getString(uIndex));
                group.add(cursor2.getString(rIndex));
                group.add(cursor2.getString(a_bIndex));
                group.add(cursor2.getString(b_cIndex));
                group.add(cursor2.getString(c_aIndex));
                group.add(cursor2.getString(a_nIndex));
                group.add(cursor2.getString(b_nIndex));
                group.add(cursor2.getString(c_nIndex));
                group.add(cursor2.getString(a_peIndex));
                group.add(cursor2.getString(b_peIndex));
                group.add(cursor2.getString(c_peIndex));
                group.add(cursor2.getString(n_peIndex));
                group.add(cursor2.getString(conclusionIndex));
                groups.add(group);
                group = new ArrayList<>();
            } while (cursor2.moveToNext());
        }
        cursor2.close();
        templatePDF.createTableInsulation2(rooms, countLines, lines, countGroups, groups);
        templatePDF.addParagraphEnd_Insulation(end, sign);
        templatePDF.closeDocument();
        templatePDF.appViewPDF(InsulationActivity.this);
    }

//    public ArrayAdapter addSpisokLinesPDF(SQLiteDatabase database, int idRoom) {
//        final ArrayList<String> spisokLines = new ArrayList <String>();
//        int countL = 1;
//        Cursor cursor = database.query(DBHelper.TABLE_LINES, new String[] {DBHelper.LN_NAME, DBHelper.LN_HEADER, DBHelper.LN_EMPTY_STRINGS}, "lnr_id = ?", new String[] {String.valueOf(idRoom)}, null, null, null);
//        if (cursor.moveToFirst()) {
//            int nameIndex = cursor.getColumnIndex(DBHelper.LN_NAME);
//            int stringsIndex = cursor.getColumnIndex(DBHelper.LN_EMPTY_STRINGS);
//            int headersIndex = cursor.getColumnIndex(DBHelper.LN_HEADER);
//            do {
//                spisokLines.add(countL++ + ". " + cursor.getString(nameIndex) + "\n(Шапка: " + cursor.getString(headersIndex) + ", Строки: " + cursor.getString(stringsIndex) + ")");
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        return new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, spisokLines);
//    }
//
//    public ArrayAdapter addSpisokRoomsPDF(SQLiteDatabase database) {
//        final ArrayList<String> spisokRooms = new ArrayList <String>();
//        int countR = 1;
//        Cursor cursor = database.query(DBHelper.TABLE_LINE_ROOMS, new String[] {DBHelper.LNR_NAME, DBHelper.LNR_HEADER, DBHelper.LNR_EMPTY_STRINGS}, null, null, null, null, null);
//        if (cursor.moveToFirst()) {
//            int nameIndex = cursor.getColumnIndex(DBHelper.LNR_NAME);
//            int stringsIndex = cursor.getColumnIndex(DBHelper.LNR_EMPTY_STRINGS);
//            int headersIndex = cursor.getColumnIndex(DBHelper.LNR_HEADER);
//            do {
//                spisokRooms.add(countR++ + ". " + cursor.getString(nameIndex) + "\n(Шапка: " + cursor.getString(headersIndex) + ", Строки: " + cursor.getString(stringsIndex) + ")");
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        return new ArrayAdapter<>(this, R.layout.list_item, spisokRooms);
//    }

    public void addSpisokRooms(SQLiteDatabase database, ListView rooms) {
        final ArrayList<String> spisokRooms = new ArrayList <String>();
        Cursor cursor = database.query(DBHelper.TABLE_LINE_ROOMS, new String[] {DBHelper.LNR_NAME}, null, null, null, null, "_id DESC");
        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(DBHelper.LNR_NAME);
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
}
