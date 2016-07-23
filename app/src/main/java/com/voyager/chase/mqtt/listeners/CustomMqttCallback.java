package com.voyager.chase.mqtt.listeners;

import com.voyager.chase.mqtt.event.MqttCallbackEvent;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;

import timber.log.Timber;

/**
 * Created by miguellysanchez on 6/28/16.
 */
public class CustomMqttCallback implements MqttCallback {

    @Override
    public void connectionLost(Throwable throwable) {
        Timber.d("Mqtt Connection lost due to : %s", throwable.getMessage());
        MqttCallbackEvent mqttCallbackEvent = new MqttCallbackEvent();
        mqttCallbackEvent.setMessageCallbackType(MqttCallbackEvent.CONNECTION_LOST_CALLBACK_TYPE);
        mqttCallbackEvent.setThrowable(throwable);
        post(mqttCallbackEvent);
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        Timber.d("Mqtt message received from topic[%s]|'%s'", topic, new String(mqttMessage.getPayload(), "UTF-8"));
        MqttCallbackEvent mqttCallbackEvent = new MqttCallbackEvent();
        mqttCallbackEvent.setMessageCallbackType(MqttCallbackEvent.ARRIVED_MESSAGE_CALLBACK_TYPE);
        mqttCallbackEvent.setTopic(topic);
        mqttCallbackEvent.setMqttMessage(mqttMessage);
        post(mqttCallbackEvent);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        Timber.d("Delivery complete to topic : %s", iMqttDeliveryToken.getTopics()[0]);
        MqttCallbackEvent mqttCallbackEvent = new MqttCallbackEvent();
        mqttCallbackEvent.setMessageCallbackType(MqttCallbackEvent.DELIVERED_MESSAGE_CALLBACK_TYPE);
        mqttCallbackEvent.setMqttDeliveryToken(iMqttDeliveryToken);
        mqttCallbackEvent.setTopic(iMqttDeliveryToken.getTopics()[0]);
        post(mqttCallbackEvent);
    }

    private void post(Object object) {
        EventBus.getDefault().post(object);
    }

}
