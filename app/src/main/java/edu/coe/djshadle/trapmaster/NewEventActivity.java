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
import android.text.Layout;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NewEventActivity extends AppCompatActivity implements OnTotalHitChange {

    //********************************** Variables and Constants ***********************************
    // General Constants
    private String TAG = "JRW";
    private String ACTIVITY_TITLE;
    private String CURRENT_USER_KEY;
    private String NUM_SHOOTER_KEY;
    private String SHOOTER_LIST_KEY;
    private String SHOOTER_SCORE_LIST_KEY;
    private final int TOTAL_NUM_SHOTS = 25;
    private final String TRAP_STATE_KEY = "TRAP_STATE_KEY";
    private final String TRAP_EXPAND_KEY = "TRAP_EXPAND_KEY";

    // General Variables
    private int numShooters_Int = 1;
    private boolean quickEventFlag_Bool;
    private ArrayList<String> shooterNames_Array;
    private ArrayList<Integer> shooterScores_Array;
    private ArrayList<Integer> trapState_Array;
    private ArrayList<Integer> trapExpand_Array;
    private String mCurrentUserEmail_Str = "Quick Event";
    DBHandler db;

    // UI References
    private ArrayList<TrapScoreItemClass> trapScoreViews_Array;
    private LinearLayout trap_LnrLay;
    private Button mSave_Btn;

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

        initializeConstants();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_new_event_portrait);
        }
        else {
            setContentView(R.layout.activity_new_event_landscape);
        }

        if (savedInstanceState != null) {
            trapState_Array = savedInstanceState.getIntegerArrayList(TRAP_STATE_KEY);
            trapExpand_Array = savedInstanceState.getIntegerArrayList(TRAP_EXPAND_KEY);
            setAllTrapStates(trapState_Array);
            setAllExpandStates(trapExpand_Array);
        } else {
            mCurrentUserEmail_Str = getIntent().getStringExtra(CURRENT_USER_KEY);
            numShooters_Int = getIntent().getIntExtra(NUM_SHOOTER_KEY, 1);
            shooterNames_Array = getIntent().getStringArrayListExtra(SHOOTER_LIST_KEY);
            quickEventFlag_Bool = getIntent().getBooleanExtra(getString(R.string.quick_event_flag_key), false);
            trapExpand_Array = new ArrayList<>(Collections.nCopies(5, 0));
        }

        if (mCurrentUserEmail_Str == null) {
            mCurrentUserEmail_Str = "Quick Event";
        }

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

        trapState_Array = getAllTrapStates();
        trapExpand_Array = getAllExpandStates();
        initializeViews();
        setAllTrapStates(trapState_Array);
        setAllExpandStates(trapExpand_Array);

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

        trapState_Array = getAllTrapStates();
        trapExpand_Array = getAllExpandStates();
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
        setAllTrapStates(trapState_Array);
        setAllExpandStates(trapExpand_Array);

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
            backButtonDialog();
        }
        return super.onKeyDown(keyCode, event);
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

        ACTIVITY_TITLE = getString(R.string.new_event_activity_title);
        CURRENT_USER_KEY = getString(R.string.current_user_key);
        NUM_SHOOTER_KEY = getString(R.string.num_shooter_key);
        SHOOTER_LIST_KEY = getString(R.string.shooter_list_key);
        SHOOTER_SCORE_LIST_KEY =  getString(R.string.shooter_score_list_key);
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

        // Initializing linear layout
        trap_LnrLay = findViewById(R.id.newEventTrap_LnrLay);

        // Initializing all trap counters
        initializeTrapCounters();

        // Initializing all buttons
        mSave_Btn = findViewById(R.id.newEventSave_Btn);
        mSave_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allTrapsChecked()) {
                    // All traps are checked and ready to save
                    Log.d("JRW", "Save button quick event: " + Boolean.toString(quickEventFlag_Bool));
                    if (quickEventFlag_Bool) {
                        // Quick Event, must have them log in
                        quickEventLogin();
                    } else {
                        // Not a quick event, launch post event dialog
                        postEventDialog();
                    }
                } else {
                    // Not all traps are checked, not ready to save
                    Toast.makeText(NewEventActivity.this,
                            "All scoring buttons need to be checked", Toast.LENGTH_LONG).show();
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

        trapScoreViews_Array = new ArrayList<>();

        for (int i = 0; i < numShooters_Int; i++) {
            final TrapScoreItemClass temp_View = new TrapScoreItemClass(this, trapExpand_Array.get(i) != 0);
            temp_View.setTotalHitChange(this);
            temp_View.setRoundText(1);
            temp_View.setShooterText(shooterNames_Array.get(i));
            temp_View.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    temp_View.setExpandBool(!temp_View.getExpandBool());
                }
            });

            trapScoreViews_Array.add(temp_View);
            trap_LnrLay.addView(temp_View);
        }
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

        //totalHits_Int = trapScore_View.getTotalNumberHit();
    }

    private ArrayList<Integer> getAllTrapStates() {
        /*******************************************************************************************
         * Function: getAllTrapStates
         *
         * Purpose: Function gets all the states of the trap counters and returns in a list
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

    private void setAllTrapStates(ArrayList<Integer> allStates_List) {
        /*******************************************************************************************
         * Function: setAllTrapStates
         *
         * Purpose: Function sets all the states of the traps
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

    private ArrayList<Integer> getAllExpandStates(){
        /*******************************************************************************************
         * Function: getAllExpandStates
         *
         * Purpose: Function gets all the expand states of the trap counters and returns in a list
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

    private void setAllExpandStates(ArrayList<Integer> expandStates_List) {
        /*******************************************************************************************
         * Function: setAllExpandStates
         *
         * Purpose: Function sets all the expand tates of the traps
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

    private ArrayList<Integer> getAllTrapScores() {
        /*******************************************************************************************
         * Function: getAllTrapScores
         *
         * Purpose: Function returns a list of all the trap scores in order
         *
         * Parameters: None
         *
         * Returns: tempScore_List
         *
         ******************************************************************************************/

        ArrayList<Integer> tempScore_List = new ArrayList<>();

        for (int i = 0; i < numShooters_Int; i++) {
            tempScore_List.add(trapScoreViews_Array.get(i).getTotalNumberHit());
        }

        return  tempScore_List;
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

        DBHandler db = new DBHandler(getApplicationContext());

        for (int i = 0; i < numShooters_Int; i++) {
            ShotClass temp_Shot = new ShotClass();

            temp_Shot.setShotShooterName_Str(shooterNames_Array.get(i));
            temp_Shot.setShotHitNum_Str(Integer.toString(trapScoreViews_Array.get(i).getTotalNumberHit()));
            temp_Shot.setShotTotalNum_Str(Integer.toString(TOTAL_NUM_SHOTS));

            db.insertShotInDB(temp_Shot);
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
                            quickEventFlag_Bool = false;
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

                    alertDialog.dismiss();

                    Intent postEventActivity_Intent = new Intent(NewEventActivity.this, PostEventActivity.class);
                    postEventActivity_Intent.putExtra(CURRENT_USER_KEY, mCurrentUserEmail_Str);
                    postEventActivity_Intent.putStringArrayListExtra(SHOOTER_LIST_KEY, shooterNames_Array);
                    postEventActivity_Intent.putIntegerArrayListExtra(SHOOTER_SCORE_LIST_KEY, getAllTrapScores());
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

                    alertDialog.dismiss();

                    saveScoresToDB();

                    Intent homeActivity_Intent = new Intent(NewEventActivity.this, homeActivity.class);
                    homeActivity_Intent.putExtra(CURRENT_USER_KEY, mCurrentUserEmail_Str);
                    startActivity(homeActivity_Intent);

                    NewEventActivity.this.finish();
                }
            });
        }
    }

    private void backButtonDialog() {
        /*******************************************************************************************
         * Function: backButtonDialog
         *
         * Purpose: Function creates a alert dialog to when back button is hit
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

        final String POSITIVE_BUTTON_TXT = "LEAVE";
        final String NEUTRAL_BUTTON_TXT = "";
        final String NEGATIVE_BUTTON_TXT = "STAY";

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

                    Intent homeActivity = new Intent(NewEventActivity.this, homeActivity.class);
                    homeActivity.putExtra(CURRENT_USER_KEY, mCurrentUserEmail_Str);
                    startActivity(homeActivity);
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
                }
            });
        }
    }

}
