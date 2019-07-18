/***************************************************************************************************
 * FILENAME : GunClass.java
 *
 * AUTHOR : Dalton Shadle
 *
 * DESCRIPTION : Holds variables and functions for GunClass object
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class GunClass {
    //************************************* Private Variables **************************************
    // Object variables
    private int gunID_Int;
    private int gunProfileID_Int;
    private String gunNickname_Str;
    private String gunModel_Str;
    private String gunGauge_Str;
    private String gunNotes_Str;

    // Dialog variables
    private int GUN_DIALOG_STATE = 0;
    private ArrayList<String> GUN_DIALOG_MSG, GUN_POS_BTN_TXT, GUN_NEU_BTN_TXT, GUN_EDT_HINT;

    //************************************* Public Functions ***************************************
    // Constructors
    public GunClass() {
        /*******************************************************************************************
         * Function: GunClass
         *
         * Purpose: Constructor for this class, no parameters so all values are initialized to temp
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        this.gunID_Int = -1;
        this.gunProfileID_Int = -1;
        this.gunNickname_Str = "";
        this.gunModel_Str = "";
        this.gunGauge_Str = "";
        this.gunNotes_Str = "";
    }

    public GunClass(int gunProfileID_Int, String gunNickname_Str,
                    String gunModel_Str, String gunGauge_Str, String gunNotes_Str) {
        /*******************************************************************************************
         * Function: GunClass
         *
         * Purpose: Constructor for this class with parameters
         *
         * Parameters: gunProfileID_Int (IN) - profile ID tagged with this gun
         *             gunNickname_Str (IN) - name of the gun
         *             gunModel_Str (IN) - model of the gun
         *             gunGauge_Str (IN) - gauge of the gun
         *             gunNotes_Str (IN) - notes tagged with the gun
         *
         * Returns: None
         *
         ******************************************************************************************/

        this.gunID_Int = -1;
        this.gunProfileID_Int = gunProfileID_Int;
        this.gunNickname_Str = gunNickname_Str;
        this.gunModel_Str = gunModel_Str;
        this.gunGauge_Str = gunGauge_Str;
        this.gunNotes_Str = gunNotes_Str;
    }

    // Getters and Setters
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
    public int getGunProfileID_Int() {
        return gunProfileID_Int;
    }

    public void setGunProfileID_Int(int gunProfileID_Int) {
        this.gunProfileID_Int = gunProfileID_Int;
    }

    public String getGunNickname_Str() {
        return gunNickname_Str;
    }

    public void setGunNickname_Str(String gunNickname_Str) {
        this.gunNickname_Str = gunNickname_Str;
    }

    public String getGunModel_Str() {
        return gunModel_Str;
    }

    public void setGunModel_Str(String gunModel_Str) {
        this.gunModel_Str = gunModel_Str;
    }

    public String getGunGauge_Str() {
        return gunGauge_Str;
    }

    public void setGunGauge_Str(String gunGauge_Str) {
        this.gunGauge_Str = gunGauge_Str;
    }

    public String getGunNotes_Str() {
        return gunNotes_Str;
    }

    public void setGunNotes_Str(String gunNotes_Str) {
        this.gunNotes_Str = gunNotes_Str;
    }

    public int getGunID_Int() {
        return gunID_Int;
    }

    public void setGunID_Int(int gunID_Str) {
        this.gunID_Int = gunID_Str;
    }

    //*************************************** Other Functions **************************************
    public void removeGunDialog(final Context context, final TrapMasterListArrayAdapter adapter) {
        /*******************************************************************************************
         * Function: removeGunItemDialog
         *
         * Purpose: Function creates dialog and prompts user to remove a gun item
         *
         * Parameters: context (IN) - Supplies the activity context to display dialog to
         *             adapter (IN) - Array adapter to refresh when complete
         *
         * Returns: None
         *
         ******************************************************************************************/

        // Constants for Dialog
        final String DIALOG_TITLE = "Delete Gun";
        final String DIALOG_MSG = "Are you sure you want to delete this gun?";

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
        final int finalGunID_Int = this.gunID_Int;
        final DBHandler db = new DBHandler(currentContext.getContext());

        // Set Dialog Title
        alertDialog.setTitle(DIALOG_TITLE);

        // Set Dialog Message
        alertDialog.setMessage(DIALOG_MSG);

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
                    db.deleteGunInDB(finalGunID_Int);

                    alertDialog.dismiss();

                    Toast.makeText(context, "Gun deleted.",
                            Toast.LENGTH_LONG).show();

                    // Refresh gun listview
                    adapter.refreshGunArrayAdapter(db.getAllGunFromDB(getGunProfileID_Int()));
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
                    // Perfrom Action on Negative button
                    alertDialog.dismiss();
                }
            });
        }
    }

    private void initializeGunDialogStrings() {
        GUN_DIALOG_MSG = new ArrayList<String>(Arrays.asList(
                "Enter a nickname for this gun.",
                "Enter the model for this gun.",
                "Enter the gauge for this gun.",
                "Enter any notes for this gun."));
        GUN_POS_BTN_TXT = new ArrayList<String>(Arrays.asList(
                "NEXT",
                "NEXT",
                "NEXT",
                "SAVE"));
        GUN_NEU_BTN_TXT = new ArrayList<String>(Arrays.asList(
                "CANCEL",
                "BACK",
                "BACK",
                "BACK"));
        GUN_EDT_HINT = new ArrayList<String>(Arrays.asList(
                "i.e. MyGun",
                "i.e. Remington Model 870 ",
                "i.e. 12",
                "i.e. This is a good gun!"));
    }

    public void editGunDialog(final Context context, final TrapMasterListArrayAdapter adapter) {
        /*******************************************************************************************
         * Function: editGunDialog
         *
         * Purpose: Function creates dialog and prompts user to add or edit a gun item, if adding
         *          an item, ID = -1 and email = current user email
         *
         * Parameters: context (IN) - Supplies the activity context to display dialog to
         *             adapter (IN) - Array adapter to refresh when complete
         *
         * Returns: None
         *
         ******************************************************************************************/

        // Dialog Constants
        String DIALOG_TITLE = "Add New Gun";;
        int POSITIVE_BTN_COLOR = Color.BLUE;
        int NEUTRAL_BTN_COLOR = Color.RED;

        // Dialog Variables
        GlobalApplicationContext currentContext = new GlobalApplicationContext();
        final DBHandler db = new DBHandler(currentContext.getContext());

        // Pre-Dialog Processing
        if (getGunID_Int() != -1) {
            DIALOG_TITLE = "Edit Gun";
        }
        initializeGunDialogStrings();

        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Set Dialog Title
        alertDialog.setTitle(DIALOG_TITLE);

        // Set Dialog Message
        alertDialog.setMessage(GUN_DIALOG_MSG.get(GUN_DIALOG_STATE));

        // Set view for gathering information
        LinearLayout subView_LnrLay = new LinearLayout(context);
        subView_LnrLay.setOrientation(LinearLayout.VERTICAL);

        // Set view
        final EditText item_Edt = new EditText(context);
        item_Edt.setHint(GUN_EDT_HINT.get(GUN_DIALOG_STATE));
        item_Edt.setText(getGunNickname_Str());
        item_Edt.setGravity(Gravity.START);
        item_Edt.setTextColor(Color.BLACK);
        subView_LnrLay.addView(item_Edt);

        // Add linear layout to alert dialog
        alertDialog.setView(subView_LnrLay);

        // Set Buttons
        // Positive Button, Right
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, GUN_POS_BTN_TXT.get(GUN_DIALOG_STATE), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Processed by onClick below
            }
        });

        // Neutral Button, Left
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, GUN_NEU_BTN_TXT.get(GUN_DIALOG_STATE), new DialogInterface.OnClickListener() {
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
                String itemEdt_Str = item_Edt.getText().toString();

                switch (GUN_DIALOG_STATE){
                    case 0:
                        // Nickname to Model
                        boolean isGunNicknameEmpty = itemEdt_Str.equals("");
                        boolean isGunNicknameInDB = db.isGunNicknameInDB(getGunProfileID_Int(), itemEdt_Str, getGunID_Int());

                        if (isGunNicknameEmpty) {
                            // Check if the gun nickname is empty
                            item_Edt.setError(context.getString(R.string.error_field_required));
                            item_Edt.requestFocus();
                        } else if (isGunNicknameInDB) {
                            // Check if the gun nickname is already used in the database, uer cannot have 2 guns with same name
                            item_Edt.setError(context.getString(R.string.error_armory_gun_already_exists));
                            item_Edt.requestFocus();
                        } else {
                            setGunNickname_Str(itemEdt_Str);
                            item_Edt.setText(getGunModel_Str());
                            GUN_DIALOG_STATE = (GUN_DIALOG_STATE + 1);
                        }

                        break;
                    case 1:
                        // Model to Gauge
                        setGunModel_Str(itemEdt_Str);
                        item_Edt.setText(getGunGauge_Str());
                        GUN_DIALOG_STATE = (GUN_DIALOG_STATE + 1);
                        break;
                    case 2:
                        // Gauge to Notes
                        setGunGauge_Str(itemEdt_Str);
                        item_Edt.setText(getGunNotes_Str());
                        GUN_DIALOG_STATE = (GUN_DIALOG_STATE + 1);
                        break;
                    case 3:
                        // Notes to save gun and close dialog
                        setGunNotes_Str(itemEdt_Str);

                        if (getGunID_Int() == -1) {
                            // User is ADDING a gun item
                            db.insertGunInDB(GunClass.this);
                        } else {
                            // User is EDITING a gun item
                            db.updateGunInDB(GunClass.this);
                        }

                        alertDialog.dismiss();

                        Toast.makeText(context, "Gun saved!",
                                Toast.LENGTH_LONG).show();

                        // Refresh gun listview
                        adapter.refreshGunArrayAdapter(db.getAllGunFromDB(getGunProfileID_Int()));

                        // Reset state counter
                        GUN_DIALOG_STATE = 0;
                        break;
                }

                alertDialog.setMessage(GUN_DIALOG_MSG.get(GUN_DIALOG_STATE));
                item_Edt.setHint(GUN_EDT_HINT.get(GUN_DIALOG_STATE));
                pos_Btn.setText(GUN_POS_BTN_TXT.get(GUN_DIALOG_STATE));
                neu_Btn.setText(GUN_NEU_BTN_TXT.get(GUN_DIALOG_STATE));
            }
        });

        neu_Btn.setTextColor(NEUTRAL_BTN_COLOR);
        neu_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform Action on Neutral button

                switch (GUN_DIALOG_STATE){
                    case 0:
                        // Nickname to cancel new gun and close dialog
                        alertDialog.dismiss();
                        break;
                    case 1:
                        // Model to Nickname
                        item_Edt.setText(getGunNickname_Str());
                        GUN_DIALOG_STATE = (GUN_DIALOG_STATE - 1);
                        break;
                    case 2:
                        // Gauge to Model
                        item_Edt.setText(getGunModel_Str());
                        GUN_DIALOG_STATE = (GUN_DIALOG_STATE - 1);
                        break;
                    case 3:
                        // Notes to Gauge
                        item_Edt.setText(getGunGauge_Str());
                        GUN_DIALOG_STATE = (GUN_DIALOG_STATE - 1);
                        break;
                }

                alertDialog.setMessage(GUN_DIALOG_MSG.get(GUN_DIALOG_STATE));
                item_Edt.setHint(GUN_EDT_HINT.get(GUN_DIALOG_STATE));
                pos_Btn.setText(GUN_POS_BTN_TXT.get(GUN_DIALOG_STATE));
                neu_Btn.setText(GUN_NEU_BTN_TXT.get(GUN_DIALOG_STATE));
            }

        });
    }

}
