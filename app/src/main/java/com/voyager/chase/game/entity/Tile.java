package com.voyager.chase.game.entity;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.voyager.chase.game.entity.player.Player;
import com.voyager.chase.game.entity.construct.Trigger;
import com.voyager.chase.game.entity.construct.Construct;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by miguellysanchez on 7/4/16.
 */
public class Tile {
    public static final String GLOBAL_VISIBILITY = "global_visibility";
    public static final String SPY_ONLY_GLOBAL_VISIBILITY = "spy_only_global_visibility";
    public static final String SENTRY_ONLY_GLOBAL_VISIBILITY = "sentry_only_global_visibility";

    private String mRoomName;
    private int xCoordinate;
    private int yCoordinate;
    private Multimap<String, Construct> mConstructsMultimap;
    private HashMap<String, Trigger> mTriggersMap;
    private HashMap<String, String> mVisibilityModifierMap;
    private Player mPlayer;

    private Tile() {
    }

    public Tile(int xCoordinate, int yCoordinate) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        mConstructsMultimap = ArrayListMultimap.create();
        mTriggersMap = new HashMap<>();
        mVisibilityModifierMap = new HashMap<>();
    }

    public String getRoomName() {
        return mRoomName;
    }

    public void setRoomName(String mRoomName) {
        this.mRoomName = mRoomName;
    }

    public int getXCoordinate() {
        return xCoordinate;
    }

    public int getYCoordinate() {
        return yCoordinate;
    }

    public Player getPlayer() {
        return mPlayer;
    }

    public void setPlayer(Player player) {
        this.mPlayer = player;
        setLocationToRenderable(player);
    }

    public void removePlayer() {
        removeLocationFromRenderable(mPlayer);
        this.mPlayer = null;
    }

    public ArrayList<Construct> getAllConstructsList() {
        return new ArrayList<>(mConstructsMultimap.values());
    }

    public void addConstruct(Construct construct) {
        mConstructsMultimap.put(construct.getUUID(), construct);
        setLocationToRenderable(construct);
        construct.onAddedToTile();
    }

    public void addTrigger(String uuid, Trigger trigger) {
        mTriggersMap.put(uuid, trigger);
    }

    public void addVisibilityModifier(String uuid, String visibilityModifier) {
        mVisibilityModifierMap.put(uuid, visibilityModifier);
    }

    public void removeItem(String uuid) {
        for (Construct construct : mConstructsMultimap.get(uuid)) {
            removeLocationFromRenderable(construct);
            construct.onRemovedFromTile();
        }
        mConstructsMultimap.removeAll(uuid);
        mTriggersMap.remove(uuid);
        mVisibilityModifierMap.remove(uuid);
    }

    public Collection<String> getVisibilityModifierList() {
        return mVisibilityModifierMap.values();
    }

    public Collection<Trigger> getTriggerList() {
        return mTriggersMap.values();
    }

    public boolean containsObstacle() {
        for (Construct construct : mConstructsMultimap.values()) {
            if (construct.isObstacle()) {
                return true;
            }
        }
        return false;
    }

    public boolean containsPlayer() {
        return mPlayer != null;
    }

    public boolean isUntargetable(){
        for (Construct construct : mConstructsMultimap.values()) {
            if (construct.isUntargetable()) {
                return true;
            }
        }
        return false;
    }

    private void setLocationToRenderable(Renderable renderable) {
        renderable.setCurrentRoomName(mRoomName);
        renderable.setCurrentTileXCoordinate(xCoordinate);
        renderable.setCurrentTileYCoordinate(yCoordinate);
    }

    private void removeLocationFromRenderable(Renderable renderable) {
        renderable.setCurrentRoomName(mRoomName);
        renderable.setCurrentTileXCoordinate(-1);
        renderable.setCurrentTileYCoordinate(-1);
    }
}
