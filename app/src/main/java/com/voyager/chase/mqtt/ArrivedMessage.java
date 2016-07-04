package com.voyager.chase.mqtt;

import android.content.Intent;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

/**
 * Created by miguellysanchez on 7/3/16.
 */
public class ArrivedMessage {

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

    public static Intent toIntent(String topic, MqttMessage mqttMessage){
        Intent intent = new Intent();
        intent.putExtra(KEY_VALUE_STRING_TOPIC, topic);
        intent.putExtra(KEY_VALUE_BYTE_ARRAY_RAW_PAYLOAD, mqttMessage.getPayload());
        try {
            intent.putExtra(KEY_VALUE_STRING_PAYLOAD, new String( mqttMessage.getPayload(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return intent;
    }

    public static ArrivedMessage fromIntent(Intent intent){
        ArrivedMessage arrivedMessage = new ArrivedMessage();
        arrivedMessage.setTopic(intent.getStringExtra(KEY_VALUE_STRING_TOPIC));
        arrivedMessage.setRawPayload(intent.getByteArrayExtra(KEY_VALUE_BYTE_ARRAY_RAW_PAYLOAD));
        arrivedMessage.setPayload(intent.getStringExtra(KEY_VALUE_STRING_PAYLOAD));
        return arrivedMessage;
    }
}
