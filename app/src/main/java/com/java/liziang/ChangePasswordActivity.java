package com.java.liziang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;

public class ChangePasswordActivity extends AppCompatActivity {
    private EditText oldPassword, newPassword, confirmNewPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        oldPassword = findViewById(R.id.OldPassword);
        newPassword = findViewById(R.id.newPassword);
        confirmNewPassword = findViewById(R.id.confirmPassword);
        findViewById(R.id.btChangePasswd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();
            }
        });
    }

    private void changePassword() {
        String oldP = oldPassword.getText().toString().trim();
        String newp = newPassword.getText().toString().trim();
        String confirmp = confirmNewPassword.getText().toString().trim();
        if(!newp.equals(confirmp)) {
            newPassword.setError("Username is required");
            newPassword.requestFocus();
        }
        String api = "/users/changePassword";
        String json = String.format("{\"username\": \"%s\", \"oldPassword\":\"%s\" \"newPassword\":\"%s\"}",MainItem.curUser, oldP, newp);
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
                JSONObject ret = null;
                Log.i(" returning something", string);
                if(string.equals("Success")) {
                    Log.i("Successfully change password", "succcessfully change password");
                }
                else {
                    Log.i("Failed to change password", "Failed to change passwd");
                }
            }
        });
    }
}