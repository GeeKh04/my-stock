package com.geekh.mystock.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.geekh.mystock.Model.BO.SaveSettings;
import com.geekh.mystock.R;
import com.geekh.mystock.Model.BO.User;

/**
 * The login page is used to authenticate the user
 * by its establishment number and password.
 * */
public class LoginActivity extends AppCompatActivity {

    private static EditText mEstabNumberView; // Establishment number
    private static EditText mPasswordView; // Password
    private Button mLogInButton;
    private SaveSettings mData;
    private Dialogs mDialog = new Dialogs(LoginActivity.this);
    public static Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** Display the graphical interface **/
        setContentView(R.layout.activity_login);

        /** Button IP Configuration **/
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.ipConfigDialog();
            }
        });

        /** Login button **/
        mLogInButton = (Button) findViewById(R.id.btnLogin);
        mLogInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // Authentication
                AuthentifyLogin();
            }
        });
    }
    
    @Override
    public void onBackPressed() {
        // Disable return to MainActivity
        moveTaskToBack(true);
    }
    /**
     * this function allows user authentication
     * if authentication is correct, it calls the onLoginSuceess() function
     * otherwise it calls onLoginFailed.
     */
    private void AuthentifyLogin() {
        // Retrieve the data entered by the user (establishment number and password).
        mEstabNumberView = (EditText) findViewById(R.id.editEstabNumber);
        mPasswordView = (EditText) findViewById(R.id.editPassword);

        // Retrieve the IP address of the server
        SaveSettings mData = new SaveSettings(LoginActivity.this);
        String ip = mData.loadValue("MyIP");
        /** Convert the data entered to a character string and assign them to the class Login
        * Create a new user **/
        User user = new User(mEstabNumberView.getText().toString(),mPasswordView.getText().toString(), ip);
        // Check the data
        if (!verify()) {
            onLoginFailed();
            return;
        }
        // if the Internet connection is available.
        if (User.isConnectToInternet(LoginActivity.this)) {
            mLogInButton.setEnabled(false);
            new Authentify().execute();
            mLogInButton.setEnabled(true);
        }
        // Otherwise: Display an error message
        else {
            mDialog.createToast(getString(R.string.error_field_CInternet));
        }
    }

    /** A function is executed when authentication is established **/
    public void onLoginSuccess() {
        // Redirection to Inventory page MENU
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    /** A function is executed when the authentication is failed **/
    public void onLoginFailed() {
        mDialog.createToast(getString(R.string.error_field_login));
    }

    /** A first verification of data entered by the user **/
    public boolean verify() {
        boolean valide = true;

        /** Verification of the validity of the establishment number. **/
        // Check if the establishment number is empty
        if (User.getEstabNumber().isEmpty()) {
            mEstabNumberView.setError(getString(R.string.error_field_required));
            valide = false;
        }
        // Check if the establishment number is length 4
        else if (User.getEstabNumber().length() != 4) {
            mEstabNumberView.setError(getString(R.string.error_invalid_estabnumber));
            valide = false;
        }else {
            mEstabNumberView.setError(null);
        }
        /** Password validity check. **/
        // Check if the establishment number is empty
        if (User.getPassword().isEmpty()) {
            mPasswordView.setError(getString(R.string.error_field_required));
            valide = false;
        }
        //Check if the password is length 6
        else if (User.getPassword().length() != 6) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            valide = false;
        }else {
            mPasswordView.setError(null);
        }
        return valide;
    }

    /** User authentication if the device is connected **/
    public class Authentify extends AsyncTask<Void, Void, Void> {
        boolean resCheck;
        int resConnect;
        final ProgressDialog dialog = ProgressDialog.show(LoginActivity.this, "",getString(R.string.connecting) , true);

        @Override
        protected void onPreExecute() {
            // Show progress bar
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            /** Test the connection with the server **/
            resCheck = User.isConnectToServer();
            // If the connection with the server is established
            if (resCheck){
                // Send user data to the server, and retrieve the response in resConnect
                resConnect  = User.Authentify();
                // If the authentication is correct
                if(resConnect == 1){
                    // Save user data for the next login
                    mData = new SaveSettings(LoginActivity.this);
                    mData.saveValue("UserID", User.getPassword());
                    mData.saveValue("EstabID", User.getEstabNumber());
                    mData.saveValue("UserName", User.getUserName());
                    mData.saveValue("EstabName", User.getEstabName());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Close progress bar
            dialog.dismiss();
            // If the connection with the server is incorrect
            if(!resCheck) {
                Dialogs dialogs = new Dialogs(LoginActivity.this);
                dialogs.ipWarningDialog();
            }
            else{
                // If the authentication is correct
                if (resConnect==1){
                    onLoginSuccess();
                }
                // If authentication is incorporated: Display an error message
                else if (resConnect==0){
                    mDialog.createToast(getString(R.string.error_invalid_estabnumber_or_password));
                }
                // If a connection problem: Display an error message
                else  if (resConnect==-1){
                    mDialog.createToast(getString(R.string.error_field_CServer));
                }
            }
        }
    }

}

