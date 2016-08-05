package com.voyager.chase.game.skill.sentry;

import com.voyager.chase.game.World;
import com.voyager.chase.game.entity.Tile;
import com.voyager.chase.game.skill.Skill;
import com.voyager.chase.game.worldeffect.WorldEffect;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by miguellysanchez on 7/25/16.
 */
public class LuxGeneratorSkill extends Skill {
    @Override
    public ArrayList<Tile> getSelectableTiles() {
        Tile currentTile = World.getInstance().getRoom(mSkillOwner.getCurrentRoomName())
                .getTileAtCoordinates(mSkillOwner.getCurrentTileX(), mSkillOwner.getCurrentTileY());
        if (currentTile.isLocked()) {
            return new ArrayList<>();
        } else {
            return null;
        }
    }

    @Override
    public void useSkillOnTile(Tile selectedTile) {
        WorldEffect luxEffect = new WorldEffect();
        luxEffect.setEffectType(WorldEffect.ADD_CONSTRUCT);

        luxEffect.setEffectContent(getSkillName());
        luxEffect.setAffectedUUID(UUID.randomUUID().toString());
        luxEffect.setAffectedRole(World.getUserPlayer().getRole());
        luxEffect.setAffectedRoom(selectedTile.getRoomName());
        luxEffect.setAffectedX(selectedTile.getXCoordinate());
        luxEffect.setAffectedY(selectedTile.getYCoordinate());
        World.getInstance().addWorldEffectToQueue(luxEffect);
    }

}
