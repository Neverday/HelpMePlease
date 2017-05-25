package com.helpmeplease.helpmeplease.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.helpmeplease.helpmeplease.R;

import java.util.List;

/**
 * Created by Phattarapong on 25-May-17.
 */

public class CustomListAdapterHospital extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Hospital> hospitalItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomListAdapterHospital(Activity activity, List<Hospital> hospitalItems) {
        this.activity = activity;
        this.hospitalItems = hospitalItems;
    }

    @Override
    public int getCount() {
        return hospitalItems.size();
    }

    @Override
    public Object getItem(int location) {
        return hospitalItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_hospital, null);



        TextView name = (TextView) convertView.findViewById(R.id.txt_name);

        // getting movie data for the row
        Hospital h = hospitalItems.get(position);


        // name
        name.setText(h.getName());
        // type



        return convertView;
    }

}
