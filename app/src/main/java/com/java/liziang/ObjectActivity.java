package com.java.liziang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.io.IOException;
import java.util.*;
import android.util.*;
import android.widget.TextView;

import okhttp3.*;
import org.json.*;

public class ObjectActivity extends AppCompatActivity {
    ObjectItem objectItem;
    public TextView name;
    RecyclerView recyclerView1;
    RecyclerView recyclerView2;
    ArrayList<ItemPro> itemPro;
    ProAdapter adapter_pro;
    String label = "";
    String subject = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        itemPro = new ArrayList<>();
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            label = bundle.getString("label");
            subject =bundle.getString("subject");
        }
        objectItem = new ObjectItem(label,subject);
        try{
            while(objectItem.get==false){
                Thread.sleep(10);
            }
        }
        catch (InterruptedException e){}
        Log.i("sssssssss",String.valueOf(objectItem.property.size()));
        for(Content ele:objectItem.property){
            itemPro.add(new ItemPro(ele.predicateLabel,ele.jectLabel,ele.isEntity));
        }
        Log.i("llllllll",String.valueOf(itemPro.size()));
        setContentView(R.layout.activity_object);
        recyclerView1 = findViewById(R.id.recycler_view_pro);
        recyclerView1.setLayoutManager(new LinearLayoutManager(ObjectActivity.this,LinearLayoutManager.VERTICAL,false));
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        adapter_pro = new ProAdapter(itemPro,ObjectActivity.this);
        adapter_pro.setOnItemClickListener(new ProAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                if(itemPro.get(position).isEntity ==true)
                    openActivity(ObjectActivity.this, itemPro.get(position).content,subject);
            }
        });
        recyclerView1.setAdapter(adapter_pro);
        name = findViewById(R.id.entity_name);
        name.setText(objectItem.name);
    }

    public static void openActivity(Context context, String label, String subject){
        Intent intent = new Intent(context, ObjectActivity.class);
        intent.putExtra("label", label);
        intent.putExtra("subject", subject);
        context.startActivity(intent);
    }

    class ItemPro{
        public String label;
        public String content;
        public Boolean isEntity;
        ItemPro(String label,String content,Boolean isEntity){
            this.label = label;this.content = content;this.isEntity = isEntity;
        }
    }
}