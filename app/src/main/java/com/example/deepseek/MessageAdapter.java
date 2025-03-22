package com.example.deepseek;

import android.content.Context;
import android.text.Spanned;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.noties.markwon.Markwon;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Message> messages;
    private  Context context;

    public MessageAdapter(List<Message> messages, Context context) {
        this.messages = messages;
        this.context=context;

    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView roleText, contentText, timeText;
        LinearLayout messageContent;

        public MessageViewHolder(View itemView) {
            super(itemView);
            roleText = itemView.findViewById(R.id.roleText);
            contentText = itemView.findViewById(R.id.contentText);
            timeText = itemView.findViewById(R.id.timeText);
            messageContent = itemView.findViewById(R.id.messageContent);
        }

        public void bind(Message message) {
            String markdownContent = message.getContent();
            Markwon markwon = Markwon.create(itemView.getContext());
            Spanned markdown = markwon.toMarkdown(markdownContent);

            roleText.setText(message.getRole());
            contentText.setText(markdown);
            timeText.setText(message.getTimestamp());

            if (message.getRole().equals("user")) {
                // 用户消息在右侧
                messageContent.setBackgroundResource(R.drawable.message_bubble_user);
                ((LinearLayout) itemView.findViewById(R.id.messageContainer)).setGravity(Gravity.END);
            } else {
                // 系统消息在左侧
                messageContent.setBackgroundResource(R.drawable.message_bubble);
                ((LinearLayout) itemView.findViewById(R.id.messageContainer)).setGravity(Gravity.START);
            }
        }
    }
}
