package com.geekh.mystock.Activity;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import com.geekh.mystock.Model.BO.Warehouse;
import com.geekh.mystock.Model.DAO.DBManager;
import com.geekh.mystock.Model.DAO.UserManager;
import com.geekh.mystock.R;


/**
 * Created by ipc on 4/30/2016.
 */
public class PrintActivity extends AppCompatActivity {
    TableLayout tablelayout;
    TextView mWarehouse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_print);
        tablelayout = (TableLayout) findViewById(R.id.tableLayout1);
        mWarehouse = (TextView) findViewById(R.id.textWarehouse);
        mWarehouse.setText(getString(R.string.warehouse)+ Warehouse.idW);
        BuildTable(Warehouse.idW);
    }

    private void BuildTable(String idW) {
        UserManager mydb= new UserManager(PrintActivity.this);

        String[] allColumns =  { DBManager.Item_Key, DBManager.Item_LABEL, DBManager.Item_KeyW,DBManager.Item_KeyC,
                DBManager.Item_KeyF,DBManager.Item_KeySF,DBManager.Item_UNIT,DBManager.Item_QTY };
        mydb.open();
        Cursor cursor = mydb.getDb().query(DBManager.Item_TABLE_NAME,allColumns, DBManager.Item_KeyW +
                " LIKE \'"+ idW+"\'", null, null, null, null);
        int rows = cursor.getCount();
        int cols = cursor.getColumnCount();

        cursor.moveToFirst();

        // outer for loop
        for (int i = 0; i < rows; i++) {

            TableRow row = new TableRow(this);
            row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));

            // inner for loop
            for (int j = 0; j < cols; j++) {
                 if (j!=2) {
                     TextView tv = new TextView(this);
                     tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                             LayoutParams.WRAP_CONTENT));

                     if (i % 2 != 0) {
                         tv.setBackgroundResource(R.drawable.row_pair_color);
                     } else {
                         tv.setBackgroundResource(R.drawable.row_impair_color);
                     }
                     tv.setGravity(Gravity.CENTER);
                     tv.setTextSize(18);
                     tv.setPadding(0, 5, 0, 5);

                     tv.setText(cursor.getString(j));

                     row.addView(tv);
                 }
            }

            cursor.moveToNext();
            tablelayout.addView(row);

        }
        cursor.close();
        mydb.close();
    }
}
