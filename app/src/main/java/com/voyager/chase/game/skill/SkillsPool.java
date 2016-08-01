package com.voyager.chase.game.skill;

import android.content.Context;

import com.voyager.chase.R;
import com.voyager.chase.game.entity.player.Player;
import com.voyager.chase.game.skill.common.EndTurnSkill;
import com.voyager.chase.game.skill.common.MoveSkill;
import com.voyager.chase.game.skill.common.SprintSkill;
import com.voyager.chase.game.skill.sentry.AlarmTrapSkill;
import com.voyager.chase.game.skill.sentry.AttackSkill;
import com.voyager.chase.game.skill.sentry.FragGrenadeSkill;
import com.voyager.chase.game.skill.sentry.MotionSensorSkill;
import com.voyager.chase.game.skill.sentry.RecoverSkill;
import com.voyager.chase.game.skill.sentry.RoomIlluminationBeaconSkill;
import com.voyager.chase.game.skill.sentry.SearchDroneSkill;
import com.voyager.chase.game.skill.sentry.ShockGrenadeSkill;
import com.voyager.chase.game.skill.sentry.StunMineSkill;
import com.voyager.chase.game.skill.sentry.SurveillanceBugSkill;
import com.voyager.chase.game.skill.spy.BaitSkill;
import com.voyager.chase.game.skill.spy.DeployBarrierSkill;
import com.voyager.chase.game.skill.spy.DropMineSkill;
import com.voyager.chase.game.skill.spy.EMPBlastSkill;
import com.voyager.chase.game.skill.spy.EmergencyHealSkill;
import com.voyager.chase.game.skill.spy.IlluminationBeaconSkill;
import com.voyager.chase.game.skill.spy.PlantDecoySkill;
import com.voyager.chase.game.skill.spy.SabotageSkill;
import com.voyager.chase.game.skill.spy.ShrapnelBlastSkill;
import com.voyager.chase.game.skill.spy.SnipeSkill;
import com.voyager.chase.game.skill.spy.ToxicCloudSkill;

/**
 * Created by miguellysanchez on 7/14/16.
 */
public class SkillsPool {

    public static final String YOUR_SKILLS_SELECTED = "key_string_arraylist_your_skills_selected";

    public static final String MOVE_SKILL_NAME = "Move";
    public static final String SABOTAGE_SKILL_NAME = "Sabotage";
    public static final String ATTACK_SKILL_NAME = "Attack";
    public static final String END_TURN_SKILL_NAME = "End Turn";

