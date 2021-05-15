package com.example.sentimentalrecommender.views;

import com.example.sentimentalrecommender.entities.Message;
import com.example.sentimentalrecommender.entities.MessageRepository;

public interface VoiceActorView {
    MessageRepository getMessageRepository();

    void addMessageToRecycler(Message message);
}
