package com.voyager.chase.game.entity;

import com.voyager.chase.game.World;
import com.voyager.chase.game.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by miguellysanchez on 7/5/16.
 */
public abstract class SkillItem extends Renderable{
    protected String uuidString;
    protected Player mOwner;
    protected HashMap<ItemTrigger, Tile> triggerTileHashMap;
    protected boolean isPassable = true;
    protected boolean isInvulnerable = false;

    protected SkillItem(){
        uuidString = UUID.randomUUID().toString();
    }

    protected abstract void onTriggered(World world);

    protected abstract void onDestroyed(World world);

    protected void removeAllTriggers(){
        for(Map.Entry<ItemTrigger, Tile> entry : triggerTileHashMap.entrySet()){
            ItemTrigger itemTrigger = entry.getKey();
            Tile tileWithTrigger = entry.getValue();
//            tile.get
        }
    }
}
