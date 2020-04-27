package com.geekh.mystock.Model.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager extends SQLiteOpenHelper {
    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    // The Warehouse table info
    public static final String Warehouse_TABLE_NAME = "Warehouse";
    public static final String Warehouse_Key = "WarehouseCode";
    public static final String Warehouse_LABEL = "WarehouseLabel";

    // Category table info
    public static final String Category_TABLE_NAME = "Category";
    public static final String Category_Key = "CategoryCode";
    public static final String Category_LABEL = "CategoryLabel";

    // The Family table info
    public static final String Family_TABLE_NAME = "Family";
    public static final String Family_Key = "FamilyCode";
    public static final String Family_LABEL = "FamilyLabel";
    public static final String Family_KeyC = "CategoryCode";

    // The SubFamily table info
    public static final String SubFamily_TABLE_NAME = "SubFamily";
    public static final String SubFamily_Key = "SubFamilyCode";
    public static final String SubFamily_LABEL = "SubFamilyLabel";
    public static final String SubFamily_KeyF = "FamilyCode";
    public static final String SubFamily_KeyC = "CategoryCode";

    // The Item table info
    public static final String Item_TABLE_NAME = "Item";
    public static final String Item_Key = "ItemCode";
    public static final String Item_LABEL = "ItemLabel";
    public static final String Item_KeyW = "WarehouseCode";
    public static final String Item_KeyC = "CategoryCode";
    public static final String Item_KeyF = "FamilyCode";
    public static final String Item_KeySF = "SubFamilyCode";
    public static final String Item_UNIT = "Unit";
    public static final String Item_QTY = "Quantity";

    // The EditWarehouse table info
    public static final String EWarehouses_TABLE_NAME = "EditWarehouse";
    public static final String EWarehouses_Key = "WarehouseCode";

    // The BarCode Item table info
    public static final String BCode_TABLE_NAME = "BarCode";
    public static final String BCode_Key = "BarCodeCode";
    public static final String BCode_KeyI = "ItemCode";
    public static final String BCode_KeyW = "WarehouseCode";

    /** Creation of the Warehouse table **/
    public static final String Warehouse_TABLE_CREATE =
            "CREATE TABLE " + Warehouse_TABLE_NAME +
                    " (" + Warehouse_Key + " TEXT PRIMARY KEY, " +
                    Warehouse_LABEL + " TEXT);";
    /** Reset the Warehouse table **/
    public static final String Warehouse_TABLE_DROP = "DROP TABLE IF EXISTS " + Warehouse_TABLE_NAME + ";";

    /** Creation of the Category table **/
    public static final String Category_TABLE_CREATE =
            "CREATE TABLE " + Category_TABLE_NAME + " (" +
                    Category_Key + " TEXT PRIMARY KEY, " +
                    Category_LABEL + " TEXT);";
    /** Reset the Category table **/
    public static final String Category_TABLE_DROP = "DROP TABLE IF EXISTS " + Category_TABLE_NAME + ";";

    /** Creation of the Item table **/
    public static final String Item_TABLE_CREATE =
            "CREATE TABLE " + Item_TABLE_NAME + " (" +
                    Item_Key + " TEXT , " +
                    Item_LABEL + " TEXT, " +
                    Item_KeyW + " TEXT, " +
                    Item_KeyC + " TEXT, " +
                    Item_KeyF + " TEXT, " +
                    Item_KeySF + " TEXT, " +
                    Item_UNIT + " TEXT, " +
                    Item_QTY + " TEXT," +
                    "PRIMARY KEY("+Item_Key+","+Item_KeyW+"));";
    /** RESET table Item **/
    public static final String Item_TABLE_DROP = "DROP TABLE IF EXISTS " + Item_TABLE_NAME + ";";

    /** Creation of the Family table **/
    public static final String Family_TABLE_CREATE =
            "CREATE TABLE " + Family_TABLE_NAME + " (" +
                    Family_Key + " TEXT , " +
                    Family_LABEL + " TEXT," +
                    Family_KeyC + " TEXT, " +
                    "PRIMARY KEY("+Family_Key+","+Family_KeyC+"));";
    /** Reset the Family table **/
    public static final String Family_TABLE_DROP = "DROP TABLE IF EXISTS " + Family_TABLE_NAME + ";";

    /** Creation of the SubFamily table **/
    public static final String SubFamily_TABLE_CREATE =
            "CREATE TABLE " + SubFamily_TABLE_NAME + " (" +
                    SubFamily_Key + " TEXT  , " +
                    SubFamily_LABEL + " TEXT," +
                    SubFamily_KeyF + " TEXT," +
                    SubFamily_KeyC + " TEXT," +
                    "PRIMARY KEY("+SubFamily_Key+","+SubFamily_KeyC+","+SubFamily_KeyF+"));";
    /** Reset the SubFamily table **/
    public static final String SubFamily_TABLE_DROP = "DROP TABLE IF EXISTS " + SubFamily_TABLE_NAME + ";";

    /** Creation of the Item Bar table **/
    public static final String BCode_TABLE_CREATE =
            "CREATE TABLE " + BCode_TABLE_NAME + " (" +
                    BCode_Key + " TEXT PRIMARY KEY, " +
                    BCode_KeyI + " TEXT," +
                    BCode_KeyW + " TEXT );";
    /** RESET table BArticle **/
    public static final String BCode_TABLE_DROP = "DROP TABLE IF EXISTS " + BCode_TABLE_NAME + ";";

    /** Creation of the editWarehouses table **/
    public static final String EWarehouses_TABLE_CREATE =
            "CREATE TABLE " + EWarehouses_TABLE_NAME +
                    " (" + EWarehouses_Key + " TEXT PRIMARY KEY);";
    /** RESET the editWarehouses table **/
    public static final String EWarehouses_TABLE_DROP = "DROP TABLE IF EXISTS " + EWarehouses_TABLE_NAME + ";";

    // Create tables in the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Warehouse_TABLE_CREATE);
        db.execSQL(Category_TABLE_CREATE);
        db.execSQL(Item_TABLE_CREATE);
        db.execSQL(Family_TABLE_CREATE);
        db.execSQL(SubFamily_TABLE_CREATE);
        db.execSQL(EWarehouses_TABLE_CREATE);
        db.execSQL(BCode_TABLE_CREATE);
    }

    // Updating tables
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Warehouse_TABLE_DROP);
        db.execSQL(Category_TABLE_DROP);
        db.execSQL(Family_TABLE_DROP);
        db.execSQL(SubFamily_TABLE_DROP);
        db.execSQL(Item_TABLE_DROP);
        db.execSQL(EWarehouses_TABLE_DROP);
        db.execSQL(BCode_TABLE_DROP);
        onCreate(db);
    }
}