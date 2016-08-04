package com.voyager.chase.utility;

import com.voyager.chase.game.entity.Room;

/**
 * Created by miguellysanchez on 7/25/16.
 */
public class TileUtility {

    public static boolean isWithinRange(int sourceX, int sourceY, int targetX, int targetY, int rangeX, int rangeY) {
        return isWithinRangeX(sourceX, targetX, rangeX) &&
                isWithinRangeY(sourceY, targetY, rangeY);
    }

    private static boolean isWithinRangeX(int sourceX, int targetX, int rangeX) {
        return targetX >= sourceX - rangeX && targetX <= sourceX + rangeX;
    }


    private static boolean isWithinRangeY(int sourceY, int targetY, int rangeY) {
        return targetY >= sourceY - rangeY && targetY <= sourceY + rangeY;
    }

    public static boolean isWithinRoom(int tileX, int tileY) {
        return tileX >= 0 && tileX < Room.ROOM_WIDTH && tileY >= 0 && tileY < Room.ROOM_HEIGHT;
    }
}
