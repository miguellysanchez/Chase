package com.voyager.chase.game.entity;

/**
 * Created by miguellysanchez on 7/18/16.
 */
public class Renderable {

    private String mCurrentRoomName;
    private int mCurrentTileXCoordinate;
    private int mCurrentTileYCoordinate;

    public static final int HIDDEN = 0;
    public static final int PROXIMITY_VISIBLE = 1;
    public static final int GLOBALLY_VISIBLE = 2;

    protected int renderDrawableId;
    protected int sentryVisibility;
    protected int spyVisibility;

    public int getSentryVisibility(){
        return sentryVisibility;
    }
    
    public int getSpyVisibility(){
        return spyVisibility;
    }

    public int getRenderDrawableId(){
        return renderDrawableId;
    }

    public String getCurrentRoomName() {
        return mCurrentRoomName;
    }

    public void setCurrentRoomName(String mCurrentRoomName) {
        this.mCurrentRoomName = mCurrentRoomName;
    }

    public int getCurrentTileXCoordinate() {
        return mCurrentTileXCoordinate;
    }

    public void setCurrentTileXCoordinate(int mCurrentTileXCoordinate) {
        this.mCurrentTileXCoordinate = mCurrentTileXCoordinate;
    }

    public int getCurrentTileYCoordinate() {
        return mCurrentTileYCoordinate;
    }

    public void setCurrentTileYCoordinate(int mCurrentTileYCoordinate) {
        this.mCurrentTileYCoordinate = mCurrentTileYCoordinate;
    }
}
