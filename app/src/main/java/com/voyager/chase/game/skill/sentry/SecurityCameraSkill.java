package com.voyager.chase.game.skill.sentry;

import com.voyager.chase.game.World;
import com.voyager.chase.game.entity.Tile;
import com.voyager.chase.game.skill.Skill;
import com.voyager.chase.game.worldeffect.WorldEffect;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by miguellysanchez on 7/17/16.
 */
public class SecurityCameraSkill extends Skill {
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
        WorldEffect securityCameraEffect = new WorldEffect();
        securityCameraEffect.setEffectType(WorldEffect.ADD_CONSTRUCT);

        securityCameraEffect.setEffectContent(getSkillName());
        securityCameraEffect.setAffectedUUID(UUID.randomUUID().toString());
        securityCameraEffect.setAffectedRole(World.getUserPlayer().getRole());
        securityCameraEffect.setAffectedRoom(selectedTile.getRoomName());
        securityCameraEffect.setAffectedX(selectedTile.getXCoordinate());
        securityCameraEffect.setAffectedY(selectedTile.getYCoordinate());
        World.getInstance().addWorldEffectToQueue(securityCameraEffect);
    }

}
