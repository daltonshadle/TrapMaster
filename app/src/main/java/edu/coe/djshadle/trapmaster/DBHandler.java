package edu.coe.djshadle.trapmaster;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Shadle on 6/23/2017.
 */

public class DBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "trapMaster.db";

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


    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
