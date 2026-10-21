package co.webserv.android.s;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.util.Log;

public class UtilsString {

    /**
     * http://stackoverflow.com/questions/3934331/android-how-to-encrypt-a-
     * string
     * 
     * @param s
     * 
     * @return
     */
    public static String getMd5Hash(String s) {

        MessageDigest digest;

        try {

            digest = MessageDigest.getInstance("MD5");

            digest.update(s.getBytes(), 0, s.length());

            String hash = new BigInteger(1, digest.digest()).toString(16);

            return hash;

        } catch (NoSuchAlgorithmException e) {

            Log.e(UtilsAndroid.LOG_TAG,
                    "UtilsString: getMd5Hash: " + e.getMessage(), e);
        }

        return "";
    }
}