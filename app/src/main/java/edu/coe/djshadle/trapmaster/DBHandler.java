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

public class DBHandler extends SQLiteOpenHelper {
    //********************************** Variables and Constants ***********************************
    //Database Variables & Constants
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "trapMaster.db";
    SQLiteDatabase dbWhole;

    //Profile Table Stuff
    private static final String TABLE_PROFILES = "profiles";
    private static final String KEY_PROFILE_ID = "id";
    private static final String KEY_PROFILE_USERNAME = "profileUsername";  //Key
    private static final String KEY_PROFILE_PASSWORD = "profilePassword";

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
    private static final String KEY_EVENT_SCORE = "eventScore";
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

        dbWhole = db;

        //Profile Table
        String CREATE_PROFILE_TABLE = "CREATE TABLE " + TABLE_PROFILES + "("
                + KEY_PROFILE_USERNAME + " TEXT PRIMARY KEY,"
                + KEY_PROFILE_ID + " INTEGER,"
                + KEY_PROFILE_PASSWORD + " TEXT" +
                ")";
        db.execSQL(CREATE_PROFILE_TABLE);

        //Gun Table
        String CREATE_GUN_TABLE = "CREATE TABLE " + TABLE_GUNS + "("
                + KEY_GUN_PROFILE_NAME + " TEXT PRIMARY KEY,"
                + KEY_GUN_ID + " INTEGER,"
                + KEY_GUN_NICKNAME + " TEXT,"
                + KEY_GUN_MODEL + " TEXT,"
                + KEY_GUN_GAUGE + " TEXT,"
                + KEY_GUN_NOTES + " TEXT"
                + ")";
        db.execSQL(CREATE_GUN_TABLE);


        //Load Table
        String CREATE_LOAD_TABLE = "CREATE TABLE " + TABLE_LOADS + "("
                + KEY_LOAD_PROFILE_NAME + " TEXT PRIMARY KEY,"
                + KEY_LOAD_ID + " INTEGER,"
                + KEY_LOAD_NICKNAME + " TEXT,"
                + KEY_LOAD_BRAND + " TEXT,"
                + KEY_LOAD_GAUGE + " TEXT,"
                + KEY_LOAD_LENGTH + " TEXT,"
                + KEY_LOAD_GRAIN + " TEXT,"
                + KEY_LOAD_NOTES + " TEXT"
                + ")";
        db.execSQL(CREATE_LOAD_TABLE);

        //Event Table Stuff
        String CREATE_EVENT_TABLE = "CREATE TABLE " + TABLE_EVENTS + "("
                + KEY_EVENT_PROFILE_NAME + " TEXT PRIMARY KEY,"
                + KEY_EVENT_ID + " INTEGER,"
                + KEY_EVENT_TEAM_NAME + " TEXT,"
                + KEY_EVENT_NAME + " TEXT,"
                + KEY_EVENT_LOCATION + " TEXT,"
                + KEY_EVENT_GUN + " TEXT,"
                + KEY_EVENT_LOAD + " TEXT,"
                + KEY_EVENT_DATE + " TEXT,"
                + KEY_EVENT_SCORE + " INTEGER,"
                + KEY_EVENT_WEATHER + " TEXT,"
                + KEY_EVENT_NOTES + " TEXT"
                + ")";
        db.execSQL(CREATE_EVENT_TABLE);


        //Location Table
        String CREATE_LOCATION_TABLE = "CREATE TABLE " + TABLE_LOCATION + "("
                + KEY_LOCATION_PROFILE_NAME + " TEXT PRIMARY KEY,"
                + KEY_LOCATION_ID + " INTEGER,"
                + KEY_LOCATION_NAME + " TEXT"
                + ")";
        db.execSQL(CREATE_LOCATION_TABLE);

