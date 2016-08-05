package com.voyager.chase.game.worldeffect;

import android.content.Context;

import com.voyager.chase.game.World;
import com.voyager.chase.game.entity.Tile;
import com.voyager.chase.game.entity.player.Player;
import com.voyager.chase.game.entity.player.Sentry;
import com.voyager.chase.game.entity.player.Spy;
import com.voyager.chase.game.entity.construct.Construct;
import com.voyager.chase.game.entity.construct.ConstructsPool;

import java.util.Collection;

import timber.log.Timber;

/**
 * Created by miguellysanchez on 6/22/16.
 */
public class EffectInterpreter {
    public static void applyEffect(Context context, WorldEffect worldEffect) {
        Player player;
        switch (worldEffect.getEffectType()) {
            case WorldEffect.MOVE_PLAYER:
                Timber.d("[MOVE_PLAYER]: %s" , worldEffect.toString());
                if (Player.SPY_ROLE.equals(worldEffect.getAffectedRole())) {
                    player = Spy.getInstance();
                } else if (Player.SENTRY_ROLE.equals(worldEffect.getAffectedRole())) {
                    player = Sentry.getInstance();
                } else {
                    throw new IllegalStateException("No player matched for the world effect.");
                }
                World.getInstance().getRoom(player.getCurrentRoomName())
                        .getTileAtCoordinates(player.getCurrentTileXCoordinate(), player.getCurrentTileYCoordinate()).removePlayer();
                World.getInstance().getRoom(worldEffect.getAffectedRoom())
                        .getTileAtCoordinates(worldEffect.getAffectedX(), worldEffect.getAffectedY()).setPlayer(player);
                break;
            case WorldEffect.MODIFY_PLAYER:
                Timber.d("[MODIFY_PLAYER]: %s" , worldEffect.toString());

                if (Player.SPY_ROLE.equals(worldEffect.getAffectedRole())) {
                    player = Spy.getInstance();
                } else if (Player.SENTRY_ROLE.equals(worldEffect.getAffectedRole())) {
                    player = Sentry.getInstance();
                } else {
                    throw new IllegalStateException("No player matched for the world effect.");
                }
                applyModifyPlayerEffect(player, worldEffect.getEffectContent());
                break;
            case WorldEffect.ADD_CONSTRUCT:
                Timber.d("[ADD_CONSTRUCT]: %s" , worldEffect.toString());

                Construct construct = ConstructsPool.getConstruct(context, worldEffect.getEffectContent(), worldEffect.getAffectedRole(), worldEffect.getAffectedUUID());
                Tile targetTile = World.getInstance().getRoom(worldEffect.getAffectedRoom()).getTileAtCoordinates( worldEffect.getAffectedX(), worldEffect.getAffectedY());
                World.getInstance().addWorldItemLocation(construct.getId(),targetTile);
                targetTile.addConstruct(construct);
                break;
            case WorldEffect.REMOVE_CONSTRUCT:
                Timber.d("[REMOVE_CONSTRUCT]: %s" , worldEffect.toString());

                Collection<Tile> worldItemLocations = World.getInstance().removeAllWorldItemLocations(worldEffect.getAffectedUUID());
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
                player.setActionPoints(0);
                player.setIsTurnSkipped(true);
                break;
        }

    }

}
