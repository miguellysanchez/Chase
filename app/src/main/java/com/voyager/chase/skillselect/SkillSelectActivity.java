package com.voyager.chase.skillselect;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.voyager.chase.R;
import com.voyager.chase.common.BaseActivity;
import com.voyager.chase.game.GameActivity;
import com.voyager.chase.game.entity.player.Player;
import com.voyager.chase.game.entity.player.Sentry;
import com.voyager.chase.game.entity.player.Spy;
import com.voyager.chase.game.skill.SkillsPool;
import com.voyager.chase.mqtt.ArrivedMessage;
import com.voyager.chase.mqtt.DeliveredMessage;
import com.voyager.chase.mqtt.Topics;
import com.voyager.chase.mqtt.listeners.MqttControlMessageListener;
import com.voyager.chase.mqtt.listeners.MqttMessageCallbackListener;
import com.voyager.chase.mqtt.payload.SkillSelectPayload;
import com.voyager.chase.utility.MqttBroadcastUtility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by miguellysanchez on 6/27/16.
 */
public class SkillSelectActivity extends BaseActivity {

    @BindView(R.id.chase_activity_skill_select_listview_skills)
    ListView mListViewSkills;
    @BindView(R.id.chase_activity_skill_select_textview_role)
    TextView mTextViewRole;
    @BindView(R.id.chase_activity_skill_select_textview_points_remaining)
    TextView mTextViewPointsRemaining;
    @BindView(R.id.chase_activity_skill_select_button_start)
    Button mButtonStartGame;

    private String mGameSessionId;
    private boolean mIsWaiting = false;
    private boolean mIsOtherPlayerWaiting = false;
    private ProgressDialog mWaitingProgressDialog;
    private String mGameRole;
    private SkillSelectAdapter mAdapter;

    @Override
    protected void onMqttServiceConnected() {
        getMqttService().connectToBroker(Topics.getSessionStatusTopic(mGameSessionId), "Disconnected ungracefully.");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chase_activity_skill_select);
        ButterKnife.bind(this);
        mGameSessionId = getPreferenceUtility().getGameSessionId();
        mGameRole = getPreferenceUtility().getGameRole();

