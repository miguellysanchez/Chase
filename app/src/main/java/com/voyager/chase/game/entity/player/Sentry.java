package com.voyager.chase.game.entity.player;

import com.voyager.chase.R;

/**
 * Created by miguellysanchez on 7/12/16.
 */
public class Sentry extends Player {

    public static final int STARTING_SKILL_POINTS = 100;
    private static final int DEFAULT_SENTRY_VISIBILITY = GLOBALLY_VISIBLE;
    private static final int DEFAULT_SPY_VISIBILITY = PROXIMITY_VISIBLE;
    private static Sentry sSentry;

    private Sentry() {
        mRole = Player.SENTRY_ROLE;
        mMaxLife = 4;
        mLife = 3;
        mActionPointsRecovery = 30;
        renderDrawableId = R.drawable.chase_ic_player_sentry;
        sentryVisibility = DEFAULT_SENTRY_VISIBILITY;
        spyVisibility = DEFAULT_SPY_VISIBILITY;
    }

    public static Sentry createInstance() {
        sSentry = new Sentry();
        return sSentry;
    }

    public static Sentry getInstance() {
        if (sSentry == null) {
            return createInstance();
        }
        return sSentry;
    }

    @Override
    public String getRole() {
        return Player.SENTRY_ROLE;
    }
}
