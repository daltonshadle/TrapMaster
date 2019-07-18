/***************************************************************************************************
 * FILENAME : EventHistoryActivity.java
 *
 * AUTHOR : Dalton Shadle
 *
 * DESCRIPTION : Holds functions for the Event History activity of this application
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
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class EventHistoryActivity extends AppCompatActivity {
    //**************************************** Constants *******************************************
    // General Constants
    private String TAG = "JRW";
    private String ACTIVITY_TITLE;
    private String ADD_SHOOTER_STRING;

    // Key Constants
    // Key Constants
    private String CURRENT_USER_KEY;
    private String NUM_ROUNDS_KEY;
    private String NUM_SHOOTER_KEY;
    private String SHOOTER_LIST_KEY;

    // Tag Constants
    private final int SHOT_LIST_TAG = 1;
    private final int EVENT_LIST_TAG = 2;

    //**************************************** Variables *******************************************
    // General Variables
    private String mCurrentUserEmail_Str = "********";
    private DBHandler db;
    private boolean isPortrait = true;

    // New Event Dialog Variables
    private ArrayList<String> shootNameCurr_List;
    private ArrayList<String> shootNameDB_List;
    private ArrayList<String> DIALOG_MSG_TXT;
    private ArrayList<String> POS_BTN_TXT;
    private ArrayList<String> NEU_BTN_TXT;
    private ArrayList<String> NEG_BTN_TXT;
    private int NEW_EVENT_DIALOG_STATE = 0;

    // Shot
    private TrapMasterListArrayAdapter mCustomShotList_Adapt;

    // Event
    private TrapMasterListArrayAdapter mCustomEventList_Adapt;

    // UI References
    private ListView mShotList_View,  mEventList_View;

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

        // Initialize constants for this activity
        initializeConstants();

        // Figure out orientation and layout
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            setContentView(R.layout.activity_event_history_portrait);
            isPortrait = true;
        } else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_event_history_landscape);
            isPortrait = false;
        }

        // Pull extra information from intent
        if (savedInstanceState != null) {

        } else {
            mCurrentUserEmail_Str = getIntent().getStringExtra(CURRENT_USER_KEY);
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

        // Figure out orientation and layout
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_event_history_portrait);
            isPortrait = true;
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_event_history_landscape);
            isPortrait = false;
        }

        // Initialize views
        initializeViews();
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

        ACTIVITY_TITLE = getString(R.string.event_history_activity_title);
        CURRENT_USER_KEY = getString(R.string.current_user_key);
        NUM_ROUNDS_KEY = getString(R.string.num_rounds_key);
        NUM_SHOOTER_KEY = getString(R.string.num_shooter_key);
        SHOOTER_LIST_KEY = getString(R.string.shooter_list_key);
        ADD_SHOOTER_STRING = "Click + to add new shooter";
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

        // Initializing list and database variables
        db = new DBHandler(getApplicationContext());

        // Initializing list view
        mShotList_View = findViewById(R.id.eventHistoryScore_List);
        mEventList_View = findViewById(R.id.eventHistoryEvent_List);

        // Setting tags for list views
        mShotList_View.setTag(SHOT_LIST_TAG);
        mEventList_View.setTag(EVENT_LIST_TAG);

        setListViewLayoutParams();

        initializeShotListView();
        initializeEventListView();

        // Initializing buttons
        FloatingActionButton mAddShot = findViewById(R.id.eventHistoryAddScore_Btn);
        FloatingActionButton mAddEvent = findViewById(R.id.eventHistoryAddEvent_Btn);

        mAddShot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Prompt user with new event dialog
                newEventDialog(EventHistoryActivity.this);
            }
        });

        mAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Display add event dialog box for user input, -1 = adding a new event
                EventClass temp_Event = new EventClass();
                temp_Event.setEventID_Int(-1);
                temp_Event.setEventEmail_Str(mCurrentUserEmail_Str);
                temp_Event.editEventDialog(EventHistoryActivity.this, mCustomEventList_Adapt);
            }
        });

        // Setting title of activity
        setTitle(ACTIVITY_TITLE);
    }

    //************************************ List view Functions *************************************
    // Shot List
    private ArrayList<RoundClass> refreshShotList() {
        /*******************************************************************************************
         * Function: refreshShotList
         *
         * Purpose: Function returns the current list of shots for the current user
         *
         * Parameters: None
         *
         * Returns: currentShotStr_List - a string list of shot for current user
         *
         ******************************************************************************************/

        // Initialize db handler and shooter and score arrays
        db = new DBHandler(this);
        ArrayList<ShooterClass> currentShooter_List = db.getAllShooterFromDB(mCurrentUserEmail_Str);
        ArrayList<RoundClass> currentShot_List = new ArrayList<>();

        // For each shooter in the db shooter list, add scores for that shooter to score list.
        for (ShooterClass shooter : currentShooter_List) {
            currentShot_List.addAll(db.getAllShotFromDB(shooter.getShooterName_Str()));
            Collections.sort(currentShot_List, Collections.<RoundClass>reverseOrder());
        }

        return currentShot_List;
    }

    private void initializeShotListView() {
        /*******************************************************************************************
         * Function: initializeShotListView
         *
         * Purpose: Function initializes the shot list view
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        try {
            // Initialize shot adapter
            mCustomShotList_Adapt = new TrapMasterListArrayAdapter(this,
                    (ArrayList<Object>)(ArrayList<?>)(refreshShotList()));


            mCustomShotList_Adapt.refreshShotArrayAdapter(refreshShotList());

            mShotList_View.setAdapter(mCustomShotList_Adapt);

        } catch (Exception e){
            Log.d("JRW", "no shots in db for this user: " + e.toString());
        }

    }

    // Event List
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

        // Initialize db handler and shooter and event array
        db = new DBHandler(this);
        ArrayList<EventClass> userEvent_List = db.getAllEventFromDB(mCurrentUserEmail_Str);

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

    // Both List
    private void setListViewLayoutParams() {
        /*******************************************************************************************
         * Function: setListViewLayoutParams
         *
         * Purpose: Function sets the layout parameters for the shot and event list view so that both
         *          use roughly half of the display (with room for buttons)
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        // Initialize variables for this function
        double screenHeight_Dbl = getResources().getDisplayMetrics().heightPixels;
        double scaleFactor_Dbl = 1;

        LinearLayout.LayoutParams params;

        // Determine variables based on orientation
        if (isPortrait) {
            scaleFactor_Dbl = 3.5;
        } else {
            scaleFactor_Dbl = 2.0;
        }

        // Set layout parameters
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)(screenHeight_Dbl / scaleFactor_Dbl));

        mEventList_View.setLayoutParams(params);
        mShotList_View.setLayoutParams(params);
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
