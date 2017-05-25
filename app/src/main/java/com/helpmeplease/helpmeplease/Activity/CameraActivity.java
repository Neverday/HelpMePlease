package com.helpmeplease.helpmeplease.Activity;

import android.app.ProgressDialog;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.helpmeplease.helpmeplease.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;

public class CameraActivity extends AppCompatActivity {
    private SharedPreferences sp;
    private final String P_NAME = "Helpme";
    ImageView imageView;
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

        Log.i("Mylog",sp.getString("strMemberId",""));
        // *** ImageView
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


                        if(imageView.getDrawable() == null)
                        {
                            ad.setMessage("กรุณาเลือกรูปภาพ");
                            ad.show();
                        }
                        else {
                            uploadImage();
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
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void uploadImage() {
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "กำลังอัฟโหลด...", "กรุณารอสักครู่...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(CameraActivity.this,"ฮัฟโหลดรูปภาพเรียบร้อย", Toast.LENGTH_LONG).show();
                       onBackPressed();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(CameraActivity.this, "ไม่สามารถอัฟโหลดรูปได้กรุณาลองใหม่อีกครั้ง", Toast.LENGTH_LONG).show();
                        //Log.e("MYLOG",volleyError.getMessage().toString());
                    }
                }) {
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap);

                //Getting Image Name

                Log.i("Picture",image);
                final String MemberID = sp.getString("strMemberId", "0");
                //Creating parameters
                java.util.Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put(KEY_IMAGE, image);
                params.put("memberid", MemberID);

//
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    }

