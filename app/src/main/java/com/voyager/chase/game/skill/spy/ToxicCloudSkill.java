package com.voyager.chase.game.skill.spy;

import com.voyager.chase.game.World;
import com.voyager.chase.game.entity.Tile;
import com.voyager.chase.game.event.ViewChangeEvent;
import com.voyager.chase.game.skill.Skill;
import com.voyager.chase.game.worldeffect.WorldEffect;
import com.voyager.chase.mqtt.payload.GameInfoPayload;
import com.voyager.chase.utility.TileUtility;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by miguellysanchez on 7/25/16.
 */
public class ToxicCloudSkill extends Skill {
    @Override
    public ArrayList<Tile> getSelectableTiles() {
        return null;
    }

    @Override
    public void useSkillOnTile(Tile tile) {
        Tile currentTile = World.getInstance().getRoom(mSkillOwner.getCurrentRoomName()).getTileAtCoordinates(mSkillOwner.getCurrentTileX(), mSkillOwner.getCurrentTileY());
        for (int x = currentTile.getXCoordinate() - 3; x <= currentTile.getXCoordinate() + 3; x++) {
            for (int y = currentTile.getXCoordinate() - 3; y <= currentTile.getYCoordinate() + 3; y++) {
                if (TileUtility.isWithinRoom(x, y)) {
                    Tile affectedTile = World.getInstance().getRoom(currentTile.getRoomName()).getTileAtCoordinates(x, y);
                    if (affectedTile.containsPlayer() && !affectedTile.getPlayer().getRole().equals(mSkillOwner.getRole())) {
                        WorldEffect damageEffect = new WorldEffect();
                        damageEffect.setEffectType(WorldEffect.MODIFY_PLAYER);
                        damageEffect.setAffectedRole(affectedTile.getPlayer().getRole());
                        damageEffect.setEffectContent(WorldEffect.REDUCE_PLAYER_LIFE);
                        World.getInstance().addWorldEffectToQueue(damageEffect);

                        GameInfoPayload gameInfoPayload = new GameInfoPayload();
                        gameInfoPayload.setSenderRole(mSkillOwner.getRole());
                        gameInfoPayload.setNonSenderMessage("A TOXIC CLOUD enshrouds you for a moment. You lost 1 life");
                        ViewChangeEvent viewChangeEvent = new ViewChangeEvent();
                        viewChangeEvent.addViewChangeType(ViewChangeEvent.GAME_INFO_UPDATE);
                        viewChangeEvent.setGameInfoUpdate(gameInfoPayload.toJson());
                        EventBus.getDefault().post(viewChangeEvent);
                    }
                }
            }
        }
    }

}
