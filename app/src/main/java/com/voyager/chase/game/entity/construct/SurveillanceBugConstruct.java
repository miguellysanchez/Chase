package com.voyager.chase.game.entity.construct;

import com.voyager.chase.R;
import com.voyager.chase.game.World;
import com.voyager.chase.game.entity.Renderable;
import com.voyager.chase.game.entity.Tile;
import com.voyager.chase.game.entity.player.Player;
import com.voyager.chase.game.event.ViewChangeEvent;
import com.voyager.chase.mqtt.payload.GameInfoPayload;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by miguellysanchez on 8/6/16.
 */
public class SurveillanceBugConstruct extends Construct {
    protected SurveillanceBugConstruct() {
        mConstructName = "SURVEILLANCE BUG";
        isObstacle = false;
        isLocked = false;
        isInvulnerable = false;
        spyVisibility = Renderable.HIDDEN;
        sentryVisibility = Renderable.HIDDEN;
    }

    @Override
    public void setOwner(Player mOwner) {
        super.setOwner(mOwner);
        if (Player.SPY_ROLE.equals(mOwner.getRole())) {
            spyVisibility = Renderable.GLOBALLY_VISIBLE;
            renderDrawableId = R.drawable.chase_ic_surveillance_bug_spy;
        } else if (Player.SENTRY_ROLE.equals(mOwner.getRole())) {
            sentryVisibility = Renderable.GLOBALLY_VISIBLE;
            renderDrawableId = R.drawable.chase_ic_surveillance_bug_sentry;
        }
    }

    @Override
    protected boolean onTriggered(Player player) {
        return false;
    }

    @Override
    public void onAddedToTile() {
        Tile tile = World.getInstance().getRoom(getCurrentRoomName()).getTileAtCoordinates(getCurrentTileX(), getCurrentTileY());
        if (Player.SENTRY_ROLE.equals(mOwner.getRole())) {
            tile.addVisibilityModifier(idString, Tile.SENTRY_ONLY_GLOBAL_VISIBILITY_MOD);
        } else if (Player.SPY_ROLE.equals(mOwner.getRole())) {
            tile.addVisibilityModifier(idString, Tile.SPY_ONLY_GLOBAL_VISIBILITY_MOD);
        }
        World.getInstance().addWorldItemLocation(idString, tile);
    }

    @Override
    public void onRemovedFromTile() {
        if (World.getUserPlayer().getRole().equals(mOwner.getRole())) {
            GameInfoPayload gameInfoPayload = new GameInfoPayload();
            gameInfoPayload.setSenderRole(mOwner.getRole());
            gameInfoPayload.setSenderMessage("Your SURVEILLANCE BUG was destroyed");
            ViewChangeEvent viewChangeEvent = new ViewChangeEvent();
            viewChangeEvent.addViewChangeType(ViewChangeEvent.GAME_INFO_UPDATE);
            viewChangeEvent.setGameInfoUpdate(gameInfoPayload.toJson());
            EventBus.getDefault().post(viewChangeEvent);
        }
        World.getInstance().removeAllWorldItemLocations(idString);
    }
}