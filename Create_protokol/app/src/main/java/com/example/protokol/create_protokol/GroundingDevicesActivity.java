package com.example.protokol.create_protokol;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GroundingDevicesActivity extends AppCompatActivity {

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grounding_devices);

        dbHelper = new DBHelper(this);
        final SQLiteDatabase database = dbHelper.getWritableDatabase();

        final ListView devices = findViewById(R.id.Devices);
        Button addDevice = findViewById(R.id.button34);
        Button addInfo = findViewById(R.id.button33);

        //НАСТРАИВАЕМ ACTIONBAR
        getSupportActionBar().setSubtitle("Заземлители");
        getSupportActionBar().setTitle("Заземл. и заз. устр-ва");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ЗАПОЛНЯЕМ LISTVIEW
        addSpisokDevices(database, devices);

        //ПОСМОТРЕТЬ, ИЗМЕНИТЬ И УДАЛИТЬ ЗАЗЕМЛИТЕЛЬ
        devices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, final long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(GroundingDevicesActivity.this);
                alert.setTitle(((TextView) view).getText());
                String arrayMenu[] = {"\nПосмотреть\n", "\nРедактировать\n", "\nУдалить заземлитель\n"};
                alert.setItems(arrayMenu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //ЗАПРОС В БД ДЛЯ ПОЛУЧЕНИЯ ID НУЖНОГО ЗАЗЕМЛИТЕЛЯ
                        Cursor cursor4 = database.query(DBHelper.TABLE_MAIN_DEVICES, new String[] {DBHelper.MD_DEVICE_ID}, null, null, null, null, null);
                        cursor4.moveToPosition(position);
                        int device_idIndex = cursor4.getColumnIndex(DBHelper.MD_DEVICE_ID);
                        final int device_id = cursor4.getInt(device_idIndex);
                        cursor4.close();

                        //ПОСМОТРЕТЬ
                        if (which == 0) {
                            String info[] = {"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};
                            //ПОЛУЧЕНИЕ ИНФОРМАЦИИ ОБ ЭЛЕМЕНТЕ
                            Cursor cursor1 = database.query(DBHelper.TABLE_MAIN_DEVICES, new String[] {DBHelper.MD_DEVICE_ID, DBHelper.MD_PURPOSE,
                                    DBHelper.MD_PLACE, DBHelper.MD_DISTANCE, DBHelper.MD_R1, DBHelper.MD_01L, DBHelper.MD_02L, DBHelper.MD_03L,
                                    DBHelper.MD_04L, DBHelper.MD_05L, DBHelper.MD_06L, DBHelper.MD_07L, DBHelper.MD_08L, DBHelper.MD_09L,
                                    DBHelper.MD_GRAPHICS, DBHelper.MD_R2, DBHelper.MD_K, DBHelper.MD_R3,
                                    DBHelper.MD_CONCLUSION, DBHelper.MD_NOTE}, "_id = ?", new String[] {String.valueOf(device_id)}, null, null, null);
                            if (cursor1.moveToFirst()) {
                                int purposeIndex = cursor1.getColumnIndex(DBHelper.MD_PURPOSE);
                                int placeIndex = cursor1.getColumnIndex(DBHelper.MD_PLACE);
                                int distanceIndex = cursor1.getColumnIndex(DBHelper.MD_DISTANCE);
                                int r1Index = cursor1.getColumnIndex(DBHelper.MD_R1);
                                int l1Index = cursor1.getColumnIndex(DBHelper.MD_01L);
                                int l2Index = cursor1.getColumnIndex(DBHelper.MD_02L);
                                int l3Index = cursor1.getColumnIndex(DBHelper.MD_03L);
                                int l4Index = cursor1.getColumnIndex(DBHelper.MD_04L);
                                int l5Index = cursor1.getColumnIndex(DBHelper.MD_05L);
                                int l6Index = cursor1.getColumnIndex(DBHelper.MD_06L);
                                int l7Index = cursor1.getColumnIndex(DBHelper.MD_07L);
                                int l8Index = cursor1.getColumnIndex(DBHelper.MD_08L);
                                int l9Index = cursor1.getColumnIndex(DBHelper.MD_09L);
                                int graphicsIndex = cursor1.getColumnIndex(DBHelper.MD_GRAPHICS);
                                int r2Index = cursor1.getColumnIndex(DBHelper.MD_R2);
                                int kIndex = cursor1.getColumnIndex(DBHelper.MD_K);
                                int r3Index = cursor1.getColumnIndex(DBHelper.MD_R3);
                                int resultIndex = cursor1.getColumnIndex(DBHelper.MD_CONCLUSION);
                                int noteIndex = cursor1.getColumnIndex(DBHelper.MD_NOTE);
                                do {
                                    info[0] = cursor1.getString(purposeIndex);
                                    info[1] = cursor1.getString(placeIndex);
                                    info[2] = cursor1.getString(distanceIndex);
                                    info[3] = cursor1.getString(r1Index);
                                    info[4] = cursor1.getString(l1Index);
                                    info[5] = cursor1.getString(l2Index);
                                    info[6] = cursor1.getString(l3Index);
                                    info[7] = cursor1.getString(l4Index);
                                    info[8] = cursor1.getString(l5Index);
                                    info[9] = cursor1.getString(l6Index);
                                    info[10] = cursor1.getString(l7Index);
                                    info[11] = cursor1.getString(l8Index);
                                    info[12] = cursor1.getString(l9Index);
                                    info[13] = cursor1.getString(graphicsIndex);
                                    info[14] = cursor1.getString(r2Index);
                                    info[15] = cursor1.getString(kIndex);
                                    info[16] = cursor1.getString(r3Index);
                                    info[17] = cursor1.getString(resultIndex);
                                    info[18] = cursor1.getString(noteIndex);
                                } while (cursor1.moveToNext());
                            }
                            cursor1.close();
                            AlertDialog.Builder builder4 = new AlertDialog.Builder(GroundingDevicesActivity.this);
                            builder4.setCancelable(false);
                            builder4.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                }
                            });
                            builder4.setMessage("Назначение заземлителя: " + info[0] + "\n" + "Место измерения: " + info[1] + "\n" +
                                    "Расстояние до токового электрода: " + info[2] + "\n" + "Допустимое R: " + info[3] + "\n" + "0,1 L: " + info[4] + "\n" +
                                    "0,2 L: " + info[5] + "\n" + "0,3 L: " + info[6] + "\n" + "0,4 L: " + info[7] + "\n" +
                                    "0,5 L: " + info[8] + "\n" + "0,6 L: " + info[9] + "\n" + "0,7 L: " + info[10] + "\n" +
                                    "0,8 L: " + info[11] + "\n" + "0,9 L: " + info[12] + "\n" + "Доп. расчеты, графики: " + info[13] + "\n" +
                                    "Принятое R: " + info[14] + "\n" + "Коэффициент поправочн.: " + info[15] + "\n" + "Приведенное R c учетом коэффициента: " + info[16] + "\n" +
                                    "Вывод: " + info[17] + "\n" + "Примечание: " + info[18] + "\n");
                            builder4.setTitle(((TextView) view).getText());
                            AlertDialog dialog4 = builder4.create();
                            dialog4.show();
                        }

                        //ИЗМЕНИТЬ
                        if (which == 1) {
                            Intent intent = new Intent("android.intent.action.GroundingDevices2");
                            intent.putExtra("device_id", device_id);
                            startActivity(intent);
                        }

                        //УДАЛИТЬ ГРУППУ
                        if (which == 2) {

                            //ПОДТВЕРЖДЕНИЕ
                            AlertDialog.Builder builder4 = new AlertDialog.Builder(GroundingDevicesActivity.this);
                            builder4.setCancelable(false);
                            builder4.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    database.delete(DBHelper.TABLE_MAIN_DEVICES, "_id = ?", new String[] {String.valueOf(device_id)});
                                    //ЗАПРОС В БД И ЗАПОЛНЕНИЕ СПИСКА ГРУПП
                                    addSpisokDevices(database, devices);
                                    Toast toast2 = Toast.makeText(getApplicationContext(),
                                            "Заземлитель удален", Toast.LENGTH_SHORT);
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

        //ПЕРЕХОД К ДОБАВЛЕНИЮ ЗАЗЕМЛИТЕЛЯ
        addDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.GroundingDevices2");
                startActivity(intent);
            }
        });

        //ПЕРЕХОД К ДОБАВЛЕНИЮ ДОП ИНФОРМАЦИИ
        addInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.GroundingDevices3");
                startActivity(intent);
            }
        });
    }

    //НАЗАД
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(GroundingDevicesActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addSpisokDevices(SQLiteDatabase database, ListView devices) {
        final ArrayList<String> spisokDevices = new ArrayList <String>();
        Cursor cursor = database.query(DBHelper.TABLE_MAIN_DEVICES, new String[] {DBHelper.MD_PURPOSE, DBHelper.MD_PLACE, DBHelper.MD_R1, DBHelper.MD_R2}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int purposeIndex = cursor.getColumnIndex(DBHelper.MD_PURPOSE);
            int placeIndex = cursor.getColumnIndex(DBHelper.MD_PLACE);
            int r1Index = cursor.getColumnIndex(DBHelper.MD_R1);
            int r2Index = cursor.getColumnIndex(DBHelper.MD_R2);
            do {
                spisokDevices.add(cursor.getString(purposeIndex) + " назначение\n(" + cursor.getString(placeIndex) + ";  допустимое R - " + cursor.getString(r1Index) + ";  принятое R - " + cursor.getString(r2Index) + ")");
            } while (cursor.moveToNext());
        }
        cursor.close();
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, spisokDevices);
        devices.setAdapter(adapter);
    }
}
