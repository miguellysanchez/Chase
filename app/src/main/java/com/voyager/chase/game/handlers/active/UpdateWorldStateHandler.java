package com.voyager.chase.game.handlers.active;

import android.content.Context;

import com.voyager.chase.game.worldeffect.EffectInterpreter;
import com.voyager.chase.game.TurnState;
import com.voyager.chase.game.event.TurnStateEvent;
import com.voyager.chase.game.handlers.TurnStateHandler;

import timber.log.Timber;

/**
 * Created by miguellysanchez on 8/1/16.
 */
public class UpdateWorldStateHandler extends TurnStateHandler {
    private Context mContext;

    public UpdateWorldStateHandler(Context context) {
        mContext = context;
    }

    @Override
    public void handleTurnStateEvent(TurnStateEvent event) {
        Timber.d("On handle UPDATE WORLD turn state event");
        EffectInterpreter.applyEffect(mContext , event.getWorldEffect());

        TurnStateEvent turnStateEvent = new TurnStateEvent();
        turnStateEvent.setTargetState(TurnState.RENDER_WORLD_STATE);
        post(turnStateEvent);
    }
}
