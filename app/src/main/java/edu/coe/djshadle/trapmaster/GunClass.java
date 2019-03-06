/***************************************************************************************************
 * FILENAME : GunClass.java
 *
 * AUTHOR : Dalton Shadle
 *
 * DESCRIPTION : Holds variables and functions for GunClass object
 *
 * NOTES : N/A
 *
 * Copyright Dalton Shadle 2019.  All rights reserved.
 *
 **************************************************************************************************/

package edu.coe.djshadle.trapmaster;

public class GunClass {
    //************************************* Private Variables **************************************
    private String gunID_Str;
    private String gunEmail_Str;  //Key
    private String gunNickname_Str;
    private String gunModel_Str;
    private String gunGauge_Str;
    private String gunNotes_Str;

    //************************************* Public Functions ***************************************
    // Constructors
    public GunClass() {
        /*******************************************************************************************
         * Function: GunClass
         *
         * Purpose: Constructor for this class, no parameters so all values are initialized to temp
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        this.gunID_Str = "tempID";
        this.gunEmail_Str = "tempEmail";
        this.gunNickname_Str = "tempNickname";
        this.gunModel_Str = "tempModel";
        this.gunGauge_Str = "tempGauge";
        this.gunNotes_Str = "tempNotes";
    }

    public GunClass(String gunEmail_Str, String gunNickname_Str,
                    String gunModel_Str, String gunGauge_Str, String gunNotes_Str) {
        /*******************************************************************************************
         * Function: GunClass
         *
         * Purpose: Constructor for this class with parameters
         *
         * Parameters: gunEmail_Str (IN) - email tagged with this gun
         *             gunNickname_Str (IN) - name of the gun
         *             gunModel_Str (IN) - model of the gun
         *             gunGauge_Str (IN) - gauge of the gun
         *             gunNotes_Str (IN) - notes tagged with the gun
         *
         * Returns: None
         *
         ******************************************************************************************/

        this.gunID_Str = "tempID";
        this.gunEmail_Str = gunEmail_Str;
        this.gunNickname_Str = gunNickname_Str;
        this.gunModel_Str = gunModel_Str;
        this.gunGauge_Str = gunGauge_Str;
        this.gunNotes_Str = gunNotes_Str;
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
    public String getGunEmail_Str() {
        return gunEmail_Str;
    }

    public void setGunEmail_Str(String gunEmail_Str) {
        this.gunEmail_Str = gunEmail_Str;
    }

    public String getGunNickname_Str() {
        return gunNickname_Str;
    }

    public void setGunNickname_Str(String gunNickname_Str) {
        this.gunNickname_Str = gunNickname_Str;
    }

    public String getGunModel_Str() {
        return gunModel_Str;
    }

    public void setGunModel_Str(String gunModel_Str) {
        this.gunModel_Str = gunModel_Str;
    }

    public String getGunGauge_Str() {
        return gunGauge_Str;
    }

    public void setGunGauge_Str(String gunGauge_Str) {
        this.gunGauge_Str = gunGauge_Str;
    }

    public String getGunNotes_Str() {
        return gunNotes_Str;
    }

    public void setGunNotes_Str(String gunNotes_Str) {
        this.gunNotes_Str = gunNotes_Str;
    }
}
