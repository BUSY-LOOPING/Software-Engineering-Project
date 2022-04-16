package com.busy.looping.seproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.busy.looping.seproject.models.EventModel;
import com.busy.looping.seproject.models.UserModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ImageView icon;
    private RecyclerView popularEventsRecyclerView;
    private RecyclerView_WithEventType_Adapter popularEventsAdapter;
    private ArrayList<EventModel> popularEventsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.main_status_bar));

        init();
        setAdapter();
        listeners();
//        startActivityForResult(new Intent(this, LoginActivity.class), 1);



    }

    private void setAdapter() {
        popularEventsRecyclerView.setHasFixedSize(false);
        popularEventsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        popularEventsRecyclerView.setAdapter(popularEventsAdapter);
    }

    private void listeners() {
        icon.setOnClickListener(v -> {
            UserProfileBottomSheet bottomSheet = new UserProfileBottomSheet(this);
            bottomSheet.show(getSupportFragmentManager(), "");
        });
    }

    private void init() {
        popularEventsList = LoginDatabase.getInstance(this).getAllEvents();
        icon = findViewById(R.id.icon);
        popularEventsRecyclerView = findViewById(R.id.popularEventsRecyclerView);
        popularEventsAdapter = new RecyclerView_WithEventType_Adapter(popularEventsList, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UserModel currentSignedInUser = LoginDatabase.getInstance(this).getCurrentSignedInUser();
        if (currentSignedInUser != null){
            if (currentSignedInUser.getRole().equals("organizer")) {
                Intent intent = new Intent(this, OrganizerActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OrganizerActivity.EVENT_DETAIL_ACT) {
            if (data != null) {
                EventModel eventModel = (EventModel) data.getSerializableExtra("updatedEvent");
                if (eventModel != null) {
                    int index = popularEventsList.indexOf(eventModel);
                    if (index > -1) {
                        popularEventsList.set(index, eventModel);
                    }
                }
            }
        }
    }
}