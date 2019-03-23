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
import android.content.res.Configuration;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

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

    // TEMP VARS TODO: Remove temp variables
    int tempInt;


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

        // Initializing temp variables TODO: Remove temp variables
        tempInt = 0;

        // Initializing database variable
        db = new DBHandler(this);

        // Initializing buttons
        FloatingActionButton mAddGun = findViewById(R.id.armoryAddGun_Btn);
        FloatingActionButton mAddLoad = findViewById(R.id.armoryAddLoad_Btn);

        mAddGun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add temp gun to database
                tempInt += 1;
                GunClass tempGun = new GunClass();
                tempGun.setGunEmail_Str(mCurrentUserEmail_Str);
                tempGun.setGunNickname_Str(Integer.toString(tempInt));
                db.insertGunInDB(tempGun);

                // Refresh gun listview
                refreshGunListView();
            }
        });

        mAddLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add temp load to database
                tempInt += 1;
                LoadClass tempLoad = new LoadClass();
                tempLoad.setLoadEmail_Str(mCurrentUserEmail_Str);
                tempLoad.setLoadNickname_Str(Integer.toString(tempInt));
                db.insertLoadInDB(tempLoad);

                // Refresh load listview
                refreshLoadListView();
            }
        });


        // Initializing Listviews
        mGunList_View = findViewById(R.id.armoryGun_List);
        mLoadList_View = findViewById(R.id.armoryLoad_List);

        setListViewLayoutParams();

        try {
            ArrayList<String> currentGunStr_List =  refreshGunList();

            mCurrentGunList_Adapt = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, currentGunStr_List);

            mGunList_View.setAdapter(mCurrentGunList_Adapt);
            mGunList_View.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                }
            });

        } catch (Exception e){
            Log.d("JRW", "no guns in db for this user");
        }

        try {
            ArrayList<String> currentLoadStr_List =  refreshLoadList();

            mCurrentLoadList_Adapt = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, currentLoadStr_List);

            mLoadList_View.setAdapter(mCurrentLoadList_Adapt);
            mLoadList_View.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                }
            });

        } catch (Exception e){
            Log.d("JRW", "no loads in db for this user");
        }

        // Setting title of activity
        setTitle("Armory");
    }

    //*************************************** Other Functions **************************************
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
            currentGunStr_List.add(tempGun.getGunEmail_Str() + " " + tempGun.getGunNickname_Str());
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
            currentLoadStr_List.add(tempLoad.getLoadEmail_Str() + " " + tempLoad.getLoadNickname_Str());
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



}
