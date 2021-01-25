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

public class DifAutomaticsActivity1 extends AppCompatActivity {

    DBHelper dbHelper;
    private TemplatePDF templatePDF;

    String[] date = {"Дата проведения проверки «", "   ", "» ", "           ", " ", "       ", " г."};
    String[] months = {"января", "февраля", "марта", "апреля", "мая", "июня", "июля", "августа", "сентября", "октября", "ноября", "декабря"};
    String zag = "Климатические условия при проведении измерений";
    String[] uslovia = {"Температура воздуха ", "UNDER", "\u00b0C. Влажность воздуха ", "UNDER", "%. Атмосферное давление ", "UNDER", " мм.рт.ст.(бар)."};
    String zag2 = "Нормативные и технические документы, на соответствие требованиям которых проведены измерения (испытания)";
    String line1 = "                                                                                                        ";
    String line2 = "ПУЭ 1.8.37 п.5, 1.7.79; ГОСТ Р МЭК 60755-2012";
    String[] sign = {"Испытание провели:", "", "", "", "", "",
            "", "( Должность )", "", "( подпись )", "", "( Ф.И.О. )",
            "Проверил:", "", "", "", "", "",
            "", "( Должность )", "", "( подпись )", "", "( Ф.И.О. )"};
    private String[] header = {"№\nп/п", "Место\nустановки", "Тип\nУЗО", "Тип автомат.\nвыключателя",
            "Uф,\n(В)", "Уставки\nрасцепителей\nавт. выкл.", "Проверка авт.\nвыключателя", "Устройство защитного отключения",
            "тепл.\nрасцеп.,\n(А)", "эл.маг.\nрасцеп.,\n(А)", "испыт.\nток,\n(А)", "время\nср.теп.\nрасц.,\n(сек.)", "ток\nсраб.эл.\nмаг.расцеп.\n(А)", "I ном.\n(мА)",
            "I утеч.\n(мА)", "I сраб.\nзащ.,\nдоп.\n(мА)", "I сраб.\nзащ.,\nизмер.\n(мА)", "время\nсраб.\nдоп.,\n(с)", "время\nсраб.\nизмер.,\n(с)",
            "Выводы\nо соответствии\nнормативному\nдокументу"};
    private String[] end = {"Заключение: ",
            "   Устройства дифференциальной защиты с функцией автоматического выключателя соответствует  ПУЭ 1.8.37 п.5, 1.7.79;",
            "   ГОСТ Р МЭК 60755-2012"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dif_automatics1);

        dbHelper = new DBHelper(this);
        final SQLiteDatabase database = dbHelper.getWritableDatabase();

        final ListView floors = findViewById(R.id.floors);
        Button addFloor = findViewById(R.id.button9);
        Button pdf = findViewById(R.id.button8);
        Button back_btn = findViewById(R.id.button10);

        //НАСТРАИВАЕМ ACTIONBAR
        getSupportActionBar().setSubtitle("Этажи");
        getSupportActionBar().setTitle("Диф. автоматы");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ЗАПРОС В БД И ЗАПОЛНЕНИЕ СПИСКА ЭТАЖЕЙ
        addSpisokFloors(database, floors);

        //ДОБАВИТЬ ЭТАЖ
        addFloor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(DifAutomaticsActivity1.this);
                final View myView = getLayoutInflater().inflate(R.layout.dialog_for_marks,null);
                alert.setCancelable(false);
                alert.setTitle("Введите название этажа:");
                final AutoCompleteTextView input = myView.findViewById(R.id.autoCompleteTextView3);
                ImageView arrow = myView.findViewById(R.id.imageView4);
                //ОТОБРАЖЕНИЕ ВЫПЛЫВАЮЩЕГО СПИСКА
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(DifAutomaticsActivity1.this, android.R.layout.simple_dropdown_item_1line, getFloors(database));
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
                        contentValues.put(DBHelper.DIF_AU_FL_NAME, nameFloor);
                        database.insert(DBHelper.TABLE_DIF_AU_FLOORS, null, contentValues);
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
                Cursor cursor4 = database.query(DBHelper.TABLE_DIF_AU_FLOORS, new String[] {DBHelper.DIF_AU_FL_ID}, null, null, null, null, "_id DESC");
                cursor4.moveToPosition(position);
                int idFloorIndex = cursor4.getColumnIndex(DBHelper.DIF_AU_FL_ID);
                final int idFloor = cursor4.getInt(idFloorIndex);
                cursor4.close();

