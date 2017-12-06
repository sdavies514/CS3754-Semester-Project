package com.mycompany.chat;
 
import com.mycompany.managers.AccountManager;
import java.io.Serializable;
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
 
    @ManagedProperty("#{chatUsers}")
    private ChatUsers users;
    
    @ManagedProperty("#{accountManager}")
    private AccountManager accountManager;
    
    private String privateMessage;
    private String globalMessage;
    private String privateUser;
    private boolean isConnected;
    
    public ChatUsers getUsers() {
        return users;
    }
 
    public void setUsers(ChatUsers users) {
        this.users = users;
    }
     
    public String getPrivateUser() {
        return privateUser;
    }
 
    public void setPrivateUser(String privateUser) {
        this.privateUser = privateUser;
    }
 
    public String getGlobalMessage() {
        return globalMessage;
    }
 
    public void setGlobalMessage(String globalMessage) {
        this.globalMessage = globalMessage;
    }
 
    public String getPrivateMessage() {
        return privateMessage;
    }
 
    public void setPrivateMessage(String privateMessage) {
        this.privateMessage = privateMessage;
    }
 
    public void sendGlobal() {
        System.out.println("LESSGO");
        eventBus.publish("/*", getAccountManager().getSelected().getUsername() + ": " + globalMessage);
         
        globalMessage = null;
    }
     
    public void sendPrivate() {
        System.out.println("privMessage is " + privateMessage);
        eventBus.publish("/" + privateUser, "[PM] " + getAccountManager().getSelected().getUsername() + ": " + privateMessage);
        privateMessage = null;
    }
     
     
    public void disconnect() {
        //remove user and update ui
        users.remove(getAccountManager().getUsername());
        RequestContext.getCurrentInstance().update("form:users");
         
        //push leave information
        eventBus.publish("/*", getAccountManager().getSelected().getUsername() + " left the channel.");
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
}