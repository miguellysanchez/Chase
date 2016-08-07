package com.voyager.chase.game.handlers.active;

import android.content.Context;

import com.voyager.chase.game.TurnState;
import com.voyager.chase.game.World;
import com.voyager.chase.game.entity.player.Sentry;
import com.voyager.chase.game.entity.player.Spy;
import com.voyager.chase.game.event.TurnStateEvent;
import com.voyager.chase.game.event.ViewChangeEvent;
import com.voyager.chase.game.handlers.TurnStateHandler;
import com.voyager.chase.mqtt.Topics;
import com.voyager.chase.mqtt.payload.GameStatusPayload;
import com.voyager.chase.results.ResultsActivity;
import com.voyager.chase.utility.MqttIssueActionUtility;
import com.voyager.chase.utility.PreferenceUtility;

import timber.log.Timber;

/**
 * Created by miguellysanchez on 7/23/16.
 */
public class UpkeepStateHandler extends TurnStateHandler {

    private String mGameSessionStatusTopic;

    public UpkeepStateHandler(Context context) {
        mGameSessionStatusTopic = Topics.getSessionStatusTopic(PreferenceUtility.getInstance(context).getGameSessionId());
    }

    @Override
    public void handleTurnStateEvent(TurnStateEvent event) {
        Timber.d("On handle UPKEEP turn state event");

        if (Spy.getInstance().getLife() <= 0 && Sentry.getInstance().getLife() <= 0) {
            showGameResults(ResultsActivity.RESULT_DRAW);
            return;
        } else if (Spy.getInstance().getLife() <= 0) {
            showGameResults(ResultsActivity.RESULT_SENTRY_WINNER);
            return;
        } else if (Sentry.getInstance().getLife() <= 0) {
            showGameResults(ResultsActivity.RESULT_SPY_WINNER_TRAPPER);
            return;
        } else if (Spy.getInstance().getObjectivesRemaining() <= 0) {
            showGameResults(ResultsActivity.RESULT_SPY_WINNER_SABOTEUR);
            return;
        }
        ViewChangeEvent viewChangeEvent = new ViewChangeEvent();
        viewChangeEvent.addViewChangeType(ViewChangeEvent.UPDATE_SKILLS_LIST);
        post(viewChangeEvent);

        TurnStateEvent turnStateEvent = new TurnStateEvent();
        if (World.getUserPlayer().getActionPoints() > 0) {
            turnStateEvent.setTargetState(TurnState.SELECT_SKILL_STATE);
            turnStateEvent.setAction(SelectSkillStateHandler.ACTION_WAITING);
        } else {
            turnStateEvent.setTargetState(TurnState.END_STATE);
            turnStateEvent.setAction(EndStateHandler.ACTION_WAITING);
        }
        post(turnStateEvent);
    }

    private void showGameResults(String results) {
        GameStatusPayload gameStatusPayload = new GameStatusPayload();
        gameStatusPayload.setSenderRole(World.getUserPlayer().getRole());
        gameStatusPayload.setAction(GameStatusPayload.SHOW_RESULTS);
        gameStatusPayload.setResults(results);
        MqttIssueActionUtility.publish(mGameSessionStatusTopic, gameStatusPayload.toJson(), true);
    }
}
