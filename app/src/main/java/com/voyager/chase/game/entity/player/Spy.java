package com.voyager.chase.game.entity.player;

import com.voyager.chase.R;

/**
 * Created by miguellysanchez on 7/12/16.
 */
public class Spy extends Player {

    public static final int STARTING_SKILL_POINTS = 20;
    public static final int DEFAULT_SPY_VISIBILITY = GLOBALLY_VISIBLE;
    public static final int DEFAULT_SENTRY_VISIBILITY = PROXIMITY_VISIBLE;
    private static final int DEFAULT_OBJECTIVES_COUNT = 3;
    private static Spy sSpy;
    private int objectivesRemaining = DEFAULT_OBJECTIVES_COUNT;

    private Spy() {
        mRole = Player.SPY_ROLE;
        mLife = 1;
        mMaxLife = 2;
        mActionPointsRecovery = 2;
        renderDrawableId = R.drawable.chase_ic_player_spy;
        sentryVisibility = DEFAULT_SENTRY_VISIBILITY;
        spyVisibility = DEFAULT_SPY_VISIBILITY;
    }

    public static Spy createInstance() {
        sSpy = new Spy();
        return sSpy;
    }

    public static Spy getInstance() {
        if (sSpy == null) {
            return createInstance();
        }
        return sSpy;
    }

    @Override
    public String getRole() {
        return Player.SPY_ROLE;
    }

    public int getObjectivesRemaining() {
        return objectivesRemaining;
    }

    public void reduceObjectivesRemaining() {
        objectivesRemaining = objectivesRemaining - 1;
    }
}
