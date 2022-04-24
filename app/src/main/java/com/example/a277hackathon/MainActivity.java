package com.example.a277hackathon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.a277hackathon.network.RestHelper;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    private Button mResearcherActivity;
    private Button mGovernmentActivity;

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity", "onResume");
        try {
            Log.d("MainActivity", RestHelper.getJSON("India", "GNI (current US$)").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean changeActivity(String selectedActivity) {
        Bundle bundle = new Bundle();
        bundle.putString("activityName", selectedActivity);

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
}