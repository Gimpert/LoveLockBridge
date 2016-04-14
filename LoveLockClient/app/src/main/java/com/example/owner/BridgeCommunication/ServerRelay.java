package com.example.owner.BridgeCommunication;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Joseph Gregory on 2/15/2016.
 */
public class ServerRelay {
    public static final String DEFAULT_URL = "http://152.14.106.21:3000";
    private static String URL = DEFAULT_URL;

    /**
     * @return the lock's hidden message
     */
    public static String unlockLock(String lockId, String latitude, String longitude, String lockPswd) {
        String urlParams = "/openLock?id=" + lockId + "&lat=" + latitude + "&lng=" + longitude + "&pswd=" + lockPswd;
        return sendGetToServer(urlParams);
    }

    /**
     * @return a string that contains the lock password and id formatted as "pass:id"
     */
    public static String addLock(String lockName, String latitude, String longitude, String lockMessage) {
        String urlParams = "/addLock";
        String bodyParams = "lat=" + latitude + "&lng=" + longitude + "&message=" + lockMessage + "%20method&name=" + lockName;
        return sendPostToServer(urlParams, bodyParams);
    }

    public static String sendKey(String lockId, String pswd,String targetEmailAddress, String targetName ,String senderName, String senderMessage) {
        String urlParams = null;
        try {
            urlParams = "/sendKey?" + "id=" + lockId + "&pswd=" + pswd + "&targetEmail=" + URLEncoder.encode(targetEmailAddress, "UTF-8") + "&targetName=" + URLEncoder.encode(targetName, "UTF-8") + "&senderName=" + URLEncoder.encode(senderName, "UTF-8") + "&senderMessage=" + URLEncoder.encode(senderMessage, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sendGetToServer(urlParams);
    }

    public static boolean isInBridgeRange(String latitude, String longitude) {
        String urlParams = "/inRangeOfBridge?lat=" + latitude + "&lng=" + longitude;
        if (sendGetToServer(urlParams) == "true") {
            return true;
        }
        return false;
    }

    public static String sendGetToServer(String urlParameters) {
        HttpURLConnection conn = null;
        try {
            //Create connection
//            URL url = new URL("http://www.android.com/");
            URL url = new URL(URL + urlParameters);

            //conn.setRequestMethod("GET");
            conn = (HttpURLConnection) url.openConnection();
            //conn.setRequestProperty("Content-Type", "application/json");

            //conn.setRequestProperty("Content-Length",Integer.toString(urlParameters.getBytes().length));
//            conn.setRequestProperty("Content-Language", "en-US");

//            conn.setUseCaches(false);
//            conn.setDoOutput(true);


            //Send request
//            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
//            wr.writeBytes(urlParameters);
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
            if(conn != null) {
                conn.disconnect();
            }
        }
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

    public static String getURL() {
        return URL;
    }

    public static void setURL(String newURL) {
        URL = newURL;
    }


}
