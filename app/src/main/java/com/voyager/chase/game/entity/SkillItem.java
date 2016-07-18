package com.voyager.chase.game.entity;

import com.voyager.chase.game.World;
import com.voyager.chase.game.entity.player.Player;

import java.util.ArrayList;

/**
 * Created by miguellysanchez on 7/5/16.
 */
public abstract class SkillItem extends Renderable{
    protected Player mOwner;
    protected ArrayList<Tile> affectedTiles;
    protected boolean isPassable = true;

    public abstract class ItemTrigger {

    }

    protected abstract void onTriggered(World world);

    protected abstract void onDestroyed(World world);
}
