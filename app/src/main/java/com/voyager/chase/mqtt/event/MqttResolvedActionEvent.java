package com.voyager.chase.mqtt.event;

import org.eclipse.paho.client.mqttv3.IMqttToken;

/**
 * Created by miguellysanchez on 7/18/16.
 */
public class MqttResolvedActionEvent {

    public static final int CONNECT_ACTION_TYPE = 1001;
    public static final int DISCONNECT_ACTION_TYPE = 1002;
    public static final int SUBSCRIBE_ACTION_TYPE = 1003;
    public static final int UNSUBSCRIBE_ACTION_TYPE = 1004;
    public static final int PUBLISH_ACTION_TYPE = 1005;
    public static final int CHECK_IS_CONNECTED_ACTION_TYPE = 1006;

    private int actionType;
    private IMqttToken mqttToken;
    private Throwable throwable;
    private boolean isSuccess;

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public IMqttToken getMqttToken() {
        return mqttToken;
    }

    public void setMqttToken(IMqttToken iMqttToken) {
        this.mqttToken = iMqttToken;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    //other convenience methods
    public String[] getTopicsList(){
        if(mqttToken!=null){
            return mqttToken.getTopics();
        }
        return null;
    }

    public String getFirstTopic(){
        if(mqttToken!=null && mqttToken.getTopics()!=null){
            return mqttToken.getTopics()[0];
        }
        return null;
    }


    public String getClientId(){
        if(getMqttToken().getClient()!=null){
            return getMqttToken().getClient().getClientId();
        }
        return null;
    }

}
