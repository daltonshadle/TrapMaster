package edu.coe.djshadle.trapmaster;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

public class CheckboxListArrayAdapter extends ArrayAdapter<String> {
    //************************************* Private Variables **************************************
    // Constants

    // Variables
    private ArrayList<String> string_Array =  new ArrayList<>();
    private ArrayList<Boolean> checked_Array = new ArrayList<>();
    private Context adapt_Context;

    public CheckboxListArrayAdapter(@NonNull Context context, @NonNull ArrayList<String> objects) {
        /*******************************************************************************************
         * Function: TrapMasterListArrayAdapter
         *
         * Purpose: Constructor for this class
         *
         * Parameters: context (IN) - the context for using this adapter
         *             objects (IN) - list of objects that will be used
         *
         * Returns: None
         *
         ******************************************************************************************/

        super(context, 0, objects);
        string_Array = objects;
        adapt_Context = context;

        for (int i = 0; i < this.string_Array.size(); i++) {
            checked_Array.add(false);
        }
    }

    @Override
    public int getCount() {
        /*******************************************************************************************
         * Function: getCount
         *
         * Purpose: Gets the size of the array adapter
         *
         * Parameters: None
         *
         * Returns: ret (OUT) - size of array
         *
         ******************************************************************************************/

        int ret = 0;

        if (string_Array != null) {
            ret = string_Array.size();
        }

        return ret;
    }

    @Override
    public String getItem(int itemIndex) {
        /*******************************************************************************************
         * Function: getItem
         *
         * Purpose: Gets item in the array adapter from itemIndex
         *
         * Parameters: itemIndex (IN) - index of item to return
         *
         * Returns: ret (OUT) - string of item in position itemIndex
         *
         ******************************************************************************************/

        String ret = null;

        if (string_Array != null) {
            ret = string_Array.get(itemIndex);
        }

        return ret;
    }

    public ArrayList<Integer> getCheckedItems() {
        /*******************************************************************************************
         * Function: getCheckedItems
         *
         * Purpose: Gets all indices of all checked items
         *
         * Parameters: None
         *
         * Returns: ret (OUT) - array of checked indices
         *
         ******************************************************************************************/

        ArrayList<Integer> ret = new ArrayList<>();

        for (int i = 0; i < string_Array.size(); i++) {
            if (checked_Array.get(i)) {
                ret.add(i);
            }
        }

        return ret;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        /*******************************************************************************************
         * Function: getView
         *
         * Purpose: Gets the view and initializes it
         *
         * Parameters: position (IN) - position of view in array
         *             convertView (IN) - view to initialize
         *             parent (IN) - parent view of convertView
         *
         * Returns: listItem (OUT) - view that has been initialized
         *
         ******************************************************************************************/

        // Initialize the list item view, context, and db handler vairables
        View listItem = convertView;

        // Handle null exceptions for list view layout
        if (listItem == null)
            listItem = LayoutInflater.from(adapt_Context).inflate(R.layout.view_checkbox_listview_item,
                    parent,false);

        // Initialize all views in list view
        CheckBox listItem_Checkbox = convertView.findViewById(R.id.list_view_item_checkbox);
        TextView listItem_Text = convertView.findViewById(R.id.list_view_item_text);

        // Set TextView string
        listItem_Text.setText(string_Array.get(position));

        // Set Checkbox listener
        final int item_Int = position;
        listItem_Checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                checked_Array.set(item_Int, b);
            }
        });

        return listItem;
    }
}
