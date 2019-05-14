/***************************************************************************************************
 * FILENAME : TrapScoreItemClass.java
 *
 * AUTHOR : Dalton Shadle
 *
 * DESCRIPTION : Holds variables and functions for TrapScoreItemClass object
 *
 * NOTES : N/A
 *
 * Copyright Dalton Shadle 2019.  All rights reserved.
 *
 **************************************************************************************************/

package edu.coe.djshadle.trapmaster;

//******************************************** Imports *********************************************
import android.animation.LayoutTransition;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.transition.AutoTransition;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionValues;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TrapScoreItemClass extends ConstraintLayout implements OnStageChangeListener {
    //********************************** Variables and Constants ***********************************
    // General Constants
    private final static int HIT = 0, MISS = 1, NEUTRAL = 2;
    private final static String TAG = "JRW";
    private final int NUM_BUTTONS_PER_LANE = 5;

    // General Variables
    private int viewScore_Int = 0;
    private boolean viewExpand_Bool = false;
    private OnTotalHitChange totalHitChange;

    // UI References
    private ArrayList<TrapTernaryButtonClass> mTrapTernaryBtn_List;
    private ArrayList<LinearLayout> mLaneLay_List;
    private ConstraintLayout mWholeConstraint_Lay;
    private TextView mRound_Txt;
    private TextView mShooter_Txt;
    private TextView mScore_Txt;
    private Button mMiss_Btn;
    private Button mHit_Btn;
    private Button mClear_Btn;

    //************************************* Public Functions ***************************************
    // Constructors
    public TrapScoreItemClass(Context context, boolean expandMode_Bool) {
        /*******************************************************************************************
         * Function: TrapScoreItemClass
         *
         * Purpose: Constructor for this class
         *
         * Parameters: context (IN) - fill_in
         *
         * Returns: None
         *
         ******************************************************************************************/

        super(context);
        viewExpand_Bool = expandMode_Bool;
        initializeViews(context);
    }

    public TrapScoreItemClass(Context context, AttributeSet attrs) {
        /*******************************************************************************************
         * Function: TrapTernaryButtonClass
         *
         * Purpose: Constructor for this class
         *
         * Parameters: context (IN) - fill_in
         *             attrs (IN) - fill_in
         *
         * Returns: None
         *
         ******************************************************************************************/

        super(context, attrs);
        initializeViews(context);
    }

    public TrapScoreItemClass(Context context, AttributeSet attrs, int defStyleAttr) {
        /*******************************************************************************************
         * Function: TrapTernaryButtonClass
         *
         * Purpose: Constructor for this class
         *
         * Parameters: context (IN) - fill_in
         *             attrs (IN) - fill_in
         *             defStyleAttr (IN) - fill_in
         *
         * Returns: None
         *
         ******************************************************************************************/

        super(context, attrs, defStyleAttr);
        initializeViews(context);
    }

    //************************************* UI View Functions **************************************
    private void initializeViews(final Context context){
        /*******************************************************************************************
         * Function: initializeViews
         *
         * Purpose: Function initializes views for this trap score
         *
         * Parameters: context (IN) - fill_in
         *
         * Returns: None
         *
         ******************************************************************************************/

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        inflater.inflate(R.layout.view_trap_score_item, this);

        // Initialize all variables
        mTrapTernaryBtn_List = new ArrayList<>();
        mLaneLay_List = new ArrayList<>();
        mWholeConstraint_Lay = findViewById(R.id.trapScoreItem_Lay);
        mRound_Txt = findViewById(R.id.trapScoreRound_txt);
        mShooter_Txt = findViewById(R.id.trapScoreName_txt);
        mScore_Txt =  findViewById(R.id.trapScoreScore_txt);

        // Initialize all textviews
        initializeTxtViews();

        // Initialize all layouts and trap ternary
        initializeTrapTernaryBtns(context);

        // Initialize buttons
        initializeBtns(context);

        // Start in collapse mode
        if (viewExpand_Bool) {
            expandView();
        } else {
            collapseView();
        }
    }

    private void initializeTxtViews(){
        /*******************************************************************************************
         * Function: initializeTxtViews
         *
         * Purpose: Function initializes textviews for this trap score
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/


    }

    private void initializeTrapTernaryBtns(Context context){
        /*******************************************************************************************
         * Function: initializeTrapTernaryBtns
         *
         * Purpose: Function initializes ternary buttons for this trap score
         *
         * Parameters: context (IN) - context for initializing views
         *
         * Returns: None
         *
         ******************************************************************************************/

        // Add all linear layouts to list by ID
        mLaneLay_List.add((LinearLayout) findViewById(R.id.trapScoreLane1_LnrLay));
        mLaneLay_List.add((LinearLayout) findViewById(R.id.trapScoreLane2_LnrLay));
        mLaneLay_List.add((LinearLayout) findViewById(R.id.trapScoreLane3_LnrLay));
        mLaneLay_List.add((LinearLayout) findViewById(R.id.trapScoreLane4_LnrLay));
        mLaneLay_List.add((LinearLayout) findViewById(R.id.trapScoreLane5_LnrLay));

        // Add all buttons to list by ID
        for (int i = 0; i < mLaneLay_List.size(); i++) {
            for (int j = 0; j < NUM_BUTTONS_PER_LANE; j++) {
                // Initialize Button
                TrapTernaryButtonClass temp_Btn = findViewById(mLaneLay_List.get(i).getChildAt(j).getId());
                temp_Btn.setStageChangeListener(this);
                mTrapTernaryBtn_List.add(temp_Btn);
            }
        }
    }

    private void initializeBtns(final Context context){
        /*******************************************************************************************
         * Function: initializeBtns
         *
         * Purpose: Function initializes buttons for this trap score
         *
         * Parameters: context (IN) - context to initialize buttons with
         *
         * Returns: None
         *
         ******************************************************************************************/

        // Init hit button
        mHit_Btn = findViewById(R.id.trapScoreHit_Btn);
        mHit_Btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setNextChild(HIT);
            }
        });

        // Init miss button
        mMiss_Btn = findViewById(R.id.trapScoreMiss_Btn);
        mMiss_Btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setNextChild(MISS);
            }
        });

        // Init clear button
        mClear_Btn = findViewById(R.id.trapScoreClear_Btn);
        mClear_Btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clearBtnDialog(context);
            }
        });
    }

    protected void onFinishInflate(){
        /*******************************************************************************************
         * Function: onFinishInflate
         *
         * Purpose: Function finishes the layout inflater
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        super.onFinishInflate();

        // Can set on click listener here I think
    }

    @Override
    public void OnStageChange() {
        /*******************************************************************************************
         * Function: OnStageChange
         *
         * Purpose: Function sets functionality for OnStageChange
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        viewScore_Int = getTotalNumberHit();
        mScore_Txt.setText(String.valueOf(viewScore_Int));
        totalHitChange.OnTotalHitChange();
    }

    public void setTotalHitChange(OnTotalHitChange eventListener){
        /*******************************************************************************************
         * Function: setTotalHitChange
         *
         * Purpose: Function sets eventListener
         *
         * Parameters: eventListener (IN) - values to set private event listener to
         *
         * Returns: None
         *
         ******************************************************************************************/

        totalHitChange = eventListener;
    }

    //************************************** Other Functions ***************************************
    public int getTotalNumberHit(){
        /*******************************************************************************************
         * Function: getTotalNumberHit
         *
         * Purpose: Function iterates through all ternary buttons and checks stage
         *
         * Parameters: None
         *
         * Returns: total - function returns the number of ternary buttons with stage HIT
         *
         ******************************************************************************************/

        int total = 0;

        for (int i = 0; i < mTrapTernaryBtn_List.size(); i++) {
            TrapTernaryButtonClass child = mTrapTernaryBtn_List.get(i);
            if (child.isHit()){
                total += 1;
            }
        }

        return total;
    }

    public void resetTrapCounter(){
        /*******************************************************************************************
         * Function: resetTrapCounter
         *
         * Purpose: Function iterates through all ternary buttons and resets their stage to NEUTRAL
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        for (int i = 0; i < mTrapTernaryBtn_List.size(); i++) {
            TrapTernaryButtonClass child = mTrapTernaryBtn_List.get(i);
            child.resetTernary();
        }

        viewScore_Int = 0;
        mScore_Txt.setText(Integer.toString(viewScore_Int));
        totalHitChange.OnTotalHitChange();
    }

    public boolean allChecked(){
        /*******************************************************************************************
         * Function: allChecked
         *
         * Purpose: Function iterates through all ternary buttons and checks stage
         *
         * Parameters: None
         *
         * Returns: checked - Function returns true if all ternary buttons are HIT or MISS
         *
         ******************************************************************************************/

        boolean checked = true;

        for (int i = 0; i < mTrapTernaryBtn_List.size(); i++) {
            TrapTernaryButtonClass child = mTrapTernaryBtn_List.get(i);
            if (child.isNeutral()){
                checked = false;
            }
        }

        return checked;
    }

    public boolean allHit(){
        /*******************************************************************************************
         * Function: allHit
         *
         * Purpose: Function iterates through all ternary buttons and checks stage
         *
         * Parameters: None
         *
         * Returns: checked - Function returns true if all ternary buttons are HIT
         *
         ******************************************************************************************/

        boolean checked = true;

        for (int i = 0; i < mTrapTernaryBtn_List.size(); i++) {
            TrapTernaryButtonClass child = mTrapTernaryBtn_List.get(i);
            if (child.isMiss()){
                checked = false;
            }
        }

        return checked;
    }

    public int getNextUncheckedButton() {
        /*******************************************************************************************
         * Function: getNextUncheckedButton
         *
         * Purpose: Function iterates through all ternary buttons and checks stage
         *
         * Parameters: None
         *
         * Returns: nextUnchecked - Function returns the first child that is unchecked.
         *                          If all checked, returns -1
         *
         ******************************************************************************************/

        int nextUnchecked = -1;

        for (int i = 0; i < mTrapTernaryBtn_List.size(); i++) {
            TrapTernaryButtonClass child = mTrapTernaryBtn_List.get(i);
            if (child.isNeutral()){
                nextUnchecked = i;
                break;
            }
        }

        return nextUnchecked;
    }

    public void setNextChild(int stage) {
        /*******************************************************************************************
         * Function: setNextChild
         *
         * Purpose: Function iterates through all ternary buttons and checks stage for next
         *          neutral child
         *
         * Parameters: stage (IN) - stage to set the next child
         *
         * Returns: None
         *
         ******************************************************************************************/

        int nextChildIndex = getNextUncheckedButton();

        if (nextChildIndex >= 0) {
            mTrapTernaryBtn_List.get(nextChildIndex).setStage(stage);

            viewScore_Int = getTotalNumberHit();
            mScore_Txt.setText(String.valueOf(viewScore_Int));
            totalHitChange.OnTotalHitChange();
        }
    }

    public void setRoundText(int roundNum_Int) {
        /*******************************************************************************************
         * Function: setRoundText
         *
         * Purpose: Function sets the text for the round text view
         *
         * Parameters: roundNum_Int (IN) - round number
         *
         * Returns: None
         *
         ******************************************************************************************/

        mRound_Txt.setText("Round " + Integer.toString(roundNum_Int));
    }

    public void setShooterText(String shooter_Str) {
        /*******************************************************************************************
         * Function: setRoundText
         *
         * Purpose: Function sets the text for the user email text view
         *
         * Parameters: shooter_Str (IN) - user email to set
         *
         * Returns: None
         *
         ******************************************************************************************/

        mShooter_Txt.setText(shooter_Str);
    }

    public ArrayList<Integer> getAllStates() {
        /*******************************************************************************************
         * Function: getAllStates
         *
         * Purpose: Function iterates through all ternary buttons and gathers stage
         *
         * Parameters: None
         *
         * Returns: allStates - returns a array list of all button states
         *
         ******************************************************************************************/

        ArrayList<Integer> allStates = new ArrayList<Integer>();

        for (int i = 0; i < mTrapTernaryBtn_List.size(); i++) {
            TrapTernaryButtonClass child = mTrapTernaryBtn_List.get(i);
            allStates.add(child.getStage());
        }

        return allStates;
    }

    public void setAllStates(ArrayList<Integer> allStates) {
        /*******************************************************************************************
         * Function: setAllStates
         *
         * Purpose: Function iterates through all ternary buttons and sets stage
         *
         * Parameters: allStates (IN) - array list of states to set to
         *
         * Returns: None
         *
         ******************************************************************************************/

        for (int i = 0; i < mTrapTernaryBtn_List.size(); i++) {
            TrapTernaryButtonClass child = mTrapTernaryBtn_List.get(i);
            child.setStage(allStates.get(i));
        }

        OnStageChange();
    }

    public void clearBtnDialog(final Context context){
        /*******************************************************************************************
         * Function: clearBtnDialog
         *
         * Purpose: Function creates dialog and prompts user to clear buttons
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        final String DIALOG_TITLE = "Clear Score";
        final String DIALOG_MSG = "Are you sure you want to clear this score?";

        final boolean POSITIVE_BTN = true;  // Right
        final boolean NEUTRAL_BTN = false;   // Left
        final boolean NEGATIVE_BTN = true;  // Middle

        final String POSITIVE_BUTTON_TXT = "CLEAR";
        final String NEUTRAL_BUTTON_TXT = "";
        final String NEGATIVE_BUTTON_TXT = "CANCEL";

        final int POSITIVE_BTN_COLOR = Color.BLUE;
        final int NEUTRAL_BTN_COLOR = Color.RED;
        final int NEGATIVE_BTN_COLOR = Color.RED;

        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Set Dialog Title
        alertDialog.setTitle(DIALOG_TITLE);

        // Set Dialog Message
        alertDialog.setMessage(DIALOG_MSG);

        // Set Buttons
        // Positive Button, Right
        if (POSITIVE_BTN) {
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, POSITIVE_BUTTON_TXT, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Processed with onClick below

                }
            });
        }

        // Neutral Button, Left
        if (NEUTRAL_BTN) {
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, NEUTRAL_BUTTON_TXT, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Processed with onClick below
                }
            });
        }

        // Negative Button, Middle
        if (NEGATIVE_BTN) {
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, NEGATIVE_BUTTON_TXT, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Processed with onClick below
                }
            });
        }


        new Dialog(context);
        alertDialog.show();

        // Set Button Colors
        if (POSITIVE_BTN) {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(POSITIVE_BTN_COLOR);
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Perform Action on Positive button
                    resetTrapCounter();
                    alertDialog.dismiss();
                }
            });
        }
        if (NEUTRAL_BTN) {
            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(NEUTRAL_BTN_COLOR);
            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Perform Action on NEUTRAL Button

                }
            });
        }
        if (NEGATIVE_BTN) {
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(NEGATIVE_BTN_COLOR);
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Perform Action on Negative button
                    alertDialog.dismiss();
                }
            });
        }
    }

    public void setAllStatesToHit() {
        /*******************************************************************************************
         * Function: setAllStatesToHit
         *
         * Purpose: Function sets all states to hit
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        for (int i = 0; i < mTrapTernaryBtn_List.size(); i++) {
            TrapTernaryButtonClass child = mTrapTernaryBtn_List.get(i);
            child.setStage(HIT);
        }

        OnStageChange();
    }

    //************************************** Expand Functions **************************************
    public boolean getExpandBool(){
        /*******************************************************************************************
         * Function: getExpandBool
         *
         * Purpose: Function returns viewExpand_Bool
         *
         * Parameters: None
         *
         * Returns: viewExpand_Bool
         *
         ******************************************************************************************/

        return viewExpand_Bool;
    }

    public void setExpandBool(Boolean expand_Bool){
        /*******************************************************************************************
         * Function: setExpandBool
         *
         * Purpose: Function sets expand bool based on parameter
         *
         * Parameters: expand_Bool (IN) - true if view is to be expanded, false if collapsed
         *
         * Returns: None
         *
         ******************************************************************************************/

        viewExpand_Bool = expand_Bool;

        if (viewExpand_Bool) {
            expandView();
        } else {
            collapseView();
        }
    }

    public void expandView(){
        /*******************************************************************************************
         * Function: expandView
         *
         * Purpose: Function expands view to show all trap buttons
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        int margin_Int = 2;

        // Animate the transition a little
        // TODO: Add transition here

        // Set lanes to vertical
        for (int i = 0; i < mLaneLay_List.size(); i++) {
            mLaneLay_List.get(i).setOrientation(LinearLayout.VERTICAL);
        }

        // Set all trap button size to default
        for (int i = 0; i < mTrapTernaryBtn_List.size(); i++) {
            TrapTernaryButtonClass temp_Btn = mTrapTernaryBtn_List.get(i);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    (int) getContext().getResources().getDimension(R.dimen.CircleTrapButton),
                    (int) getContext().getResources().getDimension(R.dimen.CircleTrapButton));
            params.setMargins(margin_Int,margin_Int,margin_Int,margin_Int);
            temp_Btn.setLayoutParams(params);
        }

        // Set boolean
        viewExpand_Bool = true;

        Log.d(TAG, "Expanding trap view");
    }

    public void collapseView(){
        /*******************************************************************************************
         * Function: collapseView
         *
         * Purpose: Function collapses view to a condensed form
         *
         * Parameters: context (IN) - context for initializing views
         *
         * Returns: None
         *
         ******************************************************************************************/

        int margin_Int = 2;

        // Animate the transition a little
        // TODO: Add transition here

        // Set lanes to horizontal
        for (int i = 0; i < mLaneLay_List.size(); i++) {
            mLaneLay_List.get(i).setOrientation(LinearLayout.HORIZONTAL);
        }

        // Set all trap button size to default
        for (int i = 0; i < mTrapTernaryBtn_List.size(); i++) {
            TrapTernaryButtonClass temp_Btn = mTrapTernaryBtn_List.get(i);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    (int) getContext().getResources().getDimension(R.dimen.SmallCircleTrapButton),
                    (int) getContext().getResources().getDimension(R.dimen.SmallCircleTrapButton));
            params.setMargins(margin_Int,margin_Int,margin_Int,margin_Int);
            temp_Btn.setLayoutParams(params);
        }


        // Set boolean
        viewExpand_Bool = false;

        Log.d(TAG, "Collapsing trap view");
    }

}
