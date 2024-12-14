package com.example.fittrack.adapter;

import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.fittrack.model.SavedWorkout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

/**
 * RecyclerView adapter for logging workouts functionality. Includes exercise cards with sets and reps.
 */
public class LogWorkoutAdapter extends FirestoreAdapter<LogWorkoutAdapter.ViewHolder> {

    public interface OnExerciseSelectedListener {

        void onExerciseMoreInfo(DocumentSnapshot snapshot);

        void onExerciseDeleted(DocumentSnapshot snapshot);

        void onSetAdded(DocumentSnapshot snapshot, int sets);

        void onSetDeleted(DocumentSnapshot snapshot, int sets);

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
            weightEt1.setText(logWorkout.getOneWeight());
            weightEt2.setText(logWorkout.getTwoWeight());
            weightEt3.setText(logWorkout.getThreeWeight());
            weightEt4.setText(logWorkout.getFourWeight());
            weightEt5.setText(logWorkout.getFiveWeight());
            weightEt6.setText(logWorkout.getSixWeight());
            repsEt1.setText(logWorkout.getOneReps());
            repsEt2.setText(logWorkout.getTwoReps());
            repsEt3.setText(logWorkout.getThreeReps());
            repsEt4.setText(logWorkout.getFourReps());
            repsEt5.setText(logWorkout.getFiveReps());
            repsEt6.setText(logWorkout.getSixReps());

