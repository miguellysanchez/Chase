package com.voyager.chase.mqtt;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.voyager.chase.BuildConfig;
import com.voyager.chase.mqtt.listeners.MqttActionMessageListener;
import com.voyager.chase.mqtt.listeners.MqttMessageCallbackListener;
import com.voyager.chase.utility.BroadcastUtility;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import timber.log.Timber;

/**
 * Created by miguellysanchez on 6/26/16.
 */
public class MqttService extends Service {

    private static final String BROKER_URL = BuildConfig.CHASE_MQTT_BROKER_URL + ":" + BuildConfig.CHASE_MQTT_BROKER_PORT;
    private static final String BROKER_USERNAME = BuildConfig.CHASE_MQTT_BROKER_USERNAME;
    private static final String BROKER_PASSWORD = BuildConfig.CHASE_MQTT_BROKER_PASSWORD;

    private final LocalBinder mLocalBinder = new LocalBinder();

    private static boolean isServiceRunning = false;

    private String mMqttClientId;
    private MqttAsyncClient mClient;
    private MqttCallback mMqttMessageCallback;

    @Override
    public void onCreate() {
        super.onCreate();
        isServiceRunning=true;
        initializeMqttClient();
        Timber.d("Service is created");
    }

    private void initializeMqttClient() {
        try {
            mMqttClientId = MqttAsyncClient.generateClientId();
            Timber.d(">>>Trying to create new MQTT client");
            mClient = new MqttAsyncClient(BROKER_URL, mMqttClientId, new MemoryPersistence());
            Timber.d(">>>MQTT client created successfully!");
            mMqttMessageCallback = (new MqttMessageCallbackListener(this));
            mClient.setCallback(mMqttMessageCallback);
        } catch (MqttException e) {
            Timber.e("Unable to create client....stopping service");
            stopSelf();
            e.printStackTrace();
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Timber.d("Service onStartCommand");

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mLocalBinder;
    }

    public class LocalBinder extends Binder {
        public MqttService getService() {
            return MqttService.this;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mClient != null){
            disconnectFromBroker();
        }
        isServiceRunning=false;
    }

    public boolean isConnectedToBroker() {
        boolean result;
        if (mClient != null && mClient.isConnected()) {
            result = true;
        } else {
            result = false;
        }
        Timber.d("Broker connection status: %s", String.valueOf(result));
        return result;
    }

    public void connectToBroker(){
        if(!isConnectedToBroker()){
            try {
                MqttConnectOptions connectOptions = new MqttConnectOptions();
                connectOptions.setUserName(BROKER_USERNAME);
                connectOptions.setPassword(BROKER_PASSWORD.toCharArray());
                connectOptions.setCleanSession(false);
                connectOptions.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);

                IMqttActionListener connectActionListener = MqttActionMessageListener.getConnectActionMessageListener(this);
                mClient.connect(connectOptions, null, connectActionListener);
            } catch (MqttException exception) {
                Timber.e("Mqtt Connect Message exception: [%s] | %s", exception.getReasonCode(), exception.getMessage());
            }
        } else {
            Timber.w("Connect attempt failed, already connected to broker");
            BroadcastUtility.broadcastIntent(this, new Intent(), "CONNECT", MqttActionMessageListener.MQTT_CALLBACK_VALUE_CONNECT_REDUNDANT);
        }
    }

    public void disconnectFromBroker(){
        if(isConnectedToBroker()){
            try {
                mClient.disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        } else {
            Timber.d("Cannot disconnect from broker. Client is either null or already disconnected");
        }
    }

    public MqttAsyncClient getMqttClient(){
        return mClient;
    }

    public static boolean isServiceRunning(){
        return isServiceRunning;
    }
}
