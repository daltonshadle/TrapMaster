/***************************************************************************************************
 * FILENAME : FirebaseInterfaceSQL.java
 *
 * AUTHOR : Dalton Shadle
 *
 * DESCRIPTION : Holds functions for backing up database to Firebase
 *
 * NOTES : N/A
 *
 * Copyright Dalton Shadle 2019.  All rights reserved.
 *
 **************************************************************************************************/


package edu.coe.djshadle.trapmaster;

//******************************************** Imports *********************************************
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FirebaseInterfaceSQL {
    //**************************************** Constants *******************************************
    private final String TAG = "JRW";

    // User Firebase
    private final String FIRE_USER = "user";

    // Guns Firebase
    private final String FIRE_GUNS = "guns";
    private final String FIRE_GUNS_ID = "ID";
    private final String FIRE_GUNS_EMAIL = "email";
    private final String FIRE_GUNS_NICKNAME = "nickname";
    private final String FIRE_GUNS_GAUGE = "gauge";
    private final String FIRE_GUNS_MODEL = "model";
    private final String FIRE_GUNS_NOTES = "notes";


    // Loads Firebase
    private final String FIRE_LOADS = "loads";

    // Shots Firebase
    private final String FIRE_SHOTS = "shots";

    //************************************* Private Variables **************************************
    private FirebaseAuth auth;


    //************************************* Public Functions ***************************************
    // Constructors
    public FirebaseInterfaceSQL() {
        // Initialize Google code for storing
        auth = FirebaseAuth.getInstance();
    }

    // Other functions
    public void insertGunInFirbase(GunClass g) {
        FirebaseFirestore database =  FirebaseFirestore.getInstance();
        FirebaseUser user =  auth.getCurrentUser();
        String userId = user.getUid();

        String gunNickname_Str = g.getGunNickname_Str();

        Map<String, Object> gun = new HashMap<>();
        gun.put(FIRE_GUNS_NICKNAME, g.getGunNickname_Str());
        gun.put(FIRE_GUNS_EMAIL, g.getGunProfileID_Int());
        gun.put(FIRE_GUNS_GAUGE, g.getGunGauge_Str());
        gun.put(FIRE_GUNS_MODEL, g.getGunModel_Str());
        gun.put(FIRE_GUNS_NOTES, g.getGunNotes_Str());

        database.collection(FIRE_GUNS).document(userId).collection(gunNickname_Str).document(gunNickname_Str).set(gun);
    }

    public void deleteGunInFirbase(GunClass g) {
        FirebaseFirestore database =  FirebaseFirestore.getInstance();
        FirebaseUser user =  auth.getCurrentUser();
        String userId = user.getUid();

        String gunNickname_Str = g.getGunNickname_Str();

        database.collection(FIRE_GUNS).document(userId).collection(gunNickname_Str).document(gunNickname_Str).delete();
    }

    public void updateGunInFirbase(GunClass g) {
        FirebaseFirestore database =  FirebaseFirestore.getInstance();
        FirebaseUser user =  auth.getCurrentUser();
        String userId = user.getUid();

        String gunNickname_Str = g.getGunNickname_Str();

        database.collection(FIRE_GUNS).document(userId).collection(gunNickname_Str).document(gunNickname_Str).delete();
    }
}
