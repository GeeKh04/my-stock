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
public class Category extends Class {
    // Parameterized constructor
    public Category(String code, String label){
        this.code = code;
        this.label = label;
    }

    protected Activity activity;

    // Constructor
    public Category(Activity context){
        this.activity = context;
    }

    /** Read the database categories **/
    public ArrayList<Class> getUserCategories(){
        try{
            ArrayList<Class> mCategories = new ArrayList<Class>();
            UserManager mydb = new UserManager(activity);
            String[] allColumns = { DBManager.Category_Key,  DBManager.Category_LABEL };

            mydb.open();
            Cursor cursor = mydb.getDb().query(DBManager.Category_TABLE_NAME,
                    allColumns, null, null, null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Category cat = new Category(cursor.getString(0),cursor.getString(1));
                mCategories.add(cat);
                cursor.moveToNext();
            }
            // closing the cursor
            cursor.close();
            mydb.close();

            return mCategories;
        } catch (Exception e) {
            Log.e("BACKGROUND_PROC", e.getMessage());
            return null;
        }
    }

    /** Fill the Category table **/
    public void AddUserCategory(String id, String label) {
        try {
            UserManager mydb = new UserManager(activity);
            mydb.open();
            mydb.AddUserCategory(id, label);
            mydb.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    /** Drop the Category table **/
    public void dropUserCategory() {
        try {
            UserManager mydb = new UserManager(activity);
            mydb.open();
            mydb.dropCategory();
            mydb.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
