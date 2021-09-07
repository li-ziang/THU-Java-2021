package com.java.liziang;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {
    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                    int version) {
        super(context, name, factory, version);
        // Log.d("creating database", "creating database");
    }

    @Override
    //数据库第一次创建时被调用
    public void onCreate(SQLiteDatabase db) {
        // Log.d("creating database", "creating database");
        db.execSQL("CREATE TABLE instance(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "instanceName VARCHAR(64), course VARCHAR(64),  " +
                "content VARCHAR(1024))");

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
        if(cursor.moveToFirst() == true)
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
}
