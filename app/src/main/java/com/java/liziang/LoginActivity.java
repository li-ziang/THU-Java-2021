package com.java.liziang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import org.json.JSONException;
import org.json.JSONObject;
public class LoginActivity extends AppCompatActivity {
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == -1){
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();
            }
            else if(msg.what == 1) {
                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_LONG).show();
            }
        }
    };
    private EditText etUsername, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);

        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        
        findViewById(R.id.tvRegisterLink).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void loginUser() {
        final String userName = etUsername.getText().toString().trim();
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

        // Log.i("adf",MainActivity.mainItem.curUser);
        String api = "/users/login";
        String json = String.format("{\"username\": \"%s\", \"password\":\"%s\"}",userName,password);
        Server server = new Server(api,json);
        Call call=server.call();

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("login fail",e.toString());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String string = response.body().string();
                Log.i("login response",string);
                JSONObject ret = null;
                try {
                    ret = new JSONObject(string);
                    String code = ret.optString("code","defaultValue");
                    String content = ret.optString("content","defaultValue");
                    Log.i("code",code);
                    Log.i("content",content);

                    if (code.equals("200")) {
                        Log.i("login success",userName);
                        MainActivity.mainItem.curUser= userName;
                        Message msg = new Message();
                        msg.what = -1;
                        handler.sendMessage(msg);
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                        startActivity(new Intent(LoginActivity.this, MainActivity.class).putExtra("username", userName));
                    } else {
                        Message msg = new Message();
                        msg.what = 1;
                        handler.sendMessage(msg);
                        Log.i("login fail","error info");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }



}