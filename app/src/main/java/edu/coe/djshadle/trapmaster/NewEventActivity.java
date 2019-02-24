package edu.coe.djshadle.trapmaster;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class NewEventActivity extends AppCompatActivity implements OnTotalHitChange, View.OnClickListener {

    //********************************** Variables and Constants ***********************************
    // General Constants
    private final boolean PORTRAIT_ORIENTATION = false; // Allow portrait orientation, landscape
                                                        // is the default TODO: Support Portrait
    private final int HIT = 0, MISS = 1, NEUTRAL = 2;
    private final int NUM_COUNTER_BUTTON = 5;
    private final String TRAP_STATE_KEY = "TRAP_STATE_KEY";

    // General Variables
    private int trapCounterChildCount_Int = 0;
    private ArrayList<Integer> trapCounterState_Array;

    // UI References
    private TextView mTxtTotalScore_View;
    private LinearLayout mTrapCounter_SubLnrLay;

    //************************************* Activity Functions *************************************
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

        if (savedInstanceState != null) {
            trapCounterState_Array = savedInstanceState.getIntegerArrayList(TRAP_STATE_KEY);
            setTrapCounterStates(trapCounterState_Array);
        }
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.

        getTrapCounterStates();
        savedInstanceState.putIntegerArrayList(TRAP_STATE_KEY, trapCounterState_Array);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.

        trapCounterState_Array = savedInstanceState.getIntegerArrayList(TRAP_STATE_KEY);
        setTrapCounterStates(trapCounterState_Array);

    }

    //************************************* UI View Functions **************************************
    private void initViews() {
        // Initializing all trap counters
        TrapCounterButtonsClass trapCount_1 = findViewById(R.id.tcbCounter1);
        TrapCounterButtonsClass trapCount_2 = findViewById(R.id.tcbCounter2);
        TrapCounterButtonsClass trapCount_3 = findViewById(R.id.tcbCounter3);
        TrapCounterButtonsClass trapCount_4 = findViewById(R.id.tcbCounter4);
        TrapCounterButtonsClass trapCount_5 = findViewById(R.id.tcbCounter5);

        trapCount_1.setTotalHitChange(this);
        trapCount_2.setTotalHitChange(this);
        trapCount_3.setTotalHitChange(this);
        trapCount_4.setTotalHitChange(this);
        trapCount_5.setTotalHitChange(this);

        // Initializing all buttons
        Button hit_Btn = findViewById(R.id.hit_Btn);
        Button miss_Btn = findViewById(R.id.miss_Btn);
        Button save_Btn = findViewById(R.id.save_Btn);
        Button clear_Btn = findViewById(R.id.clear_Btn);

        hit_Btn.setOnClickListener(this);
        miss_Btn.setOnClickListener(this);
        save_Btn.setOnClickListener(this);
        clear_Btn.setOnClickListener(this);

        // Initializing all textviews
        mTxtTotalScore_View = findViewById(R.id.totalScore_Txt);


        // Initializing all layouts
        mTrapCounter_SubLnrLay = findViewById(R.id.newEventTrapCounter_SubLnrLay);

        // Initializing integer to layout child count
        trapCounterChildCount_Int = mTrapCounter_SubLnrLay.getChildCount();

        // Initializing integer array for trap counter states (5 buttons per counter)
        trapCounterState_Array = new ArrayList<Integer>(trapCounterChildCount_Int);

    }

    @Override
    public void OnTotalHitChange() {
        int total = 0;
        for(int i = 0; i < trapCounterChildCount_Int; i++){
            TrapCounterButtonsClass tempTrapCounter =
                    (TrapCounterButtonsClass) mTrapCounter_SubLnrLay.getChildAt(i);
            total += tempTrapCounter.getTotalNumberHit();
        }

        mTxtTotalScore_View.setText(String.valueOf(total));
    }

    @Override
    public void onClick(View view) {
        // TODO: Call the getNextUncheckedChildFunction and set a variable to that index

        switch (view.getId()) {
            case R.id.hit_Btn:
                setNextUncheckedTrapCounter(HIT);
                break;
            case R.id.miss_Btn:
                setNextUncheckedTrapCounter(MISS);
                break;
            case R.id.clear_Btn:
                //get all the trapCounter controls and reset their totals and buttons
                for (int i = 0; i < (mTrapCounter_SubLnrLay.getChildCount()); i++) {
                    TrapCounterButtonsClass tempTrapCounter =
                            (TrapCounterButtonsClass) mTrapCounter_SubLnrLay.getChildAt(i);
                    tempTrapCounter.resetTrapCounter();
                }
                break;
            case R.id.save_Btn:

                break;

        }
    }

    //********************************** Trap Counter Functions ************************************
    private int getNextUncheckedTrapCounter() {
        int nextChild = -1;

        for (int i = 0; i < trapCounterChildCount_Int; i++) {
            TrapCounterButtonsClass tempTrapCounter =
                    (TrapCounterButtonsClass) mTrapCounter_SubLnrLay.getChildAt(i);
            if (!tempTrapCounter.allChecked()) {
                nextChild = i;
                break;
            }
        }

        return nextChild;
    }

    private void setNextUncheckedTrapCounter(int status) {
        int nextUnchecked = getNextUncheckedTrapCounter();

        if (nextUnchecked != -1) {
            TrapCounterButtonsClass tempTrapCounter =
                    (TrapCounterButtonsClass) mTrapCounter_SubLnrLay.getChildAt(nextUnchecked);

            tempTrapCounter.setNextChild(status);
        }
    }

    private void getTrapCounterStates() {
        for (int i = 0; i < trapCounterChildCount_Int; i++) {
            TrapCounterButtonsClass tempTrapCounter =
                    (TrapCounterButtonsClass) mTrapCounter_SubLnrLay.getChildAt(i);

            ArrayList<Integer> tempTrapCounterStates = tempTrapCounter.getChildStates();
            for (int j = 0; j < NUM_COUNTER_BUTTON; j++) {
                trapCounterState_Array.add(tempTrapCounterStates.get(j));
            }
        }
    }

    private void setTrapCounterStates(ArrayList<Integer> stateList) {
        for (int i = 0; i < trapCounterChildCount_Int; i++) {
            TrapCounterButtonsClass tempTrapCounter =
                    (TrapCounterButtonsClass) mTrapCounter_SubLnrLay.getChildAt(i);
            tempTrapCounter.resetTrapCounter();

            for (int j = 0; j < NUM_COUNTER_BUTTON; j++) {
                int index = (i * NUM_COUNTER_BUTTON) + j;
                tempTrapCounter.setNextChild(stateList.get(index));
            }
        }
    }
}
