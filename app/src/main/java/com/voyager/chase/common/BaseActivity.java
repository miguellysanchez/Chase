package com.voyager.chase.common;

import android.support.v7.app.AppCompatActivity;

import com.voyager.chase.mqtt.event.MqttResolvedActionEvent;
import com.voyager.chase.mqtt.event.MqttCallbackEvent;
import com.voyager.chase.utility.PreferenceUtility;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * Created by miguellysanchez on 6/26/16.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private PreferenceUtility mPreferenceUtility;
    protected boolean isConnectedToMqtt = false;

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    protected PreferenceUtility getPreferenceUtility() {
        if (mPreferenceUtility == null) {
            mPreferenceUtility = PreferenceUtility.getInstance(this);
        }
        return mPreferenceUtility;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMqttResolvedActionCallback(MqttResolvedActionEvent mqttResolvedActionEvent) {
        executeMqttResolvedActionCallback(mqttResolvedActionEvent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMqttMessageCallback(final MqttCallbackEvent mqttCallbackEvent) {
        executeMqttCallback(mqttCallbackEvent);
    }

    protected abstract void executeMqttResolvedActionCallback(MqttResolvedActionEvent mqttResolvedActionEvent);

    protected abstract void executeMqttCallback(MqttCallbackEvent mqttCallbackEvent);

}
