package com.example.TAppbFATIH;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormBayar extends AppCompatActivity {
    private ProgressDialog mDialog;
    EditText edNamaPemesan,edTotalHarga,edJumlahBayar,edKembali;
    InitDatabase initDatabase;
    Button btnSimpan,btnPickImage;
    ImageView imgAvatar;
    Uri uriImage;
    String urlGambar = "";
    int harga,temp = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_bayar);
        mDialog = new ProgressDialog(this);
        edNamaPemesan = findViewById(R.id.edNamaPemesan);
        edTotalHarga = findViewById(R.id.edTotalHarga);
        edJumlahBayar = findViewById(R.id.edJumlahBayar);
        initDatabase = new InitDatabase(FormBayar.this);
        btnPickImage = findViewById(R.id.btnPickImage);
        imgAvatar = findViewById(R.id.imgAvatar);
        edTotalHarga.setText(getIntent().getStringExtra("harga"));
        btnSimpan = findViewById(R.id.btnSimpan);
        edKembali = findViewById(R.id.edKembali);
        harga = Integer.parseInt(getIntent().getStringExtra("harga"));
        edJumlahBayar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edJumlahBayar.getText().toString().length() > 0) {
                    edJumlahBayar.removeTextChangedListener(this);
                    edJumlahBayar.addTextChangedListener(this);
                    temp = Integer.parseInt(charSequence.toString());
                    if (temp - harga >= 0) {
                        edKembali.setText(""+(temp-harga));
                    } else {
                        edKembali.setText("0");
                    }
                }else {
                    edJumlahBayar.removeTextChangedListener(this);
                    edJumlahBayar.addTextChangedListener(this);
                    edKembali.setText("0");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edNamaPemesan.setText(initDatabase.getGlobalVariableString("user","nama"));
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edNamaPemesan.getText().toString().isEmpty()){
                    edNamaPemesan.setError("Nama pemesan harus diisi");
                }else if (temp-harga<0){
                    edNamaPemesan.setError("Jumlah bayar harus sama atau lebih besar dari total bayar");
                }else if (urlGambar.equals("")){
                    Toast.makeText(getApplicationContext(), "bukti bayar harus di upload",Toast.LENGTH_LONG).show();
                }else {
                    pesan();
                }
            }
        });
        btnPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
//                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setFixAspectRatio(true)
                        .start(FormBayar.this);
            }
        });
    }

    public void pesan(){

        mDialog.setMessage("Please wait...");
        mDialog.show();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date now = new Date();
        Log.e("id",""+initDatabase.getGlobalVariableString("user","id"));
        APIService service = APIClient.getClient().create(APIService.class);
        Call<StatusResponse> response = service.pesan(edNamaPemesan.getText().toString(),initDatabase.getGlobalVariableString("user","id"),
                getIntent().getStringExtra("harga"),getIntent().getStringExtra("namaPaket"),getIntent().getStringExtra("idPaket"),urlGambar,
                edKembali.getText().toString(),""+sdf.format(now));
        response.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                if(response.isSuccessful()){
                    if (response.body().getStatus()==1){
                        Toast.makeText(FormBayar.this, "Pesanan tersimpan", Toast.LENGTH_SHORT).show();
//                        onBackPressed();
                        startActivity(new Intent(FormBayar.this, Nota.class)
                                .putExtra("namaPaket",getIntent().getStringExtra("namaPaket"))
                                .putExtra("waktu",""+sdf.format(now))
                                .putExtra("kembalian",""+edKembali.getText().toString())
                                .putExtra("totalHarga",""+getIntent().getStringExtra("harga"))
                                .putExtra("namaPemesan",""+edNamaPemesan.getText().toString()));
//                        startActivity(new Intent(FormBayar.this,Nota.class));
                    }else {
                        Toast.makeText(FormBayar.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(FormBayar.this, response.message(), Toast.LENGTH_SHORT).show();
                }
                mDialog.dismiss();
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                Toast.makeText(FormBayar.this, "error connection", Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }
        });
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
                        urlGambar = response.body().getUrl();
                        Picasso.with(FormBayar.this)
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
}