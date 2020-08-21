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
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class RoomElementActivity3 extends AppCompatActivity {

    DBHelper dbHelper;
    String nameFloor;
    int idFloor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_element3);

        dbHelper = new DBHelper(this);
        final SQLiteDatabase database = dbHelper.getWritableDatabase();

        TextView floor = findViewById(R.id.textView6);
        TextView room = findViewById(R.id.textView7);
        final ListView listElements = findViewById(R.id.elements);
        Button addElement = findViewById(R.id.button9);
        nameFloor = getIntent().getStringExtra("nameFloor");
        idFloor = getIntent().getIntExtra("idFloor", 0);
        final String nameRoom = getIntent().getStringExtra("nameRoom");
        final int idRoom = getIntent().getIntExtra("idRoom", 0);

        //НАСТРАИВАЕМ ACTIONBAR
        getSupportActionBar().setSubtitle("Элементы");
        getSupportActionBar().setTitle("Металлосвязь");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ВЫВОД ЭТАЖА И КОМНАТЫ
        floor.setText("Этаж: " + nameFloor);
        room.setText("Комната: " + nameRoom);

        //ЗАПРОС В БД И ЗАПОЛНЕНИЕ СПИСКА ЭЛЕМЕНТОВ
        addSpisokElements(database, listElements, idRoom);

        //ДОБАВИТЬ ЭЛЕМЕНТ
        addElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.RoomElement4");
                intent.putExtra("nameFloor", nameFloor);
                intent.putExtra("idFloor", idFloor);
                intent.putExtra("nameRoom", nameRoom);
                intent.putExtra("idRoom", idRoom);
                startActivity(intent);
                //FormAddEl(database, listElements, idRoom, false, "", "");
            }
        });

        //ПОСМОТРЕТЬ, ИЗМЕНИТЬ И УДАЛИТЬ ЭЛЕМЕНТ
        listElements.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, final long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(RoomElementActivity3.this);
                alert.setTitle(((TextView) view).getText());
                String arrayMenu[] = {"\nПосмотреть\n", "\nРедактировать\n", "\nЗначение фаза-нуль\n", "\nПовторить\n", "\nУдалить элемент\n"};
                alert.setItems(arrayMenu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //ЗАПРОС В БД ДЛЯ ПОЛУЧЕНИЯ ID НУЖНОГО ЭЛЕМЕНТА
                        Cursor cursor4 = database.query(DBHelper.TABLE_ELEMENTS, new String[] {DBHelper.EL_ID}, "room_id = ?", new String[] {String.valueOf(idRoom)}, null, null, "_id DESC");
                        cursor4.moveToPosition(position);
                        int elementIndex = cursor4.getColumnIndex(DBHelper.EL_ID);
                        final int elementId = cursor4.getInt(elementIndex);
                        cursor4.close();

                        //ПОСМОТРЕТЬ
                        if (which == 0) {
                            String numb = "", r = "", conclusion = "", phaze_zero = "";
                            //ПОЛУЧЕНИЕ ИНФОРМАЦИИ ОБ ЭЛЕМЕНТЕ
                            Cursor cursor2;
                            Cursor cursor1 = database.query(DBHelper.TABLE_ELEMENTS, new String[] {DBHelper.EL_ID, DBHelper.EL_NAME,
                                    DBHelper.EL_NUMBER, DBHelper.EL_SOPR, DBHelper.EL_CONCLUSION}, "_id = ?", new String[] {String.valueOf(elementId)}, null, null, null);
                            if (cursor1.moveToFirst()) {
                                int numbIndex = cursor1.getColumnIndex(DBHelper.EL_NUMBER);
                                int rIndex = cursor1.getColumnIndex(DBHelper.EL_SOPR);
                                int conclusionIndex = cursor1.getColumnIndex(DBHelper.EL_CONCLUSION);
                                do {
                                    cursor2 = database.query(DBHelper.TABLE_ELEMENTS_PZ, new String[] {DBHelper.EL_PZ_U, DBHelper.EL_PZ_R,
                                            DBHelper.EL_PZ_I}, "el_id = ?", new String[] {String.valueOf(elementId)}, null, null, null);
                                    if (cursor2.moveToFirst()) {
                                        int pz_uIndex = cursor2.getColumnIndex(DBHelper.EL_PZ_U);
                                        int pz_rIndex = cursor2.getColumnIndex(DBHelper.EL_PZ_R);
                                        int pz_iIndex = cursor2.getColumnIndex(DBHelper.EL_PZ_I);
                                        do {
                                            String pz_u = cursor2.getString(pz_uIndex);
                                            String pz_r = cursor2.getString(pz_rIndex);
                                            String pz_i = cursor2.getString(pz_iIndex);
                                            phaze_zero += "<br>Значение №" + String.valueOf(cursor2.getPosition() + 1) +
                                                    ":<br>Напряжение = " + pz_u +
                                                    "<br>Сопротивление = " + pz_r +
                                                    "<br>Ток К.З. = " + pz_i + "<br>";
                                        } while (cursor2.moveToNext());
                                    }
                                    cursor2.close();
                                    numb = cursor1.getString(numbIndex);
                                    r = cursor1.getString(rIndex);
                                    conclusion = cursor1.getString(conclusionIndex);
                                } while (cursor1.moveToNext());
                            }
                            cursor1.close();
                            if (phaze_zero.equals(""))
                                phaze_zero = "Значения отсутствуют";
                            AlertDialog.Builder builder4 = new AlertDialog.Builder(RoomElementActivity3.this);
                            builder4.setCancelable(false);
                            builder4.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                }
                            });
                            builder4.setMessage(Html.fromHtml("<b>Кол-во элементов: </b>" + numb + "<br>" +
                                    "<b>R допустимое: </b>" + "0,05" + "<br>" + "<b>R измеренное: </b>" + r + "<br>" +
                                    "<b>Вывод: </b>" + conclusion + "<br><br>" + "<b>Значения фаза-нуль: </b>" + "<br>" + phaze_zero));
                            builder4.setTitle(((TextView) view).getText());
                            AlertDialog dialog4 = builder4.create();
                            dialog4.show();
                        }

                        //ИЗМЕНИТЬ
                        if (which == 1) {
                            String n = ((TextView) view).getText().toString();
                            Intent intent = new Intent("android.intent.action.RoomElement4");
                            intent.putExtra("nameFloor", nameFloor);
                            intent.putExtra("idFloor", idFloor);
                            intent.putExtra("nameRoom", nameRoom);
                            intent.putExtra("idRoom", idRoom);
                            intent.putExtra("nameElem", n.substring(0, n.indexOf("(") - 1));
                            intent.putExtra("idElem", elementId);
                            startActivity(intent);
                            //changeElement(database, listElements, idRoom, elementId);
                        }

                        //ЗНАЧЕНИЕ ФАЗА-НУЛЬ
                        if (which == 2) {
                            Intent intent = new Intent("android.intent.action.RoomElementPZ");
                            intent.putExtra("nameFloor", nameFloor);
                            intent.putExtra("idFloor", idFloor);
                            intent.putExtra("nameRoom", nameRoom);
                            intent.putExtra("idRoom", idRoom);
                            intent.putExtra("nameElem", ((TextView) view).getText().toString());
                            intent.putExtra("idElem", elementId);
                            startActivity(intent);
                        }

                        //ПОВТОРИТЬ
                        if (which == 3) {
                            AlertDialog.Builder alert1 = new AlertDialog.Builder(RoomElementActivity3.this);
                            final View myView = getLayoutInflater().inflate(R.layout.dialog_for_veins,null);
                            alert1.setCancelable(false);
                            alert1.setTitle("Кол-во повторений:");
                            final EditText input = myView.findViewById(R.id.editText2);
                            openKeyboard();
                            alert1.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    closeKeyboard(myView);
                                    int numb_replay = Integer.parseInt(input.getText().toString());

                                    String name = "", unit = "", numb = "", r = "", concl = "";
                                    Cursor cursor1 = database.query(DBHelper.TABLE_ELEMENTS, new String[] {
                                            DBHelper.EL_NAME, DBHelper.EL_UNIT, DBHelper.EL_NUMBER, DBHelper.EL_SOPR, DBHelper.EL_CONCLUSION},
                                            "_id = ?", new String[] {String.valueOf(elementId)}, null, null, null);
                                    if (cursor1.moveToFirst()) {
                                        int nameIndex = cursor1.getColumnIndex(DBHelper.EL_NAME);
                                        int unitIndex = cursor1.getColumnIndex(DBHelper.EL_UNIT);
                                        int numbIndex = cursor1.getColumnIndex(DBHelper.EL_NUMBER);
                                        int rIndex = cursor1.getColumnIndex(DBHelper.EL_SOPR);
                                        int conclIndex = cursor1.getColumnIndex(DBHelper.EL_CONCLUSION);
                                        do {
                                            name = cursor1.getString(nameIndex);
                                            unit = cursor1.getString(unitIndex);
                                            numb = cursor1.getString(numbIndex);
                                            r = cursor1.getString(rIndex);
                                            concl = cursor1.getString(conclIndex);
                                        } while (cursor1.moveToNext());
                                    }
                                    cursor1.close();

                                    if (numb_replay <= 30) {
                                        for (int i = 0; i < numb_replay; ++i) {
                                            if (!r.equals("Н.З."))
                                                r = random();
                                            ContentValues contentValues = new ContentValues();
                                            contentValues.put(DBHelper.ROOM_ID, idRoom);
                                            contentValues.put(DBHelper.EL_NAME, name);
                                            contentValues.put(DBHelper.EL_UNIT, unit);
                                            contentValues.put(DBHelper.EL_NUMBER, numb);
                                            contentValues.put(DBHelper.EL_SOPR, r);
                                            contentValues.put(DBHelper.EL_CONCLUSION, concl);
                                            database.insert(DBHelper.TABLE_ELEMENTS, null, contentValues);
                                            swapElements(listElements.getAdapter().getCount() - position, listElements.getAdapter().getCount() + 1, idRoom, database);
                                            addSpisokElements(database, listElements, idRoom);
                                        }
                                        Toast toast = Toast.makeText(getApplicationContext(),
                                                "Данные сохранены", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                    else {
                                        AlertDialog.Builder alert7 = new AlertDialog.Builder(RoomElementActivity3.this);
                                        alert7.setCancelable(false);
                                        alert7.setMessage("Превышено допустимое кол-во повторений");
                                        alert7.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {

                                            }
                                        });
                                        alert7.show();
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

                        //УДАЛИТЬ ГРУППУ
                        if (which == 4) {

                            //ПОДТВЕРЖДЕНИЕ
                            AlertDialog.Builder builder4 = new AlertDialog.Builder(RoomElementActivity3.this);
                            builder4.setCancelable(false);
                            builder4.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    database.delete(DBHelper.TABLE_ELEMENTS_PZ, "el_id = ?", new String[] {String.valueOf(elementId)});
                                    database.delete(DBHelper.TABLE_ELEMENTS, "_id = ?", new String[] {String.valueOf(elementId)});
                                    //ЗАПРОС В БД И ЗАПОЛНЕНИЕ СПИСКА ГРУПП
                                    addSpisokElements(database, listElements, idRoom);
                                    Toast toast2 = Toast.makeText(getApplicationContext(),
                                            "Элемент удален", Toast.LENGTH_SHORT);
                                    toast2.show();
                                }
                            });
                            builder4.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                }
                            });
                            builder4.setMessage("Вы точно хотите удалить <" + ((TextView) view).getText() + ">?");
                            AlertDialog dialog4 = builder4.create();
                            dialog4.show();
                        }
                    }
                });
                alert.show();
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
                Intent intent = new Intent("android.intent.action.RoomElement2");
                intent.putExtra("nameFloor", nameFloor);
                intent.putExtra("idFloor", idFloor);
                startActivity(intent);
                return true;
            case R.id.action_main:
                Intent intent1 = new Intent(RoomElementActivity3.this, MenuItemsActivity.class);
                startActivity(intent1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //ЗНАЧЕНИЯ ФАЗЫ-НУЛЬ
//    public void formAddPhaseZero() {
    //public void formAddPhaseZero(final SQLiteDatabase database, final ListView listElements, final int idRoom, boolean flag, String nameParam, String numbParam) {
//        final AlertDialog.Builder alert = new AlertDialog.Builder(RoomElementActivity3.this);
//        final View myView = getLayoutInflater().inflate(R.layout.dialog_for_add_phase_zero,null);
//        alert.setCancelable(false);
//        alert.setTitle("Значение фаза-нуль");
//        final AutoCompleteTextView nameEl = myView.findViewById(R.id.autoCompleteTextView3);
//        ImageView arrow = myView.findViewById(R.id.imageView4);
//        final Switch nz = myView.findViewById(R.id.switch8);
//        final EditText numbEl = myView.findViewById(R.id.editText3);
        //ЗАПОЛНЕНИЕ НАЧАЛЬНЫХ ДАННЫХ
//        nameEl.setText(nameParam);
//        numbEl.setText(numbParam);
//        nz.setChecked(flag);
//        alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                closeKeyboard(myView);
//                final String el =  nameEl.getText().toString();
//                final String numb = numbEl.getText().toString();
//                final boolean fl = nz.isChecked();
//                String r, conclusion;
//                if (el.equals("") || numb.equals("")) {
//                    AlertDialog.Builder alert = new AlertDialog.Builder(RoomElementActivity3.this);
//                    alert.setCancelable(false);
//                    alert.setMessage("Заполните все поля!");
//                    alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int whichButton) {
//                            FormAddEl(database, listElements, idRoom, fl, el, numb);
//                        }
//                    });
//                    alert.show();
//                }
//                else {
//                    //Если новое название элемента, то вносим его в базу
//                    if (!Arrays.asList(getNamesEl(database)).contains(el)){
//                        ContentValues newName = new ContentValues();
//                        newName.put(DBHelper.NAME_EL, el);
//                        database.insert(DBHelper.TABLE_NAMES_EL, null, newName);
//                    }
//                    if (!fl) {
//                        r = random();
//                        conclusion = "соответствует";
//                    }
//                    else {
//                        r = "Н.З.";
//                        conclusion = "не соответствует";
//                    }
//                    ContentValues newEL = new ContentValues();
//                    newEL.put(DBHelper.EL_NAME, el);
//                    newEL.put(DBHelper.ROOM_ID, idRoom);
//                    newEL.put(DBHelper.EL_NUMBER, numb);
//                    newEL.put(DBHelper.EL_SOPR, r);
//                    newEL.put(DBHelper.EL_CONCLUSION, conclusion);
//                    database.insert(DBHelper.TABLE_ELEMENTS, null, newEL);
//                    //ЗАПРОС В БД И ЗАПОЛНЕНИЕ СПИСКА ЭЛЕМЕНТОВ
//                    addSpisokElements(database, listElements, idRoom);
//                    Toast toast = Toast.makeText(getApplicationContext(),
//                            "Элемент <" + el + "> добавлен", Toast.LENGTH_SHORT);
//                    toast.show();
//                }
//            }
//        });
//        alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                closeKeyboard(myView);
//            }
//        });
//        alert.setView(myView);
//        alert.show();
//    }

    //ГЕНЕРАТОР
    public String random() {
        String[] soprot = {"0,02","0,03","0,04"} ;
        Random generator = new Random();
        int randomIndex = generator.nextInt(soprot.length);
        return soprot[randomIndex];
    }

    public void swapElements(int positionStop, int positionCurrent, int idRoom, SQLiteDatabase database) {
        if (positionCurrent - positionStop == 1)
            return;
        int idCurrent, idUp, elementIndex;
        ContentValues values;

        //ЗАПРОС В БД ДЛЯ ПОЛУЧЕНИЯ ID
        Cursor cursor4 = database.query(DBHelper.TABLE_ELEMENTS, new String[] {DBHelper.EL_ID}, "room_id = ?", new String[] {String.valueOf(idRoom)}, null, null, null);
        cursor4.moveToPosition(positionCurrent - 1);
        elementIndex = cursor4.getColumnIndex(DBHelper.EL_ID);
        idCurrent = cursor4.getInt(elementIndex);
        cursor4.close();

        while (positionCurrent - positionStop != 1) {

            //ЗАПРОС В БД ДЛЯ ПОЛУЧЕНИЯ ID И НАЗВАНИЯ ГРУППЫ ПОВЫШЕ
            Cursor cursor = database.query(DBHelper.TABLE_ELEMENTS, new String[] {DBHelper.EL_ID}, "room_id = ?", new String[] {String.valueOf(idRoom)}, null, null, null);
            cursor.moveToPosition(positionCurrent - 2);
            elementIndex = cursor.getColumnIndex(DBHelper.EL_ID);
            idUp = cursor.getInt(elementIndex);
            cursor.close();

            //МЕНЯЕМ ID ЭЛЕМЕНТОВ В ТАБЛИЦЕ ЗНАЧЕНИЙ ФАЗЫ-НУЛЬ
            values = new ContentValues();
            values.put(DBHelper.EL_PZ_ELEMENT_ID, Integer.toString(idCurrent));
            database.update(DBHelper.TABLE_ELEMENTS_PZ, values,"el_id = ?", new String[]{Integer.toString(idUp)});

            //МЕНЯЕМ ID
            values = new ContentValues();
            values.put(DBHelper.EL_ID, "-1");
            database.update(DBHelper.TABLE_ELEMENTS, values,"_id = ?", new String[]{Integer.toString(idUp)});
            values = new ContentValues();
            values.put(DBHelper.EL_ID, Integer.toString(idUp));
            database.update(DBHelper.TABLE_ELEMENTS, values,"_id = ?", new String[]{Integer.toString(idCurrent)});
            values = new ContentValues();
            values.put(DBHelper.EL_ID, Integer.toString(idCurrent));
            database.update(DBHelper.TABLE_ELEMENTS, values,"_id = ?", new String[]{"-1"});

            idCurrent = idUp;
            positionCurrent--;
        }
    }

    public void addSpisokElements(SQLiteDatabase database, ListView elements, int idRoom) {
        final ArrayList<String> spisokElements = new ArrayList <String>();
        String r = "", unit;
        Cursor cursor = database.query(DBHelper.TABLE_ELEMENTS, new String[] {DBHelper.EL_NAME, DBHelper.EL_UNIT,
                DBHelper.EL_NUMBER, DBHelper.ROOM_ID, DBHelper.EL_SOPR}, "room_id = ?", new String[] {String.valueOf(idRoom)}, null, null, "_id DESC");
        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(DBHelper.EL_NAME);
            int numberIndex = cursor.getColumnIndex(DBHelper.EL_NUMBER);
            int soprIndex = cursor.getColumnIndex(DBHelper.EL_SOPR);
            int unitIndex = cursor.getColumnIndex(DBHelper.EL_UNIT);
            do {
                if (cursor.getString(soprIndex).equals("Н.З.")) {
                    r = cursor.getString(soprIndex);
                }
                unit = cursor.getString(unitIndex);
                unit = (unit.equals("пусто")) ? "" : " " + unit;
                spisokElements.add(cursor.getString(nameIndex) + " (x" + cursor.getString(numberIndex) + unit + ") " + " " + r);
                r = "";
            } while (cursor.moveToNext());
        }
        cursor.close();
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, spisokElements);
        elements.setAdapter(adapter);
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

    void openKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }

    void closeKeyboard(View myView) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(myView.getWindowToken(),0);
    }
}
