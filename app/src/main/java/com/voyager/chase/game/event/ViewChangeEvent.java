package com.voyager.chase.game.event;

import com.voyager.chase.game.entity.Tile;

import java.util.ArrayList;

/**
 * Created by miguellysanchez on 7/26/16.
 */
public class ViewChangeEvent {
    public static final String RENDER_WORLD = "render_world";
    public static final String START_GAME = "start_game";
    public static final String END_TURN_CONFIRMATION = "end_turn_confirmation";
    public static final String UPDATE_PLAYER_STATE = "update_player_state";
    public static final String SHOW_SKILL_SELECTION = "show_skill_selection";
    public static final String PROCESSING = "processing";
    public static final String UPDATE_SKILLS_LIST = "updates_skills_list";
    public static final String SHOW_CANCEL_SKILL = "show_cancel_skill";
    public static final String SHOW_TARGET_HIGHLIGHTS = "show_target_highlights";
    public static final String HIDE_TARGET_HIGHLIGHTS = "hide_target_highlights";
    public static final String TOAST_NO_VALID_TARGETS = "toast_no_valid_targets'";
    public static final String WAITING_FOR_OTHER_PLAYER = "waiting_for_other_player";
    public static final String GAME_INFO_UPDATE = "game_info_update";

    private ArrayList<String> viewChangeTypeList;
    private ArrayList<Tile> tileArrayList;
    private String gameInfoUpdate;

    public ViewChangeEvent() {
        viewChangeTypeList = new ArrayList<>();
    }

    public ArrayList<String> getViewChanges() {
        return viewChangeTypeList;
    }

    public void addViewChangeType(String viewChangeType) {
        viewChangeTypeList.add(viewChangeType);
    }

    public ArrayList<Tile> getTileArrayList() {
        return tileArrayList;
    }

    public void setTileArrayList(ArrayList<Tile> tileArrayList) {
        this.tileArrayList = tileArrayList;
    }

    public String getGameInfoUpdate() {
        return gameInfoUpdate;
    }

    public void setGameInfoUpdate(String gameInfoUpdate) {
        this.gameInfoUpdate = gameInfoUpdate;
    }
}
