package com.voyager.chase.game.executors;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.voyager.chase.game.LevelRenderer;
import com.voyager.chase.game.World;
import com.voyager.chase.game.entity.player.Player;
import com.voyager.chase.game.entity.player.Sentry;
import com.voyager.chase.game.entity.player.Spy;
import com.voyager.chase.game.skill.Skill;
import com.voyager.chase.utility.PreferenceUtility;

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
    }

    public void startTurnOrchestrator() {
        if (mCurrentState == TurnState.NULL_STATE) {
            if (Player.SPY_ROLE.equals(mGameRole)) {
                mCurrentState = TurnState.START_STATE;
            } else {
                mCurrentState = TurnState.WAITING_STATE;
            }

            advanceTurn();
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

    public void setTurnState(TurnState turnState) {
        mCurrentState = turnState;
    }

    public void advanceTurn() {
        switch (mCurrentState) {
            case START_STATE:
                if (!mCurrentPlayer.isTurnSkipped()) {
                    mCurrentPlayer.setIsTurnSkipped(false);
                } else {
                    for (Skill skill : mCurrentPlayer.getSkills()) {
                        skill.reduceCooldown();
                    }
                    mCurrentPlayer.recoverActionPoints();
                }
                setTurnState(TurnState.UPKEEP_STATE);
                advanceTurn();
                break;
            case UPKEEP_STATE:
                if (mCurrentPlayer.getActionPoints() <= 0) {
                    setTurnState(TurnState.END_STATE);
                } else {
                    mCurrentPlayer.setActionPoints(mCurrentPlayer.getActionPoints() - 1);
                    setTurnState(TurnState.SELECT_SKILL_STATE);
                }
                advanceTurn();
                break;
            case SELECT_SKILL_STATE:
                broadcastTurnState();
            case END_STATE:
                break;

        }

    }

    public void broadcastTurnState(){
        Intent turnStateIntent = new Intent(TURN_STATE_INTENT_ACTION);
        turnStateIntent.putExtra(KEY_STRING_EXTRA_TURN_STATE, mCurrentState);
        mBroadcastManager.sendBroadcast(turnStateIntent);
    }

}
