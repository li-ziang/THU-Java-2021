package com.java.liziang;
import java.io.IOException;
import java.util.*;
import android.util.*;
import okhttp3.*;
import org.json.*;


public class ObjectItem {
    public String name;
    public String course;
    public String jsonString;

    public ArrayList<Content> objContent = new ArrayList<>();
    public ArrayList<Content> property = new ArrayList<>();
    public ArrayList<Content> subContent = new ArrayList<>();

    ObjectItem(String name_, String course_) {
        name = name_;
        course = course_;
        Log.i("adsaf","fadsfa");
        if (inDatabase()) {
            int b = 1;
        } else {
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

                    try {
                        JSONObject jsonObject = new JSONObject(jsonString);
                        JSONArray jsonObjContent = jsonObject.getJSONArray("obj_content");
                        JSONArray jsonProperty = jsonObject.getJSONArray("property");
                        Boolean namedIndividual = jsonObject.getBoolean("NamedIndividual");
                        JSONArray jsonSubContent = jsonObject.getJSONArray("sub_content");

                        objContent = parseJsonArray(jsonObjContent, "object_label", "predicate_label", false);
                        property = parseJsonArray(jsonProperty, "label", "predicateLabel", true);
                        subContent = parseJsonArray(jsonSubContent, "subject_label", "predicate_label", false);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

        }
    }

    public boolean inDatabase() {
        return false;
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

