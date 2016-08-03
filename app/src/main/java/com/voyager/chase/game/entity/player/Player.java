package com.voyager.chase.game.entity.player;

import com.voyager.chase.game.entity.Renderable;
import com.voyager.chase.game.skill.Skill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by miguellysanchez on 7/5/16.
 */
public abstract class Player extends Renderable {

    public static final String SENTRY_ROLE = "SENTRY";
    public static final String SPY_ROLE = "SPY";

    private HashMap<String, Skill> mSkillsMap;
    protected int mLife = 1;
    protected int mMaxLife = 2;
    protected int mActionPoints = 0;
    protected int mActionPointsRecovery = 0;
    protected String mRole;
    protected boolean mIsTurnSkipped = false;

    public int getLife() {
        return mLife;
    }

    protected void setLife(int newLife) {
        if (newLife > 0) {
            mLife = newLife;
        } else if (newLife > mMaxLife) {
            mLife = mMaxLife;
        } else {
            mLife = 0;
        }
    }

    public void reduceLife() {
        setLife(mLife - 1);
    }

    public void recoverLife() {
        setLife(mLife + 1);
    }

    public ArrayList<Skill> getSkillsList() {
        return new ArrayList<>(mSkillsMap.values());
    }

    public void setSkills(HashMap<String, Skill> skillsMap) {
        mSkillsMap = skillsMap;
        for (Map.Entry<String, Skill> entry : skillsMap.entrySet()) {
            Skill skill = entry.getValue();
            skill.setOwner(this);
        }
    }

    public void recoverActionPoints() {
        mActionPoints = mActionPointsRecovery;
    }

    public int getActionPoints() {
        return mActionPoints;
    }

    public abstract String getRole();

    public boolean isTurnSkipped() {
        return mIsTurnSkipped;
    }

    public void setIsTurnSkipped(boolean isTurnSkipped) {
        mIsTurnSkipped = isTurnSkipped;
    }

    public void setActionPoints(int newActionPoints) {
        if (newActionPoints > 0) {
            mActionPoints = newActionPoints;
        } else if (newActionPoints > mActionPointsRecovery) {
            mActionPoints = mActionPointsRecovery;
        } else {
            mActionPoints = 0;
        }
    }

    public int getMaxLife() {
        return mMaxLife;
    }

    public int getActionPointsRecovery() {
        return mActionPointsRecovery;
    }
}
