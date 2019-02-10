package edu.coe.djshadle.trapmaster;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class homeActivity extends AppCompatActivity implements View.OnClickListener {
    //********************************* Variables and Constants ************************************
    //General Constants
    final String CURRENT_USER_KEY = "USER_KEY";

    //General Variables
    private String mCurrentUserEmail_Str = "tempEmail";

    // UI References
    private TextView mTxtUserEmail_View;

    //*********************************** Home Activity Functions **********************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        
        initViews();
        mTxtUserEmail_View.setText(mCurrentUserEmail_Str);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putString(CURRENT_USER_KEY, mTxtUserEmail_View.getText().toString());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        mCurrentUserEmail_Str = savedInstanceState.getString(CURRENT_USER_KEY);
        mTxtUserEmail_View.setText(mCurrentUserEmail_Str);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_home_portrait);
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_home_landscape);
        }

        mTxtUserEmail_View = (TextView) findViewById(R.id.txtHomeUserEmail);
        mTxtUserEmail_View.setText(mCurrentUserEmail_Str);
    }

    @Override
    public void onClick(View view) {
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
            case R.id.btnHomeQuickEvent:
                i = new Intent(this, QuickEventActivity.class);
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

    private void initViews(){
        Button mBtnNewEvent, mBtnArmory, mBtnEventHistory, mBtnProfiles, mBtnQuickEvent, mBtnTeams;
        mBtnNewEvent = (Button) findViewById(R.id.btnHomeNewEvent);
        mBtnArmory = (Button) findViewById(R.id.btnHomeArmory);
        mBtnEventHistory = (Button) findViewById(R.id.btnHomeEventHistory);
        mBtnProfiles = (Button) findViewById(R.id.btnHomeProfiles);
        mBtnQuickEvent = (Button) findViewById(R.id.btnHomeQuickEvent);
        mBtnTeams = (Button) findViewById(R.id.btnHomeTeams);

        mBtnNewEvent.setOnClickListener(this);
        mBtnArmory.setOnClickListener(this);
        mBtnEventHistory.setOnClickListener(this);
        mBtnProfiles.setOnClickListener(this);
        mBtnQuickEvent.setOnClickListener(this);
        mBtnTeams.setOnClickListener(this);

        mTxtUserEmail_View = (TextView) findViewById(R.id.txtHomeUserEmail);
    }

    //************************************** Other Functions ***************************************

}
