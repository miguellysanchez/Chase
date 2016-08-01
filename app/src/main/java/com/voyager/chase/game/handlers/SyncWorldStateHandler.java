package com.voyager.chase.game.handlers;

import com.google.gson.Gson;
import com.voyager.chase.game.event.TurnStateEvent;
import com.voyager.chase.game.mods.WorldEffect;
import com.voyager.chase.mqtt.Topics;
import com.voyager.chase.utility.MqttIssueActionUtility;

/**
 * Created by miguellysanchez on 7/27/16.
 */
public class SyncWorldStateHandler extends TurnStateHandler {
    private String mGameSessionId;
    private Gson gson;

    public SyncWorldStateHandler(String gameSessionId) {
        mGameSessionId = gameSessionId;
        gson = new Gson();
    }

    @Override
    public void handleTurnStateEvent(TurnStateEvent event) {
        WorldEffect worldEffect = event.getWorldEffect();
        String worldEffectPayloadString = gson.toJson(worldEffect);
        MqttIssueActionUtility.publish(Topics.getSessionWorldUpdateTopic(mGameSessionId), worldEffectPayloadString, false);
    }
}
