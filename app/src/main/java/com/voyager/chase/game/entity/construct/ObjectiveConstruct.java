package com.voyager.chase.game.entity.construct;

import com.voyager.chase.R;
import com.voyager.chase.game.World;
import com.voyager.chase.game.entity.Renderable;
import com.voyager.chase.game.entity.player.Player;
import com.voyager.chase.game.entity.player.Sentry;
import com.voyager.chase.game.entity.player.Spy;
import com.voyager.chase.game.event.ViewChangeEvent;
import com.voyager.chase.mqtt.payload.GameInfoPayload;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by miguellysanchez on 8/5/16.
 */
public class ObjectiveConstruct extends Construct {

    public static final String OBJECTIVE_CONSTRUCT_NAME = "OBJECTIVE_CONSTRUCT";

    public ObjectiveConstruct(String id) {
        idString = id;
        mOwner = Sentry.getInstance();
        mConstructName = OBJECTIVE_CONSTRUCT_NAME;
        renderDrawableId = R.drawable.chase_ic_construct_objective;
        isObstacle = true;
        isLocked = true;
        isInvulnerable = true;
        spyVisibility = Renderable.PROXIMITY_VISIBLE;
        sentryVisibility = Renderable.GLOBALLY_VISIBLE;
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
        Spy.getInstance().reduceObjectivesRemaining();
        GameInfoPayload payload = new GameInfoPayload();
        payload.setSenderRole(Spy.getInstance().getRole());
        if(World.getUserPlayer().getRole().equals(Spy.getInstance().getRole())){
            payload.setSenderMessage(String.format("One of your target OBJECTIVES was destroyed! %d objectives remaining", Spy.getInstance().getObjectivesRemaining()));
        } else {
            payload.setNonSenderMessage(String.format("The OBJECTIVE in Room<%s> [%d,%d] was destroyed!!", getCurrentRoomName(), getCurrentTileX(), getCurrentTileY()));
        }

        ViewChangeEvent viewChangeEvent = new ViewChangeEvent();
        viewChangeEvent.addViewChangeType(ViewChangeEvent.GAME_INFO_UPDATE);
        viewChangeEvent.setGameInfoUpdate(payload.toJson());
        EventBus.getDefault().post(viewChangeEvent);
    }
}
