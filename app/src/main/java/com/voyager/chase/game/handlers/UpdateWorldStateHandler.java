package com.voyager.chase.game.handlers;

import com.voyager.chase.game.EffectInterpreter;
import com.voyager.chase.game.TurnState;
import com.voyager.chase.game.event.TurnStateEvent;

import timber.log.Timber;

/**
 * Created by miguellysanchez on 8/1/16.
 */
public class UpdateWorldStateHandler extends TurnStateHandler {
    @Override
    public void handleTurnStateEvent(TurnStateEvent event) {
        Timber.d("On handle UPDATE WORLD turn state event");
        EffectInterpreter.applyEffect(event.getWorldEffect());

        TurnStateEvent turnStateEvent = new TurnStateEvent();
        turnStateEvent.setTargetState(TurnState.RENDER_WORLD_STATE);
        post(turnStateEvent);
    }
}
