package com.jasoncareter.ipop.model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TargetPort {

    @GET("TargetPort")
    Call<String> getTargetPort(@Query("username") String user);
}
