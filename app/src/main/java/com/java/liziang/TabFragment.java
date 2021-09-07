package com.java.liziang;

import android.content.Intent;
import android.graphics.Color;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TabFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<ItemModel> itemModel;
    CustomAdapter adapter;
    ArrayList<ItemModel> arrayList;
    public String tag;
    public TabFragment(String str) {
        tag = str;
        itemModel = new ArrayList<>();
        MainActivity.mainItem.course = str;
        Log.i("sub",MainActivity.mainItem.course);
        MainActivity.mainItem.search();
        Bundle b = new Bundle();
        b.putString("key", str);
        setArguments(b);
        try{
            while(MainActivity.mainItem.getArr==false){
                Thread.sleep(10);
            }
        }
        catch (InterruptedException e){}
        Log.i("size",String.valueOf(MainActivity.mainItem.arrList.size()));

        for(int i=0;i<MainActivity.mainItem.arrList.size();i++){
            itemModel.add(new ItemModel(MainActivity.mainItem.arrList.get(i).label,MainActivity.mainItem.arrList.get(i).category,MainActivity.mainItem.arrList.get(i).isRead));
//            Log.i("ooo",texts[i]);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new CustomAdapter(itemModel,getContext());
        adapter.setOnItemClickListener(new CustomAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                startActivity(new Intent(getContext(), ObjectActivity.class));
            }
        });
        recyclerView.setAdapter(adapter);
        return view;
    }
    class ItemModel{
        public String label;
        public String category;
        public boolean seen;
        ItemModel(String label,String category,Boolean seen){
            this.label = label;this.category = category;this.seen = seen;
        }
    }
    @Override
    public String toString(){
        return String.valueOf(itemModel.size());
    }

}
