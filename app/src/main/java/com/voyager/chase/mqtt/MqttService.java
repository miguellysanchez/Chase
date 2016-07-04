package com.voyager.chase.mqtt;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.voyager.chase.BuildConfig;
import com.voyager.chase.mqtt.listeners.MqttControlMessageListener;
import com.voyager.chase.mqtt.listeners.MqttMessageCallbackListener;
import com.voyager.chase.utility.BroadcastUtility;
import com.voyager.chase.utility.PreferenceUtility;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.nio.charset.Charset;

import timber.log.Timber;

/**
 * Created by miguellysanchez on 6/26/16.
 */
public class MqttService extends Service {

    private static final String BROKER_URL = BuildConfig.CHASE_MQTT_BROKER_URL + ":" + BuildConfig.CHASE_MQTT_BROKER_PORT;
    private static final String BROKER_USERNAME = BuildConfig.CHASE_MQTT_BROKER_USERNAME;
    private static final String BROKER_PASSWORD = BuildConfig.CHASE_MQTT_BROKER_PASSWORD;
    private static final int QUALITY_OF_SERVICE = 2;

    private final LocalBinder mLocalBinder = new LocalBinder();

    private static boolean isServiceRunning = false;

    private String mMqttClientId;
    private MqttAsyncClient mClient;
    private MqttCallback mMqttMessageCallback;

    @Override
    public void onCreate() {
        super.onCreate();
        isServiceRunning = true;
        Timber.d("Service is created");
    }

    private void initializeMqttClient() {
        try {
            mMqttClientId = PreferenceUtility.getInstance(this).getMqttClientId();
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
        if (mClient != null) {
            disconnectFromBroker();
        }
        isServiceRunning = false;
    }

    public boolean isConnectedToBroker() {
        boolean isConnectedToBroker;
        isConnectedToBroker = mClient != null && mClient.isConnected();
        Timber.d("Broker connection status: %s", String.valueOf(isConnectedToBroker));
        return isConnectedToBroker;
    }

    public void connectToBroker() {
        MqttControlMessageListener connectControlListener = MqttControlMessageListener.getConnectControlMessageListener(this);
        if (!isConnectedToBroker()) {
            try {
                initializeMqttClient();
                MqttConnectOptions connectOptions = new MqttConnectOptions();
                connectOptions.setUserName(BROKER_USERNAME);
                connectOptions.setPassword(BROKER_PASSWORD.toCharArray());
                connectOptions.setCleanSession(false);
                connectOptions.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);

                mClient.connect(connectOptions, null, connectControlListener);
            } catch (MqttException exception) {
                Timber.e("Mqtt Connect Message exception: [%s] | %s", exception.getReasonCode(), exception.getMessage());
                connectControlListener.onFailure(null, exception);
            }
        } else {
            Timber.w("Connect attempt failed, already connected to broker");
            connectControlListener.onExtraFailure(MqttControlMessageListener.MQTT_CALLBACK_VALUE_CONNECT_REDUNDANT, new Throwable("Connect attempt failed, already connected to broker"));
        }
    }

    public void disconnectFromBroker() {
        MqttControlMessageListener disconnectControlListener = MqttControlMessageListener.getDisconnectControlMessageListener(this);
        if (isConnectedToBroker()) {
            try {
                mClient.disconnect(null, disconnectControlListener);
            } catch (MqttException exception) {
                Timber.e("MQTT DISCONNECT Message Exception: [%s] | %s", exception.getReasonCode(), exception.getMessage());
                disconnectControlListener.onFailure(null, exception);
            }
        } else {
            Timber.d("Cannot disconnect from broker. Client is either null or already disconnected");
            BroadcastUtility.broadcastIntent(this, new Intent(), ControlMessage.DISCONNECT, MqttControlMessageListener.MQTT_CALLBACK_VALUE_DISCONNECT_REDUNDANT);
        }
    }


    public void publishToTopic(String topic, String message) {
        byte[] byteArray = message.getBytes(Charset.forName("UTF-8"));
        publishToTopic(topic, byteArray);
    }

    public void publishToTopic(String topic, byte[] payload) {
        MqttControlMessageListener publishControlMessageListener = MqttControlMessageListener.getPublishControlMessageListener(this);
        try{
            mClient.publish(topic, payload, QUALITY_OF_SERVICE,true, null, publishControlMessageListener);
        } catch (MqttException exception) {
            Timber.e("MQTT PUBLISH Message Exception: [%s] | %s", exception.getReasonCode(), exception.getMessage());
            publishControlMessageListener.onFailure(null, exception);        }
    }

    public void subscribeToTopic(String topic) {
        MqttControlMessageListener subscribeControlMessageListener = MqttControlMessageListener.getSubscribeControlMessageListener(this);
        try {
            mClient.subscribe(topic, QUALITY_OF_SERVICE, null, subscribeControlMessageListener);
        } catch (MqttException exception) {
            Timber.e("MQTT SUBSCRIBE Message Exception: [%s] | %s", exception.getReasonCode(), exception.getMessage());
            subscribeControlMessageListener.onFailure(null, exception);
        }
    }

    public void unsubscribeFromTopic(String topic) {
        MqttControlMessageListener unsubscribeControlMessageListener = MqttControlMessageListener.getUnsubscribeControlMessageListener(this);
        try {
            mClient.subscribe(topic, QUALITY_OF_SERVICE, null, unsubscribeControlMessageListener);
        } catch (MqttException exception) {
            Timber.e("MQTT UNSUBSCRIBE Message Exception: [%s] | %s", exception.getReasonCode(), exception.getMessage());
        }
    }

    public String getMqttClientId() {
        return mMqttClientId;
    }

    public static boolean isServiceRunning() {
        return isServiceRunning;
    }
}
