package com.jasoncareter.ipop.model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TargetFriend {
    @GET("Targetfriend?")
    Call<String> AddFriend(@Query("username") String username);
}
