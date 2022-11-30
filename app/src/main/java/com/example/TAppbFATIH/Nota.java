package com.example.TAppbFATIH;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.TAppbFATIH.retrofit.APIClient;
import com.example.TAppbFATIH.retrofit.APIService;
import com.example.TAppbFATIH.retrofit.modelResponseData.GetDetailResponse;

import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Nota extends AppCompatActivity {
    TextView txtTime,txtPaket,txtTotalHarga,txtKembalian,txtNamaPemesan;
    LinearLayout scroll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota);
        txtKembalian = findViewById(R.id.txtKembalian);
        txtTime = findViewById(R.id.txtTime);
        txtPaket = findViewById(R.id.txtPaket);
        txtTotalHarga = findViewById(R.id.txtTotalHarga);
        scroll = findViewById(R.id.scroll);
        txtNamaPemesan = findViewById(R.id.txtNamaPemesan);
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        int kembalian = Integer.parseInt(getIntent().getStringExtra("kembalian"));
        int harga = Integer.parseInt(getIntent().getStringExtra("totalHarga"));
//        getDetail(getIntent().getStringExtra("id"));
        txtPaket.setText(getIntent().getStringExtra("namaPaket"));
        txtTime.setText(getIntent().getStringExtra("waktu"));
        txtKembalian.setText(formatRupiah.format((double) kembalian)+",-");
        txtTotalHarga.setText(formatRupiah.format((double) harga)+",-");
        txtNamaPemesan.setText(getIntent().getStringExtra("namaPemesan"));

        findViewById(R.id.imgMore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(Nota.this, findViewById(R.id.imgMore));

                // Inflating popup menu from popup_menu.xml file
                popupMenu.getMenuInflater().inflate(R.menu.payment_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        // Toast message on menu item clicked
                        int id = menuItem.getItemId();
                        switch (id)
                        {
                            case R.id.idShare:
//                                String invitationLink = mInvitationUrl[0].toString();
                                final String msg = "Nota pembelian";

                                //Bitmap b = ScreenshotUtils.getScreenShot(rootcontent);
                                Bitmap bitmap = getBitmapFromView(scroll, scroll.getChildAt(0).getHeight(), scroll.getChildAt(0).getWidth());
                                Bitmap bigImage = BitmapFactory.decodeResource(getResources(), R.drawable.ic_wonderful);

                                Bitmap mergedImages = createSingleImageFromMultipleImages(bitmap, bigImage);
                                String imgBitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), mergedImages, "title", null);
                                final Uri imgBitmapUri = Uri.parse(imgBitmapPath);

                                Handler handler = new Handler();
                                Runnable myRunnable = new Runnable() {
                                    public void run() {
                                        // do something
                                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                        shareIntent.setType("*/*");
                                        shareIntent.putExtra(Intent.EXTRA_STREAM, imgBitmapUri);
                                        shareIntent.putExtra(Intent.EXTRA_TEXT, msg);
                                        startActivity(Intent.createChooser(shareIntent, "Share Via"));
                                    }
                                };
                                handler.postDelayed(myRunnable, 100);
                                break;

                        }
                        return true;
                    }
                });
                // Showing the popup menu
                popupMenu.show();
            }
        });

    }

    public void  print(){
        
    }

    private Bitmap getBitmapFromView(View view, int height, int width) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return bitmap;
    }

    private Bitmap createSingleImageFromMultipleImages(Bitmap firstImage, Bitmap secondImage) {

        /*Bitmap result = Bitmap.createBitmap(firstImage.getWidth(), firstImage.getHeight()+secondImage.getHeight(), firstImage.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawColor(getResources().getColor(R.color.white));
        canvas.drawBitmap(firstImage, 0f, secondImage.getHeight(), null);
        canvas.drawBitmap(secondImage, 0f, 0f, null);*/

        int width = firstImage.getWidth();
        int height = firstImage.getHeight() / 2;
        Bitmap combine = Bitmap.createBitmap(firstImage, 0, 0, width, height);
        Bitmap combine2 = Bitmap.createBitmap(firstImage, 0, firstImage.getHeight() / 2, width, height);

        Bitmap combine3 = Bitmap.createBitmap(firstImage.getWidth() + firstImage.getWidth() + 100, secondImage.getHeight(), firstImage.getConfig());
        Canvas canvasku = new Canvas(combine3);
        canvasku.drawColor(getResources().getColor(R.color.white));
        canvasku.drawBitmap(secondImage, 0f, 0f, null);

        Bitmap combine4 = Bitmap.createBitmap(firstImage.getWidth() + firstImage.getWidth() + 100, 5, firstImage.getConfig());
        Canvas canvasmu = new Canvas(combine4);
        canvasmu.drawColor(getResources().getColor(R.color.black));

        Bitmap result = Bitmap.createBitmap(20 + firstImage.getWidth() + firstImage.getWidth() + 60, 5 + height + combine3.getHeight() + 40, firstImage.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawColor(Color.parseColor("#f5f5f5"));
        canvas.drawBitmap(combine3, 0f, 0f, null);
        canvas.drawBitmap(combine4, 0f, combine3.getHeight(), null);
        canvas.drawBitmap(combine, 30, 20 + combine3.getHeight(), null);
        canvas.drawBitmap(combine2, width + 50, 20 + combine3.getHeight(), null);

        return result;
    }

    public void getDetail(String id){
        APIService service = APIClient.getClient().create(APIService.class);
        Call<GetDetailResponse> response = service.getDetail(id);
        response.enqueue(new Callback<GetDetailResponse>() {
            @Override
            public void onResponse(Call<GetDetailResponse> call, Response<GetDetailResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus()==1){
                        txtTime.setText(response.body().getData().get(0).getTanggal());
                        txtKembalian.setText(response.body().getData().get(0).getKembalian());
                        txtPaket.setText(response.body().getData().get(0).getNamaPaket());
                        txtTotalHarga.setText(response.body().getData().get(0).getHarga());
                    }else {
                        Toast.makeText(Nota.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(Nota.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetDetailResponse> call, Throwable t) {
                Toast.makeText(Nota.this,"error connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
}