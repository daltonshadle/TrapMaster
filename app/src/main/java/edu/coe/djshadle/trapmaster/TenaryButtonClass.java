/***************************************************************************************************
 * FILENAME : TernaryButtonClass.java
 *
 * AUTHOR : Dalton Shadle
 *
 * DESCRIPTION : Holds variables and functions for TernaryButtonClass object
 *
 * NOTES : N/A
 *
 * Copyright Dalton Shadle 2019.  All rights reserved.
 *
 **************************************************************************************************/

package edu.coe.djshadle.trapmaster;

//******************************************** Imports *********************************************
import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class TenaryButtonClass extends LinearLayout implements View.OnClickListener {
    //********************************** Variables and Constants ***********************************
    // General Constants
    private final static int HIT = 0, MISS = 1, NEUTRAL = 2;

    // General Variables
    private int btnStage_Int;
    private int btnColor_Int;

    // UI References
    private OnStageChangeListener stageChangeListener;
    private Button mButton_View;

    //************************************* Public Functions ***************************************
    // Constructors
    public TenaryButtonClass(Context context) {
        /*******************************************************************************************
         * Function: TernaryButtonClass
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

    public TenaryButtonClass(Context context, AttributeSet attrs) {
        /*******************************************************************************************
         * Function: TernaryButtonClass
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

    public TenaryButtonClass(Context context, AttributeSet attrs, int defStyleAttr) {
        /*******************************************************************************************
         * Function: TernaryButtonClass
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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        inflater.inflate(R.layout.view_ternary_button, this);
        btnStage_Int = NEUTRAL;
    }

    protected void onFinishInflate(){
        super.onFinishInflate();

        mButton_View = findViewById(R.id.btnTernaryButton);
        mButton_View.setOnClickListener(this);

        setButtonColor(btnStage_Int);
    }

    @Override
    public void onClick(View v) {
        incrementStage();
        stageChangeListener.OnStageChange();
    }

    public void setStageChangeListener(OnStageChangeListener eventListener){
        stageChangeListener = eventListener;
    }

    //************************************** Other Functions ***************************************
    public void incrementStage(){
        btnStage_Int = (btnStage_Int + 1) % 3;
        setButtonColor(btnStage_Int);
        invalidate();
    }

    public void setStage(int status){
        if(status >= HIT && status <= NEUTRAL) {
            btnStage_Int = status;
            setButtonColor(btnStage_Int);
            invalidate();
        }
    }

    private void setButtonColor(int stage1){
        switch (stage1) {
            case HIT:
                mButton_View.setText(getContext().getResources().getString(R.string.hit_string));
                btnColor_Int = getContext().getResources().getColor(R.color.hit);
                break;
            case MISS:
                mButton_View.setText(getContext().getResources().getString(R.string.miss_string));
                btnColor_Int = getContext().getResources().getColor(R.color.miss);
                break;
            case NEUTRAL:
                mButton_View.setText(getContext().getResources().getString(R.string.shot_string));
                btnColor_Int = getContext().getResources().getColor(R.color.neutral);
                break;
            default:
                mButton_View.setText("DEFAULT");
                break;
        }

        mButton_View.setBackgroundTintList(ColorStateList.valueOf(btnColor_Int));
    }

    public int getStage(){
        return btnStage_Int;
    }

    public boolean isHit() {
        return (btnStage_Int == HIT);
    }

    public boolean isMiss() {
        return (btnStage_Int == MISS);
    }

    public boolean isNeutral() {
        return (btnStage_Int == NEUTRAL);
    }

    public void resetTernary(){
        btnStage_Int = NEUTRAL;
        setButtonColor(btnStage_Int);
        invalidate();
    }
}
