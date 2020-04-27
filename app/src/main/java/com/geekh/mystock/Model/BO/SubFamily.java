package com.geekh.mystock.Model.BO;

import android.app.Activity;
import android.database.Cursor;

import com.geekh.mystock.Model.DAO.DBManager;
import com.geekh.mystock.Model.DAO.UserManager;

import java.util.ArrayList;

/**
 * Created by ipc on 5/21/2016.
 */
public class SubFamily extends Class {
    // Parameterized constructor
    public SubFamily(String code, String label){
        this.code = code;
        this.label = label;
    }
    protected Activity activity;

    // Constructor
    public SubFamily(Activity context){
        this.activity = context;
    }

    /** Read the database subfamilies **/
    public ArrayList<Class> getUserSubFamilies(String idC, String idF){
        try{
            ArrayList<Class> mSubFamilies = new ArrayList<Class>();
            UserManager mydb= new UserManager(activity);
            String[] allColumns = { DBManager.SubFamily_Key,  DBManager.SubFamily_LABEL };

            mydb.open();
            Cursor cursor = mydb.getDb().query(DBManager.SubFamily_TABLE_NAME,allColumns, DBManager.SubFamily_KeyC +
                    " LIKE \""+ idC+"\" AND "+DBManager.SubFamily_KeyF +
                    " LIKE \""+ idF+"\"", null, null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                SubFamily subFamily = new SubFamily(cursor.getString(0),cursor.getString(1));
                mSubFamilies.add(subFamily);
                cursor.moveToNext();
            }
            // closing the cursor
            cursor.close();
            mydb.close();

            return mSubFamilies;
        } catch (Exception e) {
            return null;
        }
    }

    /** Fill the SubFamily table **/
    public void AddUserSubFamily(String id, String label, String family, String category) {
        try {
            UserManager mydb = new UserManager(activity);
            mydb.open();
            mydb.AddUserSubFamily(id, label, family, category);
            mydb.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
