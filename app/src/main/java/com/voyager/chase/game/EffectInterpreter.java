package com.voyager.chase.game;

import com.voyager.chase.game.entity.Tile;
import com.voyager.chase.game.entity.player.Player;
import com.voyager.chase.game.entity.player.Sentry;
import com.voyager.chase.game.entity.player.Spy;
import com.voyager.chase.game.entity.skillitem.SkillItem;
import com.voyager.chase.game.entity.skillitem.SkillItemsPool;
import com.voyager.chase.game.mods.WorldEffect;

import java.util.Collection;

/**
 * Created by miguellysanchez on 6/22/16.
 */
public class EffectInterpreter {
    public static void applyEffect(WorldEffect worldEffect) {
        Player player = null;
        switch (worldEffect.getEffectType()) {
            case WorldEffect.MOVE_PLAYER:
                if (Player.SPY_ROLE.equals(worldEffect.getAffectedRole())) {
                    player = Spy.getInstance();
                } else if (Player.SENTRY_ROLE.equals(worldEffect.getAffectedRole())) {
                    player = Sentry.getInstance();
                } else {
                    throw new IllegalStateException("No player matched for the world effect.");
                }
                World.getInstance().getRoom(player.getCurrentRoomName())
                        .getTileAtCoordinate(player.getCurrentTileXCoordinate(), player.getCurrentTileYCoordinate()).removePlayer();
                World.getInstance().getRoom(worldEffect.getAffectedRoom())
                        .getTileAtCoordinate(worldEffect.getAffectedX(), worldEffect.getAffectedY()).setPlayer(player);
                break;
            case WorldEffect.MODIFY_PLAYER:
                if (Player.SPY_ROLE.equals(worldEffect.getAffectedRole())) {
                    player = Spy.getInstance();
                } else if (Player.SENTRY_ROLE.equals(worldEffect.getAffectedRole())) {
                    player = Sentry.getInstance();
                } else {
                    throw new IllegalStateException("No player matched for the world effect.");
                }
                applyModifyPlayerEffect(player, worldEffect.getEffectContent());
                break;
            case WorldEffect.ADD_SKILL_ITEM:
                SkillItem skillItem = SkillItemsPool.getSkillItem(worldEffect.getEffectContent());
                Tile targetTile = World.getInstance().getRoom(worldEffect.getAffectedRoom()).getTileAtCoordinate( worldEffect.getAffectedX(), worldEffect.getAffectedX());
                World.getInstance().addWorldItemLocation(worldEffect.getAffectedUUID(),targetTile);
                targetTile.addSkillItem(skillItem);
                break;
            case WorldEffect.REMOVE_SKILL_ITEM:
                Collection<Tile> worldItemLocations = World.getInstance().removeAllWorldItemLocations(worldEffect.getEffectType());
                for (Tile tile : worldItemLocations) {
                    tile.removeItem(worldEffect.getAffectedUUID());
                }
                break;
        }
    }

    private static void applyModifyPlayerEffect(Player player, String effectContent) {
        switch (effectContent) {
            case WorldEffect.REDUCE_PLAYER_LIFE:
                player.reduceLife();
                break;
            case WorldEffect.RECOVER_PLAYER_LIFE:
                player.recoverLife();
                break;
            case WorldEffect.SKIP_PLAYER_TURN:
                player.setIsTurnSkipped(true);
                break;
        }

    }

}
