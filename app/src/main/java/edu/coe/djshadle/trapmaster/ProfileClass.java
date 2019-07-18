/***************************************************************************************************
 * FILENAME : ProfileClass.java
 *
 * AUTHOR : Dalton Shadle
 *
 * DESCRIPTION : Holds variables and functions for ProfileClass object
 *
 * NOTES : N/A
 *
 * Copyright Dalton Shadle 2019.  All rights reserved.
 *
 **************************************************************************************************/

package edu.coe.djshadle.trapmaster;

public class ProfileClass {
    //************************************* Private Variables **************************************
    private int profileID_Int;
    private String profileEmail_Str;  //Key

    //************************************* Public Functions ***************************************
    // Constructors
    public ProfileClass() {
        /*******************************************************************************************
         * Function: ProfileClass
         *
         * Purpose: Constructor for this class, no parameters so all values are initialized to temp
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        this.profileID_Int = -1;
        this.profileEmail_Str = "";
    }

    public ProfileClass(int profileID_Int, String profileEmail_Str) {
        /*******************************************************************************************
         * Function: ProfileClass
         *
         * Purpose: Constructor for this class with parameters
         *
         * Parameters: profileID_Int (IN) - database ID for object, -1 for new object in database
         *             profileEmail_Str (IN) - email for the profile
         *
         * Returns: None
         *
         ******************************************************************************************/

        this.profileID_Int = profileID_Int;
        this.profileEmail_Str = profileEmail_Str;
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
    public int getProfileID_Int() {
        return profileID_Int;
    }

    public void setProfileID_Int(int profileID_Int) {
        this.profileID_Int = profileID_Int;
    }

    public String getProfileEmail_Str() {
        return profileEmail_Str;
    }

    public void setProfileEmail_Str(String profileEmail_Str) {
        this.profileEmail_Str = profileEmail_Str;
    }
}
