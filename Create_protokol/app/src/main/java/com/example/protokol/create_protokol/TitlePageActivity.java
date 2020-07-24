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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class TitlePageActivity extends AppCompatActivity {

    DBHelper dbHelper;
    Calendar calendar;
    DatePickerDialog datePickerDialog;
    boolean isNew = true;
    TemplatePDF templatePDF;
    String[] months = {"января", "февраля", "марта", "апреля", "мая", "июня", "июля", "августа", "сентября", "октября", "ноября", "декабря"};
    String[] certificate = {"Свидетельство о регистрации лаборатории\n№  ", "А-11-18-0429", "\nВыдано ",
            "«Приокское Управление  Ростехнадзора»", "\nСрок действия от  ", "« 19 » апреля 2018 г.",
            " до ", "« 19 » апреля  2021 г.", "\nАдрес ЭИЛ: ", "г. Тула, ул. Кирова, 22", "\nТелефон: ",
            " 8(4872) 710062", " Факс: ", " 8(4872) 710062", "\nЕ-mail: ", "info@smpcentr.ru"};
    String[] info = {"NUMBER", "DATE", "NAME", "ADDRESS", "TARGET"};
    EditText temperature, humidity, pressure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_page);

        dbHelper = new DBHelper(this);
        final SQLiteDatabase database = dbHelper.getWritableDatabase();

        final LinearLayout termsLayout = findViewById(R.id.termsLayout);
        final LinearLayout nameElectroLayout = findViewById(R.id.nameElectroLayout);
        final LinearLayout addressLayout = findViewById(R.id.addressLayout);
        final LinearLayout numberPrLayout = findViewById(R.id.numberPrLayout);
        final LinearLayout dateLayout = findViewById(R.id.dateLayout);
        final LinearLayout targetLayout = findViewById(R.id.targetLayout);

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
        temperature = findViewById(R.id.editText7);
        humidity = findViewById(R.id.editText8);
        pressure = findViewById(R.id.editText9);
        Button save = findViewById(R.id.button43);
        Button openPDF = findViewById(R.id.button42);

        //НАСТРАИВАЕМ ACTIONBAR
        getSupportActionBar().setTitle("Титульный лист");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ЗАПОЛНЯЕМ ДАННЫЕ, ЕСЛИ ОНИ ЕСТЬ
        Cursor cursor = database.query(DBHelper.TABLE_TITLE, new String[] {DBHelper.TITLE_NAME_ELECTRO, DBHelper.TITLE_TARGET,
                DBHelper.TITLE_ADDRESS, DBHelper.TITLE_NUMBER_OF_PROTOKOL, DBHelper.TITLE_DATE,
                DBHelper.TITLE_TEMPERATURE, DBHelper.TITLE_HUMIDITY, DBHelper.TITLE_PRESSURE}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            isNew = false;
            int nameElectroIndex = cursor.getColumnIndex(DBHelper.TITLE_NAME_ELECTRO);
            int targetIndex = cursor.getColumnIndex(DBHelper.TITLE_TARGET);
            int addressIndex = cursor.getColumnIndex(DBHelper.TITLE_ADDRESS);
            int numberPrIndex = cursor.getColumnIndex(DBHelper.TITLE_NUMBER_OF_PROTOKOL);
            int dateIndex = cursor.getColumnIndex(DBHelper.TITLE_DATE);
            int temperatureIndex = cursor.getColumnIndex(DBHelper.TITLE_TEMPERATURE);
            int humidityIndex = cursor.getColumnIndex(DBHelper.TITLE_HUMIDITY);
            int pressureIndex = cursor.getColumnIndex(DBHelper.TITLE_PRESSURE);
            do {
                nameElectroTEXT.setText(cursor.getString(nameElectroIndex));
                targetTEXT.setText(cursor.getString(targetIndex));
                addressTEXT.setText(cursor.getString(addressIndex));
                numberPrTEXT.setText(cursor.getString(numberPrIndex));
                dateTEXT.setText(cursor.getString(dateIndex));
                temperature.setText(cursor.getString(temperatureIndex));
                humidity.setText(cursor.getString(humidityIndex));
                pressure.setText(cursor.getString(pressureIndex));
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
                if (!nameElectroTEXT.getText().toString().equals("Нет"))
                    input.setText(nameElectroTEXT.getText().toString());
                openKeyboard();
                alert1.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        closeKeyboard(myView);
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
                clearFocus();
            }
        });

        //ЦЕЛЬ ИСПЫТАНИЙ
        targetBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(TitlePageActivity.this);
                alert.setCancelable(false);
                alert.setTitle("Выберите цель испытаний:");
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
                clearFocus();
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
                if (!addressTEXT.getText().toString().equals("Нет"))
                    input.setText(addressTEXT.getText().toString());
                openKeyboard();
                alert1.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        closeKeyboard(myView);
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
                clearFocus();
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
                openKeyboard();
                alert1.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        closeKeyboard(myView);
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
                clearFocus();
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
                clearFocus();
            }
        });

        //СОХРАНЕНИЕ ДАННЫХ
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isIncorrectInput(nameElectroTEXT.getText().toString(), nameElectroLayout) |
                        isIncorrectInput(addressTEXT.getText().toString(), addressLayout) |
                        isIncorrectInput(numberPrTEXT.getText().toString(), numberPrLayout) |
                        isIncorrectInput(dateTEXT.getText().toString(), dateLayout) |
                        isIncorrectInput(targetTEXT.getText().toString(), targetLayout) | (
                        isIncorrectInput(temperature.getText().toString(), termsLayout)||
                        isIncorrectInput(humidity.getText().toString(), termsLayout)||
                        isIncorrectInput(pressure.getText().toString(), termsLayout))) {
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
                            contentValues.put(DBHelper.TITLE_TEMPERATURE, temperature.getText().toString());
                            contentValues.put(DBHelper.TITLE_HUMIDITY, humidity.getText().toString());
                            contentValues.put(DBHelper.TITLE_PRESSURE, pressure.getText().toString());
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
            }
        });

        //ОТКРЫТИЕ ПДФ
        openPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ПРОСТО ОТКРЫТЬ ИЛИ С СОХРАНЕНИЕМ?
                AlertDialog.Builder builder1 = new AlertDialog.Builder(TitlePageActivity.this);
                builder1.setPositiveButton("Посмотреть", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //ПРОСТО ПОСМОТРЕТЬ
                        opPFD(database, null);
                    }
                });
                builder1.setNegativeButton("Сохранить", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //ЗАПРАШИВАЕМ НАЗВАНИЕ ФАЙЛА
                        AlertDialog.Builder alert = new AlertDialog.Builder(TitlePageActivity.this);
                        final View myView = getLayoutInflater().inflate(R.layout.dialog_for_names,null);
                        alert.setCancelable(false);
                        alert.setTitle("Введите название сохраняемого файла:");
                        final EditText input = myView.findViewById(R.id.editText);
                        openKeyboard();
                        alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                closeKeyboard(myView);
                                String namefile = input.getText().toString();
                                if (namefile.equals(""))
                                    namefile = null;
                                opPFD(database, namefile);
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
                });
                builder1.setMessage("Хотите просто посмотреть файл или же открыть с дальнейшим сохранением?");
                AlertDialog dialog1 = builder1.create();
                dialog1.show();
            }
        });

    }

    public void opPFD(SQLiteDatabase database, String namefile) {
        if (namefile == null)
            namefile = "TepmlatePDF";
        String title_p = "1.  Листов всего:\n" +
                "2.  Протокол  испытаний распространяется только на электроустановку здания, подвергаемого\n" +
                "     испытаниям.\n" +
                "3.  Протокол испытаний не может быть частично или полностью перепечатан без разрешения\n" +
                "     Заказчика или электроизмерительной лаборатории.\n" +
                "4.  На каждом листе протокола ставится печать электроизмерительной лаборатории или\n" +
                "     организации";
        String dateStr = "                       «  __  » _________  ______ г.";
        templatePDF = new TemplatePDF(getApplicationContext());
        templatePDF.openDocument(namefile, false);
        templatePDF.addMetaData("Protokol", "Item", "Company");
        templatePDF.addCenter_BD("ЭЛЕКТРОИЗМЕРИТЕЛЬНАЯ ЛАБОРАТОРИЯ", 12, 0, 0);
        templatePDF.addCenter_Nomal("Общество с ограниченной ответственностью «СМП ЦЕНТР»", 12, 0,0);
        templatePDF.addCenter_BD("( ЭЛ ООО « СМП ЦЕНТР »)", 12, 0, 5);
        templatePDF.addParagraphUp_Title(certificate, 12);
        Cursor cursor = database.query(DBHelper.TABLE_TITLE, new String[] {DBHelper.TITLE_NAME_ELECTRO, DBHelper.TITLE_TARGET,
                DBHelper.TITLE_ADDRESS, DBHelper.TITLE_NUMBER_OF_PROTOKOL, DBHelper.TITLE_DATE}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int nameElectroIndex = cursor.getColumnIndex(DBHelper.TITLE_NAME_ELECTRO);
            int targetIndex = cursor.getColumnIndex(DBHelper.TITLE_TARGET);
            int addressIndex = cursor.getColumnIndex(DBHelper.TITLE_ADDRESS);
            int numberPrIndex = cursor.getColumnIndex(DBHelper.TITLE_NUMBER_OF_PROTOKOL);
            int dateIndex = cursor.getColumnIndex(DBHelper.TITLE_DATE);
            do {
                info[0] = (cursor.getString(numberPrIndex));
                info[1] = (cursor.getString(dateIndex));
                info[2] = (cursor.getString(nameElectroIndex));
                info[3] = (cursor.getString(addressIndex));
                info[4] = (cursor.getString(targetIndex));
            } while (cursor.moveToNext());
        }
        cursor.close();
        templatePDF.addCenter_BD("ПРОТОКОЛ № " + info[0] + " от " + info[1], 16, 50, 5);
        templatePDF.addCenter_BD("проверки (испытаний) электроустановки", 13, 0, 35);
        templatePDF.addCenter_UnderAndBD("      " + info[2] + "      ", 12, 0,0);
        templatePDF.addCenter_Nomal("наименование электроустановки", 8, 0, 28);
        templatePDF.createTableTitle(info[3], info[4]);
        templatePDF.addCenter_Nomal("приёмо-сдаточные, периодические, контрольные", 8, 0, 45);
        templatePDF.addParagraph_Normal(title_p, 12, 0, 30);
        templatePDF.addParagraph_Normal("            Главный  инженер  ООО «СМП ЦЕНТР» ", 12, 0, 15);
        templatePDF.addParagraph_Normal("       м п           ____________________      / С.П. Филин /", 12, 0, 5);
        if (!info[1].equals("DATE"))
            dateStr = "                       «  " + Integer.parseInt(info[1].substring(0,2)) + "  » " +
                    months[Integer.parseInt(info[1].substring(4,6)) - 1] + "  " + info[1].substring(8,12) + " г.";
        templatePDF.addParagraph_Normal(dateStr, 12, 0,0);
        templatePDF.drawRectangleTitle();
        templatePDF.closeDocument();
        templatePDF.appViewPDF(TitlePageActivity.this);
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

    boolean isIncorrectInput(String data, LinearLayout layout) {
        if (data.equals("") || data.equals("Нет")) {
            layout.setBackgroundResource(R.drawable.incorrect_input);
            return true;
        }
        layout.setBackgroundResource(R.drawable.listview);
        return false;
    }

    void clearFocus() {
        temperature.clearFocus();
        humidity.clearFocus();
        pressure.clearFocus();
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
