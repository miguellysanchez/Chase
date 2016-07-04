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

    private static final String KEY_STRING_MQTT_CLIENT_ID = "mqtt_client_id";

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

    public String getMqttClientId(){
        return mSharedPreferences.getString(KEY_STRING_MQTT_CLIENT_ID, null);
    }

    public void setMqttClientId(String mqttClientId){
        mSharedPreferences.edit().putString(KEY_STRING_MQTT_CLIENT_ID, mqttClientId).commit();
    }
}
