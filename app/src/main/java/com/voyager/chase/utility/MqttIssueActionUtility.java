package com.voyager.chase.utility;

import com.voyager.chase.mqtt.event.IsConnectedCallback;
import com.voyager.chase.mqtt.event.MqttIssueActionEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by miguellysanchez on 7/21/16.
 */
public class MqttIssueActionUtility {

    public static void connect() {
        MqttIssueActionEvent event = new MqttIssueActionEvent(MqttIssueActionEvent.CONNECT_ACTION_TYPE);
        post(event);
    }

    public static void connect(String lastWillTopic, String lastWillPayload) {
        MqttIssueActionEvent event = new MqttIssueActionEvent(MqttIssueActionEvent.CONNECT_ACTION_TYPE);
        event.setTopic(lastWillTopic);
        event.setPayloadString(lastWillPayload);
        post(event);
    }

    public static void disconnect() {
        MqttIssueActionEvent event = new MqttIssueActionEvent(MqttIssueActionEvent.DISCONNECT_ACTION_TYPE);
        post(event);
    }

    public static void subscribe(String topic) {
        MqttIssueActionEvent event = new MqttIssueActionEvent(MqttIssueActionEvent.SUBSCRIBE_ACTION_TYPE);
        event.setTopic(topic);
        post(event);
    }

    public static void subscribe(String[] topicsList) {
        for (String topic : topicsList) {
            subscribe(topic);
        }
    }

    public static void unsubscribe(String topic) {
        MqttIssueActionEvent event = new MqttIssueActionEvent(MqttIssueActionEvent.UNSUBSCRIBE_ACTION_TYPE);
        event.setTopic(topic);
        post(event);
    }

    public static void unsubscribe(String[] topicsList) {
        for (String topic : topicsList) {
            unsubscribe(topic);
        }
    }

    public static void publish(String topic, String payloadString, boolean retained) {
        MqttIssueActionEvent event = new MqttIssueActionEvent(MqttIssueActionEvent.PUBLISH_ACTION_TYPE);
        event.setTopic(topic);
        event.setPayloadString(payloadString);
        event.setRetained(retained);
        post(event);
    }

    public static void checkIsConnected(IsConnectedCallback isConnectedCallback) {
        MqttIssueActionEvent event = new MqttIssueActionEvent(MqttIssueActionEvent.CHECK_IS_CONNECTED_ACTION_TYPE);
        event.setIsConnectedCallback(isConnectedCallback);
        post(event);
    }

    public static void post(MqttIssueActionEvent mqttIssueActionEvent) {
        EventBus.getDefault().post(mqttIssueActionEvent);
    }
}
