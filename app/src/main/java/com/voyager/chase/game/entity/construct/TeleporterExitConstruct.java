package com.voyager.chase.game.entity.construct;

import com.voyager.chase.R;
import com.voyager.chase.game.entity.player.Player;

/**
 * Created by miguellysanchez on 8/3/16.
 */
public class TeleporterExitConstruct extends Construct {

    public TeleporterExitConstruct() {
        mConstructName = "TELEPORTER EXIT";
        renderDrawableId = R.drawable.chase_ic_construct_teleporter_exit;
        isObstacle = true;
        isInvulnerable = true;
        isLocked = true;
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
