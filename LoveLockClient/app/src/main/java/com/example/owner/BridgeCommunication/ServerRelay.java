package com.example.owner.bridgecommunication;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Joseph Gregory on 2/15/2016.
 */
public class ServerRelay {
    public static final String DEFAULT_URL = "asdfafsd";
    private String URL;
    public ServerRelay() {
        this(DEFAULT_URL);
    }
    public ServerRelay(String URL) {
        this.URL = URL;
    }

    public String sendToServer(String urlParameters) {
        HttpURLConnection conn = null;
        try {
            //Create connection
            URL url = new URL(URL);
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            conn.setRequestProperty("Content-Length",
                    Integer.toString(urlParameters.getBytes().length));
            conn.setRequestProperty("Content-Language", "en-US");

            conn.setUseCaches(false);
            conn.setDoOutput(true);


            //Send request
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.close();

            //Get Response
            InputStream is = conn.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder();
            String line;
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
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

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}
