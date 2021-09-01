package com.java.liziang;

import android.graphics.Color;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
            itemModel.add(new ItemModel(MainActivity.mainItem.arrList.get(i).label,MainActivity.mainItem.arrList.get(i).category));
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

        adapter = new CustomAdapter(itemModel);
        recyclerView.setAdapter(adapter);
        return view;
    }
    class ItemModel{
        public String label;
        public String category;
        ItemModel(String label,String category){
            this.label = label;this.category = category;
        }
    }
    class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.viewHolder>{
        ArrayList<ItemModel> arrayList;

        public CustomAdapter(ArrayList<ItemModel> arrayList) {
            this.arrayList = arrayList;
        }

        @Override
        public  viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_list, viewGroup, false);
            return new viewHolder(view);
        }
        @Override
        public  void onBindViewHolder(viewHolder viewHolder, int position) {
            viewHolder.label.setText(itemModel.get(position).label);
            viewHolder.category.setText(itemModel.get(position).category);
        }


        @Override
        public int getItemCount() {
            return itemModel.size();
        }

        public class viewHolder extends RecyclerView.ViewHolder {
            TextView label;
            TextView category;

            public viewHolder(View itemView) {
                super(itemView);
                category =(TextView) itemView.findViewById(R.id.category);
                label = (TextView) itemView.findViewById(R.id.label);
            }
        }
    }
}
