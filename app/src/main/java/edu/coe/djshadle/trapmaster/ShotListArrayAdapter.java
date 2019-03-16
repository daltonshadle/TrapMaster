/***************************************************************************************************
 * FILENAME : ShotListArrayAdapter.java
 *
 * AUTHOR : Dalton Shadle
 *
 * DESCRIPTION : Class to extend the ArrayAdapter class for the ShotClass
 *
 * NOTES : N/A
 *
 * Copyright Dalton Shadle 2019.  All rights reserved.
 *
 **************************************************************************************************/

package edu.coe.djshadle.trapmaster;

//******************************************** Imports *********************************************
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ShotListArrayAdapter extends ArrayAdapter<ShotClass> {
    //************************************* Private Variables **************************************
    private ArrayList<ShotClass> shotClass_List =  new ArrayList<>();
    private Context shotClass_Context;

    //************************************* Public Functions ***************************************
    // Constructors
    public ShotListArrayAdapter(@NonNull Context context, @NonNull ArrayList<ShotClass> objects) {
        /*******************************************************************************************
         * Function: ShotListArrayAdapter
         *
         * Purpose: Constructor for this class
         *
         * Parameters: context (IN) - the context for using this adapter
         *             resource (IN) - the resource id that is using this adapter
         *             objects (IN) - list of our shot class objects that will be used
         *
         * Returns: None
         *
         ******************************************************************************************/
        super(context, 0, objects);

        shotClass_Context = context;
        shotClass_List = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;

        if(listItem == null)
            listItem = LayoutInflater.from(shotClass_Context).inflate(R.layout.view_event_history_shot_list_item,parent,false);

        ShotClass currentShot = shotClass_List.get(position);

        TextView shotScore = listItem.findViewById(R.id.shotListItemScore_Txt);
        TextView shotEmail = listItem.findViewById(R.id.shotListItemEmail_Txt);
        TextView shotEvent = listItem.findViewById(R.id.shotListItemEvent_Txt);
        TextView shotNotes = listItem.findViewById(R.id.shotListItemNotes_Txt);

        shotScore.setText(currentShot.getShotHitNum_Str());
        shotEmail.setText(currentShot.getShotEmail_Str());
        shotEvent.setText(currentShot.getShotEventName_Str());
        shotNotes.setText(currentShot.getShotNotes_Str());

        return listItem;
    }

}
