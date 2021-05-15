package com.example.sentimentalrecommender.entities;

import java.util.List;

public interface MessageRepository {
    List<Message> getAll();

    void insertAll(Message... messages);
}
