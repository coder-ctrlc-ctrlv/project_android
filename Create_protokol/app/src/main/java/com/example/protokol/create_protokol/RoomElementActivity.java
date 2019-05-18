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
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RoomElementActivity extends AppCompatActivity {

    private DBHelper dbHelper;

    //ЗАГОЛОВКИ
    private String[]header = {"№ п/п", "Месторасположение и наименование электрооборудования", "Кол-во проверенных элементов", "R перех. допустимое, (Ом)", "R перех. измеренное, (Ом)", "Вывод о\nсоответствии нормативному документу"};
    private String date = "Дата проведения проверки «__» ___________ _______г. ";
    private String zag = "Климатические условия при проведении измерений";
    private String uslovia = "Температура воздуха __С. Влажность воздуха __%. Атмосферное давление ___ мм.рт.ст.(бар).";
    private String zag2 = "Нормативные и технические документы, на соответствие требованиям которых проведена проверка:";
    private String line1 = "                                                                       ";
    private String line2 = "ПУЭ 1.8.39 п.2; ПТЭЭП Приложение 3";

    //ЗАКЛЮЧЕНИЕ
    private String zakl = "Заключение:";
    private String proverka = "Проверку провели:   _____________________     ___________    _____________" + "\n" +
                              "                                           (Должность)                    (Подпись)          (Ф.И.О.)" + "\n" + "\n" +
                              "Проверил:                 _____________________     ___________    _____________" + "\n" +
                              "                                           (Должность)                    (Подпись)          (Ф.И.О.)";
    private TemplatePDF templatePDF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_element);

        dbHelper = new DBHelper(this);
        final SQLiteDatabase database = dbHelper.getWritableDatabase();

        final ListView rooms = findViewById(R.id.rooms);
        Button addRoom = findViewById(R.id.button9);
        Button pdf = findViewById(R.id.button8);
        Button optionsPdf = findViewById(R.id.button10);

        //НАСТРАИВАЕМ ACTIONBAR
        getSupportActionBar().setSubtitle("Комнаты");
        getSupportActionBar().setTitle("Металлосвязь");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ЗАПРОС В БД И ЗАПОЛНЕНИЕ СПИСКА КОМНАТ
        addSpisokRooms(database, rooms);

        //ДОБАВИТЬ КОМНАТУ
        addRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(RoomElementActivity.this);
                final View myView = getLayoutInflater().inflate(R.layout.dialog_for_names,null);
                alert.setCancelable(false);
                alert.setTitle("Введите название комнаты:");
                final EditText input = myView.findViewById(R.id.editText);
                //ОТКРЫВАЕМ КЛАВИАТУРУ
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        final String nameRoom = input.getText().toString();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(DBHelper.KEY_NAME, nameRoom);
                        contentValues.put(DBHelper.KEY_HEADER, "-");
                        contentValues.put(DBHelper.KEY_EMPTY_STRINGS, 0);
                        database.insert(DBHelper.TABLE_ROOMS, null, contentValues);
                        //СКРЫВАЕМ КЛАВИАТУРУ
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(myView.getWindowToken(),0);
                        //ЗАПРОС В БД И ЗАПОЛНЕНИЕ СПИСКА КОМНАТ
                        addSpisokRooms(database, rooms);
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

        //ПЕРЕХОД К ЭЛЕМЕНТАМ И РЕДАКТОР КОМНАТЫ
        rooms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, final long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(RoomElementActivity.this);
                alert.setTitle(((TextView) view).getText());
                String arrayMenu[] = {"\nПерейти к элементам\n", "\nИзменить название\n", "\nУдалить комнату\n"};
                alert.setItems(arrayMenu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //ЗАПРОС В БД ДЛЯ ПОЛУЧЕНИЯ ID НУЖНОЙ КОМНАТЫ
                        Cursor cursor4 = database.query(DBHelper.TABLE_ROOMS, new String[] {DBHelper.KEY_ID}, null, null, null, null, null);
                        cursor4.moveToPosition(position);
                        int idRoomIndex = cursor4.getColumnIndex(DBHelper.KEY_ID);
                        final int idRoom = cursor4.getInt(idRoomIndex);
                        cursor4.close();

                        //ПЕРЕЙТИ К ЭЛЕМЕНТАМ
                        if (which == 0) {
                            Intent intent = new Intent("android.intent.action.RoomElement2");
                            intent.putExtra("nameRoom", ((TextView) view).getText().toString());
                            intent.putExtra("idRoom", idRoom);
                            startActivity(intent);
                        }

                        //ИЗМЕНИТЬ НАЗВАНИЕ
                        if (which == 1) {
                            AlertDialog.Builder alert1 = new AlertDialog.Builder(RoomElementActivity.this);
                            final View myView = getLayoutInflater().inflate(R.layout.dialog_for_names,null);
                            alert1.setCancelable(false);
                            alert1.setTitle("Введите новое название комнаты:");
                            final EditText input = myView.findViewById(R.id.editText);
                            //ОТКРЫВАЕМ КЛАВИАТУРУ
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                            alert1.setPositiveButton("Изменить", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    String namer = input.getText().toString();
                                    ContentValues uppname = new ContentValues();
                                    uppname.put(DBHelper.KEY_NAME, namer);
                                    database.update(DBHelper.TABLE_ROOMS,
                                            uppname,
                                            "_id = ?",
                                            new String[] {String.valueOf(idRoom)});
                                    //СКРЫВАЕМ КЛАВИАТУРУ
                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(myView.getWindowToken(),0);
                                    //ЗАПРОС В БД И ЗАПОЛНЕНИЕ СПИСКА КОМНАТ
                                    addSpisokRooms(database, rooms);
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
                            AlertDialog.Builder builder4 = new AlertDialog.Builder(RoomElementActivity.this);
                            builder4.setCancelable(false);
                            builder4.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    database.delete(DBHelper.TABLE_ELEMENTS, "room_id = ?", new String[] {String.valueOf(idRoom)});
                                    database.delete(DBHelper.TABLE_ROOMS, "_id = ?", new String[] {String.valueOf(idRoom)});
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

        //ОТКРЫТЬ PDF
        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //ПРОСТО ОТКРЫТЬ ИЛИ С СОХРАНЕНИЕМ?
                AlertDialog.Builder builder1 = new AlertDialog.Builder(RoomElementActivity.this);
                builder1.setPositiveButton("Посмотреть", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //ПРОСТО ПОСМОТРЕТЬ
                        opPFD(database, null);
                    }
                });
                builder1.setNegativeButton("Сохранить", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //ЗАПРАШИВАЕМ НАЗВАНИЕ ФАЙЛА
                        AlertDialog.Builder alert = new AlertDialog.Builder(RoomElementActivity.this);
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
        optionsPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                options(database);
            }
        });
    }

    //НАЗАД
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(RoomElementActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //ДОБАВЛЕНИЕ В PDF ЗАГОЛОВКОВ
    public void start (String namefile) {
        templatePDF = new TemplatePDF(getApplicationContext());
        templatePDF.openDocument(namefile, false);
        templatePDF.addMetaData("Protokol", "Item", "Company");
        templatePDF.addTitles("РЕЗУЛЬТАТЫ", "проверки наличия цепи между заземленными установками и элементами заземленной установки", 12);
        templatePDF.addParagraph_Normal(date, 10,5,5);
        templatePDF.addCenter_BD(zag, 12, 0,5);
        templatePDF.addCenter_Nomal(uslovia, 10,7,5);
        templatePDF.addCenter_BD(zag2, 12,7,5);
        templatePDF.addCenter_Under(line1 + line2 + line1, 10,0,5);
        templatePDF.createTableRE(header);
    }

    //НАСТРОЙКА ПДФ С РЕКУРСИЕЙ
    public void options(final SQLiteDatabase database) {
        AlertDialog.Builder alert = new AlertDialog.Builder(RoomElementActivity.this);
        alert.setCancelable(false);
        alert.setTitle("Выберете комнату:");
        alert.setAdapter(addSpisokRoomsPDF(database), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //ЗАПРОС В БД ДЛЯ ПОЛУЧЕНИЯ НУЖНОЙ КОМНАТЫ
                Cursor cursor4 = database.query(DBHelper.TABLE_ROOMS, new String[] {DBHelper.KEY_ID, DBHelper.KEY_NAME, DBHelper.KEY_HEADER, DBHelper.KEY_EMPTY_STRINGS}, null, null, null, null, null);
                cursor4.moveToPosition(which);
                int r_idIndex = cursor4.getColumnIndex(DBHelper.KEY_ID);
                int roomNameIndex = cursor4.getColumnIndex(DBHelper.KEY_NAME);
                int headerIndex = cursor4.getColumnIndex(DBHelper.KEY_HEADER);
                int stringsIndex = cursor4.getColumnIndex(DBHelper.KEY_EMPTY_STRINGS);
                final int r_id = cursor4.getInt(r_idIndex);
                int strings = cursor4.getInt(stringsIndex);
                String header = cursor4.getString(headerIndex);
                String nameRoom = cursor4.getString(roomNameIndex);
                cursor4.close();

                AlertDialog.Builder alert1 = new AlertDialog.Builder(RoomElementActivity.this);
                final View myView = getLayoutInflater().inflate(R.layout.dialog_for_options_pdf, null);
                alert1.setCancelable(false);
                alert1.setTitle(nameRoom);
                final CheckBox head = myView.findViewById(R.id.checkBox2);
                Button subtract = myView.findViewById(R.id.button2);
                Button add = myView.findViewById(R.id.button3);
                final EditText numb = myView.findViewById(R.id.editText4);

                //ЗАПОЛНЕНИЕ
                if (header.equals("-"))
                    head.setChecked(false);
                else
                    head.setChecked(true);
                numb.setText(String.valueOf(strings));

                //- ПУСТАЯ СТРОКА
                subtract.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Integer.parseInt(numb.getText().toString()) > 0)
                            numb.setText(String.valueOf(Integer.parseInt(numb.getText().toString()) - 1));
                    }
                });

                //+ ПУСТАЯ СТРОКА
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        numb.setText(String.valueOf(Integer.parseInt(numb.getText().toString()) + 1));
                    }
                });

                alert1.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //СОБИРАЕМ ДАННЫЕ
                        int strings = Integer.parseInt(numb.getText().toString());
                        String header;
                        if (head.isChecked())
                            header = "eсть";
                        else
                            header = "-";

                        //ИЗМЕНЯЕМ ЗАГОЛОВОК
                        ContentValues upphead = new ContentValues();
                        upphead.put(DBHelper.KEY_HEADER, header);
                        database.update(DBHelper.TABLE_ROOMS,
                                upphead,
                                "_id = ?",
                                new String[] {String.valueOf(r_id)});

                        //ИЗМЕНЯЕМ КОЛ-ВО ПУСТЫХ СТРОК
                        ContentValues uppstrings = new ContentValues();
                        uppstrings.put(DBHelper.KEY_EMPTY_STRINGS, strings);
                        database.update(DBHelper.TABLE_ROOMS,
                                uppstrings,
                                "_id = ?",
                                new String[] {String.valueOf(r_id)});
                        options(database);
                        Toast toast1 = Toast.makeText(getApplicationContext(),
                                "Данные сохранены", Toast.LENGTH_SHORT);
                        toast1.show();
                    }
                });
                alert1.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        options(database);
                    }
                });
                alert1.setView(myView);
                alert1.show();
            }
        });
        alert.setPositiveButton("Закрыть", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        alert.show();
    }

    public ArrayAdapter addSpisokRoomsPDF(SQLiteDatabase database) {
        final ArrayList<String> spisokRooms = new ArrayList <String>();
        int countR = 1;
        Cursor cursor = database.query(DBHelper.TABLE_ROOMS, new String[] {DBHelper.KEY_NAME, DBHelper.KEY_HEADER, DBHelper.KEY_EMPTY_STRINGS}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int stringsIndex = cursor.getColumnIndex(DBHelper.KEY_EMPTY_STRINGS);
            int headersIndex = cursor.getColumnIndex(DBHelper.KEY_HEADER);
            do {
                spisokRooms.add(countR++ + ". " + cursor.getString(nameIndex) + "\n(Шапка: " + cursor.getString(headersIndex) + ", Строки: " + cursor.getString(stringsIndex) + ")");
            } while (cursor.moveToNext());
        }
        cursor.close();
        return new ArrayAdapter<>(this, R.layout.list_item, spisokRooms);
    }

    public void addSpisokRooms(SQLiteDatabase database, ListView rooms) {
        final ArrayList<String> spisokRooms = new ArrayList <String>();
        Cursor cursor = database.query(DBHelper.TABLE_ROOMS, new String[] {DBHelper.KEY_NAME}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            do {
                spisokRooms.add(cursor.getString(nameIndex));
            } while (cursor.moveToNext());
        }
        cursor.close();
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, spisokRooms);
        rooms.setAdapter(adapter);
    }

    //ОТКРЫТИЕ PDF
    public void opPFD(SQLiteDatabase database, String namefile) {
        ArrayList<String> element = new ArrayList<>();
        ArrayList<ArrayList> elements = new ArrayList<>();
        final ArrayList<String> NZ = new ArrayList<>();
        if (namefile == null)
            namefile = "TepmlatePDF";
        start(namefile);
        String conclusion;
        boolean headBool;
        int roomPrev = -1, r_id, currentElement = 0, currentRoom = 0;
        //ПОЛУЧЕНИЕ ВСЕХ ДАННЫХ ИЗ БД И ЗАПОЛНЕНИЕ ТАБЛИЦЫ
        Cursor cursor = database.rawQuery("select * from rooms as r join elements as e on e.room_id = r._id order by e.room_id", new String[] { });
        if (cursor.moveToFirst()) {
            int roomnameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int headIndex = cursor.getColumnIndex(DBHelper.KEY_HEADER);
            int stringsIndex = cursor.getColumnIndex(DBHelper.KEY_EMPTY_STRINGS);
            int elidroomIndex = cursor.getColumnIndex(DBHelper.ROOM_ID);
            int elnameIndex = cursor.getColumnIndex(DBHelper.EL_NAME);
            int elnumberIndex = cursor.getColumnIndex(DBHelper.EL_NUMBER);
            int elsoprIndex = cursor.getColumnIndex(DBHelper.EL_SOPR);
            int conclusionIndex = cursor.getColumnIndex(DBHelper.EL_CONCLUSION);
            do {
                r_id = cursor.getInt(elidroomIndex);
                conclusion = cursor.getString(conclusionIndex);
                //ЕСЛИ ВСТРЕТИЛАСЬ НОВАЯ КОМНАТА
                if ((r_id != roomPrev)){
                    templatePDF.addElementRE(elements);
                    elements = new ArrayList<>();
                    currentElement = 0;
                    currentRoom++;
                    roomPrev = r_id;
                    headBool = !cursor.getString(headIndex).equals("-");
                    templatePDF.addRoomRE(cursor.getString(roomnameIndex), String.valueOf(currentRoom) + ". ", headBool, cursor.getInt(stringsIndex));
                }
                currentElement++;
                //ПОДСЧИТЫВАЕМ Н.З.
                if (conclusion.equals("не соответствует"))
                    NZ.add(String.valueOf(currentRoom) + "." + String.valueOf(currentElement));
                element.add(String.valueOf(currentElement) + ".");
                element.add(" " + cursor.getString(elnameIndex));
                element.add(cursor.getString(elnumberIndex));
                element.add("0,05");
                element.add(cursor.getString(elsoprIndex));
                element.add(conclusion);
                elements.add(element);
                element = new ArrayList<>();
            } while (cursor.moveToNext());
            templatePDF.addElementRE(elements);
            templatePDF.emptyStringsRE(1);
        }
        cursor.close();
        String joinedNZ;
        if (!NZ.isEmpty())
            joinedNZ = TextUtils.join("; ", NZ);
        else
            joinedNZ = "нет";
        String end1 = "a) Проверена целостность и прочность проводников заземления и зануления, переходные контакты их соединений, болтовые соединения проверены на затяжку, сварные – ударом молотка." + "\n" +
                "b) Сопротивление переходных контактов выше нормы, указаны в п/п ";
        String end2 = "\n" + "c) Не заземлено оборудование, указанное в п/п ";
        String end3 = "\n" + "d) Величина измеренного переходного сопротивления прочих контактов заземляющих и нулевых проводников,  элементов электрооборудования соответствует (не соответствует) нормам ";
        templatePDF.addCenter_BD(zakl, 12, 7, 5);
        templatePDF.addParagraph_RE_end(end1,"        нет        ", end2,"        " + joinedNZ + "        ", end3,"                          ",10);
        templatePDF.addParagraph_Normal(proverka, 10,5,5);
        templatePDF.closeDocument();
        templatePDF.appViewPDF(RoomElementActivity.this);
    }
}
