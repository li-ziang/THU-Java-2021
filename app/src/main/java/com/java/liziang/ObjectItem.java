package com.java.liziang;
import java.io.IOException;
import java.util.*;
import android.util.*;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;

import okhttp3.*;
import org.json.*;


public class ObjectItem {
    public String name;
    public String course;
    public String jsonString;
    public boolean get = false;
    public boolean isCollected=false;

    public Boolean namedIndividual =false;
    public ArrayList<Content> objContent = new ArrayList<>();
    public ArrayList<Content> property = new ArrayList<>();
    public ArrayList<Content> subContent = new ArrayList<>();

    ObjectItem(String name_, String course_) {
        get = false;
        name = name_;
        course = course_;
//        Log.i(" name of instance", name);
        if (inDatabase(name, course)) {
            Log.i(" searching in database", "searching in database");
            int b = 1;
            //: public String jsonString;

            jsonString = DbHelper.find(name, course, MainActivity.dbHelper.getReadableDatabase());

            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray jsonObjContent = jsonObject.getJSONArray("obj_content");
                JSONArray jsonProperty = jsonObject.getJSONArray("property");
                namedIndividual = jsonObject.getBoolean("NamedIndividual");
                JSONArray jsonSubContent = jsonObject.getJSONArray("sub_content");
                isCollected= jsonObject.getBoolean("isCollected");
                
                objContent = parseJsonArray(jsonObjContent, "object_label", "predicate_label", false);
                property = parseJsonArray(jsonProperty, "label", "predicateLabel", true);
                subContent = parseJsonArray(jsonSubContent, "subject_label", "predicate_label", false);
                get =true;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            get  =true;
        } else {
            Log.i("not in database", "not in database");
            String api = "/search/info";

            String json = String.format("{\"username\": \"%s\", \"course\":\"%s\", \"instanceName\":\"%s\"}", MainActivity.mainItem.curUser, course, name);
            Server server = new Server(api, json);
            Call call = server.call();

            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i("object search fail", e.toString());
                }

                @Override
                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                    jsonString = response.body().string();
                    // Log.i("object search fail",string);
                    //TODO: save jsonString ???42?????????
                    Log.i(" json jsonString", jsonString);
                    if(!jsonString.equals("failure")){
                        if(DbHelper.find(name, course, MainActivity.dbHelper.getReadableDatabase()) != null) {
                            DbHelper.delete(name, course, MainActivity.dbHelper.getWritableDatabase());
                        }
                        try{
                            JSONObject temp = new JSONObject(jsonString);
                            if(temp.has("obj_content")) {
                                DbHelper.insert(jsonString, name, course, MainActivity.dbHelper.getWritableDatabase());
                            }
                        }
                        catch(JSONException E) {}

                        try {
                            JSONObject jsonObject = new JSONObject(jsonString);
                            JSONArray jsonObjContent = jsonObject.getJSONArray("obj_content");
                            JSONArray jsonProperty = jsonObject.getJSONArray("property");
                            namedIndividual = jsonObject.getBoolean("NamedIndividual");

                            JSONArray jsonSubContent = jsonObject.getJSONArray("sub_content");
                            isCollected= jsonObject.getBoolean("isCollected");
                            objContent = parseJsonArray(jsonObjContent, "object_label", "predicate_label", false);
                            property = parseJsonArray(jsonProperty, "label", "predicateLabel", true);
                            subContent = parseJsonArray(jsonSubContent, "subject_label", "predicate_label", false);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        Log.i(" json jsonString", "fail");
                    }
                    get =true;

                }
            });

        }
    }

    static public boolean inDatabase(String instanceName, String course) {
//        return false;
        String string = DbHelper.find(instanceName, course, MainActivity.dbHelper.getReadableDatabase());


        JSONObject json;
        if(string == null) {
            return false;
        }
        try {
//            Log.i("  data string", "data");
            json = new JSONObject(string);
//            Log.i(" ?????? ?????????", json.toString());
            if(!json.has("obj_content")) {
//                Log.d("congxincun", "congxincun");
                return false;
            }
        }
        catch (JSONException e) {}



        return (DbHelper.find(instanceName, course, MainActivity.dbHelper.getReadableDatabase()) != null);
    }

    ArrayList<Content> parseJsonArray(JSONArray jsonArray, String jectLabel, String predicateLabel, Boolean isEntity) {
        ArrayList<Content> contentList = new ArrayList<>();
        try {
            for(int i=0; i<jsonArray.length(); i++) {
                JSONObject j = jsonArray.getJSONObject(i);
                if (isEntity) {
                    contentList.add(new Content(j.optString(jectLabel, "defaultValue"), j.optString(predicateLabel, "defaultValue"), j.getBoolean("isEntity")));
                } else {
                    contentList.add(new Content(j.optString(jectLabel, "defaultValue"), j.optString(predicateLabel, "defaultValue")));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contentList;}

    void addCollect(){
        if(DbHelper.find(name, course, MainActivity.dbHelper.getReadableDatabase()) == null) {
            return;
        }
        String api = "/users/addCollection";
        String json = String.format("{\"username\": \"%s\", \"instanceName\":\"%s\",\"course\":\"%s\"}", MainActivity.mainItem.curUser, name,course);
        Server server = new Server(api, json);
        isCollected=true;
        Call call = server.call();

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("addCollect fail", e.toString());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String s= response.body().string();
                Log.i("addCollect",s);
            }
        });

        DbHelper.changeCollectedStatus(course,name,MainActivity.dbHelper);
            

    }
    void delCollect(){
        String api = "/users/deleteCollection";
        String json = String.format("{\"username\": \"%s\", \"instanceName\":\"%s\",\"course\":\"%s\"}", MainActivity.mainItem.curUser, name,course);
        Server server = new Server(api, json);
        isCollected=false;

        Call call = server.call();

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("delCollect fail", e.toString());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String s= response.body().string();
                Log.i("delCollect",s);

            }
        });

        DbHelper.changeCollectedStatus(course,name,MainActivity.dbHelper);



    }

}

class Content{
    String jectLabel;
    String predicateLabel;
    Boolean isEntity;
    Content(String jectLabel_,String predicateLabel_){
        jectLabel = jectLabel_;
        predicateLabel = predicateLabel_;
    }
    Content(String jectLabel_,String predicateLabel_,Boolean isEntity_){
        jectLabel = jectLabel_;
        predicateLabel = predicateLabel_;
        isEntity=isEntity_;
    }

    @Override
    public String toString(){
	    return "jectLabel"+jectLabel+" predicateLabel"+predicateLabel;
    }

}

