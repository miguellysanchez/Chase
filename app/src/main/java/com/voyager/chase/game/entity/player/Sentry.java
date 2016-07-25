package com.voyager.chase.game.entity.player;

import com.voyager.chase.R;

/**
 * Created by miguellysanchez on 7/12/16.
 */
public class Sentry extends Player {

    public static final int STARTING_SKILL_POINTS = 10;
    private static final int DEFAULT_SENTRY_VISIBILITY = GLOBALLY_VISIBLE;
    private static final int DEFAULT_SPY_VISIBILITY = PROXIMITY_VISIBLE;
    private static Sentry sSentry;

    private Sentry() {
        mIdentity = Player.SENTRY_ROLE;
        mMaxLife = 4;
        mLife = 3;
        mActionPointsRecovery = 3;
    }

    public static Sentry createInstance() {
        sSentry = new Sentry();
        sSentry.renderDrawableId = R.drawable.chase_drawable_ic_sentry;
        sSentry.sentryVisibility = DEFAULT_SENTRY_VISIBILITY;
        sSentry.spyVisibility = DEFAULT_SPY_VISIBILITY;
        return sSentry;
    }

    public static Sentry getInstance() {
        if (sSentry == null) {
            createInstance();
        }
        return sSentry;
    }

    @Override
    public String getIdentity() {
        return Player.SENTRY_ROLE;
    }
}
