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
    private String DEFAULT_GUN_TEXT;
    private String DEFAULT_LOAD_TEXT;
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
    private ArrayList<GunClass> mUserGun_List;
    // Load
    private TrapMasterListArrayAdapter mCustomLoadList_Adapt;
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

        DEFAULT_GUN_TEXT = getString(R.string.default_gun_list_text);
        DEFAULT_LOAD_TEXT = getString(R.string.default_load_list_text);
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
        db = new DBHandler(this);

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
                temp_Gun.editGunDialog(ArmoryActivity.this);
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
                temp_Load.editLoadDialog(ArmoryActivity.this);
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
         * Returns: mUserGunList - a list of guns for current user
         *
         ******************************************************************************************/

        mUserGun_List = db.getAllGunFromDB(mCurrentUserEmail_Str);

        GunClass temp_Gun = new GunClass();
        temp_Gun.setGunNickname_Str("No guns");
        temp_Gun.setGunNotes_Str("Click the add button for a new gun!");

        if (mUserGun_List.isEmpty()) {
            mUserGun_List.add(temp_Gun);
        }

        return mUserGun_List;
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

        mCustomGunList_Adapt.clear();
        mCustomGunList_Adapt.addAll(refreshGunList());
        mCustomGunList_Adapt.notifyDataSetChanged();
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

            mGunList_View.setAdapter(mCustomGunList_Adapt);

            mGunList_View.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    int tempGunID_Int = (int) view.getTag();
                    Log.d(TAG, "OnItemClick for gun ID is here: " + tempGunID_Int + " for " + mCurrentUserEmail_Str);
                }
            });

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
         * Returns: mUserLoad_List - a list of loads for current user
         *
         ******************************************************************************************/

        mUserLoad_List = db.getAllLoadFromDB(mCurrentUserEmail_Str);

        LoadClass temp_Load = new LoadClass();
        temp_Load.setLoadNickname_Str("No loads");
        temp_Load.setLoadNotes_Str("Click the add button for a new load!");

        if (mUserLoad_List.isEmpty()) {
            mUserLoad_List.add(temp_Load);
        }

        return mUserLoad_List;
    }

    private void refreshLoadListView() {
        /*******************************************************************************************
         * Function: refreshLoadListView
         *
         * Purpose: Function refreshes the load list view with current load data from database
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        mCustomLoadList_Adapt.clear();
        mCustomLoadList_Adapt.addAll(refreshLoadList());
        mCustomLoadList_Adapt.notifyDataSetChanged();
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
