/***************************************************************************************************
 * FILENAME : EventHistoryActivity.java
 *
 * AUTHOR : Dalton Shadle
 *
 * DESCRIPTION : Holds functions for the Event History activity of this application
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
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class EventHistoryActivity extends AppCompatActivity {
    //**************************************** Constants *******************************************
    // General Constants
    private String TAG = "JRW";
    private String ACTIVITY_TITLE;

    // Key Constants
    // Key Constants
    private String CURRENT_USER_KEY;

    // Tag Constants
    private final int MATCH_LIST_TAG = 1;
    private final int EVENT_LIST_TAG = 2;

    //**************************************** Variables *******************************************
    // General Variables
    private int mCurrentProfileID_Int = -1;
    private DBHandler db;
    private boolean isPortrait = true;

    // Match
    private TrapMasterListArrayAdapter mCustomMatchList_Adapt;

    // Event
    private TrapMasterListArrayAdapter mCustomEventList_Adapt;

    // UI References
    private ListView mMatchList_View,  mEventList_View;

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

        // Initialize constants for this activity
        initializeConstants();

        // Figure out orientation and layout
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            setContentView(R.layout.activity_event_history_portrait);
            isPortrait = true;
        } else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_event_history_landscape);
            isPortrait = false;
        }

        // Pull extra information from intent
        if (savedInstanceState != null) {
            mCurrentProfileID_Int = savedInstanceState.getInt(CURRENT_USER_KEY);
        } else {
            mCurrentProfileID_Int = getIntent().getIntExtra(CURRENT_USER_KEY, -1);
        }

        // Initialize views
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

        // Figure out orientation and layout
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_event_history_portrait);
            isPortrait = true;
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_event_history_landscape);
            isPortrait = false;
        }

        // Initialize views
        initializeViews();
    }

    //********************************** Initialization Functions **********************************
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

        ACTIVITY_TITLE = getString(R.string.event_history_activity_title);
        CURRENT_USER_KEY = getString(R.string.current_user_key);
    }

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

        // Initializing list and database variables
        db = new DBHandler(getApplicationContext());

        // Initializing list view
        mMatchList_View = findViewById(R.id.eventHistoryScore_List);
        mEventList_View = findViewById(R.id.eventHistoryEvent_List);

        // Setting tags for list views
        mMatchList_View.setTag(MATCH_LIST_TAG);
        mEventList_View.setTag(EVENT_LIST_TAG);

        setListViewLayoutParams();

        initializeMatchListView();
        initializeEventListView();

        // Initializing buttons
        FloatingActionButton mAddMatch = findViewById(R.id.eventHistoryAddScore_Btn);
        FloatingActionButton mAddEvent = findViewById(R.id.eventHistoryAddEvent_Btn);

        mAddMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Prompt user with new event dialog
                ShootingEventClass newEvent = new ShootingEventClass(mCurrentProfileID_Int,EventHistoryActivity.this);
                newEvent.newEventDialog(EventHistoryActivity.this);
            }
        });

        mAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Display add event dialog box for user input, -1 = adding a new event
                EventClass temp_Event = new EventClass();
                temp_Event.setEventID_Int(-1);
                temp_Event.setEventProfileID_Int(mCurrentProfileID_Int);
                temp_Event.editEventDialog(EventHistoryActivity.this, mCustomEventList_Adapt);
            }
        });

        // Setting title of activity
        setTitle(ACTIVITY_TITLE);
    }

    //************************************ List view Functions *************************************
    // Match List
    private ArrayList<MatchClass> refreshMatchList() {
        /*******************************************************************************************
         * Function: refreshMatchList
         *
         * Purpose: Function returns the current list of Matches for the current user
         *
         * Parameters: None
         *
         * Returns: currentMatchStr_List - a string list of Match for current user
         *
         ******************************************************************************************/

        // Initialize db handler and shooter and score arrays
        db = new DBHandler(this);
        ArrayList<ShooterClass> currentShooter_List = db.getAllShooterFromDB(mCurrentProfileID_Int);
        ArrayList<MatchClass> currentMatch_List = new ArrayList<>();

        // For each shooter in the db shooter list, add scores for that shooter to score list.
        for (ShooterClass shooter : currentShooter_List) {
            currentMatch_List.addAll(db.getAllMatchesFromDB(shooter.getShooterID_Int()));
            Collections.sort(currentMatch_List, Collections.<MatchClass>reverseOrder());
        }

        return currentMatch_List;
    }

    private void initializeMatchListView() {
        /*******************************************************************************************
         * Function: initializeMatchListView
         *
         * Purpose: Function initializes the Match list view
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        try {
            // Initialize Match adapter
            mCustomMatchList_Adapt = new TrapMasterListArrayAdapter(this,
                    (ArrayList<Object>)(ArrayList<?>)(refreshMatchList()), MATCH_LIST_TAG);


            mCustomMatchList_Adapt.refreshMatchArrayAdapter(refreshMatchList());

            mMatchList_View.setAdapter(mCustomMatchList_Adapt);

        } catch (Exception e){
            Log.d("JRW", "no Matchs in db for this user: " + e.toString());
        }

    }

    // Event List
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

        // Initialize db handler and shooter and event array
        db = new DBHandler(this);
        ArrayList<EventClass> userEvent_List = db.getAllEventFromDB(mCurrentProfileID_Int);

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
                    (ArrayList<Object>)(ArrayList<?>)(refreshEventList()), EVENT_LIST_TAG);

            mCustomEventList_Adapt.refreshEventArrayAdapter(refreshEventList());

            mEventList_View.setAdapter(mCustomEventList_Adapt);

        } catch (Exception e){
            Log.d(TAG, "no events in db for this user: " + e.toString());
        }
    }

    // Both List
    private void setListViewLayoutParams() {
        /*******************************************************************************************
         * Function: setListViewLayoutParams
         *
         * Purpose: Function sets the layout parameters for the Match and event list view so that both
         *          use roughly half of the display (with room for buttons)
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        // Initialize variables for this function
        double screenHeight_Dbl = getResources().getDisplayMetrics().heightPixels;
        double scaleFactor_Dbl = 1;

        LinearLayout.LayoutParams params;

        // Determine variables based on orientation
        if (isPortrait) {
            scaleFactor_Dbl = 3.5;
        } else {
            scaleFactor_Dbl = 2.0;
        }

        // Set layout parameters
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)(screenHeight_Dbl / scaleFactor_Dbl));

        mEventList_View.setLayoutParams(params);
        mMatchList_View.setLayoutParams(params);
    }

    
}
