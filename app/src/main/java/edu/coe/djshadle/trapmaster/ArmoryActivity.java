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

        ArrayList<GunClass> currentGun_List = db.getAllGunFromDB(mCurrentUserEmail_Str);
        ArrayList<String> currentGunStr_List =  new ArrayList<>();

        for (int i = 0; i < currentGun_List.size(); i++) {
            GunClass tempGun = currentGun_List.get(i);
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

        ArrayList<LoadClass> currentLoad_List = db.getAllLoadFromDB(mCurrentUserEmail_Str);
        ArrayList<String> currentLoadStr_List =  new ArrayList<>();

        for (int i = 0; i < currentLoad_List.size(); i++) {
            LoadClass tempLoad = currentLoad_List.get(i);
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
                    // TODO: for editing load list item
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
                    // TODO: for deleting load list item
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

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        final String finalGunName_Str = gunName_Str;

        // Set Dialog Title
        alertDialog.setTitle("Delete gun");

        // Set Dialog Message
        alertDialog.setMessage("Are you sure you want to delete this gun?");


        // Set Buttons
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"DELETE", new DialogInterface.OnClickListener() {
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
        final Button deleteGunItem_Btn = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        LinearLayout.LayoutParams deleteGunItem_Params = (LinearLayout.LayoutParams) deleteGunItem_Btn.getLayoutParams();
        deleteGunItem_Params.gravity = Gravity.FILL_HORIZONTAL;
        deleteGunItem_Btn.setPadding(50, 10, 10, 10);   // Set Position
        deleteGunItem_Btn.setTextColor(Color.BLUE);
        deleteGunItem_Btn.setLayoutParams(deleteGunItem_Params);
        deleteGunItem_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform Action on Delete Button
                db.deleteGunInDB(mCurrentUserEmail_Str, finalGunName_Str);

                alertDialog.dismiss();

                Toast.makeText(ArmoryActivity.this, "Gun deleted.",
                        Toast.LENGTH_LONG).show();

                // Refresh gun listview
                refreshGunListView();
            }
        });

        final Button deleteGunItemCancel_Btn = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams deleteGunItemCancel_Params = (LinearLayout.LayoutParams) deleteGunItemCancel_Btn.getLayoutParams();
        deleteGunItemCancel_Params.gravity = Gravity.FILL_HORIZONTAL;
        deleteGunItemCancel_Btn.setTextColor(Color.RED);
        deleteGunItemCancel_Btn.setLayoutParams(deleteGunItemCancel_Params);
        deleteGunItemCancel_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform Action on Cancel button
                alertDialog.dismiss();
            }
        });
    }

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

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        final String finalLoadName_Str = loadName_Str;

        // Set Dialog Title
        alertDialog.setTitle("Delete Load");

        // Set Dialog Message
        alertDialog.setMessage("Are you sure you want to delete this load?");


        // Set Buttons
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"DELETE", new DialogInterface.OnClickListener() {
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
        final Button deleteLoadItem_Btn = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        LinearLayout.LayoutParams deleteLoadItem_Params = (LinearLayout.LayoutParams) deleteLoadItem_Btn.getLayoutParams();
        deleteLoadItem_Params.gravity = Gravity.FILL_HORIZONTAL;
        deleteLoadItem_Btn.setPadding(50, 10, 10, 10);   // Set Position
        deleteLoadItem_Btn.setTextColor(Color.BLUE);
        deleteLoadItem_Btn.setLayoutParams(deleteLoadItem_Params);
        deleteLoadItem_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform Action on Delete Button
                db.deleteLoadInDB(mCurrentUserEmail_Str, finalLoadName_Str);

                alertDialog.dismiss();

                Toast.makeText(ArmoryActivity.this, "Load deleted.",
                        Toast.LENGTH_LONG).show();

                // Refresh load listview
                refreshLoadListView();
            }
        });

        final Button deleteLoadItemCancel_Btn = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams deleteLoadItemCancel_Params = (LinearLayout.LayoutParams) deleteLoadItemCancel_Btn.getLayoutParams();
        deleteLoadItemCancel_Params.gravity = Gravity.FILL_HORIZONTAL;
        deleteLoadItemCancel_Btn.setTextColor(Color.RED);
        deleteLoadItemCancel_Btn.setLayoutParams(deleteLoadItemCancel_Params);
        deleteLoadItemCancel_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform Action on Cancel button
                alertDialog.dismiss();
            }
        });
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
        TextView title = new TextView(this);
        title.setText("Add New Gun");
        title.setPadding(10, 10, 10, 10);   // Set Position
        title.setGravity(Gravity.START);
        title.setTextColor(Color.BLACK);
        title.setTextSize(20);
        alertDialog.setCustomTitle(title);

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
            title.setText("Edit Gun");
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

                if (newGunItemName_Edt.getText().toString().equals("")) {
                    newGunItemName_Edt.setError(getString(R.string.error_field_required));
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
        TextView title = new TextView(this);
        title.setText("Add New Load");
        title.setPadding(10, 10, 10, 10);   // Set Position
        title.setGravity(Gravity.START);
        title.setTextColor(Color.BLACK);
        title.setTextSize(20);
        alertDialog.setCustomTitle(title);

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
            title.setText("Edit Load");
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

                if (newLoadItemName_Edt.getText().toString().equals("")) {
                    newLoadItemName_Edt.setError(getString(R.string.error_field_required));
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

}
