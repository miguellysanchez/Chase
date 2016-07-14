package com.voyager.chase.game.skill.common;

import com.voyager.chase.game.World;
import com.voyager.chase.game.entity.Tile;
import com.voyager.chase.game.skill.Skill;

import java.util.ArrayList;

/**
 * Created by miguellysanchez on 7/7/16.
 */
public class MoveSkill extends Skill {
    @Override
    protected ArrayList<Tile> getSelectableTiles() {
        return null;
    }

    @Override
    protected void useSkillOnTile(Tile selectedTile) {

    }

    @Override
    protected void triggerTargetSelection(World world) {

    }

    @Override
    protected void triggerSkillEffect(World world) {

    }
}
