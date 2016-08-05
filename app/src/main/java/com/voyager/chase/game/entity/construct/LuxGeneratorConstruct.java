package com.voyager.chase.game.entity.construct;

import com.voyager.chase.R;
import com.voyager.chase.game.World;
import com.voyager.chase.game.entity.Renderable;
import com.voyager.chase.game.entity.Tile;
import com.voyager.chase.game.entity.player.Player;

/**
 * Created by miguellysanchez on 8/5/16.
 */
public class LuxGeneratorConstruct extends Construct {

    protected LuxGeneratorConstruct() {
        mConstructName = "LUX GENERATOR";
        renderDrawableId = R.drawable.chase_ic_construct_lux_generator;
        isObstacle = false;
        isLocked = false;
        isInvulnerable = false;
        spyVisibility = Renderable.GLOBALLY_VISIBLE;
        sentryVisibility = Renderable.GLOBALLY_VISIBLE;
    }

    @Override
    protected boolean onTriggered(Player player) {
        return false;
    }

    @Override
    public void onAddedToTile() {
        for (Tile tile : World.getInstance().getRoom(getCurrentRoomName()).getAllTiles()) {
            tile.addVisibilityModifier(idString, Tile.GLOBAL_VISIBILITY_MOD);
            World.getInstance().addWorldItemLocation(idString, tile);
        }
    }

    @Override
    public void onRemovedFromTile() {
        World.getInstance().removeAllWorldItemLocations(idString);
    }
}
