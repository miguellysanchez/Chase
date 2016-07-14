package com.voyager.chase.game.entity;

import com.voyager.chase.game.World;
import com.voyager.chase.game.entity.player.Player;

import java.util.ArrayList;

/**
 * Created by miguellysanchez on 7/5/16.
 */
public abstract class SkillItem {

    public static final int HIDDEN = 0;
    public static final int PROXIMITY_VISIBLE = 1;
    public static final int GLOBALLY_VISIBLE = 2;

    protected Player mOwner;
    protected int mOwnerVisibility;
    protected int mNonOwnerVisibility;
    protected ArrayList<Tile> affectedTiles;

    public abstract class ItemTrigger{

    }

    protected abstract void onTriggered(World world);
    protected abstract void onDestroyed(World world);
}
