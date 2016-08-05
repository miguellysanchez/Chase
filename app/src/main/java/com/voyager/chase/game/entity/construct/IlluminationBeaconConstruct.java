package com.voyager.chase.game.entity.construct;

import com.voyager.chase.R;
import com.voyager.chase.game.World;
import com.voyager.chase.game.entity.Renderable;
import com.voyager.chase.game.entity.Tile;
import com.voyager.chase.game.entity.player.Player;
import com.voyager.chase.utility.TileUtility;

/**
 * Created by miguellysanchez on 8/5/16.
 */
public class IlluminationBeaconConstruct extends Construct {

    protected IlluminationBeaconConstruct() {
        mConstructName = "ILLUMINATION BEACON";
        renderDrawableId = R.drawable.chase_ic_construct_illumination_beacon;
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
        for (int x = getCurrentTileX() - 2; x <= getCurrentTileX() + 2; x++) {
            for (int y = getCurrentTileY() - 2; y < getCurrentTileY() + 2; y++) {
                if (TileUtility.isWithinRoom(x, y)) {
                    Tile tile = World.getInstance().getRoom(getCurrentRoomName()).getTileAtCoordinates(x, y);
                    tile.addVisibilityModifier(idString, Tile.GLOBAL_VISIBILITY_MOD);
                    World.getInstance().addWorldItemLocation(idString, tile);
                }
            }
        }
    }

    @Override
    public void onRemovedFromTile() {
        World.getInstance().removeAllWorldItemLocations(idString);
    }
}
