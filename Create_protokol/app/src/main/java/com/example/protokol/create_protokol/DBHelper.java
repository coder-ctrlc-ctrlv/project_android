package com.example.protokol.create_protokol;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.annotation.Target;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 63;
    public static final String DATABLE_NAME = "contactDB";

    //ROOM_ELEMENT

    //table1
    public static final String TABLE_FLOORS = "floors";
    public static final String FL_ID = "_id";
    public static final String FL_NAME = "floor";

    //table2
    public static final String TABLE_ROOMS = "rooms";
    public static final String KEY_ID = "_id";
    public static final String KEY_ID_FLOOR = "rfl_id";
    public static final String KEY_NAME = "room";

    //table3
    public static final String TABLE_ELEMENTS = "elements";
    public static final String EL_ID = "_id";
    public static final String EL_NAME = "element";
    public static final String EL_NUMBER = "number";
    public static final String ROOM_ID = "room_id";
    public static final String EL_SOPR = "sopr";
    public static final String EL_CONCLUSION = "conclusion";

    //INSULATION

    //table4
    public static final String TABLE_LINE_ROOMS = "lnrooms";
    public static final String LNR_ID = "_id";
    public static final String LNR_NAME = "room";

    //table5
    public static final String TABLE_LINES = "lines";
    public static final String LN_ID = "_id";
    public static final String LN_NAME = "line";
    public static final String LN_ID_ROOM = "lnr_id";

    //table6
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

    //table7
    public static final String TABLE_TITLE = "title";
    public static final String TITLE_ID = "_id";
    public static final String TITLE_NAME_ELECTRO = "name_electro";
    public static final String TITLE_TARGET = "target";
    public static final String TITLE_ADDRESS = "address";
    public static final String TITLE_NUMBER_OF_PROTOKOL = "number_protokol";
    public static final String TITLE_DATE = "date";
    public static final String TITLE_TEMPERATURE = "temperature";
    public static final String TITLE_HUMIDITY = "humidity";
    public static final String TITLE_PRESSURE = "pressure";

    //GROUNDING_DEVICES

    //table8
    public static final String TABLE_GD = "grounding_devices";
    public static final String GD_DEVICE_ID = "_id";
    //Global data
    public static final String GD_RESULT_VIEW = "result_view";
    public static final String GD_GROUND = "ground";
    public static final String GD_CHARACTER_GROUND = "character";
    public static final String GD_U = "u_volt";
    public static final String GD_MODE_NEUTRAL = "neutral";
    public static final String GD_R = "r_om";
    public static final String GD_TEMPERATURE = "temperature";
    public static final String GD_HUMIDITY = "humidity";
    public static final String GD_PRESSURE = "pressure";
    //Local data
    public static final String GD_PURPOSE = "purpose";
    public static final String GD_PLACE = "place";
    public static final String GD_DISTANCE = "distance";
    public static final String GD_R1 = "r1";
    public static final String GD_01L = "l1";
    public static final String GD_02L = "l2";
    public static final String GD_03L = "l3";
    public static final String GD_04L = "l4";
    public static final String GD_05L = "l5";
    public static final String GD_06L = "l6";
    public static final String GD_07L = "l7";
    public static final String GD_08L = "l8";
    public static final String GD_09L = "l9";
    public static final String GD_GRAPHICS = "graphic";
    public static final String GD_R2 = "r2";
    public static final String GD_K = "k";
    public static final String GD_R3 = "r3";
    public static final String GD_CONCLUSION = "conclusion";
    public static final String GD_NOTE = "note";

    //LIBRARY_NAMES_EL

    //table9
    public static final String TABLE_NAMES_EL = "names_el";
    public static final String NAME_EL_ID = "_id";
    public static final String NAME_EL = "name_el";

    //LIBRARY_MARKS

    //table10
    public static final String TABLE_MARKS = "marks";
    public static final String MARK_ID = "_id";
    public static final String MARK = "mark";

    //LIBRARY_ROOMS

    //table11
    public static final String TABLE_LIBRARY_ROOMS = "lib_rooms";
    public static final String LIB_ROOM_ID = "_id";
    public static final String LIB_ROOM_NAME = "room_name";

    //LIBRARY_LINES

    //table12
    public static final String TABLE_LIBRARY_LINES = "lib_lines";
    public static final String LIB_LINE_ID = "_id";
    public static final String LIB_LINE_NAME = "line_name";

    //LIBRARY_FLOORS

    //table13
    public static final String TABLE_LIBRARY_FLOORS = "lib_floors";
    public static final String LIB_FLOOR_ID = "_id";
    public static final String LIB_FLOOR_NAME = "floor_name";

    public DBHelper(Context context) {
        super(context, DATABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_FLOORS + "(" + FL_ID + " integer primary key AUTOINCREMENT," + FL_NAME +
                " text" + ");");

        db.execSQL("create table " + TABLE_ROOMS + "(" + KEY_ID + " integer primary key AUTOINCREMENT," + KEY_ID_FLOOR + " integer," + KEY_NAME +
            " text" + ");");

        db.execSQL("create table " + TABLE_ELEMENTS + "(" + EL_ID + " integer primary key AUTOINCREMENT," + EL_NAME +
            " text," + EL_NUMBER + " text,"  + ROOM_ID + " integer," + EL_SOPR + " text," + EL_CONCLUSION + " text" + ");");

        db.execSQL("create table " + TABLE_LINE_ROOMS + "(" + LNR_ID + " integer primary key AUTOINCREMENT," + LNR_NAME +
            " text" + ");");

        db.execSQL("create table " + TABLE_LINES + "(" + LN_ID + " integer primary key AUTOINCREMENT," + LN_NAME +
            " text," + LN_ID_ROOM + " integer" + ");");

        db.execSQL("create table " + TABLE_GROUPS + "(" + GR_ID + " integer primary key AUTOINCREMENT," + GR_LINE_ID +
            " integer," + GR_NAME + " text," + GR_U1 + " text,"  + GR_MARK + " text," + GR_VEIN + " text," + GR_SECTION +
            " text," + GR_U2 + " text," + GR_R + " text," + GR_PHASE + " text," + GR_A_B + " text," + GR_B_C +
            " text," + GR_C_A + " text," + GR_A_N + " text," + GR_B_N + " text," + GR_C_N + " text," + GR_A_PE +
            " text," + GR_B_PE + " text," + GR_C_PE + " text," + GR_N_PE + " text," + GR_CONCLUSION + " text" + ");");

        db.execSQL("create table " + TABLE_TITLE + "(" + TITLE_ID + " integer primary key AUTOINCREMENT," + TITLE_NAME_ELECTRO +
            " text," + TITLE_TARGET + " text," + TITLE_ADDRESS + " text," + TITLE_NUMBER_OF_PROTOKOL + " text," + TITLE_DATE +
            " text," + TITLE_TEMPERATURE + " text," + TITLE_HUMIDITY + " text," + TITLE_PRESSURE + " text" + ");");

        db.execSQL("create table " + TABLE_GD + "(" + GD_DEVICE_ID + " integer primary key AUTOINCREMENT," + GD_RESULT_VIEW +
            " text," + GD_GROUND + " text," + GD_CHARACTER_GROUND + " text," + GD_U + " text," + GD_MODE_NEUTRAL +
            " text," + GD_R + " text," + GD_PURPOSE + " text," + GD_PLACE + " text," + GD_DISTANCE + " text," + GD_R1 +
            " text," + GD_01L + " text," + GD_02L + " text," + GD_03L + " text," + GD_04L + " text," + GD_05L +
            " text," + GD_06L + " text," + GD_07L + " text," + GD_08L + " text," + GD_09L + " text," + GD_GRAPHICS +
            " text," + GD_R2 + " text," + GD_K + " text," + GD_R3 + " text," + GD_CONCLUSION + " text," + GD_NOTE +
            " text," + GD_TEMPERATURE + " text," + GD_HUMIDITY + " text," + GD_PRESSURE + " text" + ");");

        db.execSQL("create table if not exists " + TABLE_NAMES_EL + "(" + NAME_EL_ID + " integer primary key AUTOINCREMENT," + NAME_EL + " text" + ");");
//        db.execSQL("insert into " + TABLE_NAMES_EL + "(" + NAME_EL + ")" + " values " + "('Розетка с з.к.');");
//        db.execSQL("insert into " + TABLE_NAMES_EL + "(" + NAME_EL + ")" + " values " + "('Розетка без з.к.');");
//        db.execSQL("insert into " + TABLE_NAMES_EL + "(" + NAME_EL + ")" + " values " + "('Системный блок');");
//        db.execSQL("insert into " + TABLE_NAMES_EL + "(" + NAME_EL + ")" + " values " + "('Сетевой фильтр');");
//        db.execSQL("insert into " + TABLE_NAMES_EL + "(" + NAME_EL + ")" + " values " + "('Удлинитель с з.к.');");
//        db.execSQL("insert into " + TABLE_NAMES_EL + "(" + NAME_EL + ")" + " values " + "('Удлинитель без з.к.');");
//        db.execSQL("insert into " + TABLE_NAMES_EL + "(" + NAME_EL + ")" + " values " + "('Принтер');");
//        db.execSQL("insert into " + TABLE_NAMES_EL + "(" + NAME_EL + ")" + " values " + "('МФУ');");
//        db.execSQL("insert into " + TABLE_NAMES_EL + "(" + NAME_EL + ")" + " values " + "('Блок розеток с з.к.');");
//        db.execSQL("insert into " + TABLE_NAMES_EL + "(" + NAME_EL + ")" + " values " + "('Копир. аппарат');");
//        db.execSQL("insert into " + TABLE_NAMES_EL + "(" + NAME_EL + ")" + " values " + "('СВЧ-печь');");
//        db.execSQL("insert into " + TABLE_NAMES_EL + "(" + NAME_EL + ")" + " values " + "('Холодильник');");

        db.execSQL("create table if not exists " + TABLE_MARKS + "(" + MARK_ID + " integer primary key AUTOINCREMENT," + MARK + " text" + ");");
//        db.execSQL("insert into " + TABLE_MARKS + "(" + MARK + ")" + " values " + "('ПВС');");
//        db.execSQL("insert into " + TABLE_MARKS + "(" + MARK + ")" + " values " + "('ВВГ');");
//        db.execSQL("insert into " + TABLE_MARKS + "(" + MARK + ")" + " values " + "('АВВГ');");
//        db.execSQL("insert into " + TABLE_MARKS + "(" + MARK + ")" + " values " + "('ПУНП');");
//        db.execSQL("insert into " + TABLE_MARKS + "(" + MARK + ")" + " values " + "('АПУНП');");
//        db.execSQL("insert into " + TABLE_MARKS + "(" + MARK + ")" + " values " + "('ШВВП');");
//        db.execSQL("insert into " + TABLE_MARKS + "(" + MARK + ")" + " values " + "('АПВ');");
//        db.execSQL("insert into " + TABLE_MARKS + "(" + MARK + ")" + " values " + "('ПВ');");
//        db.execSQL("insert into " + TABLE_MARKS + "(" + MARK + ")" + " values " + "('ПВ3');");

        db.execSQL("create table if not exists " + TABLE_LIBRARY_ROOMS + "(" + LIB_ROOM_ID + " integer primary key AUTOINCREMENT," + LIB_ROOM_NAME + " text" + ");");

        db.execSQL("create table if not exists " + TABLE_LIBRARY_LINES + "(" + LIB_LINE_ID + " integer primary key AUTOINCREMENT," + LIB_LINE_NAME + " text" + ");");

        db.execSQL("create table if not exists " + TABLE_LIBRARY_FLOORS + "(" + LIB_FLOOR_ID + " integer primary key AUTOINCREMENT," + LIB_FLOOR_NAME + " text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_FLOORS);
        db.execSQL("drop table if exists " + TABLE_ROOMS);
        db.execSQL("drop table if exists " + TABLE_ELEMENTS);
        db.execSQL("drop table if exists " + TABLE_LINE_ROOMS);
        db.execSQL("drop table if exists " + TABLE_LINES);
        db.execSQL("drop table if exists " + TABLE_GROUPS);
        db.execSQL("drop table if exists " + TABLE_TITLE);
        db.execSQL("drop table if exists " + TABLE_GD);
        onCreate(db);
    }
}
