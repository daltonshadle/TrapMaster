/***************************************************************************************************
 * FILENAME : LoginActivity.java
 *
 * AUTHOR : Dalton Shadle
 *
 * DESCRIPTION : Holds functions for the Login activity of this application
 *
 * NOTES : N/A
 *
 * Copyright Dalton Shadle 2019.  All rights reserved.
 *
 **************************************************************************************************/

package edu.coe.djshadle.trapmaster;

//******************************************** Imports *********************************************
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    //********************************* Variables and Constants ************************************
    //General Constants
    private String ACTIVITY_TITLE;
    private String CURRENT_USER_KEY;
    private String CURRENT_PASS_KEY;
    private final int MIN_PASS_LENGTH = 7;
    private final String TAG = "JRW";

    //General Variables
    DBHandler db;
    private String mEmail_Str;

    // UI References
    private EditText mEmail_View;
    private EditText mPassword_View;

    //Google Variables
    FirebaseAuth auth;

    //************************************* Activity Functions *************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*******************************************************************************************
         * Function: onCreate
         *
         * Purpose: When activity is started, function initializes the activity with any
         *          saved instances
         *
         * Parameters: savedInstanceState (IN) - provides the saved instances from previous state
         *
         * Returns: None
         *
         ******************************************************************************************/

        super.onCreate(savedInstanceState);

        initializeConstants();

        setContentView(R.layout.activity_login);

        db = new DBHandler(getApplicationContext());

        // Set up the login form.
        initializeViews();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        /*******************************************************************************************
         * Function: onSaveInstanceState
         *
         * Purpose: Function saves instances when activity is paused
         *
         * Parameters: savedInstanceState (OUT) - provides the saved instances from current state
         *
         * Returns: None
         *
         ******************************************************************************************/

        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putString(CURRENT_USER_KEY, mEmail_View.getText().toString());
        savedInstanceState.putString(CURRENT_PASS_KEY, mPassword_View.getText().toString());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        /*******************************************************************************************
         * Function: onRestoreInstanceState
         *
         * Purpose: Function restores instances when activity resumes
         *
         * Parameters: savedInstanceState (IN) - provides the saved instances from previous state
         *
         * Returns: None
         *
         ******************************************************************************************/

        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        String tempEmail_Str = savedInstanceState.getString(CURRENT_USER_KEY);
        String tempPass_Str = savedInstanceState.getString(CURRENT_PASS_KEY);

        mEmail_View.setText(tempEmail_Str);
        mPassword_View.setText(tempPass_Str);
    }

    private void initializeConstants() {
        /*******************************************************************************************
         * Function: initializeConstants
         *
         * Purpose: When activity is started, function initializes the activity with constants
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        ACTIVITY_TITLE = getString(R.string.login_activity_title);
        CURRENT_USER_KEY = getString(R.string.current_user_key);
        CURRENT_PASS_KEY = getString(R.string.current_pass_key);
    }

    //************************************* UI View Functions **************************************
    private void initializeViews() {
        /*******************************************************************************************
         * Function: initializeViews
         *
         * Purpose: Function initializes all variables and all UI views to a resource id
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        mEmail_View = findViewById(R.id.email);
        mPassword_View = findViewById(R.id.password);

        mPassword_View.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignIn_Button = findViewById(R.id.email_sign_in_button);
        mEmailSignIn_Button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button mEmailRegisterButton = findViewById(R.id.email_register_button);
        mEmailRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        Button mQuickEventButton = findViewById(R.id.btnLoginQuickEvent);
        mQuickEventButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newEventActivity_Intent = new Intent(LoginActivity.this, NewEventActivity.class);
                newEventActivity_Intent.putExtra(getString(R.string.quick_event_flag_key), true);
                startActivity(newEventActivity_Intent);
            }
        });

        // Initialize Google/Firebase Auth
        auth = FirebaseAuth.getInstance();

        // If user is already signed in, go straight to the home page
        if (auth.getCurrentUser() != null) {
            mEmail_Str = auth.getCurrentUser().getEmail();
            Intent homeActivity_Intent = new Intent(LoginActivity.this, homeActivity.class);
            homeActivity_Intent.putExtra(CURRENT_USER_KEY, mEmail_Str);
            startActivity(homeActivity_Intent);
            finish();
        }

        // Set activity title
        setTitle(ACTIVITY_TITLE);
    }

    //************************************** Login Functions ***************************************
    private void attemptLogin() {
        /*******************************************************************************************
         * Function: attemptLogin
         *
         * Purpose: Function processes login information and compares with database, goes to home
         *          if criteria is met, Firebase will handle email and password authentication
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        // Reset errors.
        mEmail_View.setError(null);
        mPassword_View.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmail_View.getText().toString();
        String password = mPassword_View.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmail_View.setError(getString(R.string.error_field_required));
            focusView = mEmail_View;
            cancel = true;
        }

        // Check for a valid password.
        if (TextUtils.isEmpty(password)) {
            mPassword_View.setError(getString(R.string.error_field_required));
            focusView = mPassword_View;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            signUserWithFireBase(email, password);
        }
    }

    //************************************ Register Functions **************************************
    private void register() {
        /*******************************************************************************************
         * Function: register
         *
         * Purpose: Function processes register information and puts into database, goes to home
         *          if criteria is met
         *
         * Parameters: None
         *
         * Returns: None
         *
         ******************************************************************************************/

        // Reset errors.
        mEmail_View.setError(null);
        mPassword_View.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmail_View.getText().toString();
        String password = mPassword_View.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmail_View.setError(getString(R.string.error_field_required));
            focusView = mEmail_View;
            cancel = true;
        } else if (!isEmailValid(email)) {
            //TODO: give notification that email is not valid
            mEmail_View.setError(getString(R.string.error_invalid_email_format));
            focusView = mEmail_View;
            cancel = true;
        }

        // Check for a valid password.
        if (TextUtils.isEmpty(password)) {
            mPassword_View.setError(getString(R.string.error_field_required));
            focusView = mPassword_View;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            //TODO: give notification that password is not valid
            mPassword_View.setError(getString(R.string.error_incorrect_password_length));
            focusView = mPassword_View;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Google Code to register user
            registerUserWithFireBase(email, password);
        }
    }

    private boolean isEmailValid(String email) {
        /*******************************************************************************************
         * Function: isEmailValid
         *
         * Purpose: Function checks if email is of a valid format
         *
         * Parameters: email (IN) - email to check format
         *
         * Returns: True if the email is the correct format
         *
         ******************************************************************************************/

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;

        return pat.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        /*******************************************************************************************
         * Function: isPasswordValid
         *
         * Purpose: Function checks if password is of a valid format
         *
         * Parameters: password (IN) - password to check for valid format
         *
         * Returns: True if the password is the correct format
         *
         ******************************************************************************************/

        return password.length() >= MIN_PASS_LENGTH;
    }

    //************************************* Google Functions ***************************************
    private void registerUserWithFireBase(final String email, final String password){
        /*******************************************************************************************
         * Function: registerUserWithFireBase
         *
         * Purpose: Function registers a new user with Firebase
         *
         * Parameters: email (IN) - email of user to register
         *             password (IN) - password of user to register
         *
         * Returns: None
         *
         ******************************************************************************************/

        //create user
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            String exceptionMsg_Str = "Registration failed! " + task.getException().getMessage();

                            Toast.makeText(LoginActivity.this, exceptionMsg_Str,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            //Login was successful; continue to next activity as new user
                            ProfileClass p = new ProfileClass(email);
                            db.insertProfileInDB(p);

                            Intent homeActivity_Intent = new Intent(LoginActivity.this, homeActivity.class);
                            homeActivity_Intent.putExtra(CURRENT_USER_KEY, email);
                            startActivity(homeActivity_Intent);
                            finish();
                        }
                    }
                });
    }

    private void signUserWithFireBase(final String email, final String password){
        /*******************************************************************************************
         * Function: signUserWithFireBase
         *
         * Purpose: Function signs in a new user with Firebase
         *
         * Parameters: email (IN) - email of user to sign in
         *             password (IN) - password of user to sign in
         *
         * Returns: None
         *
         ******************************************************************************************/

        //authenticate user
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            // there was an error
                            String exceptionMsg_Str = "Sign in failed! " + task.getException().getMessage();

                            Toast.makeText(LoginActivity.this, exceptionMsg_Str,
                                    Toast.LENGTH_LONG).show();
                        } else {
                            //Login was successful; continue to next activity
                            Intent homeActivity_Intent = new Intent(LoginActivity.this, homeActivity.class);
                            homeActivity_Intent.putExtra(CURRENT_USER_KEY, email);
                            startActivity(homeActivity_Intent);
                            finish();
                        }
                    }
                });
    }

}

