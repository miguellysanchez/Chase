package com.voyager.chase.game;

/**
 * Created by miguellysanchez on 7/27/16.
 */
public enum TurnState {
    NULL_STATE, //Initial state, neither players turn has started, setup phase
    //States for when user is the active player
    START_STATE, //refill current players AP
    UPKEEP_STATE,
    SELECT_SKILL_STATE,
    TARGET_SELECTION_STATE,
    RESOLVE_SKILL_STATE,
    CHECK_QUEUE_STATE,
    SYNC_WORLD_STATE,
    UPDATE_WORLD_STATE,
    RENDER_WORLD_STATE,
    CHECK_TRIGGER_STATE,
    END_STATE,
    //State

    WAITING_STATE,
    WAITING_RENDER_STATE
}
