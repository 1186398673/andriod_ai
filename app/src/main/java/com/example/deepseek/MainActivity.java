package com.example.deepseek;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText inputText;
    private Button sendButton,settingButton;
    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private List<Message> messageList = new ArrayList<>();
    private MainViewModel viewModel;
    private Drawer drawer;

    private static final String PREFS_NAME = "ApiKeyPrefs";
    private static final String API_KEY = "ApiKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化视图
        inputText = findViewById(R.id.inputText);
        sendButton = findViewById(R.id.sendButton);

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new MessageAdapter(messageList,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withDrawerGravity(GravityCompat.START)
                .withTranslucentStatusBar(true)
                .withDisplayBelowStatusBar(true)
                .withDrawerWidthDp(300)
                .withSavedInstance(savedInstanceState)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("首页").withIdentifier(1),
                        new PrimaryDrawerItem().withName("设置API").withIdentifier(2),
                        new PrimaryDrawerItem().withName("关于").withIdentifier(3)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // 处理导航项点击事件
                        switch ((int) drawerItem.getIdentifier()) {
                            case 1:
                                // 打开首页
                                break;
                            case 2:
                                showSetApiKeyDialog();
                                break;
                            case 3:
                                // 打开关于
                                break;
                        }
                        return false;
                    }
                })
                .build();



        // 初始化 ViewModel
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // 设置按钮点击事件
        sendButton.setOnClickListener(v -> {
            String userInput = inputText.getText().toString().trim();
            String savedApiKey = getApiKey(this);
            if (savedApiKey != null) {
                // 使用已保存的 API Key
                //Toast.makeText(this, "已加载 API Key: " + savedApiKey, Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "请输入API Key " , Toast.LENGTH_SHORT).show();
            }

            if (!userInput.isEmpty()) {
                Message userMessage = new Message("user", userInput);
                messageList.add(userMessage);
                adapter.notifyItemInserted(messageList.size() - 1);
                recyclerView.scrollToPosition(messageList.size() - 1);
                inputText.setText("");

                String authToken = "Bearer " + savedApiKey;
                Message systemMessage = new Message("system", "You are a helpful assistant.");
                Message[] messages = {systemMessage, userMessage};
                ChatRequest chatRequest = new ChatRequest("deepseek-chat", messages, false);
                viewModel.sendChatRequest(authToken, chatRequest);
                messageList.add(new Message("system", "思考中..."));
                adapter.notifyItemInserted(messageList.size() - 1);
                recyclerView.scrollToPosition(messageList.size() - 1);
            }

        });

        // 观察 LiveData
        viewModel.getChatResponseLiveData().observe(this, chatResponse -> {
            if (chatResponse != null) {
                StringBuilder sb = new StringBuilder();
                for (Choice choice : chatResponse.getChoices()) {
                    MessageResponse mr = choice.getMessage();
                    sb.append(mr.getContent());
                }
                Message aiMessage = new Message("AI", sb.toString());
                messageList.set(messageList.size() - 1, aiMessage);
                adapter.notifyItemChanged(messageList.size() - 1);
                recyclerView.scrollToPosition(messageList.size() - 1);
            } else {
                Message errorMessage = new Message("系统", "请求失败，请稍后重试");
                messageList.set(messageList.size() - 1, errorMessage);
                adapter.notifyItemChanged(messageList.size() - 1);
                recyclerView.scrollToPosition(messageList.size() - 1);
                Toast.makeText(MainActivity.this, "请求失败，请稍后重试", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_button1) {
            drawer.openDrawer();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSetApiKeyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置 API Key");

        // 获取布局并设置 EditText
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_set_apikey, null);
        builder.setView(dialogView);

        EditText editApiKey = dialogView.findViewById(R.id.edit_apikey);

        editApiKey.setText(getApiKey(this));

        // 设置保存按钮
        builder.setPositiveButton("保存", (dialog, which) -> {
            String apiKey = editApiKey.getText().toString().trim();
            if (!apiKey.isEmpty()) {
                saveApiKey(this, apiKey);
                Toast.makeText(this, "API Key 已保存", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "API Key 不能为空", Toast.LENGTH_SHORT).show();
            }
        });

        // 设置取消按钮
        builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());

        // 显示对话框
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // 保存 API Key 到 SharedPreferences
    private void saveApiKey(Context context, String apiKey) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(API_KEY, apiKey);
        editor.apply();
    }

    // 从 SharedPreferences 获取 API Key
    public static String getApiKey(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(API_KEY, null);
    }
}
