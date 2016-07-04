package com.voyager.chase.mqtt;

import android.content.Intent;

import org.eclipse.paho.client.mqttv3.IMqttToken;

/**
 * Created by miguellysanchez on 6/27/16.
 */
public class ControlMessage {
    public static final String CONNECTION_LOST = "CONNECTION_LOST";
    public static final String CONNECT = "CONNECT";
    public static final String DISCONNECT = "DISCONNECT";
    public static final String SUBSCRIBE = "SUBSCRIBE";
    public static final String UNSUBSCRIBE = "UNSUBSCRIBE";
    public static final String PUBLISH = "PUBLISH";

    public static final String KEY_VALUE_STRING_CLIENT_ID = "client_id";
    public static final String KEY_VALUE_STRING_ARRAY_TOPICS_LIST = "topics_list";
    public static final String KEY_VALUE_INT_MESSAGE_ID = "message_id";
    public static final String KEY_VALUE_BOOLEAN_IS_SESSION_PRESENT = "is_session_present";

    protected String clientId;
    protected int messageId;
    protected String[] topicsList;
    protected boolean isSessionPresent;

    protected static Intent constructIntentFromToken(IMqttToken iMqttToken){
        Intent intent = new Intent();
        if(iMqttToken!=null) {
            if(iMqttToken.getClient()!=null) {
                intent.putExtra(KEY_VALUE_STRING_CLIENT_ID, iMqttToken.getClient().getClientId());
            }
            intent.putExtra(KEY_VALUE_STRING_ARRAY_TOPICS_LIST, iMqttToken.getTopics());
            intent.putExtra(KEY_VALUE_INT_MESSAGE_ID, iMqttToken.getMessageId());
            intent.putExtra(KEY_VALUE_BOOLEAN_IS_SESSION_PRESENT, iMqttToken.getSessionPresent());
        }
        return intent;
    }

    public boolean isSessionPresent() {
        return isSessionPresent;
    }

    public void setSessionPresent(boolean sessionPresent) {
        isSessionPresent = sessionPresent;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String[] getTopicsList() {
        return topicsList;
    }

    public void setTopicsList(String[] topicsList) {
        this.topicsList = topicsList;
    }
}

