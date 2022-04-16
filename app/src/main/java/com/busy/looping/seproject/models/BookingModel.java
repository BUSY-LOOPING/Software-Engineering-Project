package com.busy.looping.seproject.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.Objects;

public class BookingModel implements Serializable {

    @NonNull
    private String bookingId;

    @NonNull
    private String amountPayed;

    @NonNull
    private String noTickets;

    @Nullable
    private String discount;

    @Nullable
    private String tax;

    @NonNull
    private String eventId;

    @Nullable
    private String userId;

    public BookingModel(@NonNull String bookingId, @NonNull String price, @NonNull String noTickets, @Nullable String discount, @Nullable String tax, @NonNull String eventId, @Nullable String userId) {
        this.bookingId = bookingId;
        this.amountPayed = price;
        this.noTickets = noTickets;
        this.discount = discount;
        this.tax = tax;
        this.eventId = eventId;
        this.userId = userId;
    }

    @NonNull
    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(@NonNull String bookingId) {
        this.bookingId = bookingId;
    }

    @NonNull
    public String getAmountPayed() {
        return amountPayed;
    }

    public void setAmountPayed(@NonNull String amountPayed) {
        this.amountPayed = amountPayed;
    }

    @NonNull
    public String getNoTickets() {
        return noTickets;
    }

    public void setNoTickets(@NonNull String noTickets) {
        this.noTickets = noTickets;
    }

    @Nullable
    public String getDiscount() {
        return discount;
    }

    public void setDiscount(@Nullable String discount) {
        this.discount = discount;
    }

    @Nullable
    public String getTax() {
        return tax;
    }

    public void setTax(@Nullable String tax) {
        this.tax = tax;
    }

    @NonNull
    public String getEventId() {
        return eventId;
    }

    public void setEventId(@NonNull String eventId) {
        this.eventId = eventId;
    }

    @Nullable
    public String getUserId() {
        return userId;
    }

    public void setUserId(@Nullable String userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingModel that = (BookingModel) o;
        return bookingId.equals(that.bookingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingId);
    }
}
