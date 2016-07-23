package com.voyager.chase.mqtt.listeners;

import android.content.Context;

import com.voyager.chase.mqtt.event.MqttResolvedActionEvent;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.greenrobot.eventbus.EventBus;


/**
 * Created by miguellysanchez on 6/27/16.
 */
public class MqttResolvedActionListener implements IMqttActionListener {

    private int commandType;
    private Context mContext;

    public MqttResolvedActionListener(int commandType){
        this.commandType = commandType;
    }

    @Override
    public void onSuccess(IMqttToken iMqttToken) {
        MqttResolvedActionEvent mqttResolvedActionEvent = new MqttResolvedActionEvent();
        mqttResolvedActionEvent.setSuccess(true);
        mqttResolvedActionEvent.setActionType(commandType);
        mqttResolvedActionEvent.setMqttToken(iMqttToken);

        EventBus.getDefault().post(mqttResolvedActionEvent);
    }

    @Override
    public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
        MqttResolvedActionEvent mqttResolvedActionEvent = new MqttResolvedActionEvent();
        mqttResolvedActionEvent.setSuccess(false);
        mqttResolvedActionEvent.setActionType(commandType);
        mqttResolvedActionEvent.setMqttToken(iMqttToken);

        EventBus.getDefault().post(mqttResolvedActionEvent);
    }

    public static MqttResolvedActionListener getConnectResolvedActionListener(){
        return new MqttResolvedActionListener(MqttResolvedActionEvent.CONNECT_ACTION_TYPE);
    }

    public static MqttResolvedActionListener getDisconnectResolvedActionListener(){
        return new MqttResolvedActionListener(MqttResolvedActionEvent.DISCONNECT_ACTION_TYPE);
    }

    public static MqttResolvedActionListener getPublishResolvedActionListener() {
        return new MqttResolvedActionListener(MqttResolvedActionEvent.PUBLISH_ACTION_TYPE);
    }

    public static MqttResolvedActionListener getSubscribeResolvedActionListener(){
        return new MqttResolvedActionListener(MqttResolvedActionEvent.SUBSCRIBE_ACTION_TYPE);
    }

    public static MqttResolvedActionListener getUnsubscribeResolvedActionListener() {
        return new MqttResolvedActionListener(MqttResolvedActionEvent.UNSUBSCRIBE_ACTION_TYPE);
    }
}
