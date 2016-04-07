package com.example.owner.BridgeCommunication;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Joseph Gregory on 2/15/2016.
 */
public class ResponseParser {

    public static String parseMessage(String response){
        JSONObject jsonObject = null;
        String message = null;
        try {
           jsonObject = new JSONObject(response);
            message = jsonObject.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }

    public static String[] parseAddResponse(String response){
        JSONObject jsonObject = null;
        String id = null;
        String password = null;
        try {
            jsonObject = new JSONObject(response);
            id = jsonObject.getString("id");
            password = jsonObject.getString("password");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new String[] {id, password};
    }

    public static String parseSendResponse(String response){
        JSONObject jsonObject = null;
        String responseStatus = null;
        try {
            jsonObject = new JSONObject(response);
            responseStatus = jsonObject.getString("result");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return responseStatus;
    }
}
