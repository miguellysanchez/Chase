package com.voyager.chase.game.entity.player;

import com.voyager.chase.game.skill.Skill;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by miguellysanchez on 7/5/16.
 */
public abstract class Player {

    public static final String SENTRY_ROLE = "sentry_role";
    public static final String SPY_ROLE = "spy_role";

    private ArrayList<Skill> mSkillsList;
    protected int mLife;
    protected int mActionPoints;
    protected String mIdentity;

    protected void setLife(int life) {
        mLife = life;
    }

    protected void setSkills(ArrayList<Skill> skillList) {
        mSkillsList = skillList;
        for (Skill skill : skillList) {
            skill.setOwner(this);
        }
    }

    public abstract String getIdentity();

}
