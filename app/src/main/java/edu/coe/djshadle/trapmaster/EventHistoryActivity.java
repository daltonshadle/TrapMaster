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
import android.support.design.widget.TabLayout;
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
import android.widget.ScrollView;
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
    private final int EVENT_POS = 0;
    private final int MATCH_POS = 1;

    // Key Constants
    private String CURRENT_USER_KEY;


    //**************************************** Variables *******************************************
    // General Variables
    private int mCurrentProfileID_Int = -1;
    private int mCurrentTabPos_Int = EVENT_POS;
    private DBHandler db;
    private boolean isPortrait = true;

    // UI References
    private ScrollView mEvent_Scroll, mMatch_Scroll;
    private LinearLayout mEvent_Lay, mMatch_Lay;
    private TabLayout eventHist_Tab;

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

        // Initializing database variable
        db = new DBHandler(getApplicationContext());

        // Initialize views
        eventHist_Tab = findViewById(R.id.eventHistory_TabLay);
        mMatch_Lay = findViewById(R.id.eventHistoryMatch_Lay);
        mEvent_Lay = findViewById(R.id.eventHistoryEvent_Lay);
        mMatch_Scroll = findViewById(R.id.eventHistoryMatch_Scroll);
        mEvent_Scroll = findViewById(R.id.eventHistoryEvent_Scroll);

        eventHist_Tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case EVENT_POS:
                        // set visibilities of scroll views, EVENT
                        mEvent_Scroll.setVisibility(View.VISIBLE);
                        mMatch_Scroll.setVisibility(View.GONE);

                        Log.d(TAG, "Events Tab");

                        mCurrentTabPos_Int = EVENT_POS;
                        break;
                    case MATCH_POS:
                        // set visibilities of scroll views, MATCH
                        mEvent_Scroll.setVisibility(View.GONE);
                        mMatch_Scroll.setVisibility(View.VISIBLE);

                        Log.d(TAG, "Matches Tab");

                        mCurrentTabPos_Int = MATCH_POS;
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // Set the lists
        setEventListLay();
        setMatchListLay();

        // Set Button
        FloatingActionButton add_Btn = findViewById(R.id.eventHistoryAdd_Btn);
        add_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (mCurrentTabPos_Int) {
                    case EVENT_POS:
                        // Display add event dialog box for user input, -1 = adding a new event
                        EventClass temp_Event = new EventClass();
                        temp_Event.setEventID_Int(-1);
                        temp_Event.setEventProfileID_Int(mCurrentProfileID_Int);
                        temp_Event.editEventDialog(EventHistoryActivity.this).setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                // Reset the event list to changes in the database
                                setEventListLay();
                            }
                        });
                        break;
                    case MATCH_POS:
                        // TODO: Start a new event
                        break;
                }
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

    private void setMatchListLay() {
        /*******************************************************************************************
         * Function: setMatchListLay
         *
         * Purpose: Function initializes the match list layout
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        // Initialize match list from db, remove all views from lay
        ArrayList<MatchClass> dbMatch_List = refreshMatchList();
        mMatch_Lay.removeAllViews();

        // Iterate over all matches in list and add to layout TODO: see if profile ID can be -1
        for (int i = 0; i < dbMatch_List.size(); i++) {
            MatchClass temp_Match = dbMatch_List.get(i);
            CustomListItemClass temp_Item = new CustomListItemClass(EventHistoryActivity.this, -1, temp_Match.getMatchShooterID_Int(), temp_Match);
            temp_Item.setPadding(0, 6, 0, 0);

            mMatch_Lay.addView(temp_Item);
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

    private void setEventListLay() {
        /*******************************************************************************************
         * Function: setEventListLay
         *
         * Purpose: Function initializes the event list layout
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        // Initialize event list from db
        ArrayList<EventClass> dbEvent_List = refreshEventList();
        mEvent_Lay.removeAllViews();

        // Iterate over all events in list and add to layout
        for (int i = 0; i < dbEvent_List.size(); i++) {
            EventClass temp_Event = dbEvent_List.get(i);
            CustomListItemClass temp_Item = new CustomListItemClass(EventHistoryActivity.this, temp_Event.getEventProfileID_Int(), -1, temp_Event);
            temp_Item.setPadding(0, 6, 0, 0);

            mEvent_Lay.addView(temp_Item);
        }
    }

    
}
