package com.example.apptreasure_admin;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class testNotification extends AppCompatActivity {
    public static final String CHANNEL_ID = "default_channel_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_aff);
    }
}
