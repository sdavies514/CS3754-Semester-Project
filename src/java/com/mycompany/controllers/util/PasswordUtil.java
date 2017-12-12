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
 * Hashing is a one-way mathematical transformation which is easy to perform but
 * difficult to reverse. This process allows the system to avoid storing the
 * actual password. Because many users use the same passwords on multiple
 * systems, a security breach that exposes the actual passwords they use puts
 * their accounts at risk at many other sites.
 *
 * Note that encrypted passwords are not as secure as hashed passwords, due to
 * encryption algorithms not being one-way functions. A compromised encryption
 * key (which must necessarily be stored on the system in order to encrypt the
 * passwords in the first place) instantly defeats the security of every
 * encrypted password on the system. Also note that this drawback don't apply to
 * password hashes, so encrypting password hashes can still gain some benefits
 * from encryption while not suffering the security issues, especially if the
 * web application (containing the encryption key) and database are on different
 * servers.
 *
 * When hashing passwords, there are a couple further considerations. Not just
 * any old hashing scheme should be used. Many hash functions, such as SHA-1 for
 * example, are simply too easy to compute. This creates a security problem in
 * that an attacker can simply generate a rainbow table - a hash of many
 * possible passwords - and do reverse-lookup of the compromised hash.
 *
 * One way to mitigate this problem is to generate a unique salt for each
 * password, combine it with the password before hashing it, then store the hash
 * and salt in the database. This makes it harder to generate a single rainbow
 * table that can be used on all the passwords because each one is effectively
 * much longer and more complex due to having a unique salt combined with it.
 * Effectively, a separate rainbow table would have to be generated for each
 * salt. A salt also protects against guessing passwords based on the frequency
 * of a particular hash in the database, since even if two users use the same
 * password their salted hashes will be different.
 *
 * The situation can be made even better by using a hash function that was
 * actually designed for passwords. Such hash functions are intentionally harder
 * to compute than most hash functions, which makes generating rainbow tables
 * vastly more impractical. They also generally include a cost factor which can
 * be increased to make the hash even harder as technology progresses.
 *
 * There are only a handful of hash functions designed for password hashing and
 * currently (as of 2017) recognized as secure and best-practice: Argon2,
 * bcrypt, scrypt, Catena, Lyra2, Makwa, yescrypt, and PBKDF2. PBKDF2 is by far
 * the weakest password hashing scheme in this group.
 *
 * Some of these schemes are available through the well-respected libsodium
 * library or other C libraries via Java Native Access (JNA) or Java Native
 * Interface (JNI) but setup and deployment with these libraries can be tricky.
 * Additionally, many of the Java implementations fail to provide sane defaults
 * for parameters so they leave room for programmer error in providing them.
 *
 * The hash method we chose is based on the bcrypt password hashing scheme. This
 * scheme was chosen because it's currently the most accessible scheme in the
 * Java EE environment. It depends only on a pure-Java library and doesn't
 * require JNA or JNI so deployment is simple and reliable. It provides sane
 * defaults for parameters so its security doesn't depend on the programmer
 * avoiding configuration mistakes.
 *
 * Unfortunately, bcrypt also has a serious drawback. The bcrypt algorithm
 * truncates input at 72 characters and on NUL bytes. In order to mitigate this
 * drawback, we are preprocessing the password using a combination of SHA-384
 * hashing and base64-encoding. A base64-encoded SHA-384 hash is 64 characters,
 * which theoretically reduces the upper limit of input entropy (SHA-384 output
 * contains 384 bits of entropy vs bcrypt input has 573 bits of potential
 * entropy) but few users are going to enter passwords that take advantage of
 * anywhere near this maximum potential entropy. In exchange for this reduction
 * in entropy headspace, we effectively eliminate the upper limit on the length
 * of passwords that we can accept, which has a larger benefit to security in
 * practice than a higher theoretical maximum entropy. Despite the entropy
 * reduction, we use SHA-384 rather than something like SHA-512 because SHA-384
 * has the benefit of being the largest hash function in the SHA-2 family that
 * is resistant to length extension attacks.
 *
 * @author CJ
 */
public class PasswordUtil {

    /**
     * Hashes the given password into a form suitable for storing in the
     * database.
     *
     * @param password The cleartext password.
     * @return The bcrypt hash of the given password in Modular Crypt Format.
     *
     * This format contains:
     *
     * 1. A prefix indicating the hash function used
     *
     * 2. A cost parameter
     *
     * 3. 128-bit salt Radix-64 encoded as 22 characters
     *
     * 4. 184-bit hash value Radix-64 encoded as 31 characters
     *
     * This format has two advantages:
     *
     * 1. We don't need to store the cost parameter, salt, and hash separately
     * in the database.
     *
     * 2. We can more easily upgrade the hash function in the future as
     * technology improves, either by increasing the cost parameter or by
     * completely replacing the hash function, without needing to modify the
     * databse structure. A password can simply be upgraded to a new format,
     * after verifying it against the existing format, by re-hashing it with the
     * new scheme and storing the new result in the databse. Given that all the
     * parameters for a particular hash are stored in the database, the database
     * can easily contain passwords hashed with multiple schemes while they are
     * progressively upgraded.
     *
     * @throws NoSuchAlgorithmException If an implementation of the SHA-384
     * hashing algorithm can't be found on the system.
     */
    public static String hashpw(String password) throws NoSuchAlgorithmException {
        // Get an implementation of the SHA-384 hashing algorithm from
        // MessageDigest and compute the SHA-384 hash of the cleartext password.
        // This converts the password, regardless of its original length, to
        // 384 bits. This compresses long passwords and allows us to accept
        // passwords of arbitrary length and still hash them with bcrypt.
        MessageDigest md = MessageDigest.getInstance("SHA-384");
        byte[] sha384Hash = md.digest(password.getBytes());

        // Bcrypt accepts a character string as input, so encode the binary
        // 384-bit SHA-384 hash as a character string containing 64 characters.
        Base64.Encoder encoder = Base64.getEncoder();
        String encodedString = encoder.encodeToString(sha384Hash);

        // Generate a brand new salt for this user and hash the base64-encoded
        // character string with bcrypt. Return the result in Modular Crypt
        // Format
        return BCrypt.hashpw(encodedString, BCrypt.gensalt());
    }

    /**
     * Hashes the given password using the salt from the given hash
     *
     * @param plaintext The cleartext password.
     * @param hashed The hashed password from the database in Modular Crypt
     * Format.
     * @return True if the hash of the given plaintext password matches the
     * given hash.
     * @throws NoSuchAlgorithmException If an implementation of the SHA-384
     * hashing algorithm can't be found on the system.
     */
    public static boolean checkpw(String plaintext, String hashed) throws NoSuchAlgorithmException {
        // Get an implementation of the SHA-384 hashing algorithm from
        // MessageDigest and compute the SHA-384 hash of the cleartext password.
        // This converts the password, regardless of its original length, to
        // 384 bits. This compresses long passwords and allows us to accept
        // passwords of arbitrary length and still hash them with bcrypt.
        MessageDigest md = MessageDigest.getInstance("SHA-384");
        byte[] sha384Hash = md.digest(plaintext.getBytes());

        // Bcrypt accepts a character string as input, so encode the binary
        // 384-bit SHA-384 hash as a character string containing 64 characters.
        Base64.Encoder encoder = Base64.getEncoder();
        String encodedString = encoder.encodeToString(sha384Hash);

        // Using the salt from the given hashed password, hash the
        // base64-encoded character string with bcrypt and compare it with the
        // given hash.
        return BCrypt.checkpw(encodedString, hashed);
    }
}
