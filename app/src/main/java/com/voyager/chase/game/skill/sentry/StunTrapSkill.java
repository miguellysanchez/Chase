package com.voyager.chase.game.skill.sentry;

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
public class StunTrapSkill extends Skill {
    @Override
    public ArrayList<Tile> getSelectableTiles() {
        ArrayList<Tile> selectableTileArrayList = new ArrayList<>();
        for (int x = mSkillOwner.getCurrentTileX() - 1; x <= mSkillOwner.getCurrentTileX() + 1; x++) {
            for (int y = mSkillOwner.getCurrentTileY() - 1; y <= mSkillOwner.getCurrentTileY() + 1; y++) {
                if (TileUtility.isWithinRoom(x, y)) {
                    Tile selectableTile = World.getInstance().getRoom(mSkillOwner.getCurrentRoomName()).getTileAtCoordinates(x, y);
                    if (!selectableTile.isLocked() && !selectableTile.containsPlayer()) {
                        selectableTileArrayList.add(selectableTile);
                    }
                }
            }
        }
        return selectableTileArrayList;
    }

    @Override
    public void useSkillOnTile(Tile selectedTile) {
        WorldEffect stunTrapEffect = new WorldEffect();
        stunTrapEffect.setEffectType(WorldEffect.ADD_CONSTRUCT);

        stunTrapEffect.setEffectContent(getSkillName());
        stunTrapEffect.setAffectedUUID(UUID.randomUUID().toString());
        stunTrapEffect.setAffectedRole(World.getUserPlayer().getRole());
        stunTrapEffect.setAffectedRoom(selectedTile.getRoomName());
        stunTrapEffect.setAffectedX(selectedTile.getXCoordinate());
        stunTrapEffect.setAffectedY(selectedTile.getYCoordinate());
        World.getInstance().addWorldEffectToQueue(stunTrapEffect);
    }

}
