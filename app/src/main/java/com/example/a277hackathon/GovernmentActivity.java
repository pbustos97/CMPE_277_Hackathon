package com.example.a277hackathon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class GovernmentActivity extends AppCompatActivity {

    private ArrayList<String> mCountries = new ArrayList<String>();
    private String graphSelection;
    private int countrySelection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_government);

        mCountries.add("USA");
        mCountries.add("China");
        mCountries.add("India");

        List<Float> inner = new ArrayList<>();
        inner.add(1f);
        inner.add(2f);
        List<Float> inneralt = new ArrayList<>();
        inneralt.add(1f);
        inneralt.add(20f);
        List<List<Float>> inner2 = new ArrayList<>();
        inner2.add(inner);
        inner2.add(inneralt);
        List<List<List<Float>>> data = new ArrayList<>();
        data.add(inner2);

        this.addFragment(R.id.government_container_country, CountrySelectionFragment.class);
        this.addFragment(R.id.government_container_logo, LogoFragment.class);
        this.addFragment(R.id.graph_selection_gov, GraphSelectionFragment.class);
        this.addFragment2(R.id.government_container_graph, new ChartFragment(data));
    }

    private void addFragment(int id, Class cClass) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("countries", this.mCountries);

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(id, cClass, bundle)
                .commit();
    }

    private void addFragment2(int id, ChartFragment chart) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("countries", this.mCountries);

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(id, chart)
                .commit();
    }


    public void setGraphSelection(String graph) {
        Log.d("GovernmentActivity", "setGraphSelection: " + graph);
        this.graphSelection = graph;
    }

    public void setCountrySelection(int i) {
        this.countrySelection = i;
    }
}