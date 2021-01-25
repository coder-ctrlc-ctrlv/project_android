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

public class AutomaticsActivity1 extends AppCompatActivity {

    DBHelper dbHelper;
    private TemplatePDF templatePDF;

    String[] date = {"Дата проведения проверки «", "   ", "» ", "           ", " ", "       ", " г."};
    String[] months = {"января", "февраля", "марта", "апреля", "мая", "июня", "июля", "августа", "сентября", "октября", "ноября", "декабря"};
    String zag = "Климатические условия при проведении измерений";
    String[] uslovia = {"Температура воздуха ", "UNDER", "\u00b0C. Влажность воздуха ", "UNDER", "%. Атмосферное давление ", "UNDER", " мм.рт.ст.(бар)."};
    String zag2 = "Нормативные и технические документы, на соответствие требованиям которых проведена проверка:";
    String line1 = "                                                                                                        ";
    String line2 = "ПУЭ п. 1.8.37 п.3; ГОСТ Р 50345-2010";
    String[] sign = {"Испытание провели:", "", "", "", "", "",
            "", "( Должность )", "", "( подпись )", "", "( Ф.И.О. )",
            "Проверил:", "", "", "", "", "",
            "", "( Должность )", "", "( подпись )", "", "( Ф.И.О. )"};
    private String[] header = {"№\nп/п", "Место установки", "Обозначение по схеме", "Наименование\nаппарата, тип,\nкаталожный или\nсерийный номер",
            "Типы\nрасцепителей", "перегрузки", "короткого\nзамыкания", "Заданная выдержка\nвремени отключения при\nК.З, с", "Номинальный ток\nаппарата, А",
            "Номинальный ток\nрасцепителя, А", "Уставка\nрасцепителей", "Проверка расцепителя", "перегрузки, А", "короткого\nзамыкания, А",
            "перегрузки", "испытательный\nток, А", "Время\nсрабатывания, с", "допус-\nтимое", "изме-\nренное", "короткого замыкания",
            "длительность\nприложения\nиспытательного\nтока, с", "ток\nсрабатывания\nрасцепителя, А", "время\nсрабатывания\nрасцепителя, с",
            "Вывод о соответствии\nнормативному документу"};
    private String[] end = {"      Обозначения:\n", "       1.   Типы расцепителей:\n       1.1. ОВВ", " - максимальный расцепитель тока с обратно-зависимой выдержкой времени\n",
            "       1.2. НВВ", " - максимальный расцепитель тока с независимой выдержкой времени\n",
            "       1.3. МД", " - максимальный расцепитель тока мгновенного действия\n",
            "       1.4. B, C, D и т.д.", " - тип мгновенного расцепления по ГОСТ Р 50345-2010",
            "Заключение: ", "   Характеристики автоматических выключателей соответствуют требованиям ПУЭ п. 1.8.37 п.3; ГОСТ Р 50345-2010"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automatics1);

        dbHelper = new DBHelper(this);
        final SQLiteDatabase database = dbHelper.getWritableDatabase();

        final ListView floors = findViewById(R.id.floors);
        Button addFloor = findViewById(R.id.button9);
        Button pdf = findViewById(R.id.button8);
        Button back_btn = findViewById(R.id.button10);

        //НАСТРАИВАЕМ ACTIONBAR
        getSupportActionBar().setSubtitle("Этажи");
        getSupportActionBar().setTitle("Автомат. выключатели");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ЗАПРОС В БД И ЗАПОЛНЕНИЕ СПИСКА ЭТАЖЕЙ
        addSpisokFloors(database, floors);

