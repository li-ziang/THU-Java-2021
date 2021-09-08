package com.java.liziang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;

class CollectionInfo {
    String course;
    String intanceName;
    CollectionInfo(String course, String instanceName) {
        this.course = course;
        this.intanceName = instanceName;
    }
}

public class CollectionActivity extends AppCompatActivity {
    CollectionAdapter adapter;
    List<CollectionInfo> array;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        String api = "/users/getCollection";
        String username = MainItem.curUser;
        String json = String.format("{\"username\": \"%s\"}", username);
        Server server = new Server(api,json);
        Call call=server.call();
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("collection api failed ",e.toString());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String string = response.body().string();
                JSONArray jsonArray = null;
                array = new ArrayList<>();
                Log.i(" searching collections", string);
                try {
                    jsonArray = new JSONArray(string);
                    if (jsonArray.length() != 0) {
                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject temp = jsonArray.getJSONObject(i);
                            String course = temp.getString("course");
                            String instanceName = temp.getString("instanceName");
                            CollectionInfo ci = new CollectionInfo(course, instanceName);
                            array.add(ci);
                        }
                    } else {
                        Log.i("fail","error info");
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
        setContentView(R.layout.activity_collection);
        recyclerView = (RecyclerView) findViewById(R.id.colletion_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(CollectionActivity.this, LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new CollectionAdapter(array ,CollectionActivity.this);
        adapter.setOnItemClickListener(new CollectionAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                startActivity(new Intent(CollectionActivity.this, ObjectActivity.class));
            }
        });
        recyclerView.setAdapter(adapter);
    }
}