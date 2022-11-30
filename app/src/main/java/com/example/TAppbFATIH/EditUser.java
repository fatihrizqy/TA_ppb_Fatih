package com.example.TAppbFATIH;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.TAppbFATIH.lokalDatabase.InitDatabase;
import com.example.TAppbFATIH.retrofit.APIClient;
import com.example.TAppbFATIH.retrofit.APIService;
import com.example.TAppbFATIH.retrofit.modelResponseData.StatusResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUser extends AppCompatActivity {
    EditText mUsernameEdit, mPasswordEdit;
    Button mLoginButton;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        mUsernameEdit = (EditText) findViewById(R.id.editUsername);
        mPasswordEdit = (EditText) findViewById(R.id.editPassword);
        mLoginButton = (Button) findViewById(R.id.btnLogin);
        mDialog = new ProgressDialog(this);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUsernameEdit.getText().toString().isEmpty()){
                    mUsernameEdit.setError("Username harus diisi");
                }else if (mUsernameEdit.getText().toString().isEmpty()){
                    mUsernameEdit.setError("Password harus diisi");
                }else {
                    edit();
                }
            }
        });

    }

    public void edit(){
        InitDatabase initDatabase = new InitDatabase(EditUser.this);
        mDialog.setMessage("Please wait...");
        mDialog.show();
        APIService service = APIClient.getClient().create(APIService.class);
        Call<StatusResponse> res = service.edit_user(initDatabase.getGlobalVariableString("user","id"),mUsernameEdit.getText().toString(),mPasswordEdit.getText().toString());
        res.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus()==1){
                        Toast.makeText(EditUser.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        if (initDatabase.getTotalInit()>0){
                            initDatabase.delete("user");
                        }
                        if (initDatabase.insert(response.body().getData().get(0))>0){
                            Intent moveHalamanUtama = new Intent(EditUser.this, DashboardList.class);
                            startActivity(moveHalamanUtama);
                        }else {
                            Toast.makeText(EditUser.this, "gagal simpan data lokal", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(EditUser.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(EditUser.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                Toast.makeText(EditUser.this, "error connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
}