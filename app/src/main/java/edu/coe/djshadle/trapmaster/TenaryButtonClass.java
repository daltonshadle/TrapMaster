package edu.coe.djshadle.trapmaster;

import android.content.Context;
import android.support.annotation.Nullable;
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
        super(context);
        initializeViews(context);
    }

    public TenaryButtonClass(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    public TenaryButtonClass(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(context);
    }

    // Other Functions
    private void initializeViews(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_ternary_button, this);
        btnStage_Int = NEUTRAL;
    }

    protected void onFinishInflate(){
        super.onFinishInflate();

        mButton_View = (Button) findViewById(R.id.btnTernaryButton);
        mButton_View.setOnClickListener(this);

        setButtonColor(btnStage_Int);
    }

    private void setStage(){
        btnStage_Int = (btnStage_Int + 1) % 3;
        setButtonColor(btnStage_Int);
        invalidate();
    }

    private void setButtonColor(int stage1){
        switch (stage1) {
            case HIT:
                mButton_View.setText("HIT");
                btnColor_Int = getContext().getResources().getColor(R.color.hit);
                break;
            case MISS:
                mButton_View.setText("MISS");
                btnColor_Int = getContext().getResources().getColor(R.color.miss);
                break;
            case NEUTRAL:
                mButton_View.setText("SHOT");
                btnColor_Int = getContext().getResources().getColor(R.color.neutral);
                break;
            default:
                mButton_View.setText("DEFAULT");
                break;
        }

        mButton_View.setBackgroundColor(btnColor_Int);
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

    @Override
    public void onClick(View v) {
        setStage();
        stageChangeListener.OnStageChange();
    }

    public void setStageChangeListener(OnStageChangeListener eventListener){
        stageChangeListener = eventListener;
    }

    public void resetTernary(){
        btnStage_Int = NEUTRAL;
        setButtonColor(btnStage_Int);
        invalidate();
    }
}
