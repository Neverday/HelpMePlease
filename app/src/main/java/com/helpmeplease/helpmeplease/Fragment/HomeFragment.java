package com.helpmeplease.helpmeplease.Fragment;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.helpmeplease.helpmeplease.Activity.CameraActivity;
import com.helpmeplease.helpmeplease.Activity.TrackGPS;
import com.helpmeplease.helpmeplease.NetConnect;
import com.helpmeplease.helpmeplease.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends android.support.v4.app.Fragment implements OnMapReadyCallback,DirectionCallback {
    private SharedPreferences sp;
    private final String P_NAME = "Helpme";
    private TrackGPS gps;
    GoogleMap mMap;
    Marker mMarker;
    final private String KEY_IMAGE = "image";
    double lat, lng;
    private Button btnCamera,btnSend;
    private int checkType = 0;
    private TextView txtSend;
    String url = "http://newwer.96.lt/API/insertlocation.php";
    String serverKey = "AIzaSyBocGokTyahDh6n714yzZdPIEiwX25br3M";
    SupportMapFragment mapFragment;
    String destinationHospital,nameHospital,destinationPolice,namePolice;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        txtSend = (TextView) view.findViewById(R.id.txt_send);
        sp = getActivity().getSharedPreferences(P_NAME, Context.MODE_PRIVATE);
        checkType = sp.getInt("Checktype", 0);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        Log.i("Mylog", String.valueOf(checkType));
       mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        gps = new TrackGPS(HomeFragment.this.getActivity());

        btnCamera = (Button) view.findViewById(R.id.btn_camera);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                String url = "http://newwer.96.lt/API/insert.php";
//                String MemberID = sp.getString("strMemberId","");
//                List<NameValuePair> params = new ArrayList<NameValuePair>();
//                params.add(new BasicNameValuePair("sMemberid", MemberID));
//                NetConnect.getHttpPost(url, params);
                startActivity(new Intent(HomeFragment.this.getActivity(), CameraActivity.class));

            }
        });
        btnSend = (Button) view.findViewById(R.id.btn_send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(HomeFragment.this.getActivity());
                String  dateToday = sp.getString("Datetoday", "");
                Log.i("Today",dateToday);
                dialog.setTitle("ยืนยันคำสั่ง");
                // dialog.setIcon(R.drawable.ic_launcher);
                dialog.setCancelable(true);
                dialog.setMessage("คุณต้องการยืนยันการส่ง?");
                dialog.setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        String  dateToday = sp.getString("Datetoday", "");
                        Log.i("Today",dateToday);

                        String MemberID = sp.getString("strMemberId","");


                        List<NameValuePair> params = new ArrayList<NameValuePair>();


                        lat = gps.getLatitude();
                        lng = gps.getLongitude();


                        params.add(new BasicNameValuePair("Memberid", MemberID));
                        params.add(new BasicNameValuePair("Datetoday", dateToday));
                        params.add(new BasicNameValuePair("Text", txtSend.getText().toString()));
                        String resultServer = NetConnect.getHttpPost(url, params);

                        /*** Default Value ***/
                        String strStatusId = "0";

                        String strError = "Unknow Status!";
                        String strMemberId = "0";

                        final AlertDialog.Builder ad = new AlertDialog.Builder(HomeFragment.this.getActivity());
                        JSONObject c;
                        try {
                            c = new JSONObject(resultServer);
                            strStatusId = c.getString("StatusID");
                            strError = c.getString("Error");


                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        // Prepare LoginActivity
                        if (strStatusId.equals("0")) {
                            // Dialog
                            ad.setTitle("แจ้งเตือน ! ");
                            ad.setPositiveButton("ปิด", null);
                            ad.setMessage(strError);
                            ad.show();
                        } else {
                            Toast.makeText(HomeFragment.this.getActivity(),"ส่งข้อมูลเรียบร้อย", Toast.LENGTH_LONG).show();



                        }


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







        return view;
    }
    public void CurrentLocation(){

        if (mMarker != null) {
            mMarker.remove();
        }
        if (gps.canGetLocation()) {
            lat = gps.getLatitude();
            lng = gps.getLongitude();
            Toast.makeText(HomeFragment.this.getActivity(), "Latitude:" + Double.toString(lat)
                    + "\nLongitude:" + Double.toString(lng), Toast.LENGTH_SHORT).show();
            LatLng latLng = new LatLng(gps.getLatitude(), gps.getLongitude());


//            mMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    latLng, 16));

        } else {
            gps.showSettingsAlert();
        }

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        if(checkType == 5 ){
            requestDirectionpolice();
            btnCamera.setVisibility(View.GONE);
            btnSend.setVisibility(View.GONE);
            txtSend.setVisibility(View.GONE);

            ViewGroup.LayoutParams params = mapFragment.getView().getLayoutParams();
            params.height = 900;
            mapFragment.getView().setLayoutParams(params);
        }
        else if (checkType == 6){
            requestDirectionhospital();
            btnCamera.setVisibility(View.GONE);
            btnSend.setVisibility(View.GONE);
            txtSend.setVisibility(View.GONE);
            ViewGroup.LayoutParams params = mapFragment.getView().getLayoutParams();
            params.height = 900;
            mapFragment.getView().setLayoutParams(params);
        }
        else {
            CurrentLocation();

        }
    }

    public void requestDirectionhospital() {



        if (mMarker != null) {
            mMarker.remove();
        }
        gps = new TrackGPS(HomeFragment.this.getActivity());
        LatLng latLng = new LatLng(gps.getLatitude(), gps.getLongitude());
        destinationHospital = sp.getString("HospitalLocation","");
        nameHospital = sp.getString("HospitalName","");
        String[] arr = destinationHospital.split(",");
        Double LatitudeHospital = Double.valueOf(arr[1]);
        Double LongitudeHospital = Double.valueOf(arr[0]);

        LatLng latLngHospital = new LatLng(LongitudeHospital,LatitudeHospital);
        Toast.makeText(HomeFragment.this.getActivity(), "Direction Requesting...",
                Toast.LENGTH_SHORT).show();
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                latLng, 16));
        GoogleDirection.withServerKey(serverKey)
                .from(latLng)
                .to(latLngHospital)
                .transportMode(TransportMode.DRIVING)
                .execute(this);

         mMap.addMarker(new MarkerOptions().position(latLngHospital).title(nameHospital));



    }
    public void requestDirectionpolice() {
        if (mMarker != null) {
            mMarker.remove();
        }
        gps = new TrackGPS(HomeFragment.this.getActivity());

        destinationPolice = sp.getString("PoliceLocation","");
        namePolice = sp.getString("PoliceName","");
        String[] arr = destinationPolice.split(",");
        Double LatitudePolice = Double.valueOf(arr[1]);
        Double LongitudePolice = Double.valueOf(arr[0]);
        LatLng latLngHospital = new LatLng(LongitudePolice,LatitudePolice);

        LatLng latLng = new LatLng(gps.getLatitude(), gps.getLongitude());
        Toast.makeText(HomeFragment.this.getActivity(), "Direction Requesting...",
                Toast.LENGTH_SHORT).show();
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                latLng, 16));
        GoogleDirection.withServerKey(serverKey)
                .from(latLng)
                .to(latLngHospital)
                .transportMode(TransportMode.DRIVING)
                .execute(this);

        mMap.addMarker(new MarkerOptions().position(latLngHospital).title("สถานีตำรวจนครบาลสายไหม"));

    }


    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        gps = new TrackGPS(HomeFragment.this.getActivity());
        LatLng latLng = new LatLng(gps.getLatitude(), gps.getLongitude());
        Toast.makeText(HomeFragment.this.getActivity(), "Success with status : "
                + direction.getStatus(), Toast.LENGTH_SHORT).show();
        if (direction.isOK()) {
            mMap.addMarker(new MarkerOptions().position(latLng));


            ArrayList<LatLng> directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();
            mMap.addPolyline(DirectionConverter.createPolyline(this.getActivity(), directionPositionList, 5, Color.RED));


        }
    }

    @Override
    public void onDirectionFailure(Throwable t) {
        Toast.makeText(HomeFragment.this.getActivity(), t.getMessage(),
                Toast.LENGTH_SHORT).show();

    }

}
