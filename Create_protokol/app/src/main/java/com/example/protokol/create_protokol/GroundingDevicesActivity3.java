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
import android.widget.TextView;
import android.widget.Toast;

public class GroundingDevicesActivity3 extends AppCompatActivity {

    DBHelper dbHelper;
    boolean isNew = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grounding_devices3);

        dbHelper = new DBHelper(this);
        final SQLiteDatabase database = dbHelper.getWritableDatabase();

        Button uBTN = findViewById(R.id.button25);
        final TextView uTEXT = findViewById(R.id.textView9);
        final EditText rTEXT = findViewById(R.id.editText5);
        Button characterBTN = findViewById(R.id.button26);
        final TextView characterTEXT = findViewById(R.id.textView20);
        Button groundBTN = findViewById(R.id.button29);
        final TextView groundTEXT = findViewById(R.id.textView22);
        Button save = findViewById(R.id.button36);

        //НАСТРАИВАЕМ ACTIONBAR
        getSupportActionBar().setTitle("Заземл. и заз. устр-ва");
        getSupportActionBar().setSubtitle("Дополнительная информация");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ЗАПОЛНЯЕМ ДАННЫЕ, ЕСЛИ ОНИ ЕСТЬ
        Cursor cursor = database.query(DBHelper.TABLE_GD, new String[] {DBHelper.GD_CHARACTER_GROUND,
                DBHelper.GD_GROUND, DBHelper.GD_U, DBHelper.GD_R}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            isNew = false;
            int characterIndex = cursor.getColumnIndex(DBHelper.GD_CHARACTER_GROUND);
            int groundIndex = cursor. getColumnIndex(DBHelper.GD_GROUND);
            int uIndex = cursor. getColumnIndex(DBHelper.GD_U);
            int rIndex = cursor. getColumnIndex(DBHelper.GD_R);
            do {
                characterTEXT.setText(cursor.getString(characterIndex));
                groundTEXT.setText(cursor.getString(groundIndex));
                uTEXT.setText(cursor.getString(uIndex));
                rTEXT.setText(cursor.getString(rIndex));
            } while (cursor.moveToNext());
        }
        cursor.close();

        //ЗАЗ.УСТР. ПРИМЕНЯЕТСЯ ДЛЯ ЭЛЕКТРОУСТАНОВКИ...
        uBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(GroundingDevicesActivity3.this);
                alert.setCancelable(false);
                alert.setTitle("Выберете напряжение электроустановки:");
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
                AlertDialog.Builder alert = new AlertDialog.Builder(GroundingDevicesActivity3.this);
                alert.setCancelable(false);
                alert.setTitle("Выберете характер грунта:");
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
                AlertDialog.Builder alert = new AlertDialog.Builder(GroundingDevicesActivity3.this);
                alert.setCancelable(false);
                alert.setTitle("Выберете вид грунта:");
                final String ground_array[] = {"суглинок", "тут", "позже", "будет", "что-то", "еще"};
                alert.setItems(ground_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        groundTEXT.setText(ground_array[which]);
                    }
                });
                alert.setPositiveButton("Ввести", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        AlertDialog.Builder alert1 = new AlertDialog.Builder(GroundingDevicesActivity3.this);
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

        //СОХРАНИТЬ ДАННЫЕ
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rTEXT.getText().toString().equals("")) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(GroundingDevicesActivity3.this);
                    alert.setCancelable(false);
                    alert.setMessage("Заполните все поля!");
                    alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    });
                    alert.show();
                }
                else {
                    if (!isNew)
                        database.delete(DBHelper.TABLE_GD, null, null);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DBHelper.GD_RESULT_VIEW, "см. результаты визуального осмотра");
                    contentValues.put(DBHelper.GD_GROUND, groundTEXT.getText().toString());
                    contentValues.put(DBHelper.GD_CHARACTER_GROUND, characterTEXT.getText().toString());
                    contentValues.put(DBHelper.GD_U, uTEXT.getText().toString());
                    contentValues.put(DBHelper.GD_MODE_NEUTRAL, "TN");
                    contentValues.put(DBHelper.GD_R, rTEXT.getText().toString());
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
                Intent intent1 = new Intent(GroundingDevicesActivity3.this, MainActivity.class);
                startActivity(intent1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
