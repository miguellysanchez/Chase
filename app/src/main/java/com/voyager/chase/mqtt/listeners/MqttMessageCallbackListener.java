package com.voyager.chase.mqtt.listeners;

import android.content.Context;
import android.content.Intent;

import com.voyager.chase.mqtt.ArrivedMessage;
import com.voyager.chase.mqtt.ControlMessage;
import com.voyager.chase.mqtt.DeliveredMessage;
import com.voyager.chase.utility.BroadcastUtility;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import timber.log.Timber;

/**
 * Created by miguellysanchez on 6/28/16.
 */
public class MqttMessageCallbackListener implements MqttCallback {
    public static final String MESSAGE_ARRIVED = "message_arrived";
    public static final String DELIVERY_COMPLETE = "delivery_complete";

    public static final int MQTT_CALLBACK_VALUE_CONNECTION_LOST = -101;
    public static final int MQTT_CALLBACK_VALUE_MESSAGE_ARRIVED = 200;
    public static final int MQTT_CALLBACK_DELIVERY_COMPLETE = 300;

    private final Context mContext;

    public MqttMessageCallbackListener(Context context) {
        mContext = context;
    }

    @Override
    public void connectionLost(Throwable throwable) {
        Timber.d("Mqtt Connection lost due to : %s" , throwable.getMessage());
        Intent intent = new Intent();
        BroadcastUtility.broadcastCallbackIntent(mContext,intent, ControlMessage.CONNECTION_LOST, MQTT_CALLBACK_VALUE_CONNECTION_LOST);
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        Timber.d("Mqtt message received from topic[%s]|'%s'", topic , new String( mqttMessage.getPayload(), "UTF-8"));
        Intent intent = ArrivedMessage.toIntent(topic, mqttMessage);
        BroadcastUtility.broadcastCallbackIntent(mContext, intent, MESSAGE_ARRIVED, MQTT_CALLBACK_VALUE_MESSAGE_ARRIVED);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        Timber.d("Delivery complete to topics : %s" , iMqttDeliveryToken.getTopics()[0]);
        Intent intent = DeliveredMessage.toIntent(iMqttDeliveryToken);
        BroadcastUtility.broadcastCallbackIntent(mContext, intent, DELIVERY_COMPLETE, MQTT_CALLBACK_DELIVERY_COMPLETE);

    }


}
