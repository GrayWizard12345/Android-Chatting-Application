package com.example.hamlet.mobileprogrammingclass_chat_project.classes;

import java.util.ArrayList;

public class Chat {

    private int chatId;
    private ArrayList<Message> messages;
    private User sender;

    private boolean unread;
    private int unreadMessagesCounter;


    public Chat(int chatId, ArrayList<Message> messages, User sender, boolean unread, int unreadMessagesCounter) {
        this.chatId = chatId;
        this.messages = messages;
        this.sender = sender;
        this.unread = unread;
        this.unreadMessagesCounter = unreadMessagesCounter;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
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
}
