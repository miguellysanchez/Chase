package com.voyager.chase.game;

import com.voyager.chase.game.entity.Tile;
import com.voyager.chase.game.entity.construct.ObjectiveConstruct;
import com.voyager.chase.game.entity.construct.TeleporterEntryConstruct;
import com.voyager.chase.game.entity.construct.WallConstruct;
import com.voyager.chase.game.entity.player.Sentry;
import com.voyager.chase.game.entity.player.Spy;
import com.voyager.chase.utility.TileUtility;

/**
 * Created by miguellysanchez on 8/6/16.
 */
public class DemoInitialization {
    public static void initializeSampleGameState() {
        initializeTeleporters();
        initializeObjectives();
        initializeWalls();
        initializePlayers();
    }

    private static void initializeTeleporters() {
        generateTeleporter("A", 3, 0, "B", 7, 8);
        generateTeleporter("B", 3, 4, "A", 9, 8);
    }


    private static void initializeObjectives() {
        generateObjective("A", 0, 8);
    }

    private static void initializeWalls() {
        generateWall("A", 3, 7);
    }

    private static void initializePlayers() {
        Spy spy = Spy.getInstance();
        Sentry sentry = Sentry.getInstance();
        World.getInstance().getRoom("A").getTileAtCoordinates(2, 3).setPlayer(spy);
        World.getInstance().getRoom("A").getTileAtCoordinates(3, 3).setPlayer(sentry);
    }

    private static void generateTeleporter(String sourceRoom, int sourceX, int sourceY, String targetRoom, int targetX, int targetY) {
        World world = World.getInstance();
        if (checkIfIncorrectTileLocation(sourceRoom, sourceX, sourceY) && checkIfIncorrectTileLocation(targetRoom, targetX, targetY)) {
            throw new IllegalArgumentException("Unable to generate teleporters with invalid values!!");
        }
        Tile sourceTile = world.getRoom(sourceRoom).getTileAtCoordinates(sourceX, sourceY);
        Tile targetTile = world.getRoom(targetRoom).getTileAtCoordinates(targetX, targetY);
        if (sourceTile.containsConstruct() || targetTile.containsConstruct()) {
            throw new IllegalArgumentException("Unable to generate teleporters. Source and target tile must not have any constructs in them");
        }

        TeleporterEntryConstruct teleporterEntryConstruct = new TeleporterEntryConstruct(
                "TELEPORTER-" + sourceRoom + "-" + sourceX + "," + sourceY + ">" + targetRoom + "-" + targetX + "," + targetY,
                targetRoom, targetX, targetY);
        sourceTile.addConstruct(teleporterEntryConstruct);
        world.addWorldItemLocation(teleporterEntryConstruct.getId(), sourceTile);
    }


    private static void generateObjective(String roomName, int x, int y) {
        if (checkIfIncorrectTileLocation(roomName, x, y)) {
            throw new IllegalArgumentException("Unable to generate objectives with invalid values!!");
        }
        World world = World.getInstance();
        Tile tile = World.getInstance().getRoom(roomName).getTileAtCoordinates(x, y);
        if (tile.containsConstruct()) {
            throw new IllegalArgumentException("Unable to generate objective. Tile must not have any constructs in them");
        }

        ObjectiveConstruct objectiveConstruct = new ObjectiveConstruct("OBJECTIVE-" + roomName + "-" + x + "," + "y");
        tile.addConstruct(objectiveConstruct);
        world.addWorldItemLocation(objectiveConstruct.getId(), tile);
    }

    private static void generateWall(String roomName, int x, int y) {
        if (checkIfIncorrectTileLocation(roomName, x, y)) {
            throw new IllegalArgumentException("Unable to generate wall with invalid values!!");
        }
        World world = World.getInstance();
        Tile tile = World.getInstance().getRoom(roomName).getTileAtCoordinates(x, y);
        if (tile.containsConstruct()) {
            throw new IllegalArgumentException("Unable to generate wall. Tile must not have any constructs in them");
        }

        WallConstruct wallConstruct = new WallConstruct("WALL-" + roomName + "-" + x + "," + "y");
        tile.addConstruct(wallConstruct);
        world.addWorldItemLocation(wallConstruct.getId(), tile);
    }

    private static boolean checkIfIncorrectTileLocation(String room, int x, int y) {
        boolean isRoomValid = World.getInstance().getRoom(room) != null;
        boolean areCoordinatesCorrect = TileUtility.isWithinRoom(x,y);
        return !isRoomValid || !areCoordinatesCorrect;
    }
}
