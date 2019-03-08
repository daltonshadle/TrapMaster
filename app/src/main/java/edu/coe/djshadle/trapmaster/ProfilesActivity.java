/***************************************************************************************************
 * FILENAME : ProfilesActivity.java
 *
 * AUTHOR : Dalton Shadle
 *
 * DESCRIPTION : Holds functions for the Profiles activity of this application
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
import android.widget.TextView;

public class ProfilesActivity extends AppCompatActivity {

    //********************************** Variables and Constants ***********************************
    // General Constants

    // General Variables
    private int mTotalHits_Int = 999;
    private int mTotalShots_Int = 999;
    private String mCurrentUserEmail_Str = "********";
    private String mEventName_Str = "********";
    private String mShotNotes_Str = "********";
    private DBHandler db;

    // UI References
    private TextView mTxtTotalScore_View;

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
            setContentView(R.layout.activity_profiles_landscape);
        }
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            setContentView(R.layout.activity_profiles_portrait);
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
            setContentView(R.layout.activity_profiles_portrait);
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_profiles_landscape);
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

        // Initializing all textviews
        mTxtTotalScore_View = findViewById(R.id.tempProfile_Txt);

        // TODO: Remove temp code below to check DB functionality
        String tempTextFromDB = "";
        db = new DBHandler(this);

        ShotClass s = db.getShotFromDB(mCurrentUserEmail_Str);

        tempTextFromDB =
                "Email: " + s.getShotEmail_Str() +
                "\nEvent Name: " + s.getShotEventName_Str() +
                "\nTotal Shot: " + s.getShotTotalNum_Str() +
                "\nTotal Hit: " + s.getShotHitNum_Str() +
                "\nNotes: " + s.getShotNotes_Str();

        mTxtTotalScore_View.setText(tempTextFromDB);
    }
}
