package com.java.liziang.ui.main;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.java.liziang.R;
import com.java.liziang.TabFragment;

import java.util.ArrayList;
import java.util.List;

public class FmPagerAdapter extends FragmentStatePagerAdapter {
    public ArrayList<String> the_list;
    public ArrayList<Fragment> the_arraylist;
    public Context context;

    public FmPagerAdapter(Context context,List<String> strings, ArrayList<Fragment> array, FragmentManager fm) {
        super(fm);
        this.context = context;
        the_arraylist = new ArrayList<Fragment>();
        the_list = new ArrayList<String>();
        the_arraylist.clear();
        the_list.clear();

        //the_list.add(strings.get(1));
        for(String ele:strings) {
            the_list.add(ele);
        }
   //     Log.i("uu", String.valueOf(the_list.size()));
        for(int i=0;i<array.size();i++) {
            Log.i("ele", array.get(i).toString());
            the_arraylist.add(array.get(i));
        }
    }
    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        //Log.i("print",the_arraylist.get(position).toString());
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
    public void removeTabPage(int position) {
        if (!the_arraylist.isEmpty() && position<the_arraylist.size()) {
            the_arraylist.remove(position);
            notifyDataSetChanged();
        }
    }
    public void addTabPage(String title) {
        the_arraylist.add(new TabFragment(title));
        notifyDataSetChanged();
    }
    @Override
    public int getItemPosition(Object object)   {
            return POSITION_NONE;

    }
}
