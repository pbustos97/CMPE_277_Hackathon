package com.example.a277hackathon;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;

public class YearSelectionFragment extends Fragment {

    private AutoCompleteTextView autoCompleteTextView;
    private ArrayList<String> yearList;
    private String selectedYear;
    private int selectedYearPos;

    private static final String ARG_PARAM1 = "years";

    public YearSelectionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("YearSelectionFragment", "onCreate");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.yearList = getArguments().getStringArrayList(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("YearSelectionFragment", "onCreateView start");
        View view = inflater.inflate(R.layout.fragment_year_selection, container, false);
        this.autoCompleteTextView = (AutoCompleteTextView)view.findViewById(R.id.country_selector);

        if (getArguments() != null) {
            ArrayAdapter arrayAdapter = new ArrayAdapter(requireContext(), R.layout.dropdown_item, this.yearList);
            this.autoCompleteTextView.setAdapter(arrayAdapter);
            this.autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.d("YearSelectionFragment OnItemClick", "Selected year " + yearList.get(i));
                    selectedYearPos = i;
                    selectedYear = yearList.get(i);
                    selectCountry(selectedYearPos);
                }
            });
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ArrayAdapter arrayAdapter = new ArrayAdapter(requireContext(), R.layout.dropdown_item, this.yearList);
        this.autoCompleteTextView.setAdapter(arrayAdapter);
    }

    private void selectCountry(int country) {
        Log.d("YearSelectionFragment", "selectGraph: " + country);
        Class parentClass = getActivity().getClass();
        Log.d("YearSelectionFragment", "parentClass: " + parentClass.toString());
        if (parentClass == GovernmentActivity.class) {
            ((GovernmentActivity)getActivity()).setCountrySelection(country);
        } else if (parentClass == ResearcherActivity.class) {
            ((ResearcherActivity)getActivity()).setCountrySelection(country);
        }
    }
}