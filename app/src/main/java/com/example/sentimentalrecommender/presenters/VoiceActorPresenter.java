package com.example.sentimentalrecommender.presenters;

import android.util.Log;

import com.example.sentimentalrecommender.Application;
import com.example.sentimentalrecommender.R;
import com.example.sentimentalrecommender.entities.Message;
import com.example.sentimentalrecommender.modules.ApiModule;
import com.example.sentimentalrecommender.objects.Emotion;
import com.example.sentimentalrecommender.objects.EmotionResponse;
import com.example.sentimentalrecommender.objects.EmotionResponseItem;
import com.example.sentimentalrecommender.objects.MovieResponse;
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

            if (baseView.getMessageRepository().getAll().size() == 0) {
                Message initialMessage = new Message(
                        false,
                        Application.getAppContext().getString(R.string.welcome_message), "", "");
                baseView.getMessageRepository().insertAll(initialMessage);
                baseView.addMessageToRecycler(initialMessage);
            }
        });
    }

    public void addMessage(Message message) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        Executors.newSingleThreadExecutor().execute(() -> {
            baseView.getMessageRepository().insertAll(message);
            baseView.addMessageToRecycler(message);
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

            EmotionResponseItem emotionResponseItem = gson.fromJson(emotionJSON, EmotionResponse.class).emotion;

            Emotion mostValuable = emotionResponseItem.getMostValuable();
            Message botResponse;
            switch (mostValuable) {
                case ANGRY:
                    botResponse = new Message(false, Application.getAppContext().getString(R.string.angry_confirmation_message), "", "");
                    break;
                case SAD:
                    botResponse = new Message(false, Application.getAppContext().getString(R.string.sad_confirmation_message), "", "");
                    break;
                case FEAR:
                    botResponse = new Message(false, Application.getAppContext().getString(R.string.fear_confirmation_message), "", "");
                    break;
                case BORED:
                    botResponse = new Message(false, Application.getAppContext().getString(R.string.bored_confirmation_message), "", "");
                    break;
                case HAPPY:
                    botResponse = new Message(false, Application.getAppContext().getString(R.string.happy_confirmation_message), "", "");
                    break;
                default:
                    botResponse = new Message(false, Application.getAppContext().getString(R.string.excited_confirmation_message), "", "");
                    break;
            }
            baseView.addMessageToRecycler(botResponse);
            baseView.getMessageRepository().insertAll(botResponse);

            String movieJSON = ApiModule.getMovie(emotionResponseItem);
            MovieResponse movieResponse = gson.fromJson(movieJSON, MovieResponse.class);
            Log.d("MOVIE", movieJSON);

            Message movieMessage = new Message(false, movieResponse.FilmName, movieResponse.PosterUrl, movieResponse.FilmUrl);
            baseView.addMessageToRecycler(movieMessage);
            baseView.getMessageRepository().insertAll(movieMessage);
        });
    }
}
