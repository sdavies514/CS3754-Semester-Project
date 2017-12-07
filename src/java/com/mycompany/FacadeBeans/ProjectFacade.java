/*
 * Created by Thomas Corley on 2017.12.04  * 
 * Copyright © 2017 Osman Balci. All rights reserved. * 
 */
package com.mycompany.FacadeBeans;

import com.mycompany.EntityBeans.Project;
import com.mycompany.EntityBeans.User;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author tj
 */
@Stateless
public class ProjectFacade extends AbstractFacade<Project> {

    @PersistenceContext(unitName = "ThoughtwarePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProjectFacade() {
        super(Project.class);
    }

    public Project findByName(String name) {
        if (em.createNamedQuery("Project.findByName")
                .setParameter("name", name)
                .getResultList().isEmpty()) {
            return null;
        } else {
            return (Project) em.createNamedQuery("Project.findByName")
                    .setParameter("name", name)
                    .getSingleResult();
        }
    }
}
