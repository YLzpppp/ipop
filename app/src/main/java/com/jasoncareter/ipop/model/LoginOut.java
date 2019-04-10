package com.jasoncareter.ipop.model;


import retrofit2.Call;
import retrofit2.http.HTTP;
import retrofit2.http.Query;


public interface LoginOut {
    @HTTP(method = "GET",path = "Loginout?",hasBody = false)
    Call<String> LoginOutServer(@Query("username")String user);
}
