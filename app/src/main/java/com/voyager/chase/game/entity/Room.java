package com.voyager.chase.game.entity;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import timber.log.Timber;

/**
 * Created by miguellysanchez on 7/4/16.
 */
public class Room {
    public static final int ROOM_WIDTH = 10;
    public static final int ROOM_HEIGHT = 10;

    private String mRoomName;
    private Tile[][] mTiles2DArray = new Tile[ROOM_WIDTH][ROOM_HEIGHT];

    private Room() {
    }

    //Rooms can only be created if the the corresponding tile match the amount needed to create a room.
    public static Room createRoom(@NonNull String roomName, @NonNull ArrayList<Tile> tilesArrayList) {
        Room room = new Room();
        room.setRoomName(roomName);
        for (Tile tile : tilesArrayList) {
            room.addTile(tile);
        }
        if (room.getTileCount() == ROOM_WIDTH * ROOM_HEIGHT) {
            return room;
        }
        Timber.e("Unable to create room with number of tile not equal to %s", "" + (ROOM_WIDTH * ROOM_HEIGHT));
        return null;
    }

    public String getName() {
        return mRoomName;
    }

    private void setRoomName(String roomName) {
        mRoomName = roomName;
    }

    public Tile getTileAtCoordinates(int x, int y) {
        return mTiles2DArray[x][y];
    }

    public List<Tile> getTilesAtColumn(int x) {
        return Arrays.asList(mTiles2DArray[x]);
    }

    public List<Tile> getTilesAtRow(int y) {
        ArrayList<Tile> rowTilesList = new ArrayList<>();
        for (int column = 0; column < ROOM_WIDTH; column++) {
            rowTilesList.add(mTiles2DArray[column][y]);
        }
        return rowTilesList;
    }

    public Tile[][] getAllTiles2DArray(){
        return mTiles2DArray;
    }

    public List<Tile> getAllTiles() {
        ArrayList<Tile> allTilesList = new ArrayList<>();
        for (Tile[] tilesArray : mTiles2DArray) {
            allTilesList.addAll(Arrays.asList(tilesArray));
        }
        return allTilesList;
    }

    public int getTileCount() {
        int tileCount = 0;
        for (Tile[] tileArray : mTiles2DArray) {
            tileCount += tileArray.length;
        }
        return tileCount;
    }

    public void addTile(Tile tile) {
        tile.setRoomName(mRoomName);
        mTiles2DArray[tile.getXCoordinate()][tile.getYCoordinate()] = tile;
    }

}
