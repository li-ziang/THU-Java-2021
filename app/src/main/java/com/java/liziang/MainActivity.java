package com.java.liziang;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.os.*;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
  public static MainItem mainItem;
  public ImageView user_image;
    public static ArrayList<String> historyList = new ArrayList<String>();
    public TabLayout tabLayout;
    public ViewPager viewPager1;
    public TextView searchContent;
    public Spinner spinner;
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
        dbHelper = new DbHelper(MainActivity.this, "new.db", null, 1);
        DbHelper.insert("test", "test", "english", dbHelper.getWritableDatabase()); // 测试用
//        String test =  DbHelper.find("李白", "chinese", dbHelper.getReadableDatabase());
//        Log.i("testing database", test);
//        DbHelper.changeCollectedStatus("chinese", "李白",dbHelper);
        String course = "chinese";
        if(mainItem==null){
            mainItem = new MainItem(course);
        }

        Log.i("name in main",MainActivity.mainItem.curUser);
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
        spinner = (Spinner) findViewById(R.id.spinner1);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                String[] sequence = getResources().getStringArray(R.array.sequence);
                mainItem.sequence = pos;
                //Log.i("sequence",mainItem.sequence);
                //Toast.makeText(MainActivity.this, "你点击的是:"+sequence[pos], 2000).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        findViewById(R.id.edit_image).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showMultiSelect();
            }
        });
        findViewById(R.id.user_image).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.i("user's name",mainItem.curUser);
                if(mainItem.curUser.equals("hly2")){
                    // startActivity(new Intent(MainActivity.this, KeywordForQuesion.class));
//                startActivity(new Intent(MainActivity.this, LinkedInfo.class));
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
                else{
                    startActivity(new Intent(MainActivity.this, LogoutActivity.class));
                }

            }
        });

        findViewById(R.id.connect_image).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, InstanceLinkActivity.class));
            }
        });
        ////////////////////////
        findViewById(R.id.the_collect).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(MainActivity.mainItem.curUser.equals("hly2")){
                    Handler handler = new Handler(){
                        @Override
                        public void handleMessage(Message msg) {
                            if(msg.what == -1){
                                Toast.makeText(MainActivity.this, "", Toast.LENGTH_LONG).show();
                            }
                            else if(msg.what == 1) {
                                Toast.makeText(MainActivity.this, "未登陆，请先登陆", Toast.LENGTH_LONG).show();
                            }
                        }
                    };
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                    Intent startIntent=new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(startIntent);
                }
                else{
                startActivity(new Intent(MainActivity.this, CollectionActivity.class));}
            }
        });
        findViewById(R.id.the_history).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(MainActivity.mainItem.curUser.equals("hly2")){
                    Handler handler = new Handler(){
                        @Override
                        public void handleMessage(Message msg) {
                            if(msg.what == -1){
                                Toast.makeText(MainActivity.this, "", Toast.LENGTH_LONG).show();
                            }
                            else if(msg.what == 1) {
                                Toast.makeText(MainActivity.this, "未登陆，请先登陆", Toast.LENGTH_LONG).show();
                            }
                        }
                    };
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                    Intent startIntent=new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(startIntent);
                }
                else{
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));}
            }
        });
        /////////////////////////
        findViewById(R.id.ask_image).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, MessengerActivity.class));
        
            }
        });

        findViewById(R.id.message_image).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, KeywordForQuesion.class));
            }
        });
        findViewById(R.id.message_image_2).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(MainActivity.mainItem.curUser.equals("hly2")){
                    Handler handler = new Handler(){
                        @Override
                        public void handleMessage(Message msg) {
                            if(msg.what == -1){
                                Toast.makeText(MainActivity.this, "", Toast.LENGTH_LONG).show();
                            }
                            else if(msg.what == 1) {
                                Toast.makeText(MainActivity.this, "未登陆，请先登陆", Toast.LENGTH_LONG).show();
                            }
                        }
                    };
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                    Intent startIntent=new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(startIntent);
                }
                else{
                    MainActivity.mainItem.rec=true;
                    Intent startIntent=new Intent(MainActivity.this, QuestionsActivity.class);
                    startActivity(startIntent);
                }

            }
        });
        

        findViewById(R.id.buttonSearch).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                searchContent = (TextView) findViewById(R.id.searchEdit);
                mainItem.searchContent = searchContent.getText().toString();
                pagerAdapter.the_arraylist.clear();
                pagerAdapter.the_list.clear();
                for(String ele:mainItem.curStringList) {
                    pagerAdapter.the_arraylist.add(new TabFragment(ele));
                }
                for(int y=0;y<mainItem.curStringList.size();y++){
                    String ele = mainItem.curStringList.get(y)+"("+pagerAdapter.the_arraylist.get(y).toString()+")";
                    pagerAdapter.the_list.add(ele);
                }
                pagerAdapter.notifyDataSetChanged();
                mainItem.getSearchHistory(8);
                try{
                    while(MainActivity.mainItem.getHis==false){
                        Thread.sleep(10);
                    }
                }
                catch (InterruptedException e){}
                Log.i("fdhsjkflgbdsajhkfdsa",mainItem.searchKeyList.size()+"");
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
        user_image = findViewById(R.id.user_image);
        if(MainItem.curUser.equalsIgnoreCase("hly2")){
            user_image.setImageResource(R.drawable.user);
        }
        else{
            user_image.setImageResource(R.drawable.monkey);
        }




        // findViewById(R.id.buttonSearch).setOnClickListener(new View.OnClickListener(){
        //     @Override
        //     public void onClick(View view) {
        //         startActivity(new Intent(MainActivity.this, ObjectActivity.class));
        //     }
        // });

//        DbHelper.insert("test", "test", "test", dbHelper.getWritableDatabase());



    }



    private void showMultiSelect() {
        final List<Integer> choice = new ArrayList<>();
        final String[] items = {"chinese", "english",  "physics", "chemistry", "biology", "history", "geo", "politics"};
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
        if(mainItem.curStringList.contains("physics")) {
            isSelect[2] = true;
            choice.add(2);
        }
        if(mainItem.curStringList.contains("chemistry")) {
            isSelect[3] = true;
            choice.add(3);
        }
        if(mainItem.curStringList.contains("biology")) {
            isSelect[4] = true;
            choice.add(4);
        }
        if(mainItem.curStringList.contains("history")) {
            isSelect[5] = true;
            choice.add(5);
        }
        if(mainItem.curStringList.contains("geo")) {
            isSelect[6] = true;
            choice.add(6);
        }
        if(mainItem.curStringList.contains("politics")) {
            isSelect[7] = true;
            choice.add(7);
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
                        //Collections.sort(choice);// choice.sort();
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
                            //pagerAdapter.the_list.add(ele);
                            pagerAdapter.the_arraylist.add(new TabFragment(ele));
                        }
                        //pagerAdapter.the_list.clear();
                        for(int y=0;y<mainItem.curStringList.size();y++){
                            String ele = mainItem.curStringList.get(y)+"("+pagerAdapter.the_arraylist.get(y).toString()+")";
                            pagerAdapter.the_list.add(ele);
                        }

                        pagerAdapter.notifyDataSetChanged();
                        //viewPager1.notify();


                    }
                });

        builder.create().show();

    }
}


