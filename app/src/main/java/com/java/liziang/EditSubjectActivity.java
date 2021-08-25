package com.java.liziang;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class EditSubjectActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_subject);
        Button btnMultiSelect = findViewById(R.id.btn_multi_select);
        btnMultiSelect.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
                //showMultiSelect();

    }

}