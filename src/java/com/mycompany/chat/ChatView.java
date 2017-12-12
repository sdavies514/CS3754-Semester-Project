package com.mycompany.chat;

import com.mycompany.EntityBeans.Message;
import com.mycompany.controllers.ProjectController;
import com.mycompany.managers.AccountManager;
import java.io.Serializable;
import java.util.Date;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

@ManagedBean
@SessionScoped
public class ChatView implements Serializable {

    //private final PushContext pushContext = PushContextFactory.getDefault().getPushContext();
    private final EventBus eventBus = EventBusFactory.getDefault().eventBus();

    @ManagedProperty("#{accountManager}")
    private AccountManager accountManager;

    @ManagedProperty("#{projectController}")
    private ProjectController projectController;

    @EJB
    private com.mycompany.FacadeBeans.MessageFacade messageFacade;

    private String globalMessage;
    private boolean isConnected;

    public String getGlobalMessage() {
        return globalMessage;
    }

    public void setGlobalMessage(String globalMessage) {
        this.globalMessage = globalMessage;
    }

    public void sendGlobal() {
        Message create = new Message();
        create.setMessageText(globalMessage);
        create.setProjectId(projectController.getSelected());
        create.setUserId(accountManager.getSelected());
        create.setTimestamp(new Date());
        getMessageFacade().create(create);
        eventBus.publish("/" + projectController.getSelected().getName() + "/*", getAccountManager().getSelected().getUsername() + ": " + globalMessage);
        globalMessage = null;
    }

    public void disconnect() {
        //push leave information
        eventBus.publish("/" + projectController.getSelected().getName() + "/*", getAccountManager().getSelected().getUsername() + " left the channel.");
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
     * @return the isConnected
     */
    public boolean isIsConnected() {
        return isConnected;
    }

    /**
     * @param isConnected the isConnected to set
     */
    public void setIsConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    /**
     * @return the projectController
     */
    public ProjectController getProjectController() {
        return projectController;
    }

    /**
     * @param projectController the projectController to set
     */
    public void setProjectController(ProjectController projectController) {
        this.projectController = projectController;
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
}
