/***************************************************************************************************
 * FILENAME : NewShootingEventActivity.java
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
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class NewShootingEventActivity extends AppCompatActivity implements OnTotalHitChange {
    //***************************************** Constants ******************************************
    // General Constants
    private String TAG = "JRW";
    private String ACTIVITY_TITLE;

    // Key Constants
    private String CURRENT_USER_KEY;
    private String NUM_ROUNDS_KEY;
    private String SHOOTER_LIST_KEY;
    private String ROUND_LIST_KEY;
    private final String TRAP_STATE_KEY = "TRAP_STATE_KEY";
    private final String TRAP_EXPAND_KEY = "TRAP_EXPAND_KEY";

    //***************************************** Variables ******************************************
    // General Variables
    private String mCurrentProfileEmail_Str = "*********";
    private int mCurrentProfileID_Int = -1;
    private DBHandler db;

    // Rounds
    private int numRounds_Int = 1;
    private int currentRound_Int = 1;
    private Map<Integer, ArrayList<RoundClass>> round_Array;

    // Shooter
    private int numShooters_Int = 1;
    private ArrayList<String> shooterNames_Array;

    // Trap Counters
    private ArrayList<Integer> trapState_Array;
    private ArrayList<Integer> trapExpand_Array;

    // UI References
    private ArrayList<TrapScoreItemClass> trapScoreViews_Array;
    private LinearLayout trap_LnrLay;
    private Button mRight_Btn;
    private Button mLeft_Btn;

    // Google Variables
    FirebaseAuth auth;

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
            setContentView(R.layout.activity_new_event_portrait);
        }
        else {
            setContentView(R.layout.activity_new_event_landscape);
        }

        // Pull saved instances from prior states
        if (savedInstanceState != null) {
            trapState_Array = savedInstanceState.getIntegerArrayList(TRAP_STATE_KEY);
            trapExpand_Array = savedInstanceState.getIntegerArrayList(TRAP_EXPAND_KEY);
            setCurrentTrapStates(trapState_Array);
            setCurrentExpandStates(trapExpand_Array);
        } else {
            mCurrentProfileID_Int = getIntent().getIntExtra(CURRENT_USER_KEY, -1);
            shooterNames_Array = getIntent().getStringArrayListExtra(SHOOTER_LIST_KEY);
            trapExpand_Array = new ArrayList<>(Collections.nCopies(5, 0));
            numRounds_Int = getIntent().getIntExtra(NUM_ROUNDS_KEY, 1);
            numShooters_Int = shooterNames_Array.size();
        }

        mCurrentProfileEmail_Str = db.getProfileFromDB(mCurrentProfileID_Int).getProfileEmail_Str();

        // Initialize views
        initializeViews();

        Log.d(TAG, "On Create");
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
            setContentView(R.layout.activity_new_event_portrait);
        }
        else {
            setContentView(R.layout.activity_new_event_landscape);
        }

        // Collect and set trap states when configuration changes
        trapState_Array = getCurrentTrapStates();
        trapExpand_Array = getCurrentExpandStates();
        initializeViews();
        setCurrentTrapStates(trapState_Array);
        setCurrentExpandStates(trapExpand_Array);

        Log.d(TAG, "On Config Change");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        /*******************************************************************************************
         * Function: onSaveInstanceState
         *
         * Purpose: Function saves instances when activity is paused
         *
         * Parameters: savedInstanceState (IN) - provides the saved instances from current state
         *
         * Returns: None
         *
         ******************************************************************************************/
        
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.

        trapState_Array = getCurrentTrapStates();
        trapExpand_Array = getCurrentExpandStates();
        savedInstanceState.putIntegerArrayList(TRAP_STATE_KEY, trapState_Array);
        savedInstanceState.putIntegerArrayList(TRAP_EXPAND_KEY, trapExpand_Array);

        Log.d(TAG, "On saved instance");
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

        trapState_Array = savedInstanceState.getIntegerArrayList(TRAP_STATE_KEY);
        trapExpand_Array = savedInstanceState.getIntegerArrayList(TRAP_EXPAND_KEY);
        setCurrentTrapStates(trapState_Array);
        setCurrentExpandStates(trapExpand_Array);

        Log.d(TAG, "On restore instance");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        /*******************************************************************************************
         * Function: onCreateOptionsMenu
         *
         * Purpose: Function inflater menu from resources
         *
         * Parameters: menu (IN) - menu input
         *
         * Returns: True
         *
         ******************************************************************************************/

        // Inflate menu for new event activity
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_event_activity_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        /*******************************************************************************************
         * Function: onCreateOptionsMenu
         *
         * Purpose: Function inflater menu from resources
         *
         * Parameters: item (IN) - menu item input
         *
         * Returns: True
         *
         ******************************************************************************************/

        int id = item.getItemId();

        // switch statement for menu items
        switch (id) {
            case R.id.newEventAllHit_MenuItem:
                // All items to hit
                for (TrapScoreItemClass i:trapScoreViews_Array) {
                    i.setAllStatesToHit();
                }
                break;
            case R.id.newEventCancel_MenuItem:
                // Cancel shoot, return to home
                Intent homeActivity_Intent = new Intent(this, homeActivity.class);
                homeActivity_Intent.putExtra(CURRENT_USER_KEY, mCurrentProfileID_Int);
                startActivity(homeActivity_Intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /*******************************************************************************************
         * Function: onKeyDown
         *
         * Purpose: Override key down function to check for back button presses, dialog if so
         *
         * Parameters: keyCode (IN) - integer code fo key pressed for identifying
         *             event (IN) - event when key pressed
         *
         * Returns: None
         *
         ******************************************************************************************/

        // Handle button presses
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            // Back button was pressed, prompt user with return to home dialog
            Log.d(this.getClass().getName(), "back button pressed");
            returnToHomeDialog();
        }
        return super.onKeyDown(keyCode, event);
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

        ACTIVITY_TITLE = getString(R.string.new_event_activity_title);
        CURRENT_USER_KEY = getString(R.string.current_user_key);
        NUM_ROUNDS_KEY = getString(R.string.num_rounds_key);
        SHOOTER_LIST_KEY = getString(R.string.shooter_list_key);
        ROUND_LIST_KEY =  getString(R.string.round_list_key);

        db = new DBHandler(NewShootingEventActivity.this);
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

        // Initializing all trap counters
        initializeTrapCounters();

        // Initializing shooter score array
        round_Array = new HashMap<>();

        // Initializing all buttons
        mRight_Btn = findViewById(R.id.newEventRight_Btn);
        mRight_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Next Button
                if (allTrapsChecked()) {
                    // All traps are checked, move to next state

                    // Save current round trap states to round_Array
                    round_Array.put(currentRound_Int, saveCurrentRoundsToArray());

                    if (currentRound_Int == numRounds_Int) {
                        // Move to post event dialog
                        postEventDialog();
                    } else {
                        // Move to next round

                        // Increment current round number
                        currentRound_Int = currentRound_Int + 1;

                        // Move to next round
                        Toast.makeText(NewShootingEventActivity.this, "Next Round", Toast.LENGTH_SHORT).show();

                        // Reset all trap counters for next round
                        resetAllTrapScores();
                        setAllTrapRoundText(currentRound_Int);
                    }

                } else {
                    // Not all traps are checked, not ready to save
                    Toast.makeText(NewShootingEventActivity.this,
                            "All scoring buttons need to be checked", Toast.LENGTH_LONG).show();
                }
            }
        });

        mLeft_Btn = findViewById(R.id.newEventLeft_Btn);
        mLeft_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Previous Button

                if (currentRound_Int == 1) {
                    // Prompt user about exiting new event
                    returnToHomeDialog();
                } else {
                    // Move to previous round

                    // Save current round trap states to round_Array
                    round_Array.put(currentRound_Int, saveCurrentRoundsToArray());

                    // Decrement current round number
                    currentRound_Int = currentRound_Int - 1;

                    // Restore previous round trap states to trap score views
                    restoreCurrentRoundsToTrapCounters(currentRound_Int);
                    setAllTrapRoundText(currentRound_Int);
                }
            }
        });

        // Initializing database variable
        db = new DBHandler(getApplicationContext());

        // Initialize Google/Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Set action bar title
        try {
            setTitle(ACTIVITY_TITLE);
        } catch (Exception e) {
            // Didn't work.
        }

    }

    private void initializeTrapCounters() {
        /*******************************************************************************************
         * Function: initializeTrapCounters
         *
         * Purpose: Function initializes all trap counters for this round
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        // Initializing linear layout
        trap_LnrLay = findViewById(R.id.newEventTrap_LnrLay);

        // Initializing array
        trapScoreViews_Array = new ArrayList<>();

        // Adding all views to list
        trapScoreViews_Array.add((TrapScoreItemClass) findViewById(R.id.trapScoreItem1));
        trapScoreViews_Array.add((TrapScoreItemClass) findViewById(R.id.trapScoreItem2));
        trapScoreViews_Array.add((TrapScoreItemClass) findViewById(R.id.trapScoreItem3));
        trapScoreViews_Array.add((TrapScoreItemClass) findViewById(R.id.trapScoreItem4));
        trapScoreViews_Array.add((TrapScoreItemClass) findViewById(R.id.trapScoreItem5));

        // Add info to the views that are going to be used
        for (int i = 0; i < numShooters_Int; i++) {
            final int index = i;
            trapScoreViews_Array.get(i).setTotalHitChange(this);
            trapScoreViews_Array.get(i).setRoundText(1);
            trapScoreViews_Array.get(i).setShooterText(shooterNames_Array.get(i));
            trapScoreViews_Array.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    trapScoreViews_Array.get(index).setExpandBool(!trapScoreViews_Array.get(index).getExpandBool());
                }
            });
        }

        // Remove unused views
        for (int i = 4; i > (numShooters_Int-1); i--) {
            trap_LnrLay.removeView(trapScoreViews_Array.get(i));
            trapScoreViews_Array.remove(i);
        }
    }

    //********************************** Trap Counter Functions ************************************
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

        //totalHits_Int = trapScore_View.getTotalNumberHit();
    }

    private ArrayList<Integer> getCurrentTrapStates() {
        /*******************************************************************************************
         * Function: getCurrentTrapStates
         *
         * Purpose: Function gets current states of the trap counters and returns in a list
         *
         * Parameters: None
         *
         * Returns: allStates_List - List of all current states
         *
         ******************************************************************************************/

        // Initialize states array
        ArrayList<Integer> allStates_List = new ArrayList<>();

        // Iterate over all shooters, collect current trap states in ONE array
        for (int i = 0; i < numShooters_Int; i++) {
            allStates_List.addAll(trapScoreViews_Array.get(i).getAllStates());
        }

        return allStates_List;
    }

    private void setCurrentTrapStates(ArrayList<Integer> allStates_List) {
        /*******************************************************************************************
         * Function: setCurrentTrapStates
         *
         * Purpose: Function sets current states of the traps
         *
         * Parameters: allStates_List - List of states to set the traps to
         *
         * Returns: None
         *
         ******************************************************************************************/

        // Initialize the number of states
        int numState_Int = 25;

        // Iterate over all shooters, set the trap states from allStates_List
        for (int i = 0; i < numShooters_Int; i++) {
            int firstIndex_Int = (i * numState_Int);
            int lastIndex_Int = firstIndex_Int + numState_Int;

            trapScoreViews_Array.get(i).setAllStates(
                    new ArrayList<Integer> (allStates_List.subList(firstIndex_Int, lastIndex_Int)));
        }
    }

    private ArrayList<Integer> getCurrentExpandStates(){
        /*******************************************************************************************
         * Function: getCurrentExpandStates
         *
         * Purpose: Function gets current expand states of the trap counters and returns in a list
         *
         * Parameters: None
         *
         * Returns: getAllExpandStates - List of all current expand states
         *
         ******************************************************************************************/

        // Initialize expanded states array
        ArrayList<Integer> expandState_List = new ArrayList<>();

        // Iterate over all shooters, collect current expanded states in ONE array
        for (int i = 0; i < numShooters_Int; i++) {
            expandState_List.add((trapScoreViews_Array.get(i).getExpandBool() ? 1 : 0));
        }

        return expandState_List;
    }

    private void setCurrentExpandStates(ArrayList<Integer> expandStates_List) {
        /*******************************************************************************************
         * Function: setCurrentExpandStates
         *
         * Purpose: Function sets current expand states of the traps
         *
         * Parameters: expandStates_List - List of expand states to set the traps to
         *
         * Returns: None
         *
         ******************************************************************************************/

        // Iterate over all shooters, set expanded states for trap counters from expandStates_List
        for (int i = 0; i < numShooters_Int; i++) {
            trapScoreViews_Array.get(i).setExpandBool(expandStates_List.get(i) != 0);
        }
    }

    private boolean allTrapsChecked() {
        /*******************************************************************************************
         * Function: allTrapsChecked
         *
         * Purpose: Function checks if all traps are checked
         *
         * Parameters: None
         *
         * Returns: allChecked - TRUE if all trap counters are complete (no NEUTRAL), FALSE
         *                       otherwise
         *
         ******************************************************************************************/

        // Initialize bool to return
        boolean allChecked = true;

        // Iterate over all shooters, continue through all shooters unless one isn't fully checked
        for (int i = 0; i < numShooters_Int; i++) {
            allChecked = trapScoreViews_Array.get(i).allChecked();

            if (!allChecked) {
                break;
            }
        }

        return allChecked;
    }

    private void resetAllTrapScores() {
        /*******************************************************************************************
         * Function: resetAllTrapScores
         *
         * Purpose: Function resets all trap counters
         *
         * Parameters: None
         *
         * Returns: allChecked
         *
         ******************************************************************************************/

        // Iterate over all shooters, reset each trap counter to NEUTRAL
        for(int i = 0; i < numShooters_Int; i++) {
            trapScoreViews_Array.get(i).resetTrapCounter();
        }
    }

    private void setAllTrapRoundText(int round_Int) {
        /*******************************************************************************************
         * Function: setAllTrapRoundText
         *
         * Purpose: Function sets the rounds fo all trap counters
         *
         * Parameters: round_Int - round number to set all trap counter to
         *
         * Returns: allChecked
         *
         ******************************************************************************************/

        // Iterate over all shooters, set the round text for each trap counter
        for(int i = 0; i < numShooters_Int; i++) {
            trapScoreViews_Array.get(i).setRoundText(round_Int);
        }
    }

    //************************************ Database Functions **************************************
    private ArrayList<RoundClass> saveCurrentRoundsToArray(){
        /*******************************************************************************************
         * Function: saveCurrentRoundsToArray
         *
         * Purpose: Function takes info necessary to save current scores to rounds array
         *
         * Parameters: None
         *
         * Returns: temp_Array (OUT) - current round information in an array element for each shooter
         *
         ******************************************************************************************/

        // Initialize round array
        ArrayList<RoundClass> temp_Array = new ArrayList<>();

        // Iterate over all shooter and collect trap states for that round, save in array
        for (int i = 0; i < numShooters_Int; i++) {
            RoundClass temp_Round = new RoundClass();
            temp_Round.setRoundHitMiss_Array(trapScoreViews_Array.get(i).getAllStates());
            temp_Round.setRoundRound_Int(currentRound_Int);
            temp_Round.setRoundScore_Int(trapScoreViews_Array.get(i).getTotalNumberHit());
            temp_Round.setRoundShooterID_Int(db.getShooterInDB(shooterNames_Array.get(i)).getShooterID_Int());

            temp_Array.add(temp_Round);
        }

        return temp_Array;
    }

    private void restoreCurrentRoundsToTrapCounters(int roundNum_Int){
        /*******************************************************************************************
         * Function: restoreCurrentRoundsToTrapCounters
         *
         * Purpose: Function restores trap counter states from round_Array
         *
         * Parameters: roundNum_Int (IN) - round to set trap counter to
         *
         * Returns: None
         *
         ******************************************************************************************/

        // Iterate over all shooters and set the trap score views with previous trap states
        for (int i = 0; i < numShooters_Int; i++) {
            trapScoreViews_Array.get(i).setAllStates(round_Array.get(roundNum_Int).get(i).getRoundHitMiss_Array());
        }

    }

    private void saveScoresToDB() {
        /*******************************************************************************************
         * Function: saveScoreToDB
         *
         * Purpose: Function takes info necessary to save score to database
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        // Initialize function variables
        db = new DBHandler(getApplicationContext());

        // Iterate over shooters first then rounds
        for (int i = 0; i < numShooters_Int; i++) {
            // Initialize round ID array for each shooter
            ArrayList<Integer> roundID_Array = new ArrayList<>();
            int shooterScore_Int = 0;

            for (int j = 0; j < numRounds_Int; j++) {
                // Initialize round numbers starting at 1
                int tempCurRound_Int = i + 1;
                RoundClass temp_Round = round_Array.get(tempCurRound_Int).get(j);

                // Insert into db, save round ID's in array
                long roundID_Long = db.insertRoundInDB(temp_Round);
                roundID_Array.add((int)roundID_Long);
                shooterScore_Int = shooterScore_Int + temp_Round.getRoundScore_Int();
            }

            // Initialize match variable
            MatchClass temp_Match = new MatchClass();
            temp_Match.setMatchShooterID_Int(db.getShooterInDB(shooterNames_Array.get(i)).getShooterID_Int());
            temp_Match.setMatchRoundIDS_Array(roundID_Array);
            temp_Match.setMatchScore_Int(shooterScore_Int);

            // Save match to db
            db.insertMatchInDB(temp_Match);
        }


    }

    //*********************************** QuickEvent Functions *************************************
    private void quickEventLogin() {
        /*******************************************************************************************
         * Function: quickEventLogin
         *
         * Purpose: Function creates a alert dialog to sign user in, used in a Quick Event situation
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        // Set Dialog Title
        alertDialog.setTitle("Sign In");

        // Set all edittext views for gathering information
        LinearLayout dialogView_LnrLay = new LinearLayout(this);
        dialogView_LnrLay.setOrientation(LinearLayout.VERTICAL);

        // Set Email EditText
        final EditText email_Edt = new EditText(this);
        email_Edt.setHint("Email");
        email_Edt.setGravity(Gravity.START);
        email_Edt.setTextColor(Color.BLACK);
        dialogView_LnrLay.addView(email_Edt);

        // Set Gun Model EditText
        final EditText pass_Edt = new EditText(this);
        pass_Edt.setHint("Password");
        pass_Edt.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        pass_Edt.setTransformationMethod(PasswordTransformationMethod.getInstance());
        pass_Edt.setGravity(Gravity.START);
        pass_Edt.setTextColor(Color.BLACK);
        dialogView_LnrLay.addView(pass_Edt);

        // Add linear layout to alert dialog
        alertDialog.setView(dialogView_LnrLay);

        // Set Buttons
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"SIGN IN", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Overwritten by on click listener below
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Overwritten by on click listener below
            }
        });

        new Dialog(getApplicationContext());
        alertDialog.show();

        // Set Properties for Neutral Button
        final Button neutral_Btn = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        LinearLayout.LayoutParams neutralBtn_Params = (LinearLayout.LayoutParams) neutral_Btn.getLayoutParams();
        neutralBtn_Params.gravity = Gravity.FILL_HORIZONTAL;
        neutral_Btn.setPadding(50, 10, 10, 10);   // Set Position
        neutral_Btn.setTextColor(Color.BLUE);
        neutral_Btn.setLayoutParams(neutralBtn_Params);
        neutral_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform Action on Neutral Button
                String tempEmail_Str = email_Edt.getText().toString();
                String tempPass_Str = pass_Edt.getText().toString();
                boolean cancel_Bool = false;

                // Check for a valid email address.
                if (TextUtils.isEmpty(tempEmail_Str)) {
                    email_Edt.setError(getString(R.string.error_field_required));
                    email_Edt.requestFocus();
                    cancel_Bool = true;
                }

                // Check for a valid password.
                if (TextUtils.isEmpty(tempPass_Str)) {
                    pass_Edt.setError(getString(R.string.error_field_required));
                    pass_Edt.requestFocus();
                    cancel_Bool = true;
                }

                if (!cancel_Bool) {
                    signUserWithFireBase(tempEmail_Str, tempPass_Str);
                    alertDialog.dismiss();
                }


            }
        });

        // Set Properties for Negative Button
        final Button negative_Btn = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams negativeBtn_Params = (LinearLayout.LayoutParams) negative_Btn.getLayoutParams();
        negativeBtn_Params.gravity = Gravity.FILL_HORIZONTAL;
        negative_Btn.setTextColor(Color.RED);
        negative_Btn.setLayoutParams(negativeBtn_Params);
        negative_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform Action on Negative button
                alertDialog.dismiss();
            }
        });

        // TODO: Add another button to register a new user
    }

    private void signUserWithFireBase(final String email, final String password){
        /*******************************************************************************************
         * Function: signUserWithFireBase
         *
         * Purpose: Function signs in a new user with Firebase
         *
         * Parameters: email (IN) - email of user to sign in
         *             password (IN) - password of user to sign in
         *
         * Returns: None
         *
         ******************************************************************************************/

        //authenticate user
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(NewShootingEventActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.

                        if (!task.isSuccessful()) {
                            // there was an error
                            String exceptionMsg_Str = "Sign in failed! " + task.getException().getMessage();

                            Toast.makeText(NewShootingEventActivity.this, exceptionMsg_Str,
                                    Toast.LENGTH_LONG).show();

                            // Relaunch quick event login
                            quickEventLogin();
                        } else {
                            // Login was successful
                            // set current user and have user press save again
                            Toast.makeText(NewShootingEventActivity.this, "Sign in successful!",
                                    Toast.LENGTH_LONG).show();
                            mCurrentProfileEmail_Str = email;
                        }
                    }
                });

    }

    //************************************** Event Functions ***************************************
    private void postEventDialog() {
        /*******************************************************************************************
         * Function: postEventDialog
         *
         * Purpose: Function creates a alert dialog to start post event activity
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        final String DIALOG_TITLE = "Post Event Logger";
        final String DIALOG_MSG = "Do you want to continue to the post event logger? " +
                "(This let's you add additional info to your shoot)";

        final boolean POSITIVE_BTN = true;  // Right
        final boolean NEUTRAL_BTN = false;   // Left
        final boolean NEGATIVE_BTN = true;  // Middle

        final String POSITIVE_BUTTON_TXT = "YES";
        final String NEUTRAL_BUTTON_TXT = "";
        final String NEGATIVE_BUTTON_TXT = "NO";

        final int POSITIVE_BTN_COLOR = Color.BLUE;
        final int NEUTRAL_BTN_COLOR = Color.RED;
        final int NEGATIVE_BTN_COLOR = Color.RED;


        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        // Set Dialog Title
        alertDialog.setTitle(DIALOG_TITLE);

        // Set Dialog Message
        alertDialog.setMessage(DIALOG_MSG);

        // Set Buttons
        // Positive Button, Right
        if (POSITIVE_BTN) {
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, POSITIVE_BUTTON_TXT, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Processed with onClick below

                }
            });
        }

        // Neutral Button, Left
        if (NEUTRAL_BTN) {
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, NEUTRAL_BUTTON_TXT, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Processed with onClick below
                }
            });
        }

        // Negative Button, Middle
        if (NEGATIVE_BTN) {
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, NEGATIVE_BUTTON_TXT, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Processed with onClick below
                }
            });
        }


        new Dialog(getApplicationContext());
        alertDialog.show();

        // Set Button Colors
        if (POSITIVE_BTN) {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(POSITIVE_BTN_COLOR);
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Perform Action on Positive button

                    // Dismiss dialog
                    alertDialog.dismiss();

                    // Create intent for post event activity and put all necessary extras
                    Intent postEventActivity_Intent = new Intent(NewShootingEventActivity.this, PostEventActivity.class);
                    postEventActivity_Intent.putExtra(CURRENT_USER_KEY, mCurrentProfileID_Int);
                    postEventActivity_Intent.putExtra(NUM_ROUNDS_KEY, numRounds_Int);
                    postEventActivity_Intent.putStringArrayListExtra(SHOOTER_LIST_KEY, shooterNames_Array);
                    postEventActivity_Intent.putExtra(ROUND_LIST_KEY, (HashMap) round_Array);
                    startActivity(postEventActivity_Intent);
                }
            });
        }
        if (NEUTRAL_BTN) {
            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(NEUTRAL_BTN_COLOR);
            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Perform Action on NEUTRAL Button

                }
            });
        }
        if (NEGATIVE_BTN) {
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(NEGATIVE_BTN_COLOR);
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Perform Action on Negative button

                    // Dismiss dialog
                    alertDialog.dismiss();

                    // Save scores to database
                    saveScoresToDB();

                    // Create intent for returning to home activity and put all necessary extras
                    Intent homeActivity_Intent = new Intent(NewShootingEventActivity.this, homeActivity.class);
                    homeActivity_Intent.putExtra(CURRENT_USER_KEY, mCurrentProfileID_Int);
                    startActivity(homeActivity_Intent);

                    NewShootingEventActivity.this.finish();
                }
            });
        }
    }

    private void returnToHomeDialog() {
        /*******************************************************************************************
         * Function: returnToHomeDialog
         *
         * Purpose: Function creates a alert dialog to return to home and leave scoring
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        final String DIALOG_TITLE = "Leaving Event";
        final String DIALOG_MSG = "Are you sure you want to leave this event? " +
                "(All event data will be lost)";

        final boolean POSITIVE_BTN = true;  // Right
        final boolean NEUTRAL_BTN = false;   // Left
        final boolean NEGATIVE_BTN = true;  // Middle

        final String POSITIVE_BUTTON_TXT = "STAY";
        final String NEUTRAL_BUTTON_TXT = "";
        final String NEGATIVE_BUTTON_TXT = "LEAVE";

        final int POSITIVE_BTN_COLOR = Color.BLUE;
        final int NEUTRAL_BTN_COLOR = Color.RED;
        final int NEGATIVE_BTN_COLOR = Color.RED;


        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        // Set Dialog Title
        alertDialog.setTitle(DIALOG_TITLE);

        // Set Dialog Message
        alertDialog.setMessage(DIALOG_MSG);

        // Set Buttons
        // Positive Button, Right
        if (POSITIVE_BTN) {
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, POSITIVE_BUTTON_TXT, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Processed with onClick below

                }
            });
        }

        // Neutral Button, Left
        if (NEUTRAL_BTN) {
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, NEUTRAL_BUTTON_TXT, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Processed with onClick below
                }
            });
        }

        // Negative Button, Middle
        if (NEGATIVE_BTN) {
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, NEGATIVE_BUTTON_TXT, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Processed with onClick below
                }
            });
        }


        new Dialog(getApplicationContext());
        alertDialog.show();

        // Set Button Colors
        if (POSITIVE_BTN) {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(POSITIVE_BTN_COLOR);
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Perform Action on Positive button

                    alertDialog.dismiss();
                }
            });
        }
        if (NEUTRAL_BTN) {
            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(NEUTRAL_BTN_COLOR);
            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Perform Action on NEUTRAL Button

                }
            });
        }
        if (NEGATIVE_BTN) {
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(NEGATIVE_BTN_COLOR);
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Perform Action on Negative button

                    alertDialog.dismiss();

                    Intent homeActivity = new Intent(NewShootingEventActivity.this, homeActivity.class);
                    homeActivity.putExtra(CURRENT_USER_KEY, mCurrentProfileID_Int);
                    startActivity(homeActivity);
                }
            });
        }
    }

}
