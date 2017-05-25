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

public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Check> checkItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomListAdapter(Activity activity, List<Check> checkItems) {
        this.activity = activity;
        this.checkItems = checkItems;
    }

    @Override
    public int getCount() {
        return checkItems.size();
    }

    @Override
    public Object getItem(int location) {
        return checkItems.get(location);
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
            convertView = inflater.inflate(R.layout.list_row, null);



        TextView name = (TextView) convertView.findViewById(R.id.txt_name);
        TextView type = (TextView) convertView.findViewById(R.id.txt_type);
        TextView phone = (TextView) convertView.findViewById(R.id.txt_phone);
        // getting movie data for the row
        Check m = checkItems.get(position);


        // name
        name.setText("ชื่อผู้แจ้ง : " + m.getName());
        // type
        type.setText("ประเภท : " + m.getCategory());
        // phone
        phone.setText("เบอร์โทรศัพท์ : " + m.getPhone());


        return convertView;
    }

}