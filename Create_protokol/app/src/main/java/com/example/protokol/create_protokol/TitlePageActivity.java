package com.example.protokol.create_protokol;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class TitlePageActivity extends AppCompatActivity {

    DBHelper dbHelper;
    Calendar calendar;
    DatePickerDialog datePickerDialog;
    boolean isNew = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_page);

        dbHelper = new DBHelper(this);
        final SQLiteDatabase database = dbHelper.getWritableDatabase();

        final Button nameElectroBTN = findViewById(R.id.button37);
        final TextView nameElectroTEXT = findViewById(R.id.textView33);
        Button targetBTN = findViewById(R.id.button38);
        final TextView targetTEXT = findViewById(R.id.textView35);
        final Button addressBTN = findViewById(R.id.button39);
        final TextView addressTEXT = findViewById(R.id.textView37);
        Button numberPrBTN = findViewById(R.id.button40);
        final TextView numberPrTEXT = findViewById(R.id.textView39);
        Button dateBTN = findViewById(R.id.button41);
        final TextView dateTEXT = findViewById(R.id.textView41);
        Button save = findViewById(R.id.button43);
        Button openPDF = findViewById(R.id.button42);

        //НАСТРАИВАЕМ ACTIONBAR
        getSupportActionBar().setTitle("Титульный лист");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ЗАПОЛНЯЕМ ДАННЫЕ, ЕСЛИ ОНИ ЕСТЬ
        Cursor cursor = database.query(DBHelper.TABLE_TITLE, new String[] {DBHelper.TITLE_NAME_ELECTRO, DBHelper.TITLE_TARGET,
                DBHelper.TITLE_ADDRESS, DBHelper.TITLE_NUMBER_OF_PROTOKOL, DBHelper.TITLE_DATE}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            isNew = false;
            int nameElectroIndex = cursor.getColumnIndex(DBHelper.TITLE_NAME_ELECTRO);
            int targetIndex = cursor. getColumnIndex(DBHelper.TITLE_TARGET);
            int addressIndex = cursor. getColumnIndex(DBHelper.TITLE_ADDRESS);
            int numberPrIndex = cursor. getColumnIndex(DBHelper.TITLE_NUMBER_OF_PROTOKOL);
            int dateIndex = cursor. getColumnIndex(DBHelper.TITLE_DATE);
            do {
                nameElectroTEXT.setText(cursor.getString(nameElectroIndex));
                targetTEXT.setText(cursor.getString(targetIndex));
                addressTEXT.setText(cursor.getString(addressIndex));
                numberPrTEXT.setText(cursor.getString(numberPrIndex));
                dateTEXT.setText(cursor.getString(dateIndex));
            } while (cursor.moveToNext());
        }
        cursor.close();

        //НАИМЕНОВАНИЕ ЭЛЕКТРОУСТАНОВКИ
        nameElectroBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert1 = new AlertDialog.Builder(TitlePageActivity.this);
                final View myView = getLayoutInflater().inflate(R.layout.dialog_for_names,null);
                alert1.setCancelable(false);
                alert1.setTitle("Введите наименование элктроустановки:");
                final EditText input = myView.findViewById(R.id.editText);
                //ОТКРЫВАЕМ КЛАВИАТУРУ
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                alert1.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //СКРЫВАЕМ КЛАВИАТУРУ
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(myView.getWindowToken(),0);
                        String nameElectro = input.getText().toString();
                        nameElectroTEXT.setText(nameElectro);
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

        //ЦЕЛЬ ИСПЫТАНИЙ
        targetBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(TitlePageActivity.this);
                alert.setCancelable(false);
                alert.setTitle("Выберете цель испытаний:");
                final String targets[] = {"приёмо-сдаточные", "периодические", "контрольные"};
                alert.setItems(targets, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        targetTEXT.setText(targets[which]);
                    }
                });
                alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                alert.show();
            }
        });

        //АДРЕС
        addressBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert1 = new AlertDialog.Builder(TitlePageActivity.this);
                final View myView = getLayoutInflater().inflate(R.layout.dialog_for_names,null);
                alert1.setCancelable(false);
                alert1.setTitle("Введите адрес:");
                final EditText input = myView.findViewById(R.id.editText);
                //ОТКРЫВАЕМ КЛАВИАТУРУ
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                alert1.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //СКРЫВАЕМ КЛАВИАТУРУ
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(myView.getWindowToken(),0);
                        String address = input.getText().toString();
                        addressTEXT.setText(address);
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

        //НОМЕР ПРОТОКОЛА
        numberPrBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert1 = new AlertDialog.Builder(TitlePageActivity.this);
                final View myView = getLayoutInflater().inflate(R.layout.dialog_for_section,null);
                alert1.setCancelable(false);
                alert1.setTitle("Введите номер протокола:");
                final EditText input = myView.findViewById(R.id.editText2);
                //ОТКРЫВАЕМ КЛАВИАТУРУ
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                alert1.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //СКРЫВАЕМ КЛАВИАТУРУ
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(myView.getWindowToken(),0);
                        String numberPr = input.getText().toString();
                        numberPrTEXT.setText(numberPr);
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

        //ДАТА
        dateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(TitlePageActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        if(month < 10 || day < 10)
                            if(month < 10 && day < 10)
                                dateTEXT.setText("0" + day + ". " + "0" + (month + 1) + ". " + year + " г.");
                            else
                                if(month < 10)
                                    dateTEXT.setText(day + ". " + "0" + (month + 1) + ". " + year + " г.");
                                else
                                    dateTEXT.setText("0" + day + ". " + (month + 1) + ". " + year + " г.");
                        else
                            dateTEXT.setText(day + ". " + (month + 1) + ". " + year + " г.");
                    }
                }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });

        //СОХРАНЕНИЕ ДАННЫХ
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(TitlePageActivity.this);
                alert.setCancelable(false);
                alert.setMessage("Вы уверены, что хотите сохранить изменения?");
                alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (!isNew)
                            database.delete(DBHelper.TABLE_TITLE, null, null);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(DBHelper.TITLE_ADDRESS, addressTEXT.getText().toString());
                        contentValues.put(DBHelper.TITLE_TARGET, targetTEXT.getText().toString());
                        contentValues.put(DBHelper.TITLE_NAME_ELECTRO, nameElectroTEXT.getText().toString());
                        contentValues.put(DBHelper.TITLE_NUMBER_OF_PROTOKOL, numberPrTEXT.getText().toString());
                        contentValues.put(DBHelper.TITLE_DATE, dateTEXT.getText().toString());
                        database.insert(DBHelper.TABLE_TITLE, null, contentValues);
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Изменения сохранены", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
                alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.show();
            }
        });

        //ОТКРЫТИЕ ПДФ
        openPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameElectroTEXT.getText().toString().equals("Нет") || addressTEXT.getText().toString().equals("Нет") || targetTEXT.getText().toString().equals("Нет") ||
                    numberPrTEXT.getText().toString().equals("Нет") || dateTEXT.getText().toString().equals("Нет")) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(TitlePageActivity.this);
                    alert.setCancelable(false);
                    alert.setMessage("Заполните все поля!");
                    alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    });
                    alert.show();
                }
                else {

                }
            }
        });

    }

    //НАЗАД
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(TitlePageActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
