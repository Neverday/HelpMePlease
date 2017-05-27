package com.helpmeplease.helpmeplease.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.helpmeplease.helpmeplease.Activity.HospitalMenuActivity;
import com.helpmeplease.helpmeplease.Activity.Map;
import com.helpmeplease.helpmeplease.Activity.PoliceMenuActivity;
import com.helpmeplease.helpmeplease.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {
    ImageView imgNotify,imgFire,imgFlood,imgSnake,imgHosital,imgPolice;
    final String P_NAME = "Helpme";
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    int checktype = 0;

    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        sp = getActivity().getSharedPreferences(P_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();

        imgNotify = (ImageView) view.findViewById(R.id.Imalarm) ;
        imgFire = (ImageView) view.findViewById(R.id.Imbrun) ;
        imgFlood = (ImageView) view.findViewById(R.id.Imwater) ;
        imgSnake = (ImageView) view.findViewById(R.id.Imsnake) ;
        imgHosital = (ImageView) view.findViewById(R.id.Imhospital) ;
        imgPolice = (ImageView) view.findViewById(R.id.Impolice) ;

        imgNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checktype = 1;
                editor.putString("Typemap","แจ้งเหตุฉุกเฉิน");
                editor.putInt("Checktype",checktype);
                editor.commit();
                Intent i = new Intent(MenuFragment.this.getActivity(), Map.class);
                startActivity(i);
                //โชว์ประเภทการแจ้งเตือน ใน LOG
//                String Typemap = sp.getString("Typemap", "");
//                Log.i("MYLOG",Typemap);

            }
        });
        imgFire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checktype = 2;
                editor.putInt("Checktype",checktype);
                editor.putString("Typemap","แจ้งเหตุไฟไหม้");
                editor.commit();
                Intent i = new Intent(MenuFragment.this.getActivity(), Map.class);
                startActivity(i);
                //โชว์ประเภทการแจ้งเตือน ใน LOG
//                String Typemap = sp.getString("Typemap", "");
//                Log.i("MYLOG",Typemap);
            }
        });
        imgFlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checktype = 3;
                editor.putInt("Checktype",checktype);
                editor.putString("Typemap","แจ้งเหตุน้ำท่วม");
                editor.commit();
                Intent i = new Intent(MenuFragment.this.getActivity(), Map.class);
                startActivity(i);
                //โชว์ประเภทการแจ้งเตือน ใน LOG
//                String Typemap = sp.getString("Typemap", "");
//                Log.i("MYLOG",Typemap);
            }
        });
        imgSnake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checktype = 4;
                editor.putInt("Checktype",checktype);
                editor.putString("Typemap","แจ้งเหตุสัตว์ร้าย");
                editor.commit();
                Intent i = new Intent(MenuFragment.this.getActivity(), Map.class);
                startActivity(i);
                //โชว์ประเภทการแจ้งเตือน ใน LOG
//                String Typemap = sp.getString("Typemap", "");
//                Log.i("MYLOG",Typemap);
            }
        });
        imgPolice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checktype = 5;
                editor.putInt("Checktype",checktype);
                editor.commit();
                Intent i = new Intent(MenuFragment.this.getActivity(), PoliceMenuActivity.class);
                startActivity(i);
            }
        });
        imgHosital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checktype = 6;
                editor.putInt("Checktype",checktype);
                editor.commit();






                Intent i = new Intent(MenuFragment.this.getActivity(), HospitalMenuActivity.class);
                startActivity(i);
            }
        });

        return view;
    }


}
