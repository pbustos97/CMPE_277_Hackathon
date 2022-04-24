package com.example.a277hackathon;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

@SuppressLint("SetTextI18n")
public class AnnotationFragment extends Fragment {
    private String existingAnnotationId = null;
    private SQLiteDatabase annotationDB;
    private HashMap<String,String> titleMapping;

    private EditText titleField;
    private EditText bodyField;
    private Button saveButton;
    private TextView titleText;

    public AnnotationFragment() {

    }

    public static AnnotationFragment newInstance(String existingAnnotationId) {
        AnnotationFragment fragment = new AnnotationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.setExistingAnnotationId(existingAnnotationId);
        return fragment;
    }

    public static AnnotationFragment newInstance() {
        AnnotationFragment fragment = new AnnotationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    protected void setExistingAnnotationId(String existingAnnotationId) {
        this.existingAnnotationId = existingAnnotationId;
    }

    @Override
    public void onDestroy() {
        annotationDB.close();
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        annotationDB = SQLiteDatabase.openOrCreateDatabase(
                getContext().getApplicationInfo().dataDir + "/annotation.db", null);
        annotationDB.execSQL(
                "create table if not exists Annotations (id INTEGER PRIMARY KEY,title TEXT,body TEXT);");

        this.readDBToHashMap();

        View view = inflater.inflate(R.layout.fragment_annotation, container, false);
        titleText = view.findViewById(R.id.titleText);
        titleField = view.findViewById(R.id.titleField);
        bodyField = view.findViewById(R.id.bodyField);
        saveButton = view.findViewById(R.id.saveAnnoBtn);

        saveButton.setOnClickListener(view1 -> {
            String titleFieldTxt = titleField.getText().toString();
            String bodyFieldTxt = bodyField.getText().toString();

            ContentValues values = new ContentValues();
            values.put("title", titleFieldTxt);
            values.put("body", bodyFieldTxt);

            annotationDB.insert("Annotations", null, values);
            this.readDBToHashMap();
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (this.existingAnnotationId != null && this.existingAnnotationId.length() > 0) {
            Cursor cursor = annotationDB.query(
                    "Annotations",
                    null,
                    "id=" + existingAnnotationId,
                    null,
                    null,
                    null,
                    null);

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                int titleIdx = cursor.getColumnIndexOrThrow("title");
                int bodyIdx = cursor.getColumnIndexOrThrow("body");

                String title = cursor.getString(titleIdx);
                String body = cursor.getString(bodyIdx);

                titleText.setText("Existing annotation");
                titleField.setText(title);
                bodyField.setText(body);
            }
        }
    }

    protected void readDBToHashMap() {
        Cursor cursor = annotationDB.query(
                "Annotations",
                null,
                null,
                null,
                null,
                null,
                null);
        HashMap<String,String> annotationTitles = new HashMap<>();
        while(cursor.moveToNext()) {
            int titleIdx = cursor.getColumnIndexOrThrow("title");
            int idIdx = cursor.getColumnIndexOrThrow("id");
            String title = cursor.getString(titleIdx);
            long id = cursor.getLong(idIdx);
            annotationTitles.put(String.valueOf(id), title);
        }
        this.titleMapping = annotationTitles;
        cursor.close();
    }

    public HashMap<String,String> getAllAnnotations() {
        return this.titleMapping;
    }
}