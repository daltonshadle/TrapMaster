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
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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
    private final static String JRW = "JRW";
    private final int NUM_LANES = 5;
    private final int NUM_BUTTONS_PER_LANE = 5;
    private final String TAG = "JRW";

    // General Variables
    private int viewScore_Int = 0;
    private int parentWidth_Int = 0;
    private int parentHeight_Int = 0;
    private boolean viewExpand_Bool;
    private OnTotalHitChange totalHitChange;

    // UI References
    private ArrayList<TrapTernaryButtonClass> mTrapTernaryBtn_List;
    private ArrayList<LinearLayout> mLaneLay_List;
    private ConstraintLayout mWholeView_Lay;
    private TextView mRound_Txt;
    private TextView mUserEmail_Txt;
    private TextView mScore_Txt;
    private Button mMiss_Btn;
    private Button mHit_Btn;
    private Button mClear_Btn;

    //************************************* Public Functions ***************************************
    // Constructors
    public TrapScoreItemClass(Context context) {
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
        mWholeView_Lay = findViewById(R.id.trapScoreItem_Lay);
        mTrapTernaryBtn_List = new ArrayList<>();
        mLaneLay_List = new ArrayList<>();
        mRound_Txt = new TextView(context);
        mUserEmail_Txt = new TextView(context);
        mScore_Txt =  new TextView(context);

        // Initialize whole view layout
        ConstraintLayout.LayoutParams layout_Params = new ConstraintLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mWholeView_Lay.setLayoutParams(layout_Params);

        // Initialize all textviews
        initializeTxtViews();

        // Initialize all layouts and trap ternary
        initializeTrapTernaryBtns(context);

        // Initialize buttons
        initializeBtns(context);

        // Getting view dimensions to set in collapsed mode at start, another poor way to do so
        if (getViewTreeObserver().isAlive()) {
            getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    if (getViewTreeObserver().isAlive()) {
                        getViewTreeObserver().removeOnPreDrawListener(this);
                    }

                    parentHeight_Int = TrapScoreItemClass.this.getHeight();
                    parentWidth_Int = TrapScoreItemClass.this.getWidth();

                    collapseView(context);

                    return true;
                }
            });
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

        ConstraintSet set = new ConstraintSet();
        int topMargin_Int = 10;
        int viewPadding_Int = 10;
        int mainTextSize_Int = 25;
        int secondTextSize_Int = 20;

        // Add score text to center of layout
        mScore_Txt.setId(View.generateViewId());
        mScore_Txt.setText("0");
        mScore_Txt.setTextSize(mainTextSize_Int);
        mScore_Txt.setPadding(viewPadding_Int, viewPadding_Int, viewPadding_Int, viewPadding_Int);
        mWholeView_Lay.addView(mScore_Txt);
        set.clone(mWholeView_Lay);
        set.connect(mScore_Txt.getId(), ConstraintSet.TOP, mWholeView_Lay.getId(), ConstraintSet.TOP, topMargin_Int);
        set.connect(mScore_Txt.getId(), ConstraintSet.LEFT, mWholeView_Lay.getId(), ConstraintSet.LEFT, topMargin_Int);
        set.connect(mScore_Txt.getId(), ConstraintSet.RIGHT, mWholeView_Lay.getId(), ConstraintSet.RIGHT, topMargin_Int);
        set.applyTo(mWholeView_Lay);

        // Add round number text to left side of layout
        mRound_Txt.setId(View.generateViewId());
        mRound_Txt.setText("Round");
        mRound_Txt.setTextSize(secondTextSize_Int);
        mRound_Txt.setPadding(viewPadding_Int, viewPadding_Int, viewPadding_Int, viewPadding_Int);
        mWholeView_Lay.addView(mRound_Txt);
        set.clone(mWholeView_Lay);
        set.connect(mRound_Txt.getId(), ConstraintSet.TOP, mWholeView_Lay.getId(), ConstraintSet.TOP, topMargin_Int);
        set.connect(mRound_Txt.getId(), ConstraintSet.BOTTOM, mScore_Txt.getId(), ConstraintSet.BOTTOM, topMargin_Int);
        set.connect(mRound_Txt.getId(), ConstraintSet.LEFT, mWholeView_Lay.getId(), ConstraintSet.LEFT, topMargin_Int);
        set.applyTo(mWholeView_Lay);

        // Add user email text to layout
        mUserEmail_Txt.setId(View.generateViewId());
        mUserEmail_Txt.setText("User");
        mUserEmail_Txt.setTextSize(secondTextSize_Int);
        mUserEmail_Txt.setPadding(viewPadding_Int, viewPadding_Int, viewPadding_Int, viewPadding_Int);
        mWholeView_Lay.addView(mUserEmail_Txt);
        set.clone(mWholeView_Lay);
        set.connect(mUserEmail_Txt.getId(), ConstraintSet.TOP, mWholeView_Lay.getId(), ConstraintSet.TOP, topMargin_Int);
        set.connect(mUserEmail_Txt.getId(), ConstraintSet.BOTTOM, mScore_Txt.getId(), ConstraintSet.BOTTOM, topMargin_Int);
        set.connect(mUserEmail_Txt.getId(), ConstraintSet.RIGHT, mWholeView_Lay.getId(), ConstraintSet.RIGHT, topMargin_Int);
        set.applyTo(mWholeView_Lay);
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

        ConstraintSet set = new ConstraintSet();
        int margin_Int = 10;
        int padding_Int = 3;

        for (int i = 0; i < NUM_LANES; i++) {
            // Initialize Layout
            LinearLayout temp_Lay = new LinearLayout(context);
            temp_Lay.setId(View.generateViewId());
            temp_Lay.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams temp_Params =
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
            temp_Lay.setLayoutParams(temp_Params);

            for (int j = 0; j < NUM_BUTTONS_PER_LANE; j++) {
                // Initialize Button
                TrapTernaryButtonClass temp_Btn = new TrapTernaryButtonClass(context);
                temp_Btn.setId(View.generateViewId());
                temp_Btn.setPadding(padding_Int, padding_Int, padding_Int, padding_Int);
                temp_Btn.setStageChangeListener(this);
                temp_Lay.addView(temp_Btn);
                mTrapTernaryBtn_List.add(temp_Btn);
            }

            // Add linear layout to list of layouts
            mLaneLay_List.add(temp_Lay);
        }

        // All lanes
        LinearLayout leftMost_Lay = mLaneLay_List.get(0);
        LinearLayout leftCenter_Lay = mLaneLay_List.get(1);
        LinearLayout center_Lay = mLaneLay_List.get(2);
        LinearLayout rightCenter_Lay = mLaneLay_List.get(3);
        LinearLayout rightMost_Lay = mLaneLay_List.get(4);

        // Right most lane
        mWholeView_Lay.addView(leftMost_Lay);
        set.clone(mWholeView_Lay);
        set.connect(leftMost_Lay.getId(), ConstraintSet.TOP, mRound_Txt.getId(), ConstraintSet.BOTTOM, margin_Int);
        set.connect(leftMost_Lay.getId(), ConstraintSet.LEFT, mWholeView_Lay.getId(), ConstraintSet.LEFT, margin_Int);
        set.applyTo(mWholeView_Lay);

        // Left center lane
        mWholeView_Lay.addView(leftCenter_Lay);
        set.clone(mWholeView_Lay);
        set.connect(leftCenter_Lay.getId(), ConstraintSet.TOP, mRound_Txt.getId(), ConstraintSet.BOTTOM, margin_Int);
        set.connect(leftCenter_Lay.getId(), ConstraintSet.LEFT, leftMost_Lay.getId(), ConstraintSet.LEFT, margin_Int);
        set.connect(leftCenter_Lay.getId(), ConstraintSet.RIGHT, center_Lay.getId(), ConstraintSet.RIGHT, margin_Int);
        set.applyTo(mWholeView_Lay);

        // Center lane
        mWholeView_Lay.addView(center_Lay);
        set.clone(mWholeView_Lay);
        set.connect(center_Lay.getId(), ConstraintSet.TOP, mRound_Txt.getId(), ConstraintSet.BOTTOM, margin_Int);
        set.connect(center_Lay.getId(), ConstraintSet.LEFT, mWholeView_Lay.getId(), ConstraintSet.LEFT, margin_Int);
        set.connect(center_Lay.getId(), ConstraintSet.RIGHT, mWholeView_Lay.getId(), ConstraintSet.RIGHT, margin_Int);
        set.applyTo(mWholeView_Lay);

        // Left center lane
        mWholeView_Lay.addView(rightCenter_Lay);
        set.clone(mWholeView_Lay);
        set.connect(rightCenter_Lay.getId(), ConstraintSet.TOP, mRound_Txt.getId(), ConstraintSet.BOTTOM, margin_Int);
        set.connect(rightCenter_Lay.getId(), ConstraintSet.LEFT, center_Lay.getId(), ConstraintSet.LEFT, margin_Int);
        set.connect(rightCenter_Lay.getId(), ConstraintSet.RIGHT, rightMost_Lay.getId(), ConstraintSet.RIGHT, margin_Int);
        set.applyTo(mWholeView_Lay);

        // Right most lane
        mWholeView_Lay.addView(rightMost_Lay);
        set.clone(mWholeView_Lay);
        set.connect(rightMost_Lay.getId(), ConstraintSet.TOP, mRound_Txt.getId(), ConstraintSet.BOTTOM, margin_Int);
        set.connect(rightMost_Lay.getId(), ConstraintSet.RIGHT, mWholeView_Lay.getId(), ConstraintSet.RIGHT, margin_Int);
        set.applyTo(mWholeView_Lay);
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

        ConstraintSet set = new ConstraintSet();
        int margin_Int = 10;
        int padding_Int = 3;
        int buttonHeight_Int = 80;
        int textSize_Int = 10;

        LinearLayout refLane_Lay = mLaneLay_List.get(0);

        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, buttonHeight_Int, 1.0f);
        btnParams.setMargins(margin_Int, 0, margin_Int, 0);
        ConstraintLayout.LayoutParams layParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayout btn_Lay = new LinearLayout(context);
        btn_Lay.setId(View.generateViewId());
        btn_Lay.setOrientation(LinearLayout.HORIZONTAL);
        btn_Lay.setLayoutParams(layParams);

        // Init hit button
        mHit_Btn = new Button(context);
        mHit_Btn.setId(View.generateViewId());
        mHit_Btn.setText("HIT");
        mHit_Btn.setTextSize(textSize_Int);
        mHit_Btn.setPadding(padding_Int, padding_Int, padding_Int, padding_Int);
        mHit_Btn.setBackgroundColor(ContextCompat.getColor(context, R.color.hit));
        mHit_Btn.setLayoutParams(btnParams);

        mHit_Btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setNextChild(HIT);
            }
        });

        // Init miss button
        mMiss_Btn = new Button(context);
        mMiss_Btn.setId(View.generateViewId());
        mMiss_Btn.setText("MISS");
        mMiss_Btn.setTextSize(textSize_Int);
        mMiss_Btn.setPadding(padding_Int, padding_Int, padding_Int, padding_Int);
        mMiss_Btn.setBackgroundColor(ContextCompat.getColor(context, R.color.miss));
        mMiss_Btn.setLayoutParams(btnParams);

        mMiss_Btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setNextChild(MISS);
            }
        });

        // Init clear button
        mClear_Btn = new Button(context);
        mClear_Btn.setId(View.generateViewId());
        mClear_Btn.setText("CLEAR");
        mClear_Btn.setTextSize(textSize_Int);
        mClear_Btn.setPadding(padding_Int, padding_Int, padding_Int, padding_Int);
        mClear_Btn.setBackgroundColor(ContextCompat.getColor(context, R.color.neutral));
        mClear_Btn.setLayoutParams(btnParams);

        mClear_Btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clearBtnDialog(context);
            }
        });

        btn_Lay.addView(mClear_Btn);
        btn_Lay.addView(mMiss_Btn);
        btn_Lay.addView(mHit_Btn);

        // Set btn_lay in layout
        mWholeView_Lay.addView(btn_Lay);
        set.clone(mWholeView_Lay);
        set.connect(btn_Lay.getId(), ConstraintSet.TOP, refLane_Lay.getId(), ConstraintSet.BOTTOM, margin_Int);
        set.applyTo(mWholeView_Lay);
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

    public void setUserEmailText(String email_Str) {
        /*******************************************************************************************
         * Function: setRoundText
         *
         * Purpose: Function sets the text for the user email text view
         *
         * Parameters: email_Str (IN) - user email to set
         *
         * Returns: None
         *
         ******************************************************************************************/

        if (email_Str.contains("@")) {
            int atIndex_Int = email_Str.indexOf("@");
            email_Str = email_Str.substring(0, atIndex_Int);
        }

        mUserEmail_Txt.setText(email_Str);
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

    public void expandView(Context context){
        /*******************************************************************************************
         * Function: expandView
         *
         * Purpose: Function expands view to show all trap buttons
         *
         * Parameters: context (IN) - context for initializing views
         *
         * Returns: None
         *
         ******************************************************************************************/

        // Set lanes to vertical
        for (int i = 0; i < mLaneLay_List.size(); i++) {
            mLaneLay_List.get(i).setOrientation(LinearLayout.VERTICAL);
        }

        // Set all trap button size to default
        for (int i = 0; i < mTrapTernaryBtn_List.size(); i++) {
            TrapTernaryButtonClass temp_Btn = mTrapTernaryBtn_List.get(i);
            temp_Btn.setSize((int)context.getResources().getDimension(R.dimen.CircleTrapButton));
        }

        // Set boolean
        viewExpand_Bool = true;

        Log.d(JRW, "Expanding trap view");
    }

    public void collapseView(Context context){
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

        int width_Int = parentWidth_Int/35;

        // Set lanes to horizontal
        for (int i = 0; i < mLaneLay_List.size(); i++) {
            mLaneLay_List.get(i).setOrientation(LinearLayout.HORIZONTAL);
        }

        // Set all trap button size to default
        for (int i = 0; i < mTrapTernaryBtn_List.size(); i++) {
            TrapTernaryButtonClass temp_Btn = mTrapTernaryBtn_List.get(i);
            temp_Btn.setSize(width_Int);
        }

        // Set boolean
        viewExpand_Bool = false;

        Log.d(JRW, "Collapsing trap view");
    }

}
