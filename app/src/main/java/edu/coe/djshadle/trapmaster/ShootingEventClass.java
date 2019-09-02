/***************************************************************************************************
 * FILENAME : ShootingEventClass.java
 *
 * AUTHOR : Dalton Shadle
 *
 * DESCRIPTION : Holds variables and functions for EventClass object
 *
 * NOTES : N/A
 *
 * Copyright Dalton Shadle 2019.  All rights reserved.
 *
 **************************************************************************************************/

package edu.coe.djshadle.trapmaster;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ShootingEventClass {
    //**************************************** Constants *******************************************
    // Key Constants
    private String CURRENT_USER_KEY;
    private String NUM_ROUNDS_KEY;
    private String NUM_SHOOTER_KEY;
    private String SHOOTER_LIST_KEY;
    private String TAG = "JRW";

    //************************************* Private Variables **************************************
    // Object Variables
    private int shootingEventProfileID_Int;
    private Context shootingEvent_Context;
    private ArrayList<ShooterClass> shooter_Array;

    // Dialog Variables
    private CheckboxListArrayAdapter shooterArray_Adapt;
    private ArrayList<String> DIALOG_MSG_TXT;
    private ArrayList<String> POS_BTN_TXT;
    private ArrayList<String> NEU_BTN_TXT;
    private ArrayList<String> NEG_BTN_TXT;
    private int NEW_EVENT_DIALOG_STATE = 0;

    //************************************* Public Functions ***************************************
    // Constructors
    public ShootingEventClass(int profileID_Int, Context context) {
        /*******************************************************************************************
         * Function: ShootingEventClass
         *
         * Purpose: Constructor for this class
         *
         * Parameters: profileID_Int (IN) - ID of profile in database
         *             context (IN) - context from which this object was started from
         *
         * Returns: None
         *
         ******************************************************************************************/

        this.shootingEventProfileID_Int = profileID_Int;
        this.shootingEvent_Context = context;

        // Initialize string constants
        initializeConstants(context);
    }

    //******************************** New Shooting Event Functions ********************************
    private void initializeConstants(Context context) {
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

        CURRENT_USER_KEY = context.getString(R.string.current_user_key);
        NUM_ROUNDS_KEY = context.getString(R.string.num_rounds_key);
        NUM_SHOOTER_KEY = context.getString(R.string.num_shooter_key);
        SHOOTER_LIST_KEY = context.getString(R.string.shooter_list_key);
    }

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

        DIALOG_MSG_TXT = new ArrayList<String>(Arrays.asList(
                "How many rounds for this event?",
                "Select shooters for this event. Click the + to add more!"));
        POS_BTN_TXT = new ArrayList<String>(Arrays.asList(
                "NEXT",
                "CONTINUE"));
        NEU_BTN_TXT = new ArrayList<String>(Arrays.asList(
                "CANCEL",
                "BACK"));
        NEG_BTN_TXT = new ArrayList<String>(Arrays.asList(
                " ",
                "+"));
    }

    private CheckboxListArrayAdapter initializeShooterArrayAdapt() {
        /*******************************************************************************************
         * Function: initializeShooterArrayAdapt
         *
         * Purpose: Function initializes shooter listview adapt for starting new event
         *
         * Parameters: none
         *
         * Returns: tempShooter_Adapt - Array adapter initialized to shooters from DB
         *
         ******************************************************************************************/

        // Initialize function variables
        CheckboxListArrayAdapter tempShooter_Adapt;
        ArrayList<String> tempShooterStr_Array = new ArrayList<>();
        GlobalApplicationContext currentContext = new GlobalApplicationContext();
        final DBHandler db = new DBHandler(currentContext.getContext());

        // Pull shooters from db
        shooter_Array = db.getAllShooterFromDB(this.shootingEventProfileID_Int);

        // Iterate over all shooters and get names
        for (int i = 0; i < shooter_Array.size(); i++) {
            tempShooterStr_Array.add(shooter_Array.get(i).getShooterName_Str());
        }

        // Set array adapter
        tempShooter_Adapt = new CheckboxListArrayAdapter(currentContext.getContext(), tempShooterStr_Array);

        return tempShooter_Adapt;
    }

    public void newEventDialog(final Context context){
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
        XmlResourceParser parser = context.getResources().getLayout(R.layout.view_horizontal_number_picker);
        AttributeSet attributeSet = Xml.asAttributeSet(parser);

        // Round number picker
        final com.travijuu.numberpicker.library.NumberPicker round_NumPick =
                new com.travijuu.numberpicker.library.NumberPicker(context, attributeSet);
        round_NumPick.setMax(4);
        round_NumPick.setMin(1);
        round_NumPick.setUnit(1);
        round_NumPick.setGravity(Gravity.CENTER);
        subView_LnrLay.addView(round_NumPick);

        // Shooter name listview
        final ListView shooter_ListView = new ListView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 350);
        shooter_ListView.setLayoutParams(params);

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

                switch (NEW_EVENT_DIALOG_STATE) {
                    case 0:
                        // Round number picker to shooter number picker
                        subView_LnrLay.removeView(round_NumPick);
                        subView_LnrLay.addView(shooter_ListView);
                        shooterArray_Adapt = initializeShooterArrayAdapt();
                        shooter_ListView.setAdapter(shooterArray_Adapt);

                        NEW_EVENT_DIALOG_STATE = (NEW_EVENT_DIALOG_STATE + 1);
                        break;
                    case 1:
                        // Picking shooters to starting new event
                        ArrayList<Integer> checkedShooters_Array = shooterArray_Adapt.getCheckedItems();

                        if (checkedShooters_Array.isEmpty()) {
                            // Throw error that at least one needs to be checked
                            Toast.makeText(context, "Too few shooters selected. Select at least 1 shooter.", Toast.LENGTH_LONG).show();
                        } else if (checkedShooters_Array.size() > 5) {
                            // Throw error that there can only be 5 shooters
                            Toast.makeText(context, "Too many shooters selected. Select up to 5 shooters.", Toast.LENGTH_LONG).show();
                        } else {
                            // Get names of checked shooters
                            ArrayList<String> shooterNames_Array = new ArrayList<>();

                            for (Integer item : checkedShooters_Array) {
                                shooterNames_Array.add(shooter_Array.get(item).getShooterName_Str());
                            }

                            // Put all extras into new intent and start new event
                            Intent newShoot_Intent = new Intent(context, NewShootingEventActivity.class);
                            newShoot_Intent.putStringArrayListExtra(SHOOTER_LIST_KEY, shooterNames_Array);
                            newShoot_Intent.putExtra(NUM_ROUNDS_KEY, numRound_Int);
                            newShoot_Intent.putExtra(NUM_SHOOTER_KEY, shooterNames_Array.size());
                            newShoot_Intent.putExtra(CURRENT_USER_KEY, shootingEventProfileID_Int);
                            context.startActivity(newShoot_Intent);

                            // Dismiss dialog
                            alertDialog.dismiss();

                            // Reset state
                            NEW_EVENT_DIALOG_STATE = 0;
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
                        // Shooter number picker to add shooters listview
                        subView_LnrLay.removeView(shooter_ListView);
                        subView_LnrLay.addView(round_NumPick);

                        NEW_EVENT_DIALOG_STATE = (NEW_EVENT_DIALOG_STATE - 1);
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
                        shooter_ListView.setAdapter(initializeShooterArrayAdapt());
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
                    if (db.isShooterInDB(shootingEventProfileID_Int, shooterNameEdt_Str, -1)) {
                        // Shooter name is already in the database
                        item_Edt.setError(context.getString(R.string.error_shooter_name_already_exits));
                        item_Edt.requestFocus();
                    } else {
                        // Shooter name is not in database, proceed
                        temp_Shooter.setShooterName_Str(shooterNameEdt_Str);
                        temp_Shooter.setShooterProfileID_Int(shootingEventProfileID_Int);

                        // Add shooter to database
                        db.insertShooterInDB(temp_Shooter);

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