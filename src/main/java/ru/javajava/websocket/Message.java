package ru.javajava.websocket;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ivan on 14.11.16.
 */
public class Message {
    @JsonProperty("type")
    private String type;
    @JsonProperty("data")
    private String content;

    public static final String INITIALIZE_USER = "InitializePlayer";
    public static final String SNAPSHOT = "Snapshot";
    public static final String REMOVE_USER = "RemovePlayer";

    public String getType() {
        return type;
    }
    public String getContent() {
        return content;
    }

    public Message() {
    }

    public Message(String type, String content) {
        this.type = type;
        this.content = content;
    }

    public Message(Class clazz, String content) {
        this(clazz.getName(), content);
    }
}
