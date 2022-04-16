package com.busy.looping.seproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class ListViewAdapter extends ArrayAdapter<String> {
    private final Activity mContext;
    private final String [] titles;
    private final int [] imgResource;


    public ListViewAdapter(@NonNull Activity context, String [] titles, int [] imgResource) {
        super(context,R.layout.list_view_item, titles);
        this.mContext = context;
        this.titles = titles;
        this.imgResource = imgResource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater= mContext.getLayoutInflater();
        @SuppressLint("ViewHolder") View rowView= inflater.inflate(R.layout.list_view_item, parent,false);

        TextView textView = rowView.findViewById(R.id.txt);
        ImageView imageView = rowView.findViewById(R.id.img);

        textView.setText(titles[position]);
        imageView.setImageResource(imgResource[position]);
        Toast.makeText(mContext, "here", Toast.LENGTH_SHORT).show();
        return rowView;
    }
}
