package com.voyager.chase.game.entity.construct;

import android.content.Context;

import com.voyager.chase.R;
import com.voyager.chase.game.entity.player.Player;
import com.voyager.chase.game.entity.player.Sentry;
import com.voyager.chase.game.entity.player.Spy;
import com.voyager.chase.game.skill.sentry.RoomIlluminationBeaconSkill;
import com.voyager.chase.game.skill.sentry.SurveillanceBugSkill;
import com.voyager.chase.game.skill.spy.PlantDecoySkill;

/**
 * Created by miguellysanchez on 7/29/16.
 */
public class ConstructsPool {
    public static final String ALARM_TRAP_CONSTRUCT = "alarm_trap_construct";
    public static final String STUN_NET_CONSTRUCT = "stun_net_construct";
    public static final String MINE_CONSTRUCT = "mine_construct";


    public static Construct getConstruct(Context context, String constructName, String ownerRole, String constructUUID) {
        Construct construct = null;
        final String[] sentryConstructNames = context.getResources().getStringArray(R.array.chase_array_skill_select_name_sentry);
        final String[] spyConstructNames = context.getResources().getStringArray(R.array.chase_array_skill_select_name_spy);

        Player owner;
        if (ownerRole.equals(Spy.getInstance().getRole())) {
            owner = Spy.getInstance();
        } else if (ownerRole.equals(Sentry.getInstance().getRole())) {
            owner = Sentry.getInstance();
        } else {
            throw new IllegalStateException("Cannot assign null player to construct object");
        }

        if (constructName.equals(sentryConstructNames[0])) {
//            construct = new AlarmTrapConstruct();
        } else if (constructName.equals(sentryConstructNames[2])) {
//            construct = new StunNetConstruct();
        } else if (constructName.equals(sentryConstructNames[3])) {
//            construct = new MotionSensorConstruct();
        } else if (constructName.equals(sentryConstructNames[4])) {
//            construct = new RoomIlluminationBeaconConstruct();
        } else if (constructName.equals(sentryConstructNames[6])) {
//                construct = new SurveillanceBugConstruct();
        } else if (constructName.equals(spyConstructNames[1])) {
            construct = new MineConstruct();
        } else if (constructName.equals(spyConstructNames[2])) {
            construct = new MineConstruct();
        } else if (constructName.equals(spyConstructNames[3])) {
            construct = new MineConstruct();
        } else if (constructName.equals(spyConstructNames[4])) {
//            construct = new DecoyConstruct();
        } else if (constructName.equals(spyConstructNames[6])) {
//            construct = new BarrierConstruct();
        } else if (constructName.equals(spyConstructNames[7])) {
//            construct = new IlluminationBeaconConstruct();
        }

        if (construct != null) {
            construct.setUUID(constructUUID);
            construct.setOwner(owner);
        }
        return construct;

    }
}
