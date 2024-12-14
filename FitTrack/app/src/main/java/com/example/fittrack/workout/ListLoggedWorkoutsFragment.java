package com.example.fittrack.workout;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fittrack.R;
import com.example.fittrack.adapter.LogWorkoutNameAdapter;
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
 * A fragment used to handle all of the functionality for viewing workout history
 */
public class ListLoggedWorkoutsFragment extends Fragment implements View.OnClickListener, LogWorkoutNameAdapter.OnLoggedWorkoutSelectedListener {
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

    private Query mQueryWorkouts;
    private LogWorkoutNameAdapter mAdapterWorkouts;

    //My workouts list UI items
    private ImageButton loggedWorkoutsBackButton;
    private RecyclerView loggedWorkoutsRecyclerView;

    private String mParam1;
    private String mParam2;

    /**
     * Required empty public constructor
     */
    public ListLoggedWorkoutsFragment() {
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
    public static ListLoggedWorkoutsFragment newInstance(String param1, String param2) {
        ListLoggedWorkoutsFragment fragment = new ListLoggedWorkoutsFragment();
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

        View view = inflater.inflate(R.layout.fragment_list_logged_workouts, container, false);

        //My workouts list UI items initialization
        loggedWorkoutsBackButton = view.findViewById(R.id.list_logged_workouts_back_btn);
        loggedWorkoutsRecyclerView = view.findViewById(R.id.list_logged_workouts_recycler);

        loggedWorkoutsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loggedWorkoutsRecyclerView.setAdapter(mAdapterWorkouts);

        Query query = db.collection("users").document(user.getUid()).collection("loggedWorkouts");
        mQueryWorkouts = query;
        initSavedWorkoutsRecyclerView();


        //Pop fragment off back stack when back button is clicked
        loggedWorkoutsBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }

    /**
     * Initializes the RecyclerView for the list of logged workouts
     */
    private void initSavedWorkoutsRecyclerView() {
        if (mQueryWorkouts == null) {
            Log.w("TAG", "No query, not initializing RecyclerView");
        }

        mAdapterWorkouts = new LogWorkoutNameAdapter(mQueryWorkouts, this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (loggedWorkoutsRecyclerView.getVisibility() == View.VISIBLE){
                    //Handle update
                }

            }

            @Override
            protected void onError(FirebaseFirestoreException e) {

            }
        };

        //myWorkoutsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        loggedWorkoutsRecyclerView.setAdapter(mAdapterWorkouts);
    }

    /**
     * Called when a workout is selected from the list of logged workouts and goes to specific workout
     * @param workout DocumentSnapshot of the workout selected
     */
    @Override
    public void onGoToWorkout(DocumentSnapshot workout) {
        WorkoutHistoryFragment workoutHistory = new WorkoutHistoryFragment();
        Bundle args = new Bundle();
        args.putString("id", workout.getId());
        args.putString("workout", workout.get("name").toString());
        workoutHistory.setArguments(args);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFragment, workoutHistory)
                .addToBackStack(null)
                .commit();
    }


}
