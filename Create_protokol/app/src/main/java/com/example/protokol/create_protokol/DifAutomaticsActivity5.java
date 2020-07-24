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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class DifAutomaticsActivity5 extends AppCompatActivity {

    DBHelper dbHelper;
    String nameFloor, nameRoom, nameLine;
    int idFloor,idRoom, idLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dif_automatics5);

        dbHelper = new DBHelper(this);
        final SQLiteDatabase database = dbHelper.getWritableDatabase();

        final LinearLayout placeLayout = findViewById(R.id.placeLayout);
        final LinearLayout uzoLayout = findViewById(R.id.uzoLayout);
        final LinearLayout typeSwitchLayout = findViewById(R.id.typeSwitchLayout);
        final LinearLayout uLayout = findViewById(R.id.uLayout);
        final LinearLayout iNomLayout = findViewById(R.id.iNomLayout);
        final LinearLayout iMeasuredLayout = findViewById(R.id.iMeasuredLayout);
        final LinearLayout timeMeasuredLayout = findViewById(R.id.timeMeasuredLayout);
        final LinearLayout setThermalLayout = findViewById(R.id.setThermalLayout);
        final LinearLayout checkTimeLayout = findViewById(R.id.checkTimeLayout);
        final LinearLayout checkWorkTokLayout = findViewById(R.id.lin);
        final LinearLayout checkTestTokLayout = findViewById(R.id.checkTestTokLayout);
        final LinearLayout iLeackLayout = findViewById(R.id.iLeackLayout);

        final LinearLayout extra_paramLayout = findViewById(R.id.extraParam);
        final Button extra_paramButton = findViewById(R.id.button100);
        final TextView placeText = findViewById(R.id.textView6);
        Button placeButton = findViewById(R.id.button40);
        final TextView uzoText = findViewById(R.id.textView20);
        Button uzoButton = findViewById(R.id.button26);
        final TextView type_switchText = findViewById(R.id.textView22);
        Button type_switchButton = findViewById(R.id.button29);
        final TextView uText = findViewById(R.id.textView30);
        Button uButton = findViewById(R.id.button30);
        final TextView set_thermalText = findViewById(R.id.textView32);
        Button set_thermalButton = findViewById(R.id.button31);
        final TextView check_test_tokText = findViewById(R.id.textView25);
        Button check_test_tokButton = findViewById(R.id.button28);
        final TextView check_timeText = findViewById(R.id.textView23);
        Button check_timeButton = findViewById(R.id.button27);
        final TextView check_work_tokText = findViewById(R.id.textView39);
        final Button check_work_tokButton = findViewById(R.id.button59);
        final TextView i_nomText = findViewById(R.id.textView35);
        Button i_nomButton = findViewById(R.id.button60);
        final TextView i_leackText = findViewById(R.id.textView37);
        Button i_leackButton = findViewById(R.id.button61);
        final TextView i_measuredText = findViewById(R.id.textView40);
        Button i_measuredButton = findViewById(R.id.button62);
        final TextView time_measuredText = findViewById(R.id.textView42);
        Button time_measuredButton = findViewById(R.id.button63);
        Button saveButton = findViewById(R.id.button35);

        nameFloor = getIntent().getStringExtra("nameFloor");
        idFloor = getIntent().getIntExtra("idFloor", 0);
        nameRoom = getIntent().getStringExtra("nameRoom");
        idRoom = getIntent().getIntExtra("idRoom", 0);
        nameLine = getIntent().getStringExtra("nameLine");
        idLine = getIntent().getIntExtra("idLine", 0);
        final int idAutomat = getIntent().getIntExtra("idAutomat", -1);

        //НАСТРАИВАЕМ ACTIONBAR
        getSupportActionBar().setTitle("Диф. автоматы");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ОПРЕДЕЛЯЕМ, СОЗДАЕМ ЛИ МЫ НОВУЮ ГРУППУ ИЛИ ИЗМЕНЯЕМ СТАРУЮ
        final boolean change;
        if (idAutomat != -1) {
            change = true;
            getSupportActionBar().setSubtitle("Редактирование выключателя");
        }
        else {
            change = false;
            getSupportActionBar().setSubtitle("Добавление выключателя");
        }

        //ЗАПОЛНЕНИЕ ДАННЫХ, ЕСЛИ ОНИ ПЕРЕДАНЫ
        if (change) {
            //ЗАПРОС В БД(ПОЛУЧЕНИЕ ИНФОРМАЦИИ О ГРУППЕ)
            Cursor cursor = database.query(DBHelper.TABLE_DIF_AUTOMATICS, new String[]{DBHelper.DIF_AU_ID, DBHelper.DIF_AU_PLACE,
                    DBHelper.DIF_AU_UZO, DBHelper.DIF_AU_TYPE_SWITCH, DBHelper.DIF_AU_U,
                    DBHelper.DIF_AU_SET_THERMAL, DBHelper.DIF_AU_CHECK_TEST_TOK, DBHelper.DIF_AU_CHECK_TIME_, DBHelper.DIF_AU_CHECK_WORK_TOK, DBHelper.DIF_AU_I_NOM,
                    DBHelper.DIF_AU_I_LEAK, DBHelper.DIF_AU_I_MEASURED, DBHelper.DIF_AU_TIME_MEASURED}, "_id = ?", new String[] {String.valueOf(idAutomat)}, null, null, null);
            if (cursor.moveToFirst()) {
                int placeIndex = cursor.getColumnIndex(DBHelper.DIF_AU_PLACE);
                int uzoIndex = cursor.getColumnIndex(DBHelper.DIF_AU_UZO);
                int type_switchIndex = cursor.getColumnIndex(DBHelper.DIF_AU_TYPE_SWITCH);
                int uIndex = cursor.getColumnIndex(DBHelper.DIF_AU_U);
                int set_thermalIndex = cursor.getColumnIndex(DBHelper.DIF_AU_SET_THERMAL);
                int check_test_tokIndex = cursor.getColumnIndex(DBHelper.DIF_AU_CHECK_TEST_TOK);
                int check_timeIndex = cursor.getColumnIndex(DBHelper.DIF_AU_CHECK_TIME_);
                int check_work_tokIndex = cursor.getColumnIndex(DBHelper.DIF_AU_CHECK_WORK_TOK);
                int i_nomIndex = cursor.getColumnIndex(DBHelper.DIF_AU_I_NOM);
                int i_leackIndex = cursor.getColumnIndex(DBHelper.DIF_AU_I_LEAK);
                int i_measuredIndex = cursor.getColumnIndex(DBHelper.DIF_AU_I_MEASURED);
                int time_measuredIndex = cursor.getColumnIndex(DBHelper.DIF_AU_TIME_MEASURED);
                do {
                    placeText.setText(cursor.getString(placeIndex));
                    uzoText.setText(cursor.getString(uzoIndex));
                    type_switchText.setText(cursor.getString(type_switchIndex));
                    uText.setText(cursor.getString(uIndex));
                    set_thermalText.setText(cursor.getString(set_thermalIndex));
                    check_test_tokText.setText(cursor.getString(check_test_tokIndex));
                    check_timeText.setText(cursor.getString(check_timeIndex));
                    check_work_tokText.setText(cursor.getString(check_work_tokIndex));
                    i_nomText.setText(cursor.getString(i_nomIndex));
                    i_leackText.setText(cursor.getString(i_leackIndex));
                    i_measuredText.setText(cursor.getString(i_measuredIndex));
                    time_measuredText.setText(cursor.getString(time_measuredIndex));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        //ПОКАЗАТЬ/СКРЫТЬ ДОП. ДАННЫЕ
        extra_paramButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(extra_paramLayout.getVisibility() == View.GONE) {
                    extra_paramLayout.setVisibility(View.VISIBLE);
                    extra_paramButton.setText("Скрыть дополнительные данные");
                }
                else {
                    extra_paramLayout.setVisibility(View.GONE);
                    extra_paramButton.setText("Показать дополнительные данные");
                }
            }
        });

        //МЕСТО УСТАНОВКИ
        placeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert1 = new AlertDialog.Builder(DifAutomaticsActivity5.this);
                final View myView = getLayoutInflater().inflate(R.layout.dialog_for_names,null);
                alert1.setCancelable(false);
                alert1.setTitle("Введите обозначение по схеме:");
                final EditText input = myView.findViewById(R.id.editText);
                openKeyboard();
                alert1.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        closeKeyboard(myView);
                        String pl = input.getText().toString();
                        placeText.setText(pl);
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

        //ТИП УЗО
        uzoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(DifAutomaticsActivity5.this);
                alert.setCancelable(false);
                alert.setTitle("Выберите тип УЗО:");
                final String uzo_array[] = {"А", "АС"};
                alert.setItems(uzo_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        uzoText.setText(uzo_array[which]);
                    }
                });
                alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                alert.show();
            }
        });


        //ТИП АВТОМАТ. ВЫКЛЮЧАТЕЛЯ
        type_switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTypeSwitch(type_switchText);
            }
        });

        //Uф
        uButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(DifAutomaticsActivity5.this);
                alert.setCancelable(false);
                alert.setTitle("Выберите напряжение:");
                final String u_array[] = {"220", "380"};
                alert.setItems(u_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        uText.setText(u_array[which]);
                    }
                });
                alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                alert.show();
            }
        });

        //ТЕПЛ. РАСЦЕП.
        set_thermalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert1 = new AlertDialog.Builder(DifAutomaticsActivity5.this);
                final View myView = getLayoutInflater().inflate(R.layout.dialog_for_veins,null);
                alert1.setCancelable(false);
                alert1.setTitle("Введите уставку тепл. расцепит. (А):");
                final EditText input = myView.findViewById(R.id.editText2);
                openKeyboard();
                alert1.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        closeKeyboard(myView);
                        String set_thermal = input.getText().toString();
                        set_thermalText.setText(set_thermal);
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

        //ИСПЫТ ТОК
        check_test_tokButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert1 = new AlertDialog.Builder(DifAutomaticsActivity5.this);
                final View myView = getLayoutInflater().inflate(R.layout.dialog_for_veins,null);
                alert1.setCancelable(false);
                alert1.setTitle("Введите испыт. ток (А):");
                final EditText input = myView.findViewById(R.id.editText2);
                openKeyboard();
                alert1.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        closeKeyboard(myView);
                        String check_test_tok = input.getText().toString();
                        check_test_tokText.setText(check_test_tok);
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

        //ВРЕМЯ СР ТЕП РАСЦ
        check_timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert1 = new AlertDialog.Builder(DifAutomaticsActivity5.this);
                final View myView = getLayoutInflater().inflate(R.layout.dialog_for_veins,null);
                alert1.setCancelable(false);
                alert1.setTitle("Введите время ср. теп. расц. (сек):");
                final EditText input = myView.findViewById(R.id.editText2);
                openKeyboard();
                alert1.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        closeKeyboard(myView);
                        String check_time = input.getText().toString();
                        check_timeText.setText(check_time);
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

        //ТОК СРАБ ЭЛ МАГ РАСЦЕП
        check_work_tokButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert1 = new AlertDialog.Builder(DifAutomaticsActivity5.this);
                final View myView = getLayoutInflater().inflate(R.layout.dialog_for_veins,null);
                alert1.setCancelable(false);
                alert1.setTitle("Введите ток сраб. эл. маг. расцеп. (А):");
                final EditText input = myView.findViewById(R.id.editText2);
                openKeyboard();
                alert1.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        closeKeyboard(myView);
                        String check_work_tok = input.getText().toString();
                        check_work_tokText.setText(check_work_tok);
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

        //ТОК НОМ
        i_nomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(DifAutomaticsActivity5.this);
                alert.setCancelable(false);
                alert.setTitle("Выберите ток ном. (мА):");
                final String i_nom_array[] = {"30", "100", "300"};
                alert.setItems(i_nom_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        i_nomText.setText(i_nom_array[which]);
                    }
                });
                alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                alert.show();
            }
        });

        //ТОК УТЕЧ
        i_leackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert1 = new AlertDialog.Builder(DifAutomaticsActivity5.this);
                final View myView = getLayoutInflater().inflate(R.layout.dialog_for_section,null);
                alert1.setCancelable(false);
                alert1.setTitle("Введите ток утеч. (мА):");
                final EditText input = myView.findViewById(R.id.editText2);
                openKeyboard();
                alert1.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        closeKeyboard(myView);
                        String i_leack = input.getText().toString();
                        i_leackText.setText(i_leack);
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

        //ТОК СРАБ ЗАЩ ИЗМЕР
        i_measuredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert1 = new AlertDialog.Builder(DifAutomaticsActivity5.this);
                final View myView = getLayoutInflater().inflate(R.layout.dialog_for_section,null);
                alert1.setCancelable(false);
                alert1.setTitle("Введите ток сраб. защ. измер. (мА):");
                final EditText input = myView.findViewById(R.id.editText2);
                openKeyboard();
                alert1.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        closeKeyboard(myView);
                        String i_measured = input.getText().toString();
                        i_measuredText.setText(i_measured);
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

        //ВРЕМЯ СРАБ ИЗМЕР
        time_measuredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert1 = new AlertDialog.Builder(DifAutomaticsActivity5.this);
                final View myView = getLayoutInflater().inflate(R.layout.dialog_for_section,null);
                alert1.setCancelable(false);
                alert1.setTitle("Введите время сраб. измер. (с):");
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
            }
        });

        //СОХРАНИТЬ
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String place = placeText.getText().toString();
                String uzo = uzoText.getText().toString();
                String type_switch = type_switchText.getText().toString();
                String u = uText.getText().toString();
                String set_thermal = set_thermalText.getText().toString();
                String check_test_tok = check_test_tokText.getText().toString();
                String check_time = check_timeText.getText().toString();
                String check_work_tok = check_work_tokText.getText().toString();
                String i_nom = i_nomText.getText().toString();
                String i_leack = i_leackText.getText().toString();
                String i_measured = i_measuredText.getText().toString();
                String time_measured = time_measuredText.getText().toString();
                if (isIncorrectInput(place, placeLayout) | isIncorrectInput(uzo, uzoLayout) |
                        isIncorrectInput(type_switch, typeSwitchLayout) | isIncorrectInput(u, uLayout) |
                        isIncorrectInput(set_thermal, setThermalLayout, extra_paramLayout) |
                        isIncorrectInput(check_test_tok, checkTestTokLayout, extra_paramLayout) |
                        isIncorrectInput(check_time, checkTimeLayout, extra_paramLayout) |
                        isIncorrectInput(check_work_tok, checkWorkTokLayout, extra_paramLayout) |
                        isIncorrectInput(i_nom, iNomLayout) | isIncorrectInput(i_leack, iLeackLayout, extra_paramLayout) |
                        isIncorrectInput(i_measured, iMeasuredLayout) | isIncorrectInput(time_measured, timeMeasuredLayout)) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(DifAutomaticsActivity5.this);
                    alert.setCancelable(false);
                    alert.setMessage("Заполните все поля!");
                    alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    });
                    alert.show();
                } else {
                    String set_electromagn = getSetElectromagn(type_switch);
                    String i_extra = getI_Extra(i_nom);
                    String time_extra = getTimeExtra(u);
                    String conclusion = getConclusion(i_extra, i_measured, time_extra, time_measured);
                    //УДАЛИМ НАШ АППАРАТ, ЕСЛИ МЫ ВНОСИМ ИЗМЕНЕНИЯ, А НЕ СОЗДАЕМ НОВЫЙ
                    if (change)
                        database.delete(DBHelper.TABLE_DIF_AUTOMATICS, "_id = ?", new String[]{String.valueOf(idAutomat)});
                    //СОЗДАЕМ НОВУЮ ЗАПИСЬ
                    ContentValues contentValues = new ContentValues();
                    if (change)
                        contentValues.put(DBHelper.DIF_AU_ID, idAutomat);
                    contentValues.put(DBHelper.DIF_AU_ID_ALINE, idLine);
                    contentValues.put(DBHelper.DIF_AU_PLACE, place);
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
                    contentValues.put(DBHelper.DIF_AU_I_MEASURED, i_measured);
                    contentValues.put(DBHelper.DIF_AU_TIME_EXTRA, time_extra);
                    contentValues.put(DBHelper.DIF_AU_TIME_MEASURED, time_measured);
                    contentValues.put(DBHelper.DIF_AU_CONCLUSION, conclusion);
                    database.insert(DBHelper.TABLE_DIF_AUTOMATICS, null, contentValues);
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Данные сохранены", Toast.LENGTH_SHORT);
                    toast.show();
                    //ВОЗВРАЩАЕМСЯ НАЗАД
                    Intent intent = new Intent("android.intent.action.DifAutomatics4");
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
                Intent intent = new Intent("android.intent.action.DifAutomatics4");
                intent.putExtra("nameFloor", nameFloor);
                intent.putExtra("idFloor", idFloor);
                intent.putExtra("nameRoom", nameRoom);
                intent.putExtra("idRoom", idRoom);
                intent.putExtra("nameLine", nameLine);
                intent.putExtra("idLine", idLine);
                startActivity(intent);
                return true;
            case R.id.action_main:
                Intent intent1 = new Intent(DifAutomaticsActivity5.this, MainActivity.class);
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

    boolean isIncorrectInput(String data, LinearLayout layout, LinearLayout extraParam) {
        if (data.equals("") || data.equals("Нет")) {
            if (extraParam.getVisibility() == View.GONE) {
                extraParam.setVisibility(View.VISIBLE);
            }
            layout.setBackgroundResource(R.drawable.incorrect_input);
            return true;
        }
        layout.setBackgroundResource(R.drawable.listview);
        return false;
    }

    void getTypeSwitch(final TextView TS_text) {
        AlertDialog.Builder alert = new AlertDialog.Builder(DifAutomaticsActivity5.this);
        final View myView = getLayoutInflater().inflate(R.layout.dialog_for_type_automat,null);
        alert.setCancelable(false);
        alert.setTitle("Выберете характеристику и введите номинал автомата:");
        openKeyboard();
        final RadioButton b_btn = myView.findViewById(R.id.B);
        final RadioButton c_btn = myView.findViewById(R.id.C);
        final EditText input = myView.findViewById(R.id.editText2);
        alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                closeKeyboard(myView);
                String numb = input.getText().toString();
                if (numb.equals("")) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(DifAutomaticsActivity5.this);
                    alert.setCancelable(false);
                    alert.setMessage("Заполните поле значения!");
                    alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            getTypeSwitch(TS_text);
                        }
                    });
                    alert.show();
                }
                else {
                    String charact;
                    if (b_btn.isChecked())
                        charact = "B";
                    else if (c_btn.isChecked())
                        charact = "C";
                    else
                        charact = "D";
                    TS_text.setText(charact + " " + numb);
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

    String getSetElectromagn(String type_switch) {
        String charact = type_switch.substring(0, 1);
        Integer x = 0, y = 0; //x < y
        switch (charact) {
            case "B":
                x = 3; y = 5;
                break;
            case "C":
                x = 5; y = 10;
                break;
            case "D":
                x = 10; y = 20;
                break;
        }
        Integer numb = Integer.parseInt(type_switch.substring(type_switch.indexOf(" ") + 1));
        return String.valueOf(numb * x) + "-" + String.valueOf(numb * y);
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

    String getConclusion(String x1, String x2, String y1, String y2) {
        boolean flag;
        String concl;
        Double i = Double.parseDouble(x2.replace(",","."));
        Double time = Double.parseDouble(y2.replace(",","."));
        switch (x1) {
            case "15" + '\u003C' + "I" + '\u2264' + "30":
                flag = i > 15 && i <= 30;
                break;
            case "50" + '\u003C' + "I" + '\u2264' + "100":
                flag = i > 50 && i <= 100;
                break;
            default:
                flag = i > 150 && i <= 300;
                break;
        }
        if (flag)
            if (y1.equals("0,4"))
                flag = time <= 0.4;
            else
                flag = time <= 0.2;
        concl = flag ? "соответсвует" : "не соответств.";
        return concl;
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
