package com.example.protokol.create_protokol;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
        final SQLiteDatabase database = dbHelper.getWritableDatabase();
        Button updatebase = findViewById(R.id.button24);

        //НАСТРАИВАЕМ ACTIONBAR
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_main);

        //ОБНОВЛЕНИЕ БАЗЫ ДАННЫХ
        updatebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(false);
                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        database.delete(DBHelper.TABLE_ROOMS, null, null);
                        database.delete(DBHelper.TABLE_ELEMENTS, null, null);
                        database.delete(DBHelper.TABLE_LINE_ROOMS, null, null);
                        database.delete(DBHelper.TABLE_LINES, null, null);
                        database.delete(DBHelper.TABLE_GROUPS, null, null);
                        database.delete(DBHelper.TABLE_TITLE, null, null);
                        database.delete(DBHelper.TABLE_GD, null, null);
                    }
                });
                builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                builder.setMessage("Вы уверены, что хотите начать работу с новыми данными?");
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    public void titlePage (View view) {
        Intent intent = new Intent("android.intent.action.TitlePage");
        startActivity(intent);
    }


    public void groundingDevices (View view) {
        Intent intent = new Intent("android.intent.action.GroundingDevices");
        startActivity(intent);
    }

    public void roomElement (View view) {
        Intent intent = new Intent("android.intent.action.RoomElement");
        startActivity(intent);
    }

    public void insulation (View view) {
        Intent intent = new Intent("android.intent.action.Insulation");
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.libraries, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.libraries:
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setCancelable(false);
                alert.setTitle("Выберите библиотеку:");
                final String libararies[] = {"\nНазвания элементов\n", "\nМарки\n"};
                alert.setItems(libararies, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0) {
                            Intent intent = new Intent("android.intent.action.Library");
                            intent.putExtra("lib", "namesEl");
                            startActivity(intent);
                        }
                        if (which == 1) {
                            Intent intent = new Intent("android.intent.action.Library");
                            intent.putExtra("lib", "marks");
                            startActivity(intent);
                        }
                    }
                });
                alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                alert.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}