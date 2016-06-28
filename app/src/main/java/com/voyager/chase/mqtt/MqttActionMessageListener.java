package com.voyager.chase.mqtt;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;


/**
 * Created by miguellysanchez on 6/27/16.
 */
public class MqttActionMessageListener implements IMqttActionListener {

    public static final String KEY_ACTION_MQTT_CALLBACK = MqttActionMessageListener.class.getSimpleName()+ ".INTENT_ACTION_MQTT_CALLBACK";
    public static final String KEY_VALUE_INT_MQTT_CALLBACK = MqttActionMessageListener.class.getSimpleName() +".INTENT_VALUE_MQTT_CALLBACK";
    public static final String KEY_VALUE_STRING_MQTT_ACTION_TYPE = MqttActionMessageListener.class.getSimpleName() +".INTENT_VALUE_MQTT_ACTION_TYPE";

    public static final int MQTT_CALLBACK_VALUE_NULL = 0;

    public static final int MQTT_CALLBACK_VALUE_CONNECT_SUCCESS = 1;
    public static final int MQTT_CALLBACK_VALUE_CONNECT_FAILURE = -1;

    public static final int MQTT_CALLBACK_VALUE_DISCONNECT_SUCCESS = 2;
    public static final int MQTT_CALLBACK_VALUE_DISCONNECT_FAILURE = -2;

    public static final int MQTT_CALLBACK_VALUE_PUBLISH_SUCCESS = 3;
    public static final int MQTT_CALLBACK_VALUE_PUBLISH_FAILURE = -3;

    public static final int MQTT_CALLBACK_VALUE_SUBSCRIBE_SUCCESS = 4;
    public static final int MQTT_CALLBACK_VALUE_SUBSCRIBE_FAILURE = -4;

    private String actionType;
    private LocalBroadcastManager localBroadcastManager;
    private int successCallbackValue;
    private int failureCallbackValue;

    public MqttActionMessageListener(LocalBroadcastManager localBroadcastManager,String actionType, int successCallbackValue, int failureCallbackValue) {
        this.localBroadcastManager = localBroadcastManager;
        this.actionType = actionType;
        this.successCallbackValue = successCallbackValue;
        this.failureCallbackValue = failureCallbackValue;
    }

    @Override
    public void onSuccess(IMqttToken iMqttToken) {
        Intent intent = SuccessActionMessage.toIntent(iMqttToken);
        attachCallbackValueThenBroadcast(intent, successCallbackValue);
    }

    @Override
    public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
        Intent intent = FailureActionMessage.toIntent(iMqttToken, throwable);
        attachCallbackValueThenBroadcast(intent, failureCallbackValue);
    }

    private void attachCallbackValueThenBroadcast(Intent intent, int callbackValue) {
        intent.setAction(KEY_ACTION_MQTT_CALLBACK);
        intent.putExtra(KEY_VALUE_STRING_MQTT_ACTION_TYPE, actionType);
        intent.putExtra(KEY_VALUE_INT_MQTT_CALLBACK, callbackValue);
        localBroadcastManager.sendBroadcast(intent);
    }

    public static MqttActionMessageListener getConnectActionMessageListener(LocalBroadcastManager broadcastManager){
        return new MqttActionMessageListener(broadcastManager,"CONNECT", MQTT_CALLBACK_VALUE_CONNECT_SUCCESS, MQTT_CALLBACK_VALUE_CONNECT_FAILURE);
    }

    public static MqttActionMessageListener getDisconnectActionMessageListener(LocalBroadcastManager broadcastManager){
        return new MqttActionMessageListener(broadcastManager,"DISCONNECT", MQTT_CALLBACK_VALUE_DISCONNECT_SUCCESS, MQTT_CALLBACK_VALUE_DISCONNECT_FAILURE);
    }
}
