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
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class homeActivity extends AppCompatActivity implements View.OnClickListener {
    //********************************* Variables and Constants ************************************
    //General Constants
    final String CURRENT_USER_KEY = "USER_KEY";

    //General Variables
    private String mCurrentUserEmail_Str = "tempEmail";

    // UI References

    //Google Variables
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

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_home_landscape);
        }
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            setContentView(R.layout.activity_home_portrait);
        }

        if (savedInstanceState != null) {
            mCurrentUserEmail_Str = savedInstanceState.getString(CURRENT_USER_KEY);
        } else {
            mCurrentUserEmail_Str = getIntent().getStringExtra(getString(R.string.current_user_email));
        }

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
        savedInstanceState.putString(CURRENT_USER_KEY, mCurrentUserEmail_Str);
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
        mCurrentUserEmail_Str = savedInstanceState.getString(CURRENT_USER_KEY);
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

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_home_portrait);
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_home_landscape);
        }

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

    @Override
    public void onClick(View view) {
        /*******************************************************************************************
         * Function: OnClick
         *
         * Purpose: Function listener for buttons that provides functionality when clicked
         *
         * Parameters: view (IN) - is the clicked view that enacted listener
         *
         * Returns: None
         *
         ******************************************************************************************/

        Intent i;
        switch (view.getId()) {
            case R.id.btnHomeNewEvent:
                i = new Intent(this, NewEventActivity.class);
                i.putExtra(getString(R.string.current_user_email), mCurrentUserEmail_Str);
                startActivity(i);
                break;
            case R.id.btnHomeArmory:
                i = new Intent(this, ArmoryActivity.class);
                i.putExtra(getString(R.string.current_user_email), mCurrentUserEmail_Str);
                startActivity(i);
                break;
            case R.id.btnHomeEventHistory:
                i = new Intent(this, EventHistoryActivity.class);
                i.putExtra(getString(R.string.current_user_email), mCurrentUserEmail_Str);
                startActivity(i);
                break;
            case R.id.btnHomeProfiles:
                i = new Intent(this, ProfilesActivity.class);
                i.putExtra(getString(R.string.current_user_email), mCurrentUserEmail_Str);
                startActivity(i);
                break;
            case R.id.btnHomeTeams:
                i = new Intent(this, TeamsActivity.class);
                i.putExtra(getString(R.string.current_user_email), mCurrentUserEmail_Str);
                startActivity(i);
                break;
        }
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

        mBtnNewEvent.setOnClickListener(this);
        mBtnArmory.setOnClickListener(this);
        mBtnEventHistory.setOnClickListener(this);
        mBtnProfiles.setOnClickListener(this);
        mBtnTeams.setOnClickListener(this);

        // Set action bar title
        try {
            setTitle(mCurrentUserEmail_Str);
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

    //************************************** Other Functions ***************************************
}
