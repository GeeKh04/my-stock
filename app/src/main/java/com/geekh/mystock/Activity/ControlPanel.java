package com.geekh.mystock.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import com.geekh.mystock.Model.BO.SaveSettings;
import com.geekh.mystock.Model.BO.User;
import com.geekh.mystock.R;

/**
 * The control page allows you to verify the existence of an user account
 * by its establishment number and password: Redirection to the Inventory page MENU
 * otherwise Redirection to Login page
 * */
public class ControlPanel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** Display the graphical interface **/
        setContentView(R.layout.activity_controlpanel);
        // The call to control
        new ControlLogin().execute();
    }

    /** The existence controller of an user account **/
    public class ControlLogin extends AsyncTask<Void, Void, Void> {
        boolean resCheck = false;
        boolean resData = false;
        boolean resConnect = false;
        int resS;
        SaveSettings saveSettings = new SaveSettings(ControlPanel.this);
        ProgressBar progressBarFooter = (ProgressBar) findViewById(R.id.progressBar1);

        @Override
        protected void onPreExecute() {
            progressBarFooter.getIndeterminateDrawable().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
            progressBarFooter.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
			/** Test data existence **/
            resData = saveSettings.LoadData();
			// If data exists
            if(resData){
                resCheck = User.isConnectToInternet(ControlPanel.this);
				// If the internet connection is established
                if(resConnect) {
					// If the connection with the server is established
                    resCheck = User.isConnectToServer();
					// check the recovered data
                    if (resCheck){
                        resS  = User.Authentify();
                    }
                }
            }
            SystemClock.sleep(5000);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //progressBarFooter.setVisibility(View.GONE);
			// If the data does not exist
            if(!resData) {   /************************** edited **************************************/
				// Redirection to Login page
                Intent intent=new Intent(ControlPanel.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
			// If data exists
            else {
				// If the internet connection is not established
                if(!resConnect) {/************************** edited **************************************/
					// Redirection to Inventory page MENU
                    Intent intent=new Intent(ControlPanel.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
                } 
				// If the internet connection is established
				else{
					// If the connection with the server is established
                    if (resCheck) {
                        if (resS == 1) {
							// Redirection to Inventory page MENU
                            Intent intent = new Intent(ControlPanel.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
							// Redirection to Login page
                            Intent intent = new Intent(ControlPanel.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
					// If the connection with the server is not established
                    else {
						// Redirection to Inventory page MENU
                        Intent intent = new Intent(ControlPanel.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        }
    }

}