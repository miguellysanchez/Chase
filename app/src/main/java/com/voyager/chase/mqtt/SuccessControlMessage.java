package com.voyager.chase.mqtt;

import android.content.Intent;

import org.eclipse.paho.client.mqttv3.IMqttToken;

/**
 * Created by miguellysanchez on 6/27/16.
 */
public class SuccessControlMessage extends ControlMessage {

    public static Intent toIntent(IMqttToken iMqttToken) {
        return constructIntentFromToken(iMqttToken);
    }

    public static SuccessControlMessage fromIntent(Intent intent) {
        SuccessControlMessage successControlMessage = new SuccessControlMessage();
        successControlMessage.setClientId(intent.getStringExtra(KEY_VALUE_STRING_CLIENT_ID));
        successControlMessage.setMessageId(intent.getIntExtra(KEY_VALUE_INT_MESSAGE_ID,0));
        successControlMessage.setSessionPresent(intent.getBooleanExtra(KEY_VALUE_BOOLEAN_IS_SESSION_PRESENT, false));
        successControlMessage.setTopicsList(intent.getStringArrayExtra(KEY_VALUE_STRING_ARRAY_TOPICS_LIST));
        return successControlMessage;
    }
}
