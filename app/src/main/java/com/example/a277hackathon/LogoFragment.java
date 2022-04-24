package com.example.a277hackathon;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LogoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogoFragment extends Fragment {
    private TextView mLogoTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logo, container, false);
        this.mLogoTextView = (TextView)view.findViewById(R.id.text_view_logo);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.mLogoTextView.setText("Team No Idea");
    }
}