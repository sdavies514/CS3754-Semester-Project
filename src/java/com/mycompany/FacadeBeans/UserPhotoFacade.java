/*
 * Created by Casey Butenhoff on 2017.11.18  *
 * Copyright © 2017 Casey Butenhoff. All rights reserved. *
 */
package com.mycompany.FacadeBeans;

import com.mycompany.EntityBeans.UserPhoto;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Butenhoff
 */
@Stateless
public class UserPhotoFacade extends AbstractFacade<UserPhoto> {

    @PersistenceContext(unitName = "ThoughtwarePU")
    private EntityManager em;

    // @Override annotation indicates that the super class AbstractFacade's getEntityManager() method is overridden.
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /*
    This constructor method invokes the parent abstract class AbstractFacade.java's
    constructor method AbstractFacade, which in turn initializes its entityClass instance
    variable with the Photo class object reference returned by the Photo.class.
     */
    public UserPhotoFacade() {
        super(UserPhoto.class);
    }

    /*
    ====================================================
    The following method is added to the generated code.
    ====================================================
     */
    /**
     * @param userID is the Primary Key of the User entity in a table row in the ThoughtwareDB database.
     * @return a list of photos associated with the User whose primary key is userID
     */
    public List<UserPhoto> findPhotosByUserID(Integer userID) {

        return (List<UserPhoto>) em.createNamedQuery("UserPhoto.findPhotosByUserID")
                .setParameter("userId", userID)
                .getResultList();
    }

    /* The following methods are inherited from the parent AbstractFacade class:

        create
        edit
        find
        findAll
        remove
     */

}
