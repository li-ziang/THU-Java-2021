package com.java.liziang;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

//public class LinkedInfo extends AppCompatActivity {
//    private ListView listView;
//    private String datas[]={ "Sunday", "Monday", "Tuesday", "Wednesday",
//            "Thursday", "Friday", "Saturday" };//准备数据源
//    ArrayAdapter<String> adapter;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
//        setContentView(R.layout.activity_linked_info);
//        listView=(ListView)findViewById(R.id.lv);
//        //实例化ArrayAdapter
//        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,datas);
//        listView.setAdapter(adapter);//设置适配器
//    }
//
//}

public class LinkedInfo extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instance_link);
        ListView listView = (ListView) findViewById(R.id.list_item_link);
        //定义一个链表用于存放要显示的数据
        final List<String> adapterData = new ArrayList<String>();
        //存放要显示的数据
        for (int i = 0; i < 20; i++) {
            adapterData.add("ListItem" + i);
        }
        //创建ArrayAdapter对象adapter并设置适配器
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.activity_linked_info,adapterData);
        //将LsitView绑定到ArrayAdapter上
        listView.setAdapter(adapter);
    }
}