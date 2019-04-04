package edu.coe.djshadle.trapmaster;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TrapMasterListArrayAdapter extends ArrayAdapter<Object>{

    //************************************* Private Variables **************************************
    // Constants
    private final String TAG = "JRW";
    private final int SHOT_LIST_TAG = 1;
    private final int EVENT_LIST_TAG = 2;
    private final int GUN_LIST_TAG = 3;
    private final int LOAD_LIST_TAG = 4;


    // Variables
    private ArrayList<ShotClass> shotClass_List =  new ArrayList<>();
    private ArrayList<EventClass> eventClass_List =  new ArrayList<>();
    private ArrayList<GunClass> gunClass_List =  new ArrayList<>();
    private ArrayList<LoadClass> loadClass_List =  new ArrayList<>();
    private Context mCurrentContext;

    //************************************* Public Functions ***************************************
    // Constructors
    public TrapMasterListArrayAdapter(@NonNull Context context, @NonNull ArrayList<Object> objects) {
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

        if (!objects.isEmpty()) {
            // List is not empty
            Object first_Obj = objects.get(0);

            if (first_Obj instanceof ShotClass) {
                shotClass_List = (ArrayList<ShotClass>)(ArrayList<?>) objects;
            } else if (first_Obj instanceof EventClass) {
                eventClass_List = (ArrayList<EventClass>)(ArrayList<?>) objects;
            } else if (first_Obj instanceof GunClass) {
                gunClass_List = (ArrayList<GunClass>)(ArrayList<?>) objects;
            } else if (first_Obj instanceof LoadClass) {
                loadClass_List = (ArrayList<LoadClass>)(ArrayList<?>) objects;
            }
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        final Context parentContext = parent.getContext();

        if (listItem == null)
            listItem = LayoutInflater.from(mCurrentContext).inflate(R.layout.view_custom_list_item,
                    parent,false);

        ImageView listItem_Img = listItem.findViewById(R.id.listItem_Img);
        TextView listItemMain_Txt = listItem.findViewById(R.id.listItemMain_Txt);
        TextView listItemSecond_Txt = listItem.findViewById(R.id.listItemSecond_Txt);
        ImageButton listItemEdit_Btn = listItem.findViewById(R.id.listItemEdit_Btn);
        ImageButton listItemDelete_Btn = listItem.findViewById(R.id.listItemDelete_Btn);

        switch ((int) parent.getTag()) {
            case SHOT_LIST_TAG:
                // Item is a shot class object
                ShotClass current_Shot = shotClass_List.get(position);

                String main_Str = current_Shot.getShotEventName_Str();
                String second_Str = current_Shot.getShotNotes_Str();

                // Set tag of list view item to be database ID
                listItem.setTag(current_Shot.getShotID_Int());

                if (main_Str.equals("")) {
                    main_Str = "No event for shoot.";
                }
                if (second_Str.equals("")) {
                    second_Str = "No notes for this shoot.";
                }
                if (second_Str.length() > 40) {
                    second_Str = second_Str.substring(0, 36) + "...";
                }

                listItemMain_Txt.setText(main_Str);
                listItemSecond_Txt.setText(second_Str);

                break;
            case EVENT_LIST_TAG:
                // Item is an event class object
                EventClass current_Event = eventClass_List.get(position);

                main_Str = current_Event.getEventName_Str();
                second_Str = current_Event.getEventNotes_Str();

                // Set tag of list view item to be database ID
                listItem.setTag(current_Event.getEventID_Int());

                if (main_Str.equals("")) {
                    main_Str = "No name for event.";
                }
                if (second_Str.equals("")) {
                    second_Str = "No notes for this event.";
                }
                if (second_Str.length() > 40) {
                    second_Str = second_Str.substring(0, 36) + "...";
                }

                listItemMain_Txt.setText(main_Str);
                listItemSecond_Txt.setText(second_Str);

                break;
            case GUN_LIST_TAG:
                // Item is a gun class object
                final GunClass current_Gun = gunClass_List.get(position);

                main_Str = current_Gun.getGunNickname_Str();
                second_Str = current_Gun.getGunNotes_Str();

                // Set tag of list view item to be database ID
                listItem.setTag(current_Gun.getGunID_Int());

                if (main_Str.equals("")) {
                    main_Str = "No nickname for gun.";
                }
                if (second_Str.equals("")) {
                    second_Str = "No notes for this gun.";
                }
                if (second_Str.length() > 40) {
                    second_Str = second_Str.substring(0, 36) + "...";
                }

                listItemMain_Txt.setText(main_Str);
                listItemSecond_Txt.setText(second_Str);

                // Set on click for whole view
                listItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(TAG, Integer.toString(current_Gun.getGunID_Int()));
                    }
                });

                // Set on click for edit button
                listItemEdit_Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        current_Gun.editGunDialog(parentContext);
                    }
                });

                // Set on click for delete button
                listItemDelete_Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        current_Gun.removeGunDialog(parentContext);
                    }
                });

                break;
            case LOAD_LIST_TAG:
                // Item is a load class object
                final LoadClass current_Load = loadClass_List.get(position);

                main_Str = current_Load.getLoadNickname_Str();
                second_Str = current_Load.getLoadNotes_Str();

                // Set tag of list view item to be database ID
                listItem.setTag(current_Load.getLoadID_Int());

                if (main_Str.equals("")) {
                    main_Str = "No nickname for load.";
                }
                if (second_Str.equals("")) {
                    second_Str = "No notes for load.";
                }
                if (second_Str.length() > 40) {
                    second_Str = second_Str.substring(0, 36) + "...";
                }

                listItemMain_Txt.setText(main_Str);
                listItemSecond_Txt.setText(second_Str);

                // Set on click for whole view
                listItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(TAG, Integer.toString(current_Load.getLoadID_Int()));
                    }
                });

                // Set on click for edit button
                listItemEdit_Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        current_Load.editLoadDialog(parentContext);
                    }
                });

                // Set on click for delete button
                listItemDelete_Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        current_Load.removeLoadItemDialog(parentContext);
                    }
                });

                break;
            default:
                break;
        }

        return listItem;
    }
}
