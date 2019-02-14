package edu.coe.djshadle.trapmaster;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NewEventActivity extends AppCompatActivity implements OnTotalHitChange, OnStageChangeListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_new_event_landscape);
        }
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            setContentView(R.layout.activity_new_event_portrait);
        }

        initViews();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_new_event_portrait);
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_new_event_landscape);
        }

    }

    private void initViews() {
        TenaryButtonClass tempBtn = (TenaryButtonClass) findViewById(R.id.tbTempTer);
        tempBtn.setStageChangeListener(this);

        TrapCounterButtonsClass tempCount= (TrapCounterButtonsClass) findViewById(R.id.tcbTempCounter);
        tempCount.setTotalHitChange(this);
    }

    @Override
    public void OnTotalHitChange() {

    }

    @Override
    public void OnStageChange() {

    }
}
