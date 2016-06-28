package com.voyager.chase.mqtt.listeners;

import android.content.Context;
import android.content.Intent;

import com.voyager.chase.utility.BroadcastUtility;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import timber.log.Timber;

/**
 * Created by miguellysanchez on 6/28/16.
 */
public class MqttMessageCallbackListener implements MqttCallback {
    public static final int MQTT_CALLBACK_VALUE_CONNECTION_LOST = -101;

    private final Context mContext;

    public MqttMessageCallbackListener(Context context) {
        mContext = context;
    }

    @Override
    public void connectionLost(Throwable throwable) {
        Timber.d("Mqtt Connection lost due to : %s" , throwable.getMessage());
        Intent intent = new Intent();
        BroadcastUtility.broadcastIntent(mContext,intent,"MESSAGE", MQTT_CALLBACK_VALUE_CONNECTION_LOST);
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        Timber.d("Mqtt message received : %s |'%s'",s , new String( mqttMessage.getPayload(), "UTF-8"));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        Timber.d("Delivery complete from : %s" , "" + iMqttDeliveryToken.getMessageId());
    }


}
