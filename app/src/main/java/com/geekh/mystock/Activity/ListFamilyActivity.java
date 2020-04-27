package com.geekh.mystock.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.geekh.mystock.Model.BO.Family;
import com.geekh.mystock.Model.BO.Warehouse;
import com.geekh.mystock.Model.BO.Class;
import com.geekh.mystock.View.ClassesAdapter;
import com.geekh.mystock.R;
import com.geekh.mystock.Model.BO.User;
import java.util.ArrayList;
/**
 * The Families page: displays a list of families (Code, Libelle)
 * */
public class ListFamilyActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {
    private TextView mUserView; // username
    private TextView mEstabView; // establishment name
    private ArrayList<Class> mFamilies = null; // items type list
    private ClassesAdapter mAdapter;
    SearchView mSearchView;
    ListView mListView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Display in the top bar "Families" with the deposit code
        if(!Warehouse.idW.equals(""))
        {
            setTitle(getString(R.string.title_activity_family)+"["+ Warehouse.idW+"]");
        }
        /** Display the graphical interface **/
        setContentView(R.layout.activity_listclass);
		// It's the menu bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_cat);
        setSupportActionBar(toolbar);

        /******* home page navigation menu ********/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_cat);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_home_view_cat);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        /** Display user name and establishment name **/
        mUserView = (TextView)header.findViewById(R.id.textUser);
        mEstabView = (TextView)header.findViewById(R.id.textEstab);
        mUserView.setText(User.getUserName());
        mEstabView.setText(User.getEstabName());

        /** Display the List of codes and labels **/
        new loadFamilles().execute();

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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_cat);
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

        if (id == R.id.nav_home_view_cat) {
        } else if (id == R.id.nav_consult) {
            Warehouse.p=0;
            Intent intent = new Intent(ListFamilyActivity.this, ListWarehouseActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_search) {
            Intent intent = new Intent(ListFamilyActivity.this, SearchActivity.class);
            startActivity(intent);
            finish();
        }  else if (id == R.id.nav_print) {
            Warehouse.p=2;
            Intent intent = new Intent(ListFamilyActivity.this, ListWarehouseActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_drop) {
            new Dialogs(ListFamilyActivity.this).dropDialog();
        } else if (id == R.id.nav_close) {
            new Dialogs(ListFamilyActivity.this).closeDialog();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_cat);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onResume() {
        super.onResume();
        LoginActivity.mActivity = this;
    }
    /** Load Families **/
    public class loadFamilles extends AsyncTask<Void, Void, Void> {
        Family family = new Family(ListFamilyActivity.this);
        final ProgressDialog dialog = ProgressDialog.show(ListFamilyActivity.this, "",getString(R.string.updating_data) , true);

        @Override
        protected void onPreExecute() {
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
			// load families from the local base in the form of an arraylist
            mFamilies = family.getUserFamilies(Class.idC);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            dialog.dismiss();
			// Display the list of Families
            mAdapter = new ClassesAdapter(ListFamilyActivity.this, R.layout.listview_listclass, mFamilies);
            mListView = (ListView) findViewById(R.id.list_view_cat);
            mListView.setAdapter(mAdapter);
            // Display a message, the list is empty
            TextView mTextView = (TextView) findViewById(R.id.empty_cat);
            mTextView.setText(R.string.no_items);
            mListView.setEmptyView(mTextView);
            /** Return the selected element code **/
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String code =  mAdapter.getItemCode(position);
                    Class.idF=code;
					// Redirection to page SubFamilies
                    Intent intent = new Intent(ListFamilyActivity.this, ListSubFamilyActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
}
