package com.voyager.chase.skillselect;

/**
 * Created by miguellysanchez on 7/13/16.
 */
public class SkillSelect {

    private String mSkillCode;
    private String mName;
    private int mCooldownTurns;
    private int mSkillCost;
    private String mDescription;
    private boolean isSelected;

    private SkillSelect() {
    }

    public SkillSelect(String skillCode, String name, int skillCost, int cooldownTurns, String description) {
        mSkillCode = skillCode;
        mName = name;
        mSkillCost = skillCost;
        mCooldownTurns = cooldownTurns;
        mDescription = description;
        isSelected = false;
    }

    public String getSkillCode() {
        return mSkillCode;
    }

    public String getName() {
        return mName;
    }

    public int getSkillCost() {
        return mSkillCost;
    }

    public int getCooldownTurns() {
        return mCooldownTurns;
    }

    public String getDescription() {
        return mDescription;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
