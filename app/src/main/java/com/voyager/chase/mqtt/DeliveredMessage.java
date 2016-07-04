package com.voyager.chase.mqtt;

import android.content.Intent;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.UnsupportedEncodingException;

/**
 * Created by miguellysanchez on 7/3/16.
 */
public class DeliveredMessage {
    public static final String KEY_VALUE_STRING_TOPIC = "topic";
    public static final String KEY_VALUE_BYTE_ARRAY_RAW_PAYLOAD = "raw_payload";
    public static final String KEY_VALUE_STRING_PAYLOAD = "payload";

    private String mTopic;
    private byte[] mRawPayload;
    private String mPayload;

    public String getTopic() {
        return mTopic;
    }

    public void setTopic(String topic) {
        this.mTopic = topic;
    }

    public byte[] getRawPayload() {
        return mRawPayload;
    }

    public void setRawPayload(byte[] rawPayload) {
        this.mRawPayload = rawPayload;
    }

    public String getPayload() {
        return mPayload;
    }

    public void setPayload(String payload) {
        this.mPayload = payload;
    }

    public static Intent toIntent(IMqttDeliveryToken deliveryToken){
        Intent intent = new Intent();
        intent.putExtra(KEY_VALUE_STRING_TOPIC, deliveryToken.getTopics()[0]);
        try {
            if(deliveryToken.getMessage()!=null) {
                byte[] payload = deliveryToken.getMessage().getPayload();
                intent.putExtra(KEY_VALUE_BYTE_ARRAY_RAW_PAYLOAD, payload);
                intent.putExtra(KEY_VALUE_STRING_PAYLOAD, new String(payload, "UTF-8"));
            }
        } catch (MqttException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return intent;
    }

    public static DeliveredMessage fromIntent(Intent intent){
        DeliveredMessage deliveredMessage = new DeliveredMessage();
        deliveredMessage.setTopic(intent.getStringExtra(KEY_VALUE_STRING_TOPIC));
        deliveredMessage.setRawPayload(intent.getByteArrayExtra(KEY_VALUE_BYTE_ARRAY_RAW_PAYLOAD));
        deliveredMessage.setPayload(intent.getStringExtra(KEY_VALUE_STRING_PAYLOAD));
        return deliveredMessage;
    }


}
