/***************************************************************************************************
 * FILENAME : DBHandler.java
 *
 * AUTHOR : Dalton Shadle
 *
 * DESCRIPTION : Holds functions for handling database operations
 *
 * NOTES : Tables include: (A better description of these can be found on the draw.io schematic)
 *          - Profile
 *          - Gun
 *          - Load
 *          - Event
 *          - Location
 *          - Shot (Score)
 *          - Team
 *
 * Copyright Dalton Shadle 2019.  All rights reserved.
 *
 **************************************************************************************************/

package edu.coe.djshadle.trapmaster;

//******************************************** Imports *********************************************
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {
    //********************************** Variables and Constants ***********************************
    //Database Variables & Constants
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "trapMasterDB.db";
    private SQLiteDatabase dbWhole;

    //Profile Table Stuff
    private static final String TABLE_PROFILES = "profiles";
    private static final String KEY_PROFILE_ID = "id";
    private static final String KEY_PROFILE_USERNAME = "profileUsername";  //Key

    //Gun Table Stuff
    private static final String TABLE_GUNS = "guns";
    private static final String KEY_GUN_ID = "id";
    private static final String KEY_GUN_PROFILE_NAME = "gunProfName";  //Key
    private static final String KEY_GUN_NICKNAME = "gunNickname";
    private static final String KEY_GUN_MODEL = "gunModel";
    private static final String KEY_GUN_GAUGE = "gunGauge";
    private static final String KEY_GUN_NOTES = "gunNotes";

    //Load Table Stuff
    private static final String TABLE_LOADS = "loads";
    private static final String KEY_LOAD_ID = "id";
    private static final String KEY_LOAD_PROFILE_NAME = "loadProfName";  //Key
    private static final String KEY_LOAD_NICKNAME = "loadNickname";
    private static final String KEY_LOAD_BRAND = "loadBrand";
    private static final String KEY_LOAD_GAUGE = "loadGauge";
    private static final String KEY_LOAD_LENGTH = "loadLength";
    private static final String KEY_LOAD_GRAIN = "loadGrain";
    private static final String KEY_LOAD_NOTES = "loadNotes";

    //Event Table Stuff
    private static final String TABLE_EVENTS = "events";
    private static final String KEY_EVENT_ID = "id";
    private static final String KEY_EVENT_PROFILE_NAME = "eventProfName";  //Key
    private static final String KEY_EVENT_TEAM_NAME = "eventTeamName";  //Key
    private static final String KEY_EVENT_NAME = "eventName";
    private static final String KEY_EVENT_LOCATION = "eventLocation";
    private static final String KEY_EVENT_GUN = "eventGun";
    private static final String KEY_EVENT_LOAD = "eventLoad";
    private static final String KEY_EVENT_DATE = "eventDate";
    private static final String KEY_EVENT_WEATHER = "eventWeather";
    private static final String KEY_EVENT_NOTES = "eventNotes";

    //Location Table Stuff
    private static final String TABLE_LOCATION = "location";
    private static final String KEY_LOCATION_ID = "id";
    private static final String KEY_LOCATION_PROFILE_NAME = "locationProfName";  //Key
    private static final String KEY_LOCATION_NAME = "locationName";

    //Shot Table Stuff
    private static final String TABLE_SHOT= "shot";
    private static final String KEY_SHOT_ID = "id";
    private static final String KEY_SHOT_PROFILE_NAME = "shotProfName";  //Key
    private static final String KEY_SHOT_EVENT_NAME = "shotEventName";  //Key?
    private static final String KEY_SHOT_NUMBER = "shotNumber";
    private static final String KEY_SHOT_HIT_MISS = "shotHitMiss";
    private static final String KEY_SHOT_NOTES = "shotNotes";

    //TeamTable Stuff
    private static final String TABLE_TEAM = "team";
    private static final String KEY_TEAM_ID = "id";
    private static final String KEY_TEAM_NAME = "teamName";  //Key
    private static final String KEY_TEAM_LIST = "teamList";

    //************************************ General DB Functions ************************************
    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        /*******************************************************************************************
         * Function: DBHandler
         *
         * Purpose: Constructor for this class
         *
         * Parameters: context (IN) - FILL_IN
         *             name (IN) - FILL_IN
         *             factory (IN) - FILL_IN
         *             version (IN) - FILL_IN
         *
         * Returns: None
         *
         ******************************************************************************************/

        super(context, name, factory, version);
    }

    public DBHandler(Context context) {
        /*******************************************************************************************
         * Function: DBHandler
         *
         * Purpose: Constructor for this class
         *
         * Parameters: context (IN) - FILL_IN
         *
         * Returns: None
         *
         ******************************************************************************************/

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*******************************************************************************************
         * Function: onCreate
         *
         * Purpose: Initializes all the tables for the database
         *
         * Parameters: db (IN) - Provides the SQLite database variable for initializing
         *
         * Returns: None
         *
         ******************************************************************************************/


        //Profile Table
        String CREATE_PROFILE_TABLE = "CREATE TABLE " + TABLE_PROFILES + " ("
                + KEY_PROFILE_ID + " INTEGER PRIMARY KEY,"
                + KEY_PROFILE_USERNAME + " TEXT"
                + ")";
        db.execSQL(CREATE_PROFILE_TABLE);

        //Gun Table
        String CREATE_GUN_TABLE = "CREATE TABLE " + TABLE_GUNS + " ("
                + KEY_GUN_ID + " INTEGER PRIMARY KEY,"
                + KEY_GUN_PROFILE_NAME + " TEXT,"
                + KEY_GUN_NICKNAME + " TEXT,"
                + KEY_GUN_MODEL + " TEXT,"
                + KEY_GUN_GAUGE + " TEXT,"
                + KEY_GUN_NOTES + " TEXT"
                + ")";
        db.execSQL(CREATE_GUN_TABLE);


        //Load Table
        String CREATE_LOAD_TABLE = "CREATE TABLE " + TABLE_LOADS + " ("
                + KEY_LOAD_ID + " INTEGER PRIMARY KEY,"
                + KEY_LOAD_PROFILE_NAME + " TEXT,"
                + KEY_LOAD_NICKNAME + " TEXT,"
                + KEY_LOAD_BRAND + " TEXT,"
                + KEY_LOAD_GAUGE + " TEXT,"
                + KEY_LOAD_LENGTH + " TEXT,"
                + KEY_LOAD_GRAIN + " TEXT,"
                + KEY_LOAD_NOTES + " TEXT"
                + ")";
        db.execSQL(CREATE_LOAD_TABLE);

        //Event Table Stuff
        String CREATE_EVENT_TABLE = "CREATE TABLE " + TABLE_EVENTS + " ("
                + KEY_EVENT_ID + " INTEGER PRIMARY KEY,"
                + KEY_EVENT_PROFILE_NAME + " TEXT,"
                + KEY_EVENT_TEAM_NAME + " TEXT,"
                + KEY_EVENT_NAME + " TEXT,"
                + KEY_EVENT_LOCATION + " TEXT,"
                + KEY_EVENT_GUN + " TEXT,"
                + KEY_EVENT_LOAD + " TEXT,"
                + KEY_EVENT_DATE + " TEXT,"
                + KEY_EVENT_WEATHER + " TEXT,"
                + KEY_EVENT_NOTES + " TEXT"
                + ")";
        db.execSQL(CREATE_EVENT_TABLE);


        //Location Table
        String CREATE_LOCATION_TABLE = "CREATE TABLE " + TABLE_LOCATION + " ("
                + KEY_LOCATION_ID + " INTEGER PRIMARY KEY,"
                + KEY_LOCATION_PROFILE_NAME + " TEXT,"
                + KEY_LOCATION_NAME + " TEXT"
                + ")";
        db.execSQL(CREATE_LOCATION_TABLE);

        //Shot Table
        String CREATE_SHOT_TABLE = "CREATE TABLE " + TABLE_SHOT + " ("
                + KEY_SHOT_ID + " INTEGER PRIMARY KEY,"
                + KEY_SHOT_PROFILE_NAME + " TEXT,"
                + KEY_SHOT_EVENT_NAME + " TEXT,"
                + KEY_SHOT_NUMBER + " TEXT,"
                + KEY_SHOT_HIT_MISS + " TEXT,"
                + KEY_SHOT_NOTES + " TEXT"
                + ")";
        db.execSQL(CREATE_SHOT_TABLE);

        //Team Table
        String CREATE_TEAM_TABLE = "CREATE TABLE " + TABLE_TEAM + " ("
                + KEY_TEAM_ID + " INTEGER PRIMARY KEY,"
                + KEY_TEAM_NAME + " TEXT,"
                + KEY_TEAM_LIST + " TEXT"
                + ")";
        db.execSQL(CREATE_TEAM_TABLE);

        dbWhole = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*******************************************************************************************
         * Function: onUpgrade
         *
         * Purpose: Function upgrades the database and version number
         *
         * Parameters: db (IN) - SQLite database variable for upgrading
         *             oldVersion (IN) - old version number
         *             newVersion (IN) - new version number
         *
         * Returns: None
         *
         ******************************************************************************************/

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOADS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEAM);
        onCreate(db);
    }

    //************************************* Profile Functions **************************************
    public void insertProfileInDB (ProfileClass p){
        /*******************************************************************************************
         * Function: insertProfileInDB
         *
         * Purpose: Function inserts a profile into the database based on the info provided in p
         *
         * Parameters: p (IN) - ProfileClass object to insert into database
         *
         * Returns: None
         *
         ******************************************************************************************/

        dbWhole = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PROFILE_USERNAME, p.getProfileEmail_Str());

        dbWhole.insert(TABLE_PROFILES, null, values);
        dbWhole.close();
    }

    public ProfileClass getProfileFromDB (String email){
        /*******************************************************************************************
         * Function: getProfileFromDB
         *
         * Purpose: Function gets a profile based on the email provided
         *
         * Parameters: email (IN) - key string for finding profile
         *
         * Returns: tempProfile - variable that contains all info from database for this email
         *          TODO: make this return false or -1 or indicate if profile not found
         *
         ******************************************************************************************/

        ProfileClass tempProfile = new ProfileClass();

        dbWhole = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_PROFILES + " WHERE " + KEY_PROFILE_USERNAME + " = '" + email + "'";
        Cursor cursor = dbWhole.rawQuery(query, null);

        if (cursor != null) {
            cursor.moveToFirst();

            tempProfile.setProfileID_Str(cursor.getString(cursor.getColumnIndex(KEY_PROFILE_ID)));
            tempProfile.setProfileEmail_Str(cursor.getString(cursor.getColumnIndex(KEY_PROFILE_USERNAME)));
        }

        dbWhole.close();
        return tempProfile;
    }

    public boolean isEmailInDB(String email) {
        /*******************************************************************************************
         * Function: isEmailInDB
         *
         * Purpose: Function decides whether email provided is stored in db
         *
         * Parameters: email (IN) - key string for finding profile
         *
         * Returns: doesEmailExist - returns true if the email parameter is in the db
         *
         ******************************************************************************************/

        String dbName;
        boolean doesEmailExist = false;
        String query = "SELECT " + KEY_PROFILE_USERNAME + " FROM " + TABLE_PROFILES;

        dbWhole = this.getReadableDatabase();
        Cursor cursor = dbWhole.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                dbName = cursor.getString(cursor.getColumnIndex(KEY_PROFILE_USERNAME));

                if (dbName.equals(email)) {
                    //name already exists
                    doesEmailExist = true;
                    break;
                }
            } while (cursor.moveToNext());
        }

        dbWhole.close();
        return doesEmailExist;
    }

    //*************************************** Shot Functions ***************************************
    public void insertShotInDB (ShotClass s) {
        /*******************************************************************************************
         * Function: insertShotInDB
         *
         * Purpose: Function inserts information from ShotClass object into database
         *
         * Parameters: s (IN) - shot class object that holds information to put in database
         *
         * Returns: None
         *
         ******************************************************************************************/

        dbWhole = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SHOT_PROFILE_NAME, s.getShotEmail_Str());
        values.put(KEY_SHOT_EVENT_NAME, s.getShotEventName_Str());
        values.put(KEY_SHOT_NUMBER, s.getShotTotalNum_Str());
        values.put(KEY_SHOT_HIT_MISS, s.getShotHitNum_Str());
        values.put(KEY_SHOT_NOTES, s.getShotNotes_Str());

        dbWhole.insert(TABLE_SHOT, null, values);
        dbWhole.close();
    }

    public ArrayList<ShotClass> getAllShotFromDB (String email) {
        /*******************************************************************************************
         * Function: getAllShotFromDB
         *
         * Purpose: Function gathers shot information based on email provided
         *
         * Parameters: email (IN) - key string for receiving shot information
         *
         * Returns: tempShot - returns list of ShotClass object with database information
         *
         ******************************************************************************************/

        dbWhole = this.getReadableDatabase();

        ArrayList<ShotClass> tempShot_List = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_SHOT + " WHERE " + KEY_SHOT_PROFILE_NAME +
                " = '" + email + "'";

        Cursor cursor = dbWhole.rawQuery(query, null);

        while (cursor.moveToNext()) {
            ShotClass tempShot = new ShotClass();
            tempShot.setShotEmail_Str(cursor.getString(cursor.getColumnIndex(KEY_SHOT_PROFILE_NAME)));
            tempShot.setShotEventName_Str(cursor.getString(cursor.getColumnIndex(KEY_SHOT_EVENT_NAME)));
            tempShot.setShotTotalNum_Str(cursor.getString(cursor.getColumnIndex(KEY_SHOT_NUMBER)));
            tempShot.setShotHitNum_Str(cursor.getString(cursor.getColumnIndex(KEY_SHOT_HIT_MISS)));
            tempShot.setShotNotes_Str(cursor.getString(cursor.getColumnIndex(KEY_SHOT_NOTES)));

            tempShot_List.add(tempShot);
        }

        dbWhole.close();
        return tempShot_List;
    }

    //*************************************** Load Functions ***************************************
    public void insertLoadInDB (LoadClass l) {
        /*******************************************************************************************
         * Function: insertLoadInDB
         *
         * Purpose: Function inserts information from LoadClass object into database
         *
         * Parameters: l (IN) - load class object that holds information to put in database
         *
         * Returns: None
         *
         ******************************************************************************************/

        dbWhole = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LOAD_PROFILE_NAME, l.getLoadEmail_Str());
        values.put(KEY_LOAD_NICKNAME, l.getLoadNickname_Str());
        values.put(KEY_LOAD_BRAND, l.getLoadBrand_Str());
        values.put(KEY_LOAD_GAUGE, l.getLoadGauge_Str());
        values.put(KEY_LOAD_GRAIN, l.getLoadGrain_Str());
        values.put(KEY_LOAD_LENGTH, l.getLoadLength_Str());
        values.put(KEY_LOAD_NOTES, l.getLoadNotes_Str());

        dbWhole.insert(TABLE_LOADS, null, values);
        dbWhole.close();
    }

    public ArrayList<LoadClass> getAllLoadFromDB (String email) {
        /*******************************************************************************************
         * Function: getAllLoadFromDB
         *
         * Purpose: Function gathers load information based on email provided
         *
         * Parameters: email (IN) - key string for receiving load information
         *
         * Returns: tempLoad_List - returns list of LoadClass object with database information
         *
         ******************************************************************************************/

        dbWhole = this.getReadableDatabase();

        ArrayList<LoadClass> tempLoad_List = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_LOADS + " WHERE " + KEY_LOAD_PROFILE_NAME +
                " = '" + email + "'";

        Cursor cursor = dbWhole.rawQuery(query, null);

        while (cursor.moveToNext()) {
            LoadClass tempLoad = new LoadClass();
            tempLoad.setLoadID_Int(cursor.getInt(cursor.getColumnIndex(KEY_LOAD_ID)));
            tempLoad.setLoadEmail_Str(cursor.getString(cursor.getColumnIndex(KEY_LOAD_PROFILE_NAME)));
            tempLoad.setLoadNickname_Str(cursor.getString(cursor.getColumnIndex(KEY_LOAD_NICKNAME)));
            tempLoad.setLoadBrand_Str(cursor.getString(cursor.getColumnIndex(KEY_LOAD_BRAND)));
            tempLoad.setLoadGauge_Str(cursor.getString(cursor.getColumnIndex(KEY_LOAD_GAUGE)));
            tempLoad.setLoadGrain_Str(cursor.getString(cursor.getColumnIndex(KEY_LOAD_GRAIN)));
            tempLoad.setLoadLength_Str(cursor.getString(cursor.getColumnIndex(KEY_LOAD_LENGTH)));
            tempLoad.setLoadNotes_Str(cursor.getString(cursor.getColumnIndex(KEY_LOAD_NOTES)));

            tempLoad_List.add(tempLoad);
        }

        dbWhole.close();
        return tempLoad_List;
    }

    public void deleteLoadInDB (String email, String loadName) {
        /*******************************************************************************************
         * Function: deleteLoadInDB
         *
         * Purpose: Function deletes item from database based on email and loadName
         *
         * Parameters: email (IN) - email to remove gun from
         *             loadName (IN) - name of load to remove
         *
         * Returns: None
         *
         ******************************************************************************************/

        dbWhole = this.getWritableDatabase();

        String whereClause_Str = "(" + KEY_LOAD_PROFILE_NAME + " = '" + email + "' AND "
                + KEY_LOAD_NICKNAME + " = '" + loadName + "')";
        try {
            dbWhole.delete(TABLE_LOADS, whereClause_Str, null);
        } catch (Exception e) {
            Log.d("JRW", e.toString());
        }

        dbWhole.close();
    }

    public int getIDforLoad (String email, String loadName) {
        /*******************************************************************************************
         * Function: getIDforLoad
         *
         * Purpose: Function returns the ID number of a load in the database
         *
         * Parameters: email (IN) - email of load to get ID
         *             loadName (IN) - name of load to get ID
         *
         * Returns: loadID_Int - ID number of load
         *
         ******************************************************************************************/

        int loadID_Int = 99999;
        String query = "SELECT " + KEY_LOAD_ID + " FROM " + TABLE_LOADS + " WHERE "
                + KEY_LOAD_PROFILE_NAME + " = '" + email + "' AND " + KEY_LOAD_NICKNAME + " = '"
                + loadName + "'";

        dbWhole = this.getReadableDatabase();

        try {
            Cursor cursor = dbWhole.rawQuery(query, null);
            cursor.moveToFirst();
            loadID_Int = cursor.getInt(cursor.getColumnIndex(KEY_LOAD_ID));
        } catch (Exception e) {
            Log.d("JRW", e.toString());
        }

        dbWhole.close();

        return loadID_Int;
    }

    public LoadClass getLoadInDB (int loadID_Int) {
        /*******************************************************************************************
         * Function: getLoadInDB
         *
         * Purpose: Function returns the ID number of a load in the database
         *
         * Parameters: loadID_Int (IN) - ID of the load information to return
         *
         * Returns: tempLoad - Information of the load stored at loadID_Int
         *
         ******************************************************************************************/

        LoadClass tempLoad = new LoadClass();
        String query = "SELECT * FROM " + TABLE_LOADS + " WHERE "
                + KEY_LOAD_ID + " = " + Integer.toString(loadID_Int);

        dbWhole = this.getReadableDatabase();

        try {
            Cursor cursor = dbWhole.rawQuery(query, null);
            cursor.moveToFirst();
            tempLoad.setLoadID_Int(cursor.getInt(cursor.getColumnIndex(KEY_LOAD_ID)));
            tempLoad.setLoadEmail_Str(cursor.getString(cursor.getColumnIndex(KEY_LOAD_PROFILE_NAME)));
            tempLoad.setLoadNickname_Str(cursor.getString(cursor.getColumnIndex(KEY_LOAD_NICKNAME)));
            tempLoad.setLoadBrand_Str(cursor.getString(cursor.getColumnIndex(KEY_LOAD_BRAND)));
            tempLoad.setLoadGauge_Str(cursor.getString(cursor.getColumnIndex(KEY_LOAD_GAUGE)));
            tempLoad.setLoadGrain_Str(cursor.getString(cursor.getColumnIndex(KEY_LOAD_GRAIN)));
            tempLoad.setLoadLength_Str(cursor.getString(cursor.getColumnIndex(KEY_LOAD_LENGTH)));
            tempLoad.setLoadNotes_Str(cursor.getString(cursor.getColumnIndex(KEY_LOAD_NOTES)));
        } catch (Exception e) {
            Log.d("JRW", e.toString());
        }

        dbWhole.close();

        return tempLoad;
    }

    public void updateLoadInDB (LoadClass l, int loadID_Int) {
        /*******************************************************************************************
         * Function: updateLoadInDB
         *
         * Purpose: Function updates item from database based on database ID
         *
         * Parameters: l (IN) - object that holds information for updating item in the database
         *             loadID_Int (IN) - ID number of the item in the database
         *
         * Returns: None
         *
         ******************************************************************************************/

        dbWhole = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LOAD_PROFILE_NAME, l.getLoadEmail_Str());
        values.put(KEY_LOAD_NICKNAME, l.getLoadNickname_Str());
        values.put(KEY_LOAD_BRAND, l.getLoadBrand_Str());
        values.put(KEY_LOAD_GAUGE, l.getLoadGauge_Str());
        values.put(KEY_LOAD_GRAIN, l.getLoadGrain_Str());
        values.put(KEY_LOAD_LENGTH, l.getLoadLength_Str());
        values.put(KEY_LOAD_NOTES, l.getLoadNotes_Str());

        String whereClaus = KEY_GUN_ID + " = " + Integer.toString(loadID_Int);

        dbWhole.update(TABLE_LOADS, values, whereClaus, null);
        dbWhole.close();
    }

    public boolean isLoadNicknameInDB (String email, String nickname, int ID) {
        /*******************************************************************************************
         * Function: isLoadNicknameInDB
         *
         * Purpose: Function decides if nickname is already in db for user
         *
         * Parameters: email (IN) - key string for finding profile
         *             nickname (IN) - string to find in database
         *             ID (IN) - ID of load being passed in, this is to ignore this load when checking
         *
         * Returns: doesNicknameExist - returns true if the user already has a load under that name,
         *                              besides the load that shares the same ID
         *
         ******************************************************************************************/

        boolean doesNicknameExist = false;
        int currentID = -1;
        String currentNickname;
        String query = "SELECT * FROM " + TABLE_LOADS + " WHERE "
                + KEY_LOAD_PROFILE_NAME + " = '" + email + "'";

        dbWhole = this.getReadableDatabase();
        Cursor cursor = dbWhole.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                currentNickname = cursor.getString(cursor.getColumnIndex(KEY_LOAD_NICKNAME));
                currentID = cursor.getInt(cursor.getColumnIndex(KEY_LOAD_ID));

                if (currentNickname.equals(nickname) && currentID != ID) {
                    //name already exists
                    doesNicknameExist = true;
                    break;
                }
            } while (cursor.moveToNext());
        }

        dbWhole.close();
        return doesNicknameExist;
    }

    //**************************************** Gun Functions ***************************************
    public void insertGunInDB (GunClass g) {
        /*******************************************************************************************
         * Function: insertGunInDB
         *
         * Purpose: Function inserts information from GunClass object into database
         *
         * Parameters: g (IN) - g class object that holds information to put in database
         *
         * Returns: None
         *
         ******************************************************************************************/

        dbWhole = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_GUN_PROFILE_NAME, g.getGunEmail_Str());
        values.put(KEY_GUN_NICKNAME, g.getGunNickname_Str());
        values.put(KEY_GUN_MODEL, g.getGunModel_Str());
        values.put(KEY_GUN_GAUGE, g.getGunGauge_Str());
        values.put(KEY_GUN_NOTES, g.getGunNotes_Str());

        dbWhole.insert(TABLE_GUNS, null, values);
        dbWhole.close();
    }

    public ArrayList<GunClass> getAllGunFromDB (String email) {
        /*******************************************************************************************
         * Function: getAllGunFromDB
         *
         * Purpose: Function gathers gun information based on email provided
         *
         * Parameters: email (IN) - key string for receiving gun information
         *
         * Returns: tempShot - returns list of GunClass object with database information
         *
         ******************************************************************************************/

        dbWhole = this.getReadableDatabase();

        ArrayList<GunClass> tempGun_List = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_GUNS + " WHERE " + KEY_GUN_PROFILE_NAME +
                " = '" + email + "'";

        Cursor cursor = dbWhole.rawQuery(query, null);

        while (cursor.moveToNext()) {
            GunClass tempGun = new GunClass();
            tempGun.setGunID_Int(cursor.getInt(cursor.getColumnIndex(KEY_GUN_ID)));
            tempGun.setGunEmail_Str(cursor.getString(cursor.getColumnIndex(KEY_GUN_PROFILE_NAME)));
            tempGun.setGunNickname_Str(cursor.getString(cursor.getColumnIndex(KEY_GUN_NICKNAME)));
            tempGun.setGunModel_Str(cursor.getString(cursor.getColumnIndex(KEY_GUN_MODEL)));
            tempGun.setGunGauge_Str(cursor.getString(cursor.getColumnIndex(KEY_GUN_GAUGE)));
            tempGun.setGunNotes_Str(cursor.getString(cursor.getColumnIndex(KEY_GUN_NOTES)));

            tempGun_List.add(tempGun);
        }

        dbWhole.close();
        return tempGun_List;
    }

    public void deleteGunInDB (String email, String gunName) {
        /*******************************************************************************************
         * Function: deleteGunInDB
         *
         * Purpose: Function deletes item from database based on email and gunName
         *
         * Parameters: email (IN) - email to remove gun from
         *             gunName (IN) - name of gun to remove
         *
         * Returns: None
         *
         ******************************************************************************************/

        dbWhole = this.getWritableDatabase();

        String whereClause_Str = "(" + KEY_GUN_PROFILE_NAME + " = '" + email + "' AND "
                + KEY_GUN_NICKNAME + " = '" + gunName + "')";
        try {
            dbWhole.delete(TABLE_GUNS, whereClause_Str, null);
        } catch (Exception e) {
            Log.d("JRW", e.toString());
        }

        dbWhole.close();
    }

    public int getIDforGun (String email, String gunName) {
        /*******************************************************************************************
         * Function: getIDforGun
         *
         * Purpose: Function returns the ID number of a gun in the database
         *
         * Parameters: email (IN) - email of gun to get ID
         *             gunName (IN) - name of gun to get ID
         *
         * Returns: gunID_Int - ID number of gun
         *
         ******************************************************************************************/

        int gunID_Int = 99999;
        String query = "SELECT " + KEY_GUN_ID + " FROM " + TABLE_GUNS + " WHERE "
                + KEY_GUN_PROFILE_NAME + " = '" + email + "' AND " + KEY_GUN_NICKNAME + " = '"
                + gunName + "'";

        dbWhole = this.getReadableDatabase();

        try {
            Cursor cursor = dbWhole.rawQuery(query, null);
            cursor.moveToFirst();
            gunID_Int = cursor.getInt(cursor.getColumnIndex(KEY_GUN_ID));
        } catch (Exception e) {
            Log.d("JRW", e.toString());
        }

        dbWhole.close();

        return gunID_Int;
    }

    public GunClass getGunInDB (int gunID_Int) {
        /*******************************************************************************************
         * Function: getGunInDB
         *
         * Purpose: Function returns the ID number of a gun in the database
         *
         * Parameters: gunID_Int (IN) - ID of the gun information to return
         *
         * Returns: tempGun - Information of the gun stored at gunID_Int
         *
         ******************************************************************************************/

        GunClass tempGun = new GunClass();
        String query = "SELECT * FROM " + TABLE_GUNS + " WHERE "
                + KEY_GUN_ID + " = " + Integer.toString(gunID_Int);

        dbWhole = this.getReadableDatabase();

        try {
            Cursor cursor = dbWhole.rawQuery(query, null);
            cursor.moveToFirst();
            tempGun.setGunID_Int(cursor.getInt(cursor.getColumnIndex(KEY_GUN_ID)));
            tempGun.setGunEmail_Str(cursor.getString(cursor.getColumnIndex(KEY_GUN_PROFILE_NAME)));
            tempGun.setGunNickname_Str(cursor.getString(cursor.getColumnIndex(KEY_GUN_NICKNAME)));
            tempGun.setGunModel_Str(cursor.getString(cursor.getColumnIndex(KEY_GUN_MODEL)));
            tempGun.setGunGauge_Str(cursor.getString(cursor.getColumnIndex(KEY_GUN_GAUGE)));
            tempGun.setGunNotes_Str(cursor.getString(cursor.getColumnIndex(KEY_GUN_NOTES)));
        } catch (Exception e) {
            Log.d("JRW", e.toString());
        }

        dbWhole.close();

        return tempGun;
    }

    public void updateGunInDB (GunClass g, int gunID_Int) {
        /*******************************************************************************************
         * Function: updateGunInDB
         *
         * Purpose: Function updates item from database based on database ID
         *
         * Parameters: g (IN) - object that holds information for updating item in the database
         *             gunID_Int (IN) - ID number of the item in the database
         *
         * Returns: None
         *
         ******************************************************************************************/

        dbWhole = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_GUN_PROFILE_NAME, g.getGunEmail_Str());
        values.put(KEY_GUN_NICKNAME, g.getGunNickname_Str());
        values.put(KEY_GUN_MODEL, g.getGunModel_Str());
        values.put(KEY_GUN_GAUGE, g.getGunGauge_Str());
        values.put(KEY_GUN_NOTES, g.getGunNotes_Str());

        String whereClaus = KEY_GUN_ID + " = " + Integer.toString(gunID_Int);

        dbWhole.update(TABLE_GUNS, values, whereClaus, null);
        dbWhole.close();
    }

    public boolean isGunNicknameInDB (String email, String nickname, int ID) {
        /*******************************************************************************************
         * Function: isGunNicknameInDB
         *
         * Purpose: Function decides if nickname is already in db for user
         *
         * Parameters: email (IN) - key string for finding profile
         *             nickname (IN) - string to find in database
         *             ID (IN) - ID of gun being passed in, this is to ignore this gun when checking
         *
         * Returns: doesNicknameExist - returns true if the user already has a gun under that name,
         *                              besides the gun that shares the same ID
         *
         ******************************************************************************************/

        boolean doesNicknameExist = false;
        int currentID = -1;
        String currentNickname;
        String query = "SELECT * FROM " + TABLE_GUNS
                + " WHERE " + KEY_GUN_PROFILE_NAME + " = '" + email + "'";

        dbWhole = this.getReadableDatabase();
        Cursor cursor = dbWhole.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                currentNickname = cursor.getString(cursor.getColumnIndex(KEY_GUN_NICKNAME));
                currentID = cursor.getInt(cursor.getColumnIndex(KEY_GUN_ID));

                if (currentNickname.equals(nickname) && currentID != ID) {
                    //name already exists
                    doesNicknameExist = true;
                    break;
                }
            } while (cursor.moveToNext());
        }

        dbWhole.close();
        return doesNicknameExist;
    }

    //************************************** Event Functions ***************************************
    public void insertEventInDB (EventClass e) {
        /*******************************************************************************************
         * Function: insertEventInDB
         *
         * Purpose: Function inserts information from EventClass object into database
         *
         * Parameters: e (IN) - event class object that holds information to put in database
         *
         * Returns: None
         *
         ******************************************************************************************/

        dbWhole = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EVENT_PROFILE_NAME, e.getEventEmail_Str());
        values.put(KEY_EVENT_TEAM_NAME, e.getEventTeam_Str());
        values.put(KEY_EVENT_NAME, e.getEventName_Str());
        values.put(KEY_EVENT_LOCATION, e.getEventLocation_Str());
        values.put(KEY_EVENT_GUN, e.getEventGun_Str());
        values.put(KEY_EVENT_LOAD, e.getEventLoad_Str());
        values.put(KEY_EVENT_DATE, e.getEventDate_Str());
        values.put(KEY_EVENT_WEATHER, e.getEventWeather_Str());
        values.put(KEY_EVENT_NOTES, e.getEventNotes_Str());


        dbWhole.insert(TABLE_EVENTS, null, values);
        dbWhole.close();
    }

    public ArrayList<EventClass> getAllEventFromDB (String email) {
        /*******************************************************************************************
         * Function: getAllEventFromDB
         *
         * Purpose: Function gathers event information based on email provided
         *
         * Parameters: email (IN) - key string for receiving event information
         *
         * Returns: tempEvent_List - returns list of EventClass object with database information
         *
         ******************************************************************************************/

        dbWhole = this.getReadableDatabase();

        ArrayList<EventClass> tempEvent_List = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_EVENTS + " WHERE " + KEY_EVENT_PROFILE_NAME +
                " = '" + email + "'";

        Cursor cursor = dbWhole.rawQuery(query, null);

        while (cursor.moveToNext()) {
            EventClass tempEvent = new EventClass();

            tempEvent.setEventID_Int(cursor.getInt(cursor.getColumnIndex(KEY_EVENT_ID)));
            tempEvent.setEventEmail_Str(cursor.getString(cursor.getColumnIndex(KEY_EVENT_PROFILE_NAME)));
            tempEvent.setEventTeam_Str(cursor.getString(cursor.getColumnIndex(KEY_EVENT_TEAM_NAME)));
            tempEvent.setEventName_Str(cursor.getString(cursor.getColumnIndex(KEY_EVENT_NAME)));
            tempEvent.setEventLocation_Str(cursor.getString(cursor.getColumnIndex(KEY_EVENT_LOCATION)));
            tempEvent.setEventGun_Str(cursor.getString(cursor.getColumnIndex(KEY_EVENT_GUN)));
            tempEvent.setEventLoad_Str(cursor.getString(cursor.getColumnIndex(KEY_EVENT_LOAD)));
            tempEvent.setEventDate_Str(cursor.getString(cursor.getColumnIndex(KEY_EVENT_DATE)));
            tempEvent.setEventWeather_Str(cursor.getString(cursor.getColumnIndex(KEY_EVENT_WEATHER)));
            tempEvent.setEventNotes_Str(cursor.getString(cursor.getColumnIndex(KEY_EVENT_NOTES)));

            tempEvent_List.add(tempEvent);
        }

        dbWhole.close();
        return tempEvent_List;
    }

    public void deleteEventInDB (String email, String eventName) {
        /*******************************************************************************************
         * Function: deleteEventInDB
         *
         * Purpose: Function deletes item from database based on email and eventName
         *
         * Parameters: email (IN) - email to remove gun from
         *             eventName (IN) - name of event to remove
         *
         * Returns: None
         *
         ******************************************************************************************/

        dbWhole = this.getWritableDatabase();

        String whereClause_Str = "(" + KEY_EVENT_PROFILE_NAME + " = '" + email + "' AND "
                + KEY_EVENT_NAME + " = '" + eventName + "')";
        try {
            dbWhole.delete(TABLE_EVENTS, whereClause_Str, null);
        } catch (Exception e) {
            Log.d("JRW", e.toString());
        }

        dbWhole.close();
    }

    public int getIDforEvent (String email, String eventName) {
        /*******************************************************************************************
         * Function: getIDforEvent
         *
         * Purpose: Function returns the ID number of a event in the database
         *
         * Parameters: email (IN) - email of gun to get ID
         *             eventName (IN) - name of event to get ID
         *
         * Returns: eventID_Int - ID number of event
         *
         ******************************************************************************************/

        int eventID_Int = -1;
        String query = "SELECT " + KEY_EVENT_NAME + " FROM " + TABLE_EVENTS + " WHERE "
                + KEY_EVENT_PROFILE_NAME + " = '" + email + "' AND " + KEY_EVENT_NAME + " = '"
                + eventName + "'";

        dbWhole = this.getReadableDatabase();

        try {
            Cursor cursor = dbWhole.rawQuery(query, null);
            cursor.moveToFirst();
            eventID_Int = cursor.getInt(cursor.getColumnIndex(KEY_EVENT_ID));
        } catch (Exception e) {
            Log.d("JRW", e.toString());
        }

        dbWhole.close();

        return eventID_Int;
    }

    public EventClass getEventInDB (int eventID_Int) {
        /*******************************************************************************************
         * Function: getEventInDB
         *
         * Purpose: Function returns the ID number of a event in the database
         *
         * Parameters: eventID_Int (IN) - ID of the event information to return
         *
         * Returns: tempEvent - Information of the event stored at eventID_Int
         *
         ******************************************************************************************/

        EventClass tempEvent = new EventClass();
        String query = "SELECT * FROM " + TABLE_EVENTS + " WHERE "
                + KEY_EVENT_ID + " = " + Integer.toString(eventID_Int);

        dbWhole = this.getReadableDatabase();

        try {
            Cursor cursor = dbWhole.rawQuery(query, null);
            cursor.moveToFirst();
            tempEvent.setEventID_Int(cursor.getInt(cursor.getColumnIndex(KEY_EVENT_ID)));
            tempEvent.setEventEmail_Str(cursor.getString(cursor.getColumnIndex(KEY_EVENT_PROFILE_NAME)));
            tempEvent.setEventTeam_Str(cursor.getString(cursor.getColumnIndex(KEY_EVENT_TEAM_NAME)));
            tempEvent.setEventName_Str(cursor.getString(cursor.getColumnIndex(KEY_EVENT_NAME)));
            tempEvent.setEventLocation_Str(cursor.getString(cursor.getColumnIndex(KEY_EVENT_LOCATION)));
            tempEvent.setEventGun_Str(cursor.getString(cursor.getColumnIndex(KEY_EVENT_GUN)));
            tempEvent.setEventLoad_Str(cursor.getString(cursor.getColumnIndex(KEY_EVENT_LOAD)));
            tempEvent.setEventDate_Str(cursor.getString(cursor.getColumnIndex(KEY_EVENT_DATE)));
            tempEvent.setEventWeather_Str(cursor.getString(cursor.getColumnIndex(KEY_EVENT_WEATHER)));
            tempEvent.setEventNotes_Str(cursor.getString(cursor.getColumnIndex(KEY_EVENT_NOTES)));
        } catch (Exception e) {
            Log.d("JRW", e.toString());
        }

        dbWhole.close();

        return tempEvent;
    }

    public void updateEventInDB (EventClass e, int eventID_Int) {
        /*******************************************************************************************
         * Function: updateEventInDB
         *
         * Purpose: Function updates item from database based on database ID
         *
         * Parameters: e (IN) - object that holds information for updating item in the database
         *             eventID_Int (IN) - ID number of the item in the database
         *
         * Returns: None
         *
         ******************************************************************************************/

        dbWhole = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EVENT_PROFILE_NAME, e.getEventEmail_Str());
        values.put(KEY_EVENT_TEAM_NAME, e.getEventTeam_Str());
        values.put(KEY_EVENT_NAME, e.getEventName_Str());
        values.put(KEY_EVENT_LOCATION, e.getEventLocation_Str());
        values.put(KEY_EVENT_GUN, e.getEventGun_Str());
        values.put(KEY_EVENT_LOAD, e.getEventLoad_Str());
        values.put(KEY_EVENT_DATE, e.getEventDate_Str());
        values.put(KEY_EVENT_WEATHER, e.getEventWeather_Str());
        values.put(KEY_EVENT_NOTES, e.getEventNotes_Str());

        String whereClaus = KEY_EVENT_ID + " = " + Integer.toString(eventID_Int);

        dbWhole.update(TABLE_EVENTS, values, whereClaus, null);
        dbWhole.close();
    }

    public boolean isEventNameInDB (String email, String eventName, int ID) {
        /*******************************************************************************************
         * Function: isEventNameInDB
         *
         * Purpose: Function decides if event name is already in db for user
         *
         * Parameters: email (IN) - key string for finding profile
         *             eventName (IN) - string to find in database
         *             ID (IN) - ID of gun being passed in, this is to ignore this gun when checking,
         *                       if it is -1, it means event name is being added
         *
         * Returns: doesEventNameExist - returns true if the user already has an event under that name,
         *                               besides the event that shares the same ID
         *
         ******************************************************************************************/

        boolean doesEventNameExist = false;
        int currentID = -1;
        String currentEventName;
        String query = "SELECT * FROM " + TABLE_EVENTS
                + " WHERE " + KEY_EVENT_PROFILE_NAME + " = '" + email + "'";

        dbWhole = this.getReadableDatabase();
        Cursor cursor = dbWhole.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                currentEventName = cursor.getString(cursor.getColumnIndex(KEY_EVENT_NAME));
                currentID = cursor.getInt(cursor.getColumnIndex(KEY_EVENT_ID));

                if (currentEventName.equals(eventName) && currentID != ID) {
                    //name already exists
                    doesEventNameExist = true;
                    break;
                }
            } while (cursor.moveToNext());
        }

        dbWhole.close();
        return doesEventNameExist;
    }
}
