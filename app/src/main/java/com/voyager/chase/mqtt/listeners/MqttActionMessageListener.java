package com.voyager.chase.mqtt.listeners;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.voyager.chase.mqtt.FailureActionMessage;
import com.voyager.chase.mqtt.SuccessActionMessage;
import com.voyager.chase.utility.BroadcastUtility;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;


/**
 * Created by miguellysanchez on 6/27/16.
 */
public class MqttActionMessageListener implements IMqttActionListener {
    public static final int MQTT_CALLBACK_VALUE_CONNECT_SUCCESS = 2010;
    public static final int MQTT_CALLBACK_VALUE_CONNECT_FAILURE = -2010;
    public static final int MQTT_CALLBACK_VALUE_CONNECT_REDUNDANT = -2011;


    public static final int MQTT_CALLBACK_VALUE_DISCONNECT_SUCCESS = 2020;
    public static final int MQTT_CALLBACK_VALUE_DISCONNECT_FAILURE = -2020;

    public static final int MQTT_CALLBACK_VALUE_PUBLISH_SUCCESS = 2030;
    public static final int MQTT_CALLBACK_VALUE_PUBLISH_FAILURE = -2030;

    public static final int MQTT_CALLBACK_VALUE_SUBSCRIBE_SUCCESS = 2040;
    public static final int MQTT_CALLBACK_VALUE_SUBSCRIBE_FAILURE = -2040;

    private int successCallbackValue;
    private int failureCallbackValue;
    private String callbackType;
    private Context mContext;

    public MqttActionMessageListener(Context context, String callbackType, int successCallbackValue, int failureCallbackValue) {
        this.mContext = context;
        this.callbackType = callbackType;
        this.successCallbackValue = successCallbackValue;
        this.failureCallbackValue = failureCallbackValue;
    }

    @Override
    public void onSuccess(IMqttToken iMqttToken) {
        Intent intent = SuccessActionMessage.toIntent(iMqttToken);
        BroadcastUtility.broadcastIntent(mContext, intent,callbackType, successCallbackValue);
    }

    @Override
    public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
        Intent intent = FailureActionMessage.toIntent(iMqttToken, throwable);
        BroadcastUtility.broadcastIntent(mContext, intent, callbackType, failureCallbackValue);
    }

    public static MqttActionMessageListener getConnectActionMessageListener(Context context){
        return new MqttActionMessageListener(context,"CONNECT", MQTT_CALLBACK_VALUE_CONNECT_SUCCESS, MQTT_CALLBACK_VALUE_CONNECT_FAILURE);
    }

    public static MqttActionMessageListener getDisconnectActionMessageListener(Context context){
        return new MqttActionMessageListener(context,"DISCONNECT", MQTT_CALLBACK_VALUE_DISCONNECT_SUCCESS, MQTT_CALLBACK_VALUE_DISCONNECT_FAILURE);
    }
}
