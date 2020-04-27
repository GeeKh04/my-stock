package com.geekh.mystock.View;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.geekh.mystock.Model.BO.Warehouse;
import com.geekh.mystock.Model.DAO.DBManager;
import com.geekh.mystock.Model.DAO.UserManager;
import com.geekh.mystock.R;

import java.util.ArrayList;

public class WarehousesAdapter extends ArrayAdapter<Warehouse> implements Filterable {
    private Activity activity;
    private ArrayList<Warehouse> dps;
    private ArrayList<Warehouse> orig;

    public WarehousesAdapter(Activity context, int textViewResourceId, ArrayList<Warehouse> dps) {
        super(context, textViewResourceId, dps);
        this.activity = context;
        this.dps = dps;
    }
    /** Activate the Search bar filter **/
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                String v;
                final ArrayList<Warehouse> results = new ArrayList<Warehouse>();
                if (orig == null)
                    orig = dps;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final Warehouse g : orig) {
                            v=constraint.toString();
                            if (g.getCode().toLowerCase().contains(v.toLowerCase())
                                    || g.getLabel().toLowerCase().contains(v.toLowerCase()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          Filter.FilterResults results) {
                dps = (ArrayList<Warehouse>) results.values;
                notifyDataSetChanged();
            }
        };
        /*public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }*/
    }
    /********************************************/
    //https://github.com/amulyakhare/TextDrawable#textdrawable
    @Override
    public int getCount() {
        return dps.size();
    }

    //@Override
    public int getSize() {
        return dps.size();
    }
    public String getWarehouseCode(int position) {
        return dps.get(position).getCode();
    }
    public String getWarehouseLabel(int position) {
        return dps.get(position).getLabel();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.listview_listclass, parent, false);
            // get all UI view
            holder = new ViewHolder(convertView, position);
            // set tag for holder
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }
        if(ExistInDB(activity, getWarehouseCode(position))) {
            holder.labelView.setTextColor(Color.GREEN);
        }
        else {
            holder.labelView.setTextColor(Color.RED);
        }
        holder.labelView.setText(getWarehouseLabel(position));

        // Return the code of each elementget
        String codeView = getWarehouseCode(position);

        // Color generator
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getRandomColor();

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(codeView, color);

        holder.imageView.setImageDrawable(drawable);

        return convertView;
    }

    private class ViewHolder {
        private ImageView imageView;
        private TextView labelView;

        public ViewHolder(View v, int position) {
            imageView = (ImageView) v.findViewById(R.id.image_view);
            labelView = (TextView) v.findViewById(R.id.text_view);
        }
    }
    private boolean ExistInDB(Context context, String id){
        try{
            UserManager mydb= new UserManager(context);
            String[] allColumns =  { DBManager.Item_Key, DBManager.Item_LABEL, DBManager.Item_KeyW,DBManager.Item_KeyC,
                    DBManager.Item_KeyF,DBManager.Item_KeySF,DBManager.Item_UNIT,DBManager.Item_QTY };

            mydb.open();
            Cursor cursor = mydb.getDb().query(DBManager.Item_TABLE_NAME,allColumns, DBManager.Item_KeyW +
                    " LIKE \""+ id+"\"", null, null, null, null);

            if(cursor.getCount()>0) {
                cursor.close();
                mydb.close();
                return true;
            }
            else  {
                cursor.close();
                mydb.close();
                return false;}
        } catch (Exception e) {
            Log.e("BACKGROUND_PROC", e.getMessage());
            return false;
        }
    }
}

