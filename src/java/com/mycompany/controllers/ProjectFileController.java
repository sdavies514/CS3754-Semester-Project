/*
 * Created by Kyle Dyess on 2017.12.12  * 
 * Copyright © 2017 Kyle Dyess. All rights reserved. * 
 */
package com.mycompany.controllers;

import com.mycompany.EntityBeans.ProjectFile;
import com.mycompany.FacadeBeans.ProjectFacade;
import com.mycompany.FacadeBeans.ProjectFileFacade;
import com.mycompany.controllers.util.JsfUtil;
import com.mycompany.controllers.util.JsfUtil.PersistAction;
import com.mycompany.managers.Constants;
import com.mycompany.managers.ProjectViewManager;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Kyle Dyess
 */

@Named("projectFileController")
@SessionScoped

public class ProjectFileController implements Serializable {
    /*
    ===============================
    Instance Variables (Properties)
    ===============================

    The instance variable 'projectFacade' is annotated with the @EJB annotation.
    The @EJB annotation directs the EJB Container (of the GlassFish AS) to inject (store) the object reference
    of the ProjectFacade object, after it is instantiated at runtime, into the instance variable 'projectFacade'.
     */
    @EJB
    private ProjectFacade projectFacade;

    /*
    The instance variable 'projectFileFacade' is annotated with the @EJB annotation.
    The @EJB annotation directs the EJB Container (of the GlassFish AS) to inject (store) the object reference
    of the ProjectFileFacade object, after it is instantiated at runtime, into the instance variable 'projectFileFacade'.
     */
    @EJB
    private ProjectFileFacade projectFileFacade;
    
    /*
    Using the @Inject annotation, the compiler is directed to store the object reference of the
    ProjectViewManager CDI-named bean into the instance variable
    projectViewManager at runtime.  With this injection, the instance variables and instance methods of the 
    ProjectViewManager class can be accessed in this CDI-named bean. The following imports are required for the injection:

        import com.mycompany.managers.ProjectViewManager;
        import javax.inject.Inject;
     */
    @Inject
    private ProjectViewManager projectViewManager;

    // selected = Selected ProjectFile object
    private ProjectFile selected;

    // items = list of ProjectFile objects
    private List<ProjectFile> items = null;

    /*
    cleanedFileNameHashMap<KEY, VALUE>
        KEY   = Integer fileId
        VALUE = String cleanedFileNameForSelected
     */
    HashMap<Integer, String> cleanedFileNameHashMap = null;

    // Message to show when file type cannot be processed
    private String fileTypeMessage = "";

    private TreeNode root;
    private TreeNode selectedNode;
    
    /*
    ==================
    Constructor Method
    ==================
     */
    public ProjectFileController() {
    }

    /*
    =========================
    Getter and Setter Methods
    =========================
     */
    public ProjectFacade getProjectFacade() {
        return projectFacade;
    }

    public ProjectFileFacade getProjectFileFacade() {
        return projectFileFacade;
    }

    public ProjectFile getSelected() {
        return selected;
    }

    public void setSelected(ProjectFile selected) {
        this.selected = selected;
    }
    
    public TreeNode getSelectedNode() {
        return selectedNode;
    }
    
    public void setSelectedNode(TreeNode select) {
        this.selectedNode = select;
    }
    
    @PostConstruct
    public void init() {
        root = new DefaultTreeNode(new ProjectFile());
        for(ProjectFile pfile: getItems(projectViewManager.getSelected().getId())) {
            new DefaultTreeNode(pfile, root);
        }
    }
    
    public TreeNode getRoot() {
        return root;
    }

    public String getFileTypeMessage() {
        return fileTypeMessage;
    }

    public void setFileTypeMessage(String fileTypeMessage) {
        this.fileTypeMessage = fileTypeMessage;
    }

    public List<ProjectFile> getItems(Integer projectId) {

        if (items == null) {
            // Obtain only those files from the database that belong to the selected project
            items = getProjectFileFacade().findProjectFilesByProjectID(projectId);

            // Instantiate a new hash map object
            cleanedFileNameHashMap = new HashMap<>();

            /*
            cleanedFileNameHashMap<KEY, VALUE>
                KEY   = Integer fileId
                VALUE = String cleanedFileNameForSelected
             */
            for (int i = 0; i < items.size(); i++) {

                // Obtain the filename stored in Team7-FileStorage/ProjectFiles as 'projectId_filename'
                String storedFileName = items.get(i).getFilePath();

                // Remove the "projectId_" (e.g., "4_") prefix in the stored filename
                String cleanedFileName = storedFileName.substring(storedFileName.indexOf("_") + 1);

                // Obtain the file id
                Integer fileId = items.get(i).getId();

                // Create an entry in the hash map as a key-value pair
                cleanedFileNameHashMap.put(fileId, cleanedFileName);
            }
        }
        return items;
    }

