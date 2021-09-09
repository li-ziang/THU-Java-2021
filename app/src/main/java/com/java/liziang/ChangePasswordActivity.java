package com.java.liziang;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import android.os.Handler;
import okhttp3.Call;
import okhttp3.Callback;

public class ChangePasswordActivity extends AppCompatActivity {
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == -1){
                Toast.makeText(ChangePasswordActivity.this, "修改密码失败。请检查输入是否正确", Toast.LENGTH_LONG).show();
            }
            else if(msg.what == 1) {
                Toast.makeText(ChangePasswordActivity.this, "密码修改成功", Toast.LENGTH_LONG).show();
            }
        }
    };
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
            newPassword.setError("different passwords");
            newPassword.requestFocus();
            return;
        }
        String api = "/users/changePassword";
        String json = String.format("{\"username\": \"%s\", \"oldPassword\":\"%s\" ,\"newPassword\":\"%s\"}",MainItem.curUser, oldP, newp);
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
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);

                    startActivity(new Intent(ChangePasswordActivity.this, MainActivity.class));
                }
                else {
//                    Toast.makeText(ChangePasswordActivity.this, "有空输入！\n请重新输入！", Toast.LENGTH_SHORT).show();
                    Message msg = new Message();
                    msg.what = -1;
                    handler.sendMessage(msg);
                    Log.i("Failed to change password", "Failed to change passwd");
                }
            }
        });
    }
}