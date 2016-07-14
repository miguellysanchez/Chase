package com.voyager.chase.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.voyager.chase.R;
import com.voyager.chase.game.entity.player.Player;
import com.voyager.chase.game.skill.SkillSelect;
import com.voyager.chase.game.skill.SkillSelectAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by miguellysanchez on 6/27/16.
 */
public class SkillSelectActivity extends BaseActivity {

    @BindView(R.id.chase_activity_skill_select_listview_skills)

    @Override
    protected void onMqttServiceConnected() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chase_activity_skill_select);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        initializeViews();
    }

    private void initializeViews() {
        ArrayList<SkillSelect> skillSelectList= new ArrayList<>();


        if(Player.SENTRY_ROLE.equals(getPreferenceUtility().getGameRole())){

        } else {

        }
        skillSelectList.add()

        SkillSelectAdapter adapter = new SkillSelectAdapter(this, skillSelectList);


    }
}
