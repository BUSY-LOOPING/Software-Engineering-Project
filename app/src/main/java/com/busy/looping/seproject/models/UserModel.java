package com.busy.looping.seproject.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;

public class UserModel implements Serializable {
    @NonNull
    private String id;

    @Nullable
    private String name;

    @NonNull
    private String email;

    @NonNull
    private String hashedPassword;

    @NonNull
    private String role;

    @Nullable
    private String address;

    @Nullable
    private String phoneNo;

    public UserModel(@NonNull String id,@Nullable String name, @NonNull String email, @NonNull String hashedPassword, @NonNull String role, @Nullable String address, @Nullable String phoneNo) {
        this.id= id;
        this.name = name;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.role = role;
        this.address = address;
        this.phoneNo = phoneNo;
    }

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    @NonNull
    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(@NonNull String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    @NonNull
    public String getRole() {
        return role;
    }

    public void setRole(@NonNull String role) {
        this.role = role;
    }

    @Nullable
    public String getAddress() {
        return address;
    }

    public void setAddress(@Nullable String address) {
        this.address = address;
    }

    @Nullable
    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(@Nullable String phoneNo) {
        this.phoneNo = phoneNo;
    }


    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }
}
