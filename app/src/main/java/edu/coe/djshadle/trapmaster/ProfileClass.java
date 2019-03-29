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
    private String profileID_Str;
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

        this.profileID_Str = "tempID";
        this.profileEmail_Str = "tempEmail";
    }

    public ProfileClass(String profileEmail_Str) {
        /*******************************************************************************************
         * Function: ProfileClass
         *
         * Purpose: Constructor for this class with parameters
         *
         * Parameters: profileEmail_Str (IN) - email for the profile
         *             profilePassword_Str (IN) - password for the profile
         *
         * Returns: None
         *
         ******************************************************************************************/

        this.profileID_Str = "tempID";
        this.profileEmail_Str = profileEmail_Str;
    }

    public ProfileClass(String profileID_Str, String profileEmail_Str) {
        /*******************************************************************************************
         * Function: ProfileClass
         *
         * Purpose: Constructor for this class with parameters
         *
         * Parameters: profileID_Str (IN) - id of the profile
         *             profileEmail_Str (IN) - email for the profile
         *             profilePassword_Str (IN) - password for the profile
         *
         * Returns: None
         *
         ******************************************************************************************/

        this.profileID_Str = profileID_Str;
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
    public String getProfileID_Str() {
        return profileID_Str;
    }

    public void setProfileID_Str(String profileID_Str) {
        this.profileID_Str = profileID_Str;
    }

    public String getProfileEmail_Str() {
        return profileEmail_Str;
    }

    public void setProfileEmail_Str(String profileEmail_Str) {
        this.profileEmail_Str = profileEmail_Str;
    }
}
