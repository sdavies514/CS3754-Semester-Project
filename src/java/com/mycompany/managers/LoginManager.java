/*
 * Created by Shane Davies on 2017.11.18  *
 * Copyright © 2017 Shane Davies. All rights reserved. *
 */
package com.mycompany.managers;

import com.mycompany.EntityBeans.User;
import com.mycompany.FacadeBeans.UserFacade;
import com.mycompany.controllers.util.PasswordUtil;
import java.io.IOException;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.inject.Inject;

@Named(value = "loginManager")
@SessionScoped
/**
 *
 * @author Davies
 */
public class LoginManager implements Serializable {

    /*
    ===============================
    Instance Variables (Properties)
    ===============================
     */
    private String username;
    private String password;
    private String errorMessage;
    private String googleUsername;
    private String googleFirstName;
    private String googleLastName;
    private String googleImageUrl;
    private String googleId;

    /*
    The instance variable 'userFacade' is annotated with the @EJB annotation.
    The @EJB annotation directs the EJB Container (of the GlassFish AS) to inject (store) the object reference
    of the UserFacade object, after it is instantiated at runtime, into the instance variable 'userFacade'.
     */
    @EJB
    private UserFacade userFacade;

    @Inject
    private AccountManager accountManager;

    // Constructor method instantiating an instance of LoginManager
    public LoginManager() {
        //accountManager = new AccountManager();
    }

    /*
    =========================
    Getter and Setter Methods
    =========================
     */
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGoogleUsername() {
        return googleUsername;
    }

    public void setGoogleUsername(String username) {
        this.googleUsername = username;
        System.out.println(username);
    }

    public String getGoogleFirstName() {
        return googleFirstName;
    }

    public void setGoogleFirstName(String googleFirstName) {
        this.googleFirstName = googleFirstName;
    }

    public String getGoogleLastName() {
        return googleLastName;
    }

    public void setGoogleLastName(String googleLastName) {
        this.googleLastName = googleLastName;
    }

    public String getGoogleImageUrl() {
        return googleImageUrl;
    }

    public void setGoogleImageUrl(String googleImageUrl) {
        this.googleImageUrl = googleImageUrl;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public UserFacade getUserFacade() {
        return userFacade;
    }

    public AccountManager getAccountManager() {
        return accountManager;
    }

    /*
    ================
    Instance Methods
    ================
     */
    public String createUser() {

        // Redirect to show the CreateAccount page
        return "CreateAccount.xhtml?faces-redirect=true";
    }

    public String resetPassword() {

        // Redirect to show the EnterUsername page
        return "EnterUsername.xhtml?faces-redirect=true";
    }

    /*
    Sign in the user if the entered username and password are valid
    @return "" if an error occurs; otherwise, redirect to show the Profile page
     */
    public String loginUser() throws NoSuchAlgorithmException {

        System.out.println(username);
        // Obtain the object reference of the User object from the entered username
        User user = getUserFacade().findByUsername(getUsername());

        if (user == null) {
            errorMessage = "Entered username " + getUsername() + " does not exist!";
            return "";

        } else {
            String actualUsername = user.getUsername();
            String enteredUsername = getUsername();

            String actualHashedPassword = user.getHashedPassword();
            String enteredPassword = getPassword();

            if (!actualUsername.equals(enteredUsername)) {
                errorMessage = "Invalid Username!";
                return "";
            }

            if (!PasswordUtil.checkpw(enteredPassword, actualHashedPassword)) {
                errorMessage = "Invalid Password!";
                return "";
            }

            errorMessage = "";

            // Initialize the session map with user properties of interest
            initializeSessionMap(user);
            FacesContext.getCurrentInstance().getExternalContext().
                    getSessionMap().put("isGoogleAccount", false);

            // Redirect to show the Profile page
            return "Profile.xhtml?faces-redirect=true";
        }
    }

    /**
     * Logs the user into the application much like the loginUser() method above.
     * The main difference between the two methods is this method will create a 
     * new user if the user has not loged into the application before.
     * This allows for a seamless experience for Google users as they are not forced
     * to fill out repetitive information during the account creation process.
     * @return
     * @throws NoSuchAlgorithmException 
     */
    public String signedInWithGoogle() throws NoSuchAlgorithmException {

        // Obtain the object reference of the User object from the entered username
        User user = getUserFacade().findByUsername(getGoogleUsername());

        //System.out.println("goog: " + user.toString());
        if (user == null) {
            accountManager.setFirstName(googleFirstName);
            accountManager.setLastName(googleLastName);
            accountManager.setUsername(googleUsername);
            accountManager.setEmail(googleUsername);
            accountManager.setAddress1("Google account, please update!");
            accountManager.setCity("Google account, please update!");
            accountManager.setState("AK");
            accountManager.setZipcode("00000");
            accountManager.setSecurityQuestion(0);
            accountManager.setSecurityAnswer("Google account, please update!");
            accountManager.setPassword("Google account, please update!");
            accountManager.setGoogleImageUrl(googleImageUrl);

            accountManager.createAccount();
            
            User newUser = getUserFacade().findByUsername(getGoogleUsername());
            initializeSessionMap(newUser);
        } else {
            errorMessage = "";

            // Initialize the session map with user properties of interest
            initializeSessionMap(user);

            FacesContext.getCurrentInstance().getExternalContext().
                    getSessionMap().put("isGoogleAccount", true);
            // Redirect to show the Profile page
            return "Profile.xhtml?faces-redirect=true";
        }

        // Redirect to show the Profile page
        return "Profile.xhtml?faces-redirect=true";
    }

    /**
     * Invalidates user session if there is a user currently logged in and redirects 
     * them to the home page.  Used by Primefaces IdleMonitor to log the user out
     * after 1 hour of inactivity as per project specification.
     * @throws IOException 
     */
    public void timeout() throws IOException {
        if (accountManager.isLoggedIn()) {
            FacesContext.getCurrentInstance().getExternalContext()
                    .invalidateSession();
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("index.xhtml");
        }
    }

    /*
    Initialize the session map with the user properties of interest,
    namely, first_name, last_name, username, and user_id.
    user_id = primary key of the user entity in the database
     */
    public void initializeSessionMap(User user) {
        FacesContext.getCurrentInstance().getExternalContext().
                getSessionMap().put("first_name", user.getFirstName());
        FacesContext.getCurrentInstance().getExternalContext().
                getSessionMap().put("last_name", user.getLastName());
        FacesContext.getCurrentInstance().getExternalContext().
                getSessionMap().put("username", user.getUsername());
        FacesContext.getCurrentInstance().getExternalContext().
                getSessionMap().put("user_id", user.getId());
    }
}
