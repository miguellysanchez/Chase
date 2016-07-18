package com.voyager.chase.game;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.voyager.chase.R;
import com.voyager.chase.common.BaseActivity;
import com.voyager.chase.game.entity.player.Player;
import com.voyager.chase.game.entity.player.Sentry;
import com.voyager.chase.game.entity.player.Spy;
import com.voyager.chase.game.executors.TurnOrchestrator;
import com.voyager.chase.game.skill.Skill;
import com.voyager.chase.game.skill.SkillsAdapter;
import com.voyager.chase.game.skill.SkillsPool;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


/**
 * Created by miguellysanchez on 6/26/16.
 */

public class GameActivity extends BaseActivity {

    @BindView(R.id.chase_activity_game_linearlayout_level)
    LinearLayout mLinearLayoutLevel;
    @BindView(R.id.chase_activity_game_listview_skills)
    ListView mListViewSkills;
    @BindView(R.id.chase_activity_game_listview_info)
    ListView mListViewInfo;

    private TurnOrchestrator mTurnOrchestrator;

    @Override
    protected void onMqttServiceConnected() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chase_activity_game);
        ButterKnife.bind(this);
        constructGame();
        initializeGameState();
    }


    private void constructSkillList(Player skillOwner) {
        ArrayList<String> yourSkillsSelected = getIntent().getStringArrayListExtra(SkillsPool.YOUR_SKILLS_SELECTED);
        ArrayList<Skill> equippedSkills = new ArrayList<>();
        Timber.d("Your skills selected: %s", yourSkillsSelected.toString());
        for(String skillName: yourSkillsSelected){
            Skill skill = SkillsPool.getSkillForName(this, skillName, skillOwner);
            equippedSkills.add(skill);
        }
        SkillsAdapter adapter = new SkillsAdapter(this, new SkillsAdapter.OnClickListener() {
            @Override
            public void onClick(Skill skill) {
                skill.triggerTargetSelection(mTurnOrchestrator.getWorld());
            }
        });
        adapter.setSkillList(equippedSkills);
    }

    private void constructGame() {
        //TODO construct game session object, contains world, mqtt handler
        World world = World.sampleCreateWorld();
        Spy spy = new Spy();
        Sentry sentry = new Sentry();
        Player currentPlayer;
        String mGameRole = getPreferenceUtility().getGameRole();
        if (Player.SENTRY_ROLE.equals(mGameRole)) {
            currentPlayer = sentry;
        } else if (Player.SPY_ROLE.equals(mGameRole)) {
            currentPlayer = spy;
        } else {
            throw new IllegalStateException("Should not enter this part.");
        }
        LevelRenderer levelRenderer = new LevelRenderer(mLinearLayoutLevel, currentPlayer);
        mTurnOrchestrator = new TurnOrchestrator(this, world,spy, sentry, levelRenderer);

    }


    private void initializeGameState() {
    }
}
