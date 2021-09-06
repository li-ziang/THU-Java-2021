package com.java.liziang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class InstanceLinkActivity extends AppCompatActivity {
    public TextView searchContent;
    static public String inputText = "";
    static public String course = "";
    private Spinner spinner;
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instance_link);
        spinner = (Spinner) findViewById(R.id.spinner);
        data_list = new ArrayList<String>();
        data_list.add("chinese");
        data_list.add("english");
        data_list.add("math");
        arr_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner.setAdapter(arr_adapter);
        //添加事件Spinner事件监听
        spinner.setOnItemSelectedListener(new SpinnerSelectedListener());

        //设置默认值
        spinner.setVisibility(View.VISIBLE);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                inputText = "";
                searchContent = (TextView) findViewById(R.id.inputText);
                inputText = searchContent.getText().toString();

                if(inputText!= ""){
                    Log.i("testing instanceLinkActivity", "tesing instanceLinkActivity");
                    startActivity(new Intent(InstanceLinkActivity.this, LinkedInfoActivity.class));
                }
            }
        });

    }
    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
//            view.setText("你的血型是："+m[arg2]);
            InstanceLinkActivity.course = data_list.get(arg2);
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }
}