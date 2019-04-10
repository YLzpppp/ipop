package com.jasoncareter.ipop.model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TargetStatus {
    @GET("TargetStatus?")
    Call<String> getTargetStatus(@Query("username")String user);
}
