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

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PostEventItemClass extends ConstraintLayout {
    //********************************** Variables and Constants ***********************************
    // General Constants
    

    // General Variables
    private int profileID_Int;
    private String shooterName_Str;
    private int shooterScore_Int;
    private GunClass shooter_Gun;
    private LoadClass shooter_Load;
    private String shooterNotes_Str;

    private Boolean expand_Bool = false;

    // UI References
    TextView shooterName_Txt;
    TextView shooterScore_Txt;
    TextView shooterGun_Txt;
    TextView shooterLoad_Txt;
    TextView shooterNotes_Txt;


    //************************************* Public Functions ***************************************
    // Constructors
    public PostEventItemClass(Context context) {
        /*******************************************************************************************
         * Function: PostEventItemClass
         *
         * Purpose: Constructor for this class
         *
         * Parameters: context (IN) - fill_in
         *
         * Returns: None
         *
         ******************************************************************************************/

        super(context);
        this.profileID_Int = -1;
        initializeViews(context);
    }

    public PostEventItemClass(Context context, int profileID_Int) {
        /*******************************************************************************************
         * Function: PostEventItemClass
         *
         * Purpose: Constructor for this class, USE THIS CONSTRUCTOR FOR PROFILE_ID
         *
         * Parameters: context (IN) - fill_in
         *             profileID_Int (IN) - profile database ID necessary for functions
         *
         * Returns: None
         *
         ******************************************************************************************/

        super(context);
        this.profileID_Int = profileID_Int;
        initializeViews(context);
    }

    public PostEventItemClass(Context context, AttributeSet attrs) {
        /*******************************************************************************************
         * Function: PostEventItemClass
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
         * Function: PostEventItemClass
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
        shooterScore_Txt = findViewById(R.id.postEventScore_Txt);
        shooterGun_Txt = findViewById(R.id.postEventItemGun_Txt);
        shooterLoad_Txt = findViewById(R.id.postEventItemLoad_Txt);
        shooterNotes_Txt = findViewById(R.id.postEventItemNotes_Txt);
        final LinearLayout shooterDetails_Lay = findViewById(R.id.postEventItemDetails_Lay);
        
        // Initialize buttons
        Button gun_Btn = findViewById(R.id.postEventItemGun_Btn);
        gun_Btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog gun_Dialog = shooter_Gun.chooseGunDialog(context, profileID_Int);
                gun_Dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        PostEventItemClass.this.setShooter_Gun(shooter_Gun);
                    }
                });
            }
        });

        Button load_Btn = findViewById(R.id.postEventItemLoad_Btn);
        load_Btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog load_Dialog = shooter_Load.chooseGunDialog(context, profileID_Int);
                load_Dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        PostEventItemClass.this.setShooter_Load(shooter_Load);
                    }
                });
            }
        });

        Button note_Btn = findViewById(R.id.postEventItemNotes_Btn);
        note_Btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                notesDialog(context);
            }
        });

        final Button expand_Btn = findViewById(R.id.postEventItemExpand_Btn);
        expand_Btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (expand_Bool) {
                    // view is expanded, collapse view
                    shooterDetails_Lay.setVisibility(GONE);
                    expand_Btn.setBackground(ContextCompat.getDrawable(context, R.drawable.down_caret_icon));
                } else {
                    // view is collapsed, expand view
                    shooterDetails_Lay.setVisibility(VISIBLE);
                    expand_Btn.setBackground(ContextCompat.getDrawable(context, R.drawable.up_caret_icon));
                }
                expand_Bool = !expand_Bool;
            }
        });
        
        // Initialize class variables
        shooter_Gun = new GunClass();
        shooter_Load = new LoadClass();
        shooterNotes_Str = "";
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

    //************************************ Setters and Getters *************************************
    public void setShooterName(String name_Str) {
        shooterName_Str = name_Str;
        shooterName_Txt.setText(shooterName_Str);
    }

    public String getShooterName() {
        return shooterName_Str;
    }

    public void setShooterScore(int score_Int) {
        shooterScore_Int = score_Int;
        shooterScore_Txt.setText(Integer.toString(shooterScore_Int));
    }

    public int getShooterScore() {
        return shooterScore_Int;
    }

    public GunClass getShooter_Gun() {
        return shooter_Gun;
    }

    public void setShooter_Gun(GunClass shooter_Gun) {
        this.shooter_Gun = shooter_Gun;
        if (this.shooter_Gun.getGunNickname_Str().isEmpty()) {
            this.shooter_Gun.setGunNickname_Str("None");
        }
        String tempGun_Str = "Gun: " + this.shooter_Gun.getGunNickname_Str();
        shooterGun_Txt.setText(tempGun_Str);
    }

    public LoadClass getShooter_Load() {
        return shooter_Load;
    }

    public void setShooter_Load(LoadClass shooter_Load) {
        this.shooter_Load = shooter_Load;
        if (this.shooter_Load.getLoadNickname_Str().isEmpty()) {
            this.shooter_Load.setLoadNickname_Str("None");
        }
        String tempLoad_Str = "Load: " + this.shooter_Load.getLoadNickname_Str();
        shooterLoad_Txt.setText(tempLoad_Str);
    }

    public String getShooterNotes_Str() {
        return shooterNotes_Str;
    }

    public void setShooterNotes_Str(String shooterNotes_Str) {
        this.shooterNotes_Str = shooterNotes_Str;
        if (this.shooterNotes_Str.isEmpty()) {
            this.shooterNotes_Str = "None";
        }
        String tempNotes_Str = "Notes: " + this.shooterNotes_Str;
        shooterNotes_Txt.setText(tempNotes_Str);
    }

    public int getProfileID_Int() {
        return profileID_Int;
    }

    public void setProfileID_Int(int profileID_Int) {
        this.profileID_Int = profileID_Int;
    }

    //*************************************** Other Functions **************************************
    public void notesDialog(final Context context) {
        /*******************************************************************************************
         * Function: notesDialog
         *
         * Purpose: Function creates dialog and prompts user to add notes to shoot
         *
         * Parameters: context (IN) - Supplies the activity context to display dialog to
         *
         * Returns: The string of notes from this dialog will be saved off
         *
         ******************************************************************************************/

        // Dialog Constants
        String DIALOG_TITLE = "Notes";
        int POSITIVE_BTN_COLOR = Color.BLUE;
        int NEUTRAL_BTN_COLOR = Color.RED;

        // Dialog Variables
        GlobalApplicationContext currentContext = new GlobalApplicationContext();
        final DBHandler db = new DBHandler(currentContext.getContext());
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Set Dialog Title
        alertDialog.setTitle(DIALOG_TITLE);

        // Set Dialog Message
        alertDialog.setMessage("Add any notes for this round.");

        // Set layout for gathering information
        final RelativeLayout subView_RelLay = new RelativeLayout(context);

        // Set views
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final EditText notes_Edt = new EditText(context);
        notes_Edt.setLayoutParams(params);
        notes_Edt.setHint("\"It was a good round!\"");
        subView_RelLay.addView(notes_Edt);

        // Add linear layout to alert dialog
        alertDialog.setView(subView_RelLay);

        // Set Buttons
        // Positive Button, Right
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Processed by onClick below
            }
        });

        // Neutral Button, Left
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Processed by onClick below
            }
        });


        new Dialog(context);
        alertDialog.show();

        // Set Buttons
        final Button pos_Btn = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        final Button neu_Btn = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);

        pos_Btn.setTextColor(POSITIVE_BTN_COLOR);
        pos_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform Action on Positive button
                shooterNotes_Str = notes_Edt.getText().toString();
                PostEventItemClass.this.setShooterNotes_Str(shooterNotes_Str);

                alertDialog.dismiss();
            }
        });

        neu_Btn.setTextColor(NEUTRAL_BTN_COLOR);
        neu_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform Action on Neutral button
                alertDialog.dismiss();
            }

        });
    }
}
