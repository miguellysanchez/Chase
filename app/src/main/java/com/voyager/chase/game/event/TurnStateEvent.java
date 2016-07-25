package com.voyager.chase.game.event;

import com.voyager.chase.game.TurnOrchestrator;

/**
 * Created by miguellysanchez on 7/23/16.
 */
public class TurnStateEvent {

    private TurnOrchestrator.TurnState previousState;
    private TurnOrchestrator.TurnState nextState;


    public TurnOrchestrator.TurnState getPreviousState() {
        return previousState;
    }

    public void setPreviousState(TurnOrchestrator.TurnState previousState) {
        this.previousState = previousState;
    }

    public TurnOrchestrator.TurnState getNextState() {
        return nextState;
    }

    public void setNextState(TurnOrchestrator.TurnState nextState) {
        this.nextState = nextState;
    }
}

