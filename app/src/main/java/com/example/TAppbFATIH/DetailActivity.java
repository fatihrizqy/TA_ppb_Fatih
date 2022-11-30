package com.example.TAppbFATIH;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {
    TextView txtNamaPaket,txtHarga,txtDeskripsi;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        txtDeskripsi = findViewById(R.id.txtDeskripsi);
        txtNamaPaket = findViewById(R.id.txtNamaPaket);
        txtHarga = findViewById(R.id.txtHarga);
        btnLogin = findViewById(R.id.btnLogin);
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        int harga = Integer.parseInt(getIntent().getStringExtra("harga"));
        txtNamaPaket.setText("Nama Paket : "+getIntent().getStringExtra("namaPaket"));
        txtHarga.setText("Harga Paket : "+formatRupiah.format((double) harga)+",-" );
        txtDeskripsi.setText(getIntent().getStringExtra("deskripsi"));
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DetailActivity.this,FormBayar.class)
                        .putExtra("namaPaket",""+getIntent().getStringExtra("namaPaket"))
                        .putExtra("idPaket",""+getIntent().getStringExtra("deskripsi"))
                        .putExtra("harga",""+getIntent().getStringExtra("harga")));
            }
        });
    }
}