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
import android.content.Intent;
import android.content.res.Configuration;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
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
    private String NO_GUN_STRING;
    private String NO_LOAD_STRING;
    private String NO_EVENT_STRING;
    private final int TOTAL_NUM_SHOTS = 25;
    private final int EVENT_LIST_TAG = 2;
    private final int MAX_NUM_SHOOTERS = 5;

    // Key Constants
    private String CURRENT_USER_KEY;
    private String NUM_ROUNDS_KEY;
    private String SHOOTER_LIST_KEY;
    private String ROUND_LIST_KEY;

    //***************************************** Variables ******************************************
    // General Variables
    private String mCurrentProfileEmail_Str = "*********";
    private int mCurrentProfileID_Int = -1;
    private boolean isPortrait = true;
    private DBHandler db;

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
        NO_GUN_STRING = "No gun";
        NO_LOAD_STRING = "No load";
        NO_EVENT_STRING = "No event";
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
        initializeRoundsArray();
        setRoundInfo(currentRound_Int);

        // Initializing buttons
        Button mLeft_Btn = findViewById(R.id.postEventLeft_Btn);
        Button mRight_Btn = findViewById(R.id.postEventRight_Btn);

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
            saveScoreToDB(NO_EVENT_STRING);

            Toast.makeText(PostEventActivity.this, "Shoot saved!", Toast.LENGTH_LONG).show();

            // Return to home
            Intent homeActivity_Intent = new Intent(PostEventActivity.this, homeActivity.class);
            homeActivity_Intent.putExtra(CURRENT_USER_KEY, mCurrentProfileEmail_Str);
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

            // TODO: Save everything to db
            // saveScoreToDB(mEventName_Str);

            Toast.makeText(PostEventActivity.this, "Shoot saved!", Toast.LENGTH_LONG).show();

            // Return to home
            Intent homeActivity_Intent = new Intent(PostEventActivity.this, homeActivity.class);
            homeActivity_Intent.putExtra(CURRENT_USER_KEY, mCurrentProfileEmail_Str);
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

        // Iterate over all post event items and collect information, save to shotRounds_Array
        for (int i = 0; i < numShooters_Int; i++) {
            RoundClass temp_Round = round_Array.get(roundNum_Int).get(i);
            PostEventItemClass temp_Post = mShooterPostEventItems_List.get(i);

            // TODO: fix these
//            temp_shot.setShotGun_Str(temp_post.shooterGun_Spin.getSelectedItem().toString());
//            temp_shot.setShotLoad_Str(temp_post.shooterLoad_Spin.getSelectedItem().toString());
//            temp_shot.setShotNotes_Str(temp_post.shooterNotes_Edt.getText().toString());
//
//            shotRounds_Array.get(roundNum_Int).add(i, temp_shot);
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

            // TODO: fix these too
            mShooterPostEventItems_List.get(i).setShooterName(db.getShooterInDB(temp_Round.getRoundShooterID_Int()).getShooterName_Str());
            mShooterPostEventItems_List.get(i).setShooterScore(temp_Round.getRoundScore_Int());
        }
    }

    private void initializeRoundsArray() {
        /*******************************************************************************************
         * Function: initializeRoundsArray
         *
         * Purpose: Function initialize the shotRounds_Array with shooter information
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        // Initialize array type
        round_Array = new HashMap<>();

        // Iterate over all rounds and shooters and initialize the information in the array
        for (int i = 0; i < numRounds_Int; i++) {
            // Initializing round_num for current round since the round numbers are 1-based
            int round_num = i + 1;
            ArrayList<RoundClass> currentRound_array = new ArrayList<>();

            for (int j = 0; j < numShooters_Int; j++) {
                RoundClass temp_shot = new RoundClass();

                // TODO: Fix these
//                temp_shot.setShotShooterName_Str(shooterName_List.get(j));
//                temp_shot.setShotHitNum_Str(Integer.toString(shooterScores_Array.get(round_num).get(j).get(ROUND_SCORE_KEY)));
//                temp_shot.setShotTotalNum_Str(Integer.toString(TOTAL_NUM_SHOTS));
//                temp_shot.setShotGun_Str(NO_GUN_STRING);
//                temp_shot.setShotLoad_Str(NO_LOAD_STRING);

                currentRound_array.add(temp_shot);
            }

            // Add the round of shots to the whole array
            round_Array.put(round_num, currentRound_array);
        }
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

        // Iterate over all rounds and shooters and add to database
        for (int i = 0; i < numRounds_Int; i++) {
            // Initializing round_num for current round since the round numbers are 1-based
            int round_num = i + 1;

            for (int j = 0; j < numShooters_Int; j++) {
                RoundClass temp_shot = round_Array.get(round_num).get(i);
                eventName_Str = eventName_Str + " - Round " + Integer.toString(round_num);

                // TODO: fix this function
//                temp_shot.setShotEventName_Str(eventName_Str);
//                db.insertShotInDB(temp_shot);
            }
        }

    }

}
