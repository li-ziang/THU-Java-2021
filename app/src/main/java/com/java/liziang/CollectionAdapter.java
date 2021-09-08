package com.java.liziang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.viewHolder> {
    List<CollectionInfo> arrayList = new ArrayList<>();
    Context con;

    // 第一步 定义接口
    public interface OnItemClickListener {
        void onClick(int position);
    }

    private OnItemClickListener listener;

    // 第二步， 写一个公共的方法
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public CollectionAdapter(List<CollectionInfo> arrayList, Context con) {
        this.arrayList = arrayList;
        this.con = con;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(con).inflate(R.layout.collect_list, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder viewHolder, int position) {
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(viewHolder.getAdapterPosition());
                }
            }
        });
        viewHolder.course.setText(arrayList.get(position).course);
        viewHolder.instanceName.setText(arrayList.get(position).intanceName);

        viewHolder.pos = viewHolder.getAdapterPosition();
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView course;
        TextView instanceName;
        int pos;

        public viewHolder(View itemView) {
            super(itemView);
            course = (TextView) itemView.findViewById(R.id.collect_course);
            instanceName = (TextView) itemView.findViewById(R.id.instanceName);
        }
    }
}