package com.java.liziang;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class DbHelper extends SQLiteOpenHelper {
    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                    int version) {
        super(context, name, factory, version);
        // Log.d("creating database", "creating database");
    }

    @Override
    //数据库第一次创建时被调用
    public void onCreate(SQLiteDatabase db) {
         Log.d("creating database", "creating database");
        db.execSQL("CREATE TABLE instance(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "instanceName VARCHAR(64), course VARCHAR(64), " +
                "content VARCHAR(1024))");
        db.execSQL("CREATE TABLE curUser(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "User VARCHAR(64), username VARCHAR(64))");
    }
    //软件版本号发生改变时调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("ALTER TABLE rank ADD phone INTEGER '0'");
    }

    public static void insert(String content, String instanceName,String course, SQLiteDatabase db ) {
//        db = dbHelper.getWritableDatabase();
//        SQLiteDatabase db = MainActivity.dbHelper.getWritableDatabase();
        ContentValues values1 = new ContentValues();
        Cursor cursor =  db.rawQuery("SELECT * FROM instance WHERE instanceName = ? and course = ?",
                new String[]{instanceName, course});
        if(cursor.moveToFirst()) { // 已经缓存
            return;
        }
        values1.put("content", content);
        values1.put("course", course);
        values1.put("instanceName", instanceName);
        long result = db.insert("instance", null, values1);
        // Log.d("inserting new data", "inserting new data");
    }

    public static String find(String instanceName, String course, SQLiteDatabase db) {
//        SQLiteDatabase db = MainActivity.dbHelper.getReadableDatabase();
        Cursor cursor =  db.rawQuery("SELECT * FROM instance WHERE instanceName = ? and course = ?",
                new String[]{instanceName, course});
//        Log.d("finding data", instanceName + " " + course);
        if(cursor.moveToFirst())
        {
            String content = cursor.getString(cursor.getColumnIndex("content"));
//            Log.d(content, content);
            return content;
        }
        cursor.close();
        return null;
    }
    public static void delete(String instanceName, String course, SQLiteDatabase db) {
        db.execSQL("DELETE FROM instance WHERE instanceName = ? and course = ?",  new String[]{instanceName, course});
    }

    public static void setCollectionStatus(String course, String instanceName, Boolean collected, DbHelper helper) {
        SQLiteDatabase readDb = helper.getReadableDatabase(), writeDb = helper.getWritableDatabase();
        Cursor cursor =  readDb.rawQuery("SELECT * FROM instance WHERE instanceName = ? and course = ?",
                new String[]{instanceName, course});
        if(cursor.moveToFirst()) {
            String content = cursor.getString(cursor.getColumnIndex("content"));
            delete(instanceName, course, writeDb);
            try {
                JSONObject json = new JSONObject(content);
                if(!json.has("isCollected")) return;
                Boolean isCollected = json.getBoolean("isCollected");
                isCollected = collected;
                json.put("isCollected", isCollected);
                ContentValues values1 = new ContentValues();
                values1.put("content", json.toString());
                values1.put("course", course);
                values1.put("instanceName", instanceName);
                writeDb.insert("instance", null, values1);
//                return isCollected;
            }
            catch (JSONException e) {}
        }
//        return null;
    }

    static public void setCurUser(String username, SQLiteDatabase db) {
        ContentValues values1 = new ContentValues();
        Cursor cursor =  db.rawQuery("SELECT * FROM curUser WHERE User = 'curUser'",
                new String[]{});
        if(cursor.moveToFirst()) { // 已经缓存
            Log.i("username is ", username);
            db.execSQL("DELETE FROM curUser WHERE User = ?",  new String[]{"curUser"});
        }
        ContentValues values = new ContentValues();
        values.put("User", "curUser");
        values.put("username", username);
        db.insert("curUser", null, values);
    }

    static public String getCurUser(SQLiteDatabase db) {
        Cursor cursor =  db.rawQuery("SELECT * FROM curUser WHERE User = 'curUser'",
                new String[]{});
        if(cursor.moveToFirst()) {
            String content = cursor.getString(cursor.getColumnIndex("username"));
//            Log.d(content, content);
            if(content != null)
            return content;
        }
        return "hly2";
    }

    public static Boolean changeCollectedStatus(String course, String instanceName, DbHelper helper) {
        SQLiteDatabase readDb = helper.getReadableDatabase(), writeDb = helper.getWritableDatabase();
        Cursor cursor =  readDb.rawQuery("SELECT * FROM instance WHERE instanceName = ? and course = ?",
                new String[]{instanceName, course});
        if(cursor.moveToFirst()) {
            String content = cursor.getString(cursor.getColumnIndex("content"));
            delete(instanceName, course, writeDb);
            try {
                JSONObject json = new JSONObject(content);
                if(!json.has("isCollected")) return null;
                Boolean isCollected = json.getBoolean("isCollected");
                if(isCollected == true) isCollected = false;
                else{
                    isCollected = true;
                }
                json.put("isCollected", isCollected);
                ContentValues values1 = new ContentValues();
                values1.put("content", json.toString());
                values1.put("course", course);
                values1.put("instanceName", instanceName);
                writeDb.insert("instance", null, values1);
                return isCollected;
            }
            catch (JSONException e) {return null;}
        }
        return null;
    }
}
