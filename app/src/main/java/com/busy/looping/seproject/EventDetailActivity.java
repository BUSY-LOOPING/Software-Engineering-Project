package com.busy.looping.seproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.busy.looping.seproject.databinding.ActivityEventDetailBinding;
import com.busy.looping.seproject.models.BookingModel;
import com.busy.looping.seproject.models.EventModel;
import com.busy.looping.seproject.models.UserModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class EventDetailActivity extends AppCompatActivity {
    private ImageView img;
    private MaterialButton button;
    private EventModel eventModel;
    private UserModel signedInUser;
    private BookingModel bookingModel = null;
    private NumberPicker numberPicker;
    private ActivityEventDetailBinding binding;
    private double priceAllTickets;
    private int noTickets;
    private boolean isOrganizer = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        // inside your activity (if you did not enable transitions in your theme)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_event_detail);

        init();
        listeners();
//        setContentView(R.layout.activity_event_detail);
    }

    private void listeners() {
        button.setOnClickListener(v -> {
            if (isOrganizer) {
                Snackbar.make(binding.getRoot(), "Cancel event and refund all payments?", Snackbar.LENGTH_LONG).
                        setAction("Yes", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent();
                                intent.putExtra("cancelledEvent", eventModel);
                                setResult(RESULT_OK, intent);
                                Toast.makeText(EventDetailActivity.this, "Event Cancelled", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }).show();
            } else {
                if (signedInUser == null) {
                    Toast.makeText(this, "Sign in to book event", Toast.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(binding.getRoot(), "Pay amount \u20A8 " + priceAllTickets + " ?", Snackbar.LENGTH_LONG).
                            setAction("Pay", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    bookingModel = bookEvent(eventModel, signedInUser);
                                    if (bookingModel == null) {
                                        Toast.makeText(EventDetailActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Intent intent = new Intent(EventDetailActivity.this, BillActivity.class);
                                        intent.putExtra("event", eventModel);
                                        intent.putExtra("booking", bookingModel);
                                        startActivity(intent);
                                    }
                                }
                            }).show();
                }
            }
        });


        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                noTickets = newVal;
                priceAllTickets = (Double.parseDouble(eventModel.getPrice()) * newVal);
                binding.priceTxt.setText("\u20B9 " + priceAllTickets);
            }
        });
    }

    private void init() {
        img = binding.img;
        numberPicker = binding.noTicketsPicker;
        signedInUser = LoginDatabase.getInstance(this).getCurrentSignedInUser();
        eventModel = (EventModel) getIntent().getSerializableExtra("event");
        isOrganizer = getIntent().getBooleanExtra("isOrganizer", false);
        button = binding.bookBtn;
        bind(BR.eventVar, eventModel);
        priceAllTickets = Double.parseDouble(eventModel.getPrice());
        noTickets = 1;
        if (eventModel.getPic_path() != null) {
            Glide.with(this)
                    .load(eventModel.getPic_path())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.color.white)
                    .into(img);
        }
        setSupportActionBar(binding.toolBar);
        binding.toolBar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
        {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            @SuppressLint("SimpleDateFormat") SimpleDateFormat outputFormat = new SimpleDateFormat("E dd MMMM yyyy");
            try {
                Date date = inputFormat.parse(eventModel.getDate());
                if (date != null) {
                    String outputText = outputFormat.format(date);
                    binding.dateTxt.setText(outputText);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        int noSeatsAvailable = Integer.parseInt(eventModel.getNo_seats());
        if (noSeatsAvailable < 1) {
            binding.fillingFast.setText("No Tickets Available");
            binding.availableTxt.setText("Available  0" );
            numberPicker.setMaxValue(1);
            if (!isOrganizer) {
                numberPicker.setEnabled(false);
                button.setEnabled(false);
                button.setAlpha(0.5f);
            }
        } else {
            binding.priceTxt.setText("\u20B9 " + priceAllTickets);
            numberPicker.setMaxValue(noSeatsAvailable);
        }

        numberPicker.setMinValue(1);

        if (isOrganizer) {
            binding.priceTxt.setVisibility(View.GONE);
            binding.onwardsTxt.setVisibility(View.GONE);
            binding.fillingFast.setVisibility(View.GONE);
            binding.fillingFast.setVisibility(View.GONE);
            button.setText("Cancel Event");
        }
        {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm:ss");
            @SuppressLint("SimpleDateFormat") SimpleDateFormat outputFormat = new SimpleDateFormat("hh : mm a");
            try {
                Date date = inputFormat.parse(eventModel.getTime());
                if (date != null) {
                    String outputText = outputFormat.format(date);
                    binding.timeTxt.setText(outputText);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void bind(int id, Object obj) {
        binding.setVariable(id, obj);
        binding.executePendingBindings();
    }

    @Nullable
    private BookingModel bookEvent(@NonNull EventModel eventModel, @NonNull UserModel signedInUser) {
        BookingModel bookingModel = null;
        Random random = new Random();
        double priceTicket = Double.parseDouble(eventModel.getPrice());
        double taxMax = 15, taxMin = 1;
        if (priceTicket > 500) {
            taxMax = 20;
            taxMin = 10;
        }else if (priceTicket > 200) {
            taxMin = 10;
        }
        double discountPercent = 0.0 + (5 - 0.0) * random.nextDouble();
        double taxPercent = taxMin + (taxMax - taxMin) * random.nextDouble();
        double discount = (priceTicket * discountPercent) / 100;
        double tax = (priceTicket * taxPercent) / 100;
        String bookingId = "";
//        RandomString randomString = new RandomString();
//        String easy = RandomString.digits + "ACEFGHJKLMNPQRUVWXYabcdefhijkprstuvwx";
//        RandomString tickets = new RandomString(23, new SecureRandom(), easy);
        bookingId = randomString(10);
        double amountPayed = priceAllTickets + tax - discount;
        if (LoginDatabase.getInstance(this).insertInBooking(
                bookingId,
                amountPayed,
                noTickets,
                discount,
                tax,
                Integer.parseInt(eventModel.getEventId()),
                Integer.parseInt(signedInUser.getId()),
                Integer.parseInt(eventModel.getNo_seats()))) {
            bookingModel = LoginDatabase.getInstance(this).getLastBooking();
            Intent intent = new Intent();
            intent.putExtra("updatedEvent", eventModel);
            setResult(RESULT_OK, intent);
            int newTickets = (Integer.parseInt(eventModel.getNo_seats()) - noTickets);
            eventModel.setNo_seats("" + newTickets);
            if (newTickets < 1) {
                binding.fillingFast.setText("No Tickets Available");
                binding.availableTxt.setText("Available  0" );
                numberPicker.setMaxValue(1);
                if (!isOrganizer) {
                    numberPicker.setEnabled(false);
                    button.setEnabled(false);
                    button.setAlpha(0.5f);
                }
            } else {
                numberPicker.setMaxValue(newTickets);
            }
            binding.setEventVar(eventModel);
        }
        return bookingModel;
    }

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    private String randomString(int len){
        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }
}