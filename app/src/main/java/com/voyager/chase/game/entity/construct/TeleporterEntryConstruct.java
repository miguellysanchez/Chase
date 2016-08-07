package com.voyager.chase.game.entity.construct;

import android.view.View;

import com.voyager.chase.R;
import com.voyager.chase.game.World;
import com.voyager.chase.game.entity.Tile;
import com.voyager.chase.game.entity.player.Player;
import com.voyager.chase.game.event.ViewChangeEvent;
import com.voyager.chase.game.gameinfo.GameInfo;
import com.voyager.chase.game.worldeffect.WorldEffect;
import com.voyager.chase.mqtt.payload.GameInfoPayload;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by miguellysanchez on 8/3/16.
 */
public class TeleporterEntryConstruct extends Construct {
    private String targetRoom;
    private int targetX;
    private int targetY;

    public TeleporterEntryConstruct(String id, String targetRoom, int targetX, int targetY) {
        mIdString = id;
        mConstructName = "TELEPORTER ENTRY";
        renderDrawableId = R.drawable.chase_ic_construct_teleporter_entry;
        isObstacle = false;
        isInvulnerable = true;
        isLocked = true;
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
        } else {
            GameInfoPayload payload = new GameInfoPayload();
            payload.setSenderRole(player.getRole());
            payload.setSenderMessage("Unable to use TELEPORTER. Other player is still on the other side.");

            ViewChangeEvent viewChangeEvent = new ViewChangeEvent();
            viewChangeEvent.addViewChangeType(ViewChangeEvent.GAME_INFO_UPDATE);
            viewChangeEvent.setGameInfoUpdate(payload.toJson());
            EventBus.getDefault().post(viewChangeEvent);
        }
        return false;
    }

    @Override
    public void onAddedToTile() {
        TeleporterExitConstruct teleporterExitConstruct = new TeleporterExitConstruct(mIdString);
        Tile currentTile = World.getInstance().getRoom(getCurrentRoomName())
                .getTileAtCoordinates(getCurrentTileX(), getCurrentTileY());
        currentTile.addTrigger(mIdString, new Trigger(this));
        World.getInstance().addWorldItemLocation(mIdString, currentTile);
        World.getInstance().getRoom(targetRoom).getTileAtCoordinates(targetX, targetY).addConstruct(teleporterExitConstruct);
    }

    @Override
    public void onRemovedFromTile() {
    }

    public String getTargetRoom() {
        return targetRoom;
    }
}
