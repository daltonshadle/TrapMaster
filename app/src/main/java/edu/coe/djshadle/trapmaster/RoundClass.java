/***************************************************************************************************
 * FILENAME : RoundClass.java
 *
 * AUTHOR : Dalton Shadle
 *
 * DESCRIPTION : Holds variables and functions for RoundClass object
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
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class RoundClass implements Comparable <RoundClass>, java.io.Serializable {
    //************************************* Private Variables **************************************
    // Object variables
    private int roundID_Int;
    private int roundShooterID_Int;
    private int roundGunID_Int;
    private int roundLoadID_Int;
    private int roundRound_Int;
    private int roundScore_Int;
    private ArrayList<Integer> roundHitMiss_Array;
    private String roundNotes_Str;

    // Dialog variables
    private final String TAG = "JRW";
    private int SHOT_DIALOG_STATE = 0;
    private ArrayList<String> SHOT_DIALOG_MSG, SHOT_POS_BTN_TXT, SHOT_NEU_BTN_TXT, SHOT_EDT_HINT;

    //************************************* Public Functions ***************************************
    // Constructors
    public RoundClass() {
        /*******************************************************************************************
         * Function: RoundClass
         *
         * Purpose: Constructor for this class, no parameters so all values are initialized to temp
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        this.roundID_Int = -1;
        this.roundShooterID_Int = -1;
        this.roundGunID_Int = -1;
        this.roundLoadID_Int = -1;
        this.roundRound_Int = -1;
        this.roundScore_Int = -1;
        this.roundHitMiss_Array = new ArrayList<>();
        this.roundNotes_Str = "";
    }

    public RoundClass(int roundID_Int, int roundShooterID_Int, int roundGunID_Int,
                      int roundLoadID_Int, int roundRound_Int, int roundScore_Int,
                      ArrayList<Integer> roundHitMiss_Array, String roundNotes_Str) {
        /*******************************************************************************************
         * Function: RoundClass
         *
         * Purpose: Constructor for this class with parameters
         *
         * Parameters: roundID_Int (IN) - ID tagged with this round
         *             roundShooterID_Int (IN) - shooter id tagged with this round
         *             roundGunID_Int (IN) - gun id of round
         *             roundLoadID_Int (IN) - load id of rounds
         *             roundRound_Int (IN) - round number of round
         *             roundScore_Int (IN) - score of round
         *             roundHitMiss_Array (IN) - array of hit and miss states for round
         *             roundNotes_Str (IN) - notes tagged with the round
         *
         * Returns: None
         *
         ******************************************************************************************/

        this.roundID_Int = roundID_Int;
        this.roundShooterID_Int = roundShooterID_Int;
        this.roundGunID_Int = roundGunID_Int;
        this.roundLoadID_Int = roundLoadID_Int;
        this.roundRound_Int = roundRound_Int;
        this.roundScore_Int = roundScore_Int;
        this.roundHitMiss_Array = roundHitMiss_Array;
        this.roundNotes_Str = roundNotes_Str;
    }

    // Setters and Getters
    /*******************************************************************************************
     * Function: get*(), set*()
     *
     * Purpose: Getter and setter functions for this class, I would list them all, but I'm lazy
     *
     * Parameters: None
     *
     * Returns: None
     *
     ******************************************************************************************/
    
    public int getRoundID_Int() {
        return roundID_Int;
    }

    public void setRoundID_Int(int roundID_Int) {
        this.roundID_Int = roundID_Int;
    }

    public int getRoundShooterID_Int() {
        return roundShooterID_Int;
    }

    public void setRoundShooterID_Int(int roundShooterID_Int) {
        this.roundShooterID_Int = roundShooterID_Int;
    }

    public int getRoundGunID_Int() {
        return roundGunID_Int;
    }

    public void setRoundGunID_Int(int roundGunID_Int) {
        this.roundGunID_Int = roundGunID_Int;
    }

    public int getRoundLoadID_Int() {
        return roundLoadID_Int;
    }

    public void setRoundLoadID_Int(int roundLoadID_Int) {
        this.roundLoadID_Int = roundLoadID_Int;
    }

    public int getRoundRound_Int() {
        return roundRound_Int;
    }

    public void setRoundRound_Int(int roundRound_Int) {
        this.roundRound_Int = roundRound_Int;
    }

    public int getRoundScore_Int() {
        return roundScore_Int;
    }

    public void setRoundScore_Int(int roundScore_Int) {
        this.roundScore_Int = roundScore_Int;
    }

    public ArrayList<Integer> getRoundHitMiss_Array() {
        return roundHitMiss_Array;
    }

    public String getRoundHitMiss_Str() {
        String hitmiss_Str = "";

        for (Integer item: this.roundHitMiss_Array) {
            hitmiss_Str = hitmiss_Str + Integer.toString(item) + ",";
        }

        return hitmiss_Str;
    }

    public void setRoundHitMiss_Array(ArrayList<Integer> roundHitMiss_Array) {
        this.roundHitMiss_Array = roundHitMiss_Array;
    }

    public void setRoundHitMiss_Array(String roundHitMiss_Str) {
        ArrayList<Integer> hitmiss_Array = new ArrayList<>();

        String[] hitmiss_Str = roundHitMiss_Str.split(",");

        for (String item: hitmiss_Str) {
            if (item != "") {
                hitmiss_Array.add(Integer.parseInt(item));
            }
        }

        this.roundHitMiss_Array = hitmiss_Array;
    }

    public String getRoundNotes_Str() {
        return roundNotes_Str;
    }

    public void setRoundNotes_Str(String roundNotes_Str) {
        this.roundNotes_Str = roundNotes_Str;
    }

    //*************************************** Other Functions **************************************
    private void initializeShotDialogStrings() {
        SHOT_DIALOG_MSG = new ArrayList<String>(Arrays.asList(
                "Enter the score for this shoot.",
                "Choose an event for this shoot.",
                "Choose a gun for this shoot.",
                "Choose a load for this shoot.",
                "Enter any notes for this shoot."));
        SHOT_POS_BTN_TXT = new ArrayList<String>(Arrays.asList(
                "NEXT",
                "NEXT",
                "NEXT",
                "NEXT",
                "SAVE"));
        SHOT_NEU_BTN_TXT = new ArrayList<String>(Arrays.asList(
                "CANCEL",
                "BACK",
                "BACK",
                "BACK",
                "BACK"));
        SHOT_EDT_HINT = new ArrayList<String>(Arrays.asList(
                "i.e. 25",
                "",
                "",
                "",
                "i.e. This was a good event!"));
    }

