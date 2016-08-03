package com.voyager.chase.game.entity.construct;

import com.voyager.chase.game.entity.player.Player;

/**
 * Created by miguellysanchez on 7/24/16.
 */
public class Trigger {

    private Construct creator;

    public Trigger(Construct construct) {
        creator = construct;
    }

    public boolean invokeTrigger(Player player){
        return creator.invokeTrigger(player);
    }

}
