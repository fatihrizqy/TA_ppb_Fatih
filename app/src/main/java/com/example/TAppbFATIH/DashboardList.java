package com.example.TAppbFATIH;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.TAppbFATIH.adapter.Adapter_dashboard;
import com.example.TAppbFATIH.lokalDatabase.InitDatabase;
import com.example.TAppbFATIH.retrofit.APIClient;
import com.example.TAppbFATIH.retrofit.APIService;
import com.example.TAppbFATIH.retrofit.modelResponseData.ResponseGetList;

import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardList extends AppCompatActivity {
    CardView cardTambah;
    RecyclerView recyclerView;
    ImageView ic_more,imgMinus,imgPlus;
    LinearLayout user,admin;
    TextView txtTotal;
    Adapter_dashboard adapter_dashboard;
    boolean isActiveButton = false;
    int total = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_list);
        InitDatabase initDatabase = new InitDatabase(DashboardList.this);
        cardTambah = findViewById(R.id.cardTambah);
        recyclerView = findViewById(R.id.rcDashboard);
        ic_more = findViewById(R.id.imgMore);
        txtTotal = findViewById(R.id.txtTotal);
        user = findViewById(R.id.user);
        admin = findViewById(R.id.admin);
//        Log.e("database", initDatabase.getGlobalVariableString("user","user_access"));

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        ic_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(DashboardList.this, ic_more);

                // Inflating popup menu from popup_menu.xml file
                popupMenu.getMenuInflater().inflate(R.menu.main_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        // Toast message on menu item clicked
                        int id = menuItem.getItemId();
                        switch (id)
                        {
                            case R.id.id_maps:
                                String url = "https://goo.gl/maps/SXjySxY8gM6EMyqH7";
                                Intent bukamaps = new Intent(Intent.ACTION_VIEW);
                                bukamaps.setData(Uri.parse(url));
                                startActivity(bukamaps);
                                break;

                            case R.id.id_Update:
                                Intent intent = new Intent(DashboardList.this, EditUser.class);
                                startActivity(intent);
//                                Toast.makeText(this, "Update", Toast.LENGTH_SHORT).show();
                                break;

                            case R.id.id_Callcenter:
                                String nomor = "081217747224" ;
                                Intent panggil = new Intent(Intent. ACTION_DIAL);
                                panggil.setData(Uri. fromParts("tel",nomor,null));
                                startActivity(panggil);
                                break;

                            case R.id.id_SmsCenter:
                                String number = "+6281217747224";  // The number on which you want to send SMS
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)));
                                break;

                            case R.id.history:
                                startActivity(new Intent(DashboardList.this,HistoryList.class));
                                break;

                        }
                        return true;
                    }
                });
                // Showing the popup menu
                popupMenu.show();
            }
        });

        if (initDatabase.getGlobalVariableString("user","user_access").equals("1")){
            admin.setVisibility(View.VISIBLE);
            user.setVisibility(View.GONE);
            cardTambah.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(DashboardList.this,TambahPaket.class)
                    .putExtra("type","tambah"));
                }
            });
        }else {
            admin.setVisibility(View.GONE);
            user.setVisibility(View.VISIBLE);

        }
        getList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getList();
    }

    public void setter(String namaPaket, String idPaket, int harga){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
            txtTotal.setText(formatRupiah.format((double) harga)+",-");
            cardTambah.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(DashboardList.this,FormBayar.class)
                            .putExtra("namaPaket",""+namaPaket)
                            .putExtra("idPaket",""+idPaket)
                            .putExtra("harga",""+harga));
                }
            });
    }

    public void getList(){
        APIService service = APIClient.getClient().create(APIService.class);
        Call<ResponseGetList> tambah = service.getListDashboard();
        tambah.enqueue(new Callback<ResponseGetList>() {
            @Override
            public void onResponse(Call<ResponseGetList> call, Response<ResponseGetList> response) {
                if (response.isSuccessful()){
                    if (response.body().getData().size()>0) {
                        adapter_dashboard = new Adapter_dashboard(DashboardList.this, response.body().getData(),DashboardList.this);
                        recyclerView.setAdapter(adapter_dashboard);
                    }else {
                        Toast.makeText(getApplicationContext(),"Data kosong",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),response.message(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseGetList> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Eror Connection",Toast.LENGTH_LONG).show();

            }
        });
    }


}