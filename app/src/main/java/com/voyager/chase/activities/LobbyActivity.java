package com.voyager.chase.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.voyager.chase.R;
import com.voyager.chase.mqtt.MqttActionMessageListener;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by miguellysanchez on 6/27/16.
 */
public class LobbyActivity extends BaseActivity {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chase_activity_lobby);
        ButterKnife.bind(this);
        initializeViews();
    }

    private void initializeViews() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Connecting...");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @OnClick(R.id.chase_activity_lobby_button_connect)
    public void connectToLobby(){
        progressDialog.show();
        getMqttService().connectToBroker();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void executeMqttCallbackAction(Intent intent) {
        switch(intent.getIntExtra(MqttActionMessageListener.KEY_VALUE_INT_MQTT_CALLBACK, MqttActionMessageListener.MQTT_CALLBACK_VALUE_NULL)){
            case MqttActionMessageListener.MQTT_CALLBACK_VALUE_CONNECT_SUCCESS:
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                break;
        }
    }
}
