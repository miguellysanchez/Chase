package com.voyager.chase.game.skill.spy;

import com.voyager.chase.game.World;
import com.voyager.chase.game.entity.Tile;
import com.voyager.chase.game.event.ViewChangeEvent;
import com.voyager.chase.game.skill.Skill;
import com.voyager.chase.game.worldeffect.WorldEffect;
import com.voyager.chase.mqtt.payload.GameInfoPayload;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by miguellysanchez on 7/25/16.
 */
public class SnipeSkill extends Skill {
    @Override
    public ArrayList<Tile> getSelectableTiles() {
        ArrayList<Tile> selectableTiles = new ArrayList<>();
        ArrayList<Tile> roomTileArrayList = World.getInstance().getRoom(mSkillOwner.getCurrentRoomName()).getAllTiles();
        for (Tile tile : roomTileArrayList) {
            if (tile.getXCoordinate() != mSkillOwner.getCurrentTileXCoordinate() || tile.getYCoordinate() != mSkillOwner.getCurrentTileYCoordinate()) {
                selectableTiles.add(tile);
            }
        }
        return selectableTiles;
    }

    @Override
    public void useSkillOnTile(Tile selectedTile) {
        GameInfoPayload payload = new GameInfoPayload();
        payload.setSenderRole(mSkillOwner.getRole());
        ViewChangeEvent viewChangeEvent = new ViewChangeEvent();
        viewChangeEvent.addViewChangeType(ViewChangeEvent.GAME_INFO_UPDATE);
        if (selectedTile.containsPlayer()) {
            WorldEffect snipeEffect = new WorldEffect();
            snipeEffect.setEffectType(WorldEffect.MODIFY_PLAYER);
            snipeEffect.setEffectContent(WorldEffect.REDUCE_PLAYER_LIFE);
            snipeEffect.setAffectedRole(selectedTile.getPlayer().getRole());
            World.getInstance().addWorldEffectToQueue(snipeEffect);

            payload.setSenderMessage("You hit the other player!!");
            payload.setNonSenderMessage("You've been SNIPED!!!! You lost 1 life.");
        } else {
            payload.setSenderMessage("You missed!!");
        }
        viewChangeEvent.setGameInfoUpdate(payload.toJson());
        EventBus.getDefault().post(viewChangeEvent);

    }

}
