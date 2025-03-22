package com.example.deepseek;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity {

    private EditText inputText;
    private Button sendButton;
    private TextView responseText;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // 初始化视图
        inputText = findViewById(R.id.inputText);
        sendButton = findViewById(R.id.sendButton);
        responseText = findViewById(R.id.responseText);

        // 初始化 ViewModel
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // 设置按钮点击事件
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userInput = inputText.getText().toString().trim();
                String currentText = responseText.getText().toString();
                String newText = currentText + "\n" + userInput;
                responseText.setText(newText);
                inputText.setText("");
                String authToken = "Bearer " + "you api_key";
                Message userMessage = new Message("user", userInput);
                Message systemMessage = new Message("system", "You are a helpful assistant.");
                Message[] messages = {systemMessage, userMessage};
                ChatRequest chatRequest = new ChatRequest("deepseek-chat", messages, false);
                viewModel.sendChatRequest(authToken, chatRequest);
                responseText.setText("\n"+"加载中...");


            }
        });

        // 观察 LiveData
        viewModel.getChatResponseLiveData().observe(this, chatResponse -> {
            if (chatResponse != null) {
                StringBuilder sb = new StringBuilder();
                for (Choice choice : chatResponse.getChoices()) {
                    MessageResponse mr = choice.getMessage(); // 通过Choice获取MessageResponse
                    sb.append(mr.getContent());
                }
                responseText.setText(sb.toString());
                // 获取剪贴板管理器


            } else {
                responseText.setText("请求失败");
                // 显示错误信息
                Toast.makeText(MainActivity.this, "请求失败，请稍后重试", Toast.LENGTH_SHORT).show();
            }
        });

        // 设置窗口插图监听器
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}