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

public class EventClass {
    //************************************* Private Variables **************************************
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
        this.eventEmail_Str = "tempEmail";
        this.eventTeam_Str = "tempTeam";
        this.eventName_Str = "tempEventName";
        this.eventLocation_Str = "tempLocation";
        this.eventGun_Str = "tempGun";
        this.eventLoad_Str = "tempLoad";
        this.eventDate_Str = "tempDate";
        this.eventWeather_Str = "tempWeather";
        this.eventNotes_Str = "tempNotes";
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
}
