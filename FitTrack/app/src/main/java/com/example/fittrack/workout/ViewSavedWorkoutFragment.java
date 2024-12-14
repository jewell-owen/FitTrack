package com.example.fittrack.workout;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fittrack.R;
import com.example.fittrack.adapter.ExerciseAdapter;
import com.example.fittrack.viewmodel.SavedWorkoutViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

/**
 * A fragment used to handle the functionality for viewing and editing a saved workout plan.
 */
public class ViewSavedWorkoutFragment extends Fragment implements View.OnClickListener,  ExerciseAdapter.OnExerciseSelectedListener{

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    private SavedWorkoutViewModel mViewModel;

    private Query mQueryExercises;
    private ExerciseAdapter mAdapterExercises;


    private EditText savedWorkoutNameEditText;
    private ImageButton savedWorkoutBackButton;
    private Button savedWorkoutAddExistingExerciseButton;
    private Button savedWorkoutAddCustomExerciseButton;
    private Button savedWorkoutSaveButton;
    private RecyclerView myWorkoutExercisesRecyclerView;

    private String workout = "";
    private String id = "";
    private boolean newWorkoutCreated = false;

    /**
     * Required empty public constructor
     */
    public ViewSavedWorkoutFragment() {
        // Required empty public constructor
    }

    /**
     * Auto generated static constructor
     * @param param1 parameter 1
     * @param param2 parameter 2
     * @return A new instance of fragment ViewSavedWorkoutFragment.
     */
    public static ViewSavedWorkoutFragment newInstance(String param1, String param2) {
        ViewSavedWorkoutFragment fragment = new ViewSavedWorkoutFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
     * Called when the view is created
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        View view = inflater.inflate(R.layout.fragment_view_saved_workout, container, false);

        savedWorkoutNameEditText = view.findViewById(R.id.saved_workout_name_et);
        savedWorkoutBackButton = view.findViewById(R.id.saved_workout_back_btn);
        savedWorkoutAddExistingExerciseButton = view.findViewById(R.id.saved_workout_add_existing_exercise_btn);
        savedWorkoutAddCustomExerciseButton = view.findViewById(R.id.saved_workout_add_custom_exercise_btn);
        savedWorkoutSaveButton = view.findViewById(R.id.workout_my_workout_save_btn);
        myWorkoutExercisesRecyclerView = view.findViewById(R.id.workout_saved_workout_exercises_recycler);

        myWorkoutExercisesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        myWorkoutExercisesRecyclerView.setAdapter(mAdapterExercises);

        //Get workout if it already exists
        if (getArguments() != null){
            workout = getArguments().getString("workout");
            id = getArguments().getString("id");

            Query query = null;
            mViewModel = new ViewModelProvider(this).get(SavedWorkoutViewModel.class);

            savedWorkoutNameEditText.setText(workout);
            query = db.collection("users").document(user.getUid()).collection("workouts").document(id).collection("exercises");

            mQueryExercises = query;
            initSavedWorkoutExercisesRecyclerView();
        }
        //Create a new workout
        else {
            CollectionReference workoutRef = db.collection("users")
                    .document(user.getUid())
                    .collection("workouts");
            if (!newWorkoutCreated){
                Map<String, Object> newWorkout = new HashMap<>();
                String newWorkoutName = "Unnamed";
                newWorkout.put("name", newWorkoutName);
                savedWorkoutNameEditText.setText(newWorkoutName);

                final String[] newWorkoutID = {""};
                db.collection("users").document(user.getUid()).collection("workouts")
                        .add(newWorkout)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                newWorkoutCreated = true;
                                newWorkoutID[0] = documentReference.getId();
                                Query queryNewWorkout = null;
                                queryNewWorkout = db.collection("users").document(user.getUid()).collection("workouts").document(newWorkoutID[0]).collection("exercises");
                                mQueryExercises = queryNewWorkout;
                                id = newWorkoutID[0];
                                initSavedWorkoutExercisesRecyclerView();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
            }
        }

        // Set up the back button click listener to pop fragment off back stack
        savedWorkoutBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }

        });

        // Go to API exercise selection fragment
        savedWorkoutAddExistingExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectLibraryExerciseFragment exerciseSelect = new SelectLibraryExerciseFragment();
                Bundle args = new Bundle();
                args.putString("id", id);
                args.putString("workoutType", "savedWorkout");
                exerciseSelect.setArguments(args);
                getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, exerciseSelect)
                        .addToBackStack(null)
                    .commit();
            }
        });

        // Go to custom exercise creation fragment
        savedWorkoutAddCustomExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectCustomExerciseFragment customExercise = new SelectCustomExerciseFragment();
                Bundle args = new Bundle();
                args.putString("id", id);
                args.putString("workoutType", "savedWorkout");
                customExercise.setArguments(args);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, customExercise)
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Update the workout data in Firestore
        savedWorkoutSaveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Get the workout name from the EditText
                String newWorkoutName = savedWorkoutNameEditText.getText().toString().trim();

                // Reference to the workout document in Firestore
                DocumentReference workoutRef = db.collection("users")
                        .document(user.getUid())
                        .collection("workouts")
                        .document(id);

                // Create a map with the updated name
                Map<String, Object> updates = new HashMap<>();
                updates.put("name", newWorkoutName);

                // Update the "name" field in Firestore
                workoutRef.update(updates)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Close the fragment/activity upon successful update
                                getActivity().getSupportFragmentManager().popBackStack();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Log or handle the failure
                            }
                        });
            }
        });


        return view;
    }

    /**
     * Initializes the RecyclerView for the saved workout exercises
     */
    private void initSavedWorkoutExercisesRecyclerView() {
        if (mQueryExercises == null) {
            Log.w("TAG", "No query, not initializing RecyclerView");
        }

        mAdapterExercises = new ExerciseAdapter(mQueryExercises, this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (myWorkoutExercisesRecyclerView.getVisibility() == View.VISIBLE){
                    //Handle update
                }

            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
            }
        };

        //myWorkoutsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myWorkoutExercisesRecyclerView.setAdapter(mAdapterExercises);
    }

    /**
     * Called when an exercise is deleted from the workout
     * @param exercise DocumentSnapshot of the exercise to be deleted
     */
    @Override
    public void onExerciseDeleted(DocumentSnapshot exercise) {
        //In this case this method is called when delete button is clicked not the exercise itself
        // Reference to the workout document in Firestore
        DocumentReference workoutRef = db.collection("users")
                .document(user.getUid())
                .collection("workouts")
                .document(id)
                .collection("exercises")
                .document(exercise.getId());

        // Delete the workout document
        workoutRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

    /**
     * Called when an exercise is clicked to go to fragment to view more information
     * @param exercise DocumentSnapshot of the exercise to be viewed
     */
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
