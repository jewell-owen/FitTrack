package com.example.fittrack.workout;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.fittrack.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * A fragment used to handle the functionality for viewing the full information for an exercise.
 */
public class ViewExerciseFragment extends Fragment implements View.OnClickListener {

    private ImageButton viewExerciseBackButton;
    private TextView viewExerciseNameTv;
    private ScrollView viewExerciseScrollView;
    private LinearLayout viewExerciseLinearLayout;

    private String name = "";
    private String muscle = "";
    private String equipment = "";
    private String type = "";
    private String difficulty = "";
    private String instructions = "";

    /**
     * Called to have the fragment instantiate its user interface view and handles all other initialization
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return the view for the fragment's UI
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_view_exercise, container, false);

        viewExerciseBackButton = view.findViewById(R.id.view_exercise_back_btn);
        viewExerciseNameTv = view.findViewById(R.id.view_exercise_title_tv);
        viewExerciseScrollView = view.findViewById(R.id.view_exercise_scroll_view);
        viewExerciseLinearLayout = view.findViewById(R.id.view_exercise_card_ll);

        if (getArguments() != null) {
            name = getArguments().getString("name");
            muscle = "Muscle: " +  getArguments().getString("muscle");
            equipment = "Equipment: " +   getArguments().getString("equipment");
            type = "Type: " +  getArguments().getString("type");
            difficulty = "Difficulty: " +   getArguments().getString("difficulty");
            instructions = "Instructions: " +  getArguments().getString("instructions");
        }

        // Set up the back button click listener to pop fragment off back stack
        viewExerciseBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        viewExerciseNameTv.setText(name);

        CardView cardView = createCardView(type, muscle, equipment, difficulty, instructions);
        viewExerciseLinearLayout.addView(cardView);

        return view;
    }

    /**
     * Creates a new CardView with the provided values
     * @param type The type of exercise
     * @param muscle The muscle targeted
     * @param equipment The equipment used
     * @param difficulty The difficulty of the exercise
     * @param instructions The instructions for the exercise
     * @return The created CardView
     */
    private CardView createCardView(String type, String muscle, String equipment, String difficulty, String instructions) {
        // Inflate the CardView layout
        LayoutInflater inflater = LayoutInflater.from(getContext());
        CardView cardView = (CardView) inflater.inflate(R.layout.card_full_exercise, viewExerciseLinearLayout, false);

        // Set values to the views in the CardView
        TextView tvMuscle = cardView.findViewById(R.id.full_exercise_card_exercise_muscle_tv);
        TextView tvEquipment = cardView.findViewById(R.id.full_exercise_card_exercise_equipment_tv);
        TextView tvType = cardView.findViewById(R.id.full_exercise_card_exercise_type_tv);
        TextView tvDifficulty = cardView.findViewById(R.id.full_exercise_card_exercise_difficulty_tv);
        TextView tvInstructions = cardView.findViewById(R.id.full_exercise_card_exercise_instructions_tv);

        tvMuscle.setText(muscle);
        tvEquipment.setText(equipment);
        tvType.setText(type);
        tvDifficulty.setText(difficulty);
        tvInstructions.setText(instructions);

        return cardView;
    }

    /**
     * Required onClick
     * @param view The view that was clicked
     */
        @Override
    public void onClick(View view) {

    }
}
