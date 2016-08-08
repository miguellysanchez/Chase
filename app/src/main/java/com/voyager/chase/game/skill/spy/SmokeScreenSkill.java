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
 * Created by miguellysanchez on 8/8/16.
 */
public class SmokeScreenSkill extends Skill {
    @Override
    public ArrayList<Tile> getSelectableTiles() {
        return null;
    }

    @Override
    public void useSkillOnTile(Tile selectedTile) {
        Tile currentTile = World.getInstance().getRoom(mSkillOwner.getCurrentRoomName()).getTileAtCoordinates(mSkillOwner.getCurrentTileX(), mSkillOwner.getCurrentTileY());
        for (int x = currentTile.getXCoordinate() - 2; x <= currentTile.getXCoordinate() + 2; x++) {
            for (int y = currentTile.getXCoordinate() - 2; y <= currentTile.getYCoordinate() + 2; y++) {
                if (TileUtility.isWithinRoom(x, y)) {
                    Tile affectedTile = World.getInstance().getRoom(currentTile.getRoomName()).getTileAtCoordinates(x, y);
                    if (affectedTile.containsPlayer() && !affectedTile.getPlayer().getRole().equals(mSkillOwner.getRole())) {
                        WorldEffect skipTurnEffect = new WorldEffect();
                        skipTurnEffect.setEffectType(WorldEffect.MODIFY_PLAYER);
                        skipTurnEffect.setAffectedRole(affectedTile.getPlayer().getRole());
                        skipTurnEffect.setEffectContent(WorldEffect.SKIP_PLAYER_TURN);
                        World.getInstance().addWorldEffectToQueue(skipTurnEffect);

                        GameInfoPayload gameInfoPayload = new GameInfoPayload();
                        gameInfoPayload.setSenderRole(mSkillOwner.getRole());
                        gameInfoPayload.setNonSenderMessage("You were covered with a SMOKE SCREEN. You became disoriented. Skip your next turn.");
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
