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
 * RecyclerView adapter for displaying saved favorite exercises in a user's profile.
 * Extends {@link FirestoreAdapter} to manage Firestore query snapshots.
 */
public class FavoriteExerciseAdapter extends FirestoreAdapter<FavoriteExerciseAdapter.ViewHolder>{

    /**
     * Listener interface for handling user interactions with favorite exercise items.
     */
    public interface OnExerciseSelectedListener {

        /**
         * Called when more information about a favorite exercise is requested.
         *
         * @param exercise The {@link DocumentSnapshot} representing the exercise.
         */
        void onExerciseMoreInfo(DocumentSnapshot exercise);
    }

    private FavoriteExerciseAdapter.OnExerciseSelectedListener mListener;

    /**
     * Constructs a {@code FavoriteExerciseAdapter}.
     *
     * @param query    The Firestore query used to retrieve favorite exercise data.
     * @param listener The listener for user interactions with favorite exercise items.
     */
    public FavoriteExerciseAdapter(Query query, FavoriteExerciseAdapter.OnExerciseSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @NonNull
    @Override
    public FavoriteExerciseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new FavoriteExerciseAdapter.ViewHolder(inflater.inflate(R.layout.card_favorite_exercise, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteExerciseAdapter.ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }


    /**
     * ViewHolder for displaying an individual favorite exercise card.
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        /** TextView for the exercise name. */
        TextView nameView;

        /** Button for accessing more information about the exercise. */
        ImageButton moreInfoExerciseButton;

        /**
         * Constructs a {@code ViewHolder}.
         *
         * @param itemView The view representing a favorite exercise card.
         */
        public ViewHolder(View itemView) {
            super(itemView);

            nameView = itemView.findViewById(R.id.favorite_exercise_card_exercise_name_tv);
            moreInfoExerciseButton = itemView.findViewById(R.id.favorite_exercise_card_go_to_btn);


        }

        /**
         * Binds a favorite exercise snapshot to the view.
         *
         * @param snapshot The Firestore {@link DocumentSnapshot} containing exercise data.
         * @param listener The listener for user interactions with this item.
         */
        public void bind(final DocumentSnapshot snapshot,
                         final FavoriteExerciseAdapter.OnExerciseSelectedListener listener) {

            Exercise exercise = snapshot.toObject(Exercise.class);
            Resources resources = itemView.getResources();

            nameView.setText(exercise.getName());

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
