package com.voyager.chase.game.skill;

import android.content.Context;

import com.voyager.chase.R;
import com.voyager.chase.game.entity.player.Player;
import com.voyager.chase.game.skill.common.EndTurnSkill;
import com.voyager.chase.game.skill.common.MoveSkill;

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
        String[] spyRoleSkills = context.getResources().getStringArray(R.array.chase_array_skill_select_name_spy);
        String[] sentryRoleSkills = context.getResources().getStringArray(R.array.chase_array_skill_select_name_sentry);

        Skill returnedSkill = null;

        if (MOVE_SKILL_NAME.equals(skillName)) {
            returnedSkill = new MoveSkill();
        } else if(END_TURN_SKILL_NAME.equals(skillName)){
            returnedSkill = new EndTurnSkill();
        }
//        else if (SABOTAGE_SKILL_NAME.equals(skillName)) {
//            returnedSkill = new SabotageSkill();
//        } else if (spyRoleSkills[0].equals(skillName)) {
//            returnedSkill = new EMPBlastSkill();
//        } else if (spyRoleSkills[1].equals(skillName)) {
//            returnedSkill = new DropMineSkill(4);
//        } else if (spyRoleSkills[2].equals(skillName)) {
//            returnedSkill = new DropMineSkill(5);
//        } else if (spyRoleSkills[3].equals(skillName)) {
//            returnedSkill = new DropMineSkill(6);
//        } else if (spyRoleSkills[4].equals(skillName)) {
//            returnedSkill = new PlantDecoySkill();
//        } else if (spyRoleSkills[5].equals(skillName)) {
//            returnedSkill = new EmergencyHealSkill();
//        } else if (spyRoleSkills[6].equals(skillName)) {
//            returnedSkill = new DeployBarrierSkill();
//        } else if (spyRoleSkills[7].equals(skillName)) {
//            returnedSkill = new IlluminationBeaconSkill();
//        } else if (spyRoleSkills[8].equals(skillName)) {
//            returnedSkill = new SnipeSkill();
//        } else if (spyRoleSkills[9].equals(skillName)) {
//            returnedSkill = new ShrapnelBlastSkill();
//        } else if (spyRoleSkills[10].equals(skillName)) {
//            returnedSkill = new ToxicCloudSkill();
//        } else if (spyRoleSkills[11].equals(skillName)) {
//            returnedSkill = new BaitSkill();
//        } else if (spyRoleSkills[12].equals(skillName)) {
//            returnedSkill = new SprintSkill();
//        } else if (ATTACK_SKILL_NAME.equals(skillName)) {
//            returnedSkill = new AttackSkill();
//        } else if (sentryRoleSkills[0].equals(skillName)) {
//            returnedSkill = new AlarmTrapSkill();
//        } else if (sentryRoleSkills[1].equals(skillName)) {
//            returnedSkill = new RecoverSkill();
//        } else if (sentryRoleSkills[2].equals(skillName)) {
//            returnedSkill = new StunMineSkill();
//        } else if (sentryRoleSkills[3].equals(skillName)) {
//            returnedSkill = new MotionSensorSkill();
//        } else if (sentryRoleSkills[4].equals(skillName)) {
//            returnedSkill = new RoomIlluminationBeaconSkill();
//        } else if (sentryRoleSkills[5].equals(skillName)) {
//            returnedSkill = new FragGrenadeSkill();
//        } else if (sentryRoleSkills[6].equals(skillName)) {
//            returnedSkill = new NightVisionGogglesSkill();
//        } else if (sentryRoleSkills[7].equals(skillName)) {
//            returnedSkill = new ShockGrenadeSkill();
//        } else if (sentryRoleSkills[8].equals(skillName)) {
//            returnedSkill = new SearchDroneSkill();
//        } else if (sentryRoleSkills[9].equals(skillName)) {
//            returnedSkill = new SprintSkill();
//        }

        if (returnedSkill != null) {
            returnedSkill.setOwner(player);
        }
        return returnedSkill;
    }
}
