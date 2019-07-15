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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

public class homeActivity extends AppCompatActivity implements View.OnClickListener {
    //**************************************** Constants *******************************************
    // General Constants
    private String ADD_SHOOTER_STRING;

    // Key Constants
    private String CURRENT_USER_KEY;
    private String NUM_ROUNDS_KEY;
    private String NUM_SHOOTER_KEY;
    private String SHOOTER_LIST_KEY;

    //**************************************** Variables *******************************************
    // General Variables
    private String mCurrentUserEmail_Str = "********";

    // New Event Dialog Variables
    private ArrayList<String> shootNameCurr_List;
    private ArrayList<String> shootNameDB_List;
    private ArrayList<String> DIALOG_MSG_TXT;
    private ArrayList<String> POS_BTN_TXT;
    private ArrayList<String> NEU_BTN_TXT;
    private ArrayList<String> NEG_BTN_TXT;
    private int NEW_EVENT_DIALOG_STATE = 0;

    // UI References

    // Google Variables
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

        // Initialize constants
        initializeConstants();

        // Figure out orientation and layout
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_home_landscape);
        }
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            setContentView(R.layout.activity_home_portrait);
        }

        // Pull extra information from intent
        if (savedInstanceState != null) {
            mCurrentUserEmail_Str = savedInstanceState.getString(CURRENT_USER_KEY);
        } else {
            mCurrentUserEmail_Str = getIntent().getStringExtra(CURRENT_USER_KEY);
        }

        // Initialize views
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

        // Figure out orientation and layout
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_home_portrait);
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_home_landscape);
        }

        // Initialize views
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

        // Inflate menu item from resources
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

        CURRENT_USER_KEY = getString(R.string.current_user_key);
        NUM_ROUNDS_KEY = getString(R.string.num_rounds_key);
        NUM_SHOOTER_KEY = getString(R.string.num_shooter_key);
        SHOOTER_LIST_KEY = getString(R.string.shooter_list_key);
        ADD_SHOOTER_STRING = "Click + to add new shooter";
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

    //********************************* New Event Dialog Functions *********************************
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

        shootNameCurr_List = new ArrayList<>();

        DIALOG_MSG_TXT = new ArrayList<String>(Arrays.asList(
                "How many rounds for this event?",
                "How many shooters for this event?",
                "Enter the shooter's name. (1)",
                "Enter the shooter's name. (2)",
                "Enter the shooter's name. (3)",
                "Enter the shooter's name. (4)",
                "Enter the shooter's name. (5)"));
        POS_BTN_TXT = new ArrayList<String>(Arrays.asList(
                "NEXT",
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
                "BACK",
                "BACK"));
        NEG_BTN_TXT = new ArrayList<String>(Arrays.asList(
                " ",
                " ",
                "+",
                "+",
                "+",
                "+",
                "+"));
    }

    private ArrayAdapter<String> initializeShooterSpinnerAdapt(final Context context) {
        /*******************************************************************************************
         * Function: initializeShooterSpinnerAdapt
         *
         * Purpose: Function initializes shooter spinner for starting new event
         *
         * Parameters: context - context to get current database handler
         *
         * Returns: tempShooter_Adapt - Array adapter initialized to shooters from DB
         *
         ******************************************************************************************/

        // Initialize function variables
        ArrayAdapter<String> tempShooter_Adapt;
        ArrayList<ShooterClass> tempShooter_List;
        shootNameDB_List = new ArrayList<>();
        GlobalApplicationContext currentContext = new GlobalApplicationContext();
        final DBHandler db = new DBHandler(currentContext.getContext());

        // Pull shooters from db
        tempShooter_List = db.getAllShooterFromDB(mCurrentUserEmail_Str);

        // Iterate over all shooters and get names
        for (int i = 0; i < tempShooter_List.size(); i++) {
            shootNameDB_List.add(tempShooter_List.get(i).getShooterName_Str());
        }

        // Add shooter string to list
        shootNameDB_List.add(ADD_SHOOTER_STRING);

        // Set array adapter
        tempShooter_Adapt = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, shootNameDB_List);

        return tempShooter_Adapt;
    }

    private ArrayAdapter<String> updateShooterSpinnerAdapt(final Context context) {
        /*******************************************************************************************
         * Function: updateShooterSpinnerAdapt
         *
         * Purpose: Function updates shooter spinner for starting new event
         *
         * Parameters: context - context to get current database handler
         *
         * Returns: tempShooter_Adapt - Array adapter updated for shooters from DB, minus the
         *                              shooters already in the current shooter list
         *
         ******************************************************************************************/

        // Initialize array adapter
        ArrayAdapter<String> tempShooter_Adapt;

        // Iterate over current shooter list, remove similar shooters from DB list
        for (int i = 0; i < shootNameCurr_List.size(); i++) {
            if (shootNameDB_List.contains(shootNameCurr_List.get(i))) {
                shootNameDB_List.remove(shootNameCurr_List.get(i));
            }
        }

        // Set array adapter
        tempShooter_Adapt = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, shootNameDB_List);

        return tempShooter_Adapt;
    }

    private void addShooterToDBList(String shooterName_Str) {
        /*******************************************************************************************
         * Function: addShooterToDBList
         *
         * Purpose: Function updates DB List
         *
         * Parameters: shooterName_Str - name of shooter to add to database list
         *
         * Returns: None
         *
         ******************************************************************************************/

        // Adds shooter at correct index to database list
        int prevLastIndex_Int = shootNameDB_List.size() - 1;
        shootNameDB_List.set(prevLastIndex_Int, shooterName_Str);
        shootNameDB_List.add(ADD_SHOOTER_STRING);
    }

    private void removeLastShooterFromCurrList() {
        /*******************************************************************************************
         * Function: removeLastShooterFromCurrList
         *
         * Purpose: Function removes the last added shooter from current list and adds back to
         *          DB list
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        // Remove the shooter that was added most recently from current shooter list
        int lastIndex_Int = shootNameCurr_List.size() - 1;
        String shooterNameRemoved_Str = shootNameCurr_List.get(lastIndex_Int);
        shootNameCurr_List.remove(lastIndex_Int);
        addShooterToDBList(shooterNameRemoved_Str);
    }

    private void newEventDialog(final Context context){
        /*******************************************************************************************
         * Function: newEventDialog
         *
         * Purpose: Function creates dialog and prompts user to enter info for new event
         *
         * Parameters: context - current context for dialog and database
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
        final String NEGATIVE_BUTTON_TXT = NEG_BTN_TXT.get(NEW_EVENT_DIALOG_STATE);

        final int POSITIVE_BTN_COLOR = Color.BLUE;
        final int NEUTRAL_BTN_COLOR = Color.RED;
        final int NEGATIVE_BTN_COLOR = context.getColor(R.color.colorPrimary);

        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Set Dialog Title
        alertDialog.setTitle(DIALOG_TITLE);

        // Set Dialog Message
        alertDialog.setMessage(DIALOG_MSG);

        // Set view for gathering information
        final LinearLayout subView_LnrLay = new LinearLayout(context);
        subView_LnrLay.setOrientation(LinearLayout.VERTICAL);

        // Set views
        XmlResourceParser parser = getResources().getLayout(R.layout.view_horizontal_number_picker);
        AttributeSet attributeSet = Xml.asAttributeSet(parser);
        // Round number picker
        final com.travijuu.numberpicker.library.NumberPicker round_NumPick =
                new com.travijuu.numberpicker.library.NumberPicker(context, attributeSet);
        round_NumPick.setMax(4);
        round_NumPick.setMin(1);
        round_NumPick.setUnit(1);
        round_NumPick.setGravity(Gravity.CENTER);
        subView_LnrLay.addView(round_NumPick);

        // Shooter number picker
        final com.travijuu.numberpicker.library.NumberPicker shooter_NumPick =
                new com.travijuu.numberpicker.library.NumberPicker(context, attributeSet);
        shooter_NumPick.setMax(5);
        shooter_NumPick.setMin(1);
        shooter_NumPick.setUnit(1);
        shooter_NumPick.setGravity(Gravity.CENTER);

        // Shooter name spinner
        final Spinner item_Spin = new Spinner(context);
        item_Spin.setGravity(Gravity.START);
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(item_Spin);

            // Set popupWindow height to 500px
            popupWindow.setHeight(500);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }

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

        // Negative Button, Middle
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, NEGATIVE_BUTTON_TXT, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Processed by onClick below
            }
        });

        new Dialog(context);
        alertDialog.show();

        // Set Buttons
        final Button pos_Btn = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        final Button neu_Btn = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        final Button neg_Btn = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);

        pos_Btn.setTextColor(POSITIVE_BTN_COLOR);
        pos_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform Action on Positive button
                int numRound_Int = round_NumPick.getValue();
                int numShooter_Int = shooter_NumPick.getValue();

                switch (NEW_EVENT_DIALOG_STATE) {
                    case 0:
                        // Round number picker to shooter number picker
                        subView_LnrLay.removeView(round_NumPick);
                        subView_LnrLay.addView(shooter_NumPick);

                        NEW_EVENT_DIALOG_STATE = (NEW_EVENT_DIALOG_STATE + 1);
                        break;
                    case 1:
                        // Shooter number picker to entering shooter 1 info
                        subView_LnrLay.removeView(shooter_NumPick);
                        subView_LnrLay.addView(item_Spin);
                        item_Spin.setAdapter(initializeShooterSpinnerAdapt(context));

                        NEW_EVENT_DIALOG_STATE = (NEW_EVENT_DIALOG_STATE + 1);
                        break;
                    default:
                        // For all shooter info
                        String shooterNameSpin_Str = item_Spin.getSelectedItem().toString();

                        if (shooterNameSpin_Str.equals(ADD_SHOOTER_STRING)) {
                            // Is the default text, set error
                        } else {
                            // All is good, keep going
                            shootNameCurr_List.add(shooterNameSpin_Str);

                            if (NEW_EVENT_DIALOG_STATE > numShooter_Int) {
                                // Put all extras into new intent and start new event
                                Intent i = new Intent(context, NewEventActivity.class);
                                i.putStringArrayListExtra(SHOOTER_LIST_KEY, shootNameCurr_List);
                                i.putExtra(NUM_ROUNDS_KEY, numRound_Int);
                                i.putExtra(NUM_SHOOTER_KEY, numShooter_Int);
                                i.putExtra(CURRENT_USER_KEY, mCurrentUserEmail_Str);
                                startActivity(i);

                                // Dismiss dialog
                                alertDialog.dismiss();

                                // Reset state
                                NEW_EVENT_DIALOG_STATE = 0;
                            } else {
                                item_Spin.setAdapter(updateShooterSpinnerAdapt(context));
                                NEW_EVENT_DIALOG_STATE = (NEW_EVENT_DIALOG_STATE + 1);
                            }
                        }
                        break;
                }

                alertDialog.setMessage(DIALOG_MSG_TXT.get(NEW_EVENT_DIALOG_STATE));
                pos_Btn.setText(POS_BTN_TXT.get(NEW_EVENT_DIALOG_STATE));
                neu_Btn.setText(NEU_BTN_TXT.get(NEW_EVENT_DIALOG_STATE));
                neg_Btn.setText(NEG_BTN_TXT.get(NEW_EVENT_DIALOG_STATE));
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
                        // Shooter number picker to round number picker
                        subView_LnrLay.removeView(shooter_NumPick);
                        subView_LnrLay.addView(round_NumPick);

                        NEW_EVENT_DIALOG_STATE = (NEW_EVENT_DIALOG_STATE - 1);
                        break;
                    case 2:
                        // Shooter 1 info to number of shooters picker
                        subView_LnrLay.removeView(item_Spin);
                        subView_LnrLay.addView(shooter_NumPick);

                        NEW_EVENT_DIALOG_STATE = (NEW_EVENT_DIALOG_STATE - 1);
                        break;
                    default:
                        // Some shooter info to previous shooter info
                        NEW_EVENT_DIALOG_STATE = (NEW_EVENT_DIALOG_STATE - 1);
                        removeLastShooterFromCurrList();
                        updateShooterSpinnerAdapt(context);
                        break;
                }

                alertDialog.setMessage(DIALOG_MSG_TXT.get(NEW_EVENT_DIALOG_STATE));
                pos_Btn.setText(POS_BTN_TXT.get(NEW_EVENT_DIALOG_STATE));
                neu_Btn.setText(NEU_BTN_TXT.get(NEW_EVENT_DIALOG_STATE));
                neg_Btn.setText(NEG_BTN_TXT.get(NEW_EVENT_DIALOG_STATE));
            }
        });

        neg_Btn.setTextColor(NEGATIVE_BTN_COLOR);
        neg_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform Action on Negative button
                AlertDialog newShooter_Dialog = newShooterDialog(alertDialog.getContext());
                newShooter_Dialog.show();
                newShooter_Dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        item_Spin.setAdapter(updateShooterSpinnerAdapt(context));
                    }
                });
            }
        });
    }

    private AlertDialog newShooterDialog(final Context context){
        /*******************************************************************************************
         * Function: newShooterDialog
         *
         * Purpose: Function creates dialog and prompts user to enter info for new shooter
         *
         * Parameters: context - current context for dialog and database
         *
         * Returns: alertDialog - Returns an alert dialog message that was built from this
         *
         ******************************************************************************************/

        // Constants for Dialog
        final String DIALOG_TITLE = "New Shooter";
        final String DIALOG_MSG = "Enter the name of the shooter.";

        final String POSITIVE_BUTTON_TXT = "ADD";
        final String NEUTRAL_BUTTON_TXT =  "CANCEL";

        final int POSITIVE_BTN_COLOR = Color.BLUE;
        final int NEUTRAL_BTN_COLOR = Color.RED;

        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        final DBHandler db = new DBHandler(context);

        // Set Dialog Title
        alertDialog.setTitle(DIALOG_TITLE);

        // Set Dialog Message
        alertDialog.setMessage(DIALOG_MSG);

        // Set view for gathering information
        final LinearLayout subView_LnrLay = new LinearLayout(context);
        subView_LnrLay.setOrientation(LinearLayout.VERTICAL);

        // Set views
        final EditText item_Edt = new EditText(context);
        item_Edt.setGravity(Gravity.START);
        item_Edt.setTextColor(Color.BLACK);
        subView_LnrLay.addView(item_Edt);

        // Add linear layout to alert dialog
        alertDialog.setView(subView_LnrLay);

        // Positive Button, Right
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, POSITIVE_BUTTON_TXT, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String shooterNameEdt_Str = item_Edt.getText().toString();

                if (shooterNameEdt_Str.isEmpty()) {
                    item_Edt.setError(context.getString(R.string.error_field_required));
                    item_Edt.requestFocus();
                } else {
                    ShooterClass temp_Shooter = new ShooterClass();

                    // TODO: Need to check if name is already in DB for coach
                    if (db.isShooterInDB(mCurrentUserEmail_Str, shooterNameEdt_Str, -1)) {
                        // Shooter name is already in the database
                        item_Edt.setError(context.getString(R.string.error_shooter_name_already_exits));
                        item_Edt.requestFocus();
                    } else {
                        // Shooter name is not in database, proceed
                        temp_Shooter.setShooterName_Str(shooterNameEdt_Str);
                        temp_Shooter.setShooterCoach_Str(mCurrentUserEmail_Str);

                        // Add shooter to database
                        db.insertShooterInDB(temp_Shooter);

                        // Add shooter to current shooter list
                        addShooterToDBList(shooterNameEdt_Str);

                        // Dismiss dialog and return to previous dialog
                        alertDialog.dismiss();
                    }
                }
            }
        });

        // Neutral Button, Left
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, NEUTRAL_BUTTON_TXT, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Processed by onClick below
                alertDialog.dismiss();
            }
        });

        alertDialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(POSITIVE_BTN_COLOR);
                alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(NEUTRAL_BTN_COLOR);
            }
        });

        new Dialog(context);

        return alertDialog;
    }

}
