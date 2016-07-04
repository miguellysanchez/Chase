package com.voyager.chase.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.voyager.chase.R;
import com.voyager.chase.dialog.RequestUsernameDialog;
import com.voyager.chase.utility.PreferenceUtility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by miguellysanchez on 6/26/16.
 */
public class HomeActivity extends AppCompatActivity {

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
        String username = PreferenceUtility.getInstance(this).getMqttClientId();
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
                        String.format("Welcome, %s", PreferenceUtility.getInstance(HomeActivity.this).getMqttClientId()));
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
        onBackPressed();
    }

}
