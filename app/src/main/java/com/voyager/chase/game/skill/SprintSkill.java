package com.voyager.chase.game.skill;

import com.voyager.chase.game.World;
import com.voyager.chase.game.entity.Tile;

import java.util.ArrayList;

/**
 * Created by miguellysanchez on 7/25/16.
 */
public class SprintSkill extends Skill {
    @Override
    public ArrayList<Tile> getSelectableTiles() {
        return null;
    }

    @Override
    public void useSkillOnTile(Tile selectedTile) {

    }

    @Override
    public void onSkillSelected() {

    }

    @Override
    public void triggerSkillEffect(World world) {

    }
}
