package com.example.TAppbFATIH;


import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.TAppbFATIH.retrofit.APIClient;
import com.example.TAppbFATIH.retrofit.APIService;
import com.example.TAppbFATIH.retrofit.modelResponseData.StatusResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class regis extends AppCompatActivity {
    EditText mNamaLengkapEdit, mUsernameEdit, mPasswordedit;
    Button mRegistrasiButton, mResetButton;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private ProgressDialog mDialog;
    public static final String session = "session";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regis);
        mNamaLengkapEdit = (EditText) findViewById(R.id.editNamaLengkapRegis);
        mUsernameEdit = (EditText) findViewById(R.id.editUsernameRegis);
        mPasswordedit = (EditText) findViewById(R.id.editPasswordRegis);
        mRegistrasiButton = (Button) findViewById(R.id.btnRegistrasi);
        mResetButton = (Button) findViewById(R.id.btnReset);
        mDialog = new ProgressDialog(this);
        mRegistrasiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                pref = getSharedPreferences(session, MODE_PRIVATE);
//                editor = pref.edit();
//                editor.putString("namaLengkap", mNamaLengkapEdit.getText().toString());editor.putString("username", mUsernameEdit.getText().toString());
//                editor.putString("password", mPasswordedit.getText().toString());editor.apply();
//                Toast.makeText(regis.this, "Registrasi Sukses", Toast.LENGTH_LONG).show();
                if (mNamaLengkapEdit.getText().toString().isEmpty()){
                    mNamaLengkapEdit.setError("Silahkan isi nama lengkap");
                }else if (mPasswordedit.getText().toString().isEmpty()){
                    mPasswordedit.setError("Silahkan isi password");
                }else if (mUsernameEdit.getText().toString().isEmpty()){
                    mUsernameEdit.setError("Silahkan isi username");
                }else {
                    register();
                }
            }
        });
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNamaLengkapEdit.setText("");
                mUsernameEdit.setText("");
                mPasswordedit.setText("");
            }
        });
    }



    private void register(){
        mDialog.setMessage("Please wait...");
        mDialog.show();
        APIService service = APIClient.getClient().create(APIService.class);
        Call<StatusResponse> res = service.register("",mNamaLengkapEdit.getText().toString(),mPasswordedit.getText().toString(),mUsernameEdit.getText().toString(),"form","","","");
        res.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                if (response.isSuccessful()){
                    Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(),"failed to save",Toast.LENGTH_LONG).show();
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