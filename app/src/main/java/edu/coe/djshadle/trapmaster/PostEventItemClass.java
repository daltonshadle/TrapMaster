/***************************************************************************************************
 * FILENAME : PostEventItemClass.java
 *
 * AUTHOR : Dalton Shadle
 *
 * DESCRIPTION : Holds variables and functions for PostEventItemClass object
 *
 * NOTES : N/A
 *
 * Copyright Dalton Shadle 2019.  All rights reserved.
 *
 **************************************************************************************************/

package edu.coe.djshadle.trapmaster;

//******************************************** Imports *********************************************

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class PostEventItemClass extends ConstraintLayout {
    //********************************** Variables and Constants ***********************************
    // General Constants
    private String SHOOTER_NAME;
    private String SHOOTER_COACH_NAME;
    private int SHOOTER_SCORE;

    // General Variables

    // UI References
    TextView shooterName_Txt;
    TextView shooterScore_Txt;
    EditText shooterNotes_Edt;
    Spinner shooterGun_Spin;
    Spinner shooterLoad_Spin;

    //************************************* Public Functions ***************************************
    // Constructors
    public PostEventItemClass(Context context) {
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

    public PostEventItemClass(Context context, AttributeSet attrs) {
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

    public PostEventItemClass(Context context, AttributeSet attrs, int defStyleAttr) {
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

    public PostEventItemClass(Context context, String shooterName_Str, String shooterCoachName_Str,
                              int shooterScore_Int) {
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
        SHOOTER_NAME = shooterName_Str;
        SHOOTER_COACH_NAME = shooterCoachName_Str;
        SHOOTER_SCORE = shooterScore_Int;
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
        inflater.inflate(R.layout.view_post_event_item, this);

        // Initialize Views
        shooterName_Txt = findViewById(R.id.postEventShooterName_Txt);
        shooterScore_Txt = findViewById(R.id.postEventShooterScore_Txt);
        shooterNotes_Edt = findViewById(R.id.postEventNotes_Edt);
        shooterGun_Spin = findViewById(R.id.postEventGun_Spin);
        shooterLoad_Spin = findViewById(R.id.postEventLoad_Spin);
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

    //************************************** Other Functions ***************************************
    public void setShooterName(String shooterName_Str) {
        SHOOTER_NAME = shooterName_Str;
        shooterName_Txt.setText(SHOOTER_NAME);
    }

    public String getShooterName() {
        return SHOOTER_NAME;
    }

    public void setShooterScore(int score_Int) {
        SHOOTER_SCORE = score_Int;
        shooterScore_Txt.setText(Integer.toString(SHOOTER_SCORE) + "/25");
    }

    public int getShooterScore() {
        return SHOOTER_SCORE;
    }

    public void setShooterCoach(String shooterCoach_Str) {
        SHOOTER_COACH_NAME = shooterCoach_Str;
    }

    public String getShooterCoach() {
        return SHOOTER_COACH_NAME;
    }


}
