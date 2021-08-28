package com.java.liziang;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.io.IOException;
import java.util.*;
import android.util.*;
import okhttp3.*;
import org.json.*;

public class ObjectActivity extends AppCompatActivity {
    ObjectItem objectItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        objectItem = new ObjectItem("李白","chinese");
        setContentView(R.layout.activity_object);
    }
}