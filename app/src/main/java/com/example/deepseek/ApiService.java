package com.example.deepseek;



import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {

    @POST("text/chatcompletion_v2")
    Call<ChatResponse> getChatCompletion(
            @Header("Authorization") String authToken,
            @Body ChatRequest chatRequest
    );



}