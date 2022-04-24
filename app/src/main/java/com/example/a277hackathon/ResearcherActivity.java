package com.example.a277hackathon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;

import com.example.a277hackathon.network.RestHelper;
import com.github.mikephil.charting.charts.Chart;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ResearcherActivity extends AppCompatActivity {

    private ArrayList<String> mCountries = new ArrayList<String>();
    private int countrySelection;
    private String graphSelection;
    private JSONObject obj;
    private List<List<List<Float>>> graphData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_researcher);

        mCountries.add("USA");
        mCountries.add("China");
        mCountries.add("India");


        this.addFragment(R.id.researcher_container_country, CountrySelectionFragment.class);
        this.addFragment(R.id.researcher_container_logo, LogoFragment.class);
        this.addFragment(R.id.graph_selection_researcher, GraphSelectionFragment.class);
//        this.addFragment2(R.id.researcher_container_graph, new ChartFragment(this.graphData));
        this.addFragment2(R.id.researcher_container_annotation, AnnotationFragment.newInstance());
    }

    private void addFragment(int id, Class cClass) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("countries", this.mCountries);

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(id, cClass, bundle)
                .commit();
    }
    private void addFragment2(int id, Fragment annotation) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("countries", this.mCountries);

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(id, annotation)
                .commit();
    }

    private void removeFragments() {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment.getClass() == ChartFragment.class) {
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
            Log.d("GovernmentActivity", "fragment: " + fragment.toString());
        }
    }

    public void setGraphSelection(String graph) {
        Log.d("ResearcherActivity", "setGraphSelection: " + graph);
        this.graphSelection = graph;
        String indicator = "GNI (current US$)";
        RestHelper helper = new RestHelper(mCountries.get(this.countrySelection), indicator, "gdp", this.graphSelection, ResearcherActivity.this, getApplicationContext());
        helper.start();
    }

    public void setCountrySelection(int i) {
        this.countrySelection = i;
    }

    public void setData(JSONObject obj) {
        this.obj = obj;
        try {
            JSONArray outerArray = (JSONArray) obj.get("data");
            List<List<Float>> data = new ArrayList<List<Float>>();
            for (int i = 0; i < outerArray.length(); i++) {
                List<Float> innerList = new ArrayList<Float>();
                JSONArray innerArray = (JSONArray) outerArray.get(i);
                Log.d("ResearcherActivity", "setData outerArray: " + outerArray.get(i).toString());
                for (int j = 0; j < innerArray.length(); j++) {
                    Log.d("ResearcherActivity", "setData innerArray: " + innerArray.get(j).toString());
                    innerList.add(Float.valueOf(innerArray.get(j).toString()));
                }
                data.add(innerList);
            }
            this.graphData = new ArrayList<>();
            this.graphData.add(data);
            this.removeFragments();
            this.addFragment2(R.id.researcher_container_graph, new ChartFragment(this.graphData));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}