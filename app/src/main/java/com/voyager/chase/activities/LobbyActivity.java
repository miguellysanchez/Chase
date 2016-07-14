package com.voyager.chase.activities;

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
import com.voyager.chase.game.entity.player.Player;
import com.voyager.chase.mqtt.ArrivedMessage;
import com.voyager.chase.mqtt.ControlMessage;
import com.voyager.chase.mqtt.DeliveredMessage;
import com.voyager.chase.mqtt.Topics;
import com.voyager.chase.mqtt.listeners.MqttControlMessageListener;
import com.voyager.chase.mqtt.listeners.MqttMessageCallbackListener;
import com.voyager.chase.mqtt.payload.LobbyJoiningPayload;
import com.voyager.chase.utility.BroadcastUtility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
        if (isMqttServiceBound() && !getMqttService().isConnectedToBroker()) {
            connectToLobby();
        }
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
        getMqttService().connectToBroker();
    }

    @OnClick(R.id.chase_activity_lobby_button_exit)
    public void disconnectFromLobby() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        getMqttService().disconnectFromBroker();
    }

    @OnClick(R.id.chase_activity_lobby_button_create_game)
    public void createGame() {
        mJoinId = getMqttService().getMqttClientId();
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
        getMqttService().subscribeToTopic(Topics.constructTopic(Topics.LOBBY_TOPIC, joinId));
    }


    @Override
    public void onBackPressed() {
        if (unableToConnectDialog.isShowing()) {
            unableToConnectDialog.dismiss();
            finish();
        } else if (mLinearLayoutJoining.getVisibility() == View.VISIBLE) {
            mLinearLayoutJoining.setVisibility(View.GONE);
        } else {
            disconnectFromLobby();
        }
    }


    @Override
    protected void onMqttServiceConnected() {
        connectToLobby();
    }

    @Override
    protected void executeMqttCallbackAction(Intent intent) {
        switch (intent.getIntExtra(BroadcastUtility.KEY_INT_CALLBACK_VALUE, BroadcastUtility.CALLBACK_VALUE_NULL)) {
            case MqttControlMessageListener.MQTT_CALLBACK_VALUE_CONNECT_SUCCESS:
            case MqttControlMessageListener.MQTT_CALLBACK_VALUE_CONNECT_REDUNDANT:
                mConnectionStatus.setText("Connected");
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                mLinearLayoutOptions.setVisibility(View.VISIBLE);

                break;
            case MqttMessageCallbackListener.MQTT_CALLBACK_VALUE_CONNECTION_LOST:
            case MqttControlMessageListener.MQTT_CALLBACK_VALUE_CONNECT_FAILURE: {
                mConnectionStatus.setText("Disconnected");
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                unableToConnectDialog.show();
                mLinearLayoutOptions.setVisibility(View.GONE);
                mLinearLayoutJoining.setVisibility(View.GONE);
                mJoinState = LobbyJoiningPayload.JOIN_STATE_NULL;
                break;
            }
            case MqttControlMessageListener.MQTT_CALLBACK_VALUE_DISCONNECT_SUCCESS: {
                mConnectionStatus.setText("Disconnected");
                mLinearLayoutOptions.setVisibility(View.GONE);
                mJoinState = LobbyJoiningPayload.JOIN_STATE_NULL;
                finish();
                break;
            }
            case MqttControlMessageListener.MQTT_CALLBACK_VALUE_SUBSCRIBE_SUCCESS: {
                String[] topicsList = intent.getStringArrayExtra(ControlMessage.KEY_VALUE_STRING_ARRAY_TOPICS_LIST);
                String joinIdTopic = Topics.constructTopic(Topics.LOBBY_TOPIC, mJoinId);
                if (joinIdTopic.equals(topicsList[0])) {
                    mJoinState = LobbyJoiningPayload.JOIN_STATE_SUBSCRIBED;
                    LobbyJoiningPayload payload = new LobbyJoiningPayload();
                    payload.setClientId(getPreferenceUtility().getMqttClientId());
                    payload.setStatus(LobbyJoiningPayload.JOIN_STATE_WAITING);
                    String payloadJson = payload.toJson();
                    getMqttService().publishToTopic(joinIdTopic, payloadJson);
                }
                break;
            }
            case MqttMessageCallbackListener.MQTT_CALLBACK_VALUE_MESSAGE_ARRIVED: {
                ArrivedMessage arrivedMessage = ArrivedMessage.fromIntent(intent);
                if (!TextUtils.isEmpty(arrivedMessage.getPayload())) {
                    String joinIdTopic = Topics.constructTopic(Topics.LOBBY_TOPIC, mJoinId);
                    if (joinIdTopic.equals(arrivedMessage.getTopic())) {
                        Gson gson = new Gson();
                        LobbyJoiningPayload lobbyJoiningPayload = gson.fromJson(arrivedMessage.getPayload(), LobbyJoiningPayload.class);
                        int otherPlayerJoinStatus = lobbyJoiningPayload.getStatus();
                        String fromClientId = lobbyJoiningPayload.getClientId();
                        if(mJoinState == LobbyJoiningPayload.JOIN_STATE_WAITING
                                && otherPlayerJoinStatus == LobbyJoiningPayload.JOIN_STATE_WAITING
                                && !fromClientId.equals(getPreferenceUtility().getMqttClientId())){
                            LobbyJoiningPayload payload = new LobbyJoiningPayload();
                            payload.setClientId(getPreferenceUtility().getMqttClientId());
                            payload.setStatus(LobbyJoiningPayload.JOIN_STATE_WAITING);
                            String payloadJson = payload.toJson();
                            getMqttService().publishToTopic(joinIdTopic, payloadJson);
                        }
                    }
                }
                break;
            }
            case MqttMessageCallbackListener.MQTT_CALLBACK_DELIVERY_COMPLETE: {
                DeliveredMessage deliveredMessage = DeliveredMessage.fromIntent(intent);
                if(Topics.constructTopic(Topics.LOBBY_TOPIC, mJoinId).equals(deliveredMessage.getTopic())){
                    if(mJoinState == LobbyJoiningPayload.JOIN_STATE_SUBSCRIBED){
                        mJoinState = LobbyJoiningPayload.JOIN_STATE_WAITING;
                    } else if(mJoinState == LobbyJoiningPayload.JOIN_STATE_WAITING){
                        mJoinState = LobbyJoiningPayload.JOIN_STATE_NULL;
                        if(mJoinId.equals(getMqttService().getMqttClientId())){
                            getPreferenceUtility().setGameRole(Player.SPY_ROLE);
                        } else {
                            getPreferenceUtility().setGameRole(Player.SENTRY_ROLE);
                        }
                        finish();
                        Intent goToSkillSelectIntent = new Intent(this, SkillSelectActivity.class);
                        startActivity(goToSkillSelectIntent);
                    }
                }

                break;
            }
        }
    }

}
