package com.voyager.chase.game;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.voyager.chase.game.entity.Room;
import com.voyager.chase.game.entity.Tile;
import com.voyager.chase.game.entity.player.Player;
import com.voyager.chase.game.worldeffect.WorldEffect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by miguellysanchez on 7/4/16.
 */
public class World {
    private static World sWorld;
    private Player mUserPlayer;
    private HashMap<String, Room> mRoomsMap;

    private Multimap<String, Tile> mWorldItemsLocationMap;
    private Queue<WorldEffect> mWorldEffectQueue;

    private World() {
        mRoomsMap = new HashMap<>();
        mWorldEffectQueue = new LinkedList<>();
        mWorldItemsLocationMap = ArrayListMultimap.create();
    }

    public static World getInstance() {
        if (sWorld == null) {
            //TODO auto generating world return sampleWorld()
            return createInstance();
        }
        return sWorld;
    }

    public Room getRoom(String name) {
        return mRoomsMap.get(name);
    }

    public void addRoom(String name, Room room) {
        mRoomsMap.put(name, room);
    }

    public static World createWorld() {
        sWorld = new World();
        //TODO add parse for world template based on JSON
        return sWorld;
    }

    public static World createInstance() {
        sWorld = new World();
        String[] roomNames = new String[]{"A", "B", "C", "D", "E", "F"};

        for (String roomName : roomNames) {
            ArrayList<Tile> tilesArrayList = new ArrayList<>();
            for (int x = 0; x < Room.ROOM_WIDTH; x++) {
                for (int y = 0; y < Room.ROOM_HEIGHT; y++) {
                    Tile tile = new Tile(x, y);
                    tilesArrayList.add(tile);
                }
            }
            Room room = Room.createRoom(roomName, tilesArrayList);
            sWorld.addRoom(roomName, room);
        }
        return sWorld;
    }

    public void addWorldEffectToQueue(WorldEffect worldEffect) {
        mWorldEffectQueue.add(worldEffect);
    }

    public WorldEffect dequeueFromWorldEffectQueue() {
        return mWorldEffectQueue.remove();
    }

    public boolean isWorldEffectQueueEmpty(){
        return mWorldEffectQueue.isEmpty();
    }

    public static Player getUserPlayer(){
        return getInstance().mUserPlayer;
    }

    public static void setUserPlayer(Player player){
        getInstance().mUserPlayer = player;
    }

    public Collection<Tile> removeAllWorldItemLocations(String uuidString){
        return mWorldItemsLocationMap.removeAll(uuidString);
    }

    public void addWorldItemLocation(String uuidString, Tile tile){
        mWorldItemsLocationMap.put(uuidString, tile);
    }
}