    public static Skill getSkillForName(Context context, String skillName, Player player) {
        String[] spyRoleSkillNames = context.getResources().getStringArray(R.array.chase_array_skill_select_name_spy);
        String[] sentryRoleSkillNames = context.getResources().getStringArray(R.array.chase_array_skill_select_name_sentry);

        Skill returnedSkill = null;

        int skillCooldown = 0;
        String description = "";
        if (MOVE_SKILL_NAME.equals(skillName)) {
            returnedSkill = new MoveSkill();
            description = "Move from current position to a tile to the left, right, above, or below";
        } else if (END_TURN_SKILL_NAME.equals(skillName)) {
            returnedSkill = new EndTurnSkill();
            description = "Reduces AP to 0. Ends the current turn.";
        } else if (SABOTAGE_SKILL_NAME.equals(skillName)) {
            returnedSkill = new SabotageSkill();
            description = "Destroys all skill items/objective in any adjacent tile. Destroy all objectives to win!";
        } else if (ATTACK_SKILL_NAME.equals(skillName)) {
            returnedSkill = new AttackSkill();
            description = "Damage other player and destroy all skill items (including yours) in any tile to the left, right, above or below the current position";
        } else if (Player.SPY_ROLE.equals(player.getIdentity())) {
            int[] spyCooldownArray = context.getResources().getIntArray(R.array.chase_array_skill_select_cooldown_spy);
            String[] spyDescriptionArray = context.getResources().getStringArray(R.array.chase_array_skill_select_description_spy);
            
            int skillIndex = -1;
            if (spyRoleSkillNames[0].equals(skillName)) {
                returnedSkill = new EMPBlastSkill();
                skillIndex = 0;
            } else if (spyRoleSkillNames[1].equals(skillName)) {
                returnedSkill = new DropMineSkill();
                skillIndex = 1;
            } else if (spyRoleSkillNames[2].equals(skillName)) {
                returnedSkill = new DropMineSkill();
                skillIndex = 2;
            } else if (spyRoleSkillNames[3].equals(skillName)) {
                returnedSkill = new DropMineSkill();
                skillIndex = 3;
            } else if (spyRoleSkillNames[4].equals(skillName)) {
                returnedSkill = new PlantDecoySkill();
                skillIndex = 4;
            } else if (spyRoleSkillNames[5].equals(skillName)) {
                returnedSkill = new EmergencyHealSkill();
                skillIndex = 5;
            } else if (spyRoleSkillNames[6].equals(skillName)) {
                returnedSkill = new DeployBarrierSkill();
                skillIndex = 6;
            } else if (spyRoleSkillNames[7].equals(skillName)) {
                returnedSkill = new IlluminationBeaconSkill();
                skillIndex = 7;
            } else if (spyRoleSkillNames[8].equals(skillName)) {
                returnedSkill = new SnipeSkill();
                skillIndex = 8;
            } else if (spyRoleSkillNames[9].equals(skillName)) {
                returnedSkill = new ShrapnelBlastSkill();
                skillIndex = 9;
            } else if (spyRoleSkillNames[10].equals(skillName)) {
                returnedSkill = new ToxicCloudSkill();
                skillIndex = 10;
            } else if (spyRoleSkillNames[11].equals(skillName)) {
                returnedSkill = new BaitSkill();
                skillIndex = 11;
            } else if (spyRoleSkillNames[12].equals(skillName)) {
                returnedSkill = new SprintSkill();
                skillIndex = 12;
            }
            if(skillIndex>=0) {
                skillCooldown = spyCooldownArray[skillIndex];
                description = spyDescriptionArray[skillIndex];
            }
        } else if (Player.SENTRY_ROLE.equals(player.getIdentity())) {
            int[] sentryCooldownArray = context.getResources().getIntArray(R.array.chase_array_skill_select_cooldown_sentry);
            String[] sentryDescriptionArray = context.getResources().getStringArray(R.array.chase_array_skill_select_description_sentry);

            int skillIndex = -1;
            if (sentryRoleSkillNames[0].equals(skillName)) {
                returnedSkill = new AlarmTrapSkill();
                skillIndex = 0;
            } else if (sentryRoleSkillNames[1].equals(skillName)) {
                returnedSkill = new RecoverSkill();
                skillIndex = 1;
            } else if (sentryRoleSkillNames[2].equals(skillName)) {
                returnedSkill = new StunMineSkill();
                skillIndex = 2;
            } else if (sentryRoleSkillNames[3].equals(skillName)) {
                returnedSkill = new MotionSensorSkill();
                skillIndex = 3;
            } else if (sentryRoleSkillNames[4].equals(skillName)) {
                returnedSkill = new RoomIlluminationBeaconSkill();
                skillIndex = 4;
            } else if (sentryRoleSkillNames[5].equals(skillName)) {
                returnedSkill = new FragGrenadeSkill();
                skillIndex = 5;
            } else if (sentryRoleSkillNames[6].equals(skillName)) {
                returnedSkill = new SurveillanceBugSkill();
                skillIndex = 6;
            } else if (sentryRoleSkillNames[7].equals(skillName)) {
                returnedSkill = new ShockGrenadeSkill();
                skillIndex = 7;
            } else if (sentryRoleSkillNames[8].equals(skillName)) {
                returnedSkill = new SearchDroneSkill();
                skillIndex = 8;
            } else if (sentryRoleSkillNames[9].equals(skillName)) {
                returnedSkill = new SprintSkill();
                skillIndex = 9;
            }

            if(skillIndex>=0) {
                skillCooldown = sentryCooldownArray[skillIndex];
                description = sentryDescriptionArray[skillIndex];
            }

        } else {
            throw new IllegalStateException("Cannot have player with no identity");
        }

        if (returnedSkill != null) { // assign skill name and the corresponding owner of that skill
            returnedSkill.setOwner(player);
            returnedSkill.setSkillName(skillName);
            returnedSkill.setSkillCooldown(skillCooldown);
            returnedSkill.setDescription(description);
        }

        return returnedSkill;
    }
}
