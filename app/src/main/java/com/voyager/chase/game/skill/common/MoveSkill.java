package com.voyager.chase.game.skill.common;

import com.voyager.chase.game.World;
import com.voyager.chase.game.entity.Tile;
import com.voyager.chase.game.entity.player.Player;
import com.voyager.chase.game.worldeffect.WorldEffect;
import com.voyager.chase.game.skill.Skill;
import com.voyager.chase.utility.TileUtility;

import java.util.ArrayList;

/**
 * Created by miguellysanchez on 7/7/16.
 */
public class MoveSkill extends Skill {

    @Override
    public ArrayList<Tile> getSelectableTiles() {
        ArrayList<Tile> selectableTilesList = new ArrayList<>();
        Player player = World.getUserPlayer();
        String roomName = player.getCurrentRoomName();
        int currentTileX = player.getCurrentTileX();
        int currentTileY = player.getCurrentTileY();

        safeAddToSelectableTiles(selectableTilesList, checkTileAtCoordinates(roomName, currentTileX+1, currentTileY));
        safeAddToSelectableTiles(selectableTilesList, checkTileAtCoordinates(roomName, currentTileX-1, currentTileY));
        safeAddToSelectableTiles(selectableTilesList, checkTileAtCoordinates(roomName, currentTileX, currentTileY+1));
        safeAddToSelectableTiles(selectableTilesList, checkTileAtCoordinates(roomName, currentTileX, currentTileY-1));

        return selectableTilesList;
    }

    @Override
    public void useSkillOnTile(Tile selectedTile) {
        if(!selectedTile.containsPlayer() && !selectedTile.containsObstacle()){
            WorldEffect movePlayerWorldEffect = new WorldEffect();
            movePlayerWorldEffect.setAffectedRole(World.getUserPlayer().getRole());
            movePlayerWorldEffect.setEffectType(WorldEffect.MOVE_PLAYER);
            movePlayerWorldEffect.setAffectedRoom(selectedTile.getRoomName());
            movePlayerWorldEffect.setAffectedX(selectedTile.getXCoordinate());
            movePlayerWorldEffect.setAffectedY(selectedTile.getYCoordinate());
            World.getInstance().addWorldEffectToQueue(movePlayerWorldEffect);
        }
    }

    private Tile checkTileAtCoordinates(String roomName, int x, int y) {
        Tile checkingTile;
        if (TileUtility.isWithinRoom(x,y)) {
            checkingTile = World.getInstance().getRoom(roomName).getTileAtCoordinates(x, y);
            if(checkingTile!=null && !checkingTile.containsObstacle() && !checkingTile.containsPlayer()){
                return checkingTile;
            }
        }
        return null;
    }

    private void safeAddToSelectableTiles(ArrayList<Tile> selectableTiles, Tile tile){
        if(tile != null){
            selectableTiles.add(tile);
        }
    }

}
