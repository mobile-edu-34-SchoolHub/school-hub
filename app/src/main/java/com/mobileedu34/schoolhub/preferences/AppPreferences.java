package com.mobileedu34.schoolhub.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor prefsEditor;
    public static int PRIVATE_MODE = 0;
    public static final String PREF_NAME = "app_preferences";
    public static final String IS_FIRST_TIME_LAUNCH = "is_first_time_launch";
    public static final String USER_ID = "user_id";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_ROLE = "user_role";
    private Context context;


    public AppPreferences(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        prefsEditor = sharedPreferences.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTimeLaunch) {
        prefsEditor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTimeLaunch);
        prefsEditor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return sharedPreferences.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setUserId(String userId) {
        prefsEditor.putString(USER_ID, userId);
        prefsEditor.commit();
    }

    public String getUserId() {
        return sharedPreferences.getString(USER_ID, null);
    }

    public void setUserEmail(String email) {
        prefsEditor.putString(USER_EMAIL, email);
        prefsEditor.commit();
    }

    public String getUserEmail() {
        return sharedPreferences.getString(USER_EMAIL, null);
    }

    public void setUserRole(int role) {
        prefsEditor.putInt(USER_ROLE, role);
        prefsEditor.commit();
    }

    public int getUserRole() {
        return sharedPreferences.getInt(USER_ROLE, -1);
    }
}


