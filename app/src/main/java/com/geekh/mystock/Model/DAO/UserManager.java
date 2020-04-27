package com.geekh.mystock.Model.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.geekh.mystock.Model.BO.Category;

public class UserManager {
    // Declaration of the object of the DBManager class
    DBManager mDb;
    // Declaration of a SQLite data management variable
    SQLiteDatabase db;

    // The constructor of the UserManager class
    public UserManager(Context context) {
        this.mDb = new DBManager(context, "MyBD", null, 1);
    }

    // Open the Database in Read and Write Mode
    public SQLiteDatabase open() {
        // No need to close the last database since getWritableDatabase does it
        db = mDb.getWritableDatabase();
        return db;
    }

    // Close the Database
    public void close() {
        db.close();
    }

    // It returns the Database
    public SQLiteDatabase getDb() {
        return db;
    }

    // It fills the Items of a Warehouse
    public boolean AddUserWarehouse(String id, String label) {
        ContentValues value = new ContentValues();
        value.put(DBManager.Warehouse_Key, id);
        value.put(DBManager.Warehouse_LABEL, label);
        db.insert(DBManager.Warehouse_TABLE_NAME, null, value);
        return true;
    }

    // It fills the Items of a Warehouse
    public int AddUserItem(String id, String label, String warehouse, String category, String family, String subFamily, String unit, String qte) {
        try {
            ContentValues value = new ContentValues();
            value.put(DBManager.Item_Key, id);
            value.put(DBManager.Item_LABEL, label);
            value.put(DBManager.Item_KeyW, warehouse);
            value.put(DBManager.Item_KeyC, category);
            value.put(DBManager.Item_KeyF, family);
            value.put(DBManager.Item_KeySF, subFamily);
            value.put(DBManager.Item_UNIT, unit);
            value.put(DBManager.Item_QTY, qte);
            int res =(int) db.insert(DBManager.Item_TABLE_NAME, null, value);
            if(res==-1)
                return -1;
            else
                return 1;
        } catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // It fills the Category table
    public boolean AddUserCategory(String id, String label) {
        ContentValues value = new ContentValues();
        value.put(DBManager.Category_Key, id);
        value.put(DBManager.Category_LABEL, label);
        db.insert(DBManager.Category_TABLE_NAME, null, value);
        return true;
    }

    // It fills the Family table
    public boolean AddUserFamily(String id, String label, String category)
    {
        ContentValues value = new ContentValues();
        value.put(DBManager.Family_Key, id);
        value.put(DBManager.Family_LABEL, label);
        value.put(DBManager.Family_KeyC, category);
        db.insert(DBManager.Family_TABLE_NAME, null, value);
        return true;
    }

    // It fills the SubFamily table
    public boolean AddUserSubFamily(String id, String label, String family, String category)
    {
        ContentValues value = new ContentValues();
        value.put(DBManager.SubFamily_Key, id);
        value.put(DBManager.SubFamily_LABEL, label);
        value.put(DBManager.SubFamily_KeyF, family);
        value.put(DBManager.SubFamily_KeyC, category);
        db.insert(DBManager.SubFamily_TABLE_NAME, null, value);
        return true;
    }

    // Allows to empty all the tables in the database
    public boolean drop() {
        try {
            db.execSQL("delete from "+ DBManager.Warehouse_TABLE_NAME);
            db.execSQL("delete from "+ DBManager.Category_TABLE_NAME);
            db.execSQL("delete from "+ DBManager.Family_TABLE_NAME);
            db.execSQL("delete from "+ DBManager.SubFamily_TABLE_NAME);
            db.execSQL("delete from "+ DBManager.Item_TABLE_NAME);
            db.execSQL("delete from "+ DBManager.EWarehouses_TABLE_NAME);
            db.execSQL("delete from "+ DBManager.BCode_TABLE_NAME);
            return true;
        }catch(Exception e) {
            return false;
        }
    }

    // It empties the Warehouse table
    public boolean dropWarehouse() {
        try {
            db.execSQL("delete from "+ DBManager.Warehouse_TABLE_NAME);
            return true;
        }catch(Exception e) {
            return false;
        }
    }

    // It empties the Class from a warehouse
    public boolean dropItem(String idW) {
        try {
            db.delete(DBManager.Item_TABLE_NAME, DBManager.Item_KeyW+" =?", new String[] {idW});
            return true;
        }catch(Exception e) {
            return false;
        }
    }

    // It clears the Category table
    public boolean dropCategory() {
        try {
            db.execSQL("delete from "+ DBManager.Category_TABLE_NAME);
            db.execSQL("delete from "+ DBManager.Family_TABLE_NAME);
            db.execSQL("delete from "+ DBManager.SubFamily_TABLE_NAME);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    // It empties the EditWarehouses table
    public boolean dropEditWarehouse() {
        try {
            db.execSQL("delete from "+ DBManager.EWarehouses_TABLE_NAME);
            return true;
        }catch(Exception e) {
            return false;
        }
    }

    // It clears the Item Bar table
    public boolean dropItemBC() {
        try {
            db.execSQL("delete from "+ DBManager.BCode_TABLE_NAME);
            return true;
        }catch(Exception e) {
            return false;
        }
    }
}
