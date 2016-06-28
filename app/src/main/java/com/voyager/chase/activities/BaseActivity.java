package com.voyager.chase.activities;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

import com.voyager.chase.mqtt.listeners.MqttActionMessageListener;
import com.voyager.chase.mqtt.MqttService;
import com.voyager.chase.mqtt.listeners.MqttMessageCallbackListener;
import com.voyager.chase.utility.BroadcastUtility;


/**
 * Created by miguellysanchez on 6/26/16.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private MqttService mMqttService;
    private boolean isServiceBound = false;

    private BroadcastReceiver mMqttCallbackReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            executeMqttCallbackAction(intent);
        }
    };

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder binder) {
            mMqttService = ((MqttService.LocalBinder) binder).getService();
            isServiceBound = true;
            onMqttServiceConnected();
        }

        public void onServiceDisconnected(ComponentName className) {
            isServiceBound = false;
        }
    };

    protected abstract void onMqttServiceConnected();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MqttService.class);
        if(!MqttService.isServiceRunning()){
            startService(intent);
        }
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        IntentFilter mqttCallbackIntentFilter = new IntentFilter();
        mqttCallbackIntentFilter.addAction(BroadcastUtility.KEY_STRING_CALLBACK_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMqttCallbackReceiver, mqttCallbackIntentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isServiceBound) {
            unbindService(mServiceConnection);
        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMqttCallbackReceiver);
    }

    protected MqttService getMqttService() {
        return mMqttService;
    }

    protected void executeMqttCallbackAction(Intent intent){

    }
}
