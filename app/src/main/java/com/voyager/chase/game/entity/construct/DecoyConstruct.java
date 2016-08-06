package com.voyager.chase.game.entity.construct;

import com.voyager.chase.R;
import com.voyager.chase.game.World;
import com.voyager.chase.game.entity.Renderable;
import com.voyager.chase.game.entity.player.Player;
import com.voyager.chase.game.event.ViewChangeEvent;
import com.voyager.chase.mqtt.payload.GameInfoPayload;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by miguellysanchez on 8/6/16.
 */
public class DecoyConstruct extends Construct {
    public DecoyConstruct() {
        mConstructName = "DECOY";
        renderDrawableId = R.drawable.chase_ic_player_spy;
        isObstacle = true;
        isLocked = false;
        isInvulnerable = false;
        spyVisibility = Renderable.GLOBALLY_VISIBLE;
        sentryVisibility = Renderable.PROXIMITY_VISIBLE;
    }

    @Override
    protected boolean onTriggered(Player player) {
        return false;
    }

    @Override
    public void onAddedToTile() {
    }

    @Override
    public void onRemovedFromTile() {
        if (World.getUserPlayer().getRole().equals(mOwner.getRole())) {
            GameInfoPayload gameInfoPayload = new GameInfoPayload();
            gameInfoPayload.setSenderRole(mOwner.getRole());
            gameInfoPayload.setSenderMessage(String.format("Your DECOY at Room <%s>, tile[%d,%d] was destroyed", getCurrentRoomName(), getCurrentTileX(), getCurrentTileY()));
            ViewChangeEvent viewChangeEvent = new ViewChangeEvent();
            viewChangeEvent.addViewChangeType(ViewChangeEvent.GAME_INFO_UPDATE);
            viewChangeEvent.setGameInfoUpdate(gameInfoPayload.toJson());
            EventBus.getDefault().post(viewChangeEvent);
        }
    }
}
