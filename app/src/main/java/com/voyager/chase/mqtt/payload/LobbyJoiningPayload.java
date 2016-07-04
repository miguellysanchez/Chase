package com.voyager.chase.mqtt.payload;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Created by miguellysanchez on 6/30/16.
 */
public class LobbyJoiningPayload {

    public static final int JOIN_STATE_NULL = 0;
    public static final int JOIN_STATE_REGISTERING = 1;
    public static final int JOIN_STATE_SUBSCRIBED = 2;
    public static final int JOIN_STATE_WAITING = 3;
    public static final int JOIN_STATE_COMPLETE = 4;

    @SerializedName("client_id")
    private String clientId;

    @SerializedName("status")
    private int status;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
