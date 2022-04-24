package com.example.a277hackathon;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class GraphSelectionFragment extends Fragment {

    private Button mButtonGdp;
    private Button mButtonAgriculture;
    private Button mButtonDebt;
    private String selectedGraph;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph_selection, container, false);
        this.mButtonGdp = (Button)view.findViewById(R.id.button_macroeconomic);
        this.mButtonGdp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedGraph = "GDP";
                selectGraph(selectedGraph);
            }
        });
        this.mButtonAgriculture = (Button)view.findViewById(R.id.button_agriculture);
        this.mButtonAgriculture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedGraph = "Agriculture";
                selectGraph(selectedGraph);
            }
        });
        this.mButtonDebt = (Button)view.findViewById(R.id.button_debt);
        this.mButtonDebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedGraph = "Debt";
                selectGraph(selectedGraph);
            }
        });
        return view;
    }

    private void selectGraph(String graphString) {
        Log.d("GraphSelectionFragment", "selectGraph: " + graphString);
        Class parentClass = getActivity().getClass();
        Log.d("GraphSelectionFragment", "parentClass: " + parentClass.toString());
        if (parentClass == GovernmentActivity.class) {
            ((GovernmentActivity)getActivity()).setGraphSelection(graphString);
        } else if (parentClass == ResearcherActivity.class) {
            ((ResearcherActivity)getActivity()).setGraphSelection(graphString);
        }
    }
}