package com.voyager.chase.mqtt.payload;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Created by miguellysanchez on 7/16/16.
 */
public class SkillSelectPayload {

    @SerializedName("user_id")
    private String userId;

    @SerializedName("is_waiting")
    private boolean isWaiting;

    public boolean isWaiting() {
        return isWaiting;
    }

    public void setIsWaiting(boolean isWaiting) {
        this.isWaiting = isWaiting;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
