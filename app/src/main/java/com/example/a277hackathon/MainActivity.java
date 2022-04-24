package com.example.a277hackathon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.a277hackathon.network.RestHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button mResearcherActivity;
    private Button mGovernmentActivity;
    private JSONObject tmpObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mResearcherActivity = (Button)findViewById(R.id.button_researcher);
        this.mGovernmentActivity = (Button)findViewById(R.id.button_government);

        this.mResearcherActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeActivity("Researcher");
            }
        });
        this.mGovernmentActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeActivity("Government");
            }
        });

        requestPermissions();
        try {
            RestHelper helper = new RestHelper("India", "GNI (current US$)", MainActivity.this, getApplicationContext());
            helper.start();
//            Thread.sleep(5000);
        } catch (Exception e) {
            Log.e("MainActivity", "onResume: " + e.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity", "onResume");
    }

    private boolean changeActivity(String selectedActivity) {
        Bundle bundle = new Bundle();
        bundle.putString("activityName", selectedActivity);
        Log.d("MainActivity", tmpObject.toString());
        try {
            Log.d("MainActivity", tmpObject.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Class selectedClass = null;

        switch (selectedActivity) {
            case "Government":
                selectedClass = GovernmentActivity.class;
                break;
            case "Researcher":
                selectedClass = ResearcherActivity.class;
                break;
            default:
                return false;
        }

        if (selectedActivity.equals("Government")) {
            selectedClass = GovernmentActivity.class;
        } else if (selectedActivity.equals("Researcher")) {
            selectedClass = ResearcherActivity.class;
        }
        Intent intent = new Intent(MainActivity.this, selectedClass);
        startActivity(intent);
        return true;
    }

    public void setJsonObject(JSONObject object) {
        this.tmpObject = object;
    }

    private void requestPermissions() {
        ArrayList<String> permissionsToRequest = new ArrayList<String>();
        if (!hasAccessNetworkState()) {
            permissionsToRequest.add(Manifest.permission.ACCESS_NETWORK_STATE);
        }
        if (!hasInternet()) {
            permissionsToRequest.add(Manifest.permission.INTERNET);
        }
        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toArray(new String[permissionsToRequest.size()]), 0);
        }
    }

    private boolean hasInternet() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private boolean hasAccessNetworkState() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }
}