package com.java.liziang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class InstanceLinkActivity extends AppCompatActivity {
    public TextView searchContent;
    static public String inputText = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instance_link);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                inputText = "";
                searchContent = (TextView) findViewById(R.id.searchEdit);
                inputText = searchContent.getText().toString();
                if(inputText!= ""){
                    startActivity(new Intent(InstanceLinkActivity.this, LinkedInfoActivity.class));
                }
            }
        });

    }
}