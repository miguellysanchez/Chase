package com.voyager.chase.game.skill.spy;

import com.voyager.chase.game.World;
import com.voyager.chase.game.entity.Tile;
import com.voyager.chase.game.skill.Skill;
import com.voyager.chase.game.worldeffect.WorldEffect;
import com.voyager.chase.utility.TileUtility;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by miguellysanchez on 7/25/16.
 */
public class PlantDecoySkill extends Skill {
    @Override
    public ArrayList<Tile> getSelectableTiles() {
        ArrayList<Tile> selectableTileArrayList = new ArrayList<>();
        for (int x = mSkillOwner.getCurrentTileX() - 1; x <= mSkillOwner.getCurrentTileX() + 1; x++) {
            for (int y = mSkillOwner.getCurrentTileY() - 1; y <= mSkillOwner.getCurrentTileY() + 1; y++) {
                if (TileUtility.isWithinRoom(x, y) && !(x == mSkillOwner.getCurrentTileX() && y == mSkillOwner.getCurrentTileY())) {
                    Tile selectableTile = World.getInstance().getRoom(mSkillOwner.getCurrentRoomName()).getTileAtCoordinates(x, y);
                    if (!selectableTile.isLocked()) {
                        selectableTileArrayList.add(selectableTile);
                    }
                }
            }
        }
        return selectableTileArrayList;
    }

    @Override
    public void useSkillOnTile(Tile selectedTile) {
        WorldEffect decoySkill = new WorldEffect();
        decoySkill.setEffectType(WorldEffect.ADD_CONSTRUCT);

        decoySkill.setEffectContent(getSkillName());
        decoySkill.setAffectedUUID(UUID.randomUUID().toString());
        decoySkill.setAffectedRole(World.getUserPlayer().getRole());
        decoySkill.setAffectedRoom(selectedTile.getRoomName());
        decoySkill.setAffectedX(selectedTile.getXCoordinate());
        decoySkill.setAffectedY(selectedTile.getYCoordinate());
        World.getInstance().addWorldEffectToQueue(decoySkill);
    }

}
