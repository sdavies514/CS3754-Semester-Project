/*
 * Created by Casey Butenhoff on 2017.11.18  *
 * Copyright © 2017 Casey Butenhoff. All rights reserved. *
 */
package com.mycompany.managers;

import com.mycompany.EntityBeans.ProjectFile;
import com.mycompany.EntityBeans.User;
import com.mycompany.EntityBeans.UserFile;
import com.mycompany.FacadeBeans.ProjectFileFacade;
import com.mycompany.FacadeBeans.UserFacade;
import com.mycompany.FacadeBeans.UserFileFacade;
import com.mycompany.controllers.ProjectFileController;

import com.mycompany.controllers.UserFileController;
import javax.inject.Inject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.model.UploadedFile;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author Butenhoff
 */
@Named(value = "fileUploadManager")
@SessionScoped
/**
 *
 * @author Butenhoff
 */
public class FileUploadManager implements Serializable {

    /*
    ===============================
    Instance Variables (Properties)
    ===============================
     */
    private UploadedFile uploadedFile;

    /*
    The instance variable 'userFacade' is annotated with the @EJB annotation.
    The @EJB annotation directs the EJB Container (of the GlassFish AS) to inject (store) the object reference
    of the UserFacade object, after it is instantiated at runtime, into the instance variable 'userFacade'.
     */
    @EJB
    private UserFacade userFacade;

    /*
    The instance variable 'userFileFacade' is annotated with the @EJB annotation.
    The @EJB annotation directs the EJB Container (of the GlassFish AS) to inject (store) the object reference
    of the UserFileFacade object, after it is instantiated at runtime, into the instance variable 'userFileFacade'.
     */
    @EJB
    private UserFileFacade userFileFacade;
    
    /*
    The instance variable 'projectFileFacade' is annotated with the @EJB annotation.
    The @EJB annotation directs the EJB Container (of the GlassFish AS) to inject (store) the object reference
    of the ProjectFileFacade object, after it is instantiated at runtime, into the instance variable 'projectFileFacade'.
     */
    @EJB
    private ProjectFileFacade projectFileFacade;

    /*
    Using the @Inject annotation, the compiler is directed to store the object reference of the
    UserFileController/ProjectFileController/ProjectViewManager CDI-named beans into the instance variables
    userFileController/projectFileController/ProjectViewManager at runtime.
    With this injection, the instance variables and instance methods of the UserFileController/ProjectFileController/ProjectViewManager
    classes can be accessed in this CDI-named bean. The following imports are required for the injection:

        import com.mycompany.controllers.UserFileController;
        import com.mycompany.controllers.ProjectFileController;
        import javax.inject.Inject;
     */
    @Inject
    private UserFileController userFileController;
    
    @Inject
    private ProjectFileController projectFileController;
    
    @Inject
    private ProjectViewManager projectViewManager;

    // Resulting FacesMessage produced
    FacesMessage resultMsg;

    /*
    =========================
    Getter and Setter Methods
    =========================
     */
    // Returns the uploaded uploadedFile
    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    // Obtains the uploaded uploadedFile
    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public UserFacade getUserFacade() {
        return userFacade;
    }

    public UserFileFacade getUserFileFacade() {
        return userFileFacade;
    }
    
    public ProjectFileFacade getProjectFileFacade() {
        return projectFileFacade;
    }

    public UserFileController getUserFileController() {
        return userFileController;
    }
    
    public ProjectFileController getProjectFileController() {
        return projectFileController;
    }
    
    public ProjectViewManager getProjectViewManager() {
        return projectViewManager;
    }

