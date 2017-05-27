package com.helpmeplease.helpmeplease.Activity;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.helpmeplease.helpmeplease.NetConnect;
import com.helpmeplease.helpmeplease.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ForgetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final TextView txtEmail = (TextView) findViewById(R.id.txt_email);
        final TextView txtName = (TextView) findViewById(R.id.txt_name);
        Button btnOK = (Button) findViewById(R.id.btn_ok);
        final AlertDialog.Builder ad = new AlertDialog.Builder(ForgetActivity.this);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = "http://newwer.96.lt/API/forget.php";
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("strEmail", txtEmail.getText().toString()));
                params.add(new BasicNameValuePair("strName", txtName.getText().toString()));
                String resultServer = NetConnect.getHttpPost(url, params);

                /*** Default Value ***/
                String strStatusId = "0";

                String strError = "Unknow Status!";



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
                    ad.setTitle("แจ้งเตือน ! ");
                    ad.setPositiveButton("ปิด", null);
                    ad.setMessage(strError);
                    ad.show();
                    txtEmail.setText("");
                    txtName.setText("");
                }
                else {

                    ad.setTitle("แจ้งเตือน ! ");
                    ad.setPositiveButton("ปิด", null);
                    ad.setMessage(strError);
                    ad.show();
                    txtEmail.setText("");
                    txtName.setText("");
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
