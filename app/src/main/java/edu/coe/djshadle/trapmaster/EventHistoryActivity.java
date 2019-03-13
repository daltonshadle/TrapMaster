/***************************************************************************************************
 * FILENAME : EventHistoryActivity.java
 *
 * AUTHOR : Dalton Shadle
 *
 * DESCRIPTION : Holds functions for the Event History activity of this application
 *
 * NOTES : N/A
 *
 * Copyright Dalton Shadle 2019.  All rights reserved.
 *
 **************************************************************************************************/

package edu.coe.djshadle.trapmaster;

//******************************************** Imports *********************************************
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class EventHistoryActivity extends AppCompatActivity {

    //********************************** Variables and Constants ***********************************
    // General Constants

    // General Variables
    private String mCurrentUserEmail_Str = "********";
    private DBHandler db;

    // UI References
    private TextView mEventHistoryTxt_View;
    private ListView mScoreList_View;

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

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_event_history_landscape);
        }
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            setContentView(R.layout.activity_event_history_portrait);
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
            setContentView(R.layout.activity_event_history_portrait);
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_event_history_landscape);
        }

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

        // Initializing all views
        mEventHistoryTxt_View = findViewById(R.id.tempEventHistory_Txt);
        mScoreList_View = findViewById(R.id.eventHistoryScore_List);

        // Initializing list and database variables
        ArrayList<String> shotString_List =  new ArrayList<>();
        db = new DBHandler(this);

        try {
            ArrayList<ShotClass> s = db.getAllShotFromDB(mCurrentUserEmail_Str);

            for (ShotClass tempShot : s) {
                shotString_List.add("Email: " + tempShot.getShotEmail_Str() +
                        "\nTotal Hit: " + tempShot.getShotHitNum_Str());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, shotString_List);

            mScoreList_View.setAdapter(adapter);
            mScoreList_View.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                }
            });

        } catch (Exception e){
            Log.d("JRW", "nothing in db for this user");
        }


    }


}
