package com.java.liziang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.io.IOException;
import java.util.*;
import android.util.*;

import com.google.gson.JsonIOException;

import okhttp3.*;
import org.json.*;
public class QuestionsActivity extends AppCompatActivity {
    TextView tv;
    Button previousbutton,submitbutton, nextbutton,quitbutton;
    RadioGroup radio_g;
    RadioButton rb1,rb2,rb3,rb4;
    int flag=0;
    Boolean done=false;
    String label;



    public ArrayList<Question> questionList = new ArrayList<>();


    public static int marks=0,correct=0,wrong=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            label = bundle.getString("label");
        }
        ArrayList<String> stringArr = KeywordForQuesion.keywords;
//        stringArr.add("李白");
        getQuiz(stringArr);
        
        final TextView score = (TextView)findViewById(R.id.textView4);
        TextView textView=(TextView)findViewById(R.id.DispName);
        String name= "";

//        if (name.trim().equals(""))
//            textView.setText("Hello User");
//        else
//            textView.setText("Hello " + name);

        previousbutton=(Button)findViewById(R.id.button5);
        submitbutton=(Button)findViewById(R.id.button3);
        nextbutton=(Button)findViewById(R.id.button4);
        quitbutton=(Button)findViewById(R.id.buttonquit);
        tv=(TextView) findViewById(R.id.tvque);

        radio_g=(RadioGroup)findViewById(R.id.answersgrp);
        rb1=(RadioButton)findViewById(R.id.radioButton);
        rb2=(RadioButton)findViewById(R.id.radioButton2);
        rb3=(RadioButton)findViewById(R.id.radioButton3);
        rb4=(RadioButton)findViewById(R.id.radioButton4);

        setQuestion();
