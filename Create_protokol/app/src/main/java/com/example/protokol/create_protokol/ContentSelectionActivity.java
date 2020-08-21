package com.example.protokol.create_protokol;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.ContentValues;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ContentSelectionActivity extends AppCompatActivity {

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_selection);

        dbHelper = new DBHelper(this);
        final SQLiteDatabase database = dbHelper.getWritableDatabase();

        final CheckBox title_check = findViewById(R.id.title);
        LinearLayout visual_inspection_layout = findViewById(R.id.visual_inspection_layout);
        final CheckBox visual_inspection_check = findViewById(R.id.visual_inspection);
        LinearLayout insulation_layout = findViewById(R.id.insulation_layout);
        final CheckBox insulation_check = findViewById(R.id.insulation);
        LinearLayout automatics_layout = findViewById(R.id.automatics_layout);
        final CheckBox automatics_check = findViewById(R.id.automatics);
        LinearLayout dif_automatics_layout = findViewById(R.id.dif_automatics_layout);
        final CheckBox dif_automatics_check = findViewById(R.id.dif_automatics);
        LinearLayout grounding_devices_layout = findViewById(R.id.grounding_devices_layout);
        final CheckBox grounding_devices_check = findViewById(R.id.grounding_devices);
        LinearLayout room_element_layout = findViewById(R.id.room_element_layout);
        final CheckBox room_element_check = findViewById(R.id.room_element);
        Button next_btn = findViewById(R.id.next_btn);

        final String nameProject = getIntent().getStringExtra("nameProject");

        //НАСТРАИВАЕМ ACTIONBAR
        getSupportActionBar().setSubtitle("Создание проекта");
        getSupportActionBar().setTitle(nameProject);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title_check.setChecked(true);
            }
        });

        visual_inspection_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visual_inspection_check.setChecked(!visual_inspection_check.isChecked());
            }
        });

        insulation_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insulation_check.setChecked(!insulation_check.isChecked());
            }
        });

        automatics_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                automatics_check.setChecked(!automatics_check.isChecked());
            }
        });

        dif_automatics_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dif_automatics_check.setChecked(!dif_automatics_check.isChecked());
            }
        });

        grounding_devices_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grounding_devices_check.setChecked(!grounding_devices_check.isChecked());
            }
        });

        room_element_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                room_element_check.setChecked(!room_element_check.isChecked());
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues newProject = new ContentValues();
                newProject.put(DBHelper.PROJECT_NAME, nameProject);
                newProject.put(DBHelper.PROJECT_TITLE, convertBoolToInt(title_check.isChecked()));
                newProject.put(DBHelper.PROJECT_VISUAL_INSPECTION, convertBoolToInt(visual_inspection_check.isChecked()));
                newProject.put(DBHelper.PROJECT_INSULATION, convertBoolToInt(insulation_check.isChecked()));
                newProject.put(DBHelper.PROJECT_AUTOMATICS, convertBoolToInt(automatics_check.isChecked()));
                newProject.put(DBHelper.PROJECT_DIF_AUTOMATICS, convertBoolToInt(dif_automatics_check.isChecked()));
                newProject.put(DBHelper.PROJECT_GROUNDING_DEVICES, convertBoolToInt(grounding_devices_check.isChecked()));
                newProject.put(DBHelper.PROJECT_ROOM_ELEMENT, convertBoolToInt(room_element_check.isChecked()));
                database.insert(DBHelper.TABLE_PROJECT_INFO, null, newProject);
                Intent intent = new Intent("android.intent.action.MenuItems");
                startActivity(intent);
            }
        });
    }

    //НАЗАД
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(ContentSelectionActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    int convertBoolToInt(boolean x){
        return x ? 1 : 0;
    }
}

