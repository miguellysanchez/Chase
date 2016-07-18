package com.voyager.chase.game.skill;

import android.content.Context;
import android.support.annotation.CallSuper;

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
    protected int mCurrentCooldown = 0;
    protected String mSkillName;
    protected Player mSkillOwner;
    protected String mSkillDescription;

    public abstract ArrayList<Tile> getSelectableTiles(World world);

    public abstract void useSkillOnTile(Tile selectedTile);

    public abstract void triggerTargetSelection(World world);

    public abstract void triggerSkillEffect(World world);

    public String getSkillName(){
        return mSkillName;
    }

    public void setOwner(Player player) {
        mSkillOwner = player;
    }

    public Player getOwner() {
        return mSkillOwner;
    }

    public int getCooldown() {
        return mCooldown;
    }

    public int getCurrentCooldown() {
        return mCurrentCooldown;
    }

    public String getDescription() {
        return mSkillDescription;
    }

    public int reduceCooldown(){
        if(mCurrentCooldown > 0) {
            mCurrentCooldown -= 1;
        }
        return mCurrentCooldown;
    }

    public void addCooldown(){
        if(mCurrentCooldown == 0){
            mCurrentCooldown += mCooldown;
        }
    }
}
