package com.voyager.chase.mqtt;

import android.content.Intent;

import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * Created by miguellysanchez on 6/27/16.
 */
public class FailureActionMessage extends ActionMessage {

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

    public static FailureActionMessage fromIntent(Intent intent){
        FailureActionMessage failureActionMessage = new FailureActionMessage();
        failureActionMessage.setClientId(intent.getStringExtra(KEY_CLIENT_ID));
        failureActionMessage.setMessageId(intent.getIntExtra(KEY_MESSAGE_ID,0));
        failureActionMessage.setSessionPresent(intent.getBooleanExtra(KEY_IS_SESSION_PRESENT, false));
        failureActionMessage.setTopicsList(intent.getStringArrayExtra(KEY_TOPICS_LIST));

        failureActionMessage.setErrorCode(intent.getIntExtra(KEY_ERROR_CODE, -1));
        failureActionMessage.setErrorMessage(intent.getStringExtra(KEY_ERROR_MESSAGE));
        return failureActionMessage;
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
