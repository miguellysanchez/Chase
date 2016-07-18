package com.voyager.chase.mqtt.payload;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by miguellysanchez on 7/16/16.
 */
public class SkillSelectPayload {

    @SerializedName("is_waiting")
    private boolean isWaiting;

    @SerializedName("sender_role")
    private String senderRole;

    public boolean isWaiting() {
        return isWaiting;
    }

    public void setIsWaiting(boolean isWaiting) {
        this.isWaiting = isWaiting;
    }

    public void setSenderRole(String senderRole) {
        this.senderRole = senderRole;
    }

    public String getSenderRole() {
        return senderRole;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
