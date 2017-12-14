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

    /**
     * Given a project's RSS key, returns the project associated with that key,
     * if one exists. Otherwise, returns null.
     *
     * @param rssKey The RSS key of the project to retrieve.
     * @return The project associated with the given RSS key, or null.
     */
    public Project findByRssKey(String rssKey) {
        // getSingleResult() throws an exception if the entity can't be found,
        // so we must first make sure that it exists in order to avoid needing
        // to catch and handle an unnecessary exception.
        if (em.createNamedQuery("Project.findByRssKey")
                .setParameter("rssKey", rssKey)
                .getResultList().isEmpty()) {
            // If we can't find even a single project with the given RSS key, we
            // return null to indicate that it doesn't exist.
            return null;
        } else {
            // There should only ever be at most one project with a particular
            // RSS key, so simply return it. In the unlikely event that there is
            // more than one result, this will throw a NonUniqueResultException.
            return (Project) em.createNamedQuery("Project.findByRssKey")
                    .setParameter("rssKey", rssKey)
                    .getSingleResult();
        }
    }
}
