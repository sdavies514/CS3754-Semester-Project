package com.mycompany.chat;
 
public class ChatMessage {
     
    private String text;
    private String user;
    private boolean updateList;
 
    public ChatMessage() {
    }
 
    public ChatMessage(String text) {
        this.text = text;
    }
     
    public ChatMessage(String text, boolean updateList) {
        this.text = text;
        this.updateList = updateList;
    }
 
    public ChatMessage(String user, String text, boolean updateList) {
        this.text = text;
        this.user = user;
        this.updateList = updateList;
    }
     
    public String getText() {
        return text;
    }
 
    public ChatMessage setText(String text) {
        this.text = text;
        return this;
    }
 
    public String getUser() {
        return user;
    }
 
    public ChatMessage setUser(String user) {
        this.user = user;
        return this;
    }
 
    public boolean isUpdateList() {
        return updateList;
    }
 
    public void setUpdateList(boolean updateList) {
        this.updateList = updateList;
    }
}