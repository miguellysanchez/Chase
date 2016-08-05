package com.voyager.chase.game;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.voyager.chase.R;
import com.voyager.chase.common.BaseActivity;
import com.voyager.chase.game.entity.Tile;
import com.voyager.chase.game.entity.construct.ObjectiveConstruct;
import com.voyager.chase.game.entity.construct.TeleporterEntryConstruct;
import com.voyager.chase.game.entity.player.Player;
import com.voyager.chase.game.entity.player.Sentry;
import com.voyager.chase.game.entity.player.Spy;
import com.voyager.chase.game.event.ViewChangeEvent;
import com.voyager.chase.game.event.TurnStateEvent;
import com.voyager.chase.game.gameinfo.GameInfo;
import com.voyager.chase.game.gameinfo.GameInfoAdapter;
import com.voyager.chase.game.handlers.active.CheckQueueStateHandler;
import com.voyager.chase.game.handlers.active.CheckTriggerStateHandler;
import com.voyager.chase.game.handlers.active.EndStateHandler;
import com.voyager.chase.game.handlers.active.RenderStateHandler;
import com.voyager.chase.game.handlers.active.ResolveSkillStateHandler;
import com.voyager.chase.game.handlers.active.SelectSkillStateHandler;
import com.voyager.chase.game.handlers.active.StartStateHandler;
import com.voyager.chase.game.handlers.active.SyncWorldStateHandler;
import com.voyager.chase.game.handlers.active.TargetSelectionStateHandler;
import com.voyager.chase.game.handlers.TurnStateHandler;
import com.voyager.chase.game.handlers.active.UpdateWorldStateHandler;
import com.voyager.chase.game.handlers.active.UpkeepStateHandler;
import com.voyager.chase.game.handlers.inactive.InactiveRenderStateHandler;
import com.voyager.chase.game.handlers.inactive.InactiveUpdateWorldStateHandler;
import com.voyager.chase.game.handlers.inactive.InactivePendingStateHandler;
import com.voyager.chase.game.skill.Skill;
import com.voyager.chase.game.skill.SkillsAdapter;
import com.voyager.chase.game.skill.SkillsPool;
import com.voyager.chase.mqtt.Topics;
import com.voyager.chase.mqtt.event.MqttCallbackEvent;
import com.voyager.chase.mqtt.event.MqttResolvedActionEvent;
import com.voyager.chase.mqtt.payload.GameInfoPayload;
import com.voyager.chase.mqtt.payload.GameStatusPayload;
import com.voyager.chase.mqtt.payload.WorldEffectPayload;
import com.voyager.chase.utility.MqttIssueActionUtility;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;

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
    @BindView(R.id.chase_activity_game_linearlayout_setting_up)
    LinearLayout mLinearlayoutSettingUp;

    @BindView(R.id.chase_activity_game_textview_life)
    TextView mTextViewLife;
    @BindView(R.id.chase_activity_game_textview_action_points)
    TextView mTextViewActionPoints;
    @BindView(R.id.chase_activity_game_textview_current_room)
    TextView mTextViewCurrentRoom;
    @BindView(R.id.chase_activity_game_textview_role)
    TextView mTextViewRole;
    @BindView(R.id.chase_activity_game_button_cancel_skill)
    Button mButtonCancelSkill;
    @BindView(R.id.chase_activity_game_textview_waiting)
    TextView mTextViewWaiting;
    @BindView(R.id.chase_activity_game_linearlayout_processing)
    LinearLayout mLinearLayoutProcessing;

    private SkillsAdapter mSkillsAdapter;
    private GameInfoAdapter mGameInfoAdapter;

    private LevelRenderer mLevelRenderer;
    private HashMap<TurnState, TurnStateHandler> mTurnStateHandlerMap;
    private TurnState mCurrentState;
    private AlertDialog mForfeitGameConfirmationDialog;
    private AlertDialog mEndTurnConfirmationDialog;
    private String mGameSessionId;
    private boolean isGameStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chase_activity_game);
        ButterKnife.bind(this);
        mGameSessionId = getPreferenceUtility().getGameSessionId();
        constructGameWorld();
        initializeViews();
        initializeTurnStateHandlers();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isGameStarted) {
            isGameStarted = true;
            TurnStateEvent event = new TurnStateEvent();
            if (Player.SPY_ROLE.equals(World.getUserPlayer().getRole())) {
                event.setTargetState(TurnState.START_STATE);
            } else if (Player.SENTRY_ROLE.equals(World.getUserPlayer().getRole())) {
                event.setTargetState(TurnState.INACTIVE_PENDING_STATE);
                event.setAction(InactivePendingStateHandler.ACTION_WAITING);
            } else {
                throw new IllegalStateException("Cannot start game with no user game role");
            }
            revealProcessingView();
            EventBus.getDefault().post(event);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void constructGameWorld() {
        //TODO construct game session object, contains world, mqtt handler
        World world = World.getInstance();
        Spy spy = Spy.createInstance();
        Sentry sentry = Sentry.createInstance();
        if (Player.SPY_ROLE.equals(getPreferenceUtility().getGameRole())) {
            World.setUserPlayer(spy);
        } else if (Player.SENTRY_ROLE.equals(getPreferenceUtility().getGameRole())) {
            World.setUserPlayer(sentry);
        } else {
            throw new IllegalStateException("Must have a game role assigned at this point. Cannot construct skills list");
        }
        ArrayList<String> yourSkillsSelected = getIntent().getStringArrayListExtra(SkillsPool.YOUR_SKILLS_SELECTED);
        ArrayList<Skill> equippedSkills = new ArrayList<>();
        Timber.d("Your skills selected: %s", yourSkillsSelected.toString());

        for (String skillName : yourSkillsSelected) {
            Skill skill = SkillsPool.getSkillForName(this, skillName, World.getUserPlayer());
            equippedSkills.add(skill);
        }
        World.getUserPlayer().setSkills(equippedSkills);

        initializeSampleGameState();

        mLevelRenderer = new LevelRenderer(mLinearLayoutLevel);
        mLevelRenderer.render();
    }

    private void initializeViews() {
        mGameInfoAdapter = new GameInfoAdapter(this);
        mListViewInfo.setAdapter(mGameInfoAdapter);

        mSkillsAdapter = new SkillsAdapter(this, new SkillsAdapter.OnClickListener() {
            @Override
            public void onClick(Skill skill) {
                TurnStateEvent turnStateEvent = new TurnStateEvent();
                turnStateEvent.setAction(SelectSkillStateHandler.ACTION_SELECTED);
                turnStateEvent.setSelectedSkill(skill);
                EventBus.getDefault().post(turnStateEvent);
            }
        });
        mSkillsAdapter.setSkillList(World.getUserPlayer().getSkillsList());
        mListViewSkills.setAdapter(mSkillsAdapter);

        mButtonCancelSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revealProcessingView();
                TurnStateEvent cancelSkillEvent = new TurnStateEvent();
                cancelSkillEvent.setAction(TargetSelectionStateHandler.ACTION_CANCEL);
                EventBus.getDefault().post(cancelSkillEvent);
            }
        });

        mEndTurnConfirmationDialog = new AlertDialog.Builder(this)
                .setTitle("END OF TURN")
                .setMessage("Out of ACTION POINTS (AP). Your turn has ended.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        TurnStateEvent confirmEndTurnEvent = new TurnStateEvent();
                        confirmEndTurnEvent.setTargetState(TurnState.END_STATE);
                        confirmEndTurnEvent.setAction(EndStateHandler.ACTION_CONFIRMED);
                        EventBus.getDefault().post(confirmEndTurnEvent);
                    }
                }).create();
        mEndTurnConfirmationDialog.setCanceledOnTouchOutside(false);

        mForfeitGameConfirmationDialog = new AlertDialog.Builder(this)
                .setTitle("QUITTING GAME")
                .setMessage("Are you sure you want to quit the game?")
                .setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        forfeitGame();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        mForfeitGameConfirmationDialog.setCanceledOnTouchOutside(true);

        mTextViewRole.setText(World.getUserPlayer().getRole());
        setUserPlayerStateView();
        revealProcessingView();
    }

    private void initializeTurnStateHandlers() {
        mCurrentState = TurnState.NULL_STATE;
        mTurnStateHandlerMap = new HashMap<>();
        mTurnStateHandlerMap.put(TurnState.START_STATE, new StartStateHandler());
        mTurnStateHandlerMap.put(TurnState.UPKEEP_STATE, new UpkeepStateHandler());
        mTurnStateHandlerMap.put(TurnState.SELECT_SKILL_STATE, new SelectSkillStateHandler());
        mTurnStateHandlerMap.put(TurnState.TARGET_SELECTION_STATE, new TargetSelectionStateHandler());
        mTurnStateHandlerMap.put(TurnState.RESOLVE_SKILL_STATE, new ResolveSkillStateHandler());
        mTurnStateHandlerMap.put(TurnState.CHECK_QUEUE_STATE, new CheckQueueStateHandler());
        mTurnStateHandlerMap.put(TurnState.SYNC_WORLD_STATE, new SyncWorldStateHandler(getPreferenceUtility().getGameSessionId()));
        mTurnStateHandlerMap.put(TurnState.UPDATE_WORLD_STATE, new UpdateWorldStateHandler(this));
        mTurnStateHandlerMap.put(TurnState.RENDER_WORLD_STATE, new RenderStateHandler());
        mTurnStateHandlerMap.put(TurnState.CHECK_TRIGGER_STATE, new CheckTriggerStateHandler());
        mTurnStateHandlerMap.put(TurnState.END_STATE, new EndStateHandler(getPreferenceUtility().getGameSessionId()));

        mTurnStateHandlerMap.put(TurnState.INACTIVE_PENDING_STATE, new InactivePendingStateHandler());
        mTurnStateHandlerMap.put(TurnState.INACTIVE_UPDATE_WORLD_STATE, new InactiveUpdateWorldStateHandler(this));
        mTurnStateHandlerMap.put(TurnState.INACTIVE_RENDER_STATE, new InactiveRenderStateHandler());
    }

    private void initializeSampleGameState() {
        World world = World.getInstance();
        Spy spy = Spy.getInstance();
        Sentry sentry = Sentry.getInstance();
        //TODO sample
        world.getRoom("A").getTileAtCoordinates(2, 3).setPlayer(spy);
        world.getRoom("A").getTileAtCoordinates(3, 3).setPlayer(sentry);

        Tile teleporterTile;
        TeleporterEntryConstruct teleporterEntryConstruct;

        teleporterTile = world.getRoom("A").getTileAtCoordinates(2, 1);
        teleporterEntryConstruct = new TeleporterEntryConstruct("TELEPORTER 1", "B", 6, 1);
        teleporterTile.addConstruct(teleporterEntryConstruct);
        world.addWorldItemLocation(teleporterEntryConstruct.getId(), teleporterTile);

        teleporterTile = world.getRoom("B").getTileAtCoordinates(1, 7);
        teleporterEntryConstruct = new TeleporterEntryConstruct("TELEPORTER 2", "A", 5, 7);
        teleporterTile.addConstruct(teleporterEntryConstruct);
        world.addWorldItemLocation(teleporterEntryConstruct.getId(), teleporterTile);


        Tile objectiveTile;
        ObjectiveConstruct objectiveConstruct;

        objectiveConstruct = new ObjectiveConstruct("OBJECTIVE A");
        objectiveTile = world.getRoom("A").getTileAtCoordinates(1, 2);
        objectiveTile.addConstruct(objectiveConstruct);
        world.addWorldItemLocation(objectiveConstruct.getId(), objectiveTile);

        objectiveConstruct = new ObjectiveConstruct("OBJECTIVE B");
        objectiveTile = world.getRoom("B").getTileAtCoordinates(0, 5);
        objectiveTile.addConstruct(objectiveConstruct);
        world.addWorldItemLocation(objectiveConstruct.getId(), objectiveTile);

        objectiveConstruct = new ObjectiveConstruct("OBJECTIVE C");
        objectiveTile = world.getRoom("A").getTileAtCoordinates(9, 9);
        objectiveTile.addConstruct(objectiveConstruct);
        world.addWorldItemLocation(objectiveConstruct.getId(), objectiveTile);

    }

    @Override
    public void onBackPressed() {
        if (!mForfeitGameConfirmationDialog.isShowing()) {
            mForfeitGameConfirmationDialog.show();
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onTurnStateEvent(TurnStateEvent turnStateEvent) {
        if (turnStateEvent.getTargetState() != null) {
            mCurrentState = turnStateEvent.getTargetState();
        }
        TurnStateHandler turnStateHandler = mTurnStateHandlerMap.get(mCurrentState);
        if (turnStateHandler != null) {
            turnStateHandler.handleTurnStateEvent(turnStateEvent);
        } else {
            Timber.w("No turn state handler found for event type!!!");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onViewChangeEvent(ViewChangeEvent viewChangeEvent) {
        for (String viewChangeType : viewChangeEvent.getViewChanges()) {
            switch (viewChangeType) {
                case ViewChangeEvent.RENDER_WORLD:
                    mLevelRenderer.render();
                    break;
                case ViewChangeEvent.START_GAME:
                    mLinearlayoutSettingUp.setVisibility(View.GONE);
                    break;
                case ViewChangeEvent.END_TURN_CONFIRMATION:
                    mEndTurnConfirmationDialog.show();
                    break;
                case ViewChangeEvent.UPDATE_PLAYER_STATE:
                    setUserPlayerStateView();
                    break;
                case ViewChangeEvent.PROCESSING:
                    revealProcessingView();
                    break;
                case ViewChangeEvent.SHOW_SKILL_SELECTION:
                    revealSkillSelectionView();
                    break;
                case ViewChangeEvent.SHOW_CANCEL_SKILL:
                    revealCancelSkillView();
                    break;
                case ViewChangeEvent.SHOW_TARGET_HIGHLIGHTS:
                    ArrayList<Tile> targetableTiles = viewChangeEvent.getTileArrayList();
                    mLevelRenderer.highlightTiles(targetableTiles);
                    break;
                case ViewChangeEvent.HIDE_TARGET_HIGHLIGHTS:
                    mLevelRenderer.resetTargetHighlights();
                    break;
                case ViewChangeEvent.UPDATE_SKILLS_LIST:
                    updateSkillsListView();
                    break;
                case ViewChangeEvent.WAITING_FOR_OTHER_PLAYER:
                    revealWaitingForOtherPlayerView();
                    break;
                case ViewChangeEvent.TOAST_NO_VALID_TARGETS:
                    Toast.makeText(GameActivity.this, "No valid targets for the selected skill", Toast.LENGTH_SHORT).show();
                    break;
                case ViewChangeEvent.GAME_INFO_UPDATE:
                    MqttIssueActionUtility.publish(Topics.getSessionInfoTopic(mGameSessionId), viewChangeEvent.getGameInfoUpdate(), false);
                    break;
            }
        }
    }

    private void revealSkillSelectionView() {
        mListViewSkills.setVisibility(View.VISIBLE);
        mButtonCancelSkill.setVisibility(View.GONE);
        mTextViewWaiting.setVisibility(View.GONE);
        mLinearLayoutProcessing.setVisibility(View.GONE);
    }

    private void revealCancelSkillView() {
        mListViewSkills.setVisibility(View.GONE);
        mButtonCancelSkill.setVisibility(View.VISIBLE);
        mTextViewWaiting.setVisibility(View.GONE);
        mLinearLayoutProcessing.setVisibility(View.GONE);
    }

    private void revealWaitingForOtherPlayerView() {
        mListViewSkills.setVisibility(View.GONE);
        mButtonCancelSkill.setVisibility(View.GONE);
        mTextViewWaiting.setVisibility(View.VISIBLE);
        mLinearLayoutProcessing.setVisibility(View.GONE);
    }

    private void revealProcessingView() {
        mListViewSkills.setVisibility(View.GONE);
        mButtonCancelSkill.setVisibility(View.GONE);
        mTextViewWaiting.setVisibility(View.GONE);
        mLinearLayoutProcessing.setVisibility(View.VISIBLE);
    }

    private void setUserPlayerStateView() {
        Player player = World.getUserPlayer();
        mTextViewLife.setText("LIFE: " + player.getLife() + "/" + player.getMaxLife());
        if (player.isTurnSkipped()) {
            mTextViewActionPoints.setText("SKIPPED");
        } else {
            mTextViewActionPoints.setText("AP: " + player.getActionPoints() + "/" + player.getActionPointsRecovery());
        }
        mTextViewCurrentRoom.setText("Room: " + player.getCurrentRoomName());
    }

    private void updateSkillsListView() {
        mSkillsAdapter.setSkillList(World.getUserPlayer().getSkillsList());
        mSkillsAdapter.notifyDataSetChanged();
    }

    private void forfeitGame() {
        GameStatusPayload forfeitGamePayload = new GameStatusPayload();
        forfeitGamePayload.setSenderRole(getPreferenceUtility().getGameRole());
        forfeitGamePayload.setAction(GameStatusPayload.DISCONNECTED);
        forfeitGamePayload.setDisconnectionGraceful(true);
        MqttIssueActionUtility.publish(Topics.getSessionStatusTopic(mGameSessionId), forfeitGamePayload.toJson(), false);
    }

    @Override
    public void executeMqttResolvedActionCallback(MqttResolvedActionEvent mqttResolvedActionEvent) {
    }

    @Override
    protected void executeMqttCallback(MqttCallbackEvent mqttCallbackEvent) {
        Gson gson;
        switch (mqttCallbackEvent.getMessageCallbackType()) {
            case MqttCallbackEvent.ARRIVED_MESSAGE_CALLBACK_TYPE:
                gson = new Gson();

                if (Topics.getSessionWorldUpdateTopic(mGameSessionId)
                        .equals(mqttCallbackEvent.getTopic())) {
                    String worldEffectJson = mqttCallbackEvent.getMessagePayload();
                    WorldEffectPayload worldEffectPayload = gson.fromJson(worldEffectJson, WorldEffectPayload.class);
                    if (worldEffectPayload.getSenderRole() != null &&
                            !World.getUserPlayer().getRole().equals(worldEffectPayload.getSenderRole())) {

                        World.getInstance().addWorldEffectToQueue(worldEffectPayload.getWorldEffect());
                        TurnStateEvent turnStateEvent = new TurnStateEvent();
                        turnStateEvent.setTargetState(TurnState.INACTIVE_PENDING_STATE);
                        turnStateEvent.setAction(InactivePendingStateHandler.ACTION_UPDATE);
                        EventBus.getDefault().post(turnStateEvent);
                    } else {
                        Timber.i("Ignoring message from self");
                    }
                } else if (Topics.getSessionStatusTopic(mGameSessionId)
                        .equals(mqttCallbackEvent.getTopic())) {
                    Timber.d("HANDLING SESSION STATUS MESSAGE: %s", mqttCallbackEvent.getMessagePayload());
                    handleSessionTopicMessageReceived(mqttCallbackEvent.getMessagePayload());
                } else if (Topics.getSessionInfoTopic(mGameSessionId).equals(mqttCallbackEvent.getTopic())) {
                    String rawGameInfoPayload = mqttCallbackEvent.getMessagePayload();
                    GameInfoPayload gameInfoPayload = gson.fromJson(rawGameInfoPayload, GameInfoPayload.class);
                    GameInfo gameInfo = new GameInfo();
                    String senderRole = gameInfoPayload.getSenderRole();
                    if (World.getUserPlayer().getRole().equals(senderRole) && !TextUtils.isEmpty(gameInfoPayload.getSenderMessage())) {
                        gameInfo.setInfo(gameInfoPayload.getSenderMessage());
                    } else if (!World.getUserPlayer().getRole().equals(senderRole) && !TextUtils.isEmpty(gameInfoPayload.getNonSenderMessage())) {
                        gameInfo.setInfo(gameInfoPayload.getNonSenderMessage());
                    } else {
                        break;
                    }
                    gameInfo.setSenderRole(senderRole);
                    gameInfo.setTimestamp(System.currentTimeMillis());
                    mGameInfoAdapter.addToGameInfoArrayList(gameInfo);
                    mListViewInfo.setSelection(0);
                }
                break;
            case MqttCallbackEvent.DELIVERED_MESSAGE_CALLBACK_TYPE:
                break;
        }

    }

    private void handleSessionTopicMessageReceived(String message) {
        Gson gson = new Gson();
        GameStatusPayload gameStatusPayload = gson.fromJson(message, GameStatusPayload.class);
        if (!getPreferenceUtility().getGameRole().equals(gameStatusPayload.getSenderRole())) {
            switch (gameStatusPayload.getAction()) {
                case GameStatusPayload.TURN_FINISHED:
                    if (GameStatusPayload.TURN_FINISHED.equals(gameStatusPayload.getAction())) {
                        TurnStateEvent turnStateEvent = new TurnStateEvent();
                        turnStateEvent.setTargetState(TurnState.INACTIVE_PENDING_STATE);
                        turnStateEvent.setAction(InactivePendingStateHandler.ACTION_OTHER_PLAYER_FINISHED);
                        EventBus.getDefault().post(turnStateEvent);
                    }
                    break;
                case GameStatusPayload.DISCONNECTED:
                    MqttIssueActionUtility.disconnect();
                    if (gameStatusPayload.isDisconnectionGraceful()) {
                        //TODO Show dialog that partner has forfeited match. Show win game screen afterwards.
                    } else {
                        //TODO Show dialog that partner was disconnected;
                    }
                    break;
                case GameStatusPayload.SHOW_RESULTS:
                    //TODO show resultActivity with payload results
                    break;
            }
        }
    }
}
