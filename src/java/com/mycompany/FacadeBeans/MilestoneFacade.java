/*
 * Created by Thomas Corley on 2017.12.04  * 
 * Copyright © 2017 Osman Balci. All rights reserved. * 
 */
package com.mycompany.FacadeBeans;

import com.mycompany.EntityBeans.Milestone;
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
public class MilestoneFacade extends AbstractFacade<Milestone> {

    @PersistenceContext(unitName = "ThoughtwarePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MilestoneFacade() {
        super(Milestone.class);
    }
    
    public List<Milestone> getAllForProject(Project p){
        return (List<Milestone>) em.createQuery("SELECT c FROM Milestone c WHERE c.projectId = :project")
                .setParameter("project", p)
                .getResultList();
    }
    
}