        //ДОБАВИТЬ ЭТАЖ
        addFloor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(AutomaticsActivity1.this);
                final View myView = getLayoutInflater().inflate(R.layout.dialog_for_marks,null);
                alert.setCancelable(false);
                alert.setTitle("Введите название этажа:");
                final AutoCompleteTextView input = myView.findViewById(R.id.autoCompleteTextView3);
                ImageView arrow = myView.findViewById(R.id.imageView4);
                //ОТОБРАЖЕНИЕ ВЫПЛЫВАЮЩЕГО СПИСКА
                ArrayAdapter<String>adapter1 = new ArrayAdapter<String>(AutomaticsActivity1.this, android.R.layout.simple_dropdown_item_1line, getFloors(database));
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
                        contentValues.put(DBHelper.AU_FL_NAME, nameFloor);
                        database.insert(DBHelper.TABLE_AU_FLOORS, null, contentValues);
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
                Cursor cursor4 = database.query(DBHelper.TABLE_AU_FLOORS, new String[] {DBHelper.AU_FL_ID}, null, null, null, null, "_id DESC");
                cursor4.moveToPosition(position);
                int idFloorIndex = cursor4.getColumnIndex(DBHelper.AU_FL_ID);
                final int idFloor = cursor4.getInt(idFloorIndex);
                cursor4.close();

