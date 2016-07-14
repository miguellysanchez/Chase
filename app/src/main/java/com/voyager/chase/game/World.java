package com.voyager.chase.game;

import com.voyager.chase.game.entity.Room;
import com.voyager.chase.game.entity.Tile;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by miguellysanchez on 7/4/16.
 */
public class World {
    private HashMap<String, Room> mRoomsMap;

    public World(){
        mRoomsMap = new HashMap<>();
    }

    public Room getRoom(String name){
        return mRoomsMap.get(name);
    }

    public void addRoom(String name, Room room){
        mRoomsMap.put(name, room);
    }

    public static World createWorld(){
        //TODO add parse for world template based on JSON
        World world = new World();
        return world;
    }

    public static World sampleCreateWorld(){
        World world = new World();
        String[] roomNames = new String[]{"A", "B", "C", "D", "E", "F"};

        for(String roomName : roomNames){
            ArrayList<Tile> tilesArrayList = new ArrayList<>();
            for(int x=0; x<Room.ROOM_WIDTH; x++){
                for(int y=0; y<Room.ROOM_HEIGHT; y++) {
                    Tile tile = new Tile(x, y);
                    tilesArrayList.add(tile);
                }
            }
            Room room = Room.createRoom(roomName, tilesArrayList);
            world.addRoom(roomName, room);
        }
        return world;
    }
}
