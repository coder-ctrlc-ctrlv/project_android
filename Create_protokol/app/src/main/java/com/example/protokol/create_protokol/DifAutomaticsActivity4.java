package com.example.protokol.create_protokol;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.text.HtmlCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
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

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Random;

public class DifAutomaticsActivity4 extends AppCompatActivity {

    DBHelper dbHelper;
    String nameFloor, nameRoom;
    int idFloor, idRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dif_automatics4);

        dbHelper = new DBHelper(this);
        final SQLiteDatabase database = dbHelper.getWritableDatabase();

        TextView floor = findViewById(R.id.textView5);
        TextView room = findViewById(R.id.textView6);
        TextView line = findViewById(R.id.textView7);
        final ListView listAutomatics = findViewById(R.id.automatics);
        Button addAutomatic = findViewById(R.id.button9);
        nameFloor = getIntent().getStringExtra("nameFloor");
        idFloor = getIntent().getIntExtra("idFloor", 0);
        nameRoom = getIntent().getStringExtra("nameRoom");
        idRoom = getIntent().getIntExtra("idRoom", 0);
        final String nameLine = getIntent().getStringExtra("nameLine");
        final int idLine = getIntent().getIntExtra("idLine", 0);

        //НАСТРАИВАЕМ ACTIONBAR
        getSupportActionBar().setSubtitle("Авт. выключатели");
        getSupportActionBar().setTitle("Диф. автоматы");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ВЫВОД ЭТАЖА И КОМНАТЫ
        floor.setText("Этаж: " + nameFloor);
        room.setText("Комната: " + nameRoom);
        line.setText("Щит: " + nameLine);

        //ЗАПРОС В БД И ЗАПОЛНЕНИЕ СПИСКА ЩИТОВ
        addSpisokAutomatics(database, listAutomatics, idLine);

        addAutomatic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.DifAutomatics5");
                intent.putExtra("nameFloor", nameFloor);
                intent.putExtra("idFloor", idFloor);
                intent.putExtra("nameRoom", nameRoom);
                intent.putExtra("idRoom", idRoom);
                intent.putExtra("nameLine", nameLine);
                intent.putExtra("idLine", idLine);
                startActivity(intent);
            }
        });

        //ПОСМОТРЕТЬ, ИЗМЕНИТЬ, УДАЛИТЬ АВТОМАТ
        listAutomatics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(DifAutomaticsActivity4.this);
                alert.setTitle(((TextView) view).getText());
                final String arrayMenu[] = {"\nПосмотреть\n", "\nРедактировать\n", "\nПовторить\n", "\nУдалить\n"};
                alert.setItems(arrayMenu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //ЗАПРОС В БД ДЛЯ ПОЛУЧЕНИЯ ID НУЖНОЙ ГРУППЫ
                        Cursor cursor4 = database.query(DBHelper.TABLE_DIF_AUTOMATICS, new String[] {DBHelper.DIF_AU_ID}, "dif_auline_id = ?", new String[] {String.valueOf(idLine)}, null, null, "_id DESC");
                        cursor4.moveToPosition(position);
                        int automatIndex = cursor4.getColumnIndex(DBHelper.DIF_AU_ID);
                        final int automatId = cursor4.getInt(automatIndex);
                        cursor4.close();

                        //ПОСМОТРЕТЬ
                        if (which == 0) {
                            //ПОЛУЧЕНИЕ ИНФОРМАЦИИ О ГРУППЕ
                            String place = "", uzo = "", type_switch = "", u = "", set_thermal = "", set_electromagn = "", check_test_tok = "", check_time = "";
                            String check_work_tok = "", i_nom = "", i_leack = "", i_extra = "", i_measured = "", time_extra = "", time_measured = "", conclusion = "";
                            Cursor cursor1 = database.query(DBHelper.TABLE_DIF_AUTOMATICS, new String[] {DBHelper.DIF_AU_ID, DBHelper.DIF_AU_PLACE,
                                    DBHelper.DIF_AU_UZO, DBHelper.DIF_AU_TYPE_SWITCH, DBHelper.DIF_AU_U,
                                    DBHelper.DIF_AU_SET_THERMAL, DBHelper.DIF_AU_SET_ELECTR_MAGN, DBHelper.DIF_AU_CHECK_TEST_TOK, DBHelper.DIF_AU_CHECK_TIME_, DBHelper.DIF_AU_CHECK_WORK_TOK,
                                    DBHelper.DIF_AU_I_NOM, DBHelper.DIF_AU_I_LEAK, DBHelper.DIF_AU_I_EXTRA, DBHelper.DIF_AU_I_MEASURED,
                                    DBHelper.DIF_AU_TIME_EXTRA, DBHelper.DIF_AU_TIME_MEASURED, DBHelper.DIF_AU_CONCLUSION}, "_id = ?", new String[] {String.valueOf(automatId)}, null, null, null);
                            if (cursor1.moveToFirst()) {
                                int placeIndex = cursor1.getColumnIndex(DBHelper.DIF_AU_PLACE);
                                int uzoIndex = cursor1.getColumnIndex(DBHelper.DIF_AU_UZO);
                                int type_switchIndex = cursor1.getColumnIndex(DBHelper.DIF_AU_TYPE_SWITCH);
                                int uIndex = cursor1.getColumnIndex(DBHelper.DIF_AU_U);
                                int set_thermalIndex = cursor1.getColumnIndex(DBHelper.DIF_AU_SET_THERMAL);
                                int set_electromagnIndex = cursor1.getColumnIndex(DBHelper.DIF_AU_SET_ELECTR_MAGN);
                                int check_test_tokIndex = cursor1.getColumnIndex(DBHelper.DIF_AU_CHECK_TEST_TOK);
                                int check_timeIndex = cursor1.getColumnIndex(DBHelper.DIF_AU_CHECK_TIME_);
                                int check_work_tokIndex = cursor1.getColumnIndex(DBHelper.DIF_AU_CHECK_WORK_TOK);
                                int i_nomIndex = cursor1.getColumnIndex(DBHelper.DIF_AU_I_NOM);
                                int i_leackIndex = cursor1.getColumnIndex(DBHelper.DIF_AU_I_LEAK);
                                int i_extraIndex = cursor1.getColumnIndex(DBHelper.DIF_AU_I_EXTRA);
                                int i_measuredIndex = cursor1.getColumnIndex(DBHelper.DIF_AU_I_MEASURED);
                                int time_extraIndex = cursor1.getColumnIndex(DBHelper.DIF_AU_TIME_EXTRA);
                                int time_measuredIndex = cursor1.getColumnIndex(DBHelper.DIF_AU_TIME_MEASURED);
                                int conclusionIndex = cursor1.getColumnIndex(DBHelper.DIF_AU_CONCLUSION);
                                do {
                                    place = cursor1.getString(placeIndex);
                                    uzo = cursor1.getString(uzoIndex);
                                    type_switch = cursor1.getString(type_switchIndex);
                                    u = cursor1.getString(uIndex);
                                    set_thermal = cursor1.getString(set_thermalIndex);
                                    set_electromagn = cursor1.getString(set_electromagnIndex);
                                    check_test_tok = cursor1.getString(check_test_tokIndex);
                                    check_time = cursor1.getString(check_timeIndex);
                                    check_work_tok = cursor1.getString(check_work_tokIndex);
                                    i_nom = cursor1.getString(i_nomIndex);
                                    i_leack = cursor1.getString(i_leackIndex);
                                    i_extra = cursor1.getString(i_extraIndex);
                                    i_measured = cursor1.getString(i_measuredIndex);
                                    time_extra = cursor1.getString(time_extraIndex);
                                    time_measured = cursor1.getString(time_measuredIndex);
                                    conclusion = cursor1.getString(conclusionIndex);
                                } while (cursor1.moveToNext());
                            }
                            cursor1.close();
                            AlertDialog.Builder builder4 = new AlertDialog.Builder(DifAutomaticsActivity4.this);
                            builder4.setCancelable(false);
                            builder4.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                }
                            });
                            String min = i_extra.substring(0, i_extra.indexOf('\u003C'));
                            String max = i_extra.substring(i_extra.indexOf('\u2264') + 1);
                            builder4.setMessage(Html.fromHtml("<b>Обозначение по схеме: </b>" + place + "<br>" + "<b>Тип УЗО: </b>" + uzo + "<br>" +
                                    "<b>Тип автомат. выключателя: </b>" + type_switch + "<br>" + "<b>Uф (В): </b>" + u + "<br>" +
                                    "<b>Тепл. расцеп. (А): </b>" + set_thermal + "<br>" + "<b>Эл. маг. расцеп. (А): </b>" + set_electromagn + "<br>" +
                                    "<b>Испыт. ток (А): </b>" + check_test_tok + "<br>" + "<b>Время ср. теп. расц. (сек.): </b>" + check_time + "<br>" +
                                    "<b>Ток сраб. эл. маг. расцеп. (А): </b>" + check_work_tok + "<br>" + "<b>Ток ном. (мА): </b>" + i_nom + "<br>" +
                                    "<b>Ток утеч. (мА): </b>" + i_leack + "<br>" + "<b>Ток сраб. защ. доп. (мА): </b>" + min + " &#60 I &#8804 " + max + "<br>" +
                                    "<b>Ток сраб. защ. измер. (мА): </b>" + i_measured + "<br>" + "<b>Время сраб. доп. (с): </b>" + time_extra + "<br>" +
                                    "<b>Время сраб. измер. (с): </b>" + time_measured + "<br>" + "<b>Вывод: </b>" + conclusion));
                            builder4.setTitle(((TextView) view).getText());
                            AlertDialog dialog4 = builder4.create();
                            dialog4.show();
                        }

                        //ИЗМЕНИТЬ
                        if (which == 1) {
                            Intent intent = new Intent("android.intent.action.DifAutomatics5");
                            intent.putExtra("nameFloor", nameFloor);
                            intent.putExtra("idFloor", idFloor);
                            intent.putExtra("nameRoom", nameRoom);
                            intent.putExtra("idRoom", idRoom);
                            intent.putExtra("nameLine", nameLine);
                            intent.putExtra("idLine", idLine);
                            intent.putExtra("idAutomat", automatId);
                            startActivity(intent);
                        }

                        //ПОВТОРИТЬ
                        if (which == 2) {
                            AlertDialog.Builder alert1 = new AlertDialog.Builder(DifAutomaticsActivity4.this);
                            final View myView = getLayoutInflater().inflate(R.layout.dialog_for_names,null);
                            alert1.setCancelable(false);
                            alert1.setTitle("Введите обозначение по схеме:");
                            final EditText input = myView.findViewById(R.id.editText);
                            openKeyboard();
                            alert1.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    closeKeyboard(myView);
                                    String sym = input.getText().toString();

                                    String uzo = "", type_switch = "", u = "", set_thermal = "", set_electromagn = "", check_test_tok = "", check_time = "";
                                    String check_work_tok = "", i_nom = "", i_leack = "", i_extra = "", i_measured = "", time_extra = "", time_measured = "", conclusion = "";
                                    Cursor cursor1 = database.query(DBHelper.TABLE_DIF_AUTOMATICS, new String[] {DBHelper.DIF_AU_ID,
                                            DBHelper.DIF_AU_UZO, DBHelper.DIF_AU_TYPE_SWITCH, DBHelper.DIF_AU_U,
                                            DBHelper.DIF_AU_SET_THERMAL, DBHelper.DIF_AU_SET_ELECTR_MAGN, DBHelper.DIF_AU_CHECK_TEST_TOK, DBHelper.DIF_AU_CHECK_TIME_, DBHelper.DIF_AU_CHECK_WORK_TOK,
                                            DBHelper.DIF_AU_I_NOM, DBHelper.DIF_AU_I_LEAK, DBHelper.DIF_AU_I_EXTRA, DBHelper.DIF_AU_I_MEASURED,
                                            DBHelper.DIF_AU_TIME_EXTRA, DBHelper.DIF_AU_TIME_MEASURED, DBHelper.DIF_AU_CONCLUSION}, "_id = ?", new String[] {String.valueOf(automatId)}, null, null, null);
                                    if (cursor1.moveToFirst()) {
                                        int uzoIndex = cursor1.getColumnIndex(DBHelper.DIF_AU_UZO);
                                        int type_switchIndex = cursor1.getColumnIndex(DBHelper.DIF_AU_TYPE_SWITCH);
                                        int uIndex = cursor1.getColumnIndex(DBHelper.DIF_AU_U);
                                        int set_thermalIndex = cursor1.getColumnIndex(DBHelper.DIF_AU_SET_THERMAL);
                                        int set_electromagnIndex = cursor1.getColumnIndex(DBHelper.DIF_AU_SET_ELECTR_MAGN);
                                        int check_test_tokIndex = cursor1.getColumnIndex(DBHelper.DIF_AU_CHECK_TEST_TOK);
                                        int check_timeIndex = cursor1.getColumnIndex(DBHelper.DIF_AU_CHECK_TIME_);
                                        int check_work_tokIndex = cursor1.getColumnIndex(DBHelper.DIF_AU_CHECK_WORK_TOK);
                                        int i_nomIndex = cursor1.getColumnIndex(DBHelper.DIF_AU_I_NOM);
                                        int i_leackIndex = cursor1.getColumnIndex(DBHelper.DIF_AU_I_LEAK);
                                        int i_extraIndex = cursor1.getColumnIndex(DBHelper.DIF_AU_I_EXTRA);
                                        int i_measuredIndex = cursor1.getColumnIndex(DBHelper.DIF_AU_I_MEASURED);
                                        int time_extraIndex = cursor1.getColumnIndex(DBHelper.DIF_AU_TIME_EXTRA);
                                        int time_measuredIndex = cursor1.getColumnIndex(DBHelper.DIF_AU_TIME_MEASURED);
                                        int conclusionIndex = cursor1.getColumnIndex(DBHelper.DIF_AU_CONCLUSION);
                                        do {
                                            uzo = cursor1.getString(uzoIndex);
                                            type_switch = cursor1.getString(type_switchIndex);
                                            u = cursor1.getString(uIndex);
                                            set_thermal = cursor1.getString(set_thermalIndex);
                                            set_electromagn = cursor1.getString(set_electromagnIndex);
                                            check_test_tok = cursor1.getString(check_test_tokIndex);
                                            check_time = cursor1.getString(check_timeIndex);
                                            check_work_tok = cursor1.getString(check_work_tokIndex);
                                            i_nom = cursor1.getString(i_nomIndex);
                                            i_leack = cursor1.getString(i_leackIndex);
                                            i_extra = cursor1.getString(i_extraIndex);
                                            i_measured = cursor1.getString(i_measuredIndex);
                                            time_extra = cursor1.getString(time_extraIndex);
                                            time_measured = cursor1.getString(time_measuredIndex);
                                            conclusion = cursor1.getString(conclusionIndex);
                                        } while (cursor1.moveToNext());
                                    }
                                    cursor1.close();

                                    if (!conclusion.equals("не соответств.")) {
                                        ContentValues contentValues = new ContentValues();
                                        contentValues.put(DBHelper.DIF_AU_ID_ALINE, idLine);
                                        contentValues.put(DBHelper.DIF_AU_PLACE, sym);
                                        contentValues.put(DBHelper.DIF_AU_UZO, uzo);
                                        contentValues.put(DBHelper.DIF_AU_TYPE_SWITCH, type_switch);
                                        contentValues.put(DBHelper.DIF_AU_U, u);
                                        contentValues.put(DBHelper.DIF_AU_SET_THERMAL, set_thermal);
                                        contentValues.put(DBHelper.DIF_AU_SET_ELECTR_MAGN, set_electromagn);
                                        contentValues.put(DBHelper.DIF_AU_CHECK_TEST_TOK, check_test_tok);
                                        contentValues.put(DBHelper.DIF_AU_CHECK_TIME_, check_time);
                                        contentValues.put(DBHelper.DIF_AU_CHECK_WORK_TOK, check_work_tok);
                                        contentValues.put(DBHelper.DIF_AU_I_NOM, i_nom);
                                        contentValues.put(DBHelper.DIF_AU_I_LEAK, i_leack);
                                        contentValues.put(DBHelper.DIF_AU_I_EXTRA, i_extra);
                                        contentValues.put(DBHelper.DIF_AU_I_MEASURED, getIMeasured(i_measured, i_extra));
                                        contentValues.put(DBHelper.DIF_AU_TIME_EXTRA, time_extra);
                                        contentValues.put(DBHelper.DIF_AU_TIME_MEASURED, getTimeMeasured(time_measured, time_extra));
                                        contentValues.put(DBHelper.DIF_AU_CONCLUSION, conclusion);
                                        database.insert(DBHelper.TABLE_DIF_AUTOMATICS, null, contentValues);
                                        swapAutomatics(listAutomatics.getAdapter().getCount() - position, listAutomatics.getAdapter().getCount() + 1, idLine, database);
                                        addSpisokAutomatics(database, listAutomatics, idLine);
                                        Toast toast = Toast.makeText(getApplicationContext(),
                                                "Данные сохранены", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                    else {
                                        AlertDialog.Builder alert7 = new AlertDialog.Builder(DifAutomaticsActivity4.this);
                                        alert7.setCancelable(false);
                                        alert7.setMessage("Нельзя повторить авт. выключатель, не соответсвующий нормативному документу!");
                                        alert7.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {

                                            }
                                        });
                                        alert7.show();
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

                        //УДАЛИТЬ ГРУППУ
                        if (which == 3) {
                            //ПОДТВЕРЖДЕНИЕ
                            AlertDialog.Builder builder4 = new AlertDialog.Builder(DifAutomaticsActivity4.this);
                            builder4.setCancelable(false);
                            builder4.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    database.delete(DBHelper.TABLE_DIF_AUTOMATICS, "_id = ?", new String[] {String.valueOf(automatId)});
                                    //ЗАПРОС В БД И ЗАПОЛНЕНИЕ СПИСКА ГРУПП
                                    addSpisokAutomatics(database, listAutomatics, idLine);
                                    Toast toast2 = Toast.makeText(getApplicationContext(),
                                            "Элемент удален", Toast.LENGTH_SHORT);
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
                Intent intent = new Intent("android.intent.action.DifAutomatics3");
                intent.putExtra("nameFloor", nameFloor);
                intent.putExtra("idFloor", idFloor);
                intent.putExtra("nameRoom", nameRoom);
                intent.putExtra("idRoom", idRoom);
                startActivity(intent);
                return true;
            case R.id.action_main:
                Intent intent1 = new Intent(DifAutomaticsActivity4.this, MenuItemsActivity.class);
                startActivity(intent1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addSpisokAutomatics(SQLiteDatabase database, ListView automatics, int idLine) {
        final ArrayList<String> spisokAutomatics = new ArrayList <String>();
        Cursor cursor = database.query(DBHelper.TABLE_DIF_AUTOMATICS, new String[] {DBHelper.DIF_AU_PLACE, DBHelper.DIF_AU_UZO, DBHelper.DIF_AU_TYPE_SWITCH}, "dif_auline_id = ?", new String[] {String.valueOf(idLine)}, null, null, "_id DESC");
        if (cursor.moveToFirst()) {
            int placeIndex = cursor.getColumnIndex(DBHelper.DIF_AU_PLACE);
            int uzoIndex = cursor.getColumnIndex(DBHelper.DIF_AU_UZO);
            int type_switchIndex = cursor.getColumnIndex(DBHelper.DIF_AU_TYPE_SWITCH);
            do {
                spisokAutomatics.add(cursor.getString(placeIndex) + " | " + cursor.getString(uzoIndex) + " | " + cursor.getString(type_switchIndex));
            } while (cursor.moveToNext());
        }
        cursor.close();
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, spisokAutomatics);
        automatics.setAdapter(adapter);
    }

    public void swapAutomatics(int positionStop, int positionCurrent, int idLine, SQLiteDatabase database) {
        if (positionCurrent - positionStop == 1)
            return;
        int idCurrent, idUp, automaticIndex;
        ContentValues values;

        //ЗАПРОС В БД ДЛЯ ПОЛУЧЕНИЯ ID
        Cursor cursor4 = database.query(DBHelper.TABLE_DIF_AUTOMATICS, new String[] {DBHelper.DIF_AU_ID}, "dif_auline_id = ?", new String[] {String.valueOf(idLine)}, null, null, null);
        cursor4.moveToPosition(positionCurrent - 1);
        automaticIndex = cursor4.getColumnIndex(DBHelper.DIF_AU_ID);
        idCurrent = cursor4.getInt(automaticIndex);
        cursor4.close();

        while (positionCurrent - positionStop != 1) {

            //ЗАПРОС В БД ДЛЯ ПОЛУЧЕНИЯ ID И НАЗВАНИЯ ГРУППЫ ПОВЫШЕ
            Cursor cursor = database.query(DBHelper.TABLE_DIF_AUTOMATICS, new String[] {DBHelper.DIF_AU_ID}, "dif_auline_id = ?", new String[] {String.valueOf(idLine)}, null, null, null);
            cursor.moveToPosition(positionCurrent - 2);
            automaticIndex = cursor.getColumnIndex(DBHelper.DIF_AU_ID);
            idUp = cursor.getInt(automaticIndex);
            cursor.close();

            //МЕНЯЕМ ID
            values = new ContentValues();
            values.put(DBHelper.DIF_AU_ID, "-1");
            database.update(DBHelper.TABLE_DIF_AUTOMATICS, values,"_id = ?", new String[]{Integer.toString(idUp)});
            values = new ContentValues();
            values.put(DBHelper.DIF_AU_ID, Integer.toString(idUp));
            database.update(DBHelper.TABLE_DIF_AUTOMATICS, values,"_id = ?", new String[]{Integer.toString(idCurrent)});
            values = new ContentValues();
            values.put(DBHelper.DIF_AU_ID, Integer.toString(idCurrent));
            database.update(DBHelper.TABLE_DIF_AUTOMATICS, values,"_id = ?", new String[]{"-1"});

            idCurrent = idUp;
            positionCurrent--;
        }
    }

    public String getIMeasured(String i_prev, String limit) {
        Random generator = new Random();
        double left_lim = Double.parseDouble(limit.substring(0, limit.indexOf('\u003C')));
        double right_lim = Double.parseDouble(limit.substring(limit.indexOf('\u2264') + 1));
        double i_pr = Double.parseDouble(i_prev.replace(',', '.'));
        double percent, i_cur;
        do {
            percent = (generator.nextInt(26) + 5) / 100.00;
            if (generator.nextBoolean())
                i_cur = i_pr + (i_pr * percent);
            else
                i_cur = i_pr - (i_pr * percent);
            i_cur = Math.round(i_cur * 10) / 10.0;
        } while (i_cur > right_lim || i_cur <= left_lim);
        return String.valueOf(i_cur).replace('.', ',');
    }

    public String getTimeMeasured(String time_prev, String limit) {
        Random generator = new Random();
        double lim = Double.parseDouble(limit.replace(',', '.'));
        double time_pr = Double.parseDouble(time_prev.replace(',', '.'));
        double percent, time_cur;
        do {
            percent = (generator.nextInt(26) + 5) / 100.00;
            if (generator.nextBoolean())
                time_cur = time_pr + (time_pr * percent);
            else
                time_cur = time_pr - (time_pr * percent);
            time_cur = Math.round(time_cur * 1000) / 1000.000;
        } while (time_cur > lim);
        return String.valueOf(time_cur).replace('.', ',');
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
