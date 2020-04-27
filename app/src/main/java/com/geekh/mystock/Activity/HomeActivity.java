package com.geekh.mystock.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.geekh.mystock.Model.BO.Warehouse;
import com.geekh.mystock.R;
import com.geekh.mystock.Model.BO.User;

/**
 * The home page: the main page for accessing the other pages
 * */
 
public class HomeActivity extends AppCompatActivity {
    private TextView mUserView; // username
    private TextView mEstabView; // establishment name
    Button mViewButton, mSearchButton, mPrintButton, mDropButton, mCloseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** Display the graphical interface **/
        setContentView(R.layout.activity_home);

        /** Display connection status **/
        if(User.isConnectToInternet(HomeActivity.this)) {
            Toast.makeText(getBaseContext(), getString(R.string.connected), Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getBaseContext(), getString(R.string.connected_on_offline_mode), Toast.LENGTH_LONG).show();
        }
        /** Display username and establishment name **/
        mUserView = (TextView)findViewById(R.id.user_profile_name);
        mEstabView = (TextView)findViewById(R.id.estab_profile_name);
        mUserView.setText(User.getUserName());
        mEstabView.setText(User.getEstabName());
        /** Button Enter Inventory **/
        mViewButton = (Button) findViewById(R.id.btnView);
        mViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirection to Depots page
                Warehouse.p = 0;
                Intent intent = new Intent(HomeActivity.this, ListWarehouseActivity.class);
                startActivity(intent);
            }
        });
        /** Button Search Item **/
        mSearchButton = (Button) findViewById(R.id.btnSearch);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirection to the Search page
                Warehouse.p = 1;
                Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

		/** Button Print Inventory **/
        mPrintButton = (Button) findViewById(R.id.btnPrint);
        mPrintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirection to Print page
                Warehouse.p = 02;
                Intent intent = new Intent(HomeActivity.this, ListWarehouseActivity.class);
                startActivity(intent);
            }
        });
        /** Button Initialize the database **/
        mDropButton = (Button) findViewById(R.id.btnDrop);
        mDropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // We call the RazDialog method
                new Dialogs(HomeActivity.this).dropDialog();
            }
        });
        /** Button Logout **/
        mCloseButton = (Button) findViewById(R.id.btnSignOut);
        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // We call the CloseDialog method
                new Dialogs(HomeActivity.this).closeDialog();
            }
        });
    }

}