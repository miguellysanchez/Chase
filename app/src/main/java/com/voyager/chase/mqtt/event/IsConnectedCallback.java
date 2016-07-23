package com.voyager.chase.mqtt.event;

/**
 * Created by miguellysanchez on 7/20/16.
 */
public abstract class IsConnectedCallback {
    protected abstract void onIsConnected();

    protected abstract void onIsDisconnected();

    public void executeCallback(boolean isConnected) {
        if (isConnected) {
            onIsConnected();
        } else {
            onIsDisconnected();
        }
    }
}