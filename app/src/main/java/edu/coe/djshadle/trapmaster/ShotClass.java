/***************************************************************************************************
 * FILENAME : ShotClass.java
 *
 * AUTHOR : Dalton Shadle
 *
 * DESCRIPTION : Holds variables and functions for ShotClass object
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
import android.support.v7.app.AlertDialog;
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

public class ShotClass {
    // Might be my favorite name for a class, I will try to work this in wherever I can
    //************************************* Private Variables **************************************
    // Object variables
    private int shotID_Int;
    private String shotEmail_Str;
    private String shotEventName_Str;
    private String shotTotalNum_Str;
    private String shotHitNum_Str;
    private String shotNotes_Str;

    // Dialog variables
    private int SHOT_DIALOG_STATE = 0;
    private ArrayList<String> SHOT_DIALOG_MSG, SHOT_POS_BTN_TXT, SHOT_NEU_BTN_TXT, SHOT_EDT_HINT;

    //************************************* Public Functions ***************************************
    // Constructors
    public ShotClass() {
        /*******************************************************************************************
         * Function: ShotClass
         *
         * Purpose: Constructor for this class, no parameters so all values are initialized to temp
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        this.shotID_Int = -1;
        this.shotEmail_Str = "";
        this.shotEventName_Str = "";
        this.shotTotalNum_Str = "";
        this.shotHitNum_Str = "";
        this.shotNotes_Str = "";
    }

    public ShotClass(int shotID_Int, String shotEmail_Str, String shotEventName_Str,
                     String shotTotalNum_Str, String shotHitNum_Str, String shotNotes_Str) {
        /*******************************************************************************************
         * Function: ShotClass
         *
         * Purpose: Constructor for this class with parameters
         *
         * Parameters: shotID_Str (IN) - ID tagged with this shot
         *             shotEmail_Str (IN) - email tagged with this shot
         *             shotEventName_Str (IN) - event name of the shot
         *             shotTotalNum_Str (IN) - total number of shots
         *             shotHitNum_Str (IN) - number of shots that were hits
         *             shotNotes_Str (IN) - notes tagged with the shot
         *
         * Returns: None
         *
         ******************************************************************************************/

        this.shotID_Int = shotID_Int;
        this.shotEmail_Str = shotEmail_Str;
        this.shotEventName_Str = shotEventName_Str;
        this.shotTotalNum_Str = shotTotalNum_Str;
        this.shotHitNum_Str = shotHitNum_Str;
        this.shotNotes_Str = shotNotes_Str;
    }

    public ShotClass(String shotEmail_Str, String shotEventName_Str,
                     String shotTotalNum_Str, String shotHitNum_Str, String shotNotes_Str) {
        /*******************************************************************************************
         * Function: ShotClass
         *
         * Purpose: Constructor for this class with parameters
         *
         * Parameters: shotEmail_Str (IN) - email tagged with this shot
         *             shotEventName_Str (IN) - event name of the shot
         *             shotTotalNum_Str (IN) - total number of shots
         *             shotHitNum_Str (IN) - number of shots that were hits
         *             shotNotes_Str (IN) - notes tagged with the shot
         *
         * Returns: None
         *
         ******************************************************************************************/

        this.shotID_Int = -1;
        this.shotEmail_Str = shotEmail_Str;
        this.shotEventName_Str = shotEventName_Str;
        this.shotTotalNum_Str = shotTotalNum_Str;
        this.shotHitNum_Str = shotHitNum_Str;
        this.shotNotes_Str = shotNotes_Str;
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
    public int getShotID_Int() {
        return shotID_Int;
    }

    public void setShotID_Int(int shotID_Int) {
        this.shotID_Int = shotID_Int;
    }

    public String getShotEmail_Str() {
        return shotEmail_Str;
    }

    public void setShotEmail_Str(String shotEmail_Str) {
        this.shotEmail_Str = shotEmail_Str;
    }

    public String getShotEventName_Str() {
        return shotEventName_Str;
    }

    public void setShotEventName_Str(String shotEventName_Str) {
        this.shotEventName_Str = shotEventName_Str;
    }

    public String getShotTotalNum_Str() {
        return shotTotalNum_Str;
    }

    public void setShotTotalNum_Str(String shotTotalNum_Str) {
        this.shotTotalNum_Str = shotTotalNum_Str;
    }

    public String getShotHitNum_Str() {
        return shotHitNum_Str;
    }

    public void setShotHitNum_Str(String shotHitNum_Str) {
        this.shotHitNum_Str = shotHitNum_Str;
    }

    public String getShotNotes_Str() {
        return shotNotes_Str;
    }

    public void setShotNotes_Str(String shotNotes_Str) {
        this.shotNotes_Str = shotNotes_Str;
    }

    //*************************************** Other Functions **************************************
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

    public void editShotDialog(final Context context, final TrapMasterListArrayAdapter adapter) {
        /*******************************************************************************************
         * Function: editShotDialog
         *
         * Purpose: Function creates dialog and prompts user to add or edit an shot item, if adding
         *          an item, ID = -1 and email = current user email
         *
         * Parameters: context (IN) - Supplies the activity context to display dialog to
         *             adapter (IN) - Array adapter to refresh when complete
         *
         * Returns: None
         *
         ******************************************************************************************/

        // Dialog Constants
        String DIALOG_TITLE = "Add New Shoot";;
        int POSITIVE_BTN_COLOR = Color.BLUE;
        int NEUTRAL_BTN_COLOR = Color.RED;

        // Dialog Variables
        final DBHandler db = new DBHandler(context);

        // Pre-Dialog Processing
        if (getShotID_Int() != -1) {
            DIALOG_TITLE = "Edit Shoot";
        }
        initializeShotDialogStrings();

        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Set Dialog Title
        alertDialog.setTitle(DIALOG_TITLE);

        // Set Dialog Message
        alertDialog.setMessage(SHOT_DIALOG_MSG.get(SHOT_DIALOG_STATE));

        // Set layout for gathering information
        final RelativeLayout subView_RelLay = new RelativeLayout(context);

        // Set views
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final EditText item_Edt = new EditText(context);
        item_Edt.setHint(SHOT_EDT_HINT.get(SHOT_DIALOG_STATE));
        item_Edt.setText(getShotHitNum_Str());
        item_Edt.setGravity(Gravity.START);
        item_Edt.setTextColor(Color.BLACK);
        item_Edt.setLayoutParams(params);
        subView_RelLay.addView(item_Edt);

        final Spinner item_Spin = new Spinner(context);
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
                switch (SHOT_DIALOG_STATE){
                    case 0:
                        // Score to Event
                        String item_Txt = item_Edt.getText().toString();

                        boolean isShotScoreEmpty = item_Txt.equals("");

                        if (isShotScoreEmpty) {
                            // Check if the shoot score is empty
                            item_Edt.setError(context.getString(R.string.error_field_required));
                            item_Edt.requestFocus();
                        } else {
                            setShotHitNum_Str(item_Txt);

                            subView_RelLay.removeView(item_Edt);
                            item_Spin.setAdapter(initializeEventSpinnerAdapt(context));
                            subView_RelLay.addView(item_Spin);

                            SHOT_DIALOG_STATE = (SHOT_DIALOG_STATE + 1);
                        }

                        break;
                    case 1:
                        // Event to Notes
                        item_Txt = item_Spin.toString();

                        setShotEventName_Str(item_Txt);

                        subView_RelLay.removeView(item_Spin);
                        item_Edt.setText(getShotNotes_Str());
                        subView_RelLay.addView(item_Edt);

                        SHOT_DIALOG_STATE = (SHOT_DIALOG_STATE + 1);
                        break;
                    case 2:
                        // Notes to saving shoot and closing dialog
                        item_Txt = item_Edt.getText().toString();

                        setShotNotes_Str(item_Txt);

                        if (getShotID_Int() == -1) {
                            // Adding a new shoot
                            db.insertShotInDB(ShotClass.this);
                        } else {
                            // Editing an existing shoot
                            db.updateShotInDB(ShotClass.this);
                        }

                        alertDialog.dismiss();

                        Toast.makeText(context, "Shoot saved!",
                                Toast.LENGTH_LONG).show();

                        // Refresh shot list view
                        adapter.refreshShotArrayAdapter(db.getAllShotFromDB(getShotEmail_Str()));

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
                        item_Edt.setText(getShotHitNum_Str());

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

    private ArrayAdapter<String> initializeEventSpinnerAdapt(Context context) {
        /*******************************************************************************************
         * Function: initializeEventSpinnerAdapt
         *
         * Purpose: Function initializes event spinner for adding a editing shoot
         *
         * Parameters: context (IN) - Supplies the context for adapter
         *
         * Returns: None
         *
         ******************************************************************************************/

        ArrayAdapter<String> tempEvent_Adapt;
        ArrayList<EventClass> tempEvent_List;
        ArrayList<String> tempEventStr_List = new ArrayList<>();
        DBHandler db = new DBHandler(context);

        tempEvent_List = db.getAllEventFromDB(getShotEmail_Str());

        for (int i = 0; i < tempEvent_List.size(); i++) {
            EventClass temp_Event = tempEvent_List.get(i);
            String eventItem_Str = temp_Event.getEventName_Str();

            tempEventStr_List.add(eventItem_Str);
        }

        if (tempEventStr_List.isEmpty()) {
            tempEventStr_List.add(context.getString(R.string.add_event_text));
        }

        tempEvent_Adapt = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, tempEventStr_List);

        return tempEvent_Adapt;
    }

    public void removeShotItemDialog(final Context context, final TrapMasterListArrayAdapter adapter) {
        /*******************************************************************************************
         * Function: removeShotItemDialog
         *
         * Purpose: Function creates dialog and prompts user to remove a shoot item
         *
         * Parameters: context (IN) - Supplies the activity context to display dialog to
         *             adapter (IN) - Array adapter to refresh when complete
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


        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        final DBHandler db = new DBHandler(context);

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


        new Dialog(context);
        alertDialog.show();

        // Set Buttons
        if (POSITIVE_BTN) {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(POSITIVE_BTN_COLOR);
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Perform Action on Positive button
                    db.deleteShotInDB(getShotID_Int());

                    alertDialog.dismiss();

                    Toast.makeText(context, "Shoot deleted.",
                            Toast.LENGTH_LONG).show();

                    // Refresh shoot list view
                    adapter.refreshShotArrayAdapter(db.getAllShotFromDB(getShotEmail_Str()));
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
}
