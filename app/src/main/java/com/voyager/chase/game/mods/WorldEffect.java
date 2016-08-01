package com.voyager.chase.game.mods;

import com.voyager.chase.game.World;
import com.voyager.chase.game.entity.player.Player;

/**
 * Created by miguellysanchez on 7/25/16.
 */
public abstract class WorldEffect {
    public static final String ADD_PLAYER = "add_player";
    public static final String REMOVE_PLAYER = "remove_player";
    public static final String MODIFY_PLAYER = "modify_player";

    public static final String ADD_SKILL_ITEM = "add_skill_item";
    public static final String REMOVE_SKILL_ITEM = "remove_skill_item";
//    public static final String ADD_VISIBILITY_MODIFIER = "add_visibility_modifier";
//    public static final String REMOVE_VISIBILITY_MODIFIER = "remove_visibility_modifier";
//    public static final String ADD_ITEM_TRIGGER = "add_item_trigger";
//    public static final String REMOVE_ITEM_TRIGGER = "remove_item_trigger";



    private String effectType;

    public String getEffectType() {
        return effectType;
    }

    public void setEffectType(String effectType) {
        this.effectType = effectType;
    }

    public abstract void applyMod(World world, Player player);
}
