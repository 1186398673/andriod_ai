package com.example.deepseek;

import android.content.Context;
import android.content.SharedPreferences;
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

    private ApiService2 apiService2;

    public static final String PREFS_NAME2 = "ModelPrefs";
    public static final String KEY_SELECTED_MODEL = "selected_model";

    private Context context;

    public MainViewModel() {
        chatResponseLiveData = new MutableLiveData<>();




    }

    public LiveData<ChatResponse> getChatResponseLiveData() {
        return chatResponseLiveData;
    }


    public void sendChatRequest(String authToken, ChatRequest chatRequest,Context context) {
       if((getmodel(context).equals("abab6.5s-chat"))||(getmodel(context).equals("MiniMax-Text-01"))) {
           apiService = RetrofitClient.getClient().create(ApiService.class);
           apiService.getChatCompletion(authToken, chatRequest).enqueue(new Callback<ChatResponse>() {
               @Override
               public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                   if (response.isSuccessful()) {
                       // 添加原始响应日志
                       Log.d("MiniMaxApi", "原始响应：" + response.raw().body().toString());
                       ChatResponse chatResponse = response.body();
                       if (chatResponse != null) {
                           Log.d("MiniMaxApi", "响应体: " + chatResponse.toString());
                           chatResponseLiveData.postValue(chatResponse);
                       } else {
                           Log.e("MiniMaxApi", "响应体为空");
                           chatResponseLiveData.postValue(null);
                       }
                   } else {
                       Log.e("MiniMaxApi", "请求失败，状态码: " + response.code());
                       Log.e("MiniMaxApi", "错误信息: " + response.message());
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
       else {
           apiService2 = RetrofitClient.getClient2().create(ApiService2.class);
           apiService2.getChatCompletion2(authToken, chatRequest).enqueue(new Callback<ChatResponse>() {
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

    public void savemodel(Context context, String model) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME2, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_SELECTED_MODEL, model);
        editor.apply();
    }

    // 从 SharedPreferences 获取 API Key
    public static String getmodel(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME2, Context.MODE_PRIVATE);
        return prefs.getString(KEY_SELECTED_MODEL, null);
    }


}