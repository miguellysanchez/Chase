package com.voyager.chase.game.skill.common;

import com.voyager.chase.game.World;
import com.voyager.chase.game.entity.Tile;
import com.voyager.chase.game.skill.Skill;

import java.util.ArrayList;

/**
 * Created by miguellysanchez on 7/18/16.
 */
public class EndTurnSkill extends Skill {
    @Override
    public ArrayList<Tile> getSelectableTiles(World world) {
        ArrayList<Tile> targetTiles = new ArrayList<>();
        Tile currentTile = world.getRoom(getOwner().getCurrentRoomName()).getTileAtCoordinate(getOwner().getCurrentTileXCoordinate(), getOwner().getCurrentTileYCoordinate());
        targetTiles.add(currentTile);
        return targetTiles;
    }

    @Override
    public void useSkillOnTile(Tile selectedTile) {
        getOwner().setActionPoints(0);
    }

    @Override
    public void triggerTargetSelection(World world) {

    }

    @Override
    public void triggerSkillEffect(World world) {

    }
}
