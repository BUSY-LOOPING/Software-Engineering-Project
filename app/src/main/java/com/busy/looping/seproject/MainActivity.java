package com.busy.looping.seproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoginDatabase loginDatabase = LoginDatabase.getInstance(this);
        loginDatabase.getWritableDatabase();
        loginDatabase.close();

        startActivityForResult(new Intent(this, LoginActivity.class), 1);
        findViewById(R.id.txt1).setOnClickListener(v -> {
            if (((TextView)v).getText().equals("Book Events")) {
                ((TextView)v).setText("Organize Events");
            } else {
                ((TextView)v).setText("Book Events");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.main_status_bar));
    }
}