package com.voyager.chase.game.entity.skillitem;

import com.voyager.chase.game.World;
import com.voyager.chase.game.entity.Renderable;
import com.voyager.chase.game.entity.Tile;
import com.voyager.chase.game.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by miguellysanchez on 7/5/16.
 */
public abstract class SkillItem extends Renderable {
    protected String uuidString;
    protected Player mOwner;
    protected boolean isObstacle = false;
    protected boolean isInvulnerable = false;

    protected SkillItem(){
        uuidString = UUID.randomUUID().toString();
    }

    public abstract void onTriggered(Player player);

    public abstract void onAddedToTile();

    public abstract void onRemovedFromTile();

    public boolean isObstacle() {
        return isObstacle;
    }

    public void setIsObstacle(boolean obstacle) {
        isObstacle = obstacle;
    }

    public boolean isInvulnerable() {
        return isInvulnerable;
    }

    public void setIsInvulnerable(boolean invulnerable) {
        isInvulnerable = invulnerable;
    }

    public String getUuid() {
        return uuidString;
    }
}
