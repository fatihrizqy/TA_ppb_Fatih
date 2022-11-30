package com.example.TAppbFATIH;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ExclusiveTripPay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exclusive_trip_pay);
    }

    public void klikBayarExclusive(View view) {
        Intent intent = new Intent(ExclusiveTripPay.this, Qris.class);
        startActivity(intent);
    }
}