    /*
    ================
    Instance Methods
    ================
     */
    public void handleFileUpload(FileUploadEvent event) throws IOException {

        try {
            String user_name = (String) FacesContext.getCurrentInstance()
                    .getExternalContext().getSessionMap().get("username");

            User user = getUserFacade().findByUsername(user_name);

            /*
            To associate the file to the user, record "userId_filename" in the database.
            Since each file has its own primary key (unique id), the user can upload
            multiple files with the same name.
             */
            String userId_filename = user.getId() + "_" + event.getFile().getFileName();

            /*
            "The try-with-resources statement is a try statement that declares one or more resources.
            A resource is an object that must be closed after the program is finished with it.
            The try-with-resources statement ensures that each resource is closed at the end of the
            statement." [Oracle]
             */
            try (InputStream inputStream = event.getFile().getInputstream();) {

                // The method inputStreamToFile given below writes the uploaded file into the ThoughtwareStorage/FileStorage directory.
                inputStreamToFile(inputStream, userId_filename);
                inputStream.close();
            }

            /*
            Create a new UserFile object with attibutes: (See UserFile table definition inputStream DB)
                <> id = auto generated as the unique Primary key for the user file object
                <> filename = userId_filename
                <> user_id = user
             */
            UserFile newUserFile = new UserFile(userId_filename, user);

            /*
            ==============================================================
            If the userId_filename was used before, delete the earlier file.
            ==============================================================
             */
            List<UserFile> filesFound = getUserFileFacade().findByFilename(userId_filename);

            /*
            If the userId_filename already exists in the database,
            the filesFound List will not be empty.
             */
            if (!filesFound.isEmpty()) {

                // Remove the file with the same name from the database
                getUserFileFacade().remove(filesFound.get(0));
            }

            //---------------------------------------------------------------
            //
            // Create the new UserFile entity (row) in the ThoughtwareDB
            getUserFileFacade().create(newUserFile);

            // This sets the necessary flag to ensure the messages are preserved.
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

            getUserFileController().refreshFileList();

            resultMsg = new FacesMessage("File(s) Uploaded Successfully!");
            FacesContext.getCurrentInstance().addMessage(null, resultMsg);

            // After successful upload, show the UserFiles.xhtml facelets page
            FacesContext.getCurrentInstance().getExternalContext().redirect("UserFiles.xhtml");

        } catch (IOException e) {
            resultMsg = new FacesMessage("Something went wrong during file upload! See: " + e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, resultMsg);
        }

    }
    
    /*
    Slightly modified version of handleFileUpload from above to replace UserFiles
    with ProjectFiles.
    */
    public void handleProjectFileUpload(FileUploadEvent event) throws IOException {

        try {
            /*
            To associate the file to the project, record "projId_filename" in the database.
            Since each file has its own primary key (unique id), the project members can upload
            multiple files with the same name.
             */
            String projId_filename = projectViewManager.getSelected().getId() + "_" + event.getFile().getFileName();

            /*
            "The try-with-resources statement is a try statement that declares one or more resources.
            A resource is an object that must be closed after the program is finished with it.
            The try-with-resources statement ensures that each resource is closed at the end of the
            statement." [Oracle]
             */
            try (InputStream inputStream = event.getFile().getInputstream();) {
                // The method inputStreamToProjectFile given below writes the uploaded file into the Team7-FileStorage/ProjectFiles directory.
                inputStreamToProjectFile(inputStream, projId_filename);
                inputStream.close();
            }
            
            File fp = new File(Constants.PROJECT_FILES_ABSOLUTE_PATH, projId_filename);
            String file_size;
            
            /*
            Calculate the size of the file in human readable format.  File uploads max at 10,000,000 bytes ~10MB,
            so only KB / MB need be considered.
            */
            int resized = (int) fp.length();
            if (resized < 1024) {
                file_size = resized + " B";
            }
            else { 
                if (resized / 1024 < 1024) { // Represents KB
                    file_size = (resized / 1024) + " KB";
                }
                else {
                    file_size = ((resized / 1024) / 1024) + " MB";
                }
            }

            /*
            Create a new ProjectFile object with attibutes: (See ProjectFile table definition inputStream DB)
                <> id = auto generated as the unique Primary key for the project file object
                <> file_location = projId_filename
                <> project_id = project selected from tiered menu to navigate to this project's pages
                <> file_size = a human readable string representation of the file size
             */
            ProjectFile newProjectFile = new ProjectFile(projId_filename, projectViewManager.getSelected(), file_size);

            /*
            ==============================================================
            If the projId_filename was used before, delete the earlier file.
            ==============================================================
             */
            List<ProjectFile> filesFound = getProjectFileFacade().findByFileLocation(projId_filename);

            /*
            If the projId_filename already exists in the database,
            the filesFound List will not be empty.
             */
            if (!filesFound.isEmpty()) {

                // Remove the file with the same name from the database
                getProjectFileFacade().remove(filesFound.get(0));
            }

            //---------------------------------------------------------------
            //
            // Create the new ProjectFile entity (row) in the ThoughtwareDB
            getProjectFileFacade().create(newProjectFile);

            // This sets the necessary flag to ensure the messages are preserved.
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

            getProjectFileController().refreshFileList();

            resultMsg = new FacesMessage("File(s) Uploaded Successfully!");
            FacesContext.getCurrentInstance().addMessage(null, resultMsg);

            // After successful upload, show the UserFiles.xhtml facelets page
            FacesContext.getCurrentInstance().getExternalContext().redirect("ViewProjectFiles.xhtml");

        } catch (IOException e) {
            resultMsg = new FacesMessage("Something went wrong during file upload! See: " + e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, resultMsg);
        }
    }

