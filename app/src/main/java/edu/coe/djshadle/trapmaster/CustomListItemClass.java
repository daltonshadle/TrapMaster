/***************************************************************************************************
 * FILENAME : CustomListItemClass.java
 *
 * AUTHOR : Dalton Shadle
 *
 * DESCRIPTION : Holds variables and functions for CustomListItemClass object
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
import android.util.EventLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CustomListItemClass extends ConstraintLayout {
    //********************************** Variables and Constants ***********************************
    // General Constants
    private final String TAG = "JRW";
    private final int MATCH_LIST_TAG = 1;
    private final int EVENT_LIST_TAG = 2;
    private final int GUN_LIST_TAG = 3;
    private final int LOAD_LIST_TAG = 4;

    // General Variables
    private int profileID_Int;
    private int shooterID_Int;
    private Boolean expand_Bool = false;
    private EventClass mEvent;
    private MatchClass mMatch;
    private GunClass mGun;
    private LoadClass mLoad;
    private int typeTag_Int = -1;

    // UI References
    TextView listItemMain_Txt;
    TextView listItemSec_Txt;
    TextView listItemScore_Txt;
    TextView listItemDetails_Txt;
    ImageView listItem_Image;
    LinearLayout listItemDetails_Lay;


    //************************************* Public Functions ***************************************
    // Constructors
    public CustomListItemClass(Context context) {
        /*******************************************************************************************
         * Function: CustomListItemClass
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
        this.shooterID_Int = -1;
        initializeViews(context);
    }

    public CustomListItemClass(Context context, int profileID_Int, int shooterID_Int, Object type_Object) {
        /*******************************************************************************************
         * Function: CustomListItemClass
         *
         * Purpose: Constructor for this class, USE THIS CONSTRUCTOR FOR PROFILE_ID
         *
         * Parameters: context (IN) - fill_in
         *             profileID_Int (IN) - profile database ID necessary for functions
         *             shooterID_Int (IN) - shooter database ID necessary for functions
         *             type_Object (IN) - gives the object that is going to be used this card, can be
         *                                MatchClass, EventClass, GunClass, LoadClass
         *
         * Returns: None
         *
         ******************************************************************************************/

        super(context);
        this.profileID_Int = profileID_Int;
        this.shooterID_Int = shooterID_Int;

        if (type_Object instanceof MatchClass) {
            mMatch = (MatchClass) type_Object;
            typeTag_Int = MATCH_LIST_TAG;
        } else if (type_Object instanceof EventClass) {
            mEvent = (EventClass) type_Object;
            typeTag_Int = EVENT_LIST_TAG;
        } else if (type_Object instanceof GunClass) {
            mGun = (GunClass) type_Object;
            typeTag_Int = GUN_LIST_TAG;
        } else if (type_Object instanceof LoadClass) {
            mLoad = (LoadClass) type_Object;
            typeTag_Int = LOAD_LIST_TAG;
        }

        initializeViews(context);
    }

    public CustomListItemClass(Context context, AttributeSet attrs) {
        /*******************************************************************************************
         * Function: CustomListItemClass
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

    public CustomListItemClass(Context context, AttributeSet attrs, int defStyleAttr) {
        /*******************************************************************************************
         * Function: CustomListItemClass
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
        inflater.inflate(R.layout.view_custom_list_item, this);

        // Initialize Views
        listItemMain_Txt = findViewById(R.id.listItemMain_Txt);
        listItemSec_Txt = findViewById(R.id.listItemSecond_Txt);
        listItemScore_Txt = findViewById(R.id.listItemScore_Txt);
        listItemDetails_Txt = findViewById(R.id.listItemDetails_Txt);
        listItem_Image = findViewById(R.id.listItem_Img);
        listItemDetails_Lay = findViewById(R.id.listItemDetails_Lay);

        // Initialize buttons
        ImageButton edit_Btn = findViewById(R.id.listItemEdit_Btn);
        edit_Btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (typeTag_Int) {
                    case MATCH_LIST_TAG:
                        mMatch.editMatchDialog(context).setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                setMatchItem();
                            }
                        });
                        break;
                    case EVENT_LIST_TAG:
                        mEvent.editEventDialog(context).setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                setEventItem();
                            }
                        });
                        break;
                    case GUN_LIST_TAG:
                        mGun.editGunDialog(context).setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                setGunItem();
                            }
                        });
                        break;
                    case LOAD_LIST_TAG:
                        mLoad.editLoadDialog(context).setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                setLoadItem();
                            }
                        });
                        break;
                }
            }
        });

        ImageButton delete_Btn = findViewById(R.id.listItemDelete_Btn);
        delete_Btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialize alert dialog for a dismiss listener
                AlertDialog delete_Dialog = new AlertDialog.Builder(context).create();

                // Find case of object type
                switch (typeTag_Int) {
                    case MATCH_LIST_TAG:
                        delete_Dialog = mMatch.removeMatchItemDialog(context);
                        break;
                    case EVENT_LIST_TAG:
                        delete_Dialog = mEvent.removeEventItemDialog(context);
                        break;
                    case GUN_LIST_TAG:
                        delete_Dialog = mGun.removeGunDialog(context);
                        break;
                    case LOAD_LIST_TAG:
                        delete_Dialog = mLoad.removeLoadItemDialog(context);
                        break;
                }

                delete_Dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        // Remove view from layout
                        ((ViewGroup) CustomListItemClass.this.getParent()).removeView(CustomListItemClass.this);
                    }
                });
            }
        });

        final Button expand_Btn = findViewById(R.id.listItemExpand_Btn);
        expand_Btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (expand_Bool) {
                    // view is expanded, collapse view
                    listItemDetails_Lay.setVisibility(GONE);
                    expand_Btn.setBackground(ContextCompat.getDrawable(context, R.drawable.down_caret_icon));
                } else {
                    // view is collapsed, expand view
                    listItemDetails_Lay.setVisibility(VISIBLE);
                    expand_Btn.setBackground(ContextCompat.getDrawable(context, R.drawable.up_caret_icon));
                }
                expand_Bool = !expand_Bool;
            }
        });

        listItem_Image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, context.getString(R.string.feature_will_be_implemented_toast), Toast.LENGTH_LONG).show();
            }
        });

        // Set the item based on type
        switch (typeTag_Int) {
            case MATCH_LIST_TAG:
                setMatchItem();
                break;
            case EVENT_LIST_TAG:
                setEventItem();
                break;
            case GUN_LIST_TAG:
                setGunItem();
                break;
            case LOAD_LIST_TAG:
                setLoadItem();
                break;
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
    }

    private void setMatchItem(){
        /*******************************************************************************************
         * Function: setMatchItem
         *
         * Purpose: Function set the layout for the item
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        // Get most recent db handler
        GlobalApplicationContext currentContext = new GlobalApplicationContext();
        final DBHandler db = new DBHandler(currentContext.getContext());

        // Set textviews
        listItemMain_Txt.setText(db.getShooterInDB(mMatch.getMatchShooterID_Int()).getShooterName_Str());
        listItemSec_Txt.setText(db.getEventInDB(mMatch.getMatchEventID_Int()).getEventName_Str());
        String score_Str = mMatch.getMatchScore_Int() + "/" + mMatch.getMatchTotalShots();
        listItemScore_Txt.setText(score_Str);
        listItemScore_Txt.setTextSize(15);

        // Set visibilities
        listItem_Image.setVisibility(INVISIBLE);
        listItemScore_Txt.setVisibility(VISIBLE);

        // Add details
        listItemDetails_Txt.setText(mMatch.toString());
    }

    private void setEventItem(){
        /*******************************************************************************************
         * Function: setEventItem
         *
         * Purpose: Function set the layout for the item
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        // Set textviews
        listItemMain_Txt.setText(mEvent.getEventName_Str());
        listItemSec_Txt.setText(mEvent.getEventDate_Str());

        // Remove unneeded views
        listItem_Image.setVisibility(VISIBLE);
        listItemScore_Txt.setVisibility(INVISIBLE);

        // Add details
        listItemDetails_Txt.setText(mEvent.toString());
    }

    private void setGunItem(){
        /*******************************************************************************************
         * Function: setGunItem
         *
         * Purpose: Function set the layout for the item
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        // Set textviews
        listItemMain_Txt.setText(mGun.getGunNickname_Str());
        listItemSec_Txt.setText(mGun.getGunModel_Str());

        // Remove unneeded views
        listItem_Image.setVisibility(VISIBLE);
        listItemScore_Txt.setVisibility(INVISIBLE);

        // Add details
        listItemDetails_Txt.setText(mGun.toString());
    }

    private void setLoadItem(){
        /*******************************************************************************************
         * Function: setLoadItem
         *
         * Purpose: Function set the layout for the item
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        // Set textviews
        listItemMain_Txt.setText(mLoad.getLoadNickname_Str());
        listItemSec_Txt.setText(mLoad.getLoadBrand_Str());

        // Remove unneeded views
        listItem_Image.setVisibility(VISIBLE);
        listItemScore_Txt.setVisibility(INVISIBLE);

        // Add details
        listItemDetails_Txt.setText(mLoad.toString());
    }

}
