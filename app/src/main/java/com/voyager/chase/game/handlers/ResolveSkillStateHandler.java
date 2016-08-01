package com.voyager.chase.game.handlers;

import com.voyager.chase.game.TurnState;
import com.voyager.chase.game.World;
import com.voyager.chase.game.entity.player.Player;
import com.voyager.chase.game.event.TurnStateEvent;
import com.voyager.chase.game.event.ViewChangeEvent;
import com.voyager.chase.game.skill.Skill;

import timber.log.Timber;

/**
 * Created by miguellysanchez on 7/27/16.
 */
public class ResolveSkillStateHandler extends TurnStateHandler {
    @Override
    public void handleTurnStateEvent(TurnStateEvent event) {
        Timber.d("On handle turn state event");
        Skill activatedSkill = event.getSelectedSkill();
        activatedSkill.initiateCooldown();
        activatedSkill.useSkillOnTile(event.getTargetTile());

        Player userPlayer = World.getUserPlayer();
        userPlayer.setActionPoints(userPlayer.getActionPoints() - 1);

        ViewChangeEvent viewChangeEvent = new ViewChangeEvent();
        viewChangeEvent.addViewChangeType(ViewChangeEvent.UPDATE_PLAYER_STATE);
        post(viewChangeEvent);

        TurnStateEvent turnStateEvent = new TurnStateEvent();
        turnStateEvent.setTargetState(TurnState.CHECK_QUEUE_STATE);
        post(turnStateEvent);
    }
}
