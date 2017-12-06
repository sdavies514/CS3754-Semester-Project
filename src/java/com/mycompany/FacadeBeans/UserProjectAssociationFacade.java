/*
 * Created by Thomas Corley on 2017.12.04  * 
 * Copyright © 2017 Osman Balci. All rights reserved. * 
 */
package com.mycompany.FacadeBeans;

import com.mycompany.EntityBeans.Project;
import com.mycompany.EntityBeans.User;
import com.mycompany.EntityBeans.UserProjectAssociation;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author tj
 */
@Stateless
public class UserProjectAssociationFacade extends AbstractFacade<UserProjectAssociation> {

    @PersistenceContext(unitName = "ThoughtwarePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserProjectAssociationFacade() {
        super(UserProjectAssociation.class);
    }

    public boolean associationAlreadyExists(User u, Project p) {
        return !(em.createQuery("SELECT c FROM UserProjectAssociation c WHERE c.userId = :user AND c.projectId = :project")
                .setParameter("user", u)
                .setParameter("project", p)
                .getResultList().isEmpty());
    }

    public List<User> getUsersForProject(Project p) {
        return (List<User>) em.createQuery("SELECT c.userId FROM UserProjectAssociation c WHERE c.projectId = :project")
                .setParameter("project", p)
                .getResultList();
    }

    public List<Project> getProjectsForUser(User u) {
        return (List<Project>) em.createQuery("SELECT c.projectId FROM UserProjectAssociation c WHERE c.userId = :user")
                .setParameter("user", u)
                .getResultList();
    }

}
