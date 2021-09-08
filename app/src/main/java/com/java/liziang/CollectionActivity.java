package com.java.liziang;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import okhttp3.Call;

public class CollectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        String api = "/search/history";
        String username = MainItem.curUser;
        String json = String.format("{\"username\": \"%s\", \"number\":\"%s\"}", username, 100);
        Server server = new Server(api,json);
        Call call=server.call();

    }
}