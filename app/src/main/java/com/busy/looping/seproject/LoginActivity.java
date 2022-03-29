package com.busy.looping.seproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.busy.looping.seproject.databinding.ActivityLoginBinding;
import com.google.android.material.textfield.TextInputEditText;
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

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("signed_in", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("signed_in", false)) {
            finish();
        }

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

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "reqCode = " + requestCode + " res= " +resultCode, Toast.LENGTH_SHORT).show();
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                if (data != null) {
                    if (data.getIntExtra("state", -1) == SignUp_SignInActivity.LOGIN_SUCCESSFUL) {
                        finish();
                    }
                }
            }
        }
    }

}