//        rb1.setChecked(true);

        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(radio_g.getCheckedRadioButtonId()==-1)
                {
                    Toast.makeText(getApplicationContext(), "Please select one choice", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!questionList.get(flag).answered()){
                    RadioButton uans = (RadioButton) findViewById(radio_g.getCheckedRadioButtonId());
                    String ansText = uans.getText().toString();
//                Toast.makeText(getApplicationContext(), ansText, Toast.LENGTH_SHORT).show();

                    String userAnswer= ansText.substring(0,1);
                    questionList.get(flag).userAnswer = userAnswer;
                    setRadioGroup(radio_g,false);
                    textView.setText("正确答案是："+questionList.get(flag).answer);
//                    if_submit=true;
                    if(userAnswer.equals(questionList.get(flag).answer)) {
                        correct++;
                        Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        wrong++;
                        Toast.makeText(getApplicationContext(), "Wrong", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

        previousbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (score != null)
                    score.setText(""+correct);
                if(flag>0){
//                    if_submit=true;
                    flag--;
                    textView.setText("正确答案是："+questionList.get(flag).answer);
                    String ans = questionList.get(flag).userAnswer;
                    setRadioGroup(radio_g,false);
                    if(ans.equals("A")){
                        rb1.setChecked(true);
                    }
                    else if(ans.equals("B")){
                        rb2.setChecked(true);
                    }
                    else if(ans.equals("C")){
                        rb3.setChecked(true);
                    }
                    else if(ans.equals("D")){
                        rb4.setChecked(true);
                    }
                    setQuestion();
                }


            }
        });
        nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(questionList.get(flag).answered()){
                    if(flag==questionList.size()-1){
                        Intent intent=new Intent(getApplicationContext(),ResultActivity.class);
                        startActivity(intent);
                    }
                    else{
                        flag++;
                        if(questionList.get(flag).answered()){

                            textView.setText("正确答案是："+questionList.get(flag).answer);
                            String ans = questionList.get(flag).userAnswer;
                            setRadioGroup(radio_g,false);
                            if(ans.equals("A")){
                                rb1.setChecked(true);
                            }
                            else if(ans.equals("B")){
                                rb2.setChecked(true);
                            }
                            else if(ans.equals("C")){
                                rb3.setChecked(true);
                            }
                            else if(ans.equals("D")){
                                rb4.setChecked(true);
                            }
                            setQuestion();
                        }
                        else{
                            textView.setText("");
                            setRadioGroup(radio_g,true);
                            if (score != null)
                                score.setText(""+correct);
                            if(flag<questionList.size())
                            {
                                setQuestion();
                            }
                            else
                            {

                                marks=correct;
                                Intent in = new Intent(getApplicationContext(),ResultActivity.class);
                                startActivity(in);
                            }
                            radio_g.clearCheck();
                        }
                    }
                    }




            }
        });

        quitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.i("!!!!","???setQuestion();");
                Intent intent=new Intent(getApplicationContext(),ResultActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void setQuestion(){
        while(done==false){
            try{
                Thread.sleep(10);
            }
            catch (InterruptedException e){}
        }
        Question q = questionList.get(flag);



        tv.setText(q.question);
        rb1.setText(q.choices.get(0));
        rb2.setText(q.choices.get(1));
        rb3.setText(q.choices.get(2));
        rb4.setText(q.choices.get(3));
    }

    protected void getQuiz(ArrayList<String> stringArr){
//        JSONArray jsonArray = JSONArray.fromObject(stringArr);

    if(!MainActivity.mainItem.rec){
        Log.i("????","!!!!!");
        JSONObject jsonObject = new JSONObject();
        done=false;
        JSONArray j=new JSONArray(stringArr);
        try{
            jsonObject.put("knowledge",j);
        }
        catch (JSONException e){}

        String jsonObjectString = jsonObject.toString();
        Log.i("login fail",jsonObjectString.toString());

        String api = "/users/exam";
        Server server = new Server(api,jsonObjectString);
        Call call=server.call();

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("login fail",e.toString());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String string = response.body().string();
                if(string.equals("failure")){
                    done=true;
                    return;
                }
                Log.i("exam response",string);
                JSONArray ret = null;

                try {
                    ret = new JSONArray(string);

                    for(int i=0; i<ret.length();i++){
                        Log.i("string",ret.getJSONObject(i).toString());
                        Question tmp = new Question(ret.getJSONObject(i));
                        questionList.add(tmp);
                    }
                    done=true;

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    else{
        if(!MainActivity.mainItem.curUser.equals("hly")){

        }
        else{
            Log.i("!!!!","!!!!!");
            String api = "/users/recommend";
            String json = String.format("{\"username\": \"%s\"}", MainActivity.mainItem.curUser);
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
                    Log.i("exam response",string);
                    JSONArray ret = null;
                    if(string.equals("failure")){
                        done=true;
                        return;
                    }
                    try {
                        ret = new JSONArray(string);
    
                        for(int i=0; i<ret.length();i++){
                            Log.i("string",ret.getJSONObject(i).toString());
                            Question tmp = new Question(ret.getJSONObject(i));
                            questionList.add(tmp);
                        }
                        done=true;
    
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
    
                }
            });
        }

    }


    }

    public void setRadioGroup(RadioGroup testRadioGroup,Boolean set) {
        for (int i = 0; i < testRadioGroup.getChildCount(); i++) {
            testRadioGroup.getChildAt(i).setEnabled(set);
        }
    }

    public static void openActivity(Context context, String label){
        Intent intent = new Intent(context, ObjectActivity.class);
        intent.putExtra("label", label);
        context.startActivity(intent);
    }
}

class Question{
    String answer;
    String question;
    
    ArrayList<String> choices=new ArrayList<String>();

    String userAnswer="";

    Question(JSONObject jsonObject){
        try{
            answer = jsonObject.getString("qAnswer");
            Log.i("answer",answer);
            JSONArray questionWithAns = jsonObject.getJSONArray("questionWithAns");
            Log.i("question1",questionWithAns.toString());
            String tmp = questionWithAns.toString();
            String tmpNew=tmp.substring(2,tmp.length()-2);
            String[] buff = tmpNew.split("\",\"");
            question = buff[0];

            Log.i("question2",question);
            for(int i=1;i<5;++i){
                choices.add(buff[i]);
            }
        }
        catch(JSONException e){}


    }
    Boolean answered(){
        Log.i("not answered",String.valueOf(userAnswer.equals("")));
        return !userAnswer.equals("");
    }
    String getUserAnswer(){return userAnswer;}



}