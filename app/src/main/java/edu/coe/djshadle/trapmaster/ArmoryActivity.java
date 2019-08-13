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
import android.support.design.widget.TabLayout;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class ArmoryActivity extends AppCompatActivity {
    //***************************************** Constants ******************************************
    // General Constants
    private String TAG = "JRW";
    private String ACTIVITY_TITLE;
    private final int GUN_POS = 0;
    private final int LOAD_POS = 1;

    // Key Constants
    private String CURRENT_USER_KEY;

    // Tag Constants
    private final int GUN_LIST_TAG = 3;
    private final int LOAD_LIST_TAG = 4;

    //***************************************** Variables ******************************************
    // General Variables
    private int mCurrentProfileID_Int = -1;
    private int mCurrentTabPos_Int = GUN_POS;
    private DBHandler db;
    private boolean isPortrait = true;

    // Gun
    private TrapMasterListArrayAdapter mCustomGunList_Adapt;

    // Load
    private TrapMasterListArrayAdapter mCustomLoadList_Adapt;

    // UI References
    private ListView mGunList_View, mLoadList_View;
    private ScrollView mGun_Scroll, mLoad_Scroll;
    private LinearLayout mGun_Lay, mLoad_Lay;
    private TabLayout armory_TabLay;

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

        // Initialize constants
        initializeConstants();

        // Figure out orientation and layout
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_armory_portrait);
            isPortrait = true;
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_armory_landscape);
            isPortrait = false;
        }

        // Pull extra information from intent
        if (savedInstanceState != null) {
            mCurrentProfileID_Int = savedInstanceState.getInt(CURRENT_USER_KEY);
        } else {
            mCurrentProfileID_Int = getIntent().getIntExtra(CURRENT_USER_KEY, -1);
        }

        // Initialize views
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

        // Figure out orientation and layout
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_armory_portrait);
            isPortrait = true;
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_armory_landscape);
            isPortrait = false;
        }

        // Initialize views
        initializeViews();
    }

    //********************************** Initialization Functions **********************************
    private void initializeConstants() {
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

        ACTIVITY_TITLE = getString(R.string.armory_activity_title);
        CURRENT_USER_KEY = getString(R.string.current_user_key);
        db = new DBHandler(ArmoryActivity.this);
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
        db = new DBHandler(getApplicationContext());

        // Initialize views
        armory_TabLay = findViewById(R.id.armory_TabLay);
        mGun_Lay = findViewById(R.id.armoryGun_Lay);
        mLoad_Lay = findViewById(R.id.armoryLoad_Lay);
        mGun_Scroll = findViewById(R.id.armoryGun_Scroll);
        mLoad_Scroll = findViewById(R.id.armoryLoad_Scroll);

        armory_TabLay.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case GUN_POS:
                        // set visibilities of scroll views, GUN
                        mGun_Scroll.setVisibility(View.VISIBLE);
                        mLoad_Scroll.setVisibility(View.GONE);

                        Log.d(TAG, "Guns Tab");

                        mCurrentTabPos_Int = GUN_POS;
                        break;
                    case LOAD_POS:
                        // set visibilities of scroll views, LOAD
                        mGun_Scroll.setVisibility(View.GONE);
                        mLoad_Scroll.setVisibility(View.VISIBLE);

                        Log.d(TAG, "Loads Tab");

                        mCurrentTabPos_Int = LOAD_POS;
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // Set the lists
        setGunListLay();
        setLoadListLay();

        // Set Button
        FloatingActionButton add_Btn = findViewById(R.id.armoryAdd_Btn);
        add_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (mCurrentTabPos_Int) {
                    case GUN_POS:
                        // Display add gun dialog box for user input, ID = -1 adding a new gun
                        GunClass temp_Gun = new GunClass();
                        temp_Gun.setGunID_Int(-1);
                        temp_Gun.setGunProfileID_Int(mCurrentProfileID_Int);
                        temp_Gun.editGunDialog(ArmoryActivity.this).setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                // Reset the gun list to changes in the database
                                setGunListLay();
                            }
                        });
                        break;
                    case LOAD_POS:
                        // Display add load dialog box for user input, ID = -1 adding a new load
                        LoadClass temp_Load = new LoadClass();
                        temp_Load.setLoadID_Int(-1);
                        temp_Load.setLoadProfileID_Int(mCurrentProfileID_Int);
                        temp_Load.editLoadDialog(ArmoryActivity.this).setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                // Reset the load list to changes in the database
                                setLoadListLay();
                            }
                        });
                        break;
                }
            }
        });

        // Setting title of activity
        setTitle(ACTIVITY_TITLE);
    }

    //************************************ List view Functions *************************************
    // Gun List
    private ArrayList<GunClass> refreshGunList() {
        /*******************************************************************************************
         * Function: refreshGunList
         *
         * Purpose: Function returns the current list of guns for the current user
         *
         * Parameters: None
         *
         * Returns: userGun_List - a list of guns for current user
         *
         ******************************************************************************************/

        // Initialize db handler and shooter and event array
        db = new DBHandler(this);
        ArrayList<GunClass> userGun_List = db.getAllGunFromDB(mCurrentProfileID_Int);

        return userGun_List;
    }

    private void setGunListLay() {
        /*******************************************************************************************
         * Function: setGunListLay
         *
         * Purpose: Function initializes the gun list layout
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        // Initialize gun list from db
        ArrayList<GunClass> dbGuns_List = refreshGunList();

        // Iterate over all guns in list and add to layout
        for (int i = 0; i < dbGuns_List.size(); i++) {
            GunClass temp_Gun = dbGuns_List.get(i);
            CustomListItemClass temp_Item = new CustomListItemClass(ArmoryActivity.this, temp_Gun.getGunProfileID_Int(), -1, temp_Gun);

            mGun_Lay.addView(temp_Item);
        }
    }

    // Load List
    private ArrayList<LoadClass> refreshLoadList() {
        /*******************************************************************************************
         * Function: refreshLoadList
         *
         * Purpose: Function returns the current list of loads for the current user
         *
         * Parameters: None
         *
         * Returns: userLoad_List - a list of loads for current user
         *
         ******************************************************************************************/

        // Initialize db handler and shooter and event array
        db = new DBHandler(this);
        ArrayList<LoadClass> userLoad_List = db.getAllLoadFromDB(mCurrentProfileID_Int);

        return userLoad_List;
    }

    private void setLoadListLay() {
        /*******************************************************************************************
         * Function: setLoadListLay
         *
         * Purpose: Function initializes the load list layout
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        // Initialize load list from db
        ArrayList<LoadClass> dbLoad_List = refreshLoadList();

        // Iterate over all loads in list and add to layout
        for (int i = 0; i < dbLoad_List.size(); i++) {
            LoadClass temp_Load = dbLoad_List.get(i);
            CustomListItemClass temp_Item = new CustomListItemClass(ArmoryActivity.this, temp_Load.getLoadProfileID_Int(), -1, temp_Load);

            mLoad_Lay.addView(temp_Item);
        }
    }

}
