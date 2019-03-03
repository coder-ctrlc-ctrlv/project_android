package com.example.protokol.create_protokol;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.camera2.DngCreator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GroundingDevicesActivity2 extends AppCompatActivity {

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grounding_devices2);

        dbHelper = new DBHelper(this);
        final SQLiteDatabase database = dbHelper.getWritableDatabase();

        Button noteBTN = findViewById(R.id.button);
        final TextView noteTEXT = findViewById(R.id.textView43);
        Button placeBTN = findViewById(R.id.button27);
        final TextView placeTEXT = findViewById(R.id.textView23);
        Button purposeBTN = findViewById(R.id.button28);
        final TextView purposeTEXT = findViewById(R.id.textView25);
        final EditText distanceActiveTEXT = findViewById(R.id.editText6);
        Button rBTN = findViewById(R.id.button30);
        final TextView rTEXT = findViewById(R.id.textView30);
        Button distanceBTN = findViewById(R.id.button31);
        final TextView distanceTEXT = findViewById(R.id.textView32);
        Button save = findViewById(R.id.button35);

        final int device_id = getIntent().getIntExtra("device_id", -1);

        //НАСТРАИВАЕМ ACTIONBAR
        getSupportActionBar().setTitle("Заземл. и заз. устр-ва");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ОПРЕДЕЛЯЕМ, СОЗДАЕМ ЛИ МЫ НОВУЮ ГРУППУ ИЛИ ИЗМЕНЯЕМ СТАРУЮ
        final boolean change;
        if (device_id != -1) {
            change = true;
            getSupportActionBar().setSubtitle("Редактирование заземлителя");
        } else {
            change = false;
            getSupportActionBar().setSubtitle("Добавление заземлителя");
        }

        //ЗАПОЛНЕНИЕ ДАННЫХ, ЕСЛИ ОНИ ПЕРЕДАНЫ
        if(change) {
            //ПОЛУЧЕНИЕ ИНФОРМАЦИИ ОБ ЭЛЕМЕНТЕ
            Cursor cursor1 = database.query(DBHelper.TABLE_MAIN_DEVICES, new String[] {DBHelper.MD_DEVICE_ID, DBHelper.MD_PURPOSE,
                    DBHelper.MD_PLACE, DBHelper.MD_DISTANCE, DBHelper.MD_R1, DBHelper.MD_05L, DBHelper.MD_NOTE}, "_id = ?", new String[] {String.valueOf(device_id)}, null, null, null);
            if (cursor1.moveToFirst()) {
                int purposeIndex = cursor1.getColumnIndex(DBHelper.MD_PURPOSE);
                int placeIndex = cursor1.getColumnIndex(DBHelper.MD_PLACE);
                int distanceIndex = cursor1.getColumnIndex(DBHelper.MD_DISTANCE);
                int r1Index = cursor1.getColumnIndex(DBHelper.MD_R1);
                int l5Index = cursor1.getColumnIndex(DBHelper.MD_05L);
                int noteIndex = cursor1.getColumnIndex(DBHelper.MD_NOTE);
                do {
                    purposeTEXT.setText(cursor1.getString(purposeIndex));
                    placeTEXT.setText(cursor1.getString(placeIndex));
                    distanceTEXT.setText(cursor1.getString(distanceIndex));
                    rTEXT.setText(cursor1.getString(r1Index));
                    distanceActiveTEXT.setText(cursor1.getString(l5Index));
                    noteTEXT.setText(cursor1.getString(noteIndex));
                } while (cursor1.moveToNext());
            }
            cursor1.close();
        }


        //ПРИМЕЧАНИЕ
        noteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(GroundingDevicesActivity2.this);
                alert.setCancelable(false);
                alert.setTitle("Измерения производились c:");
                final String note_array[] = {"подключенным PEN-проводником", "отключенным PEN-проводником"};
                alert.setItems(note_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        noteTEXT.setText("Измерения производились c " + note_array[which]);
                    }
                });
                alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                alert.show();
            }
        });

        //МЕСТО ИЗМЕРЕНИЯ
        placeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert1 = new AlertDialog.Builder(GroundingDevicesActivity2.this);
                final View myView = getLayoutInflater().inflate(R.layout.dialog_for_names,null);
                alert1.setCancelable(false);
                alert1.setTitle("Введите место измерения:");
                final EditText input = myView.findViewById(R.id.editText);
                //ОТКРЫВАЕМ КЛАВИАТУРУ
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                alert1.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //СКРЫВАЕМ КЛАВИАТУРУ
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(myView.getWindowToken(),0);
                        String place = input.getText().toString();
                        placeTEXT.setText(place);
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
        });

        //НАЗНАЧЕНИЕ ЗАЗЕМЛИТЕЛЯ
        purposeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(GroundingDevicesActivity2.this);
                alert.setCancelable(false);
                alert.setTitle("Выберете назначение заземлителя:");
                final String purpose_array[] = {"Защитное", "Технологическое"};
                alert.setItems(purpose_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        purposeTEXT.setText(purpose_array[which]);
                    }
                });
                alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                alert.show();
            }
        });

        //ДОПУСТИМОЕ СОПРОТИВЛЕНИЕ
        rBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(GroundingDevicesActivity2.this);
                alert.setCancelable(false);
                alert.setTitle("Выберете допустимое сопротивление:");
                final String r_array[] = {"4,0", "10,0", "30,0"};
                alert.setItems(r_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        rTEXT.setText(r_array[which]);
                    }
                });
                alert.setPositiveButton("Ввести", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        AlertDialog.Builder alert1 = new AlertDialog.Builder(GroundingDevicesActivity2.this);
                        final View myView = getLayoutInflater().inflate(R.layout.dialog_for_section,null);
                        alert1.setCancelable(false);
                        alert1.setTitle("Введите допустимое сопротивление:");
                        final EditText input = myView.findViewById(R.id.editText2);
                        //ОТКРЫВАЕМ КЛАВИАТУРУ
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                        alert1.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //СКРЫВАЕМ КЛАВИАТУРУ
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(myView.getWindowToken(),0);
                                String r = input.getText().toString();
                                rTEXT.setText(r);
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
                });
                alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                alert.show();
            }
        });

        //РАССТОЯНИЕ ДО ТОКОВОГО ЭЛЕКТРОДА
        distanceBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert1 = new AlertDialog.Builder(GroundingDevicesActivity2.this);
                final View myView = getLayoutInflater().inflate(R.layout.dialog_for_section,null);
                alert1.setCancelable(false);
                alert1.setTitle("Введите расстояние токового электрода:");
                final EditText input = myView.findViewById(R.id.editText2);
                //ОТКРЫВАЕМ КЛАВИАТУРУ
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                alert1.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //СКРЫВАЕМ КЛАВИАТУРУ
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(myView.getWindowToken(),0);
                        String distance = input.getText().toString();
                        distanceTEXT.setText(distance);
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
        });

        //СОХРАНИТЬ ДАННЫЕ
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noteTEXT.getText().toString().equals("Не выбрано") || placeTEXT.getText().toString().equals("Не выбрано") || purposeTEXT.getText().toString().equals("Не выбрано") ||
                        rTEXT.getText().toString().equals("Не выбрано") || distanceActiveTEXT.getText().toString().equals("")) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(GroundingDevicesActivity2.this);
                    alert.setCancelable(false);
                    alert.setMessage("Заполните все поля!");
                    alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    });
                    alert.show();
                }
                else {
                    //УДАЛИМ НАШ ЗАЗЕМЛИТЕЛЬ, ЕСЛИ МЫ ВНОСИМ ИЗМЕНЕНИЯ, А НЕ СОЗДАЕМ НОВЫЙ
                    if (change)
                        database.delete(DBHelper.TABLE_MAIN_DEVICES, "_id = ?", new String[]{String.valueOf(device_id)});
                    //СОЗДАЕМ НОВУЮ ЗАПИСЬ
                    ContentValues contentValues = new ContentValues();
                    if (change)
                        contentValues.put(DBHelper.MD_DEVICE_ID, device_id);
                    contentValues.put(DBHelper.MD_PURPOSE, purposeTEXT.getText().toString());
                    contentValues.put(DBHelper.MD_PLACE, placeTEXT.getText().toString());
                    contentValues.put(DBHelper.MD_DISTANCE, distanceTEXT.getText().toString());
                    contentValues.put(DBHelper.MD_R1, rTEXT.getText().toString());
                    contentValues.put(DBHelper.MD_01L, "-");
                    contentValues.put(DBHelper.MD_02L, "число+-");
                    contentValues.put(DBHelper.MD_03L, "число+-");
                    contentValues.put(DBHelper.MD_04L, "число+-");
                    contentValues.put(DBHelper.MD_05L, distanceActiveTEXT.getText().toString());
                    contentValues.put(DBHelper.MD_06L, "число+-");
                    contentValues.put(DBHelper.MD_07L, "число+-");
                    contentValues.put(DBHelper.MD_08L, "число+-");
                    contentValues.put(DBHelper.MD_09L, "-");
                    contentValues.put(DBHelper.MD_GRAPHICS, "-");
                    contentValues.put(DBHelper.MD_R2, distanceActiveTEXT.getText().toString());
                    contentValues.put(DBHelper.MD_K, "-");
                    contentValues.put(DBHelper.MD_R3, "-");
                    contentValues.put(DBHelper.MD_CONCLUSION, "соответствует");
                    contentValues.put(DBHelper.MD_NOTE, noteTEXT.getText().toString());
                    database.insert(DBHelper.TABLE_MAIN_DEVICES, null, contentValues);
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Изменения сохранены", Toast.LENGTH_SHORT);
                    toast.show();
                    //ВОЗВРАЩАЕМСЯ НАЗАД
                    Intent intent = new Intent("android.intent.action.GroundingDevices");
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
                Intent intent = new Intent("android.intent.action.GroundingDevices");
                startActivity(intent);
                return true;
            case R.id.action_main:
                Intent intent1 = new Intent(GroundingDevicesActivity2.this, MainActivity.class);
                startActivity(intent1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
