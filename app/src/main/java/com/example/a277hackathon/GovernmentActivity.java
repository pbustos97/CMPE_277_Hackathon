package com.example.a277hackathon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;

import com.example.a277hackathon.network.RestHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GovernmentActivity extends AppCompatActivity {

    private ArrayList<String> mCountries = new ArrayList<String>();
    private String graphSelection;
    private int countrySelection;
    private JSONObject obj;
    private List<List<List<Float>>> graphData;


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

    private void removeFragments() {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment.getClass() == ChartFragment.class) {
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
            Log.d("GovernmentActivity", "fragment: " + fragment.toString());
        }
    }

    public void setGraphSelection(String graph) {
        Log.d("GovernmentActivity", "setGraphSelection: " + graph);
        this.graphSelection = graph;
        String indicator = "GNI (current US$)";
        RestHelper helper = new RestHelper(mCountries.get(this.countrySelection), indicator, "gdp", this.graphSelection, GovernmentActivity.this, getApplicationContext());
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
                Log.d("GovernmentActivity", "setData outerArray: " + outerArray.get(i).toString());
                for (int j = 0; j < innerArray.length(); j++) {
                    Log.d("GovernmentActivity", "setData innerArray: " + innerArray.get(j).toString());
                    innerList.add(Float.valueOf(innerArray.get(j).toString()));
                }
                data.add(innerList);
            }
            this.graphData = new ArrayList<>();
            this.graphData.add(data);
            this.removeFragments();
            this.addFragment2(R.id.government_container_graph, new ChartFragment(this.graphData));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}