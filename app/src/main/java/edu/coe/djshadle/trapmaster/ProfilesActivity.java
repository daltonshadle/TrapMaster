package edu.coe.djshadle.trapmaster;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        initViews();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_profiles_portrait);
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_profiles_landscape);
        }

    }

    private void initViews() {
        // Function initializes all views and variables needed for this activity

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
