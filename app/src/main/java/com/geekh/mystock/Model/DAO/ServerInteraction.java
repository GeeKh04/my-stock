package com.geekh.mystock.Model.DAO;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;

import com.geekh.mystock.Model.BO.Item;
import com.geekh.mystock.Model.BO.Category;
import com.geekh.mystock.Model.BO.Warehouse;
import com.geekh.mystock.Model.BO.Family;
import com.geekh.mystock.Model.BO.SubFamily;
import com.geekh.mystock.Model.BO.User;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class ServerInteraction {

    private static String NameSpace ;
    private static HttpTransportSE androidHttpTransport;
    private static String URL;
    // Constructor
    public ServerInteraction(){
        URL = "http://"+ User.getIp()+"/mystock";
        NameSpace = "http://tempuri.org/";
    }

    /** Check the connection with the server **/
    public static boolean isConnectToServer() {
        final String METHOD_NAME = "PingRQ";
        final String SOAP_ACTION = "http://tempuri.org/PingRQ";
        // The request
        SoapObject request = new SoapObject(NameSpace, METHOD_NAME);
        // The parameters made to the request
        request.addProperty("state", "connected");

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        androidHttpTransport = new HttpTransportSE(URL);

        try {
            // Send request
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.bodyIn;
            return response.toString().equals("true");
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /** The authentication method to the server **/
    public static int Authenticate(String estabNumber,String password) {
        final String METHOD_NAME = "Authenticate";
        final String SOAP_ACTION = "http://tempuri.org/Authenticate";

        // The parameters made to the request
        SoapObject request = new SoapObject(NameSpace, METHOD_NAME);

        request.addProperty("establishmentNumber", estabNumber);
        request.addProperty("password", password);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        androidHttpTransport = new HttpTransportSE(URL);

        try {
            // Send request
            androidHttpTransport.call(SOAP_ACTION, envelope);

            // Obtaining the result
            SoapObject response = (SoapObject)envelope.bodyIn;
            // If authentication is successful
            if(response.getProperty("AuthenticateResult").toString().equals("true")){
                // Recover user information
                User.setUserName(response.getProperty("AuthenticateUser").toString());
                User.setEstabName(response.getProperty("AuthenticateEstab").toString());
                return 1;
            }
            // otherwise
            else{
                return 0;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /** The method of loading warehouses from the server to the local database **/
    public static boolean GetAllWarehouses(Activity activity) {
        final String METHOD_NAME = "GetAllWarehouses";
        final String SOAP_ACTION = "http://tempuri.org/GetAllWarehouses";

        // Request
        SoapObject request = new SoapObject(NameSpace, METHOD_NAME);

        // The parameters made to the request
        request.addProperty("establishmentNumber", User.getEstabNumber());
        request.addProperty("password", User.getPassword());

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        androidHttpTransport = new HttpTransportSE(URL);
        try {
            // Send request
            androidHttpTransport.call(SOAP_ACTION, envelope);
            // Obtaining the result
            SoapObject response = (SoapObject)envelope.bodyIn;
            int totalWarehouses = response.getPropertyCount();

            // Saving warehouses in the database
            for (int i = 0; i < totalWarehouses; i++) {
                SoapObject response1 = (SoapObject) response.getProperty(i);
                Warehouse DB = new Warehouse(activity);
                DB.AddUserDepot(response1.getProperty("warehouseCode").toString(),
                        response1.getProperty("warehouseLabel").toString());
            }
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /** The method of loading items from the server to the local database **/
    public static boolean GetItemsByWarehouse(Activity activity) {
        final String METHOD_NAME = "GetItemsByWarehouse";
        final String SOAP_ACTION = "http://tempuri.org/GetItemsByWarehouse";

        // Request
        SoapObject request = new SoapObject(NameSpace, METHOD_NAME);

        // The parameters made to the request
        request.addProperty("establishmentNumber", User.getEstabNumber());
        request.addProperty("password", User.getPassword());
        request.addProperty("warehouse", Warehouse.idW);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        androidHttpTransport = new HttpTransportSE(URL);

        try {
            // Send request
            androidHttpTransport.call(SOAP_ACTION, envelope);
            // Obtaining the result
            SoapObject response = (SoapObject)envelope.bodyIn;
            int totalItems = response.getPropertyCount();

            // Saving Items in the database
            for (int i = 0; i < totalItems; i++) {
                SoapObject response1 = (SoapObject) response.getProperty(i);
                Item DB = new Item(activity);

                String line = response1.getProperty("quantity").toString();
                String qteWithoutSpaces = line.replaceAll("\\s+","");
                DB.AddUserArticle(response1.getProperty("code").toString(),
                        response1.getProperty("label").toString(),
                        Warehouse.idW,
                        response1.getProperty("category").toString(),
                        response1.getProperty("family").toString(),
                        response1.getProperty("subfamily").toString(),
                        response1.getProperty("unit").toString(),
                        qteWithoutSpaces);
            }
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /** The method of loading Categories, Families and Subfamilies from the server **/
    public static boolean GetAllCategories(Activity activity) {
        final String METHOD_NAME = "GetAllCategories";
        final String SOAP_ACTION = "http://tempuri.org/GetAllCategories";

        // Request
        SoapObject request = new SoapObject(NameSpace, METHOD_NAME);

        request.addProperty("establishmentNumber", User.getEstabNumber());
        request.addProperty("password", User.getPassword());

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        androidHttpTransport = new HttpTransportSE(URL);
        try {
            //  Send request
            androidHttpTransport.call(SOAP_ACTION, envelope);
            //  Obtaining the result
            SoapObject response = (SoapObject)envelope.bodyIn;
            int totalCategs = response.getPropertyCount();
            Category DB = new Category(activity);
            for (int i = 0; i < totalCategs; i++) {

                SoapObject response1 = (SoapObject) response.getProperty(i);
                // Saving Categories in the Database
                DB.AddUserCategory(response1.getProperty("categoryCode").toString(),
                        response1.getProperty("categoryLabel").toString());
                int totalFams = response1.getPropertyCount();

                for (int j = 2; j < totalFams; j++){
                    SoapObject response2 = (SoapObject) response1.getProperty(j);
                    // Saving Families in the database
                    Family D = new Family(activity);
                    D.AddUserFamily(response2.getProperty("familyCode").toString(),
                            response2.getProperty("familyLabel").toString(),
                            response1.getProperty("categoryCode").toString()
                            );
                    int totalSFams = response2.getPropertyCount();

                    for (int k = 2; k < totalSFams; k++) {
                        SoapObject response3 = (SoapObject) response2.getProperty(k);
                        // Saving of Subfamilies in the database
                        SubFamily Dt = new SubFamily(activity);
                        Dt.AddUserSubFamily(response3.getProperty("subfamilyCode").toString(),
                                response3.getProperty("subfamilyLabel").toString(),
                                response2.getProperty("familyCode").toString(),
                                response1.getProperty("categoryCode").toString()
                        );
                    }
                }
            }

            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Send modification of the Item quantity **/
    public static String SendInventory(Context context, String depot) {
        final String METHOD_NAME = "SendInventory";
        final String SOAP_ACTION = "http://tempuri.org/SendInventory";

        UserManager mydb= new UserManager(context);
        String[] allColumns =  { DBManager.Item_Key, DBManager.Item_LABEL, DBManager.Item_KeyW,DBManager.Item_KeyC,
                DBManager.Item_KeyF,DBManager.Item_KeySF,DBManager.Item_UNIT,DBManager.Item_QTY };

        SoapObject request = new SoapObject(NameSpace, METHOD_NAME);
        request.addProperty("establishmentNumber", User.getEstabNumber());
        request.addProperty("warehouse",depot);

        SoapObject request1 = new SoapObject(NameSpace, METHOD_NAME);
        /**/
        mydb.open();
        Cursor cursor = mydb.getDb().query(DBManager.Item_TABLE_NAME,
                allColumns, DBManager.Item_KeyW +
                        " LIKE \""+ depot+"\"", null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            SoapObject request2 = new SoapObject(NameSpace, METHOD_NAME);
            request2.addProperty("code", cursor.getString(0));
            request2.addProperty("quantity", cursor.getString(7));
            request1.addProperty("item", request2);
            cursor.moveToNext();
        }
        // closing the cursor
        cursor.close();
        mydb.close();

        request.addProperty("items",request1);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.implicitTypes = true;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        androidHttpTransport = new HttpTransportSE(URL);

        try {
            // Send request
            androidHttpTransport.call(SOAP_ACTION, envelope);

            // Obtaining the result
            SoapObject response = (SoapObject)envelope.bodyIn;
            if(response.getProperty("Response").toString().equals("true")){
                if(!response.getProperty("Message").toString().equals("OK")){
                    return response.getProperty("Message").toString();
                }
                return "1";
            }
            else{
                return "0";
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return "-1";
        }
    }

    /** Send the items scanner with its quantity **/
    public static boolean SendBCInventory(String idW, String idBC, String idQ) {
        final String METHOD_NAME = "SendBCInventory";
        final String SOAP_ACTION = "http://tempuri.org/SendBCInventory";

        SoapObject request = new SoapObject(NameSpace, METHOD_NAME);
        request.addProperty("establishmentNumber", User.getEstabNumber());
        request.addProperty("warehouse", idW);
        request.addProperty("barcode", idBC);
        request.addProperty("quantity", idQ);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setAddAdornments(false);
        envelope.implicitTypes = true;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        androidHttpTransport = new HttpTransportSE(URL);

        try {
            // Send request
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject response = (SoapObject)envelope.bodyIn;

            return response.toString().equals("true");
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
