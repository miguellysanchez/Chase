package com.voyager.chase.game.handlers.active;

import com.voyager.chase.game.TurnState;
import com.voyager.chase.game.World;
import com.voyager.chase.game.event.TurnStateEvent;
import com.voyager.chase.game.handlers.TurnStateHandler;
import com.voyager.chase.mqtt.Topics;
import com.voyager.chase.mqtt.payload.WorldEffectPayload;
import com.voyager.chase.utility.MqttIssueActionUtility;

import timber.log.Timber;

/**
 * Created by miguellysanchez on 7/27/16.
 */
public class SyncWorldStateHandler extends TurnStateHandler {
    private String mGameSessionId;

    public SyncWorldStateHandler(String gameSessionId) {
        mGameSessionId = gameSessionId;
    }

    @Override
    public void handleTurnStateEvent(TurnStateEvent event) {
        Timber.d("On handle SYNC WORLD turn state event");


        WorldEffectPayload worldEffectPayload = new WorldEffectPayload();
        worldEffectPayload.setSenderRole(World.getUserPlayer().getRole());
        worldEffectPayload.setWorldEffect(event.getWorldEffect());
        String worldEffectPayloadString = worldEffectPayload.toJson();
        MqttIssueActionUtility.publish(Topics.getSessionWorldUpdateTopic(mGameSessionId), worldEffectPayloadString, false);

        TurnStateEvent turnStateEvent = new TurnStateEvent();
        turnStateEvent.setTargetState(TurnState.UPDATE_WORLD_STATE);
        turnStateEvent.setWorldEffect(event.getWorldEffect());
        post(turnStateEvent);
    }
}
