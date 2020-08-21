package com.example.protokol.create_protokol;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class MenuItemsActivity extends AppCompatActivity {

    DBHelper dbHelper;
    String nameFile = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_items);

        dbHelper = new DBHelper(this);
        final SQLiteDatabase database = dbHelper.getWritableDatabase();

        LinearLayout title_layout = findViewById(R.id.title_layout);
        LinearLayout visual_inspection_layout = findViewById(R.id.visual_inspection_layout);
        LinearLayout insulation_layout = findViewById(R.id.insulation_layout);
        LinearLayout automatics_layout = findViewById(R.id.automatics_layout);
        LinearLayout dif_automatics_layout = findViewById(R.id.dif_automatics_layout);
        LinearLayout grounding_devices_layout = findViewById(R.id.grounding_devices_layout);
        LinearLayout room_element_layout = findViewById(R.id.room_element_layout);
        Button title_btn = findViewById(R.id.button1);
        Button visual_inspection_btn = findViewById(R.id.button2);
        Button insulation_btn = findViewById(R.id.button3);
        Button automatics_btn = findViewById(R.id.button4);
        Button dif_automatics_btn = findViewById(R.id.button5);
        Button grounding_devices_btn = findViewById(R.id.button6);
        Button room_element_btn = findViewById(R.id.button7);
        Button save_project_btn = findViewById(R.id.button8);

        String nameProject = "";
        LinearLayout[] allLayouts = {title_layout, visual_inspection_layout, insulation_layout,
                automatics_layout, dif_automatics_layout, grounding_devices_layout, room_element_layout};

        //СКРЫВАЕМ НЕНУЖНЫЕ ПУНКТЫ
        Cursor cursor1 = database.query(DBHelper.TABLE_PROJECT_INFO, new String[] {DBHelper.PROJECT_NAME, DBHelper.PROJECT_TITLE,
                DBHelper.PROJECT_VISUAL_INSPECTION, DBHelper.PROJECT_INSULATION, DBHelper.PROJECT_AUTOMATICS,
                DBHelper.PROJECT_DIF_AUTOMATICS, DBHelper.PROJECT_GROUNDING_DEVICES,
                DBHelper.PROJECT_ROOM_ELEMENT},null, null, null, null, null);
        if (cursor1.moveToFirst()) {
            int nameIndex = cursor1.getColumnIndex(DBHelper.PROJECT_NAME);
            int titleIndex = cursor1.getColumnIndex(DBHelper.PROJECT_TITLE);
            int visualInspectionIndex = cursor1.getColumnIndex(DBHelper.PROJECT_VISUAL_INSPECTION);
            int insulationIndex = cursor1.getColumnIndex(DBHelper.PROJECT_INSULATION);
            int automaticsIndex = cursor1.getColumnIndex(DBHelper.PROJECT_AUTOMATICS);
            int difAutomaticsIndex = cursor1.getColumnIndex(DBHelper.PROJECT_DIF_AUTOMATICS);
            int groundingDevicesIndex = cursor1.getColumnIndex(DBHelper.PROJECT_GROUNDING_DEVICES);
            int roomElementIndex = cursor1.getColumnIndex(DBHelper.PROJECT_ROOM_ELEMENT);
            nameProject = cursor1.getString(nameIndex);
            int[] valuesOfLayouts = {cursor1.getInt(titleIndex), cursor1.getInt(visualInspectionIndex),
                    cursor1.getInt(insulationIndex), cursor1.getInt(automaticsIndex), cursor1.getInt(difAutomaticsIndex),
                    cursor1.getInt(groundingDevicesIndex), cursor1.getInt(roomElementIndex)};
            for (int i = 0; i < valuesOfLayouts.length; ++i)
                if (valuesOfLayouts[i] == 0)
                    allLayouts[i].setVisibility(View.GONE);
        }
        cursor1.close();

        //НАСТРАИВАЕМ ACTIONBAR
        getSupportActionBar().setSubtitle("Главное меню");
        getSupportActionBar().setTitle(nameProject);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.TitlePage");
                startActivity(intent);
            }
        });

        visual_inspection_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        insulation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.Insulation1");
                startActivity(intent);
            }
        });

        automatics_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.Automatics1");
                startActivity(intent);
            }
        });

        dif_automatics_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.DifAutomatics1");
                startActivity(intent);
            }
        });

        room_element_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.RoomElement1");
                startActivity(intent);
            }
        });

        grounding_devices_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.GroundingDevices1");
                startActivity(intent);
            }
        });

        save_project_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProject(database);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.libraries, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(MenuItemsActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.libraries:
                AlertDialog.Builder alert = new AlertDialog.Builder(MenuItemsActivity.this);
                alert.setCancelable(false);
                alert.setTitle("Выберите библиотеку:");
                final String libararies[] = {"\nНазвания элементов\n", "\nМарки\n", "\nКомнаты\n", "\nЩиты\n", "\nЭтажи\n", "\nАвтомат. выкл.\n"};
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
                        if (which == 2) {
                            Intent intent = new Intent("android.intent.action.Library");
                            intent.putExtra("lib", "rooms");
                            startActivity(intent);
                        }
                        if (which == 3) {
                            Intent intent = new Intent("android.intent.action.Library");
                            intent.putExtra("lib", "lines");
                            startActivity(intent);
                        }
                        if (which == 4) {
                            Intent intent = new Intent("android.intent.action.Library");
                            intent.putExtra("lib", "floors");
                            startActivity(intent);
                        }
                        if (which == 5) {
                            Intent intent = new Intent("android.intent.action.Library");
                            intent.putExtra("lib", "automat");
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

    private void saveProject(final SQLiteDatabase database) {
        nameFile = "";
        Cursor cursor1 = database.query(DBHelper.TABLE_PROJECT_INFO, new String[] {DBHelper.PROJECT_NAME},
                null, null, null, null, null);
        if (cursor1.moveToFirst()) {
            int nameIndex = cursor1.getColumnIndex(DBHelper.PROJECT_NAME);
            nameFile = cursor1.getString(nameIndex);
        }
        cursor1.close();
        AlertDialog.Builder alert = new AlertDialog.Builder(MenuItemsActivity.this);
        final View myView = getLayoutInflater().inflate(R.layout.dialog_for_names,null);
        alert.setCancelable(false);
        alert.setTitle("Введите название сохраняемого файла:");
        final EditText input = myView.findViewById(R.id.editText);
        input.setText(nameFile);
        openKeyboard();
        alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                closeKeyboard(myView);
                nameFile = input.getText().toString();
                if (isCorrectInput(nameFile)) {
                    nameFile = nameFile.trim() + ".db";
                    copyFile();
                }
                else {
                    AlertDialog.Builder alert1 = new AlertDialog.Builder(MenuItemsActivity.this);
                    alert1.setCancelable(false);
                    alert1.setMessage("Некорректный ввод.\nПовторите попытку.");
                    alert1.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            saveProject(database);
                        }
                    });
                    alert1.show();
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

    private void copyFile() {
        String sd = Environment.getExternalStorageDirectory().toString();
        File data = Environment.getDataDirectory();
        String inputPath = "//data//com.example.protokol.create_protokol//databases//contactDB";
        InputStream in = null;
        OutputStream out = null;
        try {
            //СОЗДАДИМ ПАПКУ(output), ЕСЛИ ЕЕ НЕТ
            File folder = new File(sd, "Protokol_projects");
            if (!folder.exists())
                folder.mkdirs();
            File inputFile = new File(data, inputPath);
            File outputFile = new File(folder, nameFile);
            if (inputFile.exists()){
                in = new FileInputStream(inputFile);
                out = new FileOutputStream(outputFile);
                byte[] buffer = new byte[in.available()];
                int length;
                while ((length = in.read(buffer)) != -1) {
                    out.write(buffer, 0, length);
                }
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Данные были успешно сохранены", Toast.LENGTH_SHORT);
                toast.show();
            }
        } catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
    }

    boolean isCorrectInput(String data) {
        return !data.equals("") && data.matches("[0-9а-яА-Яa-zA-Z ]+") && data.trim().length() > 0;
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
