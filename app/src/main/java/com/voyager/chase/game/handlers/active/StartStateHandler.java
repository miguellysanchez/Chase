package com.voyager.chase.game.handlers.active;

import com.voyager.chase.game.TurnState;
import com.voyager.chase.game.World;
import com.voyager.chase.game.entity.player.Player;
import com.voyager.chase.game.event.TurnStateEvent;
import com.voyager.chase.game.event.ViewChangeEvent;
import com.voyager.chase.game.handlers.TurnStateHandler;
import com.voyager.chase.game.skill.Skill;

import timber.log.Timber;

/**
 * Created by miguellysanchez on 7/18/16.
 */
public class StartStateHandler extends TurnStateHandler {

    @Override
    public void handleTurnStateEvent(TurnStateEvent event) {
        Timber.d("On handle START turn state event");

        Player userPlayer = World.getUserPlayer();
        if (userPlayer.isTurnSkipped()) {
            userPlayer.setActionPoints(0);
            userPlayer.setIsTurnSkipped(false);
        } else {
            for (Skill skill : userPlayer.getSkillsList()) {
                skill.reduceCooldown();
            }
            userPlayer.recoverActionPoints();
        }
        ViewChangeEvent viewChangeEvent = new ViewChangeEvent();
        viewChangeEvent.addViewChangeType(ViewChangeEvent.UPDATE_PLAYER_STATE);
        post(viewChangeEvent);

        TurnStateEvent turnStateEvent = new TurnStateEvent();
        turnStateEvent.setTargetState(TurnState.UPKEEP_STATE);
        post(turnStateEvent);
    }
}
