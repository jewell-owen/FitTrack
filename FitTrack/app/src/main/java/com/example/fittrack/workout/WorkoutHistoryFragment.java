package com.example.fittrack.workout;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fittrack.R;
import com.example.fittrack.adapter.LogWorkoutAdapter;
import com.example.fittrack.adapter.LoggedWorkoutAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


/**
 * A fragment used to handle the functionality for viewing the information of a specific logged workout from history.
 */
public class WorkoutHistoryFragment extends Fragment implements View.OnClickListener, LoggedWorkoutAdapter.OnExerciseSelectedListener {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    private Query mQueryExercises;
    private LoggedWorkoutAdapter mAdapterExercises;


    private ImageButton workoutHistoryBackButton;
    private RecyclerView workoutHistoryExercisesRecyclerView;

    //id of this logged workout in loggedWorkouts collection
    private String id = "";

;

    /**
     * Required empty public constructor
     */
    public WorkoutHistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WorkoutFragment.
     */
    public static WorkoutHistoryFragment newInstance(String param1, String param2) {
        return new WorkoutHistoryFragment();
    }

    /**
     * Required onClick
     * @param view The view that was clicked
     */
    @Override
    public void onClick(View view) {
        int id = view.getId();
    }

    /**
     * Called when the view is started to start listening for FireStore updates
     */
    @Override
    public void onStart() {
        super.onStart();

        // Start listening for Firestore updates
        if (mAdapterExercises != null) {
            mAdapterExercises.startListening();
        }

    }

    /**
     * Called when the view is stopped to stop listening for FireStore updates
     */
    @Override
    public void onStop() {
        super.onStop();
        if (mAdapterExercises != null) {
            mAdapterExercises.stopListening();
        }
    }

    /**
     * Called when the view is created
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    /**
     * Called to have the fragment instantiate its user interface view and handles all other initialization
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return the view for the fragment's UI
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_workout_history, container, false);

        //Current workout UI items initialization
        workoutHistoryBackButton = view.findViewById(R.id.workout_history_cancel_btn);
        workoutHistoryExercisesRecyclerView = view.findViewById(R.id.active_workout_exercises_recycler);

        workoutHistoryExercisesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        workoutHistoryExercisesRecyclerView.setAdapter(mAdapterExercises);

        workoutHistoryBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //@To-Do implement logic to delete logic from firebase
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        if (getArguments() != null) {
            id = getArguments().getString("id");
            mQueryExercises = db.collection("users").document(user.getUid()).collection("loggedWorkouts").document(id).collection("exercises");
            initSavedWorkoutExercisesRecyclerView();
        }

        return view;
    }

    /**
     * Initializes the recycler view for the workout history exercises
     */
    private void initSavedWorkoutExercisesRecyclerView() {
        if (mQueryExercises == null) {
            Log.w("TAG", "No query, not initializing RecyclerView");
        }

        mAdapterExercises = new LoggedWorkoutAdapter(mQueryExercises, this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (workoutHistoryExercisesRecyclerView.getVisibility() == View.VISIBLE){
                    //Handle update
                }

            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
            }
        };

        //myWorkoutsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        workoutHistoryExercisesRecyclerView.setAdapter(mAdapterExercises);
    }

    /**
     * Called when a workout exercise is selected to go to fragment to view full information about the exercise
     * @param exercise DocumentSnapshot of the exercise that was selected
     */
    @Override
    public void onExerciseMoreInfo(DocumentSnapshot exercise) {
        Bundle bundle = new Bundle();
        bundle.putString("name", exercise.getString("name"));
        bundle.putString("type", exercise.getString("type"));
        bundle.putString("muscle", exercise.getString("muscle"));
        bundle.putString("equipment", exercise.getString("equipment"));
        bundle.putString("difficulty", exercise.getString("difficulty"));
        bundle.putString("instructions", exercise.getString("instructions"));
        ViewExerciseFragment viewExerciseFragment = new ViewExerciseFragment();
        viewExerciseFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFragment, viewExerciseFragment)
                .addToBackStack(null)
                .commit();
    }

}
