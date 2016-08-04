package com.voyager.chase.game.gameinfo;

/**
 * Created by miguellysanchez on 8/4/16.
 */
public class GameInfo {

    private String senderRole;
    private long timestamp;
    private String info;

    public String getSenderRole() {
        return senderRole;
    }

    public void setSenderRole(String senderRole) {
        this.senderRole = senderRole;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
