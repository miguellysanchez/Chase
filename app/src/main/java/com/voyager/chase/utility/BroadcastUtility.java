package com.voyager.chase.utility;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by miguellysanchez on 6/28/16.
 */
public class BroadcastUtility {

    public static final String KEY_STRING_CALLBACK_ACTION = BroadcastUtility.class.getSimpleName() + ".CALLBACK_ACTION";
    public static final String KEY_STRING_CALLBACK_TYPE_VALUE = BroadcastUtility.class.getSimpleName() + ".CALLBACK_TYPE_VALUE";
    public static final String KEY_INT_CALLBACK_VALUE = BroadcastUtility.class.getSimpleName() + ".CALLBACK_VALUE";
    public static final int CALLBACK_VALUE_NULL = 0;

    public static void broadcastIntent(Context context, Intent intent, String callbackType, int callbackValue){
        intent.setAction(KEY_STRING_CALLBACK_ACTION);
        intent.putExtra(KEY_STRING_CALLBACK_TYPE_VALUE, callbackType);
        intent.putExtra(KEY_INT_CALLBACK_VALUE, callbackValue);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
