package com.example.fittrack.adapter;

import android.content.res.Resources;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fittrack.R;
import com.example.fittrack.model.LogWorkout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

/**
 * RecyclerView adapter for displaying a list of logged workouts.
 * This adapter retrieves data from a Firestore query and populates each item in the list.
 */
public class LogWorkoutNameAdapter extends FirestoreAdapter<LogWorkoutNameAdapter.ViewHolder> {

    /**
     * Interface to handle interactions with logged workout items.
     */
    public interface OnLoggedWorkoutSelectedListener {

        /**
         * Called when a logged workout is selected.
         *
         * @param snapshot The Firestore document snapshot of the selected workout.
         */
        void onGoToWorkout(DocumentSnapshot snapshot);
    }

        private LogWorkoutNameAdapter.OnLoggedWorkoutSelectedListener mListener;

        /**
         * Constructor for the LogWorkoutNameAdapter.
         *
         * @param query    The Firestore query to retrieve logged workout data.
         * @param listener The listener to handle interactions with workout items.
         */
        public LogWorkoutNameAdapter(Query query, LogWorkoutNameAdapter.OnLoggedWorkoutSelectedListener listener) {
            super(query);
            mListener = listener;
        }

        /**
         * Inflates the layout for a single logged workout item.
         *
         * @param parent   The parent ViewGroup.
         * @param viewType The type of view (not used here).
         * @return A ViewHolder instance containing the item layout.
         */
        @NonNull
        @Override
        public LogWorkoutNameAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new LogWorkoutNameAdapter.ViewHolder(inflater.inflate(R.layout.card_logged_workout, parent, false));
        }

        /**
         * Binds the data from the Firestore snapshot to the ViewHolder.
         *
         * @param holder   The ViewHolder to bind data to.
         * @param position The position of the item in the adapter.
         */
        @Override
        public void onBindViewHolder(@NonNull LogWorkoutNameAdapter.ViewHolder holder, int position) {
            holder.bind(getSnapshot(position), mListener);
        }

        /**
         * ViewHolder class to hold references to the views in each item layout.
         */
        static class ViewHolder extends RecyclerView.ViewHolder {

            TextView nameView;
            TextView dateView;
            ImageButton goToWorkoutButton;



            /**
             * Constructs a ViewHolder and initializes its views.
             *
             * @param itemView The root view of the item layout.
             */
            public ViewHolder(View itemView) {
                super(itemView);

                nameView = itemView.findViewById(R.id.logged_workout_card_workout_name_tv);
                dateView = itemView.findViewById(R.id.logged_workout_card_workout_date_tv);
                goToWorkoutButton = itemView.findViewById(R.id.planned_workout_card_select_workout_btn);


            }

            /**
             * Binds the Firestore document data to the views.
             *
             * @param snapshot The Firestore document snapshot containing the workout data.
             * @param listener The listener for item interactions.
             */
            public void bind(final DocumentSnapshot snapshot,
                             final LogWorkoutNameAdapter.OnLoggedWorkoutSelectedListener listener) {

                LogWorkout logWorkout = snapshot.toObject(LogWorkout.class);
                Resources resources = itemView.getResources();


                nameView.setText(logWorkout.getName());
                dateView.setText(logWorkout.getDate());



                goToWorkoutButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null) {
                            listener.onGoToWorkout(snapshot);
                        }
                    }
                });




            }

        }

    }


