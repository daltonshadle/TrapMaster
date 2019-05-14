/***************************************************************************************************
 * FILENAME : TrapTernaryButtonClass.java
 *
 * AUTHOR : Dalton Shadle
 *
 * DESCRIPTION : Holds variables and functions for TrapTernaryButtonClass object
 *
 * NOTES : N/A
 *
 * Copyright Dalton Shadle 2019.  All rights reserved.
 *
 **************************************************************************************************/

package edu.coe.djshadle.trapmaster;

//******************************************** Imports *********************************************


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class TrapTernaryButtonClass extends RelativeLayout implements View.OnClickListener {
    //********************************** Variables and Constants ***********************************
    // General Constants
    private final static int HIT = 0, MISS = 1, NEUTRAL = 2;

    // General Variables
    private int btnStage_Int;
    private Drawable btnBackground_Draw;

    // UI References
    private OnStageChangeListener stageChangeListener;
    private Button mTrapTernary_Btn;

    //************************************* Public Functions ***************************************
    // Constructors
    public TrapTernaryButtonClass(Context context) {
        /*******************************************************************************************
         * Function: TrapTernaryButtonClass
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

    public TrapTernaryButtonClass(Context context, AttributeSet attrs) {
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

    public TrapTernaryButtonClass(Context context, AttributeSet attrs, int defStyleAttr) {
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
    private void initializeViews(Context context){
        /*******************************************************************************************
         * Function: initializeViews
         *
         * Purpose: Function initializes views for this ternary button
         *
         * Parameters: context (IN) - fill_in
         *
         * Returns: None
         *
         ******************************************************************************************/

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        inflater.inflate(R.layout.view_trap_ternary_button, this);
        btnStage_Int = NEUTRAL;

        // Initialize Button
        mTrapTernary_Btn = findViewById(R.id.trapTernary_Btn);
        mTrapTernary_Btn.setOnClickListener(this);

        // Set Stage
        setButtonColor(btnStage_Int);
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
    }

    @Override
    public void onClick(View v) {
        /*******************************************************************************************
         * Function: onClick
         *
         * Purpose: Function implements functionality when view is clicked
         *
         * Parameters: v (IN) - view that is clicked
         *
         * Returns: None
         *
         ******************************************************************************************/

        // Increment stage and and call stage change listener
        incrementStage();
        stageChangeListener.OnStageChange();
    }

    public void setStageChangeListener(OnStageChangeListener eventListener){
        /*******************************************************************************************
         * Function: setStageChangeListener
         *
         * Purpose: Function sets on stage change listener for trap ternary button
         *
         * Parameters: eventListener (IN) - FIL_IN
         *
         * Returns: None
         *
         ******************************************************************************************/

        stageChangeListener = eventListener;
    }

    //************************************** Other Functions ***************************************
    public void incrementStage(){
        /*******************************************************************************************
         * Function: incrementStage
         *
         * Purpose: Function increments the stage by 1 for trap ternary button
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        btnStage_Int = (btnStage_Int + 1) % 3;
        setButtonColor(btnStage_Int);
        invalidate();
    }

    private void setButtonColor(int stage){
        /*******************************************************************************************
         * Function: setButtonColor
         *
         * Purpose: Function sets the background for button
         *
         * Parameters: stage (IN) - stage to set the button to
         *
         * Returns: None
         *
         ******************************************************************************************/

        switch (stage) {
            case HIT:
                btnBackground_Draw = ResourcesCompat.getDrawable(getResources(), R.drawable.hit_trap_ternary_button_shape, null);
                break;
            case MISS:
                btnBackground_Draw = ResourcesCompat.getDrawable(getResources(), R.drawable.miss_trap_ternary_button_shape, null);
                break;
            case NEUTRAL:
                btnBackground_Draw = ResourcesCompat.getDrawable(getResources(), R.drawable.neutral_trap_ternary_button_shape, null);
                break;
            default:
                btnBackground_Draw = ResourcesCompat.getDrawable(getResources(), R.drawable.default_trap_ternary_button_shape, null);
                break;
        }

        mTrapTernary_Btn.setBackground(btnBackground_Draw);
    }

    public int getStage(){
        /*******************************************************************************************
         * Function: getStage
         *
         * Purpose: Function gets stage
         *
         * Parameters: None
         *
         * Returns: btnStage_Int
         *
         ******************************************************************************************/

        return btnStage_Int;
    }

    public void setStage(int stage){
        /*******************************************************************************************
         * Function: setStage
         *
         * Purpose: Function sets the stage for trap ternary button with status
         *
         * Parameters: stage (IN) - stage to set the button to
         *
         * Returns: None
         *
         ******************************************************************************************/

        if (stage >= HIT && stage <= NEUTRAL) {
            btnStage_Int = stage;
            setButtonColor(btnStage_Int);
            invalidate();
        }
    }

    public boolean isHit() {
        /*******************************************************************************************
         * Function: isHit
         *
         * Purpose: Function returns true if the button stage is HIT
         *
         * Parameters: None
         *
         * Returns: Returns true if the button stage is HIT
         *
         ******************************************************************************************/

        return (btnStage_Int == HIT);
    }

    public boolean isMiss() {
        /*******************************************************************************************
         * Function: isMiss
         *
         * Purpose: Function returns true if the button stage is MISS
         *
         * Parameters: None
         *
         * Returns: Returns true if the button stage is MISS
         *
         ******************************************************************************************/

        return (btnStage_Int == MISS);
    }

    public boolean isNeutral() {
        /*******************************************************************************************
         * Function: isNeutral
         *
         * Purpose: Function returns true if the button stage is NEUTRAL
         *
         * Parameters: None
         *
         * Returns: Returns true if the button stage is NEUTRAL
         *
         ******************************************************************************************/

        return (btnStage_Int == NEUTRAL);
    }

    public void resetTernary(){
        /*******************************************************************************************
         * Function: resetTernary
         *
         * Purpose: Function resets the stage of the button to NEUTRAL
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        btnStage_Int = NEUTRAL;
        setButtonColor(btnStage_Int);
        invalidate();
    }

    public void setSize(int size_Int){
        /*******************************************************************************************
         * Function: setSize
         *
         * Purpose: Function sets the size of the button
         *
         * Parameters: size_Int (IN) - dimension size for the button
         *
         * Returns: None
         *
         ******************************************************************************************/

        LayoutParams params = new LayoutParams(size_Int, size_Int);
        mTrapTernary_Btn.setLayoutParams(params);
    }
}
