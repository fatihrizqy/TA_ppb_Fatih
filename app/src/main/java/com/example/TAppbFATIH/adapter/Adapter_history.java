package com.example.TAppbFATIH.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.TAppbFATIH.Nota;
import com.example.TAppbFATIH.R;
import com.example.TAppbFATIH.retrofit.modelResponseData.DataDetail;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_history extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context context;
    List<DataDetail> dataDetails;

    public Adapter_history(Context context, List<DataDetail> dataDetail){
        this.dataDetails = dataDetail;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.model_history_list, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ListViewHolder) {
            ListViewHolder listViewHolder = (ListViewHolder) holder;
            listViewHolder.txtNama.setText(dataDetails.get(position).getNamaPemesan());
            listViewHolder.txtNamaPaket.setText(dataDetails.get(position).getNamaPaket());
            listViewHolder.txtTanggal.setText(dataDetails.get(position).getTanggal());
            String aUrl = dataDetails.get(position).getUrlBukti().replace("http", "https");
            Picasso.with(context)
                    .load(aUrl)
                    .noPlaceholder()
                    .resize(400, 400)
                    .centerInside()
                    .into(listViewHolder.imgPreview);
            listViewHolder.cardDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, Nota.class)
                    .putExtra("namaPaket",dataDetails.get(holder.getAdapterPosition()).getNamaPaket())
                    .putExtra("waktu",""+dataDetails.get(holder.getAdapterPosition()).getTanggal())
                    .putExtra("kembalian",""+dataDetails.get(holder.getAdapterPosition()).getKembalian())
                    .putExtra("totalHarga",""+dataDetails.get(holder.getAdapterPosition()).getHarga())
                    .putExtra("namaPemesan",""+dataDetails.get(holder.getAdapterPosition()).getNamaPemesan()));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dataDetails == null ? 0 : dataDetails.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPreview;
        TextView txtNamaPaket,txtNama,txtTanggal;
        CardView cardDetail;
//        LinearLayout linDeskripsi,linContent;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            txtNamaPaket = itemView.findViewById(R.id.txtNamaPaket);
            txtNama = itemView.findViewById(R.id.txtNama);
            txtTanggal = itemView.findViewById(R.id.txtTanggal);
            imgPreview = itemView.findViewById(R.id.imgPreview);
            cardDetail = itemView.findViewById(R.id.cardDetail);
        }
    }
}
