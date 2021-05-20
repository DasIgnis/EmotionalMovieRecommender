package com.example.sentimentalrecommender.presenters;

import android.util.Log;

import com.example.sentimentalrecommender.entities.Message;
import com.example.sentimentalrecommender.modules.ApiModule;
import com.example.sentimentalrecommender.objects.EmotionResponse;
import com.example.sentimentalrecommender.objects.TranslationResponse;
import com.example.sentimentalrecommender.views.VoiceActorView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Locale;
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
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        Executors.newSingleThreadExecutor().execute(() -> {
            baseView.getMessageRepository().insertAll(message);
            String content = message.content;
            if (!Locale.getDefault().getLanguage().equals("en")) {
                String responseJSON = ApiModule.getTranslation(content, Locale.getDefault().getLanguage());

                TranslationResponse response = gson.fromJson(responseJSON, TranslationResponse.class);
                if (response.outputs.size() > 0) {
                    content = response.outputs.get(0).output;
                }
            }
            Log.d("EMOTION", content);
            String emotionJSON = ApiModule.getEmotion(content);
            Log.d("EMOTION", emotionJSON);

            EmotionResponse emotionResponse = gson.fromJson(emotionJSON, EmotionResponse.class);

        });
        baseView.addMessageToRecycler(message);
    }
}
