/***************************************************************************************************
 * FILENAME : MatchClass.java
 *
 * AUTHOR : Dalton Shadle
 *
 * DESCRIPTION : Holds variables and functions for MatchClass object
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MatchClass implements Comparable <MatchClass> {
    //************************************* Private Variables **************************************
    // Object variables
    private int matchID_Int;
    private int matchShooterID_Int;
    private int matchTeamID_Int;
    private int matchEventID_Int;
    private int matchScore_Int;
    private ArrayList<Integer> matchRoundIDS_Array;
    private String matchNotes_Str;

    // Dialog variables
    private final String TAG = "JRW";

    //************************************* Public Functions ***************************************
    // Constructors
    public MatchClass() {
        /*******************************************************************************************
         * Function: MatchClass
         *
         * Purpose: Constructor for this class, no parameters so all values are initialized to temp
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        this.matchID_Int = -1;
        this.matchShooterID_Int = -1;
        this.matchTeamID_Int = -1;
        this.matchEventID_Int = -1;
        this.matchScore_Int = -1;
        this.matchRoundIDS_Array = new ArrayList<>();
        this.matchNotes_Str = "";
    }

    public MatchClass(int matchID_Int, int matchShooterID_Int, int matchTeamID_Int,
                      int matchEventID_Int, int matchScore_Int,
                      ArrayList<Integer> matchRoundIDS_Array, String matchNotes_Str) {
        /*******************************************************************************************
         * Function: MatchClass
         *
         * Purpose: Constructor for this class with parameters
         *
         * Parameters: matchID_Int (IN) - ID tagged with this match
         *             matchShooterID_Int (IN) - shooter id tagged with this match
         *             matchTeamID_Int (IN) - team id of match
         *             matchEventID_Int (IN) - event id of match
         *             matchScore_Int (IN) - score of match
         *             matchRoundIDS_Array (IN) - array of round ids for match
         *             matchNotes_Str (IN) - notes tagged with the match
         *
         * Returns: None
         *
         ******************************************************************************************/

        this.matchID_Int = matchID_Int;
        this.matchShooterID_Int = matchShooterID_Int;
        this.matchTeamID_Int = matchTeamID_Int;
        this.matchEventID_Int = matchEventID_Int;
        this.matchScore_Int = matchScore_Int;
        this.matchRoundIDS_Array = matchRoundIDS_Array;
        this.matchNotes_Str = matchNotes_Str;
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
    public int getMatchID_Int() {
        return matchID_Int;
    }

    public void setMatchID_Int(int matchID_Int) {
        this.matchID_Int = matchID_Int;
    }

    public int getMatchShooterID_Int() {
        return matchShooterID_Int;
    }

    public void setMatchShooterID_Int(int matchShooterID_Int) {
        this.matchShooterID_Int = matchShooterID_Int;
    }

    public int getMatchTeamID_Int() {
        return matchTeamID_Int;
    }

    public void setMatchTeamID_Int(int matchTeamID_Int) {
        this.matchTeamID_Int = matchTeamID_Int;
    }

    public int getMatchEventID_Int() {
        return matchEventID_Int;
    }

    public void setMatchEventID_Int(int matchEventID_Int) {
        this.matchEventID_Int = matchEventID_Int;
    }

    public int getMatchScore_Int() {
        return matchScore_Int;
    }

    public void setMatchScore_Int(int matchScore_Int) {
        this.matchScore_Int = matchScore_Int;
    }

    public ArrayList<Integer> getMatchRoundIDS_Array() {
        return matchRoundIDS_Array;
    }

    public String getMatchRoundIDS_Str() {
        String round_Str = "";

        for (Integer item: this.matchRoundIDS_Array) {
            round_Str = round_Str + Integer.toString(item) + ",";
        }

        return round_Str;
    }

    public void setMatchRoundIDS_Array(ArrayList<Integer> matchRoundIDS_Array) {
        this.matchRoundIDS_Array = matchRoundIDS_Array;
    }

    public void setMatchRoundIDS_Array(String round_Str) {
        ArrayList<Integer> round_Array = new ArrayList<>();

        String[] matchRound_Str = round_Str.split(",");

        for (String item: matchRound_Str) {
            if (item != "") {
                round_Array.add(Integer.parseInt(item));
            }
        }

        this.matchRoundIDS_Array = round_Array;
    }

    public String getMatchNotes_Str() {
        return matchNotes_Str;
    }

    public void setMatchNotes_Str(String matchNotes_Str) {
        this.matchNotes_Str = matchNotes_Str;
    }

    public String toString(){
        /*******************************************************************************************
         * Function: toString
         *
         * Purpose: Function returns item as a string
         *
         * Parameters: None
         *
         * Returns: item_Str (OUT) - string representing all values of the item
         *
         ******************************************************************************************/

        // get updated database
        GlobalApplicationContext currentContext = new GlobalApplicationContext();
        final DBHandler db = new DBHandler(currentContext.getContext());

        String item_Str = "Shooter: " + db.getShooterInDB(getMatchShooterID_Int()).getShooterName_Str() + "\n"
                + "Event: " + db.getEventInDB(getMatchEventID_Int()).getEventName_Str() + "\n"
                + "Score: " + Integer.toString(getMatchScore_Int()) + "\n"
                + "Notes: " + getMatchNotes_Str();


        return item_Str;
    }

    //************************************* Database Functions *************************************
    public AlertDialog removeMatchItemDialog(final Context context) {
        /*******************************************************************************************
         * Function: removeMatchItemDialog
         *
         * Purpose: Function creates dialog and prompts user to remove a event item
         *
         * Parameters: context (IN) - Supplies the activity context to display dialog to
         *
         * Returns: alertDialog (OUT) - AlertDialog variable this function creates
         *
         ******************************************************************************************/

        final String DIALOG_TITLE = "Delete Match";
        final String DIALOG_MSG = "Are you sure you want to delete this match?";

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

        GlobalApplicationContext currentContext = new GlobalApplicationContext();
        final DBHandler db = new DBHandler(currentContext.getContext());

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
                    db.deleteMatchInDB(getMatchID_Int());

                    alertDialog.dismiss();

                    Toast.makeText(context, "Match deleted.",
                            Toast.LENGTH_LONG).show();
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
                    alertDialog.cancel();
                }
            });
        }

        return alertDialog;
    }

    public AlertDialog editMatchDialog(final Context context) {
        /*******************************************************************************************
         * Function: editMatchDialog
         *
         * Purpose: Function creates dialog and prompts user to add or edit a load item, if adding
         *          an item, ID = -1 and email = current user email
         *
         * Parameters: context (IN) - activity context to display dialog
         *
         * Returns: alertDialog (OUT) - alert dialog created by this function
         *
         ******************************************************************************************/

        // Dialog Constants
        String DIALOG_TITLE = "Edit Match";
        int POSITIVE_BTN_COLOR = Color.BLUE;
        int NEUTRAL_BTN_COLOR = Color.RED;

        // Dialog Variables
        GlobalApplicationContext currentContext = new GlobalApplicationContext();
        final DBHandler db = new DBHandler(currentContext.getContext());

        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Set Dialog Title
        alertDialog.setTitle(DIALOG_TITLE);

        // Set Dialog Message
        alertDialog.setMessage("Message");


        // Set Buttons
        // Positive Button, Right
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "RIGHT", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Processed by onClick below
            }
        });

        // Neutral Button, Left
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "LEFT", new DialogInterface.OnClickListener() {
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

            }
        });

        neu_Btn.setTextColor(NEUTRAL_BTN_COLOR);
        neu_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform Action on Neutral button

            }

        });

        return alertDialog;
    }

    //************************************** Compare Functions *************************************
    @Override
    public int compareTo(@NonNull MatchClass s) {
        return (Integer.compare(this.getMatchID_Int(), s.getMatchID_Int()));
    }
}

