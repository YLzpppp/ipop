package com.jasoncareter.ipop.model;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * http://localhost/ipopServer/Registerserver?username=8120&password=abcdefg
 */

public interface RegisterUser {
    @GET("Registerserver?")
    Call<String> registerToserver(@Query("username") String u,@Query("password") String p ,@Query("port")int t);
}
