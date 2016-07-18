package com.voyager.chase.mqtt.listeners;

import android.content.Context;
import android.content.Intent;

import com.voyager.chase.mqtt.ControlMessage;
import com.voyager.chase.mqtt.FailureControlMessage;
import com.voyager.chase.mqtt.SuccessControlMessage;
import com.voyager.chase.utility.MqttBroadcastUtility;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;


/**
 * Created by miguellysanchez on 6/27/16.
 */
public class MqttControlMessageListener implements IMqttActionListener {
    public static final int MQTT_CALLBACK_VALUE_CONNECT_SUCCESS = 2010;
    public static final int MQTT_CALLBACK_VALUE_CONNECT_FAILURE = -2010;
    public static final int MQTT_CALLBACK_VALUE_CONNECT_REDUNDANT = -2011;


    public static final int MQTT_CALLBACK_VALUE_DISCONNECT_SUCCESS = 2020;
    public static final int MQTT_CALLBACK_VALUE_DISCONNECT_FAILURE = -2020;
    public static final int MQTT_CALLBACK_VALUE_DISCONNECT_REDUNDANT = -2021;


    public static final int MQTT_CALLBACK_VALUE_PUBLISH_SUCCESS = 2030;
    public static final int MQTT_CALLBACK_VALUE_PUBLISH_FAILURE = -2030;

    public static final int MQTT_CALLBACK_VALUE_SUBSCRIBE_SUCCESS = 2040;
    public static final int MQTT_CALLBACK_VALUE_SUBSCRIBE_FAILURE = -2040;

    public static final int MQTT_CALLBACK_VALUE_UNSUBSCRIBE_SUCCESS = 2050;
    public static final int MQTT_CALLBACK_VALUE_UNSUBSCRIBE_FAILURE = -2050;

    private int successCallbackValue;
    private int failureCallbackValue;
    private String callbackType;
    private Context mContext;

    public MqttControlMessageListener(Context context, String callbackType, int successCallbackValue, int failureCallbackValue) {
        this.mContext = context;
        this.callbackType = callbackType;
        this.successCallbackValue = successCallbackValue;
        this.failureCallbackValue = failureCallbackValue;
    }

    @Override
    public void onSuccess(IMqttToken iMqttToken) {
        Intent intent = SuccessControlMessage.toIntent(iMqttToken);
        MqttBroadcastUtility.broadcastCallbackIntent(mContext, intent,callbackType, successCallbackValue);
    }

    @Override
    public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
        Intent intent = FailureControlMessage.toIntent(iMqttToken, throwable);
        MqttBroadcastUtility.broadcastCallbackIntent(mContext, intent, callbackType, failureCallbackValue);
    }

    public void onExtraFailure(int callbackValue, Throwable throwable){
        Intent intent = FailureControlMessage.toIntent(null, throwable);
        MqttBroadcastUtility.broadcastCallbackIntent(mContext, intent, callbackType, callbackValue);
    }

    public static MqttControlMessageListener getConnectControlMessageListener(Context context){
        return new MqttControlMessageListener(context, ControlMessage.CONNECT, MQTT_CALLBACK_VALUE_CONNECT_SUCCESS, MQTT_CALLBACK_VALUE_CONNECT_FAILURE);
    }

    public static MqttControlMessageListener getDisconnectControlMessageListener(Context context){
        return new MqttControlMessageListener(context, ControlMessage.DISCONNECT, MQTT_CALLBACK_VALUE_DISCONNECT_SUCCESS, MQTT_CALLBACK_VALUE_DISCONNECT_FAILURE);
    }

    public static MqttControlMessageListener getPublishControlMessageListener(Context context) {
        return new MqttControlMessageListener(context, ControlMessage.PUBLISH, MQTT_CALLBACK_VALUE_PUBLISH_SUCCESS, MQTT_CALLBACK_VALUE_PUBLISH_FAILURE);
    }

    public static MqttControlMessageListener getSubscribeControlMessageListener(Context context){
        return new MqttControlMessageListener(context, ControlMessage.SUBSCRIBE, MQTT_CALLBACK_VALUE_SUBSCRIBE_SUCCESS, MQTT_CALLBACK_VALUE_SUBSCRIBE_FAILURE);
    }

    public static MqttControlMessageListener getUnsubscribeControlMessageListener(Context context) {
        return new MqttControlMessageListener(context, ControlMessage.UNSUBSCRIBE, MQTT_CALLBACK_VALUE_UNSUBSCRIBE_SUCCESS, MQTT_CALLBACK_VALUE_UNSUBSCRIBE_FAILURE);
    }
}
