package com.voyager.chase.game.handlers.inactive;

import com.voyager.chase.game.EffectInterpreter;
import com.voyager.chase.game.TurnState;
import com.voyager.chase.game.event.TurnStateEvent;
import com.voyager.chase.game.handlers.TurnStateHandler;
import com.voyager.chase.game.mods.WorldEffect;

/**
 * Created by miguellysanchez on 8/1/16.
 */
public class InactiveUpdateWorldStateHandler extends TurnStateHandler {
    @Override
    public void handleTurnStateEvent(TurnStateEvent event) {
        WorldEffect worldEffect = event.getWorldEffect();
        EffectInterpreter.applyEffect(worldEffect);

        TurnStateEvent turnStateEvent = new TurnStateEvent();
        turnStateEvent.setTargetState(TurnState.INACTIVE_RENDER_STATE);
        post(turnStateEvent);
    }
}
