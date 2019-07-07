package com.example.protokol.create_protokol;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
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
        final Button updatebase = findViewById(R.id.button24);

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
                        database.delete(DBHelper.TABLE_FLOORS, null, null);
                        database.delete(DBHelper.TABLE_ROOMS, null, null);
                        database.delete(DBHelper.TABLE_ELEMENTS, null, null);
                        database.delete(DBHelper.TABLE_LINE_ROOMS, null, null);
                        database.delete(DBHelper.TABLE_LINES, null, null);
                        database.delete(DBHelper.TABLE_GROUPS, null, null);
                        database.delete(DBHelper.TABLE_TITLE, null, null);
                        database.delete(DBHelper.TABLE_GD, null, null);
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Данные были успешно удалены", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
                builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                builder.setMessage("Вы уверены, что хотите удалить данные?\nНесохраненные данные будут потеряны.");
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
        Intent intent = new Intent("android.intent.action.RoomElement0");
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
                final String libararies[] = {"\nНазвания элементов\n", "\nМарки\n", "\nКомнаты\n", "\nЩиты\n", "\nЭтажи\n"};
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
                    }
                });
                alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                alert.show();
                return true;
            case R.id.loadBD:
                String sd = Environment.getExternalStorageDirectory().toString();
                final File data = Environment.getDataDirectory();
                final File folder = new File(sd, "Базы данных");
                File[] listOfFiles;
                if (!folder.exists())
                    folder.mkdirs();
                listOfFiles = folder.listFiles();
                if (listOfFiles.length == 0) {
                    AlertDialog.Builder alert2 = new AlertDialog.Builder(MainActivity.this);
                    alert2.setCancelable(false);
                    alert2.setMessage("Файлы с базой данных отсутствуют");
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
                return true;
            case R.id.saveBD:
                getNameFileAndCopy();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getNameFileAndCopy() {
        nameFile = "";
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        final View myView = getLayoutInflater().inflate(R.layout.dialog_for_names,null);
        alert.setCancelable(false);
        alert.setTitle("Введите название сохраняемого файла:");
        final EditText input = myView.findViewById(R.id.editText);
        //ОТКРЫВАЕМ КЛАВИАТУРУ
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
        alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //СКРЫВАЕМ КЛАВИАТУРУ
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(myView.getWindowToken(),0);
                nameFile = input.getText().toString();
                if (nameFile.equals("")) {
                    AlertDialog.Builder alert1 = new AlertDialog.Builder(MainActivity.this);
                    alert1.setCancelable(false);
                    alert1.setMessage("Поле ввода оказалось пустым!\nЕсли хотите, чтобы данные сохранились, введите название файла и повторите попытку.");
                    alert1.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            getNameFileAndCopy();
                        }
                    });
                    alert1.show();
                }
                else
                    if (!nameFile.matches("[0-9а-яА-Яa-zA-Z]+")){
                        AlertDialog.Builder alert1 = new AlertDialog.Builder(MainActivity.this);
                        alert1.setCancelable(false);
                        alert1.setMessage("Вы можете вводить только цифры и буквы русского или английского алфавитов.\nЕсли хотите, чтобы данные сохранились, повторите попытку с корректным названием.");
                        alert1.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                getNameFileAndCopy();
                            }
                        });
                        alert1.show();
                    } else {
                        nameFile = nameFile + ".db";
                        copyFile();
                    }
            }
        });
        alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //СКРЫВАЕМ КЛАВИАТУРУ
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(myView.getWindowToken(),0);
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
            File folder = new File(sd, "Базы данных");
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
}