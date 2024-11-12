package com.example.fittrack;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import androidx.fragment.app.Fragment;

public class ExerciseSelectFragment extends Fragment implements View.OnClickListener {

    private ImageButton exerciseSelectBackButton;
    private ImageButton exerciseSelectSearchButton;
    private Button exerciseSelectNameFilterButton;
    private Button exerciseSelectMuscleFilterButton;
    private Button exerciseSelectEquipmentFilterButton;
    private Button exerciseSelectDifficultyFilterButton;
    private EditText exerciseSelectEditText;

    private URL url = null;

    private String search = "";
    private String filter = "";
    private Button selectedFilterButton;

    private final Executor executor = Executors.newSingleThreadExecutor();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_exercise_select, container, false);

        exerciseSelectBackButton = view.findViewById(R.id.exercise_select_back_btn);
        exerciseSelectSearchButton = view.findViewById(R.id.exercise_select_search_btn);
        exerciseSelectNameFilterButton = view.findViewById(R.id.exercise_select_name_filter_btn);
        exerciseSelectMuscleFilterButton = view.findViewById(R.id.exercise_select_muscle_filter_btn);
        exerciseSelectEquipmentFilterButton = view.findViewById(R.id.exercise_select_equipment_filter_btn);
        exerciseSelectDifficultyFilterButton = view.findViewById(R.id.exercise_select_difficulty_filter_btn);
        exerciseSelectEditText = view.findViewById(R.id.exercise_select_et);

        filter = "name";
        selectedFilterButton = exerciseSelectNameFilterButton;
        exerciseSelectNameFilterButton.setBackground(getResources().getDrawable(R.drawable.button_background_green));

        exerciseSelectBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        String apiKey = BuildConfig.API_KEY;

        exerciseSelectNameFilterButton.setOnClickListener(view1 -> updateFilter("name", exerciseSelectNameFilterButton));
        exerciseSelectMuscleFilterButton.setOnClickListener(view1 -> updateFilter("muscle", exerciseSelectMuscleFilterButton));
        exerciseSelectEquipmentFilterButton.setOnClickListener(view1 -> updateFilter("equipment", exerciseSelectEquipmentFilterButton));
        exerciseSelectDifficultyFilterButton.setOnClickListener(view1 -> updateFilter("difficulty", exerciseSelectDifficultyFilterButton));

        exerciseSelectSearchButton.setOnClickListener(view1 -> executor.execute(() -> {
            search = exerciseSelectEditText.getText().toString();
            try {
                Log.d("Filter", filter);
                Log.d("Search", search);
                String requestURL = "https://api.api-ninjas.com/v1/exercises?" + filter + "=" + search;
                url = new URL(requestURL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("accept", "application/json");
                connection.setRequestProperty("X-Api-Key", apiKey); // Set the API key header

                InputStream responseStream = connection.getInputStream();
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(responseStream);

                Log.d("API Response", root.toString());
            } catch (MalformedURLException e) {
                Log.e("API Error", "Malformed URL", e);
            } catch (IOException e) {
                Log.e("API Error", "IO Exception", e);
            }
        }));

        return view;
    }

    private void updateFilter(String newFilter, Button filterButton) {
        selectedFilterButton.setBackground(getResources().getDrawable(R.drawable.button_background));
        filterButton.setBackground(getResources().getDrawable(R.drawable.button_background_green));
        filter = newFilter;
        selectedFilterButton = filterButton;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
    }
}
