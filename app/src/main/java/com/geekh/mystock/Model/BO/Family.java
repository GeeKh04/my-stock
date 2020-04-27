package com.geekh.mystock.Model.BO;

import android.app.Activity;
import android.database.Cursor;
import android.util.Log;

import com.geekh.mystock.Model.DAO.DBManager;
import com.geekh.mystock.Model.DAO.UserManager;

import java.util.ArrayList;

/**
 * Created by ipc on 5/21/2016.
 */
public class Family extends Class {
    // Parameterized constructor
    public Family(String code, String label){
        this.code = code;
        this.label = label;
    }

    protected Activity activity;

    // Constructor
    public Family(Activity context){
        this.activity = context;
    }

    /** Read the database families **/
    public ArrayList<Class> getUserFamilies(String idC){
        try{
            ArrayList<Class> mFamilies = new ArrayList<Class>();
            UserManager mydb= new UserManager(activity);
            String[] allColumns = { DBManager.Family_Key,  DBManager.Family_LABEL};

            mydb.open();
            Cursor cursor = mydb.getDb().query(DBManager.Family_TABLE_NAME,allColumns, DBManager.Family_KeyC +
                    " LIKE \""+ idC+"\"", null, null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Family family = new Family(cursor.getString(0),cursor.getString(1));
                mFamilies.add(family);
                cursor.moveToNext();
            }
            // closing the cursor
            cursor.close();
            mydb.close();

            return mFamilies;
        } catch (Exception e) {
            Log.e("BACKGROUND_PROC", e.getMessage());
            return null;
        }
    }

    /** Fill the Family table **/
    public void AddUserFamily(String id, String label, String category) {
        try {
            UserManager mydb = new UserManager(activity);
            mydb.open();
            mydb.AddUserFamily(id, label, category);
            mydb.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
