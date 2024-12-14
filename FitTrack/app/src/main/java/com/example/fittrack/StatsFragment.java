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

import java.util.ArrayList;

/**
 * A fragment used to show user statistics.
 */
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
    private TextView numSetsTextView;
    private TextView weightTextView;

    private String mParam1;
    private String mParam2;

    private int numWorkouts;
    private int numSets;
    private int weight;

    ArrayList<Integer> weightArray = new ArrayList<Integer>();



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
        numSetsTextView = view.findViewById(R.id.stats_num_sets_tv);
        weightTextView = view.findViewById(R.id.stats_weight_lifted_tv);


        numWorkoutsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        numWorkoutsRecyclerView.setAdapter(mAdapterWorkouts);


        Query query = db.collection("users").document(user.getUid()).collection("loggedWorkouts");
        mQueryWorkouts = query;
        initSavedWorkoutsRecyclerView();



        return view;
    }


    private void initSavedWorkoutsRecyclerView() {
        if (mQueryWorkouts == null) {
            Log.w("TAG", "No query, not initializing RecyclerView");
        }
        int tempArrayWeight = 0;
        for (int i = 0; i < weightArray.size(); i++) {
            tempArrayWeight += weightArray.get(i);
        }
        String setWeight = String.valueOf(tempArrayWeight);
        weightTextView.setText(setWeight);

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
                                    numWorkouts = documentCount;
                                    numWorkoutsTextView.setText(String.valueOf(numWorkouts));

                                    // Initialize numSets
                                    numSets = 0;
                                    weight = 0;
                                    for (DocumentSnapshot document : querySnapshot) {
                                        db.collection("users").document(user.getUid()).collection("loggedWorkouts")
                                                .document(document.getId()).collection("exercises").get()
                                                .addOnCompleteListener(task2 -> {
                                                    if (task2.isSuccessful()) {
                                                        QuerySnapshot querySnapshot2 = task2.getResult();
                                                        if (querySnapshot2 != null) {
                                                            for (DocumentSnapshot document2 : querySnapshot2) {
                                                                if (document2.get("sets") != null) {
                                                                    int tempSets = Math.toIntExact((long) document2.get("sets"));
                                                                    numSets += tempSets;
                                                                    int tempWeight = 0;
                                                                    for (int i = 1; i <= tempSets; i++) {
                                                                        switch (i) {
                                                                            case 1:
                                                                                if (document2.get("oneWeight") != null) {
                                                                                    if (document2.get("oneReps") != null){
                                                                                        tempWeight = Integer.parseInt(document2.get("oneWeight").toString()) * Integer.parseInt(document2.get("oneReps").toString());
                                                                                        Log.d("Weight", String.valueOf(tempWeight));
                                                                                        weightArray.add(tempWeight);
                                                                                        weight += tempWeight;
                                                                                    }
                                                                                }
                                                                                break;
                                                                            case 2:
                                                                                if (document2.get("twoWeight") != null) {
                                                                                    if (document2.get("twoReps") != null){
                                                                                        tempWeight = Integer.parseInt(document2.get("twoWeight").toString()) * Integer.parseInt(document2.get("twoReps").toString());
                                                                                        Log.d("Weight", String.valueOf(tempWeight));
                                                                                        weightArray.add(tempWeight);
                                                                                        weight += tempWeight;
                                                                                    }
                                                                                }
                                                                                break;
                                                                            case 3:
                                                                                if (document2.get("threeWeight") != null) {
                                                                                    if (document2.get("threeReps") != null){
                                                                                        tempWeight = Integer.parseInt(document2.get("threeWeight").toString()) * Integer.parseInt(document2.get("threeReps").toString());
                                                                                        Log.d("Weight", String.valueOf(tempWeight));
                                                                                        weightArray.add(tempWeight);
                                                                                        weight += tempWeight;
                                                                                    }
                                                                                }
                                                                                break;
                                                                            case 4:
                                                                                if (document2.get("fourWeight") != null) {
                                                                                    if (document2.get("fourReps") != null){
                                                                                        tempWeight = Integer.parseInt(document2.get("fourWeight").toString()) * Integer.parseInt(document2.get("fourReps").toString());
                                                                                        Log.d("Weight", String.valueOf(tempWeight));
                                                                                        weightArray.add(tempWeight);
                                                                                        weight += tempWeight;
                                                                                    }
                                                                                }
                                                                                break;
                                                                            case 5:
                                                                                if (document2.get("fiveWeight") != null) {
                                                                                    if (document2.get("fiveReps") != null){
                                                                                        tempWeight = Integer.parseInt(document2.get("fiveWeight").toString()) * Integer.parseInt(document2.get("fiveReps").toString());
                                                                                        Log.d("Weight", String.valueOf(tempWeight));
                                                                                        weightArray.add(tempWeight);
                                                                                        weight += tempWeight;
                                                                                    }
                                                                                }
                                                                                break;
                                                                            case 6:
                                                                                if (document2.get("sixWeight") != null) {
                                                                                    if (document2.get("sixReps") != null){
                                                                                        tempWeight = Integer.parseInt(document2.get("sixWeight").toString()) * Integer.parseInt(document2.get("sixReps").toString());
                                                                                        Log.d("Weight", String.valueOf(tempWeight));
                                                                                        weightArray.add(tempWeight);
                                                                                        weight += tempWeight;
                                                                                    }
                                                                                }
                                                                                break;
                                                                        }

                                                                    }
                                                                }
                                                            }
                                                            // Update the TextView after each sub-collection query
                                                            numSetsTextView.setText(String.valueOf(numSets));
                                                            weightTextView.setText(String.valueOf(weight));
                                                        }

                                                    } else {
                                                        Log.e("Firestore", "Error fetching exercises", task2.getException());
                                                    }
                                                });
                                    }
                                } else {
                                    Log.w("Firestore", "No logged workouts found.");
                                }
                            } else {
                                Log.e("Firestore", "Error fetching logged workouts", task.getException());
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