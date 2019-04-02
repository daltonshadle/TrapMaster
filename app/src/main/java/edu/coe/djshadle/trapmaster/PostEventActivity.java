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
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class PostEventActivity extends AppCompatActivity {
    //********************************** Variables and Constants ***********************************
    // General Constants
    private String ACTIVITY_TITLE;
    private String CURRENT_USER_KEY;
    private String DEFAULT_EVENT_TEXT;
    private String DEFAULT_GUN_TEXT;
    private String DEFAULT_LOAD_TEXT;
    private final String TAG = "JRW";
    private final int TOTAL_NUM_SHOTS = 25;

    // General Variables
    DBHandler db;
    private boolean isPortrait = true;
    private String mCurrentUserEmail_Str = "*********";
    // event
    private ArrayList<EventClass> mUserEvent_List;
    private ArrayAdapter<String> mCurrentEventList_Adapt;
    private String mEventName_Str = "";
    private int EVENT_DIALOG_STATE = 0;
    private ArrayList<String> EVENT_DIALOG_MSG, EVENT_POS_BTN_TXT, EVENT_NEU_BTN_TXT, EVENT_EDT_HINT;
    // shot
    private String mShotNotes_Str = "";
    private int mShotScore_Int = 0;

    // UI References
    private ListView mEventList_View;
    private EditText mShotNotes_Edt;

    //Google Variables

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
            setContentView(R.layout.activity_post_event_portrait);
            isPortrait = true;
        }
        else {
            setContentView(R.layout.activity_post_event_landscape);
            isPortrait = false;
        }

        if (savedInstanceState != null) {

        } else {
            mCurrentUserEmail_Str = getIntent().getStringExtra(CURRENT_USER_KEY);
            mShotScore_Int = getIntent().getIntExtra(getString(R.string.current_user_score), 0);
        }

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

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_post_event_portrait);
            isPortrait = true;
        }
        else {
            setContentView(R.layout.activity_post_event_landscape);
            isPortrait = false;
        }

        initializeViews();

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

        ACTIVITY_TITLE = getString(R.string.post_event_activity_title);
        CURRENT_USER_KEY = getString(R.string.current_user_key);
        DEFAULT_EVENT_TEXT = getString(R.string.default_event_text);
        DEFAULT_GUN_TEXT = getString(R.string.post_event_default_gun_text);
        DEFAULT_LOAD_TEXT = getString(R.string.post_event_default_load_text);
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

        // Initializing database variable
        db = new DBHandler(this);

        // Set action bar title
        try {
            setTitle(ACTIVITY_TITLE);
        } catch (Exception e) {
            // Didn't work.
        }

        // Initializing shot notes edit text
        mShotNotes_Edt = findViewById(R.id.postEventShotNotes_Edt);

        // Initializing buttons
        FloatingActionButton mAddEvent_Btn = findViewById(R.id.postEventAddEvent_Btn);
        Button mSave_Btn = findViewById(R.id.postEventSave_Btn);
        Button mCancel_Btn = findViewById(R.id.postEventCancel_Btn);

        // Adding button onClickListeners
        mAddEvent_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add event action
                //addNewEventDialog();

                EventClass temp_Event = new EventClass();
                temp_Event.setEventID_Int(-1);
                editEventDialog(temp_Event);
            }
        });

        mSave_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Save post event action

                // Get shot notes to save, save even if it is empty
                mShotNotes_Str = mShotNotes_Edt.getText().toString();

                // Save score to db, mEventName_Str may be empty (ie no event selected)
                saveScoreToDB(mCurrentUserEmail_Str, mEventName_Str, TOTAL_NUM_SHOTS,
                        mShotScore_Int, mShotNotes_Str);

                Toast.makeText(PostEventActivity.this, "Shoot saved!", Toast.LENGTH_LONG).show();

                Intent homeActivity_Intent = new Intent(PostEventActivity.this, homeActivity.class);
                homeActivity_Intent.putExtra(CURRENT_USER_KEY, mCurrentUserEmail_Str);
                startActivity(homeActivity_Intent);
            }
        });

        mCancel_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cancel post event action
                // Don't add any event or notes, just save score to database
                saveScoreToDB(mCurrentUserEmail_Str, "", TOTAL_NUM_SHOTS,
                        mShotScore_Int, "");

                Toast.makeText(PostEventActivity.this, "Shoot saved!", Toast.LENGTH_LONG).show();

                Intent homeActivity_Intent = new Intent(PostEventActivity.this, homeActivity.class);
                homeActivity_Intent.putExtra(CURRENT_USER_KEY, mCurrentUserEmail_Str);
                startActivity(homeActivity_Intent);
            }
        });

        // Initializing Listview
        mEventList_View = findViewById(R.id.postEvent_List);
        setListViewLayoutParams();
        initializeEventListView();
    }

    private void setListViewLayoutParams() {
        /*******************************************************************************************
         * Function: setListViewLayoutParams
         *
         * Purpose: Function sets the layout parameters for the list view so that it fits nicely
         *          with everything (with room for buttons and titles)
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        double screenHeight_Dbl = getResources().getDisplayMetrics().heightPixels;
        double scaleFactor_Dbl = 1;

        ConstraintLayout.LayoutParams params;

        if (isPortrait) {
            scaleFactor_Dbl = 2.0;
        } else {
            scaleFactor_Dbl = 2.0;
        }

        params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)(screenHeight_Dbl / scaleFactor_Dbl));
        params.topToBottom = mShotNotes_Edt.getId();

        mEventList_View.setLayoutParams(params);
    }

    //************************************** Event Functions ***************************************
    private ArrayList<String> refreshEventList() {
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

        mUserEvent_List = db.getAllEventFromDB(mCurrentUserEmail_Str);
        ArrayList<String> currentEventStr_List =  new ArrayList<>();

        for (int i = 0; i < mUserEvent_List.size(); i++) {
            EventClass tempEvent = mUserEvent_List.get(i);
            String gunListItem_Str = tempEvent.getEventName_Str();

            currentEventStr_List.add(gunListItem_Str);
        }

        if (currentEventStr_List.isEmpty()) {
            currentEventStr_List.add(DEFAULT_EVENT_TEXT);
        }

        return currentEventStr_List;
    }

    private void refreshEventListView() {
        /*******************************************************************************************
         * Function: refreshEventListView
         *
         * Purpose: Function refreshes the event list view with current event data from database
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        mCurrentEventList_Adapt.clear();
        mCurrentEventList_Adapt.addAll(refreshEventList());
        mCurrentEventList_Adapt.notifyDataSetChanged();
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
            ArrayList<String> currentEventStr_List = refreshEventList();

            mCurrentEventList_Adapt = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, currentEventStr_List);
            mEventList_View.setAdapter(mCurrentEventList_Adapt);

            mEventList_View.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String eventName_Str = (String) adapterView.getItemAtPosition(i);
                    Log.d(TAG, "OnItemClick for event is here: " + eventName_Str);

                    if (!isDefaultItemText(eventName_Str)) {
                        // event selected
                        mEventName_Str = eventName_Str;
                    }
                }
            });

        } catch (Exception e){
            Log.d(TAG, "no events in db for this user");
        }

    }

    //********************************* New Event Dialog Functions *********************************
    private void initializeEventDialogStrings() {
        EVENT_DIALOG_MSG = new ArrayList<String>(Arrays.asList(
                "Enter a name for this event.",
                "Enter the location for this event.",
                "Choose the gun used for this event.",
                "Choose the load used for this event.",
                "Choose the date this event took place.",
                "Enter a weather description for this event.",
                "Enter any notes for this event."));
        EVENT_POS_BTN_TXT = new ArrayList<String>(Arrays.asList(
                "NEXT",
                "NEXT",
                "NEXT",
                "NEXT",
                "NEXT",
                "NEXT",
                "SAVE"));
        EVENT_NEU_BTN_TXT = new ArrayList<String>(Arrays.asList(
                "CANCEL",
                "BACK",
                "BACK",
                "BACK",
                "BACK",
                "BACK",
                "BACK"));
        EVENT_EDT_HINT = new ArrayList<String>(Arrays.asList(
                "i.e. MyEvent",
                "i.e. Shooting Star Gun Club",
                "",
                "",
                "",
                "i.e. Warm, Sunny, Windy",
                "i.e. This was a good event!"));
    }

    private void editEventDialog(final EventClass event_Item) {
        /*******************************************************************************************
         * Function: editEventDialog
         *
         * Purpose: Function creates dialog and prompts user to add or edit an event item
         *
         * Parameters: event_Item (IN) - object to edit or add, if adding object, eventID = -1
         *
         * Returns: None
         *
         ******************************************************************************************/
        initializeEventDialogStrings();
        event_Item.setEventEmail_Str(mCurrentUserEmail_Str);

        // Dialog Constants
        String DIALOG_TITLE = "Add New Event";;

        int POSITIVE_BTN_COLOR = Color.BLUE;
        int NEUTRAL_BTN_COLOR = Color.RED;

        // Pre-Dialog Processing
        if (event_Item.getEventID_Int() != -1) {
            DIALOG_TITLE = "Edit Event";
        }

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        // Set Dialog Title
        alertDialog.setTitle(DIALOG_TITLE);

        // Set Dialog Message
        alertDialog.setMessage(EVENT_DIALOG_MSG.get(EVENT_DIALOG_STATE));

        // Set layout for gathering information
        final RelativeLayout subView_RelLay = new RelativeLayout(this);

        // Set views
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final EditText item_Edt = new EditText(this);
        item_Edt.setHint(EVENT_EDT_HINT.get(EVENT_DIALOG_STATE));
        item_Edt.setText(event_Item.getEventName_Str());
        item_Edt.setGravity(Gravity.START);
        item_Edt.setTextColor(Color.BLACK);
        item_Edt.setLayoutParams(params);
        subView_RelLay.addView(item_Edt);

        final Spinner item_Spin = new Spinner(this);
        item_Spin.setGravity(Gravity.START);
        item_Spin.setLayoutParams(params);

        final DatePicker item_Date = new DatePicker(this);


        // Add linear layout to alert dialog
        alertDialog.setView(subView_RelLay);

        // Set Buttons
        // Positive Button, Right
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, EVENT_POS_BTN_TXT.get(EVENT_DIALOG_STATE), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Processed by onClick below
            }
        });

        // Neutral Button, Left
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, EVENT_NEU_BTN_TXT.get(EVENT_DIALOG_STATE), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Processed by onClick below
            }
        });


        new Dialog(getApplicationContext());
        alertDialog.show();

        // Set Buttons
        final Button pos_Btn = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        final Button neu_Btn = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);

        pos_Btn.setTextColor(POSITIVE_BTN_COLOR);
        pos_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform Action on Positive button
                switch (EVENT_DIALOG_STATE){
                    case 0:
                        // Name to location
                        String item_Txt = item_Edt.getText().toString();

                        boolean isEventNameEmpty = item_Txt.equals("");
                        boolean isEventNameInDB = db.isEventNameInDB(mCurrentUserEmail_Str, item_Txt, event_Item.getEventID_Int());

                        if (isEventNameEmpty) {
                            // Check if the event name is empty
                            item_Edt.setError(getString(R.string.error_field_required));
                            item_Edt.requestFocus();
                        } else if (isEventNameInDB) {
                            // Check if the event name is already used in the database,
                            // user cannot have 2 events with same name
                            item_Edt.setError(getString(R.string.error_event_already_exists));
                            item_Edt.requestFocus();
                        } else {
                            event_Item.setEventName_Str(item_Txt);
                            item_Edt.setText(event_Item.getEventLocation_Str());
                            EVENT_DIALOG_STATE = (EVENT_DIALOG_STATE + 1);
                        }

                        break;
                    case 1:
                        // Location to Gun
                        item_Txt = item_Edt.getText().toString();

                        event_Item.setEventLocation_Str(item_Txt);

                        subView_RelLay.removeView(item_Edt);
                        item_Spin.setAdapter(initializeGunSpinnerAdapt());
                        subView_RelLay.addView(item_Spin);

                        EVENT_DIALOG_STATE = (EVENT_DIALOG_STATE + 1);
                        break;
                    case 2:
                        // Gun to Load
                        item_Txt = item_Spin.toString();

                        if (item_Txt.equals(DEFAULT_GUN_TEXT)) {
                            item_Txt = "";
                        }

                        event_Item.setEventGun_Str(item_Txt);

                        item_Spin.setAdapter(initializeLoadSpinnerAdapt());

                        EVENT_DIALOG_STATE = (EVENT_DIALOG_STATE + 1);
                        break;
                    case 3:
                        // Load to Date
                        item_Txt = item_Spin.toString();

                        if (item_Txt.equals(DEFAULT_LOAD_TEXT)) {
                            item_Txt = "";
                        }

                        event_Item.setEventLoad_Str(item_Txt);

                        subView_RelLay.removeView(item_Spin);
                        subView_RelLay.addView(item_Date);

                        EVENT_DIALOG_STATE = (EVENT_DIALOG_STATE + 1);
                        break;
                    case 4:
                        // Date to Weather
                        item_Txt = item_Date.toString();

                        event_Item.setEventDate_Str(item_Txt);

                        subView_RelLay.removeView(item_Date);
                        subView_RelLay.addView(item_Edt);
                        item_Edt.setText(event_Item.getEventWeather_Str());

                        EVENT_DIALOG_STATE = (EVENT_DIALOG_STATE + 1);
                        break;
                    case 5:
                        // Weather to Notes
                        item_Txt = item_Edt.getText().toString();

                        event_Item.setEventWeather_Str(item_Txt);
                        item_Edt.setText(event_Item.getEventNotes_Str());

                        EVENT_DIALOG_STATE = (EVENT_DIALOG_STATE + 1);
                        break;
                    case 6:
                        // Notes to Saving event and closing dialog
                        item_Txt = item_Edt.getText().toString();

                        event_Item.setEventNotes_Str(item_Txt);

                        saveEventToDB(event_Item);

                        alertDialog.dismiss();

                        Toast.makeText(PostEventActivity.this, "Event saved!",
                                Toast.LENGTH_LONG).show();

                        // Refresh event list view
                        refreshEventListView();

                        // Reset state counter
                        EVENT_DIALOG_STATE = 0;
                        break;
                }

                alertDialog.setMessage(EVENT_DIALOG_MSG.get(EVENT_DIALOG_STATE));
                item_Edt.setHint(EVENT_EDT_HINT.get(EVENT_DIALOG_STATE));
                pos_Btn.setText(EVENT_POS_BTN_TXT.get(EVENT_DIALOG_STATE));
                neu_Btn.setText(EVENT_NEU_BTN_TXT.get(EVENT_DIALOG_STATE));
            }
        });

        neu_Btn.setTextColor(NEUTRAL_BTN_COLOR);
        neu_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform Action on Neutral button

                switch (EVENT_DIALOG_STATE){
                    case 0:
                        // Name to cancel and close dialog
                        alertDialog.dismiss();
                        break;
                    case 1:
                        // Location to Name
                        item_Edt.setText(event_Item.getEventName_Str());
                        EVENT_DIALOG_STATE = (EVENT_DIALOG_STATE - 1);
                        break;
                    case 2:
                        // Gun to Location
                        item_Edt.setText(event_Item.getEventLocation_Str());

                        subView_RelLay.removeView(item_Spin);
                        subView_RelLay.addView(item_Edt);

                        EVENT_DIALOG_STATE = (EVENT_DIALOG_STATE - 1);
                        break;
                    case 3:
                        // Load to Gun
                        item_Spin.setAdapter(initializeGunSpinnerAdapt());
                        EVENT_DIALOG_STATE = (EVENT_DIALOG_STATE - 1);
                        break;
                    case 4:
                        // Date to Load
                        item_Spin.setAdapter(initializeLoadSpinnerAdapt());

                        subView_RelLay.removeView(item_Date);
                        subView_RelLay.addView(item_Spin);

                        EVENT_DIALOG_STATE = (EVENT_DIALOG_STATE - 1);
                        break;
                    case 5:
                        // Weather to Date
                        subView_RelLay.removeView(item_Edt);
                        subView_RelLay.addView(item_Date);

                        EVENT_DIALOG_STATE = (EVENT_DIALOG_STATE - 1);
                        break;
                    case 6:
                        // Notes to Weather
                        item_Edt.setText(event_Item.getEventWeather_Str());
                        EVENT_DIALOG_STATE = (EVENT_DIALOG_STATE - 1);
                        break;
                }

                alertDialog.setMessage(EVENT_DIALOG_MSG.get(EVENT_DIALOG_STATE));
                item_Edt.setHint(EVENT_EDT_HINT.get(EVENT_DIALOG_STATE));
                pos_Btn.setText(EVENT_POS_BTN_TXT.get(EVENT_DIALOG_STATE));
                neu_Btn.setText(EVENT_NEU_BTN_TXT.get(EVENT_DIALOG_STATE));
            }

        });
    }

    private ArrayAdapter<String> initializeGunSpinnerAdapt() {
        /*******************************************************************************************
         * Function: initializeGunSpinnerAdapt
         *
         * Purpose: Function initializes gun spinner for adding a new event
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        ArrayAdapter<String> tempGun_Adapt;
        ArrayList<GunClass> tempGun_List;
        ArrayList<String> tempGunStr_List = new ArrayList<>();

        tempGun_List = db.getAllGunFromDB(mCurrentUserEmail_Str);

        for (int i = 0; i < tempGun_List.size(); i++) {
            GunClass tempGun = tempGun_List.get(i);
            String gunItem_Str = tempGun.getGunNickname_Str();

            tempGunStr_List.add(gunItem_Str);
        }

        if (tempGunStr_List.isEmpty()) {
            tempGunStr_List.add(DEFAULT_GUN_TEXT);
        }

        tempGun_Adapt = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tempGunStr_List);

        return tempGun_Adapt;
    }

    private ArrayAdapter<String> initializeLoadSpinnerAdapt() {
        /*******************************************************************************************
         * Function: initializeLoadSpinnerAdapt
         *
         * Purpose: Function initializes load spinner for adding a new event
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        ArrayAdapter<String> tempLoad_Adapt;
        ArrayList<LoadClass> tempLoad_List;
        ArrayList<String> tempLoadStr_List = new ArrayList<>();

        tempLoad_List = db.getAllLoadFromDB(mCurrentUserEmail_Str);

        for (int i = 0; i < tempLoad_List.size(); i++) {
            LoadClass tempLoad = tempLoad_List.get(i);
            String loadItem_Str = tempLoad.getLoadNickname_Str();

            tempLoadStr_List.add(loadItem_Str);
        }

        if (tempLoadStr_List.isEmpty()) {
            tempLoadStr_List.add(DEFAULT_LOAD_TEXT);
        }

        tempLoad_Adapt = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tempLoadStr_List);

        return tempLoad_Adapt;
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

        ShotClass temp_Shot = new ShotClass(email_Str, eventName_Str, Integer.toString(totalShot_Int),
                Integer.toString(totalHit_Int), notes_Str);

        db.insertShotInDB(temp_Shot);
    }

    private void saveEventToDB(EventClass e){
        /*******************************************************************************************
         * Function: saveEventToDB
         *
         * Purpose: Function takes info necessary to save event to database
         *
         * Parameters: e (IN) - event object to save
         *
         * Returns: None
         *
         ******************************************************************************************/

        db.insertEventInDB(e);
    }

    //************************************** Other Functions ***************************************
    private Boolean isDefaultItemText(String Event) {
        return (Event.equals(DEFAULT_EVENT_TEXT));
    }


}
