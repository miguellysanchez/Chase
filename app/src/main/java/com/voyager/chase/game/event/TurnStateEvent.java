package com.voyager.chase.game.event;

import com.voyager.chase.game.TurnState;
import com.voyager.chase.game.entity.Tile;
import com.voyager.chase.game.entity.player.Player;
import com.voyager.chase.game.mods.WorldEffect;
import com.voyager.chase.game.skill.Skill;

/**
 * Created by miguellysanchez on 7/23/16.
 */
public class TurnStateEvent {

    private TurnState mTargetState;
    private String action;
    private Player player;
    private Skill selectedSkill;
    private Tile targetTile;
    private WorldEffect worldEffect;

    public TurnStateEvent() {
    }

    //
    public TurnState getTargetState() {
        return mTargetState;
    }

    public void setTargetState(TurnState targetState) {
        this.mTargetState = targetState;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Skill getSelectedSkill() {
        return selectedSkill;
    }

    public void setSelectedSkill(Skill selectedSkill) {
        this.selectedSkill = selectedSkill;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Tile getTargetTile() {
        return targetTile;
    }

    public void setTargetTile(Tile targetTile) {
        this.targetTile = targetTile;
    }

    public WorldEffect getWorldEffect() {
        return worldEffect;
    }
    public void setWorldEffect(WorldEffect worldEffect) {
        this.worldEffect = worldEffect;
    }

}