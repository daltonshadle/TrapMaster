/***************************************************************************************************
 * FILENAME : NewEventActivity.java
 *
 * AUTHOR : Dalton Shadle
 *
 * DESCRIPTION : Holds functions for the New Event activity of this application
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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class NewEventActivity extends AppCompatActivity implements OnTotalHitChange, View.OnClickListener {

    //********************************** Variables and Constants ***********************************
    // General Constants
    private final boolean PORTRAIT_ORIENTATION = false; // Allow portrait orientation, landscape
                                                        // is the default TODO: Support Portrait
    private final int HIT = 0, MISS = 1, NEUTRAL = 2;
    private final int NUM_COUNTER_BUTTON = 5;
    private final int TOTAL_NUM_SHOTS = 25;
    private final String TRAP_STATE_KEY = "TRAP_STATE_KEY";

    // General Variables
    private int trapCounterChildCount_Int = 0;
    private int totalHits_Int = 0;
    private boolean quickEventFlag_Bool = false;
    private ArrayList<Integer> trapCounterState_Array;
    private String mCurrentUserEmail_Str = "tempEmail";
    private String mEventName_Str = "tempEventName";
    private String mShotNotes_Str = "tempNotes";

    // UI References
    private TextView mTxtTotalScore_View;
    private LinearLayout mTrapCounter_SubLnrLay;

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

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT && PORTRAIT_ORIENTATION) {
            setContentView(R.layout.activity_new_event_portrait);
        }
        else {
            // Landscape is the default orientation by AndroidManifest.xml
            setContentView(R.layout.activity_new_event_landscape);
        }

        initializeViews();

        if (savedInstanceState != null) {
            trapCounterState_Array = savedInstanceState.getIntegerArrayList(TRAP_STATE_KEY);
            setTrapCounterStates(trapCounterState_Array);

            quickEventFlag_Bool = savedInstanceState.getBoolean(getString(R.string.quick_event_flag_key));
        } else {
            mCurrentUserEmail_Str = getIntent().getStringExtra(getString(R.string.current_user_email));
        }
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

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT && PORTRAIT_ORIENTATION) {
            setContentView(R.layout.activity_new_event_portrait);
        }
        else {
            // Landscape is the default orientation by AndroidManifest.xml
            setContentView(R.layout.activity_new_event_landscape);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        /*******************************************************************************************
         * Function: onSaveInstanceState
         *
         * Purpose: Function saves instances when activity is paused
         *
         * Parameters: savedInstanceState (OUT) - provides the saved instances from current state
         *
         * Returns: None
         *
         ******************************************************************************************/
        
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.

        getTrapCounterStates();
        savedInstanceState.putIntegerArrayList(TRAP_STATE_KEY, trapCounterState_Array);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        /*******************************************************************************************
         * Function: onRestoreInstanceState
         *
         * Purpose: Function restores instances when activity resumes
         *
         * Parameters: savedInstanceState (IN) - provides the saved instances from previous state
         *
         * Returns: None
         *
         ******************************************************************************************/
        
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.

        trapCounterState_Array = savedInstanceState.getIntegerArrayList(TRAP_STATE_KEY);
        setTrapCounterStates(trapCounterState_Array);

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

        // Initializing all trap counters
        TrapCounterButtonsClass trapCount_1 = findViewById(R.id.tcbCounter1);
        TrapCounterButtonsClass trapCount_2 = findViewById(R.id.tcbCounter2);
        TrapCounterButtonsClass trapCount_3 = findViewById(R.id.tcbCounter3);
        TrapCounterButtonsClass trapCount_4 = findViewById(R.id.tcbCounter4);
        TrapCounterButtonsClass trapCount_5 = findViewById(R.id.tcbCounter5);

        trapCount_1.setTotalHitChange(this);
        trapCount_2.setTotalHitChange(this);
        trapCount_3.setTotalHitChange(this);
        trapCount_4.setTotalHitChange(this);
        trapCount_5.setTotalHitChange(this);

        // Initializing all buttons
        Button hit_Btn = findViewById(R.id.hit_Btn);
        Button miss_Btn = findViewById(R.id.miss_Btn);
        Button save_Btn = findViewById(R.id.save_Btn);
        Button clear_Btn = findViewById(R.id.clear_Btn);

        hit_Btn.setOnClickListener(this);
        miss_Btn.setOnClickListener(this);
        save_Btn.setOnClickListener(this);
        clear_Btn.setOnClickListener(this);

        // Initializing all textviews
        mTxtTotalScore_View = findViewById(R.id.totalScore_Txt);

        // Initializing all layouts
        mTrapCounter_SubLnrLay = findViewById(R.id.newEventTrapCounter_SubLnrLay);

        // Initializing integer to layout child count
        trapCounterChildCount_Int = mTrapCounter_SubLnrLay.getChildCount();

        // Initializing integer array for trap counter states (5 buttons per counter)
        trapCounterState_Array = new ArrayList<Integer>(trapCounterChildCount_Int);

    }

    @Override
    public void OnTotalHitChange() {
        /*******************************************************************************************
         * Function: OnTotalHitChange
         *
         * Purpose: Function listener for UI trap counters to provide functionality when total hit
         *          counter is updated
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        totalHits_Int = 0;

        for (int i = 0; i < trapCounterChildCount_Int; i++){
            TrapCounterButtonsClass tempTrapCounter =
                    (TrapCounterButtonsClass) mTrapCounter_SubLnrLay.getChildAt(i);
            totalHits_Int += tempTrapCounter.getTotalNumberHit();
        }

        mTxtTotalScore_View.setText(String.valueOf(totalHits_Int));
    }

    @Override
    public void onClick(View view) {
        /*******************************************************************************************
         * Function: OnClick
         *
         * Purpose: Function listener for buttons that provides functionality when clicked
         *
         * Parameters: view (IN) - is the clicked view that enacted listener
         *
         * Returns: None
         *
         ******************************************************************************************/

        switch (view.getId()) {
            case R.id.hit_Btn:
                setNextUncheckedTrapCounter(HIT);
                break;
            case R.id.miss_Btn:
                setNextUncheckedTrapCounter(MISS);
                break;
            case R.id.clear_Btn:
                resetAllTrapCounter();
                break;
            case R.id.save_Btn:
                if (isAllTrapCounterChecked()) {
                    saveScoreToDB(mCurrentUserEmail_Str, mEventName_Str, TOTAL_NUM_SHOTS,
                            totalHits_Int, mShotNotes_Str);

                    // TODO: remove this temp code from testing

                    if (!quickEventFlag_Bool) {
                        Intent i = new Intent(this, ProfilesActivity.class);
                        i.putExtra(getString(R.string.current_user_email), mCurrentUserEmail_Str);
                        startActivity(i);
                    } else {
                        // TODO: fill in code to handle quick events, basically need them to sign in
                    }
                }
                break;

        }
    }

    //********************************** Trap Counter Functions ************************************
    private int getNextUncheckedTrapCounter() {
        /*******************************************************************************************
         * Function: getNextUncheckedTrapCounter
         *
         * Purpose: Function iterates through all trap counters to find the next neutral child
         *
         * Parameters: None
         *
         * Returns: nextChild - index of the next neutral child
         *
         ******************************************************************************************/

        int nextChild = -1;

        for (int i = 0; i < trapCounterChildCount_Int; i++) {
            TrapCounterButtonsClass tempTrapCounter =
                    (TrapCounterButtonsClass) mTrapCounter_SubLnrLay.getChildAt(i);
            if (!tempTrapCounter.allChecked()) {
                nextChild = i;
                break;
            }
        }

        return nextChild;
    }

    private void setNextUncheckedTrapCounter(int status) {
        /*******************************************************************************************
         * Function: setNextUncheckedTrapCounter
         *
         * Purpose: Function sets next unchecked child to the status provided as parameter
         *
         * Parameters: status - provides the status for what the next neutral child should be set to
         *
         * Returns: None
         *
         ******************************************************************************************/

        int nextUnchecked = getNextUncheckedTrapCounter();

        if (nextUnchecked != -1) {
            TrapCounterButtonsClass tempTrapCounter =
                    (TrapCounterButtonsClass) mTrapCounter_SubLnrLay.getChildAt(nextUnchecked);

            tempTrapCounter.setNextChild(status);
        }
    }

    private void getTrapCounterStates() {
        /*******************************************************************************************
         * Function: getTrapCounterStates
         *
         * Purpose: Function iterates through all trap counters collecting states and sets ArrayList
         *          to states of trap counters (used for saved instances)
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        for (int i = 0; i < trapCounterChildCount_Int; i++) {
            TrapCounterButtonsClass tempTrapCounter =
                    (TrapCounterButtonsClass) mTrapCounter_SubLnrLay.getChildAt(i);

            ArrayList<Integer> tempTrapCounterStates = tempTrapCounter.getChildStates();
            for (int j = 0; j < NUM_COUNTER_BUTTON; j++) {
                trapCounterState_Array.add(tempTrapCounterStates.get(j));
            }
        }
    }

    private void setTrapCounterStates(ArrayList<Integer> stateList) {
        /*******************************************************************************************
         * Function: setTrapCounterStates
         *
         * Purpose: Function iterates through all trap counters setting states determined
         *          by stateList
         *
         * Parameters: stateList (IN) - provides previous states for all trap counters
         *
         * Returns: None
         *
         ******************************************************************************************/

        for (int i = 0; i < trapCounterChildCount_Int; i++) {
            TrapCounterButtonsClass tempTrapCounter =
                    (TrapCounterButtonsClass) mTrapCounter_SubLnrLay.getChildAt(i);
            tempTrapCounter.resetTrapCounter();

            for (int j = 0; j < NUM_COUNTER_BUTTON; j++) {
                int index = (i * NUM_COUNTER_BUTTON) + j;
                tempTrapCounter.setNextChild(stateList.get(index));
            }
        }
    }

    private boolean isAllTrapCounterChecked() {
        /*******************************************************************************************
         * Function: isAllTrapCounterChecked
         *
         * Purpose: Function iterates through all trap counters checking states
         *
         * Parameters: None
         *
         * Returns: allChecked - returns true if all buttons are checked
         *
         ******************************************************************************************/

        boolean allChecked = true;

        for (int i = 0; i < trapCounterChildCount_Int; i++) {
            TrapCounterButtonsClass tempTrapCounter =
                    (TrapCounterButtonsClass) mTrapCounter_SubLnrLay.getChildAt(i);

            if(!tempTrapCounter.allChecked()) {
                allChecked = false;
                break;
            }
        }

        return allChecked;
    }

    private void resetAllTrapCounter() {
        /*******************************************************************************************
         * Function: resetAllTrapCounter
         *
         * Purpose: Function iterates through all trap counters resetting states to neutral and
         *          clearing the score
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        for (int i = 0; i < (mTrapCounter_SubLnrLay.getChildCount()); i++) {
            TrapCounterButtonsClass tempTrapCounter =
                    (TrapCounterButtonsClass) mTrapCounter_SubLnrLay.getChildAt(i);
            tempTrapCounter.resetTrapCounter();
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

        DBHandler db = new DBHandler(this);

        ShotClass temp_Shot = new ShotClass(email_Str, eventName_Str, Integer.toString(totalShot_Int),
                Integer.toString(totalHit_Int), notes_Str);

        db.insertShotInDB(temp_Shot);
    }


}
