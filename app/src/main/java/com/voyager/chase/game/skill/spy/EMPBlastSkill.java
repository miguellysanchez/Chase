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
public class EMPBlastSkill extends Skill {
    @Override
    public ArrayList<Tile> getSelectableTiles() {
        return World.getInstance().getRoom(mSkillOwner.getCurrentRoomName()).getAllTiles();
    }

    @Override
    public void useSkillOnTile(Tile selectedTile) {
        GameInfoPayload constructDestroyedInfo;
        for (Tile tile : getAffectedTiles(selectedTile)) {
            ArrayList<Construct> constructList = tile.getAllConstructsList();
            for (Construct construct : constructList) {
                if (!construct.isInvulnerable()) {
                    WorldEffect removeConstructEffect = new WorldEffect();
                    removeConstructEffect.setEffectType(WorldEffect.REMOVE_CONSTRUCT);
                    removeConstructEffect.setAffectedUUID(construct.getId());
                    World.getInstance().addWorldEffectToQueue(removeConstructEffect);

                    constructDestroyedInfo = new GameInfoPayload();
                    constructDestroyedInfo.setSenderRole(mSkillOwner.getRole());
                    constructDestroyedInfo.setSenderMessage("The EMP BLAST destroyed the " + construct.getOwner().getRole() + "'s " + construct.getConstructName());
                    ViewChangeEvent constructDestroyedEvent = new ViewChangeEvent();
                    constructDestroyedEvent.addViewChangeType(ViewChangeEvent.GAME_INFO_UPDATE);
                    constructDestroyedEvent.setGameInfoUpdate(constructDestroyedInfo.toJson());
                    EventBus.getDefault().post(constructDestroyedEvent);
                }
            }
        }
    }

    private ArrayList<Tile> getAffectedTiles(Tile selectedTile) {
        ArrayList<Tile> tileArrayList = new ArrayList<>();
        tileArrayList.add(selectedTile);
        addTilesFromRoom(tileArrayList, selectedTile.getXCoordinate() - 1, selectedTile.getYCoordinate());
        addTilesFromRoom(tileArrayList, selectedTile.getXCoordinate() + 1, selectedTile.getYCoordinate());
        addTilesFromRoom(tileArrayList, selectedTile.getXCoordinate(), selectedTile.getYCoordinate() - 1);
        addTilesFromRoom(tileArrayList, selectedTile.getXCoordinate(), selectedTile.getYCoordinate() + 1);
        return tileArrayList;
    }

    private void addTilesFromRoom(ArrayList<Tile> tileArrayList, int x, int y) {
        if (TileUtility.isWithinRoom(x, y)) {
            tileArrayList.add(World.getInstance().getRoom(mSkillOwner.getCurrentRoomName()).getTileAtCoordinates(x, y));
        }
    }

}
