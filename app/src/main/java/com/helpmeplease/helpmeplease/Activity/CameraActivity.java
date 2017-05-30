package com.helpmeplease.helpmeplease.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.helpmeplease.helpmeplease.NetConnect;
import com.helpmeplease.helpmeplease.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CameraActivity extends AppCompatActivity {
    private SharedPreferences sp;
    private final String P_NAME = "Helpme";
    private TrackGPS gps;
    SharedPreferences.Editor editor;
    ImageView imageView;

    double lat, lng;
    private String UPLOAD_URL = "http://newwer.96.lt/API/uploadimage.php";
    private Bitmap bitmap;
    private String KEY_IMAGE = "image";
    public static final int REQUEST_CAMERA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sp = getSharedPreferences(P_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
        Log.i("Mylog", sp.getString("strMemberId", ""));
        // *** ImageView
        gps = new TrackGPS(CameraActivity.this);
        imageView = (ImageView) findViewById(R.id.imgView);

        // select for album
        final Button btnSelect = (Button) findViewById(R.id.btn_camera);
        // Perform action on click
        btnSelect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cameraIntent();
            }
        });
        // *** Upload Photo
        final Button btnUpload = (Button) findViewById(R.id.btn_ok);
        // Perform action on click
        btnUpload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(CameraActivity.this);

                dialog.setTitle("ยืนยันคำสั่ง");
                // dialog.setIcon(R.drawable.ic_launcher);
                dialog.setCancelable(true);
                dialog.setMessage("คุณต้องการอัฟโหลดรูปภาพ?");
                dialog.setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

//                       // Dialog
                        final android.app.AlertDialog.Builder ad = new
                                android.app.AlertDialog.Builder(CameraActivity.this);

                        ad.setTitle("แจ้งเตือน ! ");
                        ad.setPositiveButton("ปิด", null);


                        if (imageView.getDrawable() == null) {
                            ad.setMessage("กรุณาเลือกรูปภาพ");
                            ad.show();
                        } else {

//                          Toast toast =  Toast.makeText(CameraActivity.this,"อัฟโหลดภาพเรียบร้อย",Toast.LENGTH_LONG);
//                            toast.show();
//                            String image = getStringImage(bitmap);
//                            editor.putString("Picture",image);
//                            editor.commit();
//                            onBackPressed();
                            //makeJsonObjectRequest();
                            upload();
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
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void cameraIntent() {


        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_CAMERA);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK && data
                != null && data.getData() != null) {

            // Bundle extras = data.getExtras();
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
                imageView.setBackground(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //bitmap = (Bitmap) extras.get("data");


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
            finish();
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void upload(){
        //final ProgressDialog loading = ProgressDialog.show(this, "กำลังอัฟโหลด...", "กรุณารอสักครู่...", false, false);
        //Converting Bitmap to String
        String image = getStringImage(bitmap);

        //Getting Image Name


        final String MemberID = sp.getString("strMemberId", "0");
        String TypeMenu = sp.getString("Typemap", "");
        lat = gps.getLatitude();
        lng = gps.getLongitude();



        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(KEY_IMAGE, image));
        params.add(new BasicNameValuePair("memberid", MemberID));
        params.add(new BasicNameValuePair("typeMenu", TypeMenu));
        params.add(new BasicNameValuePair("location",  Double.toString(lng) + "," + Double.toString(lat)));
        String resultServer = NetConnect.getHttpPost(UPLOAD_URL, params);

        /*** Default Value ***/
        String strStatus = "0";
        String strMemberId = "0";
        String strDatetoday = "0";

        final AlertDialog.Builder ad = new AlertDialog.Builder(CameraActivity.this);
        JSONObject c;
        try {
            c = new JSONObject(resultServer);
            strStatus = c.getString("StatusID");
            strMemberId = c.getString("MemberId");
            strDatetoday= c.getString("Datetoday");
            Log.i("todaykub",strDatetoday);
            editor.putString("Datetoday",strDatetoday);
            editor.commit();

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Prepare LoginActivity
        if (strStatus.equals("1")) {
            Toast.makeText(CameraActivity.this,"อัฟโหลดรูปภาพเรียบร้อย", Toast.LENGTH_LONG).show();
        }
        else {

            ad.setTitle("แจ้งเตือน ! ");
            ad.setPositiveButton("ปิด", null);
            ad.setMessage("ไม่สามารถอัฟโหลดรูปได้กรุณาลองใหม่อีกครั้ง");
            ad.show();

        }


    }
}


