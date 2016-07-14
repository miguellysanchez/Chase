package com.voyager.chase.activities;

import android.os.Bundle;

import com.voyager.chase.R;
import com.voyager.chase.game.World;


/**
 * Created by miguellysanchez on 6/26/16.
 */

public class GameActivity extends BaseActivity {

    @Override
    protected void onMqttServiceConnected() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chase_activity_game);
        initializeViews();
    }

    private void initializeViews() {

        //TODO construct game session object, contains world, mqtt handle
        World gameWorld = World.sampleCreateWorld();

    }
}
