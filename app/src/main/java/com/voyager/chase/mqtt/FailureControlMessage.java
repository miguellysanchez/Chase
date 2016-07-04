package com.voyager.chase.mqtt;

import android.content.Intent;

import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * Created by miguellysanchez on 6/27/16.
 */
public class FailureControlMessage extends ControlMessage {

    public static String KEY_ERROR_CODE = "error_code";
    public static String KEY_ERROR_MESSAGE = "error_message";


    private int errorCode;
    private String errorMessage;

    public static Intent toIntent(IMqttToken iMqttToken, Throwable throwable) {
        Intent intent = constructIntentFromToken(iMqttToken);
        if(iMqttToken!=null){
            MqttException mqttException = iMqttToken.getException();
            if(mqttException!=null){
                intent.putExtra(KEY_ERROR_CODE, mqttException.getReasonCode());
                intent.putExtra(KEY_ERROR_MESSAGE, mqttException.getMessage());
            } else {
                intent.putExtra(KEY_ERROR_MESSAGE, throwable.getMessage());
            }
        }
        return intent;
    }

    public static FailureControlMessage fromIntent(Intent intent){
        FailureControlMessage failureControlMessage = new FailureControlMessage();
        failureControlMessage.setClientId(intent.getStringExtra(KEY_VALUE_STRING_CLIENT_ID));
        failureControlMessage.setMessageId(intent.getIntExtra(KEY_VALUE_INT_MESSAGE_ID,0));
        failureControlMessage.setSessionPresent(intent.getBooleanExtra(KEY_VALUE_BOOLEAN_IS_SESSION_PRESENT, false));
        failureControlMessage.setTopicsList(intent.getStringArrayExtra(KEY_VALUE_STRING_ARRAY_TOPICS_LIST));

        failureControlMessage.setErrorCode(intent.getIntExtra(KEY_ERROR_CODE, -1));
        failureControlMessage.setErrorMessage(intent.getStringExtra(KEY_ERROR_MESSAGE));
        return failureControlMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
