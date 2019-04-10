package com.jasoncareter.ipop.model;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.HTTP;
import retrofit2.http.QueryMap;

public interface LoginCheck {
    @HTTP(method = "GET" , path = "Loginserver?", hasBody = false)
    Call<String> LoginToServer(@QueryMap Map<String ,String> map);
}
