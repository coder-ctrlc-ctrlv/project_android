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

public class InsulationActivity1 extends AppCompatActivity {

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
        setContentView(R.layout.activity_insulation1);

        dbHelper = new DBHelper(this);
        final SQLiteDatabase database = dbHelper.getWritableDatabase();

        final ListView floors = findViewById(R.id.floors);
        Button addFloor = findViewById(R.id.button9);
        Button addNote = findViewById(R.id.button11);
        Button pdf = findViewById(R.id.button8);
        Button optionsPdf = findViewById(R.id.button10);

        //НАСТРАИВАЕМ ACTIONBAR
        getSupportActionBar().setSubtitle("Этажи");
        getSupportActionBar().setTitle("Изоляция");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ЗАПРОС В БД И ЗАПОЛНЕНИЕ СПИСКА ЭТАЖЕЙ
        addSpisokFloors(database, floors);

        //ДОБАВИТЬ ЭТАЖ
        addFloor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity1.this);
                final View myView = getLayoutInflater().inflate(R.layout.dialog_for_marks,null);
                alert.setCancelable(false);
                alert.setTitle("Введите название этажа:");
                final AutoCompleteTextView input = myView.findViewById(R.id.autoCompleteTextView3);
                ImageView arrow = myView.findViewById(R.id.imageView4);
                //ОТОБРАЖЕНИЕ ВЫПЛЫВАЮЩЕГО СПИСКА
                ArrayAdapter<String>adapter1 = new ArrayAdapter<String>(InsulationActivity1.this, android.R.layout.simple_dropdown_item_1line, getFloors(database));
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
                        contentValues.put(DBHelper.INS_FL_NAME, nameFloor);
                        database.insert(DBHelper.TABLE_INS_FLOORS, null, contentValues);
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

        //ДОБАВИТЬ ПРИМЕЧАНИЕ
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = "Отсутствует";
                Cursor cursor1 = database.query(DBHelper.TABLE_INS_NOTES, new String[] {DBHelper.INS_NOTE}, null, null, null, null, null);
                if (cursor1.moveToFirst()) {
                    int noteIndex = cursor1.getColumnIndex(DBHelper.INS_NOTE);
                    message = cursor1.getString(noteIndex);
                }
                cursor1.close();

                AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity1.this);
                alert.setCancelable(false);
                alert.setTitle("Текущее примечание:");
                alert.setMessage("\n" + message);
                alert.setPositiveButton("Ввести", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        AlertDialog.Builder alert1 = new AlertDialog.Builder(InsulationActivity1.this);
                        final View myView = getLayoutInflater().inflate(R.layout.dialog_for_names,null);
                        alert1.setCancelable(false);
                        alert1.setTitle("Введите примечание:");
                        final EditText input = myView.findViewById(R.id.editText);
                        openKeyboard();
                        alert1.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                closeKeyboard(myView);
                                String note = input.getText().toString();
                                if (!note.equals("")) {
                                    database.delete(DBHelper.TABLE_INS_NOTES, null, null);
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put(DBHelper.INS_NOTE, note);
                                    database.insert(DBHelper.TABLE_INS_NOTES, null, contentValues);
                                    Toast toast1 = Toast.makeText(getApplicationContext(),
                                            "Примечание добавлено", Toast.LENGTH_SHORT);
                                    toast1.show();
                                }
                                else {
                                    Toast toast1 = Toast.makeText(getApplicationContext(),
                                            "Поле ввода оказалось пустым", Toast.LENGTH_SHORT);
                                    toast1.show();
                                }
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
                });
                if (!message.equals("Отсутствует")) {
                    alert.setNeutralButton("Удалить", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            database.delete(DBHelper.TABLE_INS_NOTES, null, null);
                            Toast toast1 = Toast.makeText(getApplicationContext(),
                                    "Примечание удалено", Toast.LENGTH_SHORT);
                            toast1.show();
                        }
                    });
                }
                alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                alert.show();
            }
        });

        //ПЕРЕХОД К КОМНАТАМ И РЕДАКТОР ЭТАЖА
        floors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, final long id) {

                //ЗАПРОС В БД ДЛЯ ПОЛУЧЕНИЯ ID НУЖНОГО ЭТАЖА
                Cursor cursor4 = database.query(DBHelper.TABLE_INS_FLOORS, new String[] {DBHelper.INS_FL_ID}, null, null, null, null, "_id DESC");
                cursor4.moveToPosition(position);
                int idFloorIndex = cursor4.getColumnIndex(DBHelper.INS_FL_ID);
                final int idFloor = cursor4.getInt(idFloorIndex);
                cursor4.close();

                if (((TextView) view).getText().toString().equals("БЕЗ ЭТАЖА")) {
                    Intent intent = new Intent("android.intent.action.Insulation2");
                    intent.putExtra("nameFloor", ((TextView) view).getText().toString());
                    intent.putExtra("idFloor", idFloor);
                    startActivity(intent);
                }
                else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity1.this);
                    alert.setTitle(((TextView) view).getText());
                    String arrayMenu[] = {"\nПерейти к комнатам\n", "\nИзменить название\n", "\nУдалить этаж\n"};
                    alert.setItems(arrayMenu, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //ПЕРЕЙТИ К КОМНАТАМ
                            if (which == 0) {
                                Intent intent = new Intent("android.intent.action.Insulation2");
                                intent.putExtra("nameFloor", ((TextView) view).getText().toString());
                                intent.putExtra("idFloor", idFloor);
                                startActivity(intent);
                            }

                            //ИЗМЕНИТЬ НАЗВАНИЕ
                            if (which == 1) {
                                AlertDialog.Builder alert1 = new AlertDialog.Builder(InsulationActivity1.this);
                                final View myView = getLayoutInflater().inflate(R.layout.dialog_for_marks, null);
                                alert1.setCancelable(false);
                                alert1.setTitle("Введите новое название этажа:");
                                final AutoCompleteTextView input = myView.findViewById(R.id.autoCompleteTextView3);
                                ImageView arrow = myView.findViewById(R.id.imageView4);
                                input.setText(((TextView) view).getText().toString());
                                //ОТОБРАЖЕНИЕ ВЫПЛЫВАЮЩЕГО СПИСКА
                                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(InsulationActivity1.this, android.R.layout.simple_dropdown_item_1line, getFloors(database));
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
                                        uppname.put(DBHelper.INS_FL_NAME, namefl);
                                        database.update(DBHelper.TABLE_INS_FLOORS,
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
                                AlertDialog.Builder builder4 = new AlertDialog.Builder(InsulationActivity1.this);
                                builder4.setCancelable(false);
                                builder4.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        Cursor cursor1;
                                        Cursor cursor = database.query(DBHelper.TABLE_LINE_ROOMS, new String[]{DBHelper.LNR_ID, DBHelper.INS_ID_RFLOOR}, "ins_rfl_id = ?", new String[]{String.valueOf(idFloor)}, null, null, null);
                                        if (cursor.moveToFirst()) {
                                            int room_IdIndex = cursor.getColumnIndex(DBHelper.LNR_ID);
                                            do {
                                                cursor1 = database.query(DBHelper.TABLE_LINES, new String[]{DBHelper.LN_ID, DBHelper.LN_ID_ROOM}, "lnr_id = ?", new String[]{cursor.getString(room_IdIndex)}, null, null, null);
                                                if (cursor1.moveToFirst()) {
                                                    int line_idIndex = cursor1.getColumnIndex(DBHelper.LN_ID);
                                                    do {
                                                        database.delete(DBHelper.TABLE_GROUPS, "grline_id = ?", new String[]{cursor1.getString(line_idIndex)});
                                                    } while (cursor1.moveToNext());
                                                }
                                                cursor1.close();
                                                database.delete(DBHelper.TABLE_LINES, "lnr_id = ?", new String[]{cursor.getString(room_IdIndex)});
                                            } while (cursor.moveToNext());
                                        }
                                        cursor.close();
                                        database.delete(DBHelper.TABLE_LINE_ROOMS, "ins_rfl_id = ?", new String[]{String.valueOf(idFloor)});
                                        database.delete(DBHelper.TABLE_INS_FLOORS, "_id = ?", new String[]{String.valueOf(idFloor)});
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

        //ОТКРЫТИЕ PDF
        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ПРОСТО ОТКРЫТЬ ИЛИ С СОХРАНЕНИЕМ?
                AlertDialog.Builder builder1 = new AlertDialog.Builder(InsulationActivity1.this);
                builder1.setPositiveButton("Посмотреть", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //ПРОСТО ПОСМОТРЕТЬ
                        opPFD(database, null);
                    }
                });
                builder1.setNegativeButton("Сохранить", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //ЗАПРАШИВАЕМ НАЗВАНИЕ ФАЙЛА
                        AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity1.this);
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

        //НАСТРОЙКА PDF
        optionsPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    //НАЗАД
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(InsulationActivity1.this, MainActivity.class);
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
        //СПИСОК ЩИТОВ И КОЛ-ВО ГРУПП
        ArrayList<String> lines = new ArrayList<>();
        ArrayList<Integer> countGroups = new ArrayList<>();
        //СПИСОК ГРУПП
        ArrayList<String> group = new ArrayList<>();
        ArrayList<ArrayList> groups = new ArrayList<>();

        //ЗАПРОС НА СПИСОК ЭТАЖЕЙ И КОЛ-ВО КОМНАТ
        Cursor cursor = database.rawQuery("SELECT ins_floor, count(*) AS count_rooms FROM ins_floors AS f " +
                "JOIN lnrooms AS r ON f._id = r.ins_rfl_id GROUP BY f._id ORDER BY f._id, r._id", new String[] { });
        if (cursor.moveToFirst()) {
            int nameFloorIndex = cursor.getColumnIndex(DBHelper.INS_FL_NAME);
            int countRoomIndex = cursor.getColumnIndex("count_rooms");
            do {
                floors.add(cursor.getString(nameFloorIndex));
                countRooms.add(cursor.getInt(countRoomIndex));
            } while (cursor.moveToNext());
        }
        cursor.close();

        //ЗАПРОС НА СПИСОК КОМНАТ И КОЛ-ВО ЩИТОВ
        Cursor cursor1 = database.rawQuery("SELECT room, count(*) AS count_lines FROM lnrooms AS r " +
                "JOIN `lines` AS l ON r._id = l.lnr_id GROUP BY r._id ORDER BY r.ins_rfl_id, r._id, l._id", new String[] { });
        if (cursor1.moveToFirst()) {
            int nameRoomIndex = cursor1.getColumnIndex(DBHelper.LNR_NAME);
            int countLineIndex = cursor1.getColumnIndex("count_lines");
            do {
                rooms.add(cursor1.getString(nameRoomIndex));
                countLines.add(cursor1.getInt(countLineIndex));
            } while (cursor1.moveToNext());
        }
        cursor1.close();

        //ЗАПРОС НА СПИСОК ЩИТОВ И КОЛ-ВО ГРУПП
        Cursor cursor2 = database.rawQuery("SELECT line, count(*) as count_groups FROM lnrooms AS r JOIN `lines` AS l ON r._id = l.lnr_id " +
                "JOIN groups AS g ON l._id = g.grline_id GROUP by l._id ORDER by r.ins_rfl_id, r._id, l._id, g._id", new String[] { });
        if (cursor2.moveToFirst()) {
            int nameLineIndex = cursor2.getColumnIndex(DBHelper.LN_NAME);
            int countGroupIndex = cursor2.getColumnIndex("count_groups");
            do {
                lines.add(cursor2.getString(nameLineIndex));
                countGroups.add(cursor2.getInt(countGroupIndex));
            } while (cursor2.moveToNext());
        }
        cursor2.close();

        //ЗАПРОС НА СПИСОК ГРУПП
        Cursor cursor3 = database.rawQuery("SELECT name_group, u1, mark, vein, section, " +
                "u2, r, phase, a_b, b_c, c_a, a_n, b_n, c_n, a_pe, b_pe, c_pe, n_pe, conclusion FROM groups AS g " +
                "JOIN `lines` AS l ON g.grline_id = l._id " +
                "JOIN lnrooms AS r ON l.lnr_id = r._id " +
                "ORDER by r.ins_rfl_id, r._id, l._id, g._id", new String[] { });
        if (cursor3.moveToFirst()) {
            int nameGroupIndex = cursor3.getColumnIndex(DBHelper.GR_NAME);
            int markIndex = cursor3.getColumnIndex(DBHelper.GR_MARK);
            int veinIndex = cursor3.getColumnIndex(DBHelper.GR_VEIN);
            int sectionIndex = cursor3.getColumnIndex(DBHelper.GR_SECTION);
            int workUIndex = cursor3.getColumnIndex(DBHelper.GR_U1);
            int uIndex = cursor3.getColumnIndex(DBHelper.GR_U2);
            int rIndex = cursor3.getColumnIndex(DBHelper.GR_R);
            int a_bIndex = cursor3.getColumnIndex(DBHelper.GR_A_B);
            int b_cIndex = cursor3.getColumnIndex(DBHelper.GR_B_C);
            int c_aIndex = cursor3.getColumnIndex(DBHelper.GR_C_A);
            int a_nIndex = cursor3.getColumnIndex(DBHelper.GR_A_N);
            int b_nIndex = cursor3.getColumnIndex(DBHelper.GR_B_N);
            int c_nIndex = cursor3.getColumnIndex(DBHelper.GR_C_N);
            int a_peIndex = cursor3.getColumnIndex(DBHelper.GR_A_PE);
            int b_peIndex = cursor3.getColumnIndex(DBHelper.GR_B_PE);
            int c_peIndex = cursor3.getColumnIndex(DBHelper.GR_C_PE);
            int n_peIndex = cursor3.getColumnIndex(DBHelper.GR_N_PE);
            int conclusionIndex = cursor3.getColumnIndex(DBHelper.GR_CONCLUSION);
            String vein;
            do {
                vein = cursor3.getString(veinIndex);
                group.add(cursor3.getString(nameGroupIndex));
                group.add(cursor3.getString(workUIndex));
                group.add(cursor3.getString(markIndex));
                if (vein.equals("-"))
                    group.add("-");
                else
                    group.add(vein + "x" + cursor3.getString(sectionIndex));
                group.add(cursor3.getString(uIndex));
                group.add(cursor3.getString(rIndex));
                group.add(cursor3.getString(a_bIndex));
                group.add(cursor3.getString(b_cIndex));
                group.add(cursor3.getString(c_aIndex));
                group.add(cursor3.getString(a_nIndex));
                group.add(cursor3.getString(b_nIndex));
                group.add(cursor3.getString(c_nIndex));
                group.add(cursor3.getString(a_peIndex));
                group.add(cursor3.getString(b_peIndex));
                group.add(cursor3.getString(c_peIndex));
                group.add(cursor3.getString(n_peIndex));
                group.add(cursor3.getString(conclusionIndex));
                groups.add(group);
                group = new ArrayList<>();
            } while (cursor3.moveToNext());
        }
        cursor3.close();
        concl_false = getConclFalse(floors, countRooms, countLines, countGroups, groups);
        if (!concl_false.equals(""))
            end[3] = end[3] + ",  за искл.  " + concl_false;
        getNotes(database);
        templatePDF.createTableInsulation2(floors, countRooms, rooms, countLines, lines, countGroups, groups);
        templatePDF.addParagraphEnd_Insulation(end, sign);
        templatePDF.closeDocument();
        templatePDF.appViewPDF(InsulationActivity1.this);
    }

    //ПОЛУЧЕНИЕ ПУНКТОВ, НЕ СООТВЕТСВ. НОРМ. ДОКУМЕНТАМ
    String getConclFalse(ArrayList<String> f, ArrayList<Integer> cRooms, ArrayList<Integer> cLines,
                         ArrayList<Integer> cGroups, ArrayList<ArrayList> g){
        ArrayList<String> array_conclF = new ArrayList<>();
        String str_conclF = "";
        int i, j, k, q, indexRoom = 0, indexLine = 0, indexGroup = 0;
        for (i = 1; i <= f.size(); i++) {
            for (j = 1; j <= cRooms.get(i - 1); j++) {
                for (k = 1; k <= cLines.get(indexRoom); k++) {
                    for (q = 1; q <= cGroups.get(indexLine); q++) {
                        if (g.get(indexGroup).get(16).equals("не соотв."))
                            array_conclF.add("п." + String.valueOf(indexRoom + 1) + "." + String.valueOf(k) + ". " + g.get(indexGroup).get(0));
                        ++indexGroup;
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
        Cursor cursor = database.query(DBHelper.TABLE_INS_FLOORS, new String[] {DBHelper.INS_FL_NAME}, null, null, null, null, "_id DESC");
        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(DBHelper.INS_FL_NAME);
            do {
                spisokFloors.add(cursor.getString(nameIndex));
            } while (cursor.moveToNext());
        }
        else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.INS_FL_NAME, "БЕЗ ЭТАЖА");
            database.insert(DBHelper.TABLE_INS_FLOORS, null, contentValues);
            spisokFloors.add("БЕЗ ЭТАЖА");
        }
        cursor.close();
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, spisokFloors);
        rooms.setAdapter(adapter);
    }

    void getNotes(SQLiteDatabase database) {
        end[1] = "                       \u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014";
        Cursor curs10 = database.query(DBHelper.TABLE_INS_NOTES, new String[] {DBHelper.INS_NOTE}, null, null, null, null, null);
        if (curs10.moveToFirst()) {
            int noteIndex = curs10.getColumnIndex(DBHelper.INS_NOTE);
            end[1] = "   " + curs10.getString(noteIndex);
        }
        curs10.close();
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
