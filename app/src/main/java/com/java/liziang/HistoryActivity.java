package com.java.liziang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
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

class HistoryInfo {
    String history;
    String time;
    String course;
    HistoryInfo(String history, String time, String course) {
        this.history = history;
        this.course = course;
        this.time = time;
    }
}

public class HistoryActivity extends AppCompatActivity {
    HistoryAdapter adapter;
    List<HistoryInfo> array;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        String api = "/search/history";
        String username = MainItem.curUser;
        String json = String.format("{\"username\": \"%s\", \"number\":\"%s\"}", username, 100);
        Server server = new Server(api,json);
        Call call=server.call();
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("history api failed ",e.toString());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String string = response.body().string();
                JSONArray jsonArray = null;
                array = new ArrayList<>();
                Log.i(" searching history", string);
                try {
                    jsonArray = new JSONArray(string);
                    if (jsonArray.length() != 0) {
                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject temp = jsonArray.getJSONObject(i);
                            String history = temp.getString("history");
                            String time = temp.getString("time");
                            String course = temp.getString("course");
                            HistoryInfo hi = new HistoryInfo(history, time, course);
                            array.add(hi);
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
        setContentView(R.layout.activity_history);
        recyclerView = (RecyclerView) findViewById(R.id.his_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(HistoryActivity.this, LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new HistoryAdapter(array ,HistoryActivity.this);
        adapter.setOnItemClickListener(new HistoryAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                startActivity(new Intent(HistoryActivity.this, ObjectActivity.class));
            }
        });
        recyclerView.setAdapter(adapter);
    }
}