        //Shot Table
        String CREATE_SHOT_TABLE = "CREATE TABLE " + TABLE_SHOT + "("
                + KEY_SHOT_PROFILE_NAME + " TEXT PRIMARY KEY,"
                + KEY_SHOT_ID + " INTEGER,"
                + KEY_SHOT_EVENT_NAME + " TEXT,"
                + KEY_SHOT_NUMBER + " TEXT,"
                + KEY_SHOT_HIT_MISS + " TEXT,"
                + KEY_SHOT_NOTES + " TEXT"
                + ")";
        db.execSQL(CREATE_SHOT_TABLE);

        //Team Table
        String CREATE_TEAM_TABLE = "CREATE TABLE " + TABLE_TEAM + "("
                + KEY_TEAM_NAME + " TEXT PRIMARY KEY,"
                + KEY_TEAM_ID + " INTEGER,"
                + KEY_TEAM_LIST + " TEXT"
                + ")";
        db.execSQL(CREATE_TEAM_TABLE);
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
        values.put(KEY_PROFILE_PASSWORD, p.getProfilePassword_Str());

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
        @SuppressLint("Recycle") Cursor cursor = dbWhole.rawQuery(query, null);

        if (cursor != null) {
            cursor.moveToFirst();

            tempProfile.setProfileID_Str(cursor.getString(cursor.getColumnIndex(KEY_PROFILE_ID)));
            tempProfile.setProfileEmail_Str(cursor.getString(cursor.getColumnIndex(KEY_PROFILE_USERNAME)));
            tempProfile.setProfilePassword_Str((cursor.getString(cursor.getColumnIndex(KEY_PROFILE_PASSWORD))));
        }

        dbWhole.close();
        return tempProfile;
    }

    public boolean doesPassMatchInDB(String email, String pass){
        /*******************************************************************************************
         * Function: doesPassMatchInDB
         *
         * Purpose: Function compares password to profile based on the email provided
         *
         * Parameters: email (IN) - key string for finding profile
         *             pass (IN) - password attempt
         *
         * Returns: doesPassMatch - returns true if the pass parameter matches password listed in db
         *
         ******************************************************************************************/

        String dbPass;
        boolean doesPassMatch = false;
        String query = "SELECT " + KEY_PROFILE_PASSWORD + " FROM " + TABLE_PROFILES + " WHERE " + KEY_PROFILE_USERNAME + " = '" + email + "'";

        dbWhole = this.getReadableDatabase();
        Cursor cursor = dbWhole.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                dbPass = cursor.getString(cursor.getColumnIndex(KEY_PROFILE_PASSWORD));

                if (dbPass.equals(pass)) {
                    //name already exists
                    doesPassMatch = true;
                    break;
                }
            } while (cursor.moveToNext());
        }

        dbWhole.close();
        return doesPassMatch;
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

    //************************************* Profile Functions **************************************
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

    public ShotClass getShotFromDB (String email) {
        /*******************************************************************************************
         * Function: getShotFromDB
         *
         * Purpose: Function gathers shot information based on email provided
         *
         * Parameters: email (IN) - key string for receiving shot information
         *
         * Returns: tempShot - returns ShotClass object with database information stored
         *
         ******************************************************************************************/

        ShotClass tempShot = new ShotClass();

        dbWhole = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_SHOT + " WHERE " + KEY_SHOT_PROFILE_NAME +
                " = '" + email + "'";
        @SuppressLint("Recycle") Cursor cursor = dbWhole.rawQuery(query, null);

        if (cursor != null) {
            cursor.moveToLast();

            tempShot.setShotEmail_Str(cursor.getString(cursor.getColumnIndex(KEY_SHOT_PROFILE_NAME)));
            tempShot.setShotEventName_Str(cursor.getString(cursor.getColumnIndex(KEY_SHOT_EVENT_NAME)));
            tempShot.setShotTotalNum_Str(cursor.getString(cursor.getColumnIndex(KEY_SHOT_NUMBER)));
            tempShot.setShotHitNum_Str(cursor.getString(cursor.getColumnIndex(KEY_SHOT_HIT_MISS)));
            tempShot.setShotNotes_Str(cursor.getString(cursor.getColumnIndex(KEY_SHOT_NOTES)));
        }
        dbWhole.close();
        return tempShot;
    }

}