    // Show the User File Upload Page
    public String showFileUploadPage() {

        return "UploadFile?faces-redirect=true";
    }
    
    // Show the Project File Upload Page
    public String showProjectFileUploadPage() {

        return "UploadProjectFile?faces-redirect=true";
    }

    public void upload() throws IOException {

        if (getUploadedFile().getSize() != 0) {
            copyFile(getUploadedFile());
        }
    }

    /**
     * cancel an upload
     *
     * @return
     */
    public String cancel() {
        return "Profile?faces-redirect=true";
    }

    /**
     * Used to copy a uploadedFile
     *
     * @param file
     * @return
     * @throws java.io.IOException
     */
    public FacesMessage copyFile(UploadedFile file) throws IOException {
        try {
            String user_name = (String) FacesContext.getCurrentInstance()
                    .getExternalContext().getSessionMap().get("username");

            User user = getUserFacade().findByUsername(user_name);

            /*
            To associate the file to the user, record "userId_filename" in the database.
            Since each file has its own primary key (unique id), the user can upload
            multiple files with the same name.
             */
            String userId_filename = user.getId() + "_" + file.getFileName();

            /*
            "The try-with-resources statement is a try statement that declares one or more resources.
            A resource is an object that must be closed after the program is finished with it.
            The try-with-resources statement ensures that each resource is closed at the end of the
            statement." [Oracle]
             */
            try (InputStream inputStream = file.getInputstream();) {

                // The method inputStreamToFile is given below.
                inputStreamToFile(inputStream, userId_filename);
                inputStream.close();
            }

            resultMsg = new FacesMessage("");  // No need to show a result message

        } catch (IOException e) {
            resultMsg = new FacesMessage("Something went wrong during file copy! See:" + e.getMessage());
        }

        // This sets the necessary flag to ensure the messages are preserved.
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

        FacesContext.getCurrentInstance().addMessage(null, resultMsg);
        return resultMsg;
    }

    private File inputStreamToFile(InputStream inputStream, String file_name)
            throws IOException {

        // Read the series of bytes from the input stream
        byte[] buffer = new byte[inputStream.available()];
        inputStream.read(buffer);

        // Write the series of bytes on uploadedFile.
        File targetFile = new File(Constants.USER_FILES_ABSOLUTE_PATH, file_name);

        OutputStream outStream;
        outStream = new FileOutputStream(targetFile);
        outStream.write(buffer);
        outStream.close();

        return targetFile;
    }
    
    private File inputStreamToProjectFile(InputStream inputStream, String file_name)
            throws IOException {

        // Read the series of bytes from the input stream
        byte[] buffer = new byte[inputStream.available()];
        inputStream.read(buffer);

        // Write the series of bytes on uploadedFile.
        File targetFile = new File(Constants.PROJECT_FILES_ABSOLUTE_PATH, file_name);

        OutputStream outStream;
        outStream = new FileOutputStream(targetFile);
        outStream.write(buffer);
        outStream.close();

        return targetFile;
    }

    /**
     * Sets the file location
     *
     * @param data
     */
    public void setFileLocation(UserFile data) {

        String fileName = data.getFilename();

        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.getFlash().put("data", data);
    }

    public String getFileLocation() {

        return Constants.USER_FILES_ABSOLUTE_PATH;
    }

    /**
     * Used to return the file extension for a file.
     *
     * @param filename
     * @return
     */
    public static String getExtension(String filename) {

        if (filename == null) {
            return null;
        }
        int extensionPos = filename.lastIndexOf('.');

        int lastUnixPos = filename.lastIndexOf('/');
        int lastWindowsPos = filename.lastIndexOf('\\');
        int lastSeparator = Math.max(lastUnixPos, lastWindowsPos);
        int index = lastSeparator > extensionPos ? -1 : extensionPos;

        if (index == -1) {
            return "";
        } else {
            return filename.substring(index + 1);
        }
    }

}
