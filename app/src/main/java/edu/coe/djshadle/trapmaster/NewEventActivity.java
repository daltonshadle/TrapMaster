package edu.coe.djshadle.trapmaster;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NewEventActivity extends AppCompatActivity implements OnTotalHitChange {

    //********************************** Variables and Constants ***********************************
    // General Constants
    private final boolean PORTRAIT_ORIENTATION = false; // Allow portrait orientation, landscape
                                                        // is the default TODO: Support Portrait

    // General Variables

    // UI References


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT && PORTRAIT_ORIENTATION) {
            setContentView(R.layout.activity_new_event_portrait);
        }
        else {
            // Landscape is the default orientation by AndroidManifest.xml
            setContentView(R.layout.activity_new_event_landscape);
        }

        initViews();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT && PORTRAIT_ORIENTATION) {
            setContentView(R.layout.activity_new_event_portrait);
        }
        else {
            // Landscape is the default orientation by AndroidManifest.xml
            setContentView(R.layout.activity_new_event_landscape);
        }

    }

    private void initViews() {
        TrapCounterButtonsClass tempCount= (TrapCounterButtonsClass) findViewById(R.id.tcbTempCounter);
        tempCount.setTotalHitChange(this);
    }

    @Override
    public void OnTotalHitChange() {

    }

}
