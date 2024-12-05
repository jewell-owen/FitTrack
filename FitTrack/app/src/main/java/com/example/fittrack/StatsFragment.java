package com.example.fittrack;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

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
import com.google.firebase.firestore.QuerySnapshot;

public class StatsFragment extends Fragment implements View.OnClickListener, LogWorkoutNameAdapter.OnLoggedWorkoutSelectedListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    private Query mQueryWorkouts;
    private LogWorkoutNameAdapter mAdapterWorkouts;

    //My workouts list UI items
    private RecyclerView numWorkoutsRecyclerView;
    private TextView numWorkoutsTextView;

    private String mParam1;
    private String mParam2;

    private int numWorkouts;



    public StatsFragment() {
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
    public static StatsFragment newInstance(String param1, String param2) {
        StatsFragment fragment = new StatsFragment();
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

        View view = inflater.inflate(R.layout.fragment_stats, container, false);

        //My workouts list UI items initialization
        numWorkoutsRecyclerView = view.findViewById(R.id.stats_num_workouts_recycler);
        numWorkoutsTextView = view.findViewById(R.id.stats_num_workouts_tv);


        numWorkoutsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        numWorkoutsRecyclerView.setAdapter(mAdapterWorkouts);

        db.collection("users").document(user.getUid()).collection("loggedWorkouts")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            int documentCount = querySnapshot.size();
                            System.out.println("Number of documents: " + documentCount);
                            numWorkouts = documentCount;
                            numWorkoutsTextView.setText(String.valueOf(numWorkouts));
                        } else {
                            System.out.println("No documents found.");
                        }

                    } else {
                        System.err.println("Error getting documents: " + task.getException());
                    }
                });

        Query query = db.collection("users").document(user.getUid()).collection("loggedWorkouts");
        mQueryWorkouts = query;
        initSavedWorkoutsRecyclerView();



        return view;
    }


    private void initSavedWorkoutsRecyclerView() {
        if (mQueryWorkouts == null) {
            Log.w("TAG", "No query, not initializing RecyclerView");
        }

        mAdapterWorkouts = new LogWorkoutNameAdapter(mQueryWorkouts, this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (numWorkoutsRecyclerView.getVisibility() == View.INVISIBLE){
                    //Handle update
                }
                db.collection("users").document(user.getUid()).collection("loggedWorkouts")
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                QuerySnapshot querySnapshot = task.getResult();
                                if (querySnapshot != null) {
                                    int documentCount = querySnapshot.size();
                                    System.out.println("Number of documents: " + documentCount);
                                    numWorkouts = documentCount;
                                    numWorkoutsTextView.setText(String.valueOf(numWorkouts));
                                } else {
                                    System.out.println("No documents found.");
                                }

                            } else {
                                System.err.println("Error getting documents: " + task.getException());
                            }
                        });

            }

            @Override
            protected void onError(FirebaseFirestoreException e) {

            }
        };

        //myWorkoutsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        numWorkoutsRecyclerView.setAdapter(mAdapterWorkouts);
    }

    @Override
    public void onGoToWorkout(DocumentSnapshot workout) {

    }


}
