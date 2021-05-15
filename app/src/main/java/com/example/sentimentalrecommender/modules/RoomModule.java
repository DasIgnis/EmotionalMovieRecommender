package com.example.sentimentalrecommender.modules;

import android.app.Application;

import androidx.room.Room;

import com.example.sentimentalrecommender.entities.AppDatabase;
import com.example.sentimentalrecommender.entities.MessageDataSource;
import com.example.sentimentalrecommender.entities.MessageRepository;
import com.example.sentimentalrecommender.entities.dao.MessageDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RoomModule {
    private AppDatabase appDatabase;

    public RoomModule(Application application) {
        this.appDatabase = Room.databaseBuilder(application, AppDatabase.class, "app-db").build();
    }

    @Singleton
    @Provides
    AppDatabase providesRoomDatabase() {
        return appDatabase;
    }

    @Singleton
    @Provides
    MessageDao providesMessageDao(AppDatabase appDatabase) {
        return appDatabase.messageDao();
    }

    @Singleton
    @Provides
    MessageRepository messageRepository(MessageDao messageDao) {
        return new MessageDataSource(messageDao);
    }
}
