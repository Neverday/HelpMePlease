package com.helpmeplease.helpmeplease.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.helpmeplease.helpmeplease.Activity.NewsActivity;
import com.helpmeplease.helpmeplease.Adapter.AppController;
import com.helpmeplease.helpmeplease.Adapter.CustomListAdapterFeed;
import com.helpmeplease.helpmeplease.Adapter.Feed;
import com.helpmeplease.helpmeplease.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.helpmeplease.helpmeplease.Adapter.AppController.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment {
    private ProgressDialog pDialog;
    private List<Feed> feedList = new ArrayList<Feed>();
    private ListView listView;
    private CustomListAdapterFeed adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    final String P_NAME = "Helpme";
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    public FeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        sp = getActivity().getSharedPreferences(P_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();

        listView = (ListView) view.findViewById(R.id.list);
        adapter = new CustomListAdapterFeed(FeedFragment.this.getActivity(), feedList);
        listView.setAdapter(adapter);
        pDialog = new ProgressDialog(FeedFragment.this.getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();
        initSampleData();

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.colorPrimary,
                R.color.colorPrimary,
                R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        feedList.clear();

                        initSampleData();

                        adapter.notifyDataSetChanged();

                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }


    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }

    }
    private void initSampleData() {
        // Creating volley request obj
        String url = "http://newwer.96.lt/API/getFeedNews.php";
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                Feed feed = new Feed();
                                feed.setfeedTitle(obj.getString("title"));
                                feed.setThumbnailUrl(obj.getString("picture"));
                                feed.setfeedSub(obj.getString("sub_content"));
                                feed.setfeedContent(obj.getString("content"));



                                // OnClick Item
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    public void onItemClick(AdapterView<?> myAdapter, View myView,
                                                            int position, long mylng) {

                                        String strTitle = feedList.get(position).getfeedTitled()
                                                .toString();
                                        String strSub = feedList.get(position).getfeedSub()
                                                .toString();
                                        String strContent = feedList.get(position).getfeedContent()
                                                .toString();
                                        String strPicture = feedList.get(position).getThumbnailUrl()
                                                .toString();

                                        editor.putString("strTitle", strTitle);
                                        editor.putString("strSub", strSub);
                                        editor.putString("strContent", strContent);
                                        editor.putString("strPicture", strPicture);
                                        editor.commit();

                                        Intent i = new Intent(FeedFragment.this.getActivity(), NewsActivity.class);
                                        startActivity(i);







                                    }
                                });
                                // adding room to movies array
                                feedList.add(feed);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }
}