package com.example.fittrack.workout;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.fittrack.R;
import com.example.fittrack.adapter.SavedWorkoutAdapter;
import com.example.fittrack.viewmodel.WorkoutViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

/**
 * A fragment used to handle all of the functionality for viewing all user created saved workout plans
 */
public class ListSavedWorkoutsFragment extends Fragment implements View.OnClickListener, SavedWorkoutAdapter.OnWorkoutSelectedListener{

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    /**
     * FireStore data base reference
     */
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * Firebase authentication reference
     */
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    /**
     * Firebase user reference
     */
    FirebaseUser user = mAuth.getCurrentUser();

    private WorkoutViewModel mViewModel;
    private Query mQueryWorkouts;
    private SavedWorkoutAdapter mAdapterWorkouts;

    //My workouts list UI items
    private ImageButton savedWorkoutsBackButton;
    private ImageButton addNewSavedWorkoutButton;
    private RecyclerView savedWorkoutsRecyclerView;

    private String mParam1;
    private String mParam2;

    /**
     * Required empty public constructor
     */
    public ListSavedWorkoutsFragment() {
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
    public static ListSavedWorkoutsFragment newInstance(String param1, String param2) {
        ListSavedWorkoutsFragment fragment = new ListSavedWorkoutsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Required onClick
     * @param view view for fragment
     */
    @Override
    public void onClick(View view) {
        int id = view.getId();
    }

    /**
     * Called when the fragment is first created
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
     * onStart method to start listening for Firestore updates
     */
    @Override
    public void onStart() {
        super.onStart();

        // Start listening for Firestore updates
        if (mAdapterWorkouts != null) {
            mAdapterWorkouts.startListening();
        }
    }

    /**
     * onStop method to stop listening for Firestore updates
     */
    @Override
    public void onStop() {
        super.onStop();
        if (mAdapterWorkouts != null) {
            mAdapterWorkouts.stopListening();
        }

    }

    /**
     * Creates view for the fragment and handles all initialization
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        View view = inflater.inflate(R.layout.fragment_list_saved_workouts, container, false);

        //My workouts list UI items initialization
        addNewSavedWorkoutButton = view.findViewById(R.id.list_saved_workouts_new_workout_btn);
        savedWorkoutsBackButton = view.findViewById(R.id.list_logged_workouts_back_btn);
        savedWorkoutsRecyclerView = view.findViewById(R.id.list_logged_workouts_recycler);

        savedWorkoutsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        savedWorkoutsRecyclerView.setAdapter(mAdapterWorkouts);



        mViewModel = new ViewModelProvider(this).get(WorkoutViewModel.class);
        Query query = db.collection("users").document(user.getUid()).collection("workouts");
        mQueryWorkouts = query;
        initSavedWorkoutsRecyclerView();


        //Pop fragment off back stack when back button is clicked
        savedWorkoutsBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        //Add new workout button to go to screen to create new saved workout plan
        addNewSavedWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewSavedWorkoutFragment savedWorkout = new ViewSavedWorkoutFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, savedWorkout)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

    /**
     * Initializes the RecyclerView for the list of saved workouts
     */
    private void initSavedWorkoutsRecyclerView() {
        if (mQueryWorkouts == null) {
            Log.w("TAG", "No query, not initializing RecyclerView");
        }

        mAdapterWorkouts = new SavedWorkoutAdapter(mQueryWorkouts, this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (savedWorkoutsRecyclerView.getVisibility() == View.VISIBLE){
                    //Handle update
                }

            }

            @Override
            protected void onError(FirebaseFirestoreException e) {

            }
        };

        //myWorkoutsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        savedWorkoutsRecyclerView.setAdapter(mAdapterWorkouts);
    }

    /**
     * Called when a workout is selected from the list of saved workouts and goes to specific workout
     * @param workout DocumentSnapshot of the workout selected
     */
    @Override
    public void onWorkoutSelected(DocumentSnapshot workout) {
        ViewSavedWorkoutFragment savedWorkout = new ViewSavedWorkoutFragment();
        Bundle args = new Bundle();
        args.putString("id", workout.getId());
        args.putString("workout", workout.get("name").toString());
        savedWorkout.setArguments(args);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFragment, savedWorkout)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Called when a workout is deleted from the list of saved workouts
     * @param workout DocumentSnapshot of the workout selected for deletion
     */
    public void onWorkoutDeleted(DocumentSnapshot workout) {
        DocumentReference workoutRef = db.collection("users")
                .document(user.getUid())
                .collection("workouts")
                .document(workout.getId());

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

}