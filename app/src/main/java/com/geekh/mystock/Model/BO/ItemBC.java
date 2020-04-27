package com.geekh.mystock.Model.BO;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.geekh.mystock.Model.DAO.DBManager;
import com.geekh.mystock.Model.DAO.UserManager;

import java.util.ArrayList;

/**
 * The ItemBC class
 */
public class ItemBC {
    private String barCode;
    private String warehouse;
    private String code;

    // Parameterized constructor
    public ItemBC(String barCode, String warehouse, String code) {
        this.barCode = barCode;
        this.warehouse = warehouse;
        this.code = code;
    }
    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    protected Activity activity;

    // Constructor
    public ItemBC(Activity context){
        this.activity = context;
    }

    /** Barcode reading **/
    public ArrayList<ItemBC> readItemBC() {
        ArrayList<ItemBC> mItem = null;
        mItem = new ArrayList<ItemBC>();
        try {
            UserManager mydb= new UserManager(activity);
            String[] allColumns =  { DBManager.BCode_Key, DBManager.BCode_KeyW, DBManager.BCode_KeyI};

            mydb.open();
            Cursor cursor = mydb.getDb().query(DBManager.BCode_TABLE_NAME,allColumns, null, null, null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                ItemBC art = new ItemBC(cursor.getString(0), cursor.getString(1), cursor.getString(2));
                mItem.add(art);
                cursor.moveToNext();
            }
            // closing the cursor
            cursor.close();
            mydb.close();
        } catch (Exception e) {
            Log.e("BACKGROUND_PROC", e.getMessage());
            return  null;
        }
        return mItem;
    }

    /** Add barcode **/
    public void addItemBC(String barcode, String code, String warehouse) {
        try{
            UserManager mydb = new UserManager(activity);
            mydb.open();
            ContentValues value = new ContentValues();
            value.put(DBManager.BCode_KeyI, code);
            value.put(DBManager.BCode_KeyW, warehouse);
            value.put(DBManager.BCode_Key, barcode);
            mydb.getDb().insert(DBManager.BCode_TABLE_NAME, null, value);
            mydb.close();

        } catch (Exception e) {
            Log.e("BACKGROUND_PROC", e.getMessage());
        }
    }
    /** Barcode search **/
    public String getItemBC(String barcode, String warehouse) {
        try {
            String code = "";

            UserManager mydb= new UserManager(activity);
            String[] allColumns =  { DBManager.BCode_Key, DBManager.BCode_KeyW, DBManager.BCode_KeyI};

            mydb.open();

            Cursor cursor = mydb.getDb().query(DBManager.BCode_TABLE_NAME,allColumns, DBManager.BCode_Key +
                    " LIKE \'"+ barcode+"\' AND "+DBManager.BCode_KeyW +
                    " LIKE \'"+ warehouse+"\'", null, null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                code = cursor.getString(2);
                cursor.moveToNext();
            }
            // closing the cursor
            cursor.close();
            mydb.close();
            return code;
        } catch (Exception e) {
            Log.e("BACKGROUND_PROC", e.getMessage());
            return  null;
        }
    }

    /** Clear the Item BarCode table **/
    public void dropUserItemBC() {
        try {
            UserManager mydb = new UserManager(activity);
            mydb.open();
            mydb.dropItemBC();
            mydb.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}