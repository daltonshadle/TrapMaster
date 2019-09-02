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
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class TrapStationItemClass extends ConstraintLayout implements OnStageChangeListener {
    //********************************** Variables and Constants ***********************************
    // General Constants
    private final static int HIT = 0, MISS = 1, NEUTRAL = 2;
    private final static String TAG = "JRW";

    // General Variables
    private int viewScore_Int = 0;
    private int stationNum_Int = 0;
    private OnTotalHitChange totalHitChange;

    // UI References
    private ArrayList<TrapTernaryButtonClass> mTrapTernaryBtn_List;
    private ConstraintLayout mWholeConstraint_Lay;
    private ConstraintLayout mLaneConstraint_Lay;
    private TextView mStation_Txt;
    private TextView mShooter_Txt;
    private TextView mScore_Txt;

    //************************************* Public Functions ***************************************
    // Constructors
    public TrapStationItemClass(Context context) {
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

    public TrapStationItemClass(Context context, AttributeSet attrs) {
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

    public TrapStationItemClass(Context context, AttributeSet attrs, int defStyleAttr) {
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
        inflater.inflate(R.layout.view_trap_station_item, this);

        // Initialize all variables
        mTrapTernaryBtn_List = new ArrayList<>();
        mWholeConstraint_Lay = findViewById(R.id.trapStationItem_Lay);
        mLaneConstraint_Lay = findViewById(R.id.trapStationLane_Lay);
        mStation_Txt = findViewById(R.id.trapStationNumber_txt);
        mShooter_Txt = findViewById(R.id.trapStationShooterName_txt);
        mScore_Txt =  findViewById(R.id.trapStationScore_txt);

        // Initialize all textviews
        initializeTxtViews();

        // Initialize all layouts and trap ternary
        initializeTrapTernaryBtns(context);
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

        // Add all buttons to list by ID
        for (int i = 0; i < mLaneConstraint_Lay.getChildCount(); i++) {
            // Initialize Button
            TrapTernaryButtonClass temp_Btn = findViewById(mLaneConstraint_Lay.getChildAt(i).getId());
            temp_Btn.setStageChangeListener(this);
            mTrapTernaryBtn_List.add(temp_Btn);
        }
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

    public void setStation_Txt(int stationNum_Int) {
        /*******************************************************************************************
         * Function: setStation_Txt
         *
         * Purpose: Function sets the text for the station text view
         *
         * Parameters: stationNum_Int (IN) - station number
         *
         * Returns: None
         *
         ******************************************************************************************/

        mStation_Txt.setText("Station " + Integer.toString(stationNum_Int));
        this.stationNum_Int = stationNum_Int;
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

    public int getStationNum_Int() {
        /*******************************************************************************************
         * Function: getStationNum_Int
         *
         * Purpose: Function returns the station number
         *
         * Parameters: None
         *
         * Returns: stationNum_Int (OUT) - station number
         *
         ******************************************************************************************/

        return stationNum_Int;
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

}
