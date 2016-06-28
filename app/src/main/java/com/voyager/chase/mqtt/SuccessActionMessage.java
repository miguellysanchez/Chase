package com.voyager.chase.mqtt;

import android.content.Intent;

import org.eclipse.paho.client.mqttv3.IMqttToken;

/**
 * Created by miguellysanchez on 6/27/16.
 */
public class SuccessActionMessage extends ActionMessage {

    public static Intent toIntent(IMqttToken iMqttToken) {
        return constructIntentFromToken(iMqttToken);
    }

    public static SuccessActionMessage fromIntent(Intent intent) {
        SuccessActionMessage successActionMessage = new SuccessActionMessage();
        successActionMessage.setClientId(intent.getStringExtra(KEY_CLIENT_ID));
        successActionMessage.setMessageId(intent.getIntExtra(KEY_MESSAGE_ID,0));
        successActionMessage.setSessionPresent(intent.getBooleanExtra(KEY_IS_SESSION_PRESENT, false));
        successActionMessage.setTopicsList(intent.getStringArrayExtra(KEY_TOPICS_LIST));
        return successActionMessage;
    }
}
