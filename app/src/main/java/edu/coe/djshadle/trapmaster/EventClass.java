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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class EventClass {
    //************************************* Private Variables **************************************
    // Object Variables
    private int eventID_Int;
    private int eventProfileID_Int;
    private String eventName_Str;
    private String eventLocation_Str;
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
        this.eventProfileID_Int = -1;
        this.eventName_Str = "";
        this.eventLocation_Str = "";
        this.eventDate_Str = "";
        this.eventWeather_Str = "";
        this.eventNotes_Str = "";
    }

    public EventClass(int eventProfileID_Int, String eventName_Str, String eventLocation_Str,
                      String eventDate_Str, String eventWeather_Str, String eventNotes_Str) {
        /*******************************************************************************************
         * Function: EventClass
         *
         * Purpose: Constructor for this class with parameters
         *
         * Parameters: eventProfileID_Int (IN) - ID tagged with this event
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
        this.eventProfileID_Int = eventProfileID_Int;
        this.eventName_Str = eventName_Str;
        this.eventLocation_Str = eventLocation_Str;
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

    public int getEventProfileID_Int() {
        return eventProfileID_Int;
    }

    public void setEventProfileID_Int(int eventProfileID_Int) {
        this.eventProfileID_Int = eventProfileID_Int;
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

    public void setEventFromEvent(EventClass temp_Event) {
        this.setEventID_Int(temp_Event.getEventID_Int());
        this.setEventDate_Str(temp_Event.getEventDate_Str());
        this.setEventLocation_Str(temp_Event.getEventLocation_Str());
        this.setEventName_Str(temp_Event.getEventName_Str());
        this.setEventNotes_Str(temp_Event.getEventNotes_Str());
        this.setEventProfileID_Int(temp_Event.getEventProfileID_Int());
        this.setEventWeather_Str(temp_Event.getEventWeather_Str());
    }

    public String toString(){
        /*******************************************************************************************
         * Function: toString
         *
         * Purpose: Function returns item as a string
         *
         * Parameters: None
         *
         * Returns: item_Str (OUT) - string representing all values of the item
         *
         ******************************************************************************************/

        String item_Str = "Name: " + getEventName_Str() + "\n"
                + "Location: " + getEventLocation_Str() + "\n"
                + "Date: " + getEventDate_Str() + "\n"
                + "Weather: " + getEventWeather_Str() + "\n"
                + "Notes: " + getEventNotes_Str();

        return item_Str;
    }

    //************************************ Edit Event Functions ************************************
    private void initializeEventDialogStrings() {
        EVENT_DIALOG_MSG = new ArrayList<String>(Arrays.asList(
                "Enter a name for this event.",
                "Enter the location for this event.",
                "Choose the date this event took place.",
                "Enter a weather description for this event.",
                "Enter any notes for this event."));
        EVENT_POS_BTN_TXT = new ArrayList<String>(Arrays.asList(
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
                "BACK"));
        EVENT_EDT_HINT = new ArrayList<String>(Arrays.asList(
                "i.e. MyEvent",
                "i.e. Shooting Star Gun Club",
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
                        boolean isEventNameInDB = db.isEventNameInDB(getEventProfileID_Int(), item_Txt, getEventID_Int());

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
                        // Location to Date
                        item_Txt = item_Edt.getText().toString();

                        setEventLocation_Str(item_Txt);

                        subView_RelLay.removeView(item_Edt);
                        subView_RelLay.addView(item_Date);

                        EVENT_DIALOG_STATE = (EVENT_DIALOG_STATE + 1);
                        break;
                    case 2:
                        // Date to Weather
                        item_Txt = item_Date.toString();

                        setEventDate_Str(item_Txt);

                        subView_RelLay.removeView(item_Date);
                        subView_RelLay.addView(item_Edt);
                        item_Edt.setText(getEventWeather_Str());

                        EVENT_DIALOG_STATE = (EVENT_DIALOG_STATE + 1);
                        break;
                    case 3:
                        // Weather to Notes
                        item_Txt = item_Edt.getText().toString();

                        setEventWeather_Str(item_Txt);
                        item_Edt.setText(getEventNotes_Str());

                        EVENT_DIALOG_STATE = (EVENT_DIALOG_STATE + 1);
                        break;
                    case 4:
                        // Notes to Saving event and closing dialog
                        item_Txt = item_Edt.getText().toString();

                        setEventNotes_Str(item_Txt);

                        if (getEventID_Int() == -1) {
                            // Adding a new event
                            db.insertEventInDB(EventClass.this);
                        } else {
                            // Editing an existing event
                            db.updateEventInDB(EventClass.this);
                        }

                        alertDialog.dismiss();

                        Toast.makeText(context, "Event saved!",
                                Toast.LENGTH_LONG).show();

                        // Refresh event list view
                        adapter.refreshEventArrayAdapter(db.getAllEventFromDB(getEventProfileID_Int()));

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
                        // Date to Location
                        item_Edt.setText(getEventLocation_Str());

                        subView_RelLay.removeView(item_Date);
                        subView_RelLay.addView(item_Edt);

                        EVENT_DIALOG_STATE = (EVENT_DIALOG_STATE - 1);
                        break;
                    case 3:
                        // Weather to Date
                        subView_RelLay.removeView(item_Edt);
                        subView_RelLay.addView(item_Date);

                        EVENT_DIALOG_STATE = (EVENT_DIALOG_STATE - 1);
                        break;
                    case 4:
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

    public AlertDialog editEventDialog(final Context context) {
        /*******************************************************************************************
         * Function: editEventDialog
         *
         * Purpose: Function creates dialog and prompts user to add or edit an event item, if adding
         *          an item, ID = -1 and email = current user email
         *
         * Parameters: context (IN) - Supplies the activity context to display dialog to
         *
         * Returns: alertDialog (OUT) - alert dialog message this function creates
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
                        boolean isEventNameInDB = db.isEventNameInDB(getEventProfileID_Int(), item_Txt, getEventID_Int());

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
                        // Location to Date
                        item_Txt = item_Edt.getText().toString();

                        setEventLocation_Str(item_Txt);

                        subView_RelLay.removeView(item_Edt);
                        subView_RelLay.addView(item_Date);

                        EVENT_DIALOG_STATE = (EVENT_DIALOG_STATE + 1);
                        break;
                    case 2:
                        // Date to Weather
                        item_Txt = item_Date.toString();

                        setEventDate_Str(item_Txt);

                        subView_RelLay.removeView(item_Date);
                        subView_RelLay.addView(item_Edt);
                        item_Edt.setText(getEventWeather_Str());

                        EVENT_DIALOG_STATE = (EVENT_DIALOG_STATE + 1);
                        break;
                    case 3:
                        // Weather to Notes
                        item_Txt = item_Edt.getText().toString();

                        setEventWeather_Str(item_Txt);
                        item_Edt.setText(getEventNotes_Str());

                        EVENT_DIALOG_STATE = (EVENT_DIALOG_STATE + 1);
                        break;
                    case 4:
                        // Notes to Saving event and closing dialog
                        item_Txt = item_Edt.getText().toString();

                        setEventNotes_Str(item_Txt);

                        if (getEventID_Int() == -1) {
                            // Adding a new event
                            db.insertEventInDB(EventClass.this);
                        } else {
                            // Editing an existing event
                            db.updateEventInDB(EventClass.this);
                        }

                        alertDialog.dismiss();

                        Toast.makeText(context, "Event saved!",
                                Toast.LENGTH_LONG).show();

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
                        // Date to Location
                        item_Edt.setText(getEventLocation_Str());

                        subView_RelLay.removeView(item_Date);
                        subView_RelLay.addView(item_Edt);

                        EVENT_DIALOG_STATE = (EVENT_DIALOG_STATE - 1);
                        break;
                    case 3:
                        // Weather to Date
                        subView_RelLay.removeView(item_Edt);
                        subView_RelLay.addView(item_Date);

                        EVENT_DIALOG_STATE = (EVENT_DIALOG_STATE - 1);
                        break;
                    case 4:
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

        return alertDialog;
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
                    adapter.refreshEventArrayAdapter(db.getAllEventFromDB(getEventProfileID_Int()));
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

    public AlertDialog removeEventItemDialog(final Context context) {
        /*******************************************************************************************
         * Function: removeEventItemDialog
         *
         * Purpose: Function creates dialog and prompts user to remove a event item
         *
         * Parameters: context (IN) - Supplies the activity context to display dialog to
         *
         * Returns: alertDialog (OUT) - AlertDialog variable this function creates
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
                    alertDialog.cancel();
                }
            });
        }

        return alertDialog;
    }

    private CheckboxListArrayAdapter initializeEventArrayAdapt(ArrayList<EventClass> dbEvent_List) {
        /*******************************************************************************************
         * Function: initializeEventArrayAdapt
         *
         * Purpose: Function initializes event listview adapt
         *
         * Parameters: dbEvent_List (IN) - list of events in DB to create adapter for
         *
         * Returns: tempEvent_Adapt - Array adapter initialized to events from DB
         *
         ******************************************************************************************/

        // Initialize function variables
        CheckboxListArrayAdapter tempEvent_Adapt;
        ArrayList<String> tempEventStr_Array = new ArrayList<>();
        GlobalApplicationContext currentContext = new GlobalApplicationContext();

        // Iterate over all events and get names
        for (int i = 0; i < dbEvent_List.size(); i++) {
            tempEventStr_Array.add(dbEvent_List.get(i).getEventName_Str());
        }

        // Set array adapter
        tempEvent_Adapt = new CheckboxListArrayAdapter(currentContext.getContext(), tempEventStr_Array);

        return tempEvent_Adapt;
    }

    public AlertDialog chooseEventDialog(final Context context, final int profileID_Int) {
        /*******************************************************************************************
         * Function: chooseEventDialog
         *
         * Purpose: Function creates dialog and prompts user to choose an event from the database
         *
         * Parameters: context (IN) - Supplies the activity context to display dialog to
         *             profileID_Int (IN) - profile ID for database to access events
         *
         * Returns: The EventClass variable that called this function will be set to the event chosen,
         *          if none are chosen, the variable will have the eventID = -1
         *          alertDialog (OUT) - Alert dialog created with this function
         *
         ******************************************************************************************/

        // Dialog Constants
        String DIALOG_TITLE = "Event Picker";
        int POSITIVE_BTN_COLOR = Color.BLUE;
        int NEUTRAL_BTN_COLOR = Color.RED;

        // Dialog Variables
        GlobalApplicationContext currentContext = new GlobalApplicationContext();
        final DBHandler db = new DBHandler(currentContext.getContext());
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        final ArrayList<EventClass> dbEvent_List = db.getAllEventFromDB(profileID_Int);

        // Set Dialog Title
        alertDialog.setTitle(DIALOG_TITLE);

        // Set Dialog Message
        alertDialog.setMessage("Pick an event! (You can add events in Event History)");

        // Set layout for gathering information
        final RelativeLayout subView_RelLay = new RelativeLayout(context);

        // Set views
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final ListView event_List = new ListView(context);
        final CheckboxListArrayAdapter event_Adapt = initializeEventArrayAdapt(dbEvent_List);
        event_List.setAdapter(event_Adapt);
        event_List.setLayoutParams(params);
        subView_RelLay.addView(event_List);

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
                if (event_Adapt.getCount() == 1) {
                    // Event selected, return that event
                    EventClass.this.setEventFromEvent(dbEvent_List.get(event_Adapt.getCheckedItems().get(0)));
                    alertDialog.dismiss();
                } else {
                    // Too few or many events selected
                    Toast.makeText(context, "Select 1 Event.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        neu_Btn.setTextColor(NEUTRAL_BTN_COLOR);
        neu_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform Action on Neutral button
                // Pick canceled, set ID to -1 and dismiss dialog
                EventClass.this.setEventID_Int(-1);
                alertDialog.dismiss();
            }

        });

        return alertDialog;
    }
}
