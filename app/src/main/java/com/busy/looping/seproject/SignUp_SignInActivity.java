package com.busy.looping.seproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.busy.looping.seproject.databinding.ActivitySignUpBinding;
import com.google.android.material.checkbox.MaterialCheckBox;

public class SignUp_SignInActivity extends AppCompatActivity {
    public static final int LOGIN_SUCCESSFUL = 100;
    public static final int LOGIN_UNSUCCESSFUL = 101;
    public static final int SIGNUP_SUCCESSFUL = 200;
    public static final int SIGNUP_UNSUCCESSFUL = 201;
    public static final String IS_LOGIN = "LoginActivityIsLogin";
    private ActivitySignUpBinding binding;
    private MaterialCheckBox organizerCheckBox, customerCheckBox;
    private EditText emailEt, passwordEt;
    private boolean isCustomer = true;

    private String email, password;


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

        emailEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    email = charSequence.toString();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        passwordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    password = charSequence.toString();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

//        passwordEt.setOnTouchListener((v, event) -> {
//            final int DRAWABLE_LEFT = 0;
//            final int DRAWABLE_TOP = 1;
//            final int DRAWABLE_RIGHT = 2;
//            final int DRAWABLE_BOTTOM = 3;
//
//            if(event.getAction() == MotionEvent.ACTION_UP) {
//                if(event.getRawX() >= (passwordEt.getRight() - passwordEt.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
//                    // your action here
//                    return true;
//                }
//            }
//            return false;
//        });

        binding.continueBtn.setOnClickListener(v -> {
            if (isLogin) {
                if (validateFields()) {
                    if (signInToDB(this, email, password)) {
                        Toast.makeText(this, "Sign in successful!", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = getSharedPreferences("signed_in", MODE_PRIVATE).edit();
                        editor.putBoolean("signed_in", true);
                        editor.apply();
                        finish();
                    } else {
                        Toast.makeText(this, "Sign in failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                if (validateFields())
                    if (signUpToDB(this, email, password, isCustomer)) {
                        Toast.makeText(this, "Sign up successful!", Toast.LENGTH_SHORT).show();
                        setResult(SIGNUP_SUCCESSFUL);
                        finish();
                    } else {
                        Toast.makeText(this, "Sign up failed!", Toast.LENGTH_SHORT).show();
                    }
            }
        });
    }

    public static boolean signInToDB(Context context, String email, String password) {
        boolean res = LoginDatabase.getInstance(context).containsEmailPassword(email, password);
        if (res) {
            LoginDatabase.getInstance(context).setCurrentSignedIn(email);
        }
        return res;
    }

    private boolean validateFields() {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEt.setError("Provide a valid email");
            return false;
        } else if (password.length() < 6) {
            passwordEt.setError("Password must be atleast 8 digit long");
            return false;
        }
        return true;
    }

    public static boolean signUpToDB(Context context, String email, String password, boolean isCustomer) {
        return LoginDatabase.getInstance(context).insertData(email, password, isCustomer ? "user" : "organizer");
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
        email = password = "";
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
        emailEt = binding.emailEt;
        passwordEt = binding.passwordEt;
    }
}