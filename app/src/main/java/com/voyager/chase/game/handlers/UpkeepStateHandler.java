package com.voyager.chase.game.handlers;

import com.voyager.chase.game.TurnState;
import com.voyager.chase.game.World;
import com.voyager.chase.game.entity.player.Sentry;
import com.voyager.chase.game.entity.player.Spy;
import com.voyager.chase.game.event.TurnStateEvent;
import com.voyager.chase.game.event.ViewChangeEvent;

import timber.log.Timber;

/**
 * Created by miguellysanchez on 7/23/16.
 */
public class UpkeepStateHandler extends TurnStateHandler {
    @Override
    public void handleTurnStateEvent(TurnStateEvent event) {
        Timber.d("On handle turn state event");

        if (Spy.getInstance().getLife() <= 0 && Sentry.getInstance().getLife() <= 0) {
            //TODO draw win condition;
            return;
        } else if (Spy.getInstance().getLife() <= 0) {
            //TODO draw sentry win condition;
            return;
        } else if (Sentry.getInstance().getLife() <= 0 || Spy.getInstance().getObjectivesRemaining() <= 0) {
            //TODO draw spy win condition;
            return;
        }
        ViewChangeEvent viewChangeEvent = new ViewChangeEvent();
        viewChangeEvent.addViewChangeType(ViewChangeEvent.UPDATE_SKILLS_LIST);
        post(viewChangeEvent);

        TurnStateEvent turnStateEvent = new TurnStateEvent();
        if(World.getUserPlayer().getActionPoints() > 0){
            turnStateEvent.setTargetState(TurnState.SELECT_SKILL_STATE);
            turnStateEvent.setAction(SelectSkillStateHandler.ACTION_WAITING);
        } else {
            turnStateEvent.setTargetState(TurnState.END_STATE);
        }
        post(turnStateEvent);
    }
}
