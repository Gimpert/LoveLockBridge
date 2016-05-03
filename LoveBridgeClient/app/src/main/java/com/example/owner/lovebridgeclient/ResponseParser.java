package com.example.owner.lovebridgeclient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.UnknownFormatConversionException;

/**
 * Created by Joseph Gregory on 4/21/2016.
 */
public class ResponseParser {

    public static ArrayList<String> parseResponse(String response){
        JSONObject jsonObject = null;
        ArrayList<String> newClients = new ArrayList<String>();
        try {
            jsonObject = new JSONObject(response);
            JSONArray jsonArr = jsonObject.getJSONArray("nearbyClients");
            if (jsonArr != null) {
                System.out.println(jsonArr.toString());
                int len = jsonArr.length();
                for (int i=0;i<len;i++){
                    newClients.add(jsonArr.getString(i));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newClients;
    }

}