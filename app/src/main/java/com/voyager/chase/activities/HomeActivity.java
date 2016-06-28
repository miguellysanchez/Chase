package com.voyager.chase.activities;

import android.content.Intent;
import android.os.Bundle;

import com.voyager.chase.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by miguellysanchez on 6/26/16.
 */
public class HomeActivity extends BaseActivity {

    @Override
    protected void onMqttServiceConnected() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chase_activity_home);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.chase_activity_home_button_play)
    void onPlayButtonClicked(){
        Intent lobbyActivity = new Intent(HomeActivity.this, LobbyActivity.class);
        startActivity(lobbyActivity);
    }

    @OnClick(R.id.chase_activity_home_button_quit)
    void onQuitButtonClicked(){
        onBackPressed();
    }
}
