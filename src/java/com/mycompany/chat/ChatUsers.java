package com.mycompany.chat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean
@ApplicationScoped
public class ChatUsers implements Serializable {

    private List<String> users;

    public List<String> getUsers() {
        if (users == null) {
            users = new ArrayList<>();
        }
        return users;
    }

    public void remove(String user) {
        if (users == null) {
            return;
        }
        this.users.remove(user);
    }

    public void add(String user) {
        if (users == null) {
            users = new ArrayList<>();
        }
        this.users.add(user);
    }

    public boolean contains(String user) {
        System.out.println("Users is: " + users);
        return getUsers().contains(user);
    }
}
