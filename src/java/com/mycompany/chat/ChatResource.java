/*
 * Created by Thomas Corley on 2017.12.04  * 
 * Copyright © 2017 Osman Balci. All rights reserved. * 
 *
 */
package com.mycompany.chat;

import javax.faces.bean.ApplicationScoped;
import org.primefaces.push.EventBus;
import org.primefaces.push.RemoteEndpoint;
import org.primefaces.push.annotation.OnClose;
import org.primefaces.push.annotation.OnMessage;
import org.primefaces.push.annotation.OnOpen;
import org.primefaces.push.annotation.PathParam;
import org.primefaces.push.annotation.PushEndpoint;

import javax.inject.Inject;

/*
 * This is the endpoint class that the websocket will open.
 * In the browser it will look like /Thoughtware/primepush/project/user
 * The PrimePuser servlet uses this class to determine what to do when 
 * Messages are recieved/sent and when users join and leave.
 * 
 */
@PushEndpoint("/{project}/{user}")
@ApplicationScoped
public class ChatResource {
    //The eventBus bean is the bean that handles the actual connection.
    // When we want to send a message we use its publish message to send the 
    // content to the clients.
    @Inject
    private EventBus eventBus;
    
    // We determine both the username and project based on the url that the
    // the socket is opened on.
    @PathParam("user")
    private String username;
    @PathParam("project")
    private String projectName;

    // When a socket is opened(and thus a user has gone to the chate page)
    // we publish to all the open sockets on that project that the user has 
    // joined
    @OnOpen
    public void onOpen(RemoteEndpoint r, EventBus e) throws InterruptedException {
        if (username == null) {
            return;
        }
        eventBus.publish("/" + projectName + "/*", new ChatMessage(String.format("%s has entered the %s room", username, projectName)));
    }
    
    // Likewise when a socket is closed we publish to everyone that the person has 
    // left the room. 
    @OnClose
    public void onClose(RemoteEndpoint r, EventBus e) {
        if (username == null) {
            return;
        }
        eventBus.publish("/" + projectName + "/*", new ChatMessage(String.format("%s has left the room", username)));
    }
    
    // onMessage is called both when a message is going to be sent and when
    // it is going be recieved.
    // We do not need to modify the message in transit so we just return it 
    // without changing anything. However we do specify the encoder and decoders
    // for the class. We simply encode/decode the class to/from JSON.
    @OnMessage(decoders = {MessageDecoder.class}, encoders = {MessageEncoder.class})
    public ChatMessage onMessage(ChatMessage message) {
        return message;
    }
}
