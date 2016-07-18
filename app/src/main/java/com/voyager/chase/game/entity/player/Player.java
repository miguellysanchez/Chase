package com.voyager.chase.game.entity.player;

import com.voyager.chase.game.entity.Renderable;
import com.voyager.chase.game.skill.Skill;

import java.util.ArrayList;

/**
 * Created by miguellysanchez on 7/5/16.
 */
public abstract class Player extends Renderable{

    public static final String SENTRY_ROLE = "sentry_role";
    public static final String SPY_ROLE = "spy_role";

    private ArrayList<Skill> mSkillsList;
    protected int mLife = 1;
    protected int mMaxLife = 2;
    protected int mActionPoints = 0;
    protected int mActionPointsRecovery = 0;
    protected String mIdentity;
    protected boolean mIsTurnSkipped = false;
    private String mCurrentRoomName;
    private int mCurrentTileXCoordinate;
    private int mCurrentTileYCoordinate;
    private ArrayList<Skill> skills;

    public int getLife() {
        return mLife;
    }

    public void setLife(int newLife) {
        if (newLife > 0) {
            mLife = newLife;
        } else if (newLife > mMaxLife) {
            mLife = mMaxLife;
        } else {
            mLife = 0;
        }
    }

    protected void setSkills(ArrayList<Skill> skillList) {
        mSkillsList = skillList;
        for (Skill skill : skillList) {
            skill.setOwner(this);
        }
    }

    public void recoverActionPoints() {
        mActionPoints = mActionPointsRecovery;
    }

    public int getActionPoints(){
        return mActionPoints;
    }

    public abstract String getIdentity();

    public boolean isTurnSkipped() {
        return mIsTurnSkipped;
    }

    public void setIsTurnSkipped(boolean isTurnSkipped) {
        mIsTurnSkipped = isTurnSkipped;
    }

    public void setActionPoints(int actionPoints) {
        mActionPoints = actionPoints;
    }

    public ArrayList<Skill> getSkills() {
        return skills;
    }


    public String getCurrentRoomName() {
        return mCurrentRoomName;
    }

    public void setCurrentRoomName(String mCurrentRoomName) {
        this.mCurrentRoomName = mCurrentRoomName;
    }

    public int getCurrentTileXCoordinate() {
        return mCurrentTileXCoordinate;
    }

    public void setCurrentTileXCoordinate(int mCurrentTileXCoordinate) {
        this.mCurrentTileXCoordinate = mCurrentTileXCoordinate;
    }

    public int getCurrentTileYCoordinate() {
        return mCurrentTileYCoordinate;
    }

    public void setCurrentTileYCoordinate(int mCurrentTileYCoordinate) {
        this.mCurrentTileYCoordinate = mCurrentTileYCoordinate;
    }
}
