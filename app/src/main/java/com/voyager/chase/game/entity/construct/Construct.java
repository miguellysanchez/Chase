package com.voyager.chase.game.entity.construct;

import com.voyager.chase.game.entity.Renderable;
import com.voyager.chase.game.entity.player.Player;

/**
 * Created by miguellysanchez on 7/5/16.
 */
public abstract class Construct extends Renderable {
    protected String uuidString = "UUID";
    protected Player mOwner;
    protected String mConstructName = "Construct";
    protected boolean isReusable = false;
    protected boolean isObstacle = false;
    protected boolean isInvulnerable = false;
    protected boolean isLocked = false;

    public boolean invokeTrigger(Player player) {
        return onTriggered(player);
    }

    protected abstract boolean onTriggered(Player player);

    public abstract void onAddedToTile();

    public abstract void onRemovedFromTile();

    public boolean isObstacle() {
        return isObstacle;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public boolean isInvulnerable() {
        return isInvulnerable;
    }

    public String getUUID() {
        return uuidString;
    }

    public void setUUID(String uuidString) {
        this.uuidString = uuidString;
    }

    public Player getOwner() {
        return mOwner;
    }

    public void setOwner(Player mOwner) {
        this.mOwner = mOwner;
    }

    public String getConstructName() {
        return mConstructName;
    }

}
