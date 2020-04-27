package com.geekh.mystock.Model.BO;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.geekh.mystock.Model.DAO.ServerInteraction;

/**
 * The User class
 */
public class User {

    private static String ip;// IP address
    private static String estabNumber;// Establishment number
    private static String estabName;// Establishment name
    private static String userName;// Username
    private static String password;// Password

    public static boolean edit=false;// Modification indication variable

    // Parameterized constructor
    public User(String mEstabNumber,String mPassword, String mIp) {
        estabNumber = mEstabNumber ;
        password = mPassword ;
        ip = mIp;
    }
    // Return Establishment number
    public static String getEstabNumber() {
        return estabNumber;
    }

    // Change the value of the Establishment Number
    public static void setEstabNumber(String mEstabNumber) {
        estabNumber = mEstabNumber;
    }

    // Return the establishment name
    public static String getEstabName() {
        return estabName;
    }

    // Change establishment name
    public static void setEstabName(String mEstabName) {
        estabName = mEstabName;
    }

    // Return the password
    public static String getPassword() {
        return password;
    }

    // Change The value of the password
    public static void setPassword(String mPassword) {
        password = mPassword;
    }

    // Return the IP address
    public static String getIp() {
        return ip;
    }

    // Change the IP address
    public static void setIp(String mIp) {
        ip = mIp;
    }

    // Return the username
    public static String getUserName() {
        return userName;
    }

    // Change the username
    public static void setUserName(String mUserName) {
        userName = mUserName;
    }

    /** A function that checks if the Internet connection is available **/
    public final static boolean isConnectToInternet( Context context )
    {
        final ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );
        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    /** The call has the method of verifying the connection with the server **/
    public static boolean isConnectToServer() {
        return new ServerInteraction().isConnectToServer();
    }

    /** The call to the authentication method to the server **/
    public static int Authentify() {
        return new ServerInteraction().Authenticate(estabName,password);
    }
}
