package edu.coe.djshadle.trapmaster;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class homeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnNewEvent, mBtnArmory, mBtnEventHistory, mBtnProfiles, mBtnQuickEvent, mBtnTeams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_home_landscape);
        }
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            setContentView(R.layout.activity_home_portrait);
        }

        initViews();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_home_portrait);
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_home_landscape);
        }

    }

    private void initViews(){
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
    }

    @Override
    public void onClick(View view) {
        Intent i;
        switch (view.getId()) {
            case R.id.btnHomeNewEvent:
                i = new Intent(this, NewEventActivity.class);
                startActivity(i);
                break;
            case R.id.btnHomeArmory:
                i = new Intent(this, ArmoryActivity.class);
                startActivity(i);
                break;
            case R.id.btnHomeEventHistory:
                i = new Intent(this, EventHistoryActivity.class);
                startActivity(i);
                break;
            case R.id.btnHomeProfiles:
                i = new Intent(this, ProfilesActivity.class);
                startActivity(i);
                break;
            case R.id.btnHomeQuickEvent:
                i = new Intent(this, QuickEventActivity.class);
                startActivity(i);
                break;
            case R.id.btnHomeTeams:
                i = new Intent(this, TeamsActivity.class);
                startActivity(i);
                break;

        }
    }
}
