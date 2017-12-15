package com.mycompany.controllers;
/*
 * Created by Thomas Corley on 2017.12.04  * 
 * Copyright © 2017 Osman Balci. All rights reserved. * 
 *
 */
import com.mycompany.EntityBeans.Milestone;
import com.mycompany.managers.ProjectViewManager;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleModel;

/*
 * The ScheduleController class handles the values that are passed to the 
 * milestone calendar
 */
@ManagedBean
@SessionScoped
public class ScheduleController implements Serializable {
    
    // We are loading everything lazily on page load so we use a lazy event model.
    private ScheduleModel lazyEventModel;
    // Need the projectViewManager to determine which project we are in.
    @ManagedProperty("#{projectViewManager}")
    private ProjectViewManager projectViewManager;
    // Need the milestoneController to get all Milestones from the database
    @ManagedProperty("#{milestoneController}")
    private MilestoneController milestoneController;
    // This is called at the beggining of the session. All it does is create
    // a LazyScheduleModel that will do things late. 
    @PostConstruct
    public void init() {

        lazyEventModel = new LazyScheduleModel() {
            // This will be called everytime the calendar is loaded. 
            // Aka on page load
            @Override
            public void loadEvents(Date start, Date end) {
                // Sometimes there have been reports of null pointer exceptions
                // so we cautiously check for them.
                if(milestoneController == null || projectViewManager == null)
                       return;
                // Get all milestones, and put all of the ones that are not 
                // completed into the database. 
                List<Milestone> mstones = getMilestoneController().projectMilestones(getProjectViewManager().getSelected());
                for (Milestone m : mstones) {
                    if (m != null && m.getCompletedDate() == null) {
                        addEvent(new DefaultScheduleEvent(m.getDescription(), m.getDueDate(), m.getDueDate()));
                    }
                }
            }
        };
    }

    // The rest of the functions are just setters and getters
    
    /**
     * @return the layEventModel
     */
    public ScheduleModel getLazyEventModel() {
        if(lazyEventModel == null)
            init();
        return lazyEventModel;
    }
    
      /**
     * @param m the model to set lazyEventModel to
     * 
     */
    public void setLazyEventModel(ScheduleModel m) {
        lazyEventModel = m;
    }
    
    /**
     * @return the projectViewManager
     */
    public ProjectViewManager getProjectViewManager() {
        return projectViewManager;
    }

    /**
     * @param projectViewManager the projectViewManager to set
     */
    public void setProjectViewManager(ProjectViewManager projectViewManager) {
        this.projectViewManager = projectViewManager;
    }

    /**
     * @return the milestoneController
     */
    public MilestoneController getMilestoneController() {
        return milestoneController;
    }

    /**
     * @param milestoneController the milestoneController to set
     */
    public void setMilestoneController(MilestoneController milestoneController) {
        this.milestoneController = milestoneController;
    }

}
