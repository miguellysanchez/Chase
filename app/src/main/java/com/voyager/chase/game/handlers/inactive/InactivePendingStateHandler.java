package com.voyager.chase.game.handlers.inactive;

import com.voyager.chase.game.TurnState;
import com.voyager.chase.game.World;
import com.voyager.chase.game.event.TurnStateEvent;
import com.voyager.chase.game.event.ViewChangeEvent;
import com.voyager.chase.game.handlers.TurnStateHandler;
import com.voyager.chase.game.mods.WorldEffect;

/**
 * Created by miguellysanchez on 8/1/16.
 */
public class InactivePendingStateHandler extends TurnStateHandler {
    public static final String ACTION_WAITING = "action_waiting";
    public static final String ACTION_UPDATE = "action_update";
    public static final String ACTION_OTHER_PLAYER_FINISHED = "action_other_player_finished";
    public static final String ACTION_PROCESSING_DONE = "action_processing_done";
    private boolean isOtherPlayerFinished = false;
    private boolean isProcessing = false;

    @Override
    public synchronized void handleTurnStateEvent(TurnStateEvent event) {
        switch (event.getAction()) {
            case ACTION_WAITING:
                ViewChangeEvent viewChangeEvent = new ViewChangeEvent();
                viewChangeEvent.addViewChangeType(ViewChangeEvent.WAITING_FOR_OTHER_PLAYER);
                post(viewChangeEvent);
                break;
            case ACTION_UPDATE:
                checkToAdvanceTurn();
                break;
            case ACTION_OTHER_PLAYER_FINISHED:
                isOtherPlayerFinished = true;
                checkToAdvanceTurn();
                break;
            case ACTION_PROCESSING_DONE:
                isProcessing = false;
                checkToAdvanceTurn();
                break;
        }
    }

    private void checkToAdvanceTurn() {
        if (!isProcessing) {
            if (!World.getInstance().isWorldEffectQueueEmpty()) {
                WorldEffect worldEffect = World.getInstance().dequeueFromWorldEffectQueue();
                TurnStateEvent turnStateEvent = new TurnStateEvent();
                turnStateEvent.setTargetState(TurnState.INACTIVE_UPDATE_WORLD_STATE);
                turnStateEvent.setWorldEffect(worldEffect);
                isProcessing = true;
                post(turnStateEvent);
            } else if (isOtherPlayerFinished) {
                isOtherPlayerFinished = false;
                TurnStateEvent startStateEvent = new TurnStateEvent();
                startStateEvent.setTargetState(TurnState.START_STATE);

            }
        }
    }
}
