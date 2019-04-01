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

public class ArmoryActivity extends AppCompatActivity {
    //********************************** Variables and Constants ***********************************
    // General Constants
    private final String DEFAULT_GUN_TEXT = "Click add for a new gun!";
    private final String DEFAULT_LOAD_TEXT = "Click add for a new load!";

    // General Variables
    private String mCurrentUserEmail_Str = "********";
    private DBHandler db;
    private ArrayAdapter<String> mCurrentGunList_Adapt;
    private ArrayAdapter<String> mCurrentLoadList_Adapt;
    private ArrayList<GunClass> mUserGun_List;
    private ArrayList<LoadClass> mUserLoad_List;
    private boolean isPortait = true;

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
                editGunItemDialog(-1);
                //GunClass temp_Gun = new GunClass();
                //temp_Gun.setGunID_Int(-1);
                //editGunNicknameDialog(temp_Gun);
            }
        });

        mAddLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Display add load dialog box for user input, -1 = adding a new load
                editLoadItemDialog(-1);
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
                        //GunClass temp_Gun = db.getGunInDB(db.getIDforGun(mCurrentUserEmail_Str, gunName_Str));
                        //editGunNicknameDialog(temp_Gun);
                        editGunItemDialog(db.getIDforGun(mCurrentUserEmail_Str, gunName_Str));
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
                        editLoadItemDialog(db.getIDforLoad(mCurrentUserEmail_Str, loadName_Str));
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

    private void editGunItemDialog(final int gunID_Int) {
        /*******************************************************************************************
         * Function: editGunItemDialog
         *
         * Purpose: Function creates dialog and prompts user to add new or edit gun item. Function
         *          for editing, gunID_Int = a positive number. Function for adding, gunID_Int = -1
         *
         * Parameters: gunID_Int (IN) - Database ID of the gun item selected, if adding a new gun,
         *                              set gunID_Int to -1
         *
         * Returns: None
         *
         ******************************************************************************************/

        // Set variables for this function
        GunClass gunFromDB;
        final boolean addNewGun = (gunID_Int == -1);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        // Set Dialog Title
        alertDialog.setTitle("Add New Gun");

        // Set all edittext views for gathering gun information
        LinearLayout newGunItemTxt_LnrLay = new LinearLayout(this);
        newGunItemTxt_LnrLay.setOrientation(LinearLayout.VERTICAL);

        // Set Gun Name EditText
        final EditText newGunItemName_Edt = new EditText(this);
        newGunItemName_Edt.setHint("Gun Nickname");
        newGunItemName_Edt.setGravity(Gravity.START);
        newGunItemName_Edt.setTextColor(Color.BLACK);
        newGunItemTxt_LnrLay.addView(newGunItemName_Edt);

        // Set Gun Model EditText
        final EditText newGunItemModel_Edt = new EditText(this);
        newGunItemModel_Edt.setHint("Gun Model");
        newGunItemModel_Edt.setGravity(Gravity.START);
        newGunItemModel_Edt.setTextColor(Color.BLACK);
        newGunItemTxt_LnrLay.addView(newGunItemModel_Edt);

        // Set Gun Gauge EditText
        final EditText newGunItemGauge_Edt = new EditText(this);
        newGunItemGauge_Edt.setHint("Gun Gauge");
        newGunItemGauge_Edt.setGravity(Gravity.START);
        newGunItemGauge_Edt.setTextColor(Color.BLACK);
        newGunItemTxt_LnrLay.addView(newGunItemGauge_Edt);

        // Set Gun Notes EditText
        final EditText newGunItemNotes_Edt = new EditText(this);
        newGunItemNotes_Edt.setHint("Gun Notes");
        newGunItemNotes_Edt.setGravity(Gravity.START);
        newGunItemNotes_Edt.setTextColor(Color.BLACK);
        newGunItemTxt_LnrLay.addView(newGunItemNotes_Edt);

        if (!addNewGun) {
            // User is EDITING a gun item
            gunFromDB = db.getGunInDB(gunID_Int);
            alertDialog.setTitle("Edit Gun");
            newGunItemName_Edt.setText(gunFromDB.getGunNickname_Str());
            newGunItemModel_Edt.setText(gunFromDB.getGunModel_Str());
            newGunItemGauge_Edt.setText(gunFromDB.getGunGauge_Str());
            newGunItemNotes_Edt.setText(gunFromDB.getGunNotes_Str());
        }

        // Add linear layout to alert dialog
        alertDialog.setView(newGunItemTxt_LnrLay);

        // Set Buttons
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"SAVE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Overwritten by on click listener below
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Overwritten by on click listener below
            }
        });

        new Dialog(getApplicationContext());
        alertDialog.show();

        // Set Properties for Save Button
        final Button newGunItemSave_Btn = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        LinearLayout.LayoutParams newGunItemSave_Params = (LinearLayout.LayoutParams) newGunItemSave_Btn.getLayoutParams();
        newGunItemSave_Params.gravity = Gravity.FILL_HORIZONTAL;
        newGunItemSave_Btn.setPadding(50, 10, 10, 10);   // Set Position
        newGunItemSave_Btn.setTextColor(Color.BLUE);
        newGunItemSave_Btn.setLayoutParams(newGunItemSave_Params);
        newGunItemSave_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform Action on SAVE Button
                boolean isGunNicknameEmpty = newGunItemName_Edt.getText().toString().equals("");
                boolean isGunNicknameInDB = db.isGunNicknameInDB(mCurrentUserEmail_Str, newGunItemName_Edt.getText().toString(), gunID_Int);

                if (isGunNicknameEmpty) {
                    // Check if the gun nickname is empty
                    newGunItemName_Edt.setError(getString(R.string.error_field_required));
                    newGunItemName_Edt.requestFocus();
                } else if (isGunNicknameInDB) {
                    // Check if the gun nickname is already used in the database, uer cannot have 2 guns with same name
                    newGunItemName_Edt.setError(getString(R.string.error_armory_gun_already_exists));
                    newGunItemName_Edt.requestFocus();
                } else {
                    GunClass newGunItem = new GunClass();

                    newGunItem.setGunEmail_Str(mCurrentUserEmail_Str);
                    newGunItem.setGunNickname_Str(newGunItemName_Edt.getText().toString());
                    newGunItem.setGunModel_Str(newGunItemModel_Edt.getText().toString());
                    newGunItem.setGunGauge_Str(newGunItemGauge_Edt.getText().toString());
                    newGunItem.setGunNotes_Str(newGunItemNotes_Edt.getText().toString());

                    if (addNewGun) {
                        // User is ADDING a gun item
                        db.insertGunInDB(newGunItem);
                    } else {
                        // User is EDITING a gun item
                        db.updateGunInDB(newGunItem, gunID_Int);
                    }

                    alertDialog.dismiss();

                    Toast.makeText(ArmoryActivity.this, "Gun saved!",
                            Toast.LENGTH_LONG).show();

                    // Refresh gun listview
                    refreshGunListView();
                }
            }
        });

        final Button newGunItemCancel_Btn = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams newGunItemCancel_Params = (LinearLayout.LayoutParams) newGunItemCancel_Btn.getLayoutParams();
        newGunItemCancel_Params.gravity = Gravity.FILL_HORIZONTAL;
        newGunItemCancel_Btn.setTextColor(Color.RED);
        newGunItemCancel_Btn.setLayoutParams(newGunItemCancel_Params);
        newGunItemCancel_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform Action on Cancel button
                alertDialog.dismiss();
            }
        });
    }

    // These next four functions are temporary. I might try to use them for an
    // iterative dialog to gather info one by one.
    private void editGunNicknameDialog(final GunClass gun_Item) {
        /*******************************************************************************************
         * Function: editGunNicknameDialog
         *
         * Purpose: Function creates dialog and prompts user to enter gun nickname, if adding a gun,
         *          set gun_Item ID = -1
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/
        // Pre-Dialog Processing
        String dialogTitle_Str = "Add New Gun";
        String edittext_Str = gun_Item.getGunNickname_Str();
        final int gunID_Int = gun_Item.getGunID_Int();

        if (gunID_Int != -1) {
            dialogTitle_Str = "Edit Gun";
            edittext_Str = db.getGunInDB(gunID_Int).getGunNickname_Str();
        }

        // Dialog Constants
        final String DIALOG_TITLE = dialogTitle_Str;
        final String DIALOG_MSG = "Enter the nickname of the gun.";

        final boolean POSITIVE_BTN = true;  // Right
        final boolean NEUTRAL_BTN = true;   // Left
        final boolean NEGATIVE_BTN = false;  // Middle

        final String POSITIVE_BUTTON_TXT = "NEXT";     // Right
        final String NEUTRAL_BUTTON_TXT = "CANCEL";   // Left
        final String NEGATIVE_BUTTON_TXT = "CANCEL";  // Middle

        final int POSITIVE_BTN_COLOR = Color.BLUE;
        final int NEUTRAL_BTN_COLOR = Color.RED;
        final int NEGATIVE_BTN_COLOR = Color.RED;

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        // Set Dialog Title
        alertDialog.setTitle(DIALOG_TITLE);

        // Set Dialog Message
        alertDialog.setMessage(DIALOG_MSG);

        // Set view for gathering information
        LinearLayout subView_LnrLay = new LinearLayout(this);
        subView_LnrLay.setOrientation(LinearLayout.VERTICAL);

        // Set view
        final EditText item_Edt = new EditText(this);
        item_Edt.setHint("Gun Nickname");
        item_Edt.setText(edittext_Str);
        item_Edt.setGravity(Gravity.START);
        item_Edt.setTextColor(Color.BLACK);
        subView_LnrLay.addView(item_Edt);

        // Add linear layout to alert dialog
        alertDialog.setView(subView_LnrLay);

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
                    boolean isGunNicknameEmpty = item_Edt.getText().toString().equals("");
                    boolean isGunNicknameInDB = db.isGunNicknameInDB(mCurrentUserEmail_Str, item_Edt.getText().toString(), gunID_Int);

                    if (isGunNicknameEmpty) {
                        // Check if the gun nickname is empty
                        item_Edt.setError(getString(R.string.error_field_required));
                        item_Edt.requestFocus();
                    } else if (isGunNicknameInDB) {
                        // Check if the gun nickname is already used in the database, uer cannot have 2 guns with same name
                        item_Edt.setError(getString(R.string.error_armory_gun_already_exists));
                        item_Edt.requestFocus();
                    } else {
                        // All is good, continue with the dialog
                        GunClass tempGun = gun_Item;
                        tempGun.setGunNickname_Str(item_Edt.getText().toString());
                        editGunModelDialog(tempGun);
                        alertDialog.dismiss();
                    }
                }
            });
        }
        if (NEUTRAL_BTN) {
            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(NEUTRAL_BTN_COLOR);
            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Perform Action on Neutral button
                    alertDialog.dismiss();
                }
            });
        }
        if (NEGATIVE_BTN) {
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(NEGATIVE_BTN_COLOR);
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Perfrom Action on Negative button
                }
            });
        }
    }

    private void editGunModelDialog(final GunClass gun_Item) {
        /*******************************************************************************************
         * Function: editGunModelDialog
         *
         * Purpose: Function creates dialog and prompts user to enter gun nickname, if adding a gun,
         *          set gun_Item ID = -1
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/
        // Pre-Dialog Processing
        String dialogTitle_Str = "Add New Gun";
        String edittext_Str = gun_Item.getGunModel_Str();
        final int gunID_Int = gun_Item.getGunID_Int();

        if (gunID_Int != -1) {
            dialogTitle_Str = "Edit Gun";
            edittext_Str = db.getGunInDB(gunID_Int).getGunModel_Str();
        }

        // Dialog Constants
        final String DIALOG_TITLE = dialogTitle_Str;
        final String DIALOG_MSG = "Enter the model of the gun.";

        final boolean POSITIVE_BTN = true;  // Right
        final boolean NEUTRAL_BTN = true;   // Left
        final boolean NEGATIVE_BTN = false;  // Middle

        final String POSITIVE_BUTTON_TXT = "NEXT";     // Right
        final String NEUTRAL_BUTTON_TXT = "BACK";   // Left
        final String NEGATIVE_BUTTON_TXT = "CANCEL";  // Middle

        final int POSITIVE_BTN_COLOR = Color.BLUE;
        final int NEUTRAL_BTN_COLOR = Color.RED;
        final int NEGATIVE_BTN_COLOR = Color.RED;

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        // Set Dialog Title
        alertDialog.setTitle(DIALOG_TITLE);

        // Set Dialog Message
        alertDialog.setMessage(DIALOG_MSG);

        // Set view for gathering information
        LinearLayout subView_LnrLay = new LinearLayout(this);
        subView_LnrLay.setOrientation(LinearLayout.VERTICAL);

        // Set view
        final EditText item_Edt = new EditText(this);
        item_Edt.setHint("Gun Model");
        item_Edt.setText(edittext_Str);
        item_Edt.setGravity(Gravity.START);
        item_Edt.setTextColor(Color.BLACK);
        subView_LnrLay.addView(item_Edt);

        // Add linear layout to alert dialog
        alertDialog.setView(subView_LnrLay);

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
                    GunClass tempGun = gun_Item;
                    tempGun.setGunModel_Str(item_Edt.getText().toString());
                    editGunGaugeDialog(tempGun);
                    alertDialog.dismiss();
                }
            });
        }
        if (NEUTRAL_BTN) {
            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(NEUTRAL_BTN_COLOR);
            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Perform Action on Neutral button
                    editGunNicknameDialog(gun_Item);
                    alertDialog.dismiss();
                }
            });
        }
        if (NEGATIVE_BTN) {
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(NEGATIVE_BTN_COLOR);
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Perform Action on Negative button
                }
            });
        }
    }

    private void editGunGaugeDialog(final GunClass gun_Item) {
        /*******************************************************************************************
         * Function: editGunGaugeDialog
         *
         * Purpose: Function creates dialog and prompts user to enter gun nickname, if adding a gun,
         *          set gun_Item ID = -1
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/
        // Pre-Dialog Processing
        String dialogTitle_Str = "Add New Gun";
        String edittext_Str = gun_Item.getGunGauge_Str();
        final int gunID_Int = gun_Item.getGunID_Int();

        if (gunID_Int != -1) {
            dialogTitle_Str = "Edit Gun";
            edittext_Str = db.getGunInDB(gunID_Int).getGunGauge_Str();
        }

        // Dialog Constants
        final String DIALOG_TITLE = dialogTitle_Str;
        final String DIALOG_MSG = "Enter the gauge of the gun.";

        final boolean POSITIVE_BTN = true;  // Right
        final boolean NEUTRAL_BTN = true;   // Left
        final boolean NEGATIVE_BTN = false;  // Middle

        final String POSITIVE_BUTTON_TXT = "NEXT";     // Right
        final String NEUTRAL_BUTTON_TXT = "BACK";   // Left
        final String NEGATIVE_BUTTON_TXT = "CANCEL";  // Middle

        final int POSITIVE_BTN_COLOR = Color.BLUE;
        final int NEUTRAL_BTN_COLOR = Color.RED;
        final int NEGATIVE_BTN_COLOR = Color.RED;

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        // Set Dialog Title
        alertDialog.setTitle(DIALOG_TITLE);

        // Set Dialog Message
        alertDialog.setMessage(DIALOG_MSG);

        // Set view for gathering information
        LinearLayout subView_LnrLay = new LinearLayout(this);
        subView_LnrLay.setOrientation(LinearLayout.VERTICAL);

        // Set view
        final EditText item_Edt = new EditText(this);
        item_Edt.setHint("Gun Gauge");
        item_Edt.setText(edittext_Str);
        item_Edt.setGravity(Gravity.START);
        item_Edt.setTextColor(Color.BLACK);
        subView_LnrLay.addView(item_Edt);

        // Add linear layout to alert dialog
        alertDialog.setView(subView_LnrLay);

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
                    GunClass tempGun = gun_Item;
                    tempGun.setGunGauge_Str(item_Edt.getText().toString());
                    editGunNotesDialog(tempGun);
                    alertDialog.dismiss();
                }
            });
        }
        if (NEUTRAL_BTN) {
            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(NEUTRAL_BTN_COLOR);
            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Perform Action on Neutral button
                    editGunModelDialog(gun_Item);
                    alertDialog.dismiss();
                }
            });
        }
        if (NEGATIVE_BTN) {
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(NEGATIVE_BTN_COLOR);
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Perform Action on Negative button
                }
            });
        }
    }

    private void editGunNotesDialog(final GunClass gun_Item) {
        /*******************************************************************************************
         * Function: editGunGaugeDialog
         *
         * Purpose: Function creates dialog and prompts user to enter gun nickname, if adding a gun,
         *          set gun_Item ID = -1
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/
        // Pre-Dialog Processing
        String dialogTitle_Str = "Add New Gun";
        String edittext_Str = gun_Item.getGunNotes_Str();
        final int gunID_Int = gun_Item.getGunID_Int();

        if (gunID_Int != -1) {
            dialogTitle_Str = "Edit Gun";
            edittext_Str = db.getGunInDB(gunID_Int).getGunNotes_Str();
        }

        // Dialog Constants
        final String DIALOG_TITLE = dialogTitle_Str;
        final String DIALOG_MSG = "Enter the notes on the gun.";

        final boolean POSITIVE_BTN = true;  // Right
        final boolean NEUTRAL_BTN = true;   // Left
        final boolean NEGATIVE_BTN = false;  // Middle

        final String POSITIVE_BUTTON_TXT = "SAVE";     // Right
        final String NEUTRAL_BUTTON_TXT = "BACK";   // Left
        final String NEGATIVE_BUTTON_TXT = "CANCEL";  // Middle

        final int POSITIVE_BTN_COLOR = Color.BLUE;
        final int NEUTRAL_BTN_COLOR = Color.RED;
        final int NEGATIVE_BTN_COLOR = Color.RED;

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        // Set Dialog Title
        alertDialog.setTitle(DIALOG_TITLE);

        // Set Dialog Message
        alertDialog.setMessage(DIALOG_MSG);

        // Set view for gathering information
        LinearLayout subView_LnrLay = new LinearLayout(this);
        subView_LnrLay.setOrientation(LinearLayout.VERTICAL);

        // Set view
        final EditText item_Edt = new EditText(this);
        item_Edt.setHint("Gun Notes");
        item_Edt.setText(edittext_Str);
        item_Edt.setGravity(Gravity.START);
        item_Edt.setTextColor(Color.BLACK);
        subView_LnrLay.addView(item_Edt);

        // Add linear layout to alert dialog
        alertDialog.setView(subView_LnrLay);

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
                    GunClass tempGun = gun_Item;
                    tempGun.setGunNotes_Str(item_Edt.getText().toString());
                    tempGun.setGunEmail_Str(mCurrentUserEmail_Str);

                    if (gunID_Int == -1) {
                        // new gun
                        Log.d("JRW", "New Gun");
                        db.insertGunInDB(tempGun);
                    } else {
                        // editting gun
                        Log.d("JRW", "Edit Gun");
                        db.updateGunInDB(tempGun, gunID_Int);
                    }

                    Toast.makeText(ArmoryActivity.this, "Gun saved!",
                            Toast.LENGTH_LONG).show();

                    alertDialog.dismiss();

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
                    editGunGaugeDialog(gun_Item);
                    alertDialog.dismiss();
                }
            });
        }
        if (NEGATIVE_BTN) {
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(NEGATIVE_BTN_COLOR);
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Perform Action on Negative button
                }
            });
        }
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

    private void editLoadItemDialog(final int loadID_Int) {
        /*******************************************************************************************
         * Function: editLoadItemDialog
         *
         * Purpose: Function creates dialog and prompts user to add new or edit load item. Function
         *          for editing, loadID_Int = a positive number. Function for adding, loadID_Int = -1
         *
         * Parameters: loadID_Int (IN) - Database ID of the load item selected, if adding a new load,
         *                               set loadID_Int to -1
         *
         * Returns: None
         *
         ******************************************************************************************/

        // Set variables for this function
        LoadClass loadFromDB;
        final boolean addNewLoad = (loadID_Int == -1);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        // Set Dialog Title
        alertDialog.setTitle("Add New Load");

        // Set all edittext views for gathering load information
        LinearLayout newLoadItemTxt_LnrLay = new LinearLayout(this);
        newLoadItemTxt_LnrLay.setOrientation(LinearLayout.VERTICAL);

        // Set Load Name EditText
        final EditText newLoadItemName_Edt = new EditText(this);
        newLoadItemName_Edt.setHint("Load Nickname");
        newLoadItemName_Edt.setGravity(Gravity.START);
        newLoadItemName_Edt.setTextColor(Color.BLACK);
        newLoadItemTxt_LnrLay.addView(newLoadItemName_Edt);

        // Set Load Brand EditText
        final EditText newLoadItemBrand_Edt = new EditText(this);
        newLoadItemBrand_Edt.setHint("Load Brand");
        newLoadItemBrand_Edt.setGravity(Gravity.START);
        newLoadItemBrand_Edt.setTextColor(Color.BLACK);
        newLoadItemTxt_LnrLay.addView(newLoadItemBrand_Edt);

        // Set Load Gauge EditText
        final EditText newLoadItemGauge_Edt = new EditText(this);
        newLoadItemGauge_Edt.setHint("Load Gauge");
        newLoadItemGauge_Edt.setGravity(Gravity.START);
        newLoadItemGauge_Edt.setTextColor(Color.BLACK);
        newLoadItemTxt_LnrLay.addView(newLoadItemGauge_Edt);

        // Set Load Length EditText
        final EditText newLoadItemLength_Edt = new EditText(this);
        newLoadItemLength_Edt.setHint("Load Length");
        newLoadItemLength_Edt.setGravity(Gravity.START);
        newLoadItemLength_Edt.setTextColor(Color.BLACK);
        newLoadItemTxt_LnrLay.addView(newLoadItemLength_Edt);

        // Set Load Grain EditText
        final EditText newLoadItemGrain_Edt = new EditText(this);
        newLoadItemGrain_Edt.setHint("Load Grain");
        newLoadItemGrain_Edt.setGravity(Gravity.START);
        newLoadItemGrain_Edt.setTextColor(Color.BLACK);
        newLoadItemTxt_LnrLay.addView(newLoadItemGrain_Edt);

        // Set Load Notes EditText
        final EditText newLoadItemNotes_Edt = new EditText(this);
        newLoadItemNotes_Edt.setHint("Load Notes");
        newLoadItemNotes_Edt.setGravity(Gravity.START);
        newLoadItemNotes_Edt.setTextColor(Color.BLACK);
        newLoadItemTxt_LnrLay.addView(newLoadItemNotes_Edt);

        if (!addNewLoad) {
            // User is EDITING a gun item
            loadFromDB = db.getLoadInDB(loadID_Int);
            alertDialog.setTitle("Edit Load");
            newLoadItemName_Edt.setText(loadFromDB.getLoadNickname_Str());
            newLoadItemBrand_Edt.setText(loadFromDB.getLoadBrand_Str());
            newLoadItemGauge_Edt.setText(loadFromDB.getLoadGauge_Str());
            newLoadItemLength_Edt.setText(loadFromDB.getLoadLength_Str());
            newLoadItemGrain_Edt.setText(loadFromDB.getLoadGrain_Str());
            newLoadItemNotes_Edt.setText(loadFromDB.getLoadNotes_Str());
        }

        // Add linear layout to alert dialog
        alertDialog.setView(newLoadItemTxt_LnrLay);

        // Set Buttons
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"SAVE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Overwritten by on click listener below
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Overwritten by on click listener below
            }
        });

        new Dialog(getApplicationContext());
        alertDialog.show();

        // Set Properties for Save Button
        final Button newLoadItemSave_Btn = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        LinearLayout.LayoutParams newGunItemSave_Params = (LinearLayout.LayoutParams) newLoadItemSave_Btn.getLayoutParams();
        newGunItemSave_Params.gravity = Gravity.FILL_HORIZONTAL;
        newLoadItemSave_Btn.setPadding(50, 10, 10, 10);   // Set Position
        newLoadItemSave_Btn.setTextColor(Color.BLUE);
        newLoadItemSave_Btn.setLayoutParams(newGunItemSave_Params);
        newLoadItemSave_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform Action on SAVE Button
                boolean isLoadNicknameEmpty = newLoadItemName_Edt.getText().toString().equals("");
                boolean isLoadNicknameInDB = db.isLoadNicknameInDB(mCurrentUserEmail_Str, newLoadItemName_Edt.getText().toString(), loadID_Int);

                if (isLoadNicknameEmpty) {
                    // Check if the load nickname is empty
                    newLoadItemName_Edt.setError(getString(R.string.error_field_required));
                    newLoadItemName_Edt.requestFocus();
                } else if (isLoadNicknameInDB) {
                    // Check if the load nickname is already used in the database, uer cannot have 2 loads with same name
                    newLoadItemName_Edt.setError(getString(R.string.error_armory_load_already_exists));
                    newLoadItemName_Edt.requestFocus();
                } else {
                    LoadClass newLoadItem = new LoadClass();

                    newLoadItem.setLoadEmail_Str(mCurrentUserEmail_Str);
                    newLoadItem.setLoadNickname_Str(newLoadItemName_Edt.getText().toString());
                    newLoadItem.setLoadBrand_Str(newLoadItemBrand_Edt.getText().toString());
                    newLoadItem.setLoadGauge_Str(newLoadItemGauge_Edt.getText().toString());
                    newLoadItem.setLoadLength_Str(newLoadItemLength_Edt.getText().toString());
                    newLoadItem.setLoadGrain_Str(newLoadItemGrain_Edt.getText().toString());
                    newLoadItem.setLoadNotes_Str(newLoadItemNotes_Edt.getText().toString());

                    if (addNewLoad) {
                        // User is ADDING a load item
                        db.insertLoadInDB(newLoadItem);
                    } else {
                        // User is EDITING a load item
                        db.updateLoadInDB(newLoadItem, loadID_Int);
                    }

                    alertDialog.dismiss();

                    Toast.makeText(ArmoryActivity.this, "Load saved!",
                            Toast.LENGTH_LONG).show();

                    // Refresh load listview
                    refreshLoadListView();
                }
            }
        });

        final Button newLoadItemCancel_Btn = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams newGunItemCancel_Params = (LinearLayout.LayoutParams) newLoadItemCancel_Btn.getLayoutParams();
        newGunItemCancel_Params.gravity = Gravity.FILL_HORIZONTAL;
        newLoadItemCancel_Btn.setTextColor(Color.RED);
        newLoadItemCancel_Btn.setLayoutParams(newGunItemCancel_Params);
        newLoadItemCancel_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform Action on Cancel button
                alertDialog.dismiss();
            }
        });
    }

    private boolean isDefaultItemText(String item_Str) {
        return (item_Str.contains(DEFAULT_GUN_TEXT) || item_Str.contains(DEFAULT_LOAD_TEXT));
    }

    //************************************** Other Functions ***************************************

    private void DEFAULT_DIALOG() {
        /*******************************************************************************************
         * Function: DEFAULT_DIALOG
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
        final boolean NEUTRAL_BTN = true;   // Left
        final boolean NEGATIVE_BTN = true;  // Middle

        final String POSITIVE_BUTTON_TXT = "POS";     // Right
        final String NEUTRAL_BUTTON_TXT = "DELETE";   // Left
        final String NEGATIVE_BUTTON_TXT = "CANCEL";  // Middle

        final int POSITIVE_BTN_COLOR = Color.RED;
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
                    // Perform Action on Positive button

                }
            });
        }

        // Neutral Button, Left
        if (NEGATIVE_BTN) {
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
}
