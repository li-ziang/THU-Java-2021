package com.java.liziang;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.*;
import com.google.android.material.tabs.TabLayout;
import com.java.liziang.ui.main.FmPagerAdapter;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
  public static MainItem mainItem;
    public static ArrayList<String> historyList = new ArrayList<String>();
    public TabLayout tabLayout;
    public ViewPager viewPager1;
    public TextView searchContent;
    public ListView list;
    public FmPagerAdapter pagerAdapter;
    public ListAdapter listAdapter;
    private AlertDialog.Builder builder;
    static public DbHelper dbHelper;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private String[] titles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String course = "chinese";
        mainItem = new MainItem(course);
        //      mainItem.search();
//        mainItem.getViewHistory(10);
        mainItem.getSearchHistory(8);
        try{
            while(MainActivity.mainItem.getHis==false){
                Thread.sleep(10);
            }
        }
        catch (InterruptedException e){}
        historyList.clear();
        for(String ele:mainItem.searchKeyList){
            historyList.add(ele);
        }
        Log.i("history",String.valueOf(historyList.size()));
        setContentView(R.layout.activity_main);
        list = (ListView) findViewById(R.id.the_list);
        listAdapter = new ListAdapter(getApplicationContext(),historyList);
        list.setAdapter(listAdapter);
        list.setVisibility(View.GONE);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //parent 代表listView View 代表 被点击的列表项 position 代表第几个 id 代表列表编号
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchContent.setText(listAdapter.list.get(position));
                list.setVisibility(View.GONE);
            }
        });
        tabLayout = findViewById(R.id.tab_layout2);
        viewPager1 = findViewById(R.id.viewpager);
        fragments.clear();
        for (String ele:mainItem.curStringList) {
            fragments.add(new TabFragment(ele));
        }
        pagerAdapter = new FmPagerAdapter(getApplicationContext(),mainItem.curStringList, fragments, getSupportFragmentManager());
        viewPager1.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager1);
        findViewById(R.id.edit_image).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showMultiSelect();
            }
        });
        findViewById(R.id.user_image).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                 startActivity(new Intent(MainActivity.this, MessengerActivity.class));
            }
        });
        findViewById(R.id.buttonSearch).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                searchContent = (TextView) findViewById(R.id.searchEdit);
                mainItem.searchContent = searchContent.getText().toString();
                pagerAdapter.the_arraylist.clear();
                for(String ele:mainItem.curStringList) {
                    pagerAdapter.the_arraylist.add(new TabFragment(ele));
                }
                pagerAdapter.notifyDataSetChanged();
                mainItem.getSearchHistory(8);
                try{
                    while(MainActivity.mainItem.getHis==false){
                        Thread.sleep(10);
                    }
                }
                catch (InterruptedException e){}
                listAdapter.list.clear();
                for(String ele:mainItem.searchKeyList){
                    listAdapter.list.add(ele);
                }
                listAdapter.notifyDataSetChanged();
                searchContent.setText("");

            }
        });
        list.bringToFront();
        searchContent = (TextView) findViewById(R.id.searchEdit);
        searchContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && !s.toString().equals("")) {
                    list.setVisibility(View.VISIBLE);
                } else {
                    list.setVisibility(View.GONE);
                }
            }
        });


        dbHelper = new DbHelper(MainActivity.this, "test.db", null, 1);
//        DbHelper.insert("test", "test", "english", dbHelper.getWritableDatabase()); // 测试用
        // findViewById(R.id.buttonSearch).setOnClickListener(new View.OnClickListener(){
        //     @Override
        //     public void onClick(View view) {
        //         startActivity(new Intent(MainActivity.this, ObjectActivity.class));
        //     }
        // });
    }



    private void showMultiSelect() {
        final List<Integer> choice = new ArrayList<>();
        final String[] items = {"chinese", "english", "math", "physics", "chemistry", "biology", "history", "geo", "politics"};
        //默认都未选中
        boolean[] isSelect = new boolean[9];
        if(mainItem.curStringList.contains("chinese")) {
            isSelect[0] = true;
            choice.add(0);
        }
        if(mainItem.curStringList.contains("english")) {
            isSelect[1] = true;
            choice.add(1);
        }
        if(mainItem.curStringList.contains("math")) {
            isSelect[2] = true;
            choice.add(2);
        }
        if(mainItem.curStringList.contains("physics")) {
            isSelect[3] = true;
            choice.add(3);
        }
        if(mainItem.curStringList.contains("chemistry")) {
            isSelect[4] = true;
            choice.add(4);
        }
        if(mainItem.curStringList.contains("biology")) {
            isSelect[5] = true;
            choice.add(5);
        }
        if(mainItem.curStringList.contains("history")) {
            isSelect[6] = true;
            choice.add(6);
        }
        if(mainItem.curStringList.contains("geo")) {
            isSelect[7] = true;
            choice.add(7);
        }
        if(mainItem.curStringList.contains("politics")) {
            isSelect[8] = true;
            choice.add(8);
        }
        builder = new AlertDialog.Builder(this)
                .setTitle("选择学科加入顶部栏")
                .setMultiChoiceItems(items, isSelect, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if (b) {
                            choice.add(i);
                        } else {

                            choice.remove(choice.indexOf(i));
                            Log.i("size", String.valueOf(choice.size()));
                            Log.i("index",String.valueOf(i));
                        }
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StringBuilder str = new StringBuilder();
                        mainItem.curStringList.clear();
                        Collections.sort(choice);// choice.sort();
                        for (int j = 0; j < choice.size(); j++) {
                            mainItem.curStringList.add(items[choice.get(j)]);
                        }
                        pagerAdapter.the_list.clear();
                        pagerAdapter.the_arraylist.clear();
//                        pagerAdapter.the_list.add("ele");
//                            pagerAdapter.the_arraylist.add(new TabFragment("ele"));
//
                        //pagerAdapter.notifyDataSetChanged();
                        for(String ele:mainItem.curStringList) {
                            pagerAdapter.the_list.add(ele);
                            pagerAdapter.the_arraylist.add(new TabFragment(ele));
                        }
                        pagerAdapter.notifyDataSetChanged();
                        //viewPager1.notify();


                    }
                });

        builder.create().show();

    }
}


