/***************************************************************************************************
 * FILENAME : ShooterClass.java
 *
 * AUTHOR : Dalton Shadle
 *
 * DESCRIPTION : Holds variables and functions for ShooterClass object
 *
 * NOTES : N/A
 *
 * Copyright Dalton Shadle 2019.  All rights reserved.
 *
 **************************************************************************************************/

package edu.coe.djshadle.trapmaster;

import java.util.ArrayList;
import java.util.Arrays;

public class ShooterClass {
    //************************************* Private Variables **************************************
    private int shooterID_Int;
    private String shooterName_Str;  //Key
    private String shooterCoach_Str;
    private ArrayList<String> shooterTeamName_Array;

    //************************************* Public Functions ***************************************
    // Constructors
    public ShooterClass() {
        /*******************************************************************************************
         * Function: ShooterClass
         *
         * Purpose: Constructor for this class, no parameters so all values are initialized to temp
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        this.shooterID_Int = -1;
        this.shooterName_Str = "";
        this.shooterCoach_Str = "";
        this.shooterTeamName_Array = new ArrayList<>();
    }

    public ShooterClass(int shooterID_Int, String shooterName_Str, String shooterCoach_Str,
                        ArrayList<String> shooterTeamName_Array) {
        /*******************************************************************************************
         * Function: ShooterClass
         *
         * Purpose: Constructor for this class with parameters
         *
         * Parameters: shooterID_Int (IN) - database id for shooter
         *             shooterName_Str (IN) - name for shooter
         *             shooterCoach_Str (IN) - coach for shooter
         *             shooterTeamName_Array (IN) - list of teams shooter is on
         *
         * Returns: None
         *
         ******************************************************************************************/

        this.shooterID_Int = shooterID_Int;
        this.shooterName_Str = shooterName_Str;
        this.shooterCoach_Str = shooterCoach_Str;
        this.shooterTeamName_Array = shooterTeamName_Array;
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

    public int getShooterID_Int() {
        return shooterID_Int;
    }

    public void setShooterID_Int(int shooterID_Int) {
        this.shooterID_Int = shooterID_Int;
    }

    public String getShooterName_Str() {
        return shooterName_Str;
    }

    public void setShooterName_Str(String shooterName_Str) {
        this.shooterName_Str = shooterName_Str;
    }

    public String getShooterCoach_Str() {
        return shooterCoach_Str;
    }

    public void setShooterCoach_Str(String shooterCoach_Str) {
        this.shooterCoach_Str = shooterCoach_Str;
    }

    public ArrayList<String> getShooterTeamName_Array() {
        return shooterTeamName_Array;
    }

    public void setShooterTeamName_Array(ArrayList<String> shooterTeamName_Array) {
        this.shooterTeamName_Array = shooterTeamName_Array;
    }

    public String getShooterTeamNameArray_toString() {
        String teamNameList_Str = "";

        if (!shooterTeamName_Array.isEmpty()) {
            teamNameList_Str = shooterTeamName_Array.get(0);

            for (int i = 1; i < shooterTeamName_Array.size(); i++) {
                teamNameList_Str += "," + shooterTeamName_Array.get(i);
            }
        }

        return shooterName_Str;
    }

    public void setShooterTeamNameArray_fromString(String shooterTeamNameArray_Str) {
        ArrayList<String> tempTeamName_Array = new ArrayList<>();

        if (!shooterTeamNameArray_Str.isEmpty()) {
            // string has some names
            String[] name_Array = shooterTeamNameArray_Str.split(",");
            tempTeamName_Array = new ArrayList( Arrays.asList(name_Array) );
        }

        shooterTeamName_Array = tempTeamName_Array;
    }
}
