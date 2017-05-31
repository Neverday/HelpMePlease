package com.helpmeplease.helpmeplease.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.helpmeplease.helpmeplease.NetConnect;
import com.helpmeplease.helpmeplease.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Register extends AppCompatActivity {
    private EditText name;
    private EditText email;
    private EditText tel;
    private EditText pass;
    private Button Saveregis;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);




            // btnSave
            final Button btnregister = (Button) findViewById(R.id.Saveregis);
            // Perform action on click
            btnregister.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (SaveData()) {
                        Intent i = new Intent(getApplicationContext(), Login.class);
                        startActivity(i);
                        finish();
                    }
                }
            });
        }
    }
    public boolean SaveData()
    {

        // txtUsername,txtPassword,txtName,txtEmail,txtTel
        final EditText txtUsername = (EditText)findViewById(R.id.name);
        final EditText txtEmail = (EditText)findViewById(R.id.email);
        final EditText txtPhone = (EditText)findViewById(R.id.tel);
        final EditText txtPass = (EditText)findViewById(R.id.pass);



        // Dialog
        final AlertDialog.Builder ad = new AlertDialog.Builder(this);

        ad.setTitle("แจ้งเตือน ! ");
        ad.setPositiveButton("ปิด", null);

        // Check Username
        if(txtUsername.getText().length() == 0)
        {

            ad.setMessage("กรุณากรอก [ชื่อผู้ใช้] ");
            ad.show();
            txtUsername.requestFocus();
            return false;

        }
        //Check Email
        if(txtEmail.getText().length() == 0)
        {

            ad.setMessage("กรุณากรอก [อีเมล์] ");
            ad.show();
            txtUsername.requestFocus();
            return false;

        }
        //Check Tel
        if(txtPhone.getText().length() == 0 || txtPhone.getText().length()<10 )
        {

            ad.setMessage("กรุณากรอก [เบอร์โทรศัพท์] ขั้นต่ำ 10 ตัวเลข ");
            ad.show();
            txtUsername.requestFocus();
            return false;

        }
        //Check Pass
        if(txtPass.getText().length() == 0 || txtPass.getText().length()<6)
        {

            ad.setMessage("กรุณากรอก [พาสเวิร์ด] ขั้นต่ำ 6 ตัวอํกษร ");
            ad.show();
            txtUsername.requestFocus();
            return false;

        }



        String url = "http://newwer.96.lt/API/saveADDData.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sUsername", txtUsername.getText().toString()));
        params.add(new BasicNameValuePair("sEmail", txtEmail.getText().toString()));
        params.add(new BasicNameValuePair("sTel", txtPhone.getText().toString()));
        params.add(new BasicNameValuePair("sPass", txtPass.getText().toString()));

        Log.i("Name",txtUsername.getText().toString());
        Log.i("sEmail",txtEmail.getText().toString());
        Log.i("sTel",txtPhone.getText().toString());
        Log.i("sPass",txtPass.getText().toString());


        String resultServer  = NetConnect.getHttpPost(url,params);

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
        if(strStatusID.equals("0"))
        {
            Toast.makeText(Register.this, strError, Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(Register.this, "สมัครสมาชิกเรียบร้อย", Toast.LENGTH_SHORT).show();

        }


        return true;
    }
}


