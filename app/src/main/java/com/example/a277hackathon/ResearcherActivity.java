package com.example.a277hackathon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;

import com.example.a277hackathon.Database.DatabaseHelper;
import com.example.a277hackathon.Model.Trade;
import com.example.a277hackathon.network.RestHelper;
import com.github.mikephil.charting.charts.Chart;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ResearcherActivity extends AppCompatActivity {

    private ArrayList<String> mCountries = new ArrayList<String>();
    private ArrayList<String> mYears = new ArrayList<String>();
    private int countrySelection;
    private String graphSelection;
    private JSONObject obj;
    private DatabaseHelper databaseHelper;
    private List<List<List<Float>>> graphData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_researcher);

        mCountries.add("USA");
        mCountries.add("China");
        mCountries.add("India");
        databaseHelper = new DatabaseHelper(this);

        this.addFragment(R.id.researcher_container_country, CountrySelectionFragment.class);
        this.addFragment(R.id.researcher_container_logo, LogoFragment.class);
        this.addFragment(R.id.graph_selection_researcher, GraphSelectionFragment.class);
        this.addFragment2(R.id.researcher_container_annotation, AnnotationFragment.newInstance());
    }

    private void addFragment(int id, Class cClass) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("countries", this.mCountries);
        bundle.putStringArrayList("years", this.mYears);

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
            if (fragment.getClass() == YearSelectionFragment.class) {
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
            Log.d("GovernmentActivity", "fragment: " + fragment.toString());
        }
    }

    public void setGraphSelection(String graph) {
        Log.d("ResearcherActivity", "setGraphSelection: " + graph);
        this.graphSelection = graph;
        String indicator = "GNI (current US$)";
        if (this.graphSelection.equals("Agriculture")) {
            indicator = "Fertilizer consumption (kilograms per hectare of arable land)";
        }
        RestHelper helper = new RestHelper(mCountries.get(this.countrySelection), indicator, "gdp", this.graphSelection, ResearcherActivity.this, getApplicationContext());
        helper.start();
    }

    public void setCountrySelection(int i) {
        this.countrySelection = i;
    }

    public void setData(JSONObject obj, String url) {
        this.obj = obj;
        try {
            JSONArray outerArray = (JSONArray) obj.get("data");
            List<List<Float>> data = new ArrayList<List<Float>>();
            for (int i = 0; i < outerArray.length(); i++) {
                List<Float> innerList = new ArrayList<Float>();
                JSONArray innerArray = (JSONArray) outerArray.get(i);
//                Log.d("ResearcherActivity", "setData outerArray: " + outerArray.get(i).toString());
                for (int j = 0; j < innerArray.length(); j++) {
//                    Log.d("ResearcherActivity", "setData innerArray: " + innerArray.get(j).toString());
                    innerList.add(Float.valueOf(innerArray.get(j).toString()));
                }
                data.add(innerList);
            }
            Trade trade = new Trade();
            trade.setUrl(url);
            trade.setDatapoint(data.toString());
            databaseHelper.insertTrade(trade);
            Log.i("*** tread_db--->", databaseHelper.readTreade(trade.getUrl()).toString());

            this.graphData = new ArrayList<>();
            this.graphData.add(data);
            this.removeFragments();
            //this.getYears(data);
            this.addFragment2(R.id.researcher_container_graph, new ChartFragment(this.graphData));
//            this.addFragment(R.id.researcher_container_year, YearSelectionFragment.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getYears(List<List<Float>> data) {
        Log.d("ResearcherActivity", "getYears");
        for (int i = 0; i < data.size(); i++) {
//            Log.d("RearcherActivity getYears", String.valueOf(data.get(i).toString()));
//            Log.d("RearcherActivity getYears2", String.valueOf(data.get(i).get(0).toString()));
            this.mYears.add(String.valueOf(data.get(i).get(0)));
        }
        Log.d("RearcherActivity getYears", this.mYears.toString());
    }
}