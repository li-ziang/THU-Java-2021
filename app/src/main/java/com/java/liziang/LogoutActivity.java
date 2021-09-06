package com.java.liziang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;

public class LogoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        findViewById(R.id.btnLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUser();
            }
        });
    }


    private void logoutUser() {

        String api = "/users/logout";
        String json = String.format("{\"username\": \"%s\"}",MainActivity.mainItem.curUser);
        Server server = new Server(api,json);
        Call call=server.call();

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("logout fail",e.toString());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String string = response.body().string();
                Log.i("logout response",string);
                JSONObject ret = null;
                try {
                    ret = new JSONObject(string);
                    String code = ret.optString("code","defaultValue");
                    String content = ret.optString("content","defaultValue");

                    if (code.equals("200")) {
                        //TODO:need a logout.xml file
                        MainActivity.mainItem.curUser= "hly";
                         startActivity(new Intent(LogoutActivity.this, MainActivity.class));
                    } else {
                        Log.i("logout fail","error info");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }


}