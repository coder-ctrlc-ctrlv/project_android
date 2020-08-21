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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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

import static java.lang.Math.round;
import static java.math.BigDecimal.ROUND_HALF_UP;

public class AutomaticsActivity5 extends AppCompatActivity {

    DBHelper dbHelper;
    String nameFloor, nameRoom, nameLine;
    int idFloor, idRoom, idLine;
    EditText nominal_1Edit, nominal_2Edit;
    String numb_poles = "1P";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automatics5);

        dbHelper = new DBHelper(this);
        final SQLiteDatabase database = dbHelper.getWritableDatabase();

        final LinearLayout symbolLayout = findViewById(R.id.symbolLayout);
        final LinearLayout nameLayout = findViewById(R.id.nameLayout);
        final LinearLayout typeOverloadLayout = findViewById(R.id.typeOverloadLayout);
        final LinearLayout typeKzLayout = findViewById(R.id.typeKzLayout);
        final LinearLayout excerptLayout = findViewById(R.id.excerptLayout);
        final LinearLayout firstNominalLayout = findViewById(R.id.firstNominalLayout);
        final LinearLayout secondNominalLayout = findViewById(R.id.secondNominalLayout);
        final LinearLayout setOverloadLayout = findViewById(R.id.setOverloadLayout);
        final LinearLayout setKzLayout = findViewById(R.id.setKzLayout);
        final LinearLayout lengthAnnexLayout = findViewById(R.id.lengthAnnexLayout);
        final LinearLayout timeMeasuredLayout = findViewById(R.id.timeMeasuredLayout);
        final LinearLayout tokWorkLayout = findViewById(R.id.tokWorkLayout);
        final LinearLayout timeWorkLayout = findViewById(R.id.timeWorkLayout);

        final Switch placeSwitch = findViewById(R.id.switch1);
        final Button placeButton = findViewById(R.id.button25);
        final TextView nameText = findViewById(R.id.textView6);
        Button nameButton = findViewById(R.id.button40);
        final RadioGroup radioGroup = findViewById(R.id.radioGroup);
        final TextView type_overloadText = findViewById(R.id.textView20);
        Button type_overloadButton = findViewById(R.id.button26);
        final TextView type_kzText = findViewById(R.id.textView22);
        Button type_kzButton = findViewById(R.id.button29);
        final TextView symbolText = findViewById(R.id.textView21);
        Button symbolButton = findViewById(R.id.button38);
        final TextView excerptText = findViewById(R.id.textView39);
        Button excerptButton = findViewById(R.id.button59);
        final TextView set_overloadText = findViewById(R.id.textView30);
        Button set_overloadButton = findViewById(R.id.button30);
        final TextView set_kzText = findViewById(R.id.textView32);
        Button set_kzButton = findViewById(R.id.button31);
        final TextView time_measuredText = findViewById(R.id.textView23);
        final Button time_measuredButton = findViewById(R.id.button27);
        final TextView length_annexText = findViewById(R.id.textView25);
        Button length_annexButton = findViewById(R.id.button28);
        final TextView tok_workText = findViewById(R.id.textView61);
        final Button tok_workButton = findViewById(R.id.button41);
        final TextView time_workText = findViewById(R.id.textView75);
        Button time_workButton = findViewById(R.id.button77);
        nominal_1Edit = findViewById(R.id.editText8);
        nominal_2Edit = findViewById(R.id.editText7);
        Button saveButton = findViewById(R.id.button35);

        nameFloor = getIntent().getStringExtra("nameFloor");
        idFloor = getIntent().getIntExtra("idFloor", 0);
        nameRoom = getIntent().getStringExtra("nameRoom");
        idRoom = getIntent().getIntExtra("idRoom", 0);
        nameLine = getIntent().getStringExtra("nameLine");
        idLine = getIntent().getIntExtra("idLine", 0);
        String nameAutomat = getIntent().getStringExtra("nameAutomat");
        final int idAutomat = getIntent().getIntExtra("idAutomat", -1);

        //НАСТРАИВАЕМ ACTIONBAR
        getSupportActionBar().setTitle("Автомат. выключатели");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ОПРЕДЕЛЯЕМ, СОЗДАЕМ ЛИ МЫ НОВУЮ ГРУППУ ИЛИ ИЗМЕНЯЕМ СТАРУЮ
        final boolean change;
        if (idAutomat != -1) {
            change = true;
            getSupportActionBar().setSubtitle("Редактирование аппарата");
        }
        else {
            change = false;
            getSupportActionBar().setSubtitle("Добавление аппарата");
        }

        //ЗАПОЛНЕНИЕ ДАННЫХ, ЕСЛИ ОНИ ПЕРЕДАНЫ
        if (change) {
            nameText.setText(nameAutomat.substring(0, nameAutomat.length() - 3));
            //ЗАПРОС В БД(ПОЛУЧЕНИЕ ИНФОРМАЦИИ О ГРУППЕ)
            Cursor cursor = database.query(DBHelper.TABLE_AUTOMATICS, new String[]{DBHelper.AU_ID, DBHelper.AU_PLACE,
                    DBHelper.AU_SYMBOL_SCHEME, DBHelper.AU_TYPE_OVERLOAD, DBHelper.AU_TYPE_KZ,
                    DBHelper.AU_EXCERPT, DBHelper.AU_NOMINAL_1, DBHelper.AU_NOMINAL_2, DBHelper.AU_SET_OVERLOAD, DBHelper.AU_SET_KZ,
                    DBHelper.AU_TIME_MEASURED, DBHelper.AU_LENGTH_ANNEX,
                    DBHelper.AU_TOK_WORK, DBHelper.AU_TIME_WORK}, "_id = ?", new String[] {String.valueOf(idAutomat)}, null, null, null);
            if (cursor.moveToFirst()) {
                int placeIndex = cursor.getColumnIndex(DBHelper.AU_PLACE);
                int symbolIndex = cursor.getColumnIndex(DBHelper.AU_SYMBOL_SCHEME);
                int type_overloadIndex = cursor.getColumnIndex(DBHelper.AU_TYPE_OVERLOAD);
                int type_kzIndex = cursor.getColumnIndex(DBHelper.AU_TYPE_KZ);
                int excerptIndex = cursor.getColumnIndex(DBHelper.AU_EXCERPT);
                int nominal_1Index = cursor.getColumnIndex(DBHelper.AU_NOMINAL_1);
                int nominal_2Index = cursor.getColumnIndex(DBHelper.AU_NOMINAL_2);
                int set_overloadIndex = cursor.getColumnIndex(DBHelper.AU_SET_OVERLOAD);
                int set_kzIndex = cursor.getColumnIndex(DBHelper.AU_SET_KZ);
                int time_measuredIndex = cursor.getColumnIndex(DBHelper.AU_TIME_MEASURED);
                int length_annexIndex = cursor.getColumnIndex(DBHelper.AU_LENGTH_ANNEX);
                int tok_workIndex = cursor.getColumnIndex(DBHelper.AU_TOK_WORK);
                int time_workIndex = cursor.getColumnIndex(DBHelper.AU_TIME_WORK);
                do {
                    placeSwitch.setChecked(cursor.getString(placeIndex).equals("Вводной"));
                    symbolText.setText(cursor.getString(symbolIndex));
                    type_overloadText.setText(cursor.getString(type_overloadIndex));
                    type_kzText.setText(cursor.getString(type_kzIndex));
                    excerptText.setText(cursor.getString(excerptIndex));
                    nominal_1Edit.setText(cursor.getString(nominal_1Index));
                    nominal_2Edit.setText(cursor.getString(nominal_2Index));
                    set_overloadText.setText(cursor.getString(set_overloadIndex));
                    set_kzText.setText(cursor.getString(set_kzIndex));
                    time_measuredText.setText(cursor.getString(time_measuredIndex));
                    length_annexText.setText(cursor.getString(length_annexIndex));
                    tok_workText.setText(cursor.getString(tok_workIndex));
                    time_workText.setText(cursor.getString(time_workIndex));
                } while (cursor.moveToNext());
            }
            cursor.close();
            if (nameAutomat.contains("3P")) {
                radioGroup.check(R.id.three_poles);
                numb_poles = "3P";
            }
        }
        else {
            //НАЧАЛЬНОЕ ЗНАЧЕНИЯ ВРЕМЕНИ СРАБАТЫВАНИЯ РАСЦЕПИТЕЛЯ КЗ
            time_workText.setText(getTimeWork(numb_poles));
        }

        //МЕСТО УСТАНОВКИ
        placeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeSwitch.setChecked(!placeSwitch.isChecked());
                clearFocus();
            }
        });

        //ОБОЗНАЧЕНИЕ ПО СХЕМЕ
        symbolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert1 = new AlertDialog.Builder(AutomaticsActivity5.this);
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
                clearFocus();
            }
        });

        //НАИМЕНОВАНИЕ АППАРАТА, ТИП, КАТАЛОЖНЫЙ ИЛИ СЕРИЙНЫЙ НОМЕР
        nameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(AutomaticsActivity5.this);
                final View myView = getLayoutInflater().inflate(R.layout.dialog_for_marks,null);
                alert.setCancelable(false);
                alert.setTitle("Введите наименование аппарата, тип и номер:");
                final AutoCompleteTextView automatics = myView.findViewById(R.id.autoCompleteTextView3);
                ImageView arrow = myView.findViewById(R.id.imageView4);
                //ОТОБРАЖЕНИЕ ВЫПЛЫВАЮЩЕГО СПИСКА
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(AutomaticsActivity5.this, android.R.layout.simple_dropdown_item_1line, getAutomatics(database));
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
                        nameText.setText(nameAu);
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
                clearFocus();
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
                time_workText.setText(getTimeWork(numb_poles));
            }
        });

        //ТИПЫ РАСЦЕПИТЕЛЕЙ ПЕРЕЗАГРУЗКИ
        type_overloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(AutomaticsActivity5.this);
                alert.setCancelable(false);
                alert.setTitle("Выберите тип расцепителей перегрузки:");
                final String type_overload_array[] = {"ОВВ", "НВВ"};
                alert.setItems(type_overload_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        type_overloadText.setText(type_overload_array[which]);
                    }
                });
                alert.setPositiveButton("Ввести", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        AlertDialog.Builder alert1 = new AlertDialog.Builder(AutomaticsActivity5.this);
                        final View myView = getLayoutInflater().inflate(R.layout.dialog_for_names,null);
                        alert1.setCancelable(false);
                        alert1.setTitle("Введите тип расцепителей перегрузки:");
                        final EditText input = myView.findViewById(R.id.editText);
                        openKeyboard();
                        alert1.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                closeKeyboard(myView);
                                String type_overload = input.getText().toString();
                                type_overloadText.setText(type_overload);
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
                clearFocus();
            }
        });

        //ТИПЫ РАСЦЕПИТЕЛЕЙ КОРОТКОГО ЗАМЫКАНИЯ
        type_kzButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(AutomaticsActivity5.this);
                alert.setCancelable(false);
                alert.setTitle("Выберите тип расцепителей к.з.:");
                final String type_kz_array[] = {"B", "C", "D", "МД"};
                alert.setItems(type_kz_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nominal_2_str = nominal_2Edit.getText().toString();
                        if (!nominal_2_str.equals("")) {
                            int nominal_2_int = Integer.parseInt(nominal_2_str);
                            String set_kz_str;
                            switch (which) {
                                case 0:
                                    set_kz_str = String.valueOf(nominal_2_int * 3) + "-" + String.valueOf(nominal_2_int * 5);
                                    set_kzText.setText(set_kz_str);
                                    tok_workText.setText(getTokWork(String.valueOf(nominal_2_int * 5), true));
                                    break;
                                case 1:
                                    set_kz_str = String.valueOf(nominal_2_int * 5) + "-" + String.valueOf(nominal_2_int * 10);
                                    set_kzText.setText(set_kz_str);
                                    tok_workText.setText(getTokWork(String.valueOf(nominal_2_int * 10), true));
                                    break;
                                case 2:
                                    set_kz_str = String.valueOf(nominal_2_int * 10) + "-" + String.valueOf(nominal_2_int * 20);
                                    set_kzText.setText(set_kz_str);
                                    tok_workText.setText(getTokWork(String.valueOf(nominal_2_int * 20), true));
                                    break;
                                case 3:
                                    set_kzText.setText("Нет");
                                    tok_workText.setText("Нет");
                                    break;
                            }
                        }
                        type_kzText.setText(type_kz_array[which]);
                    }
                });
                alert.setPositiveButton("Ввести", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        AlertDialog.Builder alert1 = new AlertDialog.Builder(AutomaticsActivity5.this);
                        final View myView = getLayoutInflater().inflate(R.layout.dialog_for_names,null);
                        alert1.setCancelable(false);
                        alert1.setTitle("Введите тип расцепителей к.з.:");
                        final EditText input = myView.findViewById(R.id.editText);
                        openKeyboard();
                        alert1.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                closeKeyboard(myView);
                                String type_kz = input.getText().toString();
                                type_kzText.setText(type_kz);
                                set_kzText.setText("Нет");
                                tok_workText.setText("Нет");
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
                clearFocus();
            }
        });

        //ЗАДАННАЯ ВЫДЕРЖКА ВРЕМЕНИ ОТКЛЮЧЕНИЯ ПРИ К.З.
        excerptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert1 = new AlertDialog.Builder(AutomaticsActivity5.this);
                final View myView = getLayoutInflater().inflate(R.layout.dialog_for_excerpt,null);
                alert1.setCancelable(false);
                alert1.setTitle("Введите выдержку времени отключения при к.з.:");
                final EditText input = myView.findViewById(R.id.editText2);
                openKeyboard();
                alert1.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        closeKeyboard(myView);
                        String excerpt = input.getText().toString();
                        excerptText.setText(excerpt);
                    }
                });
                alert1.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        closeKeyboard(myView);
                    }
                });
                alert1.setView(myView);
                alert1.show();
                clearFocus();
            }
        });

        //НОМИНАЛЬНЫЙ ТОК РАСЦЕПИТЕЛЯ
        nominal_2Edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("") && s.toString().length() < 7) {
                    set_overloadText.setText(getOverloadSet(s));
                    time_measuredText.setText(getTimeMeasured(s));
                    String type_kz = type_kzText.getText().toString();
                    if (type_kz.equals("B") | type_kz.equals("C") | type_kz.equals("D")) {
                        int s_int = Integer.parseInt(s.toString());
                        String set_kz;
                        if (type_kz.equals("B")) {
                            set_kz = String.valueOf(3 * s_int) + "-" + String.valueOf(5 * s_int);
                            set_kzText.setText(set_kz);
                            tok_workText.setText(getTokWork(String.valueOf(5 * s_int), true));
                        }
                        if (type_kz.equals("C")) {
                            set_kz = String.valueOf(5 * s_int) + "-" + String.valueOf(10 * s_int);
                            set_kzText.setText(set_kz);
                            tok_workText.setText(getTokWork(String.valueOf(10 * s_int), true));
                        }
                        if (type_kz.equals("D")) {
                            set_kz = String.valueOf(10 * s_int) + "-" + String.valueOf(20 * s_int);
                            set_kzText.setText(set_kz);
                            tok_workText.setText(getTokWork(String.valueOf(20 * s_int), true));
                        }
                    }
                } else {
                    set_overloadText.setText("Нет");
                    time_measuredText.setText("Нет");
                    tok_workText.setText("Нет");
                    set_kzText.setText("Нет");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //УСТАВКА РАСЦЕПИТЕЛЕЙ ПЕРЕГРУЗКИ
        set_overloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert1 = new AlertDialog.Builder(AutomaticsActivity5.this);
                final View myView = getLayoutInflater().inflate(R.layout.dialog_for_section,null);
                alert1.setCancelable(false);
                alert1.setTitle("Введите уставку расцепителей перегрузки:");
                final EditText input = myView.findViewById(R.id.editText2);
                openKeyboard();
                alert1.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        closeKeyboard(myView);
                        String set_overload = input.getText().toString();
                        set_overloadText.setText(set_overload);
                    }
                });
                alert1.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        closeKeyboard(myView);
                    }
                });
                alert1.setView(myView);
                alert1.show();
                clearFocus();
            }
        });

        //УСТАВКА РАСЦЕПИТЕЛЕЙ КОРОТКОГО ЗАМЫКАНИЯ
        set_kzButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert1 = new AlertDialog.Builder(AutomaticsActivity5.this);
                final View myView = getLayoutInflater().inflate(R.layout.dialog_for_set_kz,null);
                alert1.setCancelable(false);
                alert1.setTitle("Введите уставку расцепителей к.з.:");
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
                                    tok_workText.setText(getTokWork(input2.getText().toString(), true));
                                }
                                break;
                            case R.id.more:
                                if (!input3.getText().toString().equals("")) {
                                    set_kz = ">" + input3.getText().toString();
                                    tok_workText.setText(getTokWork(input3.getText().toString(), false));
                                }
                                break;
                        }
                        set_kzText.setText(set_kz);
                    }
                });
                alert1.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        closeKeyboard(myView);
                    }
                });
                alert1.setView(myView);
                alert1.show();
                clearFocus();
            }
        });

        //ПРОВЕРКА РАСЦЕПИТЕЛЕЙ ПЕРЕГРУЗКИ, ВРЕМЯ СРАБАТЫВАНИЯ ИЗМЕРЕННОЕ
        time_measuredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert1 = new AlertDialog.Builder(AutomaticsActivity5.this);
                final View myView = getLayoutInflater().inflate(R.layout.dialog_for_veins,null);
                alert1.setCancelable(false);
                alert1.setTitle("Введите время срабатывания измеренное(расцеп. перегр.):");
                final EditText input = myView.findViewById(R.id.editText2);
                openKeyboard();
                alert1.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        closeKeyboard(myView);
                        String time_measured = input.getText().toString();
                        time_measuredText.setText(time_measured);
                    }
                });
                alert1.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        closeKeyboard(myView);
                    }
                });
                alert1.setView(myView);
                alert1.show();
                clearFocus();
            }
        });

        //ПРОВЕРКА РАСЦЕПИТЕЛЕЙ КОРОТКОГО ЗАМЫКАНИЯ, ДЛИТЕЛЬНОСТЬ ПРИЛОЖЕНИЯ ИСПЫТАТЕЛЬНОГО ТОКА
        length_annexButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert1 = new AlertDialog.Builder(AutomaticsActivity5.this);
                final View myView = getLayoutInflater().inflate(R.layout.dialog_for_section,null);
                alert1.setCancelable(false);
                alert1.setTitle("Введите длительность приложения испытательного тока(к.з.):");
                final EditText input = myView.findViewById(R.id.editText2);
                openKeyboard();
                alert1.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        closeKeyboard(myView);
                        String length_annex = input.getText().toString();
                        length_annexText.setText(length_annex);
                    }
                });
                alert1.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        closeKeyboard(myView);
                    }
                });
                alert1.setView(myView);
                alert1.show();
                clearFocus();
            }
        });

        //ПРОВЕРКА РАСЦЕПИТЕЛЕЙ КОРОТКОГО ЗАМЫКАНИЯ, ТОК СРАБАТЫВАНИЯ РАСЦЕПИТЕЛЯ
        tok_workButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert1 = new AlertDialog.Builder(AutomaticsActivity5.this);
                final View myView = getLayoutInflater().inflate(R.layout.dialog_for_veins,null);
                alert1.setCancelable(false);
                alert1.setTitle("Введите ток срабатывания расцеп. (к.з.):");
                final EditText input = myView.findViewById(R.id.editText2);
                openKeyboard();
                alert1.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        closeKeyboard(myView);
                        String tok_work = input.getText().toString();
                        tok_workText.setText(tok_work);
                    }
                });
                alert1.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        closeKeyboard(myView);
                    }
                });
                alert1.setView(myView);
                alert1.show();
                clearFocus();
            }
        });

        //ПРОВЕРКА РАСЦЕПИТЕЛЕЙ КОРОТКОГО ЗАМЫКАНИЯ, ВРЕМЯ СРАБАТЫВАНИЯ РАСЦЕПИТЕЛЯ
        time_workButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert1 = new AlertDialog.Builder(AutomaticsActivity5.this);
                final View myView = getLayoutInflater().inflate(R.layout.dialog_for_section,null);
                alert1.setCancelable(false);
                alert1.setTitle("Введите время срабатывания расцепителя к.з.:");
                final EditText input = myView.findViewById(R.id.editText2);
                openKeyboard();
                alert1.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        closeKeyboard(myView);
                        String set_overload = input.getText().toString();
                        time_workText.setText(set_overload);
                    }
                });
                alert1.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        closeKeyboard(myView);
                    }
                });
                alert1.setView(myView);
                alert1.show();
                clearFocus();
            }
        });

        //СОХРАНИТЬ
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String place;
                if (placeSwitch.isChecked())
                    place = "Вводной";
                else
                    place = "";
                String name = nameText.getText().toString();
                String type_overload = type_overloadText.getText().toString();
                String type_kz = type_kzText.getText().toString();
                String symbol = symbolText.getText().toString();
                String excerpt = excerptText.getText().toString();
                String nom1 = nominal_1Edit.getText().toString();
                String nom2 = nominal_2Edit.getText().toString();
                String set_overload = set_overloadText.getText().toString();
                String set_kz = set_kzText.getText().toString();
                String time_measured = time_measuredText.getText().toString();
                String length_annex = length_annexText.getText().toString();
                String tok_work = tok_workText.getText().toString();
                String time_work = time_workText.getText().toString();
                if (isIncorrectInput(name, nameLayout) | isIncorrectInput(type_overload, typeOverloadLayout) |
                        isIncorrectInput(type_kz, typeKzLayout) | isIncorrectInput(symbol, symbolLayout) |
                        isIncorrectInput(excerpt, excerptLayout) | isIncorrectInput(nom1, firstNominalLayout) |
                        isIncorrectInput(nom2, secondNominalLayout) | isIncorrectInput(set_overload, setOverloadLayout) |
                        isIncorrectInput(set_kz, setKzLayout) | isIncorrectInput(time_measured, timeMeasuredLayout) |
                        isIncorrectInput(length_annex, lengthAnnexLayout) | isIncorrectInput(tok_work, tokWorkLayout) |
                        isIncorrectInput(time_work, timeWorkLayout)) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(AutomaticsActivity5.this);
                    alert.setCancelable(false);
                    alert.setMessage("Заполните все поля!");
                    alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    });
                    alert.show();
                } else {
                    name += " " + numb_poles;
                    String test_tok = getTestTok(set_overload);
                    String time_permissible = getTimePermissible(nom2);
                    String conclusion = getConclusion(time_permissible, time_measured, set_kz, tok_work);
                    //УДАЛИМ НАШ АППАРАТ, ЕСЛИ МЫ ВНОСИМ ИЗМЕНЕНИЯ, А НЕ СОЗДАЕМ НОВЫЙ
                    if (change)
                        database.delete(DBHelper.TABLE_AUTOMATICS, "_id = ?", new String[]{String.valueOf(idAutomat)});
                    //СОЗДАЕМ НОВУЮ ЗАПИСЬ
                    ContentValues contentValues = new ContentValues();
                    if (change)
                        contentValues.put(DBHelper.AU_ID, idAutomat);
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
                    contentValues.put(DBHelper.AU_TIME_WORK, time_work);
                    contentValues.put(DBHelper.AU_CONCLUSION, conclusion);
                    database.insert(DBHelper.TABLE_AUTOMATICS, null, contentValues);
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Данные сохранены", Toast.LENGTH_SHORT);
                    toast.show();
                    //ВОЗВРАЩАЕМСЯ НАЗАД
                    Intent intent = new Intent("android.intent.action.Automatics4");
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
                Intent intent = new Intent("android.intent.action.Automatics4");
                intent.putExtra("nameFloor", nameFloor);
                intent.putExtra("idFloor", idFloor);
                intent.putExtra("nameRoom", nameRoom);
                intent.putExtra("idRoom", idRoom);
                intent.putExtra("nameLine", nameLine);
                intent.putExtra("idLine", idLine);
                startActivity(intent);
                return true;
            case R.id.action_main:
                Intent intent1 = new Intent(AutomaticsActivity5.this, MenuItemsActivity.class);
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

    public String getOverloadSet(CharSequence s) {
        BigDecimal overDecimal = new BigDecimal(s.toString());
        BigDecimal k = new BigDecimal("2.55");
        BigDecimal result = overDecimal.multiply(k).setScale(1, ROUND_HALF_UP);
        String overString = result.toString();
        if (overString.contains("0") && overString.indexOf('0', overString.indexOf('.')) == overString.length() - 1)
            return overString.substring(0, overString.indexOf('.'));
        return overString.replace('.',',');
    }

    public String getTimeMeasured(CharSequence s) {
        int num = Integer.parseInt(s.toString());
        Random generator = new Random();
        if (num <= 32)
            return String.valueOf(generator.nextInt(21) + 30);
        return String.valueOf(generator.nextInt(61) + 30);
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

    public String getTimePermissible(String numb) {
        int num = Integer.parseInt(numb);
        if (num <= 32)
            return "60";
        return "120";
    }

    public String getTokWork(String numb, boolean isSubtraction) {
        int x = Integer.parseInt(numb);
        Random generator = new Random();
        if (!isSubtraction)
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

    void clearFocus() {
        nominal_1Edit.clearFocus();
        nominal_2Edit.clearFocus();
    }
}