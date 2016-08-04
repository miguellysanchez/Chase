package com.voyager.chase.game.handlers.inactive;

import android.content.Context;

import com.voyager.chase.game.worldeffect.EffectInterpreter;
import com.voyager.chase.game.TurnState;
import com.voyager.chase.game.event.TurnStateEvent;
import com.voyager.chase.game.handlers.TurnStateHandler;
import com.voyager.chase.game.worldeffect.WorldEffect;

/**
 * Created by miguellysanchez on 8/1/16.
 */
public class InactiveUpdateWorldStateHandler extends TurnStateHandler {
    private Context mContext;

    public InactiveUpdateWorldStateHandler(Context context){
        mContext = context;
    }

    @Override
    public void handleTurnStateEvent(TurnStateEvent event) {
        WorldEffect worldEffect = event.getWorldEffect();
        EffectInterpreter.applyEffect(mContext, worldEffect);

        TurnStateEvent turnStateEvent = new TurnStateEvent();
        turnStateEvent.setTargetState(TurnState.INACTIVE_RENDER_STATE);
        post(turnStateEvent);
    }
}
