package com.mycompany.controllers;

import com.mycompany.EntityBeans.Milestone;
import com.mycompany.managers.AccountManager;
import com.mycompany.managers.ProjectViewManager;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

@ManagedBean
@SessionScoped
public class ScheduleController implements Serializable {

    private ScheduleModel lazyEventModel;
    private ScheduleEvent event = new DefaultScheduleEvent();
    @ManagedProperty("#{projectViewManager}")
    private ProjectViewManager projectViewManager;

    @ManagedProperty("#{milestoneController}")
    private MilestoneController milestoneController;

    @PostConstruct
    public void init() {

        lazyEventModel = new LazyScheduleModel() {

            @Override
            public void loadEvents(Date start, Date end) {
                if(milestoneController == null || projectViewManager == null)
                       return;
                List<Milestone> mstones = getMilestoneController().projectMilestones(getProjectViewManager().getSelected());
                for (Milestone m : mstones) {
                    if (m != null && m.getCompletedDate() == null) {
                        addEvent(new DefaultScheduleEvent(m.getDescription(), m.getDueDate(), m.getDueDate()));
                    }
                }
            }
        };
    }

    public ScheduleModel getLazyEventModel() {
        return lazyEventModel;
    }

//    public void onEventSelect(SelectEvent selectEvent) {
//        event = (ScheduleEvent) selectEvent.getObject();
//    }
//
//    public ScheduleEvent getEvent() {
//        return event;
//    }
//
//    public void setEvent(ScheduleEvent event) {
//        this.event = event;
//    }

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
