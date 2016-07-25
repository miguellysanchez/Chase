package com.voyager.chase.game.mods;

import com.voyager.chase.game.World;
import com.voyager.chase.game.entity.player.Player;

/**
 * Created by miguellysanchez on 7/25/16.
 */
public abstract class WorldMod {
    public static final String ADD_SKILL_ITEM = "add_skill_item";
    public static final String REMOVE_SKILL_ITEM = "remove_skill_item";
    public static final String MODIFY_SKILL_ITEM = "modify_skill_item";

    public static final String MODIFY_PLAYER = "modify_player";
    public static final String REMOVE_PLAYER = "remove_player";
    public static final String ADD_PLAYER = "add_player";

    private String modificationType;

    public String getModificationType() {
        return modificationType;
    }

    public void setModificationType(String modificationType) {
        this.modificationType = modificationType;
    }

    public abstract void applyMod(World world, Player player);
}
