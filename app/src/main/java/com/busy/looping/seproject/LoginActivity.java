package com.busy.looping.seproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.busy.looping.seproject.databinding.ActivityLoginBinding;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    private ViewPager viewPager;
    private WormDotsIndicator dotsIndicator;
    private ImageViewPagerAdapter adapter;
    private Handler handler;
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            current ++;
            if (current > images.length - 1) {
                current = 0;
            }
            viewPager.setCurrentItem(current, true);
            handler.postDelayed(this, 3500);
        }
    };

    int current = 0;


    int [] images = {R.drawable.img, R.drawable.img_1, R.drawable.img_2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        listeners();
    }

    private void listeners() {
        binding.continueWithEmail.setOnClickListener(v -> {
            Intent intent =new Intent(this, SignUp_SignInActivity.class);
            intent.putExtra(SignUp_SignInActivity.IS_LOGIN, true);
            startActivity(intent);
        });

        binding.skip.setOnClickListener(v -> {
            finish();
        });
    }

    private void init() {
        adapter = new ImageViewPagerAdapter(this, images);
        handler = new Handler();
        viewPager = binding.viewPager;
        viewPager.setAdapter(adapter);
        dotsIndicator = binding.dotIndicator;
        dotsIndicator.setViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                current = position;
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 3000);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        handler.postDelayed(runnable, 3500);
    }
}