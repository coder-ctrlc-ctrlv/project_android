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

import java.util.Random;

public class GroundingDevicesActivity2 extends AppCompatActivity {

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grounding_devices2);

        dbHelper = new DBHelper(this);
        final SQLiteDatabase database = dbHelper.getWritableDatabase();

        Button uBTN = findViewById(R.id.button25);
        final TextView uTEXT = findViewById(R.id.textView9);

        Button characterBTN = findViewById(R.id.button26);
        final TextView characterTEXT = findViewById(R.id.textView20);

        Button groundBTN = findViewById(R.id.button29);
        final TextView groundTEXT = findViewById(R.id.textView22);

        Button rBTN = findViewById(R.id.button30);
        final TextView rTEXT = findViewById(R.id.textView30);

        Button distanceBTN = findViewById(R.id.button31);
        final TextView distanceTEXT = findViewById(R.id.textView32);

        Button noteBTN = findViewById(R.id.button);
        final TextView noteTEXT = findViewById(R.id.textView43);

        Button placeBTN = findViewById(R.id.button27);
        final TextView placeTEXT = findViewById(R.id.textView23);

        Button purposeBTN = findViewById(R.id.button28);
        final TextView purposeTEXT = findViewById(R.id.textView25);

        final EditText rEditTEXT = findViewById(R.id.editText5);
        final EditText distanceActiveTEXT = findViewById(R.id.editText6);

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
            Cursor cursor1 = database.query(DBHelper.TABLE_GD, new String[] {DBHelper.GD_DEVICE_ID, DBHelper.GD_GROUND,
                    DBHelper.GD_CHARACTER_GROUND, DBHelper.GD_U, DBHelper.GD_R, DBHelper.GD_PURPOSE, DBHelper.GD_PLACE,
                    DBHelper.GD_DISTANCE, DBHelper.GD_R1, DBHelper.GD_05L,
                    DBHelper.GD_NOTE}, "_id = ?", new String[] {String.valueOf(device_id)}, null, null, null);
            if (cursor1.moveToFirst()) {
                int uIndex = cursor1.getColumnIndex(DBHelper.GD_U);
                int characterIndex = cursor1.getColumnIndex(DBHelper.GD_CHARACTER_GROUND);
                int groundIndex = cursor1.getColumnIndex(DBHelper.GD_GROUND);
                int r1Index = cursor1.getColumnIndex(DBHelper.GD_R1);
                int distanceIndex = cursor1.getColumnIndex(DBHelper.GD_DISTANCE);
                int noteIndex = cursor1.getColumnIndex(DBHelper.GD_NOTE);
                int placeIndex = cursor1.getColumnIndex(DBHelper.GD_PLACE);
                int purposeIndex = cursor1.getColumnIndex(DBHelper.GD_PURPOSE);
                int rIndex = cursor1.getColumnIndex(DBHelper.GD_R);
                int l5Index = cursor1.getColumnIndex(DBHelper.GD_05L);
                do {
                    uTEXT.setText(cursor1.getString(uIndex));
                    characterTEXT.setText(cursor1.getString(characterIndex));
                    groundTEXT.setText(cursor1.getString(groundIndex));
                    rTEXT.setText(cursor1.getString(r1Index));
                    distanceTEXT.setText(cursor1.getString(distanceIndex));
                    noteTEXT.setText(cursor1.getString(noteIndex));
                    placeTEXT.setText(cursor1.getString(placeIndex));
                    purposeTEXT.setText(cursor1.getString(purposeIndex));
                    rEditTEXT.setText(cursor1.getString(rIndex));
                    distanceActiveTEXT.setText(cursor1.getString(l5Index));
                } while (cursor1.moveToNext());
            }
            cursor1.close();
        }

        //ЗАЗ.УСТР. ПРИМЕНЯЕТСЯ ДЛЯ ЭЛЕКТРОУСТАНОВКИ...
        uBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(GroundingDevicesActivity2.this);
                alert.setCancelable(false);
                alert.setTitle("Выберите напряжение электроустановки:");
                final String u_array[] = {"до 1000В", "до и выше 1000В", "свыше 1000В"};
                alert.setItems(u_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        uTEXT.setText(u_array[which]);
                    }
                });
                alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                alert.show();
            }
        });

        //ХАРАКТЕР ГРУНТА
        characterBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(GroundingDevicesActivity2.this);
                alert.setCancelable(false);
                alert.setTitle("Выберите характер грунта:");
                final String сharacter_array[] = {"сухой", "малой влажности", "средней влажности", "большой влажности"};
                alert.setItems(сharacter_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        characterTEXT.setText(сharacter_array[which]);
                    }
                });
                alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                alert.show();
            }
        });

        //ВИД ГРУНТА
        groundBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(GroundingDevicesActivity2.this);
                alert.setCancelable(false);
                alert.setTitle("Выберите вид грунта:");
                final String ground_array[] = {"суглинок", "тут", "позже", "будет", "что-то", "еще"};
                alert.setItems(ground_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        groundTEXT.setText(ground_array[which]);
                    }
                });
                alert.setPositiveButton("Ввести", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        AlertDialog.Builder alert1 = new AlertDialog.Builder(GroundingDevicesActivity2.this);
                        final View myView = getLayoutInflater().inflate(R.layout.dialog_for_names,null);
                        alert1.setCancelable(false);
                        alert1.setTitle("Введите вид грунта:");
                        final EditText input = myView.findViewById(R.id.editText);
                        //ОТКРЫВАЕМ КЛАВИАТУРУ
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                        alert1.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //СКРЫВАЕМ КЛАВИАТУРУ
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(myView.getWindowToken(),0);
                                String ground = input.getText().toString();
                                groundTEXT.setText(ground);
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

        //ДОПУСТИМОЕ СОПРОТИВЛЕНИЕ
        rBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(GroundingDevicesActivity2.this);
                alert.setCancelable(false);
                alert.setTitle("Выберите допустимое сопротивление:");
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
                alert.setTitle("Выберите назначение заземлителя:");
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

        //СОХРАНИТЬ ДАННЫЕ
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (placeTEXT.getText().toString().equals("Не выбрано") || rTEXT.getText().toString().equals("Не выбрано") ||
                        rEditTEXT.getText().toString().equals("") || distanceActiveTEXT.getText().toString().equals("")) {
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
                    String numbCentre = distanceActiveTEXT.getText().toString();
                    String[] randomArr = new String[6];
                    getRandomArr(randomArr, numbCentre);
                    //УДАЛИМ НАШ ЗАЗЕМЛИТЕЛЬ, ЕСЛИ МЫ ВНОСИМ ИЗМЕНЕНИЯ, А НЕ СОЗДАЕМ НОВЫЙ
                    if (change)
                        database.delete(DBHelper.TABLE_GD, "_id = ?", new String[]{String.valueOf(device_id)});
                    //СОЗДАЕМ НОВУЮ ЗАПИСЬ
                    ContentValues contentValues = new ContentValues();
                    if (change)
                        contentValues.put(DBHelper.GD_DEVICE_ID, device_id);
                    contentValues.put(DBHelper.GD_RESULT_VIEW, "см. результаты визуального осмотра");
                    contentValues.put(DBHelper.GD_GROUND, groundTEXT.getText().toString());
                    contentValues.put(DBHelper.GD_CHARACTER_GROUND, characterTEXT.getText().toString());
                    contentValues.put(DBHelper.GD_U, uTEXT.getText().toString());
                    contentValues.put(DBHelper.GD_MODE_NEUTRAL, "TN");
                    contentValues.put(DBHelper.GD_R, rEditTEXT.getText().toString());
                    contentValues.put(DBHelper.GD_PURPOSE, purposeTEXT.getText().toString());
                    contentValues.put(DBHelper.GD_PLACE, placeTEXT.getText().toString());
                    contentValues.put(DBHelper.GD_DISTANCE, distanceTEXT.getText().toString());
                    contentValues.put(DBHelper.GD_R1, rTEXT.getText().toString());
                    contentValues.put(DBHelper.GD_01L, "-");
                    contentValues.put(DBHelper.GD_02L, randomArr[0]);
                    contentValues.put(DBHelper.GD_03L, randomArr[1]);
                    contentValues.put(DBHelper.GD_04L, randomArr[2]);
                    contentValues.put(DBHelper.GD_05L, numbCentre);
                    contentValues.put(DBHelper.GD_06L, randomArr[3]);
                    contentValues.put(DBHelper.GD_07L, randomArr[4]);
                    contentValues.put(DBHelper.GD_08L, randomArr[5]);
                    contentValues.put(DBHelper.GD_09L, "-");
                    contentValues.put(DBHelper.GD_GRAPHICS, "-");
                    contentValues.put(DBHelper.GD_R2, numbCentre);
                    contentValues.put(DBHelper.GD_K, "-");
                    contentValues.put(DBHelper.GD_R3, "-");
                    contentValues.put(DBHelper.GD_CONCLUSION, "соответствует");
                    contentValues.put(DBHelper.GD_NOTE, noteTEXT.getText().toString());
                    database.insert(DBHelper.TABLE_GD, null, contentValues);
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

    void getRandomArr(String[] arr, String centre) {
        String prev = centre;
        int i;
        for (i = 3; i < 6; i++) {
            arr[i] = getNumb(prev, true);
            prev = arr[i];
        }
        prev = centre;
        for (i = 2; i >= 0; i--) {
            arr[i] = getNumb(prev, false);
            prev = arr[i];
        }
    }

    String getNumb(String pr, boolean plus) {
        Random generator = new Random();
        double numbPr, rand;
        if (pr.contains(","))
            numbPr = Double.valueOf(pr.replace(",","."));
        else
            numbPr = Double.valueOf(pr);
        rand = numbPr * ((generator.nextInt(51) + 50) / 10) / 100;
        if (plus)
            rand = numbPr + rand;
        else
            rand = numbPr - rand;
        return String.format("%.2g", rand);
    }
}
