package com.voyager.chase.game.views;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.voyager.chase.R;

/**
 * Created by miguellysanchez on 7/4/16.
 */
public class TileView extends LinearLayout {

    LinearLayout linearLayoutContainer;
    FrameLayout frameLayoutPlayerIndicator;
    ImageView imageViewPlayer;
    ImageView imageViewConstructs;
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

    public void setImageViewPlayer(ImageView imageViewPlayer) {
        this.imageViewPlayer = imageViewPlayer;
    }

    public ImageView getImageViewConstructs() {
        return imageViewConstructs;
    }

    public void setImageViewConstructs(ImageView imageViewConstructs) {
        this.imageViewConstructs = imageViewConstructs;
    }

    public View getViewFog() {
        return viewFog;
    }

    public void setViewFog(View viewFog) {
        this.viewFog = viewFog;
    }

    public void setHighlightBackground(boolean isHighlighted) {
        if (isHighlighted) {
            linearLayoutContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.azure));
        } else {
            linearLayoutContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.black));
        }
    }
}
