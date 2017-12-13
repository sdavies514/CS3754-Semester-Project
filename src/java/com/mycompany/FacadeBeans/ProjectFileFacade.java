/*
 * Created by Thomas Corley on 2017.12.04  * 
 * Copyright © 2017 Osman Balci. All rights reserved. * 
 */
package com.mycompany.FacadeBeans;

import com.mycompany.EntityBeans.ProjectFile;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author tj
 */
@Stateless
public class ProjectFileFacade extends AbstractFacade<ProjectFile> {

    /*
    Annotating 'private EntityManager em;' with '@PersistenceContext(unitName = "ThoughtwarePU")' implies that
    the EntityManager instance pointed to by 'em' is associated with the 'ThoughtwarePU' persistence context.

    Here, Entity is the ProjectFile object. The persistence context is a set of entity (ProjectFile) instances in which
    for any persistent entity identity, there is a unique entity instance.

    Within the persistence context, the entity instances and their life cycles are managed. The EntityManager API is used
    to create and remove persistent entity instances, to find entities by their primary key, and to query over entities.
     */
    @PersistenceContext(unitName = "ThoughtwarePU")
    private EntityManager em;   // 'em' holds the object reference of the instantiated EntityManager object.

    // @Override annotation indicates that the super class AbstractFacade's getEntityManager() method is overridden.
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /*
     This constructor method invokes the parent abstract class AbstractFacade.java's
     constructor method, which in turn initializes its entityClass instance variable
     with the ProjectFile class object reference returned by the ProjectFile.class.
     */
    public ProjectFileFacade() {
        super(ProjectFile.class);
    }
    
    /*
    ======================================================
    The following methods are added to the generated code.
    ======================================================
     */
    /**
     *
     * @param projID is the Primary Key of the project entity in the database
     * @return a list of object references of ProjectFiles that belong to the project whose DB Primary Key = projID
     */
    public List<ProjectFile> findProjectFilesByProjectID(Integer projID) {
        /*
        The following @NamedQuery definition is given in ProjectFile.java entity class file:
        @NamedQuery(name = "ProjectFile.findProjectFilesByProjectId", 
            query = "SELECT p FROM ProjectFile p WHERE p.projectId.id = :projectId")

        The following statement obtains the results from the named database query.
         */
        List<ProjectFile> projFiles = em.createNamedQuery("ProjectFile.findProjectFilesByProjectId")
                .setParameter("projectId", projID)
                .getResultList();

        return projFiles;
    }

    /**
     *
     * @param file_location
     * @return a list of object references of ProjectFiles with the name file_name
     */
    public List<ProjectFile> findByFileLocation(String file_location) {
        /*
        The following @NamedQuery definition is given in ProjectFile.java entity class file:
        @NamedQuery(name = "ProjectFile.findByFileLocation", 
            query = "SELECT p FROM ProjectFile p WHERE p.fileLocation = :fileLocation")})

        The following statement obtaines the results from the named database query.
         */
        List<ProjectFile> files = em.createNamedQuery("ProjectFile.findByFileLocation")
                .setParameter("fileLocation", file_location)
                .getResultList();

        return files;
    }
}
