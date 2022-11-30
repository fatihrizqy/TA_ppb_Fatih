package com.example.TAppbFATIH;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class OnedayTripPay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oneday_trip_pay);
    }

    public void klikBayarOneday(View view) {
        Intent intent = new Intent(OnedayTripPay.this, Qris.class);
        startActivity(intent);
    }
}