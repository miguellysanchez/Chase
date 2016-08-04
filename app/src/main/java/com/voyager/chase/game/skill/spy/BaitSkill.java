package com.voyager.chase.game.skill.spy;

import com.voyager.chase.game.entity.Tile;
import com.voyager.chase.game.event.ViewChangeEvent;
import com.voyager.chase.game.skill.Skill;
import com.voyager.chase.mqtt.payload.GameInfoPayload;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by miguellysanchez on 7/25/16.
 */
public class BaitSkill extends Skill {
    @Override
    public ArrayList<Tile> getSelectableTiles() {
        return null;
    }

    @Override
    public void useSkillOnTile(Tile selectedTile) {

        GameInfoPayload gameInfoPayload = new GameInfoPayload();
        gameInfoPayload.setSenderRole(mSkillOwner.getRole());
        gameInfoPayload.setSenderMessage("You fired off a flare to BAIT other players to your position");
        gameInfoPayload.setNonSenderMessage(
                String.format("A flare was fired from another tile. It came from Room <%s>, at tile[%d,%d]",
                        mSkillOwner.getCurrentRoomName(),
                        mSkillOwner.getCurrentTileXCoordinate(),
                        mSkillOwner.getCurrentTileYCoordinate()));

        ViewChangeEvent viewChangeEvent = new ViewChangeEvent();
        viewChangeEvent.addViewChangeType(ViewChangeEvent.GAME_INFO_UPDATE);
        viewChangeEvent.setGameInfoUpdate(gameInfoPayload.toJson());
        EventBus.getDefault().post(viewChangeEvent);
    }

}
