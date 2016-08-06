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
 * Created by miguellysanchez on 8/5/16.
 */
public class SecurityCameraConstruct extends Construct {

    protected SecurityCameraConstruct() {
        mConstructName = "SECURITY CAMERA";
        renderDrawableId = R.drawable.chase_ic_construct_security_camera;
        isObstacle = false;
        isLocked = false;
        isInvulnerable = false;
        spyVisibility = Renderable.PROXIMITY_VISIBLE;
        sentryVisibility = Renderable.GLOBALLY_VISIBLE;
    }

    @Override
    protected boolean onTriggered(Player player) {
        if (!player.getRole().equals(mOwner.getRole())) {
            GameInfoPayload gameInfoPayload = new GameInfoPayload();
            gameInfoPayload.setSenderRole(mOwner.getRole());
            gameInfoPayload.setSenderMessage(
                    String.format("SECURITY CAMERA saw the %s at Room <%s> , tile [%d,%d]",
                            player.getRole(), player.getCurrentRoomName(), player.getCurrentTileX(), player.getCurrentTileY()));
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
        for (int x = getCurrentTileX() - 3; x <= getCurrentTileX() + 3; x++) {
            for (int y = getCurrentTileY() - 3; y <= getCurrentTileY() + 3; y++) {
                if (TileUtility.isWithinRoom(x, y)) {
                    Tile tile = World.getInstance().getRoom(getCurrentRoomName()).getTileAtCoordinates(x, y);
                    tile.addTrigger(mIdString, new Trigger(this));
                    World.getInstance().addWorldItemLocation(mIdString, tile);
                }
            }
        }
    }

    @Override
    public void onRemovedFromTile() {
        World.getInstance().removeAllWorldItemLocations(mIdString);

        if(World.getUserPlayer().getRole().equals(mOwner.getRole())) {
            GameInfoPayload gameInfoPayload = new GameInfoPayload();
            gameInfoPayload.setSenderRole(mOwner.getRole());
            gameInfoPayload.setSenderMessage("Your SECURITY CAMERA was destroyed");
            ViewChangeEvent viewChangeEvent = new ViewChangeEvent();
            viewChangeEvent.addViewChangeType(ViewChangeEvent.GAME_INFO_UPDATE);
            viewChangeEvent.setGameInfoUpdate(gameInfoPayload.toJson());
            EventBus.getDefault().post(viewChangeEvent);
        }
    }
}
