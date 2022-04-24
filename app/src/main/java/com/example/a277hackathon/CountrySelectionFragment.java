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

public class CountrySelectionFragment extends Fragment {

    private AutoCompleteTextView autoCompleteTextView;
    private ArrayList<String> mCountryList;
    private String selectedCountry;
    private int selectedCountryPos;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CountrySelectionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("CountrySelectionFragment", "onCreate");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            this.mCountryList = getArguments().getStringArrayList("countries");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("CountrySelectionFragment", "onCreateView start");
        View view = inflater.inflate(R.layout.fragment_country_selection, container, false);
        this.autoCompleteTextView = (AutoCompleteTextView)view.findViewById(R.id.country_selector);

        if (getArguments() != null) {
            ArrayAdapter arrayAdapter = new ArrayAdapter(requireContext(), R.layout.dropdown_item, this.mCountryList);
            this.autoCompleteTextView.setAdapter(arrayAdapter);
            this.autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.d("CountrySelectionFragment OnItemClick", "Selected country " + mCountryList.get(i));
                    selectedCountryPos = i;
                    selectedCountry = mCountryList.get(i);
                }
            });
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ArrayAdapter arrayAdapter = new ArrayAdapter(requireContext(), R.layout.dropdown_item, this.mCountryList);
        this.autoCompleteTextView.setAdapter(arrayAdapter);
    }
}