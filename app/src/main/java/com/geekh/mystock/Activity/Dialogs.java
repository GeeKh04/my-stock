package com.geekh.mystock.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import android.widget.Toast;
import com.geekh.mystock.Model.BO.ItemBC;
import com.geekh.mystock.Model.BO.EditWarehouse;
import com.geekh.mystock.Model.BO.Warehouse;
import com.geekh.mystock.Model.BO.SaveSettings;
import com.geekh.mystock.Model.BO.User;
import com.geekh.mystock.Model.DAO.ServerInteraction;
import com.geekh.mystock.Model.DAO.UserManager;
import com.geekh.mystock.R;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains all the box messages that we need
 */
public class Dialogs {
    private SaveSettings mIP;
    private Activity activity;
    // Constructor
    public Dialogs(Activity context) {
        this.activity = context;
    }

    /** A message box to ensure the deletion of the database **/
    public void dropDialog(){
        AlertDialog.Builder mDropDialog = new AlertDialog.Builder(activity, R.style.AppCompatAlertDialogStyle);

        mDropDialog.setIcon(R.drawable.ic_warning_save);
        mDropDialog.setTitle(activity.getResources().getString(R.string.delete_local_base));
        mDropDialog.setMessage(activity.getResources().getString(R.string.delete_local_base_confirmation));

        mDropDialog.setPositiveButton(activity.getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                try{
                    dropUserDB();
                    Toast.makeText(activity, activity.getResources().getString(R.string.empty_base), Toast.LENGTH_LONG).show();
                }catch (Exception e) {
                    Toast.makeText(activity, activity.getResources().getString(R.string.base_not_found), Toast.LENGTH_LONG).show();
                }
            }
        });

        mDropDialog.setNegativeButton(activity.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        mDropDialog.show();
    }

    /** A message box to ensure the closure of the session **/
    public void closeDialog(){
        AlertDialog.Builder mCloseDialog = new AlertDialog.Builder(activity);

        mCloseDialog.setIcon(R.drawable.ic_profile_close);
        mCloseDialog.setTitle(activity.getResources().getString(R.string.logout));
        mCloseDialog.setMessage(activity.getResources().getString(R.string.logout_confirmation));

        mCloseDialog.setPositiveButton(activity.getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                // Deletion of user data
                SaveSettings mData = new SaveSettings(activity);
                mData.removeValue("UserID");
                mData.removeValue("EstabID");
                mData.removeValue("UserName");
                mData.removeValue("EstabName");
                // Clear the local database
                dropUserDB();
                // Redirection to the Login page
                Intent loginScreen=new Intent(activity,LoginActivity.class);
                loginScreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Toast.makeText(activity, activity.getResources().getString(R.string.logout), Toast.LENGTH_LONG).show();
                activity.startActivity(loginScreen);
                activity.finish();

            }
        });

        mCloseDialog.setNegativeButton(activity.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        mCloseDialog.show();
    }

    /** A message box to configure IP server address **/
    public void ipConfigDialog(){
        AlertDialog.Builder mIpConfigDialog = new AlertDialog.Builder(activity);

        mIpConfigDialog.setIcon(R.drawable.ic_marker);
        mIpConfigDialog.setTitle(activity.getResources().getString(R.string.ip_configuration));
        mIpConfigDialog.setMessage(activity.getResources().getString(R.string.your_ip_address));

        // Set an EditText view to get user input
        mIP = new SaveSettings(activity);
        final EditText input = new EditText(activity);
        input.setText(mIP.loadValue("MyIP"));
        mIpConfigDialog.setView(input);

        mIpConfigDialog.setPositiveButton(activity.getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                User.setIp(input.getText().toString());
                if (!User.getIp().equals("")) {
                    mIP.saveValue("MyIP",User.getIp());
                    createToast(activity.getResources().getString(R.string.saved_ip) + mIP.loadValue("MyIP"));
                } else {
                    User.setIp(mIP.loadValue("MyIP"));
                    createToast(activity.getResources().getString(R.string.no_change) + User.getIp());
                }
            }
        });

        mIpConfigDialog.setNegativeButton(activity.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        mIpConfigDialog.show();
    }

    /** A message box is displayed when the IP address is incorrect **/
    public void ipWarningDialog(){
        AlertDialog.Builder mIpConfigDialog = new AlertDialog.Builder(activity, R.style.AppCompatAlertDialogStyle);
        mIpConfigDialog.setIcon(R.drawable.ic_risk);
        mIpConfigDialog.setTitle(activity.getResources().getString(R.string.incorrect_ip));
        mIP = new SaveSettings(activity);
        String ip = mIP.loadValue("MyIP");
        String btn = activity.getResources().getString(R.string.change_ip_btn);
        if (ip==null){
            btn = activity.getResources().getString(R.string.set_ip_btn);
            mIpConfigDialog.setMessage(activity.getResources().getString(R.string.set_ip_confirmation));
        }
        else {
            mIpConfigDialog.setMessage(activity.getResources().getString(R.string.your_ip_address)+" : " + ip +
                    activity.getResources().getString(R.string.change_incorrect_ip_confirmation));
        }

        mIpConfigDialog.setPositiveButton(btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                ipConfigDialog();
            }
        });
        mIpConfigDialog.setNegativeButton(activity.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        mIpConfigDialog.show();
    }

    /** A message box to ensure that the modification is sent **/
    //sendDialog
    /** Creation of a popup displaying a message **/
    public void createToast(String text) {
        Toast.makeText(activity, text, Toast.LENGTH_LONG).show();
    }
    //http://developer.android.com/reference/android/app/ProgressDialog.html
    /** A message box to save the modification **/
    public void sendWarningDialog(){
        AlertDialog.Builder mSendDialog = new AlertDialog.Builder(activity, R.style.AppCompatAlertDialogStyle);

        mSendDialog.setIcon(R.drawable.ic_warning_save);
        mSendDialog.setTitle(activity.getResources().getString(R.string.send_modification));
        mSendDialog.setMessage(activity.getResources().getString(R.string.send_modification_later_confirmation));

        mSendDialog.setPositiveButton(activity.getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                try{
                    new EditWarehouse(activity).saveEditWarehouse(Warehouse.idW);
                }catch (Exception e) {
                    Toast.makeText(activity, activity.getResources().getString(R.string.base_not_found), Toast.LENGTH_LONG).show();
                }
            }
        });

        mSendDialog.setNegativeButton(activity.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        mSendDialog.show();
        mSendDialog.setCancelable(true);
    }

    /** A message box to ensure that the modification is sent **/
    public static void sendDialog(){
        AlertDialog.Builder mSendDialog = new AlertDialog.Builder(LoginActivity.mActivity, R.style.AppCompatAlertDialogStyle);

        mSendDialog.setIcon(R.drawable.ic_warning_save);
        mSendDialog.setTitle(LoginActivity.mActivity.getResources().getString(R.string.send_modification));
        mSendDialog.setMessage(LoginActivity.mActivity.getResources().getString(R.string.send_modification_confirmation));

        mSendDialog.setPositiveButton(LoginActivity.mActivity.getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                try{
                    sendItems3 serverRequest = new sendItems3();
                    serverRequest.execute();
                }catch (Exception e) {
                    new Dialogs(LoginActivity.mActivity).createToast(LoginActivity.mActivity.getResources().getString(R.string.server_not_found)+e.toString());
                }
            }
        });

        mSendDialog.setNegativeButton(LoginActivity.mActivity.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        mSendDialog.show();
    }
    public static class sendItems3 extends AsyncTask<Void, Void, Void> {
        String resS="0";
        String resS2="0";

        final ProgressDialog dialog = ProgressDialog.show(LoginActivity.mActivity, "",LoginActivity.mActivity.getResources().getString(R.string.sending_data_to_the_server) , true);
        EditWarehouse editWarehouse = new EditWarehouse(LoginActivity.mActivity);
        @Override
        protected void onPreExecute() {
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            List<String> depot = editWarehouse.readEditWarehouses();
            if(!depot.isEmpty()) {
                for(int i=0; i<depot.size();i++){
                    resS2 = ServerInteraction.SendInventory(LoginActivity.mActivity, depot.get(i));
                }
                editWarehouse.dropUserEDitWarehouse();
                resS=resS2;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            dialog.dismiss();
            /******************************/
            if (resS.equals("1")) {
                Toast.makeText(LoginActivity.mActivity, LoginActivity.mActivity.getResources().getString(R.string.sent_successfully), Toast.LENGTH_LONG).show();
                new sendItemBCs().execute();
            } else if(resS.equals("0")) {
                Toast.makeText(LoginActivity.mActivity, LoginActivity.mActivity.getResources().getString(R.string.sent_unsuccessfully), Toast.LENGTH_SHORT).show();
            } else if(resS.equals("-1")){
                Toast.makeText(LoginActivity.mActivity, LoginActivity.mActivity.getResources().getString(R.string.server_connexion_failed), Toast.LENGTH_SHORT).show();
            }
        }
    }
    /** Call to the server to send the bar codes **/
    public static class sendItemBCs extends AsyncTask<Void, Void, Void> {
        boolean resCheck;
        boolean resSend;
        ArrayList<ItemBC> listItemBC = new ArrayList<ItemBC>();
        ItemBC itemBC = new ItemBC(LoginActivity.mActivity);

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            /** Test the connection with the server **/
            resCheck = User.isConnectToServer();
            // If the connection with the server is established
            if(resCheck){
                listItemBC = itemBC.readItemBC();
                if(!listItemBC.isEmpty()) {
                    for(int i=0; i<listItemBC.size();i++){
                        // Send the barcode data to the server, and retrieve the response in resSend
                        resSend = ServerInteraction.SendBCInventory(listItemBC.get(i).getWarehouse(), listItemBC.get(i).getBarCode(), listItemBC.get(i).getCode());
                    }
                    // Clear the barcode table
                    itemBC.dropUserItemBC();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
        }
    }
    /** Clear the local database **/
    public void dropUserDB() {
        try {
            UserManager mydb= new UserManager(activity);
            mydb.open();
            mydb.drop();
            mydb.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}

