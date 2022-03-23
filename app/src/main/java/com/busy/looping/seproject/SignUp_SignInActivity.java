package com.busy.looping.seproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.busy.looping.seproject.databinding.ActivitySignUpBinding;
import com.google.android.material.checkbox.MaterialCheckBox;

public class SignUp_SignInActivity extends AppCompatActivity {
    public static final String IS_LOGIN = "LoginActivityIsLogin";
    private ActivitySignUpBinding binding;
    private MaterialCheckBox organizerCheckBox, customerCheckBox;
    private boolean isCustomer = true;


    private boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        listeners();
    }

    private void listeners() {
        binding.backBtn.setOnClickListener(v -> {
            finish();
        });

        binding.signUpBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, SignUp_SignInActivity.class));
        });

        binding.customerTxt.setOnClickListener(v -> {
            customerClicked();
        });

        customerCheckBox.setOnClickListener(v -> {
            customerClicked();

        });

        binding.organizerTxt.setOnClickListener(v -> {
            organizerClicked();
        });

        organizerCheckBox.setOnClickListener(v -> {
            organizerClicked();
        });
    }

    private void customerClicked() {
        isCustomer = true;
        customerCheckBox.setChecked(true);
        organizerCheckBox.setChecked(false);
    }

    private void organizerClicked() {
        isCustomer = false;
        organizerCheckBox.setChecked(true);
        customerCheckBox.setChecked(false);
    }

    private void init() {
        isLogin = getIntent().getBooleanExtra(IS_LOGIN, false);
        customerCheckBox = binding.customerCheckBox;
        organizerCheckBox = binding.organizerCheckBox;
        {
            ConstraintLayout root = binding.getRoot();
            if (isLogin) {
                for (int i = 0; i < root.getChildCount(); i++) {
                    if (root.getChildAt(i).getContentDescription() != null && root.getChildAt(i).getContentDescription().equals(getString(R.string.in_sign_up))) {
                        root.getChildAt(i).setVisibility(View.GONE);
                    }
                }
                binding.signupTxt.setText("Sign in");
            } else {
                binding.t1.setVisibility(View.GONE);
                binding.signUpBtn.setVisibility(View.GONE);
            }
        }

    }
}