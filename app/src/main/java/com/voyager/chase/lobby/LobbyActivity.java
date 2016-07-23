package com.voyager.chase.lobby;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.voyager.chase.R;
import com.voyager.chase.common.BaseActivity;
import com.voyager.chase.game.entity.player.Player;
import com.voyager.chase.home.HomeActivity;
import com.voyager.chase.mqtt.Topics;
import com.voyager.chase.mqtt.event.IsConnectedCallback;
import com.voyager.chase.mqtt.event.MqttResolvedActionEvent;
import com.voyager.chase.mqtt.event.MqttCallbackEvent;
import com.voyager.chase.mqtt.payload.LobbyJoiningPayload;
import com.voyager.chase.skillselect.SkillSelectActivity;
import com.voyager.chase.utility.MqttIssueActionUtility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by miguellysanchez on 6/27/16.
 */
public class LobbyActivity extends BaseActivity {

    @BindView(R.id.chase_activity_lobby_textview_connection_status)
    TextView mConnectionStatus;
    @BindView(R.id.chase_activity_lobby_linearlayout_joining)
    LinearLayout mLinearLayoutJoining;
    @BindView(R.id.chase_activity_lobby_linearlayout_options)
    LinearLayout mLinearLayoutOptions;
    @BindView(R.id.chase_activity_lobby_edittext_join_id_input)
    EditText mEditTextJoinIdInput;
    @BindView(R.id.chase_activity_lobby_textview_joining_id)
    TextView mTextViewJoiningId;

    private ProgressDialog progressDialog;
    private AlertDialog unableToConnectDialog;

