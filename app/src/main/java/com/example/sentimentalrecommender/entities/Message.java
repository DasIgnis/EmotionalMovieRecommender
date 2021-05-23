package com.example.sentimentalrecommender.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Message {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "isUserMessage")
    public boolean isUserMessage;

    @ColumnInfo(name = "content")
    public String content;

    @ColumnInfo(name = "image_url")
    public String imageUrl;

    @ColumnInfo(name = "movie_url")
    public String movieUrl;

    public Message(boolean isUserMessage, String content) {
        this.isUserMessage = isUserMessage;
        this.content = content;
    }

    public Message(String content, String imageUrl, String movieUrl) {

    }
}
