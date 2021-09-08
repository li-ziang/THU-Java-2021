package com.java.liziang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class KeywordForQuesion extends AppCompatActivity {
    public TextView searchContent;
    public String inputText = "";
    static public ArrayList<String> keywords;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyword_for_quesion);
        findViewById(R.id.keywordButton).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                inputText = "";
                searchContent = (TextView) findViewById(R.id.inputKeywords);
                inputText = searchContent.getText().toString();
                String[] texts = inputText.split(" ");
                keywords = new ArrayList<>();
                for(int i = 0; i < texts.length; i++) {
                    keywords.add(texts[i]);
                }
                Log.i(" asking for questions", keywords.toString());
                if(inputText!= ""){
                    startActivity(new Intent(KeywordForQuesion.this, QuestionsActivity.class));
                }
            }
        });
    }
}