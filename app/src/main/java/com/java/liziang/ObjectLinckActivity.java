package com.java.liziang;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ObjectLinckActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String sentence="I want an apple.";
        String course="english";
        LinckItem l = new LinckItem(sentence,course);
        setContentView(R.layout.activity_object_linck);
    }
}