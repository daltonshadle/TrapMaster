/***************************************************************************************************
 * FILENAME : PostEventActivity.java
 *
 * AUTHOR : Dalton Shadle
 *
 * DESCRIPTION : Holds functions for the Post Event activity of this application
 *
 * NOTES : N/A
 *
 * Copyright Dalton Shadle 2019.  All rights reserved.
 *
 **************************************************************************************************/


package edu.coe.djshadle.trapmaster;

//******************************************** Imports *********************************************
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class PostEventActivity extends AppCompatActivity {
    //********************************** Variables and Constants ***********************************
    // General Constants
    private String ACTIVITY_TITLE;
    private String CURRENT_USER_KEY;
    private String SHOOTER_LIST_KEY;
    private String SHOOTER_SCORE_LIST_KEY;
    private String NO_GUN_STRING;
    private String NO_LOAD_STRING;
    private final String TAG = "JRW";
    private final int TOTAL_NUM_SHOTS = 25;
    private final int EVENT_LIST_TAG = 2;

    // General Variables
    DBHandler db;
    private boolean isPortrait = true;
    private String mCurrentUserEmail_Str = "*********";
    private int numShooters_Int;
    private ArrayList<String> shooterName_List;
    private ArrayList<Integer> shooterScore_List;
    // event
    private TrapMasterListArrayAdapter mCustomEventList_Adapt;
    private String mEventName_Str = "";

    // UI References
    private ConstraintLayout mParent_Lay;
    private ListView mEventList_View;
    private ArrayList<PostEventItemClass> mShooterPostEventItems_List;
    private LinearLayout mPostEventItems_Lay;
    private ScrollView mPostEvent_Scroll;

    //Google Variables

    //************************************* Activity Functions *************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*******************************************************************************************
         * Function: onCreate
         *
         * Purpose: When activity is started, function initializes the activity with any
         *          saved instances
         *
         * Parameters: savedInstanceState (IN) - provides the saved instances from previous state
         *
         * Returns: None
         *
         ******************************************************************************************/

        super.onCreate(savedInstanceState);

        initializeConstants();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_post_event_portrait);
            isPortrait = true;
        }
        else {
            setContentView(R.layout.activity_post_event_landscape);
            isPortrait = false;
        }

        if (savedInstanceState != null) {

        } else {
            mCurrentUserEmail_Str = getIntent().getStringExtra(CURRENT_USER_KEY);
            shooterName_List = getIntent().getStringArrayListExtra(SHOOTER_LIST_KEY);
            shooterScore_List = getIntent().getIntegerArrayListExtra(SHOOTER_SCORE_LIST_KEY);
            numShooters_Int = shooterName_List.size();
        }

        initializeViews();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        /*******************************************************************************************
         * Function: onConfigurationChanged
         *
         * Purpose: Function called when configuration is changed (denoted by manifest file)
         *
         * Parameters: newConfig (IN) - contains information on configuration
         *
         * Returns: None
         *
         ******************************************************************************************/

        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_post_event_portrait);
            isPortrait = true;
        }
        else {
            setContentView(R.layout.activity_post_event_landscape);
            isPortrait = false;
        }

        initializeViews();

    }

    private void initializeConstants() {
        /*******************************************************************************************
         * Function: initializeConstants
         *
         * Purpose: When activity is started, function initializes the activity with constants
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        ACTIVITY_TITLE = getString(R.string.post_event_activity_title);
        CURRENT_USER_KEY = getString(R.string.current_user_key);
        SHOOTER_LIST_KEY = getString(R.string.shooter_list_key);
        SHOOTER_SCORE_LIST_KEY = getString(R.string.shooter_score_list_key);
        NO_GUN_STRING = "No gun for shoot";
        NO_LOAD_STRING = "No load for shoot";
    }

    //************************************* UI View Functions **************************************
    private void initializeViews() {
        /*******************************************************************************************
         * Function: initializeViews
         *
         * Purpose: Function initializes all variables and all UI views to a resource id
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        // Initializing database variable
        db = new DBHandler(getApplicationContext());

        // Set action bar title
        try {
            setTitle(ACTIVITY_TITLE);
        } catch (Exception e) {
            // Didn't work.
        }

        // Initialize Scroll
        mPostEvent_Scroll = findViewById(R.id.postEventItems_Scroll);
        mParent_Lay = findViewById(R.id.postEventParent_Lay);

        // Initializing Post Event Items List
        initializeShooterPostEventItems();

        // Initializing buttons
        FloatingActionButton mAddEvent_Btn = findViewById(R.id.postEventAddEvent_Btn);
        Button mSave_Btn = findViewById(R.id.postEventSave_Btn);
        Button mCancel_Btn = findViewById(R.id.postEventCancel_Btn);

        // Adding button onClickListeners
        mAddEvent_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add event action
                EventClass temp_Event = new EventClass();
                temp_Event.setEventID_Int(-1);
                temp_Event.setEventEmail_Str(mCurrentUserEmail_Str);
                temp_Event.editEventDialog(PostEventActivity.this, mCustomEventList_Adapt);
            }
        });

        mSave_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Save post event action

                // Get shot notes to save, save even if it is empty

                // Save score to db, mEventName_Str may be empty (ie no event selected)
                saveScoreToDB(mEventName_Str);

                Toast.makeText(PostEventActivity.this, "Shoot saved!", Toast.LENGTH_LONG).show();

                Intent homeActivity_Intent = new Intent(PostEventActivity.this, homeActivity.class);
                homeActivity_Intent.putExtra(CURRENT_USER_KEY, mCurrentUserEmail_Str);
                startActivity(homeActivity_Intent);

                PostEventActivity.this.finish();
            }
        });

        mCancel_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cancel post event action
                // Don't add any event or notes, just save score to database
                saveScoreToDB("No Event");

                Toast.makeText(PostEventActivity.this, "Shoot saved!", Toast.LENGTH_LONG).show();

                Intent homeActivity_Intent = new Intent(PostEventActivity.this, homeActivity.class);
                homeActivity_Intent.putExtra(CURRENT_USER_KEY, mCurrentUserEmail_Str);
                startActivity(homeActivity_Intent);

                PostEventActivity.this.finish();
            }
        });

        // Initializing List view
        mEventList_View = findViewById(R.id.postEvent_List);
        mEventList_View.setTag(EVENT_LIST_TAG);
        setListViewLayoutParams();
        initializeEventListView();

    }

    private void setListViewLayoutParams() {
        /*******************************************************************************************
         * Function: setListViewLayoutParams
         *
         * Purpose: Function sets the layout parameters for the list view so that it fits nicely
         *          with everything (with room for buttons and titles)
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        double screenHeight_Dbl = getResources().getDisplayMetrics().heightPixels;
        double scaleFactorScroll_Dbl, scaleFactorEvent_Dbl;

        ConstraintLayout.LayoutParams params;

        if (isPortrait) {
            scaleFactorScroll_Dbl = 2.5;
            scaleFactorEvent_Dbl = 3.5;

        } else {
            scaleFactorScroll_Dbl = 2.5;
            scaleFactorEvent_Dbl = 3.5;
        }

        params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)(screenHeight_Dbl / scaleFactorEvent_Dbl));
        params.topToBottom = mPostEvent_Scroll.getId();

        mEventList_View.setLayoutParams(params);

        params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)(screenHeight_Dbl / scaleFactorScroll_Dbl));
        params.topToTop = mParent_Lay.getId();

        mPostEvent_Scroll.setLayoutParams(params);

    }

    private void initializeShooterPostEventItems(){
        /*******************************************************************************************
         * Function: initializeShooterPostEventItems
         *
         * Purpose: Function initializes the post event items with shooter info
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        // Initialize linear layout
        mPostEventItems_Lay = findViewById(R.id.postEventItems_Lay);

        // Initialize list of views
        mShooterPostEventItems_List = new ArrayList<>();

        mShooterPostEventItems_List.add((PostEventItemClass) findViewById(R.id.postEventItem_1));
        mShooterPostEventItems_List.add((PostEventItemClass) findViewById(R.id.postEventItem_2));
        mShooterPostEventItems_List.add((PostEventItemClass) findViewById(R.id.postEventItem_3));
        mShooterPostEventItems_List.add((PostEventItemClass) findViewById(R.id.postEventItem_4));
        mShooterPostEventItems_List.add((PostEventItemClass) findViewById(R.id.postEventItem_5));

        // Add info to views that are going to be used
        for (int i = 0; i < numShooters_Int; i++) {
            mShooterPostEventItems_List.get(i).setShooterName(shooterName_List.get(i));
            mShooterPostEventItems_List.get(i).setShooterCoach(mCurrentUserEmail_Str);
            mShooterPostEventItems_List.get(i).setShooterScore(shooterScore_List.get(i));
            mShooterPostEventItems_List.get(i).shooterGun_Spin.setAdapter(initializeGunSpinnerAdapt());
            mShooterPostEventItems_List.get(i).shooterLoad_Spin.setAdapter(initializeLoadSpinnerAdapt());
        }

        // Remove unused views
        for (int i = 4; i > (numShooters_Int-1); i--) {
            mPostEventItems_Lay.removeView(mShooterPostEventItems_List.get(i));
            mShooterPostEventItems_List.remove(i);
        }

    }

    private ArrayAdapter<String> initializeGunSpinnerAdapt() {
        /*******************************************************************************************
         * Function: initializeGunSpinnerAdapt
         *
         * Purpose: Function initializes gun spinner
         *
         * Parameters: None
         *
         * Returns: temp_Adapt - adapter for gun spinner
         *
         ******************************************************************************************/

        ArrayAdapter<String> temp_Adapt;
        ArrayList<GunClass> tempGun_List;
        ArrayList<String> tempStr_List = new ArrayList<>();

        GlobalApplicationContext currentContext = new GlobalApplicationContext();
        final DBHandler db = new DBHandler(currentContext.getContext());

        tempGun_List = db.getAllGunFromDB(mCurrentUserEmail_Str);

        for (int i = 0; i < tempGun_List.size(); i++) {
            tempStr_List.add(tempGun_List.get(i).getGunNickname_Str());
        }

        tempStr_List.add(NO_GUN_STRING);

        temp_Adapt = new ArrayAdapter<>(currentContext.getContext(), android.R.layout.simple_list_item_1, tempStr_List);

        return temp_Adapt;
    }

    private ArrayAdapter<String> initializeLoadSpinnerAdapt() {
        /*******************************************************************************************
         * Function: initializeLoadSpinnerAdapt
         *
         * Purpose: Function initializes load spinner
         *
         * Parameters: None
         *
         * Returns: temp_Adapt - adapter for load spinner
         *
         ******************************************************************************************/

        ArrayAdapter<String> temp_Adapt;
        ArrayList<LoadClass> tempLoad_List;
        ArrayList<String> tempStr_List = new ArrayList<>();

        GlobalApplicationContext currentContext = new GlobalApplicationContext();
        final DBHandler db = new DBHandler(currentContext.getContext());

        tempLoad_List = db.getAllLoadFromDB(mCurrentUserEmail_Str);

        for (int i = 0; i < tempLoad_List.size(); i++) {
            tempStr_List.add(tempLoad_List.get(i).getLoadNickname_Str());
        }

        tempStr_List.add(NO_LOAD_STRING);

        temp_Adapt = new ArrayAdapter<>(currentContext.getContext(), android.R.layout.simple_list_item_1, tempStr_List);

        return temp_Adapt;
    }

    //************************************** Event Functions ***************************************
    private ArrayList<EventClass> refreshEventList() {
        /*******************************************************************************************
         * Function: refreshEventList
         *
         * Purpose: Function returns the current list of events for the current user
         *
         * Parameters: None
         *
         * Returns: currentEventStr_List - a string list of events for current user
         *
         ******************************************************************************************/

        ArrayList<EventClass> userEvent_List = db.getAllEventFromDB(mCurrentUserEmail_Str);

        EventClass temp_Event = new EventClass();
        temp_Event.setEventName_Str(getString(R.string.no_event_main_text));
        temp_Event.setEventNotes_Str(getString(R.string.no_event_second_text));

        if (userEvent_List.isEmpty()) {
            Log.d(TAG, "Event list empty.");
            userEvent_List.add(temp_Event);
        }

        return userEvent_List;
    }

    private void initializeEventListView() {
        /*******************************************************************************************
         * Function: initializeEventListView
         *
         * Purpose: Function initializes the event list view
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        try {

            mCustomEventList_Adapt = new TrapMasterListArrayAdapter(this,
                    (ArrayList<Object>)(ArrayList<?>)(refreshEventList()));

            mEventList_View.setAdapter(mCustomEventList_Adapt);

        } catch (Exception e){
            Log.d(TAG, "no events in db for this user: " + e.toString());
        }

        mEventList_View.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.setSelected(true);
                DBHandler db = new DBHandler(getApplicationContext());
                EventClass temp_Event = db.getEventInDB((int) view.getTag());
                mEventName_Str = temp_Event.getEventName_Str();
                Log.d(TAG, "Event List Item Click: " + Integer.toString(i) + " " + mEventName_Str);
            }
        });
    }

    //************************************ Database Functions **************************************
    private void saveScoreToDB(String eventName_Str) {
        /*******************************************************************************************
         * Function: saveScoreToDB
         *
         * Purpose: Function saves all scores and info to database
         *
         * Parameters: eventName_Str (IN) - name of the event
         *
         * Returns: None
         *
         ******************************************************************************************/

        DBHandler db = new DBHandler(GlobalApplicationContext.getContext());

        for (int i = 0; i < numShooters_Int; i++) {
            ShotClass temp_Shot = new ShotClass();
            PostEventItemClass temp_Item = mShooterPostEventItems_List.get(i);

            String name_Str, total_Str, hit_Str, gun_Str, load_Str, notes_Str;

            name_Str = temp_Item.getShooterName();
            total_Str = Integer.toString(TOTAL_NUM_SHOTS);
            hit_Str = Integer.toString(temp_Item.getShooterScore());
            notes_Str = temp_Item.shooterNotes_Edt.getText().toString();
            gun_Str = temp_Item.shooterGun_Spin.getSelectedItem().toString();
            load_Str = temp_Item.shooterLoad_Spin.getSelectedItem().toString();

            if (gun_Str.isEmpty()) {
                gun_Str = "No gun for shoot";
            }

            if (load_Str.isEmpty()) {
                load_Str = "No load for shoot";
            }

            if (notes_Str.isEmpty()) {
                notes_Str = "No notes for shoot";
            }

            if (eventName_Str.isEmpty()) {
                eventName_Str = "No event for shoot";
            }

            temp_Shot.setShotShooterName_Str(name_Str);
            temp_Shot.setShotEventName_Str(eventName_Str);
            temp_Shot.setShotTotalNum_Str(total_Str);
            temp_Shot.setShotHitNum_Str(hit_Str);
            temp_Shot.setShotGun_Str(gun_Str);
            temp_Shot.setShotLoad_Str(load_Str);
            temp_Shot.setShotNotes_Str(notes_Str);

            db.insertShotInDB(temp_Shot);

            String debug = name_Str + "\n"
                    + eventName_Str + "\n"
                    + hit_Str + "\n"
                    + gun_Str + "\n"
                    + load_Str + "\n"
                    + notes_Str + "\n";

            Log.d(TAG, debug);
        }

    }

}
