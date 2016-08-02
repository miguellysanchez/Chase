package com.voyager.chase.game.handlers.active;

import com.voyager.chase.game.TurnState;
import com.voyager.chase.game.World;
import com.voyager.chase.game.entity.Tile;
import com.voyager.chase.game.entity.player.Player;
import com.voyager.chase.game.entity.skillitem.ItemTrigger;
import com.voyager.chase.game.event.TurnStateEvent;
import com.voyager.chase.game.handlers.TurnStateHandler;

import java.util.Collection;

import timber.log.Timber;

/**
 * Created by miguellysanchez on 8/1/16.
 */
public class CheckTriggerStateHandler extends TurnStateHandler {
    @Override
    public void handleTurnStateEvent(TurnStateEvent event) {
        Timber.d("On handle CHECK TRIGGER turn state event");

        Player userPlayer = World.getUserPlayer();
        Tile currentTile = World.getInstance().getRoom(userPlayer.getCurrentRoomName())
                .getTileAtCoordinate(userPlayer.getCurrentTileXCoordinate(), userPlayer.getCurrentTileYCoordinate());
        TurnStateEvent turnStateEvent = new TurnStateEvent();
        Collection<ItemTrigger> triggerList = currentTile.getItemTriggerList();
        if (triggerList == null || triggerList.isEmpty()) {
            turnStateEvent.setTargetState(TurnState.UPKEEP_STATE);
        } else {
            for (ItemTrigger itemTrigger : triggerList) {
                itemTrigger.onTriggered(userPlayer);
            }
            turnStateEvent.setTargetState(TurnState.CHECK_QUEUE_STATE);
        }
        post(turnStateEvent);
    }
}
