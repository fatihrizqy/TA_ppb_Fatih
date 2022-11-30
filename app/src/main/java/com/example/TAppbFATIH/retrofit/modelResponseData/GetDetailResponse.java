package com.example.TAppbFATIH.retrofit.modelResponseData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetDetailResponse {
    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("data")
    @Expose
    private List<DataDetail> data;


    public String getUrl() {
        return url;
    }

    public List<DataDetail> getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
