package com.voyager.chase.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by miguellysanchez on 6/29/16.
 */
@SuppressLint("CommitPrefEdits")
public class PreferenceUtility {

    private static final String FILENAME = "com.voyager.chase.preferences";

    private static final String KEY_STRING_MQTT_USER_ID = "mqtt_user_id";
    private static final String KEY_STRING_GAME_ROLE = "game_role";
    private static final String KEY_STRING_GAME_SESSION_ID = "game_session_id";

    private final SharedPreferences mSharedPreferences;
    private static PreferenceUtility sInstance;

    private PreferenceUtility(Context context) {
        mSharedPreferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
    }

    public static PreferenceUtility getInstance(Context context) {
        if (sInstance == null) {
            synchronized (PreferenceUtility.class) {
                if (sInstance == null) {
                    sInstance = new PreferenceUtility(context);
                }
            }
        }
        return sInstance;
    }

    public void clear() {
        mSharedPreferences.edit().clear().commit();
    }

    public String getMqttUserId(){
        return mSharedPreferences.getString(KEY_STRING_MQTT_USER_ID, null);
    }

    public void setMqttUserId(String mqttUserId){
        mSharedPreferences.edit().putString(KEY_STRING_MQTT_USER_ID, mqttUserId).commit();
    }

    public String getGameRole(){
        return mSharedPreferences.getString(KEY_STRING_GAME_ROLE, null);
    }

    public void setGameRole(String gameRole){
        mSharedPreferences.edit().putString(KEY_STRING_GAME_ROLE, gameRole).commit();
    }

    public String getGameSessionId() {
        return mSharedPreferences.getString(KEY_STRING_GAME_SESSION_ID, null);
    }

    public void setGameSessionId(String gameSessionId){
        mSharedPreferences.edit().putString(KEY_STRING_GAME_SESSION_ID, gameSessionId).commit();
    }
}
