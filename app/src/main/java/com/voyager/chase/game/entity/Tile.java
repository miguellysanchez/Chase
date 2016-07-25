package com.voyager.chase.game.entity;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.voyager.chase.game.entity.player.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

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
    private HashMap<String, String> mVisibilityModifierMap;
    private Player mPlayer;

    private Tile(){}

    public Tile(int xCoordinate, int yCoordinate){
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        mSkillItemsMultimap  = ArrayListMultimap.create();
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    private String text;

    public Player getPlayer() {
        return mPlayer;
    }

    public void setPlayer(Player player) {
        this.mPlayer = player;
        player.setCurrentRoomName(mRoomName);
        player.setCurrentTileXCoordinate(xCoordinate);
        player.setCurrentTileYCoordinate(yCoordinate);
    }

    public ArrayList<SkillItem> getAllSkillItemsList(){
        return new ArrayList<>(mSkillItemsMultimap.values());
    }

    public void removeItem(String uuid){
    }

    public Collection<String> getVisibilityModifierList() {
        return mVisibilityModifierMap.values();
    }

    public void addVisibilityModifier(String uuid, String visibilityModifier){
        mVisibilityModifierMap.put(uuid, visibilityModifier);
    }

}
