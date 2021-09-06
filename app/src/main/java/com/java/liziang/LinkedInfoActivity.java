package com.java.liziang;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class LinkedInfoActivity extends AppCompatActivity {
    private ListView listView;
    String inputText;
    private String datas[]={ "Sunday", "Monday", "Tuesday", "Wednesday",
            "Thursday", "Friday", "Saturday", "Saturday" };//准备数据源
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inputText = InstanceLinkActivity.inputText;
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        setContentView(R.layout.activity_linked_info);
        listView=(ListView)findViewById(R.id.lv);
        //实例化ArrayAdapter
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,datas);
        listView.setAdapter(adapter);//设置适配器
    }
}