            String sets = Integer.toString(logWorkout.getSets());
            int intSets = logWorkout.getSets();

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
            else if (sets.equals("6")) {
                set1Layout.setVisibility(View.VISIBLE);
                set2Layout.setVisibility(View.VISIBLE);
                set3Layout.setVisibility(View.VISIBLE);
                set4Layout.setVisibility(View.VISIBLE);
                set5Layout.setVisibility(View.VISIBLE);
                set6Layout.setVisibility(View.VISIBLE);
            }

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
                int classSets = logWorkout.getSets();
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        if (intSets < 6){
                            classSets++;
                            if ((classSets) == 1){
                                set1Layout.setVisibility(View.VISIBLE);
                                set2Layout.setVisibility(View.INVISIBLE);
                                set3Layout.setVisibility(View.INVISIBLE);
                                set4Layout.setVisibility(View.INVISIBLE);
                                set5Layout.setVisibility(View.INVISIBLE);
                                set6Layout.setVisibility(View.INVISIBLE);
                            }
                            else if ((classSets) == 2){
                                set1Layout.setVisibility(View.VISIBLE);
                                set2Layout.setVisibility(View.VISIBLE);
                                set3Layout.setVisibility(View.INVISIBLE);
                                set4Layout.setVisibility(View.INVISIBLE);
                                set5Layout.setVisibility(View.INVISIBLE);
                                set6Layout.setVisibility(View.INVISIBLE);
                            }
                            else if ((classSets) == 3){
                                set1Layout.setVisibility(View.VISIBLE);
                                set2Layout.setVisibility(View.VISIBLE);
                                set3Layout.setVisibility(View.VISIBLE);
                                set4Layout.setVisibility(View.INVISIBLE);
                                set5Layout.setVisibility(View.INVISIBLE);
                                set6Layout.setVisibility(View.INVISIBLE);
                            }
                            else if ((classSets) == 4){
                                set1Layout.setVisibility(View.VISIBLE);
                                set2Layout.setVisibility(View.VISIBLE);
                                set3Layout.setVisibility(View.VISIBLE);
                                set4Layout.setVisibility(View.VISIBLE);
                                set5Layout.setVisibility(View.INVISIBLE);
                                set6Layout.setVisibility(View.INVISIBLE);
                            }
                            else if ((classSets) == 5){
                                set1Layout.setVisibility(View.VISIBLE);
                                set2Layout.setVisibility(View.VISIBLE);
                                set3Layout.setVisibility(View.VISIBLE);
                                set4Layout.setVisibility(View.VISIBLE);
                                set5Layout.setVisibility(View.VISIBLE);
                                set6Layout.setVisibility(View.INVISIBLE);
                            }
                            else if ((classSets ) == 6){
                                set1Layout.setVisibility(View.VISIBLE);
                                set2Layout.setVisibility(View.VISIBLE);
                                set3Layout.setVisibility(View.VISIBLE);
                                set4Layout.setVisibility(View.VISIBLE);
                                set5Layout.setVisibility(View.VISIBLE);
                                set6Layout.setVisibility(View.VISIBLE);
                            }
                            listener.onSetAdded(snapshot, classSets);
                            Log.d("TAG", "sets: " + classSets);

                        }

                    }

                }
            });

            deleteSetButton.setOnClickListener(new View.OnClickListener() {
                int classSets = logWorkout.getSets();
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        if (intSets > 1) {
                            classSets--;
                            if ((classSets) == 1) {
                                set1Layout.setVisibility(View.VISIBLE);
                                set2Layout.setVisibility(View.INVISIBLE);
                                set3Layout.setVisibility(View.INVISIBLE);
                                set4Layout.setVisibility(View.INVISIBLE);
                                set5Layout.setVisibility(View.INVISIBLE);
                                set6Layout.setVisibility(View.INVISIBLE);
                            } else if ((classSets) == 2) {
                                set1Layout.setVisibility(View.VISIBLE);
                                set2Layout.setVisibility(View.VISIBLE);
                                set3Layout.setVisibility(View.INVISIBLE);
                                set4Layout.setVisibility(View.INVISIBLE);
                                set5Layout.setVisibility(View.INVISIBLE);
                                set6Layout.setVisibility(View.INVISIBLE);
                            } else if ((classSets) == 3) {
                                set1Layout.setVisibility(View.VISIBLE);
                                set2Layout.setVisibility(View.VISIBLE);
                                set3Layout.setVisibility(View.VISIBLE);
                                set4Layout.setVisibility(View.INVISIBLE);
                                set5Layout.setVisibility(View.INVISIBLE);
                                set6Layout.setVisibility(View.INVISIBLE);
                            } else if ((classSets) == 4) {
                                set1Layout.setVisibility(View.VISIBLE);
                                set2Layout.setVisibility(View.VISIBLE);
                                set3Layout.setVisibility(View.VISIBLE);
                                set4Layout.setVisibility(View.VISIBLE);
                                set5Layout.setVisibility(View.INVISIBLE);
                                set6Layout.setVisibility(View.INVISIBLE);
                            } else if ((classSets) == 5) {
                                set1Layout.setVisibility(View.VISIBLE);
                                set2Layout.setVisibility(View.VISIBLE);
                                set3Layout.setVisibility(View.VISIBLE);
                                set4Layout.setVisibility(View.VISIBLE);
                                set5Layout.setVisibility(View.VISIBLE);
                                set6Layout.setVisibility(View.INVISIBLE);
                            } else if ((classSets) == 6) {
                                set1Layout.setVisibility(View.VISIBLE);
                                set2Layout.setVisibility(View.VISIBLE);
                                set3Layout.setVisibility(View.VISIBLE);
                                set4Layout.setVisibility(View.VISIBLE);
                                set5Layout.setVisibility(View.VISIBLE);
                                set6Layout.setVisibility(View.VISIBLE);
                            }
                            listener.onSetDeleted(snapshot, classSets);
                            Log.d("TAG", "sets: " + classSets);

                        }

                    }

                }
                });

            weightEt1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if (listener != null) {
                        listener.onWeightChanged(snapshot, "one", weightEt1.getText().toString());
                    }
                    return false;
                }
            });

            weightEt2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if (listener != null) {
                        listener.onWeightChanged(snapshot, "two", weightEt2.getText().toString());
                    }
                    return false;
                }
            });

            weightEt3.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if (listener != null) {
                        listener.onWeightChanged(snapshot, "three", weightEt3.getText().toString());
                    }
                    return false;
                }
            });

            weightEt4.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if (listener != null) {
                        listener.onWeightChanged(snapshot, "four", weightEt4.getText().toString());
                    }
                    return false;
                }
            });

            weightEt5.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if (listener != null) {
                        listener.onWeightChanged(snapshot, "five", weightEt5.getText().toString());
                    }
                    return false;
                }
            });

            weightEt6.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if (listener != null) {
                        listener.onWeightChanged(snapshot, "six", weightEt6.getText().toString());
                    }
                    return false;
                }
            });

            repsEt1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if (listener != null) {
                        listener.onRepsChanged(snapshot, "one", repsEt1.getText().toString());
                    }
                    return false;
                }
            });

            repsEt2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if (listener != null) {
                        listener.onRepsChanged(snapshot, "two", repsEt2.getText().toString());
                    }
                    return false;
                }
            });

            repsEt3.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if (listener != null) {
                        listener.onRepsChanged(snapshot, "three", repsEt3.getText().toString());
                    }
                    return false;
                }
            });

            repsEt4.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if (listener != null) {
                        listener.onRepsChanged(snapshot, "four", repsEt4.getText().toString());
                    }
                    return false;
                }
            });

            repsEt5.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if (listener != null) {
                        listener.onRepsChanged(snapshot, "five", repsEt5.getText().toString());
                    }
                    return false;
                }
            });

            repsEt6.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if (listener != null) {
                        listener.onRepsChanged(snapshot, "six", repsEt6.getText().toString());
                    }
                    return false;
                }
            });


        }

    }

}
