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
        generateTeleporter("A", 1, 0, "A", 7, 8);
        generateTeleporter("A", 1, 4, "C", 4, 0);
        generateTeleporter("A", 1, 5, "D", 9, 9);
        generateTeleporter("A", 8, 8, "D", 2, 1);
        generateTeleporter("A", 9, 0, "E", 0, 0);
        
        generateTeleporter("B", 2, 3, "E", 8, 9);
        generateTeleporter("B", 4, 9, "A", 2, 0);
        generateTeleporter("B", 5, 9, "B", 1, 2);
//        generateTeleporter("B", 6, 1, "F", 9, 8);
        generateTeleporter("B", 7, 1, "B", 5, 8);
        
//        generateTeleporter("C", 1, 6, "F", 9, 1);
        generateTeleporter("C", 4, 9, "A", 2, 4);
        generateTeleporter("C", 5, 0, "C", 9, 6);
        generateTeleporter("C", 9, 4, "E", 1, 8);
        
        generateTeleporter("D", 3, 2, "A", 9, 9);
//        generateTeleporter("D", 4, 7, "F", 9, 4);
        generateTeleporter("D", 9, 0, "D", 0, 9);
        
        generateTeleporter("E", 2, 1, "D", 8, 1);
        generateTeleporter("E", 7, 9, "B", 1, 3);
        
//        generateTeleporter("F", 2, 4, "E", 8, 1);
//        generateTeleporter("F", 8, 2, "C", 0, 4);
//        generateTeleporter("F", 8, 5, "D", 3, 9);
    }

    private static void initializeObjectives() {
        //TODO SAMPLE
        generateObjective("B", 9, 7);
        generateObjective("C", 4, 4);
        generateObjective("D", 0, 0);
        generateObjective("E", 0, 5);
    }

    private static void initializeWalls() {
        generateWall("A", 1, 2);
        generateWall("A", 2, 5);
        generateWall("A", 2, 6);
        generateWall("A", 2, 8);
        generateWall("A", 4, 3);
        generateWall("A", 4, 9);
        generateWall("A", 5, 3);
        generateWall("A", 5, 6);
        generateWall("A", 6, 1);
        generateWall("A", 6, 5);
        generateWall("A", 7, 2);
        generateWall("A", 7, 3);
        generateWall("A", 8, 6);
        generateWall("A", 9, 3);
        generateWall("A", 9, 6);
        
        generateWall("B", 1, 5);
        generateWall("B", 1, 6);
        generateWall("B", 1, 7);
        generateWall("B", 2, 2);
        generateWall("B", 2, 7);
        generateWall("B", 3, 1);
        generateWall("B", 4, 3);
        generateWall("B", 4, 5);
        generateWall("B", 5, 5);
        generateWall("B", 5, 7);
        generateWall("B", 6, 3);
        generateWall("B", 6, 4);
        generateWall("B", 6, 5);
        generateWall("B", 7, 3);
        generateWall("B", 8, 7);
        generateWall("B", 9, 5);
        
        generateWall("C", 0, 5);
        generateWall("C", 1, 1);
        generateWall("C", 1, 3);
        generateWall("C", 2, 1);
        generateWall("C", 2, 8);
        generateWall("C", 3, 3);
        generateWall("C", 3, 4);
        generateWall("C", 3, 6);
        generateWall("C", 3, 7);
        generateWall("C", 5, 2);
        generateWall("C", 5, 3);
        generateWall("C", 5, 6);
        generateWall("C", 7, 1);
        generateWall("C", 7, 2);
        generateWall("C", 7, 4);
        generateWall("C", 7, 6);
        generateWall("C", 7, 7);
        generateWall("C", 7, 8);
        generateWall("C", 8, 1);
        generateWall("C", 8, 4);
        
        generateWall("D", 0, 3);
        generateWall("D", 1, 0);
        generateWall("D", 1, 7);
        generateWall("D", 2, 5);
        generateWall("D", 3, 4);
        generateWall("D", 3, 7);
        generateWall("D", 4, 0);
        generateWall("D", 4, 4);
        generateWall("D", 5, 8);
        generateWall("D", 6, 2);
        generateWall("D", 6, 4);
        generateWall("D", 6, 7);
        generateWall("D", 7, 3);
        generateWall("D", 7, 6);
        generateWall("D", 8, 5);
        generateWall("D", 8, 8);

        generateWall("E", 1, 2);
        generateWall("E", 1, 4);
        generateWall("E", 2, 5);
        generateWall("E", 3, 4);
        generateWall("E", 3, 8);
        generateWall("E", 4, 0);
        generateWall("E", 5, 2);
        generateWall("E", 5, 3);
        generateWall("E", 5, 7);
        generateWall("E", 5, 8);
        generateWall("E", 5, 9);
        generateWall("E", 6, 3);
        generateWall("E", 6, 6);
        generateWall("E", 7, 1);
        generateWall("E", 8, 5);
        generateWall("E", 9, 3);

//        generateWall("F", 0, 3);
//        generateWall("F", 0, 4);
//        generateWall("F", 1, 7);
//        generateWall("F", 2, 2);
//        generateWall("F", 2, 9);
//        generateWall("F", 3, 5);
//        generateWall("F", 4, 0);
//        generateWall("F", 4, 7);
//        generateWall("F", 5, 0);
//        generateWall("F", 5, 3);
//        generateWall("F", 5, 7);
//        generateWall("F", 6, 3);
//        generateWall("F", 6, 6);
//        generateWall("F", 6, 7);
//        generateWall("F", 7, 3);
//        generateWall("F", 7, 6);
//        generateWall("F", 7, 7);
    }

    private static void initializePlayers() {
        Spy spy = Spy.getInstance();
        Sentry sentry = Sentry.getInstance();
        World.getInstance().getRoom("A").getTileAtCoordinates(2, 8).setPlayer(spy);
        World.getInstance().getRoom("C").getTileAtCoordinates(4, 1).setPlayer(sentry);
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

        ObjectiveConstruct objectiveConstruct = new ObjectiveConstruct("OBJECTIVE-" + roomName + "-" + x + "," + y);
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

        WallConstruct wallConstruct = new WallConstruct("WALL-" + roomName + "-" + x + "," + y);
        tile.addConstruct(wallConstruct);
        world.addWorldItemLocation(wallConstruct.getId(), tile);
    }

    private static boolean checkIfIncorrectTileLocation(String room, int x, int y) {
        boolean isRoomValid = World.getInstance().getRoom(room) != null;
        boolean areCoordinatesCorrect = TileUtility.isWithinRoom(x,y);
        return !isRoomValid || !areCoordinatesCorrect;
    }
}
