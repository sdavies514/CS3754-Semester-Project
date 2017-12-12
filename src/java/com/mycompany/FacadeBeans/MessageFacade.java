/*
 * Created by Thomas Corley on 2017.12.04  * 
 * Copyright © 2017 Osman Balci. All rights reserved. * 
 */
package com.mycompany.FacadeBeans;

import com.mycompany.EntityBeans.Message;
import com.mycompany.EntityBeans.Project;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author tj
 */
@Stateless
public class MessageFacade extends AbstractFacade<Message> {

    @PersistenceContext(unitName = "ThoughtwarePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MessageFacade() {
        super(Message.class);
    }
    
    
    public List<Message> getLastMessages(Project p, int n){
        return (List<Message>) em.createQuery("SELECT * FROM table WHERE c.projectId = :project ORDER BY id DESC LIMIT :num")
                .setParameter("project", p)
                .setParameter("num", n)
                .getResultList();
        
    }
}
