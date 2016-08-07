package com.voyager.chase.game.views;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.voyager.chase.R;

/**
 * Created by miguellysanchez on 7/4/16.
 */
public class TileView extends LinearLayout {

    LinearLayout linearLayoutContainer;
    FrameLayout frameLayoutPlayerIndicator;
    ImageView imageViewPlayer;
    ImageView imageViewConstructs;
    TextView textViewDestination;
    View viewFog;

    public TileView(Context context) {
        super(context);
        init();
    }

    public TileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.chase_tileview, this);
        linearLayoutContainer = (LinearLayout) findViewById(R.id.chase_tileview_linearlayout_container);
        frameLayoutPlayerIndicator = (FrameLayout) findViewById(R.id.chase_tileview_framelayout_player_indicator);
        imageViewPlayer = (ImageView) findViewById(R.id.chase_tileview_imageview_player);
        imageViewConstructs = (ImageView) findViewById(R.id.chase_tileview_imageview_item);
        textViewDestination = (TextView) findViewById(R.id.chase_tileview_textview_destination);
        viewFog = findViewById(R.id.chase_tileview_view_fog);
    }

    public LinearLayout getLinearLayoutContainer() {
        return linearLayoutContainer;
    }

    public FrameLayout getFrameLayoutPlayerIndicator(){
        return frameLayoutPlayerIndicator;
    }

    public ImageView getImageViewPlayer() {
        return imageViewPlayer;
    }

    public ImageView getImageViewConstructs() {
        return imageViewConstructs;
    }

    public View getViewFog() {
        return viewFog;
    }

    public void setHighlightBackground(boolean isHighlighted) {
        if (isHighlighted) {
            linearLayoutContainer.setBackgroundResource(R.drawable.chase_drawable_tile_highlighted);
        } else {
            linearLayoutContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.black));
        }
    }

    public TextView getTextViewDestination(){
        return textViewDestination;
    }
}
