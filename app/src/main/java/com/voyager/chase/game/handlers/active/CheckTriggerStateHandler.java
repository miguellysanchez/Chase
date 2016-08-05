package com.voyager.chase.game.handlers.active;

import com.voyager.chase.game.TurnState;
import com.voyager.chase.game.World;
import com.voyager.chase.game.entity.Tile;
import com.voyager.chase.game.entity.player.Player;
import com.voyager.chase.game.entity.construct.Trigger;
import com.voyager.chase.game.event.TurnStateEvent;
import com.voyager.chase.game.handlers.TurnStateHandler;

import java.util.ArrayList;
import java.util.Collection;

import timber.log.Timber;

/**
 * Created by miguellysanchez on 8/1/16.
 */
public class CheckTriggerStateHandler extends TurnStateHandler {
    private ArrayList<Tile> triggeredTilesList;

    public CheckTriggerStateHandler() {
        triggeredTilesList = new ArrayList<>();
    }

    @Override
    public void handleTurnStateEvent(TurnStateEvent event) {
        Timber.d("On handle CHECK TRIGGER turn state event");

        Player userPlayer = World.getUserPlayer();
        Tile currentTile = World.getInstance().getRoom(userPlayer.getCurrentRoomName())
                .getTileAtCoordinates(userPlayer.getCurrentTileXCoordinate(), userPlayer.getCurrentTileYCoordinate());
        triggeredTilesList.add(currentTile);

        TurnStateEvent turnStateEvent = new TurnStateEvent();
        Collection<Trigger> triggerList = currentTile.getTriggerList();
        if (triggerList == null || triggerList.isEmpty()) {
            resetAllTriggeredTiles();
            turnStateEvent.setTargetState(TurnState.UPKEEP_STATE);
        } else {
            boolean willReturnToQueue = validateTriggerActivation(triggerList, userPlayer);
            if (willReturnToQueue) {
                turnStateEvent.setTargetState(TurnState.CHECK_QUEUE_STATE);
            } else {
                resetAllTriggeredTiles();
                turnStateEvent.setTargetState(TurnState.UPKEEP_STATE);
            }
        }
        post(turnStateEvent);
    }

    private void resetAllTriggeredTiles() {
        for (Tile triggeredTile : triggeredTilesList) {
            for (Trigger trigger : triggeredTile.getTriggerList()) {
                trigger.resetTrigger();
            }
        }
        triggeredTilesList.clear();
    }

    private boolean validateTriggerActivation(Collection<Trigger> triggerList, Player userPlayer) {
        boolean result = false;
        for (Trigger trigger : triggerList) {
            boolean triggerResult = trigger.invokeTrigger(userPlayer);
            result = triggerResult || result;
        }
        return result;
    }
}
