/***************************************************************************************************
 * FILENAME : ArmoryActivity.java
 *
 * AUTHOR : Dalton Shadle
 *
 * DESCRIPTION : Holds functions for the Armory activity of this application (holds gun/ammo info)
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class ArmoryActivity extends AppCompatActivity {
    //********************************** Variables and Constants ***********************************
    // General Constants
    private final String DEFAULT_GUN_TEXT = "Click add for a new gun!";
    private final String DEFAULT_LOAD_TEXT = "Click add for a new load!";

    // General Variables
    private String mCurrentUserEmail_Str = "********";
    private DBHandler db;
    private boolean isPortait = true;
    // Gun
    private ArrayAdapter<String> mCurrentGunList_Adapt;
    private ArrayList<GunClass> mUserGun_List;
    private int GUN_DIALOG_STATE = 0;
    private ArrayList<String> GUN_DIALOG_MSG, GUN_POS_BTN_TXT, GUN_NEU_BTN_TXT, GUN_EDT_HINT;
    // Load
    private ArrayAdapter<String> mCurrentLoadList_Adapt;
    private ArrayList<LoadClass> mUserLoad_List;
    private int LOAD_DIALOG_STATE = 0;
    private ArrayList<String> LOAD_DIALOG_MSG, LOAD_POS_BTN_TXT, LOAD_NEU_BTN_TXT, LOAD_EDT_HINT;

    // UI References
    private ListView mGunList_View, mLoadList_View;


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
            setContentView(R.layout.activity_armory_portrait);
            isPortait = true;
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_armory_landscape);
            isPortait = false;
        }

        if (savedInstanceState != null) {

        } else {
            mCurrentUserEmail_Str = getIntent().getStringExtra(getString(R.string.current_user_email));
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
            setContentView(R.layout.activity_armory_portrait);
            isPortait = true;
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_armory_landscape);
            isPortait = false;
        }

        initializeViews();
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

        // Initializing database variable
        db = new DBHandler(this);

        // Initializing buttons
        FloatingActionButton mAddGun = findViewById(R.id.armoryAddGun_Btn);
        FloatingActionButton mAddLoad = findViewById(R.id.armoryAddLoad_Btn);

        mAddGun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Display add gun dialog box for user input, -1 = adding a new gun
                GunClass temp_Gun = new GunClass();
                temp_Gun.setGunID_Int(-1);
                editGunDialog(temp_Gun);
            }
        });

        mAddLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Display add load dialog box for user input, -1 = adding a new load
                LoadClass temp_Load = new LoadClass();
                temp_Load.setLoadID_Int(-1);
                editLoadDialog(temp_Load);
            }
        });


        // Initializing Listviews
        mGunList_View = findViewById(R.id.armoryGun_List);
        mLoadList_View = findViewById(R.id.armoryLoad_List);

        setListViewLayoutParams();

        initializeGunListView();
        initializeLoadListView();

        // Setting title of activity
        setTitle("Armory");
    }

    //************************************* Listview Functions *************************************
    private ArrayList<String> refreshGunList() {
        /*******************************************************************************************
         * Function: refreshGunList
         *
         * Purpose: Function returns the current list of guns for the current user
         *
         * Parameters: None
         *
         * Returns: currentGunStr_List - a string list of guns for current user
         *
         ******************************************************************************************/

        mUserGun_List = db.getAllGunFromDB(mCurrentUserEmail_Str);
        ArrayList<String> currentGunStr_List =  new ArrayList<>();

        for (int i = 0; i < mUserGun_List.size(); i++) {
            GunClass tempGun = mUserGun_List.get(i);
            String gunListItem_Str = tempGun.getGunNickname_Str() + " - " + tempGun.getGunModel_Str()
                    + " " + tempGun.getGunGauge_Str();

            // TEMP TODO: REMOVE TEMP CODE
            gunListItem_Str = tempGun.getGunNickname_Str();

            currentGunStr_List.add(gunListItem_Str);
        }

        if (currentGunStr_List.isEmpty()) {
            currentGunStr_List.add(DEFAULT_GUN_TEXT);
        }

        return currentGunStr_List;
    }

    private ArrayList<String> refreshLoadList() {
        /*******************************************************************************************
         * Function: refreshLoadList
         *
         * Purpose: Function returns the current list of loads for the current user
         *
         * Parameters: None
         *
         * Returns: currentLoadStr_List - a string list of loads for current user
         *
         ******************************************************************************************/

        mUserLoad_List = db.getAllLoadFromDB(mCurrentUserEmail_Str);
        ArrayList<String> currentLoadStr_List =  new ArrayList<>();

        for (int i = 0; i < mUserLoad_List.size(); i++) {
            LoadClass tempLoad = mUserLoad_List.get(i);
            String loadItem_Str = tempLoad.getLoadNickname_Str() + " - " + tempLoad.getLoadBrand_Str()
                    + " " + tempLoad.getLoadGauge_Str();

            // TEMP TODO: REMOVE TEMP CODE
            loadItem_Str = tempLoad.getLoadNickname_Str();

            currentLoadStr_List.add(loadItem_Str);
        }

        if (currentLoadStr_List.isEmpty()) {
            currentLoadStr_List.add(DEFAULT_LOAD_TEXT);
        }

        return currentLoadStr_List;
    }

    private void refreshGunListView() {
        /*******************************************************************************************
         * Function: refreshGunListView
         *
         * Purpose: Function refreshes the gun list view with current gun data from database
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        mCurrentGunList_Adapt.clear();
        mCurrentGunList_Adapt.addAll(refreshGunList());
        mCurrentGunList_Adapt.notifyDataSetChanged();
    }

    private void refreshLoadListView() {
        /*******************************************************************************************
         * Function: refreshLoadListView
         *
         * Purpose: Function refreshes the load list view with current gun data from database
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        mCurrentLoadList_Adapt.clear();
        mCurrentLoadList_Adapt.addAll(refreshLoadList());
        mCurrentLoadList_Adapt.notifyDataSetChanged();
    }

    private void initializeGunListView() {
        /*******************************************************************************************
         * Function: initializeGunListView
         *
         * Purpose: Function initializes the gun list view
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        try {
            ArrayList<String> currentGunStr_List =  refreshGunList();

            mCurrentGunList_Adapt = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, currentGunStr_List);

            mGunList_View.setAdapter(mCurrentGunList_Adapt);
            mGunList_View.setLongClickable(true);

            mGunList_View.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    // TODO: for editing gun list item
                    String gunName_Str = (String) adapterView.getItemAtPosition(i);
                    Log.d("JRW", "OnItemClick for gun is here: " + gunName_Str);

                    if (!isDefaultItemText(gunName_Str)) {
                        GunClass temp_Gun = db.getGunInDB(db.getIDforGun(mCurrentUserEmail_Str, gunName_Str));
                        editGunDialog(temp_Gun);
                    }
                }
            });

            mGunList_View.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    // TODO: for deleting gun list item
                    String gunName_Str = (String) adapterView.getItemAtPosition(i);
                    Log.d("JRW", "OnItemLongClick for gun is here: " + gunName_Str);

                    if (!isDefaultItemText(gunName_Str)) {
                        removeGunItemDialog(gunName_Str);
                    }
                    return true;
                }
            });

        } catch (Exception e){
            Log.d("JRW", "no guns in db for this user");
        }

    }

    private void initializeLoadListView() {
        /*******************************************************************************************
         * Function: initializeLoadListView
         *
         * Purpose: Function initializes the load list view
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        try {
            ArrayList<String> currentLoadStr_List =  refreshLoadList();

            mCurrentLoadList_Adapt = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, currentLoadStr_List);

            mLoadList_View.setAdapter(mCurrentLoadList_Adapt);
            mLoadList_View.setLongClickable(true);

            mLoadList_View.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String loadName_Str = (String) adapterView.getItemAtPosition(i);
                    Log.d("JRW", "OnItemClick for load is here: " + loadName_Str);

                    if (!isDefaultItemText(loadName_Str)) {
                        LoadClass temp_Load = db.getLoadInDB(db.getIDforLoad(mCurrentUserEmail_Str, loadName_Str));
                        editLoadDialog(temp_Load);
                    }

                }
            });

            mLoadList_View.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String loadName_Str = (String) adapterView.getItemAtPosition(i);
                    Log.d("JRW", "OnItemLongClick for load is here: " + loadName_Str);

                    if (!isDefaultItemText(loadName_Str)) {
                        removeLoadItemDialog(loadName_Str);
                    }
                    return true;
                }
            });

        } catch (Exception e){
            Log.d("JRW", "no loads in db for this user");
        }

    }

    private void setListViewLayoutParams() {
        /*******************************************************************************************
         * Function: setListViewLayoutParams
         *
         * Purpose: Function sets the layout parameters for the gun and load list view so that both
         *          use roughly half of the display (with room for buttons and titles)
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        double screenHeight_Dbl = getResources().getDisplayMetrics().heightPixels;
        double scaleFactor_Dbl = 1;

        LinearLayout.LayoutParams params;

        if (isPortait) {
            scaleFactor_Dbl = 3.5;
        } else {
            scaleFactor_Dbl = 2.0;
        }

        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)(screenHeight_Dbl / scaleFactor_Dbl));

        mGunList_View.setLayoutParams(params);
        mLoadList_View.setLayoutParams(params);
    }

    //************************************ Gun Dialog Functions ************************************
    private void removeGunItemDialog(String gunName_Str) {
        /*******************************************************************************************
         * Function: removeGunItemDialog
         *
         * Purpose: Function creates dialog and prompts user to remove a gun item
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

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


        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        final String finalGunName_Str = gunName_Str;

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
                    db.deleteGunInDB(mCurrentUserEmail_Str, finalGunName_Str);

                    alertDialog.dismiss();

                    Toast.makeText(ArmoryActivity.this, "Gun deleted.",
                            Toast.LENGTH_LONG).show();

                    // Refresh gun listview
                    refreshGunListView();
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

    private void editGunDialog(final GunClass gun_Item) {
        /*******************************************************************************************
         * Function: editGunDialog
         *
         * Purpose: Function creates dialog and prompts user to add or edit a gun item
         *
         * Parameters: gun_Item (IN) - object to edit or add, if adding object, gunID = -1
         *
         * Returns: None
         *
         ******************************************************************************************/
        initializeGunDialogStrings();
        gun_Item.setGunEmail_Str(mCurrentUserEmail_Str);

        // Dialog Constants
        String DIALOG_TITLE = "Add New Gun";;

        int POSITIVE_BTN_COLOR = Color.BLUE;
        int NEUTRAL_BTN_COLOR = Color.RED;

        // Pre-Dialog Processing
        if (gun_Item.getGunID_Int() != -1) {
            DIALOG_TITLE = "Edit Gun";
        }

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        // Set Dialog Title
        alertDialog.setTitle(DIALOG_TITLE);

        // Set Dialog Message
        alertDialog.setMessage(GUN_DIALOG_MSG.get(GUN_DIALOG_STATE));

        // Set view for gathering information
        LinearLayout subView_LnrLay = new LinearLayout(this);
        subView_LnrLay.setOrientation(LinearLayout.VERTICAL);

        // Set view
        final EditText item_Edt = new EditText(this);
        item_Edt.setHint(GUN_EDT_HINT.get(GUN_DIALOG_STATE));
        item_Edt.setText(gun_Item.getGunNickname_Str());
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
                String itemEdt_Str = item_Edt.getText().toString();

                switch (GUN_DIALOG_STATE){
                    case 0:
                        // Nickname
                        boolean isGunNicknameEmpty = itemEdt_Str.equals("");
                        boolean isGunNicknameInDB = db.isGunNicknameInDB(mCurrentUserEmail_Str, itemEdt_Str, gun_Item.getGunID_Int());

                        if (isGunNicknameEmpty) {
                            // Check if the gun nickname is empty
                            item_Edt.setError(getString(R.string.error_field_required));
                            item_Edt.requestFocus();
                        } else if (isGunNicknameInDB) {
                            // Check if the gun nickname is already used in the database, uer cannot have 2 guns with same name
                            item_Edt.setError(getString(R.string.error_armory_gun_already_exists));
                            item_Edt.requestFocus();
                        } else {
                            gun_Item.setGunNickname_Str(itemEdt_Str);
                            item_Edt.setText(gun_Item.getGunModel_Str());
                            GUN_DIALOG_STATE = (GUN_DIALOG_STATE + 1);
                        }

                        break;
                    case 1:
                        // Model
                        gun_Item.setGunModel_Str(itemEdt_Str);
                        item_Edt.setText(gun_Item.getGunGauge_Str());
                        GUN_DIALOG_STATE = (GUN_DIALOG_STATE + 1);
                        break;
                    case 2:
                        // Gauge
                        gun_Item.setGunGauge_Str(itemEdt_Str);
                        item_Edt.setText(gun_Item.getGunNotes_Str());
                        GUN_DIALOG_STATE = (GUN_DIALOG_STATE + 1);
                        break;
                    case 3:
                        // Notes
                        gun_Item.setGunNotes_Str(itemEdt_Str);

                        if (gun_Item.getGunID_Int() == -1) {
                            // User is ADDING a gun item
                            db.insertGunInDB(gun_Item);
                        } else {
                            // User is EDITING a gun item
                            db.updateGunInDB(gun_Item, gun_Item.getGunID_Int());
                        }

                        alertDialog.dismiss();

                        Toast.makeText(ArmoryActivity.this, "Gun saved!",
                                Toast.LENGTH_LONG).show();

                        // Refresh gun listview
                        refreshGunListView();

                        // Reset Dialog state
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
                        // Nickname
                        alertDialog.dismiss();
                        break;
                    case 1:
                        // Model
                        item_Edt.setText(gun_Item.getGunNickname_Str());
                        GUN_DIALOG_STATE = (GUN_DIALOG_STATE - 1);
                        break;
                    case 2:
                        // Gauge
                        item_Edt.setText(gun_Item.getGunModel_Str());
                        GUN_DIALOG_STATE = (GUN_DIALOG_STATE - 1);
                        break;
                    case 3:
                        // Notes
                        item_Edt.setText(gun_Item.getGunGauge_Str());
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

    //************************************ Load Dialog Functions ***********************************
    private void removeLoadItemDialog(String loadName_Str) {
        /*******************************************************************************************
         * Function: removeLoadItemDialog
         *
         * Purpose: Function creates dialog and prompts user to remove a load item
         *
         * Parameters: None
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


        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        final String finalLoadName_Str = loadName_Str;


        // Set Dialog Title
        alertDialog.setTitle(DIALOG_TITLE);

        // Set Dialog Message
        alertDialog.setMessage(DIALOG_MSG);

        // Set Buttons
        // Positive Button, Right
        if (POSITIVE_BTN) {
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, POSITIVE_BUTTON_TXT, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Perform Action on Positive button
                    db.deleteLoadInDB(mCurrentUserEmail_Str, finalLoadName_Str);

                    alertDialog.dismiss();

                    Toast.makeText(ArmoryActivity.this, "Load deleted.",
                            Toast.LENGTH_LONG).show();

                    // Refresh load listview
                    refreshLoadListView();
                }
            });
        }

        // Neutral Button, Left
        if (NEUTRAL_BTN) {
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, NEUTRAL_BUTTON_TXT, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Perform Action on NEUTRAL Button
                }
            });
        }

        // Negative Button, Middle
        if (NEGATIVE_BTN) {
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, NEGATIVE_BUTTON_TXT, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Perform Action on Negative button
                    alertDialog.dismiss();
                }
            });
        }


        new Dialog(getApplicationContext());
        alertDialog.show();

        // Set Button Colors
        if (POSITIVE_BTN) {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(POSITIVE_BTN_COLOR);
        }
        if (NEUTRAL_BTN) {
            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(NEUTRAL_BTN_COLOR);
        }
        if (NEGATIVE_BTN) {
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(NEGATIVE_BTN_COLOR);
        }
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

    private void editLoadDialog(final LoadClass load_Item) {
        /*******************************************************************************************
         * Function: editLoadDialog
         *
         * Purpose: Function creates dialog and prompts user to add or edit a load item
         *
         * Parameters: load_Item (IN) - object to edit or add, if adding object, loadID = -1
         *
         * Returns: None
         *
         ******************************************************************************************/
        initializeLoadDialogStrings();
        load_Item.setLoadEmail_Str(mCurrentUserEmail_Str);

        // Dialog Constants
        String DIALOG_TITLE = "Add New Load";;

        int POSITIVE_BTN_COLOR = Color.BLUE;
        int NEUTRAL_BTN_COLOR = Color.RED;

        // Pre-Dialog Processing
        if (load_Item.getLoadID_Int() != -1) {
            DIALOG_TITLE = "Edit Load";
        }

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        // Set Dialog Title
        alertDialog.setTitle(DIALOG_TITLE);

        // Set Dialog Message
        alertDialog.setMessage(LOAD_DIALOG_MSG.get(LOAD_DIALOG_STATE));

        // Set view for gathering information
        LinearLayout subView_LnrLay = new LinearLayout(this);
        subView_LnrLay.setOrientation(LinearLayout.VERTICAL);

        // Set view
        final EditText item_Edt = new EditText(this);
        item_Edt.setHint(LOAD_EDT_HINT.get(LOAD_DIALOG_STATE));
        item_Edt.setText(load_Item.getLoadNickname_Str());
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
                String itemEdt_Str = item_Edt.getText().toString();

                switch (LOAD_DIALOG_STATE){
                    case 0:
                        // Nickname
                        boolean isLoadNicknameEmpty = itemEdt_Str.equals("");
                        boolean isLoadNicknameInDB = db.isLoadNicknameInDB(mCurrentUserEmail_Str,
                                itemEdt_Str, load_Item.getLoadID_Int());

                        if (isLoadNicknameEmpty) {
                            // Check if the load nickname is empty
                            item_Edt.setError(getString(R.string.error_field_required));
                            item_Edt.requestFocus();
                        } else if (isLoadNicknameInDB) {
                            // Check if the load nickname is already used in the database, user
                            // cannot have 2 loads with same name
                            item_Edt.setError(getString(R.string.error_armory_load_already_exists));
                            item_Edt.requestFocus();
                        } else {
                            load_Item.setLoadNickname_Str(itemEdt_Str);
                            item_Edt.setText(load_Item.getLoadBrand_Str());
                            LOAD_DIALOG_STATE = (LOAD_DIALOG_STATE + 1);
                        }
                        break;
                    case 1:
                        // Brand
                        load_Item.setLoadBrand_Str(itemEdt_Str);
                        item_Edt.setText(load_Item.getLoadGauge_Str());
                        LOAD_DIALOG_STATE = (LOAD_DIALOG_STATE + 1);
                        break;
                    case 2:
                        // Gauge
                        load_Item.setLoadGauge_Str(itemEdt_Str);
                        item_Edt.setText(load_Item.getLoadLength_Str());
                        LOAD_DIALOG_STATE = (LOAD_DIALOG_STATE + 1);
                        break;
                    case 3:
                        // Length
                        load_Item.setLoadLength_Str(itemEdt_Str);
                        item_Edt.setText(load_Item.getLoadGrain_Str());
                        LOAD_DIALOG_STATE = (LOAD_DIALOG_STATE + 1);
                        break;
                    case 4:
                        //Grain
                        load_Item.setLoadGrain_Str(itemEdt_Str);
                        item_Edt.setText(load_Item.getLoadNotes_Str());
                        LOAD_DIALOG_STATE = (LOAD_DIALOG_STATE + 1);
                        break;
                    case 5:
                        // Notes
                        load_Item.setLoadNotes_Str(itemEdt_Str);

                        if (load_Item.getLoadID_Int() == -1) {
                            // User is ADDING a load item
                            db.insertLoadInDB(load_Item);
                        } else {
                            // User is EDITING a load item
                            db.updateLoadInDB(load_Item, load_Item.getLoadID_Int());
                        }

                        alertDialog.dismiss();

                        Toast.makeText(ArmoryActivity.this, "Load saved!",
                                Toast.LENGTH_LONG).show();

                        // Refresh load listview
                        refreshLoadListView();

                        // Reset Dialog state
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
                        // Nickname
                        alertDialog.dismiss();
                        break;
                    case 1:
                        // Brand
                        item_Edt.setText(load_Item.getLoadNickname_Str());
                        LOAD_DIALOG_STATE = (LOAD_DIALOG_STATE - 1);
                        break;
                    case 2:
                        // Gauge
                        item_Edt.setText(load_Item.getLoadBrand_Str());
                        LOAD_DIALOG_STATE = (LOAD_DIALOG_STATE - 1);
                        break;
                    case 3:
                        // Length
                        item_Edt.setText(load_Item.getLoadGauge_Str());
                        LOAD_DIALOG_STATE = (LOAD_DIALOG_STATE - 1);
                        break;
                    case 4:
                        // Grain
                        item_Edt.setText(load_Item.getLoadLength_Str());
                        LOAD_DIALOG_STATE = (LOAD_DIALOG_STATE - 1);
                        break;
                    case 5:
                        // Notes
                        item_Edt.setText(load_Item.getLoadGrain_Str());
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

    //************************************** Other Functions ***************************************
    private boolean isDefaultItemText(String item_Str) {
        return (item_Str.contains(DEFAULT_GUN_TEXT) || item_Str.contains(DEFAULT_LOAD_TEXT));
    }
    
}
