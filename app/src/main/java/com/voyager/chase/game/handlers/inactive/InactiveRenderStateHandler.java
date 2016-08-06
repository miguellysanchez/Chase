package com.voyager.chase.game.handlers.inactive;

import com.voyager.chase.game.TurnState;
import com.voyager.chase.game.event.TurnStateEvent;
import com.voyager.chase.game.event.ViewChangeEvent;
import com.voyager.chase.game.handlers.TurnStateHandler;

/**
 * Created by miguellysanchez on 8/1/16.
 */
public class InactiveRenderStateHandler extends TurnStateHandler {
    @Override
    public void handleTurnStateEvent(TurnStateEvent event) {
        ViewChangeEvent viewChangeEvent = new ViewChangeEvent();
        viewChangeEvent.addViewChangeType(ViewChangeEvent.RENDER_WORLD);
        viewChangeEvent.addViewChangeType(ViewChangeEvent.UPDATE_PLAYER_STATE);
        post(viewChangeEvent);

        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TurnStateEvent turnStateEvent = new TurnStateEvent();
        turnStateEvent.setTargetState(TurnState.INACTIVE_PENDING_STATE);
        turnStateEvent.setAction(InactivePendingStateHandler.ACTION_PROCESSING_DONE);
        post(turnStateEvent);
    }
}