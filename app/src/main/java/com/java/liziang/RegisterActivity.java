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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
//import retrofit2.Call;
//import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

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


//        Call<ResponseBody> call = RetrofitClient
//                .getInstance()
//                .getAPI()
//                .createUser(new User(userName, password));
//
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                String s = "";
//                try {
//                    s = response.body().string();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                if (s.equals("SUCCESS")) {
//                    Toast.makeText(RegisterActivity.this, "Successfully registered. Please login", Toast.LENGTH_LONG).show();
//                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
//                } else {
//                    Toast.makeText(RegisterActivity.this, "User already exists!", Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        FormBody formBody = new FormBody.Builder()
                .add("username", "test").add("password", "test")
                .build();

        Request request = new Request.Builder()
                .header("X-Client-Type", "Android")
                .url("http://10.0.2.2:8080/users/login")
                .post(RequestBody
                        .create(MediaType
                                        .parse("application/json"),
                                "{\"username\": \"test\", \"password\":\"test\"}"
                        ))
                .build();
//        Request request = new Request.Builder()
//
//                .post(formBody)
//                .url("http://10.0.2.2:8080/users/login")
//                .build();
        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("1",e.toString());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String string = response.body().string();
                Log.i("asdas", string);
            }
        });
    }
}