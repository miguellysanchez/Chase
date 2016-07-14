package com.voyager.chase.game.entity.player;

/**
 * Created by miguellysanchez on 7/12/16.
 */
public class Spy extends Player {

    @Override
    public String getIdentity() {
        return Player.SPY_ROLE;
    }
}
