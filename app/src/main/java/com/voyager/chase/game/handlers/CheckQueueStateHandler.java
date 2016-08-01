package com.voyager.chase.game.handlers;

import com.voyager.chase.game.TurnState;
import com.voyager.chase.game.World;
import com.voyager.chase.game.event.TurnStateEvent;
import com.voyager.chase.game.mods.WorldEffect;

/**
 * Created by miguellysanchez on 7/27/16.
 */
public class CheckQueueStateHandler extends TurnStateHandler {
    @Override
    public void handleTurnStateEvent(TurnStateEvent event) {
        TurnStateEvent turnStateEvent;
        turnStateEvent= new TurnStateEvent();

        if(World.getInstance().isWorldEffectQueueEmpty()){
            turnStateEvent.setTargetState(TurnState.UPKEEP_STATE);
        } else {
            turnStateEvent.setTargetState(TurnState.SYNC_WORLD_STATE);
            WorldEffect worldEffect = World.getInstance().getNextWorldEffectFromQueue();
            turnStateEvent.setWorldEffect(worldEffect);
        }
        post(turnStateEvent);
    }
}