    private int mJoinState = LobbyJoiningPayload.JOIN_STATE_NULL;
    private String mJoinId = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chase_activity_lobby);
        ButterKnife.bind(this);
        initializeViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        MqttIssueActionUtility.checkIsConnected(new IsConnectedCallback() {
            @Override
            protected void onIsConnected() {
                Timber.i("Cannot connect to lobby. Already connected to the broker");
            }

            @Override
            protected void onIsDisconnected() {
                connectToLobby();
            }
        });
    }


    private void initializeViews() {
        mConnectionStatus.setText("...Loading");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Connecting...");
        progressDialog.setCanceledOnTouchOutside(false);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Connection Error");
        dialogBuilder.setMessage("Unable to connect to server. Reconnect again?");
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                connectToLobby();
            }
        });
        dialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialogBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        unableToConnectDialog = dialogBuilder.create();
        unableToConnectDialog.setCanceledOnTouchOutside(false);
    }

    public void connectToLobby() {
        progressDialog.show();
        MqttIssueActionUtility.connect();
    }

    @OnClick(R.id.chase_activity_lobby_button_exit)
    public void exitFromLobby() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        mLinearLayoutOptions.setVisibility(View.GONE);
        mJoinState = LobbyJoiningPayload.JOIN_STATE_NULL;
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @OnClick(R.id.chase_activity_lobby_button_create_game)
    public void createGame() {
        mJoinId = getPreferenceUtility().getMqttUserId();
        findGameWithId(mJoinId);
    }

    @OnClick(R.id.chase_activity_lobby_button_join_game)
    public void joinGame() {
        mJoinId = mEditTextJoinIdInput.getText().toString().trim();
        if (TextUtils.isEmpty(mJoinId)) {
            Toast.makeText(this, "Client id must not be empty", Toast.LENGTH_LONG).show();
        } else {
            findGameWithId(mJoinId);
        }
    }

    private void findGameWithId(String joinId) {
        mTextViewJoiningId.setText(joinId);
        mLinearLayoutJoining.setVisibility(View.VISIBLE);
        mJoinState = LobbyJoiningPayload.JOIN_STATE_REGISTERING;

        MqttIssueActionUtility.subscribe(Topics.constructTopic(Topics.LOBBY_TOPIC, joinId));
    }

    @Override
    public void onBackPressed() {
        if (unableToConnectDialog.isShowing()) {
            unableToConnectDialog.dismiss();
            finish();
        } else if (mLinearLayoutJoining.getVisibility() == View.VISIBLE) {
            mLinearLayoutJoining.setVisibility(View.GONE);
        } else {
            exitFromLobby();
        }
    }

    @Override
    public void executeMqttResolvedActionCallback(MqttResolvedActionEvent mqttResolvedActionEvent) {
        Timber.d("Execute Mqtt ResolvedCommand callback");
        switch (mqttResolvedActionEvent.getActionType()) {
            case MqttResolvedActionEvent.CONNECT_ACTION_TYPE:
                if (mqttResolvedActionEvent.isSuccess()) {
                    mConnectionStatus.setText("Connected");
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    mLinearLayoutOptions.setVisibility(View.VISIBLE);
                } else {
                    handleConnectionError();
                }
                break;
            case MqttResolvedActionEvent.DISCONNECT_ACTION_TYPE:
                break;
            case MqttResolvedActionEvent.SUBSCRIBE_ACTION_TYPE:
                if (mqttResolvedActionEvent.isSuccess()) {
                    String joinIdTopic = Topics.constructTopic(Topics.LOBBY_TOPIC, mJoinId);
                    if (joinIdTopic.equals(mqttResolvedActionEvent.getFirstTopic())) {
                        mJoinState = LobbyJoiningPayload.JOIN_STATE_SUBSCRIBED;
                        LobbyJoiningPayload payload = new LobbyJoiningPayload();
                        payload.setUserId(getPreferenceUtility().getMqttUserId());
                        payload.setStatus(LobbyJoiningPayload.JOIN_STATE_WAITING);
                        String payloadJson = payload.toJson();

                        MqttIssueActionUtility.publish(joinIdTopic, payloadJson, true);
                    }
                }


        }
    }

    @Override
    protected void executeMqttCallback(MqttCallbackEvent mqttCallbackEvent) {
        Timber.d("Execute Mqtt Message callback");
        switch (mqttCallbackEvent.getMessageCallbackType()) {
            case MqttCallbackEvent.CONNECTION_LOST_CALLBACK_TYPE:
                handleConnectionError();
                break;
            case MqttCallbackEvent.ARRIVED_MESSAGE_CALLBACK_TYPE:
                if (!TextUtils.isEmpty(mqttCallbackEvent.getMessagePayload())) {
                    String joinIdTopic = Topics.constructTopic(Topics.LOBBY_TOPIC, mJoinId);
                    if (joinIdTopic.equals(mqttCallbackEvent.getTopic())) {
                        Gson gson = new Gson();
                        LobbyJoiningPayload lobbyJoiningPayload = gson.fromJson(mqttCallbackEvent.getMessagePayload(), LobbyJoiningPayload.class);
                        int otherPlayerJoinStatus = lobbyJoiningPayload.getStatus();
                        String fromClientId = lobbyJoiningPayload.getUserId();
                        if (mJoinState == LobbyJoiningPayload.JOIN_STATE_WAITING
                                && otherPlayerJoinStatus == LobbyJoiningPayload.JOIN_STATE_WAITING
                                && !fromClientId.equals(getPreferenceUtility().getMqttUserId())) {
                            LobbyJoiningPayload payload = new LobbyJoiningPayload();
                            payload.setUserId(getPreferenceUtility().getMqttUserId());
                            payload.setStatus(LobbyJoiningPayload.JOIN_STATE_WAITING);
                            String payloadJson = payload.toJson();
                            MqttIssueActionUtility.publish(joinIdTopic, payloadJson, true);
                        }
                    }
                }
                break;
            case MqttCallbackEvent.DELIVERED_MESSAGE_CALLBACK_TYPE:
                if (Topics.constructTopic(Topics.LOBBY_TOPIC, mJoinId).equals(mqttCallbackEvent.getTopic())) {
                    if (mJoinState == LobbyJoiningPayload.JOIN_STATE_SUBSCRIBED) {
                        mJoinState = LobbyJoiningPayload.JOIN_STATE_WAITING;
                    } else if (mJoinState == LobbyJoiningPayload.JOIN_STATE_WAITING) {
                        mJoinState = LobbyJoiningPayload.JOIN_STATE_NULL;
                        if (mJoinId.equals(getPreferenceUtility().getMqttUserId())) {
                            getPreferenceUtility().setGameRole(Player.SPY_ROLE);
                        } else {
                            getPreferenceUtility().setGameRole(Player.SENTRY_ROLE);
                        }
                        getPreferenceUtility().setGameSessionId(mJoinId);
                        finish();
                        Intent goToSkillSelectIntent = new Intent(this, SkillSelectActivity.class);
                        startActivity(goToSkillSelectIntent);
                    }
                }
                break;
        }

    }

    private void handleConnectionError() {
        mConnectionStatus.setText("Disconnected");
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        unableToConnectDialog.show();
        mLinearLayoutOptions.setVisibility(View.GONE);
        mLinearLayoutJoining.setVisibility(View.GONE);
        mJoinState = LobbyJoiningPayload.JOIN_STATE_NULL;
    }


}