                if (((TextView) view).getText().toString().equals("БЕЗ ЭТАЖА")) {
                    Intent intent = new Intent("android.intent.action.Automatics2");
                    intent.putExtra("nameFloor", ((TextView) view).getText().toString());
                    intent.putExtra("idFloor", idFloor);
                    startActivity(intent);
                }
                else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(AutomaticsActivity1.this);
                    alert.setTitle(((TextView) view).getText());
                    String arrayMenu[] = {"\nПерейти к комнатам\n", "\nИзменить название\n", "\nУдалить этаж\n"};
                    alert.setItems(arrayMenu, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //ПЕРЕЙТИ К КОМНАТАМ
                            if (which == 0) {
                                Intent intent = new Intent("android.intent.action.Automatics2");
                                intent.putExtra("nameFloor", ((TextView) view).getText().toString());
                                intent.putExtra("idFloor", idFloor);
                                startActivity(intent);
                            }

                            //ИЗМЕНИТЬ НАЗВАНИЕ
                            if (which == 1) {
                                AlertDialog.Builder alert1 = new AlertDialog.Builder(AutomaticsActivity1.this);
                                final View myView = getLayoutInflater().inflate(R.layout.dialog_for_marks, null);
                                alert1.setCancelable(false);
                                alert1.setTitle("Введите новое название этажа:");
                                final AutoCompleteTextView input = myView.findViewById(R.id.autoCompleteTextView3);
                                ImageView arrow = myView.findViewById(R.id.imageView4);
                                input.setText(((TextView) view).getText().toString());
                                //ОТОБРАЖЕНИЕ ВЫПЛЫВАЮЩЕГО СПИСКА
                                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(AutomaticsActivity1.this, android.R.layout.simple_dropdown_item_1line, getFloors(database));
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
                                        uppname.put(DBHelper.AU_FL_NAME, namefl);
                                        database.update(DBHelper.TABLE_AU_FLOORS,
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
                                AlertDialog.Builder builder4 = new AlertDialog.Builder(AutomaticsActivity1.this);
                                builder4.setCancelable(false);
                                builder4.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        Cursor cursor1;
                                        Cursor cursor = database.query(DBHelper.TABLE_AU_ROOMS, new String[]{DBHelper.AU_ROOM_ID, DBHelper.AU_ID_RFLOOR}, "au_rfl_id = ?", new String[]{String.valueOf(idFloor)}, null, null, null);
                                        if (cursor.moveToFirst()) {
                                            int room_IdIndex = cursor.getColumnIndex(DBHelper.AU_ROOM_ID);
                                            do {
                                                cursor1 = database.query(DBHelper.TABLE_AU_LINES, new String[]{DBHelper.AU_LINE_ID, DBHelper.AU_ID_LROOM}, "au_lroom_id = ?", new String[]{cursor.getString(room_IdIndex)}, null, null, null);
                                                if (cursor1.moveToFirst()) {
                                                    int line_idIndex = cursor1.getColumnIndex(DBHelper.AU_LINE_ID);
                                                    do {
                                                        database.delete(DBHelper.TABLE_AUTOMATICS, "auline_id = ?", new String[]{cursor1.getString(line_idIndex)});
                                                    } while (cursor1.moveToNext());
                                                }
                                                cursor1.close();
                                                database.delete(DBHelper.TABLE_AU_LINES, "au_lroom_id = ?", new String[]{cursor.getString(room_IdIndex)});
                                            } while (cursor.moveToNext());
                                        }
                                        cursor.close();
                                        database.delete(DBHelper.TABLE_AU_ROOMS, "au_rfl_id = ?", new String[]{String.valueOf(idFloor)});
                                        database.delete(DBHelper.TABLE_AU_FLOORS, "_id = ?", new String[]{String.valueOf(idFloor)});
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
            public void onClick(View v) {
                //ПРОСТО ОТКРЫТЬ ИЛИ С СОХРАНЕНИЕМ?
                AlertDialog.Builder builder1 = new AlertDialog.Builder(AutomaticsActivity1.this);
                builder1.setPositiveButton("Посмотреть", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //ПРОСТО ПОСМОТРЕТЬ
                        opPFD(database, null);
                    }
                });
                builder1.setNegativeButton("Сохранить", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //ЗАПРАШИВАЕМ НАЗВАНИЕ ФАЙЛА
                        AlertDialog.Builder alert = new AlertDialog.Builder(AutomaticsActivity1.this);
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
                Intent intent = new Intent(AutomaticsActivity1.this, MenuItemsActivity.class);
                startActivity(intent);
            }
        });
    }

    //НАЗАД
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(AutomaticsActivity1.this, MenuItemsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
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

    public void start(String namefile, SQLiteDatabase database) {
        getDateAndUsl(database);
        templatePDF = new TemplatePDF(getApplicationContext());
        templatePDF.openDocument(namefile, true);
        templatePDF.addMetaData("Protokol", "Item", "Company");
        templatePDF.addTitles("РЕЗУЛЬТАТЫ", "проверки автоматических выключателей напряжением до 1000 В", 12);
        templatePDF.addDate(date, 10);
        templatePDF.addCenter_BD(zag, 12,0,5);
        templatePDF.addClimate(uslovia, 10);
        templatePDF.addCenter_BD(zag2, 12,7,5);
        templatePDF.addCenter_Under(line1 + line2 + line1, 10,0,5);
        templatePDF.createTableAutomatics(header);
    }

    //ОТКРЫТИЕ PDF
    public void opPFD(SQLiteDatabase database, String namefile) {
        if (namefile == null)
            namefile = "TepmlatePDF";
        start(namefile, database);

        String concl_false;
        //СПИСОК ЭТАЖЕЙ И КОЛ-ВО КОМНАТ
        ArrayList<String> floors = new ArrayList<>();
        ArrayList<Integer> countRooms = new ArrayList<>();
        //СПИСОК КОМНАТ И КОЛ-ВО ЩИТОВ
        ArrayList<String> rooms = new ArrayList<>();
        ArrayList<Integer> countLines = new ArrayList<>();
        //СПИСОК ЩИТОВ И КОЛ-ВО АВТОМАТОВ
        ArrayList<String> lines = new ArrayList<>();
        ArrayList<Integer> countAutomatics = new ArrayList<>();
        //СПИСОК АВТОМАТОВ
        ArrayList<String> automatic = new ArrayList<>();
        ArrayList<ArrayList> automatics = new ArrayList<>();

        //ЗАПРОС НА СПИСОК ЭТАЖЕЙ И КОЛ-ВО КОМНАТ
        Cursor cursor = database.rawQuery("SELECT au_floor, count(*) AS count_rooms FROM au_floors AS f " +
                "JOIN au_rooms AS r ON f._id = r.au_rfl_id AND r._id IN (" +
                "SELECT au_lroom_id FROM au_lines AS l JOIN automatics AS a ON l._id = a.auline_id GROUP BY au_lroom_id" +
                ") GROUP BY f._id ORDER BY f._id, r._id", new String[] { });
        if (cursor.moveToFirst()) {
            int nameFloorIndex = cursor.getColumnIndex(DBHelper.AU_FL_NAME);
            int countRoomIndex = cursor.getColumnIndex("count_rooms");
            do {
                floors.add(cursor.getString(nameFloorIndex));
                countRooms.add(cursor.getInt(countRoomIndex));
            } while (cursor.moveToNext());
        }
        cursor.close();

        //ЗАПРОС НА СПИСОК КОМНАТ И КОЛ-ВО ЩИТОВ
        Cursor cursor1 = database.rawQuery("SELECT au_room, count(*) AS count_lines FROM au_rooms AS r " +
                "JOIN au_lines AS l ON r._id = l.au_lroom_id AND l._id IN (" +
                "SELECT auline_id FROM automatics GROUP BY auline_id" +
                ") GROUP BY r._id ORDER BY r.au_rfl_id, r._id, l._id", new String[] { });
        if (cursor1.moveToFirst()) {
            int nameRoomIndex = cursor1.getColumnIndex(DBHelper.AU_ROOM_NAME);
            int countLineIndex = cursor1.getColumnIndex("count_lines");
            do {
                rooms.add(cursor1.getString(nameRoomIndex));
                countLines.add(cursor1.getInt(countLineIndex));
            } while (cursor1.moveToNext());
        }
        cursor1.close();

        //ЗАПРОС НА СПИСОК ЩИТОВ И КОЛ-ВО АВТОМАТОВ
        Cursor cursor2 = database.rawQuery("SELECT au_line, count(*) as count_automatics FROM au_rooms AS r JOIN au_lines AS l ON r._id = l.au_lroom_id " +
                "JOIN automatics AS a ON l._id = a.auline_id GROUP by l._id ORDER by r.au_rfl_id, r._id, l._id, a._id", new String[] { });
        if (cursor2.moveToFirst()) {
            int nameLineIndex = cursor2.getColumnIndex(DBHelper.AU_LINE_NAME);
            int countAutomaticsIndex = cursor2.getColumnIndex("count_automatics");
            do {
                lines.add(cursor2.getString(nameLineIndex));
                countAutomatics.add(cursor2.getInt(countAutomaticsIndex));
            } while (cursor2.moveToNext());
        }
        cursor2.close();

        //ЗАПРОС НА СПИСОК АВТОМАТОВ
        Cursor cursor3 = database.rawQuery("SELECT place, symbol_scheme, automatic, type_overload, " +
                "type_kz, excerpt, nominal_1, nominal_2, set_overload, set_kz, test_tok, time_permissible, " +
                "time_measured, length_annex, tok_work, time_work, conclusion FROM automatics AS a " +
                "JOIN au_lines AS l ON a.auline_id = l._id " +
                "JOIN au_rooms AS r ON l.au_lroom_id = r._id " +
                "ORDER by r.au_rfl_id, r._id, l._id, a._id", new String[] { });
        if (cursor3.moveToFirst()) {
            int placeIndex = cursor3.getColumnIndex(DBHelper.AU_PLACE);
            int symbolIndex = cursor3.getColumnIndex(DBHelper.AU_SYMBOL_SCHEME);
            int nameIndex = cursor3.getColumnIndex(DBHelper.AU_NAME);
            int type_overloadIndex = cursor3.getColumnIndex(DBHelper.AU_TYPE_OVERLOAD);
            int type_kzIndex = cursor3.getColumnIndex(DBHelper.AU_TYPE_KZ);
            int excerptIndex = cursor3.getColumnIndex(DBHelper.AU_EXCERPT);
            int nom1Index = cursor3.getColumnIndex(DBHelper.AU_NOMINAL_1);
            int nom2Index = cursor3.getColumnIndex(DBHelper.AU_NOMINAL_2);
            int set_overloadIndex = cursor3.getColumnIndex(DBHelper.AU_SET_OVERLOAD);
            int set_kzIndex = cursor3.getColumnIndex(DBHelper.AU_SET_KZ);
            int test_tokIndex = cursor3.getColumnIndex(DBHelper.AU_TEST_TOK);
            int time_permissibleIndex = cursor3.getColumnIndex(DBHelper.AU_TIME_PERMISSIBLE);
            int time_measuredIndex = cursor3.getColumnIndex(DBHelper.AU_TIME_MEASURED);
            int length_annexIndex = cursor3.getColumnIndex(DBHelper.AU_LENGTH_ANNEX);
            int tok_workIndex = cursor3.getColumnIndex(DBHelper.AU_TOK_WORK);
            int time_workIndex = cursor3.getColumnIndex(DBHelper.AU_TIME_WORK);
            int conclusionIndex = cursor3.getColumnIndex(DBHelper.AU_CONCLUSION);
            do {
                automatic.add(cursor3.getString(placeIndex));
                automatic.add(cursor3.getString(symbolIndex));
                automatic.add(cursor3.getString(nameIndex));
                automatic.add(cursor3.getString(type_overloadIndex));
                automatic.add(cursor3.getString(type_kzIndex));
                automatic.add(cursor3.getString(excerptIndex));
                automatic.add(cursor3.getString(nom1Index));
                automatic.add(cursor3.getString(nom2Index));
                automatic.add(cursor3.getString(set_overloadIndex));
                automatic.add(cursor3.getString(set_kzIndex));
                automatic.add(cursor3.getString(test_tokIndex));
                automatic.add(cursor3.getString(time_permissibleIndex));
                automatic.add(cursor3.getString(time_measuredIndex));
                automatic.add(cursor3.getString(length_annexIndex));
                automatic.add(cursor3.getString(tok_workIndex));
                automatic.add(cursor3.getString(time_workIndex));
                automatic.add(cursor3.getString(conclusionIndex));
                automatics.add(automatic);
                automatic = new ArrayList<>();
            } while (cursor3.moveToNext());
        }
        cursor3.close();
        concl_false = getConclFalse(floors, countRooms, countLines, countAutomatics, automatics);
        if (!concl_false.equals(""))
            end[10] = end[10] + ",  за исключением  " + concl_false;
        templatePDF.createTableAutomatics2(floors, countRooms, rooms, countLines, lines, countAutomatics, automatics);
        templatePDF.addParagraphEnd_AU(end, sign);
        templatePDF.closeDocument();
        templatePDF.appViewPDF(AutomaticsActivity1.this);
    }

    //ПОЛУЧЕНИЕ ПУНКТОВ, НЕ СООТВЕТСВ. НОРМ. ДОКУМЕНТАМ
    String getConclFalse(ArrayList<String> f, ArrayList<Integer> cRooms, ArrayList<Integer> cLines,
                         ArrayList<Integer> cAutomatics, ArrayList<ArrayList> a){
        ArrayList<String> array_conclF = new ArrayList<>();
        String str_conclF = "";
        int i, j, k, q, indexRoom = 0, indexLine = 0, indexAutomat = 0;
        for (i = 1; i <= f.size(); i++) {
            for (j = 1; j <= cRooms.get(i - 1); j++) {
                for (k = 1; k <= cLines.get(indexRoom); k++) {
                    for (q = 1; q <= cAutomatics.get(indexLine); q++) {
                        if (a.get(indexAutomat).get(16).equals("не соотв."))
                            array_conclF.add("п." + String.valueOf(indexRoom + 1) + "." + String.valueOf(k) + ". " + a.get(indexAutomat).get(1));
                        ++indexAutomat;
                    }
                    ++indexLine;
                }
                ++indexRoom;
            }
        }
        if (!array_conclF.isEmpty())
            str_conclF = TextUtils.join("; ", array_conclF);
        return str_conclF;
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
        Cursor cursor = database.query(DBHelper.TABLE_AU_FLOORS, new String[] {DBHelper.AU_FL_NAME}, null, null, null, null, "_id DESC");
        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(DBHelper.AU_FL_NAME);
            do {
                spisokFloors.add(cursor.getString(nameIndex));
            } while (cursor.moveToNext());
        }
        else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.AU_FL_NAME, "БЕЗ ЭТАЖА");
            database.insert(DBHelper.TABLE_AU_FLOORS, null, contentValues);
            spisokFloors.add("БЕЗ ЭТАЖА");
        }
        cursor.close();
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, spisokFloors);
        rooms.setAdapter(adapter);
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