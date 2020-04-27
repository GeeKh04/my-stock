package com.geekh.mystock.View;

import android.app.Activity;
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
import com.geekh.mystock.Model.BO.Class;
import com.geekh.mystock.R;

import java.util.ArrayList;

public class ClassesAdapter extends ArrayAdapter<Class> implements Filterable {
    private Activity activity;
    private ArrayList<Class> items;
    private ArrayList<Class> orig;

    public ClassesAdapter(Activity context, int textViewResourceId, ArrayList<Class> items) {
        super(context, textViewResourceId, items);
        this.activity = context;
        this.items = items;
    }
    /************ Activate the Search bar filter ***************/
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                String v;
                final ArrayList<Class> results = new ArrayList<Class>();
                if (orig == null)
                    orig = items;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final Class g : orig) {
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
                items = (ArrayList<Class>) results.values;

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
        return items.size();
    }

    //@Override
    public String getItemCode(int position) {
        return items.get(position).getCode();
    }
    public String getItemLabel(int position) {
        return items.get(position).getLabel();
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
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        holder.labelView.setText(getItemLabel(position));

        // Return the code of each element
        String codeView = getItemCode(position);

        // Color generator
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getRandomColor();

        TextDrawable drawable = TextDrawable.builder()
                .buildRect(codeView, color);

        holder.imageView.setImageDrawable(drawable);

        return convertView;
    }

    private class ViewHolder {
        private ImageView imageView;
        private TextView labelView;

        public ViewHolder(View v) {
            imageView = (ImageView) v.findViewById(R.id.image_view);
            labelView = (TextView) v.findViewById(R.id.text_view);
        }
    }
}

