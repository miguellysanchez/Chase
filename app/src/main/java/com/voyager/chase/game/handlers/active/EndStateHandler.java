package com.voyager.chase.game.handlers.active;

import com.voyager.chase.game.TurnState;
import com.voyager.chase.game.World;
import com.voyager.chase.game.event.TurnStateEvent;
import com.voyager.chase.game.event.ViewChangeEvent;
import com.voyager.chase.game.handlers.TurnStateHandler;
import com.voyager.chase.game.handlers.inactive.InactivePendingStateHandler;
import com.voyager.chase.mqtt.Topics;
import com.voyager.chase.mqtt.payload.GameStatusPayload;
import com.voyager.chase.utility.MqttIssueActionUtility;

/**
 * Created by miguellysanchez on 8/1/16.
 */
public class EndStateHandler extends TurnStateHandler {
    public static final String ACTION_WAITING = "action_waiting";
    public static final String ACTION_CONFIRMED = "action_confirmed";

    private String mGameSessionId;

    public EndStateHandler(String gameSessionId) {
        mGameSessionId = gameSessionId;
    }

    @Override
    public void handleTurnStateEvent(TurnStateEvent event) {
        switch (event.getAction()) {
            case ACTION_WAITING:
                ViewChangeEvent viewChangeEvent = new ViewChangeEvent();
                viewChangeEvent.addViewChangeType(ViewChangeEvent.END_TURN_CONFIRMATION);
                post(viewChangeEvent);
                break;
            case ACTION_CONFIRMED:
                TurnStateEvent pendingStateEvent = new TurnStateEvent();
                pendingStateEvent.setTargetState(TurnState.INACTIVE_PENDING_STATE);
                pendingStateEvent.setAction(InactivePendingStateHandler.ACTION_WAITING);
                post(pendingStateEvent);

                GameStatusPayload payload = new GameStatusPayload();
                payload.setSenderRole(World.getUserPlayer().getRole());
                payload.setAction(GameStatusPayload.TURN_FINISHED);
                MqttIssueActionUtility.publish(Topics.getSessionStatusTopic(mGameSessionId), payload.toJson(),false);
                break;
        }
    }
}
