package com.helpmeplease.helpmeplease.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.helpmeplease.helpmeplease.R;

import java.util.List;

/**
 * Created by Phattarapong on 25-May-17.
 */

public class CustomListAdapterFeed extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Feed> feedItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomListAdapterFeed(Activity activity, List<Feed> feedItems) {
        this.activity = activity;
        this.feedItems = feedItems;
    }

    @Override
    public int getCount() {
        return feedItems.size();
    }

    @Override
    public Object getItem(int location) {
        return feedItems.get(location);
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
            convertView = inflater.inflate(R.layout.list_feed, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);
        TextView feedTitle = (TextView) convertView.findViewById(R.id.feed_titel);
        TextView feedSub = (TextView) convertView.findViewById(R.id.feed_subcontent);

        // getting movie data for the row
        Feed f = feedItems.get(position);

        // thumbnail image
        thumbNail.setImageUrl(f.getThumbnailUrl(), imageLoader);

        // title
        feedTitle.setText(f.getfeedTitled());

        // rating
        feedSub.setText(f.getfeedSub());


        return convertView;
    }

}