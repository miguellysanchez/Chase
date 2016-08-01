package com.voyager.chase.game.handlers;

import com.voyager.chase.game.TurnState;
import com.voyager.chase.game.entity.Tile;
import com.voyager.chase.game.event.TurnStateEvent;
import com.voyager.chase.game.event.ViewChangeEvent;
import com.voyager.chase.game.skill.Skill;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by miguellysanchez on 7/26/16.
 */
public class TargetSelectionStateHandler extends TurnStateHandler {
    public static final String ACTION_WAITING = "action_waiting";
    public static final String ACTION_TARGETED = "action_targeted";
    public static final String ACTION_CANCEL = "action_cancel";

    private Skill selectedSkill;

    @Override
    public void handleTurnStateEvent(TurnStateEvent event) {
        Timber.d("On handle turn state event");

        switch (event.getAction()) {
            case ACTION_WAITING:
                ViewChangeEvent viewChangeEvent;

                selectedSkill = event.getSelectedSkill();
                Timber.d("Selected skill '%s'. Assessing target", selectedSkill.getSkillName() );
                ArrayList<Tile> selectableTiles = selectedSkill.getSelectableTiles();
                if(selectableTiles == null){//when skill does not need a target
                    goToResolveSkillState(null);
                } else if(selectableTiles.isEmpty()){ // when no valid targets
                    viewChangeEvent = new ViewChangeEvent();
                    viewChangeEvent.addViewChangeType(ViewChangeEvent.TOAST_NO_VALID_TARGETS);
                            post(viewChangeEvent);
                    returnToSkillSelection();
                } else { // one or more valid targets
                    viewChangeEvent = new ViewChangeEvent();
                    viewChangeEvent.addViewChangeType(ViewChangeEvent.SHOW_TARGET_HIGHLIGHTS);
                    viewChangeEvent.addViewChangeType(ViewChangeEvent.SHOW_CANCEL_SKILL);
                    viewChangeEvent.setTileArrayList(selectableTiles);
                    post(viewChangeEvent);
                }
                break;
            case ACTION_TARGETED:
                viewChangeEvent = new ViewChangeEvent();
                viewChangeEvent.addViewChangeType(ViewChangeEvent.PROCESSING);
                post(viewChangeEvent);

                goToResolveSkillState(event.getTargetTile());
                break;
            case ACTION_CANCEL:
                viewChangeEvent = new ViewChangeEvent();
                viewChangeEvent.addViewChangeType(ViewChangeEvent.HIDE_TARGET_HIGHLIGHTS);
                returnToSkillSelection();
                post(viewChangeEvent);
                break;
        }

    }

    private void returnToSkillSelection(){
        selectedSkill = null;
        TurnStateEvent turnStateEvent = new TurnStateEvent();
        turnStateEvent.setTargetState(TurnState.SELECT_SKILL_STATE);
        turnStateEvent.setAction(SelectSkillStateHandler.ACTION_WAITING);
        post(turnStateEvent);
    }

    private void goToResolveSkillState(Tile targetTile){
        if(targetTile!=null){
            Timber.d("Target of tile is: ROOM [%s] X:%s|Y:%s", targetTile.getRoomName(), targetTile.getXCoordinate(), targetTile.getYCoordinate());
        }

        TurnStateEvent turnStateEvent = new TurnStateEvent();
        turnStateEvent.setTargetState(TurnState.RESOLVE_SKILL_STATE);
        turnStateEvent.setSelectedSkill(selectedSkill);
        turnStateEvent.setTargetTile(targetTile);

        selectedSkill = null;
        post(turnStateEvent);
    }
}
