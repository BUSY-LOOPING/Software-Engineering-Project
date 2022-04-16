package com.busy.looping.seproject;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.busy.looping.seproject.databinding.ActivityBillBinding;
import com.busy.looping.seproject.models.BookingModel;
import com.busy.looping.seproject.models.EventModel;

import java.text.DecimalFormat;
import java.util.Locale;

public class BillActivity extends AppCompatActivity {
    private ActivityBillBinding binding;
    private EventModel eventModel;
    private BookingModel bookingModel;
    private TextView totalCostWithoutTax_DiscountTxt, discountTxt, taxTxt, totalAmountTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bill);
        init();
        set();
    }

    private void set() {
        String pattern = "##,##,###.##";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
//        String str = "\u20B9 " + String.format(Locale.US, "%.2f",Double.parseDouble(eventModel.getPrice()) * Integer.parseInt(bookingModel.getNoTickets()));
        String str = "\u20B9 " + decimalFormat.format(Double.parseDouble(eventModel.getPrice()) * Integer.parseInt(bookingModel.getNoTickets()));
        totalCostWithoutTax_DiscountTxt.setText(str);
        String discountStr = "- \u20B9 ";
        String taxStr = "\u20B9 ";
        String totalAmountStr = "\u20B9 " + decimalFormat.format(Double.parseDouble(bookingModel.getAmountPayed()));


        if (bookingModel.getTax() != null)
            taxStr += decimalFormat.format(Double.parseDouble(bookingModel.getTax()));
        else
            taxStr += "0";

        if (bookingModel.getDiscount() != null)
            discountStr += decimalFormat.format(Double.parseDouble(bookingModel.getDiscount()));
        else
            discountStr += "0";

        discountTxt.setText(discountStr);
        taxTxt.setText(taxStr);

        totalAmountTxt.setText(totalAmountStr);
    }


    private void init() {
        eventModel = (EventModel) getIntent().getSerializableExtra("event");
        bookingModel = (BookingModel) getIntent().getSerializableExtra("booking");
        bind(BR.eventModel, eventModel);
        bind(BR.bookingModel, bookingModel);
        setSupportActionBar(binding.toolBar);
        totalCostWithoutTax_DiscountTxt = binding.totalCostAllTickets;
        discountTxt = binding.discountTxt;
        taxTxt = binding.taxTxt;
        totalAmountTxt = binding.totalAmountTxt;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    public void bind(int id, Object obj) {
        binding.setVariable(id, obj);
        binding.executePendingBindings();
    }
}