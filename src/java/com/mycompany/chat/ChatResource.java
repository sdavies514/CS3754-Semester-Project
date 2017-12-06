package com.mycompany.chat;

import org.primefaces.push.EventBus;
import org.primefaces.push.RemoteEndpoint;
import org.primefaces.push.annotation.OnClose;
import org.primefaces.push.annotation.OnMessage;
import org.primefaces.push.annotation.OnOpen;
import org.primefaces.push.annotation.PathParam;
import org.primefaces.push.annotation.PushEndpoint;
import org.primefaces.push.annotation.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.ServletContext;

@PushEndpoint("/{user}")
@Singleton
public class ChatResource {

    @Inject
    private EventBus eventBus;
    @Inject
    private RemoteEndpoint endpoint;

    private final Logger logger = LoggerFactory.getLogger(ChatResource.class);
    @PathParam("user")
    private String username;

    @Inject
    private ServletContext ctx;

    @OnOpen
    public void onOpen(RemoteEndpoint r, EventBus e) {
        if (username == null) {
            return;
        }
        logger.info("OnOpen {}", r);
        System.out.println("We opened " + r);
        eventBus.publish("/*", new Message(String.format("%s has entered the room", username), true));
    }

    @OnClose
    public void onClose(RemoteEndpoint r, EventBus e) {
        if (username == null) {
            return;
        }
        ChatUsers users = (ChatUsers) ctx.getAttribute("chatUsers");
        users.remove(username);

        eventBus.publish("/*", new Message(String.format("%s has left the room", username), true));
    }

    @OnMessage(decoders = {MessageDecoder.class}, encoders = {MessageEncoder.class})
    public Message onMessage(Message message) {
        return message;
    }

}
