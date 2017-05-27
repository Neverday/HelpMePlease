package com.helpmeplease.helpmeplease.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.helpmeplease.helpmeplease.Adapter.AppController;
import com.helpmeplease.helpmeplease.Adapter.CustomListAdapterPolice;
import com.helpmeplease.helpmeplease.Adapter.Police;
import com.helpmeplease.helpmeplease.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.helpmeplease.helpmeplease.Adapter.AppController.TAG;

public class PoliceMenuActivity extends AppCompatActivity {
    private ProgressDialog pDialog;
    private List<Police> policeList = new ArrayList<Police>();
    private ListView listView;
    private CustomListAdapterPolice adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    final String P_NAME = "Helpme";
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police_menu);
        sp = getSharedPreferences(P_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListAdapterPolice(this, policeList);
        listView.setAdapter(adapter);
        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();
        initSampleData();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.colorPrimary,
                R.color.colorPrimary,
                R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {

                        policeList.clear();

                        initSampleData();

                        adapter.notifyDataSetChanged();

                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            // finish the activity
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void initSampleData() {
        // Creating volley request obj
        String url = "http://newwer.96.lt/API/getPolice.php";
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
                                final Police police = new Police();
                                police.setId(obj.getString("id"));
                                police.setName(obj.getString("name"));
                                police.setLocation(obj.getString("location"));


                                // OnClick Item
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    public void onItemClick(AdapterView<?> myAdapter, View myView,
                                                            int position, long mylng) {


                                        String strPoliceName = policeList.get(position).getName()
                                                .toString();
                                        String strPoliceLocation = policeList.get(position).getLocation()
                                                .toString();
                                        editor.putString("PoliceName",strPoliceName);
                                        editor.putString("PoliceLocation",strPoliceLocation);
                                        editor.commit();
                                        Intent i = new Intent(getApplicationContext(), Map.class);
                                        startActivity(i);
                                        finish();
                                    }
                                });
                                // adding room to movies array
                                policeList.add(police);

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
