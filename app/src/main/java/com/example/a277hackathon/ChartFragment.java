package com.example.a277hackathon;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

public class ChartFragment extends Fragment {
    private LineChart lc;
    private List<List<List<Float>>> data;

    public ChartFragment(List<List<List<Float>>> data) {
        this.data = data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        lc = view.findViewById(R.id.lineChart);

        ArrayList<ILineDataSet> datasets = new ArrayList<>();

        for (List<List<Float>> level1 : this.data) {
            ArrayList<Entry> values = new ArrayList<>();
            for (List<Float> level2 : level1) {
                values.add(new Entry(level2.get(0), level2.get(1)));
            }
            LineDataSet lineDataset = new LineDataSet(values, "Country");
            lineDataset.setLineWidth(3);
            lineDataset.setColor(getResources().getColor(R.color.black));
            lineDataset.setDrawValues(false);

            datasets.add(lineDataset);
        }

        LineData data = new LineData(datasets);

        lc.setData(data);
        return view;
    }
}