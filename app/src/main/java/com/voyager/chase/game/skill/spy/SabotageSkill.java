package com.voyager.chase.game.skill.spy;

import com.voyager.chase.game.World;
import com.voyager.chase.game.entity.Tile;
import com.voyager.chase.game.entity.construct.Construct;
import com.voyager.chase.game.entity.construct.ObjectiveConstruct;
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
public class SabotageSkill extends Skill {
    @Override
    public ArrayList<Tile> getSelectableTiles() {
        ArrayList<Tile> selectableTilesList = new ArrayList<>();
        addSelectableTiles(selectableTilesList, mSkillOwner.getCurrentTileXCoordinate() - 1, mSkillOwner.getCurrentTileYCoordinate());
        addSelectableTiles(selectableTilesList, mSkillOwner.getCurrentTileXCoordinate() + 1, mSkillOwner.getCurrentTileYCoordinate());
        addSelectableTiles(selectableTilesList, mSkillOwner.getCurrentTileXCoordinate(), mSkillOwner.getCurrentTileYCoordinate() - 1);
        addSelectableTiles(selectableTilesList, mSkillOwner.getCurrentTileXCoordinate(), mSkillOwner.getCurrentTileYCoordinate() + 1);
        return selectableTilesList;
    }

    private void addSelectableTiles(ArrayList<Tile> selectableTilesList, int x, int y) {
        if (TileUtility.isWithinRoom(x, y)) {
            Tile tile = World.getInstance().getRoom(mSkillOwner.getCurrentRoomName()).getTileAtCoordinates(x, y);
            selectableTilesList.add(tile);
        }
    }


    @Override
    public void useSkillOnTile(Tile selectedTile) {
        GameInfoPayload constructDestroyedInfo;
        ArrayList<Construct> constructList = selectedTile.getAllConstructsList();
        for (Construct construct : constructList) {
            if (!construct.isInvulnerable() ||
                    ObjectiveConstruct.OBJECTIVE_CONSTRUCT_NAME.equals(construct.getConstructName())) {
                WorldEffect removeConstructEffect = new WorldEffect();
                removeConstructEffect.setEffectType(WorldEffect.REMOVE_CONSTRUCT);
                removeConstructEffect.setAffectedUUID(construct.getId());
                World.getInstance().addWorldEffectToQueue(removeConstructEffect);

                constructDestroyedInfo = new GameInfoPayload();
                constructDestroyedInfo.setSenderRole(mSkillOwner.getRole());
                constructDestroyedInfo.setSenderMessage("You SABOTAGED the " + construct.getOwner().getRole() + "'s " + construct.getConstructName());
                ViewChangeEvent constructDestroyedEvent = new ViewChangeEvent();
                constructDestroyedEvent.addViewChangeType(ViewChangeEvent.GAME_INFO_UPDATE);
                constructDestroyedEvent.setGameInfoUpdate(constructDestroyedInfo.toJson());
                EventBus.getDefault().post(constructDestroyedEvent);
            }
        }

    }

}
