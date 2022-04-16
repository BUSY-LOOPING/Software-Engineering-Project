package com.busy.looping.seproject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.busy.looping.seproject.databinding.UserProfileDialogBinding;
import com.busy.looping.seproject.models.UserModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class UserProfileBottomSheet extends BottomSheetDialogFragment {
    private Context context;
    private Dialog dialog;
    private Activity activity;
    private UserProfileDialogBinding binding;
    private UserModel currentUser;
    private TextView signInBtn, logOutBtn, saveBtn;
    private EditText userNameEt, phoneNoEt, addressEt;

    public UserProfileBottomSheet() {

    }

    public UserProfileBottomSheet (Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.user_profile_dialog, container, false);
        currentUser = LoginDatabase.getInstance(context).getCurrentSignedInUser();
        bind(BR.userModel, currentUser);
        init();
        listeners();
        return binding.getRoot();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = super.onCreateDialog(savedInstanceState);
        return dialog;
    }

    private void init() {
        signInBtn = binding.signInBtn;
        logOutBtn = binding.logOutBtn;
        saveBtn = binding.saveBtn;
        userNameEt = binding.userNameEt;
        phoneNoEt = binding.phoneNoEt;
        addressEt = binding.addressEt;
        binding.emailEt.setEnabled(false);
    }

    private void listeners() {
        signInBtn.setOnClickListener(v -> {
            dialog.cancel();
            startActivity(new Intent(activity, LoginActivity.class));
            activity.finish();
        });

        logOutBtn.setOnClickListener(v -> {
            dialog.cancel();
            LoginDatabase.getInstance(context).logOut(currentUser.getEmail());
            currentUser = null;
            binding.setUserModel(null);
            startActivity(new Intent(activity, LoginActivity.class));
            activity.finish();
        });

        saveBtn.setOnClickListener(v -> {
            Toast.makeText(context, "Updated info", Toast.LENGTH_SHORT).show();
            LoginDatabase.getInstance(context).updateUser(currentUser.getEmail(),
                    userNameEt.getText().toString().trim(),
                    phoneNoEt.getText().toString().trim(),
                    addressEt.getText().toString().trim());
        });
    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }

    public void bind(int id, Object obj) {
        binding.setVariable(id, obj);
        binding.executePendingBindings();
    }
}
