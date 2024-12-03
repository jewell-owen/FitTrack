package com.example.fittrack.adapter;

import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.example.fittrack.model.SavedWorkout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

public class LogWorkoutAdapter extends FirestoreAdapter<LogWorkoutAdapter.ViewHolder> {

    public interface OnExerciseSelectedListener {

        void onExerciseMoreInfo(DocumentSnapshot snapshot);

        void onExerciseDeleted(DocumentSnapshot snapshot);

        void onSetAdded(DocumentSnapshot snapshot);

        void onSetDeleted(DocumentSnapshot snapshot);

        void onWeightChanged(DocumentSnapshot snapshot, String set, String weight);

        void onRepsChanged(DocumentSnapshot snapshot, String set, String reps);
    }

    private LogWorkoutAdapter.OnExerciseSelectedListener mListener;

    public LogWorkoutAdapter(Query query, LogWorkoutAdapter.OnExerciseSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @NonNull
    @Override
    public LogWorkoutAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new LogWorkoutAdapter.ViewHolder(inflater.inflate(R.layout.card_active_exercise, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LogWorkoutAdapter.ViewHolder holder, int position) {
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

         EditText weightEt1;
         EditText weightEt2;
         EditText weightEt3;
         EditText weightEt4;
         EditText weightEt5;
         EditText weightEt6;

         EditText repsEt1;
         EditText repsEt2;
         EditText repsEt3;
         EditText repsEt4;
         EditText repsEt5;
         EditText repsEt6;


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

            weightEt1 = itemView.findViewById(R.id.active_exercise_card_set1_weight_et);
            weightEt2 = itemView.findViewById(R.id.active_exercise_card_set2_weight_et);
            weightEt3 = itemView.findViewById(R.id.active_exercise_card_set3_weight_et);
            weightEt4 = itemView.findViewById(R.id.active_exercise_card_set4_weight_et);
            weightEt5 = itemView.findViewById(R.id.active_exercise_card_set5_weight_et);
            weightEt6 = itemView.findViewById(R.id.active_exercise_card_set6_weight_et);

            repsEt1 = itemView.findViewById(R.id.active_exercise_card_set1_reps_et);
            repsEt2 = itemView.findViewById(R.id.active_exercise_card_set2_reps_et);
            repsEt3 = itemView.findViewById(R.id.active_exercise_card_set3_reps_et);
            repsEt4 = itemView.findViewById(R.id.active_exercise_card_set4_reps_et);
            repsEt5 = itemView.findViewById(R.id.active_exercise_card_set5_reps_et);
            repsEt6 = itemView.findViewById(R.id.active_exercise_card_set6_reps_et);

        }

        public void bind(final DocumentSnapshot snapshot,
                         final LogWorkoutAdapter.OnExerciseSelectedListener listener) {

            LogWorkout logWorkout = snapshot.toObject(LogWorkout.class);
            Resources resources = itemView.getResources();


            nameView.setText(logWorkout.getName());

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

            addSetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onSetAdded(snapshot);
                    }
                    if (set2Layout.getVisibility() == View.GONE){
                        set2Layout.setVisibility(View.VISIBLE);
                    }
                    else if (set3Layout.getVisibility() == View.GONE){
                        set3Layout.setVisibility(View.VISIBLE);
                    }
                    else if (set4Layout.getVisibility() == View.GONE){
                        set4Layout.setVisibility(View.VISIBLE);
                    }
                    else if (set5Layout.getVisibility() == View.GONE){
                        set5Layout.setVisibility(View.VISIBLE);
                    }
                    else if (set6Layout.getVisibility() == View.GONE){
                        set6Layout.setVisibility(View.VISIBLE);
                    }
                }
            });

            deleteSetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onSetDeleted(snapshot);
                    }
                    if (set6Layout.getVisibility() == View.VISIBLE){
                        set6Layout.setVisibility(View.GONE);
                    }
                    else if (set5Layout.getVisibility() == View.VISIBLE){
                        set5Layout.setVisibility(View.GONE);
                    }
                    else if (set4Layout.getVisibility() == View.VISIBLE){
                        set4Layout.setVisibility(View.GONE);
                    }
                    else if (set3Layout.getVisibility() == View.VISIBLE){
                        set3Layout.setVisibility(View.GONE);
                    }
                    else if (set2Layout.getVisibility() == View.VISIBLE){
                        set2Layout.setVisibility(View.GONE);
                    }
                }
            });

            weightEt1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void afterTextChanged(Editable editable) {
                    if (listener != null) {
                        listener.onWeightChanged(snapshot,"1",editable.toString());
                    }
                }
            });

            weightEt2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void afterTextChanged(Editable editable) {
                    if (listener != null) {
                        listener.onWeightChanged(snapshot,"2",editable.toString());
                    }
                }
            });

            weightEt3.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void afterTextChanged(Editable editable) {
                    if (listener != null) {
                        listener.onWeightChanged(snapshot,"3",editable.toString());
                    }
                }
            });

            weightEt4.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void afterTextChanged(Editable editable) {
                    if (listener != null) {
                        listener.onWeightChanged(snapshot,"4",editable.toString());
                    }
                }
            });

            weightEt5.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void afterTextChanged(Editable editable) {
                    if (listener != null) {
                        listener.onWeightChanged(snapshot,"5",editable.toString());
                    }
                }
            });

            weightEt6.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void afterTextChanged(Editable editable) {
                    if (listener != null) {
                        listener.onWeightChanged(snapshot,"6",editable.toString());
                    }
                }
            });

            repsEt1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void afterTextChanged(Editable editable) {
                    if (listener != null) {
                        listener.onRepsChanged(snapshot,"1",editable.toString());
                    }
                }
            });

            repsEt2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void afterTextChanged(Editable editable) {
                    if (listener != null) {
                        listener.onRepsChanged(snapshot,"2",editable.toString());
                    }
                }
            });

            repsEt3.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void afterTextChanged(Editable editable) {
                    if (listener != null) {
                        listener.onRepsChanged(snapshot,"3",editable.toString());
                    }
                }
            });

            repsEt4.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void afterTextChanged(Editable editable) {
                    if (listener != null) {
                        listener.onRepsChanged(snapshot,"4",editable.toString());
                    }
                }
            });

            repsEt5.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void afterTextChanged(Editable editable) {
                    if (listener != null) {
                        listener.onRepsChanged(snapshot,"5",editable.toString());
                    }
                }
            });

            repsEt6.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void afterTextChanged(Editable editable) {
                    if (listener != null) {
                        listener.onRepsChanged(snapshot,"6",editable.toString());
                    }
                }
            });


        }

    }

}
