package com.voyager.chase.game.handlers;

import com.voyager.chase.game.TurnState;
import com.voyager.chase.game.event.TurnStateEvent;
import com.voyager.chase.game.event.ViewChangeEvent;

import timber.log.Timber;

/**
 * Created by miguellysanchez on 7/26/16.
 */
public class SelectSkillStateHandler extends TurnStateHandler {

    public static final String ACTION_WAITING = "action_waiting";
    public static final String ACTION_SELECTED = "action_selected";

    @Override
    public void handleTurnStateEvent(TurnStateEvent event) {
        Timber.d("On handle SELECT SKILL turn state event");
        ViewChangeEvent viewChangeEvent = new ViewChangeEvent();
        switch (event.getAction()) {
            case ACTION_WAITING:
                viewChangeEvent.addViewChangeType(ViewChangeEvent.SHOW_SKILL_SELECTION);
                post(viewChangeEvent);
                break;
            case ACTION_SELECTED:
                viewChangeEvent.addViewChangeType(ViewChangeEvent.PROCESSING);
                post(viewChangeEvent);

                TurnStateEvent turnStateEvent = new TurnStateEvent();
                turnStateEvent.setTargetState(TurnState.TARGET_SELECTION_STATE);
                turnStateEvent.setAction(TargetSelectionStateHandler.ACTION_WAITING);
                turnStateEvent.setSelectedSkill(event.getSelectedSkill());

                post(turnStateEvent);
                break;
        }
    }
}
