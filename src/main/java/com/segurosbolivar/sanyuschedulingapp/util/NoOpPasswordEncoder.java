package com.segurosbolivar.sanyuschedulingapp.util;

import org.springframework.security.crypto.password.PasswordEncoder;

public class NoOpPasswordEncoder implements PasswordEncoder {

    /**
     * Returns the provided raw password as is, without any encoding.
     *
     * @param rawPassword the raw password to encode
     * @return the same raw password as a string
     */
    @Override
    public String encode(CharSequence rawPassword) {
        return rawPassword.toString();
    }

    /**
     * Compares the provided raw password with the encoded password as plain text.
     *
     * @param rawPassword     the raw password to check
     * @param encodedPassword the encoded password to compare with
     * @return true if the raw password matches the encoded password, false otherwise
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return rawPassword.equals(encodedPassword);
    }

}