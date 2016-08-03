package com.voyager.chase.game.entity.construct;

import com.voyager.chase.R;
import com.voyager.chase.game.World;
import com.voyager.chase.game.entity.Renderable;
import com.voyager.chase.game.entity.Tile;
import com.voyager.chase.game.entity.player.Player;
import com.voyager.chase.game.mods.WorldEffect;

/**
 * Created by miguellysanchez on 7/29/16.
 */
public class MineConstruct extends Construct {
    protected MineConstruct() {
        renderDrawableId = R.drawable.chase_drawable_ic_construct_mine;
        isObstacle = false;
        isUntargetable = false;
        isInvulnerable = false;
        spyVisibility = Renderable.GLOBALLY_VISIBLE;
        sentryVisibility = Renderable.HIDDEN;
    }

    @Override
    public boolean onTriggered(Player player) {
        if(!mOwner.getRole().equals(player.getRole())){
            WorldEffect damagePlayerEffect = new WorldEffect();
            damagePlayerEffect.setEffectType(WorldEffect.MODIFY_PLAYER);
            damagePlayerEffect.setAffectedRole(player.getRole());
            damagePlayerEffect.setEffectContent(WorldEffect.REDUCE_PLAYER_LIFE);
            World.getInstance().addWorldEffectToQueue(damagePlayerEffect);

            WorldEffect destroySelfEffect = new WorldEffect();
            destroySelfEffect.setEffectType(WorldEffect.REMOVE_CONSTRUCT);
            destroySelfEffect.setAffectedUUID(uuidString);
            World.getInstance().addWorldEffectToQueue(destroySelfEffect);
            return true;
        }
        return false;
    }

    @Override
    public void onAddedToTile() {
        Tile currentTile = World.getInstance().getRoom(getCurrentRoomName())
                .getTileAtCoordinate(getCurrentTileXCoordinate(), getCurrentTileYCoordinate());
        currentTile.addTrigger(uuidString, new Trigger(this));
        World.getInstance().addWorldItemLocation(uuidString, currentTile);
    }

    @Override
    public void onRemovedFromTile() {
        World.getInstance().removeAllWorldItemLocations(uuidString);

        //TODO add info message
    }
}
