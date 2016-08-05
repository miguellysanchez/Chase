package com.voyager.chase.game.skill.sentry;

import com.voyager.chase.game.World;
import com.voyager.chase.game.entity.Tile;
import com.voyager.chase.game.entity.player.Player;
import com.voyager.chase.game.entity.construct.Construct;
import com.voyager.chase.game.event.ViewChangeEvent;
import com.voyager.chase.game.worldeffect.WorldEffect;
import com.voyager.chase.game.skill.Skill;
import com.voyager.chase.mqtt.payload.GameInfoPayload;
import com.voyager.chase.utility.TileUtility;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by miguellysanchez on 7/25/16.
 */
public class AttackSkill extends Skill {
    @Override
    public ArrayList<Tile> getSelectableTiles() {
        ArrayList<Tile> selectableTilesList = new ArrayList<>();
        Player player = World.getUserPlayer();
        String roomName = player.getCurrentRoomName();
        int currentTileX = player.getCurrentTileXCoordinate();
        int currentTileY = player.getCurrentTileYCoordinate();

        safeAddToSelectableTiles(selectableTilesList, checkTileAtCoordinates(roomName, currentTileX + 1, currentTileY));
        safeAddToSelectableTiles(selectableTilesList, checkTileAtCoordinates(roomName, currentTileX - 1, currentTileY));
        safeAddToSelectableTiles(selectableTilesList, checkTileAtCoordinates(roomName, currentTileX, currentTileY + 1));
        safeAddToSelectableTiles(selectableTilesList, checkTileAtCoordinates(roomName, currentTileX, currentTileY - 1));

        return selectableTilesList;
    }

    @Override
    public void useSkillOnTile(Tile selectedTile) {
        GameInfoPayload playerDamageInfo = new GameInfoPayload();
        if (selectedTile.containsPlayer()) {
            Player attackedPlayer = selectedTile.getPlayer();
            WorldEffect playerDamageWorldEffect = new WorldEffect();
            playerDamageWorldEffect.setEffectType(WorldEffect.MODIFY_PLAYER);
            playerDamageWorldEffect.setAffectedRole(attackedPlayer.getRole());
            playerDamageWorldEffect.setEffectContent(WorldEffect.REDUCE_PLAYER_LIFE);
            World.getInstance().addWorldEffectToQueue(playerDamageWorldEffect);

            playerDamageInfo.setSenderRole(mSkillOwner.getRole());
            playerDamageInfo.setSenderMessage("Your ATTACK hit the " + selectedTile.getPlayer().getRole() + "!");
            playerDamageInfo.setNonSenderMessage("You were ATTACKED, and lost 1 life.");
            ViewChangeEvent playerDamagedEvent = new ViewChangeEvent();
            playerDamagedEvent.addViewChangeType(ViewChangeEvent.GAME_INFO_UPDATE);
            playerDamagedEvent.setGameInfoUpdate(playerDamageInfo.toJson());
            EventBus.getDefault().post(playerDamagedEvent);
        }

        GameInfoPayload constructDestroyedInfo;
        ArrayList<Construct> constructList = selectedTile.getAllConstructsList();
        for (Construct construct : constructList) {
            if (!construct.isInvulnerable()) {
                WorldEffect removeConstructEffect = new WorldEffect();
                removeConstructEffect.setEffectType(WorldEffect.REMOVE_CONSTRUCT);
                removeConstructEffect.setAffectedUUID(construct.getId());
                World.getInstance().addWorldEffectToQueue(removeConstructEffect);

                constructDestroyedInfo = new GameInfoPayload();
                constructDestroyedInfo.setSenderRole(mSkillOwner.getRole());
                constructDestroyedInfo.setSenderMessage(
                        "Your ATTACK hit the " + construct.getOwner().getRole() + "'s " + construct.getConstructName());
                ViewChangeEvent constructDestroyedEvent = new ViewChangeEvent();
                constructDestroyedEvent.addViewChangeType(ViewChangeEvent.GAME_INFO_UPDATE);
                constructDestroyedEvent.setGameInfoUpdate(constructDestroyedInfo.toJson());
                EventBus.getDefault().post(constructDestroyedEvent);
            }
        }
    }

    private Tile checkTileAtCoordinates(String roomName, int x, int y) {
        Tile checkingTile;
        if (TileUtility.isWithinRoom(x, y)) {
            checkingTile = World.getInstance().getRoom(roomName).getTileAtCoordinates(x, y);
            return checkingTile;
        }
        return null;
    }

    private void safeAddToSelectableTiles(ArrayList<Tile> selectableTiles, Tile tile) {
        if (tile != null) {
            selectableTiles.add(tile);
        }
    }
}
