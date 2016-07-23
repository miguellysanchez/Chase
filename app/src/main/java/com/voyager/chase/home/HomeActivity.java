package com.voyager.chase.home;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TextView;

import com.voyager.chase.R;
import com.voyager.chase.common.BaseActivity;
import com.voyager.chase.lobby.LobbyActivity;
import com.voyager.chase.home.dialog.RequestUsernameDialog;
import com.voyager.chase.mqtt.event.IsConnectedCallback;
import com.voyager.chase.mqtt.event.MqttCallbackEvent;
import com.voyager.chase.mqtt.event.MqttResolvedActionEvent;
import com.voyager.chase.utility.MqttIssueActionUtility;
import com.voyager.chase.utility.PreferenceUtility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by miguellysanchez on 6/26/16.
 */
public class HomeActivity extends BaseActivity {

    @BindView(R.id.chase_activity_home_textview_username)
    TextView mTextViewUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chase_activity_home);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        MqttIssueActionUtility.checkIsConnected(new IsConnectedCallback() {
            @Override
            protected void onIsConnected() {
                MqttIssueActionUtility.disconnect();
            }

            @Override
            protected void onIsDisconnected() {
                Timber.d("Already disconnected from MQTT Broker will not try to disconnect");
            }
        });
        String username = PreferenceUtility.getInstance(this).getMqttUserId();
        if (!TextUtils.isEmpty(username)) {
            mTextViewUsername.setText(String.format("Welcome, %s", username));
        } else {
            showPromptUsernameDialog();
        }
    }


    @OnClick(R.id.chase_activity_home_textview_username)
    public void showPromptUsernameDialog() {
        Dialog requestUsernameDialog = new RequestUsernameDialog(this);
        requestUsernameDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mTextViewUsername.setText(
                        String.format("Welcome, %s", PreferenceUtility.getInstance(HomeActivity.this).getMqttUserId()));
            }
        });
        requestUsernameDialog.show();
    }

    @OnClick(R.id.chase_activity_home_button_play)
    void onPlayButtonClicked() {
        Intent lobbyActivity = new Intent(HomeActivity.this, LobbyActivity.class);
        startActivity(lobbyActivity);
    }

    @OnClick(R.id.chase_activity_home_button_quit)
    void onQuitButtonClicked() {
        Timber.d(">>>Quitting the app");
        finish();
    }

    @Override
    protected void executeMqttResolvedActionCallback(MqttResolvedActionEvent mqttResolvedActionEvent) {
        switch (mqttResolvedActionEvent.getActionType()) {
            case MqttResolvedActionEvent.DISCONNECT_ACTION_TYPE:
                if (mqttResolvedActionEvent.isSuccess()) {
                    Timber.d("Success in disconnected from MQTT Service");
                } else {
                    MqttIssueActionUtility.disconnect(); //try to disconnect again
                }
                break;
            case MqttResolvedActionEvent.SUBSCRIBE_ACTION_TYPE:
                if(mqttResolvedActionEvent.isSuccess()){
                    MqttIssueActionUtility.subscribe("sample/topic1/topicrandom");
                    Timber.d("Test");

                } else {
                    mqttResolvedActionEvent.getThrowable();
                }
        }
    }

    @Override
    protected void executeMqttCallback(MqttCallbackEvent mqttCallbackEvent) {
    }
}
