package com.geekh.mystock.Activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.geekh.mystock.Model.BO.EditWarehouse;
import com.geekh.mystock.Model.BO.Warehouse;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.geekh.mystock.View.WarehousesAdapter;
import com.geekh.mystock.R;
import com.geekh.mystock.Model.DAO.ServerInteraction;
import com.geekh.mystock.Model.BO.User;
import java.util.ArrayList;
import java.util.List;

/**
 * The Depots page: displays a list of deposits (Code, Libelle)
 * */
public class ListWarehouseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {
    private TextView mUserView; // username
    private TextView mEstabView; // establishment name
	public static Boolean mFilter = false; // The consultation filter
    private ArrayList<Warehouse> mWarehouses = null; // Warehouse type list
    private WarehousesAdapter mAdapter;
    SearchView mSearchView;
    ListView mListView;
    EditWarehouse editWarehouse = new EditWarehouse(ListWarehouseActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** Display the graphical interface **/
        setContentView(R.layout.activity_warehouse);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /** Detect change of connection state **/
        BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                /** Test the internet connection **/
                boolean isConnected = User.isConnectToInternet(ListWarehouseActivity.this);
                // If the connection is established
                if (isConnected) {
                    // Check if there is a modification to send to the server
                    List<String> depot = editWarehouse.readEditWarehouses();
                    if(!depot.isEmpty()) {
                        new Dialogs(ListWarehouseActivity.this).sendDialog();
                    }

                }// otherwise
                else {
                    // OfflineMode
                    Toast.makeText(ListWarehouseActivity.this,getString(R.string.offline_mode), Toast.LENGTH_SHORT).show();
                }

            }
        };
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkStateReceiver, filter);

        /** Test the internet connection **/
        boolean isConnected = User.isConnectToInternet(ListWarehouseActivity.this);
        // If the connection is established
        if (isConnected) {
            // Check if there is a modification to send to the server
            List<String> warehouse = editWarehouse.readEditWarehouses();
            if(!warehouse.isEmpty()) {
                new Dialogs(ListWarehouseActivity.this).sendDialog();
            }

        }
        /** home page navigation menu **/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_home_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        /** Display username and establishment name **/
        mUserView = (TextView)header.findViewById(R.id.textUser);
        mEstabView = (TextView)header.findViewById(R.id.textEstab);
        mUserView.setText(User.getUserName());
        mEstabView.setText(User.getEstabName());
        /** Button Filter consultation **/
        if (Warehouse.p == 0 ){

            final FloatingActionMenu fabButton = (FloatingActionMenu) findViewById(R.id.fab_filter);
            fabButton.setVisibility(View.VISIBLE);
            FloatingActionButton fab_yesButton = (FloatingActionButton) findViewById(R.id.fab_filter_yes);
            FloatingActionButton fab_noButton = (FloatingActionButton) findViewById(R.id.fab_filter_no);

            fab_yesButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mFilter=true;
                    fabButton.close(true);
                }
            });
            fab_noButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mFilter=false;
                    fabButton.close(true);
                }
            });
        }
        /** Display the List of codes and labels **/
        new loadDepots().execute();

    }

    /** Search filter methods **/
    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(true);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint(getString(R.string.searching));
    }

    public boolean onQueryTextChange(String newText) {
        Filter filter = mAdapter.getFilter();
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home_view) {
        } else if (id == R.id.nav_consult) {
            Warehouse.p=0;
            Intent intent = new Intent(ListWarehouseActivity.this, ListWarehouseActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_search) {
            Intent intent = new Intent(ListWarehouseActivity.this, SearchActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_print) {
            Warehouse.p=2;
            Intent intent = new Intent(ListWarehouseActivity.this, ListWarehouseActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_drop) {
            new Dialogs(ListWarehouseActivity.this).dropDialog();
        } else if (id == R.id.nav_close) {
            new Dialogs(ListWarehouseActivity.this).closeDialog();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onResume() {
        super.onResume();
        LoginActivity.mActivity = this;
    }
    /** The call to the server to load the deposits **/
    public class loadDepots extends AsyncTask<Void, Void, Void>     {
        boolean resCheck=false;
        Warehouse depot = new Warehouse(ListWarehouseActivity.this);
        final ProgressDialog dialog = ProgressDialog.show(ListWarehouseActivity.this, "",getString(R.string.updating_data) , true);
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
				/** Update the depot table **/
				// Empty the deposit table
                depot.dropUserWarehouse();
				// Fill the deposit table
                ServerInteraction.GetAllWarehouses(ListWarehouseActivity.this);
            }
			// Recover deposits from the local base in the form of an arraylist
            mWarehouses = depot.getUserWarehouses();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            dialog.dismiss();
			// Display the list of deposits
            mAdapter = new WarehousesAdapter(ListWarehouseActivity.this, R.layout.listview_listclass, mWarehouses);
            mListView = (ListView) findViewById(R.id.list_view);
            mListView.setAdapter(mAdapter);
            // Display a message, the list is empty
            TextView mTextView = (TextView) findViewById(R.id.empty);
            mTextView.setText(R.string.no_items);
            mListView.setEmptyView(mTextView);
            /** Return the selected element code **/
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String code =  mAdapter.getWarehouseCode(position);
                    Warehouse.idW = code;
                    if(Warehouse.p == 0) {
                        if(mFilter) {
							// Redirection to Class page
                            Intent intent = new Intent(ListWarehouseActivity.this, ListItemActivity.class);
                            startActivity(intent);
                        }
                        else {
							// Redirection to Categories page
                            Intent intent = new Intent(ListWarehouseActivity.this, ListCategoryActivity.class);
                            startActivity(intent);
                        }
                    }
                    else if (Warehouse.p == 1) {
						// Redirection to Search page
                        Intent intent = new Intent(ListWarehouseActivity.this, SearchActivity.class);
                        startActivity(intent);
                    } else if (Warehouse.p == 2) {
						// Redirection to Print page
                        Intent intent = new Intent(ListWarehouseActivity.this, PrintActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }
    }
}
