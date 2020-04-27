package com.geekh.mystock.Model.BO;

import android.app.Activity;
import android.database.Cursor;
import com.geekh.mystock.Model.DAO.DBManager;
import com.geekh.mystock.Model.DAO.UserManager;
import java.util.ArrayList;

/**
 * The Warehouse class
 */
public class Warehouse {
    private String code;
    private String label;
    public static String idW = "";
    public static int p = 0;

    // Parameterized constructor
    public Warehouse(String code, String label){
        this.code = code;
        this.label = label;
    }

    // Return the code
    public String getCode() {
        return code;
    }
    // Return the name of the Warehouse
    public String getLabel() {
        return label;
    }
    /***/
    protected Activity activity;

    // Constructor
    public Warehouse(Activity context){
        this.activity = context;
    }

    /** Recovering database warehouse, Returning a list of warehouses **/
    public ArrayList<Warehouse> getUserWarehouses(){
        try{
            ArrayList<Warehouse> mWarehouses = new ArrayList<Warehouse>();
            UserManager mydb= new UserManager(activity);
            String[] allColumns = { DBManager.Warehouse_Key,  DBManager.Warehouse_LABEL };

            mydb.open();
            Cursor cursor = mydb.getDb().query(DBManager.Warehouse_TABLE_NAME,
                    allColumns, null, null, null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Warehouse dp = new Warehouse(cursor.getString(0),cursor.getString(1));
                mWarehouses.add(dp);
                cursor.moveToNext();
            }
            // closing the cursor
            cursor.close();
            mydb.close();

            return mWarehouses;
        } catch (Exception e) {
            return null;
        }
    }

    /** Fill the Warehouses table **/
    public void AddUserDepot(String id, String label) {
        try {
            UserManager mydb = new UserManager(activity);
            mydb.open();
            mydb.AddUserWarehouse(id, label);
            mydb.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    /** Empty the Warehouses table **/
    public void dropUserWarehouse() {
        try {
            UserManager mydb = new UserManager(activity);
            mydb.open();
            mydb.dropWarehouse();
            mydb.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
