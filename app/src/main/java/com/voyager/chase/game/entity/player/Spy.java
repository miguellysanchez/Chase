package com.voyager.chase.game.entity.player;

import com.voyager.chase.R;

/**
 * Created by miguellysanchez on 7/12/16.
 */
public class Spy extends Player {

    public static final int STARTING_SKILL_POINTS = 10;
    public static final int DEFAULT_SPY_VISIBILITY = GLOBALLY_VISIBLE;
    public static final int DEFAULT_SENTRY_VISIBILITY = PROXIMITY_VISIBLE;

    private static Spy sSpy;

    private Spy() {
        mIdentity = Player.SPY_ROLE;
        mLife = 1;
        mMaxLife = 2;
        mActionPointsRecovery = 2;
    }

    public static Spy createInstance() {
        sSpy = new Spy();
        sSpy.renderDrawableId = R.drawable.chase_drawable_ic_spy;
        sSpy.sentryVisibility = DEFAULT_SENTRY_VISIBILITY;
        sSpy.spyVisibility = DEFAULT_SPY_VISIBILITY;

        return sSpy;
    }

    public static Spy getInstance() {
        if (sSpy == null) {
            createInstance();
        }
        return sSpy;
    }

    @Override
    public String getIdentity() {
        return Player.SPY_ROLE;
    }
}
