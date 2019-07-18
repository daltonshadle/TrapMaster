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
    }

    public ShooterClass(int shooterID_Int, String shooterName_Str) {
        /*******************************************************************************************
         * Function: ShooterClass
         *
         * Purpose: Constructor for this class with parameters
         *
         * Parameters: shooterID_Int (IN) - database id for shooter
         *             shooterName_Str (IN) - name for shooter
         *
         * Returns: None
         *
         ******************************************************************************************/

        this.shooterID_Int = shooterID_Int;
        this.shooterName_Str = shooterName_Str;
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
}
