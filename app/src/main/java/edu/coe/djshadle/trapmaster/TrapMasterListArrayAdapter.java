package edu.coe.djshadle.trapmaster;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TrapMasterListArrayAdapter extends ArrayAdapter<Object>{

    //************************************* Private Variables **************************************
    // Constants
    private final String TAG = "JRW";
    private final int MATCH_LIST_TAG = 1;
    private final int EVENT_LIST_TAG = 2;
    private final int GUN_LIST_TAG = 3;
    private final int LOAD_LIST_TAG = 4;

    // Variables
    private ArrayList<MatchClass> matchClass_List =  new ArrayList<>();
    private ArrayList<EventClass> eventClass_List =  new ArrayList<>();
    private ArrayList<GunClass> gunClass_List =  new ArrayList<>();
    private ArrayList<LoadClass> loadClass_List =  new ArrayList<>();
    private Context mCurrentContext;

    //************************************* Public Functions ***************************************
    // Constructors
    public TrapMasterListArrayAdapter(@NonNull Context context, @NonNull ArrayList<Object> objects, int type_tag) {
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

        mCurrentContext = context;

        // Assign adapter based on type_tag
        switch (type_tag) {
            case MATCH_LIST_TAG:
                matchClass_List = (ArrayList<MatchClass>)(ArrayList<?>) objects;
                refreshMatchArrayAdapter(matchClass_List);
                break;
            case EVENT_LIST_TAG:
                eventClass_List = (ArrayList<EventClass>)(ArrayList<?>) objects;
                refreshEventArrayAdapter(eventClass_List);
                break;
            case GUN_LIST_TAG:
                gunClass_List = (ArrayList<GunClass>)(ArrayList<?>) objects;
                refreshGunArrayAdapter(gunClass_List);
                break;
            case LOAD_LIST_TAG:
                loadClass_List = (ArrayList<LoadClass>)(ArrayList<?>) objects;
                refreshLoadArrayAdapter(loadClass_List);
                break;
        }

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        // Initialize the list item view, context, and db handler vairables
        View listItem = convertView;
        final Context parentContext = parent.getContext();
        GlobalApplicationContext currentContext = new GlobalApplicationContext();
        final DBHandler db = new DBHandler(currentContext.getContext());

        // Handle null exceptions for list view layout
        if (listItem == null)
            listItem = LayoutInflater.from(mCurrentContext).inflate(R.layout.view_custom_list_item,
                    parent,false);

        // Initialize all views in list view
        ImageView listItem_Img = listItem.findViewById(R.id.listItem_Img);
        TextView listItemMain_Txt = listItem.findViewById(R.id.listItemMain_Txt);
        TextView listItemSecond_Txt = listItem.findViewById(R.id.listItemSecond_Txt);
        TextView listItemScore_Txt = listItem.findViewById(R.id.listItemScore_Txt);
        ImageButton listItemEdit_Btn = listItem.findViewById(R.id.listItemEdit_Btn);
        ImageButton listItemDelete_Btn = listItem.findViewById(R.id.listItemDelete_Btn);
        ConstraintLayout listItem_Layout = listItem.findViewById(R.id.listItem_ConLay);

        // Handle each type of list item appropriately with switch statement
        switch ((int) parent.getTag()) {
            case MATCH_LIST_TAG:
                // Item is a Match class object
                final MatchClass current_Match = matchClass_List.get(position);

                ShooterClass temp_Shooter = db.getShooterInDB(current_Match.getMatchShooterID_Int());
                String main_Str = temp_Shooter.getShooterName_Str();
                String second_Str = db.getEventInDB(current_Match.getMatchEventID_Int()).getEventName_Str();

                // Check for default item and process accordingly
                if (isDefaultItem(current_Match)) {
                    // Make unnecessary views invisible
                    listItem_Img.setVisibility(View.INVISIBLE);
                    listItemEdit_Btn.setVisibility(View.INVISIBLE);
                    listItemDelete_Btn.setVisibility(View.INVISIBLE);
                    listItemScore_Txt.setVisibility(View.INVISIBLE);
                } else {
                    // Not a default item, set score
                    listItemScore_Txt.setText(Integer.toString(current_Match.getMatchScore_Int()));
                    listItemScore_Txt.setVisibility(View.VISIBLE);
                    listItemEdit_Btn.setVisibility(View.VISIBLE);
                    listItemDelete_Btn.setVisibility(View.VISIBLE);
                    listItem_Img.setVisibility(View.INVISIBLE);
                }

                // Set tag of list view item to be database ID
                listItem.setTag(current_Match.getMatchID_Int());

                if (second_Str.length() > 40) {
                    second_Str = second_Str.substring(0, 36) + "...";
                }

                listItemMain_Txt.setText(main_Str);
                listItemSecond_Txt.setText(second_Str);

                // Set on click for edit button
                listItemEdit_Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO: make editing rounds work
                        // current_Shot.editShotDialog(parentContext, TrapMasterListArrayAdapter.this);
                    }
                });

                // Set on click for delete button
                listItemDelete_Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO: make removing rounds work
                        // current_Shot.removeShotItemDialog(parentContext, TrapMasterListArrayAdapter.this);
                    }
                });

                break;
            case EVENT_LIST_TAG:
                // Item is an event class object
                final EventClass current_Event = eventClass_List.get(position);

                main_Str = current_Event.getEventName_Str();
                second_Str = current_Event.getEventNotes_Str();

                // Check for default item and process accordingly
                if (isDefaultItem(current_Event)) {
                    // Make unnecessary views invisible
                    listItem_Img.setVisibility(View.INVISIBLE);
                    listItemEdit_Btn.setVisibility(View.INVISIBLE);
                    listItemDelete_Btn.setVisibility(View.INVISIBLE);
                } else {
                    // Make necessary views visible
                    listItem_Img.setVisibility(View.VISIBLE);
                    listItemEdit_Btn.setVisibility(View.VISIBLE);
                    listItemDelete_Btn.setVisibility(View.VISIBLE);
                }

                // Set tag of list view item to be database ID
                listItem.setTag(current_Event.getEventID_Int());

                if (second_Str.length() > 40) {
                    second_Str = second_Str.substring(0, 36) + "...";
                }

                listItemMain_Txt.setText(main_Str);
                listItemSecond_Txt.setText(second_Str);

                // Set on click for edit button
                listItemEdit_Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        current_Event.editEventDialog(parentContext, TrapMasterListArrayAdapter.this);
                    }
                });

                // Set on click for delete button
                listItemDelete_Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        current_Event.removeEventItemDialog(parentContext, TrapMasterListArrayAdapter.this);
                    }
                });

                break;
            case GUN_LIST_TAG:
                // Item is a gun class object
                final GunClass current_Gun = gunClass_List.get(position);

                main_Str = current_Gun.getGunNickname_Str();
                second_Str = current_Gun.getGunNotes_Str();

                // Check for default item and process accordingly
                if (isDefaultItem(current_Gun)) {
                    // Make unnecessary views invisible
                    listItem_Img.setVisibility(View.INVISIBLE);
                    listItemEdit_Btn.setVisibility(View.INVISIBLE);
                    listItemDelete_Btn.setVisibility(View.INVISIBLE);
                } else {
                    // Make necessary views visible
                    listItem_Img.setVisibility(View.VISIBLE);
                    listItemEdit_Btn.setVisibility(View.VISIBLE);
                    listItemDelete_Btn.setVisibility(View.VISIBLE);
                }

                // Set tag of list view item to be database ID
                listItem.setTag(current_Gun.getGunID_Int());

                if (second_Str.length() > 40) {
                    second_Str = second_Str.substring(0, 36) + "...";
                }

                listItemMain_Txt.setText(main_Str);
                listItemSecond_Txt.setText(second_Str);

                // Set on click for edit button
                listItemEdit_Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        current_Gun.editGunDialog(parentContext, TrapMasterListArrayAdapter.this);
                    }
                });

                // Set on click for delete button
                listItemDelete_Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        current_Gun.removeGunDialog(parentContext, TrapMasterListArrayAdapter.this);
                    }
                });

                break;
            case LOAD_LIST_TAG:
                // Item is a load class object
                final LoadClass current_Load = loadClass_List.get(position);

                main_Str = current_Load.getLoadNickname_Str();
                second_Str = current_Load.getLoadNotes_Str();

                // Check for default item and process accordingly
                if (isDefaultItem(current_Load)) {
                    // Make unnecessary views invisible
                    listItem_Img.setVisibility(View.INVISIBLE);
                    listItemEdit_Btn.setVisibility(View.INVISIBLE);
                    listItemDelete_Btn.setVisibility(View.INVISIBLE);
                } else {
                    // Make necessary views visible
                    listItem_Img.setVisibility(View.VISIBLE);
                    listItemEdit_Btn.setVisibility(View.VISIBLE);
                    listItemDelete_Btn.setVisibility(View.VISIBLE);
                }

                // Set tag of list view item to be database ID
                listItem.setTag(current_Load.getLoadID_Int());

                if (second_Str.length() > 40) {
                    second_Str = second_Str.substring(0, 36) + "...";
                }

                listItemMain_Txt.setText(main_Str);
                listItemSecond_Txt.setText(second_Str);

                // Set on click for edit button
                listItemEdit_Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        current_Load.editLoadDialog(parentContext, TrapMasterListArrayAdapter.this);
                        refreshLoadArrayAdapter(db.getAllLoadFromDB(current_Load.getLoadProfileID_Int()));
                    }
                });

                // Set on click for delete button
                listItemDelete_Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        current_Load.removeLoadItemDialog(parentContext, TrapMasterListArrayAdapter.this);
                    }
                });

                break;
            default:
                break;
        }

        return listItem;
    }

    public void refreshMatchArrayAdapter(ArrayList<MatchClass> match_List){
        /*******************************************************************************************
         * Function: refreshMatchArrayAdapter
         *
         * Purpose: Function refreshes the list view parent with current data
         *
         * Parameters: match_List (IN) - list of data to refresh the adapter with
         *
         * Returns: None
         *
         ******************************************************************************************/

        // Check if list is empty
        if (match_List.isEmpty()) {
            MatchClass temp_Match = new MatchClass();
            // TODO: fix empty list for Match adpater
            //temp_Match.setMatchShooterName_Str(getContext().getString(R.string.no_Match_main_text));
            //temp_Match.setMatchEventName_Str(getContext().getString(R.string.no_Match_second_text));

            match_List.add(temp_Match);
        }

        // Update the class list
        matchClass_List = match_List;

        this.clear();
        this.addAll(matchClass_List);
        this.notifyDataSetChanged();
    }

    public void refreshEventArrayAdapter(ArrayList<EventClass> event_List){
        /*******************************************************************************************
         * Function: refreshEventArrayAdapter
         *
         * Purpose: Function refreshes the list view parent with current data
         *
         * Parameters: event_List (IN) - list of data to refresh the adapter with
         *
         * Returns: None
         *
         ******************************************************************************************/

        // Check if list is empty
        if (event_List.isEmpty()) {
            EventClass temp_Event = new EventClass();
            temp_Event.setEventName_Str(getContext().getString(R.string.no_event_main_text));
            temp_Event.setEventNotes_Str(getContext().getString(R.string.no_event_second_text));

            event_List.add(temp_Event);
        }

        // Update the class list
        eventClass_List = event_List;

        this.clear();
        this.addAll(eventClass_List);
        this.notifyDataSetChanged();
    }

    public void refreshGunArrayAdapter(ArrayList<GunClass> gun_List){
        /*******************************************************************************************
         * Function: refreshGunArrayAdapter
         *
         * Purpose: Function refreshes the list view parent with current data
         *
         * Parameters: Match_List (IN) - list of data to refresh the adapter with
         *
         * Returns: None
         *
         ******************************************************************************************/

        // Check if list is empty
        if (gun_List.isEmpty()) {
            GunClass temp_Gun = new GunClass();
            temp_Gun.setGunNickname_Str(getContext().getString(R.string.no_gun_main_text));
            temp_Gun.setGunNotes_Str(getContext().getString(R.string.no_gun_second_text));

            gun_List.add(temp_Gun);
        }

        // Update the class list
        gunClass_List = gun_List;

        this.clear();
        this.addAll(gunClass_List);
        this.notifyDataSetChanged();
    }

    public void refreshLoadArrayAdapter(ArrayList<LoadClass> load_List){
        /*******************************************************************************************
         * Function: refreshLoadArrayAdapter
         *
         * Purpose: Function refreshes the list view parent with current data
         *
         * Parameters: load_List (IN) - list of data to refresh the adapter with
         *
         * Returns: None
         *
         ******************************************************************************************/

        // Check if list is empty
        if (load_List.isEmpty()) {
            LoadClass temp_Load = new LoadClass();
            temp_Load.setLoadNickname_Str(getContext().getString(R.string.no_load_main_text));
            temp_Load.setLoadNotes_Str(getContext().getString(R.string.no_load_second_text));

            load_List.add(temp_Load);
        }

        // Update the class list
        loadClass_List = load_List;

        this.clear();
        this.addAll(loadClass_List);
        this.notifyDataSetChanged();
    }

    private boolean isDefaultItem(MatchClass s) {
        /*******************************************************************************************
         * Function: isDefaultItem
         *
         * Purpose: Function returns true if the list view item is a default item
         *
         * Parameters: s (IN) - object item to check
         *
         * Returns: True if the object is a default item
         *
         ******************************************************************************************/

        String mainText_Str = getContext().getString(R.string.no_shot_main_text);
        String secondText_Str = getContext().getString(R.string.no_shot_second_text);

        // TODO: fix default item for Match
        //return (mainText_Str.equals(s.getMatchShooterName_Str()) && secondText_Str.equals(s.getMatchEventName_Str()));
        return false;
    }

    private boolean isDefaultItem(EventClass e) {
        /*******************************************************************************************
         * Function: isDefaultItem
         *
         * Purpose: Function returns true if the list view item is a default item
         *
         * Parameters: e (IN) - object item to check
         *
         * Returns: True if the object is a default item
         *
         ******************************************************************************************/

        String mainText_Str = getContext().getString(R.string.no_event_main_text);
        String secondText_Str = getContext().getString(R.string.no_event_second_text);

        return (mainText_Str.equals(e.getEventName_Str()) && secondText_Str.equals(e.getEventNotes_Str()));
    }

    private boolean isDefaultItem(GunClass g) {
        /*******************************************************************************************
         * Function: isDefaultItem
         *
         * Purpose: Function returns true if the list view item is a default item
         *
         * Parameters: e (IN) - object item to check
         *
         * Returns: True if the object is a default item
         *
         ******************************************************************************************/

        String mainText_Str = getContext().getString(R.string.no_gun_main_text);
        String secondText_Str = getContext().getString(R.string.no_gun_second_text);

        return (mainText_Str.equals(g.getGunNickname_Str()) && secondText_Str.equals(g.getGunNotes_Str()));
    }

    private boolean isDefaultItem(LoadClass l) {
        /*******************************************************************************************
         * Function: isDefaultItem
         *
         * Purpose: Function returns true if the list view item is a default item
         *
         * Parameters: l (IN) - object item to check
         *
         * Returns: True if the object is a default item
         *
         ******************************************************************************************/

        String mainText_Str = getContext().getString(R.string.no_load_main_text);
        String secondText_Str = getContext().getString(R.string.no_load_second_text);

        return (mainText_Str.equals(l.getLoadNickname_Str()) && secondText_Str.equals(l.getLoadNotes_Str()));
    }

}
