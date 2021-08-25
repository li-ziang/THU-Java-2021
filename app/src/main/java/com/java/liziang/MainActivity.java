package com.java.liziang;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.java.liziang.ui.main.FmPagerAdapter;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
  public static MainItem mainItem;
    private AlertDialog.Builder builder;

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private String[] titles ;//= new String[]{"李狗", "英超", "西甲", "意甲","狗黄1","狗黄2","狗黄3"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String course = "语文";
        mainItem = new MainItem(course);
        //mainItem.search();
        setContentView(R.layout.activity_main);
     init();
        findViewById(R.id.edit_image).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showMultiSelect();
            }
        });
        findViewById(R.id.user_image).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

    }

    private void init() {
        TabLayout tabLayout = findViewById(R.id.tab_layout2);
        ViewPager viewPager1 = findViewById(R.id.viewpager);
        fragments.clear();
        for (String ele:mainItem.curStringList) {
            fragments.add(new TabFragment(ele));
        }

        FmPagerAdapter pagerAdapter = new FmPagerAdapter(mainItem.curStringList, fragments, getSupportFragmentManager());
        viewPager1.setAdapter(pagerAdapter);


        tabLayout.setupWithViewPager(viewPager1);
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
                        init();
                        //Toast.makeText(MainActivity.this, "你选择了" + str, Toast.LENGTH_LONG).show();
                    }
                });

        builder.create().show();

    }
}
