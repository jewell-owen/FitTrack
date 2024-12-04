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

public class LoggedWorkoutAdapter extends FirestoreAdapter<LoggedWorkoutAdapter.ViewHolder>{

    public interface OnExerciseSelectedListener {

        void onExerciseMoreInfo(DocumentSnapshot snapshot);

    }

    private LoggedWorkoutAdapter.OnExerciseSelectedListener mListener;

    public LoggedWorkoutAdapter(Query query, LoggedWorkoutAdapter.OnExerciseSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @NonNull
    @Override
    public LoggedWorkoutAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new LoggedWorkoutAdapter.ViewHolder(inflater.inflate(R.layout.card_logged_exercise, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LoggedWorkoutAdapter.ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameView;
        ImageButton deleteExerciseButton;
        ImageButton deleteSetButton;
        ImageButton addSetButton;
        ImageButton moreInfoExerciseButton;

        ConstraintLayout set1Layout;
        ConstraintLayout set2Layout;
        ConstraintLayout set3Layout;
        ConstraintLayout set4Layout;
        ConstraintLayout set5Layout;
        ConstraintLayout set6Layout;

        TextView weightTv1;
        TextView weightTv2;
        TextView weightTv3;
        TextView weightTv4;
        TextView weightTv5;
        TextView weightTv6;

        TextView repsTv1;
        TextView repsTv2;
        TextView repsTv3;
        TextView repsTv4;
        TextView repsTv5;
        TextView repsTv6;


        public ViewHolder(View itemView) {
            super(itemView);

            nameView = itemView.findViewById(R.id.active_exercise_card_exercise_name_tv);
            deleteExerciseButton = itemView.findViewById(R.id.active_exercise_card_delete_exercise_btn);
            deleteSetButton = itemView.findViewById(R.id.active_exercise_card_delete_set_btn);
            addSetButton = itemView.findViewById(R.id.active_exercise_card_add_set_btn);
            moreInfoExerciseButton = itemView.findViewById(R.id.active_exercise_card_more_info_btn);

            set1Layout = itemView.findViewById(R.id.active_exercise_card_set1);
            set2Layout = itemView.findViewById(R.id.active_exercise_card_set2);
            set3Layout = itemView.findViewById(R.id.active_exercise_card_set3);
            set4Layout = itemView.findViewById(R.id.active_exercise_card_set4);
            set5Layout = itemView.findViewById(R.id.active_exercise_card_set5);
            set6Layout = itemView.findViewById(R.id.active_exercise_card_set6);

            weightTv1 = itemView.findViewById(R.id.active_exercise_card_set1_weight_et);
            weightTv2 = itemView.findViewById(R.id.active_exercise_card_set2_weight_et);
            weightTv3 = itemView.findViewById(R.id.active_exercise_card_set3_weight_et);
            weightTv4 = itemView.findViewById(R.id.active_exercise_card_set4_weight_et);
            weightTv5 = itemView.findViewById(R.id.active_exercise_card_set5_weight_et);
            weightTv6 = itemView.findViewById(R.id.active_exercise_card_set6_weight_et);

            repsTv1 = itemView.findViewById(R.id.active_exercise_card_set1_reps_et);
            repsTv2 = itemView.findViewById(R.id.active_exercise_card_set2_reps_et);
            repsTv3 = itemView.findViewById(R.id.active_exercise_card_set3_reps_et);
            repsTv4 = itemView.findViewById(R.id.active_exercise_card_set4_reps_et);
            repsTv5 = itemView.findViewById(R.id.active_exercise_card_set5_reps_et);
            repsTv6 = itemView.findViewById(R.id.active_exercise_card_set6_reps_et);

        }

        public void bind(final DocumentSnapshot snapshot,
                         final LoggedWorkoutAdapter.OnExerciseSelectedListener listener) {

            LogWorkout logWorkout = snapshot.toObject(LogWorkout.class);
            Resources resources = itemView.getResources();


            nameView.setText(logWorkout.getName());

            String oneWeight = logWorkout.getOneWeight() + " lbs";
            String twoWeight = logWorkout.getTwoWeight()+ " lbs";
            String threeWeight = logWorkout.getThreeWeight()+ " lbs";
            String fourWeight = logWorkout.getFourWeight()+ " lbs";
            String fiveWeight = logWorkout.getFiveWeight()+ " lbs";
            String sixWeight = logWorkout.getSixWeight()+ " lbs";

            String oneReps = logWorkout.getOneReps() + " reps";
            String twoReps = logWorkout.getTwoReps() + " reps";
            String threeReps = logWorkout.getThreeReps() + " reps";
            String fourReps = logWorkout.getFourReps() + " reps";
            String fiveReps = logWorkout.getFiveReps() + " reps";
            String sixReps = logWorkout.getSixReps() + " reps";

            weightTv1.setText(oneWeight);
            weightTv2.setText(twoWeight);
            weightTv3.setText(threeWeight);
            weightTv4.setText(fourWeight);
            weightTv5.setText(fiveWeight);
            weightTv6.setText(sixWeight);
            repsTv1.setText(oneReps);
            repsTv2.setText(twoReps);
            repsTv3.setText(threeReps);
            repsTv4.setText(fourReps);
            repsTv5.setText(fiveReps);
            repsTv6.setText(sixReps);

            String sets = Integer.toString(logWorkout.getSets());

            if (sets.equals("1")){
                set1Layout.setVisibility(View.VISIBLE);
                set2Layout.setVisibility(View.INVISIBLE);
                set3Layout.setVisibility(View.INVISIBLE);
                set4Layout.setVisibility(View.INVISIBLE);
                set5Layout.setVisibility(View.INVISIBLE);
                set6Layout.setVisibility(View.INVISIBLE);
            }
            else if (sets.equals("2")){
                set1Layout.setVisibility(View.VISIBLE);
                set2Layout.setVisibility(View.VISIBLE);
                set3Layout.setVisibility(View.INVISIBLE);
                set4Layout.setVisibility(View.INVISIBLE);
                set5Layout.setVisibility(View.INVISIBLE);
                set6Layout.setVisibility(View.INVISIBLE);
            }
            else if (sets.equals("3")){
                set1Layout.setVisibility(View.VISIBLE);
                set2Layout.setVisibility(View.VISIBLE);
                set3Layout.setVisibility(View.VISIBLE);
                set4Layout.setVisibility(View.INVISIBLE);
                set5Layout.setVisibility(View.INVISIBLE);
                set6Layout.setVisibility(View.INVISIBLE);
            }
            else if (sets.equals("4")){
                set1Layout.setVisibility(View.VISIBLE);
                set2Layout.setVisibility(View.VISIBLE);
                set3Layout.setVisibility(View.VISIBLE);
                set4Layout.setVisibility(View.VISIBLE);
                set5Layout.setVisibility(View.INVISIBLE);
                set6Layout.setVisibility(View.INVISIBLE);
            }
            else if (sets.equals("5")){
                set1Layout.setVisibility(View.VISIBLE);
                set2Layout.setVisibility(View.VISIBLE);
                set3Layout.setVisibility(View.VISIBLE);
                set4Layout.setVisibility(View.VISIBLE);
                set5Layout.setVisibility(View.VISIBLE);
                set6Layout.setVisibility(View.INVISIBLE);
            }
            else if (sets.equals("6")){
                set1Layout.setVisibility(View.VISIBLE);
                set2Layout.setVisibility(View.VISIBLE);
                set3Layout.setVisibility(View.VISIBLE);
                set4Layout.setVisibility(View.VISIBLE);
                set5Layout.setVisibility(View.VISIBLE);
                set6Layout.setVisibility(View.VISIBLE);
            }

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
