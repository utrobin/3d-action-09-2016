package ru.javajava;

import org.springframework.web.socket.WebSocketSession;
import ru.javajava.mechanics.base.Coords;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ivan on 15.11.16.
 */
public final class MapForTwoUsers {
    private static Map<Long, WebSocketSession> sessionToId = new HashMap<>();
    private static Coords firstCoords;
    private static Coords secondCoords;

    private MapForTwoUsers() {}
    public static boolean isItFirst(WebSocketSession session) {
        return sessionToId.get(1L).equals(session);
    }

    public  static void remove(WebSocketSession session) {
        sessionToId.remove(session);
    }
    public static  void addFirst(WebSocketSession session) {
        if (sessionToId.containsValue(session)) {
            throw new RuntimeException("It already contains this user...");
        }
        sessionToId.put(1L, session);
    }
    public static  void addSecond (WebSocketSession session) {
        if (sessionToId.containsValue(session)) {
            throw new RuntimeException("It already contains this user...");
        }
        sessionToId.put(2L, session);
    }

    public static  String getFirstCoords() {
        return firstCoords.toJSON().toString();
    }
    public  static String getSeconsCoords() {
        return secondCoords.toJSON().toString();
    }

    public static  void setFirstCoords(double x, double y, double z) {
        firstCoords = new Coords(x, y ,z);
    }

    public static void clear() {
        sessionToId.clear();
    }

    public  static void setSecondCoords(double x, double y, double z) {
        secondCoords = new Coords(x, y ,z);
    }

    public  static WebSocketSession getFirst() {
        return sessionToId.get(1L);
    }

    public  static WebSocketSession getSecond() {
        return sessionToId.get(2L);
    }
}