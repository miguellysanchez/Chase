package com.voyager.chase.game.entity.construct;

import com.voyager.chase.R;
import com.voyager.chase.game.entity.Renderable;
import com.voyager.chase.game.entity.player.Player;

/**
 * Created by miguellysanchez on 8/6/16.
 */
public class BarrierConstruct extends Construct {
    public BarrierConstruct() {
        mConstructName = "BARRIER";
        renderDrawableId = R.drawable.chase_ic_construct_barrier;
        isObstacle = true;
        isLocked = true;
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
    }
}
