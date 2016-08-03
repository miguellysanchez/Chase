package com.voyager.chase.game.skill.sentry;

import com.voyager.chase.game.World;
import com.voyager.chase.game.entity.Room;
import com.voyager.chase.game.entity.Tile;
import com.voyager.chase.game.entity.player.Player;
import com.voyager.chase.game.entity.construct.Construct;
import com.voyager.chase.game.mods.WorldEffect;
import com.voyager.chase.game.skill.Skill;

import java.util.ArrayList;

/**
 * Created by miguellysanchez on 7/25/16.
 */
public class AttackSkill extends Skill {
    @Override
    public ArrayList<Tile> getSelectableTiles() {
        ArrayList<Tile> selectableTilesList = new ArrayList<>();
        Player player = World.getUserPlayer();
        String roomName = player.getCurrentRoomName();
        int currentTileX = player.getCurrentTileXCoordinate();
        int currentTileY = player.getCurrentTileYCoordinate();

        safeAddToSelectableTiles(selectableTilesList, checkTileAtCoordinates(roomName, currentTileX + 1, currentTileY));
        safeAddToSelectableTiles(selectableTilesList, checkTileAtCoordinates(roomName, currentTileX - 1, currentTileY));
        safeAddToSelectableTiles(selectableTilesList, checkTileAtCoordinates(roomName, currentTileX, currentTileY + 1));
        safeAddToSelectableTiles(selectableTilesList, checkTileAtCoordinates(roomName, currentTileX, currentTileY - 1));

        return selectableTilesList;
    }

    @Override
    public void useSkillOnTile(Tile selectedTile) {
        if (selectedTile.containsPlayer()) {
            Player attackedPlayer = selectedTile.getPlayer();
            WorldEffect playerDamageWorldEffect = new WorldEffect();
            playerDamageWorldEffect.setEffectType(WorldEffect.MODIFY_PLAYER);
            playerDamageWorldEffect.setAffectedRole(attackedPlayer.getRole());
            playerDamageWorldEffect.setEffectContent(WorldEffect.REDUCE_PLAYER_LIFE);
            World.getInstance().addWorldEffectToQueue(playerDamageWorldEffect);
        }
        ArrayList<Construct> constructList = selectedTile.getAllConstructsList();
        for (Construct construct : constructList) {
            WorldEffect removeConstructEffect = new WorldEffect();
            removeConstructEffect.setEffectType(WorldEffect.REMOVE_CONSTRUCT);
            removeConstructEffect.setEffectContent(construct.getUUID());
            World.getInstance().addWorldEffectToQueue(removeConstructEffect);
        }
    }

    private Tile checkTileAtCoordinates(String roomName, int x, int y) {
        Tile checkingTile;
        if (x >= 0 && x < Room.ROOM_WIDTH && y >= 0 && y < Room.ROOM_HEIGHT) {
            checkingTile = World.getInstance().getRoom(roomName).getTileAtCoordinate(x, y);
            if (checkingTile != null && !checkingTile.isUntargetable()) {
                return checkingTile;
            }
        }
        return null;
    }

    private void safeAddToSelectableTiles(ArrayList<Tile> selectableTiles, Tile tile) {
        if (tile != null) {
            selectableTiles.add(tile);
        }
    }
}
