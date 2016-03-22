package com.example.owner.BridgeCommunication;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Joseph Gregory on 2/15/2016.
 */
public class ResponseParser {

    public String parseMessage(String response){
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
}
