package com.example.fittrack.workout;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.example.fittrack.BuildConfig;
import com.example.fittrack.R;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import org.json.JSONException;

public class SelectLibraryExerciseFragment extends Fragment implements View.OnClickListener {

    private ImageButton exerciseSelectBackButton;
    private ImageButton exerciseSelectSearchButton;
    private Button exerciseSelectNameFilterButton;
    private Button exerciseSelectMuscleFilterButton;
    private Button exerciseSelectEquipmentFilterButton;
    private Button exerciseSelectDifficultyFilterButton;
    private EditText exerciseSelectEditText;
    private LinearLayout cardLinearLayout;
    private ScrollView exerciseSelectScrollView;

    private URL url = null;

    private String workoutId = "";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    private String search = "";
    private String filter = "";
    private Button selectedFilterButton;

    private final Executor executor = Executors.newSingleThreadExecutor();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_select_library_exercise, container, false);

        exerciseSelectBackButton = view.findViewById(R.id.exercise_select_back_btn);
        exerciseSelectSearchButton = view.findViewById(R.id.exercise_select_search_btn);
        exerciseSelectNameFilterButton = view.findViewById(R.id.exercise_select_name_filter_btn);
        exerciseSelectMuscleFilterButton = view.findViewById(R.id.exercise_select_muscle_filter_btn);
        exerciseSelectEquipmentFilterButton = view.findViewById(R.id.exercise_select_equipment_filter_btn);
        exerciseSelectDifficultyFilterButton = view.findViewById(R.id.exercise_select_difficulty_filter_btn);
        exerciseSelectEditText = view.findViewById(R.id.exercise_select_et);
        exerciseSelectScrollView = view.findViewById(R.id.exercise_select_scroll_view);
        cardLinearLayout = view.findViewById(R.id.exercise_select_card_ll);

        if (getArguments() != null) {
            workoutId = getArguments().getString("id");
        }

        filter = "name";
        selectedFilterButton = exerciseSelectNameFilterButton;
        exerciseSelectNameFilterButton.setBackground(getResources().getDrawable(R.drawable.button_background_green));

        exerciseSelectBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        String apiKey = BuildConfig.API_KEY_EXERCISE;

        exerciseSelectNameFilterButton.setOnClickListener(view1 -> updateFilter("name", exerciseSelectNameFilterButton));
        exerciseSelectMuscleFilterButton.setOnClickListener(view1 -> updateFilter("muscle", exerciseSelectMuscleFilterButton));
        exerciseSelectEquipmentFilterButton.setOnClickListener(view1 -> updateFilter("equipment", exerciseSelectEquipmentFilterButton));
        exerciseSelectDifficultyFilterButton.setOnClickListener(view1 -> updateFilter("difficulty", exerciseSelectDifficultyFilterButton));

        exerciseSelectSearchButton.setOnClickListener(view1 -> executor.execute(() -> {
            search = exerciseSelectEditText.getText().toString();
            search = replaceSpaces(search);
            try {
                Log.d("Filter", filter);
                Log.d("Search", search);
                String requestURL = "https://api.api-ninjas.com/v1/exercises?" + filter + "=" + search;
                url = new URL(requestURL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("accept", "application/json");
                connection.setRequestProperty("X-Api-Key", apiKey);

                InputStream responseStream = connection.getInputStream();
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(responseStream);

                // Collect card views in a list on the background thread
                List<CardView> cardViews = new ArrayList<>();
                if (root.isArray()) {
                    for (JsonNode exercise : root) {
                        String name = exercise.has("name") ? exercise.get("name").asText() : "";
                        String type = exercise.has("type") ? exercise.get("type").asText() : "";
                        String muscle = exercise.has("muscle") ? exercise.get("muscle").asText() : "";
                        String equipment = exercise.has("equipment") ? exercise.get("equipment").asText() : "";
                        String difficulty = exercise.has("difficulty") ? exercise.get("difficulty").asText() : "";
                        String instructions = exercise.has("instructions") ? exercise.get("instructions").asText() : "";

                        // Log each exercise being processed
                        Log.d("Exercise Processed", "Name: " + name);

                        // Create a CardView for each exercise and add it to the list
                        try {
                            CardView cardView = createCardView(name, type, muscle, equipment, difficulty, instructions);
                            cardViews.add(cardView);
                        } catch (JSONException e) {
                            Log.e("CardView Error", "Error creating card view for exercise: " + name, e);
                        }
                    }
                }

                // Run on UI thread to add all cards to the layout
                getActivity().runOnUiThread(() -> {
                    cardLinearLayout.removeAllViews(); // Clear previous views
                    for (CardView cardView : cardViews) {
                        cardLinearLayout.addView(cardView);
                    }
                });

                Log.d("API Response", root.toString());
            } catch (MalformedURLException e) {
                Log.e("API Error", "Malformed URL", e);
            } catch (IOException e) {
                Log.e("API Error", "IO Exception", e);
            }
        }));

        return view;
    }

    private CardView createCardView(String name, String type, String muscle, String equipment, String difficulty, String instructions) throws JSONException {
        // Inflate the CardView layout
        LayoutInflater inflater = LayoutInflater.from(getContext());
        CardView cardView = (CardView) inflater.inflate(R.layout.card_exercise_select, cardLinearLayout, false);

        // Set values to the views in the CardView
        TextView tvName = cardView.findViewById(R.id.exercise_select_card_exercise_name_tv);
        ImageButton btnSeeMoreInfo = cardView.findViewById(R.id.exercise_select_card_more_info_btn);
        ImageButton btnSelectExercise = cardView.findViewById(R.id.exercise_select_card_select_exercise_btn);


        tvName.setText(name);

        btnSeeMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Handle More info
            }
        });

        btnSelectExercise.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Map<String, Object> exercise = new HashMap<>();
                exercise.put("name", name);
                exercise.put("muscle", muscle);
                exercise.put("equipment", equipment);
                exercise.put("type", type);
                exercise.put("difficulty", difficulty);
                exercise.put("instructions", instructions);
                Log.d("TAG", "workoutId: " + workoutId);
                db.collection("users").document(user.getUid()).collection("workouts").document(workoutId).collection("exercises").add(exercise)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                getActivity().getSupportFragmentManager().popBackStack();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
            }
        });

        return cardView;
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

    public String replaceSpaces(String input) {
        if (input == null) {
            return null; // Handle null input
        }
        return input.replace(" ", "_");
    }
}
