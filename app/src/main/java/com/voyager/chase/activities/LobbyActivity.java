package com.voyager.chase.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.voyager.chase.R;
import com.voyager.chase.mqtt.listeners.MqttActionMessageListener;
import com.voyager.chase.mqtt.listeners.MqttMessageCallbackListener;
import com.voyager.chase.utility.BroadcastUtility;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by miguellysanchez on 6/27/16.
 */
public class LobbyActivity extends BaseActivity {

    @BindView(R.id.chase_activity_lobby_textview_connection_status)
    TextView mConnectionStatus;

    private ProgressDialog progressDialog;
    private AlertDialog unableToConnectDialog;

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
    }

    private void initializeViews() {
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
                dialog.dismiss();
                finish();
            }
        });
        unableToConnectDialog = dialogBuilder.create();
    }

    public void connectToLobby(){
        progressDialog.show();
        getMqttService().connectToBroker();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    protected void onMqttServiceConnected() {
        connectToLobby();
    }

    @Override
    protected void executeMqttCallbackAction(Intent intent) {
        switch(intent.getIntExtra(BroadcastUtility.KEY_INT_CALLBACK_VALUE, BroadcastUtility.CALLBACK_VALUE_NULL)){
            case MqttActionMessageListener.MQTT_CALLBACK_VALUE_CONNECT_SUCCESS:

                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                break;
            case MqttMessageCallbackListener.MQTT_CALLBACK_VALUE_CONNECTION_LOST:
            case MqttActionMessageListener.MQTT_CALLBACK_VALUE_CONNECT_FAILURE:
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                unableToConnectDialog.show();
                break;
            case MqttActionMessageListener.MQTT_CALLBACK_VALUE_DISCONNECT_SUCCESS:
                finish();
                break;
        }
    }
}
