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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class EventHistoryActivity extends AppCompatActivity {

    //********************************** Variables and Constants ***********************************
    // General Constants
    private final String TAG = "JRW";
    private String ACTIVITY_TITLE;
    private String CURRENT_USER_KEY;
    private String DEFAULT_SHOT_TEXT;
    private String DEFAULT_EVENT_TEXT;
    private String DEFAULT_GUN_TEXT;
    private String DEFAULT_LOAD_TEXT;

    // General Variables
    private String mCurrentUserEmail_Str = "********";
    private DBHandler db;
    private boolean isPortrait = true;
    // Shot
    private ArrayList<ShotClass> mUserShot_List;
    private ArrayAdapter<String> mUserShotList_Adapt;
    private int SHOT_DIALOG_STATE = 0;
    private ArrayList<String> SHOT_DIALOG_MSG, SHOT_POS_BTN_TXT, SHOT_NEU_BTN_TXT, SHOT_EDT_HINT;

    // Event
    private ArrayList<EventClass> mUserEvent_List;
    private ArrayAdapter<String> mCurrentEventList_Adapt;
    private int EVENT_DIALOG_STATE = 0;
    private ArrayList<String> EVENT_DIALOG_MSG, EVENT_POS_BTN_TXT, EVENT_NEU_BTN_TXT, EVENT_EDT_HINT;

    // UI References
    private ListView mShotList_View;
    private ListView mEventList_View;

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

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            setContentView(R.layout.activity_event_history_portrait);
            isPortrait = true;
        } else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_event_history_landscape);
            isPortrait = false;
        }

        if (savedInstanceState != null) {

        } else {
            mCurrentUserEmail_Str = getIntent().getStringExtra(CURRENT_USER_KEY);
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
            setContentView(R.layout.activity_event_history_portrait);
            isPortrait = true;
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_event_history_landscape);
            isPortrait = false;
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

        ACTIVITY_TITLE = getString(R.string.event_history_activity_title);
        CURRENT_USER_KEY = getString(R.string.current_user_key);
        DEFAULT_SHOT_TEXT = getString(R.string.default_shot_text);
        DEFAULT_EVENT_TEXT = getString(R.string.default_event_text);
        DEFAULT_GUN_TEXT = getString(R.string.post_event_default_gun_text);
        DEFAULT_LOAD_TEXT = getString(R.string.post_event_default_load_text);
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
        db = new DBHandler(this);

        // Initializing list view
        mShotList_View = findViewById(R.id.eventHistoryScore_List);
        mEventList_View = findViewById(R.id.eventHistoryEvent_List);

        setListViewLayoutParams();

        initializeShotListView();
        initializeEventListView();

        // Initializing buttons
        FloatingActionButton mAddShot = findViewById(R.id.eventHistoryAddScore_Btn);
        FloatingActionButton mAddEvent = findViewById(R.id.eventHistoryAddEvent_Btn);

        mAddShot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to new event activity
                Intent newEvent_Activity = new Intent(EventHistoryActivity.this, NewEventActivity.class);
                newEvent_Activity.putExtra(CURRENT_USER_KEY, mCurrentUserEmail_Str);
                startActivity(newEvent_Activity);
            }
        });

        mAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Display add event dialog box for user input, -1 = adding a new event
                EventClass temp_Event = new EventClass();
                temp_Event.setEventID_Int(-1);
                editEventDialog(temp_Event);
            }
        });

        // Setting title of activity
        setTitle(ACTIVITY_TITLE);
    }

    //************************************* Listview Functions *************************************
    // Shot List
    private ArrayList<String> refreshShotList() {
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

        mUserShot_List = db.getAllShotFromDB(mCurrentUserEmail_Str);
        ArrayList<String> currentShotStr_List =  new ArrayList<>();

        for (int i = 0; i < mUserShot_List.size(); i++) {
            ShotClass tempShot = mUserShot_List.get(i);
            String gunListItem_Str = Integer.toString(tempShot.getShotID_Int());

            currentShotStr_List.add(gunListItem_Str);
        }

        if (currentShotStr_List.isEmpty()) {
            Log.d(TAG, "Shot list empty.");
            currentShotStr_List.add(DEFAULT_SHOT_TEXT);
        }

        Log.d(TAG, currentShotStr_List.toString());

        return currentShotStr_List;
    }

    private void refreshShotListView() {
        /*******************************************************************************************
         * Function: refreshShotListView
         *
         * Purpose: Function refreshes the shot list view with current shot data from database
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        mUserShotList_Adapt.clear();
        mUserShotList_Adapt.addAll(refreshShotList());
        mUserShotList_Adapt.notifyDataSetChanged();
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
            ArrayList<String> currentShotStr_List =  refreshShotList();

            mUserShotList_Adapt = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, currentShotStr_List);

            mShotList_View.setAdapter(mUserShotList_Adapt);
            mShotList_View.setLongClickable(true);

            mShotList_View.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    // TODO: for editing shot list item
                    String shot_Str = (String) adapterView.getItemAtPosition(i);
                    Log.d("JRW", "OnItemClick for shot is here: " + shot_Str);

                    if (!isDefaultItemText(shot_Str)) {
                        ShotClass temp_Shot = db.getShotInDB(Integer.parseInt(shot_Str));
                        editShotDialog(temp_Shot);
                    }
                }
            });

            mShotList_View.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    // TODO: for deleting gun list item
                    String shot_Str = (String) adapterView.getItemAtPosition(i);
                    Log.d("JRW", "OnItemLongClick for shot is here: " + shot_Str);

                    if (!isDefaultItemText(shot_Str)) {
                        removeShotItemDialog(Integer.parseInt(shot_Str));
                    }
                    return true;
                }
            });

        } catch (Exception e){
            Log.d("JRW", "no shots in db for this user: " + e.toString());
        }

    }

    // Event List
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
            String eventListItem_Str = tempEvent.getEventName_Str();

            currentEventStr_List.add(eventListItem_Str);
        }

        if (currentEventStr_List.isEmpty()) {
            Log.d(TAG, "Event list empty.");
            currentEventStr_List.add(DEFAULT_EVENT_TEXT);
        }

        Log.d(TAG, currentEventStr_List.toString());

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
            mShotList_View.setLongClickable(true);

            mEventList_View.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String event_Str = (String) adapterView.getItemAtPosition(i);
                    Log.d(TAG, "OnItemClick for event is here: " + event_Str + " for " + mCurrentUserEmail_Str);

                    if (!isDefaultItemText(event_Str)) {
                        // TODO: for editing shot list item
                        EventClass temp_Event = db.getEventInDB(db.getIDforEvent(mCurrentUserEmail_Str, event_Str));
                        editEventDialog(temp_Event);
                    }
                }
            });

            mEventList_View.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    // TODO: for deleting gun list item
                    String event_Str = (String) adapterView.getItemAtPosition(i);
                    Log.d("JRW", "OnItemLongClick for event is here: " + event_Str);

                    if (!isDefaultItemText(event_Str)) {
                        removeEventItemDialog(event_Str);
                    }
                    return true;
                }
            });

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

        double screenHeight_Dbl = getResources().getDisplayMetrics().heightPixels;
        double scaleFactor_Dbl = 1;

        LinearLayout.LayoutParams params;

        if (isPortrait) {
            scaleFactor_Dbl = 3.5;
        } else {
            scaleFactor_Dbl = 2.0;
        }

        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)(screenHeight_Dbl / scaleFactor_Dbl));

        mEventList_View.setLayoutParams(params);
        mShotList_View.setLayoutParams(params);
    }

    //************************************ Event Dialog Functions **********************************
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

                        if (event_Item.getEventID_Int() == -1) {
                            // Adding a new event
                            saveEventToDB(event_Item);
                        } else {
                            // Editing an existing event
                            db.updateEventInDB(event_Item, event_Item.getEventID_Int());
                        }

                        alertDialog.dismiss();

                        Toast.makeText(EventHistoryActivity.this, "Event saved!",
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

    private void removeEventItemDialog(String eventName_Str) {
        /*******************************************************************************************
         * Function: removeEventItemDialog
         *
         * Purpose: Function creates dialog and prompts user to remove a event item
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        final String DIALOG_TITLE = "Delete Event";
        final String DIALOG_MSG = "Are you sure you want to delete this event?";

        final boolean POSITIVE_BTN = true;  // Right
        final boolean NEUTRAL_BTN = false;   // Left
        final boolean NEGATIVE_BTN = true;  // Middle

        final String POSITIVE_BUTTON_TXT = "DELETE";
        final String NEUTRAL_BUTTON_TXT = "";
        final String NEGATIVE_BUTTON_TXT = "CANCEL";

        final int POSITIVE_BTN_COLOR = Color.BLUE;
        final int NEUTRAL_BTN_COLOR = Color.RED;
        final int NEGATIVE_BTN_COLOR = Color.RED;


        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        final String finalEventName_Str = eventName_Str;

        // Set Dialog Title
        alertDialog.setTitle(DIALOG_TITLE);

        // Set Dialog Message
        alertDialog.setMessage(DIALOG_MSG);

        // Set Buttons
        // Positive Button, Right
        if (POSITIVE_BTN) {
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, POSITIVE_BUTTON_TXT, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Processed by onClick below
                }
            });
        }

        // Neutral Button, Left
        if (NEUTRAL_BTN) {
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, NEUTRAL_BUTTON_TXT, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Processed by onClick below
                }
            });
        }

        // Negative Button, Middle
        if (NEGATIVE_BTN) {
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, NEGATIVE_BUTTON_TXT, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Processed by onClick below
                }
            });
        }


        new Dialog(getApplicationContext());
        alertDialog.show();

        // Set Buttons
        if (POSITIVE_BTN) {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(POSITIVE_BTN_COLOR);
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Perform Action on Positive button
                    db.deleteEventInDB(mCurrentUserEmail_Str, finalEventName_Str);

                    alertDialog.dismiss();

                    Toast.makeText(EventHistoryActivity.this, "Event deleted.",
                            Toast.LENGTH_LONG).show();

                    // Refresh event listview
                    refreshEventListView();
                }
            });
        }
        if (NEUTRAL_BTN) {
            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(NEUTRAL_BTN_COLOR);
            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Perform Action on Neutral button
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

    //*********************************** Shot Dialog Functions ************************************
    private void initializeShotDialogStrings() {
        SHOT_DIALOG_MSG = new ArrayList<String>(Arrays.asList(
                "Enter the score for this shoot.",
                "Choose an event for this shoot.",
                "Enter any notes for this shoot."));
        SHOT_POS_BTN_TXT = new ArrayList<String>(Arrays.asList(
                "NEXT",
                "NEXT",
                "SAVE"));
        SHOT_NEU_BTN_TXT = new ArrayList<String>(Arrays.asList(
                "CANCEL",
                "BACK",
                "BACK"));
        SHOT_EDT_HINT = new ArrayList<String>(Arrays.asList(
                "i.e. 25",
                "",
                "i.e. This was a good event!"));
    }

    private void editShotDialog(final ShotClass shot_Item) {
        /*******************************************************************************************
         * Function: editShotDialog
         *
         * Purpose: Function creates dialog and prompts user to add or edit an shot item
         *
         * Parameters: shot_Item (IN) - object to edit or add, if adding object, shotID = -1
         *
         * Returns: None
         *
         ******************************************************************************************/
        initializeShotDialogStrings();
        shot_Item.setShotEmail_Str(mCurrentUserEmail_Str);

        // Dialog Constants
        String DIALOG_TITLE = "Add New Shoot";;

        int POSITIVE_BTN_COLOR = Color.BLUE;
        int NEUTRAL_BTN_COLOR = Color.RED;

        // Pre-Dialog Processing
        if (shot_Item.getShotID_Int() != -1) {
            DIALOG_TITLE = "Edit Shoot";
        }

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        // Set Dialog Title
        alertDialog.setTitle(DIALOG_TITLE);

        // Set Dialog Message
        alertDialog.setMessage(SHOT_DIALOG_MSG.get(SHOT_DIALOG_STATE));

        // Set layout for gathering information
        final RelativeLayout subView_RelLay = new RelativeLayout(this);

        // Set views
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final EditText item_Edt = new EditText(this);
        item_Edt.setHint(SHOT_EDT_HINT.get(SHOT_DIALOG_STATE));
        item_Edt.setText(shot_Item.getShotHitNum_Str());
        item_Edt.setGravity(Gravity.START);
        item_Edt.setTextColor(Color.BLACK);
        item_Edt.setLayoutParams(params);
        subView_RelLay.addView(item_Edt);

        final Spinner item_Spin = new Spinner(this);
        item_Spin.setGravity(Gravity.START);
        item_Spin.setLayoutParams(params);

        // Add linear layout to alert dialog
        alertDialog.setView(subView_RelLay);

        // Set Buttons
        // Positive Button, Right
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, SHOT_POS_BTN_TXT.get(SHOT_DIALOG_STATE), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Processed by onClick below
            }
        });

        // Neutral Button, Left
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, SHOT_NEU_BTN_TXT.get(SHOT_DIALOG_STATE), new DialogInterface.OnClickListener() {
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
                switch (SHOT_DIALOG_STATE){
                    case 0:
                        // Score to Event
                        String item_Txt = item_Edt.getText().toString();

                        boolean isShotScoreEmpty = item_Txt.equals("");

                        if (isShotScoreEmpty) {
                            // Check if the shoot score is empty
                            item_Edt.setError(getString(R.string.error_field_required));
                            item_Edt.requestFocus();
                        } else {
                            shot_Item.setShotHitNum_Str(item_Txt);

                            subView_RelLay.removeView(item_Edt);
                            item_Spin.setAdapter(initializeEventSpinnerAdapt());
                            subView_RelLay.addView(item_Spin);

                            SHOT_DIALOG_STATE = (SHOT_DIALOG_STATE + 1);
                        }

                        break;
                    case 1:
                        // Event to Notes
                        item_Txt = item_Spin.toString();

                        shot_Item.setShotEventName_Str(item_Txt);

                        subView_RelLay.removeView(item_Spin);
                        item_Edt.setText(shot_Item.getShotNotes_Str());
                        subView_RelLay.addView(item_Edt);

                        SHOT_DIALOG_STATE = (SHOT_DIALOG_STATE + 1);
                        break;
                    case 2:
                        // Notes to saving shoot and closing dialog
                        item_Txt = item_Edt.getText().toString();

                        shot_Item.setShotNotes_Str(item_Txt);

                        if (shot_Item.getShotID_Int() == -1) {
                            // Adding a new shoot
                            db.insertShotInDB(shot_Item);
                        } else {
                            // Editing an existing shoot
                            db.updateShotInDB(shot_Item);
                        }

                        alertDialog.dismiss();

                        Toast.makeText(EventHistoryActivity.this, "Shoot saved!",
                                Toast.LENGTH_LONG).show();

                        // Refresh shot list view
                        refreshShotList();

                        // Reset state counter
                        SHOT_DIALOG_STATE = 0;
                        break;
                }

                alertDialog.setMessage(SHOT_DIALOG_MSG.get(SHOT_DIALOG_STATE));
                item_Edt.setHint(SHOT_EDT_HINT.get(SHOT_DIALOG_STATE));
                pos_Btn.setText(SHOT_POS_BTN_TXT.get(SHOT_DIALOG_STATE));
                neu_Btn.setText(SHOT_NEU_BTN_TXT.get(SHOT_DIALOG_STATE));
            }
        });

        neu_Btn.setTextColor(NEUTRAL_BTN_COLOR);
        neu_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform Action on Neutral button

                switch (SHOT_DIALOG_STATE){
                    case 0:
                        // Score to cancel and close dialog
                        alertDialog.dismiss();
                        break;
                    case 1:
                        // Event to score
                        subView_RelLay.removeView(item_Spin);
                        subView_RelLay.addView(item_Edt);
                        item_Edt.setText(shot_Item.getShotHitNum_Str());

                        SHOT_DIALOG_STATE = (SHOT_DIALOG_STATE - 1);
                        break;
                    case 2:
                        // Notes to event
                        subView_RelLay.removeView(item_Edt);
                        subView_RelLay.addView(item_Spin);

                        SHOT_DIALOG_STATE = (SHOT_DIALOG_STATE - 1);
                        break;
                }

                alertDialog.setMessage(SHOT_DIALOG_MSG.get(SHOT_DIALOG_STATE));
                item_Edt.setHint(SHOT_EDT_HINT.get(SHOT_DIALOG_STATE));
                pos_Btn.setText(SHOT_POS_BTN_TXT.get(SHOT_DIALOG_STATE));
                neu_Btn.setText(SHOT_NEU_BTN_TXT.get(SHOT_DIALOG_STATE));
            }

        });
    }

    private ArrayAdapter<String> initializeEventSpinnerAdapt() {
        /*******************************************************************************************
         * Function: initializeEventSpinnerAdapt
         *
         * Purpose: Function initializes event spinner for adding a editing shoot
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        ArrayAdapter<String> tempEvent_Adapt;
        ArrayList<EventClass> tempEvent_List;
        ArrayList<String> tempEventStr_List = new ArrayList<>();

        tempEvent_List = db.getAllEventFromDB(mCurrentUserEmail_Str);

        for (int i = 0; i < tempEvent_List.size(); i++) {
            EventClass temp_Event = tempEvent_List.get(i);
            String eventItem_Str = temp_Event.getEventName_Str();

            tempEventStr_List.add(eventItem_Str);
        }

        if (tempEventStr_List.isEmpty()) {
            tempEventStr_List.add(DEFAULT_EVENT_TEXT);
        }

        tempEvent_Adapt = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tempEventStr_List);

        return tempEvent_Adapt;
    }

    private void removeShotItemDialog(final int shotID_Int) {
        /*******************************************************************************************
         * Function: removeShotItemDialog
         *
         * Purpose: Function creates dialog and prompts user to remove a shoot item
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        final String DIALOG_TITLE = "Delete Shoot";
        final String DIALOG_MSG = "Are you sure you want to delete this shoot?";

        final boolean POSITIVE_BTN = true;  // Right
        final boolean NEUTRAL_BTN = false;   // Left
        final boolean NEGATIVE_BTN = true;  // Middle

        final String POSITIVE_BUTTON_TXT = "DELETE";
        final String NEUTRAL_BUTTON_TXT = "";
        final String NEGATIVE_BUTTON_TXT = "CANCEL";

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
                    // Processed by onClick below
                }
            });
        }

        // Neutral Button, Left
        if (NEUTRAL_BTN) {
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, NEUTRAL_BUTTON_TXT, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Processed by onClick below
                }
            });
        }

        // Negative Button, Middle
        if (NEGATIVE_BTN) {
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, NEGATIVE_BUTTON_TXT, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Processed by onClick below
                }
            });
        }


        new Dialog(getApplicationContext());
        alertDialog.show();

        // Set Buttons
        if (POSITIVE_BTN) {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(POSITIVE_BTN_COLOR);
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Perform Action on Positive button
                    db.deleteShotInDB(mCurrentUserEmail_Str, shotID_Int);

                    alertDialog.dismiss();

                    Toast.makeText(EventHistoryActivity.this, "Shoot deleted.",
                            Toast.LENGTH_LONG).show();

                    // Refresh shoot list view
                    refreshShotListView();
                }
            });
        }
        if (NEUTRAL_BTN) {
            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(NEUTRAL_BTN_COLOR);
            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Perform Action on Neutral button
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

    //************************************ Database Functions **************************************
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

    //*************************************** Other Functions **************************************
    private boolean isDefaultItemText(String item_Str) {
        return (item_Str.equals(DEFAULT_EVENT_TEXT) || item_Str.equals(DEFAULT_SHOT_TEXT));
    }
    
}
