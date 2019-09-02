/***************************************************************************************************
 * FILENAME : homeActivity.java
 *
 * AUTHOR : Dalton Shadle
 *
 * DESCRIPTION : Holds functions for the home activity of this application
 *
 * NOTES : N/A
 *
 * Copyright Dalton Shadle 2019.  All rights reserved.
 *
 **************************************************************************************************/

package edu.coe.djshadle.trapmaster;

//******************************************** Imports *********************************************
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

public class homeActivity extends AppCompatActivity{
    //**************************************** Constants *******************************************
    // General Constants

    // Key Constants
    private String CURRENT_USER_KEY;
    private String ACTIVITY_TITLE;

    //**************************************** Variables *******************************************
    // General Variables
    private String mCurrentProfileEmail_Str = "********";
    private int mCurrentProfileID_Int = -1;
    private DBHandler db;

    // UI References

    // Google Variables
    FirebaseAuth auth;

    //*********************************** Home Activity Functions **********************************
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
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_home_landscape);
        }
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            setContentView(R.layout.activity_home_portrait);
        }

        // Pull extra information from intent
        if (savedInstanceState != null) {
            mCurrentProfileID_Int = savedInstanceState.getInt(CURRENT_USER_KEY);
        } else {
            mCurrentProfileID_Int = getIntent().getIntExtra(CURRENT_USER_KEY, -1);
        }

        mCurrentProfileEmail_Str = db.getProfileFromDB(mCurrentProfileID_Int).getProfileEmail_Str();

        // Initialize views
        initializeViews();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        /*******************************************************************************************
         * Function: onSaveInstanceState
         *
         * Purpose: Function saves instances when activity is paused
         *
         * Parameters: savedInstanceState (OUT) - provides the saved instances from current state
         *
         * Returns: None
         *
         ******************************************************************************************/

        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putInt(CURRENT_USER_KEY, mCurrentProfileID_Int);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        /*******************************************************************************************
         * Function: onRestoreInstanceState
         *
         * Purpose: Function restores instances when activity resumes
         *
         * Parameters: savedInstanceState (IN) - provides the saved instances from previous state
         *
         * Returns: None
         *
         ******************************************************************************************/

        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        mCurrentProfileID_Int = savedInstanceState.getInt(CURRENT_USER_KEY);
    }

    @Override
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
            setContentView(R.layout.activity_home_portrait);
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_home_landscape);
        }

        // Initialize views
        initializeViews();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        /*******************************************************************************************
         * Function: onCreateOptionsMenu
         *
         * Purpose: Function inflater menu from resources
         *
         * Parameters: menu (IN) - menu input
         *
         * Returns: True
         *
         ******************************************************************************************/

        // Inflate menu item from resources
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_activity_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        /*******************************************************************************************
         * Function: onCreateOptionsMenu
         *
         * Purpose: Function inflater menu from resources
         *
         * Parameters: item (IN) - menu item input
         *
         * Returns: True
         *
         ******************************************************************************************/

        int id = item.getItemId();

        if (id == R.id.homeSignOut_MenuItem) {
            // Sign out user
            auth.signOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
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

        CURRENT_USER_KEY = getString(R.string.current_user_key);
        ACTIVITY_TITLE = getString(R.string.home_activity_title);
        //ACTIVITY_TITLE = mCurrentProfileEmail_Str;
        db = new DBHandler(homeActivity.this);
    }

    private void initializeViews(){
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

        // Initializing all buttons
        Button mBtnNewEvent = findViewById(R.id.btnHomeNewEvent);
        Button mBtnArmory = findViewById(R.id.btnHomeArmory);
        Button mBtnEventHistory = findViewById(R.id.btnHomeEventHistory);
        Button mBtnProfiles = findViewById(R.id.btnHomeProfiles);
        Button mBtnTeams = findViewById(R.id.btnHomeTeams);

        // set button on click listeners
        mBtnNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShootingEventClass newEvent = new ShootingEventClass(mCurrentProfileID_Int,homeActivity.this);
                newEvent.newEventDialog(homeActivity.this);
            }
        });
        mBtnArmory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent armory_Intent = new Intent(homeActivity.this, ArmoryActivity.class);
                armory_Intent.putExtra(CURRENT_USER_KEY, mCurrentProfileID_Int);
                startActivity(armory_Intent);
            }
        });
        mBtnEventHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent eventHistory_Intent = new Intent(homeActivity.this, EventHistoryActivity.class);
                eventHistory_Intent.putExtra(CURRENT_USER_KEY, mCurrentProfileID_Int);
                startActivity(eventHistory_Intent);
            }
        });
        mBtnProfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profiles_Intent = new Intent(homeActivity.this, ProfilesActivity.class);
                profiles_Intent.putExtra(CURRENT_USER_KEY, mCurrentProfileID_Int);
                startActivity(profiles_Intent);
            }
        });
        mBtnTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent teams_Intent = new Intent(homeActivity.this, TeamsActivity.class);
                teams_Intent.putExtra(CURRENT_USER_KEY, mCurrentProfileID_Int);
                startActivity(teams_Intent);
            }
        });

        // Set action bar title
        try {
            setTitle(ACTIVITY_TITLE);
        } catch (Exception e) {
            // Didn't work.
        }

        // Initialize Google code for storing
        auth = FirebaseAuth.getInstance();
        // this listener will be called when there is change in firebase user session
        FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(homeActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
        auth.addAuthStateListener(authListener);
    }


}
