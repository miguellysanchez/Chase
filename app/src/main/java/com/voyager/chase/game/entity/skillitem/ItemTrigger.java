package com.voyager.chase.game.entity.skillitem;

import com.voyager.chase.game.entity.player.Player;

/**
 * Created by miguellysanchez on 7/24/16.
 */
public class ItemTrigger {

    private SkillItem creator;

    public void onTriggered(Player player){
        creator.onTriggered(player);
    }

}
