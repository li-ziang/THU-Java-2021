package com.java.liziang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;

class LinkedModel {
    public String label;
    public String course;
    LinkedModel(String label, String course) {
        this.label = label;
        this.course = course;
    }
}

public class LinkedInfoActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    public String inputText, course;
    LinkedAdapter adapter;
    List<LinkedModel> array = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inputText = InstanceLinkActivity.inputText;
        course = InstanceLinkActivity.course;
        String api = "/users/linkedInstances";
        String json = String.format("{\"context\": \"%s\", \"course\":\"%s\"}", inputText, course);
        Server server = new Server(api,json);
        Call call=server.call();

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("linkedList api failed ",e.toString());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String string = response.body().string();
                JSONArray jsonArray = null;
                array = new ArrayList<>();
                try {
                    jsonArray = new JSONArray(string);
                    Log.i("info get from linkedInfo", string);
                    if (jsonArray.length() != 0) {
                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject temp = jsonArray.getJSONObject(i);
                            String name = temp.getString("entity");
                            LinkedModel lm = new LinkedModel(name, course);
                            array.add(lm);
                        }
//                        startActivity(new Intent(LinkedInfoActivity.this, InstanceLinkActivity.class));
                    } else {
                        Log.i("login fail","error info");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });
        try{
            while(array == null){
                Thread.sleep(10);
            }
        }

        catch (InterruptedException e){}
        Log.i(" fuck off", array.toString());
        setContentView(R.layout.activity_linked_info);
        recyclerView = (RecyclerView) findViewById(R.id.lv);
        recyclerView.setLayoutManager(new LinearLayoutManager(LinkedInfoActivity.this, LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new LinkedAdapter(array ,LinkedInfoActivity.this);
        adapter.setOnItemClickListener(new LinkedAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                startActivity(new Intent(LinkedInfoActivity.this, ObjectActivity.class));
            }
        });
        recyclerView.setAdapter(adapter);



    }
}