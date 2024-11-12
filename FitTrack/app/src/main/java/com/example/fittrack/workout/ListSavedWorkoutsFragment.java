package com.example.fittrack.workout;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.fittrack.R;
import com.example.fittrack.adapter.SavedWorkoutAdapter;
import com.example.fittrack.viewmodel.WorkoutViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkoutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListSavedWorkoutsFragment extends Fragment implements View.OnClickListener, SavedWorkoutAdapter.OnWorkoutSelectedListener{

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
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

    @Override
    public void onClick(View view) {
        int id = view.getId();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // Start listening for Firestore updates
        if (mAdapterWorkouts != null) {
            mAdapterWorkouts.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapterWorkouts != null) {
            mAdapterWorkouts.stopListening();
        }

    }

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
        savedWorkoutsBackButton = view.findViewById(R.id.list_saved_workouts_back_btn);
        savedWorkoutsRecyclerView = view.findViewById(R.id.list_saved_workouts_recycler);

        savedWorkoutsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        savedWorkoutsRecyclerView.setAdapter(mAdapterWorkouts);



        mViewModel = new ViewModelProvider(this).get(WorkoutViewModel.class);
        Query query = db.collection("users").document(user.getUid()).collection("workouts");
        mQueryWorkouts = query;
        initSavedWorkoutsRecyclerView();



        savedWorkoutsBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        addNewSavedWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }


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

}