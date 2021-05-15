package com.example.sentimentalrecommender.entities;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.sentimentalrecommender.entities.dao.MessageDao;

@Database(entities = {Message.class}, version = AppDatabase.VERSION)
public abstract class AppDatabase extends RoomDatabase {
    static final int VERSION = 2;

    public abstract MessageDao messageDao();
}
