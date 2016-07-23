package com.voyager.chase.mqtt;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;

import com.voyager.chase.BuildConfig;
import com.voyager.chase.mqtt.event.MqttIssueActionEvent;
import com.voyager.chase.mqtt.listeners.MqttResolvedActionListener;
import com.voyager.chase.mqtt.listeners.CustomMqttCallback;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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

    private MqttAsyncClient mClient;
    private MqttCallback mMqttMessageCallback;

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        Timber.d(">>>>>CREATED MQTTSERVICE");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Timber.d("Service onStartCommand");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (mClient != null) {
            disconnectFromBroker();
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        Timber.d(">>>>>DESTROYED MQTTSERVICE");
    }

    public boolean isConnectedToBroker() {
        boolean isConnectedToBroker = false;
        if (mClient != null) {
            isConnectedToBroker = mClient.isConnected();
        }
        Timber.d("Broker connection status: %s", String.valueOf(isConnectedToBroker));
        return isConnectedToBroker;
    }

    private void initializeMqttClient() {
        try {
            Timber.d(">>>Trying to create new MQTT client");
            mClient = new MqttAsyncClient(BROKER_URL, MqttAsyncClient.generateClientId(), new MemoryPersistence());
            Timber.d(">>>MQTT client created successfully!");
            mMqttMessageCallback = (new CustomMqttCallback());
            mClient.setCallback(mMqttMessageCallback);
        } catch (MqttException e) {
            Timber.e("Unable to create client....stopping service");
            stopSelf();
            e.printStackTrace();
        }
    }

    public void connectToBroker(String willTopic, String willPayloadString) {
        MqttResolvedActionListener connectControlListener = MqttResolvedActionListener.getConnectResolvedActionListener();
        try {
            initializeMqttClient();
            if (isConnectedToBroker()) {
                disconnectFromBroker();
            }
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setUserName(BROKER_USERNAME);
            connectOptions.setPassword(BROKER_PASSWORD.toCharArray());
            connectOptions.setCleanSession(false);
            connectOptions.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
            if (!TextUtils.isEmpty(willTopic) && !TextUtils.isEmpty(willPayloadString)) {
                byte[] byteArray = willPayloadString.getBytes(Charset.forName("UTF-8"));
                connectOptions.setWill(willTopic, byteArray, QUALITY_OF_SERVICE, false);
            }

            mClient.connect(connectOptions, null, connectControlListener);
        } catch (MqttException exception) {
            Timber.e("Mqtt Connect Message exception: [%s] | %s", exception.getReasonCode(), exception.getMessage());
            connectControlListener.onFailure(null, exception);
        }

    }

    public void disconnectFromBroker() {
        MqttResolvedActionListener disconnectControlListener = MqttResolvedActionListener.getDisconnectResolvedActionListener();
        if (isConnectedToBroker()) {
            try {
                mClient.disconnect(null, disconnectControlListener);
            } catch (MqttException exception) {
                Timber.e("MQTT DISCONNECT Message Exception: [%s] | %s", exception.getReasonCode(), exception.getMessage());
                disconnectControlListener.onFailure(null, exception);
            }
        } else {
            Timber.d("Cannot disconnect from broker. Client is either null or already disconnected");
        }
    }

    public void publishToTopic(String topic, String message, boolean retained) {
        Timber.d("Attempting to PUBLISH to topic %s with message '%s'", topic, message);
        byte[] byteArray = message.getBytes(Charset.forName("UTF-8"));
        publishToTopic(topic, byteArray, retained);
    }

    public void publishToTopic(String topic, byte[] payload, boolean retained) {
        MqttResolvedActionListener publishControlMessageListener = MqttResolvedActionListener.getPublishResolvedActionListener();
        try {
            if(isConnectedToBroker()){
                mClient.publish(topic, payload, QUALITY_OF_SERVICE, retained, null, publishControlMessageListener);
            } else {
                throw new MqttException(new Throwable("Unable to PUBLISH when disconnected from broker. Attempt to connect to broker first"));
            }
        } catch (MqttException exception) {
            Timber.e("MQTT PUBLISH Message Exception: [%s] | %s", exception.getReasonCode(), exception.getMessage());
            publishControlMessageListener.onFailure(null, exception);
        }
    }

    public void subscribeToTopic(String topic) {
        MqttResolvedActionListener subscribeControlMessageListener = MqttResolvedActionListener.getSubscribeResolvedActionListener();
        try {
            if(isConnectedToBroker()){
                Timber.d("Attempting to SUBSCRIBE to topic %s", topic);
                mClient.subscribe(topic, QUALITY_OF_SERVICE, null, subscribeControlMessageListener);
            } else {
                throw new MqttException(new Throwable("Unable to SUBSCRIBE when disconnected from broker. Attempting to connect to broker. Attempt to connect to broker first"));
            }
        } catch (MqttException exception) {
            Timber.e("MQTT SUBSCRIBE Message Exception: [%s] | %s", exception.getReasonCode(), exception.getMessage());
            subscribeControlMessageListener.onFailure(null, exception);
        }
    }

    public void unsubscribeFromTopic(String topic) {
        MqttResolvedActionListener unsubscribeControlMessageListener = MqttResolvedActionListener.getUnsubscribeResolvedActionListener();
        try {
            if(isConnectedToBroker()){
                mClient.unsubscribe(topic, null, unsubscribeControlMessageListener);
            } else {
                throw new MqttException(new Throwable("Unable to UNSUBSCRIBE when disconnected from broker. Attempting to connect to broker. Attempt to connect to broker first"));
            }
            mClient.unsubscribe(topic, null, unsubscribeControlMessageListener);
        } catch (MqttException exception) {
            Timber.e("MQTT UNSUBSCRIBE Message Exception: [%s] | %s", exception.getReasonCode(), exception.getMessage());
        }
    }

    @Subscribe
    public void onMqttIssueCommandEvent(MqttIssueActionEvent event) {
        switch (event.getActionType()) {
            case MqttIssueActionEvent.CONNECT_ACTION_TYPE:
                connectToBroker(event.getTopic(), event.getPayloadString());
                break;
            case MqttIssueActionEvent.DISCONNECT_ACTION_TYPE:
                disconnectFromBroker();
            case MqttIssueActionEvent.SUBSCRIBE_ACTION_TYPE:
                subscribeToTopic(event.getTopic());
                break;
            case MqttIssueActionEvent.UNSUBSCRIBE_ACTION_TYPE:
                unsubscribeFromTopic(event.getTopic());
                break;
            case MqttIssueActionEvent.PUBLISH_ACTION_TYPE:
                publishToTopic(event.getTopic(), event.getPayloadString(), event.isRetained());
                break;
            case MqttIssueActionEvent.CHECK_IS_CONNECTED_ACTION_TYPE:
                event.getIsConnectedCallback().executeCallback(isConnectedToBroker());
                break;
        }
    }
}
