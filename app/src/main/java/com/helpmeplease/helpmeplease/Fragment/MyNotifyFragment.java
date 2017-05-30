package com.helpmeplease.helpmeplease.Fragment;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.helpmeplease.helpmeplease.Adapter.AppController;
import com.helpmeplease.helpmeplease.Adapter.Check;
import com.helpmeplease.helpmeplease.Adapter.CustomListAdapter;
import com.helpmeplease.helpmeplease.FragmentAdmin.HomeFragmentAdmin;
import com.helpmeplease.helpmeplease.NetConnect;
import com.helpmeplease.helpmeplease.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyNotifyFragment extends Fragment {

    // Log tag
    private static final String TAG = HomeFragmentAdmin.class.getSimpleName();

    // Movies json url
    private static final String url = "http://newwer.96.lt/API/getJSON.php";
    private ProgressDialog pDialog;
    private List<Check> checkList = new ArrayList<Check>();
    private ListView listView;
    private CustomListAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    SharedPreferences shared;
    SharedPreferences.Editor editor;
    private final String P_NAME = "Helpme";
    public MyNotifyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_notify, container, false);
        shared = this.getActivity().getSharedPreferences(P_NAME, Context.MODE_PRIVATE);
        editor = shared.edit();
        listView = (ListView) view.findViewById(R.id.list);
        adapter = new CustomListAdapter(this.getActivity(), checkList);
        listView.setAdapter(adapter);
        pDialog = new ProgressDialog(this.getActivity());
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

                        checkList.clear();

                        initSampleData();

                        adapter.notifyDataSetChanged();

                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });

        return view;
    }
    private void initSampleData() {
        // Creating volley request obj
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
                                Check check = new Check();
                                check.setCategory(obj.getString("category"));
                                check.setLocation(obj.getString("location"));
                                check.setText(obj.getString("text"));
                                check.setThumbnailUrl(obj.getString("image"));
                                check.setName(obj.getString("name"));
                                check.setEmail(obj.getString("email"));
                                check.setPhone(obj.getString("phone"));
                                check.setDatenotify(obj.getString("date"));
                                Log.i("Mylog", check.getCategory());
                                Log.i("Mylog", check.getLocation());
                                editor.putString("location", check.getLocation());
                                editor.commit();
                                Log.i("Mylog", check.getText());
                                Log.i("Mylog", check.getThumbnailUrl());
                                Log.i("Mylog", check.getName());
                                Log.i("Mylog", check.getEmail());
                                Log.i("Mylog", check.getPhone());


                                // OnClick Item
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    public void onItemClick(AdapterView<?> myAdapter, View myView,
                                                            final int position, long mylng) {
                                        final Dialog dialog = new Dialog(getActivity());
                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        dialog.setContentView(R.layout.customdialogmember);
                                        dialog.setCancelable(true);

                                        String strName = checkList.get(position).getName()
                                                .toString();
                                        String strPhone = checkList.get(position).getPhone()
                                                .toString();
                                        String strType = checkList.get(position).getCategory()
                                                .toString();
                                        String strText = checkList.get(position).getText()
                                                .toString();

                                        String Picture = checkList.get(position).getThumbnailUrl()
                                                .toString();

                                        final String Date = checkList.get(position).getDatenotify()
                                                .toString();

                                        TextView textClose = (TextView) dialog.findViewById(R.id.text_close);
                                        textClose.setTextColor(getResources().getColor(R.color.colorPrimary));
                                        textClose.setOnClickListener(new View.OnClickListener() {
                                            public void onClick(View v) {
                                                dialog.cancel();
                                            }
                                        });
                                        TextView txtMap = (TextView) dialog.findViewById(R.id.text_map);
                                        txtMap.setTextColor(getResources().getColor(R.color.colorPrimary));
                                        txtMap.setOnClickListener(new View.OnClickListener() {
                                            public void onClick(View v) {

                                                AlertDialog.Builder dialog = new AlertDialog.Builder(MyNotifyFragment.this.getActivity());

                                                dialog.setTitle("ยืนยันคำสั่ง");
                                                // dialog.setIcon(R.drawable.ic_launcher);
                                                dialog.setCancelable(true);
                                                dialog.setMessage("คุณต้องการยกเลิกรายการแจ้งเหตุ?");
                                                dialog.setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        final String url = "http://newwer.96.lt/API/deletenotify.php";
                                                        final String MemberID = shared.getString("strMemberId", "0");

                                                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                                                        params.add(new BasicNameValuePair("MemberID", MemberID));
                                                        params.add(new BasicNameValuePair("Date", Date));
                                                        Log.i("Mylog",MemberID);
                                                        String resultServer = NetConnect.getHttpPost(url, params);
                                                        checkList.clear();

                                                        initSampleData();
                                                        dialog.cancel();

                                                    }
                                                });

                                                dialog.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.cancel();
                                                    }
                                                });

                                                dialog.show();

                                            }
                                        });

                                        TextView txtName = (TextView) dialog.findViewById(R.id.txt_name);
                                        txtName.setText("ชื่อผู้แจ้ง : " + strName);
                                        TextView txtPhone = (TextView) dialog.findViewById(R.id.txt_phone);
                                        txtPhone.setText("เบอร์โทรศัพท์ : " + strPhone);
                                        TextView txtType = (TextView) dialog.findViewById(R.id.txt_type);
                                        txtType.setText("ประเภท : " + strType);
                                        TextView txtText = (TextView) dialog.findViewById(R.id.txt_other);
                                        txtText.setText("ข้อความเพิ่มเติม : " + strText);

                                        // Image link from internet
                                        new MyNotifyFragment.DownloadImageFromInternet((ImageView) dialog.findViewById(R.id.imageView1))
                                                .execute(Picture);


                                        dialog.show();



                                    }
                                });
                                // adding check to movies array
                                checkList.add(check);

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
    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView = imageView;
            Toast.makeText(getActivity(), "โปรดรอสักครู่ กำลังโหลดข้อมูลภาพ", Toast.LENGTH_SHORT).show();
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }

}
