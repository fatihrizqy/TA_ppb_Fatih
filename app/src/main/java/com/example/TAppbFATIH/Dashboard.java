package com.example.TAppbFATIH;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id)
        {
            case R.id.id_maps:
                String url = "https://goo.gl/maps/ksUmbwHiKPWXtnmo8";
                Intent bukamaps = new Intent(Intent.ACTION_VIEW);
                bukamaps.setData(Uri.parse(url));
                startActivity(bukamaps);
                break;
//                Intent intent = new Intent(Dashboard.this, MediumtripPay.class);
//                startActivity(intent);
//                Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();
//                break;

            case R.id.id_Update:
                Intent intent = new Intent(Dashboard.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(this, "Update", Toast.LENGTH_SHORT).show();
                break;

            case R.id.id_Callcenter:
                String nomor = "082243091453" ;
                Intent panggil = new Intent(Intent. ACTION_DIAL);
                panggil.setData(Uri. fromParts("tel",nomor,null));
                startActivity(panggil);
                break;

            case R.id.id_SmsCenter:
//                String url2 = "https://wa.me/+6282243091453";
//                Intent sms = new Intent(Intent.ACTION_VIEW);
//                sms.setData(Uri.parse(url2));
//                startActivity(sms);
//                break;

        }

        return super.onOptionsItemSelected(item);
    }

    public void klikgmbShort(View view) {
        //membuat intent agar form ke 2 dapat ditampilkan
        Intent i = new Intent(this, ShortTripPay.class);
        startActivity(i);

    }


    public void klikgmbMedium(View view) {
        Intent i = new Intent(this, MediumtripPay.class);
        startActivity(i);
    }

    public void klikgmbPremium(View view) {
        Intent i = new Intent(this, PremiumTripPay.class);
        startActivity(i);
    }

    public void klikgmbOneday(View view) {
        Intent i = new Intent(this, OnedayTripPay.class);
        startActivity(i);
    }

    public void klikgmbExclusive(View view) {
        Intent i = new Intent(this, ExclusiveTripPay.class);
        startActivity(i);
    }

    public void kliktxtShort(View view) {
        Intent i = new Intent(this, DescShorttrip.class);
        startActivity(i);
    }

    public void kliktxtMedium(View view) {
        Intent i = new Intent(this, DescMediumtrip.class);
        startActivity(i);
    }

    public void kliktxtPremium(View view) {
        Intent i = new Intent(this, DescPremiumTrip.class);
        startActivity(i);
    }

    public void kliktxtOneday(View view) {
        Intent i = new Intent(this, DescOnedayTrip.class);
        startActivity(i);
    }
}