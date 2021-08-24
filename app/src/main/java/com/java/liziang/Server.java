package com.java.liziang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class Server {
    OkHttpClient okHttpClient;
    Request request;
    Call call_;

    public Server(String api, String json) {
        okHttpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS).build();
        request = new Request.Builder().header("X-Client-Type", "Android").url("http://10.0.2.2:8080" + api)
                .post(RequestBody.create(MediaType.parse("application/json"), json)).build();
        call_ = okHttpClient.newCall(request);
    }
    public Call call(){
        return call_;
    }

}

