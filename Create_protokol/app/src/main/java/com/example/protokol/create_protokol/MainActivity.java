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
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    DBHelper dbHelper;
    String nameFile = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
        final SQLiteDatabase database = dbHelper.getWritableDatabase();

        final Button continue_project_btn = findViewById(R.id.button1);
        final Button create_project_btn = findViewById(R.id.button2);
        final Button load_project_btn = findViewById(R.id.button3);

        //НАСТРАИВАЕМ ACTIONBAR
        getSupportActionBar().setTitle("Protokol");

        //ПРОДОЛЖИТЬ ПРОЕКТ
        continue_project_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!getNameProject(database).equals("")) {
                    go_to_menu_items();
                }
                else {
                    AlertDialog.Builder alert2 = new AlertDialog.Builder(MainActivity.this);
                    alert2.setCancelable(false);
                    alert2.setMessage("Для начала необходимо создать проект");
                    alert2.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    });
                    alert2.show();
                }
            }
        });

        //СОЗДАТЬ ПРОЕКТ
        create_project_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nameProject = getNameProject(database);
                if (!nameProject.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setCancelable(false);
                    builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Boolean emptyItems = checkEmptyItems(database);
                            if (emptyItems) {
                                AlertDialog.Builder builder10 = new AlertDialog.Builder(MainActivity.this);
                                builder10.setCancelable(false);
                                builder10.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        saveAndCreateNew(database, nameProject);
                                    }
                                });
                                builder10.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                });
                                builder10.setMessage("Остались незаполненные пункты.\nВы уверены, что хотите продолжить?");
                                AlertDialog dialog10 = builder10.create();
                                dialog10.show();
                            }
                            else
                                saveAndCreateNew(database, nameProject);
                        }
                    });
                    builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            createNew(database);
                        }
                    });
                    builder.setNeutralButton("Отмена", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.setMessage("Сохранить текущий проект? Несохранённые данные будут потеряны.");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else {
                    createNew(database);
                }
            }
        });

        //ЗАГРУЗИТЬ ПРОЕКТ
        load_project_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sd = Environment.getExternalStorageDirectory().toString();
                final File data = Environment.getDataDirectory();
                final File folder = new File(sd, "Protokol_projects");
                File[] listOfFiles;
                if (!folder.exists())
                    folder.mkdirs();
                listOfFiles = folder.listFiles();
                if (listOfFiles.length == 0) {
                    AlertDialog.Builder alert2 = new AlertDialog.Builder(MainActivity.this);
                    alert2.setCancelable(false);
                    alert2.setMessage("Сохраненные проекты отсутствуют");
                    alert2.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    });
                    alert2.show();
                }
                else {
                    int i;
                    final String[] arrayNameFiles = new String[listOfFiles.length];
                    for (i = 0; i < listOfFiles.length; i++) {
                        arrayNameFiles[i] = listOfFiles[i].getName();
                    }
                    AlertDialog.Builder alert1 = new AlertDialog.Builder(MainActivity.this);
                    alert1.setCancelable(false);
                    alert1.setTitle("Выберите файл:");
                    alert1.setItems(arrayNameFiles, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String outputPath = "//data//com.example.protokol.create_protokol//databases//contactDB";
                            InputStream in = null;
                            OutputStream out = null;
                            try {
                                File outputFile = new File(data, outputPath);
                                File inputFile = new File(folder, arrayNameFiles[which]);
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
                                            "Данные были успешно импортированы", Toast.LENGTH_SHORT);
                                    toast.show();
                                    go_to_menu_items();
                                }
                            } catch (FileNotFoundException fnfe1) {
                                Log.e("tag", fnfe1.getMessage());
                            } catch (Exception e) {
                                Log.e("tag", e.getMessage());
                            }
                        }
                    });
                    alert1.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    });
                    alert1.show();
                }
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
            case R.id.libraries:
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setCancelable(false);
                alert.setTitle("Выберите библиотеку:");
                final String libararies[] = {"\nНазвания элементов\n", "\nМарки\n", "\nПомещения\n", "\nЩиты\n", "\nЭтажи\n", "\nАвтомат. выкл.\n"};
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

    private void createNew(final SQLiteDatabase database) {
        AlertDialog.Builder alert5 = new AlertDialog.Builder(MainActivity.this);
        final View myView = getLayoutInflater().inflate(R.layout.dialog_for_names,null);
        alert5.setCancelable(false);
        alert5.setTitle("Введите название нового проекта:");
        final EditText input = myView.findViewById(R.id.editText);
        openKeyboard();
        alert5.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                closeKeyboard(myView);
                String nameProject = input.getText().toString();
                if (isCorrectInput(nameProject)) {
                    database.delete(DBHelper.TABLE_PROJECT_INFO, null, null);
                    database.delete(DBHelper.TABLE_FLOORS, null, null);
                    database.delete(DBHelper.TABLE_ROOMS, null, null);
                    database.delete(DBHelper.TABLE_ELEMENTS, null, null);
                    database.delete(DBHelper.TABLE_ELEMENTS_PZ, null, null);
                    database.delete(DBHelper.TABLE_INS_FLOORS, null, null);
                    database.delete(DBHelper.TABLE_LINE_ROOMS, null, null);
                    database.delete(DBHelper.TABLE_LINES, null, null);
                    database.delete(DBHelper.TABLE_GROUPS, null, null);
                    database.delete(DBHelper.TABLE_INS_NOTES, null, null);
                    database.delete(DBHelper.TABLE_TITLE, null, null);
                    database.delete(DBHelper.TABLE_GD, null, null);
                    database.delete(DBHelper.TABLE_AU_FLOORS, null, null);
                    database.delete(DBHelper.TABLE_AU_LINES, null, null);
                    database.delete(DBHelper.TABLE_AU_ROOMS, null, null);
                    database.delete(DBHelper.TABLE_AUTOMATICS, null, null);
                    database.delete(DBHelper.TABLE_DIF_AU_FLOORS, null, null);
                    database.delete(DBHelper.TABLE_DIF_AU_LINES, null, null);
                    database.delete(DBHelper.TABLE_DIF_AU_ROOMS, null, null);
                    database.delete(DBHelper.TABLE_DIF_AUTOMATICS, null, null);
                    database.delete(DBHelper.TABLE_PZ_FLOORS, null, null);
                    database.delete(DBHelper.TABLE_PZ_LINES, null, null);
                    database.delete(DBHelper.TABLE_PZ_ROOMS, null, null);
                    database.delete(DBHelper.TABLE_PZ, null, null);
                    go_to_content_selection(nameProject.trim());
                }
                else {
                    AlertDialog.Builder alert1 = new AlertDialog.Builder(MainActivity.this);
                    alert1.setCancelable(false);
                    alert1.setMessage("Некорректный ввод.\nПовторите попытку.");
                    alert1.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            createNew(database);
                        }
                    });
                    alert1.show();
                }
            }
        });
        alert5.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                closeKeyboard(myView);
            }
        });
        alert5.setView(myView);
        alert5.show();
    }

    private void saveAndCreateNew(final SQLiteDatabase database, final String nameProj) {
        nameFile = nameProj;
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
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
                    createNew(database);
                }
                else {
                    AlertDialog.Builder alert1 = new AlertDialog.Builder(MainActivity.this);
                    alert1.setCancelable(false);
                    alert1.setMessage("Некорректный ввод.\nПовторите попытку.");
                    alert1.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            saveAndCreateNew(database, nameProj);
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

    boolean checkEmptyItems(SQLiteDatabase db) {
        boolean res = false;
        final int[] valuesOfItems = new int[7];
        String[] array_queries_for_items = {
                "SELECT * FROM title LIMIT 1",
                "",
                "SELECT * FROM groups LIMIT 1",
                "SELECT * FROM automatics LIMIT 1",
                "SELECT * FROM dif_automatics LIMIT 1",
                "SELECT * FROM grounding_devices LIMIT 1",
                "SELECT * FROM elements LIMIT 1"
        };
        Cursor cursor1 = db.query(DBHelper.TABLE_PROJECT_INFO, new String[] {DBHelper.PROJECT_TITLE,
                DBHelper.PROJECT_VISUAL_INSPECTION, DBHelper.PROJECT_INSULATION, DBHelper.PROJECT_AUTOMATICS,
                DBHelper.PROJECT_DIF_AUTOMATICS, DBHelper.PROJECT_GROUNDING_DEVICES,
                DBHelper.PROJECT_ROOM_ELEMENT},null, null, null, null, null);
        if (cursor1.moveToFirst()) {
            int titleIndex = cursor1.getColumnIndex(DBHelper.PROJECT_TITLE);
            int visualInspectionIndex = cursor1.getColumnIndex(DBHelper.PROJECT_VISUAL_INSPECTION);
            int insulationIndex = cursor1.getColumnIndex(DBHelper.PROJECT_INSULATION);
            int automaticsIndex = cursor1.getColumnIndex(DBHelper.PROJECT_AUTOMATICS);
            int difAutomaticsIndex = cursor1.getColumnIndex(DBHelper.PROJECT_DIF_AUTOMATICS);
            int groundingDevicesIndex = cursor1.getColumnIndex(DBHelper.PROJECT_GROUNDING_DEVICES);
            int roomElementIndex = cursor1.getColumnIndex(DBHelper.PROJECT_ROOM_ELEMENT);
            valuesOfItems[0] = cursor1.getInt(titleIndex);
            valuesOfItems[1] = cursor1.getInt(visualInspectionIndex);
            valuesOfItems[2] = cursor1.getInt(insulationIndex);
            valuesOfItems[3] = cursor1.getInt(automaticsIndex);
            valuesOfItems[4] = cursor1.getInt(difAutomaticsIndex);
            valuesOfItems[5] = cursor1.getInt(groundingDevicesIndex);
            valuesOfItems[6] = cursor1.getInt(roomElementIndex);
        }
        cursor1.close();
        for (int i = 0; i < valuesOfItems.length; ++i)
            if ((i != 1) && (valuesOfItems[i] == 1) && queryIsEmpty(db, array_queries_for_items[i])) {
                res = true;
                break;
            }
        return res;
    }

    boolean queryIsEmpty(SQLiteDatabase database, String query) {
        Boolean isEmpty = true;
        Cursor cursor10 = database.rawQuery(query, new String[] { });
        if (cursor10.moveToFirst()) {
            isEmpty = false;
        }
        cursor10.close();
        return isEmpty;
    }

    String getNameProject(SQLiteDatabase database) {
        String name = "";
        Cursor cursor1 = database.query(DBHelper.TABLE_PROJECT_INFO, new String[] {DBHelper.PROJECT_NAME},
                null, null, null, null, null);
        if (cursor1.moveToFirst()) {
            int nameIndex = cursor1.getColumnIndex(DBHelper.PROJECT_NAME);
            name = cursor1.getString(nameIndex);
        }
        cursor1.close();
        return name;
    }

    boolean isCorrectInput(String data) {
        return !data.equals("") && data.matches("[0-9а-яА-Яa-zA-Z ]+") && data.trim().length() > 0;
    }

    void go_to_menu_items() {
        Intent intent = new Intent("android.intent.action.MenuItems");
        startActivity(intent);
    }

    void go_to_content_selection(String name) {
        Intent intent = new Intent("android.intent.action.ContentSelection");
        intent.putExtra("nameProject", name);
        startActivity(intent);
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