    protected void setEmbeddableKeys() {
    }

    public ProjectFile getProjectFile(Integer id) {
        return getProjectFileFacade().find(id);
    }

    public List<ProjectFile> getItemsAvailableSelectMany() {
        return getProjectFileFacade().findAll();
    }

    public List<ProjectFile> getItemsAvailableSelectOne() {
        return getProjectFileFacade().findAll();
    }
    
    /*
    ================
    Instance Methods
    ================
     */
    public ProjectFile prepareCreate() {
        selected = new ProjectFile();
        initializeEmbeddableKey();
        return selected;
    }

    protected void initializeEmbeddableKey() {
    }
    
    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ProjectFileCreated"));

        /*
        JsfUtil.isValidationFailed() returns TRUE if the validationFailed() method has been called
        for the current request. Return of FALSE means that the create operation was successful and
        we can reset items to null so that it will be recreated with the newly created project file.
         */
        if (!JsfUtil.isValidationFailed()) {
            items = null;
        }
    }
    
    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ProjectFileUpdated"));
    }
    
    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ProjectFileDeleted"));

        /*
        JsfUtil.isValidationFailed() returns TRUE if the validationFailed() method has been called
        for the current request. Return of FALSE means that the destroy operation was successful and
        we can reset items to null so that it will be recreated without the destroyed project file.
         */
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;
        }
    }

    private void persist(PersistAction persistAction, String successMessage) {

        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getProjectFileFacade().edit(selected);
                } else {
                    getProjectFileFacade().remove(selected);
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
    
    @FacesConverter(forClass = ProjectFile.class)
    public static class ProjectFileControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProjectFileController controller = (ProjectFileController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "projectFileController");
            return controller.getProjectFile(getKey(value));
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
            if (object instanceof ProjectFile) {
                ProjectFile o = (ProjectFile) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), ProjectFile.class.getName()});
                return null;
            }
        }
    }
    
    /*
    =========================
    Delete Selected Project File
    =========================
     */
    public String deleteSelectedProjectFile() {

        ProjectFile projectFileToDelete = (ProjectFile) selectedNode.getData();

        FacesMessage resultMsg;

        // This sets the necessary flag to ensure the messages are preserved.
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

        if (projectFileToDelete == null) {
            resultMsg = new FacesMessage("This project does not have a file to delete!");
        } else {
            try {
                // Delete the file from Team7-FileStorage/ProjectFiles/
                Files.deleteIfExists(Paths.get(projectFileToDelete.getFilePath()));

                // Delete the project file record from the ThoughtwareDB database
                getProjectFileFacade().remove(projectFileToDelete);
                // ProjectFileFacade inherits the remove() method from AbstractFacade

                resultMsg = new FacesMessage("Selected file is successfully deleted!");

                // See method below
                refreshFileList();

            } catch (IOException e) {
                resultMsg = new FacesMessage("Something went wrong while deleting the project file! See: " + e.getMessage());
                FacesContext.getCurrentInstance().addMessage(null, resultMsg);
            }
        }

        FacesContext.getCurrentInstance().addMessage(null, resultMsg);

        return "ProjectFiles?faces-redirect=true";
    }

    /*
    ========================
    Refresh Project's File List
    ========================
     */
    public void refreshFileList() {
        /*
        By setting the items to null, we force the getItems
        method above to retrieve all of the project's files again.
         */
        items = null;
        init();
    }
    
    /*
    =====================================
    Return Cleaned Filename given File Id
    =====================================
     */
    // This method is called from ViewProjectFiles.xhtml by passing the fileId as a parameter.
    public String cleanedFilenameForFileId(Integer fileId) {
        /*
        cleanedFileNameHashMap<KEY, VALUE>
            KEY   = Integer fileId
            VALUE = String cleanedFileNameForSelected
         */

        // Obtain the cleaned filename for the given fileId
        String cleanedFileName = cleanedFileNameHashMap.get(fileId);

        return cleanedFileName;
    }

    /*
    =========================================
    Return Cleaned Filename for Selected File
    =========================================
     */
    public String cleanedFileNameForSelected() {

        Integer fileId = selected.getId();
        /*
        cleanedFileNameHashMap<KEY, VALUE>
            KEY   = Integer fileId
            VALUE = String cleanedFileNameForSelected
         */

        // Obtain the cleaned filename for the given fileId
        String cleanedFileName = cleanedFileNameHashMap.get(fileId);

        return cleanedFileName;
    }

    /*
    ====================================
    Return Selected File's Relative Path
    ====================================
     */
    public String selectedFileRelativePath() {
        return Constants.PROJECT_FILES_RELATIVE_PATH + selected.getFileLocation();
    }
}
