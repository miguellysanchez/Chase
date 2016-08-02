package com.voyager.chase.game.mods;

import com.google.gson.annotations.SerializedName;

/**
 * Created by miguellysanchez on 7/25/16.
 */
public class WorldEffect {
    public static final String MOVE_PLAYER = "move_player";
    public static final String MODIFY_PLAYER = "modify_player";

    public static final String ADD_SKILL_ITEM = "add_skill_item";
    public static final String REMOVE_SKILL_ITEM = "remove_skill_item";
    public static final String REDUCE_PLAYER_LIFE = "reduce_player_life";
    public static final String RECOVER_PLAYER_LIFE = "recover_player_life";
    public static final String SKIP_PLAYER_TURN = "skip_player_turn";
//    public static final String ADD_VISIBILITY_MODIFIER = "add_visibility_modifier";
//    public static final String REMOVE_VISIBILITY_MODIFIER = "remove_visibility_modifier";
//    public static final String ADD_ITEM_TRIGGER = "add_item_trigger";
//    public static final String REMOVE_ITEM_TRIGGER = "remove_item_trigger";

    @SerializedName("affected_role")
    private String affectedRole;
    @SerializedName("effect_type")
    private String effectType;
    @SerializedName("effect_content")
    private String effectContent;
    @SerializedName("affected_room")
    private String affectedRoom;
    @SerializedName("affected_x")
    private int affectedX = -1;
    @SerializedName("affected_y")
    private int affectedY = -1;
    @SerializedName("affected_uuid")
    private String affectedUUID;

    public String getEffectType() {
        return effectType;
    }

    public void setEffectType(String effectType) {
        this.effectType = effectType;
    }

    public String getEffectContent() {
        return effectContent;
    }

    public void setEffectContent(String effectContent) {
        this.effectContent = effectContent;
    }

    public String getAffectedRole() {
        return affectedRole;
    }

    public void setAffectedRole(String affectedRole) {
        this.affectedRole = affectedRole;
    }

    public String getAffectedRoom() {
        return affectedRoom;
    }

    public void setAffectedRoom(String affectedRoom) {
        this.affectedRoom = affectedRoom;
    }

    public int getAffectedX() {
        return affectedX;
    }

    public void setAffectedX(int affectedX) {
        this.affectedX = affectedX;
    }

    public int getAffectedY() {
        return affectedY;
    }

    public void setAffectedY(int affectedY) {
        this.affectedY = affectedY;
    }

    public String getAffectedUUID() {
        return affectedUUID;
    }

    public void setAffectedUUID(String affectedUUID) {
        this.affectedUUID = affectedUUID;
    }
}
