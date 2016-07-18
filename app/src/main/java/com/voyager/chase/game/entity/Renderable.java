package com.voyager.chase.game.entity;

/**
 * Created by miguellysanchez on 7/18/16.
 */
public class Renderable {

    public static final int HIDDEN = 0;
    public static final int PROXIMITY_VISIBLE = 1;
    public static final int GLOBALLY_VISIBLE = 2;

    protected int mOwnerVisibility;
    protected int mNonOwnerVisibility;
}
