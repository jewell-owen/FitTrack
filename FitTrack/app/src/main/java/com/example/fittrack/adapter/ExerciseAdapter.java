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

public class ExerciseAdapter extends FirestoreAdapter<ExerciseAdapter.ViewHolder> {

    public interface OnExerciseSelectedListener {

        void onExerciseSelected(DocumentSnapshot restaurant);

    }

    private OnExerciseSelectedListener mListener;

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


    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameView;
        TextView muscleView;
        TextView equipmentView;
        TextView typeView;
        TextView difficultyView;

        ImageButton deleteExerciseButton;

        public ViewHolder(View itemView) {
            super(itemView);

            nameView = itemView.findViewById(R.id.exercise_card_exercise_name_tv);
            muscleView = itemView.findViewById(R.id.exercise_card_exercise_muscle_tv);
            equipmentView = itemView.findViewById(R.id.exercise_card_exercise_equipment_tv);
            typeView = itemView.findViewById(R.id.exercise_card_exercise_type_tv);
            difficultyView = itemView.findViewById(R.id.exercise_card_exercise_difficulty_tv);
            deleteExerciseButton = itemView.findViewById(R.id.exercise_card_delete_exercise_btn);

        }

        public void bind(final DocumentSnapshot snapshot,
                         final OnExerciseSelectedListener listener) {

            Exercise exercise = snapshot.toObject(Exercise.class);
            Resources resources = itemView.getResources();


            nameView.setText(exercise.getName());

            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onExerciseSelected(snapshot);
                    }
                }
            });
        }

    }
}