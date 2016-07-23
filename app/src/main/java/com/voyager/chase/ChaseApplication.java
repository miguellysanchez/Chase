package com.voyager.chase;

import android.app.Application;
import android.content.Intent;

import com.voyager.chase.mqtt.MqttService;

import timber.log.Timber;

/**
 * Created by miguellysanchez on 6/26/16.
 */
public class ChaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if(BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree(){
                @Override
                protected String createStackElementTag(StackTraceElement element) {
                    return super.createStackElementTag(element) + ":"+element.getLineNumber();
                }
            });
        }
        Intent startMqttServiceIntent = new Intent(this, MqttService.class);
        startService(startMqttServiceIntent);
    }


}
