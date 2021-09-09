package com.java.liziang;
import java.io.IOException;
import java.util.*;
import android.util.*;
import okhttp3.*;
import org.json.*;


public class MainItem {
    public int sequence = 0;
    static public String curUser="hly2";
    public String course="";
    public String searchContent="s";
    public ArrayList<String> curStringList =new ArrayList<>();//cur string list 4 cources
    final public ArrayList<String> stringList =new ArrayList<>(); //all the 9 cources
    public ArrayList<Item> arrList =new ArrayList<>();// item list
    Boolean getArr = false;
    Boolean getHis = false;
    public ArrayList<String> searchKeyList = new ArrayList<>(); // 搜索历史
    public ArrayList<String> viewList = new ArrayList<>(); //浏览历史

    Boolean rec=false;

    MainItem(String course){
        curStringList.add("chinese");
        curStringList.add("english");
        //curStringList.add("math");
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
    public void search(){ //实体搜索
//        getArr = true;
//        Log.i("call func","call search");
        //arrList = new ArrayList<>();
       getArr = false;
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
//               Log.i("search response",string);
               JSONArray arr = null;
               if(string.equals("failure")){
                   getArr=true;
                   return;
               }
               try {
                    arrList.clear();
                    arr = new JSONArray(string);

                    for(int i=0; i<arr.length(); i++) {
                       JSONObject jsonObj = arr.getJSONObject(i);
                       String label = jsonObj.optString("label","defaultValue");
                       String category = jsonObj.optString("category","defaultValue");
                       //: 找到是不是在数据库中
//                        label = "李白"; // TODO: 改成非硬编码
//                        course="chinese";
//                       String find_ans = DbHelper.find(label, course, MainActivity.dbHelper.getReadableDatabase());
                        Boolean inDb = ObjectItem.inDatabase(label, course);
                       Item it;
//                       Log.i(" reading", "reading");

                       if(inDb) {
//                           Log.d("isRead", label);
                           it = new Item(label, category, true);
                       }
                       else {
//                           Log.d("is not read", label);
                           it = new Item(label,category, false);
                       }
                       arrList.add(it);
                   }

//                   Log.i("label",arrList.size()+"");
                   if(sequence==0){
                       //从a到z把arrList排序
                       arrList.sort((p1, p2) -> p1.label.compareTo(p2.label));
                   }
                   else if(sequence==1){
                    arrList.sort((p1, p2) -> p1.label.compareTo(p2.label));

                    Collections.reverse(arrList);

                   }
                   else if(sequence==2){

                    Collections.sort(arrList, Comparator.comparingInt(p -> p.label.length()));

                    Collections.reverse(arrList);
                   }
                   else{
                    Collections.sort(arrList, Comparator.comparingInt(p -> p.label.length()));
                   }
                   getArr = true;
               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }
       });
       Log.i("end func","end search");
        Log.i("size in func",arrList.size()+"");
    }
    public void getViewHistory(int number){ 
        String api = "/search/history";
        String json = String.format("{\"course\":%d,\"username\": \"%s\"}",number,curUser);
        Server server = new Server(api,json);
        Call call=server.call();

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("getHistory fail",e.toString());
            }
 
            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String string = response.body().string();
                Log.i("getHistory response",string);
                JSONArray arr = null;
                ArrayList<String> viewList_ = new ArrayList<>();
                try {
                    arr = new JSONArray(string);
                    for(int i=0; i<arr.length(); i++) {
                        JSONObject jsonObj = arr.getJSONObject(i);
                        String label = jsonObj.optString("history","defaultValue");
                        viewList_.add(label);
                    }
                    viewList=viewList_;
                    Log.i("viewList size",viewList.toString()+"");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
 
            }
        });
    }

    public void getSearchHistory(int number){
        getHis = false;
        String api = "/search/searchkey";
        Log.i("this is number !!!!", String.valueOf(number));
        String json = String.format("{\"number\":%d,\"username\": \"%s\"}",number,curUser);
        Server server = new Server(api,json);
        Call call=server.call();

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("getSearchHistory fail",e.toString());
            }
 
            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String string = response.body().string();
                if(string.equals("failure")){
                    getHis =true;
                    return;
                }
                Log.i("getSearchHistory response",string);
                ArrayList<String> searchKeyList_ = new ArrayList<>();
                if(string.length()!=2){
                    String[] stringList = string.substring(1,string.length()-1).split(",");
//                Log.i("string",stringList[1].substring(1,string.length()-1));
                    // searchKeyList
                    for (String s : stringList){
                        searchKeyList_.add(s.substring(1,s.length()-1));
                    }
                }

                searchKeyList = searchKeyList_;
                getHis = true;
//                Log.i("searchKeyList size",String.valueOf(searchKeyList.size()));
//                Log.i("searchKeyList 0",searchKeyList.get(0));
 
            }
        });
    }
    
}

class Item{
    public String label;
    public String category;
    public Boolean isRead;

    public Item(String label,String category){
        this.label=label;
        this.category=category;
        this.isRead = false;
    }
    public Item(String label,String category,Boolean isRead){
        this.label=label;
        this.category=category;
        this.isRead = isRead;
    }
    public Item(){
        this("","");
    }
}