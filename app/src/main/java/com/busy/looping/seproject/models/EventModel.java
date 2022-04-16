package com.busy.looping.seproject.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.Objects;

public class EventModel implements Serializable {
    @NonNull
    private String eventName;

    @NonNull
    private String eventId;

    @NonNull
    private String price;

    @NonNull
    private String no_seats;

    @Nullable
    private String small_description;

    @Nullable
    private String full_description;

    @NonNull
    private String time;

    @NonNull
    private String date;

    @NonNull
    private String venue;

    @Nullable
    private String pic_path;


    @NonNull String eventType;
    private boolean isConfirmed;

    public EventModel(@NonNull String eventName, @NonNull String eventId, @NonNull String price, @NonNull String no_seats, @Nullable String small_description, @Nullable String full_description, @NonNull String time, @NonNull String date, @NonNull String venue,@Nullable String pic_path, boolean isConfirmed, @NonNull String eventType) {
        this.eventName = eventName;
        this.eventId = eventId;
        this.price = price;
        this.no_seats = no_seats;
        this.small_description = small_description;
        this.full_description = full_description;
        this.time = time;
        this.date = date;
        this.venue = venue;
        this.pic_path = pic_path;
        this.isConfirmed = isConfirmed;
        this.eventType = eventType;
    }

    @NonNull
    public String getEventId() {
        return eventId;
    }

    public void setEventId(@NonNull String eventId) {
        this.eventId = eventId;
    }

    @NonNull
    public String getPrice() {
        return price;
    }

    public void setPrice(@NonNull String price) {
        this.price = price;
    }

    @NonNull
    public String getNo_seats() {
        return no_seats;
    }

    public void setNo_seats(@NonNull String no_seats) {
        this.no_seats = no_seats;
    }

    @Nullable
    public String getSmall_description() {
        return small_description;
    }

    public void setSmall_description(@Nullable String small_description) {
        this.small_description = small_description;
    }

    @Nullable
    public String getFull_description() {
        return full_description;
    }

    public void setFull_description(@Nullable String full_description) {
        this.full_description = full_description;
    }

    @NonNull
    public String getTime() {
        return time;
    }

    public void setTime(@NonNull String time) {
        this.time = time;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    public void setDate(@NonNull String date) {
        this.date = date;
    }

    @NonNull
    public String getVenue() {
        return venue;
    }

    public void setVenue(@NonNull String venue) {
        this.venue = venue;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }

    @Nullable
    public String getPic_path() {
        return pic_path;
    }

    public void setPic_path(@Nullable String pic_path) {
        this.pic_path = pic_path;
    }

    @NonNull
    public String getEventName() {
        return eventName;
    }

    public void setEventName(@NonNull String eventName) {
        this.eventName = eventName;
    }

    @NonNull
    public String getEventType() {
        return eventType;
    }

    public void setEventType(@NonNull String eventType) {
        this.eventType = eventType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventModel that = (EventModel) o;
        return getEventId().equals(that.getEventId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEventId());
    }
}
