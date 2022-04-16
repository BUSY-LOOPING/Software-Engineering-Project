package com.busy.looping.seproject.CallBacks;

import androidx.annotation.NonNull;

import com.busy.looping.seproject.models.EventModel;

public interface DeleteEventCallBack {
    void deleted(@NonNull EventModel eventModel);
}
