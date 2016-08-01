package com.voyager.chase.game.handlers;

import com.voyager.chase.game.TurnState;
import com.voyager.chase.game.World;
import com.voyager.chase.game.entity.player.Player;
import com.voyager.chase.game.event.TurnStateEvent;
import com.voyager.chase.game.event.ViewChangeEvent;
import com.voyager.chase.game.skill.Skill;

import timber.log.Timber;

/**
 * Created by miguellysanchez on 7/18/16.
 */
public class StartStateHandler extends TurnStateHandler {

    @Override
    public void handleTurnStateEvent(TurnStateEvent event) {
        Timber.d("On handle turn state event");

        Player player = World.getUserPlayer();
        if (!player.isTurnSkipped()) {
            for (Skill skill : player.getSkillsList()) {
                skill.reduceCooldown();
            }
            player.setActionPoints(player.getActionPointsRecovery());
        } else {
            player.setIsTurnSkipped(true);
        }
        ViewChangeEvent viewChangeEvent = new ViewChangeEvent();
        viewChangeEvent.addViewChangeType(ViewChangeEvent.UPDATE_PLAYER_STATE);
        post(viewChangeEvent);

        TurnStateEvent turnStateEvent = new TurnStateEvent();;
        turnStateEvent.setTargetState(TurnState.UPKEEP_STATE);
        post(turnStateEvent);
    }
}