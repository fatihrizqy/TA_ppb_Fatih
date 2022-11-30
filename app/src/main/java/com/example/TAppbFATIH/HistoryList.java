package com.example.TAppbFATIH;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.Spanned;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.TAppbFATIH.adapter.Adapter_history;
import com.example.TAppbFATIH.lokalDatabase.InitDatabase;
import com.example.TAppbFATIH.retrofit.APIClient;
import com.example.TAppbFATIH.retrofit.APIService;
import com.example.TAppbFATIH.retrofit.modelResponseData.GetDetailResponse;
import com.google.gson.Gson;
import com.tejpratapsingh.pdfcreator.views.basic.PDFTextView;

import org.apache.commons.io.FileUtils;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryList extends AppCompatActivity {
    RecyclerView rcHistory;
    Adapter_history adapter_history;
    ImageView ic_more;
    String result;
    InitDatabase initDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_list);
        rcHistory = findViewById(R.id.rcHistory);
        ic_more = findViewById(R.id.imgMore);
        initDatabase = new InitDatabase(HistoryList.this);
        rcHistory.setLayoutManager(new LinearLayoutManager(this));
        ic_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(HistoryList.this, ic_more);

                // Inflating popup menu from popup_menu.xml file
                popupMenu.getMenuInflater().inflate(R.menu.history_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        // Toast message on menu item clicked
                        int id = menuItem.getItemId();
                        switch (id)
                        {
                            case R.id.exprtExcel:
//                                jsonToCSV();
                                startActivity(new Intent(HistoryList.this,Webview.class));
                                break;
                        }
                        return true;
                    }
                });
                // Showing the popup menu
                popupMenu.show();
            }
        });
        if (initDatabase.getGlobalVariableString("user","user_access").equals("1")) {
            listHistory();
        }else {
            ic_more.setVisibility(View.GONE);
            listHistorybyId(initDatabase.getGlobalVariableString("user","id"));
        }
    }

    public void jsonTopdf(){
        PDFTextView pdfIconLicenseView = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.H3);
        Spanned icon8Link = Html.fromHtml("Icon from <a href='https://icons8.com'>https://icons8.com</a>");
        pdfIconLicenseView.getView().setText(icon8Link);
    }

    public void jsonToCSV(){
        try {
            JSONArray docs = new JSONArray(result);
            File file=new File(  Environment.getExternalStorageDirectory().getAbsolutePath() + "/gowthamguru"+"/tmp2/fromJSONss.csv");
            String csv = CDL.toString(docs);
            FileUtils.writeStringToFile(file, csv);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file),"application/vnd.ms-excel");
            startActivity(intent);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }


    public void listHistory(){
        APIService service = APIClient.getClient().create(APIService.class);
        Call<GetDetailResponse> tambah = service.getListHistory();
        tambah.enqueue(new Callback<GetDetailResponse>() {
            @Override
            public void onResponse(Call<GetDetailResponse> call, Response<GetDetailResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus()==1){
                        if (response.body().getData().size()>0) {
                            adapter_history = new Adapter_history(HistoryList.this, response.body().getData());
                            rcHistory.setAdapter(adapter_history);
                            result =  new Gson().toJson(response.body().getData());
                        }else {
                            Toast.makeText(getApplicationContext(),"Data kosong",Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),response.message(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GetDetailResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"error connection",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void listHistorybyId(String id){
        APIService service = APIClient.getClient().create(APIService.class);
        Call<GetDetailResponse> tambah = service.getListHistorybyId(id);
        tambah.enqueue(new Callback<GetDetailResponse>() {
            @Override
            public void onResponse(Call<GetDetailResponse> call, Response<GetDetailResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus()==1){
                        if (response.body().getData().size()>0) {
                            adapter_history = new Adapter_history(HistoryList.this, response.body().getData());
                            rcHistory.setAdapter(adapter_history);
                            result =  new Gson().toJson(response.body().getData());
                        }else {
                            Toast.makeText(getApplicationContext(),"Data kosong",Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),response.message(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GetDetailResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"error connection",Toast.LENGTH_LONG).show();
            }
        });
    }
}