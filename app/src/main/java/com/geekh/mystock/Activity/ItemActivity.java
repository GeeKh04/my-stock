package com.geekh.mystock.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.geekh.mystock.Model.BO.Item;

import com.geekh.mystock.Model.BO.Warehouse;
import com.geekh.mystock.R;
import com.geekh.mystock.Model.BO.User;
/**
 * The Item page: The display of information for an Item (Label, Quantity, etc.)
 * */
public class ItemActivity extends AppCompatActivity {
    TextView mLabelView, mCodeView, mWarehouseView, mCategoryView, mFamilyView, mSubFamilyView, mUnitView;
    EditText  mQuantityView;

    Button  mEditButton, mCancelButton;
    Item mItem;
    Item article = new Item(ItemActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		/** Display the graphical interface **/
        setContentView(R.layout.activity_item);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Return the Class from the selected Warehouse
        mItem = article.getUserItem(Item.idI, Warehouse.idW);
        Item.idI = "";
        mLabelView = (TextView)findViewById(R.id.textLabel);
        mCodeView = (TextView)findViewById(R.id.textCode);
        mWarehouseView = (TextView)findViewById(R.id.textWarehouse);
        mCategoryView = (TextView)findViewById(R.id.textCategory);
        mFamilyView = (TextView)findViewById(R.id.textFamily);
        mSubFamilyView = (TextView)findViewById(R.id.textSubFamily);
        mUnitView = (TextView)findViewById(R.id.textUnit);
        // Assign the item values to the variables in order to display them later
        mLabelView.setText(mItem.getLabel());
        mCodeView.setText(mItem.getCode());
        mWarehouseView.setText(Warehouse.idW);
        mCategoryView.setText(mItem.getCategory());
        mFamilyView.setText(mItem.getFamily());
        mSubFamilyView.setText(mItem.getSubFamily());
        mUnitView.setText(mItem.getUnit());

        mQuantityView = (EditText) findViewById(R.id.editQty);
        mQuantityView.setEnabled(false);
        mQuantityView.setText(mItem.getQuantity());

        /** Button: validate the modification **/
        mEditButton = (Button) findViewById(R.id.btnSubmit);
        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditButton.setText(getString(R.string.validate));
                //mEditButton.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_save_24,0);
                mQuantityView.setEnabled(true);
                mQuantityView.setText("");
                mEditButton = (Button) findViewById(R.id.btnSubmit);
                mEditButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            // edit the quantity
                            if(!mQuantityView.getText().toString().equals("")){
                                mQuantityView.setText(mQuantityView.getText().toString());
                                article.setItemEdit(mItem.getCode(), Warehouse.idW,mQuantityView.getText().toString());
                                User.edit=true;
                                mEditButton.setEnabled(true);
                                // Finish
                                ListItemActivity.getInstance().finish();
                                Intent intent = new Intent(ItemActivity.this, ListItemActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                mQuantityView.setError(getString(R.string.error_field_required));
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }
        });
        /* Button: Cancel the modification */
        mCancelButton = (Button) findViewById(R.id.btnCancel);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                        mQuantityView.setText(mItem.getQuantity());
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

                // Redirection to Class Page
                ListItemActivity.getInstance().finish();
                Intent intent = new Intent(ItemActivity.this, ListItemActivity.class);
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
}