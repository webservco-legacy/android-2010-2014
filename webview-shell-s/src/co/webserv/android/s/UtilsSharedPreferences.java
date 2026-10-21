package co.webserv.android.s;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class UtilsSharedPreferences {

    /**
     * SharedPreferences.
     */
    private static SharedPreferences mSharedPreferences;

    private static void _init(Context c) {

        if (!(mSharedPreferences instanceof SharedPreferences)) {

            UtilsAndroid.logDebug(c, "UtilsSharedPreferences: init");

            mSharedPreferences = c.getSharedPreferences(c.getPackageName(),
                    Activity.MODE_PRIVATE);
        } else {

            UtilsAndroid.logDebug(c,
                    "UtilsSharedPreferences: init: already set");
        }
    }

    public static String getString(Context c, String key, String defValue) {

        _init(c);

        UtilsAndroid.logDebug(c, "UtilsSharedPreferences: getString: " + key);

        return mSharedPreferences.getString(key, defValue);
    }

    public static int getInt(Context c, String key, int defValue) {

        _init(c);

        UtilsAndroid.logDebug(c, "UtilsSharedPreferences: getInt: " + key);

        return mSharedPreferences.getInt(key, defValue);
    }

    public static boolean getBoolean(Context c, String key, boolean defValue) {

        _init(c);

        UtilsAndroid.logDebug(c, "UtilsSharedPreferences: getBoolean: " + key);

        return mSharedPreferences.getBoolean(key, defValue);
    }

    public static void putString(Context c, String key, String value) {

        _init(c);

        UtilsAndroid.logDebug(c, "UtilsSharedPreferences: putString: " + key);

        mSharedPreferences.edit().putString(key, value).apply();
    }

    public static void putInt(Context c, String key, int value) {

        _init(c);

        UtilsAndroid.logDebug(c, "UtilsSharedPreferences: putInt: " + key);

        mSharedPreferences.edit().putInt(key, value).apply();
    }

    public static void putBoolean(Context c, String key, boolean value) {

        _init(c);

        UtilsAndroid.logDebug(c, "UtilsSharedPreferences: putBoolean: " + key);

        mSharedPreferences.edit().putBoolean(key, value).apply();
    }

    public static void remove(Context c, String key) {

        _init(c);

        UtilsAndroid.logDebug(c, "UtilsSharedPreferences: remove: " + key);

        mSharedPreferences.edit().remove(key).apply();
    }
}