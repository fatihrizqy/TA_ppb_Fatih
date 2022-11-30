package com.example.TAppbFATIH.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.TAppbFATIH.MainActivity;
import com.example.TAppbFATIH.R;

public class SplashActivity extends AppCompatActivity {

    ImageView imgLogo;
    TextView txtSambut;

    @Override
    //Oncreate dibaca pertama kali
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //inisialisasi objek = koneksi objek
        imgLogo = (ImageView) findViewById(R.id.splash_imgLogo);
        txtSambut = (TextView) findViewById(R.id.txtSambutan);

        //sumber animasisplash
        Animation animationImg = AnimationUtils.loadAnimation(this,R.anim.fadeout);
        Animation animationTxt = AnimationUtils.loadAnimation(this,R.anim.fadeout);

        //implementasikan animasinya -- Mulai animasi
        imgLogo.startAnimation(animationImg);
        txtSambut.startAnimation(animationTxt);

        //Menjalankan Splash dalam beberapa detik
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //memanggil mainactivity
                Intent panggil = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(panggil);
                //Splash screen hilang
                finish();
            }
        },4000); //4detik

    }
}