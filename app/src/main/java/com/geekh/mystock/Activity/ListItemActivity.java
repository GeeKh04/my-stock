package com.geekh.mystock.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.geekh.mystock.Model.BO.Item;
import com.geekh.mystock.Model.BO.ItemBC;
import com.geekh.mystock.Model.BO.Class;

import com.geekh.mystock.Model.BO.Warehouse;
import com.geekh.mystock.Model.DAO.ServerInteraction;
import com.geekh.mystock.R;
import com.geekh.mystock.Model.BO.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * Class Page :
 *   If the filter is activated: Display a list of all the items (Label, Quantity)
 *   Otherwise: Displaying a list of Class (Label, Quantity) in relation to its category, family and subfamily
 * */
public class ListItemActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {

    private TextView mUserView; // Username
    private TextView mEstabView; // Name of the establishment
    private ArrayList<Item> mItems = null; // Item type list
	SearchView mSearchView;
    ListView mListView;
    SimpleAdapter simpleAdapter;
    FloatingActionButton mSendButton, mAddButton, mRefreshButton;
    Dialogs mDialog = new Dialogs(ListItemActivity.this);
    static ListItemActivity itemsActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemsActivity = this;
        // Display in the top bar "Class" with the deposit code
        if(!Warehouse.idW.equals(""))
        {
            setTitle(getString(R.string.title_activity_items)+"["+ Warehouse.idW+"]");
        }
        /** Display the graphical interface **/
        setContentView(R.layout.activity_listitem);
		// The menu bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        /** home page navigation menu **/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_home_view2);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
		
        /** Display username and the establishment name **/
        mUserView = (TextView)header.findViewById(R.id.textUser);
        mEstabView = (TextView)header.findViewById(R.id.textEstab);
        mUserView.setText(User.getUserName());
        mEstabView.setText(User.getEstabName());

        /** Display the List of codes and labels **/
        new loadItems().execute();

