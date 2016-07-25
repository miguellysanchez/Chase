package com.voyager.chase.game;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.voyager.chase.game.entity.Renderable;
import com.voyager.chase.game.entity.Room;
import com.voyager.chase.game.entity.SkillItem;
import com.voyager.chase.game.entity.Tile;
import com.voyager.chase.game.entity.player.Player;
import com.voyager.chase.game.entity.player.Sentry;
import com.voyager.chase.game.entity.player.Spy;
import com.voyager.chase.game.views.TileView;
import com.voyager.chase.utility.BenchmarkUtility;
import com.voyager.chase.utility.PreferenceUtility;
import com.voyager.chase.utility.TileUtility;

import java.util.ArrayList;

import timber.log.Timber;

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

    public void render(World world) {
        Player player = null;
        if (Player.SENTRY_ROLE.equals(mGameRole)) {
            player = Sentry.getInstance();
        } else if (Player.SPY_ROLE.equals(mGameRole)) {
            player = Spy.getInstance();
        } else {
            throw new IllegalStateException("Should always have an assigned game role");
        }
        BenchmarkUtility.startOrStopBenchmarkTimer("keso");
        Room currentRoom = world.getRoom(player.getCurrentRoomName());
        Tile[][] tiles2DArray = currentRoom.getAllTiles2DArray();
        for (int x = 0; x < Room.ROOM_WIDTH; x++) {
            for (int y = 0; y < Room.ROOM_HEIGHT; y++) {
                TileView tileView = mTileViewsArray[x][y];
                Tile tile = tiles2DArray[x][y];
                renderTileToView(tile, tileView, player);
            }
        }
        BenchmarkUtility.startOrStopBenchmarkTimer("keso");
    }

    public void renderTileToView(Tile tile, TileView tileView, Player userPlayer) {
        boolean willRenderFog = true;
        if (TileUtility.isWithinRange(userPlayer.getCurrentTileXCoordinate(), userPlayer.getCurrentTileYCoordinate(), tile.getXCoordinate(), tile.getYCoordinate(), 2, 2)) {
            willRenderFog = false;
        }

        if (tile.getPlayer() == null) {
            tileView.getImageViewPlayer().setVisibility(View.GONE);
        } else {
            if (shouldRender(tile.getPlayer(), tile, userPlayer)) {
                tileView.getImageViewPlayer().setVisibility(View.VISIBLE);
                tileView.getImageViewPlayer().setImageResource(tile.getPlayer().getRenderDrawableId());
                willRenderFog = false;
            }
        }

        ArrayList<SkillItem> skillItemsList = tile.getAllSkillItemsList();
        if (!skillItemsList.isEmpty()) {
            ArrayList<Drawable> layersList = new ArrayList<>();
            for (int i = 0; i < skillItemsList.size(); i++) {
                SkillItem skillItem = skillItemsList.get(i);
                if (shouldRender(skillItem, tile, userPlayer)) {
                    layersList.add(ContextCompat.getDrawable(mContext, skillItem.getRenderDrawableId()));
                }
            }
            if (!layersList.isEmpty()) {
                Drawable[] layersArray = new Drawable[layersList.size()];
                LayerDrawable layerDrawable = new LayerDrawable(layersList.toArray(layersArray));
                tileView.getImageViewSkillItems().setImageDrawable(layerDrawable);
                willRenderFog = false;
            } else {
                tileView.getImageViewSkillItems().setImageResource(android.R.color.transparent);
            }
        } else {
            tileView.getImageViewSkillItems().setImageResource(android.R.color.transparent);
        }

        tileView.getViewFog().setVisibility(willRenderFog ? View.VISIBLE : View.GONE);
    }

    private boolean shouldRender(Renderable renderable, Tile tile, Player player) {
        if (tile.getVisibilityModifierList().contains(Tile.GLOBAL_VISIBILITY)) {
            return true;
        }

        int renderableVisibility = Renderable.HIDDEN;
        if (Player.SPY_ROLE.equals(player.getIdentity())) {
            if (tile.getVisibilityModifierList().contains(Tile.SPY_ONLY_GLOBAL_VISIBILITY)) {
                return true;
            }
            renderableVisibility = renderable.getSpyVisibility();
        } else if (Player.SENTRY_ROLE.equals(player.getIdentity())) {
            if (tile.getVisibilityModifierList().contains(Tile.SENTRY_ONLY_GLOBAL_VISIBILITY)) {
                return true;
            }
            renderableVisibility = renderable.getSentryVisibility();
        }
        switch (renderableVisibility) {
            case Renderable.HIDDEN:
                return false;
            case Renderable.PROXIMITY_VISIBLE:
                if (TileUtility.isWithinRange(player.getCurrentTileXCoordinate(), player.getCurrentTileYCoordinate(), tile.getXCoordinate(), tile.getYCoordinate(), 2, 2)) {
                    return true;
                }
                break;
            case Renderable.GLOBALLY_VISIBLE:
                return true;
        }
        return false;
    }
}
