package com.mycompany.controllers;

import com.mycompany.EntityBeans.Project;
import com.mycompany.EntityBeans.User;
import com.mycompany.EntityBeans.UserProjectAssociation;
import com.mycompany.controllers.util.JsfUtil;
import com.mycompany.controllers.util.JsfUtil.PersistAction;
import com.mycompany.FacadeBeans.ProjectFacade;
import com.mycompany.FacadeBeans.UserFacade;
import com.mycompany.FacadeBeans.UserProjectAssociationFacade;
import com.mycompany.controllers.util.PasswordUtil;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@Named("projectController")
@SessionScoped
public class ProjectController implements Serializable {

    /* The instance variable 'userFacade' is annotated with the @EJB annotation.
    The @EJB annotation directs the EJB Container (of the GlassFish AS) to inject (store) the object reference
    of the UserFacade object, after it is instantiated at runtime, into the instance variable 'userFacade'.
     */
    @EJB
    private ProjectFacade ejbFacade;
    @EJB
    private UserProjectAssociationFacade userProjFacade;
    @EJB
    private UserFacade userFacade;
    private List<Project> items = null;
    private List<Project> userItems = null;
    private Project selected;
    private String cleartextPassword;

    public ProjectController() {
    }

    public Project getSelected() {
        return selected;
    }

    public void setSelected(Project selected) {
        this.selected = selected;
    }

    public boolean associationAlreadyExists(User user, Project project){
        return userProjFacade.associationAlreadyExists(user, project);
    }

    public boolean isSelectedProjectJoined(User currentUser) {
        return userProjFacade.associationAlreadyExists(currentUser, selected);
    }

    /**
     * @return the cleartextPassword
     */
    public String getCleartextPassword() {
        return cleartextPassword;
    }

    /**
     * @param cleartextPassword the cleartextPassword to set
     */
    public void setCleartextPassword(String cleartextPassword) {
        this.cleartextPassword = cleartextPassword;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ProjectFacade getFacade() {
        return ejbFacade;
    }
    
    private UserFacade getUserFacade() {
        return userFacade;
    }
    
    private UserProjectAssociationFacade getUserProjFacade() {
        return userProjFacade;
    }

    public Project prepareCreate() {
        selected = new Project();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() throws NoSuchAlgorithmException {
        // When creating a new project, we must hash the cleartext password and
        // persist the hash in the database instead of the cleartext password.
        selected.setHashedPassword(PasswordUtil.hashpw(cleartextPassword));

        // Once we've hashed the cleartext password, we want to keep it from
        // leaking into other code that uses this controller in the future, and
        // we don't need it anymore so we can clear it.
        cleartextPassword = null;

        // we also derive a key from the password that identifies this project
        // for the purposes of adding some security to its rss feed. But we
        // only want alphanumeric characters, so we remove any non-alphanumeric
        // characters by replacing them with an empty string.
        selected.setRssKey(PasswordUtil.hashpw(selected.getHashedPassword())
                .replaceAll("[^A-Za-z0-9]", ""));

        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ProjectCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ProjectUpdated"));
    }

    public List<Project> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }
    
    public List<Project> getUserItems() {
        if (userItems == null) {
            // Obtain the signed-in user's username
            String usernameOfSignedInUser = (String) FacesContext.getCurrentInstance()
                    .getExternalContext().getSessionMap().get("username");

            // Obtain the object reference of the signed-in user
            User signedInUser = getUserFacade().findByUsername(usernameOfSignedInUser);

            // Obtain only those projects from the database that belong to the signed-in user
            userItems = getUserProjFacade().getProjectsForUser(signedInUser);
        }
        return userItems;
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

    public Project getProject(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Project> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Project> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Project.class)
    public static class ProjectControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProjectController controller = (ProjectController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "projectController");
            return controller.getProject(getKey(value));
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
            if (object instanceof Project) {
                Project o = (Project) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Project.class.getName()});
                return null;
            }
        }

    }

    public boolean joinProject(User currentUser) {
        if (cleartextPassword == null) {
            // A correct project password is required to join a project, so we
            // show an error message if no password is supplied.
            JsfUtil.addErrorMessage("Password was blank");

            // Return false to indicate that a project wasn't joined.
            return false;
        }
        if (currentUser == null) {
            JsfUtil.addErrorMessage("Not logged in");
            // A user must be logged in in order to join a project, so we show
            // an error message if no user is logged in, regardless of whether
            // the project password was correct or not. Since we don't want the
            // cleartext password the user entered to leak into other code that
            // uses this controller, and we don't need it anymore, we can clear
            // it.
            cleartextPassword = null;

            // Return false to indicate that a project wasn't joined.
            return false;
        }
        if (userProjFacade.associationAlreadyExists(currentUser, selected)) {
            JsfUtil.addErrorMessage("You already joined the project");
            // A user may not join a project they have already joined, so we
            // show an error message if the user has already joined the project,
            // regardless of whether the project password was correct or not.
            // Since we don't want the cleartext password the user entered to
            // leak into other code that uses this controller, and we don't need
            // it anymore, we can clear it.
            cleartextPassword = null;

            // Return false to indicate that a project wasn't joined.
            return false;
        }
        try {
            // When joining a project, we must take the cleartext project
            // password the user entered and compare it against the password
            // that's stored in the database for that project. Since we're
            // storing the hashed password in the database, we must salt the
            // cleartext password with the same salt as the password stored in
            // the database and hash it then compare the two hashes. This is all
            // encapsulated within a single call to checkpw, and we only need to
            // pass the cleartext password and the hashed password since the
            // hashed password is stored in Modular Crypt Format which means it
            // includes the cost parameter and salt that was used to hash it.
            if (PasswordUtil.checkpw(cleartextPassword, selected.getHashedPassword())) {

                // Once we've checked the cleartext password, we want to keep it
                // from leaking into other code that uses this controller in the
                // future, and we don't need it anymore so we can clear it.
                cleartextPassword = null;

                // In order to join the user to the project, we must create a
                // new UserProjectAssociation since user-project is a
                // many-to-many relationship (many users can join a particular
                // project and a particular user can join many projects).
                Project currentProj = selected;
                UserProjectAssociation create = new UserProjectAssociation();
                create.setProjectId(currentProj);
                create.setUserId(currentUser);

                // In order to record the UserProjectAssociation in the
                // database, we must pass it to the create method of the
                // UserProjectAssociationFacade, which handles inserting it into
                // the database for us.
                userProjFacade.create(create);

                // The cleartext password matches the hashed password, so show a
                // message to the user indicating that they have successfully
                // joined the project.
                JsfUtil.addSuccessMessage("Sucessfully joined project");

                // Return true to indicate that the user was added to the
                // project.
                return true;
            } else {
                // The cleartext password doesn't match the hashed project
                // password, so show a message to the user indicating as much.
                JsfUtil.addErrorMessage("Incorrect Password");

                // In this case, We clearly don't want to join the user to the
                // project. Even though the cleartext password is incorrect, we
                // still want to keep it from leaking into other code that uses
                // this controller in the future, and we don't need it anymore
                // so we can clear it.
                cleartextPassword = null;

                // Return false to indicate that a project wasn't joined.
                return false;
            }
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }

        // We only reach here if some exception occurred in the code above, and
        // we clearly don't want to join the user to a project in that case.
        // We still want to keep the cleartext password from leaking into other
        // code that uses this controller in the future, and we don't need it
        // anymore so we can clear it.
        cleartextPassword = null;

        // Return false to indicate that a project wasn't joined.
        return false;
    }
}
