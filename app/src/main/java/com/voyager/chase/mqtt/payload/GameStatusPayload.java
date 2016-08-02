package com.voyager.chase.mqtt.payload;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Created by miguellysanchez on 8/2/16.
 */
public class GameStatusPayload {

    @SerializedName("sender_role")
    private String senderRole;
    @SerializedName("action")
    private String action;

    public String getSenderRole() {
        return senderRole;
    }

    public void setSenderRole(String senderRole) {
        this.senderRole = senderRole;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
