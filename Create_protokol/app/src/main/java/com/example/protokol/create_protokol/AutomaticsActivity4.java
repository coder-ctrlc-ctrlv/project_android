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

import java.util.ArrayList;
import java.util.Random;

public class AutomaticsActivity4 extends AppCompatActivity {

    DBHelper dbHelper;
    String nameFloor, nameRoom;
    int idFloor, idRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automatics4);

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
        getSupportActionBar().setSubtitle("Аппараты");
        getSupportActionBar().setTitle("Автомат. выключатели");
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
                Intent intent = new Intent("android.intent.action.Automatics5");
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
                AlertDialog.Builder alert = new AlertDialog.Builder(AutomaticsActivity4.this);
                alert.setTitle(((TextView) view).getText());
                final String arrayMenu[] = {"\nПосмотреть\n", "\nРедактировать\n", "\nПовторить\n", "\nУдалить\n"};
                alert.setItems(arrayMenu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //ЗАПРОС В БД ДЛЯ ПОЛУЧЕНИЯ ID НУЖНОЙ ГРУППЫ
                        Cursor cursor4 = database.query(DBHelper.TABLE_AUTOMATICS, new String[] {DBHelper.AU_ID}, "auline_id = ?", new String[] {String.valueOf(idLine)}, null, null, "_id DESC");
                        cursor4.moveToPosition(position);
                        int automatIndex = cursor4.getColumnIndex(DBHelper.AU_ID);
                        final int automatId = cursor4.getInt(automatIndex);
                        cursor4.close();

                        //ПОСМОТРЕТЬ
                        if (which == 0) {
                            //ПОЛУЧЕНИЕ ИНФОРМАЦИИ О ГРУППЕ
                            String place = "", symbol = "", type_overload = "", type_kz = "", excerpt = "", nom1 = "", nom2 = "", set_overload = "";
                            String set_kz = "", test_tok = "", time_permissible = "", time_measured = "", length_annex = "", tok_work = "", time_work = "", conclusion = "";
                            Cursor cursor1 = database.query(DBHelper.TABLE_AUTOMATICS, new String[] {DBHelper.AU_ID, DBHelper.AU_PLACE,
                                    DBHelper.AU_SYMBOL_SCHEME, DBHelper.AU_TYPE_OVERLOAD, DBHelper.AU_TYPE_KZ,
                                    DBHelper.AU_EXCERPT, DBHelper.AU_NOMINAL_1, DBHelper.AU_NOMINAL_2, DBHelper.AU_SET_OVERLOAD, DBHelper.AU_SET_KZ,
                                    DBHelper.AU_TEST_TOK, DBHelper.AU_TIME_PERMISSIBLE, DBHelper.AU_TIME_MEASURED, DBHelper.AU_LENGTH_ANNEX,
                                    DBHelper.AU_TOK_WORK, DBHelper.AU_TIME_WORK, DBHelper.AU_CONCLUSION}, "_id = ?", new String[] {String.valueOf(automatId)}, null, null, null);
                            if (cursor1.moveToFirst()) {
                                int placeIndex = cursor1.getColumnIndex(DBHelper.AU_PLACE);
                                int symbolIndex = cursor1.getColumnIndex(DBHelper.AU_SYMBOL_SCHEME);
                                int type_overloadIndex = cursor1.getColumnIndex(DBHelper.AU_TYPE_OVERLOAD);
                                int type_kzIndex = cursor1.getColumnIndex(DBHelper.AU_TYPE_KZ);
                                int excerptIndex = cursor1.getColumnIndex(DBHelper.AU_EXCERPT);
                                int nom1Index = cursor1.getColumnIndex(DBHelper.AU_NOMINAL_1);
                                int nom2Index = cursor1.getColumnIndex(DBHelper.AU_NOMINAL_2);
                                int set_overloadIndex = cursor1.getColumnIndex(DBHelper.AU_SET_OVERLOAD);
                                int set_kzIndex = cursor1.getColumnIndex(DBHelper.AU_SET_KZ);
                                int test_tokIndex = cursor1.getColumnIndex(DBHelper.AU_TEST_TOK);
                                int time_permissibleIndex = cursor1.getColumnIndex(DBHelper.AU_TIME_PERMISSIBLE);
                                int time_measuredIndex = cursor1.getColumnIndex(DBHelper.AU_TIME_MEASURED);
                                int length_annexIndex = cursor1.getColumnIndex(DBHelper.AU_LENGTH_ANNEX);
                                int tok_workIndex = cursor1.getColumnIndex(DBHelper.AU_TOK_WORK);
                                int time_workIndex = cursor1.getColumnIndex(DBHelper.AU_TIME_WORK);
                                int conclusionIndex = cursor1.getColumnIndex(DBHelper.AU_CONCLUSION);
                                do {
                                    if (cursor1.getString(placeIndex).equals(""))
                                        place = "Нет";
                                    else
                                        place = "Да";
                                    symbol = cursor1.getString(symbolIndex);
                                    type_overload = cursor1.getString(type_overloadIndex);
                                    type_kz = cursor1.getString(type_kzIndex);
                                    excerpt = cursor1.getString(excerptIndex);
                                    nom1 = cursor1.getString(nom1Index);
                                    nom2 = cursor1.getString(nom2Index);
                                    set_overload = cursor1.getString(set_overloadIndex);
                                    set_kz = cursor1.getString(set_kzIndex);
                                    test_tok = cursor1.getString(test_tokIndex);
                                    time_permissible = cursor1.getString(time_permissibleIndex);
                                    time_measured = cursor1.getString(time_measuredIndex);
                                    length_annex = cursor1.getString(length_annexIndex);
                                    tok_work = cursor1.getString(tok_workIndex);
                                    time_work = cursor1.getString(time_workIndex);
                                    conclusion = cursor1.getString(conclusionIndex);
                                } while (cursor1.moveToNext());
                            }
                            cursor1.close();
                            AlertDialog.Builder builder4 = new AlertDialog.Builder(AutomaticsActivity4.this);
                            builder4.setCancelable(false);
                            builder4.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                }
                            });
                            builder4.setMessage(Html.fromHtml("<b>Вводной: </b>" + place + "<br>" + "<b>Обознач. по схеме: </b>" + symbol + "<br>" +
                                    "<b>Типы расцепит. перегр.: </b>" + type_overload + "<br>" + "<b>Типы расцепит. к.з.: </b>" + type_kz + "<br>" +
                                    "<b>Выдержка времени откл. при к.з.: </b>" + excerpt + "<br>" + "<b>Номин. ток аппарата: </b>" + nom1 + "<br>" +
                                    "<b>Номин. ток расцепителя: </b>" + nom2 + "<br>" + "<b>Уставка расцепит. перегр.: </b>" + set_overload + "<br>" +
                                    "<b>Уставка расцепит. к.з.: </b>" + set_kz + "<br>" + "<b>Испытательный ток перегр.: </b>" + test_tok + "<br>" +
                                    "<b>Допуст. время срабат. перегр.: </b>" + time_permissible + "<br>" + "<b>Измерен. время срабат. перегр.: </b>" + time_measured + "<br>" +
                                    "<b>Длит. приложения исп. тока к.з.: </b>" + length_annex + "<br>" + "<b>Ток срабат. расцеп. к.з.: </b>" + tok_work + "<br>" +
                                    "<b>Время срабат. расцеп. к.з.: </b>" + time_work + "<br>" + "<b>Вывод: </b>" + conclusion));
                            builder4.setTitle(((TextView) view).getText());
                            AlertDialog dialog4 = builder4.create();
                            dialog4.show();
                        }

                        //ИЗМЕНИТЬ
                        if (which == 1) {
                            String n = ((TextView) view).getText().toString();
                            int firstSlesh = n.indexOf('|', 0);
                            Intent intent = new Intent("android.intent.action.Automatics5");
                            intent.putExtra("nameFloor", nameFloor);
                            intent.putExtra("idFloor", idFloor);
                            intent.putExtra("nameRoom", nameRoom);
                            intent.putExtra("idRoom", idRoom);
                            intent.putExtra("nameLine", nameLine);
                            intent.putExtra("idLine", idLine);
                            intent.putExtra("nameAutomat", n.substring(firstSlesh + 2, n.indexOf('|', firstSlesh + 1) - 1));
                            intent.putExtra("idAutomat", automatId);
                            startActivity(intent);
                        }

                        //ПОВТОРИТЬ
                        if (which == 2) {
                            AlertDialog.Builder alert1 = new AlertDialog.Builder(AutomaticsActivity4.this);
                            final View myView = getLayoutInflater().inflate(R.layout.dialog_for_names,null);
                            alert1.setCancelable(false);
                            alert1.setTitle("Введите обозначение по схеме:");
                            final EditText input = myView.findViewById(R.id.editText);
                            openKeyboard();
                            alert1.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    closeKeyboard(myView);
                                    String sym = input.getText().toString();

                                    String place = "", type_overload = "", type_kz = "", excerpt = "", nom1 = "", nom2 = "", set_overload = "", name = "";
                                    String set_kz = "", test_tok = "", time_permissible = "", length_annex = "", conclusion = "";
                                    Cursor cursor1 = database.query(DBHelper.TABLE_AUTOMATICS, new String[] {DBHelper.AU_ID, DBHelper.AU_PLACE, DBHelper.AU_NAME,
                                            DBHelper.AU_TYPE_OVERLOAD, DBHelper.AU_TYPE_KZ, DBHelper.AU_EXCERPT, DBHelper.AU_NOMINAL_1,
                                            DBHelper.AU_NOMINAL_2, DBHelper.AU_SET_OVERLOAD, DBHelper.AU_SET_KZ,
                                            DBHelper.AU_TEST_TOK, DBHelper.AU_TIME_PERMISSIBLE, DBHelper.AU_LENGTH_ANNEX, DBHelper.AU_CONCLUSION},
                                            "_id = ?", new String[] {String.valueOf(automatId)}, null, null, null);
                                    if (cursor1.moveToFirst()) {
                                        int placeIndex = cursor1.getColumnIndex(DBHelper.AU_PLACE);
                                        int nameIndex = cursor1.getColumnIndex(DBHelper.AU_NAME);
                                        int type_overloadIndex = cursor1.getColumnIndex(DBHelper.AU_TYPE_OVERLOAD);
                                        int type_kzIndex = cursor1.getColumnIndex(DBHelper.AU_TYPE_KZ);
                                        int excerptIndex = cursor1.getColumnIndex(DBHelper.AU_EXCERPT);
                                        int nom1Index = cursor1.getColumnIndex(DBHelper.AU_NOMINAL_1);
                                        int nom2Index = cursor1.getColumnIndex(DBHelper.AU_NOMINAL_2);
                                        int set_overloadIndex = cursor1.getColumnIndex(DBHelper.AU_SET_OVERLOAD);
                                        int set_kzIndex = cursor1.getColumnIndex(DBHelper.AU_SET_KZ);
                                        int test_tokIndex = cursor1.getColumnIndex(DBHelper.AU_TEST_TOK);
                                        int time_permissibleIndex = cursor1.getColumnIndex(DBHelper.AU_TIME_PERMISSIBLE);
                                        int length_annexIndex = cursor1.getColumnIndex(DBHelper.AU_LENGTH_ANNEX);
                                        int conclIndex = cursor1.getColumnIndex(DBHelper.AU_CONCLUSION);
                                        do {
                                            place = cursor1.getString(placeIndex);
                                            name = cursor1.getString(nameIndex);
                                            type_overload = cursor1.getString(type_overloadIndex);
                                            type_kz = cursor1.getString(type_kzIndex);
                                            excerpt = cursor1.getString(excerptIndex);
                                            nom1 = cursor1.getString(nom1Index);
                                            nom2 = cursor1.getString(nom2Index);
                                            set_overload = cursor1.getString(set_overloadIndex);
                                            set_kz = cursor1.getString(set_kzIndex);
                                            test_tok = cursor1.getString(test_tokIndex);
                                            time_permissible = cursor1.getString(time_permissibleIndex);
                                            length_annex = cursor1.getString(length_annexIndex);
                                            conclusion = cursor1.getString(conclIndex);
                                        } while (cursor1.moveToNext());
                                    }
                                    cursor1.close();

                                    if (!conclusion.equals("не соотв.")) {
                                        String time_measured = getTimeMeasured(nom2);
                                        String tok_work = getTokWork(set_kz.substring(set_kz.indexOf('-') + 1));
                                        ContentValues contentValues = new ContentValues();
                                        contentValues.put(DBHelper.AU_ID_ALINE, idLine);
                                        contentValues.put(DBHelper.AU_PLACE, place);
                                        contentValues.put(DBHelper.AU_SYMBOL_SCHEME, sym);
                                        contentValues.put(DBHelper.AU_NAME, name);
                                        contentValues.put(DBHelper.AU_TYPE_OVERLOAD, type_overload);
                                        contentValues.put(DBHelper.AU_TYPE_KZ, type_kz);
                                        contentValues.put(DBHelper.AU_EXCERPT, excerpt);
                                        contentValues.put(DBHelper.AU_NOMINAL_1, nom1);
                                        contentValues.put(DBHelper.AU_NOMINAL_2, nom2);
                                        contentValues.put(DBHelper.AU_SET_OVERLOAD, set_overload);
                                        contentValues.put(DBHelper.AU_SET_KZ, set_kz);
                                        contentValues.put(DBHelper.AU_TEST_TOK, test_tok);
                                        contentValues.put(DBHelper.AU_TIME_PERMISSIBLE, time_permissible);
                                        contentValues.put(DBHelper.AU_TIME_MEASURED, time_measured);
                                        contentValues.put(DBHelper.AU_LENGTH_ANNEX, length_annex);
                                        contentValues.put(DBHelper.AU_TOK_WORK, tok_work);
                                        contentValues.put(DBHelper.AU_TIME_WORK, getTimeWork(name));
                                        contentValues.put(DBHelper.AU_CONCLUSION, getConclusion(time_permissible, time_measured, set_kz, tok_work));
                                        database.insert(DBHelper.TABLE_AUTOMATICS, null, contentValues);
                                        swapAutomatics(listAutomatics.getAdapter().getCount() - position, listAutomatics.getAdapter().getCount() + 1, idLine, database);
                                        addSpisokAutomatics(database, listAutomatics, idLine);
                                        Toast toast = Toast.makeText(getApplicationContext(),
                                                "Данные сохранены", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                    else {
                                        AlertDialog.Builder alert7 = new AlertDialog.Builder(AutomaticsActivity4.this);
                                        alert7.setCancelable(false);
                                        alert7.setMessage("Нельзя повторить аппарат, не соответсвующий нормативному документу!");
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
                            AlertDialog.Builder builder4 = new AlertDialog.Builder(AutomaticsActivity4.this);
                            builder4.setCancelable(false);
                            builder4.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    database.delete(DBHelper.TABLE_AUTOMATICS, "_id = ?", new String[] {String.valueOf(automatId)});
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
                Intent intent = new Intent("android.intent.action.Automatics3");
                intent.putExtra("nameFloor", nameFloor);
                intent.putExtra("idFloor", idFloor);
                intent.putExtra("nameRoom", nameRoom);
                intent.putExtra("idRoom", idRoom);
                startActivity(intent);
                return true;
            case R.id.action_main:
                Intent intent1 = new Intent(AutomaticsActivity4.this, MenuItemsActivity.class);
                startActivity(intent1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addSpisokAutomatics(SQLiteDatabase database, ListView automatics, int idLine) {
        final ArrayList<String> spisokAutomatics = new ArrayList <String>();
        Cursor cursor = database.query(DBHelper.TABLE_AUTOMATICS, new String[] {DBHelper.AU_NAME, DBHelper.AU_SYMBOL_SCHEME, DBHelper.AU_NOMINAL_2}, "auline_id = ?", new String[] {String.valueOf(idLine)}, null, null, "_id DESC");
        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(DBHelper.AU_NAME);
            int symbolIndex = cursor.getColumnIndex(DBHelper.AU_SYMBOL_SCHEME);
            int nominalIndex = cursor.getColumnIndex(DBHelper.AU_NOMINAL_2);
            do {
                spisokAutomatics.add(cursor.getString(symbolIndex) + " | " + cursor.getString(nameIndex) + " | " + cursor.getString(nominalIndex) + ", A");
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
        Cursor cursor4 = database.query(DBHelper.TABLE_AUTOMATICS, new String[] {DBHelper.AU_ID}, "auline_id = ?", new String[] {String.valueOf(idLine)}, null, null, null);
        cursor4.moveToPosition(positionCurrent - 1);
        automaticIndex = cursor4.getColumnIndex(DBHelper.AU_ID);
        idCurrent = cursor4.getInt(automaticIndex);
        cursor4.close();

        while (positionCurrent - positionStop != 1) {

            //ЗАПРОС В БД ДЛЯ ПОЛУЧЕНИЯ ID И НАЗВАНИЯ ГРУППЫ ПОВЫШЕ
            Cursor cursor = database.query(DBHelper.TABLE_AUTOMATICS, new String[] {DBHelper.AU_ID}, "auline_id = ?", new String[] {String.valueOf(idLine)}, null, null, null);
            cursor.moveToPosition(positionCurrent - 2);
            automaticIndex = cursor.getColumnIndex(DBHelper.AU_ID);
            idUp = cursor.getInt(automaticIndex);
            cursor.close();

            //МЕНЯЕМ ID
            values = new ContentValues();
            values.put(DBHelper.AU_ID, "-1");
            database.update(DBHelper.TABLE_AUTOMATICS, values,"_id = ?", new String[]{Integer.toString(idUp)});
            values = new ContentValues();
            values.put(DBHelper.AU_ID, Integer.toString(idUp));
            database.update(DBHelper.TABLE_AUTOMATICS, values,"_id = ?", new String[]{Integer.toString(idCurrent)});
            values = new ContentValues();
            values.put(DBHelper.AU_ID, Integer.toString(idCurrent));
            database.update(DBHelper.TABLE_AUTOMATICS, values,"_id = ?", new String[]{"-1"});

            idCurrent = idUp;
            positionCurrent--;
        }
    }

    public String getTimeMeasured(String s) {
        int num = Integer.parseInt(s);
        Random generator = new Random();
        if (num <= 32)
            return String.valueOf(generator.nextInt(21) + 30);
        return String.valueOf(generator.nextInt(61) + 30);
    }

    public String getTokWork(String numb) {
        int x = Integer.parseInt(numb);
        Random generator = new Random();
        return String.valueOf(x - ((generator.nextInt(3) + 1) * 10));
    }

    public String getTimeWork(String name) {
        Random generator = new Random();
        if (name.contains("3P") || name.contains("3Р") || name.contains("3p") || name.contains("3р"))
            return "0," + String.valueOf(generator.nextInt(8) + 12);
        return "0," + String.valueOf(generator.nextInt(28) + 12);
    }

    public String getConclusion(String numb1, String numb2, String diapazon, String tok_w) {
        int left = Integer.parseInt(diapazon.substring(0, diapazon.indexOf('-')));
        int right = Integer.parseInt(diapazon.substring(diapazon.indexOf('-') + 1));
        if ((Integer.parseInt(numb2) < Integer.parseInt(numb1)) && (left <= Integer.parseInt(tok_w)) && (right >= Integer.parseInt(tok_w)))
            return "соответст.";
        return "не соотв.";
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
