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
    String texts[]={"1","2","3","4"};
    CustomAdapter adapter;
    ArrayList<ItemModel> arrayList;
    public String tag;
    public TabFragment(String str) {
        tag = str;
        Bundle b = new Bundle();
        b.putString("key", str);
        setArguments(b);
        for(int i=0;i<texts.length;i++){
            texts[i] = tag+texts[i];
            Log.i("ooo",texts[i]);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        arrayList = new ArrayList<ItemModel>();
        for(int i=0;i<texts.length;i++){
            ItemModel itemModel = new ItemModel(texts[i]);
            arrayList.add(itemModel);
        }
        adapter = new CustomAdapter(arrayList);
        recyclerView.setAdapter(adapter);
        return view;
    }
    class ItemModel{
        String text;
        ItemModel(String text){
            this.text = text;
        }
        public String getText(){
            return text;
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
            viewHolder.text.setText(arrayList.get(position).getText());
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class viewHolder extends RecyclerView.ViewHolder {
            TextView text;

            public viewHolder(View itemView) {
                super(itemView);
                text = (TextView) itemView.findViewById(R.id.the_text);
            }
        }
    }
}
