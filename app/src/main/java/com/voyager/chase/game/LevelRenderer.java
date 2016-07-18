package com.voyager.chase.game;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.voyager.chase.game.entity.Room;
import com.voyager.chase.game.entity.Tile;
import com.voyager.chase.game.entity.player.Player;
import com.voyager.chase.game.views.TileView;

/**
 * Created by miguellysanchez on 7/4/16.
 */
public class LevelRenderer {
    TileView[][] mTileViewsArray;

    Context mContext;
    LinearLayout mLinearLayoutLevel;
    Player mCurrentPlayer;

    private LevelRenderer() {
    }

    public LevelRenderer(LinearLayout levelLayout, Player currentPlayer) {
        mLinearLayoutLevel = levelLayout;
        mContext = mLinearLayoutLevel.getContext();
        mCurrentPlayer = currentPlayer;

        mTileViewsArray = new TileView[Room.ROOM_WIDTH][Room.ROOM_HEIGHT];
        for (int i = 0; i < Room.ROOM_WIDTH; i++) {
            LinearLayout columnLayout = new LinearLayout(mContext);
            columnLayout.setOrientation(LinearLayout.VERTICAL);
            for (int j = 0; j < Room.ROOM_HEIGHT; j++) {
                TileView tileView = new TileView(mContext);
                LinearLayout.LayoutParams tileViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                tileViewParams.weight = 1;
                columnLayout.addView(tileView, tileViewParams);
                mTileViewsArray[i][j] = tileView;
            }
            LinearLayout.LayoutParams columnLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            columnLayoutParams.weight = 1;
            mLinearLayoutLevel.addView(columnLayout, columnLayoutParams);
        }
    }

    public void render(World world) {
        Room currentRoom = world.getRoom(mCurrentPlayer.getCurrentRoomName());
        Tile[][] tiles2DArray = currentRoom.getAllTiles2DArray();
        for(int x=0; x<Room.ROOM_WIDTH; x++){
            for(int y=0; y<Room.ROOM_HEIGHT; y++){
                TileView tileView = mTileViewsArray[x][y];
                Tile tile = tiles2DArray[x][y];
                renderTileToView(tile, tileView);
            }
        }
    }

    public void renderTileToView(Tile tile, TileView tileView){
        if(tile.getPlayer() == null){
            tileView.getImageViewPlayer().setVisibility(View.INVISIBLE);
        } else {

        }
    }
}
