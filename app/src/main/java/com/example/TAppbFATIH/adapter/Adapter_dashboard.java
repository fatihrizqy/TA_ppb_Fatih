package com.example.TAppbFATIH.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.TAppbFATIH.DashboardList;
import com.example.TAppbFATIH.DetailActivity;
import com.example.TAppbFATIH.R;
import com.example.TAppbFATIH.TambahPaket;
import com.example.TAppbFATIH.lokalDatabase.InitDatabase;
import com.example.TAppbFATIH.retrofit.APIClient;
import com.example.TAppbFATIH.retrofit.APIService;
import com.example.TAppbFATIH.retrofit.modelResponseData.DataPaket;
import com.example.TAppbFATIH.retrofit.modelResponseData.StatusResponse;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Adapter_dashboard extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context context;
    List<DataPaket> dataPakets;
    InitDatabase initDatabase;
    DashboardList dashboardList;
    public Adapter_dashboard(Context context, List<DataPaket> dataPakets, DashboardList dashboardList){
        this.dataPakets = dataPakets;
        this.context = context;
        this.dashboardList = dashboardList;
        initDatabase = new InitDatabase(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.model_list_paket, parent, false);
        return new  ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ListViewHolder) {
            ListViewHolder listViewHolder = (ListViewHolder) holder;
            Locale localeID = new Locale("in", "ID");
            NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
            listViewHolder.txtHarga.setText(formatRupiah.format((double) dataPakets.get(position).getHarga())+",-");
            listViewHolder.txtNamaPaket.setText(dataPakets.get(position).getNamaPaket());
            String aUrl = dataPakets.get(position).getUrlGambar().replace("http", "https");
            Picasso.with(context)
                    .load(aUrl)
                    .noPlaceholder()
                    .resize(400, 400)
                    .centerInside()
                    .into(listViewHolder.imgPaket);

            if (initDatabase.getGlobalVariableString("user","user_access").equals("1")){
                listViewHolder.imgDelete.setVisibility(View.VISIBLE);
                listViewHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        delete(dataPakets.get(holder.getAdapterPosition()).getId());
                    }
                });
                listViewHolder.linContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        context.startActivity(new Intent(context,TambahPaket.class)
                                .putExtra("urlGambar",""+dataPakets.get(holder.getAdapterPosition()).getUrlGambar())
                                .putExtra("id",""+dataPakets.get(holder.getAdapterPosition()).getId())
                                .putExtra("namaPaket",""+dataPakets.get(holder.getAdapterPosition()).getNamaPaket())
                                .putExtra("harga",""+dataPakets.get(holder.getAdapterPosition()).getHarga())
                                .putExtra("pajak",""+dataPakets.get(holder.getAdapterPosition()).getPajak())
                                .putExtra("deskripsi",""+dataPakets.get(holder.getAdapterPosition()).getDeskripsi())
                        );
                    }
                });
            }else {
                listViewHolder.imgDelete.setVisibility(View.GONE);
                listViewHolder.linDeskripsi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(context, DetailActivity.class);
                        i.putExtra("namaPaket",dataPakets.get(holder.getAdapterPosition()).getNamaPaket());
                        i.putExtra("harga",""+dataPakets.get(holder.getAdapterPosition()).getHarga());
                        i.putExtra("deskripsi",""+dataPakets.get(holder.getAdapterPosition()).getDeskripsi());
                        context.startActivity(i);
                    }
                });
                listViewHolder.imgPaket.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dashboardList.setter(dataPakets.get(holder.getAdapterPosition()).getNamaPaket(),dataPakets.get(holder.getAdapterPosition()).getId(),dataPakets.get(holder.getAdapterPosition()).getHarga());
                    }
                });
            }


        }
    }

    @Override
    public int getItemCount() {
        return dataPakets == null ? 0 : dataPakets.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPaket,imgDelete;
        TextView txtNamaPaket,txtHarga;
        LinearLayout linDeskripsi,linContent;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            imgDelete = itemView.findViewById(R.id.imgDelete);
            txtNamaPaket = itemView.findViewById(R.id.txtNamaPaket);
            txtHarga = itemView.findViewById(R.id.txtHarga);
            imgPaket = itemView.findViewById(R.id.imgPaket);
            linContent = itemView.findViewById(R.id.linContent);
            linDeskripsi = itemView.findViewById(R.id.linDeskripsi);
        }
    }

    public void delete(String id){
        APIService service = APIClient.getClient().create(APIService.class);
        Call<StatusResponse> deletePaket = service.deletePaket(id);
        deletePaket.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus()==1){
                        dashboardList.getList();
                    }
                    Toast.makeText(context,response.body().getMessage(),Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(context,response.message(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                Toast.makeText(context,"eror connection",Toast.LENGTH_LONG).show();
            }
        });
    }
}
