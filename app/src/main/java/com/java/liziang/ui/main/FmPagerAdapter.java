package com.java.liziang.ui.main;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.java.liziang.R;

import java.util.ArrayList;
import java.util.List;

public class FmPagerAdapter extends FragmentPagerAdapter{
    public ArrayList<String> the_list;
    public ArrayList<Fragment> the_arraylist;

    public FmPagerAdapter(List<String> strings, ArrayList<Fragment> array, FragmentManager fm) {
        super(fm);
        the_arraylist = new ArrayList<Fragment>();
        the_list = new ArrayList<String>();
        Log.i("ele", strings.get(1));
        //the_list.add(strings.get(1));
        for(String ele:strings) {
            the_list.add(ele);
        }
   //     Log.i("uu", String.valueOf(the_list.size()));
        for(int i=0;i<array.size();i++)
            the_arraylist.add(array.get(i));
    }
    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return the_arraylist.get(position);
    }
    @Override
    public int getCount() {
        // Show 2 total pages.
        return the_arraylist.size();
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return the_list.get(position);
    }
}
