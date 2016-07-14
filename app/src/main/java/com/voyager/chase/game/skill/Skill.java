package com.voyager.chase.game.skill;

import com.voyager.chase.game.World;
import com.voyager.chase.game.entity.Tile;
import com.voyager.chase.game.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by miguellysanchez on 7/5/16.
 */
public abstract class Skill {

    protected int mCooldown;
    protected String mSkillName;
    protected Player mSkillOwner;

    protected abstract ArrayList<Tile> getSelectableTiles();

    protected abstract void useSkillOnTile(Tile selectedTile);

    protected abstract void triggerTargetSelection(World world);

    protected abstract void triggerSkillEffect(World world);

    public void setOwner(Player player) {
        mSkillOwner = player;
    }
}
