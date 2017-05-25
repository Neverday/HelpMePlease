package com.helpmeplease.helpmeplease.Fragment;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.helpmeplease.helpmeplease.Activity.EditProfileActivity;
import com.helpmeplease.helpmeplease.NetConnect;
import com.helpmeplease.helpmeplease.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends android.support.v4.app.Fragment {
    Button btnEdit;
    private SharedPreferences sp;
    SharedPreferences.Editor editor;
    private final String P_NAME = "Helpme";
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        btnEdit = (Button) view.findViewById(R.id.btn_edit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileFragment.this.getActivity(), EditProfileActivity.class);
                startActivity(i);
            }
        });

        sp = getActivity().getSharedPreferences(P_NAME, Context.MODE_PRIVATE);

        final TextView txtName = (TextView) view.findViewById(R.id.txt_username);
        final TextView txtEmail = (TextView) view.findViewById(R.id.txt_email);
        final TextView txtPhone = (TextView) view.findViewById(R.id.txt_phone);
        final TextView txtPass = (TextView) view.findViewById(R.id.txt_pass);
        final String url = "http://newwer.96.lt/API/getByMemberID.php";
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

        return view;
    }

}
