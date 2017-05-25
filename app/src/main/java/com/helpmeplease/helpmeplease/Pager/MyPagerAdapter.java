package com.helpmeplease.helpmeplease.Pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.helpmeplease.helpmeplease.Fragment.FeedFragment;
import com.helpmeplease.helpmeplease.Fragment.MenuFragment;

/**
 * Created by Phattarapong on 25-May-17.
 */

public class MyPagerAdapter extends FragmentPagerAdapter {

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new MenuFragment();

            case 1:
                return new FeedFragment();

            default:
                return new MenuFragment();

        }

    }

    @Override
    public int getCount() {
        return 2;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Menu";
            case 1:
                return "Feed News";
        }
        return null;
    }
}
