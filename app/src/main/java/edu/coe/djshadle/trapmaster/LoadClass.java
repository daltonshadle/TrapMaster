/***************************************************************************************************
 * FILENAME : LoadClass.java
 *
 * AUTHOR : Dalton Shadle
 *
 * DESCRIPTION : Holds variables and functions for LoadClass object
 *
 * NOTES : N/A
 *
 * Copyright Dalton Shadle 2019.  All rights reserved.
 *
 **************************************************************************************************/


package edu.coe.djshadle.trapmaster;

public class LoadClass {
    //************************************* Private Variables **************************************
    private String loadID_Str;
    private String loadEmail_Str;
    private String loadNickname_Str;
    private String loadBrand_Str;
    private String loadGauge_Str;
    private String loadLength_Str;
    private String loadGrain_Str;
    private String loadNotes_Str;

    //************************************* Public Functions ***************************************
    // Constructors
    public LoadClass() {
        /*******************************************************************************************
         * Function: LoadClass
         *
         * Purpose: Constructor for this class, no parameters so all values are initialized to temp
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        this.loadID_Str = "tempID";
        this.loadEmail_Str = "tempEmail";
        this.loadNickname_Str = "tempNickname";
        this.loadBrand_Str = "tempBrand";
        this.loadGauge_Str = "tempGauge";
        this.loadLength_Str = "tempLength";
        this.loadGrain_Str = "tempGrain";
        this.loadNotes_Str = "tempNotes";
    }

    public LoadClass(String loadEmail_Str, String loadNickname_Str,
                     String loadBrand_Str, String loadGauge_Str, String loadLength_Str,
                     String loadGrain_Str, String loadNotes_Str) {
        /*******************************************************************************************
         * Function: LoadClass
         *
         * Purpose: Constructor for this class with parameters
         *
         * Parameters: loadEmail_Str (IN) - email tagged with this load
         *             loadNickname_Str (IN) - name of the load
         *             loadBrand_Str (IN) - brand tagged with the load
         *             loadGauge_Str (IN) - gauge tagged with the load
         *             loadLength_Str (IN) - length tagged with the load
         *             loadGrain_Str (IN) - grain tagged with the load
         *             loadNotes_Str (IN) - notes tagged with the load
         *
         * Returns: None
         *
         ******************************************************************************************/

        this.loadID_Str = "tempID";
        this.loadEmail_Str = loadEmail_Str;
        this.loadNickname_Str = loadNickname_Str;
        this.loadBrand_Str = loadBrand_Str;
        this.loadGauge_Str = loadGauge_Str;
        this.loadLength_Str = loadLength_Str;
        this.loadGrain_Str = loadGrain_Str;
        this.loadNotes_Str = loadNotes_Str;
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
    public String getLoadEmail_Str() {
        return loadEmail_Str;
    }

    public void setLoadEmail_Str(String loadEmail_Str) {
        this.loadEmail_Str = loadEmail_Str;
    }

    public String getLoadNickname_Str() {
        return loadNickname_Str;
    }

    public void setLoadNickname_Str(String loadNickname_Str) {
        this.loadNickname_Str = loadNickname_Str;
    }

    public String getLoadBrand_Str() {
        return loadBrand_Str;
    }

    public void setLoadBrand_Str(String loadBrand_Str) {
        this.loadBrand_Str = loadBrand_Str;
    }

    public String getLoadGauge_Str() {
        return loadGauge_Str;
    }

    public void setLoadGauge_Str(String loadGauge_Str) {
        this.loadGauge_Str = loadGauge_Str;
    }

    public String getLoadLength_Str() {
        return loadLength_Str;
    }

    public void setLoadLength_Str(String loadLength_Str) {
        this.loadLength_Str = loadLength_Str;
    }

    public String getLoadGrain_Str() {
        return loadGrain_Str;
    }

    public void setLoadGrain_Str(String loadGrain_Str) {
        this.loadGrain_Str = loadGrain_Str;
    }

    public String getLoadNotes_Str() {
        return loadNotes_Str;
    }

    public void setLoadNotes_Str(String loadNotes_Str) {
        this.loadNotes_Str = loadNotes_Str;
    }
}
