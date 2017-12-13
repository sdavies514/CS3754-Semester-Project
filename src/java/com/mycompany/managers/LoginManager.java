/*
 * Created by Casey Butenhoff on 2017.11.18  *
 * Copyright © 2017 Casey Butenhoff. All rights reserved. *
 */
package com.mycompany.managers;

import com.mycompany.EntityBeans.User;
import com.mycompany.FacadeBeans.UserFacade;
import com.mycompany.controllers.util.PasswordUtil;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named(value = "loginManager")
@SessionScoped
/**
 *
 * @author Butenhoff
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

    /*
    The instance variable 'userFacade' is annotated with the @EJB annotation.
    The @EJB annotation directs the EJB Container (of the GlassFish AS) to inject (store) the object reference
    of the UserFacade object, after it is instantiated at runtime, into the instance variable 'userFacade'.
     */
    @EJB
    private UserFacade userFacade;

    // Constructor method instantiating an instance of LoginManager
    public LoginManager() {
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

            // When authenticating a user, we must take the cleartext user
            // password the user entered and compare it against the password
            // that's stored in the database for that user. Since we're
            // storing the hashed password in the database, we must salt the
            // cleartext password with the same salt as the password stored in
            // the database and hash it then compare the two hashes. This is all
            // encapsulated within a single call to checkpw, and we only need to
            // pass the cleartext password and the hashed password since the
            // hashed password is stored in Modular Crypt Format which means it
            // includes the cost parameter and salt that was used to hash it.
            if (!PasswordUtil.checkpw(enteredPassword, actualHashedPassword)) {
                // If the user entered the wrong password, then we must set an
                // error message in order to indicate that the authentication
                // has failed.
                errorMessage = "Invalid Password!";
                return "";
            }

            // If the user entered the right password, then we must clear the
            // error message in order to indicate that the authentication has
            // succeeded.
            errorMessage = "";

            // Initialize the session map with user properties of interest
            initializeSessionMap(user);

            // Redirect to show the Profile page
            return "Profile.xhtml?faces-redirect=true";
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
                getSessionMap().put("username", username);
        FacesContext.getCurrentInstance().getExternalContext().
                getSessionMap().put("user_id", user.getId());
    }

}
