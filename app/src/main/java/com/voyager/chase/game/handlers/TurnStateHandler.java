package com.voyager.chase.game.handlers;


import com.voyager.chase.game.event.TurnStateEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by miguellysanchez on 7/23/16.
 */
public abstract class TurnStateHandler {

    public abstract void handleTurnStateEvent(TurnStateEvent event);

    protected void post(Object event){
        EventBus.getDefault().post(event);
    }

}
