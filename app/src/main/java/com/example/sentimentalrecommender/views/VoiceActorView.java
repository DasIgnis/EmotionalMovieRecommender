package com.example.sentimentalrecommender.views;

import com.example.sentimentalrecommender.entities.Message;
import com.example.sentimentalrecommender.entities.MessageRepository;

import java.util.List;

public interface VoiceActorView {
    MessageRepository getMessageRepository();

    void addMessageToRecycler(Message message);

    void showMessages(List<Message> messages);
}
