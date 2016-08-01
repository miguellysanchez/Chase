package com.voyager.chase.game.skill;

import com.voyager.chase.game.World;
import com.voyager.chase.game.entity.Tile;
import com.voyager.chase.game.entity.player.Player;

import java.util.ArrayList;

/**
 * Created by miguellysanchez on 7/5/16.
 */
public abstract class Skill {

    protected int mSkillCooldown = 0;
    protected int mCurrentCooldown = 0;
    protected String mSkillName;
    protected Player mSkillOwner;
    protected String mSkillDescription;

    public abstract ArrayList<Tile> getSelectableTiles();

    public abstract void useSkillOnTile(Tile selectedTile);

    public String getSkillName() {
        return mSkillName;
    }

    public void setSkillName(String skillName) {
        this.mSkillName = skillName;
    }

    public void setOwner(Player player) {
        mSkillOwner = player;
    }

    public Player getOwner() {
        return mSkillOwner;
    }

    public int getSkillCooldown() {
        return mSkillCooldown;
    }

    public void setSkillCooldown(int skillCooldown) {
        mSkillCooldown = skillCooldown;
    }

    public int getCurrentCooldown() {
        return mCurrentCooldown;
    }

    public String getDescription() {
        return mSkillDescription;
    }

    public void setDescription(String description) {
        this.mSkillDescription = description;
    }

    public int reduceCooldown() {
        if (mCurrentCooldown > 0) {
            mCurrentCooldown -= 1;
        }
        return mCurrentCooldown;
    }

    public void initiateCooldown() {
        if (mCurrentCooldown == 0) {
            mCurrentCooldown += mSkillCooldown;
        }
    }

}
