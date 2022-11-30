package com.example.TAppbFATIH;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ShortTripPay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_trip_pay);
    }

    public void klikBayarshort(View view) {
        Intent intent = new Intent(ShortTripPay.this, Qris.class);
        startActivity(intent);
    }
}