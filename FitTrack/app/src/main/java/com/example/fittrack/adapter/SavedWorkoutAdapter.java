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
 * RecyclerView adapter for displaying a list of saved workouts.
 *
 * <p>This adapter handles displaying workout names in a card layout, along with
 * buttons for performing actions like viewing/editing or deleting a workout.
 * It connects to a Firestore query to retrieve the list of saved workouts.</p>
 */
public class SavedWorkoutAdapter extends FirestoreAdapter<SavedWorkoutAdapter.ViewHolder> {

    /**
     * Listener interface for handling user interactions with the saved workouts.
     */
    public interface OnWorkoutSelectedListener {

        /**
         * Called when a workout is selected for viewing or editing.
         *
         * @param snapshot The Firestore document snapshot representing the selected workout.
         */
        void onWorkoutSelected(DocumentSnapshot snapshot);

        /**
         * Called when a workout is selected for deletion.
         *
         * @param snapshot The Firestore document snapshot representing the workout to delete.
         */
        void onWorkoutDeleted(DocumentSnapshot snapshot);
    }


    private OnWorkoutSelectedListener mListener;

    /**
     * Constructor for the SavedWorkoutAdapter.
     *
     * @param query    The Firestore query used to fetch the list of saved workouts.
     * @param listener A listener to handle user interactions with workout cards.
     */
    public SavedWorkoutAdapter(Query query, OnWorkoutSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    /**
     * Inflates the card layout for saved workouts and creates a ViewHolder.
     *
     * @param parent   The parent ViewGroup.
     * @param viewType The type of the view (not used here, as all items share the same type).
     * @return A new ViewHolder instance for the saved workout card.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.card_saved_workout, parent, false));
    }

    /**
     * Binds a saved workout to the ViewHolder at the specified position.
     *
     * @param holder   The ViewHolder to bind the data to.
     * @param position The position of the item in the RecyclerView.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    /**
     * ViewHolder class for saved workout items. Holds references to UI elements and binds data.
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameView;
        ImageButton deleteButton;

        /**
         * Constructor for the ViewHolder.
         *
         * @param itemView The root view of the card layout.
         */
        public ViewHolder(View itemView) {
            super(itemView);

            nameView = itemView.findViewById(R.id.logged_workout_card_workout_name_tv);
            deleteButton = itemView.findViewById(R.id.planned_workout_card_select_workout_btn);

        }

        /**
         * Binds a SavedWorkout object to the ViewHolder's UI elements.
         *
         * @param snapshot The Firestore document snapshot containing the workout data.
         * @param listener A listener to handle interactions with the card's buttons.
         */
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
