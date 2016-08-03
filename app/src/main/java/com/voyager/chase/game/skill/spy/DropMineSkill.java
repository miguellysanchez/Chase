package com.voyager.chase.game.skill.spy;

import com.voyager.chase.game.World;
import com.voyager.chase.game.entity.Tile;
import com.voyager.chase.game.mods.WorldEffect;
import com.voyager.chase.game.skill.Skill;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by miguellysanchez on 7/25/16.
 */
public class DropMineSkill extends Skill {

    @Override
    public ArrayList<Tile> getSelectableTiles() {
        Tile currentTile = World.getInstance().getRoom(mSkillOwner.getCurrentRoomName())
                .getTileAtCoordinates(mSkillOwner.getCurrentTileXCoordinate(), mSkillOwner.getCurrentTileYCoordinate());
        if(currentTile.isUntargetable()){
            return new ArrayList<>();
        } else {
            return null;
        }
    }

    @Override
    public void useSkillOnTile(Tile selectedTile) {
        WorldEffect dropMineEffect = new WorldEffect();
        dropMineEffect.setEffectType(WorldEffect.ADD_CONSTRUCT);

        dropMineEffect.setEffectContent(getSkillName());
        dropMineEffect.setAffectedUUID(UUID.randomUUID().toString());
        dropMineEffect.setAffectedRole(World.getUserPlayer().getRole());
        dropMineEffect.setAffectedRoom(selectedTile.getRoomName());
        dropMineEffect.setAffectedX(selectedTile.getXCoordinate());
        dropMineEffect.setAffectedY(selectedTile.getYCoordinate());
        World.getInstance().addWorldEffectToQueue(dropMineEffect);
    }

}