        /** Button to Send the change to the server **/
        mSendButton = (FloatingActionButton) findViewById(R.id.fab_send);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSendButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_done_all_24dp));
                new sendItems().execute();
            }
        });
        if(!ListWarehouseActivity.mFilter){
            /** Button to Add an article **/
            mAddButton = (FloatingActionButton) findViewById(R.id.fab_add);
            mAddButton.setVisibility(View.VISIBLE);
            mAddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ListItemActivity.this, AddItemActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
        /** Refresh Button **/
        mRefreshButton = (FloatingActionButton) findViewById(R.id.fab_refresh);
        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListItemActivity.this, ListItemActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
    public static ListItemActivity getInstance(){
        return itemsActivity;
    }
    /** search filter methods **/
    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(true);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint(getString(R.string.searching));
    }

    public boolean onQueryTextChange(String newText) {
        Filter filter = simpleAdapter.getFilter();
        if (TextUtils.isEmpty(newText)) {
            filter.filter("");
        } else {
            filter.filter(newText);
        }
        return true;
    }

    public boolean onQueryTextSubmit(String query) {
        return false;
    }
    /***********************************************/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /** activate the search filter **/
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        mSearchView = (SearchView) menu.findItem(R.id.search_view).getActionView();
        setupSearchView();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

       /* if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_home_view2) {

        } else if (id == R.id.nav_consult) {
            Warehouse.p=0;
            Intent intent = new Intent(ListItemActivity.this, ListWarehouseActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_search) {
            Intent intent = new Intent(ListItemActivity.this, SearchActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_print) {
            Warehouse.p=2;
            Intent intent = new Intent(ListItemActivity.this, ListWarehouseActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_drop) {
            new Dialogs(ListItemActivity.this).dropDialog();
        } else if (id == R.id.nav_close) {
            new Dialogs(ListItemActivity.this).closeDialog();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onResume() {
        super.onResume();
        LoginActivity.mActivity = this;
    }
    /** The call to the server to load the items **/
    public class loadItems extends AsyncTask<Void, Void, Void> {
        boolean resCheck;
        Item item = new Item(ListItemActivity.this);
        final ProgressDialog dialog = ProgressDialog.show(ListItemActivity.this, "",getString(R.string.updating_data) , true);

        @Override
        protected void onPreExecute() {
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
			/** Test the connection with the server **/
            resCheck = User.isConnectToServer();
			// If the connection with the server is established
            if (resCheck && !User.edit){
				/** Update item table **/
				// Clear the Articles table
                item.dropUserItem(Warehouse.idW);
				// Fill the Articles table
                ServerInteraction.GetItemsByWarehouse(ListItemActivity.this);
            }
			// Recover articles from the local base in the form of an arraylist
			if(ListWarehouseActivity.mFilter) {
				mItems = item.getUserItems(Warehouse.idW);
            }
            else {
                mItems = item.getUserItems(Warehouse.idW , Class.idC, Class.idF, Class.idSF);
            }
            User.edit=false;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            dialog.dismiss();
            // Display the items list
            List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

            for (int i = 0; i < mItems.size(); i++) {

                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("listview_code", mItems.get(i).getCode());
                hm.put("listview_label", mItems.get(i).getLabel());
                hm.put("listview_quantity", mItems.get(i).getQuantity());
                aList.add(hm);
            }

            String[] from = {"listview_label", "listview_quantity"};
            int[] to = {R.id.listview_item_label, R.id.listview_item_qty};
            simpleAdapter = new SimpleAdapter(getBaseContext(), aList, R.layout.listview_listitem, from, to);
            mListView = (ListView) findViewById(R.id.list_items_view);
            mListView.setAdapter(simpleAdapter);
            // Display a message, the list is empty
            TextView mTextView = (TextView) findViewById(R.id.empty_item);
            mTextView.setText(R.string.no_items);
            mListView.setEmptyView(mTextView);
            /** Return the selected item code **/
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    HashMap<String, Object> obj = (HashMap<String, Object>) simpleAdapter.getItem(position);
                    // Return the code position in the list
                    String value = (String) obj.get("listview_code");
                    Item.idI = value;
					// Redirection to Item page
                    Intent intent = new Intent(ListItemActivity.this, ItemActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    /** The call to the server to Send the modification (online-mode) **/
    public class sendItems extends AsyncTask<Void, Void, Void> {
        String resS="";
        boolean resCheck;
        final ProgressDialog dialog = ProgressDialog.show(ListItemActivity.this, "",getString(R.string.sending_data_to_the_server) , true);

        @Override
        protected void onPreExecute() {
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
			/** Test the connection with the server **/
            resCheck = User.isConnectToServer();
			// If the connection with the server is established
            if (resCheck){
				// Send the article table
                resS= ServerInteraction.SendInventory(ListItemActivity.this, Warehouse.idW);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            dialog.dismiss();
            /******************************/
            if (resCheck) {
                mDialog.createToast(resS);
                new sendItemBCs().execute();
            } 
			// If the connection with the server is not established
			else {
                mDialog.sendWarningDialog();
            }
        }
    }
    /** The call to the server to send the barcodes **/
    public class sendItemBCs extends AsyncTask<Void, Void, Void> {
        boolean resCheck;
        boolean resSend;
        ArrayList<ItemBC> itemBCs = new ArrayList<ItemBC>();
        ItemBC itemBC = new ItemBC(ListItemActivity.this);

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            /** Test the connection with the server **/
            resCheck = User.isConnectToServer();
            // If the connection with the server is established
            if(resCheck){
                itemBCs = itemBC.readItemBC();
                if(!itemBCs.isEmpty()) {
                    for(int i=0; i<itemBCs.size();i++){
                        // Send the barcode data to the server, and retrieve the response in resSend
                        resSend = ServerInteraction.SendBCInventory(itemBCs.get(i).getWarehouse(), itemBCs.get(i).getBarCode(), itemBCs.get(i).getCode());
                    }
                    // Clear the barcode table
                    //cbArticle.RazUserCBArticle();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
        }
    }

}


