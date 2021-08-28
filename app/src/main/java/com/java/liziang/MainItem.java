package com.java.liziang;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.*;
import org.json.*;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
public class MainItem {
    public String curUser="test";
    public String course="";
    public String searchContent="s";
    public ArrayList<String> curStringList =new ArrayList<>();//cur string list 4 cources
    final private ArrayList<String> stringList =new ArrayList<>(); //all the 9 cources
    public ArrayList<Item> arrList =new ArrayList<>();// item list

    MainItem(String course){
        curStringList.add("chinese");
        curStringList.add("english");
        curStringList.add("math");
        curStringList.add("physics");


        stringList.add("chinese");
        stringList.add("english");
        stringList.add("math");
        stringList.add("physics");
        stringList.add("chemistry");
        stringList.add("biology");
        stringList.add("history");
        stringList.add("geo");
        stringList.add("politics");
        this.course=course;

    }
    MainItem(String course,String searchContent){
        this(course);
        this.searchContent=searchContent;
    }
    public void search(){
       String api = "/users/search";
       String json = String.format("{\"username\": \"%s\", \"course\":\"%s\", \"keyword\":\"%s\"}",curUser,course,searchContent);
       Server server = new Server(api,json);
       Call call=server.call();

       call.enqueue(new Callback() {
           @Override
           public void onFailure(Call call, IOException e) {
               Log.i("search fail",e.toString());
           }

           @Override
           public void onResponse(Call call, okhttp3.Response response) throws IOException {
               String string = response.body().string();
               Log.i("search response",string);
               JSONArray arr = null;
               try {
                   arr = new JSONArray(string);
                   for(int i=0; i<arr.length(); i++) {
                       JSONObject jsonObj = arr.getJSONObject(i);
                       String label = jsonObj.optString("label","defaultValue");
                       String category = jsonObj.optString("category","defaultValue");
                       Item it = new Item(label,category);
                       arrList.add(it);
                   }
                   Log.i("label",arrList.size()+"");
               } catch (JSONException e) {
                   e.printStackTrace();
               }

           }
       });

    }
}

class Item{
    public String label;
    public String category;

    public Item(String label,String category){
        this.label=label;
        this.category=category;
    }
    public Item(){
        this("","");
    }
}