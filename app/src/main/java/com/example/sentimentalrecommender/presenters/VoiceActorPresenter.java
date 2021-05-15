package com.example.sentimentalrecommender.presenters;

import com.example.sentimentalrecommender.entities.Message;
import com.example.sentimentalrecommender.views.VoiceActorView;

import java.util.concurrent.Executors;

public class VoiceActorPresenter {
    private VoiceActorView baseView;

    public void attachView(VoiceActorView view) {
        baseView = view;
    }

    public void loadMessages() {
        Executors.newSingleThreadExecutor().execute(() -> {
            baseView.showMessages(
                    baseView.getMessageRepository().getAll()
            );
        });
    }

    public void addMessage(Message message) {
        Executors.newSingleThreadExecutor().execute(() -> {
            baseView.getMessageRepository().insertAll(message);
        });
        baseView.addMessageToRecycler(message);
    }
}
