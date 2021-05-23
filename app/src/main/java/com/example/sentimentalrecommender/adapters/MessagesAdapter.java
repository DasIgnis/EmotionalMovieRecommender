package com.example.sentimentalrecommender.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sentimentalrecommender.R;
import com.example.sentimentalrecommender.entities.Message;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Message> messages;

    public MessagesAdapter() {
        messages = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @LayoutRes int layout = viewType == 0
            ? R.layout.user_message_view_holder
            : R.layout.bot_message_view_holder;

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(layout, parent, false);

        return new UsersMessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof UsersMessageViewHolder) {
            ((UsersMessageViewHolder) holder).setMessage(messages.get(position).content);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).isUserMessage ? 0 : 1;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
        this.notifyDataSetChanged();
    }

    public void addMessage(Message message) {
        this.messages.add(message);
        this.notifyDataSetChanged();
    }

    public static class UsersMessageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.message_content)
        TextView messageContent;

        public UsersMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setMessage(String message) {
            messageContent.setText(message);
        }
    }
}
