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
import java.util.List;
import java.util.Map;

public class NewEventActivity extends AppCompatActivity implements OnTotalHitChange {

    //********************************** Variables and Constants ***********************************
    // General Constants
    private String TAG = "JRW";
    private String ACTIVITY_TITLE;
    private String CURRENT_USER_KEY;
    private String NUM_ROUNDS_KEY;
    private String NUM_SHOOTER_KEY;
    private String SHOOTER_LIST_KEY;
    private String SHOOTER_SCORE_LIST_KEY;
    private final int TOTAL_NUM_SHOTS = 25;
    private final int ROUND_SCORE_KEY = TOTAL_NUM_SHOTS;
    private final String TRAP_STATE_KEY = "TRAP_STATE_KEY";
    private final String TRAP_EXPAND_KEY = "TRAP_EXPAND_KEY";

    // General Variables
    private int numShooters_Int = 1;
    private int numRounds_Int = 1;
    private int currentRound_Int = 1;
    private ArrayList<String> shooterNames_Array;
    private ArrayList<Integer> trapState_Array;
    private ArrayList<Integer> trapExpand_Array;
    private String mCurrentUserEmail_Str = "Quick Event";
    private DBHandler db;
    // 3D array of shooter scores by round, takes the form [round #][shooter #}{score index] = hit/miss
    private Map<Integer, Map<Integer, ArrayList<Integer>>> shooterScores_Array;


    // UI References
    private ArrayList<TrapScoreItemClass> trapScoreViews_Array;
    private LinearLayout trap_LnrLay;
    private Button mRight_Btn;
    private Button mLeft_Btn;

