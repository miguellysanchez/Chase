package com.voyager.chase.game.entity.construct;

import com.voyager.chase.R;
import com.voyager.chase.game.World;
import com.voyager.chase.game.entity.Renderable;
import com.voyager.chase.game.entity.Tile;
import com.voyager.chase.game.entity.player.Player;
import com.voyager.chase.game.event.ViewChangeEvent;
import com.voyager.chase.mqtt.payload.GameInfoPayload;
import com.voyager.chase.utility.TileUtility;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by miguellysanchez on 8/6/16.
 */
public class MotionSensorConstruct extends Construct {
    protected MotionSensorConstruct() {
        mConstructName = "MOTION SENSOR";
        renderDrawableId = R.drawable.chase_ic_construct_lux_generator;
        isObstacle = false;
        isLocked = false;
        isInvulnerable = false;
        spyVisibility = Renderable.HIDDEN;
        sentryVisibility = Renderable.GLOBALLY_VISIBLE;
    }

    @Override
    protected boolean onTriggered(Player player) {
        if (!player.getRole().equals(mOwner.getRole())) {
            GameInfoPayload gameInfoPayload = new GameInfoPayload();
            gameInfoPayload.setSenderRole(mOwner.getRole());
            gameInfoPayload.setSenderMessage(
                    String.format("The MOTION SENSOR picked up the %s at Room <%s> , tile [%d,%d]",
                            player.getRole(), player.getCurrentRoomName(), player.getCurrentTileX(), player.getCurrentTileY()));
            gameInfoPayload.setNonSenderMessage("You tripped the MOTION SENSOR, find it and destroy it or get away from the area!");
            ViewChangeEvent viewChangeEvent = new ViewChangeEvent();
            viewChangeEvent.addViewChangeType(ViewChangeEvent.GAME_INFO_UPDATE);
            viewChangeEvent.setGameInfoUpdate(gameInfoPayload.toJson());
            EventBus.getDefault().post(viewChangeEvent);
            return true;
        }
        return false;
    }

    @Override
    public void onAddedToTile() {
        for (int x = getCurrentTileX() - 1; x <= getCurrentTileX() + 1; x++) {
            for (int y = getCurrentTileY() - 1; y <= getCurrentTileY() + 1; y++) {
                addTriggerToTile(x, y);
            }
        }
        addTriggerToTile(getCurrentTileX() + 2, getCurrentTileY());
        addTriggerToTile(getCurrentTileX() - 2, getCurrentTileY());
        addTriggerToTile(getCurrentTileX(), getCurrentTileY() + 2);
        addTriggerToTile(getCurrentTileX(), getCurrentTileY() - 2);
    }

    private void addTriggerToTile(int x, int y) {
        if (TileUtility.isWithinRoom(x, y)) {
            Tile tile = World.getInstance().getRoom(getCurrentRoomName()).getTileAtCoordinates(x, y);
            tile.addTrigger(idString, new Trigger(this));
            World.getInstance().addWorldItemLocation(idString, tile);
        }
    }

    @Override
    public void onRemovedFromTile() {
        World.getInstance().removeAllWorldItemLocations(idString);

        if(World.getUserPlayer().getRole().equals(mOwner.getRole())) {
            GameInfoPayload gameInfoPayload = new GameInfoPayload();
            gameInfoPayload.setSenderRole(mOwner.getRole());
            gameInfoPayload.setSenderMessage("Your MOTION SENSOR was destroyed");
            ViewChangeEvent viewChangeEvent = new ViewChangeEvent();
            viewChangeEvent.addViewChangeType(ViewChangeEvent.GAME_INFO_UPDATE);
            viewChangeEvent.setGameInfoUpdate(gameInfoPayload.toJson());
            EventBus.getDefault().post(viewChangeEvent);
        }
    }
}
