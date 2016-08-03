package com.voyager.chase.game.entity.construct;

import com.voyager.chase.R;
import com.voyager.chase.game.World;
import com.voyager.chase.game.entity.player.Player;
import com.voyager.chase.game.mods.WorldEffect;

/**
 * Created by miguellysanchez on 8/3/16.
 */
public class TeleporterExitConstruct extends Construct {

    public TeleporterExitConstruct() {
        renderDrawableId = R.drawable.chase_ic_construct_teleporter_exit;
        isObstacle = true;
        isInvulnerable = true;
        isUntargetable = true;
        spyVisibility = GLOBALLY_VISIBLE;
        sentryVisibility = GLOBALLY_VISIBLE;
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
    }
}