    //Google Variables
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
            mCurrentUserEmail_Str = getIntent().getStringExtra(CURRENT_USER_KEY);
            numRounds_Int = getIntent().getIntExtra(NUM_ROUNDS_KEY, 1);
            numShooters_Int = getIntent().getIntExtra(NUM_SHOOTER_KEY, 1);
            shooterNames_Array = getIntent().getStringArrayListExtra(SHOOTER_LIST_KEY);
            trapExpand_Array = new ArrayList<>(Collections.nCopies(5, 0));
        }

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

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_new_event_portrait);
        }
        else {
            setContentView(R.layout.activity_new_event_landscape);
        }

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
         * Parameters: savedInstanceState (OUT) - provides the saved instances from current state
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
                homeActivity_Intent.putExtra(CURRENT_USER_KEY, mCurrentUserEmail_Str);
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

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Log.d(this.getClass().getName(), "back button pressed");
            returnToHomeDialog();
        }
        return super.onKeyDown(keyCode, event);
    }

    //************************************ Initialize Functions ************************************
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
        NUM_SHOOTER_KEY = getString(R.string.num_shooter_key);
        SHOOTER_LIST_KEY = getString(R.string.shooter_list_key);
        SHOOTER_SCORE_LIST_KEY =  getString(R.string.shooter_score_list_key);
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
        shooterScores_Array = new HashMap<>();

        // Initializing all buttons
        mRight_Btn = findViewById(R.id.newEventRight_Btn);
        mRight_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Next Button

                if (allTrapsChecked()) {
                    // All traps are checked, move to next state

                    // Save current round trap states to shooterScores_Array
                    saveCurrentTrapStates();

                    if (currentRound_Int == numRounds_Int) {
                        // Move to post event dialog
                        postEventDialog();
                    } else {
                        // Move to next round

                        // Increment current round number
                        currentRound_Int = currentRound_Int + 1;

                        // Move to next round
                        Toast.makeText(NewEventActivity.this, "Next Round", Toast.LENGTH_SHORT).show();

                        // Reset all trap counters for next round
                        resetAllTrapScores();
                        setAllTrapRounds(currentRound_Int);
                    }

                } else {
                    // Not all traps are checked, not ready to save
                    Toast.makeText(NewEventActivity.this,
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

                    // Save current round trap states to shooterScores_Array
                    saveCurrentTrapStates();

                    // Decrement current round number
                    currentRound_Int = currentRound_Int - 1;

                    // Restore previous round trap states to trap score views
                    restoreCurrentTrapStates();
                    setAllTrapRounds(currentRound_Int);
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

        ArrayList<Integer> allStates_List = new ArrayList<>();

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

        int numState_Int = 25;

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

        ArrayList<Integer> expandState_List = new ArrayList<>();

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

        for (int i = 0; i < numShooters_Int; i++) {
            trapScoreViews_Array.get(i).setExpandBool(expandStates_List.get(i) != 0);
        }
    }

    private void saveCurrentTrapStates() {
        /*******************************************************************************************
         * Function: saveCurrentTrapStates
         *
         * Purpose: Function saves trap states for the current round to shooterScore_Array,
         *          called when NEXT button is pressed
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        // Initialize current round score array
        Map<Integer, ArrayList<Integer>> currentRoundScores_Array = new HashMap<>();

        // Iterate over all shooter and collect trap states for that round, save in array
        for (int i = 0; i < numShooters_Int; i++) {
            ArrayList<Integer> currentShooterScore_Array = trapScoreViews_Array.get(i).getAllStates();
            currentShooterScore_Array.add(trapScoreViews_Array.get(i).getTotalNumberHit());
            currentRoundScores_Array.put(i, currentShooterScore_Array);
        }

        // Add current round scores to the current round index of shooterScore_Array
        shooterScores_Array.put(currentRound_Int, currentRoundScores_Array);
    }

    private void restoreCurrentTrapStates() {
        /*******************************************************************************************
         * Function: restoreCurrentTrapStates
         *
         * Purpose: Function restores trap states for the previous round in shooterScore_Array,
         *          called when PREVIOUS button is pressed
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        // Initialize previous round score array
        Map<Integer, ArrayList<Integer>> prevRoundScores_Array = shooterScores_Array.get(currentRound_Int);

        // Iterate over all shooters and set the trap score views with previous trap states
        for (int i = 0; i < numShooters_Int; i++) {
            trapScoreViews_Array.get(i).setAllStates(prevRoundScores_Array.get(i));
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
         * Returns: allChecked
         *
         ******************************************************************************************/

        boolean allChecked = true;

        for (int i = 0; i < trapScoreViews_Array.size(); i++) {
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

        for(int i = 0; i < numShooters_Int; i++) {
            trapScoreViews_Array.get(i).resetTrapCounter();
        }
    }

    private void setAllTrapRounds(int round_Int) {
        /*******************************************************************************************
         * Function: setAllTrapRounds
         *
         * Purpose: Function sets the rounds fo all trap counters
         *
         * Parameters: round_Int - round number to set all trap counter to
         *
         * Returns: allChecked
         *
         ******************************************************************************************/

        for(int i = 0; i < numShooters_Int; i++) {
            trapScoreViews_Array.get(i).setRoundText(round_Int);
        }
    }

    //************************************ Database Functions **************************************
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

        // Initialize db handler
        DBHandler db = new DBHandler(getApplicationContext());

        // Iterate over all shooters and all rounds, create a temp score object and save to db
        for (int i = 0; i < numRounds_Int; i++) {
            // Initialize round number for string reference
            int tempCurRound_Int = i + 1;
            for (int j = 0; j < numShooters_Int; j++) {
                // Initialize temp shot object
                ShotClass temp_Shot = new ShotClass();
                String round_Str = "No Event - Round " + Integer.toString(tempCurRound_Int);

                // Set necessary fields for temp shot
                temp_Shot.setShotShooterName_Str(shooterNames_Array.get(j));
                temp_Shot.setShotEventName_Str(round_Str);
                temp_Shot.setShotHitNum_Str(Integer.toString(shooterScores_Array.get(tempCurRound_Int).get(j).get(ROUND_SCORE_KEY)));
                temp_Shot.setShotTotalNum_Str(Integer.toString(TOTAL_NUM_SHOTS));

                // Insert into db
                db.insertShotInDB(temp_Shot);
            }
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
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(NewEventActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.

                        if (!task.isSuccessful()) {
                            // there was an error
                            String exceptionMsg_Str = "Sign in failed! " + task.getException().getMessage();

                            Toast.makeText(NewEventActivity.this, exceptionMsg_Str,
                                    Toast.LENGTH_LONG).show();

                            // Relaunch quick event login
                            quickEventLogin();
                        } else {
                            // Login was successful
                            // set current user and have user press save again
                            Toast.makeText(NewEventActivity.this, "Sign in successful!",
                                    Toast.LENGTH_LONG).show();
                            mCurrentUserEmail_Str = email;
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
                    Intent postEventActivity_Intent = new Intent(NewEventActivity.this, PostEventActivity.class);
                    postEventActivity_Intent.putExtra(CURRENT_USER_KEY, mCurrentUserEmail_Str);
                    postEventActivity_Intent.putExtra(NUM_ROUNDS_KEY, numRounds_Int);
                    postEventActivity_Intent.putStringArrayListExtra(SHOOTER_LIST_KEY, shooterNames_Array);
                    postEventActivity_Intent.putExtra(SHOOTER_SCORE_LIST_KEY, (HashMap) shooterScores_Array);
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
                    Intent homeActivity_Intent = new Intent(NewEventActivity.this, homeActivity.class);
                    homeActivity_Intent.putExtra(CURRENT_USER_KEY, mCurrentUserEmail_Str);
                    startActivity(homeActivity_Intent);

                    NewEventActivity.this.finish();
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

                    Intent homeActivity = new Intent(NewEventActivity.this, homeActivity.class);
                    homeActivity.putExtra(CURRENT_USER_KEY, mCurrentUserEmail_Str);
                    startActivity(homeActivity);
                }
            });
        }
    }

}
