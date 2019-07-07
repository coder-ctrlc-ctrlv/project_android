package com.example.protokol.create_protokol;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class InsulationActivity4 extends AppCompatActivity {

    DBHelper dbHelper;
    String nameRoom, nameLine;
    int idRoom, idLine;
    Switch reserve;
    TextView mark, vein, section, workU, u, r, phase;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insulation4);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        final TextView group = findViewById(R.id.textView8);
        reserve = findViewById(R.id.switch3);
        mark = findViewById(R.id.textView12);
        vein = findViewById(R.id.textView13);
        section = findViewById(R.id.textView14);
        workU = findViewById(R.id.textView15);
        u = findViewById(R.id.textView16);
        r = findViewById(R.id.textView17);
        phase = findViewById(R.id.textView18);
        final EditText number = findViewById(R.id.editText2);
        Button save = findViewById(R.id.button17);

        nameRoom = getIntent().getStringExtra("nameRoom");
        idRoom = getIntent().getIntExtra("idRoom", 0);
        nameLine = getIntent().getStringExtra("nameLine");
        idLine = getIntent().getIntExtra("idLine", 0);
        String nameGroup = getIntent().getStringExtra("nameGroup");
        final int idGroup = getIntent().getIntExtra("idGroup", -1);
        final int currentIndexSwap = getIntent().getIntExtra("currentIndexSwap", -1);
        final int stopIndexSwap = getIntent().getIntExtra("stopIndexSwap", -1);

        //НАСТРАИВАЕМ ACTIONBAR
        getSupportActionBar().setTitle("Изоляция");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ОПРЕДЕЛЯЕМ, СОЗДАЕМ ЛИ МЫ НОВУЮ ГРУППУ ИЛИ ИЗМЕНЯЕМ СТАРУЮ
        final boolean change;
        if (idGroup != -1) {
            change = true;
            if (currentIndexSwap == -1 && stopIndexSwap == -1)
                getSupportActionBar().setSubtitle("Редактирование группы");
            else
                getSupportActionBar().setSubtitle("Редактирование подгруппы");
        }
        else {
            change = false;
            if (currentIndexSwap == -1 && stopIndexSwap == -1)
                getSupportActionBar().setSubtitle("Добавление группы");
            else
                getSupportActionBar().setSubtitle("Добавление подгруппы");
        }

        //ДЕЛАЕМ ПЕРЕКЛЮЧАТЕЛЬ ВЫКЛЮЧЕННЫМ ПО УМОЛЧАНИЮ
        reserve.setChecked(false);
        
        //ЗАПОЛНЕНИЕ ДАННЫХ, ЕСЛИ ОНИ ПЕРЕДАНЫ
        group.setText(nameGroup);
        if (change) {
            //ЗАПРОС В БД(ПОЛУЧЕНИЕ ИНФОРМАЦИИ О ГРУППЕ)
            Cursor cursor = database.query(DBHelper.TABLE_GROUPS, new String[] {DBHelper.GR_ID, DBHelper.GR_MARK,
                    DBHelper.GR_VEIN, DBHelper.GR_SECTION, DBHelper.GR_U1,
                    DBHelper.GR_U2, DBHelper.GR_R, DBHelper.GR_PHASE, DBHelper.GR_A_B,
                    DBHelper.GR_A_N, DBHelper.GR_B_N, DBHelper.GR_C_N}, "_id = ?", new String[] {String.valueOf(idGroup)}, null, null, null);
            if (cursor.moveToFirst()) {
                int markIndex = cursor.getColumnIndex(DBHelper.GR_MARK);
                int veinIndex = cursor. getColumnIndex(DBHelper.GR_VEIN);
                int sectionIndex = cursor. getColumnIndex(DBHelper.GR_SECTION);
                int workUIndex = cursor. getColumnIndex(DBHelper.GR_U1);
                int uIndex = cursor. getColumnIndex(DBHelper.GR_U2);
                int rIndex = cursor. getColumnIndex(DBHelper.GR_R);
                int phaseIndex = cursor. getColumnIndex(DBHelper.GR_PHASE);
                int a_bIndex = cursor. getColumnIndex(DBHelper.GR_A_B);
                int a_nIndex = cursor. getColumnIndex(DBHelper.GR_A_N);
                int b_nIndex = cursor. getColumnIndex(DBHelper.GR_B_N);
                int c_nIndex = cursor. getColumnIndex(DBHelper.GR_C_N);
                do {
                    //ЗАПОЛНЕНИЕ ДАННЫХ
                    mark.setText("Марка: " + cursor.getString(markIndex));
                    //ЕСЛИ РЕЗЕРВ
                    if (cursor.getString(markIndex).equals("резерв")) {
                        reserve.setChecked(true);
                        vein.setText("Кол-во жил: -");
                        section.setText("Сеченеие: -");
                        workU.setText("Раб. напряжение: -");
                        u.setText("Напр. мегаомметра: -");
                        r.setText("Доп. сопротивление: -");
                        phase.setText("Фаза: -");
                        number.setText("-");
                        break;
                    }
                    else {
                        String namePhase = cursor.getString(phaseIndex);
                        vein.setText("Кол-во жил: " + cursor.getString(veinIndex));
                        section.setText("Сечение: " + cursor.getString(sectionIndex));
                        workU.setText("Раб. напряжение: " + cursor.getString(workUIndex));
                        u.setText("Напр. мегаомметра: " + cursor.getString(uIndex));
                        r.setText("Доп. сопротивление: " + cursor.getString(rIndex));
                        phase.setText("Фаза: " + namePhase);
                        //ЗАПОЛНЕНИЕ ЗНАЧЕНИЯ
                        switch (namePhase) {
                            case "A":
                                number.setText(cursor.getString(a_nIndex));
                                break;
                            case "B":
                                number.setText(cursor.getString(b_nIndex));
                                break;
                            case "C":
                                number.setText(cursor.getString(c_nIndex));
                                break;
                            default:
                                number.setText(cursor.getString(a_bIndex));
                                break;
                        }
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        //ИЗМЕНЕНИЕ ПЕРЕКЛЮЧАТЕЛЯ
        reserve.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mark.setText("Марка: резерв");
                    vein.setText("Кол-во жил: -");
                    section.setText("Сечение: -");
                    workU.setText("Раб. напряжение: -");
                    u.setText("Напр. мегаомметра: -");
                    r.setText("Доп. сопротивление: -");
                    phase.setText("Фаза: -");
                    number.setText("-");
                }
                else {
                    mark.setText("Марка: Не выбрана");
                    vein.setText("Кол-во жил: Не выбрано");
                    section.setText("Сечение: Не выбрано");
                    workU.setText("Раб. напряжение: Не выбрано");
                    u.setText("Напр. мегаомметра: 1000");
                    r.setText("Доп. сопротивление: 0,5");
                    phase.setText("Фаза: Не выбрана");
                    number.setText("");
                }
            }
        });

        //ДОБАВЛЕНИЕ ГРУППЫ (СОХРАНЕНИЕ ДАННЫХ)
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textName = group.getText().toString();
                String textMark = mark.getText().toString();
                String textVein = vein.getText().toString();
                String textSection = section.getText().toString();
                String textWorkU = workU.getText().toString();
                String textU = u.getText().toString();
                String textR = r.getText().toString();
                String textPhase = phase.getText().toString();
                String numb = number.getText().toString();
                //ПРОВЕРКА НА ВВОД ВСЕХ ДАННЫХ
                if (numb.equals("") || textMark.equals("Марка: Не выбрана") || textVein.equals("Кол-во жил: Не выбрано") ||
                    textSection.equals("Сечение: Не выбрано") || textWorkU.equals("Раб. напряжение: Не выбрано") ||
                    textPhase.equals("Фаза: Не выбрана") || (textVein.equals("Кол-во жил: 2") && textPhase.equals("Фаза: -")) ||
                    (textVein.equals("Кол-во жил: 3") && textPhase.equals("Фаза: -"))) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity4.this);
                    alert.setCancelable(false);
                    alert.setMessage("Заполните все поля!");
                    alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    });
                    alert.show();
                }
                else {
                    if (numb.contains(",") && Double.parseDouble(numb.replace(",",".")) > 1) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity4.this);
                        alert.setCancelable(false);
                        alert.setMessage("Число не может быть дробным, если оно больше единицы!");
                        alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        });
                        alert.show();
                    }
                    else {
                        //УДАЛИМ НАШУ ГРУППУ, ЕСЛИ МЫ ВНОСИМ ИЗМЕНЕНИЯ, А НЕ СОЗДАЕМ НОВУЮ
                        if (change)
                            database.delete(DBHelper.TABLE_GROUPS, "_id = ?", new String[]{String.valueOf(idGroup)});
                        //СОЗДАЕМ НОВУЮ ГРУППУ
                        if (reserve.isChecked()) {
                            ContentValues contentValues = new ContentValues();
                            if (change)
                                contentValues.put(DBHelper.GR_ID, idGroup);
                            contentValues.put(DBHelper.GR_LINE_ID, idLine);
                            contentValues.put(DBHelper.GR_NAME, "Гр " + textName.substring(8));
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
                            database.insert(DBHelper.TABLE_GROUPS, null, contentValues);
                        } else {
                            String namePhase = textPhase.substring(6);
                            String numberVein = textVein.substring(12);
                            String numberR = textR.substring(20);
                            //ЗАПОЛНЕНИЕ НОВОЙ СТРОКИ
                            ContentValues contentValues = new ContentValues();
                            if (change)
                                contentValues.put(DBHelper.GR_ID, idGroup);
                            contentValues.put(DBHelper.GR_LINE_ID, idLine);
                            contentValues.put(DBHelper.GR_NAME, "Гр " + textName.substring(8));
                            contentValues.put(DBHelper.GR_U1, textWorkU.substring(17));
                            contentValues.put(DBHelper.GR_MARK, textMark.substring(7));
                            contentValues.put(DBHelper.GR_VEIN, numberVein);
                            contentValues.put(DBHelper.GR_SECTION, textSection.substring(9));
                            contentValues.put(DBHelper.GR_U2, textU.substring(19));
                            contentValues.put(DBHelper.GR_R, numberR);
                            contentValues.put(DBHelper.GR_PHASE, namePhase);
                            if (Double.parseDouble(numb.replace(",", ".")) >= Double.parseDouble(numberR.replace(",", ".")))
                                contentValues.put(DBHelper.GR_CONCLUSION, "соответст.");
                            else
                                contentValues.put(DBHelper.GR_CONCLUSION, "не соотв.");
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
                        }
                        if (stopIndexSwap != -1 && currentIndexSwap != -1)
                            swapGroups(stopIndexSwap, currentIndexSwap);
                        Toast toast2 = Toast.makeText(getApplicationContext(),
                                "Данные сохранены", Toast.LENGTH_SHORT);
                        toast2.show();
                        //ПЕРЕХОД К СПИСКАМ ГРУПП
                        Intent intent = new Intent("android.intent.action.Insulation3");
                        intent.putExtra("nameRoom", nameRoom);
                        intent.putExtra("idRoom", idRoom);
                        intent.putExtra("nameLine", nameLine);
                        intent.putExtra("idLine", idLine);
                        startActivity(intent);
                    }
                }
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
                intent.putExtra("nameRoom", nameRoom);
                intent.putExtra("idRoom", idRoom);
                intent.putExtra("nameLine", nameLine);
                intent.putExtra("idLine", idLine);
                startActivity(intent);
                return true;
            case R.id.action_main:
                Intent intent1 = new Intent(InsulationActivity4.this, MainActivity.class);
                startActivity(intent1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void swapGroups(int positionStop, int positionCurrent) {
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

    //ВЫБОР МАРКИ
    public void onClickMark(View view) {
        if (!reserve.isChecked()) {
            final AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity4.this);
            final View myView = getLayoutInflater().inflate(R.layout.dialog_for_marks,null);
            alert.setCancelable(false);
            alert.setTitle("Введите марку:");
            final AutoCompleteTextView marks = myView.findViewById(R.id.autoCompleteTextView3);
            ImageView arrow = myView.findViewById(R.id.imageView4);
            //ОТОБРАЖЕНИЕ ВЫПЛЫВАЮЩЕГО СПИСКА
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, getMarks());
            marks.setAdapter(adapter1);
            arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //СКРЫВАЕМ КЛАВИАТУРУ
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(myView.getWindowToken(),0);
                    marks.showDropDown();
                }
            });
            alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String nameMark = marks.getText().toString();
                    mark.setText("Марка: " + nameMark);
                    //Если новое название элемента, то вносим его в базу
                    if (!Arrays.asList(getMarks()).contains(nameMark)){
                        ContentValues newName = new ContentValues();
                        newName.put(DBHelper.MARK, nameMark);
                        database.insert(DBHelper.TABLE_MARKS, null, newName);
                    }
                }
            });
            alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                }
            });
            alert.setView(myView);
            alert.show();
        }
        else {
            AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity4.this);
            alert.setCancelable(false);
            alert.setMessage("Выбор марок не доступен, так как группа резервная. " +
                    "Чтобы выбрать марку, нажмите на ползунок, сделав его неактивным");
            alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                }
            });
            alert.show();
        }
    }

    //ВЫБОР КОЛ-ВА ЖИЛ
    public void onClickVein(View view) {
        if (!reserve.isChecked()) {
            AlertDialog.Builder alert1 = new AlertDialog.Builder(InsulationActivity4.this);
            final View myView = getLayoutInflater().inflate(R.layout.dialog_for_veins,null);
            alert1.setCancelable(false);
            alert1.setTitle("Введите кол-во жил:");
            final EditText input = myView.findViewById(R.id.editText2);
            //ОТКРЫВАЕМ КЛАВИАТУРУ
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
            alert1.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //СКРЫВАЕМ КЛАВИАТУРУ
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(myView.getWindowToken(),0);
                    String numberVein = input.getText().toString();
                    if (Integer.parseInt(numberVein) > 5 || Integer.parseInt(numberVein) < 2){
                        AlertDialog.Builder alert5 = new AlertDialog.Builder(InsulationActivity4.this);
                        alert5.setCancelable(false);
                        alert5.setMessage("Количество жил может быть от 2 до 5");
                        alert5.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        });
                        alert5.show();
                    }
                    else {
                        vein.setText("Кол-во жил: " + numberVein);
                        if (numberVein.equals("4") || numberVein.equals("5"))
                            phase.setText("Фаза: -");
                    }
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
        else {
            AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity4.this);
            alert.setCancelable(false);
            alert.setMessage("Выбор кол-ва жил не доступен, так как группа резервная. " +
                    "Чтобы выбрать кол-во жил, нажмите на ползунок, сделав его неактивным");
            alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                }
            });
            alert.show();
        }
    }

    //ВЫБОР СЕЧЕНИЯ
    public void onClickSection(View view) {
        if (!reserve.isChecked()) {
                AlertDialog.Builder alert1 = new AlertDialog.Builder(InsulationActivity4.this);
                final View myView = getLayoutInflater().inflate(R.layout.dialog_for_section,null);
                alert1.setCancelable(false);
                alert1.setTitle("Введите сечение:");
                final EditText input = myView.findViewById(R.id.editText2);
                //ОТКРЫВАЕМ КЛАВИАТУРУ
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                alert1.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //СКРЫВАЕМ КЛАВИАТУРУ
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(myView.getWindowToken(),0);
                        String numberSection = input.getText().toString();
                        section.setText("Сечение: " + numberSection);
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
        else {
            AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity4.this);
            alert.setCancelable(false);
            alert.setMessage("Выбор сечения не доступен, так как группа резервная. " +
                    "Чтобы выбрать сечение, нажмите на ползунок, сделав его неактивным");
            alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                }
            });
            alert.show();
        }
    }

    //ВЫБОР РАБОЧЕГО НАПРЯЖЕНИЯ
    public void onClickWorkU(View view) {
        if (!reserve.isChecked()) {
            AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity4.this);
            alert.setCancelable(false);
            alert.setTitle("Выберите рабочее напряжение:");
            final String arrWorkU[] = {"12", "24", "36","220", "380"};
            alert.setItems(arrWorkU, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    workU.setText("Раб. напряжение: " + arrWorkU[which]);
                }
            });
            alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                }
            });
            alert.show();
        }
        else {
            AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity4.this);
            alert.setCancelable(false);
            alert.setMessage("Выбор напряжения не доступен, так как группа резервная. " +
                    "Чтобы выбрать напряжение, нажмите на ползунок, сделав его неактивным");
            alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                }
            });
            alert.show();
        }
    }

    //ВЫБОР НАПРЯЖЕНИЯ МЕГАОММЕТРА
    public void onClickU(View view) {
        if (!reserve.isChecked()) {
            AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity4.this);
            alert.setCancelable(false);
            alert.setTitle("Выберите напряжение мегаомметра:");
            final String arrU[] = {"500", "1000", "2500"};
            alert.setItems(arrU, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    u.setText("Напр. мегаомметра: " + arrU[which]);
                }
            });
            alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                }
            });
            alert.show();
        }
        else {
            AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity4.this);
            alert.setCancelable(false);
            alert.setMessage("Выбор напряжения не доступен, так как группа резервная. " +
                    "Чтобы выбрать напряжение, нажмите на ползунок, сделав его неактивным");
            alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                }
            });
            alert.show();
        }
    }

    //ВЫБОР ДОПУСТИМОГО СОПРОТИВЛЕНИЯ
    public void onClickR(View view) {
        if (!reserve.isChecked()) {
            AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity4.this);
            alert.setCancelable(false);
            alert.setTitle("Выберите допустимое сопротивление:");
            final String arrR[] = {"0,5", "1"};
            alert.setItems(arrR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    r.setText("Доп. сопротивление: " + arrR[which]);
                }
            });
            alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                }
            });
            alert.show();
        }
        else {
            AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity4.this);
            alert.setCancelable(false);
            alert.setMessage("Выбор сопротивления не доступен, так как группа резервная. " +
                    "Чтобы выбрать сопротивление, нажмите на ползунок, сделав его неактивным");
            alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                }
            });
            alert.show();
        }
    }

    //ВЫБОР ФАЗЫ
    public void onClickPhase(View view) {
        if ((vein.getText().toString().substring(12).equals("4") || vein.getText().toString().substring(12).equals("5"))) {
            AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity4.this);
            alert.setCancelable(false);
            alert.setMessage("Выбор фазы доступен, если количество жил равно 2 или 3. " +
                    "При ином количестве жил переходите сразу к вводу значения.");
            alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                }
            });
            alert.show();
        }
        else {
            if (!reserve.isChecked()) {
                AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity4.this);
                alert.setCancelable(false);
                alert.setTitle("Выберите фазу:");
                final String phases[] = {"A", "B", "C"};
                alert.setItems(phases, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        phase.setText("Фаза: " + phases[which]);
                    }
                });
                alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                alert.show();
            }
            else {
                AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity4.this);
                alert.setCancelable(false);
                alert.setMessage("Выбор фазы не доступен, так как группа резервная. " +
                        "Чтобы выбрать фазу, нажмите на ползунок, сделав его неактивным");
                alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                alert.show();
            }
        }
    }

    public String getRandomNumber(String x) {
        if (x.contains(","))
            return x;
        int oldNumb = Integer.parseInt(x);
        int random = 0;
        Random generator = new Random();
        if (oldNumb < 300)
            random = oldNumb;
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
        if (count == arr.length && Integer.parseInt(num) >= 300)
            pushArray(arr, num);
    }

    //ПОЛУЧЕНИЕ МАРОК
    public String[] getMarks() {
        final ArrayList<String> marks = new ArrayList <String>();
        Cursor cursor = database.query(DBHelper.TABLE_MARKS, new String[] {DBHelper.MARK}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(DBHelper.MARK);
            do {
                marks.add(cursor.getString(nameIndex));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return marks.toArray(new String[marks.size()]);
    }
}
