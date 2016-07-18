package com.voyager.chase.mqtt;

import android.support.annotation.NonNull;

import com.voyager.chase.BuildConfig;

/**
 * Created by miguellysanchez on 6/30/16.
 */
public class Topics {

    public static final String BASE_TOPIC_PREFIX = BuildConfig.BASE_MQTT_TOPIC;
    public static final String LOBBY_TOPIC = constructTopic(BASE_TOPIC_PREFIX, "lobby");
    public static final String SESSION_TOPIC = constructTopic(BASE_TOPIC_PREFIX, "session");
    public static final String SESSION_WORLD_UPDATE_SUFFIX = "worldupdate";
    public static final String SESSION_STATUS_SUFFIX = "status";
    public static final String SESSION_INFO_SUFFIX = "info";

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

    //used for session meta data: last will and testament, pass turn to next player, etc.
    public static String getSessionStatusTopic(String sessionId) {
        return constructTopic(SESSION_TOPIC, sessionId, SESSION_STATUS_SUFFIX);
    }

    public static String getSessionWorldUpdateTopic(String sessionId) {
        return constructTopic(SESSION_TOPIC, sessionId, SESSION_WORLD_UPDATE_SUFFIX);
    }

    public static String getSessionInfoTopic(String sessionId) {
        return constructTopic(SESSION_TOPIC, sessionId, SESSION_INFO_SUFFIX);
    }

}
