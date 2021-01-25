package com.example.protokol.create_protokol;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GroundingDevicesActivity1 extends AppCompatActivity {

    DBHelper dbHelper;
    private TemplatePDF templatePDF;

    String[] date = {"Дата проведения проверки «", "   ", "» ", "           ", " ", "       ", " г."};
    String[] months = {"января", "февраля", "марта", "апреля", "мая", "июня", "июля", "августа", "сентября", "октября", "ноября", "декабря"};
    String zag = "Климатические условия при проведении проверки";
    String[] uslovia = {"Температура воздуха ", "UNDER", "\u00b0C. Влажность воздуха ", "UNDER", "%. Атмосферное давление ", "UNDER", " мм.рт.ст.(бар)."};
    String zag2 = "Нормативные и технические документы, на соответствие требованиям которых проведена проверка:";
    String line1 = "                                                                                                        ";
    String line2 = "ПУЭ 1.8.39 п.5; ПТЭЭП Приложение 3";
    String[] sign = {"Испытание провели:", "", "", "", "", "",
            "", "( Должность )", "", "( подпись )", "", "( Ф.И.О. )",
            "Проверил:", "", "", "", "", "",
            "", "( Должность )", "", "( подпись )", "", "( Ф.И.О. )"};
    String[] header = {"№\nп/п", "Назначение\nзаземлителя,\nзаземляющего\nустройства", "Место\nизмерения", "Расстояние до\nтокового\nэлектрода L, м",
            "Сопротивление заземлителей (заземляющих устройств), Ом", "Допустимое,\nОм", "Измеренное при положениях потенциального электрода\n(расстояние L, м)",
            "0,1 L", "0,2 L", "0,3 L", "0,4 L", "0,5 L", "0,6 L", "0,7 L", "0,8 L", "0,9 L", "Дополнитель-\nные расчеты,\nграфики",
            "Принятое зна-\nчение сопро-\nтивления\nзаземлителя,\nОм", "Коэффициент\nпоправочн. Кп", "Приведенное\nзначение\nсопротивления\nзаземлителя с\nучетом Кп, Ом",
            "Вывод о\nсоответствии\nнормативному\nдокументу"};
    String[] textBeforeTable = {
            "1. Результаты внешнего осмотра (целостности и надёжности заземляющих устройств) ", "UNDER",
            "2. Вид грунта: ", "UNDER",
            "3. Характер грунта: ", "UNDER",
            "                                                                                                          " + "сухой, малой влажности, средней влажности, большой влажности\n",
            "4. Заземляющее устройство применяется для электроустановки: ", "UNDER",
            "5. Режим нейтрали: ", "UNDER", "                                                                                                                      до 1000 В., до и выше 1000В., свыше 1000В\n",
            "6. Удельное сопротивление грунта, Ом х м: ", "UNDER"};
    String[] textForEnd = {"Примечание: ", "UNDER", "Заключение: ", "   Заземляющее устройство соответствует требованию ПУЭ 1.8.39 п.5; ПТЭЭП Приложение 3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grounding_devices1);

        dbHelper = new DBHelper(this);
        final SQLiteDatabase database = dbHelper.getWritableDatabase();

        final ListView devices = findViewById(R.id.Devices);
        Button addDevice = findViewById(R.id.button34);
        Button pdf = findViewById(R.id.button32);
        Button back_btn = findViewById(R.id.button10);

        //НАСТРАИВАЕМ ACTIONBAR
        getSupportActionBar().setSubtitle("Заземлители");
        getSupportActionBar().setTitle("Заземл. и заз. устр-ва");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ЗАПОЛНЯЕМ LISTVIEW
        addSpisokDevices(database, devices);

        //ПОСМОТРЕТЬ, ИЗМЕНИТЬ И УДАЛИТЬ ЗАЗЕМЛИТЕЛЬ
        devices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, final long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(GroundingDevicesActivity1.this);
                alert.setTitle(((TextView) view).getText());
                String arrayMenu[] = {"\nПосмотреть\n", "\nРедактировать\n", "\nУдалить заземлитель\n"};
                alert.setItems(arrayMenu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //ЗАПРОС В БД ДЛЯ ПОЛУЧЕНИЯ ID НУЖНОГО ЗАЗЕМЛИТЕЛЯ
                        Cursor cursor4 = database.query(DBHelper.TABLE_GD, new String[] {DBHelper.GD_DEVICE_ID}, null, null, null, null, null);
                        cursor4.moveToPosition(position);
                        int device_idIndex = cursor4.getColumnIndex(DBHelper.GD_DEVICE_ID);
                        final int device_id = cursor4.getInt(device_idIndex);
                        cursor4.close();

                        //ПОСМОТРЕТЬ
                        if (which == 0) {
                            String info[] = {"","","","","","", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};
                            //ПОЛУЧЕНИЕ ИНФОРМАЦИИ ОБ ЭЛЕМЕНТЕ
                            Cursor cursor1 = database.query(DBHelper.TABLE_GD, new String[] {DBHelper.GD_DEVICE_ID, DBHelper.GD_RESULT_VIEW,
                                    DBHelper.GD_GROUND, DBHelper.GD_CHARACTER_GROUND, DBHelper.GD_U, DBHelper.GD_MODE_NEUTRAL, DBHelper.GD_R, DBHelper.GD_PURPOSE,
                                    DBHelper.GD_PLACE, DBHelper.GD_DISTANCE, DBHelper.GD_R1, DBHelper.GD_01L, DBHelper.GD_02L, DBHelper.GD_03L,
                                    DBHelper.GD_04L, DBHelper.GD_05L, DBHelper.GD_06L, DBHelper.GD_07L, DBHelper.GD_08L, DBHelper.GD_09L,
                                    DBHelper.GD_GRAPHICS, DBHelper.GD_R2, DBHelper.GD_K, DBHelper.GD_R3,
                                    DBHelper.GD_CONCLUSION, DBHelper.GD_NOTE, DBHelper.GD_TEMPERATURE,
                                    DBHelper.GD_HUMIDITY, DBHelper.GD_PRESSURE}, "_id = ?", new String[] {String.valueOf(device_id)}, null, null, null);
                            if (cursor1.moveToFirst()) {
                                int resultViewIndex = cursor1.getColumnIndex(DBHelper.GD_RESULT_VIEW);
                                int groundIndex = cursor1.getColumnIndex(DBHelper.GD_GROUND);
                                int characterIndex = cursor1.getColumnIndex(DBHelper.GD_CHARACTER_GROUND);
                                int uIndex = cursor1.getColumnIndex(DBHelper.GD_U);
                                int modeIndex = cursor1.getColumnIndex(DBHelper.GD_MODE_NEUTRAL);
                                int rIndex = cursor1.getColumnIndex(DBHelper.GD_R);
                                int purposeIndex = cursor1.getColumnIndex(DBHelper.GD_PURPOSE);
                                int placeIndex = cursor1.getColumnIndex(DBHelper.GD_PLACE);
                                int distanceIndex = cursor1.getColumnIndex(DBHelper.GD_DISTANCE);
                                int r1Index = cursor1.getColumnIndex(DBHelper.GD_R1);
                                int l1Index = cursor1.getColumnIndex(DBHelper.GD_01L);
                                int l2Index = cursor1.getColumnIndex(DBHelper.GD_02L);
                                int l3Index = cursor1.getColumnIndex(DBHelper.GD_03L);
                                int l4Index = cursor1.getColumnIndex(DBHelper.GD_04L);
                                int l5Index = cursor1.getColumnIndex(DBHelper.GD_05L);
                                int l6Index = cursor1.getColumnIndex(DBHelper.GD_06L);
                                int l7Index = cursor1.getColumnIndex(DBHelper.GD_07L);
                                int l8Index = cursor1.getColumnIndex(DBHelper.GD_08L);
                                int l9Index = cursor1.getColumnIndex(DBHelper.GD_09L);
                                int graphicsIndex = cursor1.getColumnIndex(DBHelper.GD_GRAPHICS);
                                int r2Index = cursor1.getColumnIndex(DBHelper.GD_R2);
                                int kIndex = cursor1.getColumnIndex(DBHelper.GD_K);
                                int r3Index = cursor1.getColumnIndex(DBHelper.GD_R3);
                                int resultIndex = cursor1.getColumnIndex(DBHelper.GD_CONCLUSION);
                                int noteIndex = cursor1.getColumnIndex(DBHelper.GD_NOTE);
                                int temperatureIndex = cursor1.getColumnIndex(DBHelper.GD_TEMPERATURE);
                                int humidityIndex = cursor1.getColumnIndex(DBHelper.GD_HUMIDITY);
                                int pressureIndex = cursor1.getColumnIndex(DBHelper.GD_PRESSURE);
                                do {
                                    info[0] = cursor1.getString(resultViewIndex);
                                    info[1] = cursor1.getString(groundIndex);
                                    info[2] = cursor1.getString(characterIndex);
                                    info[3] = cursor1.getString(uIndex);
                                    info[4] = cursor1.getString(modeIndex);
                                    info[5] = cursor1.getString(rIndex);
                                    info[6] = cursor1.getString(purposeIndex);
                                    info[7] = cursor1.getString(placeIndex);
                                    info[8] = cursor1.getString(distanceIndex);
                                    info[9] = cursor1.getString(r1Index);
                                    info[10] = cursor1.getString(l1Index);
                                    info[11] = cursor1.getString(l2Index);
                                    info[12] = cursor1.getString(l3Index);
                                    info[13] = cursor1.getString(l4Index);
                                    info[14] = cursor1.getString(l5Index);
                                    info[15] = cursor1.getString(l6Index);
                                    info[16] = cursor1.getString(l7Index);
                                    info[17] = cursor1.getString(l8Index);
                                    info[18] = cursor1.getString(l9Index);
                                    info[19] = cursor1.getString(graphicsIndex);
                                    info[20] = cursor1.getString(r2Index);
                                    info[21] = cursor1.getString(kIndex);
                                    info[22] = cursor1.getString(r3Index);
                                    info[23] = cursor1.getString(resultIndex);
                                    info[24] = cursor1.getString(noteIndex);
                                    info[25] = cursor1.getString(temperatureIndex);
                                    info[26] = cursor1.getString(humidityIndex);
                                    info[27] = cursor1.getString(pressureIndex);
                                } while (cursor1.moveToNext());
                            }
                            cursor1.close();
                            AlertDialog.Builder builder4 = new AlertDialog.Builder(GroundingDevicesActivity1.this);
                            builder4.setCancelable(false);
                            builder4.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                }
                            });
                            builder4.setMessage( Html.fromHtml("<b>Рез-ты внешн. осмотра: </b>" + info[0] + "<br>" + "<b>Вид грунта: </b>" + info[1] + "<br>" +
                                    "<b>Характер грунта: </b>" + info[2] + "<br>" + "<b>Электроустановка: </b>" + info[3] + "<br>" + "<b>Режим нейтрали: </b>" + info[4] + "<br>" +
                                    "<b>Удельное сопротивление грунта: </b>" + info[5] + "<br>" + "<b>Назначение заземлителя: </b>" + info[6] + "<br>" +
                                    "<b>Место измерения: </b>" + info[7] + "<br>" + "<b>Расстояние до токового электрода: </b>" + info[8] + "<br>" +
                                    "<b>Допустимое R: </b>" + info[9] + "<br>" + "<b>0,1 L: </b>" + info[10] + "<br>" + "<b>0,2 L: </b>" + info[11] + "<br>" +
                                    "<b>0,3 L: </b>" + info[12] + "<br>" + "<b>0,4 L: </b>" + info[13] + "<br>" + "<b>0,5 L: </b>" + info[14] + "<br>" +
                                    "<b>0,6 L: </b>" + info[15] + "<br>" + "<b>0,7 L: </b>" + info[16] + "<br>" + "<b>0,8 L: </b>" + info[17] + "<br>" +
                                    "<b>0,9 L: </b>" + info[18] + "<br>" + "<b>Доп. расчеты, графики: </b>" + info[19] + "<br>" + "<b>Принятое R: </b>" + info[20] + "<br>" +
                                    "<b>Коэффициент поправочн.: </b>" + info[21] + "<br>" + "<b>Приведенное R c учетом коэффициента: </b>" + info[22] + "<br>" +
                                    "<b>Вывод: </b>" + info[23] + "<br>" + "<b>Примечание: </b>" + info[24] + "<br>" + "<b>Температура: </b>" + info[25] + "<br>" +
                                    "<b>Влажность: </b>" + info[26] + "<br>" + "<b>Атм.давление: </b>" + info[27] + "<br>"));
                            builder4.setTitle(((TextView) view).getText());
                            AlertDialog dialog4 = builder4.create();
                            dialog4.show();
                        }

                        //ИЗМЕНИТЬ
                        if (which == 1) {
                            Intent intent = new Intent("android.intent.action.GroundingDevices2");
                            intent.putExtra("device_id", device_id);
                            startActivity(intent);
                        }

                        //УДАЛИТЬ ГРУППУ
                        if (which == 2) {

                            //ПОДТВЕРЖДЕНИЕ
                            AlertDialog.Builder builder4 = new AlertDialog.Builder(GroundingDevicesActivity1.this);
                            builder4.setCancelable(false);
                            builder4.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    database.delete(DBHelper.TABLE_GD, "_id = ?", new String[] {String.valueOf(device_id)});
                                    //ЗАПРОС В БД И ЗАПОЛНЕНИЕ СПИСКА ГРУПП
                                    addSpisokDevices(database, devices);
                                    Toast toast2 = Toast.makeText(getApplicationContext(),
                                            "Заземлитель удален", Toast.LENGTH_SHORT);
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

        //ПЕРЕХОД К ДОБАВЛЕНИЮ ЗАЗЕМЛИТЕЛЯ
        addDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.GroundingDevices2");
                startActivity(intent);
            }
        });

        //ОТКРЫТИЕ PDF
        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ПРОСТО ОТКРЫТЬ ИЛИ С СОХРАНЕНИЕМ?
                AlertDialog.Builder builder1 = new AlertDialog.Builder(GroundingDevicesActivity1.this);
                builder1.setPositiveButton("Посмотреть", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //ПРОСТО ПОСМОТРЕТЬ
                        opPFD(database, null);
                    }
                });
                builder1.setNegativeButton("Сохранить", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //ЗАПРАШИВАЕМ НАЗВАНИЕ ФАЙЛА
                        AlertDialog.Builder alert = new AlertDialog.Builder(GroundingDevicesActivity1.this);
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
                Intent intent = new Intent(GroundingDevicesActivity1.this, MenuItemsActivity.class);
                startActivity(intent);
            }
        });
    }

    //НАЗАД
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(GroundingDevicesActivity1.this, MenuItemsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void start(String namefile, SQLiteDatabase database) {
        getDate(database);
        templatePDF = new TemplatePDF(getApplicationContext());
        templatePDF.openDocument(namefile, true);
        templatePDF.addMetaData("Protokol", "Item", "Company");
        templatePDF.addTitles("РЕЗУЛЬТАТЫ", "проверки сопротивлений заземлителей и заземляющих устройств", 12);
        templatePDF.addDate(date, 10);
        templatePDF.addCenter_BD(zag, 12, 0, 5);
        templatePDF.addClimate(uslovia, 10);
        templatePDF.addCenter_BD(zag2, 12, 7, 5);
        templatePDF.addCenter_Under(line1 + line2 + line1, 10,0,5);
        templatePDF.addParagraph_Ground_BeforeTable(textBeforeTable);
        templatePDF.createTableGround(header);
    }

    public void getDate(SQLiteDatabase database) {
        String dateString;
        Cursor cursor1 = database.query(DBHelper.TABLE_TITLE, new String[] {DBHelper.TITLE_DATE}, null, null, null, null, null);
        if (cursor1.moveToFirst()) {
            int dateIndex = cursor1.getColumnIndex(DBHelper.TITLE_DATE);
            dateString = cursor1.getString(dateIndex);
            date[1] = " " + Integer.parseInt(dateString.substring(0,2)) + " ";
            date[3] = " " + months[Integer.parseInt(dateString.substring(4,6)) - 1] + " ";
            date[5] = dateString.substring(8,12);
        }
        cursor1.close();
    }

    public void opPFD(SQLiteDatabase database, String namefile) {
        if (namefile == null)
            namefile = "TepmlatePDF";
        String[] StrTable = {"1", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};
        Cursor cursor1 = database.query(DBHelper.TABLE_GD, new String[] {DBHelper.GD_DEVICE_ID, DBHelper.GD_RESULT_VIEW,
                DBHelper.GD_GROUND, DBHelper.GD_CHARACTER_GROUND, DBHelper.GD_U, DBHelper.GD_MODE_NEUTRAL, DBHelper.GD_R, DBHelper.GD_PURPOSE,
                DBHelper.GD_PLACE, DBHelper.GD_DISTANCE, DBHelper.GD_R1, DBHelper.GD_01L, DBHelper.GD_02L, DBHelper.GD_03L,
                DBHelper.GD_04L, DBHelper.GD_05L, DBHelper.GD_06L, DBHelper.GD_07L, DBHelper.GD_08L, DBHelper.GD_09L,
                DBHelper.GD_GRAPHICS, DBHelper.GD_R2, DBHelper.GD_K, DBHelper.GD_R3,
                DBHelper.GD_CONCLUSION, DBHelper.GD_NOTE, DBHelper.GD_TEMPERATURE,
                DBHelper.GD_HUMIDITY, DBHelper.GD_PRESSURE}, null, null, null, null, null);
        if (cursor1.moveToFirst()) {
            int resultViewIndex = cursor1.getColumnIndex(DBHelper.GD_RESULT_VIEW);
            int groundIndex = cursor1.getColumnIndex(DBHelper.GD_GROUND);
            int characterIndex = cursor1.getColumnIndex(DBHelper.GD_CHARACTER_GROUND);
            int uIndex = cursor1.getColumnIndex(DBHelper.GD_U);
            int modeIndex = cursor1.getColumnIndex(DBHelper.GD_MODE_NEUTRAL);
            int rIndex = cursor1.getColumnIndex(DBHelper.GD_R);
            int purposeIndex = cursor1.getColumnIndex(DBHelper.GD_PURPOSE);
            int placeIndex = cursor1.getColumnIndex(DBHelper.GD_PLACE);
            int distanceIndex = cursor1.getColumnIndex(DBHelper.GD_DISTANCE);
            int r1Index = cursor1.getColumnIndex(DBHelper.GD_R1);
            int l1Index = cursor1.getColumnIndex(DBHelper.GD_01L);
            int l2Index = cursor1.getColumnIndex(DBHelper.GD_02L);
            int l3Index = cursor1.getColumnIndex(DBHelper.GD_03L);
            int l4Index = cursor1.getColumnIndex(DBHelper.GD_04L);
            int l5Index = cursor1.getColumnIndex(DBHelper.GD_05L);
            int l6Index = cursor1.getColumnIndex(DBHelper.GD_06L);
            int l7Index = cursor1.getColumnIndex(DBHelper.GD_07L);
            int l8Index = cursor1.getColumnIndex(DBHelper.GD_08L);
            int l9Index = cursor1.getColumnIndex(DBHelper.GD_09L);
            int graphicsIndex = cursor1.getColumnIndex(DBHelper.GD_GRAPHICS);
            int r2Index = cursor1.getColumnIndex(DBHelper.GD_R2);
            int kIndex = cursor1.getColumnIndex(DBHelper.GD_K);
            int r3Index = cursor1.getColumnIndex(DBHelper.GD_R3);
            int resultIndex = cursor1.getColumnIndex(DBHelper.GD_CONCLUSION);
            int noteIndex = cursor1.getColumnIndex(DBHelper.GD_NOTE);
            int temperatureIndex = cursor1.getColumnIndex(DBHelper.GD_TEMPERATURE);
            int humidityIndex = cursor1.getColumnIndex(DBHelper.GD_HUMIDITY);
            int pressureIndex = cursor1.getColumnIndex(DBHelper.GD_PRESSURE);
            textBeforeTable[1] = "               " + cursor1.getString(resultViewIndex);
            textBeforeTable[3] = "                                              " + cursor1.getString(groundIndex);
            textBeforeTable[5] = "                                    " + cursor1.getString(characterIndex);
            textBeforeTable[8] = "                                                                           " + cursor1.getString(uIndex);
            textBeforeTable[10] = "         " + cursor1.getString(modeIndex) + "                               ";
            textBeforeTable[13] = "             " + cursor1.getString(rIndex) + "                  ";
            StrTable[1] = cursor1.getString(purposeIndex);
            StrTable[2] = cursor1.getString(placeIndex);
            StrTable[3] = cursor1.getString(distanceIndex);
            StrTable[4] = cursor1.getString(r1Index);
            StrTable[5] = cursor1.getString(l1Index);
            StrTable[6] = cursor1.getString(l2Index);
            StrTable[7] = cursor1.getString(l3Index);
            StrTable[8] = cursor1.getString(l4Index);
            StrTable[9] = cursor1.getString(l5Index);
            StrTable[10] = cursor1.getString(l6Index);
            StrTable[11] = cursor1.getString(l7Index);
            StrTable[12] = cursor1.getString(l8Index);
            StrTable[13] = cursor1.getString(l9Index);
            StrTable[14] = cursor1.getString(graphicsIndex);
            StrTable[15] = cursor1.getString(r2Index);
            StrTable[16] = cursor1.getString(kIndex);
            StrTable[17] = cursor1.getString(r3Index);
            StrTable[18] = cursor1.getString(resultIndex);
            uslovia[1] = " " + cursor1.getString(temperatureIndex) + " ";
            uslovia[3] = " " + cursor1.getString(humidityIndex) + " ";
            uslovia[5] = " " + cursor1.getString(pressureIndex) + " ";
            textForEnd[1] = "   " + cursor1.getString(noteIndex);
        }
        cursor1.close();
        if (StrTable[18].equals("не соответств"))
            textForEnd[3] = "   Заземляющее устройство не соответствует требованию ПУЭ 1.8.39 п.5; ПТЭЭП Приложение 3";
        start(namefile, database);
        templatePDF.addElemGround(StrTable);
        templatePDF.addParagraphEnd_Ground(textForEnd, sign);
        templatePDF.closeDocument();
        templatePDF.appViewPDF(GroundingDevicesActivity1.this);
    }

    public void addSpisokDevices(SQLiteDatabase database, ListView devices) {
        final ArrayList<String> spisokDevices = new ArrayList <String>();
        Cursor cursor = database.query(DBHelper.TABLE_GD, new String[] {DBHelper.GD_PURPOSE, DBHelper.GD_PLACE,
                DBHelper.GD_R1, DBHelper.GD_R2}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int purposeIndex = cursor.getColumnIndex(DBHelper.GD_PURPOSE);
            int placeIndex = cursor.getColumnIndex(DBHelper.GD_PLACE);
            int r1Index = cursor.getColumnIndex(DBHelper.GD_R1);
            int r2Index = cursor.getColumnIndex(DBHelper.GD_R2);
            do {
                spisokDevices.add(cursor.getString(purposeIndex) + " назначение\n(" + cursor.getString(placeIndex) +
                        ";  допустимое R - " + cursor.getString(r1Index) + ";  измеренное R - " + cursor.getString(r2Index) + ")");
            } while (cursor.moveToNext());
        }
        cursor.close();
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, spisokDevices);
        devices.setAdapter(adapter);
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
