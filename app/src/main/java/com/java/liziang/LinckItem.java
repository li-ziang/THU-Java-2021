package com.java.liziang;

import java.io.IOException;
import java.util.*;
import android.util.*;
import okhttp3.*;
import org.json.*;


public class LinckItem {
    String sentence;
    String course;
    public ArrayList<LinkWord> linkWordArray =new ArrayList<>();

    LinckItem(String sentence_,String course_){
        sentence=sentence_;
        course=course_;

        String api = "/users/linkedInstances";
        String json = String.format("{\"context\": \"%s\", \"course\":\"%s\"}",sentence,course);
        Server server = new Server(api,json);
        Call call=server.call();
 
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("linkedInstances fail",e.toString());
            }
 
            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String string = response.body().string();
                Log.i("linkedInstances response",string);
                JSONArray arr = null;
                try {
                    arr = new JSONArray(string);
                    for(int i=0; i<arr.length(); i++) {
                        JSONObject jsonObj = arr.getJSONObject(i);

                        String entityType = jsonObj.optString("entity_type","defaultValue");
                        int startIndex = jsonObj.getInt("start_index");
                        int endIndex = jsonObj.getInt("end_index");
                        String entity = jsonObj.optString("entity","defaultValue");
                        
                        LinkWord it = new LinkWord(entityType,startIndex,endIndex,entity);
                        linkWordArray.add(it);
                    }
                    Log.i("----------",linkWordArray.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
 
            }
        });
    }
}

class LinkWord{
    String entityType;
    int startIndex;
    int endIndex;
    String entity;

    LinkWord(String entityType_,int startIndex_,int endIndex_,String entity_){
        entityType = entityType_;
        startIndex = startIndex_;
        endIndex = endIndex_;
        entity = entity_;
    }
    @Override
    public String toString(){
	    return String.format("%s %d %d %s",entityType,startIndex,endIndex,entity);
    }
}
