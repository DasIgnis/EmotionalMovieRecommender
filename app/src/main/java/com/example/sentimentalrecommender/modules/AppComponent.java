package com.example.sentimentalrecommender.modules;

import android.app.Application;

import com.example.sentimentalrecommender.activities.VoiceActorActivity;
import com.example.sentimentalrecommender.entities.AppDatabase;
import com.example.sentimentalrecommender.entities.MessageRepository;
import com.example.sentimentalrecommender.entities.dao.MessageDao;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(dependencies = {}, modules = {AppModule.class, RoomModule.class})
public interface AppComponent {
    void inject(VoiceActorActivity mainActivity);

    MessageDao messageDao();

    AppDatabase appDatabase();

    MessageRepository messageRepository();

    Application application();
}
