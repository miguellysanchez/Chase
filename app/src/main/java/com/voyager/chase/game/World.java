package com.voyager.chase.game;

import com.google.common.collect.Multimap;
import com.voyager.chase.game.entity.ItemTrigger;
import com.voyager.chase.game.entity.Renderable;
import com.voyager.chase.game.entity.Room;
import com.voyager.chase.game.entity.Tile;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by miguellysanchez on 7/4/16.
 */
public class World {
    private static World sWorld;
    private HashMap<String, Room> mRoomsMap;

    private Multimap<String, Tile> mWorldItemsMap;

    private World(){
        mRoomsMap = new HashMap<>();
    }

    public static World getInstance(){
        if(sWorld == null){
            return sampleCreateWorld();
        }
        return sWorld;
    }

    public Room getRoom(String name){
        return mRoomsMap.get(name);
    }

    public void addRoom(String name, Room room){
        mRoomsMap.put(name, room);
    }

    public void removeRenderable(String renderableId){

    }

    public static World createWorld(){
        sWorld = new World();
        //TODO add parse for world template based on JSON
        return sWorld;
    }

    public static World sampleCreateWorld(){
        sWorld = new World();
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
            sWorld.addRoom(roomName, room);
        }
        return sWorld;
    }
}
