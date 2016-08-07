package com.voyager.chase.results;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.voyager.chase.R;
import com.voyager.chase.game.entity.player.Player;
import com.voyager.chase.utility.PreferenceUtility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by miguellysanchez on 8/8/16.
 */
public class ResultsActivity extends AppCompatActivity {

    public static final String KEY_RESULT = ResultsActivity.class.getCanonicalName() + ".result";

    public static final String RESULT_SPY_WINNER_SABOTEUR = "result_spy_winner_saboteur";
    public static final String RESULT_SPY_WINNER_TRAPPER = "result_spy_winner_trapper";
    public static final String RESULT_SENTRY_WINNER = "result_sentry_winner";
    public static final String RESULT_DRAW = "result_draw";
    public static final String RESULT_PARTNER_FORFEIT = "result_partner_forfeit";
    public static final String RESULT_PARTNER_DISCONNECTED = "result_partner_disconnected";
    public static final String RESULT_USER_FORFEIT = "result_user_forfeit";
    public static final String RESULT_USER_DISCONNECTED = "result_user_disconnected";

    @BindView(R.id.chase_activity_results_imageview_role)
    ImageView mImageViewRole;
    @BindView(R.id.chase_activity_results_textview_title)
    TextView mTextViewTitle;
    @BindView(R.id.chase_activity_results_imageview_icon)
    ImageView mImageViewIcon;
    @BindView(R.id.chase_activity_results_textview_subtext)
    TextView mTextViewSubtext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chase_activity_results);
        ButterKnife.bind(this);
        String result = getIntent().getStringExtra(KEY_RESULT);
        initializeViews(result);
    }

    private void initializeViews(String result) {
        mTextViewTitle.setText(result);
        String userRole = PreferenceUtility.getInstance(this).getGameRole();
        if (Player.SENTRY_ROLE.equals(userRole)) {
            mImageViewRole.setImageResource(R.drawable.chase_ic_player_sentry);
        } else if (Player.SPY_ROLE.equals(userRole)) {
            mImageViewRole.setImageResource(R.drawable.chase_ic_player_spy);
        }

        switch (result) {
            case RESULT_SPY_WINNER_SABOTEUR:
                if (Player.SENTRY_ROLE.equals(userRole)) {
                    viewResults("You LOSE!!", R.drawable.chase_ic_sabotaged, "You have failed to protect the objectives. As a SENTRY you are a disgrace. Play again?");
                } else if (Player.SPY_ROLE.equals(userRole)) {
                    viewResults("You WIN!!", R.drawable.chase_ic_winner, "Nice job on the victory SPY. You managed to destroy your objectives and avoid the SENTRY. Play again?");
                }
                break;
            case RESULT_SPY_WINNER_TRAPPER:
                if (Player.SENTRY_ROLE.equals(userRole)) {
                    viewResults("You LOSE!!", R.drawable.chase_ic_loser_death, "You were killed by the SPY. You failed to protect the base from destruction. Play again?");
                } else if (Player.SPY_ROLE.equals(userRole)) {
                    viewResults("You WIN!!", R.drawable.chase_ic_winner, "Congratulations, SPY. You weren't expected to do it, but you managed to take out the SENTRY. Play again?");
                }
                break;
            case RESULT_SENTRY_WINNER:
                if (Player.SENTRY_ROLE.equals(userRole)) {
                    viewResults("You WIN!!", R.drawable.chase_ic_winner, "Congratulations, SENTRY. You have protected the base from the SPY. Play again?");
                } else if (Player.SPY_ROLE.equals(userRole)) {
                    viewResults("You LOST!!", R.drawable.chase_ic_loser_death, "You were taken out by the SENTRY. Try to avoid him next time. Play again?");
                }
                break;
            case RESULT_DRAW:
                if (Player.SENTRY_ROLE.equals(userRole)) {
                    viewResults("DRAW!!", R.mipmap.ic_launcher, "You both died. Play again?.");
                } else if (Player.SPY_ROLE.equals(userRole)) {
                    viewResults("DRAW!!", R.mipmap.ic_launcher, "Oh no! both player were taken out. Play again?");
                }
                break;
            case RESULT_PARTNER_FORFEIT:
                viewResults("You WIN!!", R.drawable.chase_ic_winner, "Congratulations, " + userRole + "!! Your partner has forfeited. Play again?");
                break;
            case RESULT_PARTNER_DISCONNECTED:
                viewResults("GAME OVER!!", R.drawable.chase_ic_game_over, "Sorry, your partner has suddenly disconnected. Play again?");
                break;
            case RESULT_USER_FORFEIT:
                viewResults("You LOST!!", R.drawable.chase_ic_forfeit, "You have forfeited the match. Play again?");
                break;
            case RESULT_USER_DISCONNECTED:
                viewResults("DISCONNECTED!!", R.drawable.chase_ic_disconnected, "Woah, you were unexpectedly disconnected from your partner. Play again?");
                break;
        }
    }

    private void viewResults(String title, int resourceIconId, String subtext) {
        mTextViewTitle.setText(title);
        mTextViewSubtext.setText(subtext);
        mImageViewIcon.setImageResource(resourceIconId);
    }

    @OnClick(R.id.chase_activity_results_button_ok)
    public void onOkClicked() {
        onBackPressed();
    }

}
