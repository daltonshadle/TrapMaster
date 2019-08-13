/***************************************************************************************************
 * FILENAME : LoadClass.java
 *
 * AUTHOR : Dalton Shadle
 *
 * DESCRIPTION : Holds variables and functions for LoadClass object
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class LoadClass {
    //************************************* Private Variables **************************************
    // Object variables
    private int loadID_Int;
    private int loadProfileID_Int;
    private String loadNickname_Str;
    private String loadBrand_Str;
    private String loadGauge_Str;
    private String loadLength_Str;
    private String loadGrain_Str;
    private String loadNotes_Str;

    // Dialog variables
    private int LOAD_DIALOG_STATE = 0;
    private ArrayList<String> LOAD_DIALOG_MSG, LOAD_POS_BTN_TXT, LOAD_NEU_BTN_TXT, LOAD_EDT_HINT;

    //************************************* Public Functions ***************************************
    // Constructors
    public LoadClass() {
        /*******************************************************************************************
         * Function: LoadClass
         *
         * Purpose: Constructor for this class, no parameters so all values are initialized to temp
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        this.loadID_Int = -1;
        this.loadProfileID_Int = -1;
        this.loadNickname_Str = "";
        this.loadBrand_Str = "";
        this.loadGauge_Str = "";
        this.loadLength_Str = "";
        this.loadGrain_Str = "";
        this.loadNotes_Str = "";
    }

    public LoadClass(int loadProfileID_Int, String loadNickname_Str,
                     String loadBrand_Str, String loadGauge_Str, String loadLength_Str,
                     String loadGrain_Str, String loadNotes_Str) {
        /*******************************************************************************************
         * Function: LoadClass
         *
         * Purpose: Constructor for this class with parameters
         *
         * Parameters: loadProfileID_Int (IN) - profile ID tagged with this load
         *             loadNickname_Str (IN) - name of the load
         *             loadBrand_Str (IN) - brand tagged with the load
         *             loadGauge_Str (IN) - gauge tagged with the load
         *             loadLength_Str (IN) - length tagged with the load
         *             loadGrain_Str (IN) - grain tagged with the load
         *             loadNotes_Str (IN) - notes tagged with the load
         *
         * Returns: None
         *
         ******************************************************************************************/

        this.loadID_Int = -1;
        this.loadProfileID_Int = loadProfileID_Int;
        this.loadNickname_Str = loadNickname_Str;
        this.loadBrand_Str = loadBrand_Str;
        this.loadGauge_Str = loadGauge_Str;
        this.loadLength_Str = loadLength_Str;
        this.loadGrain_Str = loadGrain_Str;
        this.loadNotes_Str = loadNotes_Str;
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
    public int getLoadProfileID_Int() {
        return loadProfileID_Int;
    }

    public void setLoadProfileID_Int(int loadProfileID_Int) {
        this.loadProfileID_Int = loadProfileID_Int;
    }

    public String getLoadNickname_Str() {
        return loadNickname_Str;
    }

    public void setLoadNickname_Str(String loadNickname_Str) {
        this.loadNickname_Str = loadNickname_Str;
    }

    public String getLoadBrand_Str() {
        return loadBrand_Str;
    }

    public void setLoadBrand_Str(String loadBrand_Str) {
        this.loadBrand_Str = loadBrand_Str;
    }

    public String getLoadGauge_Str() {
        return loadGauge_Str;
    }

    public void setLoadGauge_Str(String loadGauge_Str) {
        this.loadGauge_Str = loadGauge_Str;
    }

    public String getLoadLength_Str() {
        return loadLength_Str;
    }

    public void setLoadLength_Str(String loadLength_Str) {
        this.loadLength_Str = loadLength_Str;
    }

    public String getLoadGrain_Str() {
        return loadGrain_Str;
    }

    public void setLoadGrain_Str(String loadGrain_Str) {
        this.loadGrain_Str = loadGrain_Str;
    }

    public String getLoadNotes_Str() {
        return loadNotes_Str;
    }

    public void setLoadNotes_Str(String loadNotes_Str) {
        this.loadNotes_Str = loadNotes_Str;
    }

    public int getLoadID_Int() {
        return loadID_Int;
    }

    public void setLoadID_Int(int loadID_Str) {
        this.loadID_Int = loadID_Str;
    }

    public void setLoadFromLoad(LoadClass temp_Load) {
        this.setLoadGrain_Str(temp_Load.getLoadGrain_Str());
        this.setLoadGauge_Str(temp_Load.getLoadGauge_Str());
        this.setLoadBrand_Str(temp_Load.getLoadBrand_Str());
        this.setLoadNickname_Str(temp_Load.getLoadNickname_Str());
        this.setLoadNotes_Str(temp_Load.getLoadNotes_Str());
        this.setLoadLength_Str(temp_Load.getLoadLength_Str());
        this.setLoadID_Int(temp_Load.getLoadID_Int());
        this.setLoadProfileID_Int(temp_Load.getLoadProfileID_Int());
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

        String item_Str = "Name: " + getLoadNickname_Str() + "\n"
                + "Brand: " + getLoadBrand_Str() + "\n"
                + "Gauge: " + getLoadGauge_Str() + "\n"
                + "Length(inch): " + getLoadLength_Str() + "\n"
                + "Grain: " + getLoadGrain_Str() + "\n"
                + "Notes: " + getLoadNotes_Str();

        return item_Str;
    }

    //*************************************** Other Functions **************************************
    public void removeLoadItemDialog(final Context context, final TrapMasterListArrayAdapter adapter) {
        /*******************************************************************************************
         * Function: removeLoadItemDialog
         *
         * Purpose: Function creates dialog and prompts user to remove a load item
         *
         * Parameters: context (IN) - activity context to display dialog
         *             adapter (IN) - Array adapter to refresh when complete
         *
         * Returns: None
         *
         ******************************************************************************************/

        final String DIALOG_TITLE = "Delete Load";
        final String DIALOG_MSG = "Are you sure you want to delete this load?";

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
        final int finalLoadID_Int = this.loadID_Int;
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
                    // Processed with onClick below

                }
            });
        }

        // Neutral Button, Left
        if (NEUTRAL_BTN) {
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, NEUTRAL_BUTTON_TXT, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Processed with onClick below
                }
            });
        }

        // Negative Button, Middle
        if (NEGATIVE_BTN) {
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, NEGATIVE_BUTTON_TXT, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Processed with onClick below
                }
            });
        }


        new Dialog(context);
        alertDialog.show();

        // Set Button Colors
        if (POSITIVE_BTN) {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(POSITIVE_BTN_COLOR);
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Perform Action on Positive button
                    db.deleteLoadInDB(finalLoadID_Int);

                    alertDialog.dismiss();

                    Toast.makeText(context, "Load deleted.",
                            Toast.LENGTH_LONG).show();

                    // Refresh load listview
                    adapter.refreshLoadArrayAdapter(db.getAllLoadFromDB(getLoadProfileID_Int()));
                }
            });
        }
        if (NEUTRAL_BTN) {
            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(NEUTRAL_BTN_COLOR);
            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Perform Action on NEUTRAL Button

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

    public AlertDialog removeLoadItemDialog(final Context context) {
        /*******************************************************************************************
         * Function: removeLoadItemDialog
         *
         * Purpose: Function creates dialog and prompts user to remove a load item
         *
         * Parameters: context (IN) - activity context to display dialog
         *
         * Returns: alertDialog (OUT) - AlertDialog variable this function creates
         *
         ******************************************************************************************/

        final String DIALOG_TITLE = "Delete Load";
        final String DIALOG_MSG = "Are you sure you want to delete this load?";

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
        final int finalLoadID_Int = this.loadID_Int;
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
                    // Processed with onClick below

                }
            });
        }

        // Neutral Button, Left
        if (NEUTRAL_BTN) {
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, NEUTRAL_BUTTON_TXT, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Processed with onClick below
                }
            });
        }

        // Negative Button, Middle
        if (NEGATIVE_BTN) {
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, NEGATIVE_BUTTON_TXT, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Processed with onClick below
                }
            });
        }


        new Dialog(context);
        alertDialog.show();

        // Set Button Colors
        if (POSITIVE_BTN) {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(POSITIVE_BTN_COLOR);
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Perform Action on Positive button
                    db.deleteLoadInDB(finalLoadID_Int);

                    alertDialog.dismiss();

                    Toast.makeText(context, "Load deleted.",
                            Toast.LENGTH_LONG).show();
                }
            });
        }
        if (NEUTRAL_BTN) {
            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(NEUTRAL_BTN_COLOR);
            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Perform Action on NEUTRAL Button

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

    private void initializeLoadDialogStrings() {
        LOAD_DIALOG_MSG = new ArrayList<String>(Arrays.asList(
                "Enter a nickname for this load.",
                "Enter the brand for this load.",
                "Enter the gauge for this load.",
                "Enter the length for this load.",
                "Enter the grain for this load.",
                "Enter any notes for this load."));
        LOAD_POS_BTN_TXT = new ArrayList<String>(Arrays.asList(
                "NEXT",
                "NEXT",
                "NEXT",
                "NEXT",
                "NEXT",
                "SAVE"));
        LOAD_NEU_BTN_TXT = new ArrayList<String>(Arrays.asList(
                "CANCEL",
                "BACK",
                "BACK",
                "BACK",
                "BACK",
                "BACK",
                "BACK"));
        LOAD_EDT_HINT = new ArrayList<String>(Arrays.asList(
                "i.e. MyLoad",
                "i.e. Remington",
                "i.e. 12",
                "i.e. 3 inch",
                "i.e. 30 grain",
                "i.e. This is a good load!"));
    }

    public void editLoadDialog(final Context context, final TrapMasterListArrayAdapter adapter) {
        /*******************************************************************************************
         * Function: editLoadDialog
         *
         * Purpose: Function creates dialog and prompts user to add or edit a load item, if adding
         *          an item, ID = -1 and email = current user email
         *
         * Parameters: context (IN) - activity context to display dialog
         *             adapter (IN) - Array adapter to refresh when complete
         *
         * Returns: None
         *
         ******************************************************************************************/

        // Dialog Constants
        String DIALOG_TITLE = "Add New Load";;
        int POSITIVE_BTN_COLOR = Color.BLUE;
        int NEUTRAL_BTN_COLOR = Color.RED;

        // Dialog Variables
        GlobalApplicationContext currentContext = new GlobalApplicationContext();
        final DBHandler db = new DBHandler(currentContext.getContext());

        // Pre-Dialog Processing
        if (getLoadID_Int() != -1) {
            DIALOG_TITLE = "Edit Load";
        }
        initializeLoadDialogStrings();

        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Set Dialog Title
        alertDialog.setTitle(DIALOG_TITLE);

        // Set Dialog Message
        alertDialog.setMessage(LOAD_DIALOG_MSG.get(LOAD_DIALOG_STATE));

        // Set view for gathering information
        LinearLayout subView_LnrLay = new LinearLayout(context);
        subView_LnrLay.setOrientation(LinearLayout.VERTICAL);

        // Set view
        final EditText item_Edt = new EditText(context);
        item_Edt.setHint(LOAD_EDT_HINT.get(LOAD_DIALOG_STATE));
        item_Edt.setText(getLoadNickname_Str());
        item_Edt.setGravity(Gravity.START);
        item_Edt.setTextColor(Color.BLACK);
        subView_LnrLay.addView(item_Edt);

        // Add linear layout to alert dialog
        alertDialog.setView(subView_LnrLay);

        // Set Buttons
        // Positive Button, Right
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, LOAD_POS_BTN_TXT.get(LOAD_DIALOG_STATE), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Processed by onClick below
            }
        });

        // Neutral Button, Left
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, LOAD_NEU_BTN_TXT.get(LOAD_DIALOG_STATE), new DialogInterface.OnClickListener() {
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

                switch (LOAD_DIALOG_STATE){
                    case 0:
                        // Nickname to Brand
                        boolean isLoadNicknameEmpty = itemEdt_Str.equals("");
                        boolean isLoadNicknameInDB = db.isLoadNicknameInDB(getLoadProfileID_Int(),
                                itemEdt_Str, getLoadID_Int());

                        if (isLoadNicknameEmpty) {
                            // Check if the load nickname is empty
                            item_Edt.setError(context.getString(R.string.error_field_required));
                            item_Edt.requestFocus();
                        } else if (isLoadNicknameInDB) {
                            // Check if the load nickname is already used in the database, user
                            // cannot have 2 loads with same name
                            item_Edt.setError(context.getString(R.string.error_armory_load_already_exists));
                            item_Edt.requestFocus();
                        } else {
                            setLoadNickname_Str(itemEdt_Str);
                            item_Edt.setText(getLoadBrand_Str());
                            LOAD_DIALOG_STATE = (LOAD_DIALOG_STATE + 1);
                        }
                        break;
                    case 1:
                        // Brand to Gauge
                        setLoadBrand_Str(itemEdt_Str);
                        item_Edt.setText(getLoadGauge_Str());
                        LOAD_DIALOG_STATE = (LOAD_DIALOG_STATE + 1);
                        break;
                    case 2:
                        // Gauge to Length
                        setLoadGauge_Str(itemEdt_Str);
                        item_Edt.setText(getLoadLength_Str());
                        LOAD_DIALOG_STATE = (LOAD_DIALOG_STATE + 1);
                        break;
                    case 3:
                        // Length to Grain
                        setLoadLength_Str(itemEdt_Str);
                        item_Edt.setText(getLoadGrain_Str());
                        LOAD_DIALOG_STATE = (LOAD_DIALOG_STATE + 1);
                        break;
                    case 4:
                        // Grain to Notes
                        setLoadGrain_Str(itemEdt_Str);
                        item_Edt.setText(getLoadNotes_Str());
                        LOAD_DIALOG_STATE = (LOAD_DIALOG_STATE + 1);
                        break;
                    case 5:
                        // Notes to save load and close dialog
                        setLoadNotes_Str(itemEdt_Str);

                        if (getLoadID_Int() == -1) {
                            // User is ADDING a load item
                            db.insertLoadInDB(LoadClass.this);
                        } else {
                            // User is EDITING a load item
                            db.updateLoadInDB(LoadClass.this);
                        }

                        alertDialog.dismiss();

                        Toast.makeText(context, "Load saved!",
                                Toast.LENGTH_LONG).show();

                        // Refresh load listview
                        adapter.refreshLoadArrayAdapter(db.getAllLoadFromDB(getLoadProfileID_Int()));

                        // Reset state counter
                        LOAD_DIALOG_STATE = 0;
                        break;
                }

                alertDialog.setMessage(LOAD_DIALOG_MSG.get(LOAD_DIALOG_STATE));
                item_Edt.setHint(LOAD_EDT_HINT.get(LOAD_DIALOG_STATE));
                pos_Btn.setText(LOAD_POS_BTN_TXT.get(LOAD_DIALOG_STATE));
                neu_Btn.setText(LOAD_NEU_BTN_TXT.get(LOAD_DIALOG_STATE));
            }
        });

        neu_Btn.setTextColor(NEUTRAL_BTN_COLOR);
        neu_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform Action on Neutral button

                switch (LOAD_DIALOG_STATE){
                    case 0:
                        // Nickname to cancel load and close dialog
                        alertDialog.dismiss();
                        break;
                    case 1:
                        // Brand to Nickname
                        item_Edt.setText(getLoadNickname_Str());
                        LOAD_DIALOG_STATE = (LOAD_DIALOG_STATE - 1);
                        break;
                    case 2:
                        // Gauge to Brand
                        item_Edt.setText(getLoadBrand_Str());
                        LOAD_DIALOG_STATE = (LOAD_DIALOG_STATE - 1);
                        break;
                    case 3:
                        // Length to Gauge
                        item_Edt.setText(getLoadGauge_Str());
                        LOAD_DIALOG_STATE = (LOAD_DIALOG_STATE - 1);
                        break;
                    case 4:
                        // Grain to Length
                        item_Edt.setText(getLoadLength_Str());
                        LOAD_DIALOG_STATE = (LOAD_DIALOG_STATE - 1);
                        break;
                    case 5:
                        // Notes to Grain
                        item_Edt.setText(getLoadGrain_Str());
                        LOAD_DIALOG_STATE = (LOAD_DIALOG_STATE - 1);
                        break;
                }

                alertDialog.setMessage(LOAD_DIALOG_MSG.get(LOAD_DIALOG_STATE));
                item_Edt.setHint(LOAD_EDT_HINT.get(LOAD_DIALOG_STATE));
                pos_Btn.setText(LOAD_POS_BTN_TXT.get(LOAD_DIALOG_STATE));
                neu_Btn.setText(LOAD_NEU_BTN_TXT.get(LOAD_DIALOG_STATE));
            }

        });
    }

    public AlertDialog editLoadDialog(final Context context) {
        /*******************************************************************************************
         * Function: editLoadDialog
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
        String DIALOG_TITLE = "Add New Load";;
        int POSITIVE_BTN_COLOR = Color.BLUE;
        int NEUTRAL_BTN_COLOR = Color.RED;

        // Dialog Variables
        GlobalApplicationContext currentContext = new GlobalApplicationContext();
        final DBHandler db = new DBHandler(currentContext.getContext());

        // Pre-Dialog Processing
        if (getLoadID_Int() != -1) {
            DIALOG_TITLE = "Edit Load";
        }
        initializeLoadDialogStrings();

        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Set Dialog Title
        alertDialog.setTitle(DIALOG_TITLE);

        // Set Dialog Message
        alertDialog.setMessage(LOAD_DIALOG_MSG.get(LOAD_DIALOG_STATE));

        // Set view for gathering information
        LinearLayout subView_LnrLay = new LinearLayout(context);
        subView_LnrLay.setOrientation(LinearLayout.VERTICAL);

        // Set view
        final EditText item_Edt = new EditText(context);
        item_Edt.setHint(LOAD_EDT_HINT.get(LOAD_DIALOG_STATE));
        item_Edt.setText(getLoadNickname_Str());
        item_Edt.setGravity(Gravity.START);
        item_Edt.setTextColor(Color.BLACK);
        subView_LnrLay.addView(item_Edt);

        // Add linear layout to alert dialog
        alertDialog.setView(subView_LnrLay);

        // Set Buttons
        // Positive Button, Right
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, LOAD_POS_BTN_TXT.get(LOAD_DIALOG_STATE), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Processed by onClick below
            }
        });

        // Neutral Button, Left
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, LOAD_NEU_BTN_TXT.get(LOAD_DIALOG_STATE), new DialogInterface.OnClickListener() {
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

                switch (LOAD_DIALOG_STATE){
                    case 0:
                        // Nickname to Brand
                        boolean isLoadNicknameEmpty = itemEdt_Str.equals("");
                        boolean isLoadNicknameInDB = db.isLoadNicknameInDB(getLoadProfileID_Int(),
                                itemEdt_Str, getLoadID_Int());

                        if (isLoadNicknameEmpty) {
                            // Check if the load nickname is empty
                            item_Edt.setError(context.getString(R.string.error_field_required));
                            item_Edt.requestFocus();
                        } else if (isLoadNicknameInDB) {
                            // Check if the load nickname is already used in the database, user
                            // cannot have 2 loads with same name
                            item_Edt.setError(context.getString(R.string.error_armory_load_already_exists));
                            item_Edt.requestFocus();
                        } else {
                            setLoadNickname_Str(itemEdt_Str);
                            item_Edt.setText(getLoadBrand_Str());
                            LOAD_DIALOG_STATE = (LOAD_DIALOG_STATE + 1);
                        }
                        break;
                    case 1:
                        // Brand to Gauge
                        setLoadBrand_Str(itemEdt_Str);
                        item_Edt.setText(getLoadGauge_Str());
                        LOAD_DIALOG_STATE = (LOAD_DIALOG_STATE + 1);
                        break;
                    case 2:
                        // Gauge to Length
                        setLoadGauge_Str(itemEdt_Str);
                        item_Edt.setText(getLoadLength_Str());
                        LOAD_DIALOG_STATE = (LOAD_DIALOG_STATE + 1);
                        break;
                    case 3:
                        // Length to Grain
                        setLoadLength_Str(itemEdt_Str);
                        item_Edt.setText(getLoadGrain_Str());
                        LOAD_DIALOG_STATE = (LOAD_DIALOG_STATE + 1);
                        break;
                    case 4:
                        // Grain to Notes
                        setLoadGrain_Str(itemEdt_Str);
                        item_Edt.setText(getLoadNotes_Str());
                        LOAD_DIALOG_STATE = (LOAD_DIALOG_STATE + 1);
                        break;
                    case 5:
                        // Notes to save load and close dialog
                        setLoadNotes_Str(itemEdt_Str);

                        if (getLoadID_Int() == -1) {
                            // User is ADDING a load item
                            db.insertLoadInDB(LoadClass.this);
                        } else {
                            // User is EDITING a load item
                            db.updateLoadInDB(LoadClass.this);
                        }

                        alertDialog.dismiss();

                        Toast.makeText(context, "Load saved!",
                                Toast.LENGTH_LONG).show();

                        // Reset state counter
                        LOAD_DIALOG_STATE = 0;
                        break;
                }

                alertDialog.setMessage(LOAD_DIALOG_MSG.get(LOAD_DIALOG_STATE));
                item_Edt.setHint(LOAD_EDT_HINT.get(LOAD_DIALOG_STATE));
                pos_Btn.setText(LOAD_POS_BTN_TXT.get(LOAD_DIALOG_STATE));
                neu_Btn.setText(LOAD_NEU_BTN_TXT.get(LOAD_DIALOG_STATE));
            }
        });

        neu_Btn.setTextColor(NEUTRAL_BTN_COLOR);
        neu_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform Action on Neutral button

                switch (LOAD_DIALOG_STATE){
                    case 0:
                        // Nickname to cancel load and close dialog
                        alertDialog.dismiss();
                        break;
                    case 1:
                        // Brand to Nickname
                        item_Edt.setText(getLoadNickname_Str());
                        LOAD_DIALOG_STATE = (LOAD_DIALOG_STATE - 1);
                        break;
                    case 2:
                        // Gauge to Brand
                        item_Edt.setText(getLoadBrand_Str());
                        LOAD_DIALOG_STATE = (LOAD_DIALOG_STATE - 1);
                        break;
                    case 3:
                        // Length to Gauge
                        item_Edt.setText(getLoadGauge_Str());
                        LOAD_DIALOG_STATE = (LOAD_DIALOG_STATE - 1);
                        break;
                    case 4:
                        // Grain to Length
                        item_Edt.setText(getLoadLength_Str());
                        LOAD_DIALOG_STATE = (LOAD_DIALOG_STATE - 1);
                        break;
                    case 5:
                        // Notes to Grain
                        item_Edt.setText(getLoadGrain_Str());
                        LOAD_DIALOG_STATE = (LOAD_DIALOG_STATE - 1);
                        break;
                }

                alertDialog.setMessage(LOAD_DIALOG_MSG.get(LOAD_DIALOG_STATE));
                item_Edt.setHint(LOAD_EDT_HINT.get(LOAD_DIALOG_STATE));
                pos_Btn.setText(LOAD_POS_BTN_TXT.get(LOAD_DIALOG_STATE));
                neu_Btn.setText(LOAD_NEU_BTN_TXT.get(LOAD_DIALOG_STATE));
            }

        });

        return alertDialog;
    }

    private CheckboxListArrayAdapter initializeLoadArrayAdapt(ArrayList<LoadClass> dbLoad_List) {
        /*******************************************************************************************
         * Function: initializeLoadArrayAdapt
         *
         * Purpose: Function initializes load listview adapt
         *
         * Parameters: dbLoad_List (IN) - list of loads in DB to create adapter for
         *
         * Returns: tempLoad_Adapt - Array adapter initialized to loads from DB
         *
         ******************************************************************************************/

        // Initialize function variables
        CheckboxListArrayAdapter tempLoad_Adapt;
        ArrayList<String> tempLoadStr_List = new ArrayList<>();
        GlobalApplicationContext currentContext = new GlobalApplicationContext();

        // Iterate over all items and get names
        for (int i = 0; i < dbLoad_List.size(); i++) {
            tempLoadStr_List.add(dbLoad_List.get(i).getLoadNickname_Str());
        }

        // Set array adapter
        tempLoad_Adapt = new CheckboxListArrayAdapter(currentContext.getContext(), tempLoadStr_List);

        return tempLoad_Adapt;
    }

    public AlertDialog chooseGunDialog(final Context context, final int profileID_Int) {
        /*******************************************************************************************
         * Function: chooseGunDialog
         *
         * Purpose: Function creates dialog and prompts user to choose a gun from the database
         *
         * Parameters: context (IN) - Supplies the activity context to display dialog to
         *             profileID_Int (IN) - profile ID for database
         *
         * Returns: The LoadClass variable that called this function will be set to the load chosen,
         *          if none are chosen, the variable will have the loadID = -1
         *          alertDialog (OUT) - Alert dialog created with this function
         *
         ******************************************************************************************/

        // Dialog Constants
        String DIALOG_TITLE = "Load Picker";
        int POSITIVE_BTN_COLOR = Color.BLUE;
        int NEUTRAL_BTN_COLOR = Color.RED;

        // Dialog Variables
        GlobalApplicationContext currentContext = new GlobalApplicationContext();
        final DBHandler db = new DBHandler(currentContext.getContext());
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        final ArrayList<LoadClass> dbLoad_List = db.getAllLoadFromDB(profileID_Int);

        // Set Dialog Title
        alertDialog.setTitle(DIALOG_TITLE);

        // Set Dialog Message
        alertDialog.setMessage("Pick a load! (You can add loads in the Armory)");

        // Set layout for gathering information
        final RelativeLayout subView_RelLay = new RelativeLayout(context);

        // Set views
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final ListView load_List = new ListView(context);
        final CheckboxListArrayAdapter load_Adapt = initializeLoadArrayAdapt(dbLoad_List);
        load_List.setAdapter(load_Adapt);
        load_List.setLayoutParams(params);
        subView_RelLay.addView(load_List);

        // Add linear layout to alert dialog
        alertDialog.setView(subView_RelLay);

        // Set Buttons
        // Positive Button, Right
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Processed by onClick below
            }
        });

        // Neutral Button, Left
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "CANCEL", new DialogInterface.OnClickListener() {
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
                if (load_Adapt.getCount() == 1) {
                    // Gun selected, return that load
                    LoadClass.this.setLoadFromLoad(dbLoad_List.get(load_Adapt.getCheckedItems().get(0)));
                    alertDialog.dismiss();
                } else {
                    // Too few or many guns selected
                    Toast.makeText(context, "Select 1 Load.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        neu_Btn.setTextColor(NEUTRAL_BTN_COLOR);
        neu_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform Action on Neutral button
                // Pick canceled, set ID to -1 and dismiss dialog
                LoadClass.this.setLoadID_Int(-1);
                alertDialog.dismiss();
            }

        });

        return alertDialog;
    }
}
