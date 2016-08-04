package com.voyager.chase.mqtt.payload;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Created by miguellysanchez on 8/4/16.
 */
public class GameInfoPayload {

    @SerializedName("sender_role")
    private String senderRole;
    @SerializedName("sender_message")
    private String senderMessage;
    @SerializedName("non_sender_message")
    private String nonSenderMessage;

    public String getSenderRole() {
        return senderRole;
    }

    public void setSenderRole(String senderRole) {
        this.senderRole = senderRole;
    }

    public String getSenderMessage() {
        return senderMessage;
    }

    public void setSenderMessage(String senderMessage) {
        this.senderMessage = senderMessage;
    }

    public String getNonSenderMessage() {
        return nonSenderMessage;
    }

    public void setNonSenderMessage(String nonSenderMessage) {
        this.nonSenderMessage = nonSenderMessage;
    }

    public String toJson(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
