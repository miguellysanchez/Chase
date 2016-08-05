package com.voyager.chase.game.skill.sentry;

import com.voyager.chase.game.World;
import com.voyager.chase.game.entity.Tile;
import com.voyager.chase.game.entity.construct.Construct;
import com.voyager.chase.game.entity.player.Player;
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
public class FragGrenadeSkill extends Skill {
    @Override
    public ArrayList<Tile> getSelectableTiles() {
        ArrayList<Tile> selectableTileArrayList = new ArrayList<>();
        for (int x = mSkillOwner.getCurrentTileX() - 2; x <= mSkillOwner.getCurrentTileX() + 2; x++) {
            for (int y = mSkillOwner.getCurrentTileY() - 2; y <= mSkillOwner.getCurrentTileY() + 2; y++) {
                if (TileUtility.isWithinRoom(x, y)) {
                    Tile selectableTile = World.getInstance().getRoom(mSkillOwner.getCurrentRoomName()).getTileAtCoordinates(x, y);
                    selectableTileArrayList.add(selectableTile);
                }
            }
        }
        return selectableTileArrayList;
    }

    @Override
    public void useSkillOnTile(Tile selectedTile) {
        for (int x = selectedTile.getXCoordinate() - 1; x <= selectedTile.getXCoordinate() + 1; x++) {
            for (int y = selectedTile.getYCoordinate() - 1; y <= selectedTile.getYCoordinate() + 1; y++) {
                if (TileUtility.isWithinRoom(x, y)) {
                    Tile affectableTile = World.getInstance().getRoom(selectedTile.getRoomName()).getTileAtCoordinates(x, y);
                    invokeDamagePlayer(affectableTile);
                    invokeRemoveConstruct(affectableTile);
                }
            }
        }
    }

    private void invokeDamagePlayer(Tile tile) {
        GameInfoPayload playerDamageInfo = new GameInfoPayload();

        if(tile.containsPlayer()){
            Player attackedPlayer = tile.getPlayer();
            WorldEffect playerDamageEffect = new WorldEffect();
            playerDamageEffect.setEffectType(WorldEffect.MODIFY_PLAYER);
            playerDamageEffect.setAffectedRole(attackedPlayer.getRole());
            playerDamageEffect.setEffectContent(WorldEffect.REDUCE_PLAYER_LIFE);
            World.getInstance().addWorldEffectToQueue(playerDamageEffect);

            playerDamageInfo.setSenderRole(mSkillOwner.getRole());
            playerDamageInfo.setSenderMessage("Your own FRAG GRENADE exploded near you. Lose 1 life.");
            playerDamageInfo.setNonSenderMessage("A FRAG GRENADE exploded close to you. You are wounded; lose 1 life.");
            ViewChangeEvent playerDamagedEvent = new ViewChangeEvent();
            playerDamagedEvent.addViewChangeType(ViewChangeEvent.GAME_INFO_UPDATE);
            playerDamagedEvent.setGameInfoUpdate(playerDamageInfo.toJson());
            EventBus.getDefault().post(playerDamagedEvent);
        }
    }

    private void invokeRemoveConstruct(Tile tile) {
        GameInfoPayload constructDestroyedInfo;
        ArrayList<Construct> constructList = tile.getAllConstructsList();
        for (Construct construct : constructList) {
            if (!construct.isInvulnerable()) {
                WorldEffect removeConstructEffect = new WorldEffect();
                removeConstructEffect.setEffectType(WorldEffect.REMOVE_CONSTRUCT);
                removeConstructEffect.setAffectedUUID(construct.getId());
                World.getInstance().addWorldEffectToQueue(removeConstructEffect);

                constructDestroyedInfo = new GameInfoPayload();
                constructDestroyedInfo.setSenderRole(mSkillOwner.getRole());
                constructDestroyedInfo.setSenderMessage("Your FRAG GRENADE destroyed something");
                ViewChangeEvent constructDestroyedEvent = new ViewChangeEvent();
                constructDestroyedEvent.addViewChangeType(ViewChangeEvent.GAME_INFO_UPDATE);
                constructDestroyedEvent.setGameInfoUpdate(constructDestroyedInfo.toJson());
                EventBus.getDefault().post(constructDestroyedEvent);
            }
        }
    }
}
