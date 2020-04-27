package com.geekh.mystock.Activity;

/**
 * Created by ipc on 4/24/2016.
 */

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.geekh.mystock.Model.BO.Item;
import com.geekh.mystock.Model.BO.ItemBC;
import com.geekh.mystock.R;
import com.geekh.mystock.Scanner.FullScannerActivity;
/**
 * The Search page: search for an article using its code and deposit code
 * */
public class SearchActivity extends AppCompatActivity {
    private TextView mLabelView;
    private EditText mCategoryView, mFamilyView, mSubFamilyView, mUnitView;
    private EditText  mQuantityView, mCodeView, mWarehouseView;

    Button mScanButton, mSearchButton, mCancelButton;
    Item mItem;

    private static final int ZXING_CAMERA_PERMISSION = 1;
    private Class<?> mClss;

    String Code, Warehouse, bcode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** Display the graphical interface **/
        setContentView(R.layout.activity_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Display of the coordinates of the Item
        mLabelView = (TextView) findViewById(R.id.textLabel);
        mCodeView = (EditText)findViewById(R.id.textCode);
        mWarehouseView = (EditText)findViewById(R.id.textWarehouse);
        mCategoryView = (EditText)findViewById(R.id.textCategory);
        mFamilyView = (EditText)findViewById(R.id.textFamily);
        mSubFamilyView = (EditText)findViewById(R.id.textSubFamily);
        mUnitView = (EditText)findViewById(R.id.textUnit);
        mQuantityView = (EditText) findViewById(R.id.editQty);

        if(!Item.idI.equals("")){
            bcode= Item.idI;
            mCodeView.setText(Item.idI);
            Item.idI = "";
        }
        // Button: Item scan
        mScanButton = (Button) findViewById(R.id.btnScan);
        mScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    launchActivity(FullScannerActivity.class);
                    finish();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        // Button: Item search
        mSearchButton = (Button) findViewById(R.id.btnSearch);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(!mWarehouseView.getText().toString().equals("") && !mCodeView.getText().toString().equals("")){
                        Warehouse = mWarehouseView.getText().toString();
                        Code = mCodeView.getText().toString();
                        new loadItem().execute();
                    }
                    else if (mWarehouseView.getText().toString().equals("")) {
                        mWarehouseView.setError(getString(R.string.error_field_required));
                    }
                    else if (mCodeView.getText().toString().equals("")) {
                        mCodeView.setError(getString(R.string.error_field_required));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // We reset the article data
        mCancelButton = (Button) findViewById(R.id.btnCancel);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mLabelView.setText(getString(R.string.app_name));
                    mCodeView.setText("");
                    mWarehouseView.setText("");
                    mCategoryView.setText("");
                    mFamilyView.setText("");
                    mSubFamilyView.setText("");
                    mUnitView.setText("");
                    mQuantityView.setText("");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(SearchActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /** Load Item **/
    public class loadItem extends AsyncTask<Void, Void, Void> {
        boolean resCheck;
        Item item = new Item(SearchActivity.this);
        final ProgressDialog dialog = ProgressDialog.show(SearchActivity.this, "",getString(R.string.searching) , true);

        @Override
        protected void onPreExecute() {
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // Checking the availability of the connection
            /*resCheck = User.isConnectToServer();
            if (resCheck){
                /** Update item table **
                // Empty the article table
                item.dropUserItem(Warehouse);
                // Fill the item table
                ServerInteraction.GetItemWarehouse(SearchActivity.this);
            }*/
            // Recover the item from the local database in the form of an arraylist
            if(!bcode.equals("")){
                Code = new ItemBC(SearchActivity.this).getItemBC(bcode,Warehouse);
                bcode = "";
            }
            mItem = item.getUserItem(Code, Warehouse);
            Item.idI = "";
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            dialog.dismiss();
            /** The assignment of the parameters of the article found to the variables **/
            if(mItem != null) {
                mLabelView.setText(mItem.getLabel());
                mCodeView.setText(mItem.getCode());
                mWarehouseView.setText(mItem.getWarehouse());
                mCategoryView.setText(mItem.getCategory());
                mFamilyView.setText(mItem.getFamily());
                mSubFamilyView.setText(mItem.getSubFamily());
                mUnitView.setText(mItem.getUnit());
                mQuantityView.setEnabled(false);
                mQuantityView.setText(mItem.getQuantity());
            }
            else {
                Toast.makeText(getBaseContext(), getString(R.string.no_items), Toast.LENGTH_LONG).show();
            }
        }
    }
    /** Ensure camera permission **/
    public void launchActivity(Class<?> clss) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            mClss = clss;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, ZXING_CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(this, clss);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ZXING_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(mClss != null) {
                        Intent intent = new Intent(this, mClss);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(this, getString(R.string.ask_for_camera_permission), Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }
}
