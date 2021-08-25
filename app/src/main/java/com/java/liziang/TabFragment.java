package com.java.liziang;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class TabFragment extends Fragment {
    public TabFragment(String str) {
        Bundle b = new Bundle();
        b.putString("key", str);
        setArguments(b);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab, container, false);
        TextView textView = view.findViewById(R.id.text1);
        textView.setText(getArguments().getString("key"));
        textView.setBackgroundColor(Color.rgb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
        return view;
    }
}