//    public void editShotDialog(final Context context, final TrapMasterListArrayAdapter adapter) {
//        /*******************************************************************************************
//         * Function: editShotDialog
//         *
//         * Purpose: Function creates dialog and prompts user to add or edit an round item, if adding
//         *          an item, ID = -1 and email = current user email
//         *
//         * Parameters: context (IN) - Supplies the activity context to display dialog to
//         *             adapter (IN) - Array adapter to refresh when complete
//         *
//         * Returns: None
//         *
//         ******************************************************************************************/
//
//        // Dialog Constants
//        String DIALOG_TITLE = "Add New Shoot";;
//        int POSITIVE_BTN_COLOR = Color.BLUE;
//        int NEUTRAL_BTN_COLOR = Color.RED;
//
//        // Dialog Variables
//        GlobalApplicationContext currentContext = new GlobalApplicationContext();
//        final DBHandler db = new DBHandler(currentContext.getContext());
//
//        // Pre-Dialog Processing
//        if (getRoundID_Int() != -1) {
//            DIALOG_TITLE = "Edit Shoot";
//        }
//        initializeShotDialogStrings();
//
//        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
//
//        // Set Dialog Title
//        alertDialog.setTitle(DIALOG_TITLE);
//
//        // Set Dialog Message
//        alertDialog.setMessage(SHOT_DIALOG_MSG.get(SHOT_DIALOG_STATE));
//
//        // Set layout for gathering information
//        final RelativeLayout subView_RelLay = new RelativeLayout(context);
//
//        // Set views
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
//        final EditText item_Edt = new EditText(context);
//        item_Edt.setHint(SHOT_EDT_HINT.get(SHOT_DIALOG_STATE));
//        item_Edt.setText(getRoundScore_Int());
//        item_Edt.setGravity(Gravity.START);
//        item_Edt.setTextColor(Color.BLACK);
//        item_Edt.setLayoutParams(params);
//        subView_RelLay.addView(item_Edt);
//
//        final Spinner item_Spin = new Spinner(context);
//        item_Spin.setGravity(Gravity.START);
//        item_Spin.setLayoutParams(params);
//
//        // Add linear layout to alert dialog
//        alertDialog.setView(subView_RelLay);
//
//        // Set Buttons
//        // Positive Button, Right
//        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, SHOT_POS_BTN_TXT.get(SHOT_DIALOG_STATE), new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                // Processed by onClick below
//            }
//        });
//
//        // Neutral Button, Left
//        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, SHOT_NEU_BTN_TXT.get(SHOT_DIALOG_STATE), new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                // Processed by onClick below
//            }
//        });
//
//
//        new Dialog(context);
//        alertDialog.show();
//
//        // Set Buttons
//        final Button pos_Btn = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
//        final Button neu_Btn = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
//
//        pos_Btn.setTextColor(POSITIVE_BTN_COLOR);
//        pos_Btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Perform Action on Positive button
//                switch (SHOT_DIALOG_STATE){
//                    case 0:
//                        // Score to Event
//                        String item_Txt = item_Edt.getText().toString();
//
//                        boolean isShotScoreEmpty = item_Txt.equals("");
//
//                        if (isShotScoreEmpty) {
//                            // Check if the shoot score is empty
//                            item_Edt.setError(context.getString(R.string.error_field_required));
//                            item_Edt.requestFocus();
//                        } else {
//                            setRoundScore_Int(Integer.parseInt(item_Txt));
//
//                            subView_RelLay.removeView(item_Edt);
//                            item_Spin.setAdapter(initializeEventSpinnerAdapt(context));
//                            subView_RelLay.addView(item_Spin);
//
//                            SHOT_DIALOG_STATE = (SHOT_DIALOG_STATE + 1);
//                        }
//
//                        break;
//                    case 1:
//                        // Event to Gun
//                        item_Txt = item_Spin.getSelectedItem().toString();
//
//                        Log.d(TAG, item_Txt);
//
//                        // Check if the text is the default text for events
//                        if (item_Txt.equals(context.getString(R.string.add_event_text))) {
//                            item_Txt = RoundClass.this.roundEventName_Str;
//                        }
//
//                        setShotEventName_Str(item_Txt);
//
//                        item_Spin.setAdapter(initializeGunSpinnerAdapt(context));
//
//                        SHOT_DIALOG_STATE = (SHOT_DIALOG_STATE + 1);
//                        break;
//                    case 2:
//                        // Gun to Load
//                        item_Txt = item_Spin.getSelectedItem().toString();
//
//                        Log.d(TAG, item_Txt);
//
//                        // Check if the text is the default text for events
//                        if (item_Txt.equals(context.getString(R.string.add_gun_text))) {
//                            item_Txt = RoundClass.this.roundGun_Str;
//                        }
//
//                        setShotGun_Str(item_Txt);
//
//                        item_Spin.setAdapter(initializeLoadSpinnerAdapt(context));
//
//                        SHOT_DIALOG_STATE = (SHOT_DIALOG_STATE + 1);
//                        break;
//                    case 3:
//                        // Load to Notes
//                        item_Txt = item_Spin.getSelectedItem().toString();
//
//                        Log.d(TAG, item_Txt);
//
//                        // Check if the text is the default text for events
//                        if (item_Txt.equals(context.getString(R.string.add_load_text))) {
//                            item_Txt = RoundClass.this.roundLoad_Str;
//                        }
//
//                        setShotLoad_Str(item_Txt);
//
//                        subView_RelLay.removeView(item_Spin);
//                        item_Edt.setText(getShotNotes_Str());
//                        subView_RelLay.addView(item_Edt);
//
//                        SHOT_DIALOG_STATE = (SHOT_DIALOG_STATE + 1);
//                        break;
//                    case 4:
//                        // Notes to saving shoot and closing dialog
//                        item_Txt = item_Edt.getText().toString();
//
//                        setShotNotes_Str(item_Txt);
//
//                        if (getShotID_Int() == -1) {
//                            // Adding a new shoot
//                            db.insertShotInDB(RoundClass.this);
//                        } else {
//                            // Editing an existing shoot
//                            db.updateShotInDB(RoundClass.this);
//                        }
//
//                        alertDialog.dismiss();
//
//                        Toast.makeText(context, "Shoot saved!",
//                                Toast.LENGTH_LONG).show();
//
//                        // Refresh round list view
//                        adapter.refreshShotArrayAdapter(db.getAllShotFromDB(getShotShooterName_Str()));
//
//                        // Reset state counter
//                        SHOT_DIALOG_STATE = 0;
//                        break;
//                }
//
//                alertDialog.setMessage(SHOT_DIALOG_MSG.get(SHOT_DIALOG_STATE));
//                item_Edt.setHint(SHOT_EDT_HINT.get(SHOT_DIALOG_STATE));
//                pos_Btn.setText(SHOT_POS_BTN_TXT.get(SHOT_DIALOG_STATE));
//                neu_Btn.setText(SHOT_NEU_BTN_TXT.get(SHOT_DIALOG_STATE));
//            }
//        });
//
//        neu_Btn.setTextColor(NEUTRAL_BTN_COLOR);
//        neu_Btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Perform Action on Neutral button
//
//                switch (SHOT_DIALOG_STATE){
//                    case 0:
//                        // Score to cancel and close dialog
//                        alertDialog.dismiss();
//                        break;
//                    case 1:
//                        // Event to score
//                        subView_RelLay.removeView(item_Spin);
//                        subView_RelLay.addView(item_Edt);
//                        item_Edt.setText(getShotHitNum_Str());
//
//                        SHOT_DIALOG_STATE = (SHOT_DIALOG_STATE - 1);
//                        break;
//                    case 2:
//                        // Gun to event
//                        item_Spin.setAdapter(initializeEventSpinnerAdapt(context));
//                        break;
//                    case 3:
//                        // Load to gun
//                        item_Spin.setAdapter(initializeGunSpinnerAdapt(context));
//                        break;
//                    case 4:
//                        // Notes to load
//                        subView_RelLay.removeView(item_Edt);
//                        subView_RelLay.addView(item_Spin);
//
//                        SHOT_DIALOG_STATE = (SHOT_DIALOG_STATE - 1);
//                        break;
//                }
//
//                alertDialog.setMessage(SHOT_DIALOG_MSG.get(SHOT_DIALOG_STATE));
//                item_Edt.setHint(SHOT_EDT_HINT.get(SHOT_DIALOG_STATE));
//                pos_Btn.setText(SHOT_POS_BTN_TXT.get(SHOT_DIALOG_STATE));
//                neu_Btn.setText(SHOT_NEU_BTN_TXT.get(SHOT_DIALOG_STATE));
//            }
//
//        });
//    }
//
//    private ArrayAdapter<String> initializeGunSpinnerAdapt(final Context context) {
//        /*******************************************************************************************
//         * Function: initializeGunSpinnerAdapt
//         *
//         * Purpose: Function initializes gun spinner for adding a new event
//         *
//         * Parameters: None
//         *
//         * Returns: None
//         *
//         ******************************************************************************************/
//
//        ArrayAdapter<String> tempGun_Adapt;
//        ArrayList<GunClass> tempGun_List;
//        ArrayList<String> tempGunStr_List = new ArrayList<>();
//
//        GlobalApplicationContext currentContext = new GlobalApplicationContext();
//        final DBHandler db = new DBHandler(currentContext.getContext());
//
//        String coachName_Str = db.getShooterInDB(getShotShooterName_Str()).getShooterCoach_Str();
//        tempGun_List = db.getAllGunFromDB(coachName_Str);
//
//        for (int i = 0; i < tempGun_List.size(); i++) {
//            GunClass tempGun = tempGun_List.get(i);
//            String gunItem_Str = tempGun.getGunNickname_Str();
//
//            tempGunStr_List.add(gunItem_Str);
//        }
//
//        if (tempGunStr_List.isEmpty()) {
//            tempGunStr_List.add(context.getString(R.string.add_gun_text));
//        }
//
//        tempGun_Adapt = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, tempGunStr_List);
//
//        return tempGun_Adapt;
//    }
//
//    private ArrayAdapter<String> initializeLoadSpinnerAdapt(final Context context) {
//        /*******************************************************************************************
//         * Function: initializeLoadSpinnerAdapt
//         *
//         * Purpose: Function initializes load spinner for adding a new event
//         *
//         * Parameters: None
//         *
//         * Returns: None
//         *
//         ******************************************************************************************/
//
//        ArrayAdapter<String> tempLoad_Adapt;
//        ArrayList<LoadClass> tempLoad_List;
//        ArrayList<String> tempLoadStr_List = new ArrayList<>();
//
//        GlobalApplicationContext currentContext = new GlobalApplicationContext();
//        final DBHandler db = new DBHandler(currentContext.getContext());
//
//        String coachName_Str = db.getShooterInDB(getShotShooterName_Str()).getShooterCoach_Str();
//        tempLoad_List = db.getAllLoadFromDB(coachName_Str);
//
//        for (int i = 0; i < tempLoad_List.size(); i++) {
//            LoadClass tempLoad = tempLoad_List.get(i);
//            String loadItem_Str = tempLoad.getLoadNickname_Str();
//
//            tempLoadStr_List.add(loadItem_Str);
//        }
//
//        if (tempLoadStr_List.isEmpty()) {
//            tempLoadStr_List.add(context.getString(R.string.add_load_text));
//        }
//
//        tempLoad_Adapt = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, tempLoadStr_List);
//
//        return tempLoad_Adapt;
//    }
//
//    private ArrayAdapter<String> initializeEventSpinnerAdapt(Context context) {
//        /*******************************************************************************************
//         * Function: initializeEventSpinnerAdapt
//         *
//         * Purpose: Function initializes event spinner for adding a editing shoot
//         *
//         * Parameters: context (IN) - Supplies the context for adapter
//         *
//         * Returns: None
//         *
//         ******************************************************************************************/
//
//        ArrayAdapter<String> tempEvent_Adapt;
//        ArrayList<EventClass> tempEvent_List;
//        ArrayList<String> tempEventStr_List = new ArrayList<>();
//
//        GlobalApplicationContext currentContext = new GlobalApplicationContext();
//        final DBHandler db = new DBHandler(currentContext.getContext());
//
//        tempEvent_List = db.getAllEventFromDB(getShotShooterName_Str());
//
//        for (int i = 0; i < tempEvent_List.size(); i++) {
//            EventClass temp_Event = tempEvent_List.get(i);
//            String eventItem_Str = temp_Event.getEventName_Str();
//
//            tempEventStr_List.add(eventItem_Str);
//        }
//
//        if (tempEventStr_List.isEmpty()) {
//            tempEventStr_List.add(context.getString(R.string.add_event_text));
//        }
//
//        tempEvent_Adapt = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, tempEventStr_List);
//
//        return tempEvent_Adapt;
//    }
//
//    public void removeShotItemDialog(final Context context, final TrapMasterListArrayAdapter adapter) {
//        /*******************************************************************************************
//         * Function: removeShotItemDialog
//         *
//         * Purpose: Function creates dialog and prompts user to remove a shoot item
//         *
//         * Parameters: context (IN) - Supplies the activity context to display dialog to
//         *             adapter (IN) - Array adapter to refresh when complete
//         *
//         * Returns: None
//         *
//         ******************************************************************************************/
//
//        final String DIALOG_TITLE = "Delete Shoot";
//        final String DIALOG_MSG = "Are you sure you want to delete this shoot?";
//
//        final boolean POSITIVE_BTN = true;  // Right
//        final boolean NEUTRAL_BTN = false;   // Left
//        final boolean NEGATIVE_BTN = true;  // Middle
//
//        final String POSITIVE_BUTTON_TXT = "DELETE";
//        final String NEUTRAL_BUTTON_TXT = "";
//        final String NEGATIVE_BUTTON_TXT = "CANCEL";
//
//        final int POSITIVE_BTN_COLOR = Color.BLUE;
//        final int NEUTRAL_BTN_COLOR = Color.RED;
//        final int NEGATIVE_BTN_COLOR = Color.RED;
//
//
//        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
//
//        GlobalApplicationContext currentContext = new GlobalApplicationContext();
//        final DBHandler db = new DBHandler(currentContext.getContext());
//
//        // Set Dialog Title
//        alertDialog.setTitle(DIALOG_TITLE);
//
//        // Set Dialog Message
//        alertDialog.setMessage(DIALOG_MSG);
//
//        // Set Buttons
//        // Positive Button, Right
//        if (POSITIVE_BTN) {
//            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, POSITIVE_BUTTON_TXT, new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                    // Processed by onClick below
//                }
//            });
//        }
//
//        // Neutral Button, Left
//        if (NEUTRAL_BTN) {
//            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, NEUTRAL_BUTTON_TXT, new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                    // Processed by onClick below
//                }
//            });
//        }
//
//        // Negative Button, Middle
//        if (NEGATIVE_BTN) {
//            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, NEGATIVE_BUTTON_TXT, new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                    // Processed by onClick below
//                }
//            });
//        }
//
//
//        new Dialog(context);
//        alertDialog.show();
//
//        // Set Buttons
//        if (POSITIVE_BTN) {
//            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(POSITIVE_BTN_COLOR);
//            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    // Perform Action on Positive button
//                    db.deleteShotInDB(getShotID_Int());
//
//                    alertDialog.dismiss();
//
//                    Toast.makeText(context, "Shoot deleted.",
//                            Toast.LENGTH_LONG).show();
//
//                    // Refresh shoot list view
//                    adapter.refreshShotArrayAdapter(db.getAllShotFromDB(getShotShooterName_Str()));
//                }
//            });
//        }
//        if (NEUTRAL_BTN) {
//            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(NEUTRAL_BTN_COLOR);
//            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    // Perform Action on Neutral button
//                }
//            });
//        }
//        if (NEGATIVE_BTN) {
//            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(NEGATIVE_BTN_COLOR);
//            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    // Perform Action on Negative button
//                    alertDialog.dismiss();
//                }
//            });
//        }
//    }

    //************************************** Compare Functions *************************************
    @Override
    public int compareTo(@NonNull RoundClass s) {
        return (Integer.compare(this.getRoundID_Int(), s.getRoundID_Int()));
    }
}
