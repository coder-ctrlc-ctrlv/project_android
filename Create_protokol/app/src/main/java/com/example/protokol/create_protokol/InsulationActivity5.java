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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class InsulationActivity5 extends AppCompatActivity {

    DBHelper dbHelper;
    String nameFloor, nameRoom, nameLine;
    int idFloor, idRoom, idLine;
    String numb_poles = "1P";
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insulation5);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        final LinearLayout nameAutomatLayout = findViewById(R.id.nameAutomatLayout);
        final LinearLayout typeKzLayout = findViewById(R.id.typeKzLayout);
        final LinearLayout nominalLayout = findViewById(R.id.nominalLayout);
        final LinearLayout rangeLayout = findViewById(R.id.rangeLayout);
        final LinearLayout markLayout = findViewById(R.id.markLayout);
        final LinearLayout veinLayout = findViewById(R.id.veinLayout);
        final LinearLayout sectionLayout = findViewById(R.id.sectionLayout);
        final LinearLayout workULayout = findViewById(R.id.workULayout);
        final LinearLayout phaseLayout = findViewById(R.id.phaseLayout);
        final LinearLayout numbLayout = findViewById(R.id.numbLayout);

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
        String nameGroup = getIntent().getStringExtra("nameGroup");
        final int idGroup = getIntent().getIntExtra("idGroup", -1);
        final int idSubgroup = getIntent().getIntExtra("idSubgroup", -1);
        final int currentIndexSwap = getIntent().getIntExtra("currentIndexSwap", -1);
        final int stopIndexSwap = getIntent().getIntExtra("stopIndexSwap", -1);

        //НАСТРАИВАЕМ ACTIONBAR
        getSupportActionBar().setTitle("Изоляция");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ОПРЕДЕЛЯЕМ, СОЗДАЕМ ЛИ МЫ НОВУЮ ГРУППУ ИЛИ ИЗМЕНЯЕМ СТАРУЮ
        boolean isNewSubgroup = idSubgroup != -1;
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
        
        //ЗАПОЛНЕНИЕ ДАННЫХ, ЕСЛИ ОНИ ПЕРЕДАНЫ
        nameGroupText.setText(nameGroup);
        if (isChange) {
            //ЗАПРОС В БД(ПОЛУЧЕНИЕ ИНФОРМАЦИИ О ГРУППЕ)
            String nameAu = "";
            Cursor cursor = database.query(DBHelper.TABLE_GROUPS, new String[] {DBHelper.GR_ID,
                    DBHelper.GR_AUTOMATIC, DBHelper.GR_TYPE_KZ, DBHelper.GR_NOMINAL, DBHelper.GR_RANGE,
                    DBHelper.GR_MARK, DBHelper.GR_VEIN, DBHelper.GR_SECTION, DBHelper.GR_U1,
                    DBHelper.GR_U2, DBHelper.GR_R, DBHelper.GR_PHASE, DBHelper.GR_A_B,
                    DBHelper.GR_A_N, DBHelper.GR_B_N, DBHelper.GR_C_N}, "_id = ?", new String[] {String.valueOf(idGroup)}, null, null, null);
            if (cursor.moveToFirst()) {
                int nameAuIndex = cursor.getColumnIndex(DBHelper.GR_AUTOMATIC);
                int type_kzIndex = cursor.getColumnIndex(DBHelper.GR_TYPE_KZ);
                int nominalIndex = cursor.getColumnIndex(DBHelper.GR_NOMINAL);
                int rangeIndex = cursor.getColumnIndex(DBHelper.GR_RANGE);
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
            if (nameAu.contains("3P")) {
                radioGroup.check(R.id.three_poles);
                numb_poles = "3P";
            }
        }
        else if (isNewSubgroup) {
            //ЗАПРОС В БД(ПОЛУЧЕНИЕ ИНФОРМАЦИИ О ГРУППЕ)
            String nameAu = "";
            Cursor cursor = database.query(DBHelper.TABLE_GROUPS, new String[] {DBHelper.GR_ID,
                    DBHelper.GR_AUTOMATIC, DBHelper.GR_TYPE_KZ, DBHelper.GR_NOMINAL,
                    DBHelper.GR_RANGE}, "_id = ?", new String[] {String.valueOf(idSubgroup)}, null, null, null);
            if (cursor.moveToFirst()) {
                int nameAuIndex = cursor.getColumnIndex(DBHelper.GR_AUTOMATIC);
                int type_kzIndex = cursor.getColumnIndex(DBHelper.GR_TYPE_KZ);
                int nominalIndex = cursor.getColumnIndex(DBHelper.GR_NOMINAL);
                int rangeIndex = cursor.getColumnIndex(DBHelper.GR_RANGE);
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
            if (nameAu.contains("3P")) {
                radioGroup.check(R.id.three_poles);
                numb_poles = "3P";
            }
        }

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
                final String type_kz_array[] = {"B", "C", "D", "ОВВ+МД"};
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
                //ПРОВЕРКА НА ВВОД ВСЕХ ДАННЫХ
                if (isIncorrectInput(nameAutomatText.getText().toString(), nameAutomatLayout) |
                        isIncorrectInput(type_kz, typeKzLayout) | isIncorrectInput(nominal, nominalLayout) |
                        isIncorrectInput(range, rangeLayout) | isIncorrectInput(mark, markLayout) |
                        isIncorrectInput(vein, veinLayout) | isIncorrectInput(section, sectionLayout) |
                        isIncorrectInput(workU, workULayout) | isIncorrectInput(phase, phaseLayout) |
                        isIncorrectInput(numb, numbLayout) | ((vein.equals("2") && phase.equals("-")) ||
                        (vein.equals("3") && phase.equals("-")))) {
                    if ((vein.equals("2") && phase.equals("-")) || (vein.equals("3") && phase.equals("-")))
                        phaseLayout.setBackgroundResource(R.drawable.incorrect_input);
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
                    if (numb.contains(",") && Double.parseDouble(numb.replace(",",".")) > 1) {
                        numbLayout.setBackgroundResource(R.drawable.incorrect_input);
                        AlertDialog.Builder alert = new AlertDialog.Builder(InsulationActivity5.this);
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
                Intent intent1 = new Intent(InsulationActivity5.this, MainActivity.class);
                startActivity(intent1);
                return true;
        }
        return super.onOptionsItemSelected(item);
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
        if (!num.contains(",")) {
            for (i = 0; i < arr.length; i++) {
                if (arr[i].equals(num))
                    count++;
            }
            if (count == arr.length && Integer.parseInt(num) >= 300)
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
