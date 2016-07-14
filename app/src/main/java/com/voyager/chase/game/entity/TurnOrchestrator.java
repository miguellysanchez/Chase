package com.voyager.chase.game.entity;

import com.voyager.chase.game.GameSession;
import com.voyager.chase.game.entity.player.Player;

/**
 * Created by miguellysanchez on 7/7/16.
 */
public class TurnOrchestrator {

    private TurnState mCurrentState = TurnState.START_STATE;
    private Player mCurrentPlayer;
    private GameSession mGameSession;

    public enum TurnState {
        //States for when user is the active player
        START_STATE, //refill current players AP
        CHECK_AP_STATE,
        SELECT_SKILL_STATE,
        TARGET_SELECTION_STATE,
        TRIGGER_UPDATE_WORLD_STATE,
        RELAY_WORLD_UPDATE_STATE,
        RENDER_WORLD_STATE,
        CHECK_WORLD_UPDATES_STATE,
        END_STATE

        //State
    }

    public void advanceTurn(){

    }


}
