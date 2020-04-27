package com.geekh.mystock.Model.BO;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.geekh.mystock.Model.DAO.DBManager;
import com.geekh.mystock.Model.DAO.UserManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ipc on 5/22/2016.
 */
public class EditWarehouse {
    protected Activity activity;

    // Constructor
    public EditWarehouse(Activity context){
        this.activity = context;
    }

    /** Reading EditWarehouses **/
    public List<String> readEditWarehouses() {

        List<String> chaine = null;
        chaine = new ArrayList<String>();
        try {
            UserManager mydb= new UserManager(activity);
            String[] allColumns =  { DBManager.EWarehouses_Key};

            mydb.open();
            Cursor cursor = mydb.getDb().query(DBManager.EWarehouses_TABLE_NAME,allColumns, null, null, null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                chaine.add(cursor.getString(0));
                cursor.moveToNext();
            }
            // closing the cursor
            cursor.close();
            mydb.close();
        } catch (Exception e) {
            Log.e("BACKGROUND_PROC", e.getMessage());
            return  null;
        }
        return chaine;
    }

    /** Add the Warehouse code **/
    public void saveEditWarehouse(String code) {
        try{
            UserManager mydb = new UserManager(activity);
            mydb.open();
            ContentValues value = new ContentValues();

            value.put(DBManager.EWarehouses_Key, code);

            mydb.getDb().insert(DBManager.EWarehouses_TABLE_NAME, null, value);
            mydb.close();
        } catch (Exception e) {
            Log.e("BACKGROUND_PROC", e.getMessage());
        }
    }

    /** Clear the EditWarehouses table **/
    public void dropUserEDitWarehouse() {
        try {
            UserManager mydb = new UserManager(activity);
            mydb.open();
            mydb.dropEditWarehouse();
            mydb.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
