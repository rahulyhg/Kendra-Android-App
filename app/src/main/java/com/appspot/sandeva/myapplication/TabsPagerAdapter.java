package com.appspot.sandeva.myapplication;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        Log.d("TAB===", Integer.toString(index));
        switch (index) {
            case 0:
                return new KendraFragment();
            case 1:
                return new PlanetsFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}