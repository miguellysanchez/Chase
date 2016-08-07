package com.voyager.chase.game.handlers.inactive;

import com.voyager.chase.game.TurnState;
import com.voyager.chase.game.event.TurnStateEvent;
import com.voyager.chase.game.event.ViewChangeEvent;
import com.voyager.chase.game.handlers.TurnStateHandler;

import timber.log.Timber;

/**
 * Created by miguellysanchez on 8/1/16.
 */
public class InactiveRenderStateHandler extends TurnStateHandler {

    public static final String ACTION_WAITING = "action_waiting";
    public static final String ACTION_RENDER_DONE = "action_render_done";

    @Override
    public void handleTurnStateEvent(TurnStateEvent event) {
        Timber.d("On handle INACTIVE_RENDER turn state event");

        switch (event.getAction()) {
            case ACTION_WAITING:
                ViewChangeEvent viewChangeEvent = new ViewChangeEvent();
                viewChangeEvent.addViewChangeType(ViewChangeEvent.RENDER_WORLD);
                viewChangeEvent.addViewChangeType(ViewChangeEvent.UPDATE_PLAYER_STATE);
                post(viewChangeEvent);
                break;
            case ACTION_RENDER_DONE:
                TurnStateEvent turnStateEvent = new TurnStateEvent();
                turnStateEvent.setTargetState(TurnState.INACTIVE_PENDING_STATE);
                turnStateEvent.setAction(InactivePendingStateHandler.ACTION_PROCESSING_DONE);
                post(turnStateEvent);
                break;
        }
    }
}