package com.example.hamlet.mobileprogrammingclass_chat_project.classes;

import java.util.ArrayList;
import java.util.List;

public class Chat {

    private String chatId;
    private ArrayList<Message> messages;
    private User sender;
    private User receiver;
    private boolean unread;
    private int unreadMessagesCounter;


    public Chat(String chatId, ArrayList<Message> messages, User sender, User receiver, boolean unread, int unreadMessagesCounter) {
        this.chatId = chatId;
        this.messages = messages;
        this.sender = sender;
        this.unread = unread;
        this.unreadMessagesCounter = unreadMessagesCounter;
        this.receiver = receiver;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public boolean isUnread() {
        return unread;
    }

    public void setUnread(boolean unread) {
        this.unread = unread;
    }

    public int getUnreadMessagesCounter() {
        return unreadMessagesCounter;
    }

    public void setUnreadMessagesCounter(int unreadMessagesCounter) {
        this.unreadMessagesCounter = unreadMessagesCounter;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }
}
