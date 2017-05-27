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
 * Created by Phattarapong on 28-May-17.
 */

public class CustomListAdapterPolice extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Police>policeItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomListAdapterPolice(Activity activity, List<Police> policeItems) {
        this.activity = activity;
        this.policeItems = policeItems;
    }

    @Override
    public int getCount() {
        return policeItems.size();
    }

    @Override
    public Object getItem(int location) {
        return policeItems.get(location);
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
        Police p = policeItems.get(position);


        // name
        name.setText(p.getName());
        // type



        return convertView;
    }

}
