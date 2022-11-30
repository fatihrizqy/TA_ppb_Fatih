package com.example.TAppbFATIH.retrofit.modelResponseData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataPaket{

	@SerializedName("pajak")
	@Expose
	private String pajak;

	@SerializedName("harga")
	@Expose
	private int harga;

	@SerializedName("url_gambar")
	@Expose
	private String urlGambar;

	@SerializedName("id")
	@Expose
	private String id;

	@SerializedName("deskripsi")
	@Expose
	private String deskripsi;

	@SerializedName("nama_paket")
	@Expose
	private String namaPaket;

	public String getPajak(){
		return pajak;
	}

	public int getHarga(){
		return harga;
	}

	public String getUrlGambar(){
		return urlGambar;
	}

	public String getId(){
		return id;
	}

	public String getDeskripsi(){
		return deskripsi;
	}

	public String getNamaPaket(){
		return namaPaket;
	}
}