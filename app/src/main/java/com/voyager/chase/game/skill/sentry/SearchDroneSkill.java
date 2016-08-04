package com.voyager.chase.game.skill.sentry;

import com.voyager.chase.game.entity.Tile;
import com.voyager.chase.game.entity.player.Spy;
import com.voyager.chase.game.event.ViewChangeEvent;
import com.voyager.chase.game.skill.Skill;
import com.voyager.chase.mqtt.payload.GameInfoPayload;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by miguellysanchez on 7/25/16.
 */
public class SearchDroneSkill extends Skill {
    @Override
    public ArrayList<Tile> getSelectableTiles() {
        return null;
    }

    @Override
    public void useSkillOnTile(Tile selectedTile) {
        Spy spy = Spy.getInstance();
        GameInfoPayload gameInfoPayload = new GameInfoPayload();
        gameInfoPayload.setSenderRole(mSkillOwner.getRole());
        gameInfoPayload.setSenderMessage(String.format("SEARCH DRONE located SPY in Room: <%s> at tile [%d,%d]",
                spy.getCurrentRoomName(), spy.getCurrentTileXCoordinate(), spy.getCurrentTileYCoordinate()));

        ViewChangeEvent searchDroneEffect = new ViewChangeEvent();
        searchDroneEffect.addViewChangeType(ViewChangeEvent.GAME_INFO_UPDATE);
        searchDroneEffect.setGameInfoUpdate(gameInfoPayload.toJson());

        EventBus.getDefault().post(searchDroneEffect);
    }

}
