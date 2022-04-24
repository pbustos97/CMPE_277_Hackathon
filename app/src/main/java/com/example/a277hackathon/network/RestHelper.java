package com.example.a277hackathon.network;

import android.content.Context;
import android.util.JsonReader;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.UnusedAppRestrictionsConstants;

import com.example.a277hackathon.GovernmentActivity;
import com.example.a277hackathon.MainActivity;
import com.example.a277hackathon.ResearcherActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.List;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class RestHelper extends Thread {
    private String country;
    private String indicator;
    private String filter;
    private String endpoint;
    private AppCompatActivity activity;
    private Context ctx;

    public RestHelper(String country, String indicator, String filter, String endpoint, AppCompatActivity activity, Context ctx) {
        this.country = country;
        this.indicator = indicator;
        this.filter = filter;
        this.endpoint = endpoint;
        this.ctx = ctx;
        this.activity = activity;
    }

    private JSONObject getJSON(String endpoint) throws JSONException {
        String jsonString = "";
        String a = "";
        if (endpoint.equals("Debt")) {
            a = "https://tzstkiqn45.execute-api.us-east-1.amazonaws.com/prod/finances?" + "country="+ URLEncoder.encode(country)
                    + "&indicator=" + URLEncoder.encode(indicator);
        } else if (endpoint.equals("GDP")) {
            a = "https://tzstkiqn45.execute-api.us-east-1.amazonaws.com/prod/gdp?" + "country="+ URLEncoder.encode(country)
                    + "&indicator=" + URLEncoder.encode(filter);
        } else if (endpoint.equals("Agriculture")) {
            a = "https://tzstkiqn45.execute-api.us-east-1.amazonaws.com/prod/agriculture?" + "country="+ URLEncoder.encode(country)
                    + "&indicator=" + URLEncoder.encode(indicator);
        } else {
            return null;
        }
        try {
            URL url = new URL(a);
            Log.d("RestHelper", "getJSON url: " + url.toString());
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.setDoOutput(true);
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
            Log.d("RestHelper", "getJSON jsonString: " + jsonString);

        } catch (Exception e) {
            Log.e("RestHelper", "getJSON error: " + e.getMessage());
        }
        return new JSONObject(jsonString);
    }

    private String saveJson(JSONObject object) {
        File dir = new File(ctx.getFilesDir(), "downloads");
        String file = dir.toString() + "/csv" + this.country
                + "_" + this.indicator + ".json";
        if (!dir.exists()) {
            dir.mkdir();
        }

        Log.d("RestHelper", "saveJson");
        try {
            FileOutputStream output = new FileOutputStream(file);
            output.write(object.getString("data").getBytes(), 0, object.getString("data").length());

            JSONArray jsonArray = (JSONArray)object.get("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONArray jsonArray1 = (JSONArray) jsonArray.get(i);
                Log.d("jsonArray1", jsonArray1.toString());

            }

            output.flush();
            output.close();
        } catch (FileNotFoundException | JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    @Override
    public void run() {
        super.run();
        try {
            JSONObject object = this.getJSON(endpoint);
            this.saveJson(object);
            Log.d("RestHelper", "run: "+ object.toString());
            if (activity.getClass() == MainActivity.class) {
                ((MainActivity)activity).setJsonObject(object);
            } else if (activity.getClass() == ResearcherActivity.class) {
                ((ResearcherActivity)activity).setData(object);
            } else if (activity.getClass() == GovernmentActivity.class) {
                ((GovernmentActivity)activity).setData(object);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}