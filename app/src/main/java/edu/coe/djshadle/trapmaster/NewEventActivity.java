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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class NewEventActivity extends AppCompatActivity implements OnTotalHitChange {

    //********************************** Variables and Constants ***********************************
    // General Constants
    private String TAG = "JRW";
    private String ACTIVITY_TITLE;
    private String CURRENT_USER_KEY;
    private final boolean PORTRAIT_ORIENTATION = false; // Allow portrait orientation, landscape
                                                        // is the default TODO: Support Portrait
    private final int TOTAL_NUM_SHOTS = 25;
    private final String TRAP_STATE_KEY = "TRAP_STATE_KEY";

    // General Variables
    private int totalHits_Int = 0;
    private boolean quickEventFlag_Bool;
    private ArrayList<Integer> trapState_Array;
    private String mCurrentUserEmail_Str = "Quick Event";
    DBHandler db;

    // UI References
    TrapScoreItemClass trapScore_View;
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

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT && PORTRAIT_ORIENTATION) {
            setContentView(R.layout.activity_new_event_portrait);
        }
        else {
            // Landscape is the default orientation by AndroidManifest.xml
            setContentView(R.layout.activity_new_event_landscape);
        }

        if (savedInstanceState != null) {
            trapState_Array = savedInstanceState.getIntegerArrayList(TRAP_STATE_KEY);
            trapScore_View.setAllStates(trapState_Array);

        } else {
            mCurrentUserEmail_Str = getIntent().getStringExtra(CURRENT_USER_KEY);
            quickEventFlag_Bool = getIntent().getBooleanExtra(getString(R.string.quick_event_flag_key), false);
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

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT && PORTRAIT_ORIENTATION) {
            setContentView(R.layout.activity_new_event_portrait);
        }
        else {
            // Landscape is the default orientation by AndroidManifest.xml
            setContentView(R.layout.activity_new_event_landscape);
        }

        trapState_Array = trapScore_View.getAllStates();
        initializeViews();
        trapScore_View.setAllStates(trapState_Array);

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

        trapState_Array = trapScore_View.getAllStates();
        savedInstanceState.putIntegerArrayList(TRAP_STATE_KEY, trapState_Array);

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
        trapScore_View.setAllStates(trapState_Array);

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

        if (id == R.id.newEventCancel_MenuItem) {
            // Cancel shoot, return to home
            Intent homeActivity_Intent = new Intent(this, homeActivity.class);
            homeActivity_Intent.putExtra(CURRENT_USER_KEY, mCurrentUserEmail_Str);
            startActivity(homeActivity_Intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
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

        // Initializing all trap counter
        trapScore_View = findViewById(R.id.newEventTrapScore_View);
        trapScore_View.setTotalHitChange(this);
        trapScore_View.setRoundText(1);
        trapScore_View.setUserEmailText(mCurrentUserEmail_Str);
        trapScore_View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Custom View OnClick");
                if (trapScore_View.getExpandBool()){
                    trapScore_View.collapseView(NewEventActivity.this);
                } else {
                    trapScore_View.expandView(NewEventActivity.this);
                }
            }
        });

        // Initializing all buttons
        mSave_Btn = findViewById(R.id.save_Btn);
        mSave_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (trapScore_View.allChecked()) {
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

        totalHits_Int = trapScore_View.getTotalNumberHit();
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

        DBHandler db = new DBHandler(getApplicationContext());

        ShotClass temp_Shot = new ShotClass(email_Str, eventName_Str, Integer.toString(totalShot_Int),
                Integer.toString(totalHit_Int), notes_Str);

        db.insertShotInDB(temp_Shot);
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
                "(This let's you add notes and tag an event)";

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
                    postEventActivity_Intent.putExtra(getString(R.string.current_user_score), totalHits_Int);
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

                    saveScoreToDB(mCurrentUserEmail_Str, "", TOTAL_NUM_SHOTS,
                            totalHits_Int, "");

                    Intent homeActivity_Intent = new Intent(NewEventActivity.this, homeActivity.class);
                    homeActivity_Intent.putExtra(CURRENT_USER_KEY, mCurrentUserEmail_Str);
                    startActivity(homeActivity_Intent);
                }
            });
        }
    }

}
