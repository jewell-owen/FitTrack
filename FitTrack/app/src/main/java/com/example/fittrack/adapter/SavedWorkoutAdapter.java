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
import com.example.fittrack.model.SavedWorkout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

/**
 * RecyclerView adapter for a list of saved workouts. Cards include names and buttons to go to workout for viewing or editing
 */
public class SavedWorkoutAdapter extends FirestoreAdapter<SavedWorkoutAdapter.ViewHolder> {

    public interface OnWorkoutSelectedListener {

        void onWorkoutSelected(DocumentSnapshot restaurant);

        void onWorkoutDeleted(DocumentSnapshot snapshot);
    }

    private OnWorkoutSelectedListener mListener;

    public SavedWorkoutAdapter(Query query, OnWorkoutSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.card_saved_workout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameView;
        ImageButton deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);

            nameView = itemView.findViewById(R.id.logged_workout_card_workout_name_tv);
            deleteButton = itemView.findViewById(R.id.planned_workout_card_select_workout_btn);

        }

        public void bind(final DocumentSnapshot snapshot,
                         final OnWorkoutSelectedListener listener) {

            SavedWorkout savedWorkout = snapshot.toObject(SavedWorkout.class);
            Resources resources = itemView.getResources();


            nameView.setText(savedWorkout.getName());

            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onWorkoutSelected(snapshot);
                    }
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onWorkoutDeleted(snapshot);
                    }
                }
            });
        }

    }
}
