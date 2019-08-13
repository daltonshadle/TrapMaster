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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PostEventActivity extends AppCompatActivity {
    //***************************************** Constants ******************************************
    // General Constants
    private final String TAG = "JRW";
    private String ACTIVITY_TITLE;
    private final int MAX_NUM_SHOOTERS = 5;

    // Key Constants
    private String CURRENT_USER_KEY;
    private String NUM_ROUNDS_KEY;
    private String SHOOTER_LIST_KEY;
    private String ROUND_LIST_KEY;

    //***************************************** Variables ******************************************
    // General Variables
    private int mCurrentProfileID_Int = -1;
    private boolean isPortrait = true;
    private DBHandler db;
    private EventClass mEvent;

    // Round Variables
    private int currentRound_Int = 1;
    private int numRounds_Int = 1;
    // 2D array of score objects, takes the form [round #][shooter #} = shot object
    private Map<Integer, ArrayList<RoundClass>> round_Array;

    // Shooter Variables
    private int numShooters_Int = 1;
    private ArrayList<String> shooterName_List;

    // UI References
    private ConstraintLayout mParent_Lay;
    private ArrayList<PostEventItemClass> mShooterPostEventItems_List;
    private LinearLayout mPostEventItems_Lay;
    private ScrollView mPostEvent_Scroll;

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

        // Initialize constants
        initializeConstants();

        // Set layout for correct orientation
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_post_event_portrait);
            isPortrait = true;
        }
        else {
            setContentView(R.layout.activity_post_event_landscape);
            isPortrait = false;
        }

        // Pull saved instances from prior states
        if (savedInstanceState != null) {

        } else {
            mCurrentProfileID_Int = getIntent().getIntExtra(CURRENT_USER_KEY, -1);
            shooterName_List = getIntent().getStringArrayListExtra(SHOOTER_LIST_KEY);
            round_Array = (HashMap) getIntent().getSerializableExtra(ROUND_LIST_KEY);
            numRounds_Int = getIntent().getIntExtra(NUM_ROUNDS_KEY, 1);
            numShooters_Int = shooterName_List.size();
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

        // Set layout for correct orientation
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_post_event_portrait);
            isPortrait = true;
        }
        else {
            setContentView(R.layout.activity_post_event_landscape);
            isPortrait = false;
        }

        // Initialize views
        initializeViews();
    }

    //*********************************** Initialize Functions *************************************
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
        ROUND_LIST_KEY = getString(R.string.round_list_key);
        NUM_ROUNDS_KEY = getString(R.string.num_rounds_key);
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

        // Initialize event variable
        mEvent = new EventClass();
        mEvent.setEventName_Str("Add Event");

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
        setRoundInfo(currentRound_Int);

        // Initializing buttons
        Button mLeft_Btn = findViewById(R.id.postEventLeft_Btn);
        Button mRight_Btn = findViewById(R.id.postEventRight_Btn);
        final Button mAddEvent_Btn = findViewById(R.id.postEventAddEvent_Btn);

        mLeft_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Previous button
                previousBtnAction();
            }
        });

        mRight_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Next button
                nextBtnAction();
            }
        });

        mAddEvent_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add event button
                AlertDialog event_Dialog = mEvent.chooseEventDialog(PostEventActivity.this, mCurrentProfileID_Int);
                event_Dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        if (mEvent.getEventName_Str().isEmpty()) {
                            mEvent.setEventName_Str("Add Event");
                        }
                        mAddEvent_Btn.setText(mEvent.getEventName_Str());
                    }
                });
            }
        });

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

        for (int i = 0; i < numShooters_Int; i++) {
            mShooterPostEventItems_List.get(i).setProfileID_Int(mCurrentProfileID_Int);
        }

        // Remove unused views
        for (int i = MAX_NUM_SHOOTERS - 1; i >= numShooters_Int; i--) {
            mPostEventItems_Lay.removeView(mShooterPostEventItems_List.get(i));
            mShooterPostEventItems_List.remove(i);
        }

    }

    //********************************* Post Event Item Functions **********************************
    private void previousBtnAction() {
        /*******************************************************************************************
         * Function: previousBtnAction
         *
         * Purpose: Function performs the action necessary for previous button clicks
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        if (currentRound_Int == 1) {
            // Prompt user about exiting post event or save to database
            saveScoreToDB();

            Toast.makeText(PostEventActivity.this, "Shoot saved!", Toast.LENGTH_LONG).show();

            // Return to home
            Intent homeActivity_Intent = new Intent(PostEventActivity.this, homeActivity.class);
            homeActivity_Intent.putExtra(CURRENT_USER_KEY, mCurrentProfileID_Int);
            startActivity(homeActivity_Intent);

            PostEventActivity.this.finish();
        } else {
            // Move to previous round

            // Save current round info
            saveRoundInfo(currentRound_Int);

            // Decrement current round number
            currentRound_Int = currentRound_Int - 1;

            // Set round info for next round
            setRoundInfo(currentRound_Int);
        }
    }

    private void nextBtnAction() {
        /*******************************************************************************************
         * Function: nextBtnAction
         *
         * Purpose: Function performs the action necessary for next button clicks
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        // Save current round info
        saveRoundInfo(currentRound_Int);

        if (currentRound_Int == numRounds_Int) {
            // Save everything to database
            saveScoreToDB();

            Toast.makeText(PostEventActivity.this, "Shoot saved!", Toast.LENGTH_LONG).show();

            // Return to home
            Intent homeActivity_Intent = new Intent(PostEventActivity.this, homeActivity.class);
            homeActivity_Intent.putExtra(CURRENT_USER_KEY, mCurrentProfileID_Int);
            startActivity(homeActivity_Intent);

            PostEventActivity.this.finish();
        } else {
            // Move to next round

            // Increment current round number
            currentRound_Int = currentRound_Int + 1;

            // Set round info for next round
            setRoundInfo(currentRound_Int);
        }
    }

    private void saveRoundInfo(int roundNum_Int) {
        /*******************************************************************************************
         * Function: saveRoundInfo
         *
         * Purpose: Function saves round information for the round number in roundNum_Int
         *
         * Parameters: roundNum_Int - round number to save information
         *
         * Returns: None
         *
         ******************************************************************************************/

        // Iterate over all post event items and collect information, save to rounds_Array
        for (int i = 0; i < numShooters_Int; i++) {
            // Initialize temp_Round with round variable in rounds_Array
            RoundClass temp_Round = round_Array.get(roundNum_Int).get(i);
            PostEventItemClass temp_Post = mShooterPostEventItems_List.get(i);

            // Set fields for temp round to save them
            temp_Round.setRoundGunID_Int(temp_Post.getShooter_Gun().getGunID_Int());
            temp_Round.setRoundLoadID_Int(temp_Post.getShooter_Load().getLoadID_Int());
            temp_Round.setRoundNotes_Str(temp_Post.getShooterNotes_Str());

            // Add temp round to rounds array at same location
            round_Array.get(roundNum_Int).add(i, temp_Round);
        }

    }

    private void setRoundInfo(int roundNum_Int) {
        /*******************************************************************************************
         * Function: setRoundInfo
         *
         * Purpose: Function sets round information for the round number in roundNum_Int
         *
         * Parameters: roundNum_Int - round number to set information to
         *
         * Returns: None
         *
         ******************************************************************************************/

        // Set title based on round number
        setTitle("Post Event - Round " + Integer.toString(roundNum_Int));

        // Add info to views that are going to be used
        for (int i = 0; i < numShooters_Int; i++) {
            RoundClass temp_Round = round_Array.get(roundNum_Int).get(i);

            mShooterPostEventItems_List.get(i).setShooterName(shooterName_List.get(i));
            mShooterPostEventItems_List.get(i).setShooterScore(temp_Round.getRoundScore_Int());
        }
    }

    //************************************ Database Functions **************************************
    private void saveScoreToDB() {
        /*******************************************************************************************
         * Function: saveScoreToDB
         *
         * Purpose: Function saves all scores and info to database
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        DBHandler db = new DBHandler(GlobalApplicationContext.getContext());

        // Iterate over all shooters and collect round and match info to save to db
        for (int i = 0; i < numShooters_Int; i++) {
            // Initialize match variable for this shooter, TODO: fix match team functionality
            MatchClass temp_Match = new MatchClass();
            temp_Match.setMatchEventID_Int(mEvent.getEventID_Int());
            temp_Match.setMatchTeamID_Int(-1);

            // Initialize score variable
            int tempMatchScore_Int = 0;

            // Initialize Round ID array
            ArrayList<Integer> tempRoundID_List = new ArrayList<>();

            // Iterate over all rounds for shooter and save them
            for (int j = 0; j < numRounds_Int; j++) {
                // Initializing round_num for current round since the round numbers are 1-based
                int tempRoundNum_Int = j + 1;

                // Initialize round variable for shooter round
                RoundClass temp_Round = round_Array.get(tempRoundNum_Int).get(i);
                tempMatchScore_Int = tempMatchScore_Int + temp_Round.getRoundScore_Int();
                temp_Match.setMatchShooterID_Int(temp_Round.getRoundShooterID_Int());

                // Save round to db
                int tempRoundID_Int = (int) db.insertRoundInDB(temp_Round);
                tempRoundID_List.add(tempRoundID_Int);
            }

            // Set remaining info for match
            temp_Match.setMatchRoundIDS_Array(tempRoundID_List);
            temp_Match.setMatchScore_Int(tempMatchScore_Int);

            // Save match to db
            db.insertMatchInDB(temp_Match);
        }

    }

}