        initializeViews();
    }

    private void initializeViews() {
        setTitle("Select your skills");
        mTextViewRole.setText(mGameRole);

        ArrayList<SkillSelect> skillSelectList = new ArrayList<>();
        String[] skillNames;
        int[] cooldowns;
        int[] skillCosts;
        String[] descriptions;

        if (Player.SENTRY_ROLE.equals(mGameRole)) {
            mTextViewRole.setText(Player.SENTRY_ROLE.toUpperCase());
            mTextViewPointsRemaining.setText("" + Sentry.STARTING_SKILL_POINTS);
            skillNames = getResources().getStringArray(R.array.chase_array_skill_select_name_sentry);
            skillCosts = getResources().getIntArray(R.array.chase_array_skill_select_cost_sentry);
            cooldowns = getResources().getIntArray(R.array.chase_array_skill_select_cooldown_sentry);
            descriptions = getResources().getStringArray(R.array.chase_array_skill_select_description_sentry);


        } else {
            mTextViewRole.setText(Player.SPY_ROLE.toUpperCase());
            mTextViewPointsRemaining.setText("" + Spy.STARTING_SKILL_POINTS);

            skillNames = getResources().getStringArray(R.array.chase_array_skill_select_name_spy);
            skillCosts = getResources().getIntArray(R.array.chase_array_skill_select_cost_spy);
            cooldowns = getResources().getIntArray(R.array.chase_array_skill_select_cooldown_spy);
            descriptions = getResources().getStringArray(R.array.chase_array_skill_select_description_spy);

        }
        for (int i = 0; i < skillNames.length; i++) {
            SkillSelect skillSelect = new SkillSelect(
                    "",
                    skillNames[i],
                    skillCosts[i],
                    cooldowns[i],
                    descriptions[i]
            );
            skillSelectList.add(skillSelect);
        }

        mAdapter = new SkillSelectAdapter(this, new SkillSelectAdapter.OnClickListener() {
            @Override
            public void onClick(SkillSelect skillSelect) {
                int remainingPoints = Integer.parseInt(mTextViewPointsRemaining.getText().toString());
                if (skillSelect.isSelected()) {
                    skillSelect.setSelected(false);
                    remainingPoints += skillSelect.getSkillCost();

                } else {
                    skillSelect.setSelected(true);
                    remainingPoints -= skillSelect.getSkillCost();
                }
                mTextViewPointsRemaining.setTextColor(ContextCompat.getColor(SkillSelectActivity.this,
                        remainingPoints >= 0 ? R.color.black : R.color.red));
                mTextViewPointsRemaining.setText(remainingPoints + "");
                mButtonStartGame.setEnabled(remainingPoints >= 0);
            }
        });
        mAdapter.setSkillSelectList(skillSelectList);
        mListViewSkills.setAdapter(mAdapter);

        mWaitingProgressDialog = new ProgressDialog(this);
        mWaitingProgressDialog.setMessage("Waiting for other player...");
        mWaitingProgressDialog.setCanceledOnTouchOutside(false);
        mWaitingProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mIsWaiting = false;
                notifyWaitingStatusToOtherPlayer();
            }
        });
    }

    @OnClick(R.id.chase_activity_skill_select_button_start)
    public void onStartGameButtonPressed() {
        mIsWaiting = true;
        notifyWaitingStatusToOtherPlayer();
        attemptToStartGame();
    }

    private void notifyWaitingStatusToOtherPlayer() {
        SkillSelectPayload skillSelectPayload = new SkillSelectPayload();
        skillSelectPayload.setIsWaiting(mIsWaiting);
        skillSelectPayload.setSenderRole(mGameRole);
        getMqttService().publishToTopic(Topics.getSessionStatusTopic(mGameSessionId), skillSelectPayload.toJson(), false);
    }

    private void attemptToStartGame() {
        if (mIsOtherPlayerWaiting && mIsWaiting) {
            goToGameActivity();
        }
    }

    @Override
    protected void executeMqttCallbackAction(Intent intent) {
        switch (intent.getIntExtra(MqttBroadcastUtility.KEY_INT_CALLBACK_VALUE, MqttBroadcastUtility.CALLBACK_VALUE_NULL)) {
            case MqttControlMessageListener.MQTT_CALLBACK_VALUE_CONNECT_SUCCESS: {
                getMqttService().subscribeToTopic(Topics.getSessionStatusTopic(mGameSessionId));
                getMqttService().subscribeToTopic(Topics.getSessionInfoTopic(mGameSessionId));
                getMqttService().subscribeToTopic(Topics.getSessionWorldUpdateTopic(mGameSessionId));
                break;
            }
            case MqttMessageCallbackListener.MQTT_CALLBACK_DELIVERY_COMPLETE: {
                DeliveredMessage deliveredMessage = DeliveredMessage.fromIntent(intent);
                if (Topics.getSessionStatusTopic(mGameSessionId).equals(deliveredMessage.getTopic())) {
                    renderWaitingDialog();
                }
            }
            case MqttMessageCallbackListener.MQTT_CALLBACK_VALUE_MESSAGE_ARRIVED: {
                ArrivedMessage arrivedMessage = ArrivedMessage.fromIntent(intent);
                if (Topics.getSessionStatusTopic(mGameSessionId).equals(arrivedMessage.getTopic())
                        && !TextUtils.isEmpty(arrivedMessage.getPayload())) {
                    Gson gson = new Gson();
                    SkillSelectPayload skillSelectPayload = gson.fromJson(arrivedMessage.getPayload(), SkillSelectPayload.class);
                    if(!skillSelectPayload.getSenderRole().equals(mGameRole)) {
                        mIsOtherPlayerWaiting = skillSelectPayload.isWaiting();
                        attemptToStartGame();
                    }
                }
                break;
            }
        }
    }

    private void goToGameActivity() {
        mWaitingProgressDialog.setOnDismissListener(null);
        finish();
        Intent goToGameActivityIntent = new Intent(this, GameActivity.class);
        ArrayList<String> skillNamesSelectedList = new ArrayList<>();
        skillNamesSelectedList.add(SkillsPool.MOVE_SKILL_NAME);
        if(Player.SPY_ROLE.equals(mGameRole)){
            skillNamesSelectedList.add(SkillsPool.SABOTAGE_SKILL_NAME);
        } else {
            skillNamesSelectedList.add(SkillsPool.ATTACK_SKILL_NAME);
        }
        skillNamesSelectedList.addAll(mAdapter.getSkillNamesSelectedList());
        skillNamesSelectedList.add(SkillsPool.END_TURN_SKILL_NAME);
        goToGameActivityIntent.putStringArrayListExtra(SkillsPool.YOUR_SKILLS_SELECTED, skillNamesSelectedList);
        startActivity(goToGameActivityIntent);
    }

    private void renderWaitingDialog() {
        if (mIsWaiting) {
            if(!mIsOtherPlayerWaiting) {
                mWaitingProgressDialog.show();
            }
        } else {
            if (mWaitingProgressDialog.isShowing()) {
                mWaitingProgressDialog.cancel();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mWaitingProgressDialog.isShowing()) {
            mWaitingProgressDialog.cancel();
        } else {
            super.onBackPressed();
        }
    }
}
