package com.example.owner.bridgecommunication;

/**
 * Created by Joseph Gregory on 2/15/2016.
 */
public class MessageBuilder {

    public static String buildMessage(String command, String param1) {
        return command + ":" + param1;
    }
    public static String buildMessage(String command, String param1, String param2) {
        return command + ":" + param1 + ":" + param2;
    }
    public static String buildMessage(String command, String param1, String param2, String param3) {
        return command + ":" + param1 + ":" + param2 + ":" + param3;
    }
}
