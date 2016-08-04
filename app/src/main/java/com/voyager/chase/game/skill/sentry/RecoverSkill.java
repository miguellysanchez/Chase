package com.voyager.chase.game.skill.sentry;

import com.voyager.chase.game.World;
import com.voyager.chase.game.entity.Tile;
import com.voyager.chase.game.skill.Skill;
import com.voyager.chase.game.worldeffect.WorldEffect;

import java.util.ArrayList;

/**
 * Created by miguellysanchez on 7/25/16.
 */
public class RecoverSkill extends Skill {
    @Override
    public ArrayList<Tile> getSelectableTiles() {
        return null;
    }

    @Override
    public void useSkillOnTile(Tile selectedTile) {
        WorldEffect healSelfEffect = new WorldEffect();
        healSelfEffect.setEffectType(WorldEffect.MODIFY_PLAYER);
        healSelfEffect.setAffectedRole(mSkillOwner.getRole());
        healSelfEffect.setEffectContent(WorldEffect.RECOVER_PLAYER_LIFE);
        World.getInstance().addWorldEffectToQueue(healSelfEffect);
    }

}
