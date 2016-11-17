package ru.javajava.websocket;

/**
 * Created by ivan on 14.11.16.
 */
public class Message {
    private String type;
    private String content;

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
        //noinspection ConstantConditions
        this(clazz.getName(), content);
    }
}
