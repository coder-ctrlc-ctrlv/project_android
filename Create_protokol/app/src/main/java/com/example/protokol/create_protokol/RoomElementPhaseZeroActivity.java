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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;

import static java.math.BigDecimal.ROUND_HALF_UP;

public class RoomElementPhaseZeroActivity extends AppCompatActivity {

    DBHelper dbHelper;
    String nameFloor, nameRoom;
    int idFloor, idRoom;
    Boolean isChange = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_element_phase_zero);

        dbHelper = new DBHelper(this);
        final SQLiteDatabase database = dbHelper.getWritableDatabase();

        final TextView nameText = findViewById(R.id.textView27);
        LinearLayout phaseZero_1 = findViewById(R.id.phaseZero_1);
        final LinearLayout hideLayout_1 = findViewById(R.id.hideLayout_1);
        final EditText u_1_edit = findViewById(R.id.editText2);
        final EditText r_1_edit = findViewById(R.id.editText3);
        final EditText i_1_edit = findViewById(R.id.editText4);
        LinearLayout phaseZero_2 = findViewById(R.id.phaseZero_2);
        final LinearLayout hideLayout_2 = findViewById(R.id.hideLayout_2);
        final EditText u_2_edit = findViewById(R.id.editText5);
        final EditText r_2_edit = findViewById(R.id.editText6);
        final EditText i_2_edit = findViewById(R.id.editText7);
        LinearLayout phaseZero_3 = findViewById(R.id.phaseZero_3);
        final LinearLayout hideLayout_3 = findViewById(R.id.hideLayout_3);
        final EditText u_3_edit = findViewById(R.id.editText8);
        final EditText r_3_edit = findViewById(R.id.editText9);
        final EditText i_3_edit = findViewById(R.id.editText10);
        Button deleteButton = findViewById(R.id.button34);
        Button saveButton = findViewById(R.id.button35);

        nameFloor = getIntent().getStringExtra("nameFloor");
        idFloor = getIntent().getIntExtra("idFloor", 0);
        nameRoom = getIntent().getStringExtra("nameRoom");
        idRoom = getIntent().getIntExtra("idRoom", 0);
        String nameElem = getIntent().getStringExtra("nameElem");
        final int idElem = getIntent().getIntExtra("idElem", -1);

        //НАСТРАИВАЕМ ACTIONBAR
        getSupportActionBar().setTitle("Металлосвязь");
        getSupportActionBar().setSubtitle("Значение фаза-нуль");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //УСТАНАВЛИВАЕМ ИМЯ ЭЛЕМЕНТА
        nameText.setText(nameElem);

        //ЗАПОЛНЕНИЕ ДАННЫХ, ЕСЛИ ОНИ ЕСТЬ
        Cursor cursor = database.query(DBHelper.TABLE_ELEMENTS_PZ, new String[] {DBHelper.EL_PZ_U, DBHelper.EL_PZ_R,
                DBHelper.EL_PZ_I}, "el_id = ?", new String[] {String.valueOf(idElem)}, null, null, null);
        int count_rows = cursor.getCount();
        if (cursor.moveToFirst()) {
            isChange = true;
            deleteButton.setVisibility(View.VISIBLE);
            int uIndex = cursor.getColumnIndex(DBHelper.EL_PZ_U);
            int rIndex = cursor.getColumnIndex(DBHelper.EL_PZ_R);
            int iIndex = cursor.getColumnIndex(DBHelper.EL_PZ_I);
            do {
                String u = cursor.getString(uIndex);
                String r = cursor.getString(rIndex);
                String i = cursor.getString(iIndex);
                switch (cursor.getPosition()) {
                    case 0:
                        setValues(u_1_edit, u, r_1_edit, r, i_1_edit, i);
                        break;
                    case 1:
                        setValues(u_2_edit, u, r_2_edit, r, i_2_edit, i);
                        break;
                    case 2:
                        setValues(u_3_edit, u, r_3_edit, r, i_3_edit, i);
                        break;
                    default:
                        break;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        if (count_rows == 3) {
            hideLayout_2.setVisibility(View.VISIBLE);
            hideLayout_3.setVisibility(View.VISIBLE);
        }
        else if (count_rows == 2) {
            hideLayout_2.setVisibility(View.VISIBLE);
        }

        // ЗНАЧЕНИЕ 1 СКРЫТЬ\ПОКАЗАТЬ
        phaseZero_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hideLayout_1.getVisibility() == View.GONE)
                    hideLayout_1.setVisibility(View.VISIBLE);
                else
                    hideLayout_1.setVisibility(View.GONE);
            }
        });



        // ЗНАЧЕНИЕ 2 СКРЫТЬ\ПОКАЗАТЬ
        phaseZero_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hideLayout_2.getVisibility() == View.GONE)
                    hideLayout_2.setVisibility(View.VISIBLE);
                else
                    hideLayout_2.setVisibility(View.GONE);
            }
        });

        // ЗНАЧЕНИЕ 3 СКРЫТЬ\ПОКАЗАТЬ
        phaseZero_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hideLayout_3.getVisibility() == View.GONE)
                    hideLayout_3.setVisibility(View.VISIBLE);
                else
                    hideLayout_3.setVisibility(View.GONE);
            }
        });

        // СОПРОТИВЛЕНИЕ_1
        r_1_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isGoodR(s)) {
                    i_1_edit.setText(getTok(s));
                }
                else {
                    i_1_edit.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // СОПРОТИВЛЕНИЕ_2
        r_2_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isGoodR(s)) {
                    i_2_edit.setText(getTok(s));
                }
                else {
                    i_2_edit.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // СОПРОТИВЛЕНИЕ_3
        r_3_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isGoodR(s)) {
                    i_3_edit.setText(getTok(s));
                }
                else {
                    i_3_edit.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.delete(DBHelper.TABLE_ELEMENTS_PZ, "el_id = ?", new String[] {String.valueOf(idElem)});
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
        });

        //СОХРАНИТЬ
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String u1 = u_1_edit.getText().toString();
                String r1 = r_1_edit.getText().toString();
                String i1 = i_1_edit.getText().toString();
                String u2 = u_2_edit.getText().toString();
                String r2 = r_2_edit.getText().toString();
                String i2 = i_2_edit.getText().toString();
                String u3 = u_3_edit.getText().toString();
                String r3 = r_3_edit.getText().toString();
                String i3 = i_3_edit.getText().toString();
                Boolean firstNumbIsEmpty = u1.equals("") || r1.equals("") || i1.equals("");
                Boolean secondNumbIsEmpty = u2.equals("") || r2.equals("") || i2.equals("");
                Boolean thirdNumbIsEmpty = u3.equals("") || r3.equals("") || i3.equals("");
                if (firstNumbIsEmpty && secondNumbIsEmpty && thirdNumbIsEmpty && !isChange) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(RoomElementPhaseZeroActivity.this);
                    alert.setCancelable(false);
                    alert.setMessage("Ни одно значение не заполнено!");
                    alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    });
                    alert.show();
                }
                else {
                    if (isChange)
                        database.delete(DBHelper.TABLE_ELEMENTS_PZ, "el_id = ?", new String[] {String.valueOf(idElem)});
                    if (!firstNumbIsEmpty)
                        addNewValuePhaseZero(database, idElem, u1, r1, i1);
                    if (!secondNumbIsEmpty)
                        addNewValuePhaseZero(database, idElem, u2, r2, i2);
                    if (!thirdNumbIsEmpty)
                        addNewValuePhaseZero(database, idElem, u3, r3, i3);
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
                Intent intent1 = new Intent(RoomElementPhaseZeroActivity.this, MenuItemsActivity.class);
                startActivity(intent1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void addNewValuePhaseZero(SQLiteDatabase db, int idEl, String u, String r, String i) {
        ContentValues newEL = new ContentValues();
        newEL.put(DBHelper.EL_PZ_ELEMENT_ID, idEl);
        newEL.put(DBHelper.EL_PZ_U, u);
        newEL.put(DBHelper.EL_PZ_R, r);
        newEL.put(DBHelper.EL_PZ_I, i);
        db.insert(DBHelper.TABLE_ELEMENTS_PZ, null, newEL);
    }

    void setValues(EditText edit1, String val1, EditText edit2, String val2, EditText edit3, String val3) {
        edit1.setText(val1);
        edit2.setText(val2);
        edit3.setText(val3);
    }

    boolean isGoodR(CharSequence s) {
        String r = s.toString();
        boolean isGoodComma =  (!r.contains(",")) || ((r.indexOf(",") == r.lastIndexOf(",")) &&
                (r.indexOf(",") != 0) && (r.indexOf(",") != r.length() - 1));
        boolean isGoodSize = (!r.equals("")) && (r.length() < 7);
        return isGoodComma && isGoodSize &&
                (Double.parseDouble(r.replace(',','.')) != 0);
    }

    String getTok(CharSequence s) {
        Double r = Double.parseDouble(s.toString().replace(',','.'));
        return String.valueOf((int) (220 / r));
    }
}
