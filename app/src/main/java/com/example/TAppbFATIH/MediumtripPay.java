package com.example.TAppbFATIH;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MediumtripPay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mediumtrip_pay);
    }

    public void klikBayarMedium(View view) {
        Intent intent = new Intent(MediumtripPay.this, Qris.class);
        startActivity(intent);
    }
}