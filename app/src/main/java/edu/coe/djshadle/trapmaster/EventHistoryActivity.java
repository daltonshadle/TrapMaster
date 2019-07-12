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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class EventHistoryActivity extends AppCompatActivity {

    //********************************** Variables and Constants ***********************************
    // General Constants
    private final String TAG = "JRW";
    private String ACTIVITY_TITLE;
    private String CURRENT_USER_KEY;
    private final int SHOT_LIST_TAG = 1;
    private final int EVENT_LIST_TAG = 2;

    // General Variables
    private String mCurrentUserEmail_Str = "********";
    private DBHandler db;
    private boolean isPortrait = true;
    // Shot
    private TrapMasterListArrayAdapter mCustomShotList_Adapt;

    // Event
    private TrapMasterListArrayAdapter mCustomEventList_Adapt;

    // UI References
    private ListView mShotList_View;
    private ListView mEventList_View;

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

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            setContentView(R.layout.activity_event_history_portrait);
            isPortrait = true;
        } else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_event_history_landscape);
            isPortrait = false;
        }

        if (savedInstanceState != null) {

        } else {
            mCurrentUserEmail_Str = getIntent().getStringExtra(CURRENT_USER_KEY);
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
            setContentView(R.layout.activity_event_history_portrait);
            isPortrait = true;
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_event_history_landscape);
            isPortrait = false;
        }

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
        mShotList_View = findViewById(R.id.eventHistoryScore_List);
        mEventList_View = findViewById(R.id.eventHistoryEvent_List);

        // Setting tags for list views
        mShotList_View.setTag(SHOT_LIST_TAG);
        mEventList_View.setTag(EVENT_LIST_TAG);

        setListViewLayoutParams();

        initializeShotListView();
        initializeEventListView();

        // Initializing buttons
        FloatingActionButton mAddShot = findViewById(R.id.eventHistoryAddScore_Btn);
        FloatingActionButton mAddEvent = findViewById(R.id.eventHistoryAddEvent_Btn);

        mAddShot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to new event activity
                Intent newEvent_Activity = new Intent(EventHistoryActivity.this, NewEventActivity.class);
                newEvent_Activity.putExtra(CURRENT_USER_KEY, mCurrentUserEmail_Str);
                startActivity(newEvent_Activity);
            }
        });

        mAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Display add event dialog box for user input, -1 = adding a new event
                EventClass temp_Event = new EventClass();
                temp_Event.setEventID_Int(-1);
                temp_Event.setEventEmail_Str(mCurrentUserEmail_Str);
                temp_Event.editEventDialog(EventHistoryActivity.this, mCustomEventList_Adapt);
            }
        });

        // Setting title of activity
        setTitle(ACTIVITY_TITLE);
    }

    //************************************* Listview Functions *************************************
    // Shot List
    private ArrayList<ShotClass> refreshShotList() {
        /*******************************************************************************************
         * Function: refreshShotList
         *
         * Purpose: Function returns the current list of shots for the current user
         *
         * Parameters: None
         *
         * Returns: currentShotStr_List - a string list of shot for current user
         *
         ******************************************************************************************/

        // Initialize db handler and shooter and score arrays
        db = new DBHandler(this);
        ArrayList<ShooterClass> currentShooter_List = db.getAllShooterFromDB(mCurrentUserEmail_Str);
        ArrayList<ShotClass> currentShot_List = new ArrayList<>();

        // For each shooter in the db shooter list, add scores for that shooter to score list.
        for (ShooterClass shooter : currentShooter_List) {
            currentShot_List.addAll(db.getAllShotFromDB(shooter.getShooterName_Str()));
            Collections.sort(currentShot_List, Collections.<ShotClass>reverseOrder());
        }

        return currentShot_List;
    }

    private void initializeShotListView() {
        /*******************************************************************************************
         * Function: initializeShotListView
         *
         * Purpose: Function initializes the shot list view
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        try {
            // Initialize shot adapter
            mCustomShotList_Adapt = new TrapMasterListArrayAdapter(this,
                    (ArrayList<Object>)(ArrayList<?>)(refreshShotList()));


            mCustomShotList_Adapt.refreshShotArrayAdapter(refreshShotList());

            mShotList_View.setAdapter(mCustomShotList_Adapt);

        } catch (Exception e){
            Log.d("JRW", "no shots in db for this user: " + e.toString());
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
        ArrayList<EventClass> userEvent_List = db.getAllEventFromDB(mCurrentUserEmail_Str);

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
    }

    // Both List
    private void setListViewLayoutParams() {
        /*******************************************************************************************
         * Function: setListViewLayoutParams
         *
         * Purpose: Function sets the layout parameters for the shot and event list view so that both
         *          use roughly half of the display (with room for buttons)
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        double screenHeight_Dbl = getResources().getDisplayMetrics().heightPixels;
        double scaleFactor_Dbl = 1;

        LinearLayout.LayoutParams params;

        if (isPortrait) {
            scaleFactor_Dbl = 3.5;
        } else {
            scaleFactor_Dbl = 2.0;
        }

        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)(screenHeight_Dbl / scaleFactor_Dbl));

        mEventList_View.setLayoutParams(params);
        mShotList_View.setLayoutParams(params);
    }
    
}
