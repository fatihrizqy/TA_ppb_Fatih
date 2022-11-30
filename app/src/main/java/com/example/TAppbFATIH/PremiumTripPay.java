package com.example.TAppbFATIH;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PremiumTripPay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium_trip_pay);
    }

    public void klikBayarPremium(View view) {
        Intent intent = new Intent(PremiumTripPay.this, Qris.class);
        startActivity(intent);
    }
}