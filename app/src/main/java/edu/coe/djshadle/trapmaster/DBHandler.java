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
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "trapMasterDB.db";
    private SQLiteDatabase dbWhole;

    //Profile Table Stuff
    private static final String TABLE_PROFILES = "profiles";
    private static final String KEY_PROFILE_ID = "id";
    private static final String KEY_PROFILE_EMAIL = "profile_Email";  //Key

    //Shooter Table Stuff
    private static final String TABLE_SHOOTERS = "shooters";
    private static final String KEY_SHOOTER_ID = "id";
    private static final String KEY_SHOOTER_PROFILE_ID = "shooter_ProfileID";  //Key
    private static final String KEY_SHOOTER_NAME = "shooter_Name";  //Key

    //Gun Table Stuff
    private static final String TABLE_GUNS = "guns";
    private static final String KEY_GUN_ID = "id";
    private static final String KEY_GUN_PROFILE_ID = "gun_ProfileID";  //Key
    private static final String KEY_GUN_NICKNAME = "gun_Nickname";
    private static final String KEY_GUN_MODEL = "gun_Model";
    private static final String KEY_GUN_GAUGE = "gun_Gauge";
    private static final String KEY_GUN_NOTES = "gun_Notes";

    //Load Table Stuff
    private static final String TABLE_LOADS = "loads";
    private static final String KEY_LOAD_ID = "id";
    private static final String KEY_LOAD_PROFILE_ID = "load_ProfileID";  //Key
    private static final String KEY_LOAD_NICKNAME = "load_Nickname";
    private static final String KEY_LOAD_BRAND = "load_Brand";
    private static final String KEY_LOAD_GAUGE = "load_Gauge";
    private static final String KEY_LOAD_LENGTH = "load_Length";
    private static final String KEY_LOAD_GRAIN = "load_Grain";
    private static final String KEY_LOAD_NOTES = "load_Notes";

    //Round Table Stuff
    private static final String TABLE_ROUND = "ROUND";
    private static final String KEY_ROUND_ID = "id";
    private static final String KEY_ROUND_SHOOTER_ID = "ROUND_ShooterID";  //Key
    private static final String KEY_ROUND_ROUND = "ROUND_Round";
    private static final String KEY_ROUND_SCORE = "ROUND_Score";
    private static final String KEY_ROUND_HIT_MISS_STATES = "ROUND_HitMiss";
    private static final String KEY_ROUND_GUN_ID = "ROUND_GunID";
    private static final String KEY_ROUND_LOAD_ID = "ROUND_LoadID";
    private static final String KEY_ROUND_NOTES = "ROUND_Notes";

    //Match Table Stuff
    private static final String TABLE_MATCH = "matches";
    private static final String KEY_MATCH_ID = "id";
    private static final String KEY_MATCH_SHOOTER_ID = "match_ShooterID";  //Key
    private static final String KEY_MATCH_TEAM_ID = "match_TeamID";
    private static final String KEY_MATCH_EVENT_ID = "match_EventID";
    private static final String KEY_MATCH_ROUND_IDS = "match_RoundIDs";
    private static final String KEY_MATCH_SCORE = "match_Score";
    private static final String KEY_MATCH_NOTES = "match_Notes";

    //Event Table Stuff
    private static final String TABLE_EVENTS = "events";
    private static final String KEY_EVENT_ID = "id";
    private static final String KEY_EVENT_PROFILE_ID = "event_ProfilfeID";  //Key
    private static final String KEY_EVENT_NAME = "event_Name";
    private static final String KEY_EVENT_LOCATION = "event_Location";
    private static final String KEY_EVENT_DATE = "event_Date";
    private static final String KEY_EVENT_WEATHER = "event_Weather";
    private static final String KEY_EVENT_NOTES = "event_Notes";

    //Location Table Stuff
    private static final String TABLE_LOCATION = "location";
    private static final String KEY_LOCATION_ID = "id";
    private static final String KEY_LOCATION_PROFILE_ID = "location_ProfileID";  //Key
    private static final String KEY_LOCATION_NAME = "location_Name";

    //Team Table Stuff
    private static final String TABLE_TEAM = "team";
    private static final String KEY_TEAM_ID = "id";
    private static final String KEY_TEAM_NAME = "team_Name";  //Key
    private static final String KEY_TEAM_COACH = "team_Coach"; // Key?
    private static final String KEY_TEAM_LIST = "team_List";

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
                + KEY_PROFILE_EMAIL + " TEXT"
                + ")";
        db.execSQL(CREATE_PROFILE_TABLE);

        //Shooter Table
        String CREATE_SHOOTER_TABLE = "CREATE TABLE " + TABLE_SHOOTERS + " ("
                + KEY_SHOOTER_ID + " INTEGER PRIMARY KEY,"
                + KEY_SHOOTER_PROFILE_ID + " INTEGER,"
                + KEY_SHOOTER_NAME + " TEXT"
                + ")";
        db.execSQL(CREATE_SHOOTER_TABLE);

        //Gun Table
        String CREATE_GUN_TABLE = "CREATE TABLE " + TABLE_GUNS + " ("
                + KEY_GUN_ID + " INTEGER PRIMARY KEY,"
                + KEY_GUN_PROFILE_ID + " INTEGER,"
                + KEY_GUN_NICKNAME + " TEXT,"
                + KEY_GUN_MODEL + " TEXT,"
                + KEY_GUN_GAUGE + " TEXT,"
                + KEY_GUN_NOTES + " TEXT"
                + ")";
        db.execSQL(CREATE_GUN_TABLE);

        //Load Table
        String CREATE_LOAD_TABLE = "CREATE TABLE " + TABLE_LOADS + " ("
                + KEY_LOAD_ID + " INTEGER PRIMARY KEY,"
                + KEY_LOAD_PROFILE_ID + " INTEGER,"
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
                + KEY_EVENT_PROFILE_ID + " INTEGER,"
                + KEY_EVENT_NAME + " TEXT,"
                + KEY_EVENT_LOCATION + " TEXT,"
                + KEY_EVENT_DATE + " TEXT,"
                + KEY_EVENT_WEATHER + " TEXT,"
                + KEY_EVENT_NOTES + " TEXT"
                + ")";
        db.execSQL(CREATE_EVENT_TABLE);

        //Location Table
        String CREATE_LOCATION_TABLE = "CREATE TABLE " + TABLE_LOCATION + " ("
                + KEY_LOCATION_ID + " INTEGER PRIMARY KEY,"
                + KEY_LOCATION_PROFILE_ID + " INTEGER,"
                + KEY_LOCATION_NAME + " TEXT"
                + ")";
        db.execSQL(CREATE_LOCATION_TABLE);

        //Round Table
        String CREATE_ROUND_TABLE = "CREATE TABLE " + TABLE_ROUND + " ("
                + KEY_ROUND_ID + " INTEGER PRIMARY KEY,"
                + KEY_ROUND_SHOOTER_ID + " INTEGER,"
                + KEY_ROUND_ROUND + " INTEGER,"
                + KEY_ROUND_SCORE + " INTEGER,"
                + KEY_ROUND_HIT_MISS_STATES + " TEXT,"
                + KEY_ROUND_GUN_ID + " INTEGER,"
                + KEY_ROUND_LOAD_ID + " INTEGER,"
                + KEY_ROUND_NOTES + " TEXT"
                + ")";
        db.execSQL(CREATE_ROUND_TABLE);

        //Match Table
        String CREATE_MATCH_TABLE = "CREATE TABLE " + TABLE_MATCH + " ("
                + KEY_MATCH_ID + " INTEGER PRIMARY KEY,"
                + KEY_MATCH_SHOOTER_ID + " INTEGER,"
                + KEY_MATCH_TEAM_ID + " INTEGER,"
                + KEY_MATCH_EVENT_ID + " INTEGER,"
                + KEY_MATCH_ROUND_IDS + " TEXT,"
                + KEY_MATCH_SCORE + " INTEGER,"
                + KEY_MATCH_NOTES + " TEXT"
                + ")";
        db.execSQL(CREATE_MATCH_TABLE);

        //Team Table
        String CREATE_TEAM_TABLE = "CREATE TABLE " + TABLE_TEAM + " ("
                + KEY_TEAM_ID + " INTEGER PRIMARY KEY,"
                + KEY_TEAM_NAME + " TEXT,"
                + KEY_TEAM_COACH + " TEXT,"
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOOTERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GUNS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOADS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUND);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATCH);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEAM);
        onCreate(db);
    }

    //************************************* Profile Functions **************************************
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
        String query = "SELECT * FROM " + TABLE_PROFILES + " WHERE " + KEY_PROFILE_EMAIL + " = '" + email + "'";
        Cursor cursor = dbWhole.rawQuery(query, null);

        if ((cursor != null) && (cursor.getCount() > 0)) {
            // there is a user with the email in the db
            cursor.moveToFirst();

            tempProfile.setProfileID_Int(cursor.getInt(cursor.getColumnIndex(KEY_PROFILE_ID)));
            tempProfile.setProfileEmail_Str(cursor.getString(cursor.getColumnIndex(KEY_PROFILE_EMAIL)));
        } else {
            // there is not a user in the db with this email
            tempProfile.setProfileID_Int(-1);
            tempProfile.setProfileEmail_Str(email);
        }

        dbWhole.close();
        return tempProfile;
    }

    public ProfileClass getProfileFromDB (int profileID_Int){
        /*******************************************************************************************
         * Function: getProfileFromDB
         *
         * Purpose: Function gets a profile based on the email provided
         *
         * Parameters: profileID_Int (IN) - key ID for finding profile
         *
         * Returns: tempProfile - variable that contains all info from database for this email
         *          TODO: make this return false or -1 or indicate if profile not found
         *
         ******************************************************************************************/

        ProfileClass tempProfile = new ProfileClass();

        dbWhole = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_PROFILES + " WHERE " + KEY_PROFILE_ID + " = '" + profileID_Int + "'";
        Cursor cursor = dbWhole.rawQuery(query, null);

        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();

            tempProfile.setProfileID_Int(cursor.getInt(cursor.getColumnIndex(KEY_PROFILE_ID)));
            tempProfile.setProfileEmail_Str(cursor.getString(cursor.getColumnIndex(KEY_PROFILE_EMAIL)));
        }

        dbWhole.close();
        return tempProfile;
    }

    public long insertProfileInDB (ProfileClass p){
        /*******************************************************************************************
         * Function: insertProfileInDB
         *
         * Purpose: Function inserts a profile into the database based on the info provided in p
         *
         * Parameters: p (IN) - ProfileClass object to insert into database
         *
         * Returns: profileID_Long (OUT) - returns ID generated for insert, -1 if error occurred
         *
         ******************************************************************************************/

        dbWhole = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PROFILE_EMAIL, p.getProfileEmail_Str());

        long profileID_Long = dbWhole.insert(TABLE_PROFILES, null, values);
        dbWhole.close();

        return profileID_Long;
    }

    //************************************** Round Functions ***************************************
    public RoundClass getRoundInDB (int roundID_Int) {
        /*******************************************************************************************
         * Function: getRoundInDB
         *
         * Purpose: Function returns the ID number of a shot in the database
         *
         * Parameters: roundID_Int (IN) - ID of the round information to return
         *
         * Returns: tempRound - Information of the shot stored at shotID_Int
         *
         ******************************************************************************************/

        RoundClass tempRound = new RoundClass();
        String query = "SELECT * FROM " + TABLE_ROUND + " WHERE "
                + KEY_ROUND_ID + " = " + Integer.toString(roundID_Int);

        dbWhole = this.getReadableDatabase();

        try {
            Cursor cursor = dbWhole.rawQuery(query, null);
            cursor.moveToFirst();
            tempRound.setRoundID_Int(cursor.getInt(cursor.getColumnIndex(KEY_ROUND_ID)));
            tempRound.setRoundShooterID_Int(cursor.getInt(cursor.getColumnIndex(KEY_ROUND_SHOOTER_ID)));
            tempRound.setRoundGunID_Int(cursor.getInt(cursor.getColumnIndex(KEY_ROUND_GUN_ID)));
            tempRound.setRoundLoadID_Int(cursor.getInt(cursor.getColumnIndex(KEY_ROUND_LOAD_ID)));
            tempRound.setRoundScore_Int(cursor.getInt(cursor.getColumnIndex(KEY_ROUND_SCORE)));
            tempRound.setRoundRound_Int(cursor.getInt(cursor.getColumnIndex(KEY_ROUND_ROUND)));
            tempRound.setRoundHitMiss_Array(cursor.getString(cursor.getColumnIndex(KEY_ROUND_HIT_MISS_STATES)));
            tempRound.setRoundNotes_Str(cursor.getString(cursor.getColumnIndex(KEY_ROUND_NOTES)));
        } catch (Exception e) {
            Log.d("JRW", e.toString());
        }

        dbWhole.close();

        return tempRound;
    }

    public long insertRoundInDB (RoundClass s) {
        /*******************************************************************************************
         * Function: insertRoundInDB
         *
         * Purpose: Function inserts information from RoundClass object into database
         *
         * Parameters: s (IN) - round class object that holds information to put in database
         *
         * Returns: roundID_Long (OUT) - returns ID generated for insert, -1 if error occurred
         *
         ******************************************************************************************/

        dbWhole = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ROUND_SHOOTER_ID, s.getRoundShooterID_Int());
        values.put(KEY_ROUND_GUN_ID, s.getRoundGunID_Int());
        values.put(KEY_ROUND_LOAD_ID, s.getRoundLoadID_Int());
        values.put(KEY_ROUND_SCORE, s.getRoundScore_Int());
        values.put(KEY_ROUND_ROUND, s.getRoundRound_Int());
        values.put(KEY_ROUND_HIT_MISS_STATES, s.getRoundHitMiss_Str());
        values.put(KEY_ROUND_NOTES, s.getRoundNotes_Str());

        long roundID_Long = dbWhole.insert(TABLE_ROUND, null, values);
        dbWhole.close();

        return roundID_Long;
    }

    public void deleteRoundInDB (int roundID_int) {
        /*******************************************************************************************
         * Function: deleteRoundInDB
         *
         * Purpose: Function deletes item from database based on roundID_int
         *
         * Parameters: roundID_int (IN) - ID number for round
         *
         * Returns: None
         *
         ******************************************************************************************/

        dbWhole = this.getWritableDatabase();

        String whereClause_Str = "(" + KEY_ROUND_ID + " = '" + roundID_int + "')";
        try {
            dbWhole.delete(TABLE_ROUND, whereClause_Str, null);
        } catch (Exception e) {
            Log.d("JRW", e.toString());
        }

        dbWhole.close();
    }

    public void updateRoundInDB (RoundClass s) {
        /*******************************************************************************************
         * Function: updateRoundInDB
         *
         * Purpose: Function updates item from database based on database ID
         *
         * Parameters: s (IN) - object that holds information for updating item in the database
         *
         * Returns: None
         *
         ******************************************************************************************/

        dbWhole = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ROUND_SHOOTER_ID, s.getRoundShooterID_Int());
        values.put(KEY_ROUND_GUN_ID, s.getRoundGunID_Int());
        values.put(KEY_ROUND_LOAD_ID, s.getRoundLoadID_Int());
        values.put(KEY_ROUND_SCORE, s.getRoundScore_Int());
        values.put(KEY_ROUND_ROUND, s.getRoundRound_Int());
        values.put(KEY_ROUND_HIT_MISS_STATES, s.getRoundHitMiss_Str());
        values.put(KEY_ROUND_NOTES, s.getRoundNotes_Str());

        String whereClaus = KEY_ROUND_ID + " = " + Integer.toString(s.getRoundID_Int());

        dbWhole.update(TABLE_ROUND, values, whereClaus, null);
        dbWhole.close();
    }

    public ArrayList<RoundClass> getAllRoundsFromDB (int shooterID_Int) {
        /*******************************************************************************************
         * Function: getAllRoundsFromDB
         *
         * Purpose: Function gathers round information based on shooter ID provided
         *
         * Parameters: shooterID_Int (IN) - key ID for receiving round information
         *
         * Returns: tempRound_List - returns list of RoundClass object with database information
         *
         ******************************************************************************************/

        dbWhole = this.getReadableDatabase();

        ArrayList<RoundClass> tempRound_List = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_ROUND + " WHERE " + KEY_ROUND_SHOOTER_ID +
                " = '" + shooterID_Int + "'";

        Cursor cursor = dbWhole.rawQuery(query, null);

        while (cursor.moveToNext()) {
            RoundClass tempRound = new RoundClass();
            tempRound.setRoundID_Int(cursor.getInt(cursor.getColumnIndex(KEY_ROUND_ID)));
            tempRound.setRoundShooterID_Int(cursor.getInt(cursor.getColumnIndex(KEY_ROUND_SHOOTER_ID)));
            tempRound.setRoundGunID_Int(cursor.getInt(cursor.getColumnIndex(KEY_ROUND_GUN_ID)));
            tempRound.setRoundLoadID_Int(cursor.getInt(cursor.getColumnIndex(KEY_ROUND_LOAD_ID)));
            tempRound.setRoundScore_Int(cursor.getInt(cursor.getColumnIndex(KEY_ROUND_SCORE)));
            tempRound.setRoundRound_Int(cursor.getInt(cursor.getColumnIndex(KEY_ROUND_ROUND)));
            tempRound.setRoundHitMiss_Array(cursor.getString(cursor.getColumnIndex(KEY_ROUND_HIT_MISS_STATES)));
            tempRound.setRoundNotes_Str(cursor.getString(cursor.getColumnIndex(KEY_ROUND_NOTES)));

            tempRound_List.add(tempRound);
        }

        dbWhole.close();
        return tempRound_List;
    }

    //************************************** Match Functions ***************************************
    public MatchClass getMatchInDB (int matchID_Int) {
        /*******************************************************************************************
         * Function: getMatchInDB
         *
         * Purpose: Function returns the ID number of a match in the database
         *
         * Parameters: matchID_Int (IN) - ID of the match information to return
         *
         * Returns: tempMatch - Information of the match stored at matchID_Int
         *
         ******************************************************************************************/

        MatchClass tempMatch = new MatchClass();
        String query = "SELECT * FROM " + TABLE_MATCH + " WHERE "
                + KEY_MATCH_ID + " = " + Integer.toString(matchID_Int);

        dbWhole = this.getReadableDatabase();

        try {
            Cursor cursor = dbWhole.rawQuery(query, null);
            cursor.moveToFirst();
            tempMatch.setMatchID_Int(cursor.getInt(cursor.getColumnIndex(KEY_MATCH_ID)));
            tempMatch.setMatchShooterID_Int(cursor.getInt(cursor.getColumnIndex(KEY_MATCH_SHOOTER_ID)));
            tempMatch.setMatchTeamID_Int(cursor.getInt(cursor.getColumnIndex(KEY_MATCH_TEAM_ID)));
            tempMatch.setMatchEventID_Int(cursor.getInt(cursor.getColumnIndex(KEY_MATCH_EVENT_ID)));
            tempMatch.setMatchRoundIDS_Array(cursor.getString(cursor.getColumnIndex(KEY_MATCH_ROUND_IDS)));
            tempMatch.setMatchScore_Int(cursor.getInt(cursor.getColumnIndex(KEY_MATCH_SCORE)));
            tempMatch.setMatchNotes_Str(cursor.getString(cursor.getColumnIndex(KEY_MATCH_NOTES)));
        } catch (Exception e) {
            Log.d("JRW", e.toString());
        }

        dbWhole.close();

        return tempMatch;
    }

    public long insertMatchInDB (MatchClass m) {
        /*******************************************************************************************
         * Function: insertMatchInDB
         *
         * Purpose: Function inserts information from MatchClass object into database
         *
         * Parameters: m (IN) - match class object that holds information to put in database
         *
         * Returns: matchID_Long (OUT) - returns ID generated for insert, -1 if error occurred
         *
         ******************************************************************************************/

        dbWhole = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MATCH_SHOOTER_ID, m.getMatchShooterID_Int());
        values.put(KEY_MATCH_TEAM_ID, m.getMatchTeamID_Int());
        values.put(KEY_MATCH_EVENT_ID, m.getMatchEventID_Int());
        values.put(KEY_MATCH_ROUND_IDS, m.getMatchRoundIDS_Str());
        values.put(KEY_MATCH_SCORE, m.getMatchScore_Int());
        values.put(KEY_MATCH_NOTES, m.getMatchNotes_Str());

        long matchID_Long = dbWhole.insert(TABLE_MATCH, null, values);
        dbWhole.close();

        return matchID_Long;
    }

    public void deleteMatchInDB (int matchID_Int) {
        /*******************************************************************************************
         * Function: deleteMatchInDB
         *
         * Purpose: Function deletes item from database based on matchID_Int
         *
         * Parameters: matchID_Int (IN) - ID number for match
         *
         * Returns: None
         *
         ******************************************************************************************/

        dbWhole = this.getWritableDatabase();

        String whereClause_Str = "(" + KEY_MATCH_ID + " = '" + matchID_Int + "')";
        try {
            dbWhole.delete(TABLE_MATCH, whereClause_Str, null);
        } catch (Exception e) {
            Log.d("JRW", e.toString());
        }

        dbWhole.close();
    }

    public void updateMatchInDB (MatchClass m) {
        /*******************************************************************************************
         * Function: updateMatchInDB
         *
         * Purpose: Function updates item from database based on database ID
         *
         * Parameters: m (IN) - object that holds information for updating item in the database
         *
         * Returns: None
         *
         ******************************************************************************************/

        dbWhole = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MATCH_SHOOTER_ID, m.getMatchShooterID_Int());
        values.put(KEY_MATCH_TEAM_ID, m.getMatchTeamID_Int());
        values.put(KEY_MATCH_EVENT_ID, m.getMatchEventID_Int());
        values.put(KEY_MATCH_ROUND_IDS, m.getMatchRoundIDS_Str());
        values.put(KEY_MATCH_SCORE, m.getMatchScore_Int());
        values.put(KEY_MATCH_NOTES, m.getMatchNotes_Str());

        String whereClaus = KEY_MATCH_ID + " = " + Integer.toString(m.getMatchID_Int());

        dbWhole.update(TABLE_MATCH, values, whereClaus, null);
        dbWhole.close();
    }

    public ArrayList<MatchClass> getAllMatchesFromDB (int shooterID_Int) {
        /*******************************************************************************************
         * Function: getAllMatchesFromDB
         *
         * Purpose: Function gathers match information based on shooter ID provided
         *
         * Parameters: shooterID_Int (IN) - key ID for receiving information
         *
         * Returns: tempMatch_List - returns list of objects with database information
         *
         ******************************************************************************************/

        dbWhole = this.getReadableDatabase();

        ArrayList<MatchClass> tempMatch_List = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_MATCH + " WHERE " + KEY_MATCH_SHOOTER_ID +
                " = '" + shooterID_Int + "'";

        Cursor cursor = dbWhole.rawQuery(query, null);

        while (cursor.moveToNext()) {
            MatchClass tempMatch = new MatchClass();
            tempMatch.setMatchID_Int(cursor.getInt(cursor.getColumnIndex(KEY_MATCH_ID)));
            tempMatch.setMatchShooterID_Int(cursor.getInt(cursor.getColumnIndex(KEY_MATCH_SHOOTER_ID)));
            tempMatch.setMatchTeamID_Int(cursor.getInt(cursor.getColumnIndex(KEY_MATCH_TEAM_ID)));
            tempMatch.setMatchEventID_Int(cursor.getInt(cursor.getColumnIndex(KEY_MATCH_EVENT_ID)));
            tempMatch.setMatchRoundIDS_Array(cursor.getString(cursor.getColumnIndex(KEY_MATCH_ROUND_IDS)));
            tempMatch.setMatchScore_Int(cursor.getInt(cursor.getColumnIndex(KEY_MATCH_SCORE)));
            tempMatch.setMatchNotes_Str(cursor.getString(cursor.getColumnIndex(KEY_MATCH_NOTES)));

            tempMatch_List.add(tempMatch);
        }

        dbWhole.close();
        return tempMatch_List;
    }

    //************************************** Shooter Functions *************************************
    public ShooterClass getShooterInDB (int shooterID_Int) {
        /*******************************************************************************************
         * Function: getShooterInDB
         *
         * Purpose: Function returns the ID number of a shooter in the database
         *
         * Parameters: shooterID_Int (IN) - ID of the shooter information to return
         *
         * Returns: tempShooter - Information of the shooter stored at ID
         *
         ******************************************************************************************/

        ShooterClass tempShooter = new ShooterClass();
        String query = "SELECT * FROM " + TABLE_SHOOTERS + " WHERE "
                + KEY_SHOOTER_ID + " = " + Integer.toString(shooterID_Int);

        dbWhole = this.getReadableDatabase();

        try {
            Cursor cursor = dbWhole.rawQuery(query, null);
            cursor.moveToFirst();
            tempShooter.setShooterID_Int(cursor.getInt(cursor.getColumnIndex(KEY_SHOOTER_ID)));
            tempShooter.setShooterProfileID_Int(cursor.getInt(cursor.getColumnIndex(KEY_SHOOTER_PROFILE_ID)));
            tempShooter.setShooterName_Str(cursor.getString(cursor.getColumnIndex(KEY_SHOOTER_NAME)));
        } catch (Exception e) {
            Log.d("JRW", e.toString());
        }

        dbWhole.close();

        return tempShooter;
    }

    public ShooterClass getShooterInDB (String shooterName_Str) {
        /*******************************************************************************************
         * Function: getShooterInDB
         *
         * Purpose: Function returns the ID number of a shooter in the database
         *
         * Parameters: shooterName_Str (IN) - name of the shooter information to return
         *
         * Returns: tempShooter - Information of the shooter stored at ID
         *
         ******************************************************************************************/

        ShooterClass tempShooter = new ShooterClass();
        String query = "SELECT * FROM " + TABLE_SHOOTERS + " WHERE "
                + KEY_SHOOTER_NAME + " = '" + shooterName_Str + "''";

        dbWhole = this.getReadableDatabase();

        try {
            Cursor cursor = dbWhole.rawQuery(query, null);
            cursor.moveToFirst();
            tempShooter.setShooterID_Int(cursor.getInt(cursor.getColumnIndex(KEY_SHOOTER_ID)));
            tempShooter.setShooterProfileID_Int(cursor.getInt(cursor.getColumnIndex(KEY_SHOOTER_PROFILE_ID)));
            tempShooter.setShooterName_Str(cursor.getString(cursor.getColumnIndex(KEY_SHOOTER_NAME)));
        } catch (Exception e) {
            Log.d("JRW", e.toString());
        }

        dbWhole.close();

        return tempShooter;
    }

    public long insertShooterInDB (ShooterClass s) {
        /*******************************************************************************************
         * Function: insertShooterInDB
         *
         * Purpose: Function inserts information from ShooterClass object into database
         *
         * Parameters: s (IN) - shooter class object that holds information to put in database
         *
         * Returns: shooterID_Long (OUT) - returns ID generated for insert, -1 if error occurred
         *
         ******************************************************************************************/

        dbWhole = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SHOOTER_PROFILE_ID, s.getShooterProfileID_Int());
        values.put(KEY_SHOOTER_NAME, s.getShooterName_Str());

        long shooterID_Long = dbWhole.insert(TABLE_SHOOTERS, null, values);
        dbWhole.close();

        return shooterID_Long;
    }

    public void deleteShooterInDB (int shooterID_Int) {
        /*******************************************************************************************
         * Function: deleteShooterInDB
         *
         * Purpose: Function deletes item from database based on shooter ID
         *
         * Parameters: shooterID_Int (IN) - ID of shooter to remove
         *
         * Returns: None
         *
         ******************************************************************************************/

        dbWhole = this.getWritableDatabase();

        String whereClause_Str = "(" + KEY_SHOOTER_ID + " = '" + Integer.toString(shooterID_Int) + "')";
        try {
            dbWhole.delete(TABLE_SHOOTERS, whereClause_Str, null);
        } catch (Exception e) {
            Log.d("JRW", e.toString());
        }

        dbWhole.close();
    }

    public void updateShooterInDB (ShooterClass s) {
        /*******************************************************************************************
         * Function: updateShooterInDB
         *
         * Purpose: Function updates item from database based on database ID
         *
         * Parameters: s (IN) - object that holds information for updating item in the database
         *
         * Returns: None
         *
         ******************************************************************************************/

        dbWhole = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SHOOTER_PROFILE_ID, s.getShooterProfileID_Int());
        values.put(KEY_SHOOTER_NAME, s.getShooterName_Str());

        String whereClaus = KEY_SHOOTER_ID + " = " + Integer.toString(s.getShooterID_Int());

        dbWhole.update(TABLE_SHOOTERS, values, whereClaus, null);
        dbWhole.close();
    }

    public ArrayList<ShooterClass> getAllShooterFromDB (int profileID_Int) {
        /*******************************************************************************************
         * Function: getAllShooterFromDB
         *
         * Purpose: Function gathers shooter information
         *
         * Parameters: profileID_Int (IN) - id to base where clause on
         *
         * Returns: tempShooter_List - returns list of ShooterClass object with database information
         *
         ******************************************************************************************/

        dbWhole = this.getReadableDatabase();

        ArrayList<ShooterClass> tempShooter_List = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_SHOOTERS + " WHERE " + KEY_SHOOTER_PROFILE_ID +
                " = '" + profileID_Int + "'";

        Cursor cursor = dbWhole.rawQuery(query, null);

        while (cursor.moveToNext()) {
            ShooterClass temp_Shooter = new ShooterClass();

            temp_Shooter.setShooterID_Int(cursor.getInt(cursor.getColumnIndex(KEY_SHOOTER_ID)));
            temp_Shooter.setShooterProfileID_Int(cursor.getInt(cursor.getColumnIndex(KEY_SHOOTER_PROFILE_ID)));
            temp_Shooter.setShooterName_Str(cursor.getString(cursor.getColumnIndex(KEY_SHOOTER_NAME)));
            tempShooter_List.add(temp_Shooter);
        }

        dbWhole.close();
        return tempShooter_List;
    }

    public boolean isShooterInDB (int profileID_Int, String shooterName_Str, int ID) {
        /*******************************************************************************************
         * Function: isShooterInDB
         *
         * Purpose: Function decides if shooter name is already in db for user
         *
         * Parameters: profileID_Int (IN) - ID of profile to search for shooters
         *             shooterName_Str (IN) - string to find in database
         *             ID (IN) - ID of shooter being passed in, this is to ignore this load when checking
         *                       ID = -1 if new user
         *
         * Returns: doesShooterExist - returns true if the user already has a shooter under that name,
         *                              besides the shooter that shares the same ID
         *
         ******************************************************************************************/

        boolean doesShooterExist = false;
        int currentID_Int = -1;
        String currentShooterName_Str;
        String query = "SELECT * FROM " + TABLE_SHOOTERS + " WHERE " + KEY_SHOOTER_PROFILE_ID +
                " = '" + profileID_Int + "'";

        dbWhole = this.getReadableDatabase();
        Cursor cursor = dbWhole.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                currentShooterName_Str = cursor.getString(cursor.getColumnIndex(KEY_SHOOTER_NAME));
                currentID_Int = cursor.getInt(cursor.getColumnIndex(KEY_SHOOTER_ID));

                if (currentShooterName_Str.equals(shooterName_Str) && currentID_Int != ID) {
                    //name already exists
                    doesShooterExist = true;
                    break;
                }
            } while (cursor.moveToNext());
        }

        dbWhole.close();
        return doesShooterExist;
    }

    //*************************************** Load Functions ***************************************
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
            tempLoad.setLoadProfileID_Int(cursor.getInt(cursor.getColumnIndex(KEY_LOAD_PROFILE_ID)));
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

    public long insertLoadInDB (LoadClass l) {
        /*******************************************************************************************
         * Function: insertLoadInDB
         *
         * Purpose: Function inserts information from LoadClass object into database
         *
         * Parameters: l (IN) - load class object that holds information to put in database
         *
         * Returns: loadID_Long (OUT) - returns ID generated for insert, -1 if error occurred
         *
         ******************************************************************************************/

        dbWhole = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LOAD_PROFILE_ID, l.getLoadProfileID_Int());
        values.put(KEY_LOAD_NICKNAME, l.getLoadNickname_Str());
        values.put(KEY_LOAD_BRAND, l.getLoadBrand_Str());
        values.put(KEY_LOAD_GAUGE, l.getLoadGauge_Str());
        values.put(KEY_LOAD_GRAIN, l.getLoadGrain_Str());
        values.put(KEY_LOAD_LENGTH, l.getLoadLength_Str());
        values.put(KEY_LOAD_NOTES, l.getLoadNotes_Str());

        long loadID_Long = dbWhole.insert(TABLE_LOADS, null, values);
        dbWhole.close();

        return loadID_Long;
    }

    public void deleteLoadInDB (int loadID_Int) {
        /*******************************************************************************************
         * Function: deleteLoadInDB
         *
         * Purpose: Function deletes item from database based on load ID
         *
         * Parameters: loadID_Int (IN) - ID of load to remove
         *
         * Returns: None
         *
         ******************************************************************************************/

        dbWhole = this.getWritableDatabase();

        String whereClause_Str = "(" + KEY_LOAD_ID + " = '" + Integer.toString(loadID_Int) + "')";
        try {
            dbWhole.delete(TABLE_LOADS, whereClause_Str, null);
        } catch (Exception e) {
            Log.d("JRW", e.toString());
        }

        dbWhole.close();
    }

    public void updateLoadInDB (LoadClass l) {
        /*******************************************************************************************
         * Function: updateLoadInDB
         *
         * Purpose: Function updates item from database based on database ID
         *
         * Parameters: l (IN) - object that holds information for updating item in the database
         *
         * Returns: None
         *
         ******************************************************************************************/

        dbWhole = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LOAD_PROFILE_ID, l.getLoadProfileID_Int());
        values.put(KEY_LOAD_NICKNAME, l.getLoadNickname_Str());
        values.put(KEY_LOAD_BRAND, l.getLoadBrand_Str());
        values.put(KEY_LOAD_GAUGE, l.getLoadGauge_Str());
        values.put(KEY_LOAD_GRAIN, l.getLoadGrain_Str());
        values.put(KEY_LOAD_LENGTH, l.getLoadLength_Str());
        values.put(KEY_LOAD_NOTES, l.getLoadNotes_Str());

        String whereClaus = KEY_LOAD_ID + " = " + Integer.toString(l.getLoadID_Int());

        dbWhole.update(TABLE_LOADS, values, whereClaus, null);
        dbWhole.close();
    }

    public ArrayList<LoadClass> getAllLoadFromDB (int profileID_Int) {
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
        String query = "SELECT * FROM " + TABLE_LOADS + " WHERE " + KEY_LOAD_PROFILE_ID +
                " = '" + profileID_Int + "'";

        Cursor cursor = dbWhole.rawQuery(query, null);

        while (cursor.moveToNext()) {
            LoadClass tempLoad = new LoadClass();
            tempLoad.setLoadID_Int(cursor.getInt(cursor.getColumnIndex(KEY_LOAD_ID)));
            tempLoad.setLoadProfileID_Int(cursor.getInt(cursor.getColumnIndex(KEY_LOAD_PROFILE_ID)));
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

    public boolean isLoadNicknameInDB (int profileID_Int, String nickname, int ID) {
        /*******************************************************************************************
         * Function: isLoadNicknameInDB
         *
         * Purpose: Function decides if nickname is already in db for user
         *
         * Parameters: profileID_Int (IN) - key int for finding profile
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
                + KEY_LOAD_PROFILE_ID + " = '" + profileID_Int + "'";

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
            tempGun.setGunProfileID_Int(cursor.getInt(cursor.getColumnIndex(KEY_GUN_PROFILE_ID)));
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

    public long insertGunInDB (GunClass g) {
        /*******************************************************************************************
         * Function: insertGunInDB
         *
         * Purpose: Function inserts information from GunClass object into database
         *
         * Parameters: g (IN) - g class object that holds information to put in database
         *
         * Returns: gunID_Long (OUT) - returns ID generated for insert, -1 if error occurred
         *
         ******************************************************************************************/

        dbWhole = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_GUN_PROFILE_ID, g.getGunProfileID_Int());
        values.put(KEY_GUN_NICKNAME, g.getGunNickname_Str());
        values.put(KEY_GUN_MODEL, g.getGunModel_Str());
        values.put(KEY_GUN_GAUGE, g.getGunGauge_Str());
        values.put(KEY_GUN_NOTES, g.getGunNotes_Str());

        long gunID_Long = dbWhole.insert(TABLE_GUNS, null, values);
        dbWhole.close();

        return gunID_Long;
    }

    public void deleteGunInDB (int gunID_Int) {
        /*******************************************************************************************
         * Function: deleteGunInDB
         *
         * Purpose: Function deletes item from database based on gun ID
         *
         * Parameters: gunID_Int (IN) - ID of gun to delete
         *
         * Returns: None
         *
         ******************************************************************************************/

        dbWhole = this.getWritableDatabase();

        String whereClause_Str = "(" + KEY_GUN_ID + " = '" + Integer.toString(gunID_Int) + "')";
        try {
            dbWhole.delete(TABLE_GUNS, whereClause_Str, null);
        } catch (Exception e) {
            Log.d("JRW", e.toString());
        }

        dbWhole.close();
    }

    public void updateGunInDB (GunClass g) {
        /*******************************************************************************************
         * Function: updateGunInDB
         *
         * Purpose: Function updates item from database based on database ID
         *
         * Parameters: g (IN) - object that holds information for updating item in the database
         *
         * Returns: None
         *
         ******************************************************************************************/

        dbWhole = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_GUN_PROFILE_ID, g.getGunProfileID_Int());
        values.put(KEY_GUN_NICKNAME, g.getGunNickname_Str());
        values.put(KEY_GUN_MODEL, g.getGunModel_Str());
        values.put(KEY_GUN_GAUGE, g.getGunGauge_Str());
        values.put(KEY_GUN_NOTES, g.getGunNotes_Str());

        String whereClaus = KEY_GUN_ID + " = " + Integer.toString(g.getGunID_Int());

        dbWhole.update(TABLE_GUNS, values, whereClaus, null);
        dbWhole.close();
    }

    public ArrayList<GunClass> getAllGunFromDB (int profileID_Int) {
        /*******************************************************************************************
         * Function: getAllGunFromDB
         *
         * Purpose: Function gathers gun information based on ID provided
         *
         * Parameters: profileID_Int (IN) - key ID for receiving gun information
         *
         * Returns: tempGun_List - returns list of GunClass object with database information
         *
         ******************************************************************************************/

        dbWhole = this.getReadableDatabase();

        ArrayList<GunClass> tempGun_List = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_GUNS + " WHERE " + KEY_GUN_PROFILE_ID +
                " = '" + profileID_Int + "'";

        Cursor cursor = dbWhole.rawQuery(query, null);

        while (cursor.moveToNext()) {
            GunClass tempGun = new GunClass();
            tempGun.setGunID_Int(cursor.getInt(cursor.getColumnIndex(KEY_GUN_ID)));
            tempGun.setGunProfileID_Int(cursor.getInt(cursor.getColumnIndex(KEY_GUN_PROFILE_ID)));
            tempGun.setGunNickname_Str(cursor.getString(cursor.getColumnIndex(KEY_GUN_NICKNAME)));
            tempGun.setGunModel_Str(cursor.getString(cursor.getColumnIndex(KEY_GUN_MODEL)));
            tempGun.setGunGauge_Str(cursor.getString(cursor.getColumnIndex(KEY_GUN_GAUGE)));
            tempGun.setGunNotes_Str(cursor.getString(cursor.getColumnIndex(KEY_GUN_NOTES)));

            tempGun_List.add(tempGun);
        }

        dbWhole.close();
        return tempGun_List;
    }

    public boolean isGunNicknameInDB (int profileID_Int, String nickname, int ID) {
        /*******************************************************************************************
         * Function: isGunNicknameInDB
         *
         * Purpose: Function decides if nickname is already in db for user
         *
         * Parameters: profileID_Int (IN) - key ID for finding profile
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
                + " WHERE " + KEY_GUN_PROFILE_ID + " = '" + profileID_Int + "'";

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
    public EventClass getEventInDB (int eventID_Int) {
        /*******************************************************************************************
         * Function: getEventInDB
         *
         * Purpose: Function returns an event in the database by the ID number
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
            tempEvent.setEventProfileID_Int(cursor.getInt(cursor.getColumnIndex(KEY_EVENT_PROFILE_ID)));
            tempEvent.setEventName_Str(cursor.getString(cursor.getColumnIndex(KEY_EVENT_NAME)));
            tempEvent.setEventLocation_Str(cursor.getString(cursor.getColumnIndex(KEY_EVENT_LOCATION)));
            tempEvent.setEventDate_Str(cursor.getString(cursor.getColumnIndex(KEY_EVENT_DATE)));
            tempEvent.setEventWeather_Str(cursor.getString(cursor.getColumnIndex(KEY_EVENT_WEATHER)));
            tempEvent.setEventNotes_Str(cursor.getString(cursor.getColumnIndex(KEY_EVENT_NOTES)));
        } catch (Exception e) {
            Log.d("JRW", "getEventInDB: " + e.toString());
        }

        dbWhole.close();

        return tempEvent;
    }

    public long insertEventInDB (EventClass e) {
        /*******************************************************************************************
         * Function: insertEventInDB
         *
         * Purpose: Function inserts information from EventClass object into database
         *
         * Parameters: e (IN) - event class object that holds information to put in database
         *
         * Returns: eventID_Long (OUT) - returns ID generated for insert, -1 if error occurred
         *
         ******************************************************************************************/

        dbWhole = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EVENT_PROFILE_ID, e.getEventProfileID_Int());
        values.put(KEY_EVENT_NAME, e.getEventName_Str());
        values.put(KEY_EVENT_LOCATION, e.getEventLocation_Str());
        values.put(KEY_EVENT_DATE, e.getEventDate_Str());
        values.put(KEY_EVENT_WEATHER, e.getEventWeather_Str());
        values.put(KEY_EVENT_NOTES, e.getEventNotes_Str());

        long eventID_Long = dbWhole.insert(TABLE_EVENTS, null, values);
        dbWhole.close();

        return eventID_Long;
    }

    public void deleteEventInDB (int eventID_Int) {
        /*******************************************************************************************
         * Function: deleteEventInDB
         *
         * Purpose: Function deletes item from database based on event ID
         *
         * Parameters: eventID_Int (IN) - ID of event to remove
         *
         * Returns: None
         *
         ******************************************************************************************/

        dbWhole = this.getWritableDatabase();

        String whereClause_Str = "(" + KEY_EVENT_ID + " = '" + Integer.toString(eventID_Int) + "')";
        try {
            dbWhole.delete(TABLE_EVENTS, whereClause_Str, null);
        } catch (Exception e) {
            Log.d("JRW", e.toString());
        }

        dbWhole.close();
    }

    public void updateEventInDB (EventClass e) {
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
        values.put(KEY_EVENT_PROFILE_ID, e.getEventProfileID_Int());
        values.put(KEY_EVENT_NAME, e.getEventName_Str());
        values.put(KEY_EVENT_LOCATION, e.getEventLocation_Str());
        values.put(KEY_EVENT_DATE, e.getEventDate_Str());
        values.put(KEY_EVENT_WEATHER, e.getEventWeather_Str());
        values.put(KEY_EVENT_NOTES, e.getEventNotes_Str());

        String whereClaus = KEY_EVENT_ID + " = " + Integer.toString(e.getEventID_Int());

        dbWhole.update(TABLE_EVENTS, values, whereClaus, null);
        dbWhole.close();
    }

    public ArrayList<EventClass> getAllEventFromDB (int profileID_Int) {
        /*******************************************************************************************
         * Function: getAllEventFromDB
         *
         * Purpose: Function gathers event information based on email provided
         *
         * Parameters: profileID_Int (IN) - key ID for receiving event information
         *
         * Returns: tempEvent_List - returns list of EventClass object with database information
         *
         ******************************************************************************************/

        dbWhole = this.getReadableDatabase();

        ArrayList<EventClass> tempEvent_List = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_EVENTS + " WHERE " + KEY_EVENT_PROFILE_ID +
                " = '" + profileID_Int + "'";

        Cursor cursor = dbWhole.rawQuery(query, null);

        while (cursor.moveToNext()) {
            EventClass tempEvent = new EventClass();

            tempEvent.setEventID_Int(cursor.getInt(cursor.getColumnIndex(KEY_EVENT_ID)));
            tempEvent.setEventProfileID_Int(cursor.getInt(cursor.getColumnIndex(KEY_EVENT_PROFILE_ID)));
            tempEvent.setEventName_Str(cursor.getString(cursor.getColumnIndex(KEY_EVENT_NAME)));
            tempEvent.setEventLocation_Str(cursor.getString(cursor.getColumnIndex(KEY_EVENT_LOCATION)));
            tempEvent.setEventDate_Str(cursor.getString(cursor.getColumnIndex(KEY_EVENT_DATE)));
            tempEvent.setEventWeather_Str(cursor.getString(cursor.getColumnIndex(KEY_EVENT_WEATHER)));
            tempEvent.setEventNotes_Str(cursor.getString(cursor.getColumnIndex(KEY_EVENT_NOTES)));

            tempEvent_List.add(tempEvent);
        }

        dbWhole.close();
        return tempEvent_List;
    }

    public boolean isEventNameInDB (int profileID_Int, String eventName, int ID) {
        /*******************************************************************************************
         * Function: isEventNameInDB
         *
         * Purpose: Function decides if event name is already in db for user
         *
         * Parameters: profileID_Int (IN) - key ID for finding profile
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
                + " WHERE " + KEY_EVENT_PROFILE_ID + " = '" + profileID_Int + "'";

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
