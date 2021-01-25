package com.example.protokol.create_protokol;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static java.math.BigDecimal.ROUND_HALF_UP;

public class InsulationActivity5 extends AppCompatActivity {

    DBHelper dbHelper;
    String nameFloor, nameRoom, nameLine, nameGroup;
    int idFloor, idRoom, idLine, idSubgroup;
    String numb_poles = "1P";
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insulation5);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        final LinearLayout insDataLayout = findViewById(R.id.ins_data);
        final LinearLayout autDataLayout = findViewById(R.id.aut_data);
        final LinearLayout difAutDataLayout = findViewById(R.id.dif_aut_data);
        final LinearLayout symbolLayout = findViewById(R.id.symbolLayout);
        final LinearLayout nameAutomatLayout = findViewById(R.id.nameAutomatLayout);
        final LinearLayout polesLayout = findViewById(R.id.poles_layout);
        final LinearLayout typeKzLayout = findViewById(R.id.typeKzLayout);
        final LinearLayout nominalLayout = findViewById(R.id.nominalLayout);
        final LinearLayout rangeLayout = findViewById(R.id.rangeLayout);
        final LinearLayout typeOverloadLayout = findViewById(R.id.typeOverloadLayout);
        final LinearLayout nominalApparatLayout = findViewById(R.id.nom_apLayout);
        final LinearLayout markLayout = findViewById(R.id.markLayout);
        final LinearLayout veinLayout = findViewById(R.id.veinLayout);
        final LinearLayout sectionLayout = findViewById(R.id.sectionLayout);
        final LinearLayout workULayout = findViewById(R.id.workULayout);
        final LinearLayout phaseLayout = findViewById(R.id.phaseLayout);
        final LinearLayout numbLayout = findViewById(R.id.numbLayout);
        final LinearLayout uzoDiffLayout = findViewById(R.id.uzoLayout);
        final LinearLayout uDiffLayout = findViewById(R.id.uLayout);
        final LinearLayout iNomDiffLayout = findViewById(R.id.iNomLayout);

        final Switch autOrDifAutSwitch = findViewById(R.id.switch30);
        final TextView titleAut = findViewById(R.id.titleAut);
        final TextView titleDifAut = findViewById(R.id.titleDifAut);
        final Switch vvodSwitch = findViewById(R.id.switch1);
        Button vvodButton = findViewById(R.id.button25);
        final TextView symbolText = findViewById(R.id.textView21);
        Button symbolButton = findViewById(R.id.button38);
        final TextView nameGroupText = findViewById(R.id.textView8);
        final TextView nameAutomatText = findViewById(R.id.textView6);
        Button nameAutomatButton = findViewById(R.id.button40);
        final RadioGroup radioGroup = findViewById(R.id.radioGroup);
        final TextView typeKzText = findViewById(R.id.textView20);
        Button typeKzButton = findViewById(R.id.button26);
        final TextView nominalText = findViewById(R.id.textView22);
        Button nominalButton = findViewById(R.id.button29);
        final TextView rangeText = findViewById(R.id.textView39);
        Button rangeButton = findViewById(R.id.button59);
        final TextView typeOverloadText = findViewById(R.id.textView202);
        Button typeOverloadButton = findViewById(R.id.button262);
        final TextView nominalApparatText = findViewById(R.id.textView391);
        Button nominalApparatButton = findViewById(R.id.button591);
        final TextView uzoDiffText = findViewById(R.id.textView201);
        Button uzoDiffButton = findViewById(R.id.button261);
        final TextView uDiffText = findViewById(R.id.textView301);
        Button uDiffButton = findViewById(R.id.button301);
        final TextView iNomDiffText = findViewById(R.id.textView35);
        final Button iNomDiffButton = findViewById(R.id.button601);

        final Switch reserveSwitch = findViewById(R.id.switch3);
        final TextView markText = findViewById(R.id.textView40);
        Button markButton = findViewById(R.id.button60);
        final TextView veinText = findViewById(R.id.textView25);
        Button veinButton = findViewById(R.id.button28);
        final TextView sectionText = findViewById(R.id.textView23);
        Button sectionButton = findViewById(R.id.button27);
        final TextView workUText = findViewById(R.id.textView30);
        Button workUButton = findViewById(R.id.button30);
        final TextView uText = findViewById(R.id.textView32);
        Button uButton = findViewById(R.id.button31);
        final TextView rText = findViewById(R.id.textView81);
        Button rButton = findViewById(R.id.button82);
        final TextView phaseText = findViewById(R.id.textView84);
        Button phaseButton = findViewById(R.id.button85);
        final EditText numberEdit = findViewById(R.id.editText7);
        Button save = findViewById(R.id.button17);

        nameFloor = getIntent().getStringExtra("nameFloor");
        idFloor = getIntent().getIntExtra("idFloor", 0);
        nameRoom = getIntent().getStringExtra("nameRoom");
        idRoom = getIntent().getIntExtra("idRoom", 0);
        nameLine = getIntent().getStringExtra("nameLine");
        idLine = getIntent().getIntExtra("idLine", 0);
        nameGroup = getIntent().getStringExtra("nameGroup");
        final int idGroup = getIntent().getIntExtra("idGroup", -1);
        idSubgroup = getIntent().getIntExtra("idSubgroup", -1);
        final int currentIndexSwap = getIntent().getIntExtra("currentIndexSwap", -1);
        final int stopIndexSwap = getIntent().getIntExtra("stopIndexSwap", -1);

        //НАСТРАИВАЕМ ACTIONBAR
        getSupportActionBar().setTitle("Изоляция");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ОПРЕДЕЛЯЕМ, СОЗДАЕМ ЛИ МЫ НОВУЮ ГРУППУ ИЛИ ИЗМЕНЯЕМ СТАРУЮ
        final boolean isNewSubgroup = idSubgroup != -1;
        final boolean isChange;
        if (idGroup != -1) {
            isChange = true;
            if (currentIndexSwap == -1 && stopIndexSwap == -1)
                getSupportActionBar().setSubtitle("Редактирование группы");
            else
                getSupportActionBar().setSubtitle("Редактирование подгруппы");
        }
        else {
            isChange = false;
            if (currentIndexSwap == -1 && stopIndexSwap == -1)
                getSupportActionBar().setSubtitle("Добавление группы");
            else
                getSupportActionBar().setSubtitle("Добавление подгруппы");
        }

        //ДЕЛАЕМ ПЕРЕКЛЮЧАТЕЛЬ ВЫКЛЮЧЕННЫМ ПО УМОЛЧАНИЮ
        reserveSwitch.setChecked(false);
        vvodSwitch.setChecked(false);
        autOrDifAutSwitch.setChecked(false);
        titleAut.setPaintFlags(titleAut.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        //ЗАПОЛНЕНИЕ ДАННЫХ, ЕСЛИ ОНИ ПЕРЕДАНЫ
        nameGroupText.setText(nameGroup);
        if (isChange) {
            //ЗАПРОС В БД(ПОЛУЧЕНИЕ ИНФОРМАЦИИ О ГРУППЕ)
            int aut_id = 0;
            String aut_type = "";
            String nameAu = "";
            Cursor cursor = database.query(DBHelper.TABLE_GROUPS, new String[] {DBHelper.GR_ID,
                    DBHelper.GR_AUTOMATIC, DBHelper.GR_TYPE_KZ, DBHelper.GR_NOMINAL, DBHelper.GR_RANGE,
                    DBHelper.GR_MARK, DBHelper.GR_VEIN, DBHelper.GR_SECTION, DBHelper.GR_U1,
                    DBHelper.GR_U2, DBHelper.GR_R, DBHelper.GR_PHASE, DBHelper.GR_A_B,
                    DBHelper.GR_A_N, DBHelper.GR_B_N, DBHelper.GR_C_N,
                    DBHelper.GR_AUT_ID, DBHelper.GR_AUT_TYPE}, "_id = ?", new String[] {String.valueOf(idGroup)}, null, null, null);
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
                int phaseIndex = cursor.getColumnIndex(DBHelper.GR_PHASE);
                int a_bIndex = cursor.getColumnIndex(DBHelper.GR_A_B);
                int a_nIndex = cursor.getColumnIndex(DBHelper.GR_A_N);
                int b_nIndex = cursor.getColumnIndex(DBHelper.GR_B_N);
                int c_nIndex = cursor.getColumnIndex(DBHelper.GR_C_N);
                int aut_idIndex = cursor.getColumnIndex(DBHelper.GR_AUT_ID);
                int aut_typeIndex = cursor.getColumnIndex(DBHelper.GR_AUT_TYPE);
                aut_id = cursor.getInt(aut_idIndex);
                aut_type = cursor.getString(aut_typeIndex);
                do {
                    //ЗАПОЛНЕНИЕ ДАННЫХ
                    nameAu = cursor.getString(nameAuIndex);
                    nameAutomatText.setText(nameAu.substring(0, nameAu.length() - 3));
                    typeKzText.setText(cursor.getString(type_kzIndex));
                    nominalText.setText(cursor.getString(nominalIndex));
                    rangeText.setText(cursor.getString(rangeIndex));
                    markText.setText(cursor.getString(markIndex));
                    //ЕСЛИ РЕЗЕРВ
                    if (cursor.getString(markIndex).equals("резерв")) {
                        reserveSwitch.setChecked(true);
                        veinText.setText("-");
                        sectionText.setText("-");
                        workUText.setText("-");
                        uText.setText("-");
                        rText.setText("-");
                        phaseText.setText("-");
                        numberEdit.setText("-");
                        break;
                    }
                    else {
                        String namePhase = cursor.getString(phaseIndex);
                        veinText.setText(cursor.getString(veinIndex));
                        sectionText.setText(cursor.getString(sectionIndex));
                        workUText.setText(cursor.getString(workUIndex));
                        uText.setText(cursor.getString(uIndex));
                        rText.setText(cursor.getString(rIndex));
                        phaseText.setText(namePhase);
                        //ЗАПОЛНЕНИЕ ЗНАЧЕНИЯ
                        switch (namePhase) {
                            case "A":
                                numberEdit.setText(cursor.getString(a_nIndex));
                                break;
                            case "B":
                                numberEdit.setText(cursor.getString(b_nIndex));
                                break;
                            case "C":
                                numberEdit.setText(cursor.getString(c_nIndex));
                                break;
                            default:
                                numberEdit.setText(cursor.getString(a_bIndex));
                                break;
                        }
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
            //ЗАПРОС В БД(ПОЛУЧЕНИЕ ИНФОРМАЦИИ О ДИФ./ОБЫЧН. АВТОМАТЕ)
            if (aut_type.equals("dif_aut")) {
                autOrDifAutSwitch.setChecked(true);
                titleAut.setPaintFlags(titleAut.getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));
                titleDifAut.setPaintFlags(titleDifAut.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                autDataLayout.setVisibility(View.GONE);
                difAutDataLayout.setVisibility(View.VISIBLE);
                polesLayout.setVisibility(View.GONE);
                Cursor cursor5 = database.query(DBHelper.TABLE_DIF_AUTOMATICS, new String[] {DBHelper.DIF_AU_PLACE,
                        DBHelper.DIF_AU_UZO, DBHelper.DIF_AU_U,
                        DBHelper.DIF_AU_I_NOM}, "_id = ?", new String[] {String.valueOf(aut_id)}, null, null, null);
                if (cursor5.moveToFirst()) {
                    int symbolIndex = cursor5.getColumnIndex(DBHelper.DIF_AU_PLACE);
                    int uzoIndex = cursor5.getColumnIndex(DBHelper.DIF_AU_UZO);
                    int dif_uIndex = cursor5.getColumnIndex(DBHelper.DIF_AU_U);
                    int dif_i_nomIndex = cursor5.getColumnIndex(DBHelper.DIF_AU_I_NOM);
                    symbolText.setText(cursor5.getString(symbolIndex));
                    uzoDiffText.setText(cursor5.getString(uzoIndex));
                    uDiffText.setText(cursor5.getString(dif_uIndex));
                    iNomDiffText.setText(cursor5.getString(dif_i_nomIndex));
                }
                cursor5.close();
            }
            else {
                Cursor cursor5 = database.query(DBHelper.TABLE_AUTOMATICS, new String[] {DBHelper.AU_SYMBOL_SCHEME,
                        DBHelper.AU_TYPE_OVERLOAD, DBHelper.AU_NOMINAL_1}, "_id = ?", new String[] {String.valueOf(aut_id)}, null, null, null);
                if (cursor5.moveToFirst()) {
                    int symbolIndex = cursor5.getColumnIndex(DBHelper.AU_SYMBOL_SCHEME);
                    int type_overloadIndex = cursor5.getColumnIndex(DBHelper.AU_TYPE_OVERLOAD);
                    int nom1Index = cursor5.getColumnIndex(DBHelper.AU_NOMINAL_1);
                    symbolText.setText(cursor5.getString(symbolIndex));
                    typeOverloadText.setText(cursor5.getString(type_overloadIndex));
                    nominalApparatText.setText(cursor5.getString(nom1Index));
                }
                cursor5.close();
            }
            //МЕНЯЕМ РАДИО КНОПКУ С ПОЛЮСОМ
            if (nameAu.contains("3P")) {
                radioGroup.check(R.id.three_poles);
                numb_poles = "3P";
            }

        }
        else if (isNewSubgroup) {
            //ЗАПРОС В БД(ПОЛУЧЕНИЕ ИНФОРМАЦИИ О ГРУППЕ)
            int aut_id = 0;
            String aut_type = "";
            String nameAu = "";
            Cursor cursor = database.query(DBHelper.TABLE_GROUPS, new String[] {DBHelper.GR_ID,
                    DBHelper.GR_AUTOMATIC, DBHelper.GR_TYPE_KZ, DBHelper.GR_NOMINAL,
                    DBHelper.GR_RANGE, DBHelper.GR_AUT_ID,
                    DBHelper.GR_AUT_TYPE}, "_id = ?", new String[] {String.valueOf(idSubgroup)}, null, null, null);
            if (cursor.moveToFirst()) {
                int nameAuIndex = cursor.getColumnIndex(DBHelper.GR_AUTOMATIC);
                int type_kzIndex = cursor.getColumnIndex(DBHelper.GR_TYPE_KZ);
                int nominalIndex = cursor.getColumnIndex(DBHelper.GR_NOMINAL);
                int rangeIndex = cursor.getColumnIndex(DBHelper.GR_RANGE);
                int aut_idIndex = cursor.getColumnIndex(DBHelper.GR_AUT_ID);
                int aut_typeIndex = cursor.getColumnIndex(DBHelper.GR_AUT_TYPE);
                aut_id = cursor.getInt(aut_idIndex);
                aut_type = cursor.getString(aut_typeIndex);
                do {
                    //ЗАПОЛНЕНИЕ ДАННЫХ
                    nameAu = cursor.getString(nameAuIndex);
                    nameAutomatText.setText(nameAu.substring(0, nameAu.length() - 3));
                    typeKzText.setText(cursor.getString(type_kzIndex));
                    nominalText.setText(cursor.getString(nominalIndex));
                    rangeText.setText(cursor.getString(rangeIndex));
                } while (cursor.moveToNext());
            }
            cursor.close();
            //ЗАПРОС В БД(ПОЛУЧЕНИЕ ИНФОРМАЦИИ О ДИФ./ОБЫЧН. АВТОМАТЕ)
            if (aut_type.equals("dif_aut")) {
                autOrDifAutSwitch.setChecked(true);
                titleAut.setPaintFlags(titleAut.getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));
                titleDifAut.setPaintFlags(titleDifAut.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                autDataLayout.setVisibility(View.GONE);
                difAutDataLayout.setVisibility(View.VISIBLE);
                polesLayout.setVisibility(View.GONE);
                Cursor cursor5 = database.query(DBHelper.TABLE_DIF_AUTOMATICS, new String[] {DBHelper.DIF_AU_PLACE,
                        DBHelper.DIF_AU_UZO, DBHelper.DIF_AU_U,
                        DBHelper.DIF_AU_I_NOM}, "_id = ?", new String[] {String.valueOf(aut_id)}, null, null, null);
                if (cursor5.moveToFirst()) {
                    int symbolIndex = cursor5.getColumnIndex(DBHelper.DIF_AU_PLACE);
                    int uzoIndex = cursor5.getColumnIndex(DBHelper.DIF_AU_UZO);
                    int dif_uIndex = cursor5.getColumnIndex(DBHelper.DIF_AU_U);
                    int dif_i_nomIndex = cursor5.getColumnIndex(DBHelper.DIF_AU_I_NOM);
                    symbolText.setText(cursor5.getString(symbolIndex));
                    uzoDiffText.setText(cursor5.getString(uzoIndex));
                    uDiffText.setText(cursor5.getString(dif_uIndex));
                    iNomDiffText.setText(cursor5.getString(dif_i_nomIndex));
                }
                cursor5.close();
            }
            else {
                Cursor cursor5 = database.query(DBHelper.TABLE_AUTOMATICS, new String[] {DBHelper.AU_SYMBOL_SCHEME,
                        DBHelper.AU_TYPE_OVERLOAD, DBHelper.AU_NOMINAL_1}, "_id = ?", new String[] {String.valueOf(aut_id)}, null, null, null);
                if (cursor5.moveToFirst()) {
                    int symbolIndex = cursor5.getColumnIndex(DBHelper.AU_SYMBOL_SCHEME);
                    int type_overloadIndex = cursor5.getColumnIndex(DBHelper.AU_TYPE_OVERLOAD);
                    int nom1Index = cursor5.getColumnIndex(DBHelper.AU_NOMINAL_1);
                    symbolText.setText(cursor5.getString(symbolIndex));
                    typeOverloadText.setText(cursor5.getString(type_overloadIndex));
                    nominalApparatText.setText(cursor5.getString(nom1Index));
                }
                cursor5.close();
            }
            //МЕНЯЕМ РАДИО КНОПКУ С ПОЛЮСОМ
            if (nameAu.contains("3P")) {
                radioGroup.check(R.id.three_poles);
                numb_poles = "3P";
            }
        }

        // ПЕРЕКЛЮЧАТЕЛЬ ДИФ АВТОМАТ ИЛИ АВТОМАТ
        autOrDifAutSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    titleAut.setPaintFlags(titleAut.getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));
                    titleDifAut.setPaintFlags(titleDifAut.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    autDataLayout.setVisibility(View.GONE);
                    difAutDataLayout.setVisibility(View.VISIBLE);
                    polesLayout.setVisibility(View.GONE);
                }
                else {
                    titleDifAut.setPaintFlags(titleDifAut.getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));
                    titleAut.setPaintFlags(titleAut.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    difAutDataLayout.setVisibility(View.GONE);
                    autDataLayout.setVisibility(View.VISIBLE);
                    polesLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        // ПЕРЕКЛЮЧАТЕЛЬ ВВОДНОЙ
        vvodSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    insDataLayout.setVisibility(View.GONE);
                }
                else {
                    insDataLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        // КНОПКА ВВОДНОЙ
        vvodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vvodSwitch.setChecked(!vvodSwitch.isChecked());
            }
        });

        // ОБОЗНАЧЕНИЕ ПО СХЕМЕ
        symbolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert1 = new AlertDialog.Builder(InsulationActivity5.this);
                final View myView = getLayoutInflater().inflate(R.layout.dialog_for_names,null);
                alert1.setCancelable(false);
                alert1.setTitle("Введите обозначение по схеме:");
                final EditText input = myView.findViewById(R.id.editText);
                openKeyboard();
                alert1.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        closeKeyboard(myView);
                        String sym = input.getText().toString();
                        symbolText.setText(sym);
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

        // НАЗВАНИЕ АВТОМАТА
        nameAutomatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity5.this);
                final View myView = getLayoutInflater().inflate(R.layout.dialog_for_marks,null);
                alert.setCancelable(false);
                alert.setTitle("Введите наименование аппарата, тип и номер:");
                final AutoCompleteTextView automatics = myView.findViewById(R.id.autoCompleteTextView3);
                ImageView arrow = myView.findViewById(R.id.imageView4);
                //ОТОБРАЖЕНИЕ ВЫПЛЫВАЮЩЕГО СПИСКА
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(InsulationActivity5.this, android.R.layout.simple_dropdown_item_1line, getAutomatics(database));
                automatics.setAdapter(adapter1);
                openKeyboard();
                arrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        closeKeyboard(myView);
                        automatics.showDropDown();
                    }
                });
                alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        closeKeyboard(myView);
                        String nameAu = automatics.getText().toString();
                        nameAutomatText.setText(nameAu);
                        //Если новое название элемента, то вносим его в базу
                        if (!Arrays.asList(getAutomatics(database)).contains(nameAu)){
                            ContentValues newName = new ContentValues();
                            newName.put(DBHelper.LIB_AU_NAME, nameAu);
                            database.insert(DBHelper.TABLE_LIBRARY_AUTOMATICS, null, newName);
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
                numberEdit.clearFocus();
            }
        });

        // ПОЛЮСА
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.one_pole:
                        numb_poles = "1P";
                        break;
                    case R.id.three_poles:
                        numb_poles = "3P";
                        break;
                }
            }
        });

        // ТИП РАСЦЕПИТЕЛЯ КЗ
        typeKzButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity5.this);
                alert.setCancelable(false);
                alert.setTitle("Выберите тип расцепителей к.з.:");
                final String type_kz_array[] = {"B", "C", "D", "МД"};
                alert.setItems(type_kz_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nominal_2_str = nominalText.getText().toString();
                        if (!nominal_2_str.equals("") && !nominal_2_str.equals("Нет")) {
                            int nominal_2_int = Integer.parseInt(nominal_2_str);
                            String set_kz_str;
                            switch (which) {
                                case 0:
                                    set_kz_str = String.valueOf(nominal_2_int * 3) + "-" + String.valueOf(nominal_2_int * 5);
                                    rangeText.setText(set_kz_str);
                                    break;
                                case 1:
                                    set_kz_str = String.valueOf(nominal_2_int * 5) + "-" + String.valueOf(nominal_2_int * 10);
                                    rangeText.setText(set_kz_str);
                                    break;
                                case 2:
                                    set_kz_str = String.valueOf(nominal_2_int * 10) + "-" + String.valueOf(nominal_2_int * 20);
                                    rangeText.setText(set_kz_str);
                                    break;
                                case 3:
                                    rangeText.setText("Нет");
                                    break;
                            }
                        }
                        typeKzText.setText(type_kz_array[which]);
                    }
                });
                alert.setPositiveButton("Ввести", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        AlertDialog.Builder alert1 = new AlertDialog.Builder(InsulationActivity5.this);
                        final View myView = getLayoutInflater().inflate(R.layout.dialog_for_names,null);
                        alert1.setCancelable(false);
                        alert1.setTitle("Введите тип расцепителей к.з.:");
                        final EditText input = myView.findViewById(R.id.editText);
                        openKeyboard();
                        alert1.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                closeKeyboard(myView);
                                String type_kz = input.getText().toString();
                                typeKzText.setText(type_kz);
                                rangeText.setText("Нет");
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
                alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                alert.show();
                numberEdit.clearFocus();
            }
        });

        // НОМИНАЛЬНЫЙ ТОК РАСЦЕПИТЕЛЯ
        nominalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert1 = new AlertDialog.Builder(InsulationActivity5.this);
                final View myView = getLayoutInflater().inflate(R.layout.dialog_for_veins,null);
                alert1.setCancelable(false);
                alert1.setTitle("Введите номинальный ток расцепителя:");
                final EditText input = myView.findViewById(R.id.editText2);
                openKeyboard();
                alert1.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        closeKeyboard(myView);
                        String nom_str = input.getText().toString();
                        String type_kz = typeKzText.getText().toString();
                        if (!nom_str.equals("") && nom_str.length() < 7){
                            String set_kz;
                            int nom_int = Integer.parseInt(nom_str);
                            if (type_kz.equals("B")) {
                                set_kz = String.valueOf(3 * nom_int) + "-" + String.valueOf(5 * nom_int);
                                rangeText.setText(set_kz);
                            }
                            if (type_kz.equals("C")) {
                                set_kz = String.valueOf(5 * nom_int) + "-" + String.valueOf(10 * nom_int);
                                rangeText.setText(set_kz);
                            }
                            if (type_kz.equals("D")) {
                                set_kz = String.valueOf(10 * nom_int) + "-" + String.valueOf(20 * nom_int);
                                rangeText.setText(set_kz);
                            }
                            nominalText.setText(nom_str);
                            nominalApparatText.setText(nom_str);
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
                numberEdit.clearFocus();
            }
        });

        // ДИАПАЗОН ТОКА СРАБАТЫВАНИЯ РАСЦЕПИТЕЛЯ КЗ
        rangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert1 = new AlertDialog.Builder(InsulationActivity5.this);
                final View myView = getLayoutInflater().inflate(R.layout.dialog_for_set_kz,null);
                alert1.setCancelable(false);
                alert1.setTitle("Введите диапазон тока срабатывания расцепителя к.з.:");
                final EditText input1 = myView.findViewById(R.id.editText1);
                final EditText input2 = myView.findViewById(R.id.editText2);
                final EditText input3 = myView.findViewById(R.id.editText3);
                final LinearLayout linerDash = myView.findViewById(R.id.linerDash);
                final LinearLayout linerMore = myView.findViewById(R.id.linerMore);
                final RadioGroup radioGroupSigns = myView.findViewById(R.id.radioGroupSigns);
                radioGroupSigns.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.dash:
                                linerMore.setVisibility(View.GONE);
                                linerDash.setVisibility(View.VISIBLE);
                                break;
                            case R.id.more:
                                linerDash.setVisibility(View.GONE);
                                linerMore.setVisibility(View.VISIBLE);
                                break;
                        }
                    }
                });
                openKeyboard();
                alert1.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        closeKeyboard(myView);
                        String set_kz = "Нет";
                        int radioId = radioGroupSigns.getCheckedRadioButtonId();
                        switch (radioId) {
                            case R.id.dash:
                                if (!input1.getText().toString().equals("") &&
                                        !input2.getText().toString().equals("")) {
                                    set_kz = input1.getText().toString() + "-" + input2.getText().toString();
                                }
                                break;
                            case R.id.more:
                                if (!input3.getText().toString().equals("")) {
                                    set_kz = ">" + input3.getText().toString();
                                }
                                break;
                        }
                        rangeText.setText(set_kz);
                    }
                });
                alert1.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        closeKeyboard(myView);
                    }
                });
                alert1.setView(myView);
                alert1.show();
                numberEdit.clearFocus();
            }
        });

        //ТИПЫ РАСЦЕПИТЕЛЕЙ ПЕРЕЗАГРУЗКИ
        typeOverloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity5.this);
                alert.setCancelable(false);
                alert.setTitle("Выберите тип расцепителей перегрузки:");
                final String type_overload_array[] = {"ОВВ", "НВВ"};
                alert.setItems(type_overload_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        typeOverloadText.setText(type_overload_array[which]);
                    }
                });
                alert.setPositiveButton("Ввести", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        AlertDialog.Builder alert1 = new AlertDialog.Builder(InsulationActivity5.this);
                        final View myView = getLayoutInflater().inflate(R.layout.dialog_for_names,null);
                        alert1.setCancelable(false);
                        alert1.setTitle("Введите тип расцепителей перегрузки:");
                        final EditText input = myView.findViewById(R.id.editText);
                        openKeyboard();
                        alert1.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                closeKeyboard(myView);
                                String type_overload = input.getText().toString();
                                typeOverloadText.setText(type_overload);
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
                alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                alert.show();
            }
        });

        // НОМИНАЛЬНЫЙ ТОК АППАРАТА
        nominalApparatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert1 = new AlertDialog.Builder(InsulationActivity5.this);
                final View myView = getLayoutInflater().inflate(R.layout.dialog_for_excerpt,null);
                alert1.setCancelable(false);
                alert1.setTitle("Введите номинальный ток аппарата:");
                final EditText input = myView.findViewById(R.id.editText2);
                openKeyboard();
                alert1.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        closeKeyboard(myView);
                        String nomApp = input.getText().toString();
                        nominalApparatText.setText(nomApp);
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

        // ТИП УЗО
        uzoDiffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity5.this);
                alert.setCancelable(false);
                alert.setTitle("Выберите тип УЗО:");
                final String uzo_array[] = {"А", "АС"};
                alert.setItems(uzo_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        uzoDiffText.setText(uzo_array[which]);
                    }
                });
                alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                alert.show();
            }
        });

        // Uф(В)
        uDiffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity5.this);
                alert.setCancelable(false);
                alert.setTitle("Выберите напряжение:");
                final String u_array[] = {"220", "380"};
                alert.setItems(u_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        uDiffText.setText(u_array[which]);
                    }
                });
                alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                alert.show();
            }
        });

        // НОМИНАЛЬНЫЙ ТОК (ДИФ)
        iNomDiffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity5.this);
                alert.setCancelable(false);
                alert.setTitle("Выберите ток ном. (мА):");
                final String i_nom_array[] = {"30", "100", "300"};
                alert.setItems(i_nom_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        iNomDiffText.setText(i_nom_array[which]);
                    }
                });
                alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                alert.show();
            }
        });

        //ИЗМЕНЕНИЕ ПЕРЕКЛЮЧАТЕЛЯ (РЕЗЕРВ)
        reserveSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    markText.setText("резерв");
                    veinText.setText("-");
                    sectionText.setText("-");
                    workUText.setText("-");
                    uText.setText("-");
                    rText.setText("-");
                    phaseText.setText("-");
                    numberEdit.setText("-");
                }
                else {
                    markText.setText("Нет");
                    veinText.setText("Нет");
                    sectionText.setText("Нет");
                    workUText.setText("Нет");
                    uText.setText("1000");
                    rText.setText("0,5");
                    phaseText.setText("Нет");
                    numberEdit.setText("");
                }
            }
        });

        //ВЫБОР МАРКИ
        markButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!reserveSwitch.isChecked()) {
                    final AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity5.this);
                    final View myView = getLayoutInflater().inflate(R.layout.dialog_for_marks,null);
                    alert.setCancelable(false);
                    alert.setTitle("Введите марку:");
                    final AutoCompleteTextView marks = myView.findViewById(R.id.autoCompleteTextView3);
                    ImageView arrow = myView.findViewById(R.id.imageView4);
                    //ОТОБРАЖЕНИЕ ВЫПЛЫВАЮЩЕГО СПИСКА
                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(InsulationActivity5.this, android.R.layout.simple_dropdown_item_1line, getMarks());
                    marks.setAdapter(adapter1);
                    openKeyboard();
                    arrow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            closeKeyboard(myView);
                            marks.showDropDown();
                        }
                    });
                    alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            closeKeyboard(myView);
                            String nameMark = marks.getText().toString();
                            markText.setText(nameMark);
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
                            closeKeyboard(myView);
                        }
                    });
                    alert.setView(myView);
                    alert.show();
                }
                else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity5.this);
                    alert.setCancelable(false);
                    alert.setMessage("Выбор марок не доступен, так как группа резервная. " +
                            "Чтобы выбрать марку, нажмите на ползунок, сделав его неактивным");
                    alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    });
                    alert.show();
                }
                numberEdit.clearFocus();
            }
        });

        //ВЫБОР КОЛ-ВА ЖИЛ
        veinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!reserveSwitch.isChecked()) {
                    AlertDialog.Builder alert1 = new AlertDialog.Builder(InsulationActivity5.this);
                    final View myView = getLayoutInflater().inflate(R.layout.dialog_for_veins,null);
                    alert1.setCancelable(false);
                    alert1.setTitle("Введите кол-во жил:");
                    final EditText input = myView.findViewById(R.id.editText2);
                    openKeyboard();
                    alert1.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            closeKeyboard(myView);
                            String numberVein = input.getText().toString();
                            if (!numberVein.equals("") && (Integer.parseInt(numberVein) > 5 || Integer.parseInt(numberVein) < 2)){
                                AlertDialog.Builder alert5 = new AlertDialog.Builder(InsulationActivity5.this);
                                alert5.setCancelable(false);
                                alert5.setMessage("Количество жил может быть от 2 до 5");
                                alert5.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {

                                    }
                                });
                                alert5.show();
                            }
                            else {
                                veinText.setText(numberVein);
                                if (numberVein.equals("4") || numberVein.equals("5"))
                                    phaseText.setText("-");
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
                else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity5.this);
                    alert.setCancelable(false);
                    alert.setMessage("Выбор кол-ва жил не доступен, так как группа резервная. " +
                            "Чтобы выбрать кол-во жил, нажмите на ползунок, сделав его неактивным");
                    alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    });
                    alert.show();
                }
                numberEdit.clearFocus();
            }
        });

        //ВЫБОР СЕЧЕНИЯ
        sectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!reserveSwitch.isChecked()) {
                    AlertDialog.Builder alert1 = new AlertDialog.Builder(InsulationActivity5.this);
                    final View myView = getLayoutInflater().inflate(R.layout.dialog_for_section,null);
                    alert1.setCancelable(false);
                    alert1.setTitle("Введите сечение:");
                    final EditText input = myView.findViewById(R.id.editText2);
                    openKeyboard();
                    alert1.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            closeKeyboard(myView);
                            String numberSection = input.getText().toString();
                            sectionText.setText(numberSection);
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
                else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity5.this);
                    alert.setCancelable(false);
                    alert.setMessage("Выбор сечения не доступен, так как группа резервная. " +
                            "Чтобы выбрать сечение, нажмите на ползунок, сделав его неактивным");
                    alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    });
                    alert.show();
                }
                numberEdit.clearFocus();
            }
        });

        //ВЫБОР РАБОЧЕГО НАПРЯЖЕНИЯ
        workUButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!reserveSwitch.isChecked()) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity5.this);
                    alert.setCancelable(false);
                    alert.setTitle("Выберите рабочее напряжение:");
                    final String arrWorkU[] = {"12", "24", "36","220", "380"};
                    alert.setItems(arrWorkU, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            workUText.setText(arrWorkU[which]);
                        }
                    });
                    alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    });
                    alert.show();
                }
                else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity5.this);
                    alert.setCancelable(false);
                    alert.setMessage("Выбор напряжения не доступен, так как группа резервная. " +
                            "Чтобы выбрать напряжение, нажмите на ползунок, сделав его неактивным");
                    alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    });
                    alert.show();
                }
                numberEdit.clearFocus();
            }
        });

        //ВЫБОР НАПРЯЖЕНИЯ МЕГАОММЕТРА
        uButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!reserveSwitch.isChecked()) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity5.this);
                    alert.setCancelable(false);
                    alert.setTitle("Выберите напряжение мегаомметра:");
                    final String arrU[] = {"500", "1000", "2500"};
                    alert.setItems(arrU, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            uText.setText(arrU[which]);
                        }
                    });
                    alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    });
                    alert.show();
                }
                else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity5.this);
                    alert.setCancelable(false);
                    alert.setMessage("Выбор напряжения не доступен, так как группа резервная. " +
                            "Чтобы выбрать напряжение, нажмите на ползунок, сделав его неактивным");
                    alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    });
                    alert.show();
                }
                numberEdit.clearFocus();
            }
        });

        //ВЫБОР ДОПУСТИМОГО СОПРОТИВЛЕНИЯ
        rButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!reserveSwitch.isChecked()) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity5.this);
                    alert.setCancelable(false);
                    alert.setTitle("Выберите допустимое сопротивление:");
                    final String arrR[] = {"0,5", "1"};
                    alert.setItems(arrR, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            rText.setText(arrR[which]);
                        }
                    });
                    alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    });
                    alert.show();
                }
                else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity5.this);
                    alert.setCancelable(false);
                    alert.setMessage("Выбор сопротивления не доступен, так как группа резервная. " +
                            "Чтобы выбрать сопротивление, нажмите на ползунок, сделав его неактивным");
                    alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    });
                    alert.show();
                }
                numberEdit.clearFocus();
            }
        });

        //ВЫБОР ФАЗЫ
        phaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((veinText.getText().toString().equals("4") || veinText.getText().toString().equals("5"))) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity5.this);
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
                    if (!reserveSwitch.isChecked()) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity5.this);
                        alert.setCancelable(false);
                        alert.setTitle("Выберите фазу:");
                        final String phases[] = {"A", "B", "C"};
                        alert.setItems(phases, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                phaseText.setText(phases[which]);
                            }
                        });
                        alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        });
                        alert.show();
                    }
                    else {
                        AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity5.this);
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
                numberEdit.clearFocus();
            }
        });

        //ДОБАВЛЕНИЕ ГРУППЫ (СОХРАНЕНИЕ ДАННЫХ)
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isBadAut = false;
                String uzoDiff = uzoDiffText.getText().toString();
                String uDiff = uDiffText.getText().toString();
                String iNomDiff = iNomDiffText.getText().toString();
                String typeOverload = typeOverloadText.getText().toString();
                String nomApp = nominalApparatText.getText().toString();
                if (autOrDifAutSwitch.isChecked()) {
                    if (isIncorrectInput(uzoDiff, uzoDiffLayout) |
                        isIncorrectInput(uDiff, uDiffLayout) |
                        isIncorrectInput(iNomDiff, iNomDiffLayout))
                        isBadAut = true;
                    if (!isBadAut)
                        if (uDiffText.getText().toString().equals("380"))
                            numb_poles = "3P";
                        else
                            numb_poles = "1P";
                }
                else
                    if (isIncorrectInput(nomApp, nominalApparatLayout) |
                        isIncorrectInput(typeOverload, typeOverloadLayout))
                        isBadAut = true;
                String place;
                if (vvodSwitch.isChecked())
                    place = "Вводной";
                else
                    place = "";
                String symbol = symbolText.getText().toString();
                String nameAut = nameAutomatText.getText().toString() + " " + numb_poles;
                String type_kz = typeKzText.getText().toString();
                String nominal = nominalText.getText().toString();
                String range = rangeText.getText().toString();
                String nameGr = nameGroupText.getText().toString();
                String mark = markText.getText().toString();
                String vein = veinText.getText().toString();
                String section = sectionText.getText().toString();
                String workU = workUText.getText().toString();
                String u = uText.getText().toString();
                String r = rText.getText().toString();
                String phase = phaseText.getText().toString();
                String numb = numberEdit.getText().toString();
                Boolean isBadGroup = false;
                if (!vvodSwitch.isChecked()) {
                    if (isIncorrectInput(mark, markLayout) | isIncorrectInput(vein, veinLayout) |
                        isIncorrectInput(section, sectionLayout) | isIncorrectInput(workU, workULayout) |
                        isIncorrectInput(phase, phaseLayout) | isIncorrectInput(numb, numbLayout)) {
                        if ((vein.equals("2") && phase.equals("-")) || (vein.equals("3") && phase.equals("-")))
                            phaseLayout.setBackgroundResource(R.drawable.incorrect_input);
                        if (numb.contains(",") && Double.parseDouble(numb.replace(",",".")) > 1)
                            numbLayout.setBackgroundResource(R.drawable.incorrect_input);
                        isBadGroup = true;
                    }
                }
                //ПРОВЕРКА НА ВВОД ВСЕХ ДАННЫХ
                if (isIncorrectInput(nameAutomatText.getText().toString(), nameAutomatLayout) |
                    isIncorrectInput(type_kz, typeKzLayout) | isIncorrectInput(nominal, nominalLayout) |
                    isIncorrectInput(range, rangeLayout) | isIncorrectInput(symbol, symbolLayout) |
                    isBadAut | isBadGroup) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity5.this);
                    alert.setCancelable(false);
                    alert.setMessage("Заполните все поля!");
                    alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    });
                    alert.show();
                }
                else {
                    // ПРОВЕРКА ЭТАЖЕЙ, КОМНАТ И ЩИТОВ В ДИФ/ОБЫЧН. АВТОМАТАХ
                    int f_id = checkFloorsInAutomatItem(database, nameFloor, autOrDifAutSwitch.isChecked());
                    int r_id = checkRoomsInAutomatItem(database, nameRoom, f_id, autOrDifAutSwitch.isChecked());
                    int l_id = checkLinesInAutomatItem(database, nameLine, r_id, autOrDifAutSwitch.isChecked());
                    // ЕСЛИ ВНОСИМ ИЗМЕНЕНИЯ, ТО ДОСТАЁМ АЙДИ АВТОМАТА
                    int idAutomat = 0;
                    if (isChange) {
                        Cursor cursor30 = database.query(DBHelper.TABLE_GROUPS, new String[] {
                                DBHelper.GR_AUT_ID}, "_id = ?", new String[] {String.valueOf(idGroup)}, null, null, null);
                        if (cursor30.moveToFirst()) {
                            int idIndex = cursor30.getColumnIndex(DBHelper.GR_AUT_ID);
                            idAutomat = cursor30.getInt(idIndex);
                        }
                        cursor30.close();
                    }
                    else if (isNewSubgroup) {
                        Cursor cursor30 = database.query(DBHelper.TABLE_GROUPS, new String[] {
                                DBHelper.GR_AUT_ID}, "_id = ?", new String[] {String.valueOf(idSubgroup)}, null, null, null);
                        if (cursor30.moveToFirst()) {
                            int idIndex = cursor30.getColumnIndex(DBHelper.GR_AUT_ID);
                            idAutomat = cursor30.getInt(idIndex);
                        }
                        cursor30.close();
                    }
                    // ДОБАВЛЯЕМ ДИФ./ОБЫЧН. АВТОМАТ
                    if (autOrDifAutSwitch.isChecked()) {
                        String type_switch = type_kz + " " + nominal;
                        String i_extra = getI_Extra(iNomDiff);
                        String time_extra = getTimeExtra(uDiff);
                        //УДАЛИМ НАШ АППАРАТ, ЕСЛИ МЫ ВНОСИМ ИЗМЕНЕНИЯ, А НЕ СОЗДАЕМ НОВЫЙ
                        if (isChange || isNewSubgroup)
                            database.delete(DBHelper.TABLE_DIF_AUTOMATICS, "_id = ?", new String[]{String.valueOf(idAutomat)});
                        //СОЗДАЕМ НОВУЮ ЗАПИСЬ
                        ContentValues contentValues = new ContentValues();
                        if (isChange || isNewSubgroup)
                            contentValues.put(DBHelper.DIF_AU_ID, idAutomat);
                        contentValues.put(DBHelper.DIF_AU_ID_ALINE, l_id);
                        contentValues.put(DBHelper.DIF_AU_PLACE, symbol);
                        contentValues.put(DBHelper.DIF_AU_UZO, uzoDiff);
                        contentValues.put(DBHelper.DIF_AU_TYPE_SWITCH, type_switch);
                        contentValues.put(DBHelper.DIF_AU_U, uDiff);
                        contentValues.put(DBHelper.DIF_AU_SET_THERMAL, "-");
                        contentValues.put(DBHelper.DIF_AU_SET_ELECTR_MAGN, range);
                        contentValues.put(DBHelper.DIF_AU_CHECK_TEST_TOK, "-");
                        contentValues.put(DBHelper.DIF_AU_CHECK_TIME_, "-");
                        contentValues.put(DBHelper.DIF_AU_CHECK_WORK_TOK, "-");
                        contentValues.put(DBHelper.DIF_AU_I_NOM, iNomDiff);
                        contentValues.put(DBHelper.DIF_AU_I_LEAK, "-");
                        contentValues.put(DBHelper.DIF_AU_I_EXTRA, i_extra);
                        contentValues.put(DBHelper.DIF_AU_I_MEASURED, "Нет");
                        contentValues.put(DBHelper.DIF_AU_TIME_EXTRA, time_extra);
                        contentValues.put(DBHelper.DIF_AU_TIME_MEASURED, "Нет");
                        contentValues.put(DBHelper.DIF_AU_CONCLUSION, "Нет");
                        database.insert(DBHelper.TABLE_DIF_AUTOMATICS, null, contentValues);
                    }
                    else {
                        String set_overload = getOverloadSet(nominal);
                        String test_tok = getTestTok(set_overload);
                        String time_measured = getTimeMeasured(nominal);
                        String time_permissible = getTimePermissible(nominal);
                        String tok_work = getTokWork(range);
                        String time_work = getTimeWork(numb_poles);
                        String conclusion = getConclusion(time_permissible, time_measured, range, tok_work);
                        //УДАЛИМ НАШ АППАРАТ, ЕСЛИ МЫ ВНОСИМ ИЗМЕНЕНИЯ, А НЕ СОЗДАЕМ НОВЫЙ
                        if (isChange || isNewSubgroup)
                            database.delete(DBHelper.TABLE_AUTOMATICS, "_id = ?", new String[]{String.valueOf(idAutomat)});
                        //СОЗДАЕМ НОВУЮ ЗАПИСЬ
                        ContentValues contentValues = new ContentValues();
                        if (isChange || isNewSubgroup)
                            contentValues.put(DBHelper.AU_ID, idAutomat);
                        contentValues.put(DBHelper.AU_ID_ALINE, l_id);
                        contentValues.put(DBHelper.AU_PLACE, place);
                        contentValues.put(DBHelper.AU_SYMBOL_SCHEME, symbol);
                        contentValues.put(DBHelper.AU_NAME, nameAut);
                        contentValues.put(DBHelper.AU_TYPE_OVERLOAD, typeOverload);
                        contentValues.put(DBHelper.AU_TYPE_KZ, type_kz);
                        contentValues.put(DBHelper.AU_EXCERPT, "-");
                        contentValues.put(DBHelper.AU_NOMINAL_1, nomApp);
                        contentValues.put(DBHelper.AU_NOMINAL_2, nominal);
                        contentValues.put(DBHelper.AU_SET_OVERLOAD, set_overload);
                        contentValues.put(DBHelper.AU_SET_KZ, range);
                        contentValues.put(DBHelper.AU_TEST_TOK, test_tok);
                        contentValues.put(DBHelper.AU_TIME_PERMISSIBLE, time_permissible);
                        contentValues.put(DBHelper.AU_TIME_MEASURED, time_measured);
                        contentValues.put(DBHelper.AU_LENGTH_ANNEX, "1,6");
                        contentValues.put(DBHelper.AU_TOK_WORK, tok_work);
                        contentValues.put(DBHelper.AU_TIME_WORK, time_work);
                        contentValues.put(DBHelper.AU_CONCLUSION, conclusion);
                        database.insert(DBHelper.TABLE_AUTOMATICS, null, contentValues);
                    }
                    // ЕСЛИ АВТОМАТ НЕ ВВОДНОЙ, ТО ДОБАВЛЯЕМ ГРУППУ
                    if (!vvodSwitch.isChecked()) {
                        String aut_type;
                        if (autOrDifAutSwitch.isChecked())
                            aut_type = "dif_aut";
                        else
                            aut_type = "normal_aut";
                        // ДОСТАЁМ АЙДИ ДОБАВЛЕННОГО АВТОМАТА
                        if (!isChange && !isNewSubgroup) {
                            Cursor cursor3 = database.rawQuery("SELECT last_insert_rowid() as _id", new String[]{});
                            if (cursor3.moveToFirst()) {
                                int idIndex = cursor3.getColumnIndex("_id");
                                idAutomat = cursor3.getInt(idIndex);
                            }
                            cursor3.close();
                        }
                        //УДАЛИМ НАШУ ГРУППУ, ЕСЛИ МЫ ВНОСИМ ИЗМЕНЕНИЯ, А НЕ СОЗДАЕМ НОВУЮ
                        if (isChange)
                            database.delete(DBHelper.TABLE_GROUPS, "_id = ?", new String[]{String.valueOf(idGroup)});
                        //СОЗДАЕМ НОВУЮ ГРУППУ
                        if (reserveSwitch.isChecked()) {
                            ContentValues contentValues = new ContentValues();
                            if (isChange)
                                contentValues.put(DBHelper.GR_ID, idGroup);
                            contentValues.put(DBHelper.GR_LINE_ID, idLine);
                            contentValues.put(DBHelper.GR_AUTOMATIC, nameAut);
                            contentValues.put(DBHelper.GR_TYPE_KZ, type_kz);
                            contentValues.put(DBHelper.GR_NOMINAL, nominal);
                            contentValues.put(DBHelper.GR_RANGE, range);
                            contentValues.put(DBHelper.GR_NAME, "Гр " + nameGr.substring(8));
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
                            contentValues.put(DBHelper.GR_AUT_TYPE, aut_type);
                            contentValues.put(DBHelper.GR_AUT_ID, idAutomat);
                            database.insert(DBHelper.TABLE_GROUPS, null, contentValues);
                        } else {
                            //ЗАПОЛНЕНИЕ НОВОЙ СТРОКИ
                            ContentValues contentValues = new ContentValues();
                            if (isChange)
                                contentValues.put(DBHelper.GR_ID, idGroup);
                            contentValues.put(DBHelper.GR_LINE_ID, idLine);
                            contentValues.put(DBHelper.GR_AUTOMATIC, nameAut);
                            contentValues.put(DBHelper.GR_TYPE_KZ, type_kz);
                            contentValues.put(DBHelper.GR_NOMINAL, nominal);
                            contentValues.put(DBHelper.GR_RANGE, range);
                            contentValues.put(DBHelper.GR_NAME, "Гр " + nameGr.substring(8));
                            contentValues.put(DBHelper.GR_U1, workU);
                            contentValues.put(DBHelper.GR_MARK, mark);
                            contentValues.put(DBHelper.GR_VEIN, vein);
                            contentValues.put(DBHelper.GR_SECTION, section);
                            contentValues.put(DBHelper.GR_U2, u);
                            contentValues.put(DBHelper.GR_R, r);
                            contentValues.put(DBHelper.GR_PHASE, phase);
                            if (Double.parseDouble(numb.replace(",", ".")) >= Double.parseDouble(r.replace(",", ".")))
                                contentValues.put(DBHelper.GR_CONCLUSION, "соответст.");
                            else
                                contentValues.put(DBHelper.GR_CONCLUSION, "не соотв.");
                            contentValues.put(DBHelper.GR_AUT_TYPE, aut_type);
                            contentValues.put(DBHelper.GR_AUT_ID, idAutomat);
                            //2 ЖИЛЫ
                            if (vein.equals("2")) {
                                contentValues.put(DBHelper.GR_A_B, "-");
                                contentValues.put(DBHelper.GR_B_C, "-");
                                contentValues.put(DBHelper.GR_C_A, "-");
                                contentValues.put(DBHelper.GR_A_PE, "-");
                                contentValues.put(DBHelper.GR_B_PE, "-");
                                contentValues.put(DBHelper.GR_C_PE, "-");
                                contentValues.put(DBHelper.GR_N_PE, "-");
                                if (phase.equals("A")) {
                                    contentValues.put(DBHelper.GR_A_N, numb);
                                    contentValues.put(DBHelper.GR_B_N, "-");
                                    contentValues.put(DBHelper.GR_C_N, "-");
                                }
                                if (phase.equals("B")) {
                                    contentValues.put(DBHelper.GR_A_N, "-");
                                    contentValues.put(DBHelper.GR_B_N, numb);
                                    contentValues.put(DBHelper.GR_C_N, "-");
                                }
                                if (phase.equals("C")) {
                                    contentValues.put(DBHelper.GR_A_N, "-");
                                    contentValues.put(DBHelper.GR_B_N, "-");
                                    contentValues.put(DBHelper.GR_C_N, numb);
                                }
                            }
                            //3 ЖИЛЫ
                            if (vein.equals("3")) {
                                String[] arrayRand = new String[2];
                                pushArray(arrayRand, numb);
                                contentValues.put(DBHelper.GR_A_B, "-");
                                contentValues.put(DBHelper.GR_B_C, "-");
                                contentValues.put(DBHelper.GR_C_A, "-");
                                if (phase.equals("A")) {
                                    contentValues.put(DBHelper.GR_A_N, numb);
                                    contentValues.put(DBHelper.GR_B_N, "-");
                                    contentValues.put(DBHelper.GR_C_N, "-");
                                    contentValues.put(DBHelper.GR_A_PE, arrayRand[0]);
                                    contentValues.put(DBHelper.GR_B_PE, "-");
                                    contentValues.put(DBHelper.GR_C_PE, "-");
                                    contentValues.put(DBHelper.GR_N_PE, arrayRand[1]);
                                }
                                if (phase.equals("B")) {
                                    contentValues.put(DBHelper.GR_A_N, "-");
                                    contentValues.put(DBHelper.GR_B_N, numb);
                                    contentValues.put(DBHelper.GR_C_N, "-");
                                    contentValues.put(DBHelper.GR_A_PE, "-");
                                    contentValues.put(DBHelper.GR_B_PE, arrayRand[0]);
                                    contentValues.put(DBHelper.GR_C_PE, "-");
                                    contentValues.put(DBHelper.GR_N_PE, arrayRand[1]);
                                }
                                if (phase.equals("C")) {
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
                            if (vein.equals("4")) {
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
                            if (vein.equals("5")) {
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
                    }
                    if (stopIndexSwap != -1 && currentIndexSwap != -1)
                        swapGroups(stopIndexSwap, currentIndexSwap);
                    Toast toast2 = Toast.makeText(getApplicationContext(),
                            "Данные сохранены", Toast.LENGTH_SHORT);
                    toast2.show();
                    //ПЕРЕХОД К СПИСКАМ ГРУПП
                    Intent intent = new Intent("android.intent.action.Insulation4");
                    intent.putExtra("nameFloor", nameFloor);
                    intent.putExtra("idFloor", idFloor);
                    intent.putExtra("nameRoom", nameRoom);
                    intent.putExtra("idRoom", idRoom);
                    intent.putExtra("nameLine", nameLine);
                    intent.putExtra("idLine", idLine);
                    startActivity(intent);
                }
            }
        });
    }

    // АППАРАТНОЕ НАЗАД
    @Override
    public void onBackPressed() {
        changeNameSubgroup();
        super.onBackPressed();
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
                changeNameSubgroup();
                Intent intent = new Intent("android.intent.action.Insulation4");
                intent.putExtra("nameFloor", nameFloor);
                intent.putExtra("idFloor", idFloor);
                intent.putExtra("nameRoom", nameRoom);
                intent.putExtra("idRoom", idRoom);
                intent.putExtra("nameLine", nameLine);
                intent.putExtra("idLine", idLine);
                startActivity(intent);
                return true;
            case R.id.action_main:
                changeNameSubgroup();
                Intent intent1 = new Intent(InsulationActivity5.this, MenuItemsActivity.class);
                startActivity(intent1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void changeNameSubgroup() {
        if (nameGroup.contains("/2")) {
            ContentValues uppname = new ContentValues();
            uppname.put(DBHelper.GR_NAME, "Гр " + nameGroup.substring(8, nameGroup.indexOf("/")));
            database.update(DBHelper.TABLE_GROUPS,
                    uppname,
                    "_id = ?",
                    new String[]{String.valueOf(idSubgroup)});
        }
    }

    boolean isIncorrectInput(String data, LinearLayout layout) {
        if (data.equals("") || data.equals("Нет")) {
            layout.setBackgroundResource(R.drawable.incorrect_input);
            return true;
        }
        layout.setBackgroundResource(R.drawable.listview);
        return false;
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

    public int checkFloorsInAutomatItem(SQLiteDatabase db, String floor, Boolean isDif) {
        int f_id = 0;
        if (isDif) {
            Cursor cursor1 = db.rawQuery("SELECT _id, dif_au_floor FROM dif_au_floors WHERE dif_au_floor = ?", new String[] { floor });
            if (cursor1.moveToFirst()) {
                int idIndex = cursor1.getColumnIndex("_id");
                f_id = cursor1.getInt(idIndex);
            }
            else {
                ContentValues contentValues = new ContentValues();
                contentValues.put(DBHelper.DIF_AU_FL_NAME, floor);
                db.insert(DBHelper.TABLE_DIF_AU_FLOORS, null, contentValues);
                Cursor cursor2 = db.rawQuery("SELECT last_insert_rowid() as _id", new String[] { });
                if (cursor2.moveToFirst()) {
                    int idIndex = cursor2.getColumnIndex("_id");
                    f_id = cursor2.getInt(idIndex);
                }
                cursor2.close();
            }
            cursor1.close();
        }
        else {
            Cursor cursor1 = db.rawQuery("SELECT _id, au_floor FROM au_floors WHERE au_floor = ?", new String[] { floor });
            if (cursor1.moveToFirst()) {
                int idIndex = cursor1.getColumnIndex("_id");
                f_id = cursor1.getInt(idIndex);
            }
            else {
                ContentValues contentValues = new ContentValues();
                contentValues.put(DBHelper.AU_FL_NAME, floor);
                db.insert(DBHelper.TABLE_AU_FLOORS, null, contentValues);
                Cursor cursor2 = db.rawQuery("SELECT last_insert_rowid() as _id", new String[] { });
                if (cursor2.moveToFirst()) {
                    int idIndex = cursor2.getColumnIndex("_id");
                    f_id = cursor2.getInt(idIndex);
                }
                cursor2.close();
            }
            cursor1.close();
        }
        return f_id;
    }

    public int checkRoomsInAutomatItem(SQLiteDatabase db, String room, int f_id, Boolean isDif) {
        int r_id = 0;
        if (isDif) {
            Cursor cursor1 = db.rawQuery("SELECT _id, dif_au_room FROM dif_au_rooms WHERE dif_au_room = ?", new String[] { room });
            if (cursor1.moveToFirst()) {
                int idIndex = cursor1.getColumnIndex("_id");
                r_id = cursor1.getInt(idIndex);
            }
            else {
                ContentValues contentValues = new ContentValues();
                contentValues.put(DBHelper.DIF_AU_ROOM_NAME, room);
                contentValues.put(DBHelper.DIF_AU_ID_RFLOOR, f_id);
                db.insert(DBHelper.TABLE_DIF_AU_ROOMS, null, contentValues);
                Cursor cursor2 = db.rawQuery("SELECT last_insert_rowid() as _id", new String[] { });
                if (cursor2.moveToFirst()) {
                    int idIndex = cursor2.getColumnIndex("_id");
                    r_id = cursor2.getInt(idIndex);
                }
                cursor2.close();
            }
            cursor1.close();
        }
        else {
            Cursor cursor1 = db.rawQuery("SELECT _id, au_room FROM au_rooms WHERE au_room = ?", new String[] { room });
            if (cursor1.moveToFirst()) {
                int idIndex = cursor1.getColumnIndex("_id");
                r_id = cursor1.getInt(idIndex);
            }
            else {
                ContentValues contentValues = new ContentValues();
                contentValues.put(DBHelper.AU_ROOM_NAME, room);
                contentValues.put(DBHelper.AU_ID_RFLOOR, f_id);
                db.insert(DBHelper.TABLE_AU_ROOMS, null, contentValues);
                Cursor cursor2 = db.rawQuery("SELECT last_insert_rowid() as _id", new String[] { });
                if (cursor2.moveToFirst()) {
                    int idIndex = cursor2.getColumnIndex("_id");
                    r_id = cursor2.getInt(idIndex);
                }
                cursor2.close();
            }
            cursor1.close();
        }
        return r_id;
    }

    public int checkLinesInAutomatItem(SQLiteDatabase db, String line, int r_id, Boolean isDif) {
        int l_id = 0;
        if (isDif) {
            Cursor cursor1 = db.rawQuery("SELECT _id, dif_au_line FROM dif_au_lines WHERE dif_au_line = ?", new String[] { line });
            if (cursor1.moveToFirst()) {
                int idIndex = cursor1.getColumnIndex("_id");
                l_id = cursor1.getInt(idIndex);
            }
            else {
                ContentValues contentValues = new ContentValues();
                contentValues.put(DBHelper.DIF_AU_LINE_NAME, line);
                contentValues.put(DBHelper.DIF_AU_ID_LROOM, r_id);
                db.insert(DBHelper.TABLE_DIF_AU_LINES, null, contentValues);
                Cursor cursor2 = db.rawQuery("SELECT last_insert_rowid() as _id", new String[] { });
                if (cursor2.moveToFirst()) {
                    int idIndex = cursor2.getColumnIndex("_id");
                    l_id = cursor2.getInt(idIndex);
                }
                cursor2.close();
            }
            cursor1.close();
        }
        else {
            Cursor cursor1 = db.rawQuery("SELECT _id, au_line FROM au_lines WHERE au_line = ?", new String[] { line });
            if (cursor1.moveToFirst()) {
                int idIndex = cursor1.getColumnIndex("_id");
                l_id = cursor1.getInt(idIndex);
            }
            else {
                ContentValues contentValues = new ContentValues();
                contentValues.put(DBHelper.AU_LINE_NAME, line);
                contentValues.put(DBHelper.AU_ID_LROOM, r_id);
                db.insert(DBHelper.TABLE_AU_LINES, null, contentValues);
                Cursor cursor2 = db.rawQuery("SELECT last_insert_rowid() as _id", new String[] { });
                if (cursor2.moveToFirst()) {
                    int idIndex = cursor2.getColumnIndex("_id");
                    l_id = cursor2.getInt(idIndex);
                }
                cursor2.close();
            }
            cursor1.close();
        }
        return l_id;
    }

    String getI_Extra(String x) {
        String i_extra = "";
        switch (x) {
            case "30":
                i_extra = "15" + '\u003C' + "I" + '\u2264' + "30";
                break;
            case "100":
                i_extra = "50" + '\u003C' + "I" + '\u2264' + "100";
                break;
            case "300":
                i_extra = "150" + '\u003C' + "I" + '\u2264' + "300";
                break;
        }
        return i_extra;
    }

    String getTimeExtra(String x) {
        if (x.equals("220"))
            return "0,4";
        return "0,2";
    }

    public String getOverloadSet(String s) {
        BigDecimal overDecimal = new BigDecimal(s);
        BigDecimal k = new BigDecimal("2.55");
        BigDecimal result = overDecimal.multiply(k).setScale(1, ROUND_HALF_UP);
        String overString = result.toString();
        if (overString.contains("0") && overString.indexOf('0', overString.indexOf('.')) == overString.length() - 1)
            return overString.substring(0, overString.indexOf('.'));
        return overString.replace('.',',');
    }

    public String getTestTok(String numb) {
        String xStr, yStr;
        BigDecimal y;
        BigDecimal k1 = new BigDecimal("2.5");
        BigDecimal k2 = new BigDecimal("7.5");
        if (numb.contains(",")) {
            numb = numb.replace(',', '.');
            xStr = numb.substring(0,numb.indexOf('.') - 1);
            yStr = numb.substring(numb.indexOf('.') - 1, numb.length());
        } else {
            xStr = numb.substring(0, numb.length() - 1);
            yStr = numb.substring(numb.length() - 1, numb.length());
        }
        y = new BigDecimal(yStr);
        if (y.compareTo(k1) < 0)
            return xStr + "0";
        if (y.compareTo(k2) > 0)
            return String.valueOf(Integer.parseInt(xStr) + 1) + "0";
        return xStr + "5";
    }

    public String getTimeMeasured(String s) {
        int num = Integer.parseInt(s);
        Random generator = new Random();
        if (num <= 32)
            return String.valueOf(generator.nextInt(21) + 30);
        return String.valueOf(generator.nextInt(61) + 30);
    }

    public String getTimePermissible(String numb) {
        int num = Integer.parseInt(numb);
        if (num <= 32)
            return "60";
        return "120";
    }

    public String getTokWork(String range) {
        String numb;
        if (range.contains(">"))
            numb = range.substring(1);
        else
            numb = range.substring(range.indexOf("-") + 1);
        int x = Integer.parseInt(numb);
        Random generator = new Random();
        if (range.contains(">"))
            return String.valueOf(x + ((generator.nextInt(3) + 1) * 10));
        return String.valueOf(x - ((generator.nextInt(3) + 1) * 10));
    }

    public String getTimeWork(String name) {
        Random generator = new Random();
        if (name.contains("3P"))
            return "0," + String.valueOf(generator.nextInt(8) + 12);
        return "0," + String.valueOf(generator.nextInt(28) + 12);
    }

    public String getConclusion(String numb1, String numb2, String diapazon, String tok_w) {
        String concl = "не соотв.";
        if (diapazon.contains(">")){
            int numb = Integer.parseInt(diapazon.substring(1));
            if ((Integer.parseInt(numb2) < Integer.parseInt(numb1)) && (numb > Integer.parseInt(tok_w)))
                concl = "соответст.";
        }
        else {
            int left = Integer.parseInt(diapazon.substring(0, diapazon.indexOf('-')));
            int right = Integer.parseInt(diapazon.substring(diapazon.indexOf('-') + 1));
            if ((Integer.parseInt(numb2) < Integer.parseInt(numb1)) && (left <= Integer.parseInt(tok_w)) && (right >= Integer.parseInt(tok_w)))
                concl = "соответст.";
        }
        return concl;
    }

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
        if (1000 <= oldNumb && oldNumb < 3000)
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
        if (!num.contains(",")) {
            for (i = 0; i < arr.length; i++) {
                if (arr[i].equals(num))
                    count++;
            }
            if (count == arr.length && Integer.parseInt(num) > 20)
                pushArray(arr, num);
        }
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

    //ПОЛУЧЕНИЕ АВТОМАТОВ
    public String[] getAutomatics(SQLiteDatabase database) {
        final ArrayList<String> automatics = new ArrayList <String>();
        Cursor cursor = database.query(DBHelper.TABLE_LIBRARY_AUTOMATICS, new String[] {DBHelper.LIB_AU_NAME}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(DBHelper.LIB_AU_NAME);
            do {
                automatics.add(cursor.getString(nameIndex));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return automatics.toArray(new String[automatics.size()]);
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
