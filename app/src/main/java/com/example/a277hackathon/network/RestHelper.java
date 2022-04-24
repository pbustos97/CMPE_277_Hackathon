package com.example.a277hackathon.network;

import android.content.Context;
import android.util.JsonReader;
import android.util.Log;

import com.example.a277hackathon.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class RestHelper {
    private Context ctx;
    private String type;

    public static JSONObject getJSON(String country, String indicator) throws JSONException {
        String jsonString = "";
        try {
            String a = URLEncoder.encode(country);
            URL url = new URL("https://tzstkiqn45.execute-api.us-east-1.amazonaws.com/prod/finances?"
                    + "country="+ URLEncoder.encode(country)
                    + "&indicator=" + URLEncoder.encode(indicator));
            Log.d("RestHelper", "getJSON url: " + url.toString());
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            Log.d("RestHelper", "connection created");
            connection.setRequestMethod("GET");
            Log.d("RestHelper", "set request method");
            connection.connect();

            Log.d("RestHelper", "getJSON Connected");

            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                Log.d("RestHelper", "getJSON read: " + line);
                sb.append(line + "\n");
            }
            br.close();

            jsonString = sb.toString();
            Log.d("RestHelper", "getJSON: " + jsonString);

        } catch (Exception e) {
            Log.e("RestHelper", "getJSON: " + e.getMessage());
        }
        return new JSONObject(jsonString);
    }
}