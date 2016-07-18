package com.voyager.chase.game.entity.player;

/**
 * Created by miguellysanchez on 7/12/16.
 */
public class Sentry extends Player {

    public static final int STARTING_SKILL_POINTS = 10 ;

    public Sentry(){
        mIdentity = Player.SENTRY_ROLE;
        mMaxLife = 4;
        mLife = 3;
        mActionPointsRecovery = 3;
    }

    @Override
    public String getIdentity() {
        return Player.SENTRY_ROLE;
    }
}
