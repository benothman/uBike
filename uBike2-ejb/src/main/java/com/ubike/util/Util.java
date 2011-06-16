/*
 * Copyright 2011, Nabil Benothman, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.ubike.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import sun.misc.BASE64Encoder;

/**
 * <p> This class is used to encrypt passwords and verifying mail addresses, in this class we use
 * tow algorithms combined to end with a final password witch has as a length
 * of 90 character. this tow algorithms are <u>SHA_512</u> and <u>MD5</u>
 * </p>
 * 
 * @author BENOTHMAN Nabil.
 */
public final class Util {

    /**
     *
     */
    public static final String SHA_512 = "SHA-512";
    public static final String SHA_384 = "SHA-384";
    public static final String SHA_256 = "SHA-256";
    public static final String MD5 = "MD5";
    private static final Random rand = new Random();
    private static final Calendar cal = Calendar.getInstance();

    /**
     * Create a new instance of {@code Util}
     */
    private Util() {
    }

    /**
     * get an encrypted representation of the String given as parameter.
     * @param plaintext
     * @return The cipher text
     */
    public static String encrypt(String plaintext) {

        try {
            String str1 = encrypt(plaintext, SHA_512);
            String str2 = encrypt(plaintext, MD5);
            return (plaintext = merge(str1.substring(0, str1.length() - 1),
                    str2.substring(0, str2.length() - 2)));
            //plaintext += plaintext;
        } catch (IOException exp) {
            System.err.println(exp.getMessage());
            return "";
        }
    }

    /**
     * Encrypt the given plain text using the given algorithm.
     * 
     * @param plaintext The text to encrypt
     * @param algo The algorithm used for the encryption
     * @return a String representation of the cipher text
     * @throws IOException
     */
    public static String encrypt(String plaintext, String algo) throws IOException {
        MessageDigest md = null; // step 1
        try {
            md = MessageDigest.getInstance(algo);   // step 2
            md.update(plaintext.getBytes("UTF-8")); // step 3
        } catch (NoSuchAlgorithmException e) {
            throw new IOException(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            throw new IOException(e.getMessage());
        }

        byte raw[] = md.digest(); // step 4
        String hash = (new BASE64Encoder()).encode(raw); // step 5

        return hash; // step 6
    }

    /**
     * Merge the tow given Strings
     * @param str1
     * @param str2
     * @return String representation of the merge result.
     */
    public static String merge(String str1, String str2) {

        char tab[] = new char[str1.length() + str2.length()];
        int n = 0, p = 0, tmp = 0;

        while (n < str1.length() && p < str2.length()) {
            tmp = n + p;
            tab[tmp] = str1.charAt(n++);
            tab[tmp + 1] = str2.charAt(p++);
        }

        while (n < str1.length()) {
            tab[n + p] = str1.charAt(n++);
        }
        while (p < str2.length()) {
            tab[n + p] = str2.charAt(p++);
        }

        return new String(tab);
    }

    /**
     * Generate a pseudo-random String with the given length
     *
     * @return a pseudo-random generated String.
     */
    public static String getRandomString(int length) throws Exception {
        if (length <= 0) {
            throw new Exception("Negative or null length value");
        }

        return Integer.toHexString(rand.nextInt());
    }

    /**
     *
     * @param date
     * @return
     */
    public static String formatTimestamp(Date date) {
        return DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(date);
    }

    /**
     * @param date
     * @return a formated String from the given date
     */
    public static String formatDate(Date date) {
        return DateFormat.getDateTimeInstance().format(date);
    }

    /**
     *
     * @return
     */
    public static Calendar today() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    /**
     * 
     * @param date
     * @return 
     */
    public static Date dateMidnight(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 
     * @param duration
     * @return
     */
    public static String formatDuration(long duration) {
        long h = duration / 3600;
        long m = (duration % 3600) / 60;
        return h + "h" + m + "min";
    }
}
