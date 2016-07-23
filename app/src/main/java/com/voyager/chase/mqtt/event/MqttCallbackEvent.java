package com.voyager.chase.mqtt.event;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

/**
 * Created by miguellysanchez on 7/18/16.
 */
public class MqttCallbackEvent {

    public static final int DEFAULT_MESSAGE_CALLBACK_TYPE = 0;
    public static final int CONNECTION_LOST_CALLBACK_TYPE = -101;
    public static final int ARRIVED_MESSAGE_CALLBACK_TYPE = 102;
    public static final int DELIVERED_MESSAGE_CALLBACK_TYPE = 103;

    private int messageCallbackType = DEFAULT_MESSAGE_CALLBACK_TYPE;
    private String topic;
    private Throwable throwable;
    private MqttMessage mqttMessage;
    private IMqttDeliveryToken iMqttDeliveryToken;

    public int getMessageCallbackType() {
        return messageCallbackType;
    }

    public void setMessageCallbackType(int messageCallbackType) {
        this.messageCallbackType = messageCallbackType;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public MqttMessage getMqttMessage() {
        return mqttMessage;
    }

    public void setMqttMessage(MqttMessage mqttMessage) {
        this.mqttMessage = mqttMessage;
    }

    public IMqttDeliveryToken getMqttDeliveryToken() {
        return iMqttDeliveryToken;
    }

    public void setMqttDeliveryToken(IMqttDeliveryToken iMqttDeliveryToken) {
        this.iMqttDeliveryToken = iMqttDeliveryToken;
    }
    //Convenience methods
    public byte[] getMessageRawPayload() {
        if (getMqttMessage() != null) {
            return getMqttMessage().getPayload();
        }
        return null;
    }

    public String getMessagePayload() {
        if (getMqttMessage() != null) {
            try {
                return new String(getMqttMessage().getPayload(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
