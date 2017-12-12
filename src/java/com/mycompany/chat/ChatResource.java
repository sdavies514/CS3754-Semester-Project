package com.mycompany.chat;

import com.mycompany.EntityBeans.Message;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
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

@PushEndpoint("/{project}/{user}")
@ApplicationScoped
public class ChatResource {

    @Inject
    private EventBus eventBus;
    @Inject
    private RemoteEndpoint endpoint;

    private final Logger logger = LoggerFactory.getLogger(ChatResource.class);
    @PathParam("user")
    private String username;

    @PathParam("project")
    private String projectName;

    @Inject
    private ServletContext ctx;

    @OnOpen
    public void onOpen(RemoteEndpoint r, EventBus e) throws InterruptedException {
        if (username == null) {
            System.out.println("WTF MAN");
            return;
        }
        logger.info("OnOpen {}", r);
        System.out.println("We opened " + r);
        eventBus.publish("/" + projectName + "/*", new ChatMessage(String.format("%s has entered the %s room", username, projectName)));
        e.publish("/" + projectName + "/*", new ChatMessage(String.format("%s has entered the %s room", username, projectName)));
    }

    @OnClose
    public void onClose(RemoteEndpoint r, EventBus e) {
        if (username == null) {
            return;
        }
        eventBus.publish("/" + projectName + "/*", new ChatMessage(String.format("%s has left the room", username), true));
    }

    @OnMessage(decoders = {MessageDecoder.class}, encoders = {MessageEncoder.class})
    public ChatMessage onMessage(ChatMessage message) {
        return message;
    }
}
