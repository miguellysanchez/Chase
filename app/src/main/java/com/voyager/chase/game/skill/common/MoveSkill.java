package com.voyager.chase.game.skill.common;

import com.voyager.chase.game.World;
import com.voyager.chase.game.entity.Tile;
import com.voyager.chase.game.skill.Skill;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by miguellysanchez on 7/7/16.
 */
public class MoveSkill extends Skill {
    @Override
    public ArrayList<Tile> getSelectableTiles(World world) {
        return null;
    }

    @Override
    public void useSkillOnTile(Tile selectedTile) {

    }

    @Override
    public void triggerTargetSelection(World world) {

    }

    @Override
    public void triggerSkillEffect(World world) {

    }
}
