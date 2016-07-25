package com.voyager.chase.game;

import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.voyager.chase.game.entity.player.Player;
import com.voyager.chase.game.entity.player.Sentry;
import com.voyager.chase.game.entity.player.Spy;
import com.voyager.chase.game.handlers.StartStateHandler;
import com.voyager.chase.game.handlers.TurnStateHandler;
import com.voyager.chase.game.handlers.UpkeepStateHandler;
import com.voyager.chase.utility.PreferenceUtility;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import timber.log.Timber;

/**
 * Created by miguellysanchez on 7/7/16.
 */
public class TurnOrchestrator {

    private TurnState mCurrentState = TurnState.NULL_STATE;
    private Player mCurrentPlayer;
    private Spy mSpy;
    private Sentry mSentry;
    private String mGameRole;
    private LocalBroadcastManager mBroadcastManager;
    private Queue<Runnable> mRunnableQueue;
    private HashMap<TurnState, TurnStateHandler> mTurnStateHandlerList;


    private StartStateHandler mStartStateHandler;
    private UpkeepStateHandler mUpkeepStateHandler;


    public static final String TURN_STATE_INTENT_ACTION = "com.voyager.chase.turn_state";
    public static final String KEY_STRING_EXTRA_TURN_STATE = "key_turn_state";
    public static final IntentFilter TURN_STATE_FILTER = new IntentFilter(TURN_STATE_INTENT_ACTION);
    private World mWorld;

    private TurnOrchestrator() {
    }

    public TurnOrchestrator(Context context, World world, Spy spy, Sentry sentry, LevelRenderer levelRenderer) {
        mWorld = world;
        mSpy = spy;
        mSentry = sentry;
        mGameRole = PreferenceUtility.getInstance(context).getGameRole();
        if (Player.SENTRY_ROLE.equals(mGameRole)) {
            mCurrentPlayer = mSentry;
        } else if (Player.SPY_ROLE.equals(mGameRole)) {
            mCurrentPlayer = mSpy;
        } else {
            throw new IllegalStateException("Should not enter this part.");
        }
        mRunnableQueue = new LinkedList<>();
        mBroadcastManager = LocalBroadcastManager.getInstance(context);
        mTurnStateHandlerList = new HashMap<>();
    }

    public void startTurnOrchestrator() {
        if (mCurrentState == TurnState.NULL_STATE) {
            if (Player.SPY_ROLE.equals(mGameRole)) {
                mCurrentState = TurnState.START_STATE;
            } else {
                mCurrentState = TurnState.WAITING_STATE;
            }

        } else {
            Timber.e("Illegal State, cannot start orchestrator when already out of null state");
        }
    }

    public World getWorld() {
        return mWorld;
    }

    public enum TurnState {
        NULL_STATE, //Initial state, neither players turn has started, setup phase
        //States for when user is the active player
        START_STATE, //refill current players AP
        UPKEEP_STATE,
        SELECT_SKILL_STATE,
        TARGET_SELECTION_STATE,
        TRIGGER_UPDATE_WORLD_STATE,
        RELAY_WORLD_UPDATE_STATE,
        RENDER_WORLD_STATE,
        CHECK_WORLD_UPDATES_STATE,
        END_STATE,
        //State

        WAITING_STATE,
        WAITING_RENDER_STATE
    }


}
