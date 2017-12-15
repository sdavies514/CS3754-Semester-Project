/*
 * Created by Thomas Corley on 2017.12.04  * 
 * Copyright © 2017 Osman Balci. All rights reserved. * 
 *
 */
package com.mycompany.chat;

/*
 * The ChatMessage class is a simple wrapper class that stores the User and
 * and the Message that is being sent. It gets encoded and decoded into/from
 * JSON when interacting with the front end.
 */       
public class ChatMessage {
    
    // Only two things we care about in a message
    private String text;
    private String user;
 
    public ChatMessage() {
    }
 
    // It is possible for a message to not have a username.
    // For example when someone enters a room a message is sent announcing
    // their presence.
    public ChatMessage(String text) {
        this.text = text;
    }
 
    // When a user is sending a message to the chat this constructor will be 
    // used.
    public ChatMessage(String user, String text) {
        this.text = text;
        this.user = user;
    }
    
    // Getter and Setter methods for the fields.
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
}