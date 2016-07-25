package com.voyager.chase.game;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.voyager.chase.R;
import com.voyager.chase.common.BaseActivity;
import com.voyager.chase.game.entity.Tile;
import com.voyager.chase.game.entity.player.Player;
import com.voyager.chase.game.entity.player.Sentry;
import com.voyager.chase.game.entity.player.Spy;
import com.voyager.chase.game.skill.Skill;
import com.voyager.chase.game.skill.SkillsAdapter;
import com.voyager.chase.game.skill.SkillsPool;
import com.voyager.chase.mqtt.event.MqttCallbackEvent;
import com.voyager.chase.mqtt.event.MqttResolvedActionEvent;

import java.util.ArrayList;
import java.util.UUID;

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

    @BindView(R.id.chase_activity_game_textview_life)
    TextView mTextViewLife;
    @BindView(R.id.chase_activity_game_textview_action_points)
    TextView mTextViewActionPoints;
    @BindView(R.id.chase_activity_game_textview_current_room)
    TextView mTextViewCurrentRoom;
    @BindView(R.id.chase_activity_game_textview_role)
    TextView mTextViewRole;


    private TurnOrchestrator mTurnOrchestrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chase_activity_game);
        ButterKnife.bind(this);
        mTextViewRole.setText(getPreferenceUtility().getGameRole());
        constructGame();
        initializeGameState();
    }

    private void constructSkillList(Player skillOwner) {
        ArrayList<String> yourSkillsSelected = getIntent().getStringArrayListExtra(SkillsPool.YOUR_SKILLS_SELECTED);
        ArrayList<Skill> equippedSkills = new ArrayList<>();
        Timber.d("Your skills selected: %s", yourSkillsSelected.toString());
        for (String skillName : yourSkillsSelected) {
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
        Spy spy = Spy.createInstance();
        Sentry sentry = Sentry.createInstance();
        world.getRoom("A").getTileAtCoordinate(3,3).setPlayer(spy);
        world.getRoom("A").getTileAtCoordinate(5,6).setPlayer(sentry);

        world.getRoom("A").getTileAtCoordinate(5,6).addVisibilityModifier(UUID.randomUUID().toString(), Tile.GLOBAL_VISIBILITY);
        world.getRoom("A").getTileAtCoordinate(3,3).addVisibilityModifier(UUID.randomUUID().toString(), Tile.GLOBAL_VISIBILITY);


        LevelRenderer levelRenderer = new LevelRenderer(mLinearLayoutLevel);
        levelRenderer.render(world);
//        mTurnOrchestrator = new TurnOrchestrator(this, world, spy, sentry, levelRenderer);

    }


    private void initializeGameState() {
    }

    @Override
    public void executeMqttResolvedActionCallback(MqttResolvedActionEvent mqttResolvedActionEvent) {
    }

    @Override
    protected void executeMqttCallback(MqttCallbackEvent mqttCallbackEvent) {

    }
}
