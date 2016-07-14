package com.voyager.chase.game.entity;

import com.voyager.chase.game.entity.player.Player;

import java.util.ArrayList;

/**
 * Created by miguellysanchez on 7/4/16.
 */
public class Tile {
    private String mRoomName;
    private int xCoordinate;
    private int yCoordinate;
    private ArrayList<SkillItem> mSkillItemsList;
    private ArrayList<Player> mPlayersList;

    private Tile(){}

    public Tile(int xCoordinate, int yCoordinate){
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
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
}
