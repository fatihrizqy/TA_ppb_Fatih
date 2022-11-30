package com.example.TAppbFATIH.retrofit.modelResponseData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataDetail{

	@SerializedName("kembalian")
	@Expose
	private String kembalian;

	@SerializedName("id_pemesan")
	@Expose
	private String idPemesan;

	@SerializedName("harga")
	@Expose
	private String harga;

	@SerializedName("url_bukti")
	@Expose
	private String urlBukti;

	@SerializedName("nama_pemesan")
	@Expose
	private String namaPemesan;

	@SerializedName("id")
	@Expose
	private String id;

	@SerializedName("id_paket")
	@Expose
	private String idPaket;

	@SerializedName("tanggal")
	@Expose
	private String tanggal;

	@SerializedName("nama_paket")
	@Expose
	private String namaPaket;

	public String getKembalian(){
		return kembalian;
	}

	public String getIdPemesan(){
		return idPemesan;
	}

	public String getHarga(){
		return harga;
	}

	public String getUrlBukti(){
		return urlBukti;
	}

	public String getNamaPemesan(){
		return namaPemesan;
	}

	public String getId(){
		return id;
	}

	public String getIdPaket(){
		return idPaket;
	}

	public String getTanggal(){
		return tanggal;
	}

	public String getNamaPaket(){
		return namaPaket;
	}
}