package com.voyager.chase.game.entity;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.voyager.chase.game.entity.player.Player;
import com.voyager.chase.game.entity.skillitem.ItemTrigger;
import com.voyager.chase.game.entity.skillitem.SkillItem;

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
    private Multimap<String, SkillItem> mSkillItemsMultimap;
    private HashMap<String, ItemTrigger> mItemTriggersMap;
    private HashMap<String, String> mVisibilityModifierMap;
    private Player mPlayer;

    private Tile() {
    }

    public Tile(int xCoordinate, int yCoordinate) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        mSkillItemsMultimap = ArrayListMultimap.create();
        mItemTriggersMap = new HashMap<>();
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

    public ArrayList<SkillItem> getAllSkillItemsList() {
        return new ArrayList<>(mSkillItemsMultimap.values());
    }

    public void addSkillItem(SkillItem skillItem) {
        mSkillItemsMultimap.put(skillItem.getUuid(), skillItem);
        setLocationToRenderable(skillItem);
        skillItem.onAddedToTile();
    }

    public void addItemTrigger(String uuid, ItemTrigger itemTrigger) {
        mItemTriggersMap.put(uuid, itemTrigger);
    }

    public void addVisibilityModifier(String uuid, String visibilityModifier) {
        mVisibilityModifierMap.put(uuid, visibilityModifier);
    }

    public void removeItem(String uuid) {
        for (SkillItem skillItem : mSkillItemsMultimap.get(uuid)) {
            removeLocationFromRenderable(skillItem);
            skillItem.onRemovedFromTile();
        }
        mSkillItemsMultimap.removeAll(uuid);
        mItemTriggersMap.remove(uuid);
        mVisibilityModifierMap.remove(uuid);
    }

    public Collection<String> getVisibilityModifierList() {
        return mVisibilityModifierMap.values();
    }

    public Collection<ItemTrigger> getItemTriggerList() {
        return mItemTriggersMap.values();
    }

    public boolean containsObstacle() {
        for (SkillItem skillItem : mSkillItemsMultimap.values()) {
            if (skillItem.isObstacle()) {
                return true;
            }
        }
        return false;
    }

    public boolean containsPlayer() {
        return mPlayer != null;
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
