package com.helpmeplease.helpmeplease.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

public class Login extends AppCompatActivity {
    private SharedPreferences sp;
    SharedPreferences.Editor editor;
    private final String P_NAME = "Helpme";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView btnRegister = (TextView) findViewById(R.id.Registerlink);
        TextView btnForget = (TextView) findViewById(R.id.txt_forget);
        sp = getSharedPreferences(P_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();

        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Register.class);
                startActivity(i);
            }
        });
        Button btnBack = (Button) findViewById(R.id.bCancle);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Main.class);
                startActivity(i);
            }
        });
        final AlertDialog.Builder ad = new AlertDialog.Builder(this);

        // txtUsername & txtPassword
        final EditText txtEmail = (EditText) findViewById(R.id.txt_email);
        final EditText txtPass = (EditText) findViewById(R.id.txt_pass);
        btnForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent i = new Intent(getApplicationContext(),ForgetActivity.class);
                startActivity(i);
            }
        });
        // btnLogin
        final Button btnLogin = (Button) findViewById(R.id.bLogin);
        // Perform action on click
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String url = "http://newwer.96.lt/API/login.php";
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("strEmail", txtEmail.getText().toString()));
                params.add(new BasicNameValuePair("strPass", txtPass.getText().toString()));
                String resultServer = NetConnect.getHttpPost(url, params);

                /*** Default Value ***/
                String strStatusId = "0";

                String strError = "Unknow Status!";
                String strMemberId = "0";
                String strMemberName = "0";
                String strMemberEmail= "0";
                String strMemberPhone = "0";

                JSONObject c;
                try {
                    c = new JSONObject(resultServer);
                    strStatusId = c.getString("StatusID");
                    strError = c.getString("Error");
                    strMemberId= c.getString("memberid");
                    strMemberName = c.getString("membername");
                    strMemberEmail = c.getString("memberemail");
                    strMemberPhone = c.getString("memberphone");
                    editor.putString("strMemberId",strMemberId);
                    editor.putString("strMemberName",strMemberName);
                    editor.putString("strMemberEmail",strMemberEmail);
                    editor.putString("strMemberPhone",strMemberPhone);
                    editor.commit();

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // Prepare LoginActivity
                if (strStatusId.equals("1")) {
                    Toast.makeText(Login.this, "ยินดีต้อนรับ : " + strMemberName, Toast.LENGTH_LONG).show();
                    Intent newActivity = new Intent(Login.this, Menu.class);



                    startActivity(newActivity);
                    finish();
                }
                else  if (strStatusId.equals("2")) {
                    Toast.makeText(Login.this, "ยินดีต้อนรับ : " + strMemberName, Toast.LENGTH_LONG).show();
                    Intent newActivity = new Intent(Login.this, AdminActivity.class);



                    startActivity(newActivity);
                    finish();
                }
                else {

                    ad.setTitle("แจ้งเตือน ! ");
                    ad.setPositiveButton("ปิด", null);
                    ad.setMessage(strError);
                    ad.show();
                    txtEmail.setText("");
                    txtPass.setText("");
                }


            }
        });

    }
    }

