package com.geekh.mystock.Model.BO;

import android.content.Context;
import android.content.SharedPreferences;

/** This class is used to save user data **/
public class SaveSettings {

    Context context;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyData" ;
    // Constructor
    public  SaveSettings(Context context) {
        this.context=context;
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }
    // Save data: establishment number, password or ip
    public void saveValue(String key, String value) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
    }
    // Recover data
    public String loadValue(String key) {
        SharedPreferences settings;
        String value;
        settings = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        value = settings.getString(key, null);
        return value;
    }
    // Delete data
    public void removeValue(String key) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.remove(key);
        editor.commit();
    }
    // Verification of the data existence
    public boolean LoadData( ) {
        try {
            String TempUserID = loadValue("UserID");
            String TempEstabID = loadValue("EstabID");
            // If the user exists: we recover his data
            if (!TempUserID.equals("") && !TempUserID.equals("")) {
                User.setPassword(TempUserID);// load user pass
                User.setUserName(loadValue("UserName"));
                User.setEstabNumber(TempEstabID);// load estab number
                User.setEstabName(loadValue("EstabName"));
                User.setIp(loadValue("MyIP"));
                return true;
            }
            // Otherwise
            else {
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
