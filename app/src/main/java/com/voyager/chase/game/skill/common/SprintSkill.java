package com.voyager.chase.game.skill.common;

import com.voyager.chase.game.World;
import com.voyager.chase.game.entity.Tile;
import com.voyager.chase.game.skill.Skill;
import com.voyager.chase.game.worldeffect.WorldEffect;
import com.voyager.chase.utility.TileUtility;

import java.util.ArrayList;

/**
 * Created by miguellysanchez on 7/25/16.
 */
public class SprintSkill extends Skill {
    @Override
    public ArrayList<Tile> getSelectableTiles() {
        ArrayList<Tile> selectableTileArrayList = new ArrayList<>();
        for (int x = mSkillOwner.getCurrentTileX() - 2; x <= mSkillOwner.getCurrentTileX() + 2; x++) {
            for (int y = mSkillOwner.getCurrentTileY() - 2; y <= mSkillOwner.getCurrentTileY() + 2; y++) {
                if (TileUtility.isWithinRoom(x, y)) {
                    Tile targetableTile = World.getInstance().getRoom(mSkillOwner.getCurrentRoomName()).getTileAtCoordinates(x, y);
                    if (!targetableTile.containsObstacle()) {
                        selectableTileArrayList.add(targetableTile);
                    }
                }
            }
        }
        return selectableTileArrayList;
    }

    @Override
    public void useSkillOnTile(Tile selectedTile) {
        if (!selectedTile.containsObstacle()) {
            WorldEffect sprintSkillEffect = new WorldEffect();
            sprintSkillEffect.setAffectedRole(mSkillOwner.getRole());
            sprintSkillEffect.setEffectType(WorldEffect.MOVE_PLAYER);
            sprintSkillEffect.setAffectedRoom(selectedTile.getRoomName());
            sprintSkillEffect.setAffectedX(selectedTile.getXCoordinate());
            sprintSkillEffect.setAffectedY(selectedTile.getYCoordinate());
            World.getInstance().addWorldEffectToQueue(sprintSkillEffect);
        }
    }

}
