package com.geekh.mystock.Activity;

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

import com.geekh.mystock.Model.BO.Class;
import com.geekh.mystock.Model.BO.Item;
import com.geekh.mystock.Model.BO.ItemBC;
import com.geekh.mystock.Model.BO.User;
import com.geekh.mystock.Model.BO.Warehouse;
import com.geekh.mystock.R;
import com.geekh.mystock.Scanner.FullScannerActivity;

/**
 * The Add Item page: add an item to the local base
 * */
public class AddItemActivity extends AppCompatActivity {
    private TextView mLabelView;
    private EditText mCategoryView,mFamilyView, mSubFamilyView, mUnitView;
    private EditText  mQuantityView, mCodeView, mBarCodeView;
    Button mSendButton, mCancelButton, mScanButton;

    private static final int ZXING_CAMERA_PERMISSION = 1;
    private java.lang.Class mClass;

    Dialogs mDialog = new Dialogs(AddItemActivity.this);
    String code, barCode = "", label, warehouse, category, family , subFamily, quantity, unit;
    public static boolean Add = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		/** Display the graphical interface **/
        setContentView(R.layout.activity_additem);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // the display of the coordinates of the Item
        mLabelView = (TextView) findViewById(R.id.textLabel);
        mCodeView = (EditText)findViewById(R.id.textCode);
        mBarCodeView = (EditText)findViewById(R.id.textBarCode);
        mCategoryView = (EditText)findViewById(R.id.textCategory);
        mFamilyView = (EditText)findViewById(R.id.textFamily);
        mSubFamilyView = (EditText)findViewById(R.id.textSubFamily);
        mUnitView = (EditText)findViewById(R.id.textUnit);
        mQuantityView = (EditText) findViewById(R.id.editQty);
        if(!Item.idI.equals("")){
            mBarCodeView.setText(Item.idI);
            Item.idI = "";
        }

        mCategoryView.setText(Class.idC);
        mFamilyView.setText(Class.idF);
        mSubFamilyView.setText(Class.idSF);
        /** Button Scan an Item **/
        mScanButton = (Button) findViewById(R.id.btnScan);
        mScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Add = true;
                    launchActivity(FullScannerActivity.class);// launch of the Barcode Scanner
                    finish();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        /** Button Send **/
        mSendButton = (Button) findViewById(R.id.btnAdd);
        // Sends captured data to the server
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(!mCodeView.getText().toString().equals("")
                            && !mLabelView.getText().toString().equals("")
                            && !mCategoryView.getText().toString().equals("")
                            && !mFamilyView.getText().toString().equals("")
                            && !mSubFamilyView.getText().toString().equals("")
                            && !mUnitView.getText().toString().equals("")
                            && !mQuantityView.getText().toString().equals("")){
                        warehouse = Warehouse.idW;
                        code = mCodeView.getText().toString();
                        if(!mBarCodeView.getText().toString().equals("")){
                            barCode = mBarCodeView.getText().toString();
                        }
                        label = mLabelView.getText().toString();
                        category = mCategoryView.getText().toString();
                        family =mFamilyView.getText().toString();
                        subFamily = mSubFamilyView.getText().toString();
                        unit = mUnitView.getText().toString();
                        quantity = mQuantityView.getText().toString();
                        User.edit=true;
                        new addArticle().execute();
                        ListItemActivity.getInstance().finish();
                        Intent intent = new Intent(AddItemActivity.this, ListItemActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else if (mCodeView.getText().toString().equals("")) {
                        mCodeView.setError(getString(R.string.error_field_required));
                    }
                    else if (mLabelView.getText().toString().equals("")) {
                        mLabelView.setError(getString(R.string.error_field_required));
                    }
                    else if (mCategoryView.getText().toString().equals("")) {
                        mCategoryView.setError(getString(R.string.error_field_required));
                    }
                    else if (mFamilyView.getText().toString().equals("")) {
                       mFamilyView.setError(getString(R.string.error_field_required));
                    }
                    else if (mSubFamilyView.getText().toString().equals("")) {
                        mSubFamilyView.setError(getString(R.string.error_field_required));
                    }
                    else if (mUnitView.getText().toString().equals("")) {
                        mUnitView.setError(getString(R.string.error_field_required));
                    }
                    else if (mQuantityView.getText().toString().equals("")) {
                        mQuantityView.setError(getString(R.string.error_field_required));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Reset variables to their original values
        mCancelButton = (Button) findViewById(R.id.btnCancel);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mLabelView.setText("");
                    mCodeView.setText("");
                    mBarCodeView.setText("");
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
    // Return to the Start page
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(AddItemActivity.this, ListItemActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        LoginActivity.mActivity = this;
    }
    /** Add Item **/
    public class addArticle extends AsyncTask<Void, Void, Void> {
        int resAdd=0;
        final ProgressDialog dialog = ProgressDialog.show(AddItemActivity.this, "",getString(R.string.adding_article), true);

        @Override
        protected void onPreExecute() {
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Save the article data in the Item table
                resAdd = new Item(AddItemActivity.this).AddUserArticle(code, label, warehouse, category, family, subFamily, unit, quantity);
                // Save the article data in the ItemBC table
                if(!barCode.equals("")){
                    new ItemBC(AddItemActivity.this).addItemBC(barCode,code,  warehouse);
                }
            }
            catch (Exception ex){
                resAdd = 0;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            dialog.dismiss();
            if(resAdd == 1) {
                mDialog.createToast(getString(R.string.added_successfully));
            }
            else if(resAdd == -1){
                mDialog.createToast(getString(R.string.item_exists));
            }else {
                mDialog.createToast(getString(R.string.add_not_made)+resAdd);
            }
        }
    }
    /** Ensure permission of the camera **/
    public void launchActivity(java.lang.Class c) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            mClass = c;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, ZXING_CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(this, c);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ZXING_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(mClass != null) {
                        Intent intent = new Intent(this, mClass);
                        startActivity(intent);
                    }
                } else {
                    mDialog.createToast(getString(R.string.ask_for_camera_permission));
                }
                return;
        }
    }
}

