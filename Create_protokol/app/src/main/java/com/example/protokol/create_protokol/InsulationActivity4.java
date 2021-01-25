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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class InsulationActivity4 extends AppCompatActivity {

    DBHelper dbHelper;
    String nameFloor, nameRoom;
    int idFloor, idRoom, idLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insulation4);

        dbHelper = new DBHelper(this);
        final SQLiteDatabase database = dbHelper.getWritableDatabase();

        TextView floor = findViewById(R.id.textView5);
        TextView room = findViewById(R.id.textView6);
        TextView line = findViewById(R.id.textView7);
        final ListView groups = findViewById(R.id.groups);
        Button addGroup = findViewById(R.id.button9);
        Button back_btn = findViewById(R.id.button10);
        nameFloor = getIntent().getStringExtra("nameFloor");
        idFloor = getIntent().getIntExtra("idFloor", 0);
        nameRoom = getIntent().getStringExtra("nameRoom");
        idRoom = getIntent().getIntExtra("idRoom", 0);
        final String nameLine = getIntent().getStringExtra("nameLine");
        idLine = getIntent().getIntExtra("idLine", 0);

        //НАСТРАИВАЕМ ACTIONBAR
        getSupportActionBar().setSubtitle("Группы");
        getSupportActionBar().setTitle("Изоляция");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ВЫВОД КОМНАТЫ И ЩИТА
        floor.setText("Этаж: " + nameFloor);
        room.setText("Помещение: " + nameRoom);
        line.setText("Щит: " + nameLine);

        //ЗАПРОС В БД И ЗАПОЛНЕНИЕ СПИСКА ГРУПП
        addSpisokGroups(database, groups, idLine);

        //ДОБАВИТЬ ГРУППУ
        addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numb_gr = "";
                Cursor cursor1 = database.rawQuery("SELECT count(*) FROM " + DBHelper.TABLE_GROUPS +
                        " WHERE " + DBHelper.GR_LINE_ID + " = " + String.valueOf(idLine) + " AND (" + DBHelper.GR_NAME + " LIKE '%/1%'" + " OR " + DBHelper.GR_NAME + " NOT LIKE '%/%')", null);
                if (cursor1.moveToFirst()) {
                    int countIndex = cursor1.getColumnIndex("count(*)");
                    numb_gr = String.valueOf(cursor1.getInt(countIndex) + 1);
                }
                cursor1.close();
                Intent intent = new Intent("android.intent.action.Insulation5");
                intent.putExtra("nameFloor", nameFloor);
                intent.putExtra("idFloor", idFloor);
                intent.putExtra("nameRoom", nameRoom);
                intent.putExtra("idRoom", idRoom);
                intent.putExtra("nameLine", nameLine);
                intent.putExtra("idLine", idLine);
                intent.putExtra("nameGroup", "Группа №" + numb_gr);
                startActivity(intent);
            }
        });

        //ПОСМОТРЕТЬ, ИЗМЕНИТЬ, ПОВТОРИТЬ, УДАЛИТЬ ГРУППУ И ДОБАВИТЬ ПОДГРУППУ
        groups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, final long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity4.this);
                alert.setTitle(((TextView) view).getText());
                final String arrayMenu[] = {"\nПосмотреть\n", "\nРедактировать\n", "\nПовторить\n", "\nДобавить подгруппу\n", "\nУдалить\n"};
                alert.setItems(arrayMenu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //ЗАПРОС В БД ДЛЯ ПОЛУЧЕНИЯ ID НУЖНОЙ ГРУППЫ
                        Cursor cursor4 = database.query(DBHelper.TABLE_GROUPS, new String[] {DBHelper.GR_ID}, "grline_id = ?", new String[] {String.valueOf(idLine)}, null, null, "_id DESC");
                        cursor4.moveToPosition(position);
                        int groupIndex = cursor4.getColumnIndex(DBHelper.GR_ID);
                        final int groupId = cursor4.getInt(groupIndex);
                        cursor4.close();

                        //ПОСМОТРЕТЬ
                        if (which == 0) {
                            //ПОЛУЧЕНИЕ ИНФОРМАЦИИ О ГРУППЕ
                            String nameAu = "", type_kz = "", nominal = "", range = "";
                            String mark = "", vein = "", section = "", workU = "", r = "", u = "", a_b = "", b_c = "", c_a = "", a_n = "", b_n = "", c_n = "", a_pe = "", b_pe = "", c_pe = "", n_pe = "", conclusion = "";
                            Cursor cursor1 = database.query(DBHelper.TABLE_GROUPS, new String[] {DBHelper.GR_ID,
                                    DBHelper.GR_AUTOMATIC, DBHelper.GR_TYPE_KZ, DBHelper.GR_NOMINAL, DBHelper.GR_RANGE,
                                    DBHelper.GR_MARK, DBHelper.GR_VEIN, DBHelper.GR_SECTION, DBHelper.GR_U1,
                                    DBHelper.GR_U2, DBHelper.GR_R, DBHelper.GR_A_B, DBHelper.GR_B_C, DBHelper.GR_C_A,
                                    DBHelper.GR_A_N, DBHelper.GR_B_N, DBHelper.GR_C_N, DBHelper.GR_A_PE,
                                    DBHelper.GR_B_PE, DBHelper.GR_C_PE, DBHelper.GR_N_PE, DBHelper.GR_CONCLUSION}, "_id = ?", new String[] {String.valueOf(groupId)}, null, null, null);
                            if (cursor1.moveToFirst()) {
                                int nameAuIndex = cursor1.getColumnIndex(DBHelper.GR_AUTOMATIC);
                                int type_kzIndex = cursor1.getColumnIndex(DBHelper.GR_TYPE_KZ);
                                int nominalIndex = cursor1.getColumnIndex(DBHelper.GR_NOMINAL);
                                int rangeIndex = cursor1.getColumnIndex(DBHelper.GR_RANGE);
                                int markIndex = cursor1.getColumnIndex(DBHelper.GR_MARK);
                                int veinIndex = cursor1. getColumnIndex(DBHelper.GR_VEIN);
                                int sectionIndex = cursor1. getColumnIndex(DBHelper.GR_SECTION);
                                int workUIndex = cursor1. getColumnIndex(DBHelper.GR_U1);
                                int uIndex = cursor1. getColumnIndex(DBHelper.GR_U2);
                                int rIndex = cursor1. getColumnIndex(DBHelper.GR_R);
                                int a_bIndex = cursor1. getColumnIndex(DBHelper.GR_A_B);
                                int b_cIndex = cursor1. getColumnIndex(DBHelper.GR_B_C);
                                int c_aIndex = cursor1. getColumnIndex(DBHelper.GR_C_A);
                                int a_nIndex = cursor1. getColumnIndex(DBHelper.GR_A_N);
                                int b_nIndex = cursor1. getColumnIndex(DBHelper.GR_B_N);
                                int c_nIndex = cursor1. getColumnIndex(DBHelper.GR_C_N);
                                int a_peIndex = cursor1. getColumnIndex(DBHelper.GR_A_PE);
                                int b_peIndex = cursor1. getColumnIndex(DBHelper.GR_B_PE);
                                int c_peIndex = cursor1. getColumnIndex(DBHelper.GR_C_PE);
                                int n_peIndex = cursor1. getColumnIndex(DBHelper.GR_N_PE);
                                int conclusionIndex = cursor1. getColumnIndex(DBHelper.GR_CONCLUSION);
                                do {
                                    nameAu = cursor1.getString(nameAuIndex);
                                    type_kz = cursor1.getString(type_kzIndex);
                                    nominal = cursor1.getString(nominalIndex);
                                    range = cursor1.getString(rangeIndex);
                                    mark = cursor1.getString(markIndex);
                                    vein = cursor1.getString(veinIndex);
                                    section = cursor1.getString(sectionIndex);
                                    workU = cursor1.getString(workUIndex);
                                    u = cursor1.getString(uIndex);
                                    r = cursor1.getString(rIndex);
                                    a_b = cursor1.getString(a_bIndex);
                                    b_c = cursor1.getString(b_cIndex);
                                    c_a = cursor1.getString(c_aIndex);
                                    a_n = cursor1.getString(a_nIndex);
                                    b_n = cursor1.getString(b_nIndex);
                                    c_n = cursor1.getString(c_nIndex);
                                    a_pe = cursor1.getString(a_peIndex);
                                    b_pe = cursor1.getString(b_peIndex);
                                    c_pe = cursor1.getString(c_peIndex);
                                    n_pe = cursor1.getString(n_peIndex);
                                    conclusion = cursor1.getString(conclusionIndex);
                                } while (cursor1.moveToNext());
                            }
                            cursor1.close();
                            AlertDialog.Builder builder4 = new AlertDialog.Builder(InsulationActivity4.this);
                            builder4.setCancelable(false);
                            builder4.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                }
                            });
                            builder4.setMessage(Html.fromHtml("<b>Раб. напряжение: </b>" + workU + "<br>" + "<b>Марка: </b>" + mark + "<br>" +
                                "<b>Кол-во жил: </b>" + vein + "<br>" + "<b>Сечение: </b>" + section + "<br>" +
                                "<b>Напряж. мегаомметра: </b>" + u + "<br>" + "<b>Доп. сопротивление: </b>" + r + "<br>" + "<b>A-B: </b>" + a_b + "<br>" +
                                "<b>B-C: </b>" + b_c + "<br>" + "<b>C-A: </b>" + c_a + "<br>" + "<b>A-N: </b>" + a_n + "<br>" + "<b>B-N: </b>" + b_n + "<br>" +
                                "<b>C-N: </b>" + c_n + "<br>" + "<b>A-PE: </b>" + a_pe + "<br>" + "<b>B-PE: </b>" + b_pe + "<br>" +
                                "<b>C-PE: </b>" + c_pe + "<br>" + "<b>N-PE: </b>" + n_pe + "<br>" + "<b>Вывод: </b>" + conclusion + "<br><br>" +
                                "<b>Наименование автомата: </b>" + nameAu + "<br>" + "<b>Тип расцепителя к.з.: </b>" + type_kz + "<br>" +
                                "<b>Номинальный ток расцепителя: </b>" + nominal + "<br>" + "<b>Диапазон тока срабатывания расцепителя к.з.: </b>" +
                                range + "<br>"));
                            builder4.setTitle(((TextView) view).getText());
                            AlertDialog dialog4 = builder4.create();
                            dialog4.show();
                        }

                        //ИЗМЕНИТЬ
                        if (which == 1) {
                            Intent intent = new Intent("android.intent.action.Insulation5");
                            intent.putExtra("nameFloor", nameFloor);
                            intent.putExtra("idFloor", idFloor);
                            intent.putExtra("nameRoom", nameRoom);
                            intent.putExtra("idRoom", idRoom);
                            intent.putExtra("nameLine", nameLine);
                            intent.putExtra("idLine", idLine);
                            intent.putExtra("nameGroup", "Группа №" + ((TextView) view).getText().toString().substring(3, ((TextView) view).getText().toString().indexOf(' ' , 3)));
                            intent.putExtra("idGroup", groupId);
                            startActivity(intent);
                        }

                        //ПОВТОРИТЬ ГРУППУ
                        if (which == 2) {
                            final String nameGroup;

                            if (((TextView) view).getText().toString().substring(3, ((TextView) view).getText().toString().indexOf(' ', 3)).contains("/")) {
                                int countSubgroup = 0;
                                String mainNumb_gr = ((TextView) view).getText().toString().substring(3, ((TextView) view).getText().toString().indexOf('/'));

                                //СЧИТАЕМ КОЛ-ВО ПОДГРУПП И ОПРЕДЕЛЯЕМ ИМЯ
                                Cursor cursor1 = database.rawQuery("SELECT count(*) FROM " + DBHelper.TABLE_GROUPS +
                                        " WHERE " + DBHelper.GR_LINE_ID + " = " + String.valueOf(idLine) + " AND " + DBHelper.GR_NAME + " LIKE '%" + mainNumb_gr + "/%'", null);
                                if (cursor1.moveToFirst()) {
                                    int countIndex = cursor1.getColumnIndex("count(*)");
                                    countSubgroup = cursor1.getInt(countIndex);
                                }
                                cursor1.close();
                                nameGroup = "Гр " + mainNumb_gr + "/" + String.valueOf(countSubgroup + 1);
                            }
                            else {
                                int countGroup = 0;

                                //СЧИТАЕМ КОЛ-ВО ГРУПП И ОПРЕДЕЛЯЕМ ИМЯ
                                Cursor cursor1 = database.rawQuery("SELECT count(*) FROM " + DBHelper.TABLE_GROUPS +
                                        " WHERE " + DBHelper.GR_LINE_ID + " = " + String.valueOf(idLine) + " AND (" + DBHelper.GR_NAME + " LIKE '%/1%'" + " OR " + DBHelper.GR_NAME + " NOT LIKE '%/%')", null);
                                if (cursor1.moveToFirst()) {
                                    int countIndex = cursor1.getColumnIndex("count(*)");
                                    countGroup = cursor1.getInt(countIndex);
                                }
                                cursor1.close();
                                nameGroup = "Гр " + String.valueOf(countGroup + 1);
                            }
                            String aut_type = "";
                            int aut_id = 0;
                            String nameAu = "", type_kz = "", nominal = "", range = "";
                            String nameMark = "", numberVein = "", numberSection = "", numberWorkU = "", numberU = "", numberR = "", concl = "";

                            //ЗАПРОС В БД ДЛЯ ПОЛУЧЕНИЯ ИНФО О ГРУППЕ
                            Cursor cursor = database.query(DBHelper.TABLE_GROUPS, new String[] {
                                    DBHelper.GR_AUTOMATIC, DBHelper.GR_TYPE_KZ, DBHelper.GR_NOMINAL, DBHelper.GR_RANGE,
                                    DBHelper.GR_MARK, DBHelper.GR_VEIN, DBHelper.GR_SECTION, DBHelper.GR_U1,
                                    DBHelper.GR_U2, DBHelper.GR_R, DBHelper.GR_CONCLUSION,
                                    DBHelper.GR_AUT_TYPE, DBHelper.GR_AUT_ID}, "_id = ?", new String[] {String.valueOf(groupId)}, null, null, null);
                            if (cursor.moveToFirst()) {
                                int nameAuIndex = cursor.getColumnIndex(DBHelper.GR_AUTOMATIC);
                                int type_kzIndex = cursor.getColumnIndex(DBHelper.GR_TYPE_KZ);
                                int nominalIndex = cursor.getColumnIndex(DBHelper.GR_NOMINAL);
                                int rangeIndex = cursor.getColumnIndex(DBHelper.GR_RANGE);
                                int markIndex = cursor.getColumnIndex(DBHelper.GR_MARK);
                                int veinIndex = cursor.getColumnIndex(DBHelper.GR_VEIN);
                                int sectionIndex = cursor.getColumnIndex(DBHelper.GR_SECTION);
                                int workUIndex = cursor.getColumnIndex(DBHelper.GR_U1);
                                int uIndex = cursor.getColumnIndex(DBHelper.GR_U2);
                                int rIndex = cursor.getColumnIndex(DBHelper.GR_R);
                                int conclIndex = cursor.getColumnIndex(DBHelper.GR_CONCLUSION);
                                int aut_typeIndex = cursor.getColumnIndex(DBHelper.GR_AUT_TYPE);
                                int aut_idIndex = cursor.getColumnIndex(DBHelper.GR_AUT_ID);
                                nameAu = cursor.getString(nameAuIndex);
                                type_kz = cursor.getString(type_kzIndex);
                                nominal = cursor.getString(nominalIndex);
                                range = cursor.getString(rangeIndex);
                                nameMark = cursor.getString(markIndex);
                                numberVein = cursor.getString(veinIndex);
                                numberSection = cursor.getString(sectionIndex);
                                numberWorkU = cursor.getString(workUIndex);
                                numberU = cursor.getString(uIndex);
                                numberR = cursor.getString(rIndex);
                                concl = cursor.getString(conclIndex);
                                aut_type = cursor.getString(aut_typeIndex);
                                aut_id = cursor.getInt(aut_idIndex);
                            }
                            cursor.close();

                            //ЕСЛИ ОНА РЕЗЕРВ
                            if (nameMark.equals("резерв")) {
                                final String finalNameAu = nameAu;
                                final String finalType_kz = type_kz;
                                final String finalNominal = nominal;
                                final String finalRange = range;
                                final int finalAut_id = aut_id;
                                final String finalAut_type = aut_type;
                                AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity4.this);
                                final View myView2 = getLayoutInflater().inflate(R.layout.dialog_for_names,null);
                                alert.setCancelable(false);
                                if (!nameGroup.contains("/")) {
                                    alert.setTitle("Введите обозначение по схеме:");
                                    openKeyboard();
                                }
                                else
                                    alert.setMessage("Вы уверены, что хотите повторить резервную подгруппу?");
                                final EditText symbolEdit = myView2.findViewById(R.id.editText);
                                alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        closeKeyboard(myView2);
                                        String sym = symbolEdit.getText().toString();
                                        if (!sym.equals("") || nameGroup.contains("/")) {
                                            int newAutId = finalAut_id;
                                            if (!nameGroup.contains("/"))
                                                newAutId = addAutomat(database, finalAut_id, finalAut_type, sym);
                                            ContentValues contentValues = new ContentValues();
                                            contentValues.put(DBHelper.GR_LINE_ID, idLine);
                                            contentValues.put(DBHelper.GR_AUTOMATIC, finalNameAu);
                                            contentValues.put(DBHelper.GR_TYPE_KZ, finalType_kz);
                                            contentValues.put(DBHelper.GR_NOMINAL, finalNominal);
                                            contentValues.put(DBHelper.GR_RANGE, finalRange);
                                            contentValues.put(DBHelper.GR_NAME, nameGroup);
                                            contentValues.put(DBHelper.GR_U1, "-");
                                            contentValues.put(DBHelper.GR_MARK, "резерв");
                                            contentValues.put(DBHelper.GR_VEIN, "-");
                                            contentValues.put(DBHelper.GR_SECTION, "-");
                                            contentValues.put(DBHelper.GR_U2, "-");
                                            contentValues.put(DBHelper.GR_R, "-");
                                            contentValues.put(DBHelper.GR_PHASE, "-");
                                            contentValues.put(DBHelper.GR_A_B, "-");
                                            contentValues.put(DBHelper.GR_B_C, "-");
                                            contentValues.put(DBHelper.GR_C_A, "-");
                                            contentValues.put(DBHelper.GR_A_N, "-");
                                            contentValues.put(DBHelper.GR_B_N, "-");
                                            contentValues.put(DBHelper.GR_C_N, "-");
                                            contentValues.put(DBHelper.GR_A_PE, "-");
                                            contentValues.put(DBHelper.GR_B_PE, "-");
                                            contentValues.put(DBHelper.GR_C_PE, "-");
                                            contentValues.put(DBHelper.GR_N_PE, "-");
                                            contentValues.put(DBHelper.GR_CONCLUSION, "-");
                                            contentValues.put(DBHelper.GR_AUT_TYPE, finalAut_type);
                                            contentValues.put(DBHelper.GR_AUT_ID, newAutId);
                                            database.insert(DBHelper.TABLE_GROUPS, null, contentValues);
                                            swapGroups(groups.getAdapter().getCount() - position, groups.getAdapter().getCount() + 1, idLine, database);
                                            addSpisokGroups(database, groups, idLine);
                                            Toast toast2 = Toast.makeText(getApplicationContext(),
                                                    "Группа добавлена", Toast.LENGTH_SHORT);
                                            toast2.show();
                                        }
                                        else {
                                            AlertDialog.Builder alert10 = new AlertDialog.Builder(InsulationActivity4.this);
                                            alert10.setCancelable(false);
                                            alert10.setMessage("Ошибка. Поле оказалось пустым.");
                                            alert10.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int whichButton) {

                                                }
                                            });
                                            alert10.show();
                                        }
                                    }
                                });
                                alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        closeKeyboard(myView2);
                                    }
                                });
                                if (!nameGroup.contains("/"))
                                    alert.setView(myView2);
                                alert.show();
                            }
                            //ЕСЛИ НЕ РЕЗЕРВ
                            else {
                                if (!concl.equals("не соотв.")) {
                                    changePhase(groups, database, idLine, nameAu, type_kz, nominal, range, nameGroup,
                                            numberWorkU, nameMark, numberVein, numberSection, numberU, numberR, position, aut_type, aut_id);
                                }
                                else {
                                    AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity4.this);
                                    alert.setCancelable(false);
                                    alert.setMessage("Нельзя повторить группу, не соответсвующую нормативному документу!");
                                    alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {

                                        }
                                    });
                                    alert.show();
                                }
                            }
                        }

                        //ДОБАВИТЬ ПОДГРУППУ
                        if (which == 3) {
                            String nameGroup;
                            String mainNumb_gr;

                            if (((TextView) view).getText().toString().substring(3, ((TextView) view).getText().toString().indexOf(' ', 3)).contains("/")) {
                                int countSubgroup = 0;
                                mainNumb_gr = ((TextView) view).getText().toString().substring(3, ((TextView) view).getText().toString().indexOf('/'));

                                //СЧИТАЕМ КОЛ-ВО ПОДГРУПП И ОПРЕДЕЛЯЕМ ИМЯ
                                Cursor cursor1 = database.rawQuery("SELECT count(*) FROM " + DBHelper.TABLE_GROUPS +
                                        " WHERE " + DBHelper.GR_LINE_ID + " = " + String.valueOf(idLine) + " AND " + DBHelper.GR_NAME + " LIKE '%" + mainNumb_gr + "/%'", null);
                                if (cursor1.moveToFirst()) {
                                    int countIndex = cursor1.getColumnIndex("count(*)");
                                    countSubgroup = cursor1.getInt(countIndex);
                                }
                                cursor1.close();
                                nameGroup = "Группа №" + mainNumb_gr + "/" + String.valueOf(countSubgroup + 1);
                            } else {
                                mainNumb_gr = ((TextView) view).getText().toString().substring(3, ((TextView) view).getText().toString().indexOf(' ', 3));

                                //меняем название группы, добавляя "/1"
                                ContentValues uppname = new ContentValues();
                                uppname.put(DBHelper.GR_NAME, "Гр " + mainNumb_gr + "/1");
                                database.update(DBHelper.TABLE_GROUPS,
                                        uppname,
                                        "_id = ?",
                                        new String[]{String.valueOf(groupId)});
                                nameGroup = "Группа №" + mainNumb_gr + "/2";
                            }
                            Intent intent = new Intent("android.intent.action.Insulation5");
                            intent.putExtra("nameFloor", nameFloor);
                            intent.putExtra("idFloor", idFloor);
                            intent.putExtra("nameRoom", nameRoom);
                            intent.putExtra("idRoom", idRoom);
                            intent.putExtra("nameLine", nameLine);
                            intent.putExtra("idLine", idLine);
                            intent.putExtra("nameGroup", nameGroup);
                            intent.putExtra("idSubgroup", groupId);
                            intent.putExtra("currentIndexSwap", groups.getAdapter().getCount() + 1);
                            intent.putExtra("stopIndexSwap", groups.getAdapter().getCount() - position);
                            startActivity(intent);
                        }

                        //УДАЛИТЬ ГРУППУ
                        if (which == 4) {
                            //ПОДТВЕРЖДЕНИЕ
                            AlertDialog.Builder builder4 = new AlertDialog.Builder(InsulationActivity4.this);
                            builder4.setCancelable(false);
                            builder4.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    //ДОСТАНЕМ АЙДИ И ТИП АВТОМАТА,КОТОРЫЙ ЗАТЕМ УДАЛИМ
                                    int aut_id = 0;
                                    String aut_type = "";
                                    Cursor cursor30 = database.query(DBHelper.TABLE_GROUPS, new String[] { DBHelper.GR_AUT_TYPE,
                                            DBHelper.GR_AUT_ID}, "_id = ?", new String[] {String.valueOf(groupId)}, null, null, null);
                                    if (cursor30.moveToFirst()) {
                                        int typeIndex = cursor30.getColumnIndex(DBHelper.GR_AUT_TYPE);
                                        int idIndex = cursor30.getColumnIndex(DBHelper.GR_AUT_ID);
                                        aut_id = cursor30.getInt(idIndex);
                                        aut_type = cursor30.getString(typeIndex);
                                    }
                                    cursor30.close();

                                    database.delete(DBHelper.TABLE_GROUPS, "_id = ?", new String[] {String.valueOf(groupId)});

                                    if (!((TextView) view).getText().toString().substring(3, ((TextView) view).getText().toString().indexOf(' ', 3)).contains("/")) {
                                        //УДАЛИМ АВТОМАТ
                                        if (aut_type.equals("dif_aut"))
                                            database.delete(DBHelper.TABLE_DIF_AUTOMATICS, "_id = ?", new String[] {String.valueOf(aut_id)});
                                        else
                                            database.delete(DBHelper.TABLE_AUTOMATICS, "_id = ?", new String[] {String.valueOf(aut_id)});

                                        //МЕНЯЕМ НАЗВАНИЯ У НУЖНЫХ ГРУПП И ПОДГРУПП
                                        Cursor cursor = database.query(DBHelper.TABLE_GROUPS, new String[] {DBHelper.GR_ID, DBHelper.GR_NAME, DBHelper.GR_LINE_ID}, "_id > ? and grline_id = ?", new String[] {String.valueOf(groupId), String.valueOf(idLine)}, null, null, null);
                                        if (cursor.moveToFirst()) {
                                            int namechangeIndex = cursor.getColumnIndex(DBHelper.GR_NAME);
                                            int groupidIndex = cursor.getColumnIndex(DBHelper.GR_ID);
                                            String nameCurrent;
                                            do {
                                                nameCurrent = cursor.getString(namechangeIndex);
                                                ContentValues uppnameGroup = new ContentValues();
                                                if (!nameCurrent.contains("/"))
                                                    uppnameGroup.put(DBHelper.GR_NAME, "Гр " + String.valueOf(Integer.parseInt(nameCurrent.substring(3)) - 1));
                                                else
                                                    uppnameGroup.put(DBHelper.GR_NAME, "Гр " + String.valueOf(Integer.parseInt(nameCurrent.substring(3, nameCurrent.indexOf('/'))) - 1) + "/" + nameCurrent.substring(nameCurrent.indexOf('/') + 1));
                                                database.update(DBHelper.TABLE_GROUPS,
                                                        uppnameGroup,
                                                        "_id = ?",
                                                        new String[]{cursor.getString(groupidIndex)});
                                            } while (cursor.moveToNext());
                                        }
                                        cursor.close();
                                    }
                                    else {
                                        String mainNumb_gr = ((TextView) view).getText().toString().substring(3, ((TextView) view).getText().toString().indexOf('/'));
                                        int countSubgroup = 0;

                                        //ОПРЕДЕЛЯЕМ СКОЛЬКО ПОДГРУПП ОСТАЛОСЬ
                                        Cursor cursor4 = database.rawQuery("SELECT count(*) FROM " + DBHelper.TABLE_GROUPS +
                                                " WHERE " + DBHelper.GR_LINE_ID + " = " + String.valueOf(idLine) + " AND " + DBHelper.GR_NAME + " LIKE '%" + mainNumb_gr + "/%'", null);
                                        if (cursor4.moveToFirst()) {
                                            int countIndex = cursor4.getColumnIndex("count(*)");
                                            countSubgroup = cursor4.getInt(countIndex);
                                        }
                                        cursor4.close();

                                        if (countSubgroup != 1) {
                                            //МЕНЯЕМ НАЗВАНИЯ У НУЖНЫХ ПОДГРУПП
                                            Cursor cursor3 = database.rawQuery("SELECT _id, name_group FROM " + DBHelper.TABLE_GROUPS +
                                                    " WHERE " + DBHelper.GR_LINE_ID + " = " + String.valueOf(idLine) + " AND _id > " + String.valueOf(groupId) + " AND " + DBHelper.GR_NAME + " LIKE '%" + mainNumb_gr + "/%'", null);
                                            if (cursor3.moveToFirst()) {
                                                int _idIndex = cursor3.getColumnIndex(DBHelper.GR_ID);
                                                int nameCurrentIndex = cursor3.getColumnIndex(DBHelper.GR_NAME);
                                                String _id, nameCurrent;
                                                do {
                                                    _id = cursor3.getString(_idIndex);
                                                    nameCurrent = cursor3.getString(nameCurrentIndex);
                                                    ContentValues uppnameGroup = new ContentValues();
                                                    uppnameGroup.put(DBHelper.GR_NAME, "Гр " + nameCurrent.substring(3, nameCurrent.indexOf('/')) + "/" + String.valueOf(Integer.parseInt(nameCurrent.substring(nameCurrent.indexOf('/') + 1)) - 1));
                                                    database.update(DBHelper.TABLE_GROUPS,
                                                            uppnameGroup,
                                                            "_id = ?",
                                                            new String[]{_id});
                                                } while (cursor3.moveToNext());
                                            }
                                            cursor3.close();
                                        }
                                        else {
                                            ContentValues uppnameGroup = new ContentValues();
                                            uppnameGroup.put(DBHelper.GR_NAME, "Гр " + mainNumb_gr);
                                            database.update(DBHelper.TABLE_GROUPS,
                                                    uppnameGroup,
                                                    "grline_id = ? and name_group like '%" + mainNumb_gr + "/%'",
                                                    new String[]{String.valueOf(idLine)});
                                        }
                                    }
                                    //ЗАПРОС В БД И ЗАПОЛНЕНИЕ СПИСКА ГРУПП
                                    addSpisokGroups(database, groups, idLine);
                                    Toast toast2 = Toast.makeText(getApplicationContext(),
                                            "Группа удалена", Toast.LENGTH_SHORT);
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

        //ГОТОВО
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.Insulation3");
                intent.putExtra("nameFloor", nameFloor);
                intent.putExtra("idFloor", idFloor);
                intent.putExtra("nameRoom", nameRoom);
                intent.putExtra("idRoom", idRoom);
                startActivity(intent);
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
                Intent intent = new Intent("android.intent.action.Insulation3");
                intent.putExtra("nameFloor", nameFloor);
                intent.putExtra("idFloor", idFloor);
                intent.putExtra("nameRoom", nameRoom);
                intent.putExtra("idRoom", idRoom);
                startActivity(intent);
                return true;
            case R.id.action_main:
                Intent intent1 = new Intent(InsulationActivity4.this, MenuItemsActivity.class);
                startActivity(intent1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //ГЕНЕРАЦИЯ ЧИСЛА
    public String getRandomNumber(String x) {
        if (x.contains(","))
            return x;
        int oldNumb = Integer.parseInt(x);
        int random = 0;
        Random generator = new Random();
        if (oldNumb <= 20)
            random = oldNumb;
        if (20 < oldNumb && oldNumb <= 50)
            random = (generator.nextInt(7) - 3) * 5 + oldNumb;
        if (50 < oldNumb && oldNumb <= 100)
            random = (generator.nextInt(11) - 5) * 10 + oldNumb;
        if (100 < oldNumb && oldNumb < 300)
            random = (generator.nextInt(21) - 10) * 10 + oldNumb;
        if (300 <= oldNumb && oldNumb < 500)
            random = (generator.nextInt(9) - 4) * 50 + oldNumb;
        if (500 <= oldNumb && oldNumb < 1000)
            random = (generator.nextInt(5) - 2) * 100 + oldNumb;
        if (1000 <= oldNumb && oldNumb < 3000 )
            random = (generator.nextInt(9) - 4) * 100 + oldNumb;
        if (3000 <= oldNumb)
            random = (generator.nextInt(11) - 5) * 200 + oldNumb;
        return String.valueOf(random);
    }

    public void pushArray(String[] arr, String num) {
        int i, count = 0;
        for (i = 0; i < arr.length; i++) {
            arr[i] = getRandomNumber(num);
        }
        for (i = 0; i < arr.length; i++) {
            if (arr[i].equals(num))
                count++;
        }
        if (count == arr.length && Integer.parseInt(num) > 20)
            pushArray(arr, num);
    }

    //РЕКУРСИВНАЯ ИЗМЕНА ФАЗЫ
    void changePhase(final ListView groups, final SQLiteDatabase database, final int idLine, final String nameAu,
                     final String type_kz, final String nominal, final String range, final String nameGroup,
                     final String numberWorkU, final String nameMark, final String numberVein, final String numberSection,
                     final String numberU, final String numberR, final int position, final String aut_type, final int aut_id) {
        AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity4.this);
        final View myView = getLayoutInflater().inflate(R.layout.dialog_for_repeat_group,null);
        alert.setCancelable(false);
        alert.setTitle("Введите необходимые параметры:");
        openKeyboard();
        final EditText symbolEdit = myView.findViewById(R.id.editText);
        final RadioGroup grradio = myView.findViewById(R.id.radioGroup);
        final RadioButton phaseA = myView.findViewById(R.id.phaseA);
        final RadioButton phaseB = myView.findViewById(R.id.phaseB);
        final EditText input = myView.findViewById(R.id.editText2);
        if (numberVein.equals("4") || numberVein.equals("5"))
            grradio.setVisibility(View.GONE);
        alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                closeKeyboard(myView);
                String numb = input.getText().toString();
                String symbol = symbolEdit.getText().toString();
                if (numb.equals("") || symbol.equals("")) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity4.this);
                    alert.setCancelable(false);
                    alert.setMessage("Заполните все поля!");
                    alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            changePhase(groups, database, idLine, nameAu, type_kz, nominal, range, nameGroup,
                                    numberWorkU, nameMark, numberVein, numberSection, numberU, numberR, position, aut_type, aut_id);
                        }
                    });
                    alert.show();
                }
                else
                    if (numb.contains(",") && Double.parseDouble(numb.replace(",",".")) > 1) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity4.this);
                        alert.setCancelable(false);
                        alert.setMessage("Число не может быть дробным, если оно больше единицы!");
                        alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                changePhase(groups, database, idLine, nameAu, type_kz, nominal, range, nameGroup,
                                        numberWorkU, nameMark, numberVein, numberSection, numberU, numberR, position, aut_type, aut_id);
                            }
                        });
                        alert.show();
                    }
                    else {
                        int newAutId = aut_id;
                        if (!nameGroup.contains("/"))
                            newAutId = addAutomat(database, aut_id, aut_type, symbol);
                        String namePhase;
                        if (numberVein.equals("2") || numberVein.equals("3")) {
                            if (phaseA.isChecked())
                                namePhase = "A";
                            else
                                if (phaseB.isChecked())
                                    namePhase = "B";
                                else
                                    namePhase = "C";
                        }
                        else
                            namePhase = "-";
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(DBHelper.GR_LINE_ID, idLine);
                        contentValues.put(DBHelper.GR_AUTOMATIC, nameAu);
                        contentValues.put(DBHelper.GR_TYPE_KZ, type_kz);
                        contentValues.put(DBHelper.GR_NOMINAL, nominal);
                        contentValues.put(DBHelper.GR_RANGE, range);
                        contentValues.put(DBHelper.GR_NAME, nameGroup);
                        contentValues.put(DBHelper.GR_U1, numberWorkU);
                        contentValues.put(DBHelper.GR_MARK, nameMark);
                        contentValues.put(DBHelper.GR_VEIN, numberVein);
                        contentValues.put(DBHelper.GR_SECTION, numberSection);
                        contentValues.put(DBHelper.GR_U2, numberU);
                        contentValues.put(DBHelper.GR_R, numberR);
                        contentValues.put(DBHelper.GR_PHASE, namePhase);
                        if (Double.parseDouble(numb.replace(",", ".")) >= Double.parseDouble(numberR.replace(",", ".")))
                            contentValues.put(DBHelper.GR_CONCLUSION, "соответст.");
                        else
                            contentValues.put(DBHelper.GR_CONCLUSION, "не соотв.");
                        contentValues.put(DBHelper.GR_AUT_TYPE, aut_type);
                        contentValues.put(DBHelper.GR_AUT_ID, newAutId);
                        //2 ЖИЛЫ
                        if (numberVein.equals("2")) {
                            contentValues.put(DBHelper.GR_A_B, "-");
                            contentValues.put(DBHelper.GR_B_C, "-");
                            contentValues.put(DBHelper.GR_C_A, "-");
                            contentValues.put(DBHelper.GR_A_PE, "-");
                            contentValues.put(DBHelper.GR_B_PE, "-");
                            contentValues.put(DBHelper.GR_C_PE, "-");
                            contentValues.put(DBHelper.GR_N_PE, "-");
                            if (namePhase.equals("A")) {
                                contentValues.put(DBHelper.GR_A_N, numb);
                                contentValues.put(DBHelper.GR_B_N, "-");
                                contentValues.put(DBHelper.GR_C_N, "-");
                            }
                            if (namePhase.equals("B")) {
                                contentValues.put(DBHelper.GR_A_N, "-");
                                contentValues.put(DBHelper.GR_B_N, numb);
                                contentValues.put(DBHelper.GR_C_N, "-");
                            }
                            if (namePhase.equals("C")) {
                                contentValues.put(DBHelper.GR_A_N, "-");
                                contentValues.put(DBHelper.GR_B_N, "-");
                                contentValues.put(DBHelper.GR_C_N, numb);
                            }
                        }
                        //3 ЖИЛЫ
                        if (numberVein.equals("3")) {
                            String[] arrayRand = new String[2];
                            pushArray(arrayRand, numb);
                            contentValues.put(DBHelper.GR_A_B, "-");
                            contentValues.put(DBHelper.GR_B_C, "-");
                            contentValues.put(DBHelper.GR_C_A, "-");
                            if (namePhase.equals("A")) {
                                contentValues.put(DBHelper.GR_A_N, numb);
                                contentValues.put(DBHelper.GR_B_N, "-");
                                contentValues.put(DBHelper.GR_C_N, "-");
                                contentValues.put(DBHelper.GR_A_PE, arrayRand[0]);
                                contentValues.put(DBHelper.GR_B_PE, "-");
                                contentValues.put(DBHelper.GR_C_PE, "-");
                                contentValues.put(DBHelper.GR_N_PE, arrayRand[1]);
                            }
                            if (namePhase.equals("B")) {
                                contentValues.put(DBHelper.GR_A_N, "-");
                                contentValues.put(DBHelper.GR_B_N, numb);
                                contentValues.put(DBHelper.GR_C_N, "-");
                                contentValues.put(DBHelper.GR_A_PE, "-");
                                contentValues.put(DBHelper.GR_B_PE, arrayRand[0]);
                                contentValues.put(DBHelper.GR_C_PE, "-");
                                contentValues.put(DBHelper.GR_N_PE, arrayRand[1]);
                            }
                            if (namePhase.equals("C")) {
                                contentValues.put(DBHelper.GR_A_N, "-");
                                contentValues.put(DBHelper.GR_B_N, "-");
                                contentValues.put(DBHelper.GR_C_N, numb);
                                contentValues.put(DBHelper.GR_A_PE, "-");
                                contentValues.put(DBHelper.GR_B_PE, "-");
                                contentValues.put(DBHelper.GR_C_PE, arrayRand[0]);
                                contentValues.put(DBHelper.GR_N_PE, arrayRand[1]);
                            }
                        }
                        //4 ЖИЛЫ
                        if (numberVein.equals("4")) {
                            String[] arrayRand = new String[5];
                            pushArray(arrayRand, numb);
                            contentValues.put(DBHelper.GR_A_B, numb);
                            contentValues.put(DBHelper.GR_B_C, arrayRand[0]);
                            contentValues.put(DBHelper.GR_C_A, arrayRand[1]);
                            contentValues.put(DBHelper.GR_A_N, arrayRand[2]);
                            contentValues.put(DBHelper.GR_B_N, arrayRand[3]);
                            contentValues.put(DBHelper.GR_C_N, arrayRand[4]);
                            contentValues.put(DBHelper.GR_A_PE, "-");
                            contentValues.put(DBHelper.GR_B_PE, "-");
                            contentValues.put(DBHelper.GR_C_PE, "-");
                            contentValues.put(DBHelper.GR_N_PE, "-");
                        }
                        //5 ЖИЛ
                        if (numberVein.equals("5")) {
                            String[] arrayRand = new String[9];
                            pushArray(arrayRand, numb);
                            contentValues.put(DBHelper.GR_A_B, numb);
                            contentValues.put(DBHelper.GR_B_C, arrayRand[0]);
                            contentValues.put(DBHelper.GR_C_A, arrayRand[1]);
                            contentValues.put(DBHelper.GR_A_N, arrayRand[2]);
                            contentValues.put(DBHelper.GR_B_N, arrayRand[3]);
                            contentValues.put(DBHelper.GR_C_N, arrayRand[4]);
                            contentValues.put(DBHelper.GR_A_PE, arrayRand[5]);
                            contentValues.put(DBHelper.GR_B_PE, arrayRand[6]);
                            contentValues.put(DBHelper.GR_C_PE, arrayRand[7]);
                            contentValues.put(DBHelper.GR_N_PE, arrayRand[8]);
                        }
                        database.insert(DBHelper.TABLE_GROUPS, null, contentValues);
                        swapGroups(groups.getAdapter().getCount() - position, groups.getAdapter().getCount() + 1, idLine, database);
                        addSpisokGroups(database, groups, idLine);
                        Toast toast2 = Toast.makeText(getApplicationContext(),
                                "Группа добавлена", Toast.LENGTH_SHORT);
                        toast2.show();
                    }
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

    public void swapGroups(int positionStop, int positionCurrent, int idLine, SQLiteDatabase database) {
        if (positionCurrent - positionStop == 1)
            return;
        int idCurrent, idUp, groupIndex, groupNameIndex;
        String nameCurrent, nameUp;
        ContentValues values;

        //ЗАПРОС В БД ДЛЯ ПОЛУЧЕНИЯ ID
        Cursor cursor4 = database.query(DBHelper.TABLE_GROUPS, new String[] {DBHelper.GR_ID, DBHelper.GR_NAME}, "grline_id = ?", new String[] {String.valueOf(idLine)}, null, null, null);
        cursor4.moveToPosition(positionCurrent - 1);
        groupIndex = cursor4.getColumnIndex(DBHelper.GR_ID);
        groupNameIndex = cursor4.getColumnIndex(DBHelper.GR_NAME);
        idCurrent = cursor4.getInt(groupIndex);
        nameCurrent = cursor4.getString(groupNameIndex);
        cursor4.close();

        while (positionCurrent - positionStop != 1) {

            //ЗАПРОС В БД ДЛЯ ПОЛУЧЕНИЯ ID И НАЗВАНИЯ ГРУППЫ ПОВЫШЕ
            Cursor cursor = database.query(DBHelper.TABLE_GROUPS, new String[] {DBHelper.GR_ID, DBHelper.GR_NAME}, "grline_id = ?", new String[] {String.valueOf(idLine)}, null, null, null);
            cursor.moveToPosition(positionCurrent - 2);
            groupIndex = cursor.getColumnIndex(DBHelper.GR_ID);
            groupNameIndex = cursor4.getColumnIndex(DBHelper.GR_NAME);
            idUp = cursor.getInt(groupIndex);
            nameUp = cursor.getString(groupNameIndex);
            cursor.close();

            //МЕНЯЕМ НАЗВАНИЕ
            if (!nameCurrent.contains("/"))
                if (nameUp.contains("/")) {
                    values = new ContentValues();
                    values.put(DBHelper.GR_NAME, "Гр " + nameCurrent.substring(3) + "/" + nameUp.substring(nameUp.indexOf('/') + 1));
                    database.update(DBHelper.TABLE_GROUPS, values, "_id = ?", new String[]{Integer.toString(idUp)});
                    if (nameUp.contains("/1")) {
                        values = new ContentValues();
                        values.put(DBHelper.GR_NAME, "Гр " + nameUp.substring(3, nameUp.indexOf('/')));
                        database.update(DBHelper.TABLE_GROUPS, values, "_id = ?", new String[]{Integer.toString(idCurrent)});
                        nameCurrent = "Гр " + nameUp.substring(3, nameUp.indexOf('/'));
                    }
                }
                else {
                    values = new ContentValues();
                    values.put(DBHelper.GR_NAME, "Гр " + nameUp.substring(3));
                    database.update(DBHelper.TABLE_GROUPS, values, "_id = ?", new String[]{Integer.toString(idCurrent)});
                    values = new ContentValues();
                    values.put(DBHelper.GR_NAME, "Гр " + nameCurrent.substring(3));
                    database.update(DBHelper.TABLE_GROUPS, values, "_id = ?", new String[]{Integer.toString(idUp)});
                    nameCurrent = "Гр " + nameUp.substring(3);
                }
            else
                if (nameUp.contains("/") && nameCurrent.substring(3, nameCurrent.indexOf('/')).equals(nameUp.substring(3, nameUp.indexOf('/')))) {
                    values = new ContentValues();
                    values.put(DBHelper.GR_NAME, "Гр " + nameCurrent.substring(3, nameCurrent.indexOf('/')) + "/" + nameUp.substring(nameUp.indexOf('/') + 1));
                    database.update(DBHelper.TABLE_GROUPS, values, "_id = ?", new String[]{Integer.toString(idCurrent)});
                    values = new ContentValues();
                    values.put(DBHelper.GR_NAME, "Гр " + nameUp.substring(3, nameUp.indexOf('/')) + "/" + nameCurrent.substring(nameCurrent.indexOf('/') + 1));
                    database.update(DBHelper.TABLE_GROUPS, values, "_id = ?", new String[]{Integer.toString(idUp)});
                    nameCurrent = "Гр " + nameCurrent.substring(3, nameCurrent.indexOf('/')) + "/" + nameUp.substring(nameUp.indexOf('/') + 1);
                }

            //МЕНЯЕМ ID
            values = new ContentValues();
            values.put(DBHelper.GR_ID, "-1");
            database.update(DBHelper.TABLE_GROUPS, values,"_id = ?", new String[]{Integer.toString(idUp)});
            values = new ContentValues();
            values.put(DBHelper.GR_ID, Integer.toString(idUp));
            database.update(DBHelper.TABLE_GROUPS, values,"_id = ?", new String[]{Integer.toString(idCurrent)});
            values = new ContentValues();
            values.put(DBHelper.GR_ID, Integer.toString(idCurrent));
            database.update(DBHelper.TABLE_GROUPS, values,"_id = ?", new String[]{"-1"});

            idCurrent = idUp;
            positionCurrent--;
        }
    }

    public int addAutomat (SQLiteDatabase database, final int aut_id, final String aut_type, final String symbol) {
        int new_id = -1;
        if (aut_type.equals("normal_aut")) {
            String place = "", type_overload = "", type_kz = "", excerpt = "", nom1 = "", nom2 = "", set_overload = "", name = "";
            String set_kz = "", test_tok = "", time_permissible = "", length_annex = "", conclusion = "";
            Cursor cursor1 = database.query(DBHelper.TABLE_AUTOMATICS, new String[] {DBHelper.AU_ID, DBHelper.AU_PLACE, DBHelper.AU_NAME,
                            DBHelper.AU_TYPE_OVERLOAD, DBHelper.AU_TYPE_KZ, DBHelper.AU_EXCERPT, DBHelper.AU_NOMINAL_1,
                            DBHelper.AU_NOMINAL_2, DBHelper.AU_SET_OVERLOAD, DBHelper.AU_SET_KZ,
                            DBHelper.AU_TEST_TOK, DBHelper.AU_TIME_PERMISSIBLE, DBHelper.AU_LENGTH_ANNEX, DBHelper.AU_CONCLUSION},
                    "_id = ?", new String[] {String.valueOf(aut_id)}, null, null, null);
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
                contentValues.put(DBHelper.AU_SYMBOL_SCHEME, symbol);
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
            }
        }
        else {
            String uzo = "", type_switch = "", u = "", set_thermal = "", set_electromagn = "", check_test_tok = "", check_time = "";
            String check_work_tok = "", i_nom = "", i_leack = "", i_extra = "", i_measured = "", time_extra = "", time_measured = "", conclusion = "";
            Cursor cursor1 = database.query(DBHelper.TABLE_DIF_AUTOMATICS, new String[] {DBHelper.DIF_AU_ID,
                    DBHelper.DIF_AU_UZO, DBHelper.DIF_AU_TYPE_SWITCH, DBHelper.DIF_AU_U,
                    DBHelper.DIF_AU_SET_THERMAL, DBHelper.DIF_AU_SET_ELECTR_MAGN, DBHelper.DIF_AU_CHECK_TEST_TOK, DBHelper.DIF_AU_CHECK_TIME_, DBHelper.DIF_AU_CHECK_WORK_TOK,
                    DBHelper.DIF_AU_I_NOM, DBHelper.DIF_AU_I_LEAK, DBHelper.DIF_AU_I_EXTRA, DBHelper.DIF_AU_I_MEASURED,
                    DBHelper.DIF_AU_TIME_EXTRA, DBHelper.DIF_AU_TIME_MEASURED, DBHelper.DIF_AU_CONCLUSION}, "_id = ?", new String[] {String.valueOf(aut_id)}, null, null, null);
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
                contentValues.put(DBHelper.DIF_AU_PLACE, symbol);
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
                contentValues.put(DBHelper.DIF_AU_TIME_MEASURED, getDifTimeMeasured(time_measured, time_extra));
                contentValues.put(DBHelper.DIF_AU_CONCLUSION, conclusion);
                database.insert(DBHelper.TABLE_DIF_AUTOMATICS, null, contentValues);
            }
        }
        Cursor cursor3 = database.rawQuery("SELECT last_insert_rowid() as _id", new String[]{});
        if (cursor3.moveToFirst()) {
            int idIndex = cursor3.getColumnIndex("_id");
            new_id = cursor3.getInt(idIndex);
        }
        cursor3.close();
        return new_id;
    }

    //ОБНОВЛЕНИЕ СПИСКА
    public void addSpisokGroups(SQLiteDatabase database, ListView groups, int idLine) {
        final ArrayList<String> spisokGroups = new ArrayList <String>();
        Cursor cursor = database.query(DBHelper.TABLE_GROUPS, new String[] {DBHelper.GR_NAME, DBHelper.GR_MARK,
                DBHelper.GR_VEIN, DBHelper.GR_SECTION, DBHelper.GR_PHASE}, "grline_id = ?", new String[] {String.valueOf(idLine)}, null, null, "_id DESC");
        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(DBHelper.GR_NAME);
            int markIndex = cursor.getColumnIndex(DBHelper.GR_MARK);
            int veinIndex = cursor. getColumnIndex(DBHelper.GR_VEIN);
            int sectionIndex = cursor. getColumnIndex(DBHelper.GR_SECTION);
            int phaseIndex = cursor. getColumnIndex(DBHelper.GR_PHASE);
            do {
                String nameMark = cursor.getString(markIndex);
                String numberVein = cursor.getString(veinIndex);
                if (nameMark.equals("резерв"))
                    spisokGroups.add(cursor.getString(nameIndex) + " (Резерв)");
                else
                    if (Integer.parseInt(numberVein) == 2 || Integer.parseInt(numberVein) == 3)
                        spisokGroups.add(cursor.getString(nameIndex) + " (" + nameMark + "; " + numberVein + "x" + cursor.getString(sectionIndex) +
                            "; Фаза " + cursor.getString(phaseIndex) + ")");
                    else
                        spisokGroups.add(cursor.getString(nameIndex) + " (" + nameMark + "; " + numberVein + "x" + cursor.getString(sectionIndex) + ")");
            } while (cursor.moveToNext());
        }
        cursor.close();
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, spisokGroups);
        groups.setAdapter(adapter);
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

    public String getDifTimeMeasured(String time_prev, String limit) {
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
