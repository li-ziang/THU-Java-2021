package com.java.liziang;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RelaAdapter extends RecyclerView.Adapter<RelaAdapter.viewHolder>{
    ArrayList<ObjectActivity.ItemRela> arrayList;
    Context con;
    //第一步 定义接口
    public interface OnItemClickListener {
        void onClick(int position);
    }

    private OnItemClickListener listener;

    //第二步， 写一个公共的方法
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public RelaAdapter(ArrayList<ObjectActivity.ItemRela> arrayList, Context con) {
        this.arrayList = arrayList;
        this.con =con;
    }

    @Override
    public  viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(con).inflate(R.layout.item_list_rela, viewGroup, false);
        return new viewHolder(view);
    }
    @Override
    public  void onBindViewHolder(viewHolder viewHolder, int position) {
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(viewHolder.getAdapterPosition());
                }
            }
        });
        viewHolder.label.setText(arrayList.get(position).label);
        viewHolder.content.setText(arrayList.get(position).content);
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView label;
        TextView content;
        public viewHolder(View itemView) {
            super(itemView);
            content =(TextView) itemView.findViewById(R.id.content_rela);
            label = (TextView) itemView.findViewById(R.id.label_rela);
        }
    }
}
