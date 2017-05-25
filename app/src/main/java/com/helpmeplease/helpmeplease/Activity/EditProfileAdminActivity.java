package com.helpmeplease.helpmeplease.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.helpmeplease.helpmeplease.NetConnect;
import com.helpmeplease.helpmeplease.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EditProfileAdminActivity extends AppCompatActivity {
    Button btnEdit;
    Button btnclrname,btnclrsurname,btnclrphone,btnclrsex;
    private SharedPreferences sp;
    SharedPreferences.Editor editor;
    private final String P_NAME = "Helpme";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_admin);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sp = getSharedPreferences(P_NAME, Context.MODE_PRIVATE);

        btnEdit = (Button) findViewById(R.id.btn_edit);
        btnclrname = (Button) findViewById(R.id.buttonNameClear);
        btnclrsurname = (Button) findViewById(R.id.buttonSurnamClear);
        btnclrphone = (Button) findViewById(R.id.buttonPhoneClear);
        btnclrsex = (Button) findViewById(R.id.buttonSexClear);

        btnclrname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText txtName = (EditText) findViewById(R.id.txt_username);
                txtName.setText("");
            }
        });
        btnclrsurname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText txtEmail = (EditText) findViewById(R.id.txt_email);
                txtEmail.setText("");
            }
        });

        btnclrphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText txtPhone = (EditText) findViewById(R.id.txt_phone);
                txtPhone.setText("");
            }
        });

        btnclrsex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText txtPass = (EditText) findViewById(R.id.txt_pass);
                txtPass.setText("");
            }
        });

        final TextView txtName = (TextView) findViewById(R.id.txt_username);
        final TextView txtEmail = (TextView) findViewById(R.id.txt_email);
        final TextView txtPhone = (TextView) findViewById(R.id.txt_phone);
        final TextView txtPass = (TextView) findViewById(R.id.txt_pass);
        final String url = "http://newwer.96.lt/API/getByMemberIDAdmin.php";
        final String MemberID = sp.getString("strMemberId", "0");



        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sMemberID", MemberID));


//        {"MemberID":"1","MemberName":"ภัทรพงศ์","MemberSurname":"ยอดวิเศษ","Username":"admin",
//                "Password":"1234","MemberPhone":"0820530650","MemberSex":"ชาย"}

        String resultServer = NetConnect.getHttpPost(url, params);
        String strMemberid = "default";
        String strName = "default";
        String strEmail = "default";
        String strPhone = "default";
        String strPass = "default";

        JSONObject c;
        try {
            c = new JSONObject(resultServer);
            strMemberid = c.getString("MemberID");
            strName = c.getString("MemberName");
            strEmail = c.getString("MemberEmail");
            strPhone = c.getString("MemberPhone");
            strPass = c.getString("MemberPass");
            Log.i("MYlog",strMemberid);
            Log.i("MYlog",strName);
            Log.i("MYlog",strEmail);
            Log.i("MYlog",strPhone);
            Log.i("MYlog",strPass);
            if (!strMemberid.equals("")) {
                txtName.setText(strName);
                txtEmail.setText(strEmail);
                txtPhone.setText(strPhone);
                txtPass.setText(strPass);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Dialog
        final AlertDialog.Builder  ad = new AlertDialog.Builder(EditProfileAdminActivity.this);
        ad.setTitle("แจ้งเตือน ! ");
        ad.setPositiveButton("ปิด", null);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check name
                if(txtName.getText().length() == 0)
                {
                    ad.setMessage("Please insert [Name] ");
                    ad.show();
                    txtName.requestFocus();
                }
                else if(txtEmail.getText().length() == 0)
                {
                    ad.setMessage("Please insert [Email] ");
                    ad.show();
                    txtEmail.requestFocus();
                }
                else if(txtPhone.getText().length() == 0)
                {
                    ad.setMessage("Please insert [Phone] ");
                    ad.show();
                    txtPhone.requestFocus();
                }
                else if(txtPass.getText().length() == 0)
                {
                    ad.setMessage("Please inser [Password] ");
                    ad.show();
                    txtPass.requestFocus();
                }
                else {


                    AlertDialog.Builder dialog = new AlertDialog.Builder(EditProfileAdminActivity.this);
                    dialog.setTitle("ยืนยันคำสั่ง");
                    // dialog.setIcon(R.drawable.ic_launcher);
                    dialog.setCancelable(true);
                    dialog.setMessage("คุณต้องการแก้ไขข้อมูลส่วนตัว?");
                    dialog.setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String url2 = "http://newwer.96.lt/API/EditMemberAdmin.php";


                            List<NameValuePair> params2 = new ArrayList<NameValuePair>();
                            params2.add(new BasicNameValuePair("sMemberid", MemberID));
                            params2.add(new BasicNameValuePair("sName", txtName.getText().toString()));
                            params2.add(new BasicNameValuePair("sEmail", txtEmail.getText().toString()));
                            params2.add(new BasicNameValuePair("sPhone", txtPhone.getText().toString()));
                            params2.add(new BasicNameValuePair("sPass", txtPass.getText().toString()));



                            String resultServer = NetConnect.getHttpPost(url2, params2);
                            //startActivity(new Intent(EditProfileFragment.this.getActivity(), EditActivity.class));

                            /*** Default Value ***/
                            String strStatusID = "0";
                            String strError = "Unknow Status!";

                            JSONObject c;
                            try {
                                c = new JSONObject(resultServer);
                                strStatusID = c.getString("StatusID");
                                strError = c.getString("Error");
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                            // Prepare Save Data
                            if (strStatusID.equals("0")) {
                                Toast.makeText(EditProfileAdminActivity.this, strError, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(EditProfileAdminActivity.this, strError, Toast.LENGTH_SHORT).show();
//                                txtName.setText("");
//                                txtEmail.setText("");
//                                txtPhone.setText("");
//                                txtPass.setText("");
//                                startActivity(new Intent(EditActivity.this,MemberActivity.class));
//                                finish();
                                onBackPressed();
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



            }
        });


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
}
