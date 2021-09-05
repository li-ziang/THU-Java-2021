package com.java.liziang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    //查询结果列表
    public List<String> list = new ArrayList<String>();
    public ListAdapter(Context context, ArrayList<String> arrayList) {
        mInflater = LayoutInflater.from(context);
        //查询匹配
        for(String ele:arrayList){
            list.add(ele);
        }
    }
    public int getCount() {
        return list.size();
    }
    public Object getItem(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView,ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.history_item, null);
        TextView title = (TextView) convertView.findViewById(R.id.history);
        title.setText(list.get(position));
        return convertView;
    }
}
