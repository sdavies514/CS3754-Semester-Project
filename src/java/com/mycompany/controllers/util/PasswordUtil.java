/*
 * Created by Casey Butenhoff on 2017.11.18  *
 * Copyright © 2017 Casey Butenhoff. All rights reserved. *
 */
package com.mycompany.controllers.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author CJ
 */
public class PasswordUtil {

    public static String hashpw(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-384");
        byte[] sha384Hash = md.digest(password.getBytes());
        Base64.Encoder encoder = Base64.getEncoder();
        String encodedString = encoder.encodeToString(sha384Hash);
        return BCrypt.hashpw(encodedString, BCrypt.gensalt());
    }

    public static boolean checkpw(String plaintext, String hashed) throws NoSuchAlgorithmException{
        MessageDigest md = MessageDigest.getInstance("SHA-384");
        byte[] sha384Hash = md.digest(plaintext.getBytes());
        Base64.Encoder encoder = Base64.getEncoder();
        String encodedString = encoder.encodeToString(sha384Hash);
        return BCrypt.checkpw(encodedString, hashed);
    }
}
