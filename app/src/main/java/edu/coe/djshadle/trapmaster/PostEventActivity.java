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
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class PostEventActivity extends AppCompatActivity {
    //********************************** Variables and Constants ***********************************
    // General Constants
    private final String TAG = "JRW";
    private final String DEFAULT_EVENT_TEXT = "Click to add a new event!";
    private final String DEFAULT_GUN_TEXT = "Add guns in the armory!";
    private final String DEFAULT_LOAD_TEXT = "Add loads in the armory!";
    private final int TOTAL_NUM_SHOTS = 25;

    // General Variables
    private boolean isPortait = true;
    private String mCurrentUserEmail_Str = "tempEmail";
    private ArrayList<EventClass> mUserEvent_List;
    private ArrayAdapter<String> mCurrentEventList_Adapt;
    DBHandler db;
    // shot variables
    private String mShotNotes_Str = "";
    private int mShotScore_Int = 0;
    //event variables
    private String mEventName_Str = "";
    private String mEventLocation_Str = "";
    private String mEventGun_Str = "";
    private String mEventLoad_Str = "";
    private String mEventDate_Str = "";
    private String mEventWeather_Str = "";
    private String mEventNotes_Str = "";

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

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_post_event_portrait);
            isPortait = true;
        }
        else {
            setContentView(R.layout.activity_post_event_landscape);
            isPortait = false;
        }

        if (savedInstanceState != null) {

        } else {
            mCurrentUserEmail_Str = getIntent().getStringExtra(getString(R.string.current_user_email));
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
            isPortait = true;
        }
        else {
            setContentView(R.layout.activity_post_event_landscape);
            isPortait = false;
        }

        initializeViews();

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
            setTitle("Post Event");
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
                addNewEventDialog();
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
                homeActivity_Intent.putExtra(getString(R.string.current_user_email), mCurrentUserEmail_Str);
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
                homeActivity_Intent.putExtra(getString(R.string.current_user_email), mCurrentUserEmail_Str);
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

        if (isPortait) {
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

    private Boolean isDefaultItemText(String Event) {
        return (Event.equals(DEFAULT_EVENT_TEXT));
    }

    private void addNewEventDialog() {
        /*******************************************************************************************
         * Function: addNewEventDialog
         *
         * Purpose: Function creates dialog and prompts user to add new event item to db
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        // Set variables for this function
        final String NEUTRAL_BTN_STR = "SAVE";
        final String NEGATVE_BTN_STR = "CANCEL";
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        // Set Dialog Title
        alertDialog.setTitle("Add New Event");

        // Set all views for gathering information
        LinearLayout dialogView_LnrLay = new LinearLayout(this);
        dialogView_LnrLay.setOrientation(LinearLayout.VERTICAL);

        // Set Event Name EditText
        final EditText eventName_Edt = new EditText(this);
        eventName_Edt.setHint("Event Name");
        eventName_Edt.setGravity(Gravity.START);
        eventName_Edt.setTextColor(Color.BLACK);
        dialogView_LnrLay.addView(eventName_Edt);

        // Set Event Location EditText
        final EditText eventLocation_Edt = new EditText(this);
        eventLocation_Edt.setHint("Event Location");
        eventLocation_Edt.setGravity(Gravity.START);
        eventLocation_Edt.setTextColor(Color.BLACK);
        dialogView_LnrLay.addView(eventLocation_Edt);

        // Set Gun Spinner
        final Spinner gun_Spin = new Spinner(this);
        gun_Spin.setGravity(Gravity.START);
        gun_Spin.setAdapter(initializeGunSpinnerAdapt());
        dialogView_LnrLay.addView(gun_Spin);

        // Set Load Spinner
        final Spinner load_Spin = new Spinner(this);
        load_Spin.setGravity(Gravity.START);
        load_Spin.setAdapter(initializeLoadSpinnerAdapt());
        dialogView_LnrLay.addView(load_Spin);

        // Set Date Picker
        //final DatePicker date_Pick = new DatePicker(this);
        //date_Pick.init(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, null);
        //date_Pick.
        //dialogView_LnrLay.addView(date_Pick);

        // Set Event Weather EditText
        final EditText eventWeather_Edt = new EditText(this);
        eventWeather_Edt.setHint("Give a weather description");
        eventWeather_Edt.setGravity(Gravity.START);
        eventWeather_Edt.setTextColor(Color.BLACK);
        dialogView_LnrLay.addView(eventWeather_Edt);

        // Set Event Notes EditText
        final EditText eventNotes_Edt = new EditText(this);
        eventNotes_Edt.setHint("Event Notes");
        eventNotes_Edt.setGravity(Gravity.START);
        eventNotes_Edt.setTextColor(Color.BLACK);
        dialogView_LnrLay.addView(eventNotes_Edt);


        // Add linear layout to alert dialog
        alertDialog.setView(dialogView_LnrLay);

        // Set Buttons
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,NEUTRAL_BTN_STR, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Overwritten by on click listener below
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,NEGATVE_BTN_STR, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Overwritten by on click listener below
            }
        });

        new Dialog(getApplicationContext());
        alertDialog.show();

        // Set Properties for NEUTRAL Button
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
                DateFormat dateFormat = SimpleDateFormat.getDateInstance();

                mEventName_Str = eventName_Edt.getText().toString();
                mEventLocation_Str = eventLocation_Edt.getText().toString();
                mEventGun_Str = gun_Spin.getSelectedItem().toString();
                mEventLoad_Str = load_Spin.getSelectedItem().toString();
                mEventDate_Str = dateFormat.format(Calendar.getInstance().getTime());
                mEventWeather_Str = eventWeather_Edt.getText().toString();
                mEventNotes_Str = eventNotes_Edt.getText().toString();

                // do validation checking on this info
                // save to database if good, set errors if not good
                // After this is dismissed, set the listview to have this selected item

                if (mEventName_Str.equals("")) {
                    eventName_Edt.setError(getString(R.string.error_field_required));
                    eventName_Edt.requestFocus();
                } else if (db.isEventNameInDB(mCurrentUserEmail_Str, mEventName_Str, -1)){
                    eventName_Edt.setError(getString(R.string.error_event_already_exists));
                    eventName_Edt.requestFocus();
                } else {
                    if (mEventGun_Str.equals(DEFAULT_GUN_TEXT)) {
                        // Replace default with empty string
                        mEventGun_Str = "";
                    }
                    if (mEventLoad_Str.equals(DEFAULT_LOAD_TEXT)) {
                        // Replace default with empty string
                        mEventLoad_Str = "";
                    }

                    saveEventToDB(mCurrentUserEmail_Str, mEventName_Str, mEventLocation_Str,
                            mEventGun_Str, mEventLoad_Str, mEventDate_Str, mEventWeather_Str,
                            mEventNotes_Str);

                    alertDialog.dismiss();

                    Toast.makeText(PostEventActivity.this, "Event saved!",
                            Toast.LENGTH_LONG).show();

                    refreshEventListView();
                }

            }
        });

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

    private void saveEventToDB(String email_Str, String eventName_Str, String location_Str,
                               String gunName_Str, String loadName_Str, String date_Str,
                               String weather_Str, String notes_Str){
        /*******************************************************************************************
         * Function: saveEventToDB
         *
         * Purpose: Function takes info necessary to save event to database
         *
         * Parameters: email_Str (IN) - email of the user
         *             eventName_Str (IN) - name of the event
         *             location_Str (IN) - location of event
         *             gunName_Str (IN) - nickname of gun used
         *             loadName_Str (IN) - nickname of load used
         *             date_Str (IN) - date in the form of a string
         *             weather_Str (IN) - weather in the form of a string
         *             notes_Str (IN) - notes on the event
         *
         * Returns: None
         *
         ******************************************************************************************/

        // Functionality add at a later version
        String team_Str = "";

        EventClass temp_Event = new EventClass(email_Str, team_Str, eventName_Str, location_Str, gunName_Str,
                loadName_Str, date_Str, weather_Str, notes_Str);

        db.insertEventInDB(temp_Event);
    }


}
