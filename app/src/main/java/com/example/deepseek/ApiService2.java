package com.example.deepseek;



import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService2 {


    @POST("chat/completions")
    Call<ChatResponse> getChatCompletion2(
            @Header("Authorization") String authToken,
            @Body ChatRequest chatRequest
    );



}