/***************************************************************************************************
 * FILENAME : homeActivity.java
 *
 * AUTHOR : Dalton Shadle
 *
 * DESCRIPTION : Holds functions for the home activity of this application
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
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;

public class homeActivity extends AppCompatActivity implements View.OnClickListener {
    //********************************* Variables and Constants ************************************
    //General Constants
    private String CURRENT_USER_KEY;
    private String NUM_SHOOTER_KEY;
    private String SHOOTER_LIST_KEY;

    //General Variables
    private String mCurrentUserEmail_Str = "********";
    private ArrayList<String> shootName_List;
    private ArrayList<String> DIALOG_MSG_TXT;
    private ArrayList<String> POS_BTN_TXT;
    private ArrayList<String> NEU_BTN_TXT;
    private int NEW_EVENT_DIALOG_STATE = 0;

    // UI References

    //Google Variables
    FirebaseAuth auth;

    //*********************************** Home Activity Functions **********************************
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

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_home_landscape);
        }
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            setContentView(R.layout.activity_home_portrait);
        }

        if (savedInstanceState != null) {
            mCurrentUserEmail_Str = savedInstanceState.getString(CURRENT_USER_KEY);
        } else {
            mCurrentUserEmail_Str = getIntent().getStringExtra(CURRENT_USER_KEY);
        }

        initializeViews();
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
        savedInstanceState.putString(CURRENT_USER_KEY, mCurrentUserEmail_Str);
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
        mCurrentUserEmail_Str = savedInstanceState.getString(CURRENT_USER_KEY);
    }

    @Override
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
            setContentView(R.layout.activity_home_portrait);
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_home_landscape);
        }

        initializeViews();
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
        inflater.inflate(R.menu.home_activity_menu, menu);
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

        if (id == R.id.homeSignOut_MenuItem) {
            // Sign out user
            auth.signOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
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

        Intent i;
        switch (view.getId()) {
            case R.id.btnHomeNewEvent:
                newEventDialog(this);
                break;
            case R.id.btnHomeArmory:
                i = new Intent(this, ArmoryActivity.class);
                i.putExtra(CURRENT_USER_KEY, mCurrentUserEmail_Str);
                startActivity(i);
                break;
            case R.id.btnHomeEventHistory:
                i = new Intent(this, EventHistoryActivity.class);
                i.putExtra(CURRENT_USER_KEY, mCurrentUserEmail_Str);
                startActivity(i);
                break;
            case R.id.btnHomeProfiles:
                i = new Intent(this, ProfilesActivity.class);
                i.putExtra(CURRENT_USER_KEY, mCurrentUserEmail_Str);
                startActivity(i);
                break;
            case R.id.btnHomeTeams:
                i = new Intent(this, TeamsActivity.class);
                i.putExtra(CURRENT_USER_KEY, mCurrentUserEmail_Str);
                startActivity(i);
                break;
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

        CURRENT_USER_KEY = getString(R.string.current_user_key);
        NUM_SHOOTER_KEY = getString(R.string.num_shooter_key);
        SHOOTER_LIST_KEY = getString(R.string.shooter_list_key);
    }

    private void initializeViews(){
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

        // Initializing all buttons
        Button mBtnNewEvent = findViewById(R.id.btnHomeNewEvent);
        Button mBtnArmory = findViewById(R.id.btnHomeArmory);
        Button mBtnEventHistory = findViewById(R.id.btnHomeEventHistory);
        Button mBtnProfiles = findViewById(R.id.btnHomeProfiles);
        Button mBtnTeams = findViewById(R.id.btnHomeTeams);

        mBtnNewEvent.setOnClickListener(this);
        mBtnArmory.setOnClickListener(this);
        mBtnEventHistory.setOnClickListener(this);
        mBtnProfiles.setOnClickListener(this);
        mBtnTeams.setOnClickListener(this);

        // Set action bar title
        try {
            setTitle(mCurrentUserEmail_Str);
        } catch (Exception e) {
            // Didn't work.
        }

        // Initialize Google code for storing
        auth = FirebaseAuth.getInstance();
        // this listener will be called when there is change in firebase user session
        FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(homeActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
        auth.addAuthStateListener(authListener);
    }

    //************************************** Other Functions ***************************************
    private void initializeNewEventDialogStrings() {
        /*******************************************************************************************
         * Function: initializeNewEventDialogStrings
         *
         * Purpose: Function initializes all the string list for the dialog message
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        shootName_List = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            shootName_List.add("Shooter " + Integer.toString(i));
        }

        DIALOG_MSG_TXT = new ArrayList<String>(Arrays.asList(
                "How many shooters for this event?",
                "Enter the shooter's name.",
                "Enter the shooter's name.",
                "Enter the shooter's name.",
                "Enter the shooter's name.",
                "Enter the shooter's name."));
        POS_BTN_TXT = new ArrayList<String>(Arrays.asList(
                "NEXT",
                "NEXT",
                "NEXT",
                "NEXT",
                "NEXT",
                "CONTINUE"));
        NEU_BTN_TXT = new ArrayList<String>(Arrays.asList(
                "CANCEL",
                "BACK",
                "BACK",
                "BACK",
                "BACK",
                "BACK"));
    }

    private void newEventDialog(final Context context){
        /*******************************************************************************************
         * Function: newEventDialog
         *
         * Purpose: Function creates dialog and prompts user to enter info for new event
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        // Initialize shooter name list for new event
        initializeNewEventDialogStrings();

        // Constants for Dialog
        final String DIALOG_TITLE = "New Event";
        final String DIALOG_MSG = DIALOG_MSG_TXT.get(NEW_EVENT_DIALOG_STATE);

        final String POSITIVE_BUTTON_TXT = POS_BTN_TXT.get(NEW_EVENT_DIALOG_STATE);
        final String NEUTRAL_BUTTON_TXT =  NEU_BTN_TXT.get(NEW_EVENT_DIALOG_STATE);

        final int POSITIVE_BTN_COLOR = Color.BLUE;
        final int NEUTRAL_BTN_COLOR = Color.RED;

        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Set Dialog Title
        alertDialog.setTitle(DIALOG_TITLE);

        // Set Dialog Message
        alertDialog.setMessage(DIALOG_MSG);

        // Set view for gathering information
        final LinearLayout subView_LnrLay = new LinearLayout(context);
        subView_LnrLay.setOrientation(LinearLayout.VERTICAL);

        // Set number picker view
        XmlResourceParser parser = getResources().getLayout(R.layout.view_horizontal_number_picker);
        AttributeSet attributeSet = Xml.asAttributeSet(parser);
        final com.travijuu.numberpicker.library.NumberPicker shooter_NumPick =
                new com.travijuu.numberpicker.library.NumberPicker(context, attributeSet);
        shooter_NumPick.setMax(5);
        shooter_NumPick.setMin(1);
        shooter_NumPick.setUnit(1);
        shooter_NumPick.setGravity(Gravity.CENTER);
        subView_LnrLay.addView(shooter_NumPick);

        // Set edit text view
        final EditText item_Edt = new EditText(context);
        item_Edt.setGravity(Gravity.START);
        item_Edt.setTextColor(Color.BLACK);

        // Add linear layout to alert dialog
        alertDialog.setView(subView_LnrLay);

        // Positive Button, Right
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, POSITIVE_BUTTON_TXT, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Processed by onClick below
            }
        });

        // Neutral Button, Left
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, NEUTRAL_BUTTON_TXT, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Processed by onClick below
            }
        });

        new Dialog(context);
        alertDialog.show();

        // Set Buttons
        final Button pos_Btn = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        final Button neu_Btn = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);

        pos_Btn.setTextColor(POSITIVE_BTN_COLOR);
        pos_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform Action on Positive button
                int numShooter_Int = shooter_NumPick.getValue();
                String shooterName_Str = item_Edt.getText().toString();

                switch (NEW_EVENT_DIALOG_STATE) {
                    case 0:
                        // Number picker to entering shooter 1 info
                        subView_LnrLay.removeView(shooter_NumPick);
                        subView_LnrLay.addView(item_Edt);

                        NEW_EVENT_DIALOG_STATE = (NEW_EVENT_DIALOG_STATE + 1);
                        break;
                    default:
                        // For all shooter info
                        if (shooterName_Str.isEmpty()) {
                            item_Edt.setError(context.getString(R.string.error_field_required));
                            item_Edt.requestFocus();
                        } else {
                            shootName_List.set(NEW_EVENT_DIALOG_STATE, shooterName_Str);

                            if (numShooter_Int == NEW_EVENT_DIALOG_STATE) {
                                // Put all extras into new intent and start new event
                                Intent i = new Intent(context, NewEventActivity.class);
                                i.putStringArrayListExtra(SHOOTER_LIST_KEY, shootName_List);
                                i.putExtra(NUM_SHOOTER_KEY, numShooter_Int);
                                i.putExtra(CURRENT_USER_KEY, mCurrentUserEmail_Str);
                                startActivity(i);

                                // Dismiss dialog
                                alertDialog.dismiss();

                                // Reset state
                                NEW_EVENT_DIALOG_STATE = 0;

                            } else {
                                NEW_EVENT_DIALOG_STATE = (NEW_EVENT_DIALOG_STATE + 1);
                            }
                        }
                        break;
                }

                alertDialog.setMessage(DIALOG_MSG_TXT.get(NEW_EVENT_DIALOG_STATE));
                item_Edt.setText(shootName_List.get(NEW_EVENT_DIALOG_STATE));
                pos_Btn.setText(POS_BTN_TXT.get(NEW_EVENT_DIALOG_STATE));
                neu_Btn.setText(NEU_BTN_TXT.get(NEW_EVENT_DIALOG_STATE));
            }
        });

        neu_Btn.setTextColor(NEUTRAL_BTN_COLOR);
        neu_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform Action on Neutral button
                switch (NEW_EVENT_DIALOG_STATE) {
                    case 0:
                        // Cancel and close dialog
                        alertDialog.dismiss();
                        break;
                    case 1:
                        // Shooter 1 info to number of shooters picker
                        subView_LnrLay.removeView(item_Edt);
                        subView_LnrLay.addView(shooter_NumPick);

                        NEW_EVENT_DIALOG_STATE = (NEW_EVENT_DIALOG_STATE - 1);
                        break;
                    default:
                        // Some shooter info to previous shooter info
                        NEW_EVENT_DIALOG_STATE = (NEW_EVENT_DIALOG_STATE - 1);
                        break;
                }

                alertDialog.setMessage(DIALOG_MSG_TXT.get(NEW_EVENT_DIALOG_STATE));
                item_Edt.setText(shootName_List.get(NEW_EVENT_DIALOG_STATE));
                pos_Btn.setText(POS_BTN_TXT.get(NEW_EVENT_DIALOG_STATE));
                neu_Btn.setText(NEU_BTN_TXT.get(NEW_EVENT_DIALOG_STATE));

            }
        });
    }

}
