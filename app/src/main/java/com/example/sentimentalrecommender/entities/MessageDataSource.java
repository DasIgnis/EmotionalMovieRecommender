package com.example.sentimentalrecommender.entities;

import com.example.sentimentalrecommender.entities.dao.MessageDao;

import java.util.List;

import javax.inject.Inject;

public class MessageDataSource implements MessageRepository {

    private MessageDao messageDao;

    @Inject
    public MessageDataSource(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    @Override
    public List<Message> getAll() {
        return messageDao.getAll();
    }

    @Override
    public void insertAll(Message... messages) {
        messageDao.insertAll(messages);
    }
}
