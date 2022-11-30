package com.example.TAppbFATIH;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.TAppbFATIH.lokalDatabase.InitDatabase;
import com.example.TAppbFATIH.retrofit.APIClient;
import com.example.TAppbFATIH.retrofit.APIService;
import com.example.TAppbFATIH.retrofit.modelResponseData.StatusResponse;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahPaket extends AppCompatActivity {
    Button btnPickImage,btnSimpan;
    ImageView imgAvatar;
    EditText edNamaPaket,edHarga,edDeskripsi;
    Uri uriImage;
    String url;
    InitDatabase initDatabase;
    private ProgressDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_paket);

        mDialog = new ProgressDialog(this);
        btnPickImage = findViewById(R.id.btnPickImage);
        imgAvatar = findViewById(R.id.imgAvatar);
        edNamaPaket = findViewById(R.id.edNamaPaket);
        edDeskripsi = findViewById(R.id.edDeskripsi);
        edHarga = findViewById(R.id.edHarga);
        btnSimpan = findViewById(R.id.btnSimpan);
        initDatabase = new InitDatabase(TambahPaket.this);
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edNamaPaket.getText().toString().isEmpty()){
                    edNamaPaket.setError("Nama paket harus diisi");
                }else if (edDeskripsi.getText().toString().isEmpty()){
                    edDeskripsi.setError("Deskripsi harus diisi");
                }else if ((edHarga.getText().toString().isEmpty())){
                    edHarga.setError("Harga harus diisi");
                }else {
                    if (initDatabase.getGlobalVariableString("user","id").equals("1")){
                        editPaket();
                    }else {
                        tambahPaket();
                    }
                }
            }
        });

        btnPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
//                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setFixAspectRatio(true)
                        .start(TambahPaket.this);
            }
        });
        if (initDatabase.getGlobalVariableString("user","id").equals("1")){
            Picasso.with(TambahPaket.this)
                    .load(getIntent().getStringExtra("urlGambar"))
                    .noPlaceholder()
                    .resize(400, 400)
                    .centerInside()
                    .into(imgAvatar);
            edNamaPaket.setText(""+getIntent().getStringExtra("namaPaket"));
            edDeskripsi.setText(""+getIntent().getStringExtra("deskripsi"));
            edHarga.setText(""+getIntent().getStringExtra("harga"));
            url = getIntent().getStringExtra("urlGambar");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                uriImage = result.getUri();
                uploadImage();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(getApplicationContext(),""+error,Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void editPaket(){
        int pajak = (int) (Integer.parseInt(edHarga.getText().toString()) * 0.1);
        APIService service = APIClient.getClient().create(APIService.class);
        Call<StatusResponse> tambah = service.tambahPaket(edNamaPaket.getText().toString(),""+pajak,edHarga.getText().toString(),url,
                edDeskripsi.getText().toString());
        tambah.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus()==1){
                        Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_LONG).show();
                        onBackPressed();
                    }else {
                        Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),response.message(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"eror connection",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void uploadImage(){
        mDialog.setMessage("Please wait...");
        mDialog.show();
        File file = new File(uriImage.getPath());
        RequestBody requestBody = RequestBody.create(MediaType.parse("**/*//*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("avatar", file.getName(), requestBody);
        APIService service = APIClient.getClient().create(APIService.class);
        Call<StatusResponse> upload = service.uploadImage(fileToUpload);
        upload.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus()==1){
//                        Log.e("gambar",response.body().getUrl());
//                        String aUrl = response.body().getUrl().replace("http", "https");
                        url = response.body().getUrl();
                        Picasso.with(TambahPaket.this)
                                .load(uriImage)
                                .noPlaceholder()
                                .resize(400, 400)
                                .centerInside()
                                .into(imgAvatar);
                    }
                    Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(),"eror"+response.message(),Toast.LENGTH_LONG).show();
                }
                mDialog.dismiss();
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Eror connection",Toast.LENGTH_LONG).show();
                mDialog.dismiss();
            }
        });
    }

    public void tambahPaket(){
        mDialog.setMessage("Please wait...");
        mDialog.show();
        int pajak = (int) (Integer.parseInt(edHarga.getText().toString()) * 0.1);
//        Log.e("pajak",""+pajak);
        APIService service = APIClient.getClient().create(APIService.class);
        Call<StatusResponse> tambah = service.tambahPaket(edNamaPaket.getText().toString(),""+pajak,edHarga.getText().toString(),url,
                edDeskripsi.getText().toString());
//        Call<StatusResponse> tambah = service.tambahPaket("dashjd","47","47898","urygyul",
//               " edDeskripsi.getText().toString()");
        tambah.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                if (response.isSuccessful()){
                   if (response.body().getStatus()==1){
                       Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_LONG).show();
                       onBackPressed();
                   }else {
                       Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_LONG).show();
                   }
                }else {
                    Toast.makeText(getApplicationContext(),response.message(),Toast.LENGTH_LONG).show();
                }
                mDialog.dismiss();
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"eror connection",Toast.LENGTH_LONG).show();
                mDialog.dismiss();
            }
        });
    }
}