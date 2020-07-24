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

public class RoomElementActivity4 extends AppCompatActivity {

    DBHelper dbHelper;
    String nameFloor, nameRoom;
    int idFloor, idRoom;
    String unit = "пусто";
    String rChange = "", conclusionChange = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_element4);

        dbHelper = new DBHelper(this);
        final SQLiteDatabase database = dbHelper.getWritableDatabase();

        final LinearLayout nameLayout = findViewById(R.id.nameLayout);
        final LinearLayout numbLayout = findViewById(R.id.numbLayout);

        final TextView nameText = findViewById(R.id.textView39);
        Button nameButton = findViewById(R.id.button59);
        final RadioGroup radioGroup = findViewById(R.id.radioGroup);
        final Switch nzSwitch = findViewById(R.id.switch1);
        final Button nzButton = findViewById(R.id.button25);
        final TextView numbElementsText = findViewById(R.id.textView21);
        Button numbElementsButton = findViewById(R.id.button38);
        Button saveButton = findViewById(R.id.button35);

        nameFloor = getIntent().getStringExtra("nameFloor");
        idFloor = getIntent().getIntExtra("idFloor", 0);
        nameRoom = getIntent().getStringExtra("nameRoom");
        idRoom = getIntent().getIntExtra("idRoom", 0);
        String nameElem = getIntent().getStringExtra("nameElem");
        final int idElem = getIntent().getIntExtra("idElem", -1);

        //НАСТРАИВАЕМ ACTIONBAR
        getSupportActionBar().setTitle("Металлосвязь");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ОПРЕДЕЛЯЕМ, СОЗДАЕМ ЛИ МЫ НОВУЮ ГРУППУ ИЛИ ИЗМЕНЯЕМ СТАРУЮ
        final boolean change;
        if (idElem != -1) {
            change = true;
            getSupportActionBar().setSubtitle("Редактирование элемента");
        }
        else {
            change = false;
            getSupportActionBar().setSubtitle("Добавление элемента");
        }

        //ЗАПОЛНЕНИЕ ДАННЫХ, ЕСЛИ ОНИ ПЕРЕДАНЫ
        if (change) {
            nameText.setText(nameElem);
            //ЗАПРОС В БД(ПОЛУЧЕНИЕ ИНФОРМАЦИИ О ГРУППЕ)
            Cursor cursor = database.query(DBHelper.TABLE_ELEMENTS, new String[] {DBHelper.EL_NAME, DBHelper.EL_UNIT,
                    DBHelper.EL_NUMBER, DBHelper.EL_SOPR, DBHelper.EL_CONCLUSION}, "_id = ?", new String[] {String.valueOf(idElem)}, null, null, null);
            if (cursor.moveToFirst()) {
                int unitIndex = cursor.getColumnIndex(DBHelper.EL_UNIT);
                int numbIndex = cursor.getColumnIndex(DBHelper.EL_NUMBER);
                int rIndex = cursor.getColumnIndex(DBHelper.EL_SOPR);
                int conclusionIndex = cursor.getColumnIndex(DBHelper.EL_CONCLUSION);
                do {
                    unit = cursor.getString(unitIndex);
                    numbElementsText.setText(cursor.getString(numbIndex));
                    rChange = cursor.getString(rIndex);
                    conclusionChange = cursor.getString(conclusionIndex);
                } while (cursor.moveToNext());
            }
            cursor.close();
            nzSwitch.setChecked(rChange.equals("Н.З."));
            if (unit.equals("гн"))
                radioGroup.check(R.id.radio_gn);
            else if (unit.equals("шт"))
                radioGroup.check(R.id.radio_sht);
        }

        nameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(RoomElementActivity4.this);
                final View myView = getLayoutInflater().inflate(R.layout.dialog_for_marks,null);
                alert.setCancelable(false);
                alert.setTitle("Введите название предмета:");
                final AutoCompleteTextView nameEl = myView.findViewById(R.id.autoCompleteTextView3);
                ImageView arrow = myView.findViewById(R.id.imageView4);
                //ОТОБРАЖЕНИЕ ВЫПЛЫВАЮЩЕГО СПИСКА
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(RoomElementActivity4.this, android.R.layout.simple_dropdown_item_1line, getNamesEl(database));
                nameEl.setAdapter(adapter1);
                openKeyboard();
                arrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        closeKeyboard(myView);
                        nameEl.showDropDown();
                    }
                });
                alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        closeKeyboard(myView);
                        String name = nameEl.getText().toString();
                        nameText.setText(name);
                        //Если новое название элемента, то вносим его в базу
                        if (!Arrays.asList(getNamesEl(database)).contains(name)){
                            ContentValues newName = new ContentValues();
                            newName.put(DBHelper.NAME_EL, name);
                            database.insert(DBHelper.TABLE_NAMES_EL, null, newName);
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
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_gn:
                        unit = "гн";
                        break;
                    case R.id.radio_sht:
                        unit = "шт";
                        break;
                    case R.id.radio_empty:
                        unit = "пусто";
                        break;
                }
            }
        });

        nzButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nzSwitch.setChecked(!nzSwitch.isChecked());
            }
        });

        numbElementsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert1 = new AlertDialog.Builder(RoomElementActivity4.this);
                final View myView = getLayoutInflater().inflate(R.layout.dialog_for_veins,null);
                alert1.setCancelable(false);
                alert1.setTitle("Введите кол-во проверенных элементов:");
                final EditText input = myView.findViewById(R.id.editText2);
                openKeyboard();
                alert1.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        closeKeyboard(myView);
                        numbElementsText.setText(input.getText().toString());
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

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String r, conclusion;
                if (nzSwitch.isChecked()) {
                    r = "Н.З.";
                    conclusion = "не соответствует";
                }
                else {
                    conclusion = "соответствует";
                    if (rChange.equals("Н.З.") || rChange.equals(""))
                        r = random();
                    else
                        r = rChange;
                }
                String name = nameText.getText().toString();
                String numb = numbElementsText.getText().toString();
                if (isIncorrectInput(name, nameLayout) | isIncorrectInput(numb, numbLayout)) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(RoomElementActivity4.this);
                    alert.setCancelable(false);
                    alert.setMessage("Заполните все поля!");
                    alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    });
                    alert.show();
                }
                else {
                    //Если новое название элемента, то вносим его в базу
                    if (!Arrays.asList(getNamesEl(database)).contains(name)){
                        ContentValues newName = new ContentValues();
                        newName.put(DBHelper.NAME_EL, name);
                        database.insert(DBHelper.TABLE_NAMES_EL, null, newName);
                    }
                    //УДАЛИМ НАШ ЭЛЕМЕНТ, ЕСЛИ МЫ ВНОСИМ ИЗМЕНЕНИЯ, А НЕ СОЗДАЕМ НОВЫЙ
                    if (change)
                        database.delete(DBHelper.TABLE_ELEMENTS, "_id = ?", new String[]{String.valueOf(idElem)});
                    //СОЗДАЕМ НОВУЮ ЗАПИСЬ
                    ContentValues newEL = new ContentValues();
                    if (change)
                        newEL.put(DBHelper.EL_ID, idElem);
                    newEL.put(DBHelper.EL_NAME, name);
                    newEL.put(DBHelper.EL_UNIT, unit);
                    newEL.put(DBHelper.ROOM_ID, idRoom);
                    newEL.put(DBHelper.EL_NUMBER, numb);
                    newEL.put(DBHelper.EL_SOPR, r);
                    newEL.put(DBHelper.EL_CONCLUSION, conclusion);
                    database.insert(DBHelper.TABLE_ELEMENTS, null, newEL);
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Данные сохранены", Toast.LENGTH_SHORT);
                    toast.show();
                    //ВОЗВРАЩАЕМСЯ НАЗАД
                    Intent intent = new Intent("android.intent.action.RoomElement3");
                    intent.putExtra("nameFloor", nameFloor);
                    intent.putExtra("idFloor", idFloor);
                    intent.putExtra("nameRoom", nameRoom);
                    intent.putExtra("idRoom", idRoom);
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
                Intent intent = new Intent("android.intent.action.RoomElement3");
                intent.putExtra("nameFloor", nameFloor);
                intent.putExtra("idFloor", idFloor);
                intent.putExtra("nameRoom", nameRoom);
                intent.putExtra("idRoom", idRoom);
                startActivity(intent);
                return true;
            case R.id.action_main:
                Intent intent1 = new Intent(RoomElementActivity4.this, MainActivity.class);
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

    //ПОЛУЧЕНИЕ НАЗВАНИЙ ЭЛЕМЕНТОВ
    public String[] getNamesEl(SQLiteDatabase database) {
        final ArrayList<String> nameEl = new ArrayList <String>();
        Cursor cursor = database.query(DBHelper.TABLE_NAMES_EL, new String[] {DBHelper.NAME_EL}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(DBHelper.NAME_EL);
            do {
                nameEl.add(cursor.getString(nameIndex));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return nameEl.toArray(new String[nameEl.size()]);
    }

    //ГЕНЕРАТОР
    public String random() {
        String[] soprot = {"0,02","0,03","0,04"} ;
        Random generator = new Random();
        int randomIndex = generator.nextInt(soprot.length);
        return soprot[randomIndex];
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
