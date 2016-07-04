package com.voyager.chase.mqtt;

import android.support.annotation.NonNull;

import com.voyager.chase.BuildConfig;

/**
 * Created by miguellysanchez on 6/30/16.
 */
public class Topics {

    public static final String BASE_TOPIC_PREFIX = BuildConfig.BASE_MQTT_TOPIC;
    public static final String LOBBY_TOPIC = constructTopic(BASE_TOPIC_PREFIX, "lobby");

    public static String constructTopic(@NonNull String... topicLevels) {
        StringBuilder topicBuilder = new StringBuilder();
        for (int i = 0; i < topicLevels.length; i++) {
            topicBuilder.append(topicLevels[i]);
            if (i + 1 < topicLevels.length) {
                topicBuilder.append("/");
            }
        }
        return topicBuilder.toString();
    }
}
