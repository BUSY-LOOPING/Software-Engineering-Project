package com.busy.looping.seproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.busy.looping.seproject.models.EventModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecyclerView_WithEventType_Adapter extends RecyclerView.Adapter<RecyclerView_WithEventType_Adapter.GeneralRecyclerViewAdapterViewHolder> {
    private ArrayList<EventModel> list;
    private Context mContext;

    public RecyclerView_WithEventType_Adapter(@NonNull ArrayList<EventModel> list, @NonNull Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public GeneralRecyclerViewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.event_item_with_event_type, parent, false);
        return new GeneralRecyclerViewAdapterViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull GeneralRecyclerViewAdapterViewHolder holder, int position) {
        holder.pic.setImageResource(0);
        if (list.get(position).getPic_path() != null) {
            Glide.with(mContext)
                    .load(list.get(position).getPic_path())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.color.black)
                    .into(holder.pic);
        }
        @SuppressLint("SimpleDateFormat") SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat outputFormat = new SimpleDateFormat("E,dd MMMM");
        try {
            Date date = inputFormat.parse(list.get(position).getDate());
            if (date != null) {
                String outputText = outputFormat.format(date);
                holder.dayDateTxt.setText(outputText);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.eventNameTxt.setText(list.get(position).getEventName());
        holder.eventTypeTxt.setText(list.get(position).getEventType());
        holder.itemView.setOnClickListener(v -> {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) mContext, holder.pic, "transition");
            Intent intent = new Intent(mContext, EventDetailActivity.class);
            intent.putExtra("event", list.get(position));
            ((Activity) mContext).startActivityForResult(intent, OrganizerActivity.EVENT_DETAIL_ACT,options.toBundle());
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class GeneralRecyclerViewAdapterViewHolder extends RecyclerView.ViewHolder {
        private final ImageView pic;
        private final TextView dayDateTxt, eventNameTxt, eventTypeTxt;
        public GeneralRecyclerViewAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.img);
            dayDateTxt = itemView.findViewById(R.id.day_date_txt);
            eventNameTxt = itemView.findViewById(R.id.eventNameTxt);
            eventTypeTxt = itemView.findViewById(R.id.eventTypeTxt);
        }
    }
}
