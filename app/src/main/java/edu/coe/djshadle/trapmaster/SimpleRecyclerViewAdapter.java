/***************************************************************************************************
 * FILENAME : SimpleRecyclerViewAdapter.java
 *
 * AUTHOR : Dalton Shadle
 *
 * DESCRIPTION : Holds variables and functions for SimpleRecyclerViewAdapter object
 *
 * NOTES : N/A
 *
 * Copyright Dalton Shadle 2019.  All rights reserved.
 *
 **************************************************************************************************/

package edu.coe.djshadle.trapmaster;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SimpleRecyclerViewAdapter extends RecyclerView.Adapter<SimpleRecyclerViewAdapter.ViewHolder>{
    //**************************************** Constants *******************************************
    // Key Constants
    private String TAG = "JRW";

    //************************************* Private Variables **************************************
    // Object Variables
    private ArrayList<String> mData;
    private LayoutInflater mInflater;
    private Context mContext;

    //************************************* Public Functions ***************************************
    // Constructors
    SimpleRecyclerViewAdapter(Context context, ArrayList<String> data) {
        /*******************************************************************************************
         * Function: SimpleRecyclerViewAdapter
         *
         * Purpose: Constructor for this class
         *
         * Parameters: context (IN) - context of where this object was declared
         *             data (IN) - list of data for this object
         *
         * Returns: None
         *
         ******************************************************************************************/

        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /*******************************************************************************************
         * Function: onCreateViewHolder
         *
         * Purpose: inflates the row layout from xml when needed
         *
         * Parameters: parent (IN) - parent of the view
         *             viewType (IN) - type of view of parent
         *
         * Returns: ViewHolder (OUT) - new ViewHolder object initialized
         *
         ******************************************************************************************/

        View view = mInflater.inflate(R.layout.view_simple_recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        /*******************************************************************************************
         * Function: onBindViewHolder
         *
         * Purpose: binds the data to the ViewHolder in each row
         *
         * Parameters: holder (IN) - view to bind data to in recycler view
         *             position (IN) - position of view in recycler view
         *
         * Returns: None
         *
         ******************************************************************************************/

        String item = mData.get(position);
        holder.recyclerItem_Txt.setText(item);
        holder.recyclerItem_Img.setImageDrawable(mContext.getDrawable(R.drawable.three_bar_movement_icon));
        holder.recyclerItem_Img.setScaleType(ImageView.ScaleType.FIT_CENTER);
    }

    public void moveItem(int oldPos, int newPos) {
        /*******************************************************************************************
         * Function: moveItem
         *
         * Purpose: moves item from old position to new position
         *
         * Parameters: oldPos (IN) - old position of view
         *             newPos (IN) - new position of view
         *
         * Returns: None
         *
         ******************************************************************************************/

        String item = mData.get(oldPos);
        mData.remove(oldPos);
        mData.add(newPos, item);
        notifyItemMoved(oldPos, newPos);
    }

    @Override
    public int getItemCount() {
        /*******************************************************************************************
         * Function: getItemCount
         *
         * Purpose: returns the total number of elements in recycler view
         *
         * Parameters: None
         *
         * Returns: mData.size() (OUT) - total number of elements
         *
         ******************************************************************************************/

        return mData.size();
    }

    String getItem(int pos) {
        /*******************************************************************************************
         * Function: getItem
         *
         * Purpose: returns the string item of the position
         *
         * Parameters: pos (IN) - position of item to return string of
         *
         * Returns: string (OUT) - string item at pos
         *
         ******************************************************************************************/

        return mData.get(pos);
    }

    ArrayList<String> getAllItems() {
        /*******************************************************************************************
         * Function: getAllItems
         *
         * Purpose: returns the all string items
         *
         * Parameters: None
         *
         * Returns: mData (OUT) - list of string items
         *
         ******************************************************************************************/

        return mData;
    }

    //**************************************** Subclasses ******************************************
    public class ViewHolder extends RecyclerView.ViewHolder {
        /*******************************************************************************************
         * Class: ViewHolder
         *
         * Purpose: stores and recycles views as they are scrolled off screen
         *
         ******************************************************************************************/

        TextView recyclerItem_Txt;
        ImageView recyclerItem_Img;

        ViewHolder(View itemView) {
            super(itemView);
            recyclerItem_Txt = itemView.findViewById(R.id.recyclerItemText_Txt);
            recyclerItem_Img = itemView.findViewById(R.id.recyclerItemImage_Img);
        }
    }
}
