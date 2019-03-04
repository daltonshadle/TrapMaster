package edu.coe.djshadle.trapmaster;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends AppCompatActivity {

    //********************************* Variables and Constants ************************************
    //General Constants
    final int MIN_PASS_LENGTH = 7;

    //General Variables
    private String mEmail_Str = "SAVED_EMAIL";
    private String mPassword_Str = "SAVED_PASS";
    DBHandler db;

    // UI References
    private EditText mEmail_View;
    private EditText mPassword_View;

    //********************************** Login Activity Functions **********************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DBHandler(this);

        // Set up the login form.
        setUpViews();
    }

    private void setUpViews() {
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

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putString(mEmail_Str, mEmail_View.getText().toString());
        savedInstanceState.putString(mPassword_Str, mPassword_View.getText().toString());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        String tempEmail_Str = savedInstanceState.getString(mEmail_Str);
        String tempPass_Str = savedInstanceState.getString(mPassword_Str);

        mEmail_View.setText(tempEmail_Str);
        mPassword_View.setText(tempPass_Str);
    }

    //************************************** Login Functions ***************************************
    private void attemptLogin() {
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
        } else if (!isEmailInDB(email)) {
            //TODO: give notification that email is not in DB
            mEmail_View.setError(getString(R.string.error_invalid_email));
            focusView = mEmail_View;
            cancel = true;
        }

        // Check for a valid password.
        if (TextUtils.isEmpty(password)) {
            mPassword_View.setError(getString(R.string.error_field_required));
            focusView = mPassword_View;
            cancel = true;
        } else if (!isPasswordWithEmail(email, password)) {
            //TODO: give notification that password does not match email
            mPassword_View.setError(getString(R.string.error_incorrect_password));
            focusView = mPassword_View;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            //Login was successful; continue to next activity
            Intent homeActivity_Intent = new Intent(LoginActivity.this, homeActivity.class);
            homeActivity_Intent.putExtra(getString(R.string.current_user_email), email);
            startActivity(homeActivity_Intent);
            finish();
        }
    }

    private boolean isEmailInDB(String email) {
        return db.isEmailInDB(email);
    }

    private boolean isPasswordWithEmail(String email, String password) {
        return db.doesPassMatchInDB(email, password);
    }

    //************************************ Register Functions **************************************
    private void register() {
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
            //Login was successful; continue to next activity as new user
            ProfileClass p = new ProfileClass(email, password);
            db.insertProfileInDB(p);

            Intent homeActivity_Intent = new Intent(LoginActivity.this, homeActivity.class);
            homeActivity_Intent.putExtra(getString(R.string.current_user_email), email);
            startActivity(homeActivity_Intent);
            finish();
        }
    }

    private boolean isEmailValid(String email) {
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
        return password.length() >= MIN_PASS_LENGTH;
    }

}

