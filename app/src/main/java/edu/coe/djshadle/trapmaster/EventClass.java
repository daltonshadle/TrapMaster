/***************************************************************************************************
 * FILENAME : EventClass.java
 *
 * AUTHOR : Dalton Shadle
 *
 * DESCRIPTION : Holds variables and functions for EventClass object
 *
 * NOTES : N/A
 *
 * Copyright Dalton Shadle 2019.  All rights reserved.
 *
 **************************************************************************************************/

package edu.coe.djshadle.trapmaster;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class EventClass {
    //************************************* Private Variables **************************************
    // Object Variables
    private int eventID_Int;
    private String eventEmail_Str;
    private String eventTeam_Str;
    private String eventName_Str;
    private String eventLocation_Str;
    private String eventGun_Str;
    private String eventLoad_Str;
    private String eventDate_Str;
    private String eventWeather_Str;
    private String eventNotes_Str;

    // Dialog Variables
    private int EVENT_DIALOG_STATE = 0;
    private ArrayList<String> EVENT_DIALOG_MSG, EVENT_POS_BTN_TXT, EVENT_NEU_BTN_TXT, EVENT_EDT_HINT;

    //************************************* Public Functions ***************************************
    // Constructors
    public EventClass() {
        /*******************************************************************************************
         * Function: EventClass
         *
         * Purpose: Constructor for this class, no parameters so all values are initialized to temp
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        this.eventID_Int = -1;
        this.eventEmail_Str = "";
        this.eventTeam_Str = "";
        this.eventName_Str = "";
        this.eventLocation_Str = "";
        this.eventGun_Str = "";
        this.eventLoad_Str = "";
        this.eventDate_Str = "";
        this.eventWeather_Str = "";
        this.eventNotes_Str = "";
    }

    public EventClass(String eventEmail_Str, String eventTeam_Str,
                      String eventName_Str, String eventLocation_Str, String eventGun_Str,
                      String eventLoad_Str, String eventDate_Str,
                      String eventWeather_Str, String eventNotes_Str) {
        /*******************************************************************************************
         * Function: EventClass
         *
         * Purpose: Constructor for this class with parameters
         *
         * Parameters: eventEmail_Str (IN) - email tagged with this event
         *             eventTeam_Str (IN) - team tagged with the event
         *             eventName_Str (IN) - name of the event
         *             eventLocation_Str (IN) - location tagged with the event
         *             eventGun_Str (IN) - gun tagged with the event
         *             eventLoad_Str (IN) - load tagged with the event
         *             eventDate_Str (IN) - date tagged with the event
         *             eventWeather_Str (IN) - weather tagged with the event
         *             eventNotes_Str (IN) - notes tagged with the event
         *
         * Returns: None
         *
         ******************************************************************************************/

        this.eventID_Int = -1;
        this.eventEmail_Str = eventEmail_Str;
        this.eventTeam_Str = eventTeam_Str;
        this.eventName_Str = eventName_Str;
        this.eventLocation_Str = eventLocation_Str;
        this.eventGun_Str = eventGun_Str;
        this.eventLoad_Str = eventLoad_Str;
        this.eventDate_Str = eventDate_Str;
        this.eventWeather_Str = eventWeather_Str;
        this.eventNotes_Str = eventNotes_Str;
    }

    // Getters and Setters
    /*******************************************************************************************
     * Function: get*(), set*()
     *
     * Purpose: Getter and setter functions for this class, I would list them all, but I'm lazy
     *
     * Parameters: None
     *
     * Returns: None
     *
     ******************************************************************************************/
    public String getEventEmail_Str() {
        return eventEmail_Str;
    }

    public void setEventEmail_Str(String eventEmail_Str) {
        this.eventEmail_Str = eventEmail_Str;
    }

    public String getEventTeam_Str() {
        return eventTeam_Str;
    }

    public void setEventTeam_Str(String eventTeam_Str) {
        this.eventTeam_Str = eventTeam_Str;
    }

    public String getEventName_Str() {
        return eventName_Str;
    }

    public void setEventName_Str(String eventName_Str) {
        this.eventName_Str = eventName_Str;
    }

    public String getEventLocation_Str() {
        return eventLocation_Str;
    }

    public void setEventLocation_Str(String eventLocation_Str) {
        this.eventLocation_Str = eventLocation_Str;
    }

    public String getEventGun_Str() {
        return eventGun_Str;
    }

    public void setEventGun_Str(String eventGun_Str) {
        this.eventGun_Str = eventGun_Str;
    }

    public String getEventLoad_Str() {
        return eventLoad_Str;
    }

    public void setEventLoad_Str(String eventLoad_Str) {
        this.eventLoad_Str = eventLoad_Str;
    }

    public String getEventDate_Str() {
        return eventDate_Str;
    }

    public void setEventDate_Str(String eventDate_Str) {
        this.eventDate_Str = eventDate_Str;
    }

    public String getEventWeather_Str() {
        return eventWeather_Str;
    }

    public void setEventWeather_Str(String eventWeather_Str) {
        this.eventWeather_Str = eventWeather_Str;
    }

    public String getEventNotes_Str() {
        return eventNotes_Str;
    }

    public void setEventNotes_Str(String eventNotes_Str) {
        this.eventNotes_Str = eventNotes_Str;
    }

    public int getEventID_Int() {
        return eventID_Int;
    }

    public void setEventID_Int(int eventID_Int) {
        this.eventID_Int = eventID_Int;
    }

    //*************************************** Other Functions **************************************
    private void initializeEventDialogStrings() {
        EVENT_DIALOG_MSG = new ArrayList<String>(Arrays.asList(
                "Enter a name for this event.",
                "Enter the location for this event.",
                "Choose the gun used for this event.",
                "Choose the load used for this event.",
                "Choose the date this event took place.",
                "Enter a weather description for this event.",
                "Enter any notes for this event."));
        EVENT_POS_BTN_TXT = new ArrayList<String>(Arrays.asList(
                "NEXT",
                "NEXT",
                "NEXT",
                "NEXT",
                "NEXT",
                "NEXT",
                "SAVE"));
        EVENT_NEU_BTN_TXT = new ArrayList<String>(Arrays.asList(
                "CANCEL",
                "BACK",
                "BACK",
                "BACK",
                "BACK",
                "BACK",
                "BACK"));
        EVENT_EDT_HINT = new ArrayList<String>(Arrays.asList(
                "i.e. MyEvent",
                "i.e. Shooting Star Gun Club",
                "",
                "",
                "",
                "i.e. Warm, Sunny, Windy",
                "i.e. This was a good event!"));
    }

    public void editEventDialog(final Context context, final TrapMasterListArrayAdapter adapter) {
        /*******************************************************************************************
         * Function: editEventDialog
         *
         * Purpose: Function creates dialog and prompts user to add or edit an event item, if adding
         *          an item, ID = -1 and email = current user email
         *
         * Parameters: context (IN) - Supplies the activity context to display dialog to
         *             adapter (IN) - Array adapter to refresh when complete
         *
         * Returns: None
         *
         ******************************************************************************************/

        // Dialog Constants
        String DIALOG_TITLE = "Add New Event";
        int POSITIVE_BTN_COLOR = Color.BLUE;
        int NEUTRAL_BTN_COLOR = Color.RED;

        // Dialog Variables
        GlobalApplicationContext currentContext = new GlobalApplicationContext();
        final DBHandler db = new DBHandler(currentContext.getContext());

        // Pre-Dialog Processing
        if (getEventID_Int() != -1) {
            DIALOG_TITLE = "Edit Event";
        }
        initializeEventDialogStrings();

        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Set Dialog Title
        alertDialog.setTitle(DIALOG_TITLE);

        // Set Dialog Message
        alertDialog.setMessage(EVENT_DIALOG_MSG.get(EVENT_DIALOG_STATE));

        // Set layout for gathering information
        final RelativeLayout subView_RelLay = new RelativeLayout(context);

        // Set views
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final EditText item_Edt = new EditText(context);
        item_Edt.setHint(EVENT_EDT_HINT.get(EVENT_DIALOG_STATE));
        item_Edt.setText(getEventName_Str());
        item_Edt.setGravity(Gravity.START);
        item_Edt.setTextColor(Color.BLACK);
        item_Edt.setLayoutParams(params);
        subView_RelLay.addView(item_Edt);

        final Spinner item_Spin = new Spinner(context);
        item_Spin.setGravity(Gravity.START);
        item_Spin.setLayoutParams(params);

        final DatePicker item_Date = new DatePicker(context);


        // Add linear layout to alert dialog
        alertDialog.setView(subView_RelLay);

        // Set Buttons
        // Positive Button, Right
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, EVENT_POS_BTN_TXT.get(EVENT_DIALOG_STATE), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Processed by onClick below
            }
        });

        // Neutral Button, Left
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, EVENT_NEU_BTN_TXT.get(EVENT_DIALOG_STATE), new DialogInterface.OnClickListener() {
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
                switch (EVENT_DIALOG_STATE){
                    case 0:
                        // Name to location
                        String item_Txt = item_Edt.getText().toString();

                        boolean isEventNameEmpty = item_Txt.equals("");
                        boolean isEventNameInDB = db.isEventNameInDB(getEventEmail_Str(), item_Txt, getEventID_Int());

                        if (isEventNameEmpty) {
                            // Check if the event name is empty
                            item_Edt.setError(context.getString(R.string.error_field_required));
                            item_Edt.requestFocus();
                        } else if (isEventNameInDB) {
                            // Check if the event name is already used in the database,
                            // user cannot have 2 events with same name
                            item_Edt.setError(context.getString(R.string.error_event_already_exists));
                            item_Edt.requestFocus();
                        } else {
                            setEventName_Str(item_Txt);
                            item_Edt.setText(getEventLocation_Str());
                            EVENT_DIALOG_STATE = (EVENT_DIALOG_STATE + 1);
                        }

                        break;
                    case 1:
                        // Location to Gun
                        item_Txt = item_Edt.getText().toString();

                        setEventLocation_Str(item_Txt);

                        subView_RelLay.removeView(item_Edt);
                        item_Spin.setAdapter(initializeGunSpinnerAdapt(context));
                        subView_RelLay.addView(item_Spin);

                        EVENT_DIALOG_STATE = (EVENT_DIALOG_STATE + 1);
                        break;
                    case 2:
                        // Gun to Load
                        item_Txt = item_Spin.getSelectedItem().toString();

                        if (item_Txt.equals(context.getString(R.string.add_gun_text))) {
                            item_Txt = context.getString(R.string.no_gun_main_text);
                        }

                        setEventGun_Str(item_Txt);

                        item_Spin.setAdapter(initializeLoadSpinnerAdapt(context));

                        EVENT_DIALOG_STATE = (EVENT_DIALOG_STATE + 1);
                        break;
                    case 3:
                        // Load to Date
                        item_Txt = item_Spin.getSelectedItem().toString();

                        if (item_Txt.equals(context.getString(R.string.add_load_text))) {
                            item_Txt = context.getString(R.string.no_load_main_text);
                        }

                        setEventLoad_Str(item_Txt);

                        subView_RelLay.removeView(item_Spin);
                        subView_RelLay.addView(item_Date);

                        EVENT_DIALOG_STATE = (EVENT_DIALOG_STATE + 1);
                        break;
                    case 4:
                        // Date to Weather
                        item_Txt = item_Date.toString();

                        setEventDate_Str(item_Txt);

                        subView_RelLay.removeView(item_Date);
                        subView_RelLay.addView(item_Edt);
                        item_Edt.setText(getEventWeather_Str());

                        EVENT_DIALOG_STATE = (EVENT_DIALOG_STATE + 1);
                        break;
                    case 5:
                        // Weather to Notes
                        item_Txt = item_Edt.getText().toString();

                        setEventWeather_Str(item_Txt);
                        item_Edt.setText(getEventNotes_Str());

                        EVENT_DIALOG_STATE = (EVENT_DIALOG_STATE + 1);
                        break;
                    case 6:
                        // Notes to Saving event and closing dialog
                        item_Txt = item_Edt.getText().toString();

                        setEventNotes_Str(item_Txt);

                        if (getEventID_Int() == -1) {
                            // Adding a new event
                            db.insertEventInDB(EventClass.this);
                        } else {
                            // Editing an existing event
                            db.updateEventInDB(EventClass.this, getEventID_Int());
                        }

                        alertDialog.dismiss();

                        Toast.makeText(context, "Event saved!",
                                Toast.LENGTH_LONG).show();

                        // Refresh event list view
                        adapter.refreshEventArrayAdapter(db.getAllEventFromDB(getEventEmail_Str()));

                        // Reset state counter
                        EVENT_DIALOG_STATE = 0;
                        break;
                }

                alertDialog.setMessage(EVENT_DIALOG_MSG.get(EVENT_DIALOG_STATE));
                item_Edt.setHint(EVENT_EDT_HINT.get(EVENT_DIALOG_STATE));
                pos_Btn.setText(EVENT_POS_BTN_TXT.get(EVENT_DIALOG_STATE));
                neu_Btn.setText(EVENT_NEU_BTN_TXT.get(EVENT_DIALOG_STATE));
            }
        });

        neu_Btn.setTextColor(NEUTRAL_BTN_COLOR);
        neu_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform Action on Neutral button

                switch (EVENT_DIALOG_STATE){
                    case 0:
                        // Name to cancel and close dialog
                        alertDialog.dismiss();
                        break;
                    case 1:
                        // Location to Name
                        item_Edt.setText(getEventName_Str());
                        EVENT_DIALOG_STATE = (EVENT_DIALOG_STATE - 1);
                        break;
                    case 2:
                        // Gun to Location
                        item_Edt.setText(getEventLocation_Str());

                        subView_RelLay.removeView(item_Spin);
                        subView_RelLay.addView(item_Edt);

                        EVENT_DIALOG_STATE = (EVENT_DIALOG_STATE - 1);
                        break;
                    case 3:
                        // Load to Gun
                        item_Spin.setAdapter(initializeGunSpinnerAdapt(context));
                        EVENT_DIALOG_STATE = (EVENT_DIALOG_STATE - 1);
                        break;
                    case 4:
                        // Date to Load
                        item_Spin.setAdapter(initializeLoadSpinnerAdapt(context));

                        subView_RelLay.removeView(item_Date);
                        subView_RelLay.addView(item_Spin);

                        EVENT_DIALOG_STATE = (EVENT_DIALOG_STATE - 1);
                        break;
                    case 5:
                        // Weather to Date
                        subView_RelLay.removeView(item_Edt);
                        subView_RelLay.addView(item_Date);

                        EVENT_DIALOG_STATE = (EVENT_DIALOG_STATE - 1);
                        break;
                    case 6:
                        // Notes to Weather
                        item_Edt.setText(getEventWeather_Str());
                        EVENT_DIALOG_STATE = (EVENT_DIALOG_STATE - 1);
                        break;
                }

                alertDialog.setMessage(EVENT_DIALOG_MSG.get(EVENT_DIALOG_STATE));
                item_Edt.setHint(EVENT_EDT_HINT.get(EVENT_DIALOG_STATE));
                pos_Btn.setText(EVENT_POS_BTN_TXT.get(EVENT_DIALOG_STATE));
                neu_Btn.setText(EVENT_NEU_BTN_TXT.get(EVENT_DIALOG_STATE));
            }

        });
    }

    private ArrayAdapter<String> initializeGunSpinnerAdapt(final Context context) {
        /*******************************************************************************************
         * Function: initializeGunSpinnerAdapt
         *
         * Purpose: Function initializes gun spinner for adding a new event
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        ArrayAdapter<String> tempGun_Adapt;
        ArrayList<GunClass> tempGun_List;
        ArrayList<String> tempGunStr_List = new ArrayList<>();

        GlobalApplicationContext currentContext = new GlobalApplicationContext();
        final DBHandler db = new DBHandler(currentContext.getContext());

        tempGun_List = db.getAllGunFromDB(getEventEmail_Str());

        for (int i = 0; i < tempGun_List.size(); i++) {
            GunClass tempGun = tempGun_List.get(i);
            String gunItem_Str = tempGun.getGunNickname_Str();

            tempGunStr_List.add(gunItem_Str);
        }

        if (tempGunStr_List.isEmpty()) {
            tempGunStr_List.add(context.getString(R.string.add_gun_text));
        }

        tempGun_Adapt = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, tempGunStr_List);

        return tempGun_Adapt;
    }

    private ArrayAdapter<String> initializeLoadSpinnerAdapt(final Context context) {
        /*******************************************************************************************
         * Function: initializeLoadSpinnerAdapt
         *
         * Purpose: Function initializes load spinner for adding a new event
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        ArrayAdapter<String> tempLoad_Adapt;
        ArrayList<LoadClass> tempLoad_List;
        ArrayList<String> tempLoadStr_List = new ArrayList<>();

        GlobalApplicationContext currentContext = new GlobalApplicationContext();
        final DBHandler db = new DBHandler(currentContext.getContext());

        tempLoad_List = db.getAllLoadFromDB(getEventEmail_Str());

        for (int i = 0; i < tempLoad_List.size(); i++) {
            LoadClass tempLoad = tempLoad_List.get(i);
            String loadItem_Str = tempLoad.getLoadNickname_Str();

            tempLoadStr_List.add(loadItem_Str);
        }

        if (tempLoadStr_List.isEmpty()) {
            tempLoadStr_List.add(context.getString(R.string.add_load_text));
        }

        tempLoad_Adapt = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, tempLoadStr_List);

        return tempLoad_Adapt;
    }

    public void removeEventItemDialog(final Context context, final TrapMasterListArrayAdapter adapter) {
        /*******************************************************************************************
         * Function: removeEventItemDialog
         *
         * Purpose: Function creates dialog and prompts user to remove a event item
         *
         * Parameters: context (IN) - Supplies the activity context to display dialog to
         *             adapter (IN) - Array adapter to refresh when complete
         *
         * Returns: None
         *
         ******************************************************************************************/

        final String DIALOG_TITLE = "Delete Event";
        final String DIALOG_MSG = "Are you sure you want to delete this event?";

        final boolean POSITIVE_BTN = true;  // Right
        final boolean NEUTRAL_BTN = false;   // Left
        final boolean NEGATIVE_BTN = true;  // Middle

        final String POSITIVE_BUTTON_TXT = "DELETE";
        final String NEUTRAL_BUTTON_TXT = "";
        final String NEGATIVE_BUTTON_TXT = "CANCEL";

        final int POSITIVE_BTN_COLOR = Color.BLUE;
        final int NEUTRAL_BTN_COLOR = Color.RED;
        final int NEGATIVE_BTN_COLOR = Color.RED;

        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        GlobalApplicationContext currentContext = new GlobalApplicationContext();
        final DBHandler db = new DBHandler(currentContext.getContext());

        // Set Dialog Title
        alertDialog.setTitle(DIALOG_TITLE);

        // Set Dialog Message
        alertDialog.setMessage(DIALOG_MSG);

        // Set Buttons
        // Positive Button, Right
        if (POSITIVE_BTN) {
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, POSITIVE_BUTTON_TXT, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Processed by onClick below
                }
            });
        }

        // Neutral Button, Left
        if (NEUTRAL_BTN) {
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, NEUTRAL_BUTTON_TXT, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Processed by onClick below
                }
            });
        }

        // Negative Button, Middle
        if (NEGATIVE_BTN) {
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, NEGATIVE_BUTTON_TXT, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Processed by onClick below
                }
            });
        }


        new Dialog(context);
        alertDialog.show();

        // Set Buttons
        if (POSITIVE_BTN) {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(POSITIVE_BTN_COLOR);
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Perform Action on Positive button
                    db.deleteEventInDB(getEventID_Int());

                    alertDialog.dismiss();

                    Toast.makeText(context, "Event deleted.",
                            Toast.LENGTH_LONG).show();

                    // Refresh event listview
                    adapter.refreshEventArrayAdapter(db.getAllEventFromDB(getEventEmail_Str()));
                }
            });
        }
        if (NEUTRAL_BTN) {
            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(NEUTRAL_BTN_COLOR);
            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Perform Action on Neutral button
                }
            });
        }
        if (NEGATIVE_BTN) {
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(NEGATIVE_BTN_COLOR);
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Perform Action on Negative button
                    alertDialog.dismiss();
                }
            });
        }
    }
}
