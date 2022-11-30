package com.example.TAppbFATIH.retrofit;

import com.example.TAppbFATIH.retrofit.modelResponseData.GetDetailResponse;
import com.example.TAppbFATIH.retrofit.modelResponseData.ResponseGetList;
import com.example.TAppbFATIH.retrofit.modelResponseData.StatusResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIService {
    @FormUrlEncoded
    @POST("/rest_api.php?function=insert_user")
    Call<StatusResponse> register (@Field("id")String id,
                                   @Field("nama")String nama,
                                   @Field("password")String password,
                                   @Field("username")String username,
                                   @Field("login_type")String login_type,
                                   @Field("token")String token,
                                   @Field("user_access")String user_access,
                                   @Field("isLogin")String isLogin);

    @FormUrlEncoded
    @POST("/rest_api.php?function=insert_google")
    Call<StatusResponse> registerGoogle (@Field("id")String id,
                                   @Field("nama")String nama,
                                   @Field("password")String password,
                                   @Field("username")String username,
                                   @Field("login_type")String login_type,
                                   @Field("token")String token,
                                   @Field("user_access")String user_access,
                                   @Field("isLogin")String isLogin);

    @FormUrlEncoded
    @POST("/rest_api.php?function=login")
    Call<StatusResponse> login (@Field("username")String username,
                                @Field("password")String password);

    @Multipart
    @POST("/rest_api.php?function=upload_image")
    Call<StatusResponse> uploadImage(@Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("/rest_api.php?function=tambah_paket")
    Call<StatusResponse> tambahPaket (@Field("nama_paket")String nama_paket,
                                         @Field("pajak")String pajak,
                                         @Field("harga")String harga,
                                         @Field("url_gambar")String url_gambar,
                                         @Field("deskripsi")String deskripsi);

    @FormUrlEncoded
    @POST("/rest_api.php?function=tambah_paket")
    Call<StatusResponse> editPaket (@Field("nama_paket")String nama_paket,
                                      @Field("pajak")String pajak,
                                      @Field("harga")String harga,
                                      @Field("url_gambar")String url_gambar,
                                      @Field("deskripsi")String deskripsi);
    @FormUrlEncoded
    @POST("/rest_api.php?function=pesan")
    Call<StatusResponse> pesan (@Field("nama_pemesan")String nama_pemesan,
                                      @Field("id_pemesan")String id_pemesan,
                                      @Field("harga")String harga,
                                      @Field("nama_paket")String nama_paket,
                                      @Field("id_paket")String id_paket,
                                      @Field("url_bukti")String url_bukti,
                                    @Field("kembalian")String kembalian,
                                    @Field("tanggal")String tanggal);


    @FormUrlEncoded
    @POST("/rest_api.php?function=pesan")
    Call<GetDetailResponse> getDetail (@Field("id")String id);

    @FormUrlEncoded
    @POST("/rest_api.php?function=delete_paket")
    Call<StatusResponse> deletePaket (@Field("id")String id);

    @FormUrlEncoded
    @POST("/rest_api.php?function=update_user")
    Call<StatusResponse> edit_user (@Field("id")String id,
                                       @Field("username")String username,
                                       @Field("password")String password);

    @GET("/rest_api.php?function=get_dashboard_list")
    Call<ResponseGetList> getListDashboard ();

    @GET("/rest_api.php?function=get_history_list")
    Call<GetDetailResponse> getListHistory ();

    @FormUrlEncoded
    @POST("/rest_api.php?function=get_history_byid")
    Call<GetDetailResponse> getListHistorybyId (@Field("id")String id);

}
