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
                // Display add gun dialog box for user input
                addGunItemDialog();
            }
        });

        mAddLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Display add load dialog box for user input
                addLoadItemDialog();
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
            currentGunStr_List.add(tempGun.getGunNickname_Str() + " - " + tempGun.getGunModel_Str()
                    + " " + tempGun.getGunGauge_Str());
        }

        if (currentGunStr_List.isEmpty()) {
            currentGunStr_List.add("Click add for a new gun!");
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
            currentLoadStr_List.add(tempLoad.getLoadNickname_Str() + " - " + tempLoad.getLoadBrand_Str()
                    + " " + tempLoad.getLoadGauge_Str());
        }

        if (currentLoadStr_List.isEmpty()) {
            currentLoadStr_List.add("Click add for a new load!");
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
            mGunList_View.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    // TODO: for editing gun list item

                }
            });

            mGunList_View.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    // TODO: for deleting gun list item

                    return false;
                }
            });

        } catch (Exception e){
            Log.d("JRW", "no guns in db for this user");
        }

    }

    private void initializeLoadListView() {
        /*******************************************************************************************
         * Function: initializeGunListView
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
            mLoadList_View.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    // TODO: for editing load list item
                }
            });

            mGunList_View.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    // TODO: for deleting load list item
                    return false;
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

    private void addGunItemDialog() {
        /*******************************************************************************************
         * Function: addGunItemDialog
         *
         * Purpose: Function creates dialog and prompts user to add new gun item
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

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

                    db.insertGunInDB(newGunItem);

                    alertDialog.dismiss();

                    Toast.makeText(ArmoryActivity.this, "New gun saved!",
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

    private void addLoadItemDialog() {
        /*******************************************************************************************
         * Function: addLoadItemDialog
         *
         * Purpose: Function creates dialog and prompts user to add new load item
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

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

                    db.insertLoadInDB(newLoadItem);

                    alertDialog.dismiss();

                    Toast.makeText(ArmoryActivity.this, "New load saved!",
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

}
