package com.geekh.mystock.Model.BO;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.geekh.mystock.Model.DAO.DBManager;
import com.geekh.mystock.Model.DAO.UserManager;

import java.util.ArrayList;

/**
 * The Item class
 */
public class Item {

    private String code;
    private String label;
    private String warehouse;
    private String category;
    private String family;
    private String subFamily;
    private String unit;
    private String quantity;
    public static String idI = "";
    public static int tmp = 0;

    // Parameterized constructor
    public Item(String code, String label, String warehouse, String category, String family, String subFamily, String unit, String quantity){
        this.code = code;
        this.label = label;
        this.warehouse = warehouse;
        this.category = category;
        this.family = family;
        this.subFamily = subFamily;
        this.unit = unit;
        this.quantity = quantity;
    }
    // Return quantity
    public String getQuantity() {
        return quantity;
    }
    // Assign the Quantity to the corresponding variable
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
    // Return unit
    public String getUnit() {
        return unit;
    }
    // Assign the Unit to the corresponding variable
    public void setUnit(String unit) {
        this.unit = unit;
    }
    // Return the code Under Family
    public String getSubFamily() {
        return subFamily;
    }
    // Assign the code Sub Family to the corresponding variable
    public void setSubFamily(String subFamily) {
        this.subFamily = subFamily;
    }
    // Return the Family code
    public String getFamily() {
        return family;
    }
    // Assign the Family code to the corresponding variable
    public void setFamily(String family) {
        this.family = family;
    }
    // Return the category code
    public String getCategory() {
        return category;
    }
    // Assign the Category code to the corresponding variable
    public void setCategory(String category) {
        this.category = category;
    }
    // Return the name of the Item
    public String getLabel() {
        return label;
    }
    // Assign the Item Name to the corresponding variable
    public void setLabel(String label) {
        this.label = label;
    }
    // Returns the item code
    public String getCode() {
        return code;
    }
    // Assign the Item code to the corresponding variable
    public void setCode(String code) {
        this.code = code;
    }
    // Return the Warehouse code
    public String getWarehouse() {
        return warehouse;
    }
    // Assign the Warehouse code to the corresponding variable
    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    protected Activity activity;

    // Constructor
    public Item(Activity context){
        this.activity = context;
    }

    /** Read the database items **/
    public ArrayList<Item> getUserItems(String idW){
        try{
            ArrayList<Item> mItems = new ArrayList<Item>();
            UserManager mydb= new UserManager(activity);
            String[] allColumns =  { DBManager.Item_Key, DBManager.Item_LABEL, DBManager.Item_KeyW,DBManager.Item_KeyC,
                    DBManager.Item_KeyF,DBManager.Item_KeySF,DBManager.Item_UNIT,DBManager.Item_QTY };

            mydb.open();
            Cursor cursor;
            cursor = mydb.getDb().query(DBManager.Item_TABLE_NAME,allColumns, DBManager.Item_KeyW +
                    " LIKE \'"+ idW+"\'", null, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Item art=new Item(cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7));
                mItems.add(art);
                cursor.moveToNext();
            }
            // closing the cursor
            cursor.close();
            mydb.close();

            return mItems;
        } catch (Exception e) {
            Log.e("BACKGROUND_PROC", e.getMessage());
            return  null;
        }
    }

    /** Read the subfamily items from the database **/
    public ArrayList<Item> getUserItems(String idW, String idC, String idF, String idSF){
        try{
            ArrayList<Item> mItems = new ArrayList<Item>();
            UserManager mydb= new UserManager(activity);
            String[] allColumns =  { DBManager.Item_Key, DBManager.Item_LABEL, DBManager.Item_KeyW,DBManager.Item_KeyC,
                    DBManager.Item_KeyF,DBManager.Item_KeySF,DBManager.Item_UNIT,DBManager.Item_QTY };

            mydb.open();
            Cursor cursor;
            cursor = mydb.getDb().query(DBManager.Item_TABLE_NAME,allColumns, DBManager.Item_KeyW +
                    " LIKE \'"+ idW+"\' AND "+DBManager.Item_KeyC +
                    " LIKE \'"+ idC+"\' AND "+DBManager.Item_KeyF +
                    " LIKE \'"+ idF+"\' AND "+DBManager.Item_KeySF +
                    " LIKE \'"+ idSF+"\'", null, null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Item art=new Item(cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7));
                mItems.add(art);
                cursor.moveToNext();
            }
            // la fermeture du curseur
            cursor.close();
            mydb.close();

            return mItems;
        } catch (Exception e) {
            Log.e("BACKGROUND_PROC", e.getMessage());
            return  null;
        }
    }
    /** Return a database item using its code and warehouse code **/
    public Item getUserItem(String idI, String idW){
        try{
            Item mItem = null;
            UserManager mydb= new UserManager(activity);
            String[] allColumns =  { DBManager.Item_Key, DBManager.Item_LABEL, DBManager.Item_KeyW,DBManager.Item_KeyC,
                    DBManager.Item_KeyF,DBManager.Item_KeySF,DBManager.Item_UNIT,DBManager.Item_QTY };

            mydb.open();

            Cursor cursor = mydb.getDb().query(DBManager.Item_TABLE_NAME,allColumns, DBManager.Item_Key +
                    " LIKE \'"+ idI+"\' AND "+DBManager.Item_KeyW +
                    " LIKE \'"+ idW+"\'", null, null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                mItem = new Item(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5),
                        cursor.getString(6), cursor.getString(7));

                cursor.moveToNext();
            }
            // closing the cursor
            cursor.close();
            mydb.close();

            return mItem;
        } catch (Exception e) {
            Log.e("BACKGROUND_PROC", e.getMessage());
            return null;
        }
    }

    /** Fill the Item table **/
    public int AddUserArticle(String id, String label, String warehouse, String category, String family, String subFamily, String unit, String quantity) {
        try {
            UserManager mydb = new UserManager(activity);
            mydb.open();
            int res = mydb.AddUserItem(id, label, warehouse, category, family, subFamily, unit, quantity);
            mydb.close();
            return res;
        } catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /** Change the quantity of an item in the database,
     ** takes as parameter the code, item warehouse code and the new quantity **/
    public void setItemEdit(String id, String idW,String quantity){
        try{
            UserManager mydb= new UserManager(activity);

            mydb.open();
            ContentValues value = new ContentValues();
            value.put(DBManager.Item_QTY, quantity);
            mydb.getDb().update(DBManager.Item_TABLE_NAME, value, DBManager.Item_Key  + " = ? AND "+DBManager.Item_KeyW  + " = ?", new String[] {id,idW});
            mydb.close();
        } catch (Exception e) {
            Log.e("BACKGROUND_PROC", e.getMessage());
        }
    }
    /** Empty the Table of Items **/
    public void dropUserItem(String idW) {
        try {
            UserManager mydb = new UserManager(activity);
            mydb.open();
            mydb.dropItem(idW);
            mydb.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
