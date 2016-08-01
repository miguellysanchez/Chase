package com.voyager.chase.game;

import com.voyager.chase.game.entity.Tile;
import com.voyager.chase.game.mods.WorldEffect;

import java.util.Collection;

/**
 * Created by miguellysanchez on 6/22/16.
 */
public class EffectInterpreter {
    public static void interpretEffect(WorldEffect worldEffect) {
        switch (worldEffect.getEffectType()) {
            case WorldEffect.ADD_PLAYER:
                break;
            case WorldEffect.REMOVE_PLAYER:
                break;
            case WorldEffect.MODIFY_PLAYER:
                break;
            case WorldEffect.ADD_SKILL_ITEM:
                break;
            case WorldEffect.REMOVE_SKILL_ITEM:
                Collection<Tile> worldItemLocations = World.getInstance().removeAllWorldItemLocation(worldEffect.getEffectType());
                for(Tile tile: worldItemLocations){
                    tile.removeItem(worldEffect.get);
                }
                break;
        }
    }
}
