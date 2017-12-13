package com.mycompany.controllers;

import com.mycompany.FacadeBeans.*;
import com.mycompany.EntityBeans.Milestone;
import com.mycompany.EntityBeans.Project;
import com.mycompany.controllers.util.JsfUtil;
import com.mycompany.controllers.util.JsfUtil.PersistAction;
import com.mycompany.FacadeBeans.MilestoneFacade;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@Named("milestoneController")
@SessionScoped
public class MilestoneController implements Serializable {

    @EJB
    private com.mycompany.FacadeBeans.MilestoneFacade ejbFacade;
    private List<Milestone> items = null;
    private Milestone selected;
    
    @ManagedProperty("#{projectController}")
    private ProjectController projectController;

    public MilestoneController() {
    }

    public Milestone getSelected() {
        return selected;
    }

    public void setSelected(Milestone selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private MilestoneFacade getFacade() {
        return ejbFacade;
    }

    public Milestone prepareCreate() {
        selected = new Milestone();
        initializeEmbeddableKey();
        return selected;
    }

    public void create(Project p) {
        selected.setProjectId(p);
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("MilestoneCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("MilestoneUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("MilestoneDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Milestone> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }
    
    public List<Milestone> projectMilestones(Project p){
        return ejbFacade.getAllForProject(p);
    }
    
    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Milestone getMilestone(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Milestone> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Milestone> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Milestone.class)
    public static class MilestoneControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MilestoneController controller = (MilestoneController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "milestoneController");
            return controller.getMilestone(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Milestone) {
                Milestone o = (Milestone) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Milestone.class.getName()});
                return null;
            }
        }

    }

}
