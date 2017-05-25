package com.helpmeplease.helpmeplease.Activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.helpmeplease.helpmeplease.Pager.MyPagerAdapter;
import com.helpmeplease.helpmeplease.R;

public class Menu extends FragmentActivity  {
    ViewPager pager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);
        tabLayout = (TabLayout) findViewById(R.id.tabs);


        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(pager);


    }


}
