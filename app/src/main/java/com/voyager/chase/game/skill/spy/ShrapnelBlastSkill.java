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
public class ShrapnelBlastSkill extends Skill {
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
                    if(affectedTile.containsPlayer()){
                        WorldEffect damageEffect = new WorldEffect();
                        damageEffect.setEffectType(WorldEffect.MODIFY_PLAYER);
                        damageEffect.setAffectedRole(affectedTile.getPlayer().getRole());
                        damageEffect.setEffectContent(WorldEffect.REDUCE_PLAYER_LIFE);
                        World.getInstance().addWorldEffectToQueue(damageEffect);

                        GameInfoPayload gameInfoPayload = new GameInfoPayload();
                        gameInfoPayload.setSenderRole(mSkillOwner.getRole());
                        if(mSkillOwner.getRole().equals(affectedTile.getPlayer().getRole())){
                            gameInfoPayload.setSenderMessage("You were caught in the area of the SHRAPNEL BLAST!!. You lost 1 life");
                        } else {
                            gameInfoPayload.setNonSenderMessage("You have been caught in a SHRAPNEL BLAST. You lost 1 life");
                        }

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