                if (((TextView) view).getText().toString().equals("БЕЗ ЭТАЖА")) {
                    Intent intent = new Intent("android.intent.action.DifAutomatics2");
                    intent.putExtra("nameFloor", ((TextView) view).getText().toString());
                    intent.putExtra("idFloor", idFloor);
                    startActivity(intent);
                }
                else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(DifAutomaticsActivity1.this);
                    alert.setTitle(((TextView) view).getText());
                    String arrayMenu[] = {"\nПерейти к комнатам\n", "\nИзменить название\n", "\nУдалить этаж\n"};
                    alert.setItems(arrayMenu, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //ПЕРЕЙТИ К КОМНАТАМ
                            if (which == 0) {
                                Intent intent = new Intent("android.intent.action.DifAutomatics2");
                                intent.putExtra("nameFloor", ((TextView) view).getText().toString());
                                intent.putExtra("idFloor", idFloor);
                                startActivity(intent);
                            }

                            //ИЗМЕНИТЬ НАЗВАНИЕ
                            if (which == 1) {
                                AlertDialog.Builder alert1 = new AlertDialog.Builder(DifAutomaticsActivity1.this);
                                final View myView = getLayoutInflater().inflate(R.layout.dialog_for_marks, null);
                                alert1.setCancelable(false);
                                alert1.setTitle("Введите новое название этажа:");
                                final AutoCompleteTextView input = myView.findViewById(R.id.autoCompleteTextView3);
                                ImageView arrow = myView.findViewById(R.id.imageView4);
                                input.setText(((TextView) view).getText().toString());
                                //ОТОБРАЖЕНИЕ ВЫПЛЫВАЮЩЕГО СПИСКА
                                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(DifAutomaticsActivity1.this, android.R.layout.simple_dropdown_item_1line, getFloors(database));
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
                                        uppname.put(DBHelper.DIF_AU_FL_NAME, namefl);
                                        database.update(DBHelper.TABLE_DIF_AU_FLOORS,
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
                                AlertDialog.Builder builder4 = new AlertDialog.Builder(DifAutomaticsActivity1.this);
                                builder4.setCancelable(false);
                                builder4.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        Cursor cursor1;
                                        Cursor cursor = database.query(DBHelper.TABLE_DIF_AU_ROOMS, new String[]{DBHelper.DIF_AU_ROOM_ID, DBHelper.DIF_AU_ID_RFLOOR}, "dif_au_rfl_id = ?", new String[]{String.valueOf(idFloor)}, null, null, null);
                                        if (cursor.moveToFirst()) {
                                            int room_IdIndex = cursor.getColumnIndex(DBHelper.DIF_AU_ROOM_ID);
                                            do {
                                                cursor1 = database.query(DBHelper.TABLE_DIF_AU_LINES, new String[]{DBHelper.DIF_AU_LINE_ID, DBHelper.DIF_AU_ID_LROOM}, "dif_au_lroom_id = ?", new String[]{cursor.getString(room_IdIndex)}, null, null, null);
                                                if (cursor1.moveToFirst()) {
                                                    int line_idIndex = cursor1.getColumnIndex(DBHelper.DIF_AU_LINE_ID);
                                                    do {
                                                        database.delete(DBHelper.TABLE_DIF_AUTOMATICS, "dif_auline_id = ?", new String[]{cursor1.getString(line_idIndex)});
                                                    } while (cursor1.moveToNext());
                                                }
                                                cursor1.close();
                                                database.delete(DBHelper.TABLE_DIF_AU_LINES, "dif_au_lroom_id = ?", new String[]{cursor.getString(room_IdIndex)});
                                            } while (cursor.moveToNext());
                                        }
                                        cursor.close();
                                        database.delete(DBHelper.TABLE_DIF_AU_ROOMS, "dif_au_rfl_id = ?", new String[]{String.valueOf(idFloor)});
                                        database.delete(DBHelper.TABLE_DIF_AU_FLOORS, "_id = ?", new String[]{String.valueOf(idFloor)});
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
                AlertDialog.Builder builder1 = new AlertDialog.Builder(DifAutomaticsActivity1.this);
                builder1.setPositiveButton("Посмотреть", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //ПРОСТО ПОСМОТРЕТЬ
                        opPFD(database, null);
                    }
                });
                builder1.setNegativeButton("Сохранить", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //ЗАПРАШИВАЕМ НАЗВАНИЕ ФАЙЛА
                        AlertDialog.Builder alert = new AlertDialog.Builder(DifAutomaticsActivity1.this);
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
                Intent intent = new Intent(DifAutomaticsActivity1.this, MenuItemsActivity.class);
                startActivity(intent);
            }
        });
    }

    //НАЗАД
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(DifAutomaticsActivity1.this, MenuItemsActivity.class);
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
        templatePDF.addTitles("РЕЗУЛЬТАТЫ", "проверки устройства дифференциальной защиты с функцией автоматического выключателя", 12);
        templatePDF.addDate(date, 10);
        templatePDF.addCenter_BD(zag, 12,0,5);
        templatePDF.addClimate(uslovia, 10);
        templatePDF.addCenter_BD(zag2, 12,7,5);
        templatePDF.addCenter_Under(line1 + line2 + line1, 10,0,5);
        templatePDF.createTableDifAutomatics(header);
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
        ArrayList<String> dif_automatic = new ArrayList<>();
        ArrayList<ArrayList> dif_automatics = new ArrayList<>();

        //ЗАПРОС НА СПИСОК ЭТАЖЕЙ И КОЛ-ВО КОМНАТ
        Cursor cursor = database.rawQuery("SELECT dif_au_floor, count(*) AS count_rooms FROM dif_au_floors AS f " +
                "JOIN dif_au_rooms AS r ON f._id = r.dif_au_rfl_id AND r._id IN (" +
                "SELECT dif_au_lroom_id FROM dif_au_lines AS l JOIN dif_automatics AS a ON l._id = a.dif_auline_id GROUP BY dif_au_lroom_id" +
                ") GROUP BY f._id ORDER BY f._id, r._id", new String[] { });
        if (cursor.moveToFirst()) {
            int nameFloorIndex = cursor.getColumnIndex(DBHelper.DIF_AU_FL_NAME);
            int countRoomIndex = cursor.getColumnIndex("count_rooms");
            do {
                floors.add(cursor.getString(nameFloorIndex));
                countRooms.add(cursor.getInt(countRoomIndex));
            } while (cursor.moveToNext());
        }
        cursor.close();

        //ЗАПРОС НА СПИСОК КОМНАТ И КОЛ-ВО ЩИТОВ
        Cursor cursor1 = database.rawQuery("SELECT dif_au_room, count(*) AS count_lines FROM dif_au_rooms AS r " +
                "JOIN dif_au_lines AS l ON r._id = l.dif_au_lroom_id AND l._id IN (" +
                "SELECT dif_auline_id FROM dif_automatics GROUP BY dif_auline_id" +
                ") GROUP BY r._id ORDER BY r.dif_au_rfl_id, r._id, l._id", new String[] { });
        if (cursor1.moveToFirst()) {
            int nameRoomIndex = cursor1.getColumnIndex(DBHelper.DIF_AU_ROOM_NAME);
            int countLineIndex = cursor1.getColumnIndex("count_lines");
            do {
                rooms.add(cursor1.getString(nameRoomIndex));
                countLines.add(cursor1.getInt(countLineIndex));
            } while (cursor1.moveToNext());
        }
        cursor1.close();

        //ЗАПРОС НА СПИСОК ЩИТОВ И КОЛ-ВО АВТОМАТОВ
        Cursor cursor2 = database.rawQuery("SELECT dif_au_line, count(*) as count_automatics FROM dif_au_rooms AS r JOIN dif_au_lines AS l ON r._id = l.dif_au_lroom_id " +
                "JOIN dif_automatics AS a ON l._id = a.dif_auline_id GROUP by l._id ORDER by r.dif_au_rfl_id, r._id, l._id, a._id", new String[] { });
        if (cursor2.moveToFirst()) {
            int nameLineIndex = cursor2.getColumnIndex(DBHelper.DIF_AU_LINE_NAME);
            int countAutomaticsIndex = cursor2.getColumnIndex("count_automatics");
            do {
                lines.add(cursor2.getString(nameLineIndex));
                countAutomatics.add(cursor2.getInt(countAutomaticsIndex));
            } while (cursor2.moveToNext());
        }
        cursor2.close();

        //ЗАПРОС НА СПИСОК АВТОМАТОВ
        Cursor cursor3 = database.rawQuery("SELECT place, uzo, type_switch, " +
                "u, set_thermal, set_electromagn, check_test_tok, check_time, check_work_tok, i_nom, i_leack, " +
                "i_extra, i_measured, time_extra, time_measured, conclusion FROM dif_automatics AS a " +
                "JOIN dif_au_lines AS l ON a.dif_auline_id = l._id " +
                "JOIN dif_au_rooms AS r ON l.dif_au_lroom_id = r._id " +
                "ORDER by r.dif_au_rfl_id, r._id, l._id, a._id", new String[] { });
        if (cursor3.moveToFirst()) {
            int placeIndex = cursor3.getColumnIndex(DBHelper.DIF_AU_PLACE);
            int uzoIndex = cursor3.getColumnIndex(DBHelper.DIF_AU_UZO);
            int type_switchIndex = cursor3.getColumnIndex(DBHelper.DIF_AU_TYPE_SWITCH);
            int uIndex = cursor3.getColumnIndex(DBHelper.DIF_AU_U);
            int set_thermalIndex = cursor3.getColumnIndex(DBHelper.DIF_AU_SET_THERMAL);
            int set_electromagnIndex = cursor3.getColumnIndex(DBHelper.DIF_AU_SET_ELECTR_MAGN);
            int check_test_tokIndex = cursor3.getColumnIndex(DBHelper.DIF_AU_CHECK_TEST_TOK);
            int check_timeIndex = cursor3.getColumnIndex(DBHelper.DIF_AU_CHECK_TIME_);
            int check_work_tokIndex = cursor3.getColumnIndex(DBHelper.DIF_AU_CHECK_WORK_TOK);
            int i_nomIndex = cursor3.getColumnIndex(DBHelper.DIF_AU_I_NOM);
            int i_leackIndex = cursor3.getColumnIndex(DBHelper.DIF_AU_I_LEAK);
            int i_extraIndex = cursor3.getColumnIndex(DBHelper.DIF_AU_I_EXTRA);
            int i_measuredIndex = cursor3.getColumnIndex(DBHelper.DIF_AU_I_MEASURED);
            int time_extraIndex = cursor3.getColumnIndex(DBHelper.DIF_AU_TIME_EXTRA);
            int time_measuredIndex = cursor3.getColumnIndex(DBHelper.DIF_AU_TIME_MEASURED);
            int conclusionIndex = cursor3.getColumnIndex(DBHelper.DIF_AU_CONCLUSION);
            do {
                dif_automatic.add(cursor3.getString(placeIndex));
                dif_automatic.add(cursor3.getString(uzoIndex));
                dif_automatic.add(cursor3.getString(type_switchIndex));
                dif_automatic.add(cursor3.getString(uIndex));
                dif_automatic.add(cursor3.getString(set_thermalIndex));
                dif_automatic.add(cursor3.getString(set_electromagnIndex));
                dif_automatic.add(cursor3.getString(check_test_tokIndex));
                dif_automatic.add(cursor3.getString(check_timeIndex));
                dif_automatic.add(cursor3.getString(check_work_tokIndex));
                dif_automatic.add(cursor3.getString(i_nomIndex));
                dif_automatic.add(cursor3.getString(i_leackIndex));
                dif_automatic.add(cursor3.getString(i_extraIndex));
                dif_automatic.add(cursor3.getString(i_measuredIndex));
                dif_automatic.add(cursor3.getString(time_extraIndex));
                dif_automatic.add(cursor3.getString(time_measuredIndex));
                dif_automatic.add(cursor3.getString(conclusionIndex));
                dif_automatics.add(dif_automatic);
                dif_automatic = new ArrayList<>();
            } while (cursor3.moveToNext());
        }
        cursor3.close();
        concl_false = getConclFalse(floors, countRooms, countLines, countAutomatics, dif_automatics);
        if (!concl_false.equals(""))
            end[2] = end[2] + ",  за исключением  " + concl_false;
        templatePDF.createTableDifAutomatics2(floors, countRooms, rooms, countLines, lines, countAutomatics, dif_automatics);
        templatePDF.addParagraphEnd_DAU(end, sign);
        templatePDF.closeDocument();
        templatePDF.appViewPDF(DifAutomaticsActivity1.this);
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
                        if (a.get(indexAutomat).get(15).equals("не соответств."))
                            array_conclF.add("п." + String.valueOf(indexRoom + 1) + "." + String.valueOf(k) + ". " + a.get(indexAutomat).get(0));
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
        Cursor cursor = database.query(DBHelper.TABLE_DIF_AU_FLOORS, new String[] {DBHelper.DIF_AU_FL_NAME}, null, null, null, null, "_id DESC");
        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(DBHelper.DIF_AU_FL_NAME);
            do {
                spisokFloors.add(cursor.getString(nameIndex));
            } while (cursor.moveToNext());
        }
        else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.DIF_AU_FL_NAME, "БЕЗ ЭТАЖА");
            database.insert(DBHelper.TABLE_DIF_AU_FLOORS, null, contentValues);
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
