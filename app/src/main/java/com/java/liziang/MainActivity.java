package com.java.liziang;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.java.liziang.ui.main.FmPagerAdapter;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
  public static MainItem mainItem;
      String course = "语文";
        mainItem = new MainItem(course);
      //mainItem.search();
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private String[] titles = new String[]{"李狗", "英超", "西甲", "意甲","狗黄1","狗黄2","狗黄3"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     init();

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

        for (int i = 0; i < titles.length; i++) {
            fragments.add(new TabFragment(titles[i]));
        }

        FmPagerAdapter pagerAdapter = new FmPagerAdapter(Arrays.asList(titles), fragments, getSupportFragmentManager());
        viewPager1.setAdapter(pagerAdapter);


        tabLayout.setupWithViewPager(viewPager1);
    }
}
