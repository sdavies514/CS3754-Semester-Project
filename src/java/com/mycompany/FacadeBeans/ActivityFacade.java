/*
 * Created by Thomas Corley on 2017.12.04  * 
 * Copyright © 2017 Osman Balci. All rights reserved. * 
 */
package com.mycompany.FacadeBeans;

import com.mycompany.EntityBeans.Activity;
import com.mycompany.EntityBeans.Project;
import com.mycompany.EntityBeans.User;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author tj
 */
@Stateless
public class ActivityFacade extends AbstractFacade<Activity> {

    @PersistenceContext(unitName = "ThoughtwarePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ActivityFacade() {
        super(Activity.class);
    }

    public List<Activity> findByProject(Project project) {
        return em.createQuery("SELECT a FROM Activity a WHERE a.projectId = :project")
                .setParameter("project", project)
                .getResultList();
    }
}
