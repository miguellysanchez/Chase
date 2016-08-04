package com.voyager.chase.game.skill.sentry;

import com.voyager.chase.game.World;
import com.voyager.chase.game.entity.Tile;
import com.voyager.chase.game.event.ViewChangeEvent;
import com.voyager.chase.game.gameinfo.GameInfo;
import com.voyager.chase.game.skill.Skill;
import com.voyager.chase.game.worldeffect.WorldEffect;
import com.voyager.chase.mqtt.payload.GameInfoPayload;
import com.voyager.chase.utility.TileUtility;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by miguellysanchez on 7/25/16.
 */
public class ShockGrenadeSkill extends Skill {
    @Override
    public ArrayList<Tile> getSelectableTiles() {
        return World.getInstance().getRoom(mSkillOwner.getCurrentRoomName()).getAllTiles();
    }

    @Override
    public void useSkillOnTile(Tile selectedTile) {
        for (int x = selectedTile.getXCoordinate() - 2; x <= selectedTile.getXCoordinate() + 2; x++){
            for(int y = selectedTile.getXCoordinate() - 2; y <= selectedTile.getYCoordinate() + 2; y++){
                if(TileUtility.isWithinRoom(x, y)){
                    Tile affectedTile = World.getInstance().getRoom(selectedTile.getRoomName()).getTileAtCoordinates(x,y);
                    if(affectedTile.containsPlayer() && !mSkillOwner.getRole().equals(affectedTile.getPlayer().getRole())){
                        WorldEffect stunEffect = new WorldEffect();
                        stunEffect.setEffectType(WorldEffect.MODIFY_PLAYER);
                        stunEffect.setAffectedRole(affectedTile.getPlayer().getRole());
                        stunEffect.setEffectContent(WorldEffect.SKIP_PLAYER_TURN);
                        World.getInstance().addWorldEffectToQueue(stunEffect);

                        GameInfoPayload gameInfoPayload = new GameInfoPayload();
                        gameInfoPayload.setSenderRole(mSkillOwner.getRole());
                        gameInfoPayload.setSenderMessage("You have shocked the other player.");
                        gameInfoPayload.setNonSenderMessage("You have been shocked by the other player's SHOCK GRENADE. Your next turn will be skipped.");

                        ViewChangeEvent viewChangeEvent = new ViewChangeEvent();
                        viewChangeEvent.addViewChangeType(ViewChangeEvent.GAME_INFO_UPDATE);
                        viewChangeEvent.setGameInfoUpdate(gameInfoPayload.toJson());
                        EventBus.getDefault().post(viewChangeEvent);
                        return;
                    }
                }
            }
        }
    }

}
