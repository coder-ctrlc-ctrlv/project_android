package com.example.protokol.create_protokol;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import java.lang.annotation.Target;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 87;
    public static final String DATABLE_NAME = "contactDB";

    //PROJECT INFORMATION

    //table1
    public static final String TABLE_PROJECT_INFO = "project_info";
    public static final String PROJECT_ID = "_id";
    public static final String PROJECT_NAME = "name";
    public static final String PROJECT_TITLE = "title";
    public static final String PROJECT_VISUAL_INSPECTION = "visual_inspection";
    public static final String PROJECT_INSULATION = "insulation";
    public static final String PROJECT_AUTOMATICS = "automatics";
    public static final String PROJECT_DIF_AUTOMATICS = "dif_automatics";
    public static final String PROJECT_GROUNDING_DEVICES = "grounding_devices";
    public static final String PROJECT_ROOM_ELEMENT = "room_element";

    //ROOM_ELEMENT

    //table2
    public static final String TABLE_FLOORS = "floors";
    public static final String FL_ID = "_id";
    public static final String FL_NAME = "floor";

    //table3
    public static final String TABLE_ROOMS = "rooms";
    public static final String KEY_ID = "_id";
    public static final String KEY_ID_FLOOR = "rfl_id";
    public static final String KEY_NAME = "room";

    //table4
    public static final String TABLE_ELEMENTS = "elements";
    public static final String EL_ID = "_id";
    public static final String EL_NAME = "element";
    public static final String EL_UNIT = "unit";
    public static final String EL_NUMBER = "number";
    public static final String ROOM_ID = "room_id";
    public static final String EL_SOPR = "sopr";
    public static final String EL_CONCLUSION = "conclusion";

    //table5
    public static final String TABLE_ELEMENTS_PZ = "elements_pz";
    public static final String EL_PZ_ID = "_id";
    public static final String EL_PZ_ELEMENT_ID = "el_id";
    public static final String EL_PZ_U = "u";
    public static final String EL_PZ_R = "r";
    public static final String EL_PZ_I = "i";

    //INSULATION

    //table6
    public static final String TABLE_INS_FLOORS = "ins_floors";
    public static final String INS_FL_ID = "_id";
    public static final String INS_FL_NAME = "ins_floor";

    //table7
    public static final String TABLE_LINE_ROOMS = "lnrooms";
    public static final String LNR_ID = "_id";
    public static final String INS_ID_RFLOOR = "ins_rfl_id";
    public static final String LNR_NAME = "room";

    //table8
    public static final String TABLE_LINES = "lines";
    public static final String LN_ID = "_id";
    public static final String LN_NAME = "line";
    public static final String LN_ID_ROOM = "lnr_id";

    //table9
    public static final String TABLE_GROUPS = "groups";
    public static final String GR_LINE_ID = "grline_id";
    public static final String GR_ID = "_id";
    public static final String GR_AUTOMATIC = "name_automatic";
    public static final String GR_TYPE_KZ = "gr_type_kz";
    public static final String GR_NOMINAL = "nominal";
    public static final String GR_RANGE = "range";
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
    public static final String GR_AUT_TYPE = "aut_type";
    public static final String GR_AUT_ID = "aut_id";

    //table10
    public static final String TABLE_INS_NOTES = "ins_notes";
    public static final String INS_NOTE_ID = "_id";
    public static final String INS_NOTE = "ins_note";

    //TITLE_PAGE

    //table11
    public static final String TABLE_TITLE = "title";
    public static final String TITLE_ID = "_id";
    public static final String TITLE_NAME_ELECTRO = "name_electro";
    public static final String TITLE_TARGET = "target";
    public static final String TITLE_ADDRESS = "address";
    public static final String TITLE_NUMBER_OF_PROTOKOL = "number_protokol";
    public static final String TITLE_DATE = "date";
    public static final String TITLE_FIRST_WORKER = "first_worker";
    public static final String TITLE_SECOND_WORKER = "second_worker";
    public static final String TITLE_CHIEF = "chief";
    public static final String TITLE_TEMPERATURE = "temperature";
    public static final String TITLE_HUMIDITY = "humidity";
    public static final String TITLE_PRESSURE = "pressure";

    //GROUNDING_DEVICES

    //table12
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

    //AUTOMATICS

    //table13
    public static final String TABLE_AU_FLOORS = "au_floors";
    public static final String AU_FL_ID = "_id";
    public static final String AU_FL_NAME = "au_floor";

    //table14
    public static final String TABLE_AU_ROOMS = "au_rooms";
    public static final String AU_ROOM_ID = "_id";
    public static final String AU_ID_RFLOOR = "au_rfl_id";
    public static final String AU_ROOM_NAME = "au_room";

    //table15
    public static final String TABLE_AU_LINES = "au_lines";
    public static final String AU_LINE_ID = "_id";
    public static final String AU_ID_LROOM = "au_lroom_id";
    public static final String AU_LINE_NAME = "au_line";

    //table16
    public static final String TABLE_AUTOMATICS = "automatics";
    public static final String AU_ID = "_id";
    public static final String AU_ID_ALINE = "auline_id";
    public static final String AU_PLACE = "place";
    public static final String AU_SYMBOL_SCHEME = "symbol_scheme";
    public static final String AU_NAME = "automatic";
    public static final String AU_TYPE_OVERLOAD = "type_overload";
    public static final String AU_TYPE_KZ = "type_kz";
    public static final String AU_EXCERPT = "excerpt";
    public static final String AU_NOMINAL_1 = "nominal_1";
    public static final String AU_NOMINAL_2 = "nominal_2";
    public static final String AU_SET_OVERLOAD = "set_overload";
    public static final String AU_SET_KZ = "set_kz";
    public static final String AU_TEST_TOK = "test_tok";
    public static final String AU_TIME_PERMISSIBLE = "time_permissible";
    public static final String AU_TIME_MEASURED = "time_measured";
    public static final String AU_LENGTH_ANNEX = "length_annex";
    public static final String AU_TOK_WORK = "tok_work";
    public static final String AU_TIME_WORK = "time_work";
    public static final String AU_CONCLUSION = "conclusion";

    //DIF_AUTOMATICS

    //table17
    public static final String TABLE_DIF_AU_FLOORS = "dif_au_floors";
    public static final String DIF_AU_FL_ID = "_id";
    public static final String DIF_AU_FL_NAME = "dif_au_floor";

    //table18
    public static final String TABLE_DIF_AU_ROOMS = "dif_au_rooms";
    public static final String DIF_AU_ROOM_ID = "_id";
    public static final String DIF_AU_ID_RFLOOR = "dif_au_rfl_id";
    public static final String DIF_AU_ROOM_NAME = "dif_au_room";

    //table19
    public static final String TABLE_DIF_AU_LINES = "dif_au_lines";
    public static final String DIF_AU_LINE_ID = "_id";
    public static final String DIF_AU_ID_LROOM = "dif_au_lroom_id";
    public static final String DIF_AU_LINE_NAME = "dif_au_line";

    //table20
    public static final String TABLE_DIF_AUTOMATICS = "dif_automatics";
    public static final String DIF_AU_ID = "_id";
    public static final String DIF_AU_ID_ALINE = "dif_auline_id";
    public static final String DIF_AU_PLACE = "place";
    public static final String DIF_AU_UZO = "uzo";
    public static final String DIF_AU_TYPE_SWITCH = "type_switch";
    public static final String DIF_AU_U = "u";
    public static final String DIF_AU_SET_THERMAL = "set_thermal";
    public static final String DIF_AU_SET_ELECTR_MAGN = "set_electromagn";
    public static final String DIF_AU_CHECK_TEST_TOK = "check_test_tok";
    public static final String DIF_AU_CHECK_TIME_ = "check_time";
    public static final String DIF_AU_CHECK_WORK_TOK = "check_work_tok";
    public static final String DIF_AU_I_NOM = "i_nom";
    public static final String DIF_AU_I_LEAK = "i_leack";
    public static final String DIF_AU_I_EXTRA = "i_extra";
    public static final String DIF_AU_I_MEASURED = "i_measured";
    public static final String DIF_AU_TIME_EXTRA = "time_extra";
    public static final String DIF_AU_TIME_MEASURED = "time_measured";
    public static final String DIF_AU_CONCLUSION = "conclusion";

    //PHASE_ZERO

    //table21
    public static final String TABLE_PZ_FLOORS = "pz_floors";
    public static final String PZ_FL_ID = "_id";
    public static final String PZ_FL_NAME = "pz_floor";

    //table22
    public static final String TABLE_PZ_ROOMS = "pz_rooms";
    public static final String PZ_ROOM_ID = "_id";
    public static final String PZ_ID_RFLOOR = "pz_rfl_id";
    public static final String PZ_ROOM_NAME = "pz_room";

    //table23
    public static final String TABLE_PZ_LINES = "pz_lines";
    public static final String PZ_LINE_ID = "_id";
    public static final String PZ_ID_LROOM = "pz_lroom_id";
    public static final String PZ_LINE_NAME = "pz_line";

    //table24
    public static final String TABLE_PZ = "phaze_zero";
    public static final String PZ_ID = "_id";
    public static final String PZLINE_ID = "pzline_id";
    public static final String PZ_PLACE = "place";
    public static final String PZ_TYPE_SYMBOL = "type_symbol";
    public static final String PZ_TYPE_RELEASE = "type_release";
    public static final String PZ_NOMINAL = "nominal";
    public static final String PZ_RANGE = "range";
    public static final String PZ_R_L1 = "r_l1";
    public static final String PZ_R_L2 = "r_l2";
    public static final String PZ_R_L3 = "r_l3";
    public static final String PZ_I_L1 = "i_l1";
    public static final String PZ_I_L2 = "i_l2";
    public static final String PZ_I_L3 = "i_l3";
    public static final String PZ_POSSIBLE = "possible";
    public static final String PZ_FACTUAL = "factual";
    public static final String PZ_CONCLUSION = "conclusion";

    //LIBRARY_NAMES_EL
    //table25
    public static final String TABLE_NAMES_EL = "names_el";
    public static final String NAME_EL_ID = "_id";
    public static final String NAME_EL = "name_el";

    //LIBRARY_MARKS
    //table26
    public static final String TABLE_MARKS = "marks";
    public static final String MARK_ID = "_id";
    public static final String MARK = "mark";

    //LIBRARY_ROOMS
    //table27
    public static final String TABLE_LIBRARY_ROOMS = "lib_rooms";
    public static final String LIB_ROOM_ID = "_id";
    public static final String LIB_ROOM_NAME = "room_name";

    //LIBRARY_LINES
    //table28
    public static final String TABLE_LIBRARY_LINES = "lib_lines";
    public static final String LIB_LINE_ID = "_id";
    public static final String LIB_LINE_NAME = "line_name";

    //LIBRARY_FLOORS
    //table29
    public static final String TABLE_LIBRARY_FLOORS = "lib_floors";
    public static final String LIB_FLOOR_ID = "_id";
    public static final String LIB_FLOOR_NAME = "floor_name";

    //LIBRARY_AUTOMATICS
    //table30
    public static final String TABLE_LIBRARY_AUTOMATICS = "lib_automatics";
    public static final String LIB_AU_ID = "_id";
    public static final String LIB_AU_NAME = "au_name";

    public DBHelper(Context context) {
        super(context, DATABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            db.disableWriteAheadLogging();
        }
        super.onOpen(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_PROJECT_INFO + "(" + PROJECT_ID + " integer primary key AUTOINCREMENT," + PROJECT_NAME +
                " text," + PROJECT_TITLE + " integer," + PROJECT_VISUAL_INSPECTION + " integer,"  + PROJECT_INSULATION +
                " integer," + PROJECT_AUTOMATICS + " integer," + PROJECT_DIF_AUTOMATICS + " integer," + PROJECT_GROUNDING_DEVICES +
                " integer," + PROJECT_ROOM_ELEMENT + " integer" + ");");

        db.execSQL("create table " + TABLE_FLOORS + "(" + FL_ID + " integer primary key AUTOINCREMENT," + FL_NAME +
                " text" + ");");

        db.execSQL("create table " + TABLE_ROOMS + "(" + KEY_ID + " integer primary key AUTOINCREMENT," + KEY_ID_FLOOR + " integer," + KEY_NAME +
            " text" + ");");

        db.execSQL("create table " + TABLE_ELEMENTS + "(" + EL_ID + " integer primary key AUTOINCREMENT," + EL_NAME +
            " text," + EL_UNIT + " text," + EL_NUMBER + " text,"  + ROOM_ID + " integer," + EL_SOPR + " text," + EL_CONCLUSION + " text" + ");");

        db.execSQL("create table " + TABLE_ELEMENTS_PZ + "(" + EL_PZ_ID + " integer primary key AUTOINCREMENT," + EL_PZ_ELEMENT_ID + " integer," + EL_PZ_U +
                " text," + EL_PZ_R + " text," + EL_PZ_I + " text" + ");");

        db.execSQL("create table " + TABLE_INS_FLOORS + "(" + INS_FL_ID + " integer primary key AUTOINCREMENT," + INS_FL_NAME +
                " text" + ");");

        db.execSQL("create table " + TABLE_LINE_ROOMS + "(" + LNR_ID + " integer primary key AUTOINCREMENT," + INS_ID_RFLOOR + " integer," + LNR_NAME +
            " text" + ");");

        db.execSQL("create table " + TABLE_LINES + "(" + LN_ID + " integer primary key AUTOINCREMENT," + LN_NAME +
            " text," + LN_ID_ROOM + " integer" + ");");

        db.execSQL("create table " + TABLE_GROUPS + "(" + GR_ID + " integer primary key AUTOINCREMENT," + GR_LINE_ID +
            " integer," + GR_AUTOMATIC + " text," + GR_TYPE_KZ + " text," + GR_NOMINAL + " text," + GR_RANGE +
            " text," + GR_NAME + " text," + GR_U1 + " text,"  + GR_MARK + " text," + GR_VEIN + " text," + GR_SECTION +
            " text," + GR_U2 + " text," + GR_R + " text," + GR_PHASE + " text," + GR_A_B + " text," + GR_B_C +
            " text," + GR_C_A + " text," + GR_A_N + " text," + GR_B_N + " text," + GR_C_N + " text," + GR_A_PE +
            " text," + GR_B_PE + " text," + GR_C_PE + " text," + GR_N_PE + " text," + GR_CONCLUSION + " text," + GR_AUT_TYPE +
            " text," + GR_AUT_ID + " integer" + ");");

        db.execSQL("create table " + TABLE_INS_NOTES + "(" + INS_NOTE_ID + " integer primary key AUTOINCREMENT," + INS_NOTE +
                " text" + ");");

        db.execSQL("create table " + TABLE_TITLE + "(" + TITLE_ID + " integer primary key AUTOINCREMENT," + TITLE_NAME_ELECTRO +
            " text," + TITLE_TARGET + " text," + TITLE_ADDRESS + " text," + TITLE_NUMBER_OF_PROTOKOL + " text," + TITLE_DATE +
            " text," + TITLE_FIRST_WORKER + " text," + TITLE_SECOND_WORKER + " text," + TITLE_CHIEF +
            " text," + TITLE_TEMPERATURE + " text," + TITLE_HUMIDITY + " text," + TITLE_PRESSURE + " text" + ");");

        db.execSQL("create table " + TABLE_GD + "(" + GD_DEVICE_ID + " integer primary key AUTOINCREMENT," + GD_RESULT_VIEW +
            " text," + GD_GROUND + " text," + GD_CHARACTER_GROUND + " text," + GD_U + " text," + GD_MODE_NEUTRAL +
            " text," + GD_R + " text," + GD_PURPOSE + " text," + GD_PLACE + " text," + GD_DISTANCE + " text," + GD_R1 +
            " text," + GD_01L + " text," + GD_02L + " text," + GD_03L + " text," + GD_04L + " text," + GD_05L +
            " text," + GD_06L + " text," + GD_07L + " text," + GD_08L + " text," + GD_09L + " text," + GD_GRAPHICS +
            " text," + GD_R2 + " text," + GD_K + " text," + GD_R3 + " text," + GD_CONCLUSION + " text," + GD_NOTE +
            " text," + GD_TEMPERATURE + " text," + GD_HUMIDITY + " text," + GD_PRESSURE + " text" + ");");

        db.execSQL("create table " + TABLE_AU_FLOORS + "(" + AU_FL_ID + " integer primary key AUTOINCREMENT," + AU_FL_NAME +
                " text" + ");");

        db.execSQL("create table " + TABLE_AU_ROOMS + "(" + AU_ROOM_ID + " integer primary key AUTOINCREMENT," + AU_ID_RFLOOR + " integer," + AU_ROOM_NAME +
                " text" + ");");

        db.execSQL("create table " + TABLE_AU_LINES + "(" + AU_LINE_ID + " integer primary key AUTOINCREMENT," + AU_ID_LROOM + " integer," + AU_LINE_NAME +
                " text" + ");");

        db.execSQL("create table " + TABLE_AUTOMATICS + "(" + AU_ID + " integer primary key AUTOINCREMENT," + AU_ID_ALINE +
                " integer," + AU_PLACE + " text," + AU_SYMBOL_SCHEME + " text," + AU_NAME + " text," + AU_TYPE_OVERLOAD +
                " text," + AU_TYPE_KZ + " text," + AU_EXCERPT + " text," + AU_NOMINAL_1 + " text," + AU_NOMINAL_2 + " text," + AU_SET_OVERLOAD +
                " text," + AU_SET_KZ + " text," + AU_TEST_TOK + " text," + AU_TIME_PERMISSIBLE + " text," + AU_TIME_MEASURED + " text," + AU_LENGTH_ANNEX +
                " text," + AU_TOK_WORK + " text," + AU_TIME_WORK + " text," + AU_CONCLUSION + " text" + ");");

        db.execSQL("create table " + TABLE_DIF_AU_FLOORS + "(" + DIF_AU_FL_ID + " integer primary key AUTOINCREMENT," + DIF_AU_FL_NAME +
                " text" + ");");

        db.execSQL("create table " + TABLE_DIF_AU_ROOMS + "(" + DIF_AU_ROOM_ID + " integer primary key AUTOINCREMENT," + DIF_AU_ID_RFLOOR + " integer," + DIF_AU_ROOM_NAME +
                " text" + ");");

        db.execSQL("create table " + TABLE_DIF_AU_LINES + "(" + DIF_AU_LINE_ID + " integer primary key AUTOINCREMENT," + DIF_AU_ID_LROOM + " integer," + DIF_AU_LINE_NAME +
                " text" + ");");

        db.execSQL("create table " + TABLE_DIF_AUTOMATICS + "(" + DIF_AU_ID + " integer primary key AUTOINCREMENT," + DIF_AU_ID_ALINE +
                " integer," + DIF_AU_PLACE + " text," + DIF_AU_UZO + " text," + DIF_AU_TYPE_SWITCH + " text," + DIF_AU_U +
                " text," + DIF_AU_SET_THERMAL + " text," + DIF_AU_SET_ELECTR_MAGN + " text," + DIF_AU_CHECK_TEST_TOK + " text," + DIF_AU_CHECK_TIME_ + " text," + DIF_AU_CHECK_WORK_TOK +
                " text," + DIF_AU_I_NOM + " text," + DIF_AU_I_LEAK + " text," + DIF_AU_I_EXTRA + " text," + DIF_AU_I_MEASURED + " text," + DIF_AU_TIME_EXTRA +
                " text," + DIF_AU_TIME_MEASURED + " text," + DIF_AU_CONCLUSION + " text" + ");");

        db.execSQL("create table " + TABLE_PZ_FLOORS + "(" + PZ_FL_ID + " integer primary key AUTOINCREMENT," + PZ_FL_NAME +
                " text" + ");");

        db.execSQL("create table " + TABLE_PZ_ROOMS + "(" + PZ_ROOM_ID + " integer primary key AUTOINCREMENT," + PZ_ID_RFLOOR + " integer," + PZ_ROOM_NAME +
                " text" + ");");

        db.execSQL("create table " + TABLE_PZ_LINES + "(" + PZ_LINE_ID + " integer primary key AUTOINCREMENT," + PZ_ID_LROOM + " integer," + PZ_LINE_NAME +
                " text" + ");");

        db.execSQL("create table " + TABLE_PZ + "(" + PZ_ID + " integer primary key AUTOINCREMENT," + PZLINE_ID +
                " integer," + PZ_PLACE + " text," + PZ_TYPE_SYMBOL + " text," + PZ_TYPE_RELEASE + " text," + PZ_NOMINAL +
                " text," + PZ_RANGE + " text," + PZ_R_L1 + " text," + PZ_R_L2 + " text," + PZ_R_L3 + " text," + PZ_I_L1 +
                " text," + PZ_I_L2 + " text," + PZ_I_L3 + " text," + PZ_POSSIBLE + " text," + PZ_FACTUAL + " text," + PZ_CONCLUSION + " text" + ");");

        db.execSQL("create table if not exists " + TABLE_NAMES_EL + "(" + NAME_EL_ID + " integer primary key AUTOINCREMENT," + NAME_EL + " text" + ");");
        //start of comment
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
        //end of comment

        db.execSQL("create table if not exists " + TABLE_MARKS + "(" + MARK_ID + " integer primary key AUTOINCREMENT," + MARK + " text" + ");");
        //start of comment
//        db.execSQL("insert into " + TABLE_MARKS + "(" + MARK + ")" + " values " + "('ПВС');");
//        db.execSQL("insert into " + TABLE_MARKS + "(" + MARK + ")" + " values " + "('ВВГ');");
//        db.execSQL("insert into " + TABLE_MARKS + "(" + MARK + ")" + " values " + "('АВВГ');");
//        db.execSQL("insert into " + TABLE_MARKS + "(" + MARK + ")" + " values " + "('ПУНП');");
//        db.execSQL("insert into " + TABLE_MARKS + "(" + MARK + ")" + " values " + "('АПУНП');");
//        db.execSQL("insert into " + TABLE_MARKS + "(" + MARK + ")" + " values " + "('ШВВП');");
//        db.execSQL("insert into " + TABLE_MARKS + "(" + MARK + ")" + " values " + "('АПВ');");
//        db.execSQL("insert into " + TABLE_MARKS + "(" + MARK + ")" + " values " + "('ПВ');");
//        db.execSQL("insert into " + TABLE_MARKS + "(" + MARK + ")" + " values " + "('ПВ3');");
        //end of comment

        db.execSQL("create table if not exists " + TABLE_LIBRARY_ROOMS + "(" + LIB_ROOM_ID + " integer primary key AUTOINCREMENT," + LIB_ROOM_NAME + " text" + ");");

        db.execSQL("create table if not exists " + TABLE_LIBRARY_LINES + "(" + LIB_LINE_ID + " integer primary key AUTOINCREMENT," + LIB_LINE_NAME + " text" + ");");

        db.execSQL("create table if not exists " + TABLE_LIBRARY_FLOORS + "(" + LIB_FLOOR_ID + " integer primary key AUTOINCREMENT," + LIB_FLOOR_NAME + " text" + ");");

        db.execSQL("create table if not exists " + TABLE_LIBRARY_AUTOMATICS + "(" + LIB_AU_ID + " integer primary key AUTOINCREMENT," + LIB_AU_NAME + " text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_PROJECT_INFO);
        db.execSQL("drop table if exists " + TABLE_FLOORS);
        db.execSQL("drop table if exists " + TABLE_ROOMS);
        db.execSQL("drop table if exists " + TABLE_ELEMENTS);
        db.execSQL("drop table if exists " + TABLE_ELEMENTS_PZ);
        db.execSQL("drop table if exists " + TABLE_INS_FLOORS);
        db.execSQL("drop table if exists " + TABLE_LINE_ROOMS);
        db.execSQL("drop table if exists " + TABLE_LINES);
        db.execSQL("drop table if exists " + TABLE_GROUPS);
        db.execSQL("drop table if exists " + TABLE_INS_NOTES);
        db.execSQL("drop table if exists " + TABLE_TITLE);
        db.execSQL("drop table if exists " + TABLE_GD);
        db.execSQL("drop table if exists " + TABLE_AU_FLOORS);
        db.execSQL("drop table if exists " + TABLE_AU_ROOMS);
        db.execSQL("drop table if exists " + TABLE_AU_LINES);
        db.execSQL("drop table if exists " + TABLE_AUTOMATICS);
        db.execSQL("drop table if exists " + TABLE_DIF_AU_FLOORS);
        db.execSQL("drop table if exists " + TABLE_DIF_AU_ROOMS);
        db.execSQL("drop table if exists " + TABLE_DIF_AU_LINES);
        db.execSQL("drop table if exists " + TABLE_DIF_AUTOMATICS);
        db.execSQL("drop table if exists " + TABLE_PZ_FLOORS);
        db.execSQL("drop table if exists " + TABLE_PZ_ROOMS);
        db.execSQL("drop table if exists " + TABLE_PZ_LINES);
        db.execSQL("drop table if exists " + TABLE_PZ);
        onCreate(db);
    }
}
