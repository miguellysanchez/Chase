package com.voyager.chase.game.entity.construct;

import com.voyager.chase.R;
import com.voyager.chase.game.World;
import com.voyager.chase.game.entity.Tile;
import com.voyager.chase.game.entity.player.Player;
import com.voyager.chase.game.mods.WorldEffect;

/**
 * Created by miguellysanchez on 8/3/16.
 */
public class TeleporterEntryConstruct extends Construct {

    private String targetRoom;
    private int targetX;
    private int targetY;

    public TeleporterEntryConstruct(String targetRoom, int targetX, int targetY) {
        renderDrawableId = R.drawable.chase_ic_construct_teleporter_entry;
        isObstacle = false;
        isInvulnerable = true;
        isUntargetable = true;
        spyVisibility = GLOBALLY_VISIBLE;
        sentryVisibility = GLOBALLY_VISIBLE;

        this.targetRoom = targetRoom;
        this.targetX = targetX;
        this.targetY = targetY;
    }

    @Override
    protected boolean onTriggered(Player player) {
        Tile destinationTile = World.getInstance().getRoom(targetRoom).getTileAtCoordinates(targetX, targetY);
        if (!destinationTile.containsPlayer()) {
            WorldEffect teleportPlayerEffect = new WorldEffect();
            teleportPlayerEffect.setAffectedRole(player.getRole());
            teleportPlayerEffect.setEffectType(WorldEffect.MOVE_PLAYER);
            teleportPlayerEffect.setAffectedRoom(targetRoom);
            teleportPlayerEffect.setAffectedX(targetX);
            teleportPlayerEffect.setAffectedY(targetY);
            World.getInstance().addWorldEffectToQueue(teleportPlayerEffect);
            return true;
        }
        return false;
    }

    @Override
    public void onAddedToTile() {
        TeleporterExitConstruct teleporterExitConstruct = new TeleporterExitConstruct();
        Tile currentTile = World.getInstance().getRoom(getCurrentRoomName())
                .getTileAtCoordinates(getCurrentTileXCoordinate(), getCurrentTileYCoordinate());
        currentTile.addTrigger(uuidString, new Trigger(this));
        World.getInstance().addWorldItemLocation(uuidString, currentTile);
        World.getInstance().getRoom(targetRoom).getTileAtCoordinates(targetX, targetY).addConstruct(teleporterExitConstruct);
    }

    @Override
    public void onRemovedFromTile() {

    }
}
