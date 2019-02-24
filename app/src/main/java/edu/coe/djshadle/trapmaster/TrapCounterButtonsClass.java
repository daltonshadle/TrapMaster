package edu.coe.djshadle.trapmaster;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class TrapCounterButtonsClass extends LinearLayout implements OnStageChangeListener {

    //********************************** Variables and Constants ***********************************
    // General Constants
    private final static int HIT = 0, MISS = 1, NEUTRAL = 2;

    // General Variables
    private int trapCounterChildCount_Int = 0;

    // UI References
	TenaryButtonClass shot1_View, shot2_View, shot3_View, shot4_View, shot5_View;
    private LinearLayout trapCounterLayout;
    private TextView totalHitText;
    private OnTotalHitChange totalHitChange;

    //************************************* Public Functions ***************************************
    // Constructors
    public TrapCounterButtonsClass(Context context) {
        super(context);
        initializeViews(context);
    }

    public TrapCounterButtonsClass(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    public TrapCounterButtonsClass(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(context);
    }

    // Other Functions
    private void initializeViews(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_trap_score_buttons, this);

        shot1_View = findViewById(R.id.terShot1);
        shot2_View = findViewById(R.id.terShot2);
        shot3_View = findViewById(R.id.terShot3);
        shot4_View = findViewById(R.id.terShot4);
        shot5_View = findViewById(R.id.terShot5);

        shot1_View.setStageChangeListener(this);
        shot2_View.setStageChangeListener(this);
        shot3_View.setStageChangeListener(this);
        shot4_View.setStageChangeListener(this);
        shot5_View.setStageChangeListener(this);

        trapCounterLayout = findViewById(R.id.lnrTrapScoreBtns);
        totalHitText = findViewById(R.id.txtHits);

        // the minus one is for the textview at the end of this layout
        trapCounterChildCount_Int = trapCounterLayout.getChildCount() - 1;

    }

    protected void onFinishInflate(){
        super.onFinishInflate();
    }

    @Override
    public void OnStageChange() {
        int totalHits = getTotalNumberHit();
        totalHitText.setText(String.valueOf(totalHits));
        totalHitChange.OnTotalHitChange();
    }

    public void setTotalHitChange(OnTotalHitChange eventListener){
        totalHitChange = eventListener;
    }

    public int getTotalNumberHit(){
        // Function iterates through all ternary buttons and checks stage
        // Function returns the number of ternary buttons with stage HIT
        int total = 0;

        for (int i = 0; i < trapCounterChildCount_Int; i++) {
            TenaryButtonClass child = (TenaryButtonClass) trapCounterLayout.getChildAt(i);
            if(child.isHit()){
                total += 1;
            }
        }

        return total;
    }

    public void resetTrapCounter(){
        // Function iterates through all ternary buttons and resets their stage to NEUTRAL
        for (int i = 0; i < trapCounterChildCount_Int; i++) {
            TenaryButtonClass child = (TenaryButtonClass) trapCounterLayout.getChildAt(i);
            child.resetTernary();
        }

        totalHitText.setText("0");
        totalHitChange.OnTotalHitChange();
    }

    public boolean allChecked(){
        // Function iterates through all ternary buttons and checks stage
        // Function returns true if all ternary buttons are HIT or MISS (not NEUTRAL)
        boolean checked = true;
        for (int i = 0; i < trapCounterChildCount_Int; i++) {
            TenaryButtonClass child = (TenaryButtonClass) trapCounterLayout.getChildAt(i);
            if(child.isNeutral()){
                checked = false;
            }
        }
        return checked;
    }

    public boolean allHit(){
        // Function iterates through all ternary buttons and checks stage
        // Function returns true if all ternary buttons are HIT
        boolean checked = true;
        for (int i = 0; i < trapCounterChildCount_Int; i++) {
            TenaryButtonClass child = (TenaryButtonClass) trapCounterLayout.getChildAt(i);
            if(child.isMiss()){
                checked = false;
            }
        }
        return checked;
    }

    public int getNextUncheckedButton() {
        // Function iterates through all ternary buttons and checks stage
        // Function returns the first child that is unchecked. If all checked, returns -1

        int nextUnchecked = -1;

        for (int i = 0; i < trapCounterChildCount_Int; i++) {
            TenaryButtonClass child = (TenaryButtonClass) trapCounterLayout.getChildAt(i);
            if(child.isNeutral()){
                nextUnchecked = i;
                break;
            }
        }

        return nextUnchecked;
    }

    public void setNextChild(int status) {
        // Function iterates through all ternary buttons and checks stage for next neutral child
        // Function sets next child to the status provided as parameter
        int nextChildIndex = getNextUncheckedButton();
        if (nextChildIndex >= 0) {
            TenaryButtonClass child = (TenaryButtonClass) trapCounterLayout.getChildAt(nextChildIndex);
            child.setStage(status);

            int totalHits = getTotalNumberHit();
            totalHitText.setText(String.valueOf(totalHits));
            totalHitChange.OnTotalHitChange();
        }
    }

    public ArrayList<Integer> getChildStates() {
        // Function iterates through all ternary buttons and gathers stage
        // Function returns the five stages of the trap counter buttons

        ArrayList<Integer> childStates = new ArrayList<Integer>(trapCounterChildCount_Int);

        for (int i = 0; i < trapCounterChildCount_Int; i++) {
            TenaryButtonClass child = (TenaryButtonClass) trapCounterLayout.getChildAt(i);
            childStates.add(child.getStage());
        }

        return childStates;
    }
}
