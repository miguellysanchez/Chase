package com.voyager.chase.game;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.voyager.chase.R;
import com.voyager.chase.game.entity.Renderable;
import com.voyager.chase.game.entity.Room;
import com.voyager.chase.game.entity.construct.Construct;
import com.voyager.chase.game.entity.Tile;
import com.voyager.chase.game.entity.construct.TeleporterEntryConstruct;
import com.voyager.chase.game.entity.player.Player;
import com.voyager.chase.game.entity.player.Sentry;
import com.voyager.chase.game.entity.player.Spy;
import com.voyager.chase.game.event.TurnStateEvent;
import com.voyager.chase.game.handlers.active.TargetSelectionStateHandler;
import com.voyager.chase.game.views.TileView;
import com.voyager.chase.utility.PreferenceUtility;
import com.voyager.chase.utility.TileUtility;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by miguellysanchez on 7/4/16.
 */
public class LevelRenderer {
    private TileView[][] mTileViewsArray;

    private Context mContext;
    private LinearLayout mLinearLayoutLevel;
    private String mGameRole;

    private LevelRenderer() {
    }

    public LevelRenderer(LinearLayout levelLayout) {
        mLinearLayoutLevel = levelLayout;
        mContext = mLinearLayoutLevel.getContext();
        mGameRole = PreferenceUtility.getInstance(mContext).getGameRole();

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

    public void render() {
        Player player;
        if (Player.SENTRY_ROLE.equals(mGameRole)) {
            player = Sentry.getInstance();
        } else if (Player.SPY_ROLE.equals(mGameRole)) {
            player = Spy.getInstance();
        } else {
            throw new IllegalStateException("Should always have an assigned game role");
        }
        Room currentRoom = World.getInstance().getRoom(player.getCurrentRoomName());
        Tile[][] tiles2DArray = currentRoom.getAllTiles2DArray();
        for (int x = 0; x < Room.ROOM_WIDTH; x++) {
            for (int y = 0; y < Room.ROOM_HEIGHT; y++) {
                TileView tileView = mTileViewsArray[x][y];
                Tile tile = tiles2DArray[x][y];
                renderTileToView(tile, tileView, player);
            }
        }
    }

    private void renderTileToView(Tile tile, TileView tileView, Player userPlayer) {
        if (tile.getRoomName().equals(userPlayer.getCurrentRoomName())
                && tile.getXCoordinate() == userPlayer.getCurrentTileX()
                && tile.getYCoordinate() == userPlayer.getCurrentTileY()) {
            tileView.getFrameLayoutPlayerIndicator().setBackgroundColor(ContextCompat.getColor(mContext, R.color.light_yellow));
        } else {
            tileView.getFrameLayoutPlayerIndicator().setBackgroundColor(ContextCompat.getColor(mContext, R.color.lighter_gray));
        }

        boolean willRenderFog = true;
        if (TileUtility.isWithinRange(userPlayer.getCurrentTileX(), userPlayer.getCurrentTileY(), tile.getXCoordinate(), tile.getYCoordinate(), 2, 2)) {
            willRenderFog = false;
        }

        if (tile.getPlayer() == null) {
            tileView.getImageViewPlayer().setVisibility(View.GONE);
        } else {
            if (shouldRender(tile.getPlayer(), tile, userPlayer)) {
                tileView.getImageViewPlayer().setVisibility(View.VISIBLE);
                tileView.getImageViewPlayer().setImageResource(tile.getPlayer().getRenderDrawableId());
                willRenderFog = false;
            } else {
                tileView.getImageViewPlayer().setVisibility(View.GONE);
            }
        }

        ArrayList<Construct> constructsList = tile.getAllConstructsList();
        if (!constructsList.isEmpty()) {
            ArrayList<Drawable> layersList = new ArrayList<>();
            for (int i = 0; i < constructsList.size(); i++) {
                Construct construct = constructsList.get(i);
                if(construct instanceof TeleporterEntryConstruct){
                    TeleporterEntryConstruct teleporterEntryConstruct = (TeleporterEntryConstruct) construct;
                    tileView.getTextViewDestination().setText(teleporterEntryConstruct.getTargetRoom());
                    tileView.getTextViewDestination().setVisibility(View.VISIBLE);
                } else {
                    tileView.getTextViewDestination().setText("");
                    tileView.getTextViewDestination().setVisibility(View.GONE);
                }

                if (shouldRender(construct, tile, userPlayer)) {
                    layersList.add(ContextCompat.getDrawable(mContext, construct.getRenderDrawableId()));
                }
            }
            if (!layersList.isEmpty()) {
                Drawable[] layersArray = new Drawable[layersList.size()];
                LayerDrawable layerDrawable = new LayerDrawable(layersList.toArray(layersArray));
                tileView.getImageViewConstructs().setImageDrawable(layerDrawable);
                willRenderFog = false;
            } else {
                tileView.getImageViewConstructs().setImageResource(android.R.color.transparent);
            }
        } else {
            tileView.getImageViewConstructs().setImageResource(android.R.color.transparent);
            tileView.getTextViewDestination().setText("");
            tileView.getTextViewDestination().setVisibility(View.GONE);
        }

        if (shouldRender(null, tile, userPlayer)) {
            willRenderFog = false;
        }

        tileView.getViewFog().setVisibility(willRenderFog ? View.VISIBLE : View.GONE);
    }

    private boolean shouldRender(Renderable renderable, Tile tile, Player userPlayer) {
//        if(true) {
//            return true;
//        }
        if (tile.getVisibilityModifierList().contains(Tile.GLOBAL_VISIBILITY_MOD)) {
            return true;
        }

        int renderableVisibility = Renderable.HIDDEN;
        if (Player.SPY_ROLE.equals(userPlayer.getRole())) {
            if (tile.getVisibilityModifierList().contains(Tile.SPY_ONLY_GLOBAL_VISIBILITY_MOD)) {
                return true;
            }
            if (renderable != null) {
                renderableVisibility = renderable.getSpyVisibility();
            }
        } else if (Player.SENTRY_ROLE.equals(userPlayer.getRole())) {
            if (tile.getVisibilityModifierList().contains(Tile.SENTRY_ONLY_GLOBAL_VISIBILITY_MOD)) {
                return true;
            }
            if (renderable != null) {
                renderableVisibility = renderable.getSentryVisibility();
            }
        }

        switch (renderableVisibility) {
            case Renderable.HIDDEN:
                return false;
            case Renderable.PROXIMITY_VISIBLE:
                if (TileUtility.isWithinRange(userPlayer.getCurrentTileX(), userPlayer.getCurrentTileY(), tile.getXCoordinate(), tile.getYCoordinate(), 2, 2)) {
                    return true;
                }
                break;
            case Renderable.GLOBALLY_VISIBLE:
                return true;
        }
        return false;
    }

    public void highlightTiles(ArrayList<Tile> tileArrayList) {
        for (final Tile tile : tileArrayList) {
            TileView targetTileView = mTileViewsArray[tile.getXCoordinate()][tile.getYCoordinate()];
            targetTileView.setHighlightBackground(true);
            targetTileView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TurnStateEvent targetTileSelectedEvent = new TurnStateEvent();
                    targetTileSelectedEvent.setAction(TargetSelectionStateHandler.ACTION_TARGETED);
                    targetTileSelectedEvent.setTargetTile(tile);
                    resetTargetHighlights();
                    EventBus.getDefault().post(targetTileSelectedEvent);
                }
            });
        }

        //TODO highlight the corresponding
    }

    public void resetTargetHighlights() {
        for (int x = 0; x < Room.ROOM_WIDTH; x++) {
            for (int y = 0; y < Room.ROOM_HEIGHT; y++) {
                TileView tileView = mTileViewsArray[x][y];
                tileView.setOnClickListener(null);
                tileView.setHighlightBackground(false);
            }
        }
    }
}
