package com.voyager.chase.mqtt.event;

/**
 * Created by miguellysanchez on 7/18/16.
 */
public class MqttIssueActionEvent {
    public static final int CONNECT_ACTION_TYPE = 1001;
    public static final int DISCONNECT_ACTION_TYPE = 1002;
    public static final int SUBSCRIBE_ACTION_TYPE = 1003;
    public static final int UNSUBSCRIBE_ACTION_TYPE = 1004;
    public static final int PUBLISH_ACTION_TYPE = 1005;
    public static final int CHECK_IS_CONNECTED_ACTION_TYPE = 1006;

    private int actionType;
    private String topic;
    private String payloadString;
    private boolean isRetained = false;
    private IsConnectedCallback isConnectedCallback;

    public MqttIssueActionEvent(int actionType){
        this.actionType = actionType;
    }

    public int getActionType() {
        return actionType;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getPayloadString() {
        return payloadString;
    }

    public void setPayloadString(String payloadString) {
        this.payloadString = payloadString;
    }

    public boolean isRetained() {
        return isRetained;
    }

    public void setRetained(boolean retained) {
        isRetained = retained;
    }

    public IsConnectedCallback getIsConnectedCallback() {
        return isConnectedCallback;
    }

    public void setIsConnectedCallback(IsConnectedCallback isConnectedCallback) {
        this.isConnectedCallback = isConnectedCallback;
    }

}
