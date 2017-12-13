/*
 * Created by Kyle Dyess on 2017.12.13  * 
 * Copyright © 2017 Kyle Dyess. All rights reserved. * 
 */
package com.mycompany.managers;

import com.mycompany.EntityBeans.Project;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author Kyle Dyess
 */

@Named("projectViewManager")
@SessionScoped

public class ProjectViewManager implements Serializable {
    // selected = Selected Project object
    private Project selected;
    
    /*
    ==================
    Constructor Method
    ==================
     */
    public ProjectViewManager() {
    }

    /*
    =========================
    Getter and Setter Methods
    =========================
     */
    public Project getSelected() {
        return selected;
    }

    public void setSelected(Project selected) {
        this.selected = selected;
    }
    
    /*
    =============================
    Project Page Redirect Methods
    =============================
    */
    public String showProjectHome(Project proj) {
        this.selected = proj;
        return "ViewProject.xhtml?faces-redirect=true";
    }
    
    public String showProjectChat(Project proj) {
        this.selected = proj;
        return "Chat.xhtml?faces-redirect=true";
    }
    
    public String showProjectTimeline(Project proj) {
        this.selected = proj;
        return "Timeline.xhtml?faces-redirect=true";
    }
    
    public String showProjectFiles(Project proj) {
        this.selected = proj;
        return "ViewProjectFiles.xhtml?faces-redirect=true";
    }
    
    public String showProjectCalendar(Project proj) {
        this.selected = proj;
        return "Calendar.xhtml?faces-redirect=true";
    }
}
