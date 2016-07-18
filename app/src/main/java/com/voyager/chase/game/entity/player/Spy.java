package com.voyager.chase.game.entity.player;

/**
 * Created by miguellysanchez on 7/12/16.
 */
public class Spy extends Player {

    public static final int STARTING_SKILL_POINTS = 10;

    public Spy() {
        mIdentity = Player.SPY_ROLE;
        mLife = 1;
        mMaxLife = 2;
        mActionPointsRecovery = 2;
    }

    @Override
    public String getIdentity() {
        return Player.SPY_ROLE;
    }
}
