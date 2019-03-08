/***************************************************************************************************
 * FILENAME : ShotClass.java
 *
 * AUTHOR : Dalton Shadle
 *
 * DESCRIPTION : Holds variables and functions for ShotClass object
 *
 * NOTES : N/A
 *
 * Copyright Dalton Shadle 2019.  All rights reserved.
 *
 **************************************************************************************************/

package edu.coe.djshadle.trapmaster;

public class ShotClass {
    // Might be my favorite name for a class, I will try to work this in wherever I can
    //************************************* Private Variables **************************************
    private String shotID_Str;
    private String shotEmail_Str;
    private String shotEventName_Str;
    private String shotTotalNum_Str;
    private String shotHitNum_Str;
    private String shotNotes_Str;

    //************************************* Public Functions ***************************************
    // Constructors
    public ShotClass() {
        /*******************************************************************************************
         * Function: ShotClass
         *
         * Purpose: Constructor for this class, no parameters so all values are initialized to temp
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        this.shotID_Str = "tempID";
        this.shotEmail_Str = "tempEmail";
        this.shotEventName_Str = "tempEventName";
        this.shotTotalNum_Str = "tempTotalShot";
        this.shotHitNum_Str = "tempTotalHit";
        this.shotNotes_Str = "tempNotes";
    }

    public ShotClass(String shotID_Str, String shotEmail_Str, String shotEventName_Str,
                     String shotTotalNum_Str, String shotHitNum_Str, String shotNotes_Str) {
        /*******************************************************************************************
         * Function: ShotClass
         *
         * Purpose: Constructor for this class with parameters
         *
         * Parameters: shotID_Str (IN) - ID tagged with this shot
         *             shotEmail_Str (IN) - email tagged with this shot
         *             shotEventName_Str (IN) - event name of the shot
         *             shotTotalNum_Str (IN) - total number of shots
         *             shotHitNum_Str (IN) - number of shots that were hits
         *             shotNotes_Str (IN) - notes tagged with the shot
         *
         * Returns: None
         *
         ******************************************************************************************/

        this.shotID_Str = shotID_Str;
        this.shotEmail_Str = shotEmail_Str;
        this.shotEventName_Str = shotEventName_Str;
        this.shotTotalNum_Str = shotTotalNum_Str;
        this.shotHitNum_Str = shotHitNum_Str;
        this.shotNotes_Str = shotNotes_Str;
    }

    public ShotClass(String shotEmail_Str, String shotEventName_Str,
                     String shotTotalNum_Str, String shotHitNum_Str, String shotNotes_Str) {
        /*******************************************************************************************
         * Function: ShotClass
         *
         * Purpose: Constructor for this class with parameters
         *
         * Parameters: shotEmail_Str (IN) - email tagged with this shot
         *             shotEventName_Str (IN) - event name of the shot
         *             shotTotalNum_Str (IN) - total number of shots
         *             shotHitNum_Str (IN) - number of shots that were hits
         *             shotNotes_Str (IN) - notes tagged with the shot
         *
         * Returns: None
         *
         ******************************************************************************************/

        this.shotID_Str = "tempID";
        this.shotEmail_Str = shotEmail_Str;
        this.shotEventName_Str = shotEventName_Str;
        this.shotTotalNum_Str = shotTotalNum_Str;
        this.shotHitNum_Str = shotHitNum_Str;
        this.shotNotes_Str = shotNotes_Str;
    }

    // Setters and Getters
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
    public String getShotID_Str() {
        return shotID_Str;
    }

    public void setShotID_Str(String shotID_Str) {
        this.shotID_Str = shotID_Str;
    }

    public String getShotEmail_Str() {
        return shotEmail_Str;
    }

    public void setShotEmail_Str(String shotEmail_Str) {
        this.shotEmail_Str = shotEmail_Str;
    }

    public String getShotEventName_Str() {
        return shotEventName_Str;
    }

    public void setShotEventName_Str(String shotEventName_Str) {
        this.shotEventName_Str = shotEventName_Str;
    }

    public String getShotTotalNum_Str() {
        return shotTotalNum_Str;
    }

    public void setShotTotalNum_Str(String shotTotalNum_Str) {
        this.shotTotalNum_Str = shotTotalNum_Str;
    }

    public String getShotHitNum_Str() {
        return shotHitNum_Str;
    }

    public void setShotHitNum_Str(String shotHitNum_Str) {
        this.shotHitNum_Str = shotHitNum_Str;
    }

    public String getShotNotes_Str() {
        return shotNotes_Str;
    }

    public void setShotNotes_Str(String shotNotes_Str) {
        this.shotNotes_Str = shotNotes_Str;
    }
}
