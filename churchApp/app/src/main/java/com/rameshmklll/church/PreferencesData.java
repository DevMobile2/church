package com.rameshmklll.church;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by venkatesh on 13/12/17.
 */

public class PreferencesData {

    private static final String PREF_PROVIDER_TYPE = "provider_type";
    private static SharedPreferences preferences;
    public static final String SHARED_PREF="credentials";
    private static SharedPreferences.Editor editor;
    private static final String IS_LOGGED_IN = "is_logged_in";

    public static synchronized void initPrefs(Context context) {
        if (preferences == null) {
            preferences = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        }
    }
    public static SharedPreferences getSharedPreferences()
    {
        if(preferences!=null)
            return preferences;
        else
            throw new RuntimeException("Prefs class not correctly instantiated please call Prefs.iniPrefs(context) in the Application class onCreate.");
    }

    public static void putProviderType(Context con, String type)
    {
        if(preferences==null)
            preferences=con.getSharedPreferences(SHARED_PREF, 0);
        editor=preferences.edit();
        editor.putString(PREF_PROVIDER_TYPE, type);
        editor.apply();
    }

    public static String getProviderType(Context con)
    {
        if(preferences==null)
            preferences = con.getSharedPreferences(SHARED_PREF, 0);
        return preferences.getString(PREF_PROVIDER_TYPE, "");
    }

    public static void putLoggedIn(Context con, boolean bool) {
        if(preferences==null)
            preferences=con.getSharedPreferences(SHARED_PREF, 0);
        editor=preferences.edit();
        editor.putBoolean(IS_LOGGED_IN, bool);
        editor.apply();
    }

    public static boolean getLoggedIn(Context con)
    {
        if(preferences==null)
            preferences = con.getSharedPreferences(SHARED_PREF, 0);
        return preferences.getBoolean(IS_LOGGED_IN, false);
    }



}
