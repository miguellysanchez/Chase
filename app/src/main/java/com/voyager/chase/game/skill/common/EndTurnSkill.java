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
    public ArrayList<Tile> getSelectableTiles() {
        return null;
    }

    @Override
    public void useSkillOnTile(Tile selectedTile) {
        getOwner().setActionPoints(0);
    }

}
