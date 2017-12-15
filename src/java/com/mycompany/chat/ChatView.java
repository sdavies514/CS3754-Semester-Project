package com.mycompany.chat;
/*
 * Created by Thomas Corley on 2017.12.04  * 
 * Copyright © 2017 Osman Balci. All rights reserved. * 
 *
 */
import com.mycompany.EntityBeans.Message;
import com.mycompany.managers.AccountManager;
import com.mycompany.managers.ProjectViewManager;
import java.io.Serializable;
import java.util.Date;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

/*
 * ChatView interfaces with the jsf template to provide the the real time chat
 * functionality.
 */
@ManagedBean
@SessionScoped
public class ChatView implements Serializable {

    // The eventBus is the bean that we use to publish the messages to all
    // web socket listners.
    private final EventBus eventBus = EventBusFactory.getDefault().eventBus();

    // The accountManager is needed to determine which user this session is a 
    // a part of so we can determine who is sending the messages.
    @ManagedProperty("#{accountManager}")
    private AccountManager accountManager;

    // The projectViewManager is used to determine which project the user is 
    // currently viewing. 
    @ManagedProperty("#{projectViewManager}")
    private ProjectViewManager projectViewManager;

    // Every message that is sent is saved in the database, this is used for 
    // RSS feed, and could also allow for message playback feature to happen.
    @EJB
    private com.mycompany.FacadeBeans.MessageFacade messageFacade;

    // When a message is to be sent this field is populated by the jsf template 
    private String globalMessage;

    // Triggered by the front end. We simply save the message contents to the
    // database and then publish it to everyone that has a websocket connection
    // to the current project path. 
    public void sendGlobal() {
        // Message we are saving to the db
        Message create = new Message();
        create.setMessageText(globalMessage);
        create.setProjectId(projectViewManager.getSelected());
        create.setUserId(accountManager.getSelected());
        // Default constructor date defaults to the current time.
        create.setTimestamp(new Date());
        // Actually save the message to the db.
        getMessageFacade().create(create);
        // Send message to /projectName/* so everyone in the room gets the
        // message
        eventBus.publish("/" + projectViewManager.getSelected().getName() + "/*", getAccountManager().getSelected().getUsername() + ": " + globalMessage);
        globalMessage = null;
    }
    
    // When we leave the room we announce to everyone we have left
    public void disconnect() {
        //push leave information
        eventBus.publish("/" + projectViewManager.getSelected().getName() + "/*", getAccountManager().getSelected().getUsername() + " left the channel.");
    }
       
    // Below just contains setters and gettters.
    
    public String getGlobalMessage() {
        return globalMessage;
    }

    public void setGlobalMessage(String globalMessage) {
        this.globalMessage = globalMessage;
    }

    /**
     * @return the accountManager
     */
    public AccountManager getAccountManager() {
        return accountManager;
    }

    /**
     * @param accountManager the accountManager to set
     */
    public void setAccountManager(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    /**
     * @return the messageFacade
     */
    public com.mycompany.FacadeBeans.MessageFacade getMessageFacade() {
        return messageFacade;
    }

    /**
     * @param messageFacade the messageFacade to set
     */
    public void setMessageFacade(com.mycompany.FacadeBeans.MessageFacade messageFacade) {
        this.messageFacade = messageFacade;
    }

    /**
     * @return the projectViewManager
     */
    public ProjectViewManager getProjectViewManager() {
        return projectViewManager;
    }

    /**
     * @param projectViewManager the projectViewManager to set
     */
    public void setProjectViewManager(ProjectViewManager projectViewManager) {
        this.projectViewManager = projectViewManager;
    }
}
