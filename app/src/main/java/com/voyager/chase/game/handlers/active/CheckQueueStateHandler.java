package com.voyager.chase.game.handlers.active;

import com.voyager.chase.game.TurnState;
import com.voyager.chase.game.World;
import com.voyager.chase.game.event.TurnStateEvent;
import com.voyager.chase.game.handlers.TurnStateHandler;
import com.voyager.chase.game.mods.WorldEffect;

import timber.log.Timber;

/**
 * Created by miguellysanchez on 7/27/16.
 */
public class CheckQueueStateHandler extends TurnStateHandler {
    @Override
    public void handleTurnStateEvent(TurnStateEvent event) {
        Timber.d("On handle CHECK QUEUE turn state event");

        TurnStateEvent turnStateEvent;
        turnStateEvent= new TurnStateEvent();

        if(World.getInstance().isWorldEffectQueueEmpty()){
            turnStateEvent.setTargetState(TurnState.CHECK_TRIGGER_STATE);
        } else {
            turnStateEvent.setTargetState(TurnState.SYNC_WORLD_STATE);
            WorldEffect worldEffect = World.getInstance().dequeueFromWorldEffectQueue();
            turnStateEvent.setWorldEffect(worldEffect);
            Timber.d(">>>>>DEQUEUEING WORLD EFFECT: %s", worldEffect.toString());
        }
        post(turnStateEvent);
    }
}
