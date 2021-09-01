package com.java.liziang;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import java.io.IOException;
import androidx.core.content.ContextCompat;
import com.github.bassaer.chatmessageview.view.ChatView;

import java.io.IOException;
import java.util.Random;
import com.github.bassaer.chatmessageview.model.IChatUser;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import okhttp3.*;

import android.util.Log;
import android.view.View;

import com.github.bassaer.chatmessageview.model.Message;
import com.github.bassaer.chatmessageview.view.MessageView;

import java.util.ArrayList;


public class MessengerActivity extends Activity {

    private ChatView mChatView;
    private String course;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);

        //User id
        int myId = 0;
        //User icon
        Bitmap myIcon = BitmapFactory.decodeResource(getResources(), R.drawable.face_2);
        //User name
        String myName = "Michael";

        int yourId = 1;
        Bitmap yourIcon = BitmapFactory.decodeResource(getResources(), R.drawable.face_1);
        String yourName = "Emily";

        final iUser me = new iUser(myId, myName, myIcon);
        final iUser you = new iUser(yourId, yourName, yourIcon);

        mChatView = (ChatView)findViewById(R.id.chat_view);

        //Set UI parameters if you need
        mChatView.setRightBubbleColor(ContextCompat.getColor(this, R.color.green500));
        mChatView.setLeftBubbleColor(Color.WHITE);
        mChatView.setBackgroundColor(ContextCompat.getColor(this, R.color.blueGray500));
        mChatView.setSendButtonColor(ContextCompat.getColor(this, R.color.blueGray500));
        mChatView.setSendIcon(R.drawable.ic_action_send);
        mChatView.setRightMessageTextColor(Color.WHITE);
        mChatView.setLeftMessageTextColor(Color.BLACK);
        mChatView.setUsernameTextColor(Color.WHITE);
        mChatView.setSendTimeTextColor(Color.WHITE);
        mChatView.setDateSeparatorColor(Color.WHITE);
        mChatView.setInputTextHint("new message...");
        mChatView.setMessageMarginTop(5);
        mChatView.setMessageMarginBottom(5);

        //Click Send Button
        mChatView.setOnClickSendButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputText = mChatView.getInputText();
                //new message
                Message message = new Message.Builder()
                        .setUser(me)
                        .setRight(true)
                        .setText(inputText)
                        .hideIcon(true)
                        .build();
                //Set to chat view
                mChatView.send(message);
                //Reset edit text
                mChatView.setInputText("");
                String output;
                if(inputText.length()>4 && inputText.substring(0,4).equals("set:")){
                    String c = inputText.substring(4);
                    ArrayList<String> stringList = MainActivity.mainItem.stringList;
                    if(stringList.contains(c)){
                        output = c+" set";
                    }
                    else{
                        output="set failed";
                    }


                }else{
                    String api = "/users/Question";
                    String json = String.format("{\"inputQuestion\": \"%s\", \"course\":\"%s\"}",inputText,"chinese");
                    Server server = new Server(api,json);
                    Call call=server.call();



                    ArrayList<String> arrString = new ArrayList<>();


                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.i("getSearchHistory fail",e.toString());
                        }

                        @Override
                        public void onResponse(Call call, okhttp3.Response response)  {


                            try{
                                String output = response.body().string();
                                Log.i("search response",output);
                                arrString.add(output);
                            }
                            catch(IOException e){}
                        }
                    });

                    try{
                        while(arrString.size()==0){
                            Thread.sleep(10);
                        }
                    }
                    catch (InterruptedException e){}
                    output= arrString.get(0);
                    if(output==""){
                        output="我不知道";
                    }
                }






                //Receive message
                final Message receivedMessage = new Message.Builder()
                        .setUser(you)
                        .setRight(false)
                        .setText(output)
                        .build();

                // This is a demo bot
                // Return within 3 seconds
                int sendDelay = 0;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mChatView.receive(receivedMessage);
                    }
                }, sendDelay);
            }

        });

    }
}

class iUser implements IChatUser {
    Integer id;
    String name;
    Bitmap icon;

    public iUser(int id, String name, Bitmap icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    @Override
    public String getId() {
        return this.id.toString();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Bitmap getIcon() {
        return this.icon;
    }

    @Override
    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }
}

