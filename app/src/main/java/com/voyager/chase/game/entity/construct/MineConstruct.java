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
 * Created by miguellysanchez on 7/29/16.
 */
public class MineConstruct extends Construct {
    protected MineConstruct() {
        mConstructName = "MINE";
        renderDrawableId = R.drawable.chase_ic_construct_mine;
        isObstacle = false;
        isLocked = false;
        isInvulnerable = false;
        spyVisibility = Renderable.GLOBALLY_VISIBLE;
        sentryVisibility = Renderable.HIDDEN;
    }

    @Override
    protected boolean onTriggered(Player player) {
        if(!mOwner.getRole().equals(player.getRole())){
            WorldEffect damagePlayerEffect = new WorldEffect();
            damagePlayerEffect.setEffectType(WorldEffect.MODIFY_PLAYER);
            damagePlayerEffect.setAffectedRole(player.getRole());
            damagePlayerEffect.setEffectContent(WorldEffect.REDUCE_PLAYER_LIFE);
            World.getInstance().addWorldEffectToQueue(damagePlayerEffect);

            WorldEffect destroySelfEffect = new WorldEffect();
            destroySelfEffect.setEffectType(WorldEffect.REMOVE_CONSTRUCT);
            destroySelfEffect.setAffectedUUID(idString);
            World.getInstance().addWorldEffectToQueue(destroySelfEffect);

            GameInfoPayload payload = new GameInfoPayload();
            payload.setSenderRole(player.getRole());
            payload.setSenderMessage("You have stepped on a MINE. You lost a life");

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
                .getTileAtCoordinates(getCurrentTileXCoordinate(), getCurrentTileYCoordinate());
        currentTile.addTrigger(idString, new Trigger(this));
        World.getInstance().addWorldItemLocation(idString, currentTile);
    }

    @Override
    public void onRemovedFromTile() {
        World.getInstance().removeAllWorldItemLocations(idString);
    }
}
