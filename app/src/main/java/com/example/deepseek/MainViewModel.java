package com.example.deepseek;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {
    private MutableLiveData<ChatResponse> chatResponseLiveData;
    private ApiService apiService;

    public MainViewModel() {
        chatResponseLiveData = new MutableLiveData<>();
        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    public LiveData<ChatResponse> getChatResponseLiveData() {
        return chatResponseLiveData;
    }


    public void sendChatRequest(String authToken, ChatRequest chatRequest) {
        apiService.getChatCompletion(authToken, chatRequest).enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                if (response.isSuccessful()) {
                    // 添加原始响应日志
                    Log.d("DeepSeekApi", "原始响应：" + response.raw().body().toString());
                    ChatResponse chatResponse = response.body();
                    if (chatResponse != null) {
                        Log.d("DeepSeekApi", "响应体: " + chatResponse.toString());
                        chatResponseLiveData.postValue(chatResponse);
                    } else {
                        Log.e("DeepSeekApi", "响应体为空");
                        chatResponseLiveData.postValue(null);
                    }
                } else {
                    Log.e("DeepSeekApi", "请求失败，状态码: " + response.code());
                    Log.e("DeepSeekApi", "错误信息: " + response.message());
                    chatResponseLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                Log.e("DeepSeekApi", "请求失败", t);
                chatResponseLiveData.postValue(null);
            }

        });
    }

}