package com.voyager.chase.mqtt.payload;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.voyager.chase.game.worldeffect.WorldEffect;

/**
 * Created by miguellysanchez on 8/1/16.
 */
public class WorldEffectPayload {

    @SerializedName("sender_role")
    private String senderRole;
    @SerializedName("world_effect")
    private WorldEffect worldEffect;

    public String getSenderRole() {
        return senderRole;
    }

    public void setSenderRole(String senderRole) {
        this.senderRole = senderRole;
    }

    public WorldEffect getWorldEffect() {
        return worldEffect;
    }

    public void setWorldEffect(WorldEffect worldEffect) {
        this.worldEffect = worldEffect;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
