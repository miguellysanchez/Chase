package com.voyager.chase.game.handlers;

import com.voyager.chase.game.TurnState;
import com.voyager.chase.game.event.TurnStateEvent;
import com.voyager.chase.game.event.ViewChangeEvent;

import timber.log.Timber;

/**
 * Created by miguellysanchez on 8/1/16.
 */
public class RenderStateHandler extends TurnStateHandler {
    @Override
    public void handleTurnStateEvent(TurnStateEvent event) {
        Timber.d("On handle RENDER turn state event");

        ViewChangeEvent viewChangeEvent = new ViewChangeEvent();
        viewChangeEvent.addViewChangeType(ViewChangeEvent.UPDATE_PLAYER_STATE);
        viewChangeEvent.addViewChangeType(ViewChangeEvent.RENDER_WORLD);
        post(viewChangeEvent);

        //TODO Add a delay before checking queue for next world effect
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TurnStateEvent turnStateEvent = new TurnStateEvent();
        turnStateEvent.setTargetState(TurnState.CHECK_QUEUE_STATE);
        post(turnStateEvent);
    }
}
