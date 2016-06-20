/*
 * Copyright (c) 2016. Cloves Almeida. All rights reserved.
 */

package tt.calories.security;

import org.springframework.security.crypto.codec.Base64;
import java.security.MessageDigest;

/**
 * Class to hash passwords.
 *
 * @author Cloves J. G. Almeida <cjalmeida@gmail.com>
 * @version 1.0.0
 */
public class PasswordHasher {

    /**
     * Hash a (salt, password) using SHA-512.
     */
    public static String hash(String text, String salt) {
        if (text == null) return null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes("UTF-8"));
            byte[] bytes = md.digest(text.getBytes("UTF-8"));
            return new String(Base64.encode(bytes));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
