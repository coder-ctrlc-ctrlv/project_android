package com.example.protokol.create_protokol;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.annotation.Target;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 39;
    public static final String DATABLE_NAME = "contactDB";

    //ROOM_ELEMENT

    //table1
    public static final String TABLE_ROOMS = "rooms";
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "room";
    public static final String KEY_HEADER = "header";
    public static final String KEY_EMPTY_STRINGS = "strings";

    //table2
    public static final String TABLE_ELEMENTS = "elements";
    public static final String EL_ID = "_id";
    public static final String EL_NAME = "element";
    public static final String EL_NUMBER = "number";
    public static final String ROOM_ID = "room_id";
    public static final String EL_SOPR = "sopr";
    public static final String EL_CONCLUSION = "conclusion";

    //INSULATION

    //table3
    public static final String TABLE_LINE_ROOMS = "lnrooms";
    public static final String LNR_ID = "_id";
    public static final String LNR_NAME = "room";
    public static final String LNR_HEADER = "r_header";
    public static final String LNR_EMPTY_STRINGS = "r_strings";

    //table4
    public static final String TABLE_LINES = "lines";
    public static final String LN_ID = "_id";
    public static final String LN_NAME = "line";
    public static final String LN_ID_ROOM = "lnr_id";
    public static final String LN_HEADER = "l_header";
    public static final String LN_EMPTY_STRINGS = "l_strings";

    //table5
    public static final String TABLE_GROUPS = "groups";
    public static final String GR_LINE_ID = "grline_id";
    public static final String GR_ID = "_id";
    public static final String GR_NAME = "name_group";
    public static final String GR_U1 = "u1";
    public static final String GR_MARK = "mark";
    public static final String GR_VEIN = "vein";
    public static final String GR_SECTION = "section";
    public static final String GR_U2 = "u2";
    public static final String GR_R = "r";
    public static final String GR_PHASE = "phase";
    public static final String GR_A_B = "a_b";
    public static final String GR_B_C = "b_c";
    public static final String GR_C_A = "c_a";
    public static final String GR_A_N = "a_n";
    public static final String GR_B_N = "b_n";
    public static final String GR_C_N = "c_n";
    public static final String GR_A_PE = "a_pe";
    public static final String GR_B_PE = "b_pe";
    public static final String GR_C_PE = "c_pe";
    public static final String GR_N_PE = "n_pe";
    public static final String GR_CONCLUSION = "conclusion";

    //TITLE_PAGE

    //table6
    public static final String TABLE_TITLE = "title";
    public static final String TITLE_ID = "_id";
    public static final String TITLE_NAME_ELECTRO = "name_electro";
    public static final String TITLE_TARGET = "target";
    public static final String TITLE_ADDRESS = "address";
    public static final String TITLE_NUMBER_OF_PROTOKOL = "number_protokol";
    public static final String TITLE_DATE = "date";

    //GROUNDING_DEVICES

    //table7
    public static final String TABLE_GD = "grounding_devices";
    public static final String GD_DEVICE_ID = "_id";
    public static final String GD_RESULT_VIEW = "result_view";
    public static final String GD_GROUND = "ground";
    public static final String GD_CHARACTER_GROUND = "character";
    public static final String GD_U = "u_volt";
    public static final String GD_MODE_NEUTRAL = "neutral";
    public static final String GD_R = "r_om";

    //table8
    public static final String TABLE_MAIN_DEVICES = "main_devices";
    public static final String MD_DEVICE_ID = "_id";
    public static final String MD_PURPOSE = "purpose";
    public static final String MD_PLACE = "place";
    public static final String MD_DISTANCE = "distance";
    public static final String MD_R1 = "r1";
    public static final String MD_01L = "l1";
    public static final String MD_02L = "l2";
    public static final String MD_03L = "l3";
    public static final String MD_04L = "l4";
    public static final String MD_05L = "l5";
    public static final String MD_06L = "l6";
    public static final String MD_07L = "l7";
    public static final String MD_08L = "l8";
    public static final String MD_09L = "l9";
    public static final String MD_GRAPHICS = "graphic";
    public static final String MD_R2 = "r2";
    public static final String MD_K = "k";
    public static final String MD_R3 = "r3";
    public static final String MD_CONCLUSION = "conclusion";
    public static final String MD_NOTE = "note";

    public DBHelper(Context context) {
        super(context, DATABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_ROOMS + "(" + KEY_ID + " integer primary key AUTOINCREMENT," + KEY_NAME +
            " text," + KEY_HEADER + " text," + KEY_EMPTY_STRINGS + " integer" + ");");

        db.execSQL("create table " + TABLE_ELEMENTS + "(" + EL_ID + " integer primary key AUTOINCREMENT," + EL_NAME +
            " text," + EL_NUMBER + " text,"  + ROOM_ID + " integer," + EL_SOPR + " text," + EL_CONCLUSION + " text" + ");");

        db.execSQL("create table " + TABLE_LINE_ROOMS + "(" + LNR_ID + " integer primary key AUTOINCREMENT," + LNR_NAME +
            " text," + LNR_HEADER + " text," + LNR_EMPTY_STRINGS + " integer" + ");");

        db.execSQL("create table " + TABLE_LINES + "(" + LN_ID + " integer primary key AUTOINCREMENT," + LN_NAME +
            " text," + LN_ID_ROOM + " integer," + LN_HEADER + " text," + LN_EMPTY_STRINGS + " integer" + ");");

        db.execSQL("create table " + TABLE_GROUPS + "(" + GR_ID + " integer primary key AUTOINCREMENT," + GR_LINE_ID + " integer," + GR_NAME +
            " text," + GR_U1 + " text,"  + GR_MARK + " text," + GR_VEIN + " text," + GR_SECTION + " text," + GR_U2 + " text," + GR_R + " text," + GR_PHASE + " text," + GR_A_B + " text," +
            GR_B_C + " text," + GR_C_A + " text," + GR_A_N + " text," + GR_B_N + " text," + GR_C_N + " text," + GR_A_PE + " text," + GR_B_PE + " text," + GR_C_PE + " text," +
            GR_N_PE + " text," + GR_CONCLUSION + " text" + ");");

        db.execSQL("create table " + TABLE_TITLE + "(" + TITLE_ID + " integer primary key AUTOINCREMENT," + TITLE_NAME_ELECTRO + " text," + TITLE_TARGET + " text," + TITLE_ADDRESS + " text," +
            TITLE_NUMBER_OF_PROTOKOL + " text," + TITLE_DATE + " text" + ");");

        db.execSQL("create table " + TABLE_GD + "(" + GD_DEVICE_ID + " integer primary key AUTOINCREMENT," + GD_RESULT_VIEW + " text," + GD_GROUND + " text," + GD_CHARACTER_GROUND +
                " text," + GD_U + " text," + GD_MODE_NEUTRAL + " text," + GD_R + " text" + ");");

        db.execSQL("create table " + TABLE_MAIN_DEVICES + "(" + MD_DEVICE_ID + " integer primary key AUTOINCREMENT," + MD_PURPOSE +
                " text," + MD_PLACE + " text," + MD_DISTANCE + " text," + MD_R1 + " text," + MD_01L + " text," + MD_02L + " text," + MD_03L + " text," +
                MD_04L + " text," + MD_05L + " text," + MD_06L + " text," + MD_07L + " text," + MD_08L + " text," + MD_09L + " text," +
                MD_GRAPHICS + " text," + MD_R2 + " text," + MD_K + " text," + MD_R3 + " text," + MD_CONCLUSION + " text," + MD_NOTE + " text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_ROOMS);
        db.execSQL("drop table if exists " + TABLE_ELEMENTS);
        db.execSQL("drop table if exists " + TABLE_LINE_ROOMS);
        db.execSQL("drop table if exists " + TABLE_LINES);
        db.execSQL("drop table if exists " + TABLE_GROUPS);
        db.execSQL("drop table if exists " + TABLE_TITLE);
        db.execSQL("drop table if exists " + TABLE_GD);
        db.execSQL("drop table if exists " + TABLE_MAIN_DEVICES);
        onCreate(db);
    }
}
