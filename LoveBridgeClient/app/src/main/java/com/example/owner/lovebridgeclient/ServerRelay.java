package com.example.owner.lovebridgeclient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Joseph Gregory on 4/19/2016.
 */
public class ServerRelay {
    public static final String DEFAULT_URL = "http://152.14.106.21:3000";
    private static String URL = DEFAULT_URL;


    public static String pingServer(String name,  String latitude, String longitude, String range) {
        String urlParams = "/registerBridge";
        String bodyParams = null;
        bodyParams = "name=" + name + "&lat=" + latitude + "&lng=" + longitude + "&range=" + range;
        ;
        return sendPostToServer(urlParams, bodyParams);
    }


    public static String sendPostToServer(String urlParameters, String bodyParameters) {
        HttpURLConnection conn = null;
        try {
            //Create connection
//            URL url = new URL("http://www.android.com/");
            URL url = new URL(URL + urlParameters);


            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Content-Type", "application/json");

//            conn.setRequestProperty("Content-Length",Integer.toString(urlParameters.getBytes().length));
//            conn.setRequestProperty("Content-Language", "en-US");

            conn.setUseCaches(false);
            conn.setDoOutput(true);

            //Add to body
            conn.getOutputStream().write(bodyParameters.getBytes());
//            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
//            wr.writeBytes(bodyParameters);
//            wr.close();

            //Get Response
            InputStream is = conn.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder();
            String line;
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }


            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}