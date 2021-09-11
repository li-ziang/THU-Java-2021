package com.java.liziang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

public class RegisterActivity extends AppCompatActivity {
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == -1){
                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
            }
            else if(msg.what == 1) {
                Toast.makeText(RegisterActivity.this, "注册失败，用户名重复", Toast.LENGTH_LONG).show();
            }
        }
    };
    private EditText etUsername, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.etRUserName);
        etPassword = findViewById(R.id.etRPassword);

        findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        findViewById(R.id.tvLoginLink).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    private void registerUser() {
        String userName = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (userName.isEmpty()) {
            etUsername.setError("Username is required");
            etUsername.requestFocus();
            return;
        } else if (password.isEmpty()) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        String api = "/users/register";
        String json = String.format("{\"username\": \"%s\", \"password\":\"%s\"}",userName,password);
        Server server = new Server(api,json);
        Call call=server.call();


        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("register fail",e.toString());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String string = response.body().string();
                Log.i("register response",string);
                JSONObject ret = null;
                try {
                    ret = new JSONObject(string);
                    String code = ret.optString("code","defaultValue");
                    String content = ret.optString("content","defaultValue");
                    Log.i("code",code);
                    Log.i("content",content);

                    if (code.equals("200")) {
//                        Toast.makeText(RegisterActivity.this, "Successfully registered. Please login", Toast.LENGTH_LONG).show();
                        Message msg = new Message();
                        msg.what = -1;
                        handler.sendMessage(msg);
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    } else {
                        Message msg = new Message();
                        msg.what = 1;
                        handler.sendMessage(msg);
                        Log.i("regist fail","user already exit");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


    }
}