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
    private String TAG = "JRW";
    private String ACTIVITY_TITLE;
    private String CURRENT_USER_KEY;
    private final int GUN_LIST_TAG = 3;
    private final int LOAD_LIST_TAG = 4;

    // General Variables
    private String mCurrentUserEmail_Str = "********";
    private DBHandler db;
    private boolean isPortrait = true;
    // Gun
    private TrapMasterListArrayAdapter mCustomGunList_Adapt;
    // Load
    private TrapMasterListArrayAdapter mCustomLoadList_Adapt;

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

        initializeConstants();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_armory_portrait);
            isPortrait = true;
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_armory_landscape);
            isPortrait = false;
        }

        if (savedInstanceState != null) {

        } else {
            mCurrentUserEmail_Str = getIntent().getStringExtra(CURRENT_USER_KEY);
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
            isPortrait = true;
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_armory_landscape);
            isPortrait = false;
        }

        initializeViews();
    }

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

        // Initializing buttons
        FloatingActionButton mAddGun = findViewById(R.id.armoryAddGun_Btn);
        FloatingActionButton mAddLoad = findViewById(R.id.armoryAddLoad_Btn);

        mAddGun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Display add gun dialog box for user input, ID = -1 and email = current email for
                // adding a new gun
                GunClass temp_Gun = new GunClass();
                temp_Gun.setGunID_Int(-1);
                temp_Gun.setGunEmail_Str(mCurrentUserEmail_Str);
                temp_Gun.editGunDialog(ArmoryActivity.this, mCustomGunList_Adapt);
            }
        });

        mAddLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Display add load dialog box for user input, ID = -1 and email = current email for
                // adding a new load
                LoadClass temp_Load = new LoadClass();
                temp_Load.setLoadID_Int(-1);
                temp_Load.setLoadEmail_Str(mCurrentUserEmail_Str);
                temp_Load.editLoadDialog(ArmoryActivity.this, mCustomLoadList_Adapt);
            }
        });


        // Initializing Listviews
        mGunList_View = findViewById(R.id.armoryGun_List);
        mLoadList_View = findViewById(R.id.armoryLoad_List);

        // Setting tags for list views
        mGunList_View.setTag(GUN_LIST_TAG);
        mLoadList_View.setTag(LOAD_LIST_TAG);

        setListViewLayoutParams();

        initializeGunListView();
        initializeLoadListView();

        // Setting title of activity
        setTitle(ACTIVITY_TITLE);
    }

    //************************************* Listview Functions *************************************
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
        ArrayList<GunClass> userGun_List = db.getAllGunFromDB(mCurrentUserEmail_Str);

        return userGun_List;
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
            mCustomGunList_Adapt = new TrapMasterListArrayAdapter(this,
                    (ArrayList<Object>)(ArrayList<?>)(refreshGunList()));

            mCustomGunList_Adapt.refreshGunArrayAdapter(refreshGunList());

            mGunList_View.setAdapter(mCustomGunList_Adapt);

        } catch (Exception e){
            Log.d("JRW", "no guns in db for this user");
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
        ArrayList<LoadClass> userLoad_List = db.getAllLoadFromDB(mCurrentUserEmail_Str);

        return userLoad_List;
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
            mCustomLoadList_Adapt = new TrapMasterListArrayAdapter(this,
                    (ArrayList<Object>)(ArrayList<?>)(refreshLoadList()));

            mCustomLoadList_Adapt.refreshLoadArrayAdapter(refreshLoadList());

            mLoadList_View.setAdapter(mCustomLoadList_Adapt);

        } catch (Exception e){
            Log.d("JRW", "no loads in db for this user");
        }
    }

    // Both List
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

        if (isPortrait) {
            scaleFactor_Dbl = 3.5;
        } else {
            scaleFactor_Dbl = 2.0;
        }

        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)(screenHeight_Dbl / scaleFactor_Dbl));

        mGunList_View.setLayoutParams(params);
        mLoadList_View.setLayoutParams(params);
    }

}
