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
 * Recycler view adapter for saved favorite exercise in user profile
 */
public class FavoriteExerciseAdapter extends FirestoreAdapter<FavoriteExerciseAdapter.ViewHolder>{

    public interface OnExerciseSelectedListener {

        void onExerciseMoreInfo(DocumentSnapshot exercise);
    }

    private FavoriteExerciseAdapter.OnExerciseSelectedListener mListener;

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


    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameView;

        ImageButton moreInfoExerciseButton;

        public ViewHolder(View itemView) {
            super(itemView);

            nameView = itemView.findViewById(R.id.favorite_exercise_card_exercise_name_tv);
            moreInfoExerciseButton = itemView.findViewById(R.id.favorite_exercise_card_go_to_btn);


        }

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
