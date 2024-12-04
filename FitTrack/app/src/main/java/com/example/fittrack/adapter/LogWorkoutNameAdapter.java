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

public class LogWorkoutNameAdapter extends FirestoreAdapter<LogWorkoutNameAdapter.ViewHolder> {

        public interface OnLoggedWorkoutSelectedListener {

            void onGoToWorkout(DocumentSnapshot snapshot);

        }

        private LogWorkoutNameAdapter.OnLoggedWorkoutSelectedListener mListener;

        public LogWorkoutNameAdapter(Query query, LogWorkoutNameAdapter.OnLoggedWorkoutSelectedListener listener) {
            super(query);
            mListener = listener;
        }

        @NonNull
        @Override
        public LogWorkoutNameAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new LogWorkoutNameAdapter.ViewHolder(inflater.inflate(R.layout.card_logged_workout, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull LogWorkoutNameAdapter.ViewHolder holder, int position) {
            holder.bind(getSnapshot(position), mListener);
        }

        static class ViewHolder extends RecyclerView.ViewHolder {

            TextView nameView;
            TextView dateView;
            ImageButton goToWorkoutButton;




            public ViewHolder(View itemView) {
                super(itemView);

                nameView = itemView.findViewById(R.id.logged_workout_card_workout_name_tv);
                dateView = itemView.findViewById(R.id.logged_workout_card_workout_date_tv);
                goToWorkoutButton = itemView.findViewById(R.id.planned_workout_card_select_workout_btn);


            }

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


