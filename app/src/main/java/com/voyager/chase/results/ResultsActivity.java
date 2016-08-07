package com.voyager.chase.results;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.voyager.chase.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by miguellysanchez on 8/8/16.
 */
public class ResultsActivity extends AppCompatActivity {

    public static final String KEY_RESULT = ResultsActivity.class.getCanonicalName() + ".result";

    public static final String RESULT_SPY_WINNER_SABOTEUR = "result_spy_winner_saboteur";
    public static final String RESULT_SPY_WINNER_TRAPPER = "result_spy_winner_trapper";
    public static final String RESULT_SENTRY_WINNER = "result_sentry_winner";
    public static final String RESULT_DRAW = "result_draw";
    public static final String RESULT_PARTNER_FORFEIT ="result_partner_forfeit";
    public static final String RESULT_PARTNER_DISCONNECTED = "result_partner_disconnected";
    public static final String RESULT_USER_FORFEIT ="result_user_forfeit";
    public static final String RESULT_USER_DISCONNECTED = "result_user_disconnected";

    @BindView(R.id.chase_activity_results_title)
    TextView mTextViewTitle;

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
        switch (result){
            case RESULT_SPY_WINNER_SABOTEUR:
                break;
            case RESULT_SPY_WINNER_TRAPPER:
                break;
            case RESULT_SENTRY_WINNER:
                break;
            case RESULT_DRAW:
                break;
            case RESULT_PARTNER_FORFEIT:
                break;
            case RESULT_PARTNER_DISCONNECTED:
                break;
            case RESULT_USER_FORFEIT:
                break;
            case RESULT_USER_DISCONNECTED:
                break;
        }
    }


}
