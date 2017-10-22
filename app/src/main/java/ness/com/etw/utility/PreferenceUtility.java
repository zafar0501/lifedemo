package ness.com.etw.utility;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import ness.com.etw.common.constant.StringConstant;

public class PreferenceUtility {

    public static final String TAG = PreferenceUtility.class.getSimpleName();
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences sharedPreferences_logincredential;
    private static PreferenceUtility preferenceUtility = null;

    public PreferenceUtility(Context context) {
        sharedPreferences = context.getSharedPreferences(StringConstant.IPreference.ETW_PREF, 0);
        sharedPreferences_logincredential = context.getSharedPreferences(StringConstant.IPreference.ETW_PREF_LOGINCRENEDTIAL, 0);
    }

    public static PreferenceUtility getPreferenceInstance(Context context) {

        if (preferenceUtility == null) {
            preferenceUtility = new PreferenceUtility(context);
        }
        return preferenceUtility;

    }

    public static void clearPreferences() {

        sharedPreferences.edit().clear().apply();
        Log.d(TAG, "clearPreferences: " + PreferenceUtility.getAuthToken());
    }

    // Saved Login data in SharedPrefernce
    public static void setLoginData(String comp_id, String user_name, Context context) {
        SharedPreferences.Editor editor = sharedPreferences_logincredential.edit();
        editor.putString(StringConstant.IPreference.USER_COMPANY_ID, comp_id);
        editor.putString(StringConstant.IPreference.USER_NAME, user_name);
        editor.apply();
    }

    public static String getCompanyID() {

        return sharedPreferences_logincredential.getString(StringConstant.IPreference.USER_COMPANY_ID, null);
    }

    public static String getUserName() {
        return sharedPreferences_logincredential.getString(StringConstant.IPreference.USER_NAME, null);
    }

    //region Auth Token
    public static void setAuthToken(String authToken, Context context) {


        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(StringConstant.IPreference.AUTH_TOKEN_PREF, authToken);
        editor.apply();
    }

    public static String getAuthToken() {

        return sharedPreferences.getString(StringConstant.IPreference.AUTH_TOKEN_PREF, null);

    }

    //USER_ID
    public static void setUserID(String userId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(StringConstant.IPreference.USER_UD, userId);
        editor.apply();
    }

    public static String getUserId() {
        return sharedPreferences.getString(StringConstant.IPreference.USER_UD, null);
    }

    // UserInfo...
    public static void setUserInfo(String F_NAME, String L_NAME, String Title, Context context) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(StringConstant.IPreference.USER_FIRST_NAME, F_NAME);
        editor.putString(StringConstant.IPreference.USER_LAST_NAME, L_NAME);
        editor.putString(StringConstant.IPreference.USER_TITLE, Title);
        editor.apply();
    }

    //endregion
    public static String getUserInfoName() {

        String F_NAME, L_NAME, TITLE;
        F_NAME = sharedPreferences.getString(StringConstant.IPreference.USER_FIRST_NAME, null);
        L_NAME = sharedPreferences.getString(StringConstant.IPreference.USER_LAST_NAME, null);
        TITLE = sharedPreferences.getString(StringConstant.IPreference.USER_TITLE, null);

        return F_NAME + " " + L_NAME;

    }

    public static String getUserInfoTitle() {
        return sharedPreferences.getString(StringConstant.IPreference.USER_TITLE, null);
    }

}
