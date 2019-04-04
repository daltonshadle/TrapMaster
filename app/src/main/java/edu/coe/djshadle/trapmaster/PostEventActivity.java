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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
    private final String TAG = "JRW";
    private final int TOTAL_NUM_SHOTS = 25;
    private final int EVENT_LIST_TAG = 2;

    // General Variables
    DBHandler db;
    private boolean isPortrait = true;
    private String mCurrentUserEmail_Str = "*********";
    // event
    private TrapMasterListArrayAdapter mCustomEventList_Adapt;
    private String mEventName_Str = "";
    // shot
    private String mShotNotes_Str = "";
    private int mShotScore_Int = 0;

    // UI References
    private ListView mEventList_View;
    private EditText mShotNotes_Edt;

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
            mShotScore_Int = getIntent().getIntExtra(getString(R.string.current_user_score), 0);
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
        db = new DBHandler(this);

        // Set action bar title
        try {
            setTitle(ACTIVITY_TITLE);
        } catch (Exception e) {
            // Didn't work.
        }

        // Initializing shot notes edit text
        mShotNotes_Edt = findViewById(R.id.postEventShotNotes_Edt);

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
                mShotNotes_Str = mShotNotes_Edt.getText().toString();

                // Save score to db, mEventName_Str may be empty (ie no event selected)
                saveScoreToDB(mCurrentUserEmail_Str, mEventName_Str, TOTAL_NUM_SHOTS,
                        mShotScore_Int, mShotNotes_Str);

                Toast.makeText(PostEventActivity.this, "Shoot saved!", Toast.LENGTH_LONG).show();

                Intent homeActivity_Intent = new Intent(PostEventActivity.this, homeActivity.class);
                homeActivity_Intent.putExtra(CURRENT_USER_KEY, mCurrentUserEmail_Str);
                startActivity(homeActivity_Intent);
            }
        });

        mCancel_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cancel post event action
                // Don't add any event or notes, just save score to database
                saveScoreToDB(mCurrentUserEmail_Str, "", TOTAL_NUM_SHOTS,
                        mShotScore_Int, "");

                Toast.makeText(PostEventActivity.this, "Shoot saved!", Toast.LENGTH_LONG).show();

                Intent homeActivity_Intent = new Intent(PostEventActivity.this, homeActivity.class);
                homeActivity_Intent.putExtra(CURRENT_USER_KEY, mCurrentUserEmail_Str);
                startActivity(homeActivity_Intent);
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
        double scaleFactor_Dbl = 1;

        ConstraintLayout.LayoutParams params;

        if (isPortrait) {
            scaleFactor_Dbl = 2.0;
        } else {
            scaleFactor_Dbl = 2.0;
        }

        params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)(screenHeight_Dbl / scaleFactor_Dbl));
        params.topToBottom = mShotNotes_Edt.getId();

        mEventList_View.setLayoutParams(params);
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
    }

    //************************************ Database Functions **************************************
    private void saveScoreToDB(String email_Str, String eventName_Str, int totalShot_Int,
                               int totalHit_Int, String notes_Str) {
        /*******************************************************************************************
         * Function: saveScoreToDB
         *
         * Purpose: Function takes info necessary to save score to database
         *
         * Parameters: email_Str (IN) - email of the user
         *             eventName_Str (IN) - name of the event
         *             totalShot_Int (IN) - number of total possible shots taken
         *             totalHit_Int (IN) - number of total hits
         *             notes_Str (IN) - notes from the shoot
         *
         * Returns: None
         *
         ******************************************************************************************/

        ShotClass temp_Shot = new ShotClass(email_Str, eventName_Str, Integer.toString(totalShot_Int),
                Integer.toString(totalHit_Int), notes_Str);

        db.insertShotInDB(temp_Shot);
    }

}
