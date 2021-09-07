package com.java.liziang;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class LinkedAdapter extends RecyclerView.Adapter<LinkedAdapter.viewHolder>{
    List<LinkedModel> arrayList = new ArrayList<>();
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

    public LinkedAdapter(List<LinkedModel> arrayList, Context con) {
        this.arrayList = arrayList;
        this.con =con;
    }

    @Override
    public  viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(con).inflate(R.layout.linked_list, viewGroup, false);
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
        viewHolder.course.setText(arrayList.get(position).course);
        viewHolder.pos = viewHolder.getAdapterPosition();
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView label;
        TextView course;
        int pos;
        public viewHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.label);
            course = (TextView) itemView.findViewById(R.id.course);
        }
    }
}