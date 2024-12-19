package com.example.fittrack.adapter;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fittrack.R;
import com.example.fittrack.model.Exercise;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

/**
 * RecyclerView adapter for displaying exercise cards resulting from an API search.
 * Extends {@link FirestoreAdapter} to manage Firestore query snapshots.
 */
public class ExerciseAdapter extends FirestoreAdapter<ExerciseAdapter.ViewHolder> {

    /**
     * Listener interface for handling user interactions with exercise items.
     */
    public interface OnExerciseSelectedListener {

        /**
         * Called when an exercise is deleted.
         *
         * @param exercise The {@link DocumentSnapshot} representing the exercise.
         */
        void onExerciseDeleted(DocumentSnapshot exercise);

        /**
         * Called when more information about an exercise is requested.
         *
         * @param exercise The {@link DocumentSnapshot} representing the exercise.
         */
        void onExerciseMoreInfo(DocumentSnapshot exercise);
    }

    private OnExerciseSelectedListener mListener;

    /**
     * Constructs an {@code ExerciseAdapter}.
     *
     * @param query    The Firestore query used to retrieve exercise data.
     * @param listener The listener for user interactions with exercise items.
     */
    public ExerciseAdapter(Query query, OnExerciseSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.card_exercise, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    /**
     * ViewHolder for displaying an individual exercise card.
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        /** TextView for the exercise name. */
        TextView nameView;

        /** TextView for the exercise's targeted muscle group. */
        TextView muscleView;

        /** TextView for the exercise's required equipment. */
        TextView equipmentView;

        /** TextView for the type of exercise. */
        TextView typeView;

        /** TextView for the difficulty level of the exercise. */
        TextView difficultyView;

        /** Button for deleting the exercise. */
        ImageButton deleteExerciseButton;

        /** Button for requesting more information about the exercise. */
        ImageButton moreInfoExerciseButton;


        /**
         * Constructs a {@code ViewHolder}.
         *
         * @param itemView The view representing an exercise card.
         */
        public ViewHolder(View itemView) {
            super(itemView);

            nameView = itemView.findViewById(R.id.active_exercise_card_exercise_name_tv);
            muscleView = itemView.findViewById(R.id.exercise_card_exercise_muscle_tv);
            equipmentView = itemView.findViewById(R.id.exercise_card_exercise_equipment_tv);
            typeView = itemView.findViewById(R.id.exercise_card_exercise_type_tv);
            difficultyView = itemView.findViewById(R.id.exercise_card_exercise_difficulty_tv);
            deleteExerciseButton = itemView.findViewById(R.id.active_exercise_card_delete_exercise_btn);
            moreInfoExerciseButton = itemView.findViewById(R.id.active_exercise_card_more_info_btn);


        }

        /**
         * Binds an exercise snapshot to the view.
         *
         * @param snapshot The Firestore {@link DocumentSnapshot} containing exercise data.
         * @param listener The listener for user interactions with this item.
         */
        public void bind(final DocumentSnapshot snapshot,
                         final OnExerciseSelectedListener listener) {

            Exercise exercise = snapshot.toObject(Exercise.class);
            Resources resources = itemView.getResources();

            String muscle = "Muscle: " + exercise.getMuscle();
            String equipment = "Equipment: " + exercise.getEquipment();
            String type = "Type: " + exercise.getType();
            String difficulty = "Difficulty: " + exercise.getDifficulty();

            nameView.setText(exercise.getName());
            muscleView.setText(muscle);
            equipmentView.setText(equipment);
            typeView.setText(type);
            difficultyView.setText(difficulty);

            // Click listener
            deleteExerciseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onExerciseDeleted(snapshot);
                    }
                }
            });

            moreInfoExerciseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onExerciseMoreInfo(snapshot);
                    }
                }
            });
        }

    }
}
