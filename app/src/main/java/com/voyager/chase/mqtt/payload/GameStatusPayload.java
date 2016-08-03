package com.voyager.chase.mqtt.payload;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Created by miguellysanchez on 8/2/16.
 */
public class GameStatusPayload {

    public static final String DISCONNECTED = "disconnected";
    public static final String TURN_FINISHED = "turn_finished";
    public static final String SHOW_RESULTS = "show_results";

    @SerializedName("sender_role")
    private String senderRole;
    @SerializedName("action")
    private String action;
    @SerializedName("disconnection_graceful")
    private boolean disconnectionGraceful = false;

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

    public boolean isDisconnectionGraceful() {
        return disconnectionGraceful;
    }

    public void setDisconnectionGraceful(boolean disconnectionGraceful) {
        this.disconnectionGraceful = disconnectionGraceful;
    }
}
