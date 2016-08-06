package com.voyager.chase.game.entity.construct;

import com.voyager.chase.R;
import com.voyager.chase.game.World;
import com.voyager.chase.game.entity.Renderable;
import com.voyager.chase.game.entity.Tile;
import com.voyager.chase.game.entity.player.Player;
import com.voyager.chase.game.event.ViewChangeEvent;
import com.voyager.chase.game.worldeffect.WorldEffect;
import com.voyager.chase.mqtt.payload.GameInfoPayload;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by miguellysanchez on 8/6/16.
 */
public class StunTrapConstruct extends Construct {
    public StunTrapConstruct() {
        mConstructName = "STUN TRAP";
        renderDrawableId = R.drawable.chase_ic_construct_stun_trap;
        isObstacle = false;
        isLocked = false;
        isInvulnerable = false;
        spyVisibility = Renderable.HIDDEN;
        sentryVisibility = Renderable.GLOBALLY_VISIBLE;
    }

    @Override
    protected boolean onTriggered(Player player) {
        if (!mOwner.getRole().equals(player.getRole())) {
            WorldEffect skipPlayerTurn = new WorldEffect();
            skipPlayerTurn.setEffectType(WorldEffect.MODIFY_PLAYER);
            skipPlayerTurn.setAffectedRole(player.getRole());
            skipPlayerTurn.setEffectContent(WorldEffect.SKIP_PLAYER_TURN);
            World.getInstance().addWorldEffectToQueue(skipPlayerTurn);

            WorldEffect destroySelfEffect = new WorldEffect();
            destroySelfEffect.setEffectType(WorldEffect.REMOVE_CONSTRUCT);
            destroySelfEffect.setAffectedUUID(mIdString);
            World.getInstance().addWorldEffectToQueue(destroySelfEffect);

            GameInfoPayload payload = new GameInfoPayload();
            payload.setSenderRole(player.getRole());
            payload.setSenderMessage("Triggered a STUN TRAP. End current turn and skip your next turn.");
            payload.setNonSenderMessage(String.format("Your STUN TRAP in Room <%s>, Tile [%d,%d] was triggered", getCurrentRoomName(), getCurrentTileX(), getCurrentTileY()));

            ViewChangeEvent viewChangeEvent = new ViewChangeEvent();
            viewChangeEvent.addViewChangeType(ViewChangeEvent.GAME_INFO_UPDATE);
            viewChangeEvent.setGameInfoUpdate(payload.toJson());
            EventBus.getDefault().post(viewChangeEvent);
            return true;
        }
        return false;
    }

    @Override
    public void onAddedToTile() {
        Tile currentTile = World.getInstance().getRoom(getCurrentRoomName())
                .getTileAtCoordinates(getCurrentTileX(), getCurrentTileY());
        currentTile.addTrigger(mIdString, new Trigger(this));
        World.getInstance().addWorldItemLocation(mIdString, currentTile);
    }

    @Override
    public void onRemovedFromTile() {
        World.getInstance().removeAllWorldItemLocations(mIdString);
